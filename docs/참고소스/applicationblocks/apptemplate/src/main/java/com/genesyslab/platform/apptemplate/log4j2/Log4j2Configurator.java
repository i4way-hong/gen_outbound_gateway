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

import com.genesyslab.platform.apptemplate.lmslogger.LmsLoggerFactory;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;
import com.genesyslab.platform.apptemplate.lmslogger.impl.MSEventSenderImpl;
import com.genesyslab.platform.apptemplate.lmslogger.log4j2.GMessageServerDeliveryManagerImpl;

import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;

import com.genesyslab.platform.apptemplate.log4j2plugin.GMessageServerAppender;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4J2LoggerFactoryImpl;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.config.Configuration;

import java.util.Map.Entry;


/**
 * This class contains configuration appliance logic for Log4j2 logging framework.
 */
@SuppressWarnings("requires-transitive")
public class Log4j2Configurator {

    private static final StatusLogger LOGGER = StatusLogger.getLogger();

    private static final boolean usePsdkCommonsLog4jCtx = true;
    private static final boolean useCurrentLog4jCtx = false;


    /**
     * Returns Platform SDK common Log4j2 LoggerContext.
     *
     * @return PSDK common LoggerContext.
     */
    @SuppressWarnings("exports")
    public static org.apache.logging.log4j.spi.LoggerContext getLoggerContext() {
        if (usePsdkCommonsLog4jCtx) {
            return Log4J2LoggerFactoryImpl.getLoggerContext();
        }
        return org.apache.logging.log4j.LogManager.getFactory().getContext(
                Log4j2Configurator.class.getName(),
                Log4j2Configurator.class.getClassLoader(),
                null, useCurrentLog4jCtx, null, null);
    }


    /**
     * Parses and applies Log4j2 logging configuration to the LoggerContext.
     * <p/>
     * In case of successful configuration appliance this method also resets
     * Platform SDK Commons and LmsEventLogger loggers factories to their PSDK Log4j2 implementations.
     *
     * @param appConfig the application configuration to extract logging configuration from.
     * @param lmsConveyor reference to new LmsMessageConveyor or null to create default one.
     * @return <code>true</code> if configuration was created and applied, or <code>false</code> if it was not.
     * @throws NullPointerException if given application configuration is <code>null</code>.
     * @see #getLoggerContext()
     * @see com.genesyslab.platform.apptemplate.configuration.GApplicationConfiguration GApplicationConfiguration
     */
    public static boolean applyLoggingConfig(
            final IGApplicationConfiguration appConfig,
            final LmsMessageConveyor lmsConveyor) {
        return applyLoggingConfig(
                PsdkLog4j2Configuration.parse(appConfig),
                appConfig,
                lmsConveyor);
    }

    /**
     * Applies Log4j2 logging configuration to the LoggerContext.
     *
     * @param config the logging configuration to apply.
     * @param appConfig the application configuration. 
     * @param lmsConveyor reference to new LmsMessageConveyor or null to create default one.
     * @return <code>true</code> if configuration successfully applied, or <code>false</code> if it was not.
     * @throws NullPoniterException if given application configuration is <code>null</code>.
     * @see #getLoggerContext()
     * @see com.genesyslab.platform.apptemplate.configuration.GApplicationConfiguration GApplicationConfiguration
     */
    public static boolean applyLoggingConfig(
            final Configuration config,
            final IGApplicationConfiguration appConfig,
            final LmsMessageConveyor lmsConveyor) {
        if (config == null) {
            LOGGER.warn("Got no logging options in the given configuration");
            return false;
        }

        LOGGER.warn("Setting log configuration: " + config);
        final org.apache.logging.log4j.spi.LoggerContext logCtx = setConfig(config);

        if (logCtx != null) {
            // Set/Reset the PSDK Common loggers factory:
            Log.setLoggerFactory(Log.LOG_FACTORY_LOG4J2);
        }

        LmsMessageConveyor mc = lmsConveyor;
        if (mc == null) {
            LmsLoggerFactory llf = LmsLoggerFactory.getLoggerFactory();
            if (llf != null) {
                mc = llf.getMessageConveyor();
                if (mc != null && appConfig != null) {
                    mc = mc.clone();
                }
            }
        }
        if (mc == null) {
            mc = new LmsMessageConveyor();
        }
        mc.loadConfiguration(appConfig);

        if (logCtx != null) {
            // Set/Reset the AppTemplate LMS loggers factory:
            LmsLoggerFactory.setLoggerFactoryImpl(Log.LOG_FACTORY_LOG4J2, mc);
        }

        if (logCtx instanceof org.apache.logging.log4j.core.LoggerContext) {
            final Configuration configActual = ((org.apache.logging.log4j.core.LoggerContext) logCtx)
                    .getConfiguration();
            for (Entry<String, Appender> entry: configActual.getAppenders().entrySet()) {
                if (entry.getValue() instanceof GMessageServerAppender) {
                    final String appenderName = entry.getKey();
                    final GMessageServerAppender appender = (GMessageServerAppender) entry.getValue();
                    if (PsdkLog4j2Configuration.PSDK_APPTPL_MS_APPENDER_NAME.equals(appenderName)) {
                        try {
                            appender.setupDeliveryManager(
                                    new GMessageServerDeliveryManagerImpl(appenderName,
                                            new MSEventSenderImpl(appenderName, appConfig)));
                        } catch (Exception ex) {
                            LOGGER.warn("Failed to initialize MSEventSender", ex);
                        }
                    } else {
                        appender.setupDeliveryManager(
                                new GMessageServerDeliveryManagerImpl(appenderName, null));
                    }
                }
            }
            return true;
        } else if (logCtx != null) {
            LOGGER.warn("Unsupported LoggerContext implementation: " + logCtx.getClass());
        }

        return false;
    }


    /**
     * Applies given Log4j2 configuration to Log4j2 Core PSDK LoggerContext.
     *
     * @param config Log4j2 logging configuration description structure.
     * @return reference to Log4j2 logger context if configuration has been successfully passed to,
     *         or <code>null</code>.
     * @see #getLoggerContext()
     * @see #applyLoggingConfig(IGApplicationConfiguration, LmsMessageConveyor)
     * @see org.apache.logging.log4j.core.LoggerContext#start(Configuration)
     */
    @SuppressWarnings("exports")
    public static org.apache.logging.log4j.spi.LoggerContext setConfig(
            final Configuration config) {
        try {
            org.apache.logging.log4j.spi.LoggerContext loggerContextSpi = getLoggerContext();
            if (loggerContextSpi instanceof org.apache.logging.log4j.core.LoggerContext) {
                @SuppressWarnings("resource")
                final org.apache.logging.log4j.core.LoggerContext context =
                        (org.apache.logging.log4j.core.LoggerContext) loggerContextSpi;
                loggerContextSpi.getLogger(Log4j2Configurator.class.getName()).info(
                        "Changing Log4j configuration [ctx:" + context.getName() + "] to: " + config);
                context.start(config);
                return loggerContextSpi;
            }
            LOGGER.error("Failed to setup Log4j2 configuration - unsupported LoggerContext type: {}",
                    (loggerContextSpi != null) ? loggerContextSpi.getClass().getName() : null);
        } catch (final Throwable thr) {
            LOGGER.error("Failed to setup Log4j2 configuration", thr);
        }
        return null;
    }
}
