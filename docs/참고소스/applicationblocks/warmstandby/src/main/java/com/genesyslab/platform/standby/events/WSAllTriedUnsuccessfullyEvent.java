/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby.events;

import com.genesyslab.platform.standby.WSHandler;
import com.genesyslab.platform.standby.WarmStandby;

/**
 * WSAllTriedUnsuccessfullyEvent is used in warm standby handler.
 * @see WSHandler#onAllEndpointsTriedUnsuccessfully(WSAllTriedUnsuccessfullyEvent) 
 */
public final class WSAllTriedUnsuccessfullyEvent extends WSEvent {

    private final int retryNumber;
    
    /**
     * Creates a WSAllTriedUnsuccessfullyEvent instance.
     * @param warmStandby warm standby.
     * @param retryNumber indicates how many times all endpoints has been tried unsuccessfully.
     */
    public WSAllTriedUnsuccessfullyEvent(WarmStandby warmStandby, int retryNumber) {
        super(warmStandby);
        this.retryNumber = retryNumber;
    }
    
    /**
     * Returns how many times all endpoints have been tried unsuccessfully.
     * Every time when the channel is opened retry number is reset to zero.
     * @return retry number.
     */
    public int getRetryNumber() {
        return retryNumber;
    }
}
