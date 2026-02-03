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

import com.genesyslab.platform.standby.WarmStandby;

import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.MessageReceiverSupport;
import com.genesyslab.platform.commons.protocol.OutputChannelListener;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.ProtocolDescription;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.ReferenceBuilder;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.commons.protocol.RequestFuture;

import com.genesyslab.platform.commons.connection.interceptor.Interceptor;
import com.genesyslab.platform.commons.connection.configuration.ConnectionConfiguration;

import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.threading.CompletionHandler;
import com.genesyslab.platform.commons.timer.TimerAction;
import com.genesyslab.platform.commons.timer.TimerActionTicket;
import com.genesyslab.platform.commons.timer.TimerFactory;


/**
 * Cluster Protocol internal supplemental class for holding Cluster Node connection.
 */
class ProtocolNodeRec {

    private final WarmStandby warmstandby;
    private final InnerProtocolInstanceWrapper protocolWrapper;

    private final Object sync = new Object();
    private volatile long lastRequestTimestamp = 0;
    private volatile TimerActionTicket drainCloseTicket = null;


    ProtocolNodeRec(
            final WarmStandby warmstandby) {
        this.warmstandby = warmstandby;
        this.protocolWrapper = new InnerProtocolInstanceWrapper(
                warmstandby.getChannel());
    }

    public Protocol protocolNode() {
        return protocolWrapper;
    }

    public WarmStandby warmstandby() {
        return warmstandby;
    }

    /**
     * Checks value of the node last request message time stamp,
     * and starts or schedules delayed closing of the connection.<br/>
     * It's to ensure that the connection stay open for the protocol timeout period
     * after last user request.
     * <p/>
     * Note: Cluster Protocol excludes this node from load balancers' list
     * before calling this method. So, no further messages are going to be sent
     * to this node.
     */
    void drain() {
        synchronized (sync) {
            if (drainCloseTicket == null) {
                long timeout = protocolWrapper.getTimeout();
                if (timeout <= 0) {
                    timeout = Protocol.DEFAULT_TIMEOUT;
                }
                long closeDelay = 0;
                if ((lastRequestTimestamp != 0) && (timeout > 0)) {
                    final long diff = System.currentTimeMillis() - lastRequestTimestamp;
                    if (diff < timeout) {
                        closeDelay = timeout - diff;
                    }
                }
                if (closeDelay > 0) {
                    drainCloseTicket = TimerFactory.getTimer().schedule(
                            closeDelay, new TimerAction() {
                                @Override
                                public void onTimer() {
                                    warmstandby.closeAsync();
                                }
                                @Override
                                public String toString() {
                                    return "Cluster Node graceful close for "
                                            + warmstandby.getChannel().getEndpoint();
                                }});
                } else {
                    warmstandby.closeAsync();
                }
            }
        }
    }

    <A> void closeAsync(
            final CompletionHandler<Void, A> handler,
            final A attachment) {
        synchronized (sync) {
            if (drainCloseTicket != null) {
                drainCloseTicket.cancel();
            }
            warmstandby.closeAsync(handler, attachment);
        }
    }


