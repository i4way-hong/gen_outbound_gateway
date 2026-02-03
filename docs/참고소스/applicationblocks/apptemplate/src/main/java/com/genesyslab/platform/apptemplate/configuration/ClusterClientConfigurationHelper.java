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

import com.genesyslab.platform.apptemplate.configuration.GApplicationConfiguration.GAppConnConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGAppConnConfiguration;

import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.cache.IConfCache;
import com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;

import com.genesyslab.platform.standby.WSConfig;

import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;


/**
 * Provides helpers for {@link com.genesyslab.platform.clusterprotocol.ClusterProtocol ClusterProtocol}
 * configuration.
 * <p/>
 * Usage samples:<ul>
 * <li>Simple client application connecting to any UCS cluster.<br/>
 * This sample checks connection configuration for "WCC mode" UCS 9.0 cluster,
 * then for "WDE mode" UCS cluster, and then for "legacy mode" connection to UCS (primary/backup) server.
 * <code><pre>
 * // Take "my application configuration" from context, or read it in a way like this:
 * IGApplicationConfiguration myApp = new GCOMApplicationConfiguration(
 *         confService.retrieveObject(CfgApplication.class,
 *                 new CfgApplicationQuery(myAppName)));
 *
 * // For the first, try UCS 9 connection cluster:
 * List&lt;WSConfig&gt; conns = ClusterClientConfigurationHelper.createRefClusterProtocolEndpoints(
 *         confService, myApp, CfgAppType.CFGContactServer);
 * if (conns == null || conns.isEmpty()) {
 *     // If there is no UCS 9 cluster connected, then we try older UCS cluster, or simple UCS connection(s):
 *     conns = ClusterClientConfigurationHelper.createClusterProtocolEndpoints(
 *           myApp, CfgAppType.CFGContactServer);
 * }
 *
 * ucsClusterProtocol.setNodes(conns);
 * </pre></code></li>
 * <li>WCC based cluster node application connecting to any UCS cluster.<br/>
 * This sample works in context of WCC.
 * <code><pre>
 * // Take "my application configuration" from context, or read it in a way like this:
 * final IGApplicationConfiguration myApp = new GCOMApplicationConfiguration(
 *         confService.retrieveObject(CfgApplication.class,
 *                 new CfgApplicationQuery(myAppName)));
 *
 * IGApplicationConfiguration myClusterApp = null;
 * // if we do not have 'myClusterApp' from WCC context, we may take it by this way:
 * final List&lt;IGAppConnConfiguration&gt; clusters = GApplicationConfiguration
 *         .getAppServers(myApp.getAppServers(), CfgAppType.CFGApplicationCluster);
 * if (clusters != null) {
 *     if (clusters.size() == 1) {
 *         myClusterApp = clusters.get(0).getTargetServerConfiguration();
 *         log.infoFormat(
 *                 "Application is recognized as a node of cluster ''{0}''",
 *                 myClusterApp.getApplicationName());
 *     } else if (clusters.size() &gt; 1) {
 *         log.error("Application has more than one application cluster connected"
 *                 + " - its treated as a standalone app");
 *     }
 * }
 *
 * // Select application cluster connection start point:
 * final IGApplicationConfiguration connSrc = (myClusterApp != null) ? myClusterApp : myApp;
 *
 * // For the first, try UCS 9 connection cluster:
 * List&lt;WSConfig&gt; conns = ClusterClientConfigurationHelper.createRefClusterProtocolEndpoints(
 *         confService, connSrc, CfgAppType.CFGContactServer);
 * if (conns == null || conns.isEmpty()) {
 *     // If there is no UCS 9 cluster connected, then we try older UCS cluster, or simple UCS connection(s):
 *     conns = ClusterClientConfigurationHelper.createClusterProtocolEndpoints(
 *             connSrc, CfgAppType.CFGContactServer);
 * }
 *
 * ucsClusterProtocol.setNodes(conns);
 * </pre></code></li></ul>
 *
 * @see com.genesyslab.platform.clusterprotocol.ClusterProtocol#setNodes(Iterable) ClusterProtocol.setNodes(nodes)
 */
public class ClusterClientConfigurationHelper {

