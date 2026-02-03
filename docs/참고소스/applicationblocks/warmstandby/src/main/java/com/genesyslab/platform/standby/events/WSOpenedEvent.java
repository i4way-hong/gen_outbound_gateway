/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby.events;

import com.genesyslab.platform.commons.protocol.ChannelOpenedEvent;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.standby.WSHandler;
import com.genesyslab.platform.standby.WarmStandby;

/**
 * WSOpenedEvent is used in warm standby handler.
 * @see WSHandler#onChannelOpened(WSOpenedEvent) 
 */
public final class WSOpenedEvent extends WSEvent {

    private final ChannelOpenedEvent event;

    /**
     * Creates a WSOpenedEvent instance.
     * @param warmStandby warm standby.
     * @param event provides event specific information.
     */
    public WSOpenedEvent(WarmStandby warmStandby, ChannelOpenedEvent event) {
        super(warmStandby);
        if (event == null) {
            throw new IllegalArgumentException("null event", null);
        }
        this.event = event;
    }

    /**
     * Gets the channel opened event.
     * @return the channel opened event.
     */
    public ChannelOpenedEvent getChannelOpenedEvent() {
        return event;
    }

    /**
     * Gets endpoint related to the event.
     * @return endpoint related to the event.
     */
    public Endpoint getEndpoint() {
        return event.getEndpoint();
    }
}
