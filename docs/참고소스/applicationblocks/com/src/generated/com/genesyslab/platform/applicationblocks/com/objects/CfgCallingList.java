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
 * <em>Calling Lists</em> are references to tables of information about the numbers
 * to call during an outbound campaign. These objects also specify conditions that
 * Outbound Contact applications observe when working with these Calling Lists.
 *
 * <p/>
 * Deletion of Calling List X will cause the following
 * events set out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * modification of <code>callingLists</code> of all campaigns
 * that included Calling List X
 * </li>
 * <li>
 * deletion of Calling List X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaCallingList.html">
 * <b>CfgDeltaCallingList</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgTableAccess.html">
 * <b>CfgTableAccess</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgFilter.html">
 * <b>CfgFilter</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgCampaign.html">
 * <b>CfgCampaign</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgCallingList
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGCallingList;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgCallingList(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgCallingList - "
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
    public CfgCallingList(
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
    public CfgCallingList(final IConfService confService) {
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
            if (getMetaData().getAttribute("tableAccessDBID") != null) {
                if (getLinkValue("tableAccessDBID") == null) {
                    throw new ConfigException("Mandatory property 'tableAccessDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("maxAttempts") != null) {
                if (getProperty("maxAttempts") == null) {
                    setProperty("maxAttempts", 10);
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
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this calling list is allocated. Mandatory. Once specified,
     * cannot be changed.
     *
     * @return instance of referred object or null
     */
    public final CfgTenant getTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "tenantDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this calling list is allocated. Mandatory. Once specified,
     * cannot be changed.
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
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this calling list is allocated. Mandatory. Once specified,
     * cannot be changed.
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
     * A pointer to the calling list name.
     * Mandatory. Must be unique within the Configuration Database.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the calling list name.
     * Mandatory. Must be unique within the Configuration Database.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * A pointer to the calling list
     * description.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A pointer to the calling list
     * description.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * A unique identifier of the table the calling list refers
     * to. Reference to <code>
     * <a href="CfgTableAccess.html">CfgTableAccess</a>
     * </code> object of <code>CFGTTCallingList</code>
     * type can be used only. Mandatory.
     *
     * @return instance of referred object or null
     */
    public final CfgTableAccess getTableAccess() {
        return (CfgTableAccess) getProperty(CfgTableAccess.class, "tableAccessDBID");
    }

    /**
     * A unique identifier of the table the calling list refers
     * to. Reference to <code>
     * <a href="CfgTableAccess.html">CfgTableAccess</a>
     * </code> object of <code>CFGTTCallingList</code>
     * type can be used only. Mandatory.
     *
     * @param value new property value
     * @see #getTableAccess()
     */
    public final void setTableAccess(final CfgTableAccess value) {
        setProperty("tableAccessDBID", value);
    }

    /**
     * A unique identifier of the table the calling list refers
     * to. Reference to <code>
     * <a href="CfgTableAccess.html">CfgTableAccess</a>
     * </code> object of <code>CFGTTCallingList</code>
     * type can be used only. Mandatory.
     *
     * @param dbid DBID identifier of referred object
     * @see #getTableAccess()
     */
    public final void setTableAccessDBID(final int dbid) {
        setProperty("tableAccessDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the TableAccess property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getTableAccessDBID() {
        return getLinkValue("tableAccessDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFilter.html">CfgFilter</a>
     * </code>
     * of this calling list. Optional. If specified, the filter format
     * must have reference to the format the table access refers to.
     *
     * @return instance of referred object or null
     */
    public final CfgFilter getFilter() {
        return (CfgFilter) getProperty(CfgFilter.class, "filterDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFilter.html">CfgFilter</a>
     * </code>
     * of this calling list. Optional. If specified, the filter format
     * must have reference to the format the table access refers to.
     *
     * @param value new property value
     * @see #getFilter()
     */
    public final void setFilter(final CfgFilter value) {
        setProperty("filterDBID", value);
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFilter.html">CfgFilter</a>
     * </code>
     * of this calling list. Optional. If specified, the filter format
     * must have reference to the format the table access refers to.
     *
     * @param dbid DBID identifier of referred object
     * @see #getFilter()
     */
    public final void setFilterDBID(final int dbid) {
        setProperty("filterDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Filter property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getFilterDBID() {
        return getLinkValue("filterDBID");
    }

    /**
     * A pointer to list of identifiers of <code>
     * <a href="CfgTreatment.html">CfgTreatment</a>
     * </code> dedicated to the calling
     * list. Optional.
     *
     * @return list of configuration objects or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgTreatment> getTreatments() {
        return (Collection<CfgTreatment>) getProperty("treatmentDBIDs");
    }

    /**
     * A pointer to list of identifiers of <code>
     * <a href="CfgTreatment.html">CfgTreatment</a>
     * </code> dedicated to the calling
     * list. Optional.
     *
     * @param value new property value
     * @see #getTreatments()
     */
    public final void setTreatments(final Collection<CfgTreatment> value) {
        setProperty("treatmentDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the Treatments property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getTreatmentDBIDs() {
        return getLinkListCollection("treatmentDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the Treatments property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setTreatmentDBIDs(final Collection<Integer> value) {
        setProperty("treatmentDBIDs", value);
    }

    /**
     * A unique identifier of logTableAccess. It is recommended
     * to dedicate single logTableAccessDBID to all CallingLists associated
     * with one Campaign. . Reference to <code>
     * <a href="CfgTableAccess.html">CfgTableAccess</a>
     * </code> object of
     * <code>CFGTTLogTable</code> type can be used only. Optional.
     *
     * @return instance of referred object or null
     */
    public final CfgTableAccess getLogTableAccess() {
        return (CfgTableAccess) getProperty(CfgTableAccess.class, "logTableAccessDBID");
    }

    /**
     * A unique identifier of logTableAccess. It is recommended
     * to dedicate single logTableAccessDBID to all CallingLists associated
     * with one Campaign. . Reference to <code>
     * <a href="CfgTableAccess.html">CfgTableAccess</a>
     * </code> object of
     * <code>CFGTTLogTable</code> type can be used only. Optional.
     *
     * @param value new property value
     * @see #getLogTableAccess()
     */
    public final void setLogTableAccess(final CfgTableAccess value) {
        setProperty("logTableAccessDBID", value);
    }

    /**
     * A unique identifier of logTableAccess. It is recommended
     * to dedicate single logTableAccessDBID to all CallingLists associated
     * with one Campaign. . Reference to <code>
     * <a href="CfgTableAccess.html">CfgTableAccess</a>
     * </code> object of
     * <code>CFGTTLogTable</code> type can be used only. Optional.
     *
     * @param dbid DBID identifier of referred object
     * @see #getLogTableAccess()
     */
    public final void setLogTableAccessDBID(final int dbid) {
        setProperty("logTableAccessDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the LogTableAccess property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getLogTableAccessDBID() {
        return getLinkValue("logTableAccessDBID");
    }

    /**
     * An earliest time when dial can
     * be done. The value is measured in seconds since 00:00:00 of current
     * day. Default value is 08:00:00. Overwrites the settings in database
     * table specified in field CFGFrom. Mandatory.
     *
     * @return property value or null
     */
    public final Integer getTimeFrom() {
        return (Integer) getProperty("timeFrom");
    }

    /**
     * An earliest time when dial can
     * be done. The value is measured in seconds since 00:00:00 of current
     * day. Default value is 08:00:00. Overwrites the settings in database
     * table specified in field CFGFrom. Mandatory.
     *
     * @param value new property value
     * @see #getTimeFrom()
     */
    public final void setTimeFrom(final Integer value) {
        setProperty("timeFrom", value);
    }

    /**
     * An latest time when dial can
     * be done. The value is measured in seconds since 00:00:00 of current
     * day. Default value is 18:00:00. Overwrites the settings in database
     * table specified in field CFGUntil. Mandatory.
     *
     * @return property value or null
     */
    public final Integer getTimeUntil() {
        return (Integer) getProperty("timeUntil");
    }

    /**
     * An latest time when dial can
     * be done. The value is measured in seconds since 00:00:00 of current
     * day. Default value is 18:00:00. Overwrites the settings in database
     * table specified in field CFGUntil. Mandatory.
     *
     * @param value new property value
     * @see #getTimeUntil()
     */
    public final void setTimeUntil(final Integer value) {
        setProperty("timeUntil", value);
    }

    /**
     * A maximum number of attempts
     * the single record can be dialed for one campaign (total). Default
     * value is 10. Mandatory.
     *
     * @return property value or null
     */
    public final Integer getMaxAttempts() {
        return (Integer) getProperty("maxAttempts");
    }

    /**
     * A maximum number of attempts
     * the single record can be dialed for one campaign (total). Default
     * value is 10. Mandatory.
     *
     * @param value new property value
     * @see #getMaxAttempts()
     */
    public final void setMaxAttempts(final Integer value) {
        setProperty("maxAttempts", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>
     * for this calling list. Optional.
     *
     * @return instance of referred object or null
     */
    public final CfgScript getScript() {
        return (CfgScript) getProperty(CfgScript.class, "scriptDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>
     * for this calling list. Optional.
     *
     * @param value new property value
     * @see #getScript()
     */
    public final void setScript(final CfgScript value) {
        setProperty("scriptDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>
     * for this calling list. Optional.
     *
     * @param dbid DBID identifier of referred object
     * @see #getScript()
     */
    public final void setScriptDBID(final int dbid) {
        setProperty("scriptDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Script property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getScriptDBID() {
        return getLinkValue("scriptDBID");
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
}
