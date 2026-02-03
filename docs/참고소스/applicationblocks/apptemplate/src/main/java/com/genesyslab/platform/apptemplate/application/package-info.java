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
 * This package provides helper components for connectivity with Genesys Configuration Framework.
 * <p/>
 * CME application configuration monitoring may be done with
 * {@link com.genesyslab.platform.apptemplate.application.GFApplicationConfigurationManager
 * GFApplicationConfigurationManager}.<br/>
 * It manages Configuration Server connection, the application configuration reading,
 * and its update notifications.<br/>
 * It also checks availability of Log4j2 logging framework, and automatically enables its configuration
 * by the Genesys CME application logging options.
 * <p/>
 * The shortest way to get an application configured for logging in accordance to the application
 * "log" section may look like:<code><pre>
 * GFApplicationConfigurationManager appManager =
 *         GFApplicationConfigurationManager.newBuilder()
 *         .withCSEndpoint(new Endpoint("CS-primary", csHost1, csPort1))
 *         .withCSEndpoint(new Endpoint("CS-backup", csHost2, csPort2))
 *         .withClientId(clientType, clientName)
 *         .withUserId(csUsername, csPassword)
 *         .build();
 *
 * // If the application uses options other than the logging related, it may
 * // register own handler for appliance of the configuration:
 * appManager.register(new GFAppCfgOptionsEventListener() {
 *     public void handle(final GFAppCfgEvent event) {
 *         Log.getLogger(getClass()).info(
 *                 "The application configuration options received: " + event);
 *         // Initialize or update own application options from 'event.getAppConfig()'
 *     }});
 *
 * appManager.init();
 *
 * // Do the application work...
 *
 * // On the application exit:
 * appManager.done();
 * </pre></code>
 *
 * @see com.genesyslab.platform.apptemplate.application.GFApplicationConfigurationManager
 *        GFApplicationConfigurationManager
 */
package com.genesyslab.platform.apptemplate.application;
