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
package com.genesyslab.platform.applicationblocks.warmstandby;

import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Listener;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.ListenerHelper;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.RegistrationException;

import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.threading.InvokerFactory;

import com.genesyslab.platform.commons.timer.TimerAction;
import com.genesyslab.platform.commons.timer.TimerActionTicket;
import com.genesyslab.platform.commons.timer.TimerFactory;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import java.util.EventObject;


/**
 * WarmStandbyService class realizes a redundancy configuration consisting of two
 * servers: Primary and Backup, where the Primary server operates in active
 * mode and the Backup server in standby mode. Only the Primary server
 * accepts connections and message exchanges with the clients.
 * In case of the Primary server failure, the Backup server switches
 * to active mode assuming the role and behavior of the Primary server.
 */
public class WarmStandbyService {

    private static final Integer DEFAULT_TIMEOUT = 10000;
    private static final Short DEFAULT_ATTEMPTS = 3;
    private static final String WARMSTANDBY_INVOKER_NAME = "warmstandby";

    private final Protocol channel;
    private AsyncInvoker intInvoker = null;
    private AsyncInvoker extInvoker = null;

    private WarmStandbyConfiguration configuration;
    private WarmStandbyState state = WarmStandbyState.OFF;
    private short attempt = 1;
    private short switchover = 0;

    private WarmStandbyConnectionFailureHandler defFailureHandler =
            new DefaultWarmStandbyConnectionFailureHandler();
    private WarmStandbyConnectionFailureHandler failureHandler = null;

    private final Object wsGuard = new Object();

    private WarmStandbyChannelListener listener =
                new WarmStandbyChannelListener();

    private ListenerHelper listeners = new ListenerHelper();
    private TimerActionTicket reopenTimer = null;

    private static final ILogger log =
        Log.getLogger(WarmStandbyService.class);


    /**
     * Creates a WarmStandbyService instance.
     *
     * @param protocolChannel channel the WarmStandbyService is responsible for
     */
    public WarmStandbyService(final Protocol protocolChannel) {
        this.channel = protocolChannel;
    }


    /**
     * Activates (switches on) the WarmStandbyService.
     *
     * @throws IllegalStateException if this class is in inappropriate state
     *                               to start (already started, not configured)
     */
    public void start() {
        synchronized (wsGuard) {
            if (state != WarmStandbyState.OFF) {
                throw new IllegalStateException("Warm Standby is already started.");
            }
            if (configuration == null) {
                throw new IllegalStateException("Warm Standby's Configuration is not defined.");
            }

            channel.addChannelListener(listener);

            attempt = 1;
            switchover = 0;
            setState(WarmStandbyState.IDLE);
        }
        log.debugFormat("Started on channel {0}", channel);
    }

    /**
     * Deactivates (switches off) the WarmStandbyService.
     *
     * @throws IllegalStateException if this class is in inappropriate state
     *                               to stop (is not started)
     */
    public void stop() {
        synchronized (wsGuard) {
            if (state == WarmStandbyState.OFF) {
                throw new IllegalStateException("Warm Standby is not started.");
            }
            cancelReopenTimer();

            channel.removeChannelListener(listener);

            setState(WarmStandbyState.OFF);

            releaseDefaultWarmstandbyInvoker();
        }

        if (log.isDebug()) {
            log.debug("Stopped on channel " + channel);
        }
    }

    private void releaseDefaultWarmstandbyInvoker() {
        if (intInvoker != null) {
            InvokerFactory.releaseInvoker(
                    WARMSTANDBY_INVOKER_NAME);
            intInvoker = null;
        }
    }

    public void setConnectionFailureHandler(
            final WarmStandbyConnectionFailureHandler handler) {
        synchronized (wsGuard) {
            failureHandler = handler;
        }
    }

    /**
     * Sets WarmStandby Configuration.
     *
     * @param conf WarmStandby configuration
     */
    public void applyConfiguration(final WarmStandbyConfiguration conf) {
        if (conf == null) {
            throw new NullPointerException("configuration is null.");
        }

        applyConfiguration(conf, (short) 1);
    }

    /**
     * Sets WarmStandby Configuration.
     *
     * @param conf WarmStandby configuration
     * @param reset flag to reset reconnect attempt counter
     */
    public void applyConfiguration(
                final WarmStandbyConfiguration conf, final boolean reset) {
        if (conf == null) {
            throw new NullPointerException("configuration is null.");
        }

        short newAtempt = attempt;
        if (reset) {
            newAtempt = (short) 1;
        }

        applyConfiguration(conf, newAtempt);
    }

