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
package com.genesyslab.platform.apptemplate.lmslogger.slf4j;

import com.genesyslab.platform.apptemplate.lmslogger.LmsLogLevel;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageTemplate;
import com.genesyslab.platform.apptemplate.lmslogger.AbstractLmsEventLogger;

import com.genesyslab.platform.management.protocol.messageserver.LogCategory;
import com.genesyslab.platform.management.protocol.messageserver.AttributeList;

import com.genesyslab.platform.commons.log.ILogger;

import org.slf4j.MDC;

import java.util.Map;


/**
 * SLF4J logging interface based implementation of AppTemplate
 * {@link com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger LmsEventLogger}.
 *
 * @see Slf4jLmsLoggerFactory
 */
public class Slf4jLmsEventLogger extends AbstractLmsEventLogger {

    /**
     * Every plain message logged by a LmsLogger will bear this marker.
     */
    public static org.slf4j.Marker PSDK_MESSAGE_MARKER =
            org.slf4j.MarkerFactory.getMarker("PSDK_MESSAGE");

    /**
     * Every LMS localized message logged by a LmsLogger will bear this marker.
     * It allows marker-aware implementations to perform additional processing on localized messages.
     */
    public static org.slf4j.Marker PSDK_LMS_MESSAGE_MARKER =
            org.slf4j.MarkerFactory.getMarker(STR_PSDK_LMS_MESSAGE_MARKER);

    /**
     * Every LMS localized Alarm level message logged by a LmsLogger will bear this marker.
     * It allows marker-aware implementations to perform additional processing on localized messages.
     */
    public static org.slf4j.Marker PSDK_LMS_ALARM_MESSAGE_MARKER =
            org.slf4j.MarkerFactory.getMarker(STR_PSDK_LMS_ALARM_MESSAGE_MARKER);

    static {
        PSDK_LMS_ALARM_MESSAGE_MARKER.add(PSDK_LMS_MESSAGE_MARKER);
    }


    private final org.slf4j.Logger logger;
    private final org.slf4j.spi.LocationAwareLogger laLogger;


