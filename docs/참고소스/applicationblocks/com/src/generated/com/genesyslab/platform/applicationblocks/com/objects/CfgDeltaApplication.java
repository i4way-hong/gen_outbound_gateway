//===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:

// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.

// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.

// THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
// ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
// DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
// WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
// NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
// NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
// EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
// CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
// NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).

// Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================

/* ATTENTION! DO NOT MODIFY THIS FILE - IT IS AUTOMATICALLY GENERATED! */

package com.genesyslab.platform.applicationblocks.com.objects;

import com.genesyslab.platform.applicationblocks.com.*;

import com.genesyslab.platform.configuration.protocol.obj.*;
import com.genesyslab.platform.configuration.protocol.types.*;

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import javax.annotation.Generated;

import java.util.Calendar;
import java.util.Collection;

import org.w3c.dom.Node;


/**
 * <p/>
 * The changes to make to a <code>
 * <a href="CfgApplication.html">CfgApplication</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDeltaApplication extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaApplication(final IConfService confService) {
        super(confService, "CfgDeltaApplication");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaApplication(
            final IConfService confService,
            final ConfObjectDelta objData) {
        super(confService, objData, null);
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param xmlData XML object containing object delta data
     */
    public CfgDeltaApplication(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgApplication configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgApplication configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgApplication retrieveCfgApplication() throws ConfigException {
        return (CfgApplication) (super.retrieveObject());
    }

    /**
     * A pointer to the name of the application.
     * Mandatory. Must be unique within the Configuration Database.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the application password.
     * Max length 64 symbols. Not used in 5.1.
     *
     * @return property value or null
     */
    public final String getPassword() {
        return (String) getProperty("password");
    }

    /**
     * Type of the application. Mandatory.
     * Set automatically according to the value of
     * <code>type</code> of the application prototype specified
     * in <code>appPrototypeDBID</code> or explicitly during the creation
     * time. Once specified, cannot be changed. See <code>
     * <a href="../Enumerations/CfgAppType.html">CfgAppType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgAppType getType() {
        return (CfgAppType) getProperty(CfgAppType.class, "type");
    }

    /**
     * A pointer to the application version.
     * Mandatory. Set automatically according to the value of
     * <code>version</code> of the application prototype specified
     * in <code>appPrototypeDBID</code> or explicitly.
     *
     * @return property value or null
     */
    public final String getVersion() {
        return (String) getProperty("version");
    }

    /**
     * A pointer to the list of structures of type
     * <code>
     * <a href="CfgConnInfo.html">CfgConnInfo</a>
     * </code>.
     *
     * @return list of structures or null
     */
    public final Collection<CfgConnInfo> getAddedAppServers() {
        return (Collection<CfgConnInfo>) getProperty("appServerDBIDs");
    }

    /**
     * A pointer to the list of identifiers
     * of the <code>
     * <a href="CfgTenant.html">Tenants</a>
     * </code>
     * that are served by this application. Makes sense
     * only for applications of the daemon type. For applications of
     * <code>CFGTServer</code> and <code>CFGHAProxy</code> type,
     * can contain only one tenant. A tenant can be added to this list
     * only if the account that the application is associated with has at
     * least <code>read-only access</code> to this tenant. When used as
     * an entry in <code>CfgDeltaApplication</code>, it is a pointer to a list of
     * identifiers of the tenants added to the existing list.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgTenant> getAddedTenants() {
        return (Collection<CfgTenant>) getProperty("tenantDBIDs");
    }

    /**
     * A pointer to the list of identifiers
     * of the <code>
     * <a href="CfgTenant.html">Tenants</a>
     * </code>
     * that are served by this application. Makes sense
     * only for applications of the daemon type. For applications of
     * <code>CFGTServer</code> and <code>CFGHAProxy</code> type,
     * can contain only one tenant. A tenant can be added to this list
     * only if the account that the application is associated with has at
     * least <code>read-only access</code> to this tenant. When used as
     * an entry in <code>CfgDeltaApplication</code>, it is a pointer to a list of
     * identifiers of the tenants added to the existing list.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedTenantDBIDs() {
        return getLinkListCollection("tenantDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedTenantDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getTenantDBIDs() {
        return getLinkListCollection("tenantDBIDs");
    }

    /**
     * An indicator of whether this application
     * can be a server to some other applications. Read-only (set automatically
     * according to the value of <code>type</code> above). See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgFlag getIsServer() {
        return (CfgFlag) getProperty(CfgFlag.class, "isServer");
    }

    /**
     * A pointer to the structure containing
     * server-specific information. Can be specified if, according to the
     * value specified for the
     * <code>type</code> above, the application is a daemon
     * and must be set to <code>NULL</code> otherwise. Once specified,
     * cannot be set to <code>NULL</code>. See <code>
     * <a href="CfgServer.html">CfgServer</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgServer getServerInfo() {
        return (CfgServer) getProperty(CfgServer.class, "serverInfo");
    }

    /**
     * A pointer to the list of application-specific configuration
     * options (see the comments below). When used as an entry in <code>CfgDeltaApplication</code>,
     * it is a pointer to a list of options added to the existing list.
     *
     * @return property value or null
     */
    public final KeyValueCollection getAddedOptions() {
        return (KeyValueCollection) getProperty("options");
    }

    /**
     * Current object state. Mandatory. Refer to <code>
     * <a href="../Enumerations/CfgObjectState.html">CfgObjectState</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) getProperty(CfgObjectState.class, "state");
    }

    /**
     * A pointer to the list of user-defined properties.Parameter userProperties has the following structure: Each key-value pair of the primary list (TKVList *userProperties) uses the key for the name of a user-defined section, and the value for a secondary list, that also has the TKVList structure and specifies the properties defined within that section.
     *
     * @return property value or null
     */
    public final KeyValueCollection getAddedUserProperties() {
        return (KeyValueCollection) getProperty("userProperties");
    }

    /**
     * A unique identifier of an application prototype this application
     * is based on. Optional. See <code>
     * <a href="CfgAppPrototype.html">CfgAppPrototype</a>
     * </code>. The association
     * with application prototype could be specified at moment of creation
     * of application object only.
     *
     * @return instance of referred object or null
     */
    public final CfgAppPrototype getAppPrototype() {
        return (CfgAppPrototype) getProperty(CfgAppPrototype.class, "appPrototypeDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the AppPrototype property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getAppPrototypeDBID() {
        return getLinkValue("appPrototypeDBID");
    }

    /**
     * A pointer to the list of additional properties. See <code>section Comments</code> at the beginning of this
     * document. Only described below options can be added to this property.
     * This field can not be changed as long as this server remains a backup
     * for some other server (see <code>
     * <a href="CfgServer.html">CfgServer</a>
     * </code> structure) and
     * can only be non-empty for the applications of type <code>CFGTServer</code> and
     * <code>CFGHAProxy</code>
     *
     * @return property value or null
     */
    public final KeyValueCollection getAddedFlexibleProperties() {
        return (KeyValueCollection) getProperty("flexibleProperties");
    }

    /**
     * Working directory for the
     * application. Must be specified if, according to the value specified
     * in <code>isServer</code> property, the application is a server and
     * optional otherwise. See comments.
     *
     * @return property value or null
     */
    public final String getWorkDirectory() {
        return (String) getProperty("workDirectory");
    }

    /**
     * The name of executable to be
     * used to start the application. Must be specified if, according to
     * the value specified in <code>isServer</code> property, the application
     * is a server and optional otherwise. See comments.
     *
     * @return property value or null
     */
    public final String getCommandLine() {
        return (String) getProperty("commandLine");
    }

    /**
     * Indicates whether the application
     * should be automatically restarted by Local Control Agent after its
     * crash. Mandatory. Recommended to be set to <code>CFGTrue</code> by
     * default. See comments. See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getAutoRestart() {
        return (CfgFlag) getProperty(CfgFlag.class, "autoRestart");
    }

    /**
     * A period of time within which the application is expected
     * to be completely started. Must be specified if, according to the
     * value specified in <code>isServer</code> property, the application
     * is a server and optional otherwise. Default value is 90 seconds.
     * See comments.
     *
     * @return property value or null
     */
    public final Integer getStartupTimeout() {
        return (Integer) getProperty("startupTimeout");
    }

    /**
     * A period of time within which the application is expected
     * to be completely shut down. Must be specified if, according to the
     * value specified in <code>isServer</code> property, the application
     * is a server and optional otherwise. Default value is 90 seconds.
     * See comments.
     *
     * @return property value or null
     */
    public final Integer getShutdownTimeout() {
        return (Integer) getProperty("shutdownTimeout");
    }

    /**
     * Defines the HA type if this application is considered as
     * server. Mandatory. Default is <code>CFGHTColdStandby.</code> See
     * comments. See <code>
     * <a href="../Enumerations/CfgHAType.html">CfgHAType</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgHAType getRedundancyType() {
        return (CfgHAType) getProperty(CfgHAType.class, "redundancyType");
    }

    /**
     * A role of application within
     * HA/redundancy group. Must be considered in association with redundancyType
     * property. Default is <code>CFGTrue </code>. Read-only (set in accordance with
     * the current role within the HA/redundancy group). See comments. See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getIsPrimary() {
        return (CfgFlag) getProperty(CfgFlag.class, "isPrimary");
    }

    /**
     * A type of application startup.
     * Indicates whether this application have to be started by Management
     * Layer. See type <code>
     * <a href="../Enumerations/CfgStartupType.html">CfgStartupType</a>
     * </code>
     * Read-only. The property
     * is accessible via API only and not shown by Configuration Manager.
     * Specified during application prototype definition automatically
     * according following: The value is associated with application type
     * <code>CfgAppType.</code> The value for the applications of <code>CFGDBServer</code> and
     * <code>CFGApplicationCluster</code> type is set to <code>CFGSUTDisabled,</code> for
     * other applications of server type is set to <code>CFGSUTAutomatic.</code> For
     * the applications of non-server type is set to <code>CFGSUTDisabled.</code>
     *
     * @return property value or null
     */
    public final CfgStartupType getStartupType() {
        return (CfgStartupType) getProperty(CfgStartupType.class, "startupType");
    }

    /**
     * A pointer to the additional arguments to be used to start
     * the application. Optional.
     *
     * @return property value or null
     */
    public final String getCommandLineArguments() {
        return (String) getProperty("commandLineArguments");
    }

    /**
     * A pointer to the list of structures of type
     * <code>
     * <a href="CfgPortInfo.html">CfgPortInfo</a>
     * </code> containing information about listening ports for this Server application.
     * When used as an entry in <code>
     * <a href="CfgDeltaApplication.html">CfgDeltaApplication</a>
     * </code>, it is a pointer to a list of port infos
     * added to the existing list.
     *
     * @return list of structures or null
     */
    public final Collection<CfgPortInfo> getAddedPortInfos() {
        return (Collection<CfgPortInfo>) getProperty("portInfos");
    }

    /**
     * A pointer to the list of the objects associated with this Application
     * (every item of this list is structured as
     * <code>
     * <a href="CfgObjectResource.html">CfgObjectResource</a>
     * </code>). When used as an entry
     * in <code>
     * <a href="CfgDeltaApplication.html">CfgDeltaApplication</a>
     * </code>, it is a pointer to a list of resources
     * added to the existing list.
     * Only objects of type <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>
     *  can be associated with Application object through <code>resources</code>
     *
     * @return list of structures or null
     */
    public final Collection<CfgObjectResource> getAddedResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
    }

    /**
     * A type of application object. Indicate whenever this is an executable process or it is a service that composed from other services and\or processes.
     *
     * @return property value or null
     */
    public final CfgAppComponentType getComponentType() {
        return (CfgAppComponentType) getProperty(CfgAppComponentType.class, "componentType");
    }

    /**
     * A pointer to the list of identifiers of the servers this
     * application can no longer be a client to.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgConnInfo> getDeletedAppServers() {
        return (Collection<CfgConnInfo>) getProperty("deletedAppServerDBIDs");
    }

    /**
     * A pointer to the list of identifiers of the servers this
     * application can no longer be a client to.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedAppServerDBIDs() {
        return getLinkListCollection("deletedAppServerDBIDs");
    }

    /**
     * A pointer to the list of identifiers of the tenants that
     * are no longer associated with this application. For applications
     * of
     * <code>CFGTServer</code> type, cannot be specified as
     * long as the application is associated with a switch (see <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>).
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgTenant> getDeletedTenants() {
        return (Collection<CfgTenant>) getProperty("deletedTenantDBIDs");
    }

    /**
     * A pointer to the list of identifiers of the tenants that
     * are no longer associated with this application. For applications
     * of
     * <code>CFGTServer</code> type, cannot be specified as
     * long as the application is associated with a switch (see <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>).
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedTenantDBIDs() {
        return getLinkListCollection("deletedTenantDBIDs");
    }

    /**
     * A pointer to the list of deleted application-specific options.
     * The structure of this parameter is described in the comments to
     * type <code>
     * <a href="CfgApplication.html">CfgApplication</a>
     * </code> above. An option is deleted by
     * specifying the name of the section that this option belongs to,
     * and the name of the option itself with any value. A whole section
     * is deleted by specifying the name of that section and an empty secondary
     * list.
     *
     * @return property value or null
     */
    public final KeyValueCollection getDeletedOptions() {
        return (KeyValueCollection) getProperty("deletedOptions");
    }

    /**
     * A pointer to the list of application-specific options whose
     * values have been changed. The structure of this parameter is described
     * in the comments to type <code>
     * <a href="CfgApplication.html">CfgApplication</a>
     * </code> above. A value
     * of an option is changed by specifying the name of the section that
     * this option belongs to, the name of the option itself, and the new
     * value of that option.
     *
     * @return property value or null
     */
    public final KeyValueCollection getChangedOptions() {
        return (KeyValueCollection) getProperty("changedOptions");
    }

    /**
     * A pointer to the list of deleted user-defined properties. Has the same structure as parameter userProperties.
     *
     * @return property value or null
     */
    public final KeyValueCollection getDeletedUserProperties() {
        return (KeyValueCollection) getProperty("deletedUserProperties");
    }

    /**
     * A pointer to the list of user-defined properties whose values have been changed. Has the same structure as parameter userProperties
     *
     * @return property value or null
     */
    public final KeyValueCollection getChangedUserProperties() {
        return (KeyValueCollection) getProperty("changedUserProperties");
    }

    /**
     * A pointer to the list of deleted options within <code>flexibleProperties</code> property.
     *
     * @return property value or null
     */
    public final KeyValueCollection getDeletedFlexibleProperties() {
        return (KeyValueCollection) getProperty("deletedFlexibleProperties");
    }

    /**
     * Should not be used. The structure of the <code>flexibleProperties</code> implies
     * only add and delete actions.
     *
     * @return property value or null
     */
    public final KeyValueCollection getChangedFlexibleProperties() {
        return (KeyValueCollection) getProperty("changedFlexibleProperties");
    }

    /**
     * A pointer to the list of structures
     * <code>
     * <a href="CfgConnInfo.html">CfgConnInfo</a>
     * </code> type. Each structure contains <code>appServerDBID</code>
     * and the information about connection parameters that have been changed.
     *
     * @return list of structures or null
     */
    public final Collection<CfgConnInfo> getChangedAppServers() {
        return (Collection<CfgConnInfo>) getProperty("changedAppServerDBIDs");
    }

    /**
     * A pointer to the list of deleted resources (every
     * item of this list is structured as <code>
     * <a href="CfgID.html">CfgID</a>
     * </code>).
     *
     * @return list of structures or null
     */
    public final Collection<CfgResourceID> getDeletedResources() {
        return (Collection<CfgResourceID>) getProperty("deletedResources");
    }

    /**
     * A pointer to the list of structures
     * <code>
     * <a href="CfgObjectResource.html">CfgObjectResource</a>
     * </code> type. Each structure contains
     * information about the resource parameters that have been changed.
     *
     * @return list of structures or null
     */
    public final Collection<CfgObjectResource> getChangedResources() {
        return (Collection<CfgObjectResource>) getProperty("changedResources");
    }

    /**
     * A pointer to the list of deleted resources (every
     * item of this list is a pointer to a string containing identifier (<code>id</code>) of the listening port structure.
     *
     * @return list of string values or null
     */
    public final Collection<CfgPortInfo> getDeletedPortInfos() {
        return (Collection<CfgPortInfo>) getProperty("deletedPortInfos");
    }

    /**
     * A pointer to the list of structures
     * <code>
     * <a href="CfgPortInfo.html">CfgPortInfo</a>
     * </code> type. Each structure contains
     * information about the listening port parameters that have been changed.
     *
     * @return list of structures or null
     */
    public final Collection<CfgPortInfo> getChangedPortInfos() {
        return (Collection<CfgPortInfo>) getProperty("changedPortInfos");
    }
}
