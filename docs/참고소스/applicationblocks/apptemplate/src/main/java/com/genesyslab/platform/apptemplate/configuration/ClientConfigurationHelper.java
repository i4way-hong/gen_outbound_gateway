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

import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGHost;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGPortInfo;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGServerInfo;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGAppConnConfiguration;
import com.genesyslab.platform.apptemplate.util.ConfigurationUtil;

import com.genesyslab.platform.standby.WSConfig;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration;

import com.genesyslab.platform.configuration.protocol.types.CfgTraceMode;

import com.genesyslab.platform.commons.protocol.Endpoint;

import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions;
import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions.AddpTraceMode;
import com.genesyslab.platform.commons.connection.configuration.ClientConnectionOptions;
import com.genesyslab.platform.commons.connection.configuration.ConnectionConfiguration;
import com.genesyslab.platform.commons.connection.configuration.ManagedConfiguration;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.connection.interceptor.AddpInterceptor;
import com.genesyslab.platform.commons.connection.tls.SSLExtendedOptions;
import com.genesyslab.platform.commons.connection.tls.TLSConfiguration;
import com.genesyslab.platform.commons.connection.tls.TLSConfigurationParser;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import javax.net.ssl.SSLContext;

import java.util.List;
import java.util.ArrayList;


/**
 * Helper class for client connection configuration creation.
 *
 * <p/>It creates {@link Endpoint} with initialized client connection configuration
 * based on {@link IGApplicationConfiguration} interface information.
 *
 * <p/>Also it may create {@link WarmStandbyConfiguration} representing configuration for
 * {@link com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService WarmStandbyService}
 * including pair of connection endpoints - primary and backup
 * (as it is specified in the provided configuration).
 *
 * <p/>COM AB usage example: <example><code><pre>
 *   String appName = "&lt;my-app-name&gt;";
 *   CfgApplication cfgApplication = confService.retrieveObject(
 *           CfgApplication.class, new CfgApplicationQuery(appName));
 *
 *   GCOMApplicationConfiguration appConfiguration =
 *           new GCOMApplicationConfiguration(cfgApplication);
 *
 *   IGAppConnConfiguration connConfig = appConfiguration.getAppServer(CfgAppType.CFGStatServer);
 *
 *   Endpoint epStatSrv = <b>ClientConfigurationHelper</b>.<i>createEndpoint</i>(
 *               appConfiguration, connConfig,
 *               connConfig.getTargetServerConfiguration());
 *
 *   StatServerProtocol statProtocol = new StatServerProtocol(epStatSrv);
 *   statProtocol.setClientName(clientName);
 *
 *   statProtocol.open();
 * </pre></code></example>
 *
 * @see IGApplicationConfiguration
 * @see GApplicationConfiguration
 * @see GCOMApplicationConfiguration
 */
public class ClientConfigurationHelper {

    private static final ILogger log =  Log.getLogger(ClientConfigurationHelper.class);

    /**
     * The id of the default application port.
     */
    private static final String DEFAULT_PORT_ID = "default";

    /*
     Defined in a "Framework 8.1 Deployment Guide", https://cafeg.genesyslab.com/docs/DOC-69006
     Used specifically for IPv6 flags.
     */
    private static final String ENABLE_IPV6_SECTION = "common";


    /**
     * Creates configuration for
     * {@link com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService WarmStandbyService}.
     * Its' result includes parameters for connection to primary and backup servers
     * defined in the specified application configuration information.<br/>
     * TLS configuration
     * WarmStandbyService may be used for automatic connection restoration
     * to primary or backup server when it's got lost.
     *
     * <p/>Method usage example: <example><code><pre>
     *   String appName = "&lt;my-app-name&gt;";
     *   CfgApplication cfgApplication = confService.retrieveObject(
     *           CfgApplication.class, new CfgApplicationQuery(appName));
     *
     *   GCOMApplicationConfiguration appConfiguration =
     *           new GCOMApplicationConfiguration(cfgApplication);
     *
     *   IGAppConnConfiguration connConfig = appConfiguration.getAppServer(CfgAppType.CFGStatServer);
     *
     *   WarmStandbyConfiguration wsConfig =
     *           <b>ClientConfigurationHelper</b>.<i>createWarmStandbyConfig</i>(
     *                   appConfiguration, connConfig);
     *
     *   StatServerProtocol statProtocol = new StatServerProtocol(wsConfig.getActiveEndpoint());
     *   statProtocol.setClientName(clientName);
     *
     *   WarmStandbyService wsService = new WarmStandbyService(statProtocol);
     *   wsService.applyConfiguration(wsConfig);
     *   wsService.start();
     *   statProtocol.beginOpen();
     * </pre></code></example>
     *
     * <b><i>Note:</i></b> This sample contains initialization logic only
     * without proper components dispose functionality.<br/>
     * Do not forget to keep reference to the WarmStandbyService instance and
     * to perform its dispose when it is not needed any more.
     *
     * @param appConfig main application configuration
     * @param connConfig configuration of particular application connection
     * @return WarmStandbyConfiguration instance with connection configurations to primary and
     *         backup servers' endpoints
     * @throws ConfigurationException if provided configuration properties are wrong or insufficient
     */
    public static WarmStandbyConfiguration createWarmStandbyConfig(
            final IGApplicationConfiguration appConfig,
            final IGAppConnConfiguration connConfig)
                    throws ConfigurationException {
        return createWarmStandbyConfig(appConfig, connConfig, false, null, null, false, null, null);
    }