    private void applyConfiguration(
                final WarmStandbyConfiguration conf, final short attempt) {
        synchronized (wsGuard) {
            Integer timeout;
            if (conf.getTimeout() == null) {
                if (configuration == null) {
                    timeout = DEFAULT_TIMEOUT;
                } else {
                    timeout = configuration.getTimeout();
                }
            } else {
                timeout = conf.getTimeout();
            }

            Short attempts;
            if (conf.getAttempts() == null) {
                if (configuration == null) {
                    attempts = DEFAULT_ATTEMPTS;
                } else {
                    attempts = configuration.getAttempts();
                }
            } else {
                attempts = conf.getAttempts();
            }

			configuration = new WarmStandbyConfiguration(
                    conf.getActiveEndpoint(), conf.getStandbyEndpoint(),
                    timeout, attempts);
            configuration.setSwitchovers(conf.getSwitchovers());
        	configuration.setFirstReconnectTimeout(conf.getFirstReconnectTimeout());

            this.attempt = attempt;

            try {
                channel.setEndpoint(configuration.getActiveEndpoint());
            } catch (IllegalStateException ex) {
                log.warnFormat("WarmStandbyService can''t update Endpoint on channel {0}: {1}",
                        new Object[] {channel, ex});
            }
        }

        if (log.isDebug()) {
            log.debugFormat("Configuration on ''{0}'' is changed to {1}",
                        new Object[] {channel.getEndpoint(), configuration });
        }
    }

    /**
     * Adds listener about WarmStandbyService events.
     *
     * @param listener event listener
     */
    public final void addListener(final WarmStandbyListener listener) {
        listeners.addListener(listener);
    }

    /**
     * Removes listener about WarmStandbyService events.
     *
     * @param listener event listener
     */
    public final void removeListener(final WarmStandbyListener listener) {
        listeners.removeListener(listener);
    }

    /**
     * Gets the channel the WarmStandbyService is responsible for.
     *
     * @return the channel the WarmStandbyService is responsible for
     */
    public final Protocol getChannel() {
        return channel;
    }

    /**
     * Sets invoker. It's used by this class to perform asynchronous operations,
     * such as firing events.
     *
     * @param invoker asynchronous invoker
     */
    public final void setInvoker(final AsyncInvoker invoker) {
        synchronized (wsGuard) {
            extInvoker = invoker;
        }
    }

    /**
     * Gets WarmStandbyService Configuration.
     *
     * @return configuration in effect
     */
    public final WarmStandbyConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Gets the WarmStandbyService State.
     *
     * @return current state
     */
    public final WarmStandbyState getState() {
        return state;
    }

    /**
     * Gets current reconnection attempt value.
     *
     * @return current count of attempts
     */
    public final short getAttempt() {
        return attempt;
    }

    /**
     * Gets current switchover number value.
     *
     * @return current count of switchovers
     */
    public final short getSwitchover() {
        return switchover;
    }

    /////////////// non public

    /**
     * Switches over Active and Standby (server) Uri's.
     */
    protected void switchover() {
        Endpoint tempEndpoint = configuration.getActiveEndpoint();
        configuration.setActiveEndpoint(configuration.getStandbyEndpoint());
        configuration.setStandbyEndpoint(tempEndpoint);
        switchover++;
    }

    /**
     * Access to the internal invoker.
     *
     * @return invoker instance
     */
    protected AsyncInvoker getInvoker() {
        synchronized (wsGuard) {
            if (extInvoker != null) {
                return extInvoker;
            }
            if (intInvoker == null) {
                intInvoker = InvokerFactory.namedInvoker(
                    WARMSTANDBY_INVOKER_NAME);
            }
            return intInvoker;
        }
    }

