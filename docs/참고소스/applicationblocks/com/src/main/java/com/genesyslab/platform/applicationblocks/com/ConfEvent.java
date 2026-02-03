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
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;

/**
 * This interface represents an event fired by Configuration Server.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public interface ConfEvent {
    /**
     * Describes the configuration event types that can be received from Configuration Server.
     */
    public enum EventType {
        /**
         * The event signifies the creation of a Configuration Server object.
         */
        ObjectCreated,

        /**
         * The event signifies the deletion of a Configuration Server object.
         */
        ObjectDeleted,

        /**
         * The event signifies the update of a Configuration Server object.
         */
        ObjectUpdated
    }

    /**
     * The type of event received.
     *
     * @return event type
     */
    EventType getEventType();

    boolean isUnsolicited();

    /**
     * The type of the object about which we're receiving the event.
     *
     * @return configuration object type
     */
    CfgObjectType getObjectType();

    /**
     * The ID of the object about which we're receiving the event.
     *
     * @return object ID
     */
    int getObjectId();

    /**
     * Attached configuration information. It can be object or delta object.
     * Or Null if not applicable.
     *
     * @return configuration object
     */
    ICfgObject getCfgObject();
    
    /**
     * Exposes unsolicited notification event's number.  
     * Returned value can be used with {@link com.genesyslab.platform.configuration.protocol.confserver.requests.connectivity.RequestHistoryLog RequestHistoryLog} to
     * poll previously sent notifications from Configuration Server.   
     * @return unsolicited event ID .
     */
    public int getUnsolicitedEventNumber();
}
