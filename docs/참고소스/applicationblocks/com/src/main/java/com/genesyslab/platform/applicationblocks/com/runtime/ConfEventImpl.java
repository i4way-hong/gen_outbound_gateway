//===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:

// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.

// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.

// THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
// ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
// DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
// WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
// NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
// NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
// EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
// CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
// NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).

// Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.com.runtime;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.applicationblocks.com.ConfEvent;
import com.genesyslab.platform.applicationblocks.com.ICfgObject;


/**
 * This class represents an event fired by Configuration Server.
 */
class ConfEventImpl implements ConfEvent {
    private EventType      eventType;
    private int            objectId;
    private CfgObjectType  objectType;
    private ICfgObject     cfgObj;
    private boolean        isUnsolicited;
    private int            unsolicitedNum;

    ConfEventImpl(
            final int           objId,
            final CfgObjectType objType,
            final EventType     evType,
            final ICfgObject    obj,
            final Integer       unsolicitedNum) {
        this.eventType  = evType;
        this.objectId   = objId;
        this.objectType = objType;
        this.cfgObj     = obj;
        this.unsolicitedNum = unsolicitedNum;
    }

    /**
     * The type of event received.
     *
     * @return event type
     */
    public EventType getEventType() {
        return this.eventType;
    }

    public boolean isUnsolicited() {
        return isUnsolicited;
    }

    void setUnsolicited(boolean value) {
        isUnsolicited = value;
    }

    /**
     * The type of the object about which we're receiving the event.
     *
     * @return configuration object type
     */
    public CfgObjectType getObjectType() {
        return this.objectType;
    }

    /**
     * The ID of the object about which we're receiving the event.
     *
     * @return object ID
     */
    public int getObjectId() {
        return this.objectId;
    }

    /**
     * Attached configuration information. It can be object or delta object.
     * Or Null if not applicable.
     *
     * @return configuration object
     */
    public ICfgObject getCfgObject() {
        return cfgObj;
    }

    @Override
    public String toString() {
        return getClass().getName()
                + ": Event Type = " + eventType
                + ", Object Type = " + objectType
                + ", Object Id = " + objectId
                + ".";
    }

    public int getUnsolicitedEventNumber() {
        return this.unsolicitedNum;
    }
}
