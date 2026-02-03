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
package com.genesyslab.platform.apptemplate.lmslogger.impl;

import com.genesyslab.platform.apptemplate.lmslogger.LmsLogLevel;
import com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageTemplate;
import com.genesyslab.platform.apptemplate.lmslogger.AbstractLmsEventLogger;

import com.genesyslab.platform.management.protocol.messageserver.LogLevel;
import com.genesyslab.platform.management.protocol.messageserver.LogCategory;
import com.genesyslab.platform.management.protocol.messageserver.AttributeList;

import com.genesyslab.platform.commons.log.ILogger;


public class DirectLmsEventLogger extends AbstractLmsEventLogger {

    public static final int DEFAULT_LMS_EVENT_ID = 9900;

    private final MSEventSender sender;
    private final LogLevel loggerLevel;
    private final LmsEventLogger appendant;

    protected DirectLmsEventLogger(
            final LmsMessageConveyor imc,
            final MSEventSender sender,
            final LogLevel logLevel,
            final LmsEventLogger appendant) {
        super(imc);
        this.sender = sender;
        this.loggerLevel = logLevel;
        this.appendant = appendant;
    }


    @Override
    public ILogger createChildLogger(final String name) {
        return this;
    }


    @Override
    public boolean isDebug() {
        if (appendant != null) {
            if (appendant.isDebug()) {
                return true;
            }
        }
        return false; // "plain" debug messages are not to be sent to MessageServer
    }

    @Override
    public boolean isInfo() {
        if (sender != null) {
            if (loggerLevel != null && loggerLevel.ordinal() <= LogLevel.Info.ordinal()) {
                return true;
            }
        }
        if (appendant != null) {
            if (appendant.isInfo()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isWarn() {
        if (sender != null) {
            if (loggerLevel != null && loggerLevel.ordinal() <= LogLevel.Interaction.ordinal()) {
                return true;
            }
        }
        if (appendant != null) {
            if (appendant.isWarn()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isError() {
        if (sender != null) {
            if (loggerLevel == null || loggerLevel.ordinal() <= LogLevel.Error.ordinal()) {
                return true;
            }
        }
        if (appendant != null) {
            if (appendant.isError()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isFatalError() {
        if (sender != null) {
            if (loggerLevel == null || loggerLevel.ordinal() <= LogLevel.Alarm.ordinal()) {
                return true;
            }
        }
        if (appendant != null) {
            if (appendant.isFatalError()) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void doLogEvent(
            final LogCategory category,
            final Level logLevel,
            final LmsMessageTemplate key,
            final Object... args) {
        if (key == null) {
            return;
        }
        if (sender != null) {
            boolean isLevelAllowed = true;
            if (loggerLevel != null) {
                final LogLevel msgLevel = getLogLevel(key.getLevel());
                isLevelAllowed = (msgLevel != null)
                        && msgLevel.ordinal() >= loggerLevel.ordinal();
            }
            if (isLevelAllowed) {
                String translatedMsg = key.getMessage();
                @SuppressWarnings("unused")
                Throwable throwable = null;
                if (args != null && args.length > 0) {
                    try {
                        translatedMsg = String.format(translatedMsg, args);
                    } catch (final Exception ex) {
                        throwable = ex;
                    }
                }

                AttributeList gmsAttrs = null;
                if (printAttributes && args != null && args.length > 0) {
                    Object obj = args[args.length - 1];
                    if (obj instanceof Throwable) {
                        throwable = (Throwable) obj;
                        if (args.length > 1) {
                            obj = args[args.length - 2];
                        } else {
                            obj = null;
                        }
                    }
                    if (obj instanceof AttributeList) {
                        gmsAttrs = (AttributeList) obj;
                    }
                }

                sender.push(key.getId(), category,
                        getLogLevel(key.getLevel()),
                        translatedMsg, null, gmsAttrs);
            }
        }

        if (appendant != null) {
            appendant.log(category, key.getId(), args);
        }
    }


    @Override
    protected void log(final Object message, final Throwable e, final Level level) {
        if (sender != null) {
            final LogLevel msgLevel = getLogLevel(level);
            boolean isLevelAllowed = false;
            if ((msgLevel != null)
                    && msgLevel.ordinal() > LogLevel.Debug.ordinal()) {
                isLevelAllowed = (loggerLevel == null)
                        || (msgLevel.ordinal() >= loggerLevel.ordinal());
            }
            if (isLevelAllowed) {
                sender.push(DEFAULT_LMS_EVENT_ID, LogCategory.Default,
                        msgLevel, message.toString(), null, null);
            }
        }

        if (appendant != null) {
            switch (level.ordinal()) {
            case 4:
                appendant.fatalError(message, e);
                break;
            case 3:
                appendant.error(message, e);
                break;
            case 2:
                appendant.warn(message, e);
                break;
            case 1:
                appendant.info(message, e);
                break;
            case 0:
                appendant.debug(message, e);
                break;
            default: // just in case...
                appendant.debug(message, e);
                break;
            }
        }
    }


    @Override
    protected void logArgs(final String message, final Object args, final Level level) {
        Throwable thr = null;
        if (args instanceof Object[]) {
            final Object[] arr = (Object[]) args;
            if (arr.length > 0 && arr[arr.length - 1] instanceof Throwable) {
                thr = (Throwable) arr[arr.length - 1];
            }
        }
        log(formatMessage(message, args), thr, level);
    }


    protected static LogLevel getLogLevel(
            final LmsLogLevel lmsLogLevel) {
        switch (lmsLogLevel) {
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
            throw new IllegalArgumentException("Unsupported 'LmsLogLevel' = " + lmsLogLevel);
        }
    }

    protected static LogLevel getLogLevel(final Level level) {
        switch (level.ordinal()) {
        case 4:
            return LogLevel.Alarm;
        case 3:
            return LogLevel.Error;
        case 2:
            return LogLevel.Interaction;
        case 1:
            return LogLevel.Info;
        case 0:
            return LogLevel.Debug;
        default: // just in case...
            return LogLevel.Unknown;
        }
    }
}