    /**
     * Creates configuration for
     * {@link com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService WarmStandbyService}.
     * Its' result includes parameters for connection to primary and backup servers
     * defined in the specified application configuration information.
     * <p>WarmStandbyService may be used for automatic connection restoration
     * to primary or backup server when it's got lost.</p>
     * <p>It is typical and highly recommended for primary and backup servers to have identical configuration,
     * but TLS can be configured separately for primary and backup. Reason to do it is that TLS certificates
     * can contain server host name as an additional security measure and host names will be different for
     * primary and backup servers.</p>
     *
     * <p>Method usage example: <example><code><pre>
     *   String appName = "&lt;my-app-name&gt;";
     *   CfgApplication cfgApplication = confService.retrieveObject(
     *           CfgApplication.class, new CfgApplicationQuery(appName));
     *
     *   GCOMApplicationConfiguration appConfiguration =
     *           new GCOMApplicationConfiguration(cfgApplication);
     *
     *   IGAppConnConfiguration connConfig = appConfiguration.getAppServer(CfgAppType.CFGStatServer);
     *
     *   // TLS preparation section follows
     *   final ITLSConnectionConfiguration tlsConfiguration = new TLSConnectionConfiguration();
     *   TLSConfigurationParser.parseClientTLSConfiguration(appConfiguration, connConfig, tlsConfiguration);
     *
     *   final IGApplicationConfiguration.IGServerInfo primaryServer =
     *           connConfig.getTargetServerConfiguration().getServerInfo();
     *   final IGApplicationConfiguration.IGServerInfo backupServer =
     *           primaryServer.getBackup().getServerInfo();
     *
     *   // Configure TLS for Primary
     *   tlsConfiguration.setExpectedHostname(primaryServer.getHost().getName());
     *   final SSLContext primarySSLContext = TLSConfigurationHelper.createSSLContext(tlsConfiguration);
     *   final SSLExtendedOptions primarySSLOptions = TLSConfigurationHelper.createSSLExtendedOptions(tlsConfiguration);
     *   final boolean primaryTLSEnabled = true;
     *
     *   // Configure TLS for Backup
     *   tlsConfiguration.setExpectedHostname(backupServer.getHost().getName());
     *   final SSLContext backupSSLContext = TLSConfigurationHelper.createSSLContext(tlsConfiguration);
     *   final SSLExtendedOptions backupSSLOptions = TLSConfigurationHelper.createSSLExtendedOptions(tlsConfiguration);
     *   final boolean backupTLSEnabled = true;
     *   // TLS preparation section ends
     *
     *   WarmStandbyConfiguration wsConfig = <b>ClientConfigurationHelper</b>.<i>createWarmStandbyConfig</i>(
     *           appConfiguration, connConfig,
     *           primaryTLSEnabled, primarySSLContext, primarySSLOptions,
     *           backupTLSEnabled, backupSSLContext, backupSSLOptions);
     *
     *   StatServerProtocol statProtocol = new StatServerProtocol(wsConfig.getActiveEndpoint());
     *   statProtocol.setClientName(clientName);
     *
     *   WarmStandbyService wsService = new WarmStandbyService(statProtocol);
     *   wsService.applyConfiguration(wsConfig);
     *   wsService.start();
     *   statProtocol.beginOpen();
     * </pre></code></example>
     *
     * <b><i>Note:</i></b> This sample contains initialization logic only
     * without proper components dispose functionality.<br/>
     * Do not forget to keep reference to the WarmStandbyService instance and
     * to perform its dispose when it is not needed any more.
     *
     * @param appConfig main application configuration
     * @param connConfig configuration of particular application connection
     * @param primaryTLSEnabled "TLS enabled" flag for primary connection
     * @param primarySSLContext TLS configuration for primary connection
     * @param primarySSLOptions TLS extended options for primary connection
     * @param backupTLSEnabled "TLS enabled" flag for backup connection
     * @param backupSSLContext TLS configuration for backup connection
     * @param backupSSLOptions TLS extended options for backup connection
     * @return WarmStandbyConfiguration instance with connection configurations to primary and
     *         backup servers' endpoints
     * @throws ConfigurationException if provided configuration properties are wrong or insufficient
     */
    public static WarmStandbyConfiguration createWarmStandbyConfig(
            final IGApplicationConfiguration appConfig,
            final IGAppConnConfiguration connConfig,
            final boolean primaryTLSEnabled,
            final SSLContext primarySSLContext,
            final SSLExtendedOptions primarySSLOptions,
            final boolean backupTLSEnabled,
            final SSLContext backupSSLContext,
            final SSLExtendedOptions backupSSLOptions)
                throws ConfigurationException {
        if (appConfig == null || connConfig == null) {
            throw new ConfigurationException(
                    "Null argument is not allowed");
        }
        IGApplicationConfiguration targetPrimary = connConfig.getTargetServerConfiguration();
        if (targetPrimary == null) {
            throw new ConfigurationException(
                    "Target connection application is not configured");
        }
        if (targetPrimary.getServerInfo() == null) {
            throw new ConfigurationException(
                    "Target connection application should have ServerInfo");
        }

        try {
            IGApplicationConfiguration targetBackup = targetPrimary.getServerInfo().getBackup();
            Endpoint primaryEp = createEndpoint(
                    appConfig, connConfig, targetPrimary, primaryTLSEnabled, primarySSLContext, primarySSLOptions);
            Endpoint backupEp;
            if (targetBackup != null) {
                backupEp = createEndpoint(
                        appConfig, connConfig, targetBackup, backupTLSEnabled, backupSSLContext, backupSSLOptions);
                backupEp = correctBackupEndpoint(backupEp);
            } else {
                backupEp = primaryEp; // todo any adjustments if no backup specified?
            }

            WarmStandbyConfiguration ret = new WarmStandbyConfiguration(primaryEp, backupEp);

            ret.setTimeout(targetPrimary.getServerInfo().getTimeout());

            Integer attempts = targetPrimary.getServerInfo().getAttempts();
            if (attempts != null) {
                ret.setAttempts(attempts.shortValue());
            }

            return ret;
        }
        catch(Exception ex) {
            throw new ConfigurationException("Creation of WarmStandbyConfiguration has failed", ex);
        }
    }

