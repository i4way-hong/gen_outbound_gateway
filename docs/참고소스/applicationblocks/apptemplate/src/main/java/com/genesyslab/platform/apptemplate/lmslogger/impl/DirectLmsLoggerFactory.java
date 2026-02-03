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

import com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger;
import com.genesyslab.platform.apptemplate.lmslogger.LmsLoggerFactory;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;

import com.genesyslab.platform.apptemplate.configuration.log.TargetType;
import com.genesyslab.platform.apptemplate.configuration.log.VerboseLevel;
import com.genesyslab.platform.apptemplate.configuration.log.TargetDescriptor;
import com.genesyslab.platform.apptemplate.configuration.log.GAppLoggingOptions;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;

import com.genesyslab.platform.management.protocol.messageserver.LogLevel;
import com.genesyslab.platform.management.protocol.messageserver.LogCategory;
import com.genesyslab.platform.management.protocol.messageserver.AttributeList;

import java.util.Date;
import java.util.List;


public class DirectLmsLoggerFactory extends LmsLoggerFactory {

    private final MSEventSender sender;
    private final LogLevel logLevel;
    private final LmsLoggerFactory appendant;

    public DirectLmsLoggerFactory(
            final LmsMessageConveyor lmsc,
            final IGApplicationConfiguration appConfig) {
        this(lmsc, new GAppLoggingOptions(appConfig, null), appConfig, null);
    }

    public DirectLmsLoggerFactory(
            final LmsMessageConveyor lmsc,
            final GAppLoggingOptions logConfig,
            final IGApplicationConfiguration appConfig,
            final LmsLoggerFactory appendant) {
        super(lmsc);

        VerboseLevel level = null;
        final List<TargetDescriptor> tgts;
        if (logConfig != null) {
            tgts = logConfig.getOutputDescriptors();
        } else {
            tgts = null;
        }
        if (tgts != null) {
            for (TargetDescriptor tgt: tgts) {
                if (TargetType.MESSAGESERVER.equals(tgt.getTargetType())) {
                    if (level != null) {
                        final VerboseLevel lvl = tgt.getSupportedVerboseLevel();
                        if (lvl != null && lvl.ordinal() < level.ordinal()) {
                            level = lvl;
                        }
                    } else {
                        level = tgt.getSupportedVerboseLevel();
                    }
                }
            }
        }

        if (level != null) {
            switch (level) {
            case NONE:
                this.logLevel = null;
                break;
            case ALARM:
                this.logLevel = LogLevel.Alarm;
                break;
            case STANDARD:
                this.logLevel = LogLevel.Error;
                break;
            case INTERACTION:
                this.logLevel = LogLevel.Interaction;
                break;
            case TRACE:
                this.logLevel = LogLevel.Info;
                break;
            case DEBUG:
                this.logLevel = LogLevel.Debug;
                break;
            case ALL:
                this.logLevel = LogLevel.Debug;
                break;
            default:
                this.logLevel = LogLevel.Unknown;
                break;
            }
        } else {
            this.logLevel = LogLevel.Error;
        }

        if (level != null) {
            this.sender = new MSEventSenderImpl("MessageServer", appConfig);
        } else {
            // Create events ignoring sender: 
            this.sender = new MSEventSender() {
                @Override public void stop() { /* do nothing */ }
                @Override public void start() { /* do nothing */ }
                @Override
                public void push(final Integer entryId,
                        final LogCategory entryCategory,
                        final LogLevel level, final String entryText,
                        final Date date, final AttributeList attributes) {
                    /* do nothing */
                }
            };
        }

        this.appendant = appendant;
    }


    @Override
    public LmsEventLogger getLmsLogger(final String name) {
        return new DirectLmsEventLogger(
                getMessageConveyor(), sender, logLevel,
                appendant != null ? appendant.getLmsLogger(name) : null);
    }

    @Override
    public LmsEventLogger getLmsLogger(final Class<?> clazz) {
        return new DirectLmsEventLogger(
                getMessageConveyor(), sender, logLevel,
                appendant != null ? appendant.getLmsLogger(clazz) : null);
    }


    public static Builder newBuilder() {
        return new Builder();
    }


    public static class Builder
            extends AbstractFactoryBuilder<DirectLmsLoggerFactory, Builder> {

        protected LmsLoggerFactory appendant = null;

        public Builder withAppendant(final LmsLoggerFactory loggerFactory) {
            appendant = loggerFactory;
            return this;
        }

        @Override
        public DirectLmsLoggerFactory build() {
            return new DirectLmsLoggerFactory(
                    getLmsConveyor(), getLogOptions(), appConfig, appendant);
        }
    }
}
