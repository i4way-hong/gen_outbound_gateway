// ===============================================================================
// Genesys Platform SDK
// ===============================================================================
//
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:
//
// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.
//
// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.
//
// THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
// ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
// DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
// WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
// NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
// NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
// EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
// CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
// NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
// Copyright (c) 2009 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.apptemplate.lmslogger;

import com.genesyslab.platform.commons.log.ILogger;


/**
 * Extension of PSDK Common logging interface with methods named in style of common logging interfaces like Log4j2.
 */
public interface ILoggerEx extends ILogger {

    /**
     * Logs a message at the <code>FATAL ERROR</code> level.<br/>
     * To format log messages PSDK uses <code>MessageFormat</code> class.<br/>
     * Quick examples on the rules of formatting: <code><pre>
     * log.fatal("Value is ''{0}''", 1);
     * log.fatal("Value for index: [{0}] = {1}", ind, val);</pre></code>
     * <p/>
     * <i><b>Note</b>: Some of PSDK supported logging frameworks (namely "<code>Slf4J</code>"
     * and "<code>java.util.logging</code>") do not support <code>FATAL ERROR</code> messages level.<br/>
     * So, in <code>Slf4J</code> logging adapter PSDK marks such messages with "PSDK_FATAL_MESSAGE" marker.<br/>
     * In <code>java.util.logging</code> adapter this level is processed as <code>ERROR</code>
     * (<code>SEVERE</code>).</i>
     *
     * @param message the message to log.
     * @param args parameters to the message.
     * @see java.text.MessageFormat
     */
    void fatal(String message, Object... args);

    /**
     * Logs a message at the <code>ERROR</code> level.<br/>
     * To format log messages PSDK uses <code>MessageFormat</code> class.<br/>
     * Quick examples on the rules of formatting: <code><pre>
     * log.error("Value is ''{0}''", 1);
     * log.error("Value for index: [{0}] = {1}", ind, val);</pre></code>
     *
     * @param message the message to log.
     * @param args parameters to the message.
     * @see java.text.MessageFormat
     */
    void error(String message, Object... args);

    /**
     * Logs a message at the <code>WARNING</code> level.<br/>
     * To format log messages PSDK uses <code>MessageFormat</code> class.<br/>
     * Quick examples on the rules of formatting: <code><pre>
     * log.warn("Value is ''{0}''", 1);
     * log.warn("Value for index: [{0}] = {1}", ind, val);</pre></code>
     *
     * @param message the message to log.
     * @param args parameters to the message.
     * @see java.text.MessageFormat
     */
    void warn(String message, Object... args);

    /**
     * Logs a message at the <code>INFO</code> level.<br/>
     * To format log messages PSDK uses <code>MessageFormat</code> class.<br/>
     * Quick examples on the rules of formatting: <code><pre>
     * log.info("Value is ''{0}''", 1);
     * log.info("Value for index: [{0}] = {1}", ind, val);</pre></code>
     *
     * @param message the message to log.
     * @param args parameters to the message.
     * @see java.text.MessageFormat
     */
    void info(String message, Object... args);

    /**
     * Logs formatted debug message with optional arguments.
     * To format log messages PSDK uses <code>MessageFormat</code> class.<br/>
     * Quick examples on the rules of formatting: <code><pre>
     * log.debug("Value is ''{0}''", 1);
     * log.debug("Value for index: [{0}] = {1}", ind, val);</pre></code>
     *
     * @param message message with format tags
     * @param args arguments for format
     * @see java.text.MessageFormat
     */
    void debug(String message, Object... args);

    /**
     * Logs formatted trace message with optional arguments.
     * To format log messages PSDK uses <code>MessageFormat</code> class.<br/>
     * Quick examples on the rules of formatting: <code><pre>
     * log.trace("Value is ''{0}''", 1);
     * log.trace("Value for index: [{0}] = {1}", ind, val);</pre></code>
     *
     * @param message message with format tags
     * @param args arguments for format
     * @see java.text.MessageFormat
     */
    void trace(String message, Object... args);

}
