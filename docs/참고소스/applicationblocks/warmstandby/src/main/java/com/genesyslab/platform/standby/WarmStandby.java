/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby;

import java.util.List;
import java.util.concurrent.Future;

import com.genesyslab.platform.commons.protocol.ChannelOpenedEvent;
import com.genesyslab.platform.commons.protocol.ClientChannel;
import com.genesyslab.platform.commons.protocol.DisabledMethodException;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.threading.CompletionHandler;
import com.genesyslab.platform.standby.exceptions.WSCanceledException;
import com.genesyslab.platform.standby.exceptions.WSException;

/**
 *  WarmStandby is used for opening/restoring a channel connection to any endpoint from the pool.
 */
public class WarmStandby {

    final WarmStandbyImpl impl;

    private WarmStandby(String name, ClientChannel channel) {
        impl = new WarmStandbyImpl(name, this, channel);
    }

    /**
     * Creates the new warm stanby instance and defines endpoint pool.
     * <p>
     * <b>Note:</b> if endpoints with same app,host and port will be passed
     * multiple times then only first one will be used while opening.
     * </p>
     * <p>
     * The iteration through endpoints always starts from the begin of the
     * endpoint pool. <br>
     * Endpoint from which channel has been disconnected and endpoints which
     * have been already checked unsuccessfully after disconnection, are skipped
     * from the iteration. <br>
     * If some other order of iteration is desired than in the appropriate event
     * handler (onChannelDisconnected, onEndpointCheckUnsuccessfully, ..) the
     * endpoints order in the pool can be changed appropriately.
     * </p>
     * 
     * @param channel
     *            that must be attached to the new WarmStandby instance.
     * @param endpoints
     *            array of endpoints.
     * @return new WarmStandby instance.
     * @throws IllegalArgumentException
     *             if argument channel is null.
     * @throws IllegalArgumentException
     *             if null endpoint occurs.
     * @throws DisabledMethodException
     *             if the channel has already been attached to another warm
     *             standby.
     */
    public WarmStandby(ClientChannel channel, Endpoint... endpoints) {
        this(null, channel);
        getConfig().setEndpoints(endpoints);
    }

    /**
     * Creates the new warm stanby instance and defines endpoints pool.
     * <p>
     * <b>Note:</b> if endpoints with same app,host and port will be passed
     * multiple times then only first one will be used while opening.
     * </p>
     * <p>
     * The iteration through endpoints always starts from the begin of the
     * endpoint pool. <br>
     * Endpoint from which channel has been disconnected and endpoints which
     * have been already checked unsuccessfully after disconnection, are skipped
     * from the iteration. <br>
     * If some other order of iteration is desired than in the appropriate event
     * handler (onChannelDisconnected, onEndpointCheckUnsuccessfully, ..) the
     * endpoints order in the pool can be changed appropriately.
     * </p>
     * 
     * @param channel
     *            that must be attached to the new WarmStandby instance.
     * @param endpoints
     *            list of endpoints.
     * @return new WarmStandby instance.
     * @throws IllegalArgumentException
     *             if argument channel is null.
     * @throws IllegalArgumentException
     *             if null endpoint occurs.
     * @throws DisabledMethodException
     *             if the channel has already been attached to another warm
     *             standby.
     */
    public WarmStandby(ClientChannel channel, List<Endpoint> endpoints) {
        this(null, channel);
        getConfig().setEndpoints(endpoints);
    }

    
    /**
     * Creates the new warm stanby instance and defines endpoint pool.
     * <p>
     * <b>Note:</b> if endpoints with same app,host and port will be passed
     * multiple times then only first one will be used while opening.
     * </p>
     * <p>
     * The iteration through endpoints always starts from the begin of the
     * endpoint pool. <br>
     * Endpoint from which channel has been disconnected and endpoints which
     * have been already checked unsuccessfully after disconnection, are skipped
     * from the iteration. <br>
     * If some other order of iteration is desired than in the appropriate event
     * handler (onChannelDisconnected, onEndpointCheckUnsuccessfully, ..) the
     * endpoints order in the pool can be changed appropriately.
     * </p>
     * @param name of the warmStandby instance.
     * @param channel
     *            that must be attached to the new WarmStandby instance.
     * @param endpoints
     *            array of endpoints.
     * @return new WarmStandby instance.
     * @throws IllegalArgumentException
     *             if argument channel is null.
     * @throws IllegalArgumentException
     *             if null endpoint occurs.
     * @throws DisabledMethodException
     *             if the channel has already been attached to another warm
     *             standby.
     */
    public WarmStandby(String name, ClientChannel channel, Endpoint... endpoints) {
        this(name, channel);
        getConfig().setEndpoints(endpoints);
    }

