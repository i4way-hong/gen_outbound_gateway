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

import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genesyslab.platform.configuration.protocol.types.CfgHAType;
import com.genesyslab.platform.configuration.protocol.types.CfgTraceMode;

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.util.List;


/**
 * <p>This interface represents base Genesys CME application options,
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
 * @see GApplicationConfiguration
 * @see GCOMApplicationConfiguration
 */
public interface IGApplicationConfiguration {

    /**
     * Returns the application name.<br/>
     * It represents the correspondent name in Genesys Configuration framework.
     *
     * @return the application name
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getName()
     */
    String getApplicationName();

    /**
     * Returns type of the application in terms of Genesys Configuration framework.
     *
     * @return the application type
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getType()
     */
    CfgAppType getApplicationType();

    /**
     * Returns unique application object identifier in context of Genesys Configuration Database.
     *
     * @return the application DBID
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getDBID()
     */
    Integer getDbid();

    /**
     * Returns actual object state in the Genesys Configuration Database.
     *
     * @return actual object state
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getState()
     */
    CfgObjectState getObjectState();

    /**
     * Returns indicator of whether this application can be a server to some other applications.
     * This value depends on the application type - {@link #getApplicationType()}.
     *
     * @return property value or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getIsServer()
     */
    Boolean isServer();

    /**
     * Returns value meaning role of application within HA/redundancy group.
     *
     * @return property value or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getIsPrimary()
     */
    Boolean isPrimary();

    /**
     * Returns the application version.
     *
     * @return the application version
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getVersion()
     */
    String getVersion();

    /**
     * Returns structure with server type application specific properties.
     * It should be null for client type applications.
     *
     * @return the server type specific application properties or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getServerInfo()
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer
     */
    IGServerInfo getServerInfo();

    /**
     * Returns pointer to the list of structures of type
     * {@link IGPortInfo} containing information about listening ports for this server application.
     *
     * @return list of structures or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getPortInfos()
     */
    List<IGPortInfo> getPortInfos();

    /**
     * Returns the HA type if this application is considered as server.
     *
     * @return the application HA type or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getRedundancyType()
     */
    CfgHAType getRedundancyType();

    /**
     * Returns list of structures describing connected server applications.
     *
     * @return list of structures or null
     * @see IGAppConnConfiguration
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getAppServers()
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo
     */
    List<IGAppConnConfiguration> getAppServers();

    /**
     * Returns pointer to the list of application-specific configuration options.
     *
     * @return collection of options' sections or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getOptions()
     */
    KeyValueCollection getOptions();

    /**
     * Returns pointer to the list of user-defined properties.<br/>
     * It represents the "Annex" tab of the application object in CME.
     *
     * @return collection of properties sections or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getUserProperties()
     */
    KeyValueCollection getUserProperties();

    /**
     * Returns pointer to the list of additional properties.
     *
     * @return collection of additional properties or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getFlexibleProperties()
     */
    KeyValueCollection getFlexibleProperties();

    /**
     * Returns list with information about tenants that are served by this application.<br/>
     * This value may be <code>null</code> if tenants information was not read/requested
     * (by default it is not requested).
     *
     * @return list with tenants information or null
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication#getTenants()
     */
    List<IGTenantInfo> getTenants();


    /**
     * This structure represents group of server type specific application properties.
     *
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer
     */
    interface IGServerInfo {

        /**
         * Returns reference to structure describing host where this server resides.
         *
         * @return the host description structure
         * @see IGHost
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getHost()
         */
        IGHost getHost();

        /**
         * Returns name of the port to connect to on the target server.
         *
         * @return target port name/id
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getPort()
         */
        String getPort();

        /**
         * Returns reconnect timeout for connection to the target application.
         *
         * @return reconnect timeout in seconds
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getTimeout()
         */
        Integer getTimeout();

        /**
         * Returns number of attempts to connect to this server before trying
         * to connect to the backup server.
         *
         * @return reconnect attempts number or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getAttempts()
         */
        Integer getAttempts();

        /**
         * Returns description of server which is to be contacted if connection to this server fails.
         *
         * @return reference to description of backup server application or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgServer#getBackupServer()
         */
        IGApplicationConfiguration getBackup();
    }


    /**
     * This structure contains properties for listening port of server type application.
     *
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo
     */
    interface IGPortInfo {

        /**
         * Returns servers' port name/identifier.
         *
         * @return port identifier
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getId()
         */
        String getId();

        /**
         * Returns TCP/IP port number for listening on by "this" application.
         *
         * @return port number
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getPort()
         */
        Integer getPort();

        /**
         * Returns name of the connection control protocol.
         * Available values: "addp". Default: none.
         *
         * @return control protocol name or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getConnProtocol()
         */
        String getConnProtocol();

