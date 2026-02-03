/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby.events;

import java.util.EventObject;

import com.genesyslab.platform.standby.WarmStandby;

abstract class WSEvent extends EventObject {

    protected WSEvent(WarmStandby warmStandby) {
        super(warmStandby);
    }

    public WarmStandby getWarmStandby() {
        return (WarmStandby) getSource();
    }

}
