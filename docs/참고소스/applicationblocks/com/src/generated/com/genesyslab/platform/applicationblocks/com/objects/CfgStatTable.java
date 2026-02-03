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
 * <em>Statistical Tables</em> are groups of Statistical Days that represent
 * statistically-modeled performances of Agent Groups over a calendar period of
 * up to one year.
 * <p/>
 * Call-processing applications can use Statistical Tables to provide load
 * balancing between Agent Groups when the real-time statistics for those groups
 * are unavailable.
 *
 * <p/>
 * Deletion of Stat Table X will cause the following events
 * set out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>capacityTableDBID</code> or <code> quotaTableDBID</code> of
 * all agent groups that were associated with Stat Table X
 * </li>
 * <li>
 * Modification of <code>capacityTableDBID</code> or <code> quotaTableDBID</code> of
 * all place groups that were associated with Stat Table X
 * </li>
 * <li>
 * Modification of <code>capacityTableDBID</code> or <code> quotaTableDBID</code> of
 * all DN groups that were associated with Stat Table X
 * </li>
 * <li>
 * Deletion of Stat Table X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaStatTable.html">
 * <b>CfgDeltaStatTable</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgStatDay.html">
 * <b>CfgStatDay</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgStatTable
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGStatTable;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgStatTable(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgStatTable - "
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
    public CfgStatTable(
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
    public CfgStatTable(final IConfService confService) {
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
            if (getMetaData().getAttribute("tenantDBID") != null) {
                if (getLinkValue("tenantDBID") == null) {
                    throw new ConfigException("Mandatory property 'tenantDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("name") != null) {
                if (getProperty("name") == null) {
                    throw new ConfigException("Mandatory property 'name' not set.");
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
     * <a href="CfgTenant.html">Tenant</a>
     * </code> that this table is defined for. Mandatory.
     * Once specified, cannot be changed.
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
     * </code> that this table is defined for. Mandatory.
     * Once specified, cannot be changed.
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
     * </code> that this table is defined for. Mandatory.
     * Once specified, cannot be changed.
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
     * A pointer to the name of the table.
     * Mandatory. Must be unique within the tenant.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the name of the table.
     * Mandatory. Must be unique within the tenant.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Type of stat table. Once specified,
     * cannot be changed. Refer to <code>
     * <a href="../Enumerations/CfgStatTableType.html">CfgStatTableType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgStatTableType getType() {
        return (CfgStatTableType) getProperty(CfgStatTableType.class, "type");
    }

    /**
     * Type of stat table. Once specified,
     * cannot be changed. Refer to <code>
     * <a href="../Enumerations/CfgStatTableType.html">CfgStatTableType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgStatTableType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
    }

    /**
     * A pointer to the list of identifiers of the <code>
     * <a href="CfgStatDay.html">Statistical Days</a>
     * </code>
     * that constitute the table. When used as an entry in <code>
     * <a href="CfgDeltaStatTable.html">CfgDeltaStatTable</a>
     * </code> (see
     * below), it is a pointer to a list of days added to the existing
     * list.
     *
     * @return list of configuration objects or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgStatDay> getStatDays() {
        return (Collection<CfgStatDay>) getProperty("statDayDBIDs");
    }

    /**
     * A pointer to the list of identifiers of the <code>
     * <a href="CfgStatDay.html">Statistical Days</a>
     * </code>
     * that constitute the table. When used as an entry in <code>
     * <a href="CfgDeltaStatTable.html">CfgDeltaStatTable</a>
     * </code> (see
     * below), it is a pointer to a list of days added to the existing
     * list.
     *
     * @param value new property value
     * @see #getStatDays()
     */
    public final void setStatDays(final Collection<CfgStatDay> value) {
        setProperty("statDayDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the StatDays property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getStatDayDBIDs() {
        return getLinkListCollection("statDayDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the StatDays property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setStatDayDBIDs(final Collection<Integer> value) {
        setProperty("statDayDBIDs", value);
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
     * The time the Universal Routing Server can wait until the corresponding resources become available.
     *
     * @return property value or null
     */
    public final Integer getWaitThreshold() {
        return (Integer) getProperty("waitThreshold");
    }

    /**
     * The time the Universal Routing Server can wait until the corresponding resources become available.
     *
     * @param value new property value
     * @see #getWaitThreshold()
     */
    public final void setWaitThreshold(final Integer value) {
        setProperty("waitThreshold", value);
    }

    /**
     * A flat rate measured in cost units.
     *
     * @return property value or null
     */
    public final Integer getFlatRate() {
        return (Integer) getProperty("flatRate");
    }

    /**
     * A flat rate measured in cost units.
     *
     * @param value new property value
     * @see #getFlatRate()
     */
    public final void setFlatRate(final Integer value) {
        setProperty("flatRate", value);
    }

    /**
     * The hourly rate for an agent. This value is used only for Variable Rate Contracts.
     *
     * @return property value or null
     */
    public final Integer getAgentHourlyRate() {
        return (Integer) getProperty("agentHourlyRate");
    }

    /**
     * The hourly rate for an agent. This value is used only for Variable Rate Contracts.
     *
     * @param value new property value
     * @see #getAgentHourlyRate()
     */
    public final void setAgentHourlyRate(final Integer value) {
        setProperty("agentHourlyRate", value);
    }

    /**
     * Flag determining whether flatRate (<code>CFGTrue</code>) or agentHourlyRate (<code>CFGFalse</code>) should be selected.
     * Refer to <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getUseFlatRate() {
        return (CfgFlag) getProperty(CfgFlag.class, "useFlatRate");
    }

    /**
     * Flag determining whether flatRate (<code>CFGTrue</code>) or agentHourlyRate (<code>CFGFalse</code>) should be selected.
     * Refer to <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getUseFlatRate()
     */
    public final void setUseFlatRate(final CfgFlag value) {
        setProperty("useFlatRate", value);
    }
}
