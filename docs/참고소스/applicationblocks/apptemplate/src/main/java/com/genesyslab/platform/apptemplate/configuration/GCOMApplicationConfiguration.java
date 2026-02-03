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

import com.genesyslab.platform.apptemplate.util.EnumFactory;

import com.genesyslab.platform.applicationblocks.com.objects.CfgHost;
import com.genesyslab.platform.applicationblocks.com.objects.CfgServer;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTenant;
import com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;

import com.genesyslab.platform.configuration.protocol.types.CfgAppType;

import java.util.Collection;
import java.util.LinkedList;


/**
 * This class represents base Genesys CME application options,
 * which can be loaded from initialized COM AB {@link CfgApplication},
 * its' related objects and structures.
 *
 * <p/>It extends base class {@link GApplicationConfiguration} with only one feature -
 * constructor, which can initialize the application configuration properties with
 * information retrieved from COM AB application object.
 *
 * <p/>COM AB direct usage example: <example><code><pre>
 *     String appName = "&lt;app-name&gt;";
 *     CfgApplication cfgApplication = confService.retrieveObject(
 *             CfgApplication.class, new CfgApplicationQuery(appName));
 *
 *     GCOMApplicationConfiguration appConfiguration =
 *             new GCOMApplicationConfiguration(cfgApplication);
 * </pre></code></example>
 */