    /**
     * Callback method for connection lost event handling.
     * It implements default logic for choosing failure handler and calling it for appropriate
     * connection restore actions.
     *
     * @param event connection lost event
     */
    protected void processConnectivityFailure(final ChannelClosedEvent event) {
        if (log.isDebug()) {
            log.debug("Processing channel '" + channel
                      + "' closed (failure " + event
                      + ")... State: " + state);
            if (event.getPreviousChannelState() == ChannelState.Opening) {
                log.debug("Channel closing caused by registration failure.");
            } else {
                log.debug("Channel closing caused by connection failure.");
            }
        }

        synchronized (wsGuard) {
            if (state == WarmStandbyState.OFF) {
                return;
            }

            WarmStandbyConnectionFailureContextImpl context =
                new WarmStandbyConnectionFailureContextImpl(event);

            try {
                if (event.getCause() instanceof RegistrationException) {
                    if (failureHandler != null) {
                        try {
                            if (failureHandler.handleRegistrationFailure(
                                    context)) {
                                return;
                            }
                        } catch (Exception ex) {
                            log.warn("Exception in user defined ConnectionFailureHandler at "
                                + configuration.getActiveEndpoint(), ex);
                        }
                    }
                    defFailureHandler.handleRegistrationFailure(context);
                    return;
                }
                if (failureHandler != null) {
                    try {
                        if (failureHandler.handleConnectionFailure(
                                context)) {
                            return;
                        }
                    } catch (Exception ex) {
                        log.warn("Exception in user defined ConnectionFailureHandler at "
                            + configuration.getActiveEndpoint(), ex);
                    }
                }
                defFailureHandler.handleConnectionFailure(context);
            } finally {
                context.invalidate();
            }
        }
    }

    private void fireSwitchedOver() {
        if (log.isDebug()) {
            log.debug("Firing 'SwitchedOver'. Active:"
                      + configuration.getActiveEndpoint() + ", Standby:"
                      + configuration.getStandbyEndpoint());
        }

        getInvoker().invoke(new SwichoverVisitor());
    }


    private void fireStateChanged() {
        if (log.isDebug()) {
            log.debug("Firing 'StateChange'. State: '" + state
                      + "' Channel: '" + channel);
        }

        getInvoker().invoke(new StateChangedVisitor(state));
    }

    private void setState(final WarmStandbyState newState) {
        if (newState == null) {
            throw new NullPointerException("newState can't be null");
        }

        if (newState.equals(this.state)) {
            return;
        }

        this.state = newState;
        fireStateChanged();
    }

    private void reopenChannel() {
        synchronized (wsGuard) {
            cancelReopenTimer();

            if (state == WarmStandbyState.OFF) {
                return;
            }

            if (channel.getState() != ChannelState.Closed) {
                return;
            }

            if (log.isDebug()) {
                log.debug("Reopening the channel " + channel
                          + ", " + attempt + " of "
                          + configuration.getAttempts());
            }

            int currAttempt = attempt++;

            setState(WarmStandbyState.RECONNECTING);

            try {
                channel.beginOpen();
            } catch (Exception e) {
                if (log.isDebug()) {
                    log.debug("Reopening the channel " + channel
                              + ", " + currAttempt + " of "
                              + configuration.getAttempts() + " failed", e);
                }
            }
        }
    }

    private void cancelReopenTimer() {
        if (reopenTimer != null) {
            reopenTimer.cancel();
            reopenTimer = null;
        }
    }


    private class WarmStandbyChannelListener implements ChannelListener {

        public void onChannelOpened(final EventObject event) {
            if (state == WarmStandbyState.OFF) {
                return;
            }

            cancelReopenTimer();

            if (log.isDebug()) {
                log.debugFormat("Processing channel ''{0}'' opened... WS State: ''{1}''",
                        new Object[] {channel, state});
            }

            synchronized (wsGuard) {
                // tbd: setting endpoint in onOpened, what for?
                //channel.setEndpoint(configuration.getActiveEndpoint());
                attempt = 1;
                switchover = 0;

                setState(WarmStandbyState.IDLE);
            }
        }

        public void onChannelClosed(final ChannelClosedEvent event) {
            if (state == WarmStandbyState.OFF) {
                return;
            }

            log.debugFormat("Processing closed on channel ''{0}''", channel);

            if (event == null) {
                return;
            }

            if (event.getCause() != null) {
                processConnectivityFailure(event);
            } else {
                log.infoFormat("Closing caused by a request on channel ''{0}''", channel);
                synchronized (wsGuard) {
                    if (state == WarmStandbyState.RECONNECTING) {
                        setState(WarmStandbyState.IDLE);
                    }
                }
            }
        }

        public void onChannelError(final ChannelErrorEvent event) {
            // ignored, not needed for warm standby
        }
    }


    private class StateChangedVisitor
                implements ListenerHelper.NotificationVisitor, Runnable {

        private final WarmStandbyStateChangedEvent event;

        public StateChangedVisitor(final WarmStandbyState state) {
            event = new WarmStandbyStateChangedEvent(WarmStandbyService.this, state);
        }

        public void visitListener(final Listener listener) {
            ((WarmStandbyListener) listener).onStateChanged(event);
        }

        public void run() {
            listeners.notifyListeners(this);
        }
    }

