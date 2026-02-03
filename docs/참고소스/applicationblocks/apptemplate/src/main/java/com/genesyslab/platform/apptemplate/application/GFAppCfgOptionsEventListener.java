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
package com.genesyslab.platform.apptemplate.application;

import com.genesyslab.platform.apptemplate.application.GFAppCfgEvent.AppCfgEventType;

import com.genesyslab.platform.applicationblocks.commons.Predicate;


/**
 */
public abstract class GFAppCfgOptionsEventListener
        extends GFAppCfgEventListener {

    public static final Predicate<GFAppCfgEvent> THE_APP_CONFIGDATA_FILTER =
            new TheAppConfigDataFilter();


    @Override
    public Predicate<GFAppCfgEvent> getFilter() {
        return THE_APP_CONFIGDATA_FILTER;
    }

    /**
     * This method will be called on configuration managers' {@link GFAppCfgEvent} after applying
     * of the application configuration options event filter {@link #getFilter()}
     * (returning {@link #THE_APP_CONFIGDATA_FILTER}).<br/>
     * This filter passes {@link AppCfgEventType#AppConfigReceived} and {@link AppCfgEventType#AppConfigUpdated}
     * events only.
     */
    @Override
    public abstract void handle(GFAppCfgEvent obj);


    protected static class TheAppConfigDataFilter implements Predicate<GFAppCfgEvent> {
        @Override
        public boolean invoke(final GFAppCfgEvent event) {
            if (event == null) {
                return false;
            }
            GFApplicationContext appCtx = event.getAppContext();
            if (appCtx == null) {
                return false;
            }
            AppCfgEventType evType = event.getEventType();
            return AppCfgEventType.AppConfigReceived.equals(evType)
                    || AppCfgEventType.AppConfigUpdated.equals(evType);
        }
    }
}
