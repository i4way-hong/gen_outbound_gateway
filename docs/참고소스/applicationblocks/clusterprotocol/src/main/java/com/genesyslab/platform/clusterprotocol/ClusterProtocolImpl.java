// ===============================================================================
//  Genesys Platform SDK Application Blocks
// ===============================================================================
//
//  Any authorized distribution of any copy of this code (including any related
//  documentation) must reproduce the following restrictions, disclaimer and copyright
//  notice:
//
//  The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
//  (even as a part of another name), endorse and/or promote products derived from
//  this code without prior written permission from Genesys Telecommunications
//  Laboratories, Inc.
//
//  The use, copy, and/or distribution of this code is subject to the terms of the Genesys
//  Developer License Agreement.  This code shall not be used, copied, and/or
//  distributed under any other license agreement.
//
//  THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
//  ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
//  DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
//  WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
//  NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
//  NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
//  EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
//  CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
//  NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
//  Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.clusterprotocol;

import com.genesyslab.platform.clusterprotocol.lb.ClusterProtocolLoadBalancer;
import com.genesyslab.platform.clusterprotocol.lb.DefaultClusterProtocolLoadBalancerFactory;

import com.genesyslab.platform.standby.WSConfig;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.events.WSOpenedEvent;
import com.genesyslab.platform.standby.events.WSDisconnectedEvent;
import com.genesyslab.platform.standby.events.WSTriedUnsuccessfullyEvent;

import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.ClientChannel;
import com.genesyslab.platform.commons.protocol.RequestFuture;
import com.genesyslab.platform.commons.protocol.AbstractChannel;
import com.genesyslab.platform.commons.protocol.ReferenceBuilder;
import com.genesyslab.platform.commons.protocol.IntReferenceBuilder;
import com.genesyslab.platform.commons.protocol.ProtocolDescription;
import com.genesyslab.platform.commons.protocol.ChannelOpenedEvent;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;

import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.commons.protocol.ProtocolTimeoutException;
import com.genesyslab.platform.commons.protocol.ChannelNotOpenedException;
import com.genesyslab.platform.commons.protocol.ChannelClosedOnSendException;
import com.genesyslab.platform.commons.protocol.ChannelClosedOnRequestException;

import com.genesyslab.platform.commons.connection.configuration.ConnectionConfiguration;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.threading.CompletionHandler;
import com.genesyslab.platform.commons.threading.SingleThreadInvoker;
import com.genesyslab.platform.commons.timer.TimerAction;
import com.genesyslab.platform.commons.timer.TimerActionTicket;
import com.genesyslab.platform.commons.timer.TimerFactory;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Abstract implementation base of Cluster Protocol interface.<br/>
 * It's a common functionality for all specific Cluster Protocols.
 *
 * @param <P> the base client protocol type.
 * @param <B> the base client protocol builder type.
 * @see com.genesyslab.platform.clusterprotocol.esp.EspClusterProtocol EspClusterProtocol
 * @see com.genesyslab.platform.clusterprotocol.ucs.UcsClusterProtocol UcsClusterProtocol
 * @see com.genesyslab.platform.clusterprotocol.email.EspEmailClusterProtocol EspEmailClusterProtocol
 * @see com.genesyslab.platform.clusterprotocol.chat.FlexChatClusterProtocol FlexChatClusterProtocol
 */
