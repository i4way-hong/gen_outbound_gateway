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
 * A <em>Place</em> is a location that has one or more DNs operated by a single
 * agent.
 * <p/>
 * Configure Places and assign individual DNs to them in order to monitor
 * performance and availability of Agents, Agent Groups, and Place Groups, and to
 * provide this information to call-processing applications.
 * <p/>
 * A typical Agent Place consists of two DNs: one DN that an agent uses to take
 * customer calls and another DN the agent uses to make consultation calls and
 * transfers. If you are using the multimedia options of the Genesys products,
 * Places may have to be equipped with DNs of other types, such as E-mail Address.
 * <p/>
 * Make sure the configuration of Places in the Configuration Database always
 * matches the actual wiring arrangements in the contact center.
 *
 * <p/>
 * Deletion of Place X will cause the following events
 * set out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>placeDBIDs</code> of all place groups
 * that included Place X
 * </li>
 * <li>
 * Modification of <code>placeDBID</code> of all persons who
 * had Place X assigned as the default place
 * </li>
 * <li>
 * Deletion of Place X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaPlace.html">
 * <b>CfgDeltaPlace</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgDN.html">
 * <b>CfgDN</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgPlaceGroup.html">
 * <b>CfgPlaceGroup</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgPlace
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGPlace;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgPlace(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgPlace - "
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
    public CfgPlace(
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
    public CfgPlace(final IConfService confService) {
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
     * </code> that this place belongs to. Mandatory. Once specified, cannot
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
     * </code> that this place belongs to. Mandatory. Once specified, cannot
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
     * </code> that this place belongs to. Mandatory. Once specified, cannot
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
     * A pointer to the name of the place.
     * Mandatory. Must be unique within the tenant.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the name of the place.
     * Mandatory. Must be unique within the tenant.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgDN> getDNs() {
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
     * @param value new property value
     * @see #getDNs()
     */
    public final void setDNs(final Collection<CfgDN> value) {
        setProperty("DNDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the DNs property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDNDBIDs() {
        return getLinkListCollection("DNDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the DNs property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setDNDBIDs(final Collection<Integer> value) {
        setProperty("DNDBIDs", value);
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
     * A unique identifier of the capacity rule
     * (<code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>)
     * associated with this place.
     *
     * @param value new property value
     * @see #getCapacityRule()
     */
    public final void setCapacityRule(final CfgScript value) {
        setProperty("capacityRuleDBID", value);
    }

    /**
     * A unique identifier of the capacity rule
     * (<code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>)
     * associated with this place.
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
     * A unique identifier of <code>
     * <a href="CfgFolder.html">Site</a>
     * </code> (CfgFolder) with which this Place is associated.
     *
     * @param value new property value
     * @see #getSite()
     */
    public final void setSite(final CfgFolder value) {
        setProperty("siteDBID", value);
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFolder.html">Site</a>
     * </code> (CfgFolder) with which this Place is associated.
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
     * A unique identifier of <code>
     * <a href="CfgObjectiveTable.html">Cost Contract</a>
     * </code> (CfgObjectiveTable) with which this Place is associated.
     *
     * @param value new property value
     * @see #getContract()
     */
    public final void setContract(final CfgObjectiveTable value) {
        setProperty("contractDBID", value);
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgObjectiveTable.html">Cost Contract</a>
     * </code> (CfgObjectiveTable) with which this Place is associated.
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