    /**
     * Creates configuration for new implementation of the warm standby:
     * {@link com.genesyslab.platform.standby.WarmStandby WarmStandby}. Its'
     * result includes parameters for connection to primary and backup servers
     * defined in the specified application configuration information.<br/>
     * If TLS configuration specified in CME, result will also include secure
     * connection context. WarmStandby may be used for automatic connection
     * restoration to primary or backup server when it's got lost.
     *
     * <p/>
     * Method usage example: <example><code><pre>
     *   String appName = "&lt;my-app-name&gt;";
     *   CfgApplication cfgApplication = confService.retrieveObject(
     *           CfgApplication.class, new CfgApplicationQuery(appName));
     * 
     *   GCOMApplicationConfiguration appConfiguration =
     *           new GCOMApplicationConfiguration(cfgApplication);
     * 
     *   IGAppConnConfiguration connConfig = appConfiguration.getAppServer(CfgAppType.CFGStatServer);
     * 
     *   WSConfig wsConfig =
     *           <b>ClientConfigurationHelper</b>.<i>createWarmStandbyConfigEx</i>(
     *                   appConfiguration, connConfig);
     * 
     *   StatServerProtocol statProtocol = new StatServerProtocol();
     *   statProtocol.setClientName(clientName);
     * 
     *   WarmStandby warmStandby = new WarmStandby(statProtocol);
     *   warmStandby.setConfig(wsConfig);
     *   warmStandby.autoRestore();
     * </pre></code></example>
     *
     * <b><i>Note:</i></b> This sample contains initialization logic only
     * without proper components dispose functionality.<br/>
     * Call <code>warmStandby.close()</code> or directly
     * <code>statProtocol.close()</code> to release resources.
     *
     * @param appConfig main application configuration
     * @param connConfig configuration of particular application connection
     * @return WSConfig instance with connection configurations to primary and
     *         backup servers' endpoints
     * @throws ConfigurationException if provided configuration properties are wrong or insufficient
     */
    public static WSConfig createWarmStandbyConfigEx(
            final IGApplicationConfiguration appConfig,
            final IGAppConnConfiguration connConfig)
            throws ConfigurationException {
        if (appConfig == null || connConfig == null ) {
             throw new ConfigurationException("appConfig, connConfig  must not be null");
        }
        return createWarmStandbyConfigEx(null, appConfig, connConfig, connConfig.getTargetServerConfiguration());
    }


