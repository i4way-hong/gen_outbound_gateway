/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby;

import com.genesyslab.platform.standby.events.WSAllTriedUnsuccessfullyEvent;
import com.genesyslab.platform.standby.events.WSDisconnectedEvent;
import com.genesyslab.platform.standby.events.WSOpenedEvent;
import com.genesyslab.platform.standby.events.WSTriedUnsuccessfullyEvent;

/**
 * The handler provides possibility to handle warmstandby activity events and
 * make some changes in the configuration and/or stop/restart the warmstandby to
 * affect the default wamstandby behaviour.
 * <p>
 * <b>NOTE:</b> the warmstandby starts to make decision what to do next only
 * after handler events processing will be completed in case when there is a
 * assigned handler. i.e. handler is called synchronously by warmstandby.
 * </p>
 * <p>
 * <b>NOTE:</b> the warmstandby's logic and handler notification runs in
 * wramstandby's invoker.
 * </p>
 */
public interface IWSHandler {

    /**
     * It is called when the channel has been opened.
     *
     * @param event
     *            provides additional information about the event.
     */
    void onChannelOpened(WSOpenedEvent event);

    /**
     * It is called when the channel disconnection occurs.
     *
     * @param event
     *            provides additional information about channel closing.
     */
    void onChannelDisconnected(WSDisconnectedEvent event);

    /**
     * It is called when the channel hasn't been opened due to some error.
     *
     * @param event
     *            provides additional information about the fail.
     */
    void onEndpointTriedUnsuccessfully(WSTriedUnsuccessfullyEvent event);

    /**
     * It is called when all endpoints have been checked unsuccessfully (all
     * servers have been unavaliable) or the endpoint pool is empty.
     *
     * @param event
     *            provides additional information about the fail.
     */
    void onAllEndpointsTriedUnsuccessfully(WSAllTriedUnsuccessfullyEvent event);

}