    private class SwichoverVisitor
                implements ListenerHelper.NotificationVisitor, Runnable {

        public void visitListener(final Listener listener) {
            ((WarmStandbyListener) listener).onSwitchover(
                        new EventObject(WarmStandbyService.this));
        }

        public void run() {
            listeners.notifyListeners(this);
        }
    }

    private class ReopenTimerTask implements TimerAction {

        public void onTimer() {
            reopenChannel();
        }

        @Override
        public String toString() {
            return  "WarmStandbyService.ReopenTimerTask for " + channel;
        }
    }

    private class WarmStandbyConnectionFailureContextImpl
            implements WarmStandbyConnectionFailureContext {

        private final ChannelClosedEvent event;

        private boolean invalidated = false;

        private final Object lock = new Object();


        WarmStandbyConnectionFailureContextImpl(
                final ChannelClosedEvent event) {
            this.event = event;
        }

        void invalidate() {
            synchronized (lock) {
                invalidated = true;
            }
        }

        private void throwIfInvalid() {
            if (invalidated) {
                throw new IllegalStateException(
                        "ConnectionFailureContext has been invalidated");
            }
        }

        public ChannelClosedEvent getEvent() {
            synchronized (lock) {
                throwIfInvalid();

                return event;
            }
        }

        public WarmStandbyService getService() {
            synchronized (lock) {
                throwIfInvalid();

                return WarmStandbyService.this;
            }
        }

        public void doSwitchover() {
            synchronized (lock) {
                throwIfInvalid();

                if (log.isDebug()) {
                    log.debug("Switching over the configurations from "
                          + configuration.getActiveEndpoint()
                          + " to " + configuration.getStandbyEndpoint());
                }

                WarmStandbyService.this.switchover();
                channel.setEndpoint(configuration.getActiveEndpoint());
                attempt = 1;
                fireSwitchedOver();
            }
        }

        public void doStandbyOff() {
            synchronized (lock) {
                throwIfInvalid();

                if (log.isDebug()) {
                    log.debug("WarmStandby switched off for "
                          + configuration.getActiveEndpoint()
                          + " <> " + configuration.getStandbyEndpoint());
                }

                attempt = 1;
                switchover = 0;
                setState(WarmStandbyState.OFF);

                releaseDefaultWarmstandbyInvoker();
            }
        }

        public void scheduleReconnect(final long delayMillisec) {
            synchronized (lock) {
                throwIfInvalid();

                synchronized (wsGuard) {
                    cancelReopenTimer();
                    setState(WarmStandbyState.WAITING);
                    reopenTimer = TimerFactory.getTimer().schedule(
                            delayMillisec, new ReopenTimerTask());
                }
            }
        }
    }

    private static class DefaultWarmStandbyConnectionFailureHandler
            implements WarmStandbyConnectionFailureHandler {

        public boolean handleRegistrationFailure(
                final WarmStandbyConnectionFailureContext context) {
            log.info("Channel closing after registration failure:",
                    context.getEvent().getCause());
            context.doStandbyOff();
            return true;
        }

        public boolean handleConnectionFailure(
                final WarmStandbyConnectionFailureContext context) {

            WarmStandbyService service = context.getService();
            WarmStandbyConfiguration config = service.getConfiguration();

            boolean switchoverRequired =
                    (service.getAttempt() > config.getAttempts());
            boolean waitForTimeout =
                    (service.getAttempt() > 1);

            if (service.getSwitchover() < 2) {
                switchoverRequired = true;
                waitForTimeout = false;
            }

            if (switchoverRequired) {
                Short cfgSwitchovers = config.getSwitchovers();
                if (cfgSwitchovers == null
                        || service.getSwitchover() < cfgSwitchovers) {
                    context.doSwitchover();
                    if (service.getSwitchover() > 2
                            && config.getAttempts() < 2) {
                        waitForTimeout = true;
                    } else {
                        waitForTimeout = false;
                    }
                } else {
                    if (log.isDebug()) {
                        log.debug("Switchover " + service.getSwitchover()
                                + " of " + cfgSwitchovers
                                + ", WarmStandbyService is stopped");
                    }
                    context.doStandbyOff();
                    return true;
                }
            }
            Integer firstReconnectTimeout = config.getFirstReconnectTimeout();
			int fastTimeout = ((service.switchover<=1) 
					&& (firstReconnectTimeout != null)) 
						? firstReconnectTimeout : 100;

            Integer timeout = config.getTimeout();
			context.scheduleReconnect(
                    	waitForTimeout ? ( timeout==null 
                    							? DEFAULT_TIMEOUT 
            									: timeout) 
        							   : fastTimeout );
            return true;
        }
    }
}