    /**
     * Creates configuration for new implementation of the warm standby:
     * {@link com.genesyslab.platform.standby.WarmStandby WarmStandby}. Its'
     * result includes parameters for connection to primary and backup servers
     * defined in the specified application configuration information.<br/>
     * If TLS configuration specified in CME, result will also include secure
     * connection context. WarmStandby may be used for automatic connection
     * restoration to primary or backup server when it's got lost.
     *
     * <p/>
     * Method usage example: <example><code><pre>
     *   String appName = "&lt;my-app-name&gt;";
     *   CfgApplication cfgApplication = confService.retrieveObject(
     *           CfgApplication.class, new CfgApplicationQuery(appName));
     * 
     *   GCOMApplicationConfiguration appConfiguration =
     *           new GCOMApplicationConfiguration(cfgApplication);
     * 
     *   IGAppConnConfiguration connConfig = appConfiguration.getAppServer(CfgAppType.CFGStatServer);
     * 
     *   WSConfig wsConfig =
     *           <b>ClientConfigurationHelper</b>.<i>createWarmStandbyConfigEx</i>(
     *                   appConfiguration, connConfig);
     * 
     *   StatServerProtocol statProtocol = new StatServerProtocol();
     *   statProtocol.setClientName(clientName);
     * 
     *   WarmStandby warmStandby = new WarmStandby(statProtocol);
     *   warmStandby.setConfig(wsConfig);
     *   warmStandby.autoRestore();
     * </pre></code></example>
     *
     * <b><i>Note:</i></b> This sample contains initialization logic only
     * without proper components dispose functionality.<br/>
     * Call <code>warmStandby.close()</code> or directly
     * <code>statProtocol.close()</code> to release resources.
     *
     * @param wsconfigName name of the WarmStandby configura and it's primary Endpoint
     * @param appConfig main application configuration
     * @param connConfig configuration of particular application connection
     * @param targetPrimary application configuration of the primary server
     * @return WSConfig instance with connection configurations to primary and
     *         backup servers' endpoints
     * @throws ConfigurationException if provided configuration properties are wrong or insufficient
     */
    public static WSConfig createWarmStandbyConfigEx(
            final String wsconfigName,
            final IGApplicationConfiguration appConfig,
            final IGAppConnConfiguration connConfig,
            final IGApplicationConfiguration targetPrimary)
            throws ConfigurationException {

        if (appConfig == null || connConfig == null || targetPrimary == null ) {
            throw new ConfigurationException("appConfig,  connConfig,  targetPrimary must not be null");
        }

        IGApplicationConfiguration.IGServerInfo primaryServerInfo = targetPrimary.getServerInfo();
        if (primaryServerInfo == null) {
            throw new ConfigurationException(
                    "Target connection application should have ServerInfo");
        }

        IGApplicationConfiguration targetBackup = targetPrimary.getServerInfo()    .getBackup();
        GApplicationConfiguration.IGServerInfo backupServerInfo = null;
        if (targetBackup != null) {
            backupServerInfo = targetBackup.getServerInfo();
            if (backupServerInfo == null) {
                throw new ConfigurationException(
                        "Backup Server application should have ServerInfo");
            }
        }

        try {
            SSLContext primarySslContext = null;
            SSLExtendedOptions primarySslOptions = null;

            SSLContext backupSslContext = null;
            SSLExtendedOptions backupSslOptions = null;

            GConfigTlsPropertyReader tlsReader = new GConfigTlsPropertyReader(
                    appConfig, connConfig);
            boolean securedConn = Boolean.parseBoolean(tlsReader.getProperty("tls"));
            if (securedConn) {
                TLSConfiguration tlsConfiguration = TLSConfigurationParser
                        .parseTlsConfiguration(tlsReader, true);

                // Configure TLS for Primary
                primarySslContext = tlsConfiguration.createSslContext();
                primarySslOptions = tlsConfiguration.createSslExtendedOptions();

                // Configure TLS for Backup
                if (backupServerInfo != null) {
                    backupSslContext = tlsConfiguration.createSslContext();
                    backupSslOptions = tlsConfiguration.createSslExtendedOptions();
                }
            }

            List<Endpoint> endpoints = new ArrayList<Endpoint>(2);

            Endpoint primaryEp = createEndpoint(wsconfigName, appConfig, connConfig,
                    targetPrimary, securedConn, primarySslContext,
                    primarySslOptions);

            endpoints.add(primaryEp);

            if (targetBackup != null) {
                String backupEpName = wsconfigName != null ? targetBackup.getApplicationName() : null;

                Endpoint backupEp = createEndpoint(backupEpName, appConfig, connConfig,
                        targetBackup, securedConn, backupSslContext,
                        backupSslOptions);
                backupEp = correctBackupEndpoint(backupEp);
                endpoints.add(backupEp);
            }

            WSConfig ret = wsconfigName != null ? new WSConfig(wsconfigName) : new WSConfig();
            ret.setEndpoints(endpoints);
            fillWSTimouts(ret, connConfig, targetBackup);

            return ret;
        } catch (Exception ex) {
            throw new ConfigurationException(
                    "Creation of WarmStandbyConfiguration has failed", ex);
        }
    }


