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
package com.genesyslab.platform.apptemplate.lmslogger.log4j2;

import com.genesyslab.platform.apptemplate.lmslogger.LmsLogLevel;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageTemplate;
import com.genesyslab.platform.apptemplate.lmslogger.AbstractLmsEventLogger;

import com.genesyslab.platform.apptemplate.log4j2plugin.Log4j2LmsMessage;

import com.genesyslab.platform.management.protocol.messageserver.LogLevel;
import com.genesyslab.platform.management.protocol.messageserver.LogCategory;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log4J2LoggerImpl;


/**
 * Log4j 2.x based implementation of {@link com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger
 * LmsEventLogger}.
 *
 * @see Log4j2LmsLoggerFactory
 */
public class Log4j2LmsEventLogger extends AbstractLmsEventLogger {

    /**
     * Every LMS message logged by this <code>LmsLogger</code> will bear this marker.
     * It allows marker-aware implementations to perform specific processing on the localized messages.
     */
    public static final org.apache.logging.log4j.Marker PSDK_LMS_MESSAGE_MARKER =
            org.apache.logging.log4j.MarkerManager.getMarker(STR_PSDK_LMS_MESSAGE_MARKER);

    public static final org.apache.logging.log4j.Marker PSDK_MESSAGE_MARKER =
            Log4J2LoggerImpl.PSDK_MESSAGE_MARKER;

    public static final org.apache.logging.log4j.Marker PSDK_INT_MESSAGE_MARKER =
            Log4J2LoggerImpl.PSDK_INT_MESSAGE_MARKER;


    private final org.apache.logging.log4j.Logger logger;
    private final org.apache.logging.log4j.spi.ExtendedLogger extLogger;

    protected Log4j2LmsEventLogger(
            final LmsMessageConveyor imc,
            final org.apache.logging.log4j.Logger logger) {
        super(imc);
        this.logger = logger;
        if (logger instanceof org.apache.logging.log4j.spi.ExtendedLogger) {
            extLogger = (org.apache.logging.log4j.spi.ExtendedLogger) logger;
        } else {
            extLogger = null;
        }
    }

    @Override
    public ILogger createChildLogger(final String name) {
        return this;
    }


    @Override
    public boolean isDebug() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfo() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarn() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isError() {
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isFatalError() {
        return logger.isFatalEnabled();
    }


    /**
     * Log a localized message.
     *
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    @Override
    protected void doLogEvent(
            final LogCategory category,
            final Level logLevel,
            final LmsMessageTemplate key,
            final Object... args) {
        if (key == null) {
            return;
        }

        int level = 0;
        LogLevel ll = getLmsLogLevel(key.getLevel());
        if (ll != null) {
            level = ll.ordinal();
        }

        final org.apache.logging.log4j.Level msgLevel = getLogLevel(key.getLevel());
        final org.apache.logging.log4j.Level userLevel = (logLevel != null ? getLogLevel(logLevel) : null);

        final Log4j2LmsMessage msg = new Log4j2LmsMessage(
                key.getId(),
                key.getName(),
                category.asInteger(),
                level,
                key.getMessage(),
                args);

        final Throwable throwable = msg.getThrowable();

        if (extLogger != null) {
            if (extLogger.isEnabled(msgLevel, PSDK_LMS_MESSAGE_MARKER, msg, throwable)
                    || (userLevel != null
                        && extLogger.isEnabled(userLevel, PSDK_LMS_MESSAGE_MARKER, msg, throwable))) {
                extLogger.logMessage(LOG_WRAPPER_FQCN,
                        (userLevel != null ? userLevel : msgLevel),
                        PSDK_LMS_MESSAGE_MARKER,
                        msg, throwable);
            }
        } else {
            logger.log(
                    (userLevel != null ? userLevel : msgLevel),
                    PSDK_LMS_MESSAGE_MARKER,
                    msg, throwable
            );
        }
    }


    @Override
    protected void log(final Object message, final Throwable thr, final Level level) {
        if (extLogger != null) {
            extLogger.logIfEnabled(LOG_WRAPPER_FQCN, getLogLevel(level), PSDK_MESSAGE_MARKER, message, thr);	
        } else {
            logger.log(getLogLevel(level), PSDK_MESSAGE_MARKER, message, thr);
        }
    }

    @Override
    protected void logArgs(final String message, final Object args, final Level level) {
        if (extLogger != null) {
            extLogger.logIfEnabled(LOG_WRAPPER_FQCN, getLogLevel(level), PSDK_MESSAGE_MARKER,
                    formatMessage(message, args));
        } else {
            logger.log(getLogLevel(level), PSDK_MESSAGE_MARKER, formatMessage(message, args));
        }
    }


    protected static org.apache.logging.log4j.Level getLogLevel(
            final Level logLevel) {
        if (logLevel != null) {
            switch (logLevel.ordinal()) {
            case 0:
                return org.apache.logging.log4j.Level.DEBUG;
            case 1:
                return org.apache.logging.log4j.Level.INFO;
            case 2:
                return org.apache.logging.log4j.Level.WARN;
            case 3:
                return org.apache.logging.log4j.Level.ERROR;
            case 4:
                return org.apache.logging.log4j.Level.FATAL;
            }
        }
        return null;
    }

    protected static org.apache.logging.log4j.Level getLogLevel(
            final LmsLogLevel lmsLogLevel) {
        if (lmsLogLevel != null) {
            switch (lmsLogLevel) {
            case ALARM:
                return org.apache.logging.log4j.Level.FATAL;
            case STANDARD:
                return org.apache.logging.log4j.Level.ERROR;
            case INTERACTION:
                return org.apache.logging.log4j.Level.WARN;
            case TRACE:
                return org.apache.logging.log4j.Level.INFO;
            case DEBUG:
                return org.apache.logging.log4j.Level.DEBUG;
            case UNKNOWN:
                return org.apache.logging.log4j.Level.TRACE;
            }
        }
        return null;
    }

    protected static LogLevel getLmsLogLevel(
            final LmsLogLevel lmsLevel) {

        if (lmsLevel != null) {
            switch (lmsLevel) {
            case ALARM:
                return LogLevel.Alarm;
            case STANDARD:
                return LogLevel.Error;
            case INTERACTION:
                return LogLevel.Interaction;
            case TRACE:
                return LogLevel.Info;
            case DEBUG:
                return LogLevel.Debug;
            case UNKNOWN:
                return LogLevel.Unknown;
            case NONE:
                return LogLevel.None;
            }
        }
        return null;
    }
}