    private static final ILogger log = Log.getLogger(ClusterClientConfigurationHelper.class);


    /**
     * Creates the list of {@link WSConfig} for {@link com.genesyslab.platform.clusterprotocol.ClusterProtocol
     * Cluster Protocol}.<br/>
     * It transparently supports both Cluster and legacy primary/backup WarmStanby configurations.
     * If client application is connected to a cluster, then WSConfig objects will be created
     * from cluster application connections. If no servers of the specified type found in cluster connections,
     * helper will try to resolve them directly from client application connections.<br/>
     * Only servers of the specified type will be included in the result {@link WSConfig} list.
     *
     * @param clientApp
     *            the client application that has connection to a cluster of servers
     *            or to a single server
     * @param serverType
     *            type of the server(s) to create connection configuration to
     * @return collection of WSConfig objects
     * @throws ConfigurationException
     */
    public static List<WSConfig> createClusterProtocolEndpoints(
            final IGApplicationConfiguration clientApp,
            final CfgAppType serverType)
                    throws ConfigurationException {
        checkArgsNotNull(clientApp, "clientApp");
        checkArgsNotNull(serverType, "serverType");

        final Collection<IGAppConnConfiguration> clientConnections = clientApp.getAppServers();
        if (clientConnections == null || clientConnections.size() == 0) {
            log.debugFormat("Client application ''{0}'' has no connections", clientApp.getApplicationName());
            return new LinkedList<WSConfig>();
        }

        List<WSConfig> wsconfList =
                parseClusterConfiguration(clientApp, clientConnections, serverType);
        if (wsconfList.size() == 0) {
            wsconfList = parseSimpleConfiguration(clientApp, clientConnections, serverType);
            if (log.isDebug()) {
                log.debugFormat("Client application ''{0}'' connections to ''{1}'' count: {2}",
                        new Object[] { clientApp.getApplicationName(), serverType, wsconfList.size() });
            }
        }
        return wsconfList;
    }

    /**
     * Creates the list of {@link WSConfig} for {@link com.genesyslab.platform.clusterprotocol.ClusterProtocol
     * Cluster Protocol}.<br/>
     * It transparently supports both Cluster and legacy primary/backup WarmStanby configurations.
     * If client application is connected to a cluster, then WSConfig objects will be created
     * from cluster application connections. If no servers of the specified type found in cluster connections,
     * helper will try to resolve them directly from client application connections.<br/>
     * Only servers of the specified type will be included in the result {@link WSConfig} list.
     *
     * @param clientApp
     *            the client application that has connection to a cluster of servers
     *            or to a single server
     * @param clusterConn
     *            connection to the specific cluster
     * @param serverType
     *            type of the server(s) to create connection configuration to
     * @return collection of WSConfig objects
     * @throws ConfigurationException
     */
    public static List<WSConfig> createClusterProtocolEndpoints(
            final IGApplicationConfiguration clientApp,
            final IGAppConnConfiguration clusterConn,
            final CfgAppType serverType)
                    throws ConfigurationException {
        checkArgsNotNull(clientApp, "clientApp");
        checkArgsNotNull(clusterConn, "clusterConn");
        checkArgsNotNull(serverType, "serverType");

        List<WSConfig> wsconfList =
                parseClusterConfiguration(clientApp, clusterConn, serverType);
        if (wsconfList.size() == 0) {
            final Collection<IGAppConnConfiguration> clientConnections = clientApp.getAppServers();
            if (clientConnections != null && clientConnections.size() > 0) {
                wsconfList = parseSimpleConfiguration(clientApp, clientConnections, serverType);
                if (log.isDebug()) {
                    log.debugFormat("Client application ''{0}'' connections to ''{1}'' count: {2}",
                            new Object[] { clientApp.getApplicationName(), serverType, wsconfList.size() });
                }
            } else {
                log.debugFormat("Client application ''{0}'' has no connections", clientApp.getApplicationName());
            }
        }
        return wsconfList;
    }


