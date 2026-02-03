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

import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;
import com.genesyslab.platform.apptemplate.configuration.GApplicationConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGAppConnConfiguration;
import com.genesyslab.platform.apptemplate.application.GFAppCfgEvent.AppCfgEventType;

import com.genesyslab.platform.applicationblocks.com.ICfgObject;
import com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaApplication;

import com.genesyslab.platform.applicationblocks.commons.Predicate;

import com.genesyslab.platform.configuration.protocol.types.CfgAppType;

import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.util.List;
import java.util.Collection;


/**
 * Abstract base class for appliance and update of the application logging
 * framework configuration by incoming application configuration from
 * the application configuration manager ({@link GFApplicationConfigurationManager}).
 * <p/>
 * It allows applications to implement automatic configuration update for
 * custom/own logging frameworks.<br/>
 * For example, it may be done with the following code sample:<code><pre>
 * GFApplicationConfigurationManager appManager =
 *         GFApplicationConfigurationManager.createManager(confService);
 * appManager.register(new <b>MyLogFwkConfigurer</b>());
 * appManager.init();
 *
 * public class <b>MyLogFwkConfigurer</b> extends GFAppCfgLogOptionsEventListener {
 *     @Override
 *     protected boolean applyLoggingOptions(
 *             final GFApplicationContext appCtx,
 *             final IGApplicationConfiguration appConfig,
 *             final CfgDeltaApplication deltaApp,
 *             final LmsMessageConveyor lmsMessages) {
 *         boolean lmsUpdated = super.applyLoggingOptions(appCtx, appConfig, deltaApp, lmsMessages);
 *         if (lmsUpdated) {
 *             // update custom LmsLoggerFactory with updated lmsMessages (if exists)
 *         }
 *         KeyValueCollection logSection = null;
 *         KeyValueCollection appOptions = appConfig.getOptions();
 *         if (appOptions != null) {
 *             logSection = appOptions.getList(LOG_SECTION_NAME);
 *         }
 *         if (logSection != null) {
 *             GAppLoggingOptions logOpts = new GAppLoggingOptions(logSection, null);
 *             // apply 'logOpts' to own log framework...
 *         } else {
 *             // no 'log' section in the application configuration - apply default...
 *         }
 *         return true;
 *     }
 * }
 * </pre></code>
 *
 * @see com.genesyslab.platform.apptemplate.log4j2.GFAppLog4j2Updater GFAppLog4j2Updater
 */
public abstract class GFAppCfgLogOptionsEventListener extends GFAppCfgOptionsEventListener {

    protected final static String LOG_SECTION_NAME     = "log";
    protected final static String LOG_EXT_SECTION_NAME = "log-extended";


    public static final Predicate<GFAppCfgEvent> THE_APP_LOG_OPTIONS_FILTER =
            new TheAppLogOptionsFilter();


    public GFAppCfgLogOptionsEventListener() {
    }


    @Override
    public Predicate<GFAppCfgEvent> getFilter() {
        return THE_APP_LOG_OPTIONS_FILTER;
    }

    @Override
    public void handle(final GFAppCfgEvent event) {
        if (event == null) {
            return;
        }
        GFApplicationContext appCtx = event.getAppContext();
        if (appCtx == null) {
            return;
        }
        IGApplicationConfiguration newAppConfig = event.getAppConfig();
        if (AppCfgEventType.AppConfigReceived.equals(event.getEventType())) {
            IGApplicationConfiguration thisAppConfig = appCtx.getConfiguration();
            if (thisAppConfig == null || newAppConfig == null) {
                onAppConfigReceived(appCtx, newAppConfig);
            } else {
                Integer thisAppDbid = thisAppConfig.getDbid();
                if (thisAppDbid == null) {
                    onAppConfigReceived(appCtx, newAppConfig);
                } else if (thisAppDbid.equals(newAppConfig.getDbid())) {
                    if (logOptionsChanged(appCtx, newAppConfig, null)) {
                        onAppConfigUpdated(appCtx, newAppConfig, null);
                    } else {
                        onAppConfigReceived(appCtx, newAppConfig);
                    }
                }
            }
        } else if (AppCfgEventType.AppConfigUpdated.equals(event.getEventType())) {
            ICfgObject obj = event.getConfData();
            IGApplicationConfiguration thisAppConfig = appCtx.getConfiguration();
            if (thisAppConfig != null) {
                if (logOptionsChanged(appCtx, newAppConfig, (CfgDeltaApplication) obj)) {
                    onAppConfigUpdated(appCtx, newAppConfig, (CfgDeltaApplication) obj);
                } else {
                    onAppConfigReceived(appCtx, newAppConfig);
                }
            } else {
                // TODO what if ConfigUpdated is arrived before ConfigReceived?
            }
        }
    }


    protected void onAppConfigReceived(
            final GFApplicationContext appCtx,
            final IGApplicationConfiguration appConfig) {
        LmsMessageConveyor lmsMessages = appCtx.getLmsLoggerFactory().getMessageConveyor();
        if (lmsMessages != null) {
            lmsMessages = lmsMessages.clone();
        }
        applyLoggingOptions(appCtx, appConfig, null, lmsMessages);
    }

    protected void onAppConfigUpdated(
            final GFApplicationContext appCtx,
            final IGApplicationConfiguration appConfig,
            final CfgDeltaApplication deltaApp) {
        LmsMessageConveyor lmsMessages = appCtx.getLmsLoggerFactory().getMessageConveyor();
        if (lmsMessages != null) {
            lmsMessages = lmsMessages.clone();
        }
        applyLoggingOptions(appCtx, appConfig, deltaApp, lmsMessages);
    }