    /**
     * It creates configured Endpoint instance with attached connection configuration
     * using {@link IGApplicationConfiguration} structures.
     *
     * <p/>Example using COM AB: <example><code><pre>
     *   String appName = "&lt;my-app-name&gt;";
     *   CfgApplication cfgApplication = confService.retrieveObject(
     *           CfgApplication.class, new CfgApplicationQuery(appName));
     *
     *   GCOMApplicationConfiguration appConfiguration =
     *           new GCOMApplicationConfiguration(cfgApplication);
     *
     *   IGAppConnConfiguration connConfig = appConfiguration.getAppServer(CfgAppType.CFGTServer);
     *
     *   Endpoint epTSrv = <b>ClientConfigurationHelper</b>.<i>createEndpoint</i>(
     *               appConfiguration, connConfig,
     *               connConfig.getTargetServerConfiguration());
     *
     *   TServerProtocol tsProtocol = new TServerProtocol(epTSrv);
     *   tsProtocol.setClientName(clientName);
     *
     *   tsProtocol.open();
     * </pre></code></example>
     *
     * @param appConfig main application configuration
     * @param connConfig configuration of particular application connection
     * @param targetServerConfig target server application configuration
     * @return configured Endpoint containing appropriate connection configuration attached
     * @throws ConfigurationException if provided configuration properties are wrong or insufficient
     */
    public static Endpoint createEndpoint(
            final IGApplicationConfiguration appConfig,
            final IGAppConnConfiguration connConfig,
            final IGApplicationConfiguration targetServerConfig)
            throws ConfigurationException {

        SSLContext sslContext = null;
        SSLExtendedOptions sslExtendeOptions = null;

        GConfigTlsPropertyReader tlsReader = new GConfigTlsPropertyReader(appConfig, connConfig);
        boolean tls = Boolean.parseBoolean(tlsReader.getProperty("tls"));
        if (tls) {
            TLSConfiguration tlsConfiguration = TLSConfigurationParser.parseTlsConfiguration(tlsReader, true);
            sslContext = tlsConfiguration.createSslContext();
            sslExtendeOptions = tlsConfiguration.createSslExtendedOptions();
        }

        return createEndpoint(appConfig, connConfig, targetServerConfig, tls, sslContext, sslExtendeOptions);
    }