    /**
     * Creates the list of {@link WSConfig} for {@link com.genesyslab.platform.clusterprotocol.ClusterProtocol
     * Cluster Protocol}.<br/>
     * If client application is connected to a cluster, then WSConfig objects will be created
     * from cluster nodes of given <code>serverType</code>, which have references (connections)
     * to this application cluster application object.<br/>
     * Only servers of the specified type will be included in the result {@link WSConfig} list.
     * <p/>
     * <i><b>Note</b>: this type of application cluster is specific to <code>Genesys WCC</code> based
     * applications like <code>UCS 9.0</code>, <code>GWE</code>, etc.</i>
     *
     * @param confService
     *            the Configuration Server API service for reading of cluster nodes
     * @param clientApp
     *            the client application that has connection to a cluster of servers
     *            or to a single server
     * @param serverType
     *            type of the server(s) to create connection configuration to
     * @return collection of WSConfig objects
     * @throws ConfigurationException
     */
    public static List<WSConfig> createRefClusterProtocolEndpoints(
            final IConfService confService,
            final IGApplicationConfiguration clientApp,
            final CfgAppType serverType)
                    throws ConfigurationException {
        checkArgsNotNull(confService, "confService");
        checkArgsNotNull(clientApp, "clientApp");
        checkArgsNotNull(serverType, "serverType");

        final List<IGAppConnConfiguration> servers = clientApp.getAppServers();
        if (servers != null && !servers.isEmpty()) {
            for (final IGAppConnConfiguration conn: servers) {
                if (conn != null) {
                    final IGApplicationConfiguration srv = conn.getTargetServerConfiguration();
                    if (srv != null && CfgAppType.CFGApplicationCluster.equals(srv.getApplicationType())) {
                        final List<WSConfig> nodes = createRefClusterProtocolEndpoints(
                                confService, clientApp, conn, serverType);
                        if (nodes != null && !nodes.isEmpty()) {
                            return nodes;
                        }
                    }
                }
            }
        }

        return createEmptyList();
    }


    /**
     * Creates the list of {@link WSConfig} for {@link com.genesyslab.platform.clusterprotocol.ClusterProtocol
     * Cluster Protocol}.<br/>
     * If client application is connected to a cluster, then WSConfig objects will be created
     * from cluster nodes of given <code>serverType</code>, which have references (connections)
     * to this application cluster application object.<br/>
     * Only servers of the specified type will be included in the result {@link WSConfig} list.
     * <p/>
     * <i><b>Note</b>: this type of application cluster is specific to <code>Genesys WCC</code> based
     * applications like <code>UCS 9.0</code>, <code>GWE</code>, etc.</i>
     *
     * @param confService
     *            the Configuration Server API service for reading of cluster nodes
     * @param clientApp
     *            the client application that has connection to a cluster of servers
     *            or to a single server
     * @param clusterConn
     *            connection to the specific cluster
     * @param serverType
     *            type of the server(s) to create connection configuration to
     * @return collection of WSConfig objects
     * @throws ConfigurationException
     */
    public static List<WSConfig> createRefClusterProtocolEndpoints(
            final IConfService confService,
            final IGApplicationConfiguration clientApp,
            final IGAppConnConfiguration clusterConn,
            final CfgAppType serverType)
                    throws ConfigurationException {
        checkArgsNotNull(confService, "confService");
        checkArgsNotNull(clientApp, "clientApp");
        checkArgsNotNull(serverType, "serverType");

        checkArgsNotNull(clusterConn, "clusterConn");
        final IGApplicationConfiguration clusterApp = clusterConn.getTargetServerConfiguration();
        checkArgsNotNull(clusterApp, "clusterApp");
        final Integer clusterDbid = clusterApp.getDbid();
        checkArgsNotNull(clusterDbid, "clusterApp.DBID");

        final List<WSConfig> ret = createEmptyList();
        final Collection<CfgApplication> servers = readRefApplications(
                confService, clusterDbid, serverType);
        if (servers != null && !servers.isEmpty()) {
            for (final CfgApplication server : servers) {
                final GCOMApplicationConfiguration srvConfig =
                        new GCOMApplicationConfiguration(server, false);
                try {
                    final WSConfig conf = ClientConfigurationHelper.createWarmStandbyConfigEx(
                            server.getName(), clientApp, clusterConn, srvConfig);
                    if (conf != null) {
                        ret.add(conf);
                    }
                } catch (final ConfigurationException ex) {
                    log.error("Failed to create WSConfig to cluster node " + server.getName(), ex);
                }
            }
        }

        return ret;
    }