    /**
     * Cluster node protocol wrapping helper to be exposed as Cluster Protocol Node.<br/>
     * It is to hide/block some usual PSDK protocols functionality from direct access
     * bypassing Cluster Protocol container.<br/>
     * Its to protect Cluster Protocol consistency and stability.
     */
    protected class InnerProtocolInstanceWrapper
            implements Protocol {

        protected final Protocol protocol;

        public InnerProtocolInstanceWrapper(
                final Protocol protocol) {
            this.protocol = protocol;
        }


        @Override
        public void setTimeout(final long timeout) {
            protocol.setTimeout(timeout);
        }

        @Override
        public long getTimeout() {
            return protocol.getTimeout();
        }


        @Override
        public void setMessageHandler(
                final MessageHandler msgHandler) {
            throw new UnsupportedOperationException(
                    "Cluster inner protocols events may be received on the compound ClusterProtocol instance");
        }

        @Override
        public void open()
                throws RegistrationException, ProtocolException,
                        IllegalStateException, InterruptedException {
            open(getTimeout());
        }

        @Override
        public void close()
                throws ProtocolException, IllegalStateException,
                        InterruptedException {
            close(getTimeout());
        }

        @Override
        public void open(final long timeout)
                throws RegistrationException, ProtocolException,
                        IllegalStateException, InterruptedException {
            throw new UnsupportedOperationException(
                    "Cluster Protocol Node lifecycle is controlled by the container");
        }

        @Override
        public void close(final long timeout)
                throws ProtocolException, IllegalStateException,
                        InterruptedException {
            throw new UnsupportedOperationException(
                    "Cluster Protocol Node lifecycle is controlled by the container");
        }

        @Override
        public void beginOpen() throws ProtocolException {
            throw new UnsupportedOperationException(
                    "Cluster Protocol Node lifecycle is controlled by the container");
        }

        @Override
        public void beginClose() {
            throw new UnsupportedOperationException(
                    "Cluster Protocol Node lifecycle is controlled by the container");
        }

        @Override
        public ChannelState getState() {
            return protocol.getState();
        }

        @Override
        public void addChannelListener(
                final ChannelListener listener) {
            throw new UnsupportedOperationException(
                    "Cluster inner protocols events may be received on the compound ClusterProtocol instance");
        }

        @Override
        public void removeChannelListener(ChannelListener listener) {
            throw new UnsupportedOperationException(
                    "Cluster inner protocols events may be received on the compound ClusterProtocol instance");
        }


        @Override
        public Object receive() throws InterruptedException, IllegalStateException {
            return protocol.receive();
        }

        @Override
        public Object receive(final long timeout)
                throws InterruptedException, IllegalStateException {
            return protocol.receive(timeout);
        }

        @Override
        public void clearInput() {
            protocol.clearInput();
        }

        @Override
        public int getInputSize() {
            return protocol.getInputSize();
        }

        @Override
        public void setInputSize(final int inputSize) {
            protocol.setInputSize(inputSize);
        }

        @Override
        public void releaseReceivers() {
            protocol.releaseReceivers();
        }

        @Override
        public void send(final Message message)
                throws ProtocolException {
            lastRequestTimestamp = System.currentTimeMillis();
            protocol.send(message);
        }

        @Override
        public void addListener(final OutputChannelListener listener) {
            throw new UnsupportedOperationException(
                    "Cluster inner protocols events may be received on the compound ClusterProtocol instance");
        }

        @Override
        public void removeListener(final OutputChannelListener listener) {
            throw new UnsupportedOperationException(
                    "Cluster inner protocols events may be received on the compound ClusterProtocol instance");
        }

        @Override
        public Message request(final Message message)
                throws ProtocolException, IllegalStateException {
            lastRequestTimestamp = System.currentTimeMillis();
            return protocol.request(message);
        }

        @Override
        public Message request(final Message message, final long timeout)
                throws ProtocolException, IllegalStateException {
            lastRequestTimestamp = System.currentTimeMillis();
            return protocol.request(message, timeout);
        }

        @Override
        public RequestFuture beginRequest(final Message message)
                throws ProtocolException, IllegalStateException {
            lastRequestTimestamp = System.currentTimeMillis();
            return protocol.beginRequest(message);
        }

        @Override
        public <A> void requestAsync(
                final Message message,
                final A attachment,
                final CompletionHandler<Message, ? super A> handler)
                        throws ProtocolException, IllegalStateException {
            lastRequestTimestamp = System.currentTimeMillis();
            protocol.requestAsync(message, attachment, handler);
        }

        @Override
        public <A> void requestAsync(
                final Message message,
                final A attachment,
                final CompletionHandler<Message, ? super A> handler,
                final long timeout)
                        throws ProtocolException, IllegalStateException {
            lastRequestTimestamp = System.currentTimeMillis();
            protocol.requestAsync(message, attachment, handler, timeout);
        }

        @Override
        public Message endRequest(final RequestFuture future)
                throws ProtocolException {
            return protocol.endRequest(future);
        }

        @Override
        public Message endRequest(
                final RequestFuture future,
                final long timeout)
                        throws ProtocolException {
            return protocol.endRequest(future, timeout);
        }

        @Override
        public boolean getCopyResponse() {
            return protocol.getCopyResponse();
        }

        @Override
        public void setCopyResponse(final boolean copyResponse) {
            protocol.setCopyResponse(copyResponse);
        }

        @Override
        public Interceptor getInterceptor() {
            return protocol.getInterceptor();
        }

        @Override
        public void setReceiver(final MessageReceiverSupport receiver) {
            throw new UnsupportedOperationException(
                    "Cluster inner protocols events may be received on the compound ClusterProtocol instance");
        }

        @Override
        public void resetReceiver() {
            protocol.resetReceiver();
        }

        @Override
        public ConnectionConfiguration getConfiguration() {
            return protocol.getConfiguration();
        }

        @Override
        public void configure(final ConnectionConfiguration config) {
            protocol.configure(config);
        }

        @Override
        public Endpoint getEndpoint() {
            return protocol.getEndpoint();
        }

        @Override
        public void setEndpoint(final Endpoint endpoint) {
            throw new UnsupportedOperationException(
                    "Cluster Protocol Node Endpoint is controlled by the container");
        }

        @Override
        public ProtocolDescription getProtocolDescription() {
            return protocol.getProtocolDescription();
        }

        @Override
        public void setInvoker(final AsyncInvoker invoker) {
        }

        @Override
        public void setConnectionInvoker(
                final AsyncInvoker connectionInvoker) {
        }

        @Override
        public int getProtocolId() {
            return protocol.getProtocolId();
        }

        @Override
        public ReferenceBuilder getReferenceBuilder() {
            return protocol.getReferenceBuilder();
        }

        @Override
        public String toString() {
            return "ClusterProtocolNode(" + warmstandby.getName()
                    + ": " + protocol.getEndpoint() + ")";
        }
    }
}
