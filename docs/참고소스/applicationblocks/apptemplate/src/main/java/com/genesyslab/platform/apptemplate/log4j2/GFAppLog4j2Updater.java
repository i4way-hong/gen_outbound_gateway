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
package com.genesyslab.platform.apptemplate.log4j2;

import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;
import com.genesyslab.platform.apptemplate.application.GFApplicationContext;
import com.genesyslab.platform.apptemplate.application.GFAppCfgLogOptionsEventListener;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;

import com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaApplication;

import com.genesyslab.platform.commons.log.Log4J2Checker;


/**
 * Application logging configuration applier/updater for Log4j2.
 */
public class GFAppLog4j2Updater extends GFAppCfgLogOptionsEventListener {

    /**
     * Creates an instance of Log4j2 logging configuration updater.
     *
     * @throws IllegalArgumentException if no Log4j2 API or Log4j2 CORE library available.
     */
	public GFAppLog4j2Updater() {
        if (!Log4J2Checker.isAvailable()
                || !Log4J2Checker.isCoreAvailable()) {
            throw new IllegalArgumentException(
                    "GFAppLog4j2Updater can't be instantiated - no log4j2-core API available");
        }
    }

    /**
     * Parses logging options of given application configuration and applies it
     * to the Log4j2 logging framework.
     *
     * @param appCtx the application configuration managers context.
     * @param appConfig the new application configuration to apply.
     * @param deltaApp the application delta object caused the configuration update, or null.
     * @param lmsMessages the LMS messages conveyor to update correspondent options to.
     * @return <code>true</code> - if application logging configuration was updated with new configuration,
     *         <code>false</code> - if no changes were applied.
     */
    @Override
    protected boolean applyLoggingOptions(
            final GFApplicationContext appCtx,
            final IGApplicationConfiguration appConfig,
            final CfgDeltaApplication deltaApp,
            final LmsMessageConveyor lmsMessages) {
        return Log4j2Configurator.applyLoggingConfig(appConfig, lmsMessages);
    }
}
