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

/**
 * This package provides support of Genesys LMS files and LMS events logging.
 * <p/>
 * Simple usage:<code><pre>
 * class SampleClass {
 *
 *     protected final static LmsEventLogger LOG = LmsLoggerFactory.getLogger(SampleClass.class);
 *
 *     public void method() {
 *         // Use logger to generate event:
 *         LOG.log(LogCategory.Application, CommonLmsEnum.GCTI_LOAD_RESOURCE, "users.db", "no such file");
 *             // => "Unable to load resource 'users.db', error code 'no such file'"
 *
 *         // or, for event GCTI_CFG_APP[6053, STANDARD, "Configuration for application obtained"]:
 *         LOG.log(CommonLmsEnum.GCTI_CFG_APP);
 *           // or
 *         LOG.log(6053); // => "Configuration for application obtained"
 *
 *         // or "plain" logging methods:
 *         try {
 *             LOG.debug("Starting cache load...");
 *             // ... do something ...
 *         } catch (final Exception exception) {
 *             LOG.error("Failed to load cache", exception);
 *         }
 * .....
 * </pre></code>
 * With direct initialization of LmsLoggerFactory singleton and LmsMessageConveyor:<code><pre>
 * // Initialize logger factory:
 * LmsMessageConveyor lmsConveyor = new LmsMessageConveyor(CommonLmsEnum.class, MyAppLmsEnum.class);
 * lmsConveyor.loadConfiguration("common.lms");
 * LmsLoggerFactory.createInstance(lmsConveyor);
 *
 * // Create logger instance:
 * LmsEventLogger lmsLog = LmsLoggerFactory.getLogger("MyApplication");
 *
 * // Use logger to generate event:
 * lmsLog.log(LogCategory.Application, CommonLmsEnum.GCTI_LOAD_RESOURCE, "users.db", "no such file");
 *
 * // or, for event GCTI_CFG_APP[6053, STANDARD, "Configuration for application obtained"]:
 * lmsLog.log(CommonLmsEnum.GCTI_CFG_APP);
 * </pre></code>
 *
 * @see com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor LmsMessageConveyor
 * @see com.genesyslab.platform.apptemplate.lmslogger.LmsLoggerFactory LmsLoggerFactory
 * @see com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger LmsEventLogger
 * @see com.genesyslab.platform.apptemplate.lmslogger.CommonLmsEnum CommonLmsEnum
 */
package com.genesyslab.platform.apptemplate.lmslogger;
