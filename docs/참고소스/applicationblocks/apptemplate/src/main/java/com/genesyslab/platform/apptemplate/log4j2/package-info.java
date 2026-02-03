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
 * This package contains a set of AppTemplate classes related to Log4j v2 support.
 * <p/>
 * Its main facade classes for direct configuration by user applications are
 * {@link com.genesyslab.platform.apptemplate.log4j2.Log4j2Configurator Log4j2Configurator} and
 * {@link com.genesyslab.platform.apptemplate.log4j2.PsdkLog4j2Configuration PsdkLog4j2Configuration}.<br/>
 * It also contains helper adaptor for automatic logging configuration and re-configuration
 * {@link com.genesyslab.platform.apptemplate.log4j2.GFAppLog4j2Updater GFAppLog4j2Updater},
 * which may be used with the application configuration manager
 * {@link com.genesyslab.platform.apptemplate.application.GFApplicationConfigurationManager
 * GFApplicationConfigurationManager}.
 * <p/>
 * If application configuration is managed with one of the mentioned configuration managers,
 * to keep application logging configuration up to date, it is enough to register there an instance of
 * {@link com.genesyslab.platform.apptemplate.log4j2.GFAppLog4j2Updater GFAppLog4j2Updater}.
 * It listens for the application configuration updates, and if its logging options got changed,
 * it uses {@link com.genesyslab.platform.apptemplate.log4j2.Log4j2Configurator Log4j2Configurator}
 * to reconfigure Log4j2.
 * <p/>
 * For a case, when application does not use the configuration manager, or for some testing purposes,
 * it is possible to configure Log4j2 explicitly.<br/>
 * Simple application configuration usage:<code><pre>
 * CfgApplication theApp = confService.retrieveObject(
 *         CfgApplication.class, new CfgApplicationQuery(myAppName));
 * GApplicationConfiguration <b>appConfig</b> = new GCOMApplicationConfiguration(theApp);
 *
 * LmsMessageConveyor <b>lmsMessages</b> = new LmsMessageConveyor(CommonLmsEnum.class, MyAppLmsEnum.class);
 * <b>lmsMessages</b>.loadConfiguration(<b>appConfig</b>);
 *
 * <b>Log4j2Configurator</b>.<b>applyLoggingConfig</b>(<b>appConfig</b>, <b>lmsMessages</b>);
 * </pre></code>
 * Or simple Log4j2 configuration without application configuration reading (without ConfService usage):<code><pre>
 * GApplicationConfiguration <b>appConfig</b> = new GApplicationConfiguration();
 * <b>appConf</b>.setApplicationName(myAppName);
 * KeyValueCollection options = new KeyValueCollection();
 * KeyValueCollection logSection = new KeyValueCollection();
 * logSection.addString("verbose", "all");
 * logSection.addString("message-format", "full");
 * logSection.addString("standard", "Log4j2ConfiguratorTest-std");
 * logSection.addString("all", "stdout, Log4j2ConfiguratorTest-all");
 * options.addList("log", logSection);
 * <b>appConf</b>.setOptions(options);
 *
 * <b>Log4j2Configurator</b>.<b>applyLoggingConfig</b>(<b>appConfig</b>, null);
 * </pre></code>
 * Or, if no LMS files or network logging target is supposed, it is possible to configure logging
 * without creation of the application configuration structure:<code><pre>
 * KeyValueCollection logOpts = new KeyValueCollection();
 * logOpts.addString("verbose", "all");
 * logOpts.addString("message-format", "full");
 * logOpts.addString("standard", "Log4j2ConfiguratorTest-std");
 * logOpts.addString("all", "stdout, Log4j2ConfiguratorTest-all");
 *
 * <b>Log4j2Configurator</b>.<b>setConfig</b>(<b>PsdkLog4j2Configuration</b>.<b>parse</b>(logOpts));
 * </pre></code>
 */
package com.genesyslab.platform.apptemplate.log4j2;