    /**
     * It creates configured Endpoint instance with attached connection configuration
     * using {@link IGApplicationConfiguration} structures. Allows to specify if TLS should be turned on immediately
     * upon connection.
     *
     * <p/>Example using COM AB: <example><code><pre>
     * String appName = "&lt;my-app-name&gt;";
     * CfgApplication cfgApplication = confService.retrieveObject(
     *         CfgApplication.class, new CfgApplicationQuery(appName));
     *
     * GCOMApplicationConfiguration appConfiguration =
     *         new GCOMApplicationConfiguration(cfgApplication);
     *
     * IGAppConnConfiguration connConfig = appConfiguration.getAppServer(CfgAppType.CFGTServer);
     *
     * // TLS preparation section follows
     * final ITLSConnectionConfiguration tlsConfiguration = new TLSConnectionConfiguration();
     * TLSConfigurationParser.parseClientTLSConfiguration(appConfiguration, connConfig, tlsConfiguration);
     *
     * // TLS customization code goes here...
     * // As an example, host name verification is turned on
     * final IGApplicationConfiguration.IGServerInfo targetServer =
     *         connConfig.getTargetServerConfiguration().getServerInfo();
     * tlsConfiguration.setExpectedHostname(targetServer.getHost().getName());
     *
     * // Get TLS configuration objects for connection
     * final SSLContext serverSSLContext = TLSConfigurationHelper.createSSLContext(tlsConfiguration);
     * final SSLExtendedOptions serverSSLOptions =
     *         TLSConfigurationHelper.createSSLExtendedOptions(tlsConfiguration);
     * final boolean serverTLSEnabled = true;
     * // TLS preparation section ends
     *
     * Endpoint epTSrv = ClientConfigurationHelper.createEndpoint(
     *         appConfiguration, connConfig,
     *         connConfig.getTargetServerConfiguration(),
     *         serverTLSEnabled, serverSSLContext, serverSSLOptions);
     *
     * TServerProtocol tsProtocol = new TServerProtocol(epTSrv);
     * tsProtocol.setClientName(clientName);
     *
     * tsProtocol.open();
     * </pre></code></example>
     *
     * @param appConfig main application configuration
     * @param connConfig configuration of particular application connection
     * @param targetServerConfig target server application configuration
     * @param tlsEnabled true - TLS will be turned on immediately upon connection, false -
     *                   TLS is not turned on immediately but can be turned on by protocol if
     *                   "TLS Auto-detect" feature is used.
     * @param sslContext SSL configuration for the endpoint
     * @param sslOptions SSL extended options for the endpoint
     * @return configured Endpoint containing appropriate connection configuration attached
     * @throws ConfigurationException if provided configuration properties are wrong or insufficient
     */
    public static Endpoint createEndpoint(
            final IGApplicationConfiguration appConfig,
            final IGAppConnConfiguration connConfig,
            final IGApplicationConfiguration targetServerConfig,
            final boolean tlsEnabled,
            final SSLContext sslContext,
            final SSLExtendedOptions sslOptions)
                    throws ConfigurationException {
        return createEndpoint(null, appConfig, connConfig, targetServerConfig, tlsEnabled, sslContext, sslOptions);
    }


    /**
     * It creates configured Endpoint instance with attached connection configuration
     * using {@link IGApplicationConfiguration} structures. Allows to specify if TLS should be turned on immediately
     * upon connection.
     *
     * <p/>Example using COM AB: <example><code><pre>
     * String appName = "&lt;my-app-name&gt;";
     * CfgApplication cfgApplication = confService.retrieveObject(
     *         CfgApplication.class, new CfgApplicationQuery(appName));
     *
     * GCOMApplicationConfiguration appConfiguration =
     *         new GCOMApplicationConfiguration(cfgApplication);
     *
     * IGAppConnConfiguration connConfig = appConfiguration.getAppServer(CfgAppType.CFGTServer);
     *
     * // TLS preparation section follows
     * final ITLSConnectionConfiguration tlsConfiguration = new TLSConnectionConfiguration();
     * TLSConfigurationParser.parseClientTLSConfiguration(appConfiguration, connConfig, tlsConfiguration);
     *
     * // TLS customization code goes here...
     * // As an example, host name verification is turned on
     * final IGApplicationConfiguration.IGServerInfo targetServer =
     *         connConfig.getTargetServerConfiguration().getServerInfo();
     * tlsConfiguration.setExpectedHostname(targetServer.getHost().getName());
     *
     * // Get TLS configuration objects for connection
     * final SSLContext serverSSLContext = TLSConfigurationHelper.createSSLContext(tlsConfiguration);
     * final SSLExtendedOptions serverSSLOptions =
     *         TLSConfigurationHelper.createSSLExtendedOptions(tlsConfiguration);
     * final boolean serverTLSEnabled = true;
     * // TLS preparation section ends
     *
     * Endpoint epTSrv = ClientConfigurationHelper.createEndpoint(
     *         appConfiguration, connConfig,
     *         connConfig.getTargetServerConfiguration(),
     *         serverTLSEnabled, serverSSLContext, serverSSLOptions);
     *
     * TServerProtocol tsProtocol = new TServerProtocol(epTSrv);
     * tsProtocol.setClientName(clientName);
     *
     * tsProtocol.open();
     * </pre></code></example>
     *
     * @param appConfig main application configuration
     * @param connConfig configuration of particular application connection
     * @param targetServerConfig target server application configuration
     * @param tlsEnabled true - TLS will be turned on immediately upon connection, false -
     *                   TLS is not turned on immediately but can be turned on by protocol if
     *                   "TLS Auto-detect" feature is used.
     * @param sslContext SSL configuration for the endpoint
     * @param sslOptions SSL extended options for the endpoint
     * @param endpointName endpoint name
     * @return configured Endpoint containing appropriate connection configuration attached
     * @throws ConfigurationException if provided configuration properties are wrong or insufficient
     */
    public static Endpoint createEndpoint(
            final String endpointName,
            final IGApplicationConfiguration appConfig,
            final IGAppConnConfiguration connConfig,
            final IGApplicationConfiguration targetServerConfig,
            final boolean tlsEnabled,
            final SSLContext sslContext,
            final SSLExtendedOptions sslOptions)
                throws ConfigurationException {
        if (appConfig == null || connConfig == null || targetServerConfig == null) {
            throw new ConfigurationException(
                    "Parameters appConfig, connConfig and targetServerConfig must not be null");
        }

        String epName = endpointName != null ? endpointName : "conn-"
                + appConfig.getApplicationName() + "-" + targetServerConfig.getApplicationName();

        IGServerInfo srvInfo = targetServerConfig.getServerInfo();
        if (srvInfo == null) {
            throw new ConfigurationException(
                    "Target application does not contain ServerInfo structure");
        }
        IGHost srvHost = srvInfo.getHost();
        if (srvHost == null) {
            throw new ConfigurationException(
                    "Target application does not contain ServerInfo.Host structure");
        }
        String host = srvHost.getName();

        String portId = connConfig.getPortId();
        if (portId == null || portId.length()==0) {
            portId = DEFAULT_PORT_ID;
        }
        List<IGPortInfo> portInfos = targetServerConfig.getPortInfos();
        if (portInfos == null || portInfos.isEmpty()) {
            throw new ConfigurationException(
                    "No ports information specified in target server configuration");
        }
        IGPortInfo portInfo = GApplicationConfiguration.getPortInfo(portInfos, portId);
        if (portInfo == null || portInfo.getPort() == null) {
            throw new ConfigurationException(
                    "No '" + portId + "' port information specified in target server configuration");
        }
        int portNum = portInfo.getPort();

        PropertyConfiguration config = new PropertyConfiguration();

        fillClientCommonOptions(config, appConfig, connConfig);
        fillClientIPv6Options(config, appConfig, connConfig);
        fillClientAddpOptions(config, connConfig);

        return new Endpoint(epName, host, portNum, config, tlsEnabled, sslContext, sslOptions);
    }


