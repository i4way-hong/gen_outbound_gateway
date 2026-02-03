// ===============================================================================
//  Genesys Platform SDK
// ===============================================================================
//
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:
//
// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.
//
// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.
//
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
//
// Copyright (c) 2009 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.apptemplate.configuration.log;

import com.genesyslab.platform.apptemplate.lmslogger.LmsLogLevel;
import com.genesyslab.platform.apptemplate.util.ConfigurationUtil;

import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.NullLoggerImpl;

import java.util.Map;
import java.util.Locale;
import java.util.HashMap;


/**
 * Parser of "log-extended" section of CME Application objects' Options.
 * <p/>
 * This class is automatically used by
 * {@link com.genesyslab.platform.apptemplate.application.GFApplicationConfigurationManager
 * Application Configuration Manager} and
 * {@link com.genesyslab.platform.apptemplate.log4j2.Log4j2Configurator Log4j2 Configurator}.
 * For a case, when application does not use the configuration manager, does need some custom logging configuration,
 * or for some testing purposes, it is possible to use this class to create logging configuration.<br/>
 * Simple application configuration usage:<code><pre>
 * CfgApplication theApp = confService.retrieveObject(
 *         CfgApplication.class, new CfgApplicationQuery(myAppName));
 * GApplicationConfiguration <b>appConfig</b> = new GCOMApplicationConfiguration(theApp);
 *
 * <b>GAppLoggingOptions</b> logOpts = new <b>GAppLoggingOptions</b>(<b>appConfig</b>, null);
 *
 * <b>GAppLogExtOptions</b> <b><u>logExtOpts</u></b> = <b>logOpts</b>.getLogExtendedOptions();
 * </pre></code>
 * Or simple initialization without application configuration reading (without ConfService usage):<code><pre>
 * KeyValueCollection <b>logSection</b> = new KeyValueCollection();
 * <b>logSection</b>.addString("verbose", "all");
 * <b>logSection</b>.addString("message-format", "full");
 * <b>logSection</b>.addString("standard", "Log4j2ConfiguratorTest-std");
 * <b>logSection</b>.addString("all", "stdout, Log4j2ConfiguratorTest-all");
 *
 * KeyValueCollection <b>logExtSection</b> = new KeyValueCollection();
 * <b>logExtSection</b>.addString("<b>level-reassign-</b>14005", "ALARM");
 * <b>logExtSection</b>.addString("<b>level-reassign-</b>14006", "ALARM");
 * <b>logExtSection</b>.addString("<b>logger-</b>psdk", "com.genesyslab.platform: level=debug");
 * <b>logExtSection</b>.addString("<b>logger-</b>apache", "org.apache: level=error, additivity=false, includeLocation=false");
 *
 * <b>GAppLoggingOptions</b> <b>logOpts</b> = new <b>GAppLoggingOptions</b>(<b>logSection</b>, <b>logExtSection</b>, null);
 *
 * <b>GAppLogExtOptions</b> <b><u>logExtOpts</u></b> = <b>logOpts</b>.getLogExtendedOptions();
 * </pre></code>
 */
public class GAppLogExtOptions implements Cloneable {

    public static final String LEVEL_REASSIGN_OPT_PREFIX = "level-reassign-";
    public static final String LEVEL_REASSIGN_DISABLE_EXT_OPT = "level-reassign-disable";

    public static final String LOG_EXT_LOGGER_PREFIX = "logger-";

    private final Map<Integer, LmsLogLevel> levelReassigns;
    private final Map<String, CustomLoggerExtConfig> customLoggers;

    private final ILogger logger;


    /**
     * Parsing constructor of "log-extended" section of application Options.<code><pre>
     * KeyValueCollection <b>logExtSection</b> = new KeyValueCollection();
     * <b>logExtSection</b>.addString("<b>level-reassign-</b>14005", "ALARM");
     * <b>logExtSection</b>.addString("<b>level-reassign-</b>14006", "ALARM");
     * <b>logExtSection</b>.addString("<b>logger-</b>psdk", "com.genesyslab.platform: level=debug");
     * <b>logExtSection</b>.addString("<b>logger-</b>apache", "org.apache: level=error");
     *
     * <b>GAppLogExtOptions</b> logExtOpts = new <b>GAppLogExtOptions</b>(<b>logExtSection</b>, null);
     * </pre></code>
     *
     * @param logExtOptions the application "log-extended" Options section.
     * @param logger optional "status" logger to print errors of options parsing methods.
     * @see GAppLoggingOptions
     */
    public GAppLogExtOptions(
            final KeyValueCollection logExtOptions,
            final ILogger logger) {
        if (logger != null) {
            this.logger = logger;
        } else {
            this.logger = NullLoggerImpl.SINGLETON;
        }

        if (logExtOptions != null) {
            customLoggers = parseCustomLoggers(logExtOptions);
            levelReassigns = parseLevelReassignments(logExtOptions);
        } else {
            customLoggers = null;
            levelReassigns = null;
        }
    }