    private static List<WSConfig> parseClusterConfiguration(
            final IGApplicationConfiguration clientApp,
            final Collection<IGAppConnConfiguration> clientConnections,
            final CfgAppType appType)
                    throws ConfigurationException {
        for (final IGAppConnConfiguration conn : clientConnections) {
            final List<WSConfig> item = parseClusterConfiguration(
                    clientApp, conn, appType);
            if (item != null && !item.isEmpty()) {
                return item;
            }
        }

        return createEmptyList();
    }

    private static List<WSConfig> parseClusterConfiguration(
            final IGApplicationConfiguration clientApp,
            final IGAppConnConfiguration clientConnection,
            final CfgAppType appType)
                    throws ConfigurationException {
        final IGApplicationConfiguration clusterApp = getTargetApp(
                clientConnection, CfgAppType.CFGApplicationCluster, CfgObjectState.CFGEnabled);
        if (clusterApp != null) {
            final String clusterName = clusterApp.getApplicationName();
            log.debugFormat("Read cluster application ''{0}'' connections", clusterName);

            final List<IGAppConnConfiguration> clusterNodesConnList = clusterApp.getAppServers();
            if (clusterNodesConnList != null && !clusterNodesConnList.isEmpty()) {
                final List<IGAppConnConfiguration> sharedOptsConnList = new LinkedList<IGAppConnConfiguration>();
                for (final IGAppConnConfiguration targetNodeConn : clusterNodesConnList) {
                    if (targetNodeConn != null) {
                        sharedOptsConnList.add(applySharedConnOptions(clientConnection, targetNodeConn));
                    }
                }

                final List<WSConfig> wsconfList = parseSimpleConfiguration(clientApp, sharedOptsConnList, appType);
                if (log.isDebug()) {
                    log.debugFormat("Cluster application ''{0}'' connections to ''{1}'' count: {2}",
                            new Object[] { clusterName, appType, wsconfList.size() });
                }
                return wsconfList;
            } else {
                log.debugFormat("Cluster application ''{0}'' has no connections", clusterName);
            }
        }

        return createEmptyList();
    }

    private static List<WSConfig> parseSimpleConfiguration(
            final IGApplicationConfiguration clientApp,
            final Collection<IGAppConnConfiguration> targetConnList, 
            final CfgAppType appType)
                    throws ConfigurationException {
        final List<WSConfig> wsconfList = createEmptyList();
        for (final IGAppConnConfiguration targetConn : targetConnList) {
            final IGApplicationConfiguration targetApp =
                    getTargetApp(targetConn, appType, CfgObjectState.CFGEnabled);
            if (targetApp != null) {
                final String configName = targetApp.getApplicationName();
                final WSConfig wsconf = ClientConfigurationHelper.createWarmStandbyConfigEx(
                        configName, clientApp, targetConn, targetApp);
                wsconfList.add(wsconf);
            }
        }
        return wsconfList;
    }

    private static IGAppConnConfiguration applySharedConnOptions(
            final IGAppConnConfiguration sharedConn,
            final IGAppConnConfiguration targetConn) {		
        final GAppConnConfiguration resultConnection = new GAppConnConfiguration(targetConn);
        if (resultConnection.getConnProtocol() == null && sharedConn.getConnProtocol() != null) {
            resultConnection.setConnProtocol(sharedConn.getConnProtocol());
            resultConnection.setTimeoutLocal(sharedConn.getTimeoutLocal());
            resultConnection.setTimeoutRemote(sharedConn.getTimeoutRemote());
            resultConnection.setTraceMode(sharedConn.getTraceMode());
        }
        if (resultConnection.getAppParams() == null && sharedConn.getAppParams() != null) {
            resultConnection.setAppParams(sharedConn.getAppParams());
        }
        else if (resultConnection.getAppParams() != null && sharedConn.getAppParams() != null) {
            final Map<String, String> map = new HashMap<String, String>();
            putParams(map, sharedConn.getAppParams());
            putParams(map, resultConnection.getAppParams());
            resultConnection.setAppParams(printParams(map));
        }

        if (resultConnection.getTransportParams() == null && sharedConn.getTransportParams() != null) {
            resultConnection.setTransportParams(sharedConn.getTransportParams());
        }
        else if (resultConnection.getTransportParams() != null && sharedConn.getTransportParams() != null) {
            final Map<String, String> map = new HashMap<String, String>();
            putParams(map, sharedConn.getTransportParams());
            putParams(map, resultConnection.getTransportParams());
            resultConnection.setTransportParams(printParams(map));
        }

        return resultConnection;
    }

