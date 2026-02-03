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
 * <code>CfgDeltaCallingList</code> is applicable for
 * Configuration Library/Server release 5.1.5xx and later.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDeltaCallingList extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaCallingList(final IConfService confService) {
        super(confService, "CfgDeltaCallingList");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaCallingList(
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
    public CfgDeltaCallingList(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgCallingList configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgCallingList configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgCallingList retrieveCfgCallingList() throws ConfigException {
        return (CfgCallingList) (super.retrieveObject());
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
     * Retrieves the dbid of the object that is being linked to by the Tenant property.
     * Configuration server provides it only if the property value has been changed.
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
     * A pointer to the calling list
     * description.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
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
     * Retrieves the dbid of the object that is being linked to by the TableAccess property.
     * Configuration server provides it only if the property value has been changed.
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
     * Retrieves the dbid of the object that is being linked to by the Filter property.
     * Configuration server provides it only if the property value has been changed.
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
    public final Collection<CfgTreatment> getAddedTreatments() {
        return (Collection<CfgTreatment>) getProperty("treatmentDBIDs");
    }

    /**
     * A pointer to list of identifiers of <code>
     * <a href="CfgTreatment.html">CfgTreatment</a>
     * </code> dedicated to the calling
     * list. Optional.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedTreatmentDBIDs() {
        return getLinkListCollection("treatmentDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedTreatmentDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getTreatmentDBIDs() {
        return getLinkListCollection("treatmentDBIDs");
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
     * Retrieves the dbid of the object that is being linked to by the LogTableAccess property.
     * Configuration server provides it only if the property value has been changed.
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
     * Retrieves the dbid of the object that is being linked to by the Script property.
     * Configuration server provides it only if the property value has been changed.
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
     * A pointer to the list of user-defined properties.Parameter userProperties has the following structure: Each key-value pair of the primary list (TKVList *userProperties) uses the key for the name of a user-defined section, and the value for a secondary list, that also has the TKVList structure and specifies the properties defined within that section.
     *
     * @return property value or null
     */
    public final KeyValueCollection getAddedUserProperties() {
        return (KeyValueCollection) getProperty("userProperties");
    }

    /**
     * A pointer to the list of deleted treatments.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgTreatment> getDeletedTreatments() {
        return (Collection<CfgTreatment>) getProperty("deletedTreatmentDBIDs");
    }

    /**
     * A pointer to the list of deleted treatments.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedTreatmentDBIDs() {
        return getLinkListCollection("deletedTreatmentDBIDs");
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
}