public class GCOMApplicationConfiguration
        extends GApplicationConfiguration {

    private static final long serialVersionUID = -9136678820075681720L;

    private transient CfgApplication cfgApplication;


    /**
     * Loads application configuration properties from given COM AB CfgApplication
     * object including configuration of connected CfgApplication's and CfgHost's.
     *
     * @param cfgApplication application properties represented as COM AB application object
     * @throws ConfigurationException in case of configuration problems
     */
    public GCOMApplicationConfiguration(
            final CfgApplication cfgApplication)
                throws ConfigurationException {
        this(cfgApplication, true, true);
    }

    /**
     * Loads application configuration properties from given COM AB CfgApplication
     * object including configuration of connected CfgApplication's and CfgHost's.
     *
     * @param cfgApplication application properties represented as COM AB application object
     * @param readConnections flag indicating needs to read connected applications configuration
     * @throws ConfigurationException in case of configuration problems
     */
    public GCOMApplicationConfiguration(
            final CfgApplication cfgApplication,
            final boolean readConnections)
                throws ConfigurationException {
        this(cfgApplication, readConnections, readConnections);
    }

    /**
     * Loads application configuration properties from given COM AB CfgApplication
     * object including configuration of connected CfgApplication's and CfgHost's.
     *
     * @param cfgApplication application properties represented as COM AB application object
     * @param readConnections flag indicating needs to read connected applications configuration
     * @param readClusterConnections flag indicating needs to read connected applications clusters configuration
     * @throws ConfigurationException in case of configuration problems
     */
    public GCOMApplicationConfiguration(
            final CfgApplication cfgApplication,
            final boolean readConnections,
            final boolean readClusterConnections)
                throws ConfigurationException {
        this(cfgApplication, readConnections, readClusterConnections, false);
    }

    /**
     * Loads application configuration properties from given COM AB CfgApplication
     * object including configuration of connected CfgApplication's and CfgHost's.
     *
     * @param cfgApplication application properties represented as COM AB application object
     * @param readConnections flag indicating needs to read connected applications configuration
     * @param readClusterConnections flag indicating needs to read connected applications clusters configuration
     * @param readTenantsInfo flag indicating needs to read applications tenants information
     * @throws ConfigurationException in case of configuration problems
     */
    public GCOMApplicationConfiguration(
            final CfgApplication cfgApplication,
            final boolean readConnections,
            final boolean readClusterConnections,
            final boolean readTenantsInfo)
                throws ConfigurationException {
        if (cfgApplication == null) {
            throw new NullPointerException("CfgApplication");
        }

        setApplicationName(cfgApplication.getName());
        setApplicationType(cfgApplication.getType());
        setDbid(cfgApplication.getDBID());
        setObjectState(cfgApplication.getState());
        setVersion(cfgApplication.getVersion());

        setIsServer(EnumFactory.CfgFlag2Boolean(cfgApplication.getIsServer()));
        if (isServer() != null && isServer()) {
            setIsPrimary(EnumFactory.CfgFlag2Boolean(cfgApplication.getIsPrimary()));
            CfgServer srvInfo = cfgApplication.getServerInfo();
            if (srvInfo != null) {
                setServerInfo(new GCOMServerInfo(srvInfo));
            }
            setRedundancyType(cfgApplication.getRedundancyType());

            Collection<CfgPortInfo> cfgPortInfos = cfgApplication.getPortInfos();
            if (cfgPortInfos != null) {
                LinkedList<IGPortInfo> ports = new LinkedList<IGPortInfo>();
                for (CfgPortInfo cfgPortInfo : cfgPortInfos) {
                    ports.add(new GCOMPortInfo(cfgPortInfo));
                }
                setPortInfos(ports);
            }
        }

        setOptionsClone(cfgApplication.getOptions());
        setUserPropertiesClone(cfgApplication.getUserProperties());
        setFlexiblePropertiesClone(cfgApplication.getFlexibleProperties());

        if (readTenantsInfo) {
            final Collection<CfgTenant> tenants = cfgApplication.getTenants();
            if (tenants != null) {
                final LinkedList<IGTenantInfo> tenantsInfo =
                        new LinkedList<IGTenantInfo>();
                for (final CfgTenant tenant: tenants) {
                    tenantsInfo.add(new GCOMTenantInfo(tenant));
                }
                setTenants(tenantsInfo);
            }
        }

        if (readConnections) {
            final Collection<CfgConnInfo> connInfos = cfgApplication.getAppServers();
            if (connInfos != null) {
                final LinkedList<IGAppConnConfiguration> connections =
                        new LinkedList<IGAppConnConfiguration>();
                final boolean thisIsCluster = CfgAppType.CFGApplicationCluster.equals(cfgApplication.getType());
                for (final CfgConnInfo connInfo: connInfos) {
                    connections.add(new GCOMAppConnConfiguration(connInfo,
                            readClusterConnections && !thisIsCluster,
                            readTenantsInfo));
                }
                setAppServers(connections);
            }
        }

        this.cfgApplication = cfgApplication;
    }

    /**
     * Copying constructor.<br/>
     * <b><i>Note:</i></b> It creates new <code>GCOMApplicationConfiguration</code> instance,
     * but does not clone referred structures like {@link #getCfgApplication()},
     * {@link #getAppServers()}, {@link #getPortInfos()}, {@link #getOptions()}, etc.
     *
     * @param conf original configuration to copy configuration values from
     */
    public GCOMApplicationConfiguration(
            final GCOMApplicationConfiguration conf) {
        super(conf);
        cfgApplication = conf.cfgApplication;
    }


    /**
     * Returns reference to the original CfgApplication object
     * as source of initial configuration properties values.
     *
     * @return reference to COM AB application object
     */
    public CfgApplication getCfgApplication() {
        return cfgApplication;
    }

    /**
     * This method is used from {@link #toString()} to build
     * string representation of the internal content (configuration properties names and values).
     *
     * @return string representation of the configuration content
     * @see #toString()
     */
    @Override
    protected String contentToString() {
        StringBuilder sb = new StringBuilder(super.contentToString());
        sb.append("      CfgApplication: ").append(cfgApplication != null).append("\n");
        return sb.toString();
    }

    @Override
    public GCOMApplicationConfiguration clone() {
        return (GCOMApplicationConfiguration) super.clone();
    }


    /**
     * This class is an extension of base {@link GServerInfo} structure
     * with logic related to COM AB information extraction as detached configuration data.
     *
     * <p/>It is designed for usage by {@link GCOMApplicationConfiguration} container.
     *
     * @see GServerInfo
     * @see CfgServer
     */
    public static class GCOMServerInfo extends GServerInfo {

        private static final long serialVersionUID = 8664869535647123376L;

        private transient CfgServer serverInfo;


        /**
         * Structure constructor for extraction of configuration data
         * from Genesys Configuration Server objects and structures
         * represented with COM AB.
         *
         * @param serverInfo COM AB structure with initial information
         * @throws ConfigurationException
         */
        public GCOMServerInfo(final CfgServer serverInfo)
                    throws ConfigurationException {
            if (serverInfo == null) {
                throw new ConfigurationException("CfgServerInfo is null");
            }
            CfgHost cfgHost;
            try {
                cfgHost = serverInfo.getHost();
            } catch (Exception e) {
                throw new ConfigurationException(
                        "Exception retrieving referred CfgHost", e);
            }
            if (cfgHost != null) {
                setHost(new GCOMHost(cfgHost));
            }
            setPort(serverInfo.getPort());
            setTimeout(serverInfo.getTimeout());
            setAttempts(serverInfo.getAttempts());
            CfgApplication backupApp;
            try {
                backupApp = serverInfo.getBackupServer();
            } catch (Exception e) {
                throw new ConfigurationException(
                        "Exception retrieving referred backup CfgApplication", e);
            }
            if (backupApp != null) {
                setBackup(new GCOMApplicationConfiguration(backupApp, false));
            }

            this.serverInfo = serverInfo;
        }

        /**
         * Copying constructor.<br/>
         * <b><i>Note:</i></b> It creates new <code>GCOMServerInfo</code> instance,
         * but does not clone referred structures {@link #getHost()} and {@link #getBackup()}.
         *
         * @param conf original configuration to copy configuration values from
         */
        public GCOMServerInfo(final GCOMServerInfo conf) {
            super(conf);
            serverInfo = conf.serverInfo;
        }

        /**
         * Returns reference to the original COM AB configuration structure.
         *
         * @return reference to COM AB original structure
         */
        public CfgServer getCfgServerInfo() {
            return serverInfo;
        }

        /**
         * This method is used from {@link #toString()} to build
         * string representation of the internal content (configuration properties names and values).
         *
         * @return string representation of the configuration content
         * @see #toString()
         */
        @Override
        protected String contentToString() {
            StringBuilder sb = new StringBuilder(super.contentToString());
            sb.append("      CfgServerInfo: ").append(serverInfo != null).append("\n");
            return sb.toString();
        }

        @Override
        public GCOMServerInfo clone() {
            return (GCOMServerInfo) super.clone();
        }
    }


    /**
     * This class is an extension of base {@link GHost} structure
     * with logic related to COM AB information extraction as detached configuration data.
     *
     * <p/>It is designed for usage by {@link GCOMApplicationConfiguration} container.
     *
     * @see GHost
     * @see CfgHost
     */
    public static class GCOMHost extends GHost {

        private static final long serialVersionUID = 52331064652452086L;

        private transient CfgHost cfgHost;


        /**
         * Structure constructor for extraction of configuration data
         * from Genesys Configuration Server objects and structures
         * represented with COM AB.
         *
         * @param cfgHost COM AB structure with initial information
         * @throws ConfigurationException
         */
        public GCOMHost(final CfgHost cfgHost) throws ConfigurationException {
            if (cfgHost == null) {
                throw new ConfigurationException("CfgHost is null");
            }
            setName(cfgHost.getName());
            setDbid(cfgHost.getDBID());
            setIPAddress(cfgHost.getIPaddress());
            setLCAPort(cfgHost.getLCAPort());
            setObjectState(cfgHost.getState());
            setUserProperties(cfgHost.getUserProperties());

            this.cfgHost = cfgHost;
        }

        /**
         * Copying constructor.<br/>
         * <b><i>Note:</i></b> It creates new <code>GCOMHost</code> instance,
         * but does not clone referred structure {@link #getUserProperties()}.
         *
         * @param conf original configuration to copy configuration values from
         */
        public GCOMHost(final GCOMHost conf) {
            super(conf);
            cfgHost = conf.cfgHost;
        }

        /**
         * Returns reference to the original COM AB configuration object.
         *
         * @return reference to the original object
         */
        public CfgHost getCfgHost() {
            return cfgHost;
        }

        /**
         * This method is used from {@link #toString()} to build
         * string representation of the internal content (configuration properties names and values).
         *
         * @return string representation of the configuration content
         * @see #toString()
         */
        @Override
        protected String contentToString() {
            StringBuilder sb = new StringBuilder(super.contentToString());
            sb.append("      CfgHost: ").append(cfgHost != null).append("\n");
            return sb.toString();
        }

        @Override
        public GCOMHost clone() {
            return (GCOMHost) super.clone();
        }
    }


    /**
     * This class is an extension of base {@link GPortInfo} structure
     * with logic related to COM AB information extraction as detached configuration data.
     *
     * <p/>It is designed for usage by {@link GCOMApplicationConfiguration} container.
     *
     * @see GPortInfo
     * @see CfgPortInfo
     */
    public static class GCOMPortInfo extends GPortInfo {

        private static final long serialVersionUID = -1580193218083985757L;

        private transient CfgPortInfo portInfo;


        /**
         * Structure constructor for extraction of configuration data
         * from Genesys Configuration Server objects and structures
         * represented with COM AB.
         *
         * @param portInfo COM AB structure with initial information
         * @throws ConfigurationException
         */
        public GCOMPortInfo(final CfgPortInfo portInfo)
                    throws ConfigurationException {
            if (portInfo == null) {
                throw new ConfigurationException("CfgPortInfo");
            }
            setId(portInfo.getId());
            setPort(Integer.parseInt(portInfo.getPort()));
            setConnProtocol(portInfo.getConnProtocol());
            setTransportParams(portInfo.getTransportParams());
            setAppParams(portInfo.getAppParams());
            setDescription(portInfo.getDescription());

            this.portInfo = portInfo;
        }

        /**
         * Copying constructor.
         *
         * @param info original configuration to copy configuration values from
         */
        public GCOMPortInfo(final GCOMPortInfo info) {
            super(info);
            portInfo = info.portInfo;
        }

        /**
         * Returns reference to the original COM AB configuration structure.
         *
         * @return reference to the original structure
         */
        public CfgPortInfo getCfgPortInfo() {
            return portInfo;
        }

        /**
         * This method is used from {@link #toString()} to build
         * string representation of the internal content (configuration properties names and values).
         *
         * @return string representation of the configuration content
         * @see #toString()
         */
        @Override
        protected String contentToString() {
            StringBuilder sb = new StringBuilder(super.contentToString());
            sb.append("      CfgPortInfo: ").append(portInfo != null).append("\n");
            return sb.toString();
        }

        @Override
        public GCOMPortInfo clone() {
            return (GCOMPortInfo) super.clone();
        }
    }


    /**
     * This class is an extension of base {@link GAppConnConfiguration} structure
     * with logic related to COM AB information extraction as detached configuration data.
     *
     * <p/>It is designed for usage by {@link GCOMApplicationConfiguration} container.
     *
     * @see GAppConnConfiguration
     * @see CfgConnInfo
     */
    public static class GCOMAppConnConfiguration extends GAppConnConfiguration {

        private static final long serialVersionUID = -7897433900937010292L;

        private transient CfgConnInfo connInfo;

        /**
         * Structure constructor for extraction of configuration data
         * from Genesys Configuration Server objects and structures
         * represented with COM AB.
         *
         * @param connInfo COM AB structure with initial information
         * @throws ConfigurationException
         */
        public GCOMAppConnConfiguration(final CfgConnInfo connInfo)
                    throws ConfigurationException {
            this(connInfo, true, false);
        }

        /**
         * Structure constructor for extraction of configuration data
         * from Genesys Configuration Server objects and structures
         * represented with COM AB.
         *
         * @param connInfo COM AB structure with initial information
         * @throws ConfigurationException
         */
        public GCOMAppConnConfiguration(
                final CfgConnInfo connInfo,
                final boolean readClusterConnections)
                    throws ConfigurationException {
            this(connInfo, readClusterConnections, false);
        }

        /**
         * Structure constructor for extraction of configuration data
         * from Genesys Configuration Server objects and structures
         * represented with COM AB.
         *
         * @param connInfo COM AB structure with initial information
         * @throws ConfigurationException
         */
        public GCOMAppConnConfiguration(
                final CfgConnInfo connInfo,
                final boolean readClusterConnections,
                final boolean readTenantsInfo)
                    throws ConfigurationException {
            if (connInfo == null) {
                throw new ConfigurationException("CfgConnInfo is null");
            }
            setPortId(connInfo.getId());
            setConnProtocol(connInfo.getConnProtocol());
            setTimeoutLocal(connInfo.getTimoutLocal());
            setTimeoutRemote(connInfo.getTimoutRemote());
            setTraceMode(connInfo.getMode());
            setTransportParams(connInfo.getTransportParams());
            setAppParams(connInfo.getAppParams());
            setProxyParams(connInfo.getProxyParams());
            setDescription(connInfo.getDescription());
            CfgApplication connApp;
            try {
                connApp = connInfo.getAppServer();
            } catch (Exception e) {
                throw new ConfigurationException(
                        "Exception while retrieving connected CfgApplication", e);
            }
            if (connApp == null) {
                throw new ConfigurationException(
                        "Failed to retrieve connected CfgApplication");
            }
            setTargetServerConfiguration(
                    new GCOMApplicationConfiguration(connApp,
                            readClusterConnections
                                    && CfgAppType.CFGApplicationCluster.equals(connApp.getType()),
                            false,
                            readTenantsInfo));

            this.connInfo = connInfo;
        }

        /**
         * Copying constructor.<br/>
         * <b><i>Note:</i></b> It creates new <code>GCOMAppConnConfiguration</code> instance,
         * but does not clone referred structure {@link #getTargetServerConfiguration()}.
         *
         * @param conf original configuration to copy configuration values from
         */
        public GCOMAppConnConfiguration(
                final GCOMAppConnConfiguration conf) {
            super(conf);
            connInfo = conf.connInfo;
        }

        /**
         * Returns reference to the original COM AB configuration structure.
         *
         * @return reference to the original structure
         */
        public CfgConnInfo getCfgConnInfo() {
            return connInfo;
        }

        /**
         * This method is used from {@link #toString()} to build
         * string representation of the internal content (configuration properties names and values).
         *
         * @return string representation of the configuration content
         * @see #toString()
         */
        @Override
        protected String contentToString() {
            StringBuilder sb = new StringBuilder(super.contentToString());
            sb.append("      CfgConnInfo: ").append(connInfo != null).append("\n");
            return sb.toString();
        }

        @Override
        public GCOMAppConnConfiguration clone() {
            return (GCOMAppConnConfiguration) super.clone();
        }
    }


    /**
     * This class is an extension of base {@link GTenantInfo} structure
     * with logic related to COM AB information extraction as detached configuration data.
     *
     * <p/>It is designed for usage by {@link GCOMApplicationConfiguration} container.
     *
     * @see GTenantInfo
     * @see CfgTenant
     */
    public static class GCOMTenantInfo extends GTenantInfo {

        private static final long serialVersionUID = -8268483254159847465L;

        private transient CfgTenant cfgTenant;


        /**
         * Structure constructor for extraction of configuration data
         * from Genesys Configuration Server objects and structures
         * represented with COM AB.
         *
         * @param cfgTenant COM AB structure with initial information
         * @throws ConfigurationException
         */
        public GCOMTenantInfo(final CfgTenant cfgTenant) throws ConfigurationException {
            if (cfgTenant == null) {
                throw new ConfigurationException("CfgTenant is null");
            }
            setName(cfgTenant.getName());
            setDbid(cfgTenant.getDBID());
            setObjectState(cfgTenant.getState());
            setIsServiceProvider(EnumFactory.CfgFlag2Boolean(cfgTenant.getIsServiceProvider()));
            setPassword(cfgTenant.getPassword());

            this.cfgTenant = cfgTenant;
        }

        /**
         * Copying constructor.
         *
         * @param conf original configuration to copy configuration values from
         */
        public GCOMTenantInfo(final GCOMTenantInfo conf) {
            super(conf);
            cfgTenant = conf.cfgTenant;
        }

        /**
         * Returns reference to the original COM AB configuration object.
         *
         * @return reference to the original object
         */
        public CfgTenant getCfgTenant() {
            return cfgTenant;
        }

        /**
         * This method is used from {@link #toString()} to build
         * string representation of the internal content (configuration properties names and values).
         *
         * @return string representation of the configuration content
         * @see #toString()
         */
        @Override
        protected String contentToString() {
            StringBuilder sb = new StringBuilder(super.contentToString());
            sb.append("      CfgTenant: ").append(cfgTenant != null).append("\n");
            return sb.toString();
        }

        @Override
        public GCOMTenantInfo clone() {
            return (GCOMTenantInfo) super.clone();
        }
    }
}
