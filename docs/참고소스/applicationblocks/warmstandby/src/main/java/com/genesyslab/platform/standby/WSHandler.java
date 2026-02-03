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
 * The handler template  provides possibility to handle warmstandby activity events and
 * make some changes in the configuration and/or stop/restart the warmstandby to
 * affect the default warmstandby behaviour.
 * <p>
 * <b>NOTE:</b> the warmstandby starts to make decision what to do next only
 * after handler events processing will be completed in case when there is a
 * assigned handler.
 * </p>
 * <p>
 * <b>NOTE:</b> the warmstandby's logic and handler notification run in
 * warmstandby's invoker.
 * </p>
 */
public abstract class WSHandler implements IWSHandler {

    @Override
    public void onChannelOpened(final WSOpenedEvent event) {
    }

    @Override
    public void onChannelDisconnected(final WSDisconnectedEvent event) {
    }

    @Override
    public void onEndpointTriedUnsuccessfully(final WSTriedUnsuccessfullyEvent event) {
    }

    @Override
    public void onAllEndpointsTriedUnsuccessfully(final WSAllTriedUnsuccessfullyEvent event) {
    }
}