    /**
     * Creates the new warm stanby instance and defines endpoints pool.
     * <p>
     * <b>Note:</b> if endpoints with same app,host and port will be passed
     * multiple times then only first one will be used while opening.
     * </p>
     * <p>
     * The iteration through endpoints always starts from the begin of the
     * endpoint pool. <br>
     * Endpoint from which channel has been disconnected and endpoints which
     * have been already checked unsuccessfully after disconnection, are skipped
     * from the iteration. <br>
     * If some other order of iteration is desired than in the appropriate event
     * handler (onChannelDisconnected, onEndpointCheckUnsuccessfully, ..) the
     * endpoints order in the pool can be changed appropriately.
     * </p>
     * 
     * @param name of the warmStandby instance.
     * @param channel
     *            that must be attached to the new WarmStandby instance.
     * @param endpoints
     *            list of endpoints.
     * @return new WarmStandby instance.
     * @throws IllegalArgumentException
     *             if argument channel is null.
     * @throws IllegalArgumentException
     *             if null endpoint occurs.
     * @throws DisabledMethodException
     *             if the channel has already been attached to another warm
     *             standby.
     */
    public WarmStandby(String name, ClientChannel channel, List<Endpoint> endpoints) {
        this(name, channel);
        getConfig().setEndpoints(endpoints);
    }    
    
    /**
     * Gets warmStandby name.
     * @return warmStandby name.
     */
    public String getName() {
        return impl.getName();
    }

    
    /**
     * Gets attached channel.
     * 
     * @return attached channel.
     */
    public ClientChannel getChannel() {
        return impl.getChannel();
    }

    /**
     * Gets warmstandby's configuration.
     * 
     * @return warmstandby's configuration.
     */
    public WSConfig getConfig() {
        return impl.getConfig();
    }

    /**
     * Applies a new configuration. All changes have effect on the fly. The
     * 
     * @throws IllegalArgumentException
     *             if the argument value is null.
     */
    public void setConfig(WSConfig config) {
        impl.setConfig(config);
    }

    /**
     * Checks if the channel state is opened.
     * 
     * @return true if the channel state is opened.
     */
    public boolean isOpened() {
        return impl.isOpened();
    }

    /**
     * Opens synchronously the channel to any available server specified in the
     * configuration.
     * <p>
     * This method blocks until the channel is opened, also in the case that
     * channel opening is already in progress.
     * </p>
     * <p>
     * Do not use directly channel's open/close operations. Use the
     * warmstandby's ones.
     * </p>
     * 
     * @throws InterruptedException
     *             it can be thrown if the caller thread has been interrupted.
     *             It interrupts the waiting for the completion. 
     * @throws WSException 
     *             throws as descendant WSCanceledException, 
     *             WSNoAvailableServersException or WSEmptyEndpointPoolException.
     * @throws com.genesyslab.platform.standby.exceptions.WSNoAvailableServersException
     *             when all endpoints have been checked unsuccessfully.
     * @throws com.genesyslab.platform.standby.exceptions.WSEmptyEndpointPoolException
     *             (descendant of WSNoAvailableServersException)
     *             when no any endpoint in the pool.
     * @throws WSCanceledException
     *             if open operation has been canceled.
     * @throws RuntimeException 
     *             when it's called inside warmStandBy handler or completion handler.
     *             It prevents from infinite lock.
     */
    public void open() throws InterruptedException, WSException {
        impl.open();
    }