    static void fillClientCommonOptions(
            final PropertyConfiguration targetConfig,
            final IGApplicationConfiguration appConfig,
            final IGAppConnConfiguration connConfig)
                    throws ConfigurationException {
        GConfigPropertyReader reader = new GConfigPropertyReader(appConfig, connConfig, "");

        for (OptionDescription od : PsdkDefaultOptions.OPTIONS) {
            String optionValue = reader.getProperty(od);
            if (null != optionValue) {
                targetConfig.setOption(od.getOptionName(), optionValue);
            }
        }
    }

    static void fillClientIPv6Options(
            final ClientConnectionOptions    targetConfig,
            final IGApplicationConfiguration appConfig,
            final IGAppConnConfiguration     connConfig) {
        String enableIpv6Str = Connection.DEFAULT_ENABLE_IPV6;

        KeyValueCollection options = appConfig.getOptions();
        if (null != options) {
            KeyValueCollection common = options.getList(ENABLE_IPV6_SECTION);
            if (null != common) {
                enableIpv6Str = common.getString(Connection.ENABLE_IPV6_KEY);
            }
        }

        String ipVersion = Connection.DEFAULT_IP_VERSION;

        if (null != connConfig) {
            String tParams = connConfig.getTransportParams();
            if (null != tParams) {
                ipVersion = ConfigurationUtil.findTransportParameter(
                        tParams, Connection.IP_VERSION_KEY);

                String param = ConfigurationUtil.findTransportParameter(
                        tParams, Connection.ENABLE_IPV6_KEY);
                if(param !=  null) {
                    enableIpv6Str = param;
                }
            }
        }

        targetConfig.setIPv6Enabled(
                ConfigurationUtil.isTrue(enableIpv6Str));
        targetConfig.setIPVersion(ipVersion);
    }

    static void fillClientAddpOptions(
            final ClientADDPOptions targetConfig,
            final IGAppConnConfiguration connConfig) {

        targetConfig.setUseAddp(AddpInterceptor.NAME.equalsIgnoreCase(
                connConfig.getConnProtocol()));

        targetConfig.setAddpClientTimeout(connConfig.getTimeoutLocal());
        targetConfig.setAddpServerTimeout(connConfig.getTimeoutRemote());

        CfgTraceMode trMode = connConfig.getTraceMode();
        if (trMode != null) {
            targetConfig.setAddpTraceMode(AddpTraceMode.parse(trMode.toString()));
        }
    }