    @Override
    public GAppLogExtOptions clone() {
        try {
            return (GAppLogExtOptions) super.clone();
        } catch (final CloneNotSupportedException e) {
            // Its not expected
            return this;
        }
    }


    /**
     * Returns LMS events levels reassignment configuration.<br/>
     * It represents extended logging options like following:<pre>
     * [log-extended]
     *   "<b>level-reassign-</b>14005" = "ALARM"
     *   "<b>level-reassign-</b>14006" = "ALARM"
     * </pre>
     * This value is <code>null</code> if there are no reassignment declarations or reassignment
     * is disabled with<pre>
     * [log-extended]
     *   "<b>level-reassign-disabled</b>" = "true"
     * </pre>
     *
     * @return map with levels reassignment declarations or null.
     */
    public Map<Integer, LmsLogLevel> getLevelReassigns() {
        return levelReassigns;
    }

    /**
     * Returns loggers customization options.<br/>
     * It represents extended logging options like following:<pre>
     * [log-extended]
     *   "<b>logger-</b>psdk" = "com.genesyslab.platform: level=debug"
     *   "<b>logger-</b>apache" = "org.apache: level=error"
     * </pre>
     * This sample represents a map with two elements:<pre>
     *   "com.genesyslab.platform" =&gt; CustomLoggerExtConfig(
     *                                       ID = "psdk",
     *                                       Name = "com.genesyslab.platform",
     *                                       Property["level"] = "debug"
     *                                ),
     *   "org.apache"              =&gt; CustomLoggerExtConfig(
     *                                       ID = "apache",
     *                                       Name = "org.apache",
     *                                       Property["level"] = "error"
     *                                )
     * </pre>
     * This value is <code>null</code> if there are no loggers configuration declarations.
     *
     * @return map with loggers customization options or null.
     * @see CustomLoggerExtConfig
     */
    public Map<String, CustomLoggerExtConfig> getCustomLoggers() {
        return customLoggers;
    }


    /**
     * Extracts map of LMS events level reassignment configuration from "log-extended" section
     * of the application configuration object.
     *
     * @param optsLogExt the extended log options of the application.
     * @return filled map, or null if there are no reassignment declarations or reassignment is disabled.
     */
    protected Map<Integer, LmsLogLevel> parseLevelReassignments(
            final KeyValueCollection optsLogExt) {
        final String disableReassign = optsLogExt.getString(LEVEL_REASSIGN_DISABLE_EXT_OPT);
        if (disableReassign == null || !ConfigurationUtil.isTrue(disableReassign)) {
            HashMap<Integer, LmsLogLevel> result = null;
            for (Object kvpo: optsLogExt) {
                final KeyValuePair kvp = (KeyValuePair) kvpo;
                final String key = kvp.getStringKey().toLowerCase(Locale.ENGLISH);
                if (key.startsWith(LEVEL_REASSIGN_OPT_PREFIX) && !key.equals(LEVEL_REASSIGN_DISABLE_EXT_OPT)) {
                    final String sid = key.substring(LEVEL_REASSIGN_OPT_PREFIX.length());
                    final String lvl = kvp.getStringValue();
                    try {
                        final int id = Integer.parseInt(sid);
                        final LmsLogLevel level = Enum.valueOf(LmsLogLevel.class, lvl.toUpperCase(Locale.ENGLISH));
                        if (level != null) {
                            if (result == null) {
                                result = new HashMap<Integer, LmsLogLevel>();
                            }
                            result.put(id, level);
                        } else {
                            if (logger.isWarn()) {
                                logger.warn("Unknown reassignment log level for '" + id
                                        + "' = '" + lvl + "'");
                            }
                        }
                    } catch (final Exception ex) {
                        if (logger.isWarn()) {
                            logger.warn("Error parsing log level reassignment: '" + sid
                                    + "' = '" + lvl + "'", ex);
                        }
                    }
                }
            }
            return result;
        }
        return null;
    }

    /**
     * Parsing method for custom loggers declarations.
     *
     * @param optsLogExt the extended log options of the application.
     * @return map with parsed loggers customization options.
     * @see CustomLoggerExtConfig
     */
    protected Map<String, CustomLoggerExtConfig> parseCustomLoggers(
            final KeyValueCollection optsLogExt) {
        Map<String, CustomLoggerExtConfig> result = null;
        for (final Object obj: optsLogExt) {
            final KeyValuePair pair = (KeyValuePair) obj;
            final String name = pair.getStringKey();
            if (name.startsWith(LOG_EXT_LOGGER_PREFIX)) {
                final String loggerId = name.substring(LOG_EXT_LOGGER_PREFIX.length());
                try {
                    final CustomLoggerExtConfig logConf =
                            new CustomLoggerExtConfig(loggerId, pair.getStringValue());
                    if (result == null) {
                        result = new HashMap<String, CustomLoggerExtConfig>();
                    }
                    result.put(logConf.getName(), logConf);
                } catch (final Exception ex) {
                    if (logger.isError()) {
                        logger.error("Exception parsing custom logger configuration \"" + loggerId + "\"", ex);
                    }
                }
            }
        }
        return result;
    }
}