    /**
     * Opens asynchronously the channel to any available server specified in the
     * configuration.
     * <p>
     * The future of this method blocks until the channel is opened, also in the
     * case that channel opening is already in progress.
     * </p>
     * <p>
     * It may be used to continue the connection restoring to the same endpoint
     * inside the event OnEndpointCheckedUnsuccessfully handler when event cause
     * is RegistrationException. To restart the warm standby use
     * {@link #close()} before.
     * </p>
     * <p>
     * Do not use directly channel's open/close operations. Use the
     * warmstandby's ones.
     * </p>
     * 
     * @returns future of the operation.
     */
    public Future<ChannelOpenedEvent> openAsync() {
        return impl.openAsync();
    }

    /**
     * Opens asynchronously the channel to any available server specified in the
     * configuration.
     * <p>
     * The completion handler is notified about success when the channel is
     * opened, also in the case that channel opening is already in progress.
     * </p>
     * <p>
     * It may be used to continue the connection restoring to the same endpoint
     * inside the event OnEndpointCheckedUnsuccessfully handler when event cause
     * is RegistrationException.
     * To restart warmStandby use {use {@link #openAsync()}.
     * </p>
     * <p>
     * Do not use directly channel's open/close operations. Use the
     * warmstandby's ones.
     * </p>
     * 
     * @param handler
     *            is completion handler.
     * @param attachment
     *            that passed to completion handler.
     */
    public <A> void openAsync(final CompletionHandler<ChannelOpenedEvent, A> handler, final A attachment) {
        impl.openAsync(handler, attachment);
    }

    /**
     * Synchronously closes the channel and stops all WarmStandby's activity.
     * <p>
     * It disables auto restore, cancels channel opening and closes the channel
     * synchronously.
     * </p>
     * <p>
     * Do not use directly channel's open/close operations. Use the
     * warmstandby's ones.
     * </p>
     */
    public void close() throws InterruptedException {
        impl.close();
    }

    /**
     * Asynchronously closes the channel and stops all WarmStandby's activity.
     * <p>
     * It disables auto restore, cancels channel opening and closes the channel
     * asynchronously.
     * </p>
     * <p>
     * Do not use directly channel's open/close operations. Use the
     * warmstandby's ones.
     * </p>
     * 
     * @returns future of the operation.
     */
    public Future<Void> closeAsync() {
        return impl.closeAsync();
    }

    /**
     * Asynchronously closes the channel and stops all WarmStandby's activity.
     * <p>
     * It disables auto restore, cancels channel opening and closes the channel
     * asynchronously.
     * </p>
     * <p>
     * Do not use directly channel's open/close operations. Use the
     * warmstandby's ones.
     * </p>
     * 
     * @param handler
     *            is completion handler.
     * @param attachment
     *            that must be passed to the completion handler.
     */
    public <A> void closeAsync(CompletionHandler<Void, A> handler, A attachment) {
        impl.closeAsync(handler, attachment);
    }

    /**
     * Enables auto restore and opens asynchronously the channel if it's closed.
     */
    public void autoRestore() {
        impl.autoRestore(true);
    }

    /**
     * Enables auto restore and opens asynchronously the channel if it's needed.
     * 
     * @param open
     *            flag specify if asynchronous channel opening must be initiated
     *            if the channel is closed.
     */
    public void autoRestore(boolean open) {
        impl.autoRestore(open);
    }

    /**
     * Sets a handler.
     * 
     * @param handler
     *            that will handle the WarmStandby's events.
     * @throws IllegalArgumentException
     *             if the argument is null.
     */
    public void setHandler(WSHandler handler) {
        impl.setHandler(handler);
    }

    /**
     * Sets a handler.
     *
     * @param handler
     *            that will handle the WarmStandby's events.
     * @throws IllegalArgumentException
     *             if the argument is null.
     */
    public void setHandler(IWSHandler handler) {
        impl.setHandler(handler);
    }


}
