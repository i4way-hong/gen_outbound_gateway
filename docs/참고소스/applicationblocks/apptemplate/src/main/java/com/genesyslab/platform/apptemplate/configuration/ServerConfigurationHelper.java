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
package com.genesyslab.platform.apptemplate.configuration;

import com.genesyslab.platform.apptemplate.util.ConfigurationUtil;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.WildcardEndpoint;

import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.tls.SSLExtendedOptions;
import com.genesyslab.platform.commons.connection.tls.TLSConfiguration;
import com.genesyslab.platform.commons.connection.tls.TLSConfigurationParser;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.connection.configuration.ClientConnectionOptions;

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import javax.net.ssl.SSLContext;


/**
 * Helper class for {@link com.genesyslab.platform.commons.protocol.ServerChannel ServerChannel}
 * connection configuration initialization.
 *
 * <p/>It uses COM AB style application configuration information and creates Endpoint
 * instance initialized with properties like listening TCP port number, ADDP
 * parameters, TLS options, etc.
 *
 * <p/>Usage sample is following:<sample><code><pre>
 *  String appName = "&lt;my-app-name&gt;";
 *  CfgApplication cfgApplication = confService.retrieveObject(
 *          CfgApplication.class, new CfgApplicationQuery(appName));
 *  GCOMApplicationConfiguration appConfig =
 *          new GCOMApplicationConfiguration(cfgApplication);
 *
 *  Endpoint endpoint = <b>ServerConfigurationHelper</b>.<i>createListeningEndpoint</i>(
 *                              appConfig, appConfig.getPortInfo("default"));
 *
 *  ExternalServiceProtocolListener serverChannel =
 *                      new ExternalServiceProtocolListener(endpoint);
 *  serverChannel.setReceiver(requestReceiver);
 *  serverChannel.open();
 *  ...
 * </pre></code></sample>
 */
public class ServerConfigurationHelper {

    /*
     * Defined in a "Framework 8.1 Deployment Guide", https://cafeg.genesyslab.com/docs/DOC-69006
     * Used specifically for IPv6 flags.
     */
    private static final String ENABLE_IPV6_SECTION = "common";


    /**
     * Builds server channel configuration from the given application configuration information.<br/>
     *
     * Resulting Endpoint will contain all the configuration information,
     * so, it's enough to use something like:<code><pre>
     *  Endpoint endpoint = <b>ServerConfigurationHelper</b>.<i>createListeningEndpoint</i>(
     *                              appConfig, appConfig.getPortInfo("default")
     *                      );
     *  ExternalServiceProtocolListener serverChannel =
     *                      new ExternalServiceProtocolListener(endpoint);
     *  ...
     * </pre></code>
     *
     * @param application main application configuration
     * @param portInfo configuration of particular port from the application
     * @return new instance of "local" Endpoint to listen on with attached connection configuration
     * @throws ConfigurationException is thrown in case of problems in the given application configuration
     */
    public static Endpoint createListeningEndpoint(
            final IGApplicationConfiguration application,
            final IGApplicationConfiguration.IGPortInfo portInfo)
                throws ConfigurationException {
        SSLContext sslContext = null;
        SSLExtendedOptions sslExtendeOptions = null;

        GConfigTlsPropertyReader tlsReader = new GConfigTlsPropertyReader(application, portInfo);
        boolean tls = Boolean.parseBoolean(tlsReader.getProperty("tls"));
        if (tls) {
            TLSConfiguration tlsConfiguration = TLSConfigurationParser.parseTlsConfiguration(tlsReader, false);
            sslContext = tlsConfiguration.createSslContext();
            sslExtendeOptions = tlsConfiguration.createSslExtendedOptions();
        }

        return createListeningEndpoint(application, portInfo, tls, sslContext, sslExtendeOptions);
    }

