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
 * <a href="CfgGroup.html">CfgGroup</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDeltaGroup extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaGroup(final IConfService confService) {
        super(confService, "CfgDeltaGroup");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaGroup(
            final IConfService confService,
            final ConfStructure objData) {
        super(confService, objData, null);
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param xmlData XML object containing object delta data
     */
    public CfgDeltaGroup(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgGroup configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code>
     * that this group belongs to. Mandatory. Once specified, cannot be
     * changed.
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
     * A pointer to name of the group. Mandatory.
     * See comments to
     * <code>
     * <a href="CfgAgentGroup.html">CfgAgentGroup</a>
     * </code>,
     * <code>
     * <a href="CfgPlaceGroup.html">CfgPlaceGroup</a>, and
     * <a href="CfgDNGroup.html">CfgDNGroup</a>
     * </code>.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the list of the
     * identifiers of the <code>
     * <a href="CfgPerson.html">CfgPerson</a>
     * </code> objects that serve as supervisors
     * of this group. Applicable for CfgAgentGroup only. When used as an
     * entry in <code>CfgDeltaGroup</code>, it is a pointer to a list of identifiers
     * of the Persons added to the existing list. A person assigned as a supervisor to
     * this Group should belong either to the same Tenant as the Group or to
     * the Tenant <code>Environment</code>.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgPerson> getAddedManagers() {
        return (Collection<CfgPerson>) getProperty("managerDBIDs");
    }

    /**
     * A pointer to the list of the
     * identifiers of the <code>
     * <a href="CfgPerson.html">CfgPerson</a>
     * </code> objects that serve as supervisors
     * of this group. Applicable for CfgAgentGroup only. When used as an
     * entry in <code>CfgDeltaGroup</code>, it is a pointer to a list of identifiers
     * of the Persons added to the existing list. A person assigned as a supervisor to
     * this Group should belong either to the same Tenant as the Group or to
     * the Tenant <code>Environment</code>.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedManagerDBIDs() {
        return getLinkListCollection("managerDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedManagerDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getManagerDBIDs() {
        return getLinkListCollection("managerDBIDs");
    }

    /**
     * A pointer to the list of identifiers
     * of the telephony objects from which calls can be routed/diverted
     * to this group. This list can include DNs whose types are set to
     * <code>
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGExtRoutingPoint">CFGExtRoutingPoint</a>,
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGServiceNumber">CFGServiceNumber</a>,
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGRoutingQueue">CFGRoutingQueue</a>,
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGRoutingPoint">CFGRoutingPoint</a>
     * </code> or <code>
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGACDQueue">CFGACDQueue</a>,
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGVirtACDQueue">CFGVirtACDQueue</a>
     * </code> and
     * <code> <a href="../../../configuration/protocol/types/CfgDNType.html#CFGVirtRoutingPoint">CFGVirtRoutingPoint</a></code>. When used as an entry in <code><a href="CfgDeltaGroup.html">CfgDeltaGroup</a></code>,
     * it is a pointer to a list of identifiers of the DNs added to the existing list.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgDN> getAddedRouteDNs() {
        return (Collection<CfgDN>) getProperty("routeDNDBIDs");
    }

    /**
     * A pointer to the list of identifiers
     * of the telephony objects from which calls can be routed/diverted
     * to this group. This list can include DNs whose types are set to
     * <code>
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGExtRoutingPoint">CFGExtRoutingPoint</a>,
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGServiceNumber">CFGServiceNumber</a>,
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGRoutingQueue">CFGRoutingQueue</a>,
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGRoutingPoint">CFGRoutingPoint</a>
     * </code> or <code>
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGACDQueue">CFGACDQueue</a>,
     * <a href="../../../configuration/protocol/types/CfgDNType.html#CFGVirtACDQueue">CFGVirtACDQueue</a>
     * </code> and
     * <code> <a href="../../../configuration/protocol/types/CfgDNType.html#CFGVirtRoutingPoint">CFGVirtRoutingPoint</a></code>. When used as an entry in <code><a href="CfgDeltaGroup.html">CfgDeltaGroup</a></code>,
     * it is a pointer to a list of identifiers of the DNs added to the existing list.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedRouteDNDBIDs() {
        return getLinkListCollection("routeDNDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedRouteDNDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getRouteDNDBIDs() {
        return getLinkListCollection("routeDNDBIDs");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgStatTable.html">Stat Table</a>
     * </code> of <code>CFGCapacityTable</code> type
     * associated with this group.
     *
     * @return instance of referred object or null
     */
    public final CfgStatTable getCapacityTable() {
        return (CfgStatTable) getProperty(CfgStatTable.class, "capacityTableDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the CapacityTable property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getCapacityTableDBID() {
        return getLinkValue("capacityTableDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgStatTable.html">Stat Table</a>
     * </code> of <code>CFGQuotaTable</code> type
     * associated with this group.
     *
     * @return instance of referred object or null
     */
    public final CfgStatTable getQuotaTable() {
        return (CfgStatTable) getProperty(CfgStatTable.class, "quotaTableDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the QuotaTable property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getQuotaTableDBID() {
        return getLinkValue("quotaTableDBID");
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
     * A unique identifier of the capacity rule (<code>
     * <a href="CfgScript.html">Script</a>
     * </code>) associated
     * with this group. Applicable for CfgPlaceGroup and CfgAgentGroup
     * only.
     *
     * @return instance of referred object or null
     */
    public final CfgScript getCapacityRule() {
        return (CfgScript) getProperty(CfgScript.class, "capacityRuleDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the CapacityRule property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getCapacityRuleDBID() {
        return getLinkValue("capacityRuleDBID");
    }

    /**
     * A unique identifier of Site (<code>
     * <a href="CfgFolder.html">Folder</a>
     * </code>) with which this Group is associated.
     *
     * @return instance of referred object or null
     */
    public final CfgFolder getSite() {
        return (CfgFolder) getProperty(CfgFolder.class, "siteDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Site property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getSiteDBID() {
        return getLinkValue("siteDBID");
    }

    /**
     * A unique identifier of Cost Contract (<code>
     * <a href="CfgObjectiveTable.html">Objective Table</a>
     * </code>) with which this Group is associated.
     *
     * @return instance of referred object or null
     */
    public final CfgObjectiveTable getContract() {
        return (CfgObjectiveTable) getProperty(CfgObjectiveTable.class, "contractDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Contract property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getContractDBID() {
        return getLinkValue("contractDBID");
    }

    /**
     * A pointer to the list of identifiers of the Person objects
     * that have been deleted from this group.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgPerson> getDeletedManagers() {
        return (Collection<CfgPerson>) getProperty("deletedManagerDBIDs");
    }

    /**
     * A pointer to the list of identifiers of the Person objects
     * that have been deleted from this group.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedManagerDBIDs() {
        return getLinkListCollection("deletedManagerDBIDs");
    }

    /**
     * A pointer to the list of identifiers of the telephony objects
     * that have been deleted from this group.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgDN> getDeletedRouteDNs() {
        return (Collection<CfgDN>) getProperty("deletedRouteDNDBIDs");
    }

    /**
     * A pointer to the list of identifiers of the telephony objects
     * that have been deleted from this group.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedRouteDNDBIDs() {
        return getLinkListCollection("deletedRouteDNDBIDs");
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
