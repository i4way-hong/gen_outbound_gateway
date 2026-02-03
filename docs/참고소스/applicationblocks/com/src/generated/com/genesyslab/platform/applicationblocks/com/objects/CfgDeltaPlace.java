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
 * <a href="CfgPlace.html">CfgPlace</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDeltaPlace extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaPlace(final IConfService confService) {
        super(confService, "CfgDeltaPlace");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaPlace(
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
    public CfgDeltaPlace(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgPlace configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgPlace configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgPlace retrieveCfgPlace() throws ConfigException {
        return (CfgPlace) (super.retrieveObject());
    }

    /**
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code> that this place belongs to. Mandatory. Once specified, cannot
     * be changed.
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
     * A pointer to the name of the place.
     * Mandatory. Must be unique within the tenant.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the list of identifiers
     * of the telephony objects that are assigned to this place. When used
     * as an entry in <code>
     * <a href="CfgDeltaPlace.html">CfgDeltaPlace</a>
     * </code> (see below), it is a pointer
     * to a list of identifiers of the objects added to the existing list.
     * DNs assigned to the place must belong to the tenant specified by <code>tenantDBID</code> above.
     * One DN cannot be assigned to more than one place. DNs of the following
     * types cannot be included into a place: <code>CFGACDQueue, CFGRoutingPoint,
     * CFGVirtACDQueue, CFGVirtRoutingPoint, CFGTrunk, CFGTrunkGroup, CFGTieLine,
     * CFGTieLineGroup, CFGExtRoutingPoint</code> and <code>CFGRoutingQueue.
     * CFGDestinationLabel, CFGServiceNumber,CFGAccessResource.</code>
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgDN> getAddedDNs() {
        return (Collection<CfgDN>) getProperty("DNDBIDs");
    }

    /**
     * A pointer to the list of identifiers
     * of the telephony objects that are assigned to this place. When used
     * as an entry in <code>
     * <a href="CfgDeltaPlace.html">CfgDeltaPlace</a>
     * </code> (see below), it is a pointer
     * to a list of identifiers of the objects added to the existing list.
     * DNs assigned to the place must belong to the tenant specified by <code>tenantDBID</code> above.
     * One DN cannot be assigned to more than one place. DNs of the following
     * types cannot be included into a place: <code>CFGACDQueue, CFGRoutingPoint,
     * CFGVirtACDQueue, CFGVirtRoutingPoint, CFGTrunk, CFGTrunkGroup, CFGTieLine,
     * CFGTieLineGroup, CFGExtRoutingPoint</code> and <code>CFGRoutingQueue.
     * CFGDestinationLabel, CFGServiceNumber,CFGAccessResource.</code>
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedDNDBIDs() {
        return getLinkListCollection("DNDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedDNDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getDNDBIDs() {
        return getLinkListCollection("DNDBIDs");
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
     * A unique identifier of the capacity rule
     * (<code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>)
     * associated with this place.
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
     * A unique identifier of <code>
     * <a href="CfgFolder.html">Site</a>
     * </code> (CfgFolder) with which this Place is associated.
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
     * A unique identifier of <code>
     * <a href="CfgObjectiveTable.html">Cost Contract</a>
     * </code> (CfgObjectiveTable) with which this Place is associated.
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
     * A pointer to the list of identifiers of the telephony objects that
     * have been removed from this place.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgDN> getDeletedDNs() {
        return (Collection<CfgDN>) getProperty("deletedDNDBIDs");
    }

    /**
     * A pointer to the list of identifiers of the telephony objects that
     * have been removed from this place.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedDNDBIDs() {
        return getLinkListCollection("deletedDNDBIDs");
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