    /**
     * Builds server channel configuration from the given application configuration information.
     * Allows to turn on TLS for the server channel.<br/>
     *
     * Resulting Endpoint will contain all the configuration information,
     * so, it's enough to use something like:<code><pre>
     *
     * final IGApplicationConfiguration.IGPortInfo portConfig = appConfig.getPortInfo("secure");
     *
     * // TLS preparation section follows
     * final ITLSConnectionConfiguration tlsConfiguration = new TLSConnectionConfiguration();
     * TLSConfigurationParser.parseServerTLSConfiguration(appConfiguration, portConfig, tlsConfiguration);
     *
     * // TLS customization code goes here...
     * // As an example, mutual TLS mode is turned on
     * tlsConfiguration.setTLSMutual(true);
     *
     * // Get TLS configuration objects for connection
     * final SSLContext serverSSLContext = TLSConfigurationHelper.createSSLContext(tlsConfiguration);
     * final SSLExtendedOptions serverSSLOptions =
     *         TLSConfigurationHelper.createSSLExtendedOptions(tlsConfiguration);
     * final boolean serverTLSEnabled = true;
     * // TLS preparation section ends
     *
     * Endpoint endpoint = <b>ServerConfigurationHelper</b>.<i>createListeningEndpoint</i>(
     *         appConfig, portConfig,
     *         serverTLSEnabled, serverSSLContext, serverSSLOptions
     *         );
     * ExternalServiceProtocolListener serverChannel =
     *         new ExternalServiceProtocolListener(endpoint);
     * ...
     * </pre></code>
     *
     * @param application main application configuration
     * @param portInfo configuration of particular port from the application
     * @param tlsEnabled if set to {@code true}, TLS is enabled for the server socket;
     *                   if set to {@code false}, TLS will not be used.
     * @param sslContext TLS configuration to be used with this Endpoint. Can be null if TLS will not be used.
     * @param sslOptions Additional TLS options to be used with this Endpoint. Can be null if TLS will not be used.
     * @return new instance of "local" Endpoint to listen on with attached connection configuration
     * @throws ConfigurationException is thrown in case of problems in the given application configuration
     */
    public static Endpoint createListeningEndpoint(
            final IGApplicationConfiguration application,
            final IGApplicationConfiguration.IGPortInfo portInfo,
            final boolean tlsEnabled,
            final SSLContext sslContext,
            final SSLExtendedOptions sslOptions)
                throws ConfigurationException {
        if (application == null) {
            throw new ConfigurationException(
                    "ServerChannelConfiguration requires non-null application configuration");
        }
        IGApplicationConfiguration.IGServerInfo serverInfo = application.getServerInfo();
        if (serverInfo == null) {
            throw new ConfigurationException(
                    "ServerChannelConfiguration requires non-null serverInfo configuration");
        }
        if (portInfo == null) {
            throw new ConfigurationException(
                    "No proper listening port info specified");
        }
        Integer portNum = portInfo.getPort();
        if (portNum == null) {
            throw new ConfigurationException(
                    "No listening port value specified");
        }

        String endpointName = "srv-" + application.getApplicationName()
                + "-" + portNum;

        // null means 'listen on all available network interfaces'
        String nicName = ConfigurationUtil.findTransportParameter(
                portInfo.getTransportParams(),
                // todo proper option name/value in "transport params" for this:
                "listening-nicname");

        PropertyConfiguration config = new PropertyConfiguration();

        fillServerCommonOptions(config, application, portInfo);
        fillServerIPv6Options(config, application, portInfo);
        fillServerAddpOptions(config, application, portInfo);

        if (nicName != null) {
            return new Endpoint(endpointName, nicName, portNum, config, tlsEnabled, sslContext, sslOptions);
        } else {
            return new WildcardEndpoint(endpointName, portNum, config, tlsEnabled, sslContext, sslOptions);
        }
    }


    static void fillServerCommonOptions(
            final PropertyConfiguration targetConfig,
            final IGApplicationConfiguration appConfig,
            final IGApplicationConfiguration.IGPortInfo portConfig) {
        GConfigPropertyReader reader = new GConfigPropertyReader(appConfig, portConfig, "");

        for (OptionDescription od : PsdkDefaultOptions.OPTIONS) {
            String optionValue = reader.getProperty(od);
            if (null != optionValue) {
                targetConfig.setOption(od.getOptionName(), optionValue);
            }
        }
    }

    static void fillServerAddpOptions(
            final ClientConnectionOptions targetConfig,
            final IGApplicationConfiguration appConfig,
            final IGApplicationConfiguration.IGPortInfo portConfig) {
        targetConfig.setUseAddp(true); // todo disable ADDP by some option?
    }

    static void fillServerIPv6Options(
            final ClientConnectionOptions targetConfig,
            final IGApplicationConfiguration appConfig,
            final IGApplicationConfiguration.IGPortInfo portConfig) {
        String enableIpv6Str = Connection.DEFAULT_ENABLE_IPV6;

        KeyValueCollection options = appConfig.getOptions();
        if (null != options) {
            KeyValueCollection common = options.getList(ENABLE_IPV6_SECTION);
            if (null != common) {
                enableIpv6Str = common.getString(Connection.ENABLE_IPV6_KEY);
            }
        }

        targetConfig.setIPv6Enabled(
                ConfigurationUtil.isTrue(enableIpv6Str));
    }
}
