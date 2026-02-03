/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby.events;

import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.standby.WarmStandby;

abstract class WSClosedEvent extends WSEvent {

    private final ChannelClosedEvent event;

    protected WSClosedEvent(WarmStandby warmStandby, ChannelClosedEvent event) {
        super(warmStandby);
        if (event == null) {
            throw new IllegalArgumentException("null event", null);
        }
        this.event = event;
    }
    
    /**
     * Gets the channel closed event.
     * @return the channel closed event.
     */
    public ChannelClosedEvent getChannelClosedEvent() {
        return event;
    }

    /**
     * Gets cause of the event.
     * @return cause of the event.
     */
    public Throwable getCause() {
        return event.getCause();
    }

    /**
     * Gets endpoint related to the event.
     * @return endpoint related to the event.
     */
    public Endpoint getEndpoint() {
        return event.getEndpoint();
    }
}
