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
 * A group.
 *
 * <p/>
 * For special requirements that apply to the entities
 * of <code>
 * <a href="CfgGroup.html">CfgGroup</a>
 * </code> when it is used as part of an access group,
 * see the description of parameter
 * <code>groupInfo</code> of type <code>CfgAccessGroup</code> in
 * section <code>Access Control Functions and Data Types</code>.
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaGroup.html">
 * <b>CfgDeltaGroup</b>
 * </a>
 * </p>
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgGroup extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgGroup(
            final IConfService confService,
            final ConfStructure objData,
            final ICfgObject parent) {
        super(confService, objData, parent);
    }

    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param xmlData XML object containing structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgGroup(
            final IConfService confService,
            final Node xmlData,
            final ICfgObject parent) {
        super(confService, xmlData, parent);
    }

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgGroup(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgGroup")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
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
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
                }
            }
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
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code>
     * that this group belongs to. Mandatory. Once specified, cannot be
     * changed.
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
     * that this group belongs to. Mandatory. Once specified, cannot be
     * changed.
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
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgPerson> getManagers() {
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
     * @param value new property value
     * @see #getManagers()
     */
    public final void setManagers(final Collection<CfgPerson> value) {
        setProperty("managerDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the Managers property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getManagerDBIDs() {
        return getLinkListCollection("managerDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the Managers property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setManagerDBIDs(final Collection<Integer> value) {
        setProperty("managerDBIDs", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgDN> getRouteDNs() {
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
     * @param value new property value
     * @see #getRouteDNs()
     */
    public final void setRouteDNs(final Collection<CfgDN> value) {
        setProperty("routeDNDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the RouteDNs property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getRouteDNDBIDs() {
        return getLinkListCollection("routeDNDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the RouteDNs property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setRouteDNDBIDs(final Collection<Integer> value) {
        setProperty("routeDNDBIDs", value);
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
     * A unique identifier of the <code>
     * <a href="CfgStatTable.html">Stat Table</a>
     * </code> of <code>CFGCapacityTable</code> type
     * associated with this group.
     *
     * @param value new property value
     * @see #getCapacityTable()
     */
    public final void setCapacityTable(final CfgStatTable value) {
        setProperty("capacityTableDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgStatTable.html">Stat Table</a>
     * </code> of <code>CFGCapacityTable</code> type
     * associated with this group.
     *
     * @param dbid DBID identifier of referred object
     * @see #getCapacityTable()
     */
    public final void setCapacityTableDBID(final int dbid) {
        setProperty("capacityTableDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the CapacityTable property.
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
     * A unique identifier of the <code>
     * <a href="CfgStatTable.html">Stat Table</a>
     * </code> of <code>CFGQuotaTable</code> type
     * associated with this group.
     *
     * @param value new property value
     * @see #getQuotaTable()
     */
    public final void setQuotaTable(final CfgStatTable value) {
        setProperty("quotaTableDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgStatTable.html">Stat Table</a>
     * </code> of <code>CFGQuotaTable</code> type
     * associated with this group.
     *
     * @param dbid DBID identifier of referred object
     * @see #getQuotaTable()
     */
    public final void setQuotaTableDBID(final int dbid) {
        setProperty("quotaTableDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the QuotaTable property.
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
     * A unique identifier of the capacity rule (<code>
     * <a href="CfgScript.html">Script</a>
     * </code>) associated
     * with this group. Applicable for CfgPlaceGroup and CfgAgentGroup
     * only.
     *
     * @param value new property value
     * @see #getCapacityRule()
     */
    public final void setCapacityRule(final CfgScript value) {
        setProperty("capacityRuleDBID", value);
    }

    /**
     * A unique identifier of the capacity rule (<code>
     * <a href="CfgScript.html">Script</a>
     * </code>) associated
     * with this group. Applicable for CfgPlaceGroup and CfgAgentGroup
     * only.
     *
     * @param dbid DBID identifier of referred object
     * @see #getCapacityRule()
     */
    public final void setCapacityRuleDBID(final int dbid) {
        setProperty("capacityRuleDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the CapacityRule property.
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
     * A unique identifier of Site (<code>
     * <a href="CfgFolder.html">Folder</a>
     * </code>) with which this Group is associated.
     *
     * @param value new property value
     * @see #getSite()
     */
    public final void setSite(final CfgFolder value) {
        setProperty("siteDBID", value);
    }

    /**
     * A unique identifier of Site (<code>
     * <a href="CfgFolder.html">Folder</a>
     * </code>) with which this Group is associated.
     *
     * @param dbid DBID identifier of referred object
     * @see #getSite()
     */
    public final void setSiteDBID(final int dbid) {
        setProperty("siteDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Site property.
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
     * A unique identifier of Cost Contract (<code>
     * <a href="CfgObjectiveTable.html">Objective Table</a>
     * </code>) with which this Group is associated.
     *
     * @param value new property value
     * @see #getContract()
     */
    public final void setContract(final CfgObjectiveTable value) {
        setProperty("contractDBID", value);
    }

    /**
     * A unique identifier of Cost Contract (<code>
     * <a href="CfgObjectiveTable.html">Objective Table</a>
     * </code>) with which this Group is associated.
     *
     * @param dbid DBID identifier of referred object
     * @see #getContract()
     */
    public final void setContractDBID(final int dbid) {
        setProperty("contractDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Contract property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getContractDBID() {
        return getLinkValue("contractDBID");
    }
}