public abstract class ClusterProtocolImpl
                <P extends ClientChannel,
                 B extends ProtocolBuilder<P, B>>
        extends AbstractClusterChannel<P>
        implements ClusterProtocol {

    private final ProtocolBuilder<P, B> protocolBuilder;

    private volatile ChannelState          channelState       = ChannelState.Closed;
    private final    Lock                  channelStateLock   = new ReentrantLock(true);
    private final    Condition             channelStateChange = channelStateLock.newCondition();

    private final Map<String, ProtocolNodeRec> clusterProtocols =
            new LinkedHashMap<String, ProtocolNodeRec>();

    private final Set<ProtocolNodeRec> protocolsOpened =
            new HashSet<ProtocolNodeRec>();
    private final Set<ProtocolNodeRec> protocolsOpening =
            new HashSet<ProtocolNodeRec>();
    private final List<ProtocolNodeRec> protocolsRemoving =
            new LinkedList<ProtocolNodeRec>();

    protected final ClusterProtocolPolicy       protocolPolicy;
    protected final ClusterProtocolLoadBalancer loadBalancer;

    private final ReferenceBuilder refBuilder = new IntReferenceBuilder();
    private final int protocolId;
    private volatile SingleThreadInvoker intInvoker = null;

    private final static ILogger log = Log.getLogger(ClusterProtocolImpl.class);


    protected ClusterProtocolImpl(
            final ProtocolBuilder<P, B>       protocolBuilder,
            final ClusterProtocolPolicy       protocolPolicy,
            final ClusterProtocolLoadBalancer loadBalancer) {
        if (protocolBuilder == null) {
            throw new IllegalArgumentException("ProtocolBuilder is null");
        }
        this.protocolId = AbstractChannel.generateChannelId();
        if (protocolPolicy == null) {
            this.protocolPolicy = new DefaultClusterProtocolPolicy();
        } else {
            this.protocolPolicy = protocolPolicy;
        }
        if (loadBalancer == null) {
            this.loadBalancer = DefaultClusterProtocolLoadBalancerFactory
                    .getFactory().getLoadBalancer();
        } else {
            this.loadBalancer = loadBalancer;
        }

        this.protocolBuilder = protocolBuilder
                .withRefBuilder(refBuilder);

        final ProtocolDescription protocolDescription = protocolBuilder.description();
        if (protocolDescription == null) {
            throw new IllegalArgumentException("ProtocolDescription is null");
        }
        this.endpoint = new Endpoint(protocolDescription.toString() + "-Cluster",
                new PropertyConfiguration());
    }

    @Override
    public ClusterProtocolPolicy policy() {
        return protocolPolicy;
    }


    @Override
    public ProtocolDescription getProtocolDescription() {
        return protocolBuilder.description();
    }


    @Override
    public int getProtocolId() {
        return protocolId;
    }


    // this method is called from 'open' methods inside of synchronized block
    private void resetIntInvoker() {
        if (intInvoker != null) {
            intInvoker.release();
        }
        intInvoker = null;
    }

    // this method is called from 'open' methods inside of synchronized block
    private AsyncInvoker getIntInvoker() {
        if (intInvoker == null) {
            intInvoker = new SingleThreadInvoker(getProtocolDescription().toString());
        }
        return intInvoker;
    }

    // this method is called inside of synchronized block
    private void releaseIntInvoker() {
        if (intInvoker != null) {
            intInvoker.release();
        }
    }


    @Override
    public ReferenceBuilder getReferenceBuilder() {
        return refBuilder;
    }

    @Override
    public void setTimeout(final long timeout) {
        channelStateLock.lock();
        try {
            super.setTimeout(timeout);
            final long tmt = getTimeout(); // - relay on parent initialization logic
            for (final ProtocolNodeRec item: clusterProtocols.values()) {
                item.warmstandby().getChannel().setTimeout(tmt);
            }
        } finally {
            channelStateLock.unlock();
        }
    }


    protected void validateEndpoints(
            final Iterable<Endpoint> endpoints) {
        throwNull(endpoints, "endpoints");
        final HashSet<String> names = new HashSet<String>();
        for (final Endpoint endpoint: endpoints) {
            throwNull(endpoint, "endpoint");
            final String name = endpoint.getName();
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Endpoint does not contain name");
            }
            if (names.contains(name)) {
                throw new IllegalArgumentException(
                        "Given Endpoint's set contains duplicated names (" + name + ")");
            }
            names.add(name);
        }
    }

    protected void validateWSConfigs(
            final Iterable<WSConfig> nodes) {
        throwNull(nodes, "nodes");
        final HashSet<String> names = new HashSet<String>();
        for (final WSConfig node: nodes) {
            throwNull(node, "node");
            final String name = node.getName();
            final List<Endpoint> eps = node.getEndpoints();
            if (eps == null || eps.isEmpty()) {
                throw new IllegalArgumentException("WSConfig does not contain Endpoints");
            }
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("WSConfig does not contain name");
            }
            if (names.contains(name)) {
                throw new IllegalArgumentException(
                        "Given WSConfig's set contains duplicated names (" + name + ")");
            }
            names.add(name);
        }
    }


    @Override
    public ChannelState getState() {
        return channelState;
    }


    @Override
    public <A> void openAsync(
            final CompletionHandler<ChannelOpenedEvent, ? super A> handler,
            final A attachment)
                    throws ProtocolException {
        channelStateLock.lock();
        try {
            throwNotClosed();
            if (clusterProtocols.isEmpty()) {
                throw new IllegalStateException("No cluster Endpoint's initialized");
            }
            setState(ChannelState.Opening, null);
            final int num = clusterProtocols.size();
            final AsyncOpenHelper<A> openHelper =
                    new AsyncOpenHelper<A>(num, handler, attachment, getTimeout());
            resetIntInvoker();
            for (final ProtocolNodeRec item: clusterProtocols.values()) {
                protocolsOpening.add(item);
                item.warmstandby().getChannel().setInvoker(getIntInvoker());
                item.warmstandby().openAsync(openHelper, attachment);
            }
        } finally {
            channelStateLock.unlock();
        }
    }


    /**
     * Initiates connections to the initialized set of cluster nodes.<br/>
     * Cluster Protocol will become <code>Opened</code> when at least one node is connected.
     *
     * @param timeout the timeout to wait for <code>Opened</code> state.
     * @throws IllegalStateException if protocol is not <code>Closed</code>,
     *     or it has no set nodes configurations or Endpoint's.
     * @throws ProtocolTimeoutException if cluster protocol got no notification about
     *     node connection in the given timeout frame. Note: it does not fail the open request,
     *     protocol is <code>Opening</code> and WarmStandby services continue to run re-connections.
     */
    @Override
    public void open(final long timeout)
            throws RegistrationException, ProtocolException,
                    IllegalStateException, InterruptedException {
        final long tmStart = System.currentTimeMillis();
        final long tmFinish = tmStart + ((timeout >= 0) ? timeout : DEFAULT_TIMEOUT);
        AsyncOpenHelper<Void> openHelper;

        channelStateLock.lock();
        try {
            throwNotClosed();
            if (clusterProtocols.isEmpty()) {
                throw new IllegalStateException("No cluster Endpoint's initialized");
            }
            setState(ChannelState.Opening, null);
            openHelper = new AsyncOpenHelper<Void>(
                    clusterProtocols.size(), null, null, timeout);
            resetIntInvoker();
            for (final ProtocolNodeRec item: clusterProtocols.values()) {
                protocolsOpening.add(item);
                item.warmstandby().getChannel().setInvoker(getIntInvoker());
                item.warmstandby().openAsync(openHelper, null);
            }
        } finally {
            channelStateLock.unlock();
        }

        long tmt = tmFinish - System.currentTimeMillis();
        if (tmt < 1) {
            tmt = 1;
        }
        try {
            openHelper.get(tmt, TimeUnit.MILLISECONDS);
        } catch (final ExecutionException ex) {
            throw new ProtocolException("Failed to wait for open", ex);
        } catch (final TimeoutException ex) {
            throw new ProtocolTimeoutException("Failed to wait for open", ex);
        }
    }

    @Override
    public void beginOpen()
            throws ProtocolException {
        openAsync(null, null);
    }


    @Override
    public void close(final long timeout)
            throws ProtocolException, IllegalStateException,
                    InterruptedException {
        final long tmStart = System.currentTimeMillis();
        final long tmFinish = tmStart + ((timeout >= 0) ? timeout : DEFAULT_TIMEOUT);
        AsyncCloseHelper<Void, Void> closeHelper = null;

        channelStateLock.lock();
        try {
            if (channelState.equals(ChannelState.Closed)) {
                releaseIntInvoker();
                return;
            }
            if (channelState.equals(ChannelState.Closing)) {
                for (;;) {
                    long tmt = tmFinish - System.currentTimeMillis();
                    if (tmt < 1) {
                        tmt = 1;
                    }
                    if (channelStateChange.await(tmt, TimeUnit.MILLISECONDS)) {
                        if (channelState.equals(ChannelState.Closed)) {
                            return;
                        }
                        if (!channelState.equals(ChannelState.Closing)) {
                            break;
                        }
                    } else {
                        throw new ProtocolTimeoutException("Timeout waiting for closed state");
                    }
                }
            }

            setState(ChannelState.Closing, null);
            try {
                loadBalancer.clear();
            } catch (final Exception ex) {
                log.warn("Exception clearing Load Balancer", ex);
            }

            final int num = clusterProtocols.size();
            final int num2 = protocolsRemoving.size();
            if (num + num2 > 0) {
                closeHelper = new AsyncCloseHelper<Void, Void>(num + num2, null);
                for (final ProtocolNodeRec item: clusterProtocols.values()) {
                    try {
                        item.warmstandby().closeAsync(closeHelper, null);
                    } catch (final Exception ex) {
                        fireErrorEvent(new ChannelErrorEvent(item.protocolNode(), ex));
                    }
                }
                protocolsOpening.clear();
                for (final ProtocolNodeRec item: protocolsRemoving) {
                    try {
                        item.closeAsync(closeHelper, null);
                    } catch (final Exception ex) {
                        fireErrorEvent(new ChannelErrorEvent(item.protocolNode(), ex));
                    }
                }
                protocolsRemoving.clear();
            }
        } finally {
            channelStateLock.unlock();
        }

        long tmt = tmFinish - System.currentTimeMillis();
        if (tmt < 1) {
            tmt = 1;
        }
        try {
            if (closeHelper != null) {
                closeHelper.get(tmt, TimeUnit.MILLISECONDS);
            }
            setState(ChannelState.Closed, null);
        } catch (final ExecutionException ex) {
            throw new ProtocolException("Failed to wait for open", ex);
        } catch (final TimeoutException ex) {
            throw new ProtocolTimeoutException("Failed to wait for close", ex);
        }
    }

    @Override
    public void beginClose() {
        closeAsync(null, null);
    }

    @Override
    public <A> void closeAsync(
            final CompletionHandler<ChannelClosedEvent, ? super A> handler,
            final A attachment) {
        channelStateLock.lock();
        try {
            if (channelState.equals(ChannelState.Closed)) {
                if (handler != null) {
                    new CompletionHelper<ChannelClosedEvent, A>(handler)
                            .completed(new ChannelClosedEvent(
                                            ClusterProtocolImpl.this, (Throwable) null,
                                            ChannelState.Closed),
                                    attachment);
                }
                releaseIntInvoker();
                return;
            }

            final int num = clusterProtocols.size();
            final int num2 = protocolsRemoving.size();
            if (num + num2 > 0) {
                setState(ChannelState.Closing, null);
                try {
                    loadBalancer.clear();
                } catch (final Exception ex) {
                    log.warn("Exception clearing Load Balancer", ex);
                }

                final AsyncCloseHelper<Void, A> closeHelper =
                        new AsyncCloseHelper<Void, A>(num + num2, handler);
                for (final ProtocolNodeRec item: clusterProtocols.values()) {
                    try {
                        item.warmstandby().closeAsync(closeHelper, attachment);
                    } catch (final Exception ex) {
                        fireErrorEvent(new ChannelErrorEvent(item.protocolNode(), ex));
                    }
                }
                protocolsOpening.clear();
                for (final ProtocolNodeRec item: protocolsRemoving) {
                    try {
                        item.closeAsync(closeHelper, attachment);
                    } catch (final Exception ex) {
                        fireErrorEvent(new ChannelErrorEvent(item.protocolNode(), ex));
                    }
                }
                protocolsRemoving.clear();
            } else {
                final ChannelState prevState = channelState;
                setState(ChannelState.Closed, null);

                if (handler != null) {
                    new CompletionHelper<ChannelClosedEvent, A>(handler)
                            .completed(new ChannelClosedEvent(
                                            ClusterProtocolImpl.this, (Throwable) null,
                                            prevState),
                                    attachment);
                }
            }
        } finally {
            channelStateLock.unlock();
        }
    }


    protected P createProtocol() {
        return protocolBuilder.build();
    }

    protected ProtocolNodeRec createProtocolNode(
            final WSConfig wsConfig) {
        final P protocol = createProtocol();

        if (protocol.getEndpoint() == null) {
            final List<Endpoint> endpoints = wsConfig.getEndpoints();
            if (endpoints != null && endpoints.size() > 0) {
                protocol.setEndpoint(endpoints.get(0));
            }
        }

        final WarmStandby warmstandby = new WarmStandby(wsConfig.getName(), protocol);
        warmstandby.setConfig(wsConfig);

        final ProtocolNodeRec protocolItem = new ProtocolNodeRec(warmstandby);
        setupProtocolNode(protocolItem);

        return protocolItem;
    }


    protected void setState(
            final ChannelState newState,
            final Throwable cause) {
        channelStateLock.lock();
        try {
            if ((newState == null) || newState.equals(channelState)
                || (newState.equals(ChannelState.Closing)
                    && channelState.equals(ChannelState.Closed))) {
                return; // ignore incorrect state change
            }

            if (log.isDebug()) {
                log.debug("Channel state transition [" + endpoint + "]: " 
                        + channelState + " -> " + newState);
            }
            final ChannelState prevState = channelState;
            channelState = newState;

            if (newState.equals(ChannelState.Closed)) {
                if (!prevState.equals(ChannelState.Opening)) {
                    final ChannelClosedEvent event =
                            new ChannelClosedEvent(ClusterProtocolImpl.this, cause, prevState);
                    fireClosedEvent(event);
                }
                releaseIntInvoker();
            } else if (newState.equals(ChannelState.Opening)) {
                if (prevState.equals(ChannelState.Opened)) {
                    final ChannelClosedEvent event =
                            new ChannelClosedEvent(ClusterProtocolImpl.this, cause, prevState);
                    fireClosedEvent(event);
                }
            } else if (newState.equals(ChannelState.Opened)) {
                fireOpenedEvent(
                        new ChannelOpenedEvent(ClusterProtocolImpl.this));
            }

            channelStateChange.signalAll();
        } finally {
            channelStateLock.unlock();
        }
    }


    protected Protocol chooseProtocol(final Message request) {
        if (request != null && protocolPolicy.useRequestProtocolId()) {
            final int rqProtocolId = request.getProtocolId();
            if (rqProtocolId != 0) {
                final Protocol protocol = getNodeProtocol(rqProtocolId);
                if (protocol != null && protocol.getState().equals(ChannelState.Opened)) {
                    return protocol;
                }
            }
        }
        return loadBalancer.chooseNode(request);
    }


    @Override
    public Protocol getNextAvailableProtocol() {
        return chooseProtocol(null);
    }

    @Override
    public Protocol getNextAvailableProtocol(final Message message) {
        return chooseProtocol(message);
    }


    @Override
    public List<Protocol> getOpenedNodesProtocols() {
        channelStateLock.lock();
        try {
            final List<Protocol> nodes = new ArrayList<Protocol>(
                    protocolsOpened.size());
            for (final ProtocolNodeRec item: protocolsOpened) {
                nodes.add(item.protocolNode());
            }
            return nodes;
        } finally {
            channelStateLock.unlock();
        }
    }

    @Override
    public List<Protocol> getAllNodesProtocols() {
        channelStateLock.lock();
        try {
            final List<Protocol> nodes = new ArrayList<Protocol>(
                    clusterProtocols.size());
            for (final ProtocolNodeRec item: clusterProtocols.values()) {
                nodes.add(item.protocolNode());
            }
            return nodes;
        } finally {
            channelStateLock.unlock();
        }
    }


    @Override
    public Protocol getNodeProtocol(final String nodeName) {
        channelStateLock.lock();
        try {
            final ProtocolNodeRec item = clusterProtocols.get(nodeName);
            if (item != null) {
                return item.protocolNode();
            }
            return null;
        } finally {
            channelStateLock.unlock();
        }
    }

    @Override
    public Protocol getNodeProtocol(final int protocolId) {
        channelStateLock.lock();
        try {
            if (!clusterProtocols.isEmpty()) {
                for (final ProtocolNodeRec item: clusterProtocols.values()) {
                    final Protocol protocol = item.protocolNode();
                    if (protocol.getProtocolId() == protocolId) {
                        return protocol;
                    }
                }
            }
            return null;
        } finally {
            channelStateLock.unlock();
        }
    }


    @Override
    public void send(
            final Message message)
                    throws ProtocolException {
        throwNull(message, "message");
        throwOnClosed();
        final long timeout = getTimeout();
        final long tmStart = System.currentTimeMillis();
        final boolean waitOnOpening = protocolPolicy.waitOnChannelOpening();

        while (true) {
            final ChannelState state = getState();
            if (state.equals(ChannelState.Closed)
                    || state.equals(ChannelState.Closing)) {
                throw new ChannelClosedOnSendException(
                        "Channel has been closed before message delivery");
            } else if (state.equals(ChannelState.Opening)
                    && !waitOnOpening) {
                throw new ChannelClosedOnSendException(
                        "Protocol is not opened");
            }

            final Protocol protocol = chooseProtocol(message);
            if (protocol != null && protocol.getState() == ChannelState.Opened) {
                try {
                    protocol.send(message);
                    break;
                } catch (final ChannelClosedOnSendException ex) {
                    log.debug("Protocol connection has been lost right before sending", ex);
                    fireErrorEvent(new ChannelErrorEvent(protocol, ex));
                    // bypass the exception for retry...
                } catch (final ChannelNotOpenedException ex) {
                    log.debug("Protocol connection has been lost before sending", ex);
                    fireErrorEvent(new ChannelErrorEvent(protocol, ex));
                    // bypass the exception for retry...
                }
            }

            channelStateLock.lock();
            try {
                if (protocol != null) {
                    try {
                        loadBalancer.removeNode(protocol);
                    } catch (final Exception ex) {
                        log.warn("Exception removing node from Load Balancer", ex);
                    }
                }
                if (channelState.equals(ChannelState.Opening)
                        && waitOnOpening) {
                    long ttl = 0;
                    if (timeout > 0) {
                        ttl = timeout - (System.currentTimeMillis() - tmStart);
                        if (ttl <= 0) {
                            throw new ChannelClosedOnSendException(
                                    "Got no connected protocol in the timeout period");
                        }
                    }

                    if (ttl > 0) {
                        channelStateChange.await(ttl, TimeUnit.MILLISECONDS);
                    } else {
                        channelStateChange.await();
                    }
                }
            } catch (final InterruptedException ex) {
                throw new ChannelClosedOnSendException(
                        "Failed to wait for protocol connection", ex);
            } finally {
                channelStateLock.unlock();
            }
        }
    }


    @Override
    public Message request(
            final Message message)
                    throws ProtocolException, IllegalStateException {
        return request(message, getTimeout());
    }

    @Override
    public Message request(
            final Message message,
            final long timeout)
                    throws ProtocolException, IllegalStateException {
        return endRequest(beginRequest(message), timeout);
    }

    @Override
    public RequestFuture beginRequest(
            final Message message)
                    throws ProtocolException, IllegalStateException {
        return beginRequest(message, getTimeout());
    }

    public RequestFuture beginRequest(
            final Message message,
            final long timeout)
                    throws ProtocolException, IllegalStateException {
        throwNull(message, "message");
        throwOnClosed();
        final long tmStart = System.currentTimeMillis();
        final boolean waitOnOpening = protocolPolicy.waitOnChannelOpening();

        while (true) {
            final ChannelState state = getState();
            if (state.equals(ChannelState.Closed)
                    || state.equals(ChannelState.Closing)) {
                throw new ChannelClosedOnSendException(
                        "Channel has been closed before request delivery");
            } else if (state.equals(ChannelState.Opening)
                    && !waitOnOpening) {
                throw new ChannelClosedOnSendException(
                        "Protocol is not opened");
            }

            final Protocol protocol = chooseProtocol(message);
            if (protocol != null && protocol.getState() == ChannelState.Opened) {
                try {
                    return new RequestFutureWrapper(
                            protocol.beginRequest(message/*, timeout*/),
                            protocol);
                } catch (final ChannelClosedOnSendException ex) {
                    log.debug("Protocol connection has been lost right before the request", ex);
                    fireErrorEvent(new ChannelErrorEvent(protocol, ex));
                    // bypass the exception for retry...
                } catch (final ChannelNotOpenedException ex) {
                    log.debug("Protocol connection has been lost before the request", ex);
                    fireErrorEvent(new ChannelErrorEvent(protocol, ex));
                    // bypass the exception for retry...
                }
            }

            channelStateLock.lock();
            try {
                if (protocol != null) {
                    try {
                        loadBalancer.removeNode(protocol);
                    } catch (final Exception ex) {
                        log.warn("Exception removing node from Load Balancer", ex);
                    }
                }
                if (channelState.equals(ChannelState.Opening)
                        && waitOnOpening) {
                    long ttl = 0;
                    if (timeout > 0) {
                        ttl = timeout - (System.currentTimeMillis() - tmStart);
                        if (ttl <= 0) {
                            throw new ChannelClosedOnSendException(
                                    "Got no connected protocol in the timeout period");
                        }
                    }

                    if (ttl > 0) {
                        channelStateChange.await(ttl, TimeUnit.MILLISECONDS);
                    } else {
                        channelStateChange.await();
                    }
                }
            } catch (final InterruptedException ex) {
                throw new ChannelClosedOnSendException(
                        "Failed to wait for protocol connection", ex);
            } finally {
                channelStateLock.unlock();
            }
        }
    }

    @Override
    public <A> void requestAsync(
            final Message message,
            final A attachment,
            final CompletionHandler<Message, ? super A> handler)
                    throws ProtocolException, IllegalStateException {
        requestAsync(message, attachment, handler, getTimeout());
    }

    @Override
    public <A> void requestAsync(
            final Message message,
            final A attachment,
            final CompletionHandler<Message, ? super A> handler,
            final long timeout)
                    throws ProtocolException, IllegalStateException {
        throwNull(message, "message");
        throwOnClosed();
        final long tmStart = System.currentTimeMillis();
        final boolean waitOnOpening = protocolPolicy.waitOnChannelOpening();

        while (true) {
            final ChannelState state = getState();
            if (state.equals(ChannelState.Closed)
                    || state.equals(ChannelState.Closing)) {
                throw new ChannelClosedOnSendException(
                        "Channel has been closed before request delivery");
            } else if (state.equals(ChannelState.Opening)
                    && !waitOnOpening) {
                throw new ChannelClosedOnSendException(
                        "Protocol is not opened");
            }

            final Protocol protocol = chooseProtocol(message);
            if (protocol != null && protocol.getState() == ChannelState.Opened) {
                try {
                    protocol.requestAsync(message, attachment,
                            (handler != null) ? new CompletionHelper<Message, A>(handler) : null,
                            timeout);
                    break;
                } catch (final ChannelClosedOnSendException ex) {
                    log.debug("Protocol connection has been lost right before the request", ex);
                    fireErrorEvent(new ChannelErrorEvent(protocol, ex));
                    // bypass the exception for retry...
                } catch (final ChannelNotOpenedException ex) {
                    log.debug("Protocol connection has been lost before the request", ex);
                    fireErrorEvent(new ChannelErrorEvent(protocol, ex));
                    // bypass the exception for retry...
                }
            }

            channelStateLock.lock();
            try {
                if (protocol != null) {
                    try {
                        loadBalancer.removeNode(protocol);
                    } catch (final Exception ex) {
                        log.warn("Exception removing node from Load Balancer", ex);
                    }
                }
                if (channelState.equals(ChannelState.Opening)
                        && waitOnOpening) {
                    long ttl = 0;
                    if (timeout > 0) {
                        ttl = timeout - (System.currentTimeMillis() - tmStart);
                        if (ttl <= 0) {
                            throw new ChannelClosedOnSendException(
                                    "Got no connected protocol in the timeout period");
                        }
                    }

                    if (ttl > 0) {
                        channelStateChange.await(ttl, TimeUnit.MILLISECONDS);
                    } else {
                        channelStateChange.await();
                    }
                }
            } catch (final InterruptedException ex) {
                throw new ChannelClosedOnSendException(
                        "Failed to wait for protocol connection", ex);
            } finally {
                channelStateLock.unlock();
            }
        }
    }


    @Override
    public Message endRequest(
            final RequestFuture future)
                    throws ProtocolException {
        return endRequest(future, getTimeout());
    }

    @Override
    public Message endRequest(
            final RequestFuture future,
            final long timeout)
                    throws ProtocolException {
        if (future != null) {
            try {
                final Object res = future.get(timeout, TimeUnit.MILLISECONDS);
                if (res instanceof Message) {
                    return (Message) res;
                }
            } catch (final InterruptedException ex) {
                final Throwable cause = ex.getCause();
                if (cause instanceof ChannelClosedOnSendException) {
                    throw (ChannelClosedOnSendException) cause;
                }
                if (cause instanceof ProtocolException) {
                    throw new ChannelClosedOnRequestException(cause.toString());
                }
                if (cause instanceof IllegalStateException) {
                    throw (IllegalStateException) cause;
                }
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                throw new CancellationException(ex.toString());
            }
        }
        return null;
    }


    @Override
    protected void onChannelOpened(
            final ProtocolNodeRec protocolItem,
            final WSOpenedEvent event) {
        throwNull(protocolItem, "ClusterProtocolNode");
        channelStateLock.lock();
        try {
            if (clusterProtocols.containsValue(protocolItem)
                    && protocolsOpening.remove(protocolItem)) {
                protocolsOpened.add(protocolItem);
                try {
                    loadBalancer.addNode(protocolItem.protocolNode());
                } catch (final Exception ex) {
                    log.warn("Exception adding node to Load Balancer", ex);
                }

                if (channelState.equals(ChannelState.Opening)) {
                    setState(ChannelState.Opened, null);
                }

                fireOpenedInternalChannelEvent(event.getChannelOpenedEvent());
            } else {
                log.warn("Unexpected ChannelOpenedEvent: " + event);
                protocolItem.closeAsync(null, null);
            }
        } finally {
            channelStateLock.unlock();
        }
    }

    @Override
    protected void onChannelDisconnected(
            final ProtocolNodeRec protocolItem,
            final WSDisconnectedEvent event) {
        throwNull(protocolItem, "ClusterProtocolNode");
        channelStateLock.lock();
        try {
            if (protocolsOpened.remove(protocolItem)) {
                try {
                    loadBalancer.removeNode(protocolItem.protocolNode());
                } catch (final Exception ex) {
                    log.warn("Exception removing node from Load Balancer", ex);
                }
            }
            if (protocolsRemoving.remove(protocolItem)
                    || channelState.equals(ChannelState.Closing)
                    || channelState.equals(ChannelState.Closed)) {
                protocolsOpening.remove(protocolItem);
            } else {
                protocolsOpening.add(protocolItem);
            }

            if (event != null) {
                fireClosedInternalChannelEvent(event.getChannelClosedEvent());
            }

            if (protocolsOpened.isEmpty()) {
                if (protocolsOpening.isEmpty()) {
                    if (protocolsRemoving.isEmpty()) {
                        setState(ChannelState.Closed, null);
                    }
                    else {
                        setState(ChannelState.Closing, null);
                    }
                } else {
                    if (channelState.equals(ChannelState.Opened)) {
                        setState(ChannelState.Opening, null);
                    }
                }
            }
        } finally {
            channelStateLock.unlock();
        }
    }

    @Override
    protected void onChannelFailure(
            final ProtocolNodeRec protocolItem,
            final WSTriedUnsuccessfullyEvent event) {
        throwNull(protocolItem, "ClusterProtocolNode");
        channelStateLock.lock();
        try {
            if (protocolsOpened.remove(protocolItem)) {
                try {
                    loadBalancer.removeNode(protocolItem.protocolNode());
                } catch (final Exception ex) {
                    log.warn("Exception removing node from Load Balancer", ex);
                }
            }
            if (protocolsRemoving.remove(protocolItem)
                    || event.isRestoringStopped()
                    || channelState.equals(ChannelState.Closed)
                    || channelState.equals(ChannelState.Closing)) {
                protocolsOpening.remove(protocolItem);
            } else {
                protocolsOpening.add(protocolItem);
            }

            if (protocolsOpened.isEmpty()) {
                if (protocolsOpening.isEmpty()) {
                    if (protocolsRemoving.isEmpty()) {
                        setState(ChannelState.Closed, null);
                    }
                    else {
                        setState(ChannelState.Closing, null);
                    }
                } else {
                    if (getState().equals(ChannelState.Opened)) {
                        setState(ChannelState.Opening, null);
                    }
                }
            }
        } finally {
            channelStateLock.unlock();
        }
    }


    @Override
    public Endpoint getEndpoint() {
        return endpoint;
    }


    @Override
    public void setEndpoint(final Endpoint endpoint) {
        throwNull(endpoint, "endpoint");
        throwNotClosed();
        try {
            this.endpoint = (Endpoint) endpoint.clone();
        } catch (CloneNotSupportedException e) {
            this.endpoint = endpoint;
        }
        configure(this.endpoint.getConfiguration());
    }


    @Override
    public ConnectionConfiguration getConfiguration() {
        if (endpoint != null) {
            return endpoint.getConfiguration();
        }
        return null;
    }


    @Override
    public void configure(final ConnectionConfiguration config) {
        throwNull(config, "ConnectionConfiguration");
        loadBalancer.configure(config);
        // TODO update myEndpoint
    }


    @Override
    public void setNodesEndpoints(final Endpoint... endpoints) {
        throwNull(endpoints, "endpoints");
        validateEndpoints(Arrays.asList(endpoints));
        final ArrayList<WSConfig> nodes = new ArrayList<WSConfig>(endpoints.length);
        for (final Endpoint endpoint: endpoints) {
            final String name = endpoint.getName();
            nodes.add(new WSConfig(name).setEndpoints(endpoint));
        }

        setNodesImpl(nodes);
    }

    @Override
    public void setNodesEndpoints(final Iterable<Endpoint> endpoints) {
        validateEndpoints(endpoints);
        final ArrayList<WSConfig> nodes = new ArrayList<WSConfig>();
        for (final Endpoint endpoint: endpoints) {
            final String name = endpoint.getName();
            nodes.add(new WSConfig(name).setEndpoints(endpoint));
        }

        setNodesImpl(nodes);
    }

    @Override
    public void setNodes(final WSConfig... nodes) {
        throwNull(nodes, "nodes");
        final List<WSConfig> list = Arrays.asList(nodes);
        validateWSConfigs(list);

        setNodesImpl(list);
    }

    @Override
    public void setNodes(final Iterable<WSConfig> nodes) {
        validateWSConfigs(nodes);

        setNodesImpl(nodes);
    }

    protected void setNodesImpl(final Iterable<WSConfig> nodes) {
        channelStateLock.lock();
        try {
            Map<String, WSConfig> nodes2add = null;
            Map<String, WSConfig> nodes2update = null;
            List<String>          nodes2remove = null;

            for (final WSConfig node: nodes) {
                final String name = node.getName();
                final ProtocolNodeRec item = clusterProtocols.get(name);
                if (item != null) {
                    if (nodes2update == null) {
                        nodes2update = new HashMap<String, WSConfig>();
                        nodes2update.put(name, node);
                    } else {
                        nodes2update.put(name, node);
                    }
                } else {
                    if (nodes2add == null) {
                        nodes2add = new LinkedHashMap<String, WSConfig>();
                        nodes2add.put(name, node);
                    } else {
                        nodes2add.put(name, node);
                    }
                }
            }
            for (final String itemName: clusterProtocols.keySet()) {
                if (nodes2update == null || !nodes2update.containsKey(itemName)) {
                    if (nodes2remove == null) {
                        nodes2remove = new ArrayList<String>();
                    }
                    nodes2remove.add(itemName);
                }
            }

            boolean doOpen = channelState.equals(ChannelState.Opened)
                          || channelState.equals(ChannelState.Opening);

            if (nodes2add != null) {
                for (final Entry<String, WSConfig> entry: nodes2add.entrySet()) {
                    final ProtocolNodeRec newItem = createProtocolNode(entry.getValue());
                    if (newItem != null) {
                        final String name = entry.getKey();
                        clusterProtocols.put(name, newItem);
                        if (doOpen) {
                            protocolsOpening.add(newItem);
                            newItem.warmstandby().openAsync();
                        }
                    }
                }
            }

            if (nodes2update != null) {
                for (final Entry<String, WSConfig> entry: nodes2update.entrySet()) {
                    final ProtocolNodeRec item = clusterProtocols.get(entry.getKey());
                    if (item != null) {
                        item.warmstandby().setConfig(entry.getValue());
                        // conditional graceful reopen?
                    }
                }
            }

            if (nodes2remove != null) {
                for (final String nodeName: nodes2remove) {
                    final ProtocolNodeRec item = clusterProtocols.remove(nodeName);
                    if (item != null) {
                        if (protocolsOpened.remove(item)) {
                            try {
                                loadBalancer.removeNode(item.protocolNode());
                            } catch (final Exception ex) {
                                log.warn("Exception removing node from Load Balancer", ex);
                            }
                            protocolsRemoving.add(item);
                            item.drain();
                        } else if (protocolsOpening.remove(item)) {
                            protocolsRemoving.add(item);
                            item.closeAsync(new CompletionHandler<Void, Void>() {
                                @Override
                                public void completed(final Void result, final Void attachment) {
                                    updateState();
                                }
                                @Override
                                public void failed(final Throwable exc, final Void attachment) {
                                    updateState();
                                }
                                protected void updateState() {
                                    channelStateLock.lock();
                                    try {
                                        if (protocolsRemoving.remove(item)) {
                                            if (clusterProtocols.isEmpty()
                                                    && protocolsRemoving.isEmpty()) {
                                                setState(ChannelState.Closed, null);
                                            }
                                        }
                                    } finally {
                                        channelStateLock.unlock();
                                    }
                                }
                            }, null);
                        }
                    }
                }
            }

            if (clusterProtocols.isEmpty()) {
                if (protocolsRemoving.isEmpty()) {
                    setState(ChannelState.Closed, null);
                } else {
                    setState(ChannelState.Closing, null);
                }
            }
        } finally {
            channelStateLock.unlock();
        }
    }

    @Override
    public void addNodesEndpoints(final Endpoint... endpoints) {
        throwNull(endpoints, "endpoints");
        validateEndpoints(Arrays.asList(endpoints));
        final ArrayList<WSConfig> nodes = new ArrayList<WSConfig>(endpoints.length);
        for (final Endpoint endpoint: endpoints) {
            final String name = endpoint.getName();
            nodes.add(new WSConfig(name).setEndpoints(endpoint));
        }

        addNodesImpl(nodes);
    }

    @Override
    public void addNodesEndpoints(final Iterable<Endpoint> endpoints) {
        validateEndpoints(endpoints);
        final ArrayList<WSConfig> nodes = new ArrayList<WSConfig>();
        for (final Endpoint endpoint: endpoints) {
            final String name = endpoint.getName();
            nodes.add(new WSConfig(name).setEndpoints(endpoint));
        }

        addNodesImpl(nodes);
    }

    @Override
    public void addNodes(final WSConfig... nodes) {
        throwNull(nodes, "nodes");
        final List<WSConfig> list = Arrays.asList(nodes);
        validateWSConfigs(list);

        addNodesImpl(list);
    }

    @Override
    public void addNodes(final Iterable<WSConfig> nodes) {
        validateWSConfigs(nodes);

        addNodesImpl(nodes);
    }

    protected void addNodesImpl(final Iterable<WSConfig> nodes) {
        channelStateLock.lock();
        try {
            final boolean doOpen = channelState.equals(ChannelState.Opened)
                    || channelState.equals(ChannelState.Opening);

            for (final WSConfig node: nodes) {
                final String name = node.getName();
                final ProtocolNodeRec item = clusterProtocols.get(name);
                if (item != null) {
                    if (log.isWarn()) {
                        log.warn("Tried to add existing Endpoint: " + name + " - updating configuration...");
                    }
                    item.warmstandby().setConfig(node);
                } else {
                    final ProtocolNodeRec newItem = createProtocolNode(node);
                    if (newItem != null) {
                        clusterProtocols.put(name, newItem);
                        if (doOpen) {
                            protocolsOpening.add(newItem);
                            newItem.warmstandby().openAsync();
                        }
                    }
                }
            }
        } finally {
            channelStateLock.unlock();
        }
    }


    @Override
    public void removeNodes(final String... names) {
        throwNull(names, "names");
        removeNodes(Arrays.asList(names));
    }

    @Override
    public void removeNodes(final Iterable<String> names) {
        throwNull(names, "names");
        channelStateLock.lock();
        try {
            for (final String name: names) {
                if (name != null && !name.isEmpty()) {
                    final ProtocolNodeRec item = clusterProtocols.remove(name);
                    if (item != null) {
                        if (protocolsOpened.remove(item)) {
                            try {
                                loadBalancer.removeNode(item.protocolNode());
                            } catch (final Exception ex) {
                                log.warn("Exception removing node from Load Balancer", ex);
                            }
                            protocolsRemoving.add(item);
                            item.drain();
                        } else if (protocolsOpening.remove(item)) {
                            protocolsRemoving.add(item);
                            item.closeAsync(new CompletionHandler<Void, Void>() {
                                @Override
                                public void completed(final Void result, final Void attachment) {
                                    updateState();
                                }
                                @Override
                                public void failed(final Throwable exc, final Void attachment) {
                                    updateState();
                                }
                                protected void updateState() {
                                    channelStateLock.lock();
                                    try {
                                        if (protocolsRemoving.remove(item)) {
                                            if (clusterProtocols.isEmpty()
                                                    && protocolsRemoving.isEmpty()) {
                                                setState(ChannelState.Closed, null);
                                            }
                                        }
                                    } finally {
                                        channelStateLock.unlock();
                                    }
                                }
                            }, null);
                        }
                    } else {
                        if (log.isWarn()) {
                            log.warn("Tried to remove non-existing Node: " + name);
                        }
                    }
                } else {
                    log.warn("Empty Node name value specified");
                }
            }

            if (clusterProtocols.isEmpty()) {
                if (protocolsRemoving.isEmpty()) {
                    setState(ChannelState.Closed, null);
                } else {
                    setState(ChannelState.Closing, null);
                }
            }
        } finally {
            channelStateLock.unlock();
        }
    }


    @Override
    public List<WSConfig> getNodesConfig() {
        channelStateLock.lock();
        try {
            final ArrayList<WSConfig> ret = new ArrayList<WSConfig>(clusterProtocols.size());
            for (final ProtocolNodeRec item: clusterProtocols.values()) {
                ret.add((WSConfig) item.warmstandby().getConfig());
            }
            return ret;
        } finally {
            channelStateLock.unlock();
        }
    }


    /**
     * This method does nothing.<br/>
     * Cluster protocol does not provide internal invoker customization.
     */
    @Override
    @Deprecated
    public void setInvoker(final AsyncInvoker invoker) {
    }

    /**
     * This method does nothing.<br/>
     * Cluster protocol does not use connection invoker.
     */
    @Override
    @Deprecated
    public void setConnectionInvoker(final AsyncInvoker connectionInvoker) {
    }



    protected class CompletionHelper<V, A>
            implements CompletionHandler<V, A> {

        private final CompletionHandler<V, ? super A> userHandler;

        public CompletionHelper(final CompletionHandler<V, ? super A> handler) {
            this.userHandler = handler;
        }

        @Override
        public void completed(final V result, final A attachment) {
            execService.execute(new Runnable() {
                public void run() {
                    userHandler.completed(result, attachment);
                }});
        }

        @Override
        public void failed(final Throwable exc, final A attachment) {
            execService.execute(new Runnable() {
                public void run() {
                    userHandler.failed(exc, attachment);
                }});
        }
    }

    private class AsyncOpenHelper<A>
            implements CompletionHandler<ChannelOpenedEvent, A> {

        private final AtomicReference<ChannelOpenedEvent> completion =
                new AtomicReference<ChannelOpenedEvent>(null);
        private final AtomicReference<Throwable> failure = new AtomicReference<Throwable>(null);

        private final AtomicInteger countDown;
        private final CompletionHandler<ChannelOpenedEvent, ? super A> userHandler;

        private final TimerActionTicket timeoutActionTicket;

        public AsyncOpenHelper(
                final int channelsNumber,
                final CompletionHandler<ChannelOpenedEvent, ? super A> handler,
                final A attachment,
                final long timeout) {
            if (channelsNumber <= 0) {
                throw new IllegalArgumentException("Channels number is not positive value");
            }
            this.countDown = new AtomicInteger(channelsNumber);
            this.userHandler = handler;
            if (timeout > 0) {
                timeoutActionTicket = TimerFactory.getTimer().schedule(timeout,
                        new TimerAction() {
                            @Override
                            public void onTimer() {
                                onTimeout(attachment);
                            }
                            @Override
                            public String toString() {
                                return "Cluster Open timeout task for " + getEndpoint();
                            }
                        });
            } else {
                timeoutActionTicket = null;
            }
        }

        @Override
        public synchronized void completed(final ChannelOpenedEvent result, final A attachment) {
            if (completion.compareAndSet(null,
                    new ChannelOpenedEvent(ClusterProtocolImpl.this))) {
                if (timeoutActionTicket != null) {
                    timeoutActionTicket.cancel();
                }
                countDown.decrementAndGet();
                if (userHandler != null) {
                    execService.execute(new Runnable() {
                        public void run() {
                            userHandler.completed(result, attachment);
                        }});
                }
                execService.execute(new Runnable() {
                    public void run() {
                        synchronized (AsyncOpenHelper.this) {
                            AsyncOpenHelper.this.notifyAll();
                        }
                    }});
            } else {
                countDown.decrementAndGet();
            }
        }

        @Override
        public synchronized void failed(final Throwable exc, final A attachment) {
            if (timeoutActionTicket != null) {
                timeoutActionTicket.cancel();
            }
//            // dead code
//            if (exc instanceof RegistrationException) { // just in case...
//                failure.compareAndSet(null, exc);
//                if (countDown.decrementAndGet() == 0) {
//                    if (completion.get() == null && userHandler != null) {
//                        execService.execute(new Runnable() {
//                            public void run() {
//                                userHandler.failed(exc, attachment);
//                            }});
//                    }
//                    execService.execute(new Runnable() {
//                        public void run() {
//                            synchronized (AsyncOpenHelper.this) {
//                                AsyncOpenHelper.this.notifyAll();
//                            }
//                        }});
//                }
//            }

        }

        public synchronized void onTimeout(final A attachment) {
            if (completion.get() == null) {
                final TimeoutException exc = new TimeoutException();
                if (failure.compareAndSet(null, exc)) {
                    if (userHandler != null) {
                        execService.execute(new Runnable() {
                            public void run() {
                                userHandler.failed(exc, attachment);
                            }});
                    }
                    execService.execute(new Runnable() {
                        public void run() {
                            synchronized (AsyncOpenHelper.this) {
                                AsyncOpenHelper.this.notifyAll();
                            }
                        }});
                }
            }
        }

        public synchronized ChannelOpenedEvent get(final long timeout, final TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
            final long tmStart = System.currentTimeMillis();
            final long tmtMs = (timeout > 0 && unit != null) ? unit.toMillis(timeout) : timeout;
            while (true) {
                final ChannelOpenedEvent value = completion.get();
                if (value != null) {
                    return value;
                }
                if (countDown.get() <= 0) {
                    final Throwable thr = failure.get();
                    if (thr != null) {
                        throw new ExecutionException(thr);
                    }
                }
                if (tmtMs >= 0) {
                    final long timeleft = tmtMs - (System.currentTimeMillis() - tmStart);
                    if (timeleft <= 0) {
                        throw new TimeoutException();
                    }
                    AsyncOpenHelper.this.wait(timeleft);
                } else {
                    AsyncOpenHelper.this.wait();
                }
            }
        }
    }


    private class AsyncCloseHelper<V, A>
            implements CompletionHandler<V, A> {

        private final AtomicInteger countDown;
        private final AtomicReference<V> resultholder = new AtomicReference<V>(null);
        private final CompletionHandler<ChannelClosedEvent, ? super A> userHandler;
        private final AtomicReference<Throwable> failure = new AtomicReference<Throwable>(null);

        protected final Lock lock = new ReentrantLock(true);

        public AsyncCloseHelper(
                final int channelsNumber,
                final CompletionHandler<ChannelClosedEvent, ? super A> handler) {
            if (channelsNumber <= 0) {
                throw new IllegalArgumentException("Channels number is not positive value");
            }
            this.countDown = new AtomicInteger(channelsNumber);
            this.userHandler = handler;
        }

        @Override
        public void completed(final V result, final A attachment) {
            lock.lock();
            try {
                resultholder.compareAndSet(null, result);
                if (countDown.decrementAndGet() == 0) {
                    setState(ChannelState.Closed, null);
                    if (userHandler != null) {
                        execService.execute(new Runnable() {
                            public void run() {
                                userHandler.completed(
                                        new ChannelClosedEvent(ClusterProtocolImpl.this),
                                        attachment);
                            }});
                    }
                    execService.execute(new Runnable() {
                        public void run() {
                            synchronized (AsyncCloseHelper.this) {
                                AsyncCloseHelper.this.notifyAll();
                            }
                        }});
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void failed(final Throwable exc, final A attachment) {
            lock.lock();
            try {
                failure.compareAndSet(null, exc);
                if (countDown.decrementAndGet() == 0) {
                    setState(ChannelState.Closed, null);
                    if (userHandler != null) {
                        execService.execute(new Runnable() {
                            public void run() {
                                userHandler.failed(exc, attachment);
                            }});
                    }
                    execService.execute(new Runnable() {
                        public void run() {
                            synchronized (AsyncCloseHelper.this) {
                                AsyncCloseHelper.this.notifyAll();
                            }
                        }});
                }
            } finally {
                lock.unlock();
            }
        }

        public synchronized V get(final long timeout, final TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
            final long tmStart = System.currentTimeMillis();
            final long tmtMs = (timeout > 0 && unit != null) ? unit.toMillis(timeout) : timeout;
            while (true) {
                if (countDown.get() <= 0) {
                    final Throwable thr = failure.get();
                    if (thr != null) {
                        throw new ExecutionException(thr);
                    }
                    return resultholder.get();
                }
                if (tmtMs >= 0) {
                    final long timeleft = tmtMs - (System.currentTimeMillis() - tmStart);
                    if (timeleft <= 0) {
                        throw new TimeoutException();
                    }
                    AsyncCloseHelper.this.wait(timeleft);
                } else {
                    AsyncCloseHelper.this.wait();
                }
            }
        }
    }


    protected class RequestFutureWrapper implements RequestFuture {

        private final RequestFuture rqFuture;
        private final Protocol protocol;

        public RequestFutureWrapper(
                final RequestFuture rqFuture,
                final Protocol protocol) {
            this.rqFuture = rqFuture;
            this.protocol = protocol;
        }

        @Override
        public boolean isCancelled() {
            return rqFuture.isCancelled();
        }

        @Override
        public Message getRequest() {
            return rqFuture.getRequest();
        }

        @Override
        public boolean isDone() {
            return rqFuture.isDone();
        }

        @Override
        public boolean cancel(final boolean mayInterruptIfRunning) {
            return rqFuture.cancel(mayInterruptIfRunning);
        }

        @Override
        public Message get() throws InterruptedException {
            return get(0);
        }

        @Override
        public Message get(final long timeout) throws InterruptedException {
            final Message ret = rqFuture.get(timeout, TimeUnit.MILLISECONDS);
            if (ret == null && !protocol.getState().equals(ChannelState.Opened)) {
                final InterruptedException exc = new InterruptedException();
                exc.initCause(new ChannelClosedOnRequestException(
                        "Channel has been closed while waiting for server response"));
                throw exc;
            }
            return ret;
        }

        @Override
        public Message get(final long timeout, final TimeUnit unit) throws InterruptedException {
            return get((timeout > 0) && (unit != null) ? unit.toMillis(timeout) : timeout);
        }

        @Override
        public boolean isCanceled() {
            return rqFuture.isCancelled();
        }

        @Override
        public Message getResponse() throws InterruptedException {
            return get();
        }

        @Override
        public Message getResponse(final long timeout) throws InterruptedException {
            return get(timeout);
        }
    }
}
