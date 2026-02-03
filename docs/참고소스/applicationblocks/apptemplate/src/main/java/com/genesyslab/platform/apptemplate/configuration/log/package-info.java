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
 * Dedicated package for CME Application Logging Options configuration support.
 * <p/>
 * Application logging configuration options are located in "log" and "log-extended" sections of
 * "Options" property of correspondent CME Application object.
 * Its values are defined as string type values in a
 * {@link com.genesyslab.platform.commons.collections.KeyValueCollection KeyValueCollection}s.<br/>
 * The logging configuration helper classes are to parse the application logging options
 * and expose them as a set of typified properties.
 * <p/>
 * Simple initialization example:<code><pre>
 * IGApplicationConfiguration appConfiguration = ...;
 * {@link com.genesyslab.platform.apptemplate.configuration.log.GAppLoggingOptions GAppLoggingOptions} logOpts = new GAppLoggingOptions(appConfiguration, null);
 * </pre></code>
 * This configuration may be used for initialization of specific logging framework configuration.<br/>
 * AppTemplate AB contains configuration helper for Log4j2 logging framework.<br/>
 * And it allows creation of custom configuration helper for some other logging framework.<br/>
 *
 * @see com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration IGApplicationConfiguration
 * @see com.genesyslab.platform.apptemplate.configuration.GApplicationConfiguration GApplicationConfiguration
 * @see com.genesyslab.platform.apptemplate.configuration.GCOMApplicationConfiguration GCOMApplicationConfiguration
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication CfgApplication
 * @see com.genesyslab.platform.applicationblocks.com.ConfService ConfService
 */
package com.genesyslab.platform.apptemplate.configuration.log;