    private static void putParams(final Map<String, String> params, final String value) {
        if (value != null && value.length() > 0) {
            final String[] pairs = value.split(";");
            for (final String pair : pairs) {
                final String[] kv = pair.split("=");
                if (kv.length == 2) {
                    params.put(kv[0].trim(), kv[1].trim());
                } else {
                    log.warn("Unexpected configuration option: "+ value);
                }
            }
        }
    }

    private static String printParams(final Map<String, String> params) {
        if (params.size() == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<String, String> p = it.next();
            if (p.getKey() != null && p.getValue() != null) {
                sb.append(p.getKey()).append("=").append(p.getValue());
            }
            if (it.hasNext()) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    private static IGApplicationConfiguration getTargetApp(
            final IGAppConnConfiguration conn, 
            final CfgAppType appType,
            final CfgObjectState appState) {
        if (conn == null) {
            return null;
        }
        final IGApplicationConfiguration targetApp = conn.getTargetServerConfiguration();
        if (targetApp == null) {
            log.debugFormat("Target application of existing connection ''{0}'' is null", conn);
            return null;
        }
        if (!appType.equals(targetApp.getApplicationType())) {
            return null;
        }
        if (!appState.equals(targetApp.getObjectState())) {
            if (log.isDebug()) {
                log.debugFormat("Skip application ''{0}'' with state ''{1}''",
                        new Object[] { targetApp.getApplicationName(), targetApp.getObjectState() });
            }
            return null;
        }
        return targetApp;
    }


    private static List<CfgApplication> readRefApplications(
            final IConfService confService,
            final int          clusterAppDbid,
            final CfgAppType   serverType)
                    throws ConfigurationException {
        final IConfCache cache = confService.getCache();

        // TODO evaluate cached cluster:
        boolean readCache = (cache != null) && false;

        List<CfgApplication> ret = null;
        if (readCache) {
            final Iterable<CfgApplication> objs = cache.retrieveMultiple(CfgApplication.class);
            if (objs != null) {
                ret = new ArrayList<CfgApplication>();
                for (final CfgApplication app : objs) {
                    if (serverType.equals(app.getType())
                            && CfgObjectState.CFGEnabled.equals(app.getState())) {
                        final Collection<CfgConnInfo> conns = app.getAppServers();
                        if (conns != null && !conns.isEmpty()) {
                            for (final CfgConnInfo conn : conns) {
                                if (conn.getAppServerDBID().intValue() == clusterAppDbid) {
                                    ret.add(app);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            final CfgApplicationQuery query = new CfgApplicationQuery();
            query.setServerDbid(clusterAppDbid);
            query.setAppType(serverType);
            query.setState(CfgObjectState.CFGEnabled);

            try {
                final Collection<CfgApplication> servers = confService.retrieveMultipleObjects(
                        CfgApplication.class, query);
                if (servers != null) {
                    ret = new ArrayList<CfgApplication>();
                    ret.addAll(servers);
                }
            } catch (final ConfigException ex) {
                throw new ConfigurationException("Failed to read configuration information", ex);
            } catch (final InterruptedException ex) {
                log.error("Configuration reading is interrupted", ex);
                Thread.currentThread().interrupt();
            }
        }
        return ret;
    }


    private static void checkArgsNotNull(final Object arg, final String name) {
        if (arg == null) {
            throw new NullPointerException("'" + name + "' is null");
        }
    }


    private static List<WSConfig> createEmptyList() {
        return new LinkedList<WSConfig>();
    }
}
