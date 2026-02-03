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

import com.genesyslab.platform.management.protocol.messageserver.LogLevel;

import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.PsdkCustomization.PsdkOption;


/**
 * Implementation of {@link LmsLoggerFactory} which produces "simple" {@link LmsEventLogger}'s.
 *
 * @see SimpleLmsEventLogger
 */
public class SimpleLmsLoggerFactory extends LmsLoggerFactory {

    protected final LogLevel logLevel;


    /**
     * Creates console LmsEventLogger factory with new instance of default LMS message conveyor,
     * and default log level, which may be overridden with PSDK Customization option
     * {@link PsdkOption#PsdkLoggerConsoleLevel}.
     *
     * @see LmsMessageConveyor#LmsMessageConveyor()
     * @see PsdkOption#PsdkLoggerConsoleLevel
     * @see PsdkCustomization
     */
    public SimpleLmsLoggerFactory() {
        this(new LmsMessageConveyor());
    }

    /**
     * Creates console LmsEventLogger factory with default log level, which may be overridden
     * with PSDK Customization option {@link PsdkOption#PsdkLoggerConsoleLevel}.
     *
     * @param lmsc reference to actual LMS message conveyor.
     * @see PsdkOption#PsdkLoggerConsoleLevel
     * @see PsdkCustomization
     */
    public SimpleLmsLoggerFactory(final LmsMessageConveyor lmsc) {
        super(lmsc);
        final Integer lvl = PsdkCustomization.getIntOption(
                PsdkOption.PsdkLoggerConsoleLevel, null);
        if (lvl != null) {
            final LogLevel level = LogLevel.valueOf(lvl + 1); // Level.DEBUG(0) -> LogLevel.Debug(1), etc
            if (level != null) {
                logLevel = level;
            } else {
                logLevel = LogLevel.Info;
            }
        } else {
            logLevel = LogLevel.Info;
        }
    }

    /**
     * Creates console LmsEventLogger factory with default log level, which may be overridden
     * with PSDK Customization option {@link PsdkOption#PsdkLoggerConsoleLevel}.
     *
     * @param lmsc reference to actual LMS message conveyor.
     * @param level log level for <code>LmsEventLogger</code>'s.
     */
    public SimpleLmsLoggerFactory(final LmsMessageConveyor lmsc, final LogLevel level) {
        super(lmsc);
        logLevel = (level != null) ? level : LogLevel.Info;
    }


    @Override
    public LmsEventLogger getLmsLogger(final String name) {
        return new SimpleLmsEventLogger(getMessageConveyor(), logLevel);
    }

    @Override
    public LmsEventLogger getLmsLogger(final Class<?> clazz) {
        return new SimpleLmsEventLogger(getMessageConveyor(), logLevel);
    }


    /**
     * Creates builder of "simple" console printing <code>LmsLoggerFactory</code>.
     *
     * @return new instance of builder.
     */
    public static Builder newBuilder() {
        return new Builder();
    }


    public static class Builder
            extends AbstractFactoryBuilder<SimpleLmsLoggerFactory, Builder> {

        private LogLevel level;

        public Builder withLevel(final LogLevel level) {
            this.level = level;
            return this;
        }

        @Override
        public SimpleLmsLoggerFactory build() {
            return new SimpleLmsLoggerFactory(getLmsConveyor(), level);
        }
    }
}
