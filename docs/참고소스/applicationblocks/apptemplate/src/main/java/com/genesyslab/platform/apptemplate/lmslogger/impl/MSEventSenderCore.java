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
package com.genesyslab.platform.apptemplate.lmslogger.impl;

import com.genesyslab.platform.apptemplate.util.ConfigurationUtil;
import com.genesyslab.platform.apptemplate.lmslogger.CommonLmsEnum;
import com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger;
import com.genesyslab.platform.apptemplate.lmslogger.LmsLoggerFactory;
import com.genesyslab.platform.apptemplate.configuration.log.GAppLoggingOptions;
import com.genesyslab.platform.apptemplate.configuration.ConfigurationException;
import com.genesyslab.platform.apptemplate.configuration.ClientConfigurationHelper;
import com.genesyslab.platform.apptemplate.configuration.GApplicationConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGServerInfo;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGAppConnConfiguration;

import com.genesyslab.platform.commons.connection.impl.ContextUtil;
import com.genesyslab.platform.commons.timer.Scheduler;
import com.genesyslab.platform.commons.timer.TimerAction;
import com.genesyslab.platform.commons.timer.TimerActionTicket;
import com.genesyslab.platform.commons.timer.impl.SchedulerImpl;
import com.genesyslab.platform.standby.WSConfig;
import com.genesyslab.platform.standby.WSHandler;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.events.WSTriedUnsuccessfullyEvent;

import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.management.protocol.MessageServerProtocol;
import com.genesyslab.platform.management.protocol.messageserver.requests.RequestLogMessage;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.connection.ConnectionManager;
import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.threading.InvokerInfo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.LinkedList;
import java.util.EventObject;


/**
 * The anchor point for Genesys Message Server LMS events delivery subsystem.
 * <p/>
 * <i><b>Note:</b> This class is a part of PSDK internal LMS Events delivery mechanism.<br/>
 * It is not supposed for direct usage from applications.</i>
 */
class MSEventSenderCore {

    public static final String NAME = "MSAppender";
    public static final String TIMER_NAME = "MSAppenderTimer";

    private final MSEventSenderImpl theSenderImpl;

    private final MessageServerProtocol protocol;
    private final WarmStandby warmStandby;

    private final ThreadPoolExecutor executor;
    private final SchedulerImpl scheduler;

    private final AtomicReference<ChannelState> stateCtrl =
            new AtomicReference<ChannelState>();

    private static final String serverType = "MessageServer";
    private static final LmsEventLogger LOG = LmsLoggerFactory.getLogger(MSEventSenderCore.class);

    private class InvokerAndTimerImpl implements AsyncInvoker, Scheduler, InvokerInfo {

        private final AtomicLong invokeCounter = new AtomicLong();

        @Override
        public void invoke(Runnable target) {
            invokeCounter.incrementAndGet();
            executor.execute(target);
        }

        @Override
        public void dispose() {
        }

        @Override
        public TimerActionTicket schedule(long delay, TimerAction action) {
            return scheduler.schedule(delay, action);
        }

        @Override
        public TimerActionTicket schedule(long delay, long period, TimerAction action) {
            return scheduler.schedule(delay, period, action);
        }

        @Override
        public void shutdown() {
        }

        @Override
        public int getQueueMaxSize() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getQueueSize() {
            return executor.getQueue().size();
        }

        @Override
        public long getInvokesCount() {
            return invokeCounter.get();
        }

        @Override
        public long getInvokesRejected() {
            return 0;
        }
    }


