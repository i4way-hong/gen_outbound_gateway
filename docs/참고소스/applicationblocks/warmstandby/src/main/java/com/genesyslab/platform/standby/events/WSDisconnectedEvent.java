/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby.events;

import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.standby.WSHandler;
import com.genesyslab.platform.standby.WarmStandby;

/**
 * WSDisconnectedEvent is used in warm standby handler.
 * @see WSHandler#onChannelDisconnected(WSDisconnectedEvent) 
 */
public final class WSDisconnectedEvent extends WSClosedEvent {

    /**
     * Creates a instance of WSDisconnectedEvent.
     * @param warmStandby warm standby.
     * @param event provides event specific information.
     */
    public WSDisconnectedEvent(WarmStandby warmStandby, ChannelClosedEvent event) {
        super(warmStandby, event);
    }
}
