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

import com.genesyslab.platform.commons.GEnum;


/**
 * <c>WarmStandbyState</c> enumeration lists all the states the <c>WarmStandbyService</c> can have.
 */
public final class WarmStandbyState extends GEnum {
    private static int cnt = 0;

    /**
     * WarmStandbyService is stopped/deactivated.
     */
    public static final WarmStandbyState OFF = new WarmStandbyState("Off");

    /**
     * WarmStandbyService is started/activated and is monitoring the
     * channel state transitions.
     */
    public static final WarmStandbyState IDLE = new WarmStandbyState("Idle");

    /**
     * WarmStandbyService is active and trying to reconnect the channel.
     */
    public static final WarmStandbyState RECONNECTING =
                new WarmStandbyState("Reconnecting");

    /**
     * WarmStandbyService is active and waiting for the reconnect
     * timeout to expire.
     */
    public static final WarmStandbyState WAITING =
                new WarmStandbyState("Waiting");


    private WarmStandbyState(final String name) {
        super(cnt++, name, WarmStandbyState.class);
    }
}