        /**
         * Returns transport parameters for the listening port.
         *
         * @return transport parameters or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getTransportParams()
         */
        String getTransportParams();

        /**
         * Returns application parameters for the listening port.
         *
         * @return application parameters or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getAppParams()
         */
        String getAppParams();

        /**
         * Returns description of the listening port.
         *
         * @return port description or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo#getDescription()
         */
        String getDescription();
    }


    /**
     * The application connection configuration structure reflects COM AB
     * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo CfgConnInfo} information.<br/>
     * It contains reference to connected server with related connection properties.
     *
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo
     */
    interface IGAppConnConfiguration {

        /**
         * Returns application configuration of the connected server.
         *
         * @return application configuration of the connected server
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getAppServer()
         */
        IGApplicationConfiguration getTargetServerConfiguration();

        /**
         * Returns identifier of the server's listening port.
         * Should correspond to {@link IGPortInfo#getId()}.
         *
         * @return connection port name/identifier
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getId()
         */
        String getPortId();

        /**
         * Returns name of the connection control protocol.
         * Available values: "addp". Default: none.
         *
         * @return name of the connection control protocol
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getConnProtocol()
         */
        String getConnProtocol();

        /**
         * Returns the heart-bit polling interval measured in seconds, on client site.<br/>
         * Valuable if connection protocol ({@link #getConnProtocol()}) is "addp".
         *
         * @return local ADDP timeout or null
         * @see #getConnProtocol()
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getTimoutLocal()
         */
        Integer getTimeoutLocal();

        /**
         * Returns the heart-bit polling interval measured in seconds, on server site.<br/>
         * Valuable if connection protocol ({@link #getConnProtocol()}) is "addp".
         *
         * @return remote ADDP timeout or null
         * @see #getConnProtocol()
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getTimoutRemote()
         */
        Integer getTimeoutRemote();

        /**
         * Returns the ADDP trace mode dedicated for this connection.
         * Default value is CFGTMNoTraceMode ("no addp trace logs").
         *
         * @return trace mode or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getMode()
         */
        CfgTraceMode getTraceMode();

        /**
         * Returns connection protocol's transport parameters.
         *
         * @return the connection transport parameters
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getTransportParams()
         */
        String getTransportParams();

        /**
         * Returns connection protocol's application parameters.
         *
         * @return the connection application parameters
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getAppParams()
         */
        String getAppParams();

        /**
         * Returns connection protocol's proxy parameters.
         *
         * @return the connection proxy parameters
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getProxyParams()
         */
        String getProxyParams();

        /**
         * Returns optional description of the connection.
         *
         * @return connection description or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo#getDescription()
         */
        String getDescription();
    }


    /**
     * Structure describing host where server is configured to run.<br/>
     * It reflects detached information from COM AB
     * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgHost CfgHost}.
     *
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost
     */
    interface IGHost {

        /**
         * Returns the host name.
         *
         * @return the host name
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getName()
         */
        String getName();

        /**
         * Returns the host DBID.
         *
         * @return the host object DBID
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getDBID()
         */
        Integer getDbid();

        /**
         * Returns the host TCP/IP address.
         *
         * @return the host TCP/IP address
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getIPaddress()
         */
        String getIPAddress();

        /**
         * Returns port number on which the Local Control Agent for this host is supposed to be running.
         *
         * @return the LCA port number
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getLCAPort()
         */
        String getLCAPort();

        /**
         * Returns actual object state in the Genesys Configuration Database.
         *
         * @return actual object state
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getState()
         */
        CfgObjectState getObjectState();

        /**
         * Returns pointer to the list of user-defined properties.<br/>
         * It represents the "Annex" tab of the host object in CME.
         *
         * @return collection of properties sections or null
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgHost#getUserProperties()
         */
        KeyValueCollection getUserProperties();
    }


    /**
     * Structure describing Tenant which is referred in the server application configuration.<br/>
     * It reflects detached information from COM AB
     * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgTenant CfgTenant}.
     *
     * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant
     */
    interface IGTenantInfo {

        /**
         * Returns the tenant name.
         *
         * @return the tenant name
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getName()
         */
        String getName();

        /**
         * Returns the tenant DBID.
         *
         * @return the tenant object DBID
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getDBID()
         */
        Integer getDbid();

        /**
         * An indicator of whether the tenant belongs to the Service Provider.
         *
         * @return the property value
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getIsServiceProvider()
         */
        Boolean isServiceProvider();

        /**
         * Returns actual object state in the Genesys Configuration Database.
         *
         * @return actual object state
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getState()
         */
        CfgObjectState getObjectState();

        /**
         * @deprecated
         * @see #getObjectState()
         */
        @Deprecated
        CfgObjectState getState();

        /**
         * The tenant password.
         *
         * @return the tenant password
         * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTenant#getPassword()
         */
        String getPassword();
    }
}