    protected Slf4jLmsEventLogger(final LmsMessageConveyor imc, final org.slf4j.Logger logger) {
        super(imc);
        this.logger = logger;
        if (logger instanceof org.slf4j.spi.LocationAwareLogger) {
            this.laLogger = (org.slf4j.spi.LocationAwareLogger) logger;
        } else {
            this.laLogger = null;
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
        return logger.isErrorEnabled();
    }


    @Override
    protected void doLogEvent(
            final LogCategory category,
            final Level logLevel,
            final LmsMessageTemplate key,
            final Object... args) {
        String translatedMsg = null;
        Throwable throwable = null;
        LmsLogLevel level = null;
        if (key != null) {
            level = key.getLevel();
            translatedMsg = key.getMessage();
        }
        if (translatedMsg != null) {
            if (args != null && args.length > 0) {
                try {
                    translatedMsg = String.format(translatedMsg, args);
                } catch (final Exception ex) {
                    throwable = ex;
                }
            }
        } else {
            translatedMsg = "null";
        }

        AttributeList gmsAttrs = null;
        if (args != null && args.length > 0) {
            Object obj = args[args.length - 1];
            if (obj instanceof Throwable) {
                throwable = (Throwable) obj;
                if (args.length > 1) {
                    obj = args[args.length - 2];
                } else {
                    obj = null;
                }
            }
            if (printAttributes && obj instanceof AttributeList) {
                gmsAttrs = (AttributeList) obj;
            }
        }

        if (category != null) {
            MDC.put(CTX_LMSATTR_CATEGORY, category.name());
        }
        if (level != null) {
            MDC.put(CTX_LMSATTR_LEVEL, level.name());
        }
        if (key != null) {
            MDC.put(CTX_LMSATTR_ID, Integer.toString(key.getId()));
            MDC.put(CTX_LMSATTR_NAME, key.getName());
        }

        if (gmsAttrs != null) {
            for (Map.Entry<String, String> entry: gmsAttrs.entrySet()) {
                MDC.put(CTX_ATTRMAP_PREFIX + entry.getKey(), entry.getValue());
            }
        }

        if (level == null) {
            level = LmsLogLevel.UNKNOWN;
        }

        if (logLevel != null) {
            switch (logLevel.ordinal()) {
            case 0:
                level = LmsLogLevel.DEBUG;
                break;
            case 1:
                level = LmsLogLevel.TRACE;
                break;
            case 2:
                level = LmsLogLevel.INTERACTION;
                break;
            case 3:
                level = LmsLogLevel.STANDARD;
                break;
            case 4:
                level = LmsLogLevel.ALARM;
                break;
            default:
                level = LmsLogLevel.UNKNOWN;
                break;
            }
        }

        try {
            switch (level) {
            case TRACE:
                if (laLogger != null) {
                    laLogger.log(PSDK_LMS_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                            org.slf4j.spi.LocationAwareLogger.INFO_INT,
                            translatedMsg, args, throwable);
                } else {
                    logger.info(
                            PSDK_LMS_MESSAGE_MARKER,
                            translatedMsg,
                            throwable);
                }
                break;

            case DEBUG:
                if (laLogger != null) {
                    laLogger.log(PSDK_LMS_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                            org.slf4j.spi.LocationAwareLogger.DEBUG_INT,
                            translatedMsg, args, throwable);
                } else {
                    logger.debug(
                            PSDK_LMS_MESSAGE_MARKER,
                            translatedMsg,
                            throwable);
                }
                break;

            case INTERACTION:
                if (laLogger != null) {
                    laLogger.log(PSDK_LMS_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                            org.slf4j.spi.LocationAwareLogger.WARN_INT,
                            translatedMsg, args, throwable);
                } else {
                    logger.warn(
                            PSDK_LMS_MESSAGE_MARKER,
                            translatedMsg,
                            throwable);
                }
                break;

            case STANDARD:
                if (laLogger != null) {
                    laLogger.log(PSDK_LMS_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                            org.slf4j.spi.LocationAwareLogger.ERROR_INT,
                            translatedMsg, args, throwable);
                } else {
                    logger.error(
                            PSDK_LMS_MESSAGE_MARKER,
                            translatedMsg,
                            throwable);
                }
                break;

            case ALARM:
                if (laLogger != null) {
                    laLogger.log(PSDK_LMS_ALARM_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                            org.slf4j.spi.LocationAwareLogger.ERROR_INT,
                            translatedMsg, args, throwable);
                } else {
                    logger.error(
                            PSDK_LMS_ALARM_MESSAGE_MARKER,
                            translatedMsg,
                            throwable);
                }
                break;
            case NONE: //Event must not be logged.
                break;
            case UNKNOWN:
            default:
                if (laLogger != null) {
                    laLogger.log(PSDK_LMS_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                            org.slf4j.spi.LocationAwareLogger.DEBUG_INT,
                            translatedMsg, args, throwable);
                } else {
                    logger.debug(
                            PSDK_LMS_MESSAGE_MARKER,
                            translatedMsg,
                            throwable);
                }
                break;
            }

        } finally {
            MDC.remove(CTX_LMSATTR_ID);
            MDC.remove(CTX_LMSATTR_NAME);
            MDC.remove(CTX_LMSATTR_LEVEL);
            MDC.remove(CTX_LMSATTR_CATEGORY);
            if (gmsAttrs != null) {
                for (Map.Entry<String, String> entry: gmsAttrs.entrySet()) {
                    MDC.remove(CTX_ATTRMAP_PREFIX + entry.getKey());
                }
            }
        }
    }


    @Override
    protected void log(final Object message, final Throwable throwable, final Level level) {
        switch (level.ordinal()) {
        case 0: // DEBUG
            if (laLogger != null) {
                laLogger.log(PSDK_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                        org.slf4j.spi.LocationAwareLogger.DEBUG_INT,
                        message.toString(), null, throwable);
            } else {
                logger.debug(
                        PSDK_MESSAGE_MARKER,
                        message.toString(),
                        throwable);
            }
            break;
        case 1: // INFO
            if (laLogger != null) {
                laLogger.log(PSDK_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                        org.slf4j.spi.LocationAwareLogger.INFO_INT,
                        message.toString(), null, throwable);
            } else {
                logger.info(
                        PSDK_MESSAGE_MARKER,
                        message.toString(),
                        throwable);
            }
            break;
        case 2: // WARN
            if (laLogger != null) {
                laLogger.log(PSDK_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                        org.slf4j.spi.LocationAwareLogger.WARN_INT,
                        message.toString(), null, throwable);
            } else {
                logger.warn(
                        PSDK_MESSAGE_MARKER,
                        message.toString(),
                        throwable);
            }
            break;
        case 3: // ERROR
            if (laLogger != null) {
                laLogger.log(PSDK_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                        org.slf4j.spi.LocationAwareLogger.ERROR_INT,
                        message.toString(), null, throwable);
            } else {
                logger.error(
                        PSDK_MESSAGE_MARKER,
                        message.toString(),
                        throwable);
            }
            break;
        case 4: // FATAL_ERROR
            if (laLogger != null) {
                laLogger.log(PSDK_MESSAGE_MARKER, LOG_WRAPPER_FQCN,
                        org.slf4j.spi.LocationAwareLogger.ERROR_INT,
                        message.toString(), null, throwable);
            } else {
                logger.error(
                        PSDK_MESSAGE_MARKER,
                        message.toString(),
                        throwable);
            }
            break;
        }
    }


    @Override
    protected void logArgs(final String message, final Object args, final Level level) {
        log(formatMessage(message, args), getThrowableArg(args), level);
    }
}
