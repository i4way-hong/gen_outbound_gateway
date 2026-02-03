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
package com.genesyslab.platform.apptemplate.lmslogger.jul;

import com.genesyslab.platform.apptemplate.lmslogger.LmsLogLevel;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageTemplate;
import com.genesyslab.platform.apptemplate.lmslogger.AbstractLmsEventLogger;

import com.genesyslab.platform.management.protocol.messageserver.LogCategory;

import com.genesyslab.platform.commons.log.ILogger;


/**
 * <code>java.util.logging</code> based logging interface implementation of AppTemplate
 * {@link com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger LmsEventLogger}.
 *
 * @see JulLmsLoggerFactory
 */
public class JulLmsEventLogger extends AbstractLmsEventLogger {

    private final java.util.logging.Logger logger;


    protected JulLmsEventLogger(
            final LmsMessageConveyor imc,
            final java.util.logging.Logger logger) {
        super(imc);
        this.logger = logger;
    }

    @Override
    public ILogger createChildLogger(final String name) {
        return this;
    }


    @Override
    public boolean isFatalError() {
        return logger.isLoggable(java.util.logging.Level.SEVERE);
    }

    @Override
    public boolean isError() {
        return logger.isLoggable(java.util.logging.Level.SEVERE);
    }

    @Override
    public boolean isWarn() {
        return logger.isLoggable(java.util.logging.Level.WARNING);
    }

    @Override
    public boolean isInfo() {
        return logger.isLoggable(java.util.logging.Level.INFO);
    }

    @Override
    public boolean isDebug() {
        return logger.isLoggable(java.util.logging.Level.FINE);
    }


    @Override
    protected void doLogEvent(
            final LogCategory category,
            final Level logLevel,
            final LmsMessageTemplate key,
            final Object... args) {
        java.util.logging.Level lvl = null;
        String translatedMsg = "null";
        Throwable throwable = null;

        if (logLevel != null) {
            lvl = getLogLevel(logLevel);
        }
        if (lvl == null && key != null) {
            lvl = getLogLevel(key.getLevel());
        }
        if (lvl == null) {
            if (key == null || !LmsLogLevel.NONE.equals(key.getLevel())) {
                lvl = getLogLevel(DEFAULT_LMS_EVENT.getLevel());
            }
        }

        if (key != null) {
            translatedMsg = key.getMessage();
            if (args != null && args.length > 0) {
                try {
                    translatedMsg = String.format(translatedMsg, args);
                } catch (final Exception ex) {
                    throwable = ex;
                }
            }
        }
        if (lvl != null) { //NONE log level results in lvl being null.
            java.util.logging.LogRecord logRecord =
                    new java.util.logging.LogRecord(lvl, translatedMsg);

            inferCaller(logRecord);

            if (args != null && args.length > 0) {
                Object obj = args[args.length - 1];
                if (obj instanceof Throwable) {
                    throwable = (Throwable) obj;
                }
            }

            if (throwable != null) {
                logRecord.setThrown(throwable);
            }

            logger.log(logRecord);
        }
    }


    @Override
    protected void log(final Object message, final Throwable thr, final Level level) {
        java.util.logging.LogRecord logRecord =
                new java.util.logging.LogRecord(getLogLevel(level), message.toString());
        inferCaller(logRecord);
        if (thr != null) {
            logRecord.setThrown(thr);
        }

        logger.log(logRecord);
    }

    @Override
    protected void logArgs(final String message, final Object args, final Level level) {
        log(formatMessage(message, args), null, level);
    }


    protected static java.util.logging.Level getLogLevel(
            final LmsLogLevel lmsLogLevel) {
        if (lmsLogLevel != null) {
            switch (lmsLogLevel) {
            case ALARM:
                return java.util.logging.Level.SEVERE;
            case STANDARD:
                return java.util.logging.Level.SEVERE;
            case INTERACTION:
                return java.util.logging.Level.WARNING;
            case TRACE:
                return java.util.logging.Level.INFO;
            case DEBUG:
                return java.util.logging.Level.FINER;
            case UNKNOWN:
                return java.util.logging.Level.FINEST;
            }
        }
        return null;
    }

    protected static java.util.logging.Level getLogLevel(final Level level) {
        if (level != null) {
            switch (level.ordinal()) {
            case 0:
                return java.util.logging.Level.FINE;
            case 1:
                return java.util.logging.Level.INFO;
            case 2:
                return java.util.logging.Level.WARNING;
            case 3:
                return java.util.logging.Level.SEVERE;
            case 4:
                return java.util.logging.Level.SEVERE;
            }
        }
        return null;
    }


    // Private method to infer the caller's class and method names
    private void inferCaller(
            final java.util.logging.LogRecord logRecord) {
        String logPack = "com.genesyslab.platform.apptemplate.lmslogger.";

        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        boolean lookingForLogger = true;
        for (int ix = 0; ix < stackTrace.length; ix++) {
            StackTraceElement frame = stackTrace[ix];
            String cname = frame.getClassName();
            if (lookingForLogger) {
                // Skip all frames until we have found the first logger frame.
                if (cname.startsWith(logPack)) {
                    lookingForLogger = false;
                }
            } else {
                if (!cname.startsWith(logPack)) {
                    // We've found the relevant frame.
                    logRecord.setSourceClassName(cname);
                    logRecord.setSourceMethodName(frame.getMethodName());
                    return;
                }
            }
        }
    }
}
