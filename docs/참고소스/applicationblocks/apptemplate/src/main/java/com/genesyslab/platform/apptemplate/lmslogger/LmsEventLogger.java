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
package com.genesyslab.platform.apptemplate.lmslogger;

import com.genesyslab.platform.management.protocol.messageserver.LogCategory;


/**
 * Facade interface for LMS Events logging.<br/>
 * It allows applications to generate LMS Events with or without specific LMS templates
 * enumerations.<code><pre>
 * lmsLogger.log(CommonLmsEnum.GCTI_LOAD_RESOURCE, rcName, errCode);
 * lmsLogger.log(LogCategory.Application, CommonLmsEnum.GCTI_LOAD_RESOURCE, rcName, errCode);
 *
 * // or:
 * lmsLogger.log(2002, rcName, errCode); // 2002 corresponds to CommonLmsEnum.GCTI_LOAD_RESOURCE
 * lmsLogger.log(LogCategory.Application, 2002, rcName, errCode);
 * </pre></code>
 *
 * @see LmsLoggerFactory
 * @see LmsLoggerFactory#getLogger(Class)
 * @see LmsLoggerFactory#getLogger(String)
 */
public interface LmsEventLogger extends ILoggerEx {

    /**
     * Log marker for LMS messages events.</br>
     * It is supposed that every LMS message logged by a LmsLogger will bear this marker.
     * It allows marker-aware implementations to perform additional processing on localized messages.
     */
    String STR_PSDK_LMS_MESSAGE_MARKER = "PSDK_LMS_MESSAGE";

    /**
     * Log marker for LMS Alarm level messages events.</br>
     * It is supposed that LMS Alarm message logged by a LmsLogger may bear this marker.
     * It allows marker-aware implementations to perform additional processing on localized messages.
     */
    String STR_PSDK_LMS_ALARM_MESSAGE_MARKER = "PSDK_LMS_ALARM_MESSAGE";


    /**
     * Logs a localized message.<br/>
     * Its usage may look like:<code><pre>
     * lmsLogger.log(LogCategory.Application, CommonLmsEnum.GCTI_APP_STARTED);
     * lmsLogger.log(LogCategory.Application, CommonLmsEnum.GCTI_LOAD_RESOURCE, rcName, errCode);
     * </pre></code>
     *
     * @param category the log event category.
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void log(
            LogCategory category,
            LmsMessageTemplate key,
            Object... args);

    /**
     * Logs a localized message.<br/>
     * Its usage may look like:<code><pre>
     * lmsLogger.log(LogCategory.Application, 5060);                  // CommonLmsEnum.GCTI_APP_STARTED
     * lmsLogger.log(LogCategory.Application, 2002, rcName, errCode); // CommonLmsEnum.GCTI_LOAD_RESOURCE
     * </pre></code>
     *
     * @param category the log event category.
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void log(
            final LogCategory category,
            final int key,
            final Object... args);

    /**
     * Logs a localized message.<br/>
     * Its usage may look like:<code><pre>
     * lmsLogger.log(CommonLmsEnum.GCTI_APP_STARTED);
     * lmsLogger.log(CommonLmsEnum.GCTI_LOAD_RESOURCE, rcName, errCode);
     * </pre></code>
     *
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void log(
            final LmsMessageTemplate key,
            final Object... args);

    /**
     * Logs a localized message.<br/>
     * Its usage may look like:<code><pre>
     * lmsLogger.log(5060);                  // CommonLmsEnum.GCTI_APP_STARTED
     * lmsLogger.log(2002, rcName, errCode); // CommonLmsEnum.GCTI_LOAD_RESOURCE
     * </pre></code>
     *
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void log(
            final int key,
            final Object... args);


    /**
     * Logs localized LMS event as a debug message.
     * <p/>
     * <i><b>Note</b>: "Debug" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.debug(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>debug</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param category the log event category.
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void debug(
            LogCategory category,
            LmsMessageTemplate key,
            Object... args);

    /**
     * Logs localized LMS event as a debug message.
     * <p/>
     * <i><b>Note</b>: "Debug" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.debug(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>debug</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void debug(
            final LmsMessageTemplate key,
            final Object... args);


    /**
     * Logs localized LMS event as an info message.
     * <p/>
     * <i><b>Note</b>: "Info" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.info(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>info</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param category the log event category.
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void info(
            LogCategory category,
            LmsMessageTemplate key,
            Object... args);

    /**
     * Logs localized LMS event as an info message.
     * <p/>
     * <i><b>Note</b>: "Info" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.info(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>info</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void info(
            final LmsMessageTemplate key,
            final Object... args);


    /**
     * Logs localized LMS event as a warning message.
     * <p/>
     * <i><b>Note</b>: "Warn" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.warn(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>warn</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param category the log event category.
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void warn(
            LogCategory category,
            LmsMessageTemplate key,
            Object... args);

    /**
     * Logs localized LMS event as a warning message.
     * <p/>
     * <i><b>Note</b>: "Warn" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.warn(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>warn</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void warn(
            final LmsMessageTemplate key,
            final Object... args);


    /**
     * Logs localized LMS event as an error message.
     * <p/>
     * <i><b>Note</b>: "Error" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.error(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>error</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param category the log event category.
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void error(
            LogCategory category,
            LmsMessageTemplate key,
            Object... args);

    /**
     * Logs localized LMS event as an error message.
     * <p/>
     * <i><b>Note</b>: "Error" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.error(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>error</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void error(
            final LmsMessageTemplate key,
            final Object... args);


    /**
     * Logs localized LMS event as a fatal error message.
     * <p/>
     * <i><b>Note</b>: "Fatal error" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.fatal(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>fatal</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param category the log event category.
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void fatal(
            LogCategory category,
            LmsMessageTemplate key,
            Object... args);

    /**
     * Logs localized LMS event as a fatal error message.
     * <p/>
     * <i><b>Note</b>: "Fatal error" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.fatal(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>fatal</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void fatal(
            final LmsMessageTemplate key,
            final Object... args);

    /**
     * Logs localized LMS event as a fatal error message.
     * <p/>
     * <i><b>Note</b>: "Fatal error" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.fatal(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>fatal</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param category the log event category.
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void fatalError(
            LogCategory category,
            LmsMessageTemplate key,
            Object... args);

    /**
     * Logs localized LMS event as a fatal error message.
     * <p/>
     * <i><b>Note</b>: "Fatal error" level of this method does not affect LMS events level
     * ({@link LmsMessageTemplate#getLevel()}) logged with this method.<br/>
     * <code>LmsEventLogger</code> applies LMS localization and pass this call to actual underlying
     * log system as "<code>.fatal(...)</code>" method call.<br/>
     * So, common logging functionality will have these events as "<code>fatal</code>",
     * but in case of Message Server appender, or Log4j2
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout GLmsLayout},
     * there will be value of {@link LmsMessageTemplate#getLevel()}.</i>
     *
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    void fatalError(
            final LmsMessageTemplate key,
            final Object... args);
}
