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
import com.genesyslab.platform.applicationblocks.com.runtime.ProtocolOperationsHelper;
import com.genesyslab.platform.configuration.protocol.obj.*;
import com.genesyslab.platform.configuration.protocol.types.*;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionStructure;

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import javax.annotation.Generated;

import java.util.Calendar;
import java.util.Collection;

import org.w3c.dom.Node;


/**
 * <p/>
 * <em>Applications</em> are the various Genesys software programs that serve a
 * contact center. There are two types of Applications: GUI-based Applications and
 * daemon Applications.
 *
 * <p/>
 * Deletion of Application(Server) X will cause the following
 * events set out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of detect event of the alarm conditions which
 * referred on Application X
 * </li>
 * <li>
 * Modification of <code>backupServerDBID</code> field of the
 * application that had Application X as a backup server
 * </li>
 * <li>
 * Modification of <code>appServerDBIDs</code> field of all applications
 * that had Application X in their connections
 * </li>
 * <li>
 * Modification of campaign groups of all campaigns that had <code>statServerDBID</code> or
 * <code>dialerDBID</code> fields set to Application X
 * </li>
 * <li>
 * Modification of IVR objects that had <code>IVRServerDBID</code> field
 * set to Application X
 * </li>
 * <li>
 * Modification of solution and host objects that had <code>SCSDBID</code> field
 * set to Application X
 * </li>
 * <li>
 * Deletion of Application X
 * </li>
 * </ul>
 * <p/>
 * An application/server cannot be deleted as long as it associated
 * with at least one client application i.e., the connection between
 * client and server is specified within configuration object or it
 * is included to at least one non-optional solution component, or
 * it is assigned as DBServer to at least one table access.
 * <p/>
 * Parameter <code>options</code> has the following structure:
 * Each key-value pair of the primary list (
 * <code>TKVList *options</code>) uses the key for the name
 * of a configuration section, and the value for a secondary list,
 * that also has the <code>TKVList</code>
 * structure and specifies the configuration options defined within
 * that section. Each key-value pair of the secondary list uses the
 * key for the name of a configuration option, and the value for its
 * current setting. Configuration options can be defined as variables
 * of integer, character, or binary type. Names of sections must be
 * unique within the primary list. Names of options must be unique
 * within the secondary list.
 * <br/><b>Note:</b><br/>
 * Configuration Server is not concerned with logical meanings
 * of application-specific configuration sections, options, or their
 * values.
 * <br/>
 * <p/>
 * Applications of the daemon type are allowed to establish
 * one and only one communication session to Configuration Server.
 * <p/>
 * Access privileges of an application of the daemon type are
 * determined by the access privileges of the account it is associated
 * with. By default, a new application of the daemon type is associated
 * with access group <code>System</code> (see comments to
 * <code>CfgAccessGroup</code> in section<code> Access Control
 * Functions and Data Types</code>). Function <code>ConfSetAccount</code>
 * can be used to change the default account.
 * <p/>
 * Access privileges of an application of the GUI type are determined
 * by the access privileges of the currently logged-on person. See
 * comments to <code>
 * <a href="CfgPerson.html">CfgPerson</a>
 * </code>.
 * <p/>
 * An application is allowed to establish a communication session
 * with Configuration Server only if the currently logged-on person
 * (for GUI applications) or the account (for daemon applications)
 * has <code>Execute</code> permission with respect to this Application
 * (see type <code>CfgACE</code>).
 * <p/>
 * An application of <code>CFGConfigServer</code> type with
 * <code>DBID</code> = <code>99</code>
 * shall be pre-defined (scripted) in the Configuration Database before Configuration
 * Server is started for the first time. The object that represents
 * this application cannot be deleted.
 * <p/>
 * An application of <code>CFGSCE</code> type with
 * <code>DBID</code> = <code>100</code>
 * shall be pre-defined (scripted) in the Configuration Database before Configuration
 * Server is started for the first time. The object that represents
 * this application cannot be deleted.
 * <p/>
 * An application can be included into different solutions. Configuration
 * Server does not provide the synchronization property <code>tenantDBID</code> of
 * CfgService and property
 * <code>tenantDBIDs</code> of CfgApplication. The <code>tenantDBIDs</code>
 * list should be updated manually or by wizard every time a solution
 * the application is a part of is assigned to a new tenant (the corresponding
 * tenant's id should be added to the list). Similar (manual or by
 * wizard) update should be made if a solution the application is included
 * into is no longer associated with a tenant (the corresponding tenant's
 * id should be removed from the list).
 * <p/>
 * An application can not be deleted as long as there is at least
 * one solution the application is a part of.
 * <p/>
 * After upgrading from CME 5.1.x to 6.1 the following default
 * values should be set for the application:
 * <ul type="bullet">
 * <li>
 * <code>workDirectory = '.'</code>
 * </li>
 * <li>
 * <code>commandLine = name</code> (the value of name property)
 * </li>
 * <li>
 * <code>autoRestart = CFGFalse</code>
 * </li>
 * <li>
 * <code>startupTimeout = 90</code>
 * </li>
 * <li>
 * <code>redundancyType=CFGHTColdStandby</code>
 * </li>
 * <li>
 * <code>isPrimary=CFGTrue</code>
 * </li>
 * <li>
 * <code>startupType= </code>&lt;should be set according to description
 * of <code>startupType</code> above&gt;
 * </li>
 * </ul>
 * <p/>
 * An application cannot be deleted if it has a type <code>CFGITCUtility</code> (53)
 * <p/>
 * The name of the application can not be changed if there is,
 * at least, one active client exist registered under this name
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaApplication.html">
 * <b>CfgDeltaApplication</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgHost.html">
 * <b>CfgHost</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgApplication
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGApplication;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgApplication(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgApplication - "
                    + objData.getObjectType());
        }
    }

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param xmlData XML object containing object data
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgApplication(
            final IConfService confService,
            final Node xmlData,
            final Object[] additionalParameters) {
        super(confService, xmlData, additionalParameters);
    }

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgApplication(final IConfService confService) {
        super(confService,
                new ConfObject(confService.getMetaData()
                        .getClassById(OBJECT_TYPE.ordinal())),
                false, null);
    }


    /**
     * Synchronizes changes in the class with Configuration Server.
     *
     * @throws ConfigException in case of protocol level exception, data transformation,
     *                         or server side constraints
     */
    @Override
    @SuppressWarnings("unchecked")
    public void save() throws ConfigException {
        if ((getConfigurationService() != null)
            && getConfigurationService().getPolicy()
                   .getValidateBeforeSave()) {
            if (getMetaData().getAttribute("name") != null) {
                if (getProperty("name") == null) {
                    throw new ConfigException("Mandatory property 'name' not set.");
                }
            }
            if (getMetaData().getAttribute("appServerDBIDs") != null) {
                Collection<CfgConnInfo> appServerDBIDs = (Collection<CfgConnInfo>) getProperty("appServerDBIDs");
                if (appServerDBIDs != null) {
                    for (CfgConnInfo item : appServerDBIDs) {
                        item.checkPropertiesForSave();
                    }
                }
            }
            if (getMetaData().getAttribute("serverInfo") != null) {
                CfgServer serverInfo = (CfgServer) getProperty("serverInfo");
                if (serverInfo != null) {
                    serverInfo.checkPropertiesForSave();
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
                }
            }
            if (getMetaData().getAttribute("portInfos") != null) {
                Collection<CfgPortInfo> portInfos = (Collection<CfgPortInfo>) getProperty("portInfos");
                if (portInfos != null) {
                    for (CfgPortInfo item : portInfos) {
                        item.checkPropertiesForSave();
                    }
                }
            }
            if (getMetaData().getAttribute("resources") != null) {
                Collection<CfgObjectResource> resources = (Collection<CfgObjectResource>) getProperty("resources");
                if (resources != null) {
                    for (CfgObjectResource item : resources) {
                        item.checkPropertiesForSave();
                    }
                }
            }
        }
        super.save();
    }

    /**
     * An identifier of this object in the Configuration Database.
     * Generated by Configuration Server and is unique within an object type.
     * Identifiers of deleted objects are not used again. Read-only.
     *
     * @return property value or null
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * An identifier of this object in the Configuration Database.
     * Generated by Configuration Server and is unique within an object type.
     * Identifiers of deleted objects are not used again. Read-only.
     *
     * @param value new property value
     * @see #getDBID()
     */
    public final void setDBID(final Integer value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("DBID", value);
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
     * A pointer to the name of the application.
     * Mandatory. Must be unique within the Configuration Database.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
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
     * A pointer to the application password.
     * Max length 64 symbols. Not used in 5.1.
     *
     * @param value new property value
     * @see #getPassword()
     */
    public final void setPassword(final String value) {
        setProperty("password", value);
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
     * Type of the application. Mandatory.
     * Set automatically according to the value of
     * <code>type</code> of the application prototype specified
     * in <code>appPrototypeDBID</code> or explicitly during the creation
     * time. Once specified, cannot be changed. See <code>
     * <a href="../Enumerations/CfgAppType.html">CfgAppType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgAppType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
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
     * A pointer to the application version.
     * Mandatory. Set automatically according to the value of
     * <code>version</code> of the application prototype specified
     * in <code>appPrototypeDBID</code> or explicitly.
     *
     * @param value new property value
     * @see #getVersion()
     */
    public final void setVersion(final String value) {
        setProperty("version", value);
    }

    /**
     * A pointer to the list of structures of type
     * <code>
     * <a href="CfgConnInfo.html">CfgConnInfo</a>
     * </code>.
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgConnInfo> getAppServers() {
        return (Collection<CfgConnInfo>) getProperty("appServerDBIDs");
    }

    /**
     * A pointer to the list of structures of type
     * <code>
     * <a href="CfgConnInfo.html">CfgConnInfo</a>
     * </code>.
     *
     * @param value new property value
     * @see #getAppServers()
     */
    public final void setAppServers(final Collection<CfgConnInfo> value) {
        setProperty("appServerDBIDs", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgTenant> getTenants() {
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
     * @param value new property value
     * @see #getTenants()
     */
    public final void setTenants(final Collection<CfgTenant> value) {
        setProperty("tenantDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the Tenants property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getTenantDBIDs() {
        return getLinkListCollection("tenantDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the Tenants property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setTenantDBIDs(final Collection<Integer> value) {
        setProperty("tenantDBIDs", value);
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
     * A pointer to the structure containing
     * server-specific information. Can be specified if, according to the
     * value specified for the
     * <code>type</code> above, the application is a daemon
     * and must be set to <code>NULL</code> otherwise. Once specified,
     * cannot be set to <code>NULL</code>. See <code>
     * <a href="CfgServer.html">CfgServer</a>
     * </code>.
     *
     * @param value new property value
     * @see #getServerInfo()
     */
    public final void setServerInfo(final CfgServer value) {
        setProperty("serverInfo", value);
    }

    /**
     * A pointer to the list of application-specific configuration
     * options (see the comments below). When used as an entry in <code>CfgDeltaApplication</code>,
     * it is a pointer to a list of options added to the existing list.
     *
     * @return property value or null
     */
    public final KeyValueCollection getOptions() {
        return (KeyValueCollection) getProperty("options");
    }

    /**
     * A pointer to the list of application-specific configuration
     * options (see the comments below). When used as an entry in <code>CfgDeltaApplication</code>,
     * it is a pointer to a list of options added to the existing list.
     *
     * @param value new property value
     * @see #getOptions()
     */
    public final void setOptions(final KeyValueCollection value) {
        setProperty("options", value);
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
     * Current object state. Mandatory. Refer to <code>
     * <a href="../Enumerations/CfgObjectState.html">CfgObjectState</a>
     * </code>
     *
     * @param value new property value
     * @see #getState()
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * A pointer to the list of user-defined properties.Parameter userProperties has the following structure: Each key-value pair of the primary list (TKVList *userProperties) uses the key for the name of a user-defined section, and the value for a secondary list, that also has the TKVList structure and specifies the properties defined within that section.
     *
     * @return property value or null
     */
    public final KeyValueCollection getUserProperties() {
        return (KeyValueCollection) getProperty("userProperties");
    }

    /**
     * A pointer to the list of user-defined properties.Parameter userProperties has the following structure: Each key-value pair of the primary list (TKVList *userProperties) uses the key for the name of a user-defined section, and the value for a secondary list, that also has the TKVList structure and specifies the properties defined within that section.
     *
     * @param value new property value
     * @see #getUserProperties()
     */
    public final void setUserProperties(final KeyValueCollection value) {
        setProperty("userProperties", value);
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
     * A unique identifier of an application prototype this application
     * is based on. Optional. See <code>
     * <a href="CfgAppPrototype.html">CfgAppPrototype</a>
     * </code>. The association
     * with application prototype could be specified at moment of creation
     * of application object only.
     *
     * @param value new property value
     * @see #getAppPrototype()
     */
    public final void setAppPrototype(final CfgAppPrototype value) {
        setProperty("appPrototypeDBID", value);
    }

    /**
     * A unique identifier of an application prototype this application
     * is based on. Optional. See <code>
     * <a href="CfgAppPrototype.html">CfgAppPrototype</a>
     * </code>. The association
     * with application prototype could be specified at moment of creation
     * of application object only.
     *
     * @param dbid DBID identifier of referred object
     * @see #getAppPrototype()
     */
    public final void setAppPrototypeDBID(final int dbid) {
        setProperty("appPrototypeDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the AppPrototype property.
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
    public final KeyValueCollection getFlexibleProperties() {
        return (KeyValueCollection) getProperty("flexibleProperties");
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
     * @param value new property value
     * @see #getFlexibleProperties()
     */
    public final void setFlexibleProperties(final KeyValueCollection value) {
        setProperty("flexibleProperties", value);
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
     * Working directory for the
     * application. Must be specified if, according to the value specified
     * in <code>isServer</code> property, the application is a server and
     * optional otherwise. See comments.
     *
     * @param value new property value
     * @see #getWorkDirectory()
     */
    public final void setWorkDirectory(final String value) {
        setProperty("workDirectory", value);
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
     * The name of executable to be
     * used to start the application. Must be specified if, according to
     * the value specified in <code>isServer</code> property, the application
     * is a server and optional otherwise. See comments.
     *
     * @param value new property value
     * @see #getCommandLine()
     */
    public final void setCommandLine(final String value) {
        setProperty("commandLine", value);
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
     * Indicates whether the application
     * should be automatically restarted by Local Control Agent after its
     * crash. Mandatory. Recommended to be set to <code>CFGTrue</code> by
     * default. See comments. See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getAutoRestart()
     */
    public final void setAutoRestart(final CfgFlag value) {
        setProperty("autoRestart", value);
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
     * to be completely started. Must be specified if, according to the
     * value specified in <code>isServer</code> property, the application
     * is a server and optional otherwise. Default value is 90 seconds.
     * See comments.
     *
     * @param value new property value
     * @see #getStartupTimeout()
     */
    public final void setStartupTimeout(final Integer value) {
        setProperty("startupTimeout", value);
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
     * A period of time within which the application is expected
     * to be completely shut down. Must be specified if, according to the
     * value specified in <code>isServer</code> property, the application
     * is a server and optional otherwise. Default value is 90 seconds.
     * See comments.
     *
     * @param value new property value
     * @see #getShutdownTimeout()
     */
    public final void setShutdownTimeout(final Integer value) {
        setProperty("shutdownTimeout", value);
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
     * Defines the HA type if this application is considered as
     * server. Mandatory. Default is <code>CFGHTColdStandby.</code> See
     * comments. See <code>
     * <a href="../Enumerations/CfgHAType.html">CfgHAType</a>
     * </code>
     *
     * @param value new property value
     * @see #getRedundancyType()
     */
    public final void setRedundancyType(final CfgHAType value) {
        setProperty("redundancyType", value);
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
     * A role of application within
     * HA/redundancy group. Must be considered in association with redundancyType
     * property. Default is <code>CFGTrue </code>. Read-only (set in accordance with
     * the current role within the HA/redundancy group). See comments. See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getIsPrimary()
     */
    public final void setIsPrimary(final CfgFlag value) {
        setProperty("isPrimary", value);
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
     * A pointer to the additional arguments to be used to start
     * the application. Optional.
     *
     * @param value new property value
     * @see #getCommandLineArguments()
     */
    public final void setCommandLineArguments(final String value) {
        setProperty("commandLineArguments", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgPortInfo> getPortInfos() {
        return (Collection<CfgPortInfo>) getProperty("portInfos");
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
     * @param value new property value
     * @see #getPortInfos()
     */
    public final void setPortInfos(final Collection<CfgPortInfo> value) {
        setProperty("portInfos", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgObjectResource> getResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
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
     * @param value new property value
     * @see #getResources()
     */
    public final void setResources(final Collection<CfgObjectResource> value) {
        setProperty("resources", value);
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
     * A type of application object. Indicate whenever this is an executable process or it is a service that composed from other services and\or processes.
     *
     * @param value new property value
     * @see #getComponentType()
     */
    public final void setComponentType(final CfgAppComponentType value) {
        setProperty("componentType", value);
    }


    /**
     * Reads "Logon As" account of this application. 
     * Applicable only for the Server type applications.
     * @return object that identifies logon account for this application     
     * @throws ConfigException if logon account can't be read from server
     */
	public CfgACEID retrieveLogonAs() throws ConfigException {
		IConfService service = getConfigurationService();
		if (service == null)
			throw new ConfigException(
					String.format("CfgApplication %s has no ConfService to read Logon As account", this.getName()));
		return ProtocolOperationsHelper.getLogonAs(service, this);
	}

	/**
	 * Updates "Logon As" account for this application. Operation is applicable for the server applications.
	 * Account type can be either CfgPerson or CfgAccessGroup.
	 * The CfgAccessGroup could be used to set "Logon As" under the SYSTEM account:
	 * <code>
	 *   application.updateLogonAs(CfgObjectType.CFGAccessGroup, WellKnownDBIDs.SystemDBID);
	 * </code> 
	 * @param accountType type of the account object: CFGPerson or CFGAccessGroup
	 * @param accountDbid account object's DBID
	 * @throws IllegalArgumentException if accountDBID is zero or negative, if accountType null or has unexpected value
	 * @throws ConfigException if account wasn't updated
	 */
	public void updateLogonAs(CfgObjectType accountType, int accountDbid)
			throws ConfigException {
		IConfService service = getConfigurationService();
		if (service == null)
			throw new ConfigException(
					String.format("CfgApplication %s has no ConfService to update Logon As account", this.getName()));
		ProtocolOperationsHelper.setLogonAs(getConfigurationService(), this, accountType, accountDbid);
	}

    }
