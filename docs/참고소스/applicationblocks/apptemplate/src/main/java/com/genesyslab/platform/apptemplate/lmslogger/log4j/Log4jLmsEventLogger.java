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
package com.genesyslab.platform.apptemplate.lmslogger.log4j;

import com.genesyslab.platform.apptemplate.lmslogger.LmsLogLevel;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageTemplate;
import com.genesyslab.platform.apptemplate.lmslogger.AbstractLmsEventLogger;

import com.genesyslab.platform.management.protocol.messageserver.LogLevel;
import com.genesyslab.platform.management.protocol.messageserver.LogCategory;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.AbstractLogger;


/**
 * Log4j 1.x based implementation of {@link com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger
 * LmsEventLogger}.
 *
 * @see Log4jLmsLoggerFactory
 */
public class Log4jLmsEventLogger extends AbstractLmsEventLogger {

    private final org.apache.log4j.Logger logger;


    protected Log4jLmsEventLogger(
            final LmsMessageConveyor imc,
            final org.apache.log4j.Logger logger) {
        super(imc);
        this.logger = logger;
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
    @SuppressWarnings("deprecation")
    public boolean isError() {
        return logger.isEnabledFor(org.apache.log4j.Priority.ERROR);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFatalError() {
        return logger.isEnabledFor(org.apache.log4j.Priority.FATAL);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isWarn() {
        return logger.isEnabledFor(org.apache.log4j.Priority.WARN);
    }


    @Override
    protected void log(final Object message, final Throwable thr, final Level level) {
        logger.log(LOG_WRAPPER_FQCN, getLogLevel(level), message, thr);
    }

    @Override
    protected void logArgs(final String message, final Object args, final Level level) {
        logger.log(LOG_WRAPPER_FQCN, getLogLevel(level),
                AbstractLogger.formatMessage(message, args), (Throwable) null);
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

        String translatedMsg = key.getMessage();
        Throwable throwable = null;
        if (args != null && args.length > 0) {
            try {
                translatedMsg = String.format(translatedMsg, args);
            } catch (final Exception ex) {
                throwable = ex;
            }
        }

        if (args != null && args.length > 0) {
            Object obj = args[args.length - 1];
            if (obj instanceof Throwable) {
                throwable = (Throwable) obj;
            }
        }
        if (logLevel != null || !LmsLogLevel.NONE.equals(key.getLevel())) {//Only log events with a log level different from NONE.
            logger.log(LOG_WRAPPER_FQCN,
                    (logLevel != null ? getLogLevel(logLevel) : getLogLevel(key.getLevel())),
                    translatedMsg,
                    throwable);
        }
    }

    /*
    protected void doLogEventStruct(
            final LogCategory category,
            final LmsMessageTemplate key,
            final Object... args) {
        if (key == null) {
            return;
        }
        String translatedMsg = String.format(key.getMessage(), args);

        Throwable throwable = null;
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

        String confirm = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        StructuredDataMessage msg = new StructuredDataMessage(
                confirm, translatedMsg, key.getName());

        msg.put(CTX_LMSATTR_ID, Integer.toString(key.getId()));
        msg.put(CTX_LMSATTR_LEVEL, key.getLevel().name());
        msg.put(CTX_LMSATTR_CATEGORY, category.name());
        if (gmsAttrs != null) {
            for (Map.Entry<String, String> entry: gmsAttrs.entrySet()) {
                msg.put(AbstractLmsEventLogger.CTX_ATTRMAP_PREFIX + entry.getKey(), entry.getValue());
            }
        }

        logger.log(fqcn,
                getLogLevel(key.getLevel()),
                msg, throwable
        );
    }*/


    protected static org.apache.log4j.Level getLogLevel(
            final LmsLogLevel lmsLogLevel) {
        if (lmsLogLevel != null) {
            switch (lmsLogLevel) {
            case ALARM:
                return org.apache.log4j.Level.FATAL;
            case STANDARD:
                return org.apache.log4j.Level.ERROR;
            case INTERACTION:
                return org.apache.log4j.Level.WARN;
            case TRACE:
                return org.apache.log4j.Level.INFO;
            case DEBUG:
                return org.apache.log4j.Level.DEBUG;
            case UNKNOWN:
                return org.apache.log4j.Level.TRACE; // ???
            case NONE:
                return null;
            default: // just in case...
                throw new IllegalArgumentException("Unsupported 'LmsLogLevel' = " + lmsLogLevel);
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
            default: // just in case...
                throw new IllegalArgumentException("Unsupported 'LmsLogLevel' = " + lmsLevel);
            }
        }
        return null;
    }

    protected static org.apache.log4j.Level getLogLevel(final Level level) {
        if (level != null) {
            switch (level.ordinal()) {
            case 0:
                return org.apache.log4j.Level.DEBUG;
            case 1:
                return org.apache.log4j.Level.INFO;
            case 2:
                return org.apache.log4j.Level.WARN;
            case 3:
                return org.apache.log4j.Level.ERROR;
            case 4:
                return org.apache.log4j.Level.FATAL;
            }
        }
        return null;
    }
}