    protected MSEventSenderCore(
            final MSEventSenderImpl senderImpl)
            throws ConfigurationException {
        this.theSenderImpl = senderImpl;

        ContextUtil.specifyLocalThreadConnFactoryName(ConnectionManager.OIO);
        try {
            this.protocol = createMessageServerProtocol();
            if (this.protocol == null) {
                throw new IllegalArgumentException("Failed to create MSProtocol");
            }

            final String threadName = theSenderImpl.getName();

            executor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    new DaemonThreadFactory("MSAppenderInvoker-" + threadName));

            scheduler = new SchedulerImpl(TIMER_NAME);

            protocol.setInvoker(new InvokerAndTimerImpl());

            protocol.addChannelListener(new MSChannelListener());


            WSConfig wsConfig = createWSConfig();

            if (wsConfig == null) {
                throw new IllegalArgumentException("Failed to extract MSEndpoint information");
            }

            this.warmStandby = new WarmStandby(protocol);
            this.warmStandby.setConfig(wsConfig);
            this.warmStandby.setHandler(new WSEventHandler());

        }
        finally {
            ContextUtil.specifyLocalThreadConnFactoryName(null);
        }
    }


    public static void handleQueue(
            final MSEventSenderImpl senderImpl,
            final BlockingDeque<RequestLogMessage> theQueue)
            throws ConfigurationException, InterruptedException {
        final MSEventSenderCore instance = new MSEventSenderCore(senderImpl);
        instance.processQueue(theQueue);
    }


    private WSConfig createWSConfig() throws ConfigurationException {
        WSConfig wsConfig = null;
        final LinkedList<Endpoint> res = new LinkedList<Endpoint>();

        final IGApplicationConfiguration appConfig = theSenderImpl.getAppConfig();
        if (appConfig != null) {
            final List<IGAppConnConfiguration> servers =
                    GApplicationConfiguration.getAppServers(
                            appConfig.getAppServers(), CfgAppType.CFGMessageServer);
            if (servers != null && servers.size() > 0) {
                // In case of one MessageServer application in the Connections section,
                // WarmStandby is configured by this connection:
                if (servers.size() == 1) {
                    wsConfig = ClientConfigurationHelper
                            .createWarmStandbyConfigEx(appConfig, servers.get(0));
                }

                // We have more than one MesageServer applications in the Connections section,
                // then we collect all Message Servers Endpoints and create default WSConfig with this list:
                if (wsConfig == null) {
                    for (IGAppConnConfiguration conn: servers) {
                        final IGApplicationConfiguration tgtApp = conn.getTargetServerConfiguration();
                        if (tgtApp != null) {
                            Endpoint endpoint = ClientConfigurationHelper
                                    .createEndpoint(appConfig, conn, tgtApp);
                            if (endpoint != null) {
                                res.add(endpoint);
                            }

                            final IGServerInfo srvInfo = tgtApp.getServerInfo();
                            if (srvInfo != null) {
                                final IGApplicationConfiguration tgtBackup = srvInfo.getBackup();
                                if (tgtBackup != null) {
                                    endpoint = ClientConfigurationHelper
                                            .createEndpoint(appConfig, conn, tgtBackup);
                                    if (endpoint != null) {
                                        res.add(endpoint);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (wsConfig == null) {
            if (res.size() == 0) {
                final String host = theSenderImpl.getHost();
                if (host != null && !host.isEmpty()) {
                    res.add(new Endpoint("MS-" + theSenderImpl.getName(),
                            host, theSenderImpl.getPort()));
                }
                final String bkHost = theSenderImpl.getBackupHost();
                if (bkHost != null && !bkHost.isEmpty()) {
                    res.add(new Endpoint("MS-" + theSenderImpl.getName() + "-backup",
                            bkHost, theSenderImpl.getBackupPort()));
                }
            }
            if (res.size() > 0) {
                wsConfig = new WSConfig();
                wsConfig.setEndpoints(res);
            }
        }
        return wsConfig;
    }

    private MessageServerProtocol createMessageServerProtocol() {
        final MessageServerProtocol protocol = new MessageServerProtocol();

        final IGApplicationConfiguration appConfig = theSenderImpl.getAppConfig();
        if (appConfig != null) {
            protocol.setClientName(appConfig.getApplicationName());

            final Integer appDbid = appConfig.getDbid();
            if (appDbid != null) {
                protocol.setClientId(appDbid);
            } else {
                protocol.setClientId(Integer.valueOf(0));
            }

            final String clientHost = new GAppLoggingOptions(appConfig, null)
                    .getEventlogHost();
            if (clientHost != null && !clientHost.isEmpty()) {
                protocol.setClientHost(clientHost);
            } else {
                protocol.setClientHost(ConfigurationUtil.getLocalhostName());
            }

            final CfgAppType appType = appConfig.getApplicationType();
            if (appType != null) {
                protocol.setClientType(appType.ordinal());
            } else {
                protocol.setClientType(0);
            }
        } else {
            final Map<String, String> properties = theSenderImpl.getConnProperties();
            if (properties != null) {
                final String clientName = properties.get("ClientName");
                if (clientName == null || clientName.isEmpty()) {
                    throw new IllegalArgumentException("No ClientName for MSEventSender");
                }
                protocol.setClientName(clientName);

                Integer clientDbid = null;
                final String clientId = properties.get("ClientId");
                if (clientName == null || clientName.isEmpty()) {
                    try {
                        clientDbid = Integer.parseInt(clientId);
                    } catch (final Exception ex) {
                        //LOGGER.warn("Error in MSEventSender properties", ex);
                        clientDbid = 0;
                    }
                }
                if (clientDbid == null) {
                    clientDbid = 0;
                }
                protocol.setClientId(clientDbid);

                final String clientHost = properties.get("ClientHost");
                if (clientHost != null && !clientHost.isEmpty()) {
                    protocol.setClientHost(clientHost);
                } else {
                    protocol.setClientHost(ConfigurationUtil.getLocalhostName());
                }

                int clType = -1;
                final String clientType = properties.get("ClientType");
                if (clientType != null && !clientType.isEmpty()) {
                    try {
                        clType = Integer.parseInt(clientType);
                    } catch (final Exception ex) {
                        String appTypeName = clientType.toLowerCase(Locale.ENGLISH);
                        if (!appTypeName.startsWith("cfg")) {
                            appTypeName = "cfg" + appTypeName;
                        }
                        final CfgAppType appType = CfgAppType.valueOf(appTypeName);
                        if (appType != null) {
                            clType = appType.ordinal();
                        }
                    }
                }
                if (clType < 0) {
                    clType = 0;
                }
                protocol.setClientType(clType);
            } else {
                throw new IllegalArgumentException("No properties given for MSEventSender");
            }
        }

        return protocol;
    }


    protected void processQueue(
            final BlockingDeque<RequestLogMessage> theQueue)
            throws InterruptedException {
        warmStandby.autoRestore(false);
        warmStandby.openAsync();
        try {
            final RequestLogMessage interruptMarker = RequestLogMessage.create();
            boolean isInterrupted = false;
            while (!isInterrupted) {
                synchronized (stateCtrl) {
                    while (!ChannelState.Opened.equals(stateCtrl.get())
                            && !isInterrupted) {
                        stateCtrl.wait();
                    }
                }
                RequestLogMessage msg;
                while (!isInterrupted || !theQueue.isEmpty()) {
                    try {
                        msg = theQueue.take();
                    } catch (final InterruptedException ex) {
                        if (isInterrupted) {
                            // second interruption stops sending
                            throw ex;
                        }
                        isInterrupted = true;
                        if (theQueue.isEmpty()) {
                            // sending interrupted with empty events queue
                            return;
                        }
                        theQueue.offer(interruptMarker);
                        msg = theQueue.take();
                    }
                    if (msg == interruptMarker) {
                        // drop events arrived after interruption (if any):
                        return;
                    } else if (msg != null) {
                        try {
                            protocol.send(msg);
                            msg = null;
                        } catch (final ProtocolException ex) {
                            if (ChannelState.Opened.equals(protocol.getState())) {
                                // something is wrong with this message
                                // TODO check cases
                                msg = null; // - drop it
                            } else {
                                // connection to Message Server lost -
                                //   goto external cycle to wait for connection open:
                                break;
                            }
                        } catch (final IllegalStateException ex) {
                            // connection to Message Server lost -
                            //   goto external cycle to wait for connection open:
                            break;
                        } finally {
                            if (msg != null) {
                                theQueue.offerFirst(msg);
                                break;
                            }
                        }
                    }
                }
            }
            //} catch (final InterruptedException ex) {
            //    if (!theQueue.isEmpty()) {
            //         // TODO we have unsent messages... dropping it for now...
            //         LOGGER.warn("Exiting MSEventSenderCore with "
            //                + theQueue.size() + " unsent MessageServer events");
            //    }
        } finally {
            warmStandby.close();
            executor.shutdown();
            scheduler.shutdown();
        }
    }


    protected class MSChannelListener implements ChannelListener {

        @Override
        public void onChannelOpened(final EventObject event) {
            synchronized (stateCtrl) {
                stateCtrl.set(ChannelState.Opened);
                stateCtrl.notify();
            }
            final Endpoint ep = protocol.getEndpoint();
            LOG.log(CommonLmsEnum.GCTI_CLIENT_CONNECTED,
                    serverType, theSenderImpl.getServerName(),
                    ep.getHost(), ep.getPort());
        }

        @Override
        public void onChannelClosed(final ChannelClosedEvent event) {
            final Endpoint ep = protocol.getEndpoint();
            synchronized (stateCtrl) {
                stateCtrl.set(ChannelState.Closed);
                stateCtrl.notify();
            }
            final Throwable cause = (event != null) ? event.getCause() : null;
            final String msg = (cause != null) ? cause.toString() : "Unknown cause";
            if (cause != null) {
                LOG.log(CommonLmsEnum.GCTI_CLIENT_CONNECTION_LOST,
                        serverType, theSenderImpl.getServerName(),
                        ep.getHost(), ep.getPort(), msg, cause);
            } else {
                LOG.log(CommonLmsEnum.GCTI_CLIENT_DISCONNECTED,
                        serverType, theSenderImpl.getServerName());
            }
        }

        @Override
        public void onChannelError(final ChannelErrorEvent event) { /* nothing to do */ }
    }

    protected class WSEventHandler extends WSHandler {

        @Override
        public void onEndpointTriedUnsuccessfully(final WSTriedUnsuccessfullyEvent event) {
            final Endpoint ep = protocol.getEndpoint();
            final String cause = event.getCause() != null ? event.getCause().getMessage() : "Unknown cause";
            LOG.log(CommonLmsEnum.GCTI_CLIENT_UNABLE_CONNECT,
                    serverType, theSenderImpl.getServerName(),
                    ep.getHost(), ep.getPort(), cause, event.getCause());
        }
    }

    protected class DaemonThreadFactory implements ThreadFactory {

        private final String name;

        protected DaemonThreadFactory(final String name) {
            this.name = name;
        }

        @Override
        public Thread newThread(final Runnable r) {
            final Thread thr = new Thread(null, r, name);
            thr.setDaemon(true);
            return thr;
        }
    }
}
