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
package com.genesyslab.platform.apptemplate.application;

import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;

import com.genesyslab.platform.applicationblocks.com.ICfgObject;


/**
 * Interface describing event on Genesys CME application configuration manager.
 * <p/>
 * Such events are delivered to user defined registered
 * {@link com.genesyslab.platform.applicationblocks.commons.broker.Subscriber Subscriber}'s.
 *
 * @see GFApplicationConfigurationManager
 * @see GFApplicationConfigurationManager#register(com.genesyslab.platform.applicationblocks.commons.broker.Subscriber)
 *        GFApplicationConfigurationManager.register(Subscriber&lt;GFAppCfgEvent&gt;)
 * @see GFApplicationConfigurationManager#register(com.genesyslab.platform.applicationblocks.commons.Action,
 *        com.genesyslab.platform.applicationblocks.commons.Predicate)
 *        GFApplicationConfigurationManager.register(Action&lt;GFAppCfgEvent&gt;, Predicate&lt;GFAppCfgEvent&gt;)
 */
public interface GFAppCfgEvent {

    /**
     * Enumeration with possible types of Genesys CME application configuration manager
     * events.
     *
     * @see GFAppCfgEvent
     */
    enum AppCfgEventType {

        /**
         * Notifies application on configuration server connection opened
         * (or restored) event.<br/>
         * This event is not to deliver configuration objects properties.
         * If this connection has been opened for the first time,
         * or it has been reopened without session restored, then
         * {@link #AppConfigReceived} event will follow.
         */
        ConfSrvConnected,

        /**
         * This event means that configuration server connection has been lost.
         */
        ConfSrvDisconnected,

        /**
         * This event is to deliver the application configuration data
         * after {@link #ConfSrvConnected}, or at explicit application request.
         */
        AppConfigReceived,

        /**
         * This is to notify about some change(s) in the application
         * configuration object properties in the configuration server.<br/>
         * This event also contains <code>ConfData</code> value with
         * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaApplication
         * CfgDeltaApplication}.
         */
        AppConfigUpdated,

        /**
         * This is to notify about some change(s) in the applications' host
         * configuration object properties in the configuration server.<br/>
         * This event also contains <code>ConfData</code> value with
         * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaHost
         * CfgDeltaHost}.
         */
        AppHostUpdated,

        /**
         * This is to notify about some change(s) in properties of application
         * configuration object, which is in the Connections section
         * of the application in the configuration server.<br/>
         * This event also contains <code>ConfData</code> value with
         * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaApplication
         * CfgDeltaApplication}.
         */
        ConnSrvUpdated,

        /**
         * This is to notify about some change(s) in properties of host
         * configuration object, which is configured as a host server for
         * an application, which is in the Connections section
         * of the application in the configuration server.<br/>
         * This event also contains <code>ConfData</code> value with
         * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaHost
         * CfgDeltaHost}.
         */
        ConnHostUpdated,
    }

    /**
     * Type of the Genesys CME application configuration manager event.
     *
     * @return the event type.
     */
    AppCfgEventType getEventType();

    /**
     * Provides the Genesys CME application configuration manager context.
     *
     * @return the application manager context reference.
     */
    GFApplicationContext getAppContext();

    /**
     * Provides "new" application configuration, which is to be used for
     * configuration/reconfiguration of the application.
     *
     * @return the "new" application configuration or <code>null</null>
     * (depending on the event type).
     */
    IGApplicationConfiguration getAppConfig();

    /**
     * Provides original configuration data which caused this event.<br/>
     * It's actual type (class) depends on the event type:<br/>
     * <table><tr><th>Event Type</th><th>Value type</th></tr>
     * <tr><td>{@link AppCfgEventType#ConfSrvConnected ConfSrvConnected},
     *         {@link AppCfgEventType#ConfSrvDisconnected ConfSrvDisconnected}</td>
     *     <td><code>null</code></td></tr>
     * <tr><td>{@link AppCfgEventType#AppConfigReceived AppConfigReceived}</td>
     *     <td>{@link com.genesyslab.platform.applicationblocks.com.objects.CfgApplication
     *             CfgApplication}</td></tr>
     * <tr><td>{@link AppCfgEventType#AppConfigUpdated AppConfigUpdated},
     *         {@link AppCfgEventType#ConnSrvUpdated ConnSrvUpdated}</td>
     *     <td>{@link com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaApplication
     *             CfgDeltaApplication}</td></tr>
     * <tr><td>{@link AppCfgEventType#AppHostUpdated AppHostUpdated},
     *         {@link AppCfgEventType#ConnHostUpdated ConnHostUpdated}</td>
     *     <td>{@link com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaHost
     *             CfgDeltaHost}</td></tr>
     * </table>
     *
     * @return the event originating configuration data object.
     */
    ICfgObject getConfData();
}
