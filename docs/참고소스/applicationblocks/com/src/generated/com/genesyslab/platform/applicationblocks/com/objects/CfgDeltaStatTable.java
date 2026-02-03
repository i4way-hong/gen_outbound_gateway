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
 * <a href="CfgStatTable.html">CfgStatTable</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDeltaStatTable extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaStatTable(final IConfService confService) {
        super(confService, "CfgDeltaStatTable");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaStatTable(
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
    public CfgDeltaStatTable(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgStatTable configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgStatTable configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgStatTable retrieveCfgStatTable() throws ConfigException {
        return (CfgStatTable) (super.retrieveObject());
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
     * Retrieves the dbid of the object that is being linked to by the Tenant property.
     * Configuration server provides it only if the property value has been changed.
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
    public final Collection<CfgStatDay> getAddedStatDays() {
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
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedStatDayDBIDs() {
        return getLinkListCollection("statDayDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedStatDayDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getStatDayDBIDs() {
        return getLinkListCollection("statDayDBIDs");
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
     * The time the Universal Routing Server can wait until the corresponding resources become available.
     *
     * @return property value or null
     */
    public final Integer getWaitThreshold() {
        return (Integer) getProperty("waitThreshold");
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
     * The hourly rate for an agent. This value is used only for Variable Rate Contracts.
     *
     * @return property value or null
     */
    public final Integer getAgentHourlyRate() {
        return (Integer) getProperty("agentHourlyRate");
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
     * A pointer to the list of identifiers of stat days that are
     * no longer defined within this table.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgStatDay> getDeletedStatDays() {
        return (Collection<CfgStatDay>) getProperty("deletedStatDayDBIDs");
    }

    /**
     * A pointer to the list of identifiers of stat days that are
     * no longer defined within this table.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedStatDayDBIDs() {
        return getLinkListCollection("deletedStatDayDBIDs");
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
