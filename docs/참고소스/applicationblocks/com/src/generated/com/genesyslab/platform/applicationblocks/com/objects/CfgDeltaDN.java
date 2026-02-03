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
 * <a href="CfgDN.html">CfgDN</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDeltaDN extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaDN(final IConfService confService) {
        super(confService, "CfgDeltaDN");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaDN(
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
    public CfgDeltaDN(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgDN configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgDN configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgDN retrieveCfgDN() throws ConfigException {
        return (CfgDN) (super.retrieveObject());
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgSwitch.html">Switch</a>
     * </code> to which this DN belongs. Mandatory. Once specified, cannot
     * be changed.
     *
     * @return instance of referred object or null
     */
    public final CfgSwitch getSwitch() {
        return (CfgSwitch) getProperty(CfgSwitch.class, "switchDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Switch property.
     * Configuration server provides it only if the property value has been changed.
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
     * to which this DN belongs. Read-only (set automatically according
     * to the current value of <code>tenantDBID</code> of the switch specified in <code>switchDBID</code>).
     * See <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>.
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
     * Type of this DN. See <code>
     * <a href="../Enumerations/CfgDNType.html">CfgDNType</a>
     * </code>.
     * Mandatory. Once specified, cannot be changed.
     *
     * @return property value or null
     */
    public final CfgDNType getType() {
        return (CfgDNType) getProperty(CfgDNType.class, "type");
    }

    /**
     * Directory number assigned to this
     * DN within the switch. Mandatory. Must be unique within the switch
     * for all dn types except
     * <code>CFGDestinationLabel</code> and <code>CFGAccessResource</code>.
     * The uniqueness of <code>CFGAccessResource</code> is defined by combination
     * of number and DN type. Once specified, cannot be changed. Please
     * see the comment regarding the parameter <code>DNRange</code> in
     * <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>.
     *
     * @return property value or null
     */
    public final String getNumber() {
        return (String) getProperty("number");
    }

    /**
     * A pointer to the identifier
     * of an entity permanently associated with this DN (e.g., an IVR port
     * number, channel name, or access number).
     *
     * @return property value or null
     */
    public final String getAssociation() {
        return (String) getProperty("association");
    }

    /**
     * A pointer to the list of identifiers
     * of the objects (DBIDs) to which the calls residing at this DN can be routed/diverted
     * by default. Makes sense only if type is set to
     * <code>CFGRoutingPoint, CFGExtRoutingPoint, CFGServiceNumber,
     * CFGRoutingQueue</code>,<code> CFGACDQueue</code>, <code>CFGVirtACDQueue</code>,
     * or <code>CFGVirtRoutingPoint,</code> and <code>CFGAccessResource</code> and
     * shall be set to <code>NULL</code> for all other values of <code>type</code>.
     * When used as an entry in <code>CfgDeltaDN</code> (see below), it
     * is a pointer to a list of identifiers of the objects added to the
     * existing list. The DN for which this list is specified cannot be
     * added to this list. If DN type is <code>CFGAccessResource</code>
     * the property must be presented on GUI (Config Manager) with caption _Remote
     * Resources_.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgDN> getAddedDestDNs() {
        return (Collection<CfgDN>) getProperty("destDNDBIDs");
    }

    /**
     * A pointer to the list of identifiers
     * of the objects (DBIDs) to which the calls residing at this DN can be routed/diverted
     * by default. Makes sense only if type is set to
     * <code>CFGRoutingPoint, CFGExtRoutingPoint, CFGServiceNumber,
     * CFGRoutingQueue</code>,<code> CFGACDQueue</code>, <code>CFGVirtACDQueue</code>,
     * or <code>CFGVirtRoutingPoint,</code> and <code>CFGAccessResource</code> and
     * shall be set to <code>NULL</code> for all other values of <code>type</code>.
     * When used as an entry in <code>CfgDeltaDN</code> (see below), it
     * is a pointer to a list of identifiers of the objects added to the
     * existing list. The DN for which this list is specified cannot be
     * added to this list. If DN type is <code>CFGAccessResource</code>
     * the property must be presented on GUI (Config Manager) with caption _Remote
     * Resources_.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedDestDNDBIDs() {
        return getLinkListCollection("destDNDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedDestDNDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getDestDNDBIDs() {
        return getLinkListCollection("destDNDBIDs");
    }

    /**
     * An indicator of whether a login
     * procedure is necessary to activate the telephony object associated
     * with this DN. Read-only (set automatically according to the current
     * value of <code>DNLoginID</code> below). See <code>CfgFlag</code>.
     * The value should not be taken into consideration if DN type is <code>CFGAccessResource</code>.
     *
     * @return property value or null
     */
    public final CfgFlag getLoginFlag() {
        return (CfgFlag) getProperty(CfgFlag.class, "loginFlag");
    }

    /**
     * A pointer to the login identifier
     * used to activate this DN. Makes sense only if
     * <code>type</code> is set to <code>CFGACDPosition</code>,
     * <code>CFGExtension</code>,<code> CFGEAPort</code>, <code>CFGVoiceMail</code>,
     * or <code>CFGMixed</code>. For type <code>CFGAccessResource</code>
     * specifies the type of the resource and must be presented on GIU(Configuration Manager)
     * with caption _Resource Type_
     *
     * @return property value or null
     */
    public final String getDNLoginID() {
        return (String) getProperty("DNLoginID");
    }

    /**
     * An indicator of whether T-Server
     * shall register this DN within the switch. Recommended to be set
     * to <code>CFGDRTrue</code> by default.
     * See <code>
     * <a href="../Enumerations/CfgDNRegisterFlag.html">CfgDNRegisterFlag</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgDNRegisterFlag getRegisterAll() {
        return (CfgDNRegisterFlag) getProperty(CfgDNRegisterFlag.class, "registerAll");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgDNGroup.html">DN Group</a>
     * </code>
     *  used in number translation.
     *
     * @return instance of referred object or null
     */
    public final CfgDNGroup getGroup() {
        return (CfgDNGroup) getProperty(CfgDNGroup.class, "groupDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Group property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getGroupDBID() {
        return getLinkValue("groupDBID");
    }

    /**
     * Number of trunks associated with
     * this DN. Makes sense only if <code>type</code> is set to <code>CFGDestinationLabel</code>.
     *
     * @return property value or null
     */
    public final Integer getTrunks() {
        return (Integer) getProperty("trunks");
    }

    /**
     * Type of routing that applies
     * to this DN. See type <code>
     * <a href="../Enumerations/CfgRouteType.html">CfgRouteType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgRouteType getRouteType() {
        return (CfgRouteType) getProperty(CfgRouteType.class, "routeType");
    }

    /**
     * The number used as a substitute
     * of a regular directory number in certain types of routing.
     *
     * @return property value or null
     */
    public final String getOverride() {
        return (String) getProperty("override");
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
     * Name of this DN required if the DN is planned to be used as a
     * target in routing strategies. If specified, must be unique within the tenant
     * this DN belongs to. It is strongly recommended to give names to DNs of the
     * following types:
     * <code>CFGACDQueue, CFGRoutingPoint, CFGVirtualACDQueue, CFGVirtualRoutingPoint,</code> and
     * <code>CFGRoutingQueue</code>.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * An indicator of whether the
     * <code>override</code> value shall be used instead of
     * the <code>number</code> or <code>name</code> value for accessing this
     * DN in certain types of routing. Recommended to be set to <code>CFGTrue</code> by default.
     * See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgFlag getUseOverride() {
        return (CfgFlag) getProperty(CfgFlag.class, "useOverride");
    }

    /**
     * An integer that corresponds to a combination of switch-specific
     * settings for this DN. Cannot be set to a zero or negative value.
     *
     * @return property value or null
     */
    public final Integer getSwitchSpecificType() {
        return (Integer) getProperty("switchSpecificType");
    }

    /**
     * A pointer to the list of structures that specify the numbers
     * to be dialed from different switches to get this DN. Makes sense
     * only if <code>type</code> is set to <code>CFGExtRoutingPoint</code> and
     * <code> CFGAccessResource</code>. See <code>
     * <a href="CfgDNAccessNumber.html">CfgDNAccessNumber</a>
     * </code>.
     *
     * @return list of structures or null
     */
    public final Collection<CfgDNAccessNumber> getAddedAccessNumbers() {
        return (Collection<CfgDNAccessNumber>) getProperty("accessNumbers");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFolder.html">Site</a>
     * </code> (CfgFolder) with which this DN is associated.
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
     * </code> (CfgObjectiveTable) with which this DN is associated.
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
     * A pointer to the list of identifiers of the objects to which
     * the calls residing at this DN can no longer be routed by default.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgDN> getDeletedDestDNs() {
        return (Collection<CfgDN>) getProperty("deletedDestDNDBIDs");
    }

    /**
     * A pointer to the list of identifiers of the objects to which
     * the calls residing at this DN can no longer be routed by default.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedDestDNDBIDs() {
        return getLinkListCollection("deletedDestDNDBIDs");
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

    /**
     * A pointer to the list of deleted access numbers ( every item
     * of this list is structured as <code>
     * <a href="CfgDNAccessNumber.html">CfgDNAccessNumber</a>
     * </code>)
     *
     * @return list of structures or null
     */
    public final Collection<CfgDNAccessNumber> getDeletedAccessNumbers() {
        return (Collection<CfgDNAccessNumber>) getProperty("deletedAccessNumbers");
    }
}
