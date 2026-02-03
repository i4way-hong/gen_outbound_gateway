// ===============================================================================
//  Genesys Platform SDK Application Blocks
// ===============================================================================
//
//  Any authorized distribution of any copy of this code (including any related
//  documentation) must reproduce the following restrictions, disclaimer and copyright
//  notice:
//
//  The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
//  (even as a part of another name), endorse and/or promote products derived from
//  this code without prior written permission from Genesys Telecommunications
//  Laboratories, Inc.
//
//  The use, copy, and/or distribution of this code is subject to the terms of the Genesys
//  Developer License Agreement.  This code shall not be used, copied, and/or
//  distributed under any other license agreement.
//
//  THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
//  ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
//  DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
//  WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
//  NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
//  NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
//  EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
//  CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
//  NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
//  Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.applicationblocks.warmstandby;

import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;


/**
 * Connection failure context interface will be provided by WarmStandbyService
 * to custom WarmStandbyConnectionFailureHandler in case of connection failure.
 *
 * @see WarmStandbyConnectionFailureHandler
 * @see WarmStandbyService
 */
public interface WarmStandbyConnectionFailureContext {

    /**
     * Gets the reason of the connection failure.
     *
     * @return channel closed event
     */
    ChannelClosedEvent getEvent();

    /**
     * Gets the service instance where connection failure happened.
     *
     * @return WarmStandby service reference
     */
    WarmStandbyService getService();

    /**
     * Requests to change "active" and "standby" endpoints
     * of the WarmStandby service. After this operation <code>scheduleReconnect()</code>
     * should be called to schedule the connection reopen task.
     *
     * @see #scheduleReconnect(long)
     */
    void doSwitchover();

    /**
     * Sets service state to "off" and clears reconnect counters.
     */
    void doStandbyOff();

    /**
     * This method allows user to notify the WarmStandby service when
     * channel reopen action should be done after current failure event.
     * It will initiate beginOpen() procedure on the protocol connection
     * instance served by the current WarmStandbyService.
     *
     * @param millisecDelay delay before reopen try
     */
    void scheduleReconnect(long millisecDelay);
}
