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

import com.genesyslab.platform.commons.protocol.*;
import com.genesyslab.platform.standby.WSHandler;
import com.genesyslab.platform.standby.events.WSOpenedEvent;
import com.genesyslab.platform.standby.events.WSDisconnectedEvent;
import com.genesyslab.platform.standby.events.WSTriedUnsuccessfullyEvent;

import com.genesyslab.platform.commons.connection.interceptor.Interceptor;
import com.genesyslab.platform.commons.connection.interceptor.NoInterceptorImpl;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import java.util.EventObject;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Abstract Cluster Protocol channel methods.<br/>
 * It's a common functionality for all Cluster Protocols.
 *
 * @param <P> the base client protocol type.
 */
public abstract class AbstractClusterChannel
                <P extends ClientChannel>
        implements InputChannel, OutputChannel, RequestChannel,
                MessageReceiverManagement {

    protected Endpoint endpoint;

    private long timeout = DEFAULT_TIMEOUT;

    protected ThreadPoolExecutor execService;

    private final ListenerHelper channelListenerHelper = new ListenerHelper();
    private final ListenerHelper outputChannelListenerHelper = new ListenerHelper();

    private MessageHandler messageHandler = null;
    private Boolean copyResponse = null;

    private final ChannelListener channelListener =
            new IntChannelListener();
    private final OutputChannelListener outputChannelListener =
            new IntOutputChannelListener();

    private final static Interceptor NO_INTERCEPTOR = new NoInterceptorImpl();

    private final static ILogger log = Log.getLogger(AbstractClusterChannel.class);

    protected final int protocolId;

    private static AtomicInteger clusterCounter = new AtomicInteger();
    private static final int clusterNumber = clusterCounter.incrementAndGet();

    protected AbstractClusterChannel() {
        this.protocolId = AbstractChannel.generateChannelId();

        ThreadFactory threadFactory = new ThreadFactory() {

            @Override
            public Thread newThread(final Runnable r) {
                final Thread thread = new Thread(r);
                thread.setName(getClass().getSimpleName() + "-" + clusterNumber + " protocolId=" + protocolId);
                thread.setDaemon(true);
                return thread;
            }
        };

        execService = new ThreadPoolExecutor(0, 1, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), threadFactory);

    }

    void activateExecutor() {
        execService.setCorePoolSize(1);
        execService.prestartCoreThread();
    }

    void deactivateExecutor() {
        execService.setCorePoolSize(0);
    }



    @Override
    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }


    @Override
    public void setMessageHandler(
            final MessageHandler msgHandler) {
        throwNotClosed();
        this.messageHandler = msgHandler;
    }

    @Override
    public boolean getCopyResponse() {
        return (copyResponse != null) && copyResponse.booleanValue();
    }

    @Override
    public void setCopyResponse(final boolean copyResponse) {
        throwNotClosed();
        this.copyResponse = copyResponse;
    }


    protected abstract P createProtocol();


    protected void setupProtocolNode(final ProtocolNodeRec protocolItem) {
        final Protocol protocol = protocolItem.warmstandby().getChannel();
        protocol.setTimeout(timeout);

        protocol.addChannelListener(channelListener);
        protocol.addListener(outputChannelListener);
        protocol.setMessageHandler(new IntMessageHandler(protocol));

        if (copyResponse != null) {
            protocol.setCopyResponse(copyResponse);
        }

        protocolItem.warmstandby().setHandler(new IntWsHandler(protocolItem));
        protocolItem.warmstandby().autoRestore(false);
    }


    /**
     * Initiates connections to the initialized set of cluster nodes.<br/>
     * Cluster Protocol will become <code>Opened</code> when at least one node is connected.
     *
     * @throws IllegalStateException if protocol is not <code>Closed</code>,
     *     or it has no set nodes configurations or Endpoint's.
     * @throws ProtocolTimeoutException if cluster protocol got no notification about
     *     node connection in the protocol timeout frame. Note: it does not fail the open request,
     *     protocol is <code>Opening</code> and WarmStandby services continue to run re-connections.
     */
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
    public void addChannelListener(final ChannelListener listener) {
        channelListenerHelper.addListener(listener);
    }

    @Override
    public void removeChannelListener(final ChannelListener listener) {
        channelListenerHelper.removeListener(listener);
    }

    @Override
    public void addListener(final OutputChannelListener listener) {
        outputChannelListenerHelper.addListener(listener);
    }

    @Override
    public void removeListener(final OutputChannelListener listener) {
        outputChannelListenerHelper.removeListener(listener);
    }


    /**
     * This deprecated method returns <code>null</code>.
     */
    @Override
    @Deprecated
    public Message receive()
            throws InterruptedException, IllegalStateException {
        return null;
    }

    /**
     * This deprecated method returns <code>null</code>.
     */
    @Override
    @Deprecated
    public Message receive(final long timeout)
            throws InterruptedException, IllegalStateException {
        return null;
    }


    /**
     * This deprecated method does nothing.
     */
    @Override
    @Deprecated
    public void clearInput() {
    }

    /**
     * This deprecated method returns zero.
     */
    @Override
    @Deprecated
    public int getInputSize() {
        return 0;
    }

    /**
     * This deprecated method does nothing.
     */
    @Override
    @Deprecated
    public void setInputSize(final int inputSize) {
    }

    /**
     * This deprecated method does nothing.
     */
    @Override
    @Deprecated
    public void releaseReceivers() {
    }


    protected abstract void onChannelOpened(
            final ProtocolNodeRec protocolItem,
            final WSOpenedEvent event);

    protected void fireOpenedEvent(final EventObject event) {
        log.debug("Firing 'onChannelOpened'");
        execService.execute(new ChannelOpenVisitor(event));
    }


    protected void fireOpenedInternalChannelEvent(final ChannelOpenedEvent event) {
        log.debug("Firing 'onInternalChannelOpened'");
        execService.execute(new InternalChannelOpenVisitor(event));
    }

    protected void fireClosedInternalChannelEvent(final ChannelClosedEvent event) {
        log.debug("Firing 'onInternalChannelClosed'");
        execService.execute(new InternalChannelCloseVisitor(event));
    }


    protected abstract void onChannelDisconnected(
            final ProtocolNodeRec protocolItem,
            final WSDisconnectedEvent event);

    protected void fireClosedEvent(final ChannelClosedEvent event) {
        log.debug("Firing 'onChannelClosed'");
        execService.execute(new ChannelClosedVisitor(event));
    }


    protected abstract void onChannelFailure(
            final ProtocolNodeRec protocolItem,
            final WSTriedUnsuccessfullyEvent event);


    protected void fireOnMessageRecv(
            final InputChannel  channel,
            final Message       message) {
        final MessageHandler mh = messageHandler;
        if (mh != null) {
            execService.execute(new IncomingMessageVisitor(message, mh));
        }
    }


    protected void fireOnMessageSend(
            final OutputChannel channel,
            final Message       message) {
        execService.execute(new OutgoingMessageVisitor(channel, message));
    }


    protected <E extends Throwable> E fireErrorEvent(final E thrown) {
        throwNull(thrown, "Throwable");
        fireErrorEvent(new ChannelErrorEvent(AbstractClusterChannel.this, thrown));
        return thrown;
    }

    protected void fireErrorEvent(final ChannelErrorEvent event) {
        throwNull(event, "ChannelErrorEvent");
        execService.execute(new ChanelErrorVisitor(event));
    }


    /**
     * Returns singleton instance of default implementation.
     */
    public Interceptor getInterceptor() {
        return NO_INTERCEPTOR;
    }


    /**
     * This deprecated method throws {@link UnsupportedOperationException}.
     */
    @Override
    @Deprecated
    public void setReceiver(final MessageReceiverSupport receiver) {
        throw new UnsupportedOperationException(
                "MessageReceiver is not supported by the protocol type");
    }


    /**
     * This deprecated method does nothing.
     */
    @Override
    @Deprecated
    public void resetReceiver() {
    }


    protected void throwNull(final Object obj, final String name) {
        if (obj == null) {
            throw new IllegalArgumentException(
                    "Null pointer: " + name);
        }
    }

    protected void throwNotClosed() throws ChannelNotClosedException {
        if (getState() != ChannelState.Closed) {
            throw new ChannelNotClosedException("Operation is not allowed on non closed channel");
        }
    }

    protected void throwNotOpened() throws ChannelNotOpenedException {
        if (getState() != ChannelState.Opened) {
            throw new ChannelNotOpenedException("Connection is not opened");
        }
    }

    protected void throwOnClosed() throws ChannelNotOpenedException {
        final ChannelState state = getState();
        if (state.equals(ChannelState.Closed)
                || state.equals(ChannelState.Closing)) {
            throw new ChannelNotOpenedException("Protocol closed");
        }
    }

    protected class IntWsHandler extends WSHandler {

        protected final ProtocolNodeRec protocolItem;

        public IntWsHandler(
                final ProtocolNodeRec protocolItem) {
            this.protocolItem = protocolItem;
        }

        @Override
        public void onChannelOpened(final WSOpenedEvent event) {
            AbstractClusterChannel.this.onChannelOpened(
                    protocolItem, event);
        }

        @Override
        public void onChannelDisconnected(final WSDisconnectedEvent event) {
            AbstractClusterChannel.this.onChannelDisconnected(
                    protocolItem, event);
        }

        @Override
        public void onEndpointTriedUnsuccessfully(
                final WSTriedUnsuccessfullyEvent event) {
            AbstractClusterChannel.this.onChannelFailure(
                    protocolItem, event);
        }
    }

    protected class IntMessageHandler implements MessageHandler {

        protected final InputChannel channel;

        public IntMessageHandler(final InputChannel channel) {
            this.channel = channel;
        }

        @Override
        public void onMessage(final Message message) {
            fireOnMessageRecv(channel, message);
        }
    }

    protected class IntChannelListener implements ChannelListener {

        @Override
        public void onChannelOpened(final EventObject event) {
            // Nothing to do - this event is handled by WarmStandby
        }

        @Override
        public void onChannelClosed(final ChannelClosedEvent event) {
            // Nothing to do - this event is handled by WarmStandby
        }

        @Override
        public void onChannelError(final ChannelErrorEvent event) {
            fireErrorEvent(event);
        }
    }

    protected class IntOutputChannelListener implements OutputChannelListener {

        @Override
        public void onSend(final OutputChannel channel, final Message message) {
            fireOnMessageSend(channel, message);
        }
    }


    protected abstract class AsyncVisitor
            implements ListenerHelper.NotificationVisitor, Runnable {

        protected final Runnable notification;

        protected AsyncVisitor(
                final ListenerHelper listenerHelper) {
            notification = listenerHelper.createNotificationTask(this);
        }

        @Override
        public void run() {
            notification.run();
        }
    }

    protected class InternalChannelOpenVisitor extends AsyncVisitor {

        private final EventObject event;

        public InternalChannelOpenVisitor(final EventObject event) {
            super(channelListenerHelper);
            this.event = event;
        }

        public void visitListener(final Listener listener) {
            if (listener instanceof ClusterChannelListener) {
                ((ClusterChannelListener) listener).onInternalChannelOpened((ChannelOpenedEvent) event);
            }
        }
    }

    protected class InternalChannelCloseVisitor extends AsyncVisitor {

        private final EventObject event;

        public InternalChannelCloseVisitor(final EventObject event) {
            super(channelListenerHelper);
            this.event = event;
        }

        public void visitListener(final Listener listener) {
            if (listener instanceof ClusterChannelListener) {
                ((ClusterChannelListener) listener).onInternalChannelClosed((ChannelClosedEvent) event);
            }
        }
    }


    protected class ChannelOpenVisitor extends AsyncVisitor {

        private final EventObject event;

        public ChannelOpenVisitor(final EventObject event) {
            super(channelListenerHelper);
            this.event = event;
        }

        public void visitListener(final Listener listener) {
            ((ChannelListener) listener).onChannelOpened(event);
        }
    }

    protected class ChannelClosedVisitor extends AsyncVisitor {

        private final ChannelClosedEvent event;

        public ChannelClosedVisitor(final ChannelClosedEvent event) {
            super(channelListenerHelper);
            this.event = event;
        }

        public void visitListener(final Listener listener) {
            ((ChannelListener) listener).onChannelClosed(event);
        }
    }

    protected class ChanelErrorVisitor extends AsyncVisitor {

        private final ChannelErrorEvent event;

        public ChanelErrorVisitor(final ChannelErrorEvent event) {
            super(channelListenerHelper);
            this.event = event;
        }

        public void visitListener(final Listener listener) {
            ((ChannelListener) listener).onChannelError(event);
        }
    }


    protected class IncomingMessageVisitor implements Runnable {

        private final Message message;
        private final MessageHandler messageHandler;

        public IncomingMessageVisitor(
                final Message message,
                final MessageHandler handler) {
            this.message = message;
            this.messageHandler = handler;
        }

        @Override
        public void run() {
            messageHandler.onMessage(message);
        }
    }

    protected class OutgoingMessageVisitor extends AsyncVisitor {

        private final OutputChannel channel;
        private final Message message;

        public OutgoingMessageVisitor(
                final OutputChannel channel,
                final Message message) {
            super(outputChannelListenerHelper);
            this.channel = channel;
            this.message = message;
        }

        public void visitListener(final Listener listener) {
            ((OutputChannelListener) listener).onSend(channel, message);
        }
    }
}
