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
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageTemplate;

import com.genesyslab.platform.management.protocol.messageserver.LogCategory;

import com.genesyslab.platform.commons.log.ILogger;


/**
 * "Silent" implementation of
 * {@link com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger LmsEventLogger}.<br/>
 * All log messages are ignored.
 *
 * @see SilentLmsLoggerFactory
 */
public class SilentLmsEventLogger implements LmsEventLogger {

    public static final SilentLmsEventLogger SINGLETON =
            new SilentLmsEventLogger();

    SilentLmsEventLogger() {}

    @Override
    public ILogger createChildLogger(final String name) {
        return this;
    }


    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void log(
            final LogCategory category,
            final LmsMessageTemplate key,
            final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void log(
            final LogCategory category,
            final int key,
            final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void log(
            final LmsMessageTemplate key,
            final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void log(
            final int key,
            final Object... args) {
        // Do not log anything...
    }


    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void trace(final String message, final Object... args) {
        // Do not log anything...
    }


    /**
     * Returns <code>false</code>. "Silent" logger implementation does not log anything.
     */
    @Override
    public boolean isDebug() {
        return false;
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void debug(final Object message) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void debug(final Object message, final Throwable thr) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void debug(final String message, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void debugFormat(final String message, final Object args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void debug(final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void debug(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }


    /**
     * Returns <code>false</code>. "Silent" logger implementation does not log anything.
     */
    @Override
    public boolean isInfo() {
        return false;
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void info(final Object message) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void info(final Object message, final Throwable thr) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void infoFormat(final String message, final Object args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void info(final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void info(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void info(final String message, final Object... args) {
        // Do not log anything...
    }


	/**
     * Returns <code>false</code>. "Silent" logger implementation does not log anything.
     */
    @Override
    public boolean isWarn() {
        return false;
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void warn(final Object message) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void warn(final Object message, final Throwable thr) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void warnFormat(final String message, final Object args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void warn(final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void warn(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void warn(final String message, final Object... args) {
        // Do not log anything...
    }


    /**
     * Returns <code>false</code>. "Silent" logger implementation does not log anything.
     */
    @Override
    public boolean isError() {
        return false;
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void error(final Object message) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void error(final Object message, final Throwable thr) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void errorFormat(final String message, final Object args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void error(final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void error(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void error(final String message, final Object... args) {
        // Do not log anything...
    }


    /**
     * Returns <code>false</code>. "Silent" logger implementation does not log anything.
     */
    @Override
    public boolean isFatalError() {
        return false;
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void fatalError(final Object message) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void fatalError(final Object message, final Throwable thr) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void fatalErrorFormat(final String message, final Object args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void fatalError(final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void fatalError(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void fatal(final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void fatal(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
        // Do not log anything...
    }

    /**
     * "Silent" logger implementation does not log anything.
     */
    @Override
    public void fatal(final String message, final Object... args) {
        // Do not log anything...
    }
}
