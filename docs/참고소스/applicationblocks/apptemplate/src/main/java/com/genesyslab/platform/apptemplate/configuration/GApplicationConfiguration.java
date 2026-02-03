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
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGAppConnConfiguration;

import com.genesyslab.platform.apptemplate.util.ConfigurationUtil;

import com.genesyslab.platform.configuration.protocol.types.CfgHAType;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.types.CfgTraceMode;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;

import com.genesyslab.platform.commons.util.CompareUtils;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.util.List;
import java.util.LinkedList;

import java.io.Serializable;


/**
 * <p>This class represents base Genesys CME application options,
 * which can be loaded from configuration server (with COM AB),
 * or initialized by some other way like custom user code or
 * any beans management mechanisms/frameworks.</p>
 *
 * <p>It provides detached properties from COM AB
 * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgApplication CfgApplication}
 * and {@link com.genesyslab.platform.applicationblocks.com.objects.CfgHost CfgHost} objects
 * without COM AB specific internal XML containers and can be created and filled with or without
 * {@link com.genesyslab.platform.applicationblocks.com.IConfService IConfService} usage.</p>
 *
 * @see GCOMApplicationConfiguration
 */
public class GApplicationConfiguration
        implements IGApplicationConfiguration,
                Cloneable, Serializable {

    private static final long serialVersionUID = 2551492682459458485L;

    private String applicationName;
    private CfgAppType applicationType;
    private Integer dbid;
    private CfgObjectState objectState;
    private String version;
    private Boolean isServer;
    private Boolean isPrimary;
    private IGServerInfo serverInfo;
    private CfgHAType redundancyType;
    private List<IGPortInfo> portInfos;
    private KeyValueCollection options;
    private KeyValueCollection userProperties;
    private KeyValueCollection flexibleProperties;
    private List<IGAppConnConfiguration> appServers;
    private List<IGTenantInfo> tenants;


    /**
     * Default empty constructor.
     * Creates uninitialized configuration object.
     */
    public GApplicationConfiguration() {
    }

    /**
     * Coping constructor.<br/>
     * <i><b>Note:</b></i> it does not clone referred structures {@link #getServerInfo() ServerInfo},
     * {@link #getAppServers() AppServers}, and {@link #getTenants() Tenants}.
     *
     * @param appConfig original configuration
     */
    public GApplicationConfiguration(
            final IGApplicationConfiguration appConfig) {
        if (appConfig == null) {
            throw new NullPointerException("appConfig");
        }

        applicationName = appConfig.getApplicationName();
        applicationType = appConfig.getApplicationType();
        dbid = appConfig.getDbid();
        objectState = appConfig.getObjectState();
        version = appConfig.getVersion();
        isServer = appConfig.isServer();
        isPrimary = appConfig.isPrimary();
        serverInfo = appConfig.getServerInfo();
        redundancyType = appConfig.getRedundancyType();
        portInfos = appConfig.getPortInfos();
        setOptionsClone(appConfig.getOptions());
        setUserPropertiesClone(appConfig.getUserProperties());
        setFlexiblePropertiesClone(appConfig.getFlexibleProperties());
        appServers = appConfig.getAppServers();
        tenants = appConfig.getTenants();
    }


    /**
     * Returns the application name.<br/>
     * It represents the correspondent name in Genesys Configuration framework.
     *
     * @return the application name
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getName()
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Sets the application name.<br/>
     * It represents the correspondent name in Genesys Configuration framework.
     *
     * @param appName application name
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getName()
     */
    public void setApplicationName(final String appName) {
        applicationName = appName;
    }

    /**
     * Returns type of the application in terms of Genesys Configuration framework.
     *
     * @return the application type
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getType()
     */
    public CfgAppType getApplicationType() {
        return applicationType;
    }

    /**
     * Sets type of the application in terms of Genesys Configuration framework.
     *
     * @param appType application type
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getType()
     */
    public void setApplicationType(final CfgAppType appType) {
        applicationType = appType;
    }

    /**
     * Returns unique application object identifier in context of Genesys Configuration Database.
     *
     * @return the application DBID
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getDBID()
     */
    public Integer getDbid() {
        return dbid;
    }

    /**
     * Sets unique application object identifier in terms of Genesys Configuration Database.
     *
     * @param dbid application DBID
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getDBID()
     */
    public void setDbid(final Integer dbid) {
        this.dbid = dbid;
    }

    /**
     * Returns actual object state in the Genesys Configuration Database.
     *
     * @return actual object state
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getState()
     */
    public CfgObjectState getObjectState() {
        return objectState;
    }

    /**
     * Sets actual object state in terms of Genesys Configuration Database.
     *
     * @param objectState actual object state
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getState()
     */
    public void setObjectState(final CfgObjectState objectState) {
        this.objectState = objectState;
    }

    /**
     * Returns indicator of whether this application can be a server to some other applications.
     * This value depends on the application type - {@link #getApplicationType()}.
     *
     * @return property value or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getIsServer()
     */
    public Boolean isServer() {
        return isServer;
    }

    /**
     * Sets indicator of whether this application can be a server to some other applications.
     *
     * @param isServer the property value
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getIsServer()
     */
    public void setIsServer(final Boolean isServer) {
        this.isServer = isServer;
    }

    /**
     * Returns value meaning role of application within HA/redundancy group.
     *
     * @return property value or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getIsPrimary()
     */
    public Boolean isPrimary() {
        return isPrimary;
    }

    /**
     * Sets value meaning role of application within HA/redundancy group.
     *
     * @param isPrimary property value or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getIsPrimary()
     */
    public void setIsPrimary(final Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    /**
     * Returns the application version.
     *
     * @return the application version
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getVersion()
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the application version.
     *
     * @param version application version
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getVersion()
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Returns structure with server type application specific properties.
     * It should be null for client type applications.
     *
     * @return the server type specific application properties or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getServerInfo()
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer
     */
    public IGServerInfo getServerInfo() {
        return serverInfo;
    }

    /**
     * Sets structure with server type application specific properties.
     * It should be null for client type applications.
     *
     * @param serverInfo server type specific application properties or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getServerInfo()
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer
     */
    public void setServerInfo(final IGServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    /**
     * Returns pointer to the list of structures of type
     * {@link IGPortInfo} containing information about listening ports for this server application.
     *
     * @return list of structures or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getPortInfos()
     */
    public List<IGPortInfo> getPortInfos() {
        return portInfos;
    }

    /**
     * Overloaded method to select listening port configuration by specified port name (id).<br/>
     * It searches for value in {@link #getPortInfos()}.
     *
     * @param portId listening port name (id) to look for
     * @return listening port configuration or null
     * @see #getPortInfos()
     * @see #getPortInfo(List, String)
     */
    public IGPortInfo getPortInfo(final String portId) {
        return getPortInfo(getPortInfos(), portId);
    }

    /**
     * Utility method to select listening port configuration by specified port name (id).
     *
     * @param allPorts list of port descriptions to select from
     * @param portId listening port name (id) to look for
     * @return listening port configuration or null
     */
    public static IGPortInfo getPortInfo(
            final List<IGPortInfo> allPorts,
            final String portId) {
        if (allPorts != null) {
            for (IGPortInfo portInfo : allPorts) {
                if (portInfo.getId().equals(portId)) {
                    return portInfo;
                }
            }
        }
        return null;
    }

    /**
     * Sets pointer to the list of structures of type
     * {@link IGPortInfo} containing information about listening ports for this server application.
     *
     * @param portInfos list of structures or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getPortInfos()
     */
    public void setPortInfos(final List<IGPortInfo> portInfos) {
        this.portInfos = portInfos;
    }

    /**
     * Returns the HA type if this application is considered as server.
     *
     * @return the application HA type or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getRedundancyType()
     */
    public CfgHAType getRedundancyType() {
        return redundancyType;
    }

    /**
     * Sets the HA type if this application is considered as server.
     *
     * @param redundancyType application HA type or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getRedundancyType()
     */
    public void setRedundancyType(final CfgHAType redundancyType) {
        this.redundancyType = redundancyType;
    }

    /**
     * Returns list of structures describing connected server applications.
     *
     * @return list of structures or null
     * @see IGAppConnConfiguration
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getAppServers()
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo
     */
    public List<IGAppConnConfiguration> getAppServers() {
        return appServers;
    }

    /**
     * Overloaded method to select subset of connected applications with specific application type.<br/>
     * It searches for values in {@link #getAppServers()}.
     *
     * @param type application type to look for
     * @return list of connected applications with specific type or null
     * @see #getAppServers()
     */
    public List<IGAppConnConfiguration> getAppServers(final CfgAppType type) {
        return getAppServers(getAppServers(), type);
    }

    /**
     * Overloaded method to select single connected application with specific application type.<br/>
     * It searches for value in {@link #getAppServers()}.
     *
     * @param type application type to look for
     * @return application connection configuration for specified application type or null
     * @see #getAppServers()
     */
    public IGAppConnConfiguration getAppServer(final CfgAppType type) {
        List<IGAppConnConfiguration> connected = getAppServers(getAppServers(), type);
        if (connected == null || connected.isEmpty()) {
            return null;
        }
        //if (connected.size() > 1) {
            // throw new Configuration exception // todo ?
        //}
        return connected.get(0);
    }

    /**
     * Utility method to select subset of connected applications with specific application type.
     *
     * @param allConnections list of applications connections to select from
     * @param type application type to look for
     * @return list of connected applications with specific type or null
     */
    public static List<IGAppConnConfiguration> getAppServers(
            final List<IGAppConnConfiguration> allConnections,
            final CfgAppType type) {
        List<IGAppConnConfiguration> selectedItems = null;
        if (allConnections != null) {
            selectedItems = new LinkedList<IGAppConnConfiguration>();
            for (IGAppConnConfiguration item : allConnections) {
                if (item.getTargetServerConfiguration() != null
                        && item.getTargetServerConfiguration().getApplicationType() == type) {
                    selectedItems.add(item);
                }
            }
        }
        return selectedItems;
    }

    /**
     * Sets list of structures describing connected server applications.
     *
     * @param appServers list of structures or null
     * @see IGAppConnConfiguration
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getAppServers()
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo
     */
    public void setAppServers(final List<IGAppConnConfiguration> appServers) {
        this.appServers = appServers;
    }

    /**
     * Returns pointer to the list of application-specific configuration options.
     *
     * @return collection of options' sections or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getOptions()
     */
    public KeyValueCollection getOptions() {
        return options;
    }

    /**
     * Sets value of the application-specific configuration options.
     *
     * @param options collection of options' sections or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getOptions()
     */
    public void setOptions(final KeyValueCollection options) {
        this.options = options;
    }

    /**
     * Clones and sets value of the application-specific configuration options.
     *
     * @param options collection of options' sections or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getOptions()
     */
    protected void setOptionsClone(final KeyValueCollection options) {
        this.options = ConfigurationUtil.deepKVListClone(options);
    }

    /**
     * Returns pointer to the list of user-defined properties.<br/>
     * It represents the "Annex" tab of the application object in CME.
     *
     * @return collection of properties sections or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getUserProperties()
     */
    public KeyValueCollection getUserProperties() {
        return userProperties;
    }

    /**
     * Sets value of the user-defined application properties.<br/>
     * It represents the "Annex" tab of the application object in CME.
     *
     * @param userProperties collection of properties sections or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getUserProperties()
     */
    public void setUserProperties(final KeyValueCollection userProperties) {
        this.userProperties = userProperties;
    }

    /**
     * Clones and sets value of the user-defined application properties.<br/>
     * It represents the "Annex" tab of the application object in CME.
     *
     * @param userProperties collection of properties sections or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getUserProperties()
     */
    protected void setUserPropertiesClone(final KeyValueCollection userProperties) {
        this.userProperties = ConfigurationUtil.deepKVListClone(userProperties);
    }

    /**
     * Returns pointer to the list of additional properties.
     *
     * @return collection of additional properties or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getFlexibleProperties()
     */
    public KeyValueCollection getFlexibleProperties() {
        return flexibleProperties;
    }

    /**
     * Sets value of the additional application properties.
     *
     * @param flexibleProperties collection of additional properties or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getFlexibleProperties()
     */
    public void setFlexibleProperties(final KeyValueCollection flexibleProperties) {
        this.flexibleProperties = flexibleProperties;
    }

    /**
     * Clones and sets value of the additional application properties.
     *
     * @param flexibleProperties collection of additional properties or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getFlexibleProperties()
     */
    protected void setFlexiblePropertiesClone(final KeyValueCollection flexibleProperties) {
        this.flexibleProperties = ConfigurationUtil.deepKVListClone(flexibleProperties);
    }

    /**
     * Returns list with information about tenants that are served by this application.<br/>
     * This value may be <code>null</code> if tenants information was not read/requested
     * (by default it is not requested).
     *
     * @return list with tenants information or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getTenants()
     */
    public List<IGTenantInfo> getTenants() {
        return tenants;
    }

    /**
     * Sets list with information about tenants that are served by this application.<br/>
     * This value may be <code>null</code> if tenants information was not read/requested
     * (by default it is not requested).
     *
     * @param tenants list with tenants information
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getTenants()
     */
    public void setTenants(final List<IGTenantInfo> tenants) {
        this.tenants = tenants;
    }


    @Override
    public GApplicationConfiguration clone() {
        try {
            final GApplicationConfiguration clone =
                    (GApplicationConfiguration) super.clone();
            if (options != null) {
                clone.options = (KeyValueCollection) options.clone();
            }
            if (userProperties != null) {
                clone.userProperties = (KeyValueCollection) userProperties.clone();
            }
            if (flexibleProperties != null) {
                clone.flexibleProperties = (KeyValueCollection) flexibleProperties.clone();
            }
            if (serverInfo instanceof GServerInfo) {
                clone.serverInfo = ((GServerInfo) serverInfo).clone();
            }
            if (portInfos != null) {
                clone.portInfos = new LinkedList<IGPortInfo>();
                for (IGPortInfo portInfo: portInfos) {
                    if (portInfo instanceof GPortInfo) {
                        clone.portInfos.add(((GPortInfo) portInfo).clone());
                    } else {
                        clone.portInfos.add(portInfo);
                    }
                }
            }
            if (tenants != null) {
                clone.tenants = new LinkedList<IGTenantInfo>();
                for (IGTenantInfo tenant: tenants) {
                    if (tenant instanceof GTenantInfo) {
                        clone.tenants.add(((GTenantInfo) tenant).clone());
                    } else {
                        clone.tenants.add(tenant);
                    }
                }
            }
            if (appServers != null) {
                clone.appServers = new LinkedList<IGAppConnConfiguration>();
                for (IGAppConnConfiguration connInfo: appServers) {
                    if (connInfo instanceof GAppConnConfiguration) {
                        clone.appServers.add(((GAppConnConfiguration) connInfo).clone());
                    } else {
                        clone.appServers.add(connInfo);
                    }
                }
            }
            return clone;
        } catch (final CloneNotSupportedException e) {
            return this;
        }
    }


    @Override
    public String toString() {
        return getClass().getName() + " {\n"
                + contentToString() + "}";
    }

    /**
     * This method is used from {@link #toString()} to build
     * string representation of the internal content (configuration properties names and values).
     *
     * @return string representation of the configuration content
     * @see #toString()
     */
    protected String contentToString() {
        final String prefix = "    ";
        StringBuilder sb = new StringBuilder();

        sb.append(prefix).append("ApplicationName: ").append(applicationName).append("\n");
        if (applicationType != null) {
            sb.append(prefix).append("ApplicationType: ").append(applicationType).append("\n");
        }
        if (dbid != null) {
            sb.append(prefix).append("DBID: ").append(dbid).append("\n");
        }
        if (objectState != null) {
            sb.append(prefix).append("ObjectState: ").append(objectState).append("\n");
        }
        if (version != null) {
            sb.append(prefix).append("Version: ").append(version).append("\n");
        }
        if (isServer != null) {
            sb.append(prefix).append("IsServer: ").append(isServer).append("\n");
        }
        if (isPrimary != null) {
            sb.append(prefix).append("IsPrimary: ").append(isPrimary).append("\n");
        }
        if (redundancyType != null) {
            sb.append(prefix).append("RedundancyType: ").append(redundancyType).append("\n");
        }
        if (serverInfo != null) {
            sb.append(prefix).append("ServerInfo: ").append(serverInfo).append("\n");
        }
        if (portInfos != null) {
            sb.append(prefix).append("PortInfos: ").append(portInfos).append("\n");
        }
        if (options != null) {
            sb.append(prefix).append("Options: ").append(options).append("\n");
        }
        if (userProperties != null) {
            sb.append(prefix).append("UserProperties: ").append(userProperties).append("\n");
        }
        if (flexibleProperties != null) {
            sb.append(prefix).append("FlexibleProperties: ").append(flexibleProperties).append("\n");
        }
        if (appServers != null) {
            sb.append(prefix).append("AppServers: ").append(appServers).append("\n");
        }
        if (tenants != null) {
            sb.append(prefix).append("Tenants: ").append(tenants).append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        final GApplicationConfiguration other = (GApplicationConfiguration) obj;
        if (!CompareUtils.equals(other.applicationName, applicationName)
         || !CompareUtils.equals(other.applicationType, applicationType)
         || !CompareUtils.equals(other.dbid, dbid)
         || !CompareUtils.equals(other.objectState, objectState)
         || !CompareUtils.equals(other.version, version)
         || !CompareUtils.equals(other.isServer, isServer)
         || !CompareUtils.equals(other.isPrimary, isPrimary)
         || !CompareUtils.equals(other.serverInfo, serverInfo)
         || !CompareUtils.equals(other.redundancyType, redundancyType)
         || !CompareUtils.equals(other.portInfos, portInfos)
         || !CompareUtils.equals(other.options, options)
         || !CompareUtils.equals(other.userProperties, userProperties)
         || !CompareUtils.equals(other.flexibleProperties, flexibleProperties)
         || !CompareUtils.equals(other.appServers, appServers)
         || !CompareUtils.equals(other.tenants, tenants)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = getClass().hashCode();
        if (applicationName != null) {
            hash = hash * 31 + applicationName.hashCode();
        }
        if (applicationType != null) {
            hash = hash * 31 + applicationType.hashCode();
        }
        if (dbid != null) {
            hash = hash * 31 + dbid.hashCode();
        }
        if (objectState != null) {
            hash = hash * 31 + objectState.hashCode();
        }
        if (version != null) {
            hash = hash * 31 + version.hashCode();
        }
        if (isServer != null) {
            hash = hash * 31 + isServer.hashCode();
        }
        if (isPrimary != null) {
            hash = hash * 31 + isPrimary.hashCode();
        }
        if (serverInfo != null) {
            hash = hash * 31 + serverInfo.hashCode();
        }
        if (redundancyType != null) {
            hash = hash * 31 + redundancyType.hashCode();
        }
        if (portInfos != null) {
            hash = hash * 31 + portInfos.hashCode();
        }
        if (options != null) {
            hash = hash * 31 + options.hashCode();
        }
        if (userProperties != null) {
            hash = hash * 31 + userProperties.hashCode();
        }
        if (flexibleProperties != null) {
            hash = hash * 31 + flexibleProperties.hashCode();
        }
        if (appServers != null) {
            hash = hash * 31 + appServers.hashCode();
        }
        if (tenants != null) {
            hash = hash * 31 + tenants.hashCode();
        }
        return hash;
    }


    /**
     * This structure represents group of server type specific application properties.
     *
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer
     */
    public static class GServerInfo implements IGServerInfo, Cloneable, Serializable {

        private static final long serialVersionUID = 8962660361677630847L;

        private IGHost host;
        private String port;
        private Integer timeout;
        private Integer attempts;
        private IGApplicationConfiguration backup;

        /**
         * Default empty constructor.
         * Creates uninitialized configuration object.
         */
        public GServerInfo() {
        }

        /**
         * Coping constructor.<br/>
         * <i><b>Note:</b></i> it does not clone referred structures {@link #getHost() Host},
         * {@link #getBackup() Backup}, etc.
         *
         * @param conf original configuration
         */
        public GServerInfo(final IGServerInfo conf) {
            this.host = conf.getHost();
            this.port = conf.getPort();
            this.timeout = conf.getTimeout();
            this.attempts = conf.getAttempts();
            this.backup = conf.getBackup();
        }

        /**
         * Returns name of the port to connect to on the target server.
         *
         * @return target port name/id
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getPort()
         */
        public String getPort() {
            return port;
        }

        /**
         * Returns reconnect timeout for connection to the target application.
         *
         * @return reconnect timeout in seconds
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getTimeout()
         */
        public Integer getTimeout() {
            return timeout;
        }

        /**
         * Returns number of attempts to connect to this server before trying
         * to connect to the backup server.
         *
         * @return reconnect attempts number or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getAttempts()
         */
        public Integer getAttempts() {
            return attempts;
        }

        /**
         * Returns description of server which is to be contacted if connection to this server fails.
         *
         * @return reference to description of backup server application or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getBackupServer()
         */
        public IGApplicationConfiguration getBackup() {
            return backup;
        }

        /**
         * Returns reference to structure describing host where this server resides.
         *
         * @return the host description structure
         * @see IGHost
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getHost()
         */
        public IGHost getHost() {
            return host;
        }

        /**
         * Sets name of the port to connect to on the target server.
         *
         * @param port target port name/id
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getPort()
         */
        public void setPort(final String port) {
            this.port = port;
        }

        /**
         * Sets reconnect timeout for connection to the target application.
         *
         * @param timeout reconnect timeout in seconds
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getTimeout()
         */
        public void setTimeout(final Integer timeout) {
            this.timeout = timeout;
        }

        /**
         * Sets number of attempts to connect to this server before trying
         * to connect to the backup server.
         *
         * @param attempts reconnect attempts number or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getAttempts()
         */
        public void setAttempts(final Integer attempts) {
            this.attempts = attempts;
        }

        /**
         * Sets reference to description of server which is to be contacted if connection to this server fails.
         *
         * @param backup reference to description of backup server application or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getBackupServer()
         */
        public void setBackup(final IGApplicationConfiguration backup) {
            this.backup = backup;
        }

        /**
         * Sets reference to structure describing host where this server resides.
         *
         * @param host the host description structure
         * @see IGHost
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getHost()
         */
        public void setHost(final IGHost host) {
            this.host = host;
        }

        @Override
        public GServerInfo clone() {
            try {
                final GServerInfo clone = (GServerInfo) super.clone();
                if (backup instanceof GApplicationConfiguration) {
                    clone.backup = ((GApplicationConfiguration) backup).clone();
                }
                if (host instanceof GHost) {
                    clone.host = ((GHost) host).clone();
                }
                return clone;
            } catch (final CloneNotSupportedException ex) {
                return this;
            }
        }

        @Override
        public String toString() {
            return getClass().getName() + " {\n"
                    + contentToString() + "}";
        }

        /**
         * This method is used from {@link #toString()} to build
         * string representation of the internal content (configuration properties names and values).
         *
         * @return string representation of the configuration content
         * @see #toString()
         */
        protected String contentToString() {
            final String prefix = "      ";
            StringBuilder sb = new StringBuilder();

            if (host != null) {
                sb.append(prefix).append("Host: ").append(host).append("\n");
            }
            if (port != null) {
                sb.append(prefix).append("Port: ").append(port).append("\n");
            }
            if (timeout != null) {
                sb.append(prefix).append("Timeout: ").append(timeout).append("\n");
            }
            if (attempts != null) {
                sb.append(prefix).append("Attempts: ").append(attempts).append("\n");
            }
            if (backup != null) {
                sb.append(prefix).append("Backup: ").append(backup).append("\n");
            }

            return sb.toString();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !obj.getClass().equals(getClass())) {
                return false;
            }
            final GServerInfo other = (GServerInfo) obj;
            if (!CompareUtils.equals(other.host, host)
             || !CompareUtils.equals(other.port, port)
             || !CompareUtils.equals(other.timeout, timeout)
             || !CompareUtils.equals(other.attempts, attempts)
             || !CompareUtils.equals(other.backup, backup)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = getClass().hashCode();
            if (host != null) {
                hash = hash * 31 + host.hashCode();
            }
            if (port != null) {
                hash = hash * 31 + port.hashCode();
            }
            if (timeout != null) {
                hash = hash * 31 + timeout.hashCode();
            }
            if (attempts != null) {
                hash = hash * 31 + attempts.hashCode();
            }
            if (backup != null) {
                hash = hash * 31 + backup.hashCode();
            }
            return hash;
        }
    }


    /**
     * Structure describing host where server is configured to run.<br/>
     * It reflects detached information from COM AB
     * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgHost CfgHost}.
     *
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost
     */
    public static class GHost implements IGHost, Cloneable, Serializable {

        private static final long serialVersionUID = 5244876898828787685L;

        private String name;
        private Integer dbid;
        private String ipaddress;
        private String lcaport;
        private CfgObjectState state;
        private KeyValueCollection userProperties;

        /**
         * Default empty constructor.
         * Creates uninitialized configuration object.
         */
        public GHost() {
        }

        /**
         * Coping constructor.<br/>
         * <i><b>Note:</b></i> it does not clone referred structure
         * {@link #getUserProperties() UserProperties}.
         *
         * @param conf original configuration
         */
        public GHost(final IGHost conf) {
            name = conf.getName();
            dbid = conf.getDbid();
            ipaddress = conf.getIPAddress();
            lcaport = conf.getLCAPort();
            state = conf.getObjectState();
            userProperties = conf.getUserProperties();
        }

        /**
         * Returns the host name.
         *
         * @return host name
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getName()
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the host DBID.
         *
         * @return the host object DBID
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getDBID()
         */
        public Integer getDbid() {
            return dbid;
        }

        /**
         * Returns the host TCP/IP address.
         *
         * @return the host TCP/IP address
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getIPaddress()
         */
        public String getIPAddress() {
            return ipaddress;
        }

        /**
         * Returns port number on which the Local Control Agent for this host is supposed to be running.
         *
         * @return the LCA port number
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getLCAPort()
         */
        public String getLCAPort() {
            return lcaport;
        }

        /**
         * Returns actual object state in the Genesys Configuration Database.
         *
         * @return actual object state
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getState()
         */
        public CfgObjectState getObjectState() {
            return state;
        }

        /**
         * Returns pointer to the list of user-defined properties.<br/>
         * It represents the "Annex" tab of the host object in CME.
         *
         * @return collection of properties sections or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getUserProperties()
         */
        public KeyValueCollection getUserProperties() {
            return userProperties;
        }

        /**
         * Sets the host name.
         *
         * @param name the host name
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getName()
         */
        public void setName(final String name) {
            this.name = name;
        }

        /**
         * Sets the host DBID.
         *
         * @param dbid the host object DBID
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getDBID()
         */
        public void setDbid(final Integer dbid) {
            this.dbid = dbid;
        }

        /**
         * Sets the host TCP/IP address.
         *
         * @param ipaddress the host TCP/IP address
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getIPaddress()
         */
        public void setIPAddress(final String ipaddress) {
            this.ipaddress = ipaddress;
        }

        /**
         * Sets port number on which the Local Control Agent for this host is supposed to be running.
         *
         * @param lcaport the LCA port number
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getLCAPort()
         */
        public void setLCAPort(final String lcaport) {
            this.lcaport = lcaport;
        }

        /**
         * Sets actual object state in the Genesys Configuration Database.
         *
         * @param state actual object state
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getState()
         */
        public void setObjectState(final CfgObjectState state) {
            this.state = state;
        }

        /**
         * Sets pointer to the list of user-defined properties.<br/>
         * It represents the "Annex" tab of the host object in CME.
         *
         * @param userProperties collection of properties sections or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getUserProperties()
         */
        public void setUserProperties(final KeyValueCollection userProperties) {
            this.userProperties = userProperties;
        }

        @Override
        public GHost clone() {
            try {
                final GHost clone = (GHost) super.clone();
                if (userProperties != null) {
                    clone.userProperties = (KeyValueCollection) userProperties.clone();
                }
                return clone;
            } catch (final CloneNotSupportedException ex) {
                return this;
            }
        }

        @Override
        public String toString() {
            return getClass().getName() + " {\n"
                    + contentToString() + "}";
        }

        /**
         * This method is used from {@link #toString()} to build
         * string representation of the internal content (configuration properties names and values).
         *
         * @return string representation of the configuration content
         * @see #toString()
         */
        protected String contentToString() {
            final String prefix = "        ";
            StringBuilder sb = new StringBuilder();

            sb.append(prefix).append("Name: ").append(name).append("\n");
            if (dbid != null) {
                sb.append(prefix).append("DBID: ").append(dbid).append("\n");
            }
            if (state != null) {
                sb.append(prefix).append("State: ").append(state).append("\n");
            }
            sb.append(prefix).append("IPAddress: ").append(ipaddress).append("\n");
            if (lcaport != null) {
                sb.append(prefix).append("LCAPort: ").append(lcaport).append("\n");
            }
            if (userProperties != null) {
                sb.append(prefix).append("UserProperties: ").append(userProperties).append("\n");
            }

            return sb.toString();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !obj.getClass().equals(getClass())) {
                return false;
            }
            final GHost other = (GHost) obj;
            if (!CompareUtils.equals(other.name, name)
             || !CompareUtils.equals(other.dbid, dbid)
             || !CompareUtils.equals(other.state, state)
             || !CompareUtils.equals(other.ipaddress, ipaddress)
             || !CompareUtils.equals(other.lcaport, lcaport)
             || !CompareUtils.equals(other.userProperties, userProperties)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = getClass().hashCode();
            if (name != null) {
                hash = hash * 31 + name.hashCode();
            }
            if (dbid != null) {
                hash = hash * 31 + dbid.hashCode();
            }
            if (ipaddress != null) {
                hash = hash * 31 + ipaddress.hashCode();
            }
            if (lcaport != null) {
                hash = hash * 31 + lcaport.hashCode();
            }
            if (state != null) {
                hash = hash * 31 + state.hashCode();
            }
            if (userProperties != null) {
                hash = hash * 31 + userProperties.hashCode();
            }
            return hash;
        }
    }


    /**
     * This structure contains properties for listening port of server type application.
     *
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo
     */
    public static class GPortInfo implements IGPortInfo, Cloneable, Serializable {

        private static final long serialVersionUID = 6364646589586718441L;

        private String id;
        private Integer port;
        private String connProtocol;
        private String transportParams;
        private String appParams;
        private String description;

        /**
         * Default empty constructor.
         * Creates uninitialized configuration object.
         */
        public GPortInfo() {
        }

        /**
         * Coping constructor.
         *
         * @param info original configuration
         */
        public GPortInfo(final IGPortInfo info) {
            id = info.getId();
            port = info.getPort();
            connProtocol = info.getConnProtocol();
            transportParams = info.getTransportParams();
            appParams = info.getAppParams();
            description = info.getDescription();
        }

        /**
         * Returns servers' port name/identifier.
         *
         * @return port identifier
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getId()
         */
        public String getId() {
            return id;
        }

        /**
         * Returns TCP/IP port number for listening on by "this" application.
         *
         * @return port number
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getPort()
         */
        public Integer getPort() {
            return port;
        }

        /**
         * Returns name of the connection control protocol.
         * Available values: "addp". Default: none.
         *
         * @return control protocol name or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getConnProtocol()
         */
        public String getConnProtocol() {
            return connProtocol;
        }

        /**
         * Returns transport parameters for the listening port.
         *
         * @return transport parameters or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getTransportParams()
         */
        public String getTransportParams() {
            return transportParams;
        }

        /**
         * Returns application parameters for the listening port.
         *
         * @return application parameters or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getAppParams()
         */
        public String getAppParams() {
            return appParams;
        }

        /**
         * Returns description of the listening port.
         *
         * @return port description or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getDescription()
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets servers' port name/identifier.
         *
         * @param id port identifier
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getId()
         */
        public void setId(final String id) {
            this.id = id;
        }

        /**
         * Sets TCP/IP port number for listening on by "this" application.
         *
         * @param port port number
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getPort()
         */
        public void setPort(final Integer port) {
            this.port = port;
        }

        /**
         * Sets name of the connection control protocol.
         * Available values: "addp". Default: none.
         *
         * @param connProtocol control protocol name or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getConnProtocol()
         */
        public void setConnProtocol(final String connProtocol) {
            this.connProtocol = connProtocol;
        }

        /**
         * Sets transport parameters for the listening port.
         *
         * @param transportParams transport parameters or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getTransportParams()
         */
        public void setTransportParams(final String transportParams) {
            this.transportParams = transportParams;
        }

        /**
         * Sets application parameters for the listening port.
         *
         * @param appParams application parameters or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getAppParams()
         */
        public void setAppParams(final String appParams) {
            this.appParams = appParams;
        }

        /**
         * Sets description of the listening port.
         *
         * @param description port description or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getDescription()
         */
        public void setDescription(final String description) {
            this.description = description;
        }

        @Override
        public GPortInfo clone() {
            try {
                return (GPortInfo) super.clone();
            } catch (final CloneNotSupportedException ex) {
                return this;
            }
        }

        @Override
        public String toString() {
            return getClass().getName() + " {\n"
                    + contentToString() + "}";
        }

        /**
         * This method is used from {@link #toString()} to build
         * string representation of the internal content (configuration properties names and values).
         *
         * @return string representation of the configuration content
         * @see #toString()
         */
        protected String contentToString() {
            final String prefix = "      ";
            StringBuilder sb = new StringBuilder();

            sb.append(prefix).append("ID: ").append(id).append("\n");
            sb.append(prefix).append("Port: ").append(port).append("\n");
            if (description != null) {
                sb.append(prefix).append("Description: ").append(description).append("\n");
            }
            if (appParams != null) {
                sb.append(prefix).append("AppParams: ").append(appParams).append("\n");
            }
            if (connProtocol != null) {
                sb.append(prefix).append("ConnProtocol: ").append(connProtocol).append("\n");
            }
            if (transportParams != null) {
                sb.append(prefix).append("TransportParams: ").append(transportParams).append("\n");
            }

            return sb.toString();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !obj.getClass().equals(getClass())) {
                return false;
            }
            final GPortInfo other = (GPortInfo) obj;
            if (!CompareUtils.equals(other.id, id)
             || !CompareUtils.equals(other.port, port)
             || !CompareUtils.equals(other.description, description)
             || !CompareUtils.equals(other.appParams, appParams)
             || !CompareUtils.equals(other.connProtocol, connProtocol)
             || !CompareUtils.equals(other.transportParams, transportParams)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = getClass().hashCode();
            if (id != null) {
                hash = hash * 31 + id.hashCode();
            }
            if (port != null) {
                hash = hash * 31 + port.hashCode();
            }
            if (connProtocol != null) {
                hash = hash * 31 + connProtocol.hashCode();
            }
            if (transportParams != null) {
                hash = hash * 31 + transportParams.hashCode();
            }
            if (appParams != null) {
                hash = hash * 31 + appParams.hashCode();
            }
            if (description != null) {
                hash = hash * 31 + description.hashCode();
            }
            return hash;
        }
    }


    /**
     * The application connection configuration structure reflects COM AB
     * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo CfgConnInfo} information.<br/>
     * It contains reference to connected server with related connection properties.
     *
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo
     */
    public static class GAppConnConfiguration
            implements IGAppConnConfiguration, Cloneable, Serializable {

        private static final long serialVersionUID = 2780939634393903092L;

        private String portId;
        private String connProtocol;
        private Integer timeoutLocal;
        private Integer timeoutRemote;
        private CfgTraceMode traceMode;
        private String transportParams;
        private String appParams;
        private String proxyParams;
        private String description;
        private IGApplicationConfiguration targetServer;

        /**
         * Default empty constructor.
         * Creates uninitialized configuration object.
         */
        public GAppConnConfiguration() {
        }

        /**
         * Coping constructor.<br/>
         * <i><b>Note:</b></i> it does not clone referred structure
         * {@link #getTargetServerConfiguration() TargetServerConfiguration}.
         *
         * @param conf original configuration
         */
        public GAppConnConfiguration(
                final IGAppConnConfiguration conf) {
            portId = conf.getPortId();
            connProtocol = conf.getConnProtocol();
            timeoutLocal = conf.getTimeoutLocal();
            timeoutRemote = conf.getTimeoutRemote();
            traceMode = conf.getTraceMode();
            transportParams = conf.getTransportParams();
            appParams = conf.getAppParams();
            proxyParams = conf.getProxyParams();
            description = conf.getDescription();
            targetServer = conf.getTargetServerConfiguration();
        }

        /**
         * Returns application configuration of the connected server.
         *
         * @return application configuration of the connected server
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getAppServer()
         */
        public IGApplicationConfiguration getTargetServerConfiguration() {
            return targetServer;
        }

        /**
         * Returns identifier of the server's listening port.
         * Should correspond to {@link IGPortInfo#getId()}.
         *
         * @return connection port name/identifier
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getId()
         */
        public String getPortId() {
            return portId;
        }

        /**
         * Returns name of the connection control protocol.
         * Available values: "addp". Default: none.
         *
         * @return name of the connection control protocol
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getConnProtocol()
         */
        public String getConnProtocol() {
            return connProtocol;
        }

        /**
         * Returns the heart-bit polling interval measured in seconds, on client site.<br/>
         * Valuable if connection protocol ({@link #getConnProtocol()}) is "addp".
         *
         * @return local ADDP timeout or null
         * @see #getConnProtocol()
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getTimoutLocal()
         */
        public Integer getTimeoutLocal() {
            return timeoutLocal;
        }

        /**
         * Returns the heart-bit polling interval measured in seconds, on server site.<br/>
         * Valuable if connection protocol ({@link #getConnProtocol()}) is "addp".
         *
         * @return remote ADDP timeout or null
         * @see #getConnProtocol()
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getTimoutRemote()
         */
        public Integer getTimeoutRemote() {
            return timeoutRemote;
        }

        /**
         * Returns the ADDP trace mode dedicated for this connection.
         * Default value is CFGTMNoTraceMode ("no addp trace logs").
         *
         * @return trace mode or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getMode()
         */
        public CfgTraceMode getTraceMode() {
            return traceMode;
        }

        /**
         * Returns connection protocol's transport parameters.
         *
         * @return the connection transport parameters
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getTransportParams()
         */
        public String getTransportParams() {
            return transportParams;
        }

        /**
         * Returns connection protocol's application parameters.
         *
         * @return the connection application parameters
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getAppParams()
         */
        public String getAppParams() {
            return appParams;
        }

        /**
         * Returns connection protocol's proxy parameters.
         *
         * @return the connection proxy parameters
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getProxyParams()
         */
        public String getProxyParams() {
            return proxyParams;
        }

        /**
         * Returns optional description of the connection.
         *
         * @return connection description or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getDescription()
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets reference to application configuration of the connected server.
         *
         * @param targetServer application configuration of the connected server
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getAppServer()
         */
        public void setTargetServerConfiguration(final IGApplicationConfiguration targetServer) {
            this.targetServer = targetServer;
        }

        /**
         * Sets identifier of the server's listening port.
         * Should correspond to {@link IGPortInfo#getId()}.
         *
         * @param portId connection port name/identifier
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getId()
         */
        public void setPortId(final String portId) {
            this.portId = portId;
        }

        /**
         * Sets name of the connection control protocol.
         * Available values: "addp". Default: none.
         *
         * @param connProtocol name of the connection control protocol
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getConnProtocol()
         */
        public void setConnProtocol(final String connProtocol) {
            this.connProtocol = connProtocol;
        }

        /**
         * Sets the heart-bit polling interval measured in seconds, on client site.<br/>
         * Valuable if connection protocol ({@link #getConnProtocol()}) is "addp".
         *
         * @param timeout local ADDP timeout or null
         * @see #setConnProtocol(String)
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getTimoutLocal()
         */
        public void setTimeoutLocal(final Integer timeout) {
            this.timeoutLocal = timeout;
        }

        /**
         * Sets the heart-bit polling interval measured in seconds, on server site.<br/>
         * Valuable if connection protocol ({@link #getConnProtocol()}) is "addp".
         *
         * @param timeout remote ADDP timeout or null
         * @see #setConnProtocol(String)
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getTimoutRemote()
         */
        public void setTimeoutRemote(final Integer timeout) {
            this.timeoutRemote = timeout;
        }

        /**
         * Sets the ADDP trace mode dedicated for this connection.
         * Default value is CFGTMNoTraceMode ("no addp trace logs").
         *
         * @param traceMode trace mode or null
         * @see #setConnProtocol(String)
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getMode()
         */
        public void setTraceMode(final CfgTraceMode traceMode) {
            this.traceMode = traceMode;
        }

        /**
         * Sets connection protocol's transport parameters.
         *
         * @param transportParams connection transport parameters
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getTransportParams()
         */
        public void setTransportParams(final String transportParams) {
            this.transportParams = transportParams;
        }

        /**
         * Sets connection protocol's application parameters.
         *
         * @param appParams connection application parameters
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getAppParams()
         */
        public void setAppParams(final String appParams) {
            this.appParams = appParams;
        }

        /**
         * Sets connection protocol's proxy parameters.
         *
         * @param proxyParams connection proxy parameters
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getProxyParams()
         */
        public void setProxyParams(final String proxyParams) {
            this.proxyParams = proxyParams;
        }

        /**
         * Sets optional description of the connection.
         *
         * @param description connection description or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getDescription()
         */
        public void setDescription(final String description) {
            this.description = description;
        }

        @Override
        public GAppConnConfiguration clone() {
            try {
                final GAppConnConfiguration clone = (GAppConnConfiguration) super.clone();
                if (targetServer instanceof GApplicationConfiguration) {
                    clone.targetServer = ((GApplicationConfiguration) targetServer).clone();
                }
                return clone;
            } catch (final CloneNotSupportedException ex) {
                return this;
            }
        }

        @Override
        public String toString() {
            return getClass().getName() + " {\n"
                    + contentToString() + "}";
        }

        /**
         * This method is used from {@link #toString()} to build
         * string representation of the internal content (configuration properties names and values).
         *
         * @return string representation of the configuration content
         * @see #toString()
         */
        protected String contentToString() {
            final String prefix = "      ";
            StringBuilder sb = new StringBuilder();

            sb.append(prefix).append("TargetServer: ").append(targetServer).append("\n");
            if (description != null) {
                sb.append(prefix).append("Description: ").append(description).append("\n");
            }
            if (portId != null) {
                sb.append(prefix).append("PortId: ").append(portId).append("\n");
            }
            if (appParams != null) {
                sb.append(prefix).append("AppParams: ").append(appParams).append("\n");
            }
            if (connProtocol != null) {
                sb.append(prefix).append("ConnProtocol: ").append(connProtocol).append("\n");
            }
            if (transportParams != null) {
                sb.append(prefix).append("TransportParams: ").append(transportParams).append("\n");
            }
            if (proxyParams != null) {
                sb.append(prefix).append("ProxyParams: ").append(proxyParams).append("\n");
            }
            if (traceMode != null) {
                sb.append(prefix).append("TraceMode: ").append(traceMode).append("\n");
            }
            if (timeoutLocal != null) {
                sb.append(prefix).append("TimeoutLocal: ").append(timeoutLocal).append("\n");
            }
            if (timeoutRemote != null) {
                sb.append(prefix).append("TimeoutRemote: ").append(timeoutRemote).append("\n");
            }

            return sb.toString();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !obj.getClass().equals(getClass())) {
                return false;
            }
            final GAppConnConfiguration other = (GAppConnConfiguration) obj;
            if (!CompareUtils.equals(other.portId, portId)
             || !CompareUtils.equals(other.connProtocol, connProtocol)
             || !CompareUtils.equals(other.timeoutLocal, timeoutLocal)
             || !CompareUtils.equals(other.timeoutRemote, timeoutRemote)
             || !CompareUtils.equals(other.traceMode, traceMode)
             || !CompareUtils.equals(other.transportParams, transportParams)
             || !CompareUtils.equals(other.appParams, appParams)
             || !CompareUtils.equals(other.proxyParams, proxyParams)
             || !CompareUtils.equals(other.description, description)
             || !CompareUtils.equals(other.targetServer, targetServer)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = getClass().hashCode();
            if (portId != null) {
                hash = hash * 31 + portId.hashCode();
            }
            if (connProtocol != null) {
                hash = hash * 31 + connProtocol.hashCode();
            }
            if (timeoutLocal != null) {
                hash = hash * 31 + timeoutLocal.hashCode();
            }
            if (timeoutRemote != null) {
                hash = hash * 31 + timeoutRemote.hashCode();
            }
            if (traceMode != null) {
                hash = hash * 31 + traceMode.hashCode();
            }
            if (transportParams != null) {
                hash = hash * 31 + transportParams.hashCode();
            }
            if (appParams != null) {
                hash = hash * 31 + appParams.hashCode();
            }
            if (proxyParams != null) {
                hash = hash * 31 + proxyParams.hashCode();
            }
            if (description != null) {
                hash = hash * 31 + description.hashCode();
            }
            if (targetServer != null) {
                hash = hash * 31 + targetServer.hashCode();
            }
            return hash;
        }
    }


    /**
     * Structure describing Tenant which is referred in the server application configuration.<br/>
     * It reflects detached information from COM AB
     * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgTenant CfgTenant}.
     *
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant
     */
    public static class GTenantInfo implements IGTenantInfo, Cloneable, Serializable {

        private static final long serialVersionUID = -8410049164004753298L;

        private String name;
        private Integer dbid;
        private CfgObjectState state;
        private Boolean isServiceProvider;
        private String ps_wrd;

        /**
         * Default empty constructor.
         * Creates uninitialized configuration object.
         */
        public GTenantInfo() {
        }

        /**
         * Coping constructor.
         *
         * @param conf original configuration
         */
        public GTenantInfo(final IGTenantInfo conf) {
            this.name = conf.getName();
            this.dbid = conf.getDbid();
            this.state = conf.getObjectState();
            this.isServiceProvider = conf.isServiceProvider();
            this.ps_wrd = conf.getPassword();
        }

        /**
         * Sets the tenant name.
         *
         * @param name the tenant name
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getName()
         */
        public void setName(final String name) {
            this.name = name;
        }

        /**
         * Returns the tenant name.
         *
         * @return the tenant name
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getName()
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the tenant DBID.
         *
         * @param dbid the tenant object DBID
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getDBID()
         */
        public void setDbid(final Integer dbid) {
            this.dbid = dbid;
        }

        /**
         * Returns the tenant DBID.
         *
         * @return the tenant object DBID
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getDBID()
         */
        public Integer getDbid() {
            return dbid;
        }

        /**
         * Sets actual object state.
         *
         * @param state actual object state
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getState()
         */
        public void setObjectState(final CfgObjectState state) {
            this.state = state;
        }

        /**
         * @deprecated
         * @see #setObjectState(CfgObjectState)
         */
        @Deprecated
        public void setState(final CfgObjectState state) {
            this.state = state;
        }

        /**
         * Returns actual object state.
         *
         * @return actual object state
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getState()
         */
        public CfgObjectState getObjectState() {
            return state;
        }

        @Deprecated
        public CfgObjectState getState() {
            return state;
        }

        /**
         * An indicator of whether the tenant belongs to the Service Provider.
         *
         * @param isServiceProvider the property value
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getIsServiceProvider()
         */
        public void setIsServiceProvider(final Boolean isServiceProvider) {
            this.isServiceProvider = isServiceProvider;
        }

        /**
         * An indicator of whether the tenant belongs to the Service Provider.
         *
         * @return the property value
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getIsServiceProvider()
         */
        public Boolean isServiceProvider() {
            return isServiceProvider;
        }

        /**
         * Sets the tenant password.
         *
         * @param password the tenant new password
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getPassword()
         */
        public void setPassword(final String password) {
            this.ps_wrd = password;
        }

        /**
         * Returns the tenant password.
         *
         * @return the tenant password
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getPassword()
         */
        public String getPassword() {
            return ps_wrd;
        }

        @Override
        public GTenantInfo clone() {
            try {
                final GTenantInfo clone = (GTenantInfo) super.clone();
                return clone;
            } catch (final CloneNotSupportedException ex) {
                return this;
            }
        }

        @Override
        public String toString() {
            return getClass().getName() + " {\n"
                    + contentToString() + "}";
        }

        /**
         * This method is used from {@link #toString()} to build
         * string representation of the internal content (configuration properties names and values).
         *
         * @return string representation of the configuration content
         * @see #toString()
         */
        protected String contentToString() {
            final String prefix = "      ";
            StringBuilder sb = new StringBuilder();

            sb.append(prefix).append("Name: ").append(name).append("\n");
            sb.append(prefix).append("DBID: ").append(dbid).append("\n");
            sb.append(prefix).append("State: ").append(state).append("\n");
            sb.append(prefix).append("IsServiceProvider: ").append(isServiceProvider).append("\n");

            return sb.toString();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !obj.getClass().equals(getClass())) {
                return false;
            }
            final GTenantInfo other = (GTenantInfo) obj;
            if (!CompareUtils.equals(other.name, name)
             || !CompareUtils.equals(other.dbid, dbid)
             || !CompareUtils.equals(other.state, state)
             || !CompareUtils.equals(other.isServiceProvider, isServiceProvider)
             || !CompareUtils.equals(other.ps_wrd, ps_wrd)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = getClass().hashCode();
            if (name != null) {
                hash = hash * 31 + name.hashCode();
            }
            if (dbid != null) {
                hash = hash * 31 + dbid.hashCode();
            }
            if (state != null) {
                hash = hash * 31 + state.hashCode();
            }
            if (isServiceProvider != null) {
                hash = hash * 31 + isServiceProvider.hashCode();
            }
            if (ps_wrd != null) {
                hash = hash * 31 + ps_wrd.hashCode();
            }
            return hash;
        }
    }
}
