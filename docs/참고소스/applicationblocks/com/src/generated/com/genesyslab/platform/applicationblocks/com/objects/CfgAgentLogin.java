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
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionStructure;

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import javax.annotation.Generated;

import java.util.Calendar;
import java.util.Collection;

import org.w3c.dom.Node;


/**
 * <p/>
 * <em>Agent Logins</em> are unique codes defined within a Switch and assigned to
 * agents. They identify which Agent is working at which Place during a particular
 * working session.
 * <p/>
 * Configuration of Agent Logins in the Configuration Database must exactly match
 * the configuration of those Agent Logins in the switching system. Before adding
 * or deleting a particular Agent Login, make sure that the same change was made
 * in the database of the switching system.
 * <p/>
 * When you specify Agent Logins as objects in a Switch, they are not associated
 * with any particular agents.
 *
 * <p/>
 * Deletion of Agent Login X will cause the following events
 * set out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>agentLogins</code> of the agent who
 * had Agent Login X assigned
 * </li>
 * <li>
 * Deletion of Agent Login X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaAgentLogin.html">
 * <b>CfgDeltaAgentLogin</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgSwitch.html">
 * <b>CfgSwitch</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgAgentLogin
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGAgentLogin;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgAgentLogin(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgAgentLogin - "
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
    public CfgAgentLogin(
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
    public CfgAgentLogin(final IConfService confService) {
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
            if (getMetaData().getAttribute("switchDBID") != null) {
                if (getLinkValue("switchDBID") == null) {
                    throw new ConfigException("Mandatory property 'switchDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("loginCode") != null) {
                if (getProperty("loginCode") == null) {
                    throw new ConfigException("Mandatory property 'loginCode' not set.");
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
                }
            }
            if (getMetaData().getAttribute("useOverride") != null) {
                if (getProperty("useOverride") == null) {
                    setProperty("useOverride", 2);
                }
            }
            if (getMetaData().getAttribute("switchSpecificType") != null) {
                if (getProperty("switchSpecificType") == null) {
                    throw new ConfigException("Mandatory property 'switchSpecificType' not set.");
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
     * A unique identifier of the
     * <code>
     * <a href="CfgSwitch.html">Switch</a>
     * </code> to which this agent login belongs. Mandatory. Once specified,
     * cannot be changed.
     *
     * @return instance of referred object or null
     */
    public final CfgSwitch getSwitch() {
        return (CfgSwitch) getProperty(CfgSwitch.class, "switchDBID");
    }

    /**
     * A unique identifier of the
     * <code>
     * <a href="CfgSwitch.html">Switch</a>
     * </code> to which this agent login belongs. Mandatory. Once specified,
     * cannot be changed.
     *
     * @param value new property value
     * @see #getSwitch()
     */
    public final void setSwitch(final CfgSwitch value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("switchDBID", value);
    }

    /**
     * A unique identifier of the
     * <code>
     * <a href="CfgSwitch.html">Switch</a>
     * </code> to which this agent login belongs. Mandatory. Once specified,
     * cannot be changed.
     *
     * @param dbid DBID identifier of referred object
     * @see #getSwitch()
     */
    public final void setSwitchDBID(final int dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("switchDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Switch property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getSwitchDBID() {
        return getLinkValue("switchDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code>
     * to which this agent login belongs. Read-only (set automatically
     * according to the current value of
     * <code>tenantDBID</code> of the switch specified in <code>switchDBID</code>).
     * See type <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>.
     *
     * @return instance of referred object or null
     */
    public final CfgTenant getTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "tenantDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code>
     * to which this agent login belongs. Read-only (set automatically
     * according to the current value of
     * <code>tenantDBID</code> of the switch specified in <code>switchDBID</code>).
     * See type <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>.
     *
     * @param value new property value
     * @see #getTenant()
     */
    public final void setTenant(final CfgTenant value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("tenantDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code>
     * to which this agent login belongs. Read-only (set automatically
     * according to the current value of
     * <code>tenantDBID</code> of the switch specified in <code>switchDBID</code>).
     * See type <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>.
     *
     * @param dbid DBID identifier of referred object
     * @see #getTenant()
     */
    public final void setTenantDBID(final int dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("tenantDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Tenant property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getTenantDBID() {
        return getLinkValue("tenantDBID");
    }

    /**
     * A pointer to the agent login
     * code. Mandatory. Must be unique within the switch. Once specified,
     * cannot be changed.
     *
     * @return property value or null
     */
    public final String getLoginCode() {
        return (String) getProperty("loginCode");
    }

    /**
     * A pointer to the agent login
     * code. Mandatory. Must be unique within the switch. Once specified,
     * cannot be changed.
     *
     * @param value new property value
     * @see #getLoginCode()
     */
    public final void setLoginCode(final String value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("loginCode", value);
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

    public final KeyValueCollection getUserProperties() {
        return (KeyValueCollection) getProperty("userProperties");
    }

    public final void setUserProperties(final KeyValueCollection value) {
        setProperty("userProperties", value);
    }

    /**
     * The number used as a substitute
     * of a regular agent login in certain types of routing.
     *
     * @return property value or null
     */
    public final String getOverride() {
        return (String) getProperty("override");
    }

    /**
     * The number used as a substitute
     * of a regular agent login in certain types of routing.
     *
     * @param value new property value
     * @see #getOverride()
     */
    public final void setOverride(final String value) {
        setProperty("override", value);
    }

    /**
     * An indicator of whether the
     * <code>override</code> value shall be used instead of
     * the <code>loginCode</code> value for accessing this agent login
     * in certain types of routing. Recommended to be set to <code>CFGTrue</code> by default.
     * See type <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgFlag getUseOverride() {
        return (CfgFlag) getProperty(CfgFlag.class, "useOverride");
    }

    /**
     * An indicator of whether the
     * <code>override</code> value shall be used instead of
     * the <code>loginCode</code> value for accessing this agent login
     * in certain types of routing. Recommended to be set to <code>CFGTrue</code> by default.
     * See type <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>.
     *
     * @param value new property value
     * @see #getUseOverride()
     */
    public final void setUseOverride(final CfgFlag value) {
        setProperty("useOverride", value);
    }

    /**
     * An integer that corresponds to a combination of switch-specific
     * settings for this agent login. Cannot be set to a zero or negative
     * value.
     *
     * @return property value or null
     */
    public final Integer getSwitchSpecificType() {
        return (Integer) getProperty("switchSpecificType");
    }

    /**
     * An integer that corresponds to a combination of switch-specific
     * settings for this agent login. Cannot be set to a zero or negative
     * value.
     *
     * @param value new property value
     * @see #getSwitchSpecificType()
     */
    public final void setSwitchSpecificType(final Integer value) {
        setProperty("switchSpecificType", value);
    }

    /**
     * A pointer to the agent login password.
     *
     * @return property value or null
     */
    public final String getPassword() {
        return (String) getProperty("password");
    }

    /**
     * A pointer to the agent login password.
     *
     * @param value new property value
     * @see #getPassword()
     */
    public final void setPassword(final String value) {
        setProperty("password", value);
    }
}
