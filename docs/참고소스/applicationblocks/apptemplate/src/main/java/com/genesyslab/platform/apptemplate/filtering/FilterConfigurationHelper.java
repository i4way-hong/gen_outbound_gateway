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
package com.genesyslab.platform.apptemplate.filtering;

import java.util.Collection;
import java.util.List;


import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.NotificationFilter;
import com.genesyslab.platform.applicationblocks.com.NotificationQuery;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.apptemplate.configuration.ConfigurationException;
import com.genesyslab.platform.apptemplate.configuration.GCOMApplicationConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGAppConnConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGHost;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGPortInfo;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGServerInfo;
import com.genesyslab.platform.apptemplate.filtering.impl.configuration.FilterChainConfiguration;
import com.genesyslab.platform.apptemplate.filtering.impl.configuration.FilterChainFactory;
import com.genesyslab.platform.apptemplate.filtering.impl.configuration.FilterChainNotificationHandler;
import com.genesyslab.platform.commons.log.*;
import com.genesyslab.platform.commons.protocol.*;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;

/**
 * Platform SDK protocols log most of the
 * {@link com.genesyslab.platform.commons.protocol.Message Message} content
 * (except secure fields) when debug level is enabled. It is possible
 * to assign message filter for the protocol object with
 * {@link DuplexChannel#setLogMessageFilter(MessageFilter) setLogMessageFilter(filter)} method to filter out
 * unneeded messages and thus reduce log volume. <br>
 * <br>
 * The FilterConfigurationHelper assigns default log filter
 * implementation to the protocol object. Default implementation supports
 * filter configuration from CME and handles configuration updates at runtime. It is
 * possible to modify filter options in CME for the opened protocol. <br>
 * <br>
 * Here is a sample how to create protocol and assign default message filter
 * implementation using apptemplate helpers:
 * 
 * <pre>
 * <code>
 *   String appName = "&lt;my-app-name&gt;";
 *   CfgApplication cfgApplication = confService.retrieveObject(
 *           CfgApplication.class, new CfgApplicationQuery(appName));
 * 
 *   GCOMApplicationConfiguration appConfiguration =
 *           new GCOMApplicationConfiguration(cfgApplication);
 * 
 *   IGAppConnConfiguration connConfig = appConfiguration.getAppServer(CfgAppType.CFGStatServer);
 * 
 *   Endpoint epStatSrv = ClientConfigurationHelper.<i>createEndpoint</i>(
 *               appConfiguration, connConfig,
 *               connConfig.getTargetServerConfiguration());
 * 
 *   StatServerProtocol statProtocol = new StatServerProtocol(epStatSrv);
 *   statProtocol.setClientName(clientName);
 *    
 *   <b>FilterConfigurationHelper.bind(statProtocol, appConfiguration, confService);</b>
 * 
 *   statProtocol.open();  
 * </code>
 * </pre>
 * When protocol object is not required anymore, release assigned filters.
 * <pre>
 * <code>
 *   FilterConfigurationHelper.unbind(statProtocol, confService);
 * </code>
 * </pre>
 * See "Message Filter User guide" how to define filters in CME.
 */
public class FilterConfigurationHelper {

    static ILogger log = Log.getMessageFilteringLogger();

    /**
     * Binds default message filter implementation with a protocol object. Filters can receive configuration updates
     * from Config Server at runtime. 
     * @param protocol Protocol.
     * @param applicationName Name of application where protocol and filters are defined.
     * @param service ConfService to read application and subscribe for configuration updates notification.
     * @throws ConfigurationException
     */
    public static void bind(DuplexChannel protocol, String applicationName, IConfService service)
            throws ConfigurationException {
        if (protocol == null) {
            throw new ConfigurationException("protocol argument can't be null");
        }
        if (applicationName == null) {
            throw new ConfigurationException("applicationName argument can't be null");
        }
        if (service == null) {
            throw new ConfigurationException("COM service argument can't be null");
        }

        GCOMApplicationConfiguration comApp;
        try {
            CfgApplicationQuery query = new CfgApplicationQuery();
            query.setName(applicationName);
            CfgApplication app = service.retrieveObject(CfgApplication.class, query);
            comApp = new GCOMApplicationConfiguration(app);

        } catch (Exception ex) {
            throw new ConfigurationException(String.format("Can't read application %s",
                    applicationName), ex);
        }

        bind(protocol, comApp, service);
    }

    /**
     * Binds default message filter implementation with a protocol object. Filters can receive configuration updates
     * from Config Server at a runtime.      
     * @param protocol Protocol. 
     * @param cfgApplication Application where protocol and filters are defined.
     * @param service ConfService to subscribe for configuration updates notification.
     * @throws ConfigurationException 
     */
    public static void bind(DuplexChannel protocol, IGApplicationConfiguration cfgApplication,
            IConfService service) throws ConfigurationException {
        if (protocol == null) {
            throw new ConfigurationException("protocol argument can't be null");
        }
        if (cfgApplication == null) {
            throw new ConfigurationException("cfgApplication argument can't be null");
        }
        if (service == null) {
            throw new ConfigurationException("COM service argument can't be null");
        }

        Integer appDbid = cfgApplication.getDbid();
        if (appDbid == null) {
            throw new ConfigurationException("Application dbid is null");
        }

        Endpoint endpoint = protocol.getEndpoint();
        if (endpoint == null) {
            throw new ConfigurationException("protocol's endpoint is null");
        }

        List<IGAppConnConfiguration> applicationConnections = cfgApplication.getAppServers();
        IGAppConnConfiguration conn = getConnConfiguration(applicationConnections, endpoint);
        if (conn == null) {
            throw new ConfigurationException(String.format(
                    "No connection object was found in application for protocol endpoint %s",
                    endpoint));
        }

        FilterChainConfiguration chainConfiguration = FilterChainFactory.create(appDbid,
                getTargetApplicationDBID(conn));

        FilterChainNotificationHandler handler = new FilterChainNotificationHandler(
                chainConfiguration);

        enableNotifications(handler, service);

        FilterChainFactory.applyFilterOptions(chainConfiguration, cfgApplication.getOptions(),
                null, null);
        FilterChainFactory.setEnabledFilters(chainConfiguration, conn.getAppParams());
        chainConfiguration.save();

        protocol.setLogMessageFilter(handler);
        protocol.addChannelListener(handler);
        
        if (log.isDebug()) {
            log.debugFormat(
                    "Assigned log FilterChain implementation for the protocol, endpoint=''{0}'', conn dbid={1}, app dbid={2}",
                    new Object[] {endpoint, chainConfiguration.getTargetApplicationDBID(), chainConfiguration.getApplicationDBID()});
        }
    }

