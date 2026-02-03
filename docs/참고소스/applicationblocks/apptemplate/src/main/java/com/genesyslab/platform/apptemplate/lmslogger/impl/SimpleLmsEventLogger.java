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
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageTemplate;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;
import com.genesyslab.platform.apptemplate.lmslogger.AbstractLmsEventLogger;

import com.genesyslab.platform.management.protocol.messageserver.LogLevel;
import com.genesyslab.platform.management.protocol.messageserver.LogCategory;

import com.genesyslab.platform.commons.log.ILogger;

import java.io.PrintStream;


/**
 * "Simple" implementation of
 * {@link com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger LmsEventLogger}.<br/>
 * It's to print LMS log messages to <code>stdout</code>.
 */
public class SimpleLmsEventLogger extends AbstractLmsEventLogger {

    protected final PrintStream stream;
    protected final LogLevel logLevel;


    protected SimpleLmsEventLogger(final LmsMessageConveyor imc, final LogLevel logLevel) {
        this(imc, null, logLevel);
    }

    protected SimpleLmsEventLogger(final LmsMessageConveyor imc, final PrintStream stream, final LogLevel logLevel) {
        super(imc);
        this.stream = (stream != null) ? stream : System.out;
        this.logLevel = (logLevel != null) ? logLevel : LogLevel.Error;
    }


    @Override
    public ILogger createChildLogger(final String name) {
        return this;
    }


    @Override
    public boolean isDebug() {
        return logLevel != null && logLevel.ordinal() <= LogLevel.Debug.ordinal();
    }

    @Override
    public boolean isInfo() {
        return logLevel != null && logLevel.ordinal() <= LogLevel.Info.ordinal();
    }

    @Override
    public boolean isWarn() {
        return logLevel != null && logLevel.ordinal() <= LogLevel.Interaction.ordinal();
    }

    @Override
    public boolean isError() {
        return logLevel == null || logLevel.ordinal() <= LogLevel.Error.ordinal();
    }

    @Override
    public boolean isFatalError() {
        return logLevel == null || logLevel.ordinal() <= LogLevel.Alarm.ordinal();
    }


    @Override
    protected void doLogEvent(
            final LogCategory category,
            final Level logLevel,
            final LmsMessageTemplate key,
            final Object... args) {
        Integer msgId;
        LmsLogLevel msgLevel;
        String translatedMsg;

        Throwable throwable = getThrowableArg(args);

        if (key != null) {
            msgLevel = key.getLevel();
        } else {
            msgLevel = DEFAULT_LMS_EVENT.getLevel();
        }

        if (key != null) {
            msgId = key.getId();
            translatedMsg = key.getMessage();
            if (args != null && args.length > 0) {
                try {
                    translatedMsg = String.format(translatedMsg, args);
                } catch (final Exception ex) {
                    if (throwable == null ) {
                        throwable = ex;
                    }
                }
            }
        } else {
            msgId = DEFAULT_LMS_EVENT.getId();
            translatedMsg = "null";
        }
        if (logLevel != null || !LmsLogLevel.NONE.equals(msgLevel)) {
            stream.println(
                    String.format("%1$td.%1$tm.%1$tY %1$tk:%1$tM:%1$tS.%1$tL %2$s %3$s %4$05d %5$s",
                            System.currentTimeMillis(),
                            category,
                            (logLevel != null ? logLevel : msgLevel),
                            msgId,
                            translatedMsg)
            );
            if (throwable != null && isDebug()) {
                throwable.printStackTrace(stream);
            }
        }
    }

    @Override
    protected void log(final Object message, final Throwable e, final Level level) {
        stream.println(
                String.format("%1$td.%1$tm.%1$tY %1$tk:%1$tM:%1$tS.%1$tL %2$s 09900 %3$s",
                        System.currentTimeMillis(), level, message.toString())
        );
    }

    @Override
    protected void logArgs(final String message, final Object args, final Level level) {
        stream.println(
                String.format("%1$td.%1$tm.%1$tY %1$tk:%1$tM:%1$tS.%1$tL %2$s 09900 %3$s",
                        System.currentTimeMillis(), level,
                        formatMessage(message, args))
        );
    }
}
