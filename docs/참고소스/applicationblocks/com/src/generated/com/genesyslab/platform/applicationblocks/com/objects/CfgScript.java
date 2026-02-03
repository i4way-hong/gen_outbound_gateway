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
 * <em>Scripts</em> identify processing scenarios or treatments that can be applied
 * to customer interactions.
 *
 * <p/>
 * Deletion of Script X will cause the following events
 * set out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of campaigns that had <code>scriptDBID</code> field
 * set to Script X
 * </li>
 * <li>
 * Modification of campaign groups that had <code>scriptDBID</code> field
 * set to Script X
 * </li>
 * <li>
 * Modification of calling lists that had <code>scriptDBID</code> field
 * set to Script X
 * </li>
 * <li>
 * Modification of voice prompts that had <code>scriptDBID</code> field
 * set to Script X
 * </li>
 * <li>
 * Modification of alarm conditions that had <code>scriptDBID</code> field
 * set to Script X
 * </li>
 * <li>
 * Modification of alarm conditions that had to Script X included
 * in their <code>reactionScriptDBIDs</code> or <code>clearanceScriptDBIDs</code>field
 * </li>
 * <li>
 * Modification of agents that had <code>capacityRuleDBID</code> field
 * set to Script X
 * </li>
 * <li>
 * Modification of places that had <code>capacityRuleDBID</code> field
 * set to Script X
 * </li>
 * <li>
 * Modification of agent groups that had <code>capacityRuleDBID</code> field
 * set to Script X
 * </li>
 * <li>
 * Modification of place groups that had <code>capacityRuleDBID</code> field
 * set to Script X
 * </li>
 * <li>
 * Modification of tenants that had <code>defaultCapacityRuleDBID</code> field
 * set to Script X
 * </li>
 * <li>
 * Deletion of Script X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaScript.html">
 * <b>CfgDeltaScript</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgScript
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGScript;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgScript(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgScript - "
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
    public CfgScript(
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
    public CfgScript(final IConfService confService) {
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
            if (getMetaData().getAttribute("tenantDBID") != null) {
                if (getLinkValue("tenantDBID") == null) {
                    throw new ConfigException("Mandatory property 'tenantDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("type") != null) {
                if (getProperty("type") == null) {
                    throw new ConfigException("Mandatory property 'type' not set.");
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
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
     * A pointer to the name of the script.
     * Mandatory. Must be unique within the tenant.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the name of the script.
     * Mandatory. Must be unique within the tenant.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code> this script belongs to. Mandatory. Once specified, cannot
     * be changed.
     *
     * @return instance of referred object or null
     */
    public final CfgTenant getTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "tenantDBID");
    }

    /**
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code> this script belongs to. Mandatory. Once specified, cannot
     * be changed.
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
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code> this script belongs to. Mandatory. Once specified, cannot
     * be changed.
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
     * Script index.
     *
     * @return property value or null
     */
    public final Integer getIndex() {
        return (Integer) getProperty("index");
    }

    /**
     * Script index.
     *
     * @param value new property value
     * @see #getIndex()
     */
    public final void setIndex(final Integer value) {
        setProperty("index", value);
    }

    /**
     * Type of this script. Mandatory. Once
     * specified, cannot be changed. See type <code>
     * <a href="../Enumerations/CfgScriptType.html">CfgScriptType</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgScriptType getType() {
        return (CfgScriptType) getProperty(CfgScriptType.class, "type");
    }

    /**
     * Type of this script. Mandatory. Once
     * specified, cannot be changed. See type <code>
     * <a href="../Enumerations/CfgScriptType.html">CfgScriptType</a>
     * </code>
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgScriptType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
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
     * A pointer to the list of the objects associated with this Host
     * (every item of this list is structured as
     * <code>
     * <a href="CfgObjectResource.html">CfgObjectResource</a>
     * </code>). When used as an entry
     * in <code>
     * <a href="CfgDeltaHost.html">CfgDeltaHost</a>
     * </code>, it is a pointer to a list of resources
     * added to the existing list.
     * Only objects of type <code>
     * <a href="CfgAppPrototype.html">CfgAppPrototype</a>
     * </code> and
     * <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code> and
     * can be associated with Script object through <code>resources</code>
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgObjectResource> getResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
    }

    /**
     * A pointer to the list of the objects associated with this Host
     * (every item of this list is structured as
     * <code>
     * <a href="CfgObjectResource.html">CfgObjectResource</a>
     * </code>). When used as an entry
     * in <code>
     * <a href="CfgDeltaHost.html">CfgDeltaHost</a>
     * </code>, it is a pointer to a list of resources
     * added to the existing list.
     * Only objects of type <code>
     * <a href="CfgAppPrototype.html">CfgAppPrototype</a>
     * </code> and
     * <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code> and
     * can be associated with Script object through <code>resources</code>
     *
     * @param value new property value
     * @see #getResources()
     */
    public final void setResources(final Collection<CfgObjectResource> value) {
        setProperty("resources", value);
    }
}