    /**
     * Unregisters filter from protocol object. 
     * @param protocol Protocol.
     * @param service  ConfService to unsubscribe from configuration updates notification. 
     * @throws ConfigurationException
     */
    public static void unbind(DuplexChannel protocol, IConfService service)
            throws ConfigurationException {
        if (protocol == null)
            throw new ConfigurationException("The channel argument can't be null");
        if (service == null) {
            throw new ConfigurationException("COM service argument can't be null");
        }

        MessageFilter mf = protocol.getLogMessageFilter();

        if (mf instanceof FilterChainNotificationHandler) {
            protocol.setLogMessageFilter(null);
            FilterChainNotificationHandler handler = (FilterChainNotificationHandler) mf;
            service.unregister(handler);
            handler.getChainConfiguration().clear();

            protocol.removeChannelListener(handler);

            if (log.isDebug()) {
                log.debugFormat("Removed FilterChain implementation from the protocol, endpoint=''{0}''",
                        new Object[] { protocol.getEndpoint() });
            }
        } else if (log.isWarn()) {
            log.warnFormat("Can't remove FilterChain implementation, it wasn't set to the protocol, endpoint=''{0}''",
                        new Object[] { protocol.getEndpoint() });
        }

    }

    private static IGAppConnConfiguration getConnConfiguration(
            Collection<IGAppConnConfiguration> connections, Endpoint endpoint) {
        IGAppConnConfiguration connConfiguration = null;
        if (connections == null || endpoint == null)
            return null;

        String protocolHost = endpoint.getHost();
        int protocolPort = endpoint.getPort();
        if (protocolHost == null || protocolHost.length() == 0) {
            if (log.isWarn()) {
                log.warn("Protocol endpoint doesn't have specified host ");
            }
            return null;
        }

        for (IGAppConnConfiguration conn : connections) {
            IGApplicationConfiguration targetServerConfig = conn.getTargetServerConfiguration();
            if (targetServerConfig == null) {
                if (log.isWarn()) {
                    log.warn("Connection doesn't have application structure. Connection: " + conn);
                }
                continue;
            }

            IGServerInfo srvInfo = targetServerConfig.getServerInfo();
            if (srvInfo == null) {
                if (log.isWarn()) {
                    log.warn("Target application does not contain ServerInfo structure. Connection: "
                            + conn);
                }
                continue;
            }

            IGHost srvHost = srvInfo.getHost();
            if (srvHost == null) {
                if (log.isWarn()) {
                    log.warn("Target application does not contain ServerInfo.Host structure. Connection: "
                            + conn);
                }
                continue;
            }

            if (protocolHost.equals(srvHost.getName())) {
                List<IGPortInfo> portInfos = targetServerConfig.getPortInfos();
                if (portInfos == null || portInfos.isEmpty()) {
                    if (log.isWarn()) {
                        log.warn("No ports information specified in target server configuration. Connection: "
                                + conn);
                    }
                    continue;
                }
                for (IGPortInfo port : portInfos) {
                    Integer p = port.getPort();
                    if (p == null) {
                        if (log.isWarn()) {
                            log.warn("Port is null in portInfos connection configuration. Connection: "
                                    + conn);
                        }
                    } else if (p.equals(protocolPort)) {
                        connConfiguration = conn;
                        break;
                    }
                }
            }
        }
        return connConfiguration;
    }

    private static void enableNotifications(FilterChainNotificationHandler handler,
            IConfService service) throws ConfigurationException {
        NotificationQuery query = new NotificationQuery();
        query.setObjectDbid(handler.getChainConfiguration().getApplicationDBID());
        query.setObjectType(CfgObjectType.CFGApplication);

        NotificationFilter nf = new NotificationFilter(query);
        service.register(handler, nf);
        try {
            service.subscribe(query);
        } catch (Exception ex) {
            throw new ConfigurationException(
                    "Exception occured during filter subscription to Config Server notifications.",
                    ex);
        }
    }

    private static int getTargetApplicationDBID(IGAppConnConfiguration conn)
            throws ConfigurationException {

        IGApplicationConfiguration appConfig = conn.getTargetServerConfiguration();
        if (appConfig == null) {
            throw new ConfigurationException("Target application of the connection object is null");
        }
        Integer targetDbid = appConfig.getDbid();
        if (targetDbid == null) {
            throw new ConfigurationException("Target application object dbid is null");
        }
        return targetDbid;
    }

}
