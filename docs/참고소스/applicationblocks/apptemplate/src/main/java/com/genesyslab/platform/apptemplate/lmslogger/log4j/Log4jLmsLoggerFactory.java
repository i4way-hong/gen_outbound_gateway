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

import com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger;
import com.genesyslab.platform.apptemplate.lmslogger.LmsLoggerFactory;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;

import com.genesyslab.platform.commons.log.Log4JChecker;


/**
 * Log4j v1.x based implementation of {@link LmsLoggerFactory}.
 * It is not supposed for explicit usage by application.<br/>
 * The abstract factory {@link LmsLoggerFactory} uses it when Log4j logging target is enabled.
 *
 * @see LmsLoggerFactory
 * @see LmsLoggerFactory#createInstance(com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor)
 *          LmsLoggerFactory.createInstance(LmsMessageConveyor)
 */
public class Log4jLmsLoggerFactory extends LmsLoggerFactory {

    /**
     * The factory constructor.<br/>
     * It is not supposed for explicit usage by application.<br/>
     * The abstract factory {@link LmsLoggerFactory} calls it when Log4j v1.x logging target is enabled.
     *
     * @throws IllegalArgumentException if Log4j v1.x API is not available.
     * @see LmsLoggerFactory
     */
    public Log4jLmsLoggerFactory(final LmsMessageConveyor lmsc) {
        super(lmsc);
        if (!Log4JChecker.isAvailable()) {
            throw new IllegalArgumentException(
                    "Can't initialize Log4j logging adaptor - Log4j 1.x API is not available");
        }
    }


    @Override
    public LmsEventLogger getLmsLogger(final String name) {
        return new Log4jLmsEventLogger(getMessageConveyor(),
                org.apache.log4j.Logger.getLogger(name));
    }

    @Override
    public LmsEventLogger getLmsLogger(final Class<?> clazz) {
        return new Log4jLmsEventLogger(getMessageConveyor(),
                org.apache.log4j.Logger.getLogger(clazz.getName()));
    }


    public static Builder newBuilder() {
        return new Builder();
    }


    public static class Builder
            extends AbstractFactoryBuilder<Log4jLmsLoggerFactory, Builder> {

        @Override
        public Log4jLmsLoggerFactory build() {
            return new Log4jLmsLoggerFactory(getLmsConveyor());
        }
    }
}
