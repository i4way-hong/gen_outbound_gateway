/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby.events;

import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.standby.WSHandler;
import com.genesyslab.platform.standby.WarmStandby;

/**
 * WSTriedUnsuccessfullyEvent is used in warm standby handler.
 * @see WSHandler#onEndpointTriedUnsuccessfully(WSTriedUnsuccessfullyEvent) 
 */
public final class WSTriedUnsuccessfullyEvent extends WSClosedEvent {

    private boolean resume;

    /**
     * Creates a WSTriedUnsuccessfullyEvent instance.
     * @param warmStandby warm standby.
     * @param event provides event specific information.
     */
    public WSTriedUnsuccessfullyEvent(WarmStandby warmStandby, ChannelClosedEvent event) {
        super(warmStandby, event);
    }

    /**
     * Checks if channel connection restoring will be stopped.
     * @return true if channel connection restoring will be stopped.
     * @see {@link #resume}
     */
    public boolean isRestoringStopped() {
        Throwable cause = getChannelClosedEvent().getCause();
        return !resume && (cause instanceof RegistrationException);
    }
    
    
    /**
     * Specify that the same endpoint should be retried again (ASAP) 
     * if warm standby open operation won't be canceled.
     */
    public void resume() {
        resume = true;
    }

}