    static void fillWSTimouts(WSConfig config,
            IGAppConnConfiguration clientConnection,
            IGApplicationConfiguration backupServer) {

        int[] intDelays = null;

        String applicationParameters = clientConnection.getAppParams();

        if (applicationParameters != null) {
            String retryDelay = ConfigurationUtil.findAppParameter(applicationParameters,
                    PsdkDefaultOptions.WSRetry_Delay);

            if (retryDelay != null) {
                String[] strDelays = retryDelay.split(",");
                intDelays = new int[strDelays.length];
                for (int i = 0; i < intDelays.length; i++) {
                    intDelays[i] = millisecondsTimout(PsdkDefaultOptions.WSRetry_Delay, strDelays[i], 1000);
                }
            }

            String reconnectRange = ConfigurationUtil.findAppParameter(
                    applicationParameters,
                    PsdkDefaultOptions.WSReconnect_Rand_Delay);
            if (reconnectRange != null) {
                config.setReconnectionRandomDelayRange(
                        millisecondsTimout(PsdkDefaultOptions.WSReconnect_Rand_Delay, reconnectRange, 0));
            }

            String opentimeout = ConfigurationUtil.findAppParameter(
                    applicationParameters, 
                    PsdkDefaultOptions.WSTimeout);
            if (opentimeout != null) {
                config.setTimeout(millisecondsTimout(PsdkDefaultOptions.WSTimeout, opentimeout, 0));
            }
        }

        if (intDelays == null) {
            final IGApplicationConfiguration srv = clientConnection.getTargetServerConfiguration();
            final IGServerInfo srvInfo = (srv != null) ? srv.getServerInfo() : null;
            if (srvInfo != null) {
                final Integer tmt = srvInfo.getTimeout();
                if (tmt != null && tmt.intValue() > 0) {
                    intDelays = new int[] {tmt.intValue() * 1000};
                }
            }
        }

        if (intDelays != null && intDelays.length > 0) {
            config.setRetryDelay(intDelays);
        }

        if (backupServer != null) {
            KeyValueCollection options = backupServer.getOptions();
            if (options != null) {
                KeyValueCollection wsSection = options.getList("warm-standby");
                if (wsSection != null) {
                    String backupDelay = wsSection
                            .getString(PsdkDefaultOptions.WSBackup_Delay);
                    if (backupDelay != null) {
                        config.setBackupDelay(millisecondsTimout(PsdkDefaultOptions.WSBackup_Delay, backupDelay, 0));
                    }
                }
            }
        }
    }

    private static int millisecondsTimout(String optionName, String seconds, int defaultValue) {
        int value;
        try {
            value = Integer.parseInt(seconds) * 1000;
        } catch (NumberFormatException ex) {
            log.warn(optionName + " unexpected value: " +  seconds + ". Setting default value: "+defaultValue);
            value = defaultValue;
        }
        if (value < 0) {
            log.warn(optionName + " can't be negative: " +  value + ". Setting default value: "+defaultValue );
            value = defaultValue;
        }
        return value;
    }


    /**
     * Internal method for adjustment of backup endpoint connection configuration.<br/>
     *
     * Currently it does swap 'transport-port' and 'backup-transport-port' options
     * responsible for client side port binding feature.
     *
     * @param endpoint initially created endpoint.
     * @return New endpoint instance with corrected connection configuration or given one if no corrections needed.
     */
    static Endpoint correctBackupEndpoint(
            final Endpoint endpoint) {
        ConnectionConfiguration conf = endpoint.getConfiguration();

        if (conf != null) {
            boolean isChanged = false;

            String sPort = conf.getOption(Connection.BIND_PORT_KEY);
            String sBPort = conf.getOption(Connection.BACKUP_BIND_PORT_KEY);
            if (sPort != null || sBPort != null) {
                try {
                    if (conf instanceof ManagedConfiguration) {
                        conf = ((ManagedConfiguration)conf).getConfiguration().clone();
                    }
                    else {
                        conf = conf.clone();
                    }
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                conf.setOption(Connection.BIND_PORT_KEY, sBPort);
                conf.setOption(Connection.BACKUP_BIND_PORT_KEY, sPort);
                isChanged = true;
            }

            if (isChanged) {
                return new Endpoint(endpoint.getName(),
                        endpoint.getHost(), endpoint.getPort(),
                        conf, conf.getBoolean(Connection.TLS_KEY),
                        endpoint.getSSLContext(),
                        endpoint.getSSLOptions());
            }
        }

        return endpoint;
    }
}