    /**
     * Applies new logging options to given <code>LmsMessagesConveyor</code>.
     *
     * @param appCtx the application configuration managers context.
     * @param appConfig the new application configuration to apply.
     * @param deltaApp the application delta object caused the configuration update, or null.
     * @param lmsMessages the LMS messages conveyor to update correspondent options to.
     * @return <code>true</code> - if <code>lmsMessages</code> was updated with new configuration,
     *         <code>false</code> - if <code>appConfig</code> or <code>lmsMessages</code> is null.
     */
    protected boolean applyLoggingOptions(
            final GFApplicationContext appCtx,
            final IGApplicationConfiguration appConfig,
            final CfgDeltaApplication deltaApp,
            final LmsMessageConveyor lmsMessages) {
        if (appConfig != null && lmsMessages != null) {
            lmsMessages.loadConfiguration(appConfig);
            return true;
        }
        return false;
    }


    protected static class TheAppLogOptionsFilter extends TheAppConfigDataFilter {
        @Override
        public boolean invoke(final GFAppCfgEvent event) {
            if (!super.invoke(event)) {
                return false;
            }
            CfgDeltaApplication deltaApp = null;
            ICfgObject cfgObj = event.getConfData();
            if (cfgObj instanceof CfgDeltaApplication) {
                deltaApp = (CfgDeltaApplication) cfgObj;
            }
            return logOptionsChanged(
                    event.getAppContext(),
                    event.getAppConfig(),
                    deltaApp);
        }
    }


    protected static boolean logOptionsChanged(
            final GFApplicationContext appCtx,
            final IGApplicationConfiguration appConfig,
            final CfgDeltaApplication deltaApp) {
        if (deltaApp != null) {
            KeyValueCollection kvl = deltaApp.getAddedOptions();
            if (kvl != null) {
                if (kvl.getPair(LOG_SECTION_NAME) != null
                        || kvl.getPair(LOG_EXT_SECTION_NAME) != null) {
                    return true;
                }
            }
            kvl = deltaApp.getChangedOptions();
            if (kvl != null) {
                if (kvl.getPair(LOG_SECTION_NAME) != null
                        || kvl.getPair(LOG_EXT_SECTION_NAME) != null) {
                    return true;
                }
            }
            kvl = deltaApp.getDeletedOptions();
            if (kvl != null) {
                if (kvl.isEmpty() && appCtx != null) {
                    kvl = appCtx.getConfiguration().getOptions();
                }
                if (kvl != null) {
                    if (kvl.getPair(LOG_SECTION_NAME) != null
                            || kvl.getPair(LOG_EXT_SECTION_NAME) != null) {
                        return true;
                    }
                }
            }
            if (containsMessageServerConnection(deltaApp.getChangedAppServers())
                    || containsMessageServerConnection(deltaApp.getAddedAppServers())
                    || containsMessageServerConnection(deltaApp.getDeletedAppServers())) {
                return true;
            }
            return false;
        } else { // delta is null -> compare log sections
            IGApplicationConfiguration appConfig0 = appCtx.getConfiguration();
            if (appConfig0 == null && appConfig == null) {
                return false;
            }
            if (appConfig0 == null || appConfig == null) {
                return true;
            }

            KeyValueCollection opts = appConfig.getOptions();
            KeyValueCollection opts0 = appConfig0.getOptions();
            if (diffSection(opts0, opts, LOG_SECTION_NAME)
                    || diffSection(opts0, opts, LOG_EXT_SECTION_NAME)) {
                return true;
            }

            List<IGAppConnConfiguration> cnts = GApplicationConfiguration
                    .getAppServers(appConfig.getAppServers(), CfgAppType.CFGMessageServer);
            if (cnts != null && cnts.isEmpty()) {
                cnts = null;
            }
            List<IGAppConnConfiguration> cnts0 = GApplicationConfiguration
                    .getAppServers(appConfig0.getAppServers(), CfgAppType.CFGMessageServer);
            if (cnts0 != null && cnts0.isEmpty()) {
                cnts0 = null;
            }
            if (cnts != null && cnts0 != null) {
                if (cnts.size() != cnts0.size()) {
                    return true;
                }
                for (final IGAppConnConfiguration cnt: cnts0) {
                    if (!cnts.remove(cnt)) {
                        return true;
                    }
                }
                if (!cnts.isEmpty()) {
                    return true;
                }
            } else if (cnts != null || cnts0 != null) {
                return true;
            }

            return false;
        }
    }

    protected static boolean containsMessageServerConnection(
            final Collection<CfgConnInfo> connections) {
        if (connections != null) {
            for (final CfgConnInfo cnt: connections) {
                if (cnt != null) {
                    final CfgApplication app = cnt.getAppServer();
                    if (app != null && CfgAppType.CFGMessageServer.equals(app.getType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected static boolean diffSection(
            final KeyValueCollection opts1,
            final KeyValueCollection opts2,
            final String section) {
        KeyValuePair sect1 = null;
        KeyValuePair sect2 = null;
        if (opts1 != null) {
            sect1 = opts1.getPair(section);
        }
        if (opts2 != null) {
            sect2 = opts2.getPair(section);
        }
        if (sect1 != null && sect2 != null) {
            return !sect1.getValue().equals(sect2.getValue());
        }
        return (sect1 != null) || (sect2 != null);
    }
}
