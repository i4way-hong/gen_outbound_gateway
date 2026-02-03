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
package com.genesyslab.platform.apptemplate.application;

import com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger;
import com.genesyslab.platform.apptemplate.lmslogger.LmsLoggerFactory;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;
import com.genesyslab.platform.apptemplate.application.GFAppCfgEvent.AppCfgEventType;
import com.genesyslab.platform.apptemplate.configuration.ConfigurationException;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;
import com.genesyslab.platform.apptemplate.configuration.GCOMApplicationConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGHost;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGServerInfo;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGTenantInfo;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGAppConnConfiguration;

import com.genesyslab.platform.applicationblocks.com.ConfEvent;
import com.genesyslab.platform.applicationblocks.com.ICfgObject;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfEvent.EventType;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;

import com.genesyslab.platform.applicationblocks.commons.Action;
import com.genesyslab.platform.applicationblocks.commons.Predicate;
import com.genesyslab.platform.applicationblocks.commons.broker.Subscriber;
import com.genesyslab.platform.applicationblocks.commons.broker.SubscriptionService;

import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestRegisterNotification;

import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.RegistrationException;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.threading.SingleThreadInvoker;

import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.io.Closeable;
import java.util.List;
import java.util.Iterator;
import java.util.EventObject;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * The Application Configuration Management component facade class.
 * <p/>
 * It monitors given application configuration and configuration
 * of its connected applications (servers), and notifies the application about changes
 * of their properties in the Configuration Server.
 * <p/>
 * The application configuration manager instance may be created by using the builder -
 * {@link #newBuilder()}.
 */
public class GFApplicationConfigurationManager
        implements SubscriptionService<GFAppCfgEvent>, Closeable {

    private final String applicationName;
    private final IConfService confService;

    private final boolean doCSSubscribe;
    private final boolean readTenantInfo;
    private final boolean doCSDispose;

    protected GFApplicationContextImpl appContext;

    public enum ManagerState {
        Created,
        Connected,
        Initialized,
        Disconnected,
        Failed,
        Disposed;
    }

    private volatile ManagerState state = ManagerState.Created;
    private volatile Throwable failureCause = null;
    private final Lock stateLock = new ReentrantLock(true);
    private final Condition stateChanged = stateLock.newCondition();

    private final List<Subscriber<GFAppCfgEvent>> subscribers =
            new CopyOnWriteArrayList<Subscriber<GFAppCfgEvent>>();

    protected GCOMApplicationConfiguration appConfig = null;

    protected LmsMessageConveyor lmsMessageConveyor = null;
    protected LmsLoggerFactory lmsLoggerFactory = null;
    protected LmsEventLogger lmsLogger = null;

    private static final ILogger log = Log.getLogger(GFApplicationConfigurationManager.class);

    private final ConfServerChannelListenerImpl confChannelListener = new ConfServerChannelListenerImpl();
    private final ConfEventHandler confEventHandler = new ConfEventHandler();

    private final SingleThreadInvoker invoker;

    protected WarmStandby theWarmStandby = null;


    /**
     * The protected constructor is to be used from inside of the builder infrastructure.
     *
     * @param appName name of the application to manage its configuration.
     * @param confService the configuration service reference.
     * @param warmStandby the WarmStandbyService created on the <b>confService</b>s protocol instance.
     * @see #newBuilder()
     */
    protected GFApplicationConfigurationManager(
            final String appName,
            final IConfService confService,
            final WarmStandby warmStandby,
            final boolean doCSSubscribe,
            final boolean readTenantInfo,
            final boolean doCSDispose) {
        this.applicationName = appName;
        this.confService = confService;
        this.doCSSubscribe = doCSSubscribe;
        this.readTenantInfo = readTenantInfo;
        this.doCSDispose = doCSDispose;

        this.theWarmStandby = warmStandby;

        this.invoker = new SingleThreadInvoker("AppManager-" + applicationName);
    }

    protected void initLmsFactory(
            final LmsMessageConveyor lmsMessages) {
        final LmsLoggerFactory lf = LmsLoggerFactory.getLoggerFactory();
        if (lf == null) {
            if (lmsMessages != null) {
                this.lmsMessageConveyor = lmsMessages;
            } else {
                this.lmsMessageConveyor = new LmsMessageConveyor();
            }
            this.lmsLoggerFactory = LmsLoggerFactory.createInstance(
                    this.lmsMessageConveyor);
        } else {
            if (lmsMessages != null) {
                this.lmsMessageConveyor = lmsMessages;
                if (lf.getMessageConveyor() == lmsMessages) {
                    this.lmsLoggerFactory = lf;
                } else {
                    this.lmsLoggerFactory = LmsLoggerFactory.createInstance(
                            this.lmsMessageConveyor);
                }
            } else {
                this.lmsMessageConveyor = lf.getMessageConveyor();
                this.lmsLoggerFactory = lf;
            }
        }

        this.lmsLogger = this.lmsLoggerFactory.getLmsLogger(applicationName);
    }


    /**
     * Provides builder for application configuration manager instance.<br/>
     * Look at the builder documentation for the details on its usage - {@link ManagerBuilder}.
     *
     * @return new instance of the manager builder.
     */
    public static ManagerBuilder newBuilder() {
        return new ManagerBuilder();
    }


    public ManagerState getState() {
        return state; // sync?
    }

    public String getAppName() {
        return applicationName;
    }

    public GFApplicationContext getAppContext() {
        return appContext;
    }


    public IGApplicationConfiguration getAppConfiguration()
            throws ConfigurationException, IllegalStateException,
                InterruptedException {
        stateLock.lock();
        try {
            while (true) {
                switch (state) {
                case Created:
                    throw new IllegalStateException("AppConfigManager is not initialized");
                case Initialized:
                    return appContext.getConfiguration();
                case Disconnected:
                    final IGApplicationConfiguration cfg = appContext.getConfiguration();
                    if (cfg != null) {
                        return cfg;
                    }
                    throw new IllegalStateException("AppConfigManager is disconnected");
                case Connected:
                    stateChanged.await();
                    break;
                case Failed:
                    if (failureCause instanceof ConfigurationException) {
                        throw (ConfigurationException) failureCause;
                    }
                    if (failureCause instanceof RegistrationException) {
                        throw new ConfigurationException("Failed with CS registration", failureCause);
                    }
                    throw new IllegalStateException("AppConfigManager is failed", failureCause);
                case Disposed:
                    throw new IllegalStateException("AppConfigManager is disposed");
                }
            }
        } finally {
            stateLock.unlock();
        }
    }


    public void init()
            throws ConfigurationException, IllegalStateException {
        init(false);
    }

    public void init(final boolean waitForConfiguration)
            throws ConfigurationException, IllegalStateException {
        stateLock.lock();
        try {
            if (state == ManagerState.Disposed) {
                throw new IllegalStateException("AppConfigManager is already disposed");
            }
            final Protocol protocol = confService.getProtocol();
            if (protocol == null) {
                throw new IllegalStateException("AppConfigManager has no config server protocol");
            }
            final long protocolTimeout = protocol.getTimeout();
            final ChannelState protocolState = protocol.getState();

            switch (state) {
            case Initialized:
                // its already initialized
                break;

            case Created:
                protocol.addChannelListener(confChannelListener);
                confService.register(confEventHandler, null);
                state = ManagerState.Disconnected;

                try {
                    if (ChannelState.Opened.equals(protocolState)) {
                        state = ManagerState.Connected;
                        if (doCSSubscribe) {
                            doSubscription();
                        }
                        readTheApplication();
                    } else if (ChannelState.Closed.equals(protocolState)) {
                        if (theWarmStandby != null) {
                            theWarmStandby.autoRestore(false);
                            theWarmStandby.open();
                        } else {
                            protocol.open();
                        }
                        state = ManagerState.Connected;
                    }
                } catch (final ConfigurationException ex) {
                    log.error("Exception in application configuration manager init()", ex);
                    state = ManagerState.Failed;
                    failureCause = ex;
                    throw ex;
                } catch (final WSException ex) {
                    log.error("Application configuration manager registration exception", ex);
                    state = ManagerState.Failed;
                    failureCause = ex;
                    if (ex.getCause() instanceof RegistrationException) {
                        throw new ConfigurationException(
                                "CS Registration failure", ex.getCause());
                    }
                    throw new ConfigurationException("CS connection failure", ex);
                } catch (final RegistrationException ex) {
                    log.error("Application configuration manager registration exception", ex);
                    state = ManagerState.Failed;
                    failureCause = ex;
                    throw new ConfigurationException("CS Registration failure", ex);
                } catch (final ProtocolException ex) {
                    // this exception is from the protocol when no WS is used
                    log.error("Application configuration manager init() protocol exception", ex);
                    state = ManagerState.Failed;
                    failureCause = ex;
                    throw new ConfigurationException("CS Registration failure", ex);
                } catch (final InterruptedException ex) {
                    log.error("Application configuration manager init() interrupted", ex);
                    Thread.currentThread().interrupt();
                } catch (final RuntimeException ex) {
                    log.error("Application configuration manager init() runtime exception", ex);
                    throw ex;
                } catch (final Exception ex) {
                    log.error("Application configuration manager init() exception", ex);
                }
                if (waitForConfiguration) {
                    waitForInitialized(System.currentTimeMillis() + protocolTimeout);
                }
                break;

            case Disconnected:
                if (theWarmStandby == null) {
                    // restore connection if no WS active?
                    if (ChannelState.Closed.equals(protocolState)) {
                        try {
                            protocol.beginOpen();
                        } catch (final ProtocolException ex) {
                            log.error("Application configuration manager init() protocol exception", ex);
                            throw new ConfigurationException("CS Registration failure", ex);
                        }
                    }
                }
                // continue with waiting for the configuration:

            case Connected:
                if (waitForConfiguration) {
                    waitForInitialized(System.currentTimeMillis() + protocolTimeout);
                }
                break;

            case Failed:
                throw new IllegalStateException("AppConfigManager is failed", failureCause);

            case Disposed:
                throw new IllegalStateException("AppConfigManager is already disposed");
            }
        } finally {
            stateLock.unlock();
        }
    }

    private void waitForInitialized(final long ttl) {
        while (true) {
            if (ManagerState.Initialized.equals(state)) {
                // we've got the configuration - return the method
                return;
            } else if (ManagerState.Failed.equals(state)) {
                throw new IllegalStateException("AppConfigManager initialization is failed", failureCause);
            } else if (ManagerState.Disposed.equals(state)) {
                throw new IllegalStateException("AppConfigManager is disposed");
            }
            long ttw = ttl - System.currentTimeMillis();
            if (ttw > 0) {
                try {
                    stateChanged.await(ttw, TimeUnit.MILLISECONDS);
                } catch (final InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } else {
                throw new RuntimeException("timeout");
            }
        }
    }


    public void refresh(final boolean waitForConfiguration)
            throws IllegalStateException {
        stateLock.lock();
        try {
            if (state == ManagerState.Disposed) {
                throw new IllegalStateException("AppConfigManager is already disposed");
            }
            final Protocol protocol = confService.getProtocol();
            if (protocol == null) {
                throw new IllegalStateException("AppConfigManager has no config server protocol");
            }
            final long protocolTimeout = protocol.getTimeout();

            switch (state) {
            case Created:
                throw new IllegalStateException("AppConfigManager is not initialized");
            case Initialized:
                if (!doCSSubscribe) {
                    state = ManagerState.Connected;
                    try {
                        readTheApplication();
                    } catch (final ConfigurationException ex) {
                        log.error("Failed to refresh application configuration", ex);
                    }
                    if (waitForConfiguration) {
                        waitForInitialized(System.currentTimeMillis() + protocolTimeout);
                    }
                }
                break;
            case Disconnected:
                // restore connection?
            case Connected:
                if (waitForConfiguration) {
                    waitForInitialized(System.currentTimeMillis() + protocolTimeout);
                }
                break;
            case Failed:
                throw new IllegalStateException("AppConfigManager initialization is failed", failureCause);
            case Disposed:
                throw new IllegalStateException("AppConfigManager is disposed");
            }
        } finally {
            stateLock.unlock();
        }
    }


    /**
     * @deprecated
     * @see #close()
     */
    @Deprecated
    public void done() throws ConfigurationException {
        close();
    }

    @Override
    public void close() {
        stateLock.lock();
        try {
            if (!ManagerState.Disposed.equals(state)) {
                if (doCSDispose) {
                    if (theWarmStandby != null) {
                        theWarmStandby.close();
                    } else {
                        confService.getProtocol().close();
                    }
                }
                confService.getProtocol().removeChannelListener(confChannelListener);
                confService.unregister(confEventHandler);
                if (doCSDispose) {
                    ConfServiceFactory.releaseConfService(confService);
                }
                invoker.release();
            }
        } catch (final Exception ex) {
            log.error("Exception in application configuration manager done()", ex);
        } finally {
            state = ManagerState.Disposed;
            stateChanged.signalAll();
            stateLock.unlock();
        }
    }


    @Override
    public void register(final Subscriber<GFAppCfgEvent> subscriber) {
        if (subscribers.contains(subscriber)) {
            throw new IllegalArgumentException("The subscriber is already registered");
        } else {
            subscribers.add(subscriber);
        }
    }

    @Override
    public void register(final Action<GFAppCfgEvent> handler, final Predicate<GFAppCfgEvent> filter) {
        subscribers.add(new EventSubscriberWrapper(filter, handler));
    }

    @Override
    public void unregister(final Subscriber<GFAppCfgEvent> subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void unregister(final Action<GFAppCfgEvent> handler) {
        final Iterator<Subscriber<GFAppCfgEvent>> iterator = subscribers.iterator();
        while (iterator.hasNext()) {
            final Subscriber<GFAppCfgEvent> subscriber = iterator.next();
            if ((subscriber == handler)
                || ((subscriber instanceof EventSubscriberWrapper)
                    && ((EventSubscriberWrapper) subscriber).action == handler)) {
                iterator.remove();
                break;
            }
        }
    }


    /**
     * @throws ConfigurationException
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    protected void readTheApplication()
                    throws ConfigurationException {
        try {
            final CfgApplication cfgApp = confService.retrieveObject(
                    CfgApplication.class,
                    new CfgApplicationQuery(applicationName));
            if (cfgApp != null) {
                final GCOMApplicationConfiguration appConf = new GCOMApplicationConfiguration(
                        cfgApp, true, true, readTenantInfo);
                GFApplicationConfigurationManager.this.handleEvent(
                        new GFAppCfgEventImpl(AppCfgEventType.AppConfigReceived,
                                appContext, appConf, cfgApp),
                        new Runnable() {
                            public void run() {
                                stateLock.lock();
                                try {
                                    appConfig = appConf;
                                    state = ManagerState.Initialized;
                                    stateChanged.signalAll();
                                } finally {
                                    stateLock.unlock();
                                }
                            }});
                return;
            }
        } catch (final Exception ex) {
            throw new ConfigurationException("Failed to initialize configuration for '"
                    + applicationName + "'", ex);
        }
        throw new IllegalStateException("No application configuration read for '"
                + applicationName + "'");
    }

    protected GCOMApplicationConfiguration getNewConfig() {
        try {
            Integer appDbid = null;
            CfgApplication cfgApp = null;
            final IGApplicationConfiguration currConfig = appConfig;
            if (currConfig != null) {
                appDbid = currConfig.getDbid();
            }
            if (appDbid != null) {
                cfgApp = (CfgApplication) confService.retrieveObject(
                        CfgObjectType.CFGApplication, appDbid);
                if (cfgApp == null) {
                    throw new IllegalStateException(
                            "No application configuration read DBID=" + appDbid);
                }
            }
            if (cfgApp == null) {
                cfgApp = confService.retrieveObject(
                        CfgApplication.class,
                        new CfgApplicationQuery(applicationName));
            }
            if (cfgApp != null) {
                return new GCOMApplicationConfiguration(cfgApp);
            }
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to read CME application '"
                    + applicationName + "'", ex);
        }
        throw new IllegalStateException("No application configuration read for '"
                + applicationName + "'");
    }

    private void updateCurrentConfig(final CfgApplication cfgApp) {
        try {
            appConfig = new GCOMApplicationConfiguration(
                    cfgApp, true, true, readTenantInfo);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to read CME application '"
                    + applicationName + "'", ex);
        }
    }


    protected boolean isCurrentApp(
            final IGApplicationConfiguration appConfig,
            final int appDbid) {
        final Integer currAppDbid = appConfig.getDbid();
        return currAppDbid != null && currAppDbid.intValue() == appDbid;
    }

    protected boolean isCurrentHost(
            final IGApplicationConfiguration appConfig,
            final int hostDbid) {
        final IGServerInfo srvInfo = appConfig.getServerInfo();
        if (srvInfo != null) {
            final IGHost host = srvInfo.getHost();
            if (host != null) {
                final Integer srvHostDbid = host.getDbid();
                if (srvHostDbid != null && srvHostDbid.intValue() == hostDbid) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isAppConnected(
            final IGApplicationConfiguration appConfig,
            final int appDbid,
            final boolean checkClusterNodes) {
        final List<IGAppConnConfiguration> connList = appConfig.getAppServers();
        for (final IGAppConnConfiguration conn: connList) {
            final IGApplicationConfiguration srv = conn.getTargetServerConfiguration();
            if (srv != null) {
                final Integer srvDbid = srv.getDbid();
                if (srvDbid != null && srvDbid.intValue() == appDbid) {
                    return true;
                }
                final IGServerInfo srvInfo = srv.getServerInfo();
                if (srvInfo != null) {
                    final IGApplicationConfiguration backup = srvInfo.getBackup();
                    if (backup != null) {
                        final Integer backupDbid = backup.getDbid();
                        if (backupDbid != null && backupDbid.intValue() == appDbid) {
                            return true;
                        }
                    }
                }
                if (checkClusterNodes && CfgAppType.CFGApplicationCluster.equals(srv.getApplicationType())) {
                    return isAppConnected(srv, appDbid, false);
                }
            }
        }
        return false;
    }

    protected boolean isAppConnected(
            final IGApplicationConfiguration appConfig,
            final int appDbid) {
        return isAppConnected(appConfig, appDbid, true);
    }

    protected boolean isHostConnected(
            final IGApplicationConfiguration appConfig,
            final int hostDbid,
            final boolean checkClusterNodes) {
        final List<IGAppConnConfiguration> connList = appConfig.getAppServers();
        if (connList == null) {
            return false;
        }
        for (final IGAppConnConfiguration conn: connList) {
            final IGApplicationConfiguration srv = conn.getTargetServerConfiguration();
            if (srv != null) {
                final IGServerInfo srvInfo = srv.getServerInfo();
                if (srvInfo != null) {
                    final IGHost host = srvInfo.getHost();
                    if (host != null) {
                        final Integer srvHostDbid = host.getDbid();
                        if (srvHostDbid != null && srvHostDbid.intValue() == hostDbid) {
                            return true;
                        }
                    }
                    final IGApplicationConfiguration backup = srvInfo.getBackup();
                    if (backup != null) {
                        final IGServerInfo srvBackupInfo = backup.getServerInfo();
                        if (srvBackupInfo != null) {
                            final IGHost backupHost = srvBackupInfo.getHost();
                            if (backupHost != null) {
                                final Integer backupHostDbid = backupHost.getDbid();
                                if (backupHostDbid != null && backupHostDbid.intValue() == hostDbid) {
                                    return true;
                                }
                            }
                        }
                    }
                    if (checkClusterNodes && CfgAppType.CFGApplicationCluster.equals(srv.getApplicationType())) {
                        return isHostConnected(srv, hostDbid, false);
                    }
                }
            }
        }
        return false;
    }

    protected boolean isHostConnected(
            final IGApplicationConfiguration appConfig,
            final int hostDbid) {
        return isHostConnected(appConfig, hostDbid, true);
    }

    protected AsyncInvoker getInvoker() {
        return invoker;
    }


    protected void handleEvent(final GFAppCfgEvent appEvent) {
        handleEvent(appEvent, null);
    }

    protected void handleEvent(final GFAppCfgEvent appEvent, final Runnable task) {
        handleTask(new Runnable() {
            public void run() {
                for (Subscriber<GFAppCfgEvent> subscriber: subscribers) {
                    final Predicate<GFAppCfgEvent> filter = subscriber.getFilter();
                    try {
                        if (filter == null || filter.invoke(appEvent)) {
                            subscriber.handle(appEvent);
                        }
                    } catch (final Exception ex) {
                        log.warn("Exception in GFAppCfgEvent notification", ex);
                    }
                }
                if (task != null) {
                    try {
                        task.run();
                    } catch (final Exception ex) {
                        log.warn("Exception in GFAppCfgEvent notification", ex);
                    }
                }
            }});
    }

    protected void handleTask(final Runnable task) {
        invoker.invoke(task);
    }

    protected void doSubscription() {
        try {
            final KeyValueCollection subscriptionFilter = new KeyValueCollection();
            final KeyValueCollection objectFilter = new KeyValueCollection();
            final KeyValuePair objTypeOpt = new KeyValuePair("object_type");
            objTypeOpt.setIntValue(CfgObjectType.CFGApplication.asInteger());
            objectFilter.add(objTypeOpt);
            subscriptionFilter.addList("subscription", objectFilter);
            confService.getProtocol().send(RequestRegisterNotification.create(subscriptionFilter));

            objTypeOpt.setIntValue(CfgObjectType.CFGHost.asInteger());
            confService.getProtocol().send(RequestRegisterNotification.create(subscriptionFilter));

            if (readTenantInfo) {
                objTypeOpt.setIntValue(CfgObjectType.CFGTenant.asInteger());
                confService.getProtocol().send(RequestRegisterNotification.create(subscriptionFilter));
            }
        } catch (final Exception ex) {
            log.error("Failed to subscribe for objects update notifications", ex);
        }
    }


    protected class GFApplicationContextImpl implements GFApplicationContext {

        @Override
        public IConfService getConfService() {
            return confService;
        }

        @Override
        public IGApplicationConfiguration getConfiguration() {
            return appConfig;
        }

        @Override
        public LmsLoggerFactory getLmsLoggerFactory() {
            return lmsLoggerFactory;
        }

        @Override
        public LmsEventLogger getLmsEventLogger() {
            return lmsLogger;
        }
    }


    private class EventSubscriberWrapper implements Subscriber<GFAppCfgEvent> {

        protected final Predicate<GFAppCfgEvent> filter;
        protected final Action<GFAppCfgEvent> action;

        protected EventSubscriberWrapper(
                final Predicate<GFAppCfgEvent> filter,
                final Action<GFAppCfgEvent> action) {
            this.action = action;
            this.filter = filter;
        }

        @Override
        public Predicate<GFAppCfgEvent> getFilter() {
            return filter;
        }

        @Override
        public void handle(final GFAppCfgEvent obj) {
            action.handle(obj);
        }
    }

    /**
     * Internal implementation of Genesys CME application configuration manager event.
     */
    protected class GFAppCfgEventImpl extends EventObject implements GFAppCfgEvent {

        private static final long serialVersionUID = -1075498544520325485L;

        private AppCfgEventType eventType;
        private IGApplicationConfiguration appConfig;

        private transient GFApplicationContext appContext;
        private transient ICfgObject confData;

        /**
         * Internal constructor of event implementation class.
         */
        protected GFAppCfgEventImpl(
                final AppCfgEventType type,
                final GFApplicationContext ctx,
                final IGApplicationConfiguration config,
                final ICfgObject confData) {
            super(GFApplicationConfigurationManager.this);
            this.eventType = type;
            this.appContext = ctx;
            this.appConfig = config;
            this.confData = confData;
        }


        @Override
        public AppCfgEventType getEventType() {
            return eventType;
        }

        @Override
        public GFApplicationContext getAppContext() {
            return appContext;
        }

        @Override
        public IGApplicationConfiguration getAppConfig() {
            return appConfig;
        }

        @Override
        public ICfgObject getConfData() {
            return confData;
        }

        @Override
        public String toString() {
            return "GFAppCfgEvent[type=" + eventType + ",app="
                    + ((appConfig != null) ? ('"' + appConfig.getApplicationName() + '"'): "null")
                    + "]";
        }
    }


    private class ConfEventHandler implements Action<ConfEvent> {
        @Override
        public void handle(final ConfEvent event) {
            final IGApplicationConfiguration currentConfig = appConfig;
            if (currentConfig != null) {
                if (!EventType.ObjectUpdated.equals(event.getEventType())) {
                    return;
                }
                final ICfgObject obj = event.getCfgObject();
                if (obj != null) {
                    final CfgObjectType objType = obj.getObjectType();
                    GCOMApplicationConfiguration newConfig = null;
                    if (CfgObjectType.CFGApplication.equals(objType)) {
                        if (isCurrentApp(currentConfig, obj.getObjectDbid())) {
                            newConfig = getNewConfig();
                            GFApplicationConfigurationManager.this.handleEvent(
                                    new GFAppCfgEventImpl(AppCfgEventType.AppConfigUpdated,
                                            appContext, newConfig, obj));
                        } else if (isAppConnected(currentConfig, obj.getObjectDbid())) {
                            newConfig = getNewConfig();
                            GFApplicationConfigurationManager.this.handleEvent(
                                    new GFAppCfgEventImpl(AppCfgEventType.ConnSrvUpdated,
                                            appContext, newConfig, obj));
                        }

                    } else if (CfgObjectType.CFGHost.equals(objType)) {
                        if (isCurrentHost(currentConfig, obj.getObjectDbid())) {
                            newConfig = getNewConfig();
                            GFApplicationConfigurationManager.this.handleEvent(
                                    new GFAppCfgEventImpl(AppCfgEventType.AppHostUpdated,
                                            appContext, newConfig, obj));
                        }
                        if (isHostConnected(currentConfig, obj.getObjectDbid())) {
                            if (newConfig == null) {
                                newConfig = getNewConfig();
                            }
                            GFApplicationConfigurationManager.this.handleEvent(
                                    new GFAppCfgEventImpl(AppCfgEventType.ConnHostUpdated,
                                            appContext, newConfig, obj));
                        }

                    } else if (CfgObjectType.CFGTenant.equals(objType)) {
                        if (readTenantInfo) {
                            final List<IGTenantInfo> tenants = currentConfig.getTenants();
                            if (tenants != null) {
                                final int dbid = obj.getObjectDbid();
                                boolean isOurTenant = false;
                                for (final IGTenantInfo tenant : tenants) {
                                    final Integer tntDbid = tenant.getDbid();
                                    if (tntDbid != null && tntDbid.intValue() == dbid) {
                                        isOurTenant = true;
                                        break;
                                    }
                                }
                                if (isOurTenant) {
                                    // TODO separate 'tenant update' event:
                                    newConfig = getNewConfig();
                                    GFApplicationConfigurationManager.this.handleEvent(
                                            new GFAppCfgEventImpl(AppCfgEventType.AppConfigUpdated,
                                                    appContext, newConfig, obj));
                                }
                            }
                        }
                    }

                    if (newConfig != null) {
                        updateCurrentConfig(newConfig.getCfgApplication());
                    }
                }
            }
        }
    }


    private class ConfServerChannelListenerImpl implements ChannelListener {

        @Override
        public void onChannelOpened(final EventObject event) {
            boolean sessionIsNew = (appConfig == null);
            if (!((ConfServerProtocol) confService.getProtocol())
                    .getServerContext().isSessionRestored()) {
                sessionIsNew = true;
            }

            final ManagerState nextState = sessionIsNew
                    ? ManagerState.Connected : ManagerState.Initialized;

            handleEvent(
                    new GFAppCfgEventImpl(
                        AppCfgEventType.ConfSrvConnected,
                        appContext, null, null),
                    new Runnable() {
                        public void run() {
                            stateLock.lock();
                            try {
                                state = nextState;
                                failureCause = null;
                                stateChanged.signalAll();
                            } finally {
                                stateLock.unlock();
                            }
                        }});

            if (sessionIsNew) {
                if (doCSSubscribe) {
                    doSubscription();
                }

                handleTask(new Runnable() {
                    public void run() {
                        try {
                            readTheApplication();
                        } catch (ConfigurationException ex) {
                            log.error("Exception reading application configuration", ex);
                        }
                    }});
            }
        }

        @Override
        public void onChannelClosed(final ChannelClosedEvent event) {
            handleEvent(
                    new GFAppCfgEventImpl(
                        AppCfgEventType.ConfSrvDisconnected,
                        appContext, null, null),
                    new Runnable() {
                        public void run() {
                            stateLock.lock();
                            try {
                                if (!ManagerState.Disposed.equals(state)
                                        && !ManagerState.Failed.equals(state)) {
                                    if (event.getCause() instanceof RegistrationException) {
                                        state = ManagerState.Failed;
                                        failureCause = event.getCause();
                                    } else {
                                        state = ManagerState.Disconnected;
                                        failureCause = event.getCause();
                                    }
                                }
                                stateChanged.signalAll();
                            } finally {
                                stateLock.unlock();
                            }
                        }});
        }

        @Override
        public void onChannelError(final ChannelErrorEvent event) { /* nothing to do */ }
    }


    /**
     * Dedicated builder for basic implementation of application configuration manager.
     * <p/>
     * It provides two main ways of the manager building - with or without pre-configured ConfService instance.
     * If application provides ConfService, it should also take care on the protocol connection state management
     * (i.e. WarmStandby configuration, etc), ConfService cache configuration, and CS updates subscriptions.
     * <h4>Creation of the manager without pre-configured ConfService</h4>
     * It means that application does not need special customizations on ConfService, so, the builder will create
     * internal ConfService instance with default configuration which is required for the manager main operations.
     * <code><pre>
     * // Create the manager:
     * GFApplicationConfigurationManager appManager =
     *         GFApplicationConfigurationManager.newBuilder()
     *         .withLmsConveyor(lmsConveyor)     // - Optional, may be omitted if application does not need custom LMS conveyor.
     *         .withCSEndpoint(cfgServerPrimary) // - It's possible to provide several CS Endpoints -
     *         .withCSEndpoint(cfgServerBackup)  //   WarmStandby will use this list for connection restoration.
     *         .withWarmStandby(true)            // - Enables/disables WarmStandby initialization on building;
     *                                           //   by default it is enabled if more than one CS Endpoint provided.
     *         .withClientId(clientType, clientName)
     *         .withUserId(csUsername, csPassword)   // - It's not needed if application is server type.
     *         .build();
     *
     * // Register own application configuration appliance handler:
     * appManager.register(new GFAppCfgOptionsEventListener() {
     *         public void handle(final GFAppCfgEvent event) {
     *             Log.getLogger(getClass()).info(
     *                     "The application configuration options received: " + event.getAppConfig());
     *         }});
     *
     * // Activate the manager:
     * appManager.init();
     *
     * // Do the application work...
     *
     * // On application shutdown:
     * appManager.done();
     * </pre></code>
     * <h4>Creation of the manager with pre-configured ConfService</h4>
     * By this way applications may create custom configuration of ConfService instance.
     * Customization may include own ConfService extended implementation, custom service/cache policy,
     * ConfService ConfConfCache implementation, etc.<code><pre>
     * // Create Configuration Server protocol:
     * ConfServerProtocol protocol = new ConfServerProtocol(cfgServerPrimary);
     * protocol.setClientName(clientName);
     * protocol.setClientApplicationType(clientType.ordinal());
     * protocol.setUserName(csUsername);
     * protocol.setUserPassword(csPassword);
     *
     * // Initialize WarmStandby service:
     * WarmStandby csWarmStandby = new WarmStandby(protocol, cfgServerPrimary, cfgServerBackup);
     *
     * // Create ConfService:
     * IConfService confService = ConfServiceFactory.createConfService(protocol, servicePolicy, cachePolicy);
     *
     * // Start WarmStandby service:
     * csWarmStandby.autoRestore();
     *
     * // Create the manager:
     * GFApplicationConfigurationManager appManager =
     *           GFApplicationConfigurationManager.newBuilder()
     *           .withConfService(confService)
     *           .withDoCSSubscription(true) // Do subscriptions for updates on application and host CS object types
     *           .build();
     *
     * // Register own application configuration appliance handler:
     * appManager.register(new GFAppCfgOptionsEventListener() {
     *         public void handle(final GFAppCfgEvent event) {
     *             Log.getLogger(getClass()).info(
     *                     "The application configuration options received: " + event.getAppConfig());
     *         }});
     *
     * // Activate the manager:
     * appManager.init();
     *
     * // Do the application work...
     *
     * // On application shutdown:
     * appManager.done();
     * </pre></code>
     *
     * @see GFApplicationConfigurationManager#newBuilder()
     */
    public static class ManagerBuilder
            extends AbstractManagerBuilder<GFApplicationConfigurationManager, ManagerBuilder> {

        @Override
        protected GFApplicationConfigurationManager setupContext(
                final GFApplicationConfigurationManager manager) {
            super.setupContext(manager);
            manager.appContext = manager.new GFApplicationContextImpl();
            return manager;
        }

        public GFApplicationConfigurationManager build() {
            checkRequiredParameters();

            GFApplicationConfigurationManager mgr = null;
            if (confService != null) {
                boolean doSubscriptions = (doCSSubscription != null && doCSSubscription);
                boolean readTenants = (readTenantsInfo != null && readTenantsInfo);
                mgr = new GFApplicationConfigurationManager(
                        clientName, confService, null,
                        doSubscriptions, readTenants, false);
            } else {
                ConfServerProtocol protocol = createProtocol();
                if (protocol != null) {
                    WarmStandby warmStandby = createWarmStandby(protocol);
                    IConfService cs = ConfServiceFactory.createConfService(protocol, true);
                    if (cs != null) {
                        boolean doSubscriptions = (doCSSubscription == null || doCSSubscription);
                        boolean readTenants = (readTenantsInfo != null && readTenantsInfo);
                        mgr = new GFApplicationConfigurationManager(
                                clientName, cs, warmStandby,
                                doSubscriptions, readTenants, true);
                    }
                }
            }

            if (mgr == null) {
                throw new IllegalArgumentException(
                        "GFApplicationConfigurationManager.Builder: no required parameters given");
            }

            return setupContext(mgr);
        }
    }
}
