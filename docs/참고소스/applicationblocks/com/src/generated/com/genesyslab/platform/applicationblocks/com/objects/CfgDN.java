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
 * A <em>DN</em> is a communication device that is uniquely identified by a
 * directory number, where customer interactions (for example, telephone calls or
 * e-mails) reside and are handled.
 *
 * <p/>
 * Deletion of DN X will cause the following events set
 * out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>DNDBIDs</code> of the place that had
 * DN X assigned
 * </li>
 * <li>
 * Modification of <code>destDNDBIDs</code> of all DNs whose
 * destination DNs included DN X
 * </li>
 * <li>
 * Modification of <code>routeDNDBIDs</code> of all agent groups
 * whose route DNs included DN X
 * </li>
 * <li>
 * Modification of <code>routeDNDBIDs</code> of all place groups
 * whose route DNs included DN X
 * </li>
 * <li>
 * Modification of <code>DNs</code> of all DN groups whose DNs
 * included DN X
 * </li>
 * <li>
 * Modification of <code>routeDNDBIDs</code> of all DN groups
 * whose route DNs included DN X
 * </li>
 * <li>
 * Modification of <code>
 * <a href="CfgTreatment.html">CfgTreatment</a>
 * </code> objects which had
 * <code>destDNDBID</code> field set to DN X
 * </li>
 * <li>
 * Modification of <code>
 * <a href="CfgIVRPort.html">CfgIVRPort</a>
 * </code> objects which had <code>DNDBID</code> field
 * set to DN X
 * </li>
 * <li>
 * Modification of <code>
 * <a href="CfgCampaignGroup.html">CfgCampaignGroup</a>
 * </code>
 *  which had <code>origDNDBID</code>
 * field set to DN X
 * </li>
 * <li>
 * Deletion of DN X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaDN.html">
 * <b>CfgDeltaDN</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgSwitch.html">
 * <b>CfgSwitch</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgDNGroup.html">
 * <b>CfgDNGroup</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgPlace.html">
 * <b>CfgPlace</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDN
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGDN;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgDN(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgDN - "
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
    public CfgDN(
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
    public CfgDN(final IConfService confService) {
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
            if (getMetaData().getAttribute("switchDBID") != null) {
                if (getLinkValue("switchDBID") == null) {
                    throw new ConfigException("Mandatory property 'switchDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("type") != null) {
                if (getProperty("type") == null) {
                    throw new ConfigException("Mandatory property 'type' not set.");
                }
            }
            if (getMetaData().getAttribute("number") != null) {
                if (getProperty("number") == null) {
                    throw new ConfigException("Mandatory property 'number' not set.");
                }
            }
            if (getMetaData().getAttribute("registerAll") != null) {
                if (getProperty("registerAll") == null) {
                    setProperty("registerAll", 2);
                }
            }
            if (getMetaData().getAttribute("routeType") != null) {
                if (getProperty("routeType") == null) {
                    throw new ConfigException("Mandatory property 'routeType' not set.");
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
                }
            }
            if (getMetaData().getAttribute("useOverride") != null) {
                if (getProperty("useOverride") == null) {
                    setProperty("useOverride", 2);
                }
            }
            if (getMetaData().getAttribute("accessNumbers") != null) {
                Collection<CfgDNAccessNumber> accessNumbers = (Collection<CfgDNAccessNumber>) getProperty("accessNumbers");
                if (accessNumbers != null) {
                    for (CfgDNAccessNumber item : accessNumbers) {
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
     * A unique identifier of the <code>
     * <a href="CfgSwitch.html">Switch</a>
     * </code> to which this DN belongs. Mandatory. Once specified, cannot
     * be changed.
     *
     * @param value new property value
     * @see #getSwitch()
     */
    public final void setSwitch(final CfgSwitch value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("switchDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgSwitch.html">Switch</a>
     * </code> to which this DN belongs. Mandatory. Once specified, cannot
     * be changed.
     *
     * @param dbid DBID identifier of referred object
     * @see #getSwitch()
     */
    public final void setSwitchDBID(final int dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("switchDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Switch property.
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
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code>
     * to which this DN belongs. Read-only (set automatically according
     * to the current value of <code>tenantDBID</code> of the switch specified in <code>switchDBID</code>).
     * See <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>.
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
     * to which this DN belongs. Read-only (set automatically according
     * to the current value of <code>tenantDBID</code> of the switch specified in <code>switchDBID</code>).
     * See <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>.
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
     * Type of this DN. See <code>
     * <a href="../Enumerations/CfgDNType.html">CfgDNType</a>
     * </code>.
     * Mandatory. Once specified, cannot be changed.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgDNType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
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
     * @param value new property value
     * @see #getNumber()
     */
    public final void setNumber(final String value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("number", value);
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
     * A pointer to the identifier
     * of an entity permanently associated with this DN (e.g., an IVR port
     * number, channel name, or access number).
     *
     * @param value new property value
     * @see #getAssociation()
     */
    public final void setAssociation(final String value) {
        setProperty("association", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgDN> getDestDNs() {
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
     * @param value new property value
     * @see #getDestDNs()
     */
    public final void setDestDNs(final Collection<CfgDN> value) {
        setProperty("destDNDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the DestDNs property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDestDNDBIDs() {
        return getLinkListCollection("destDNDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the DestDNs property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setDestDNDBIDs(final Collection<Integer> value) {
        setProperty("destDNDBIDs", value);
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
     * A pointer to the login identifier
     * used to activate this DN. Makes sense only if
     * <code>type</code> is set to <code>CFGACDPosition</code>,
     * <code>CFGExtension</code>,<code> CFGEAPort</code>, <code>CFGVoiceMail</code>,
     * or <code>CFGMixed</code>. For type <code>CFGAccessResource</code>
     * specifies the type of the resource and must be presented on GIU(Configuration Manager)
     * with caption _Resource Type_
     *
     * @param value new property value
     * @see #getDNLoginID()
     */
    public final void setDNLoginID(final String value) {
        setProperty("DNLoginID", value);
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
     * An indicator of whether T-Server
     * shall register this DN within the switch. Recommended to be set
     * to <code>CFGDRTrue</code> by default.
     * See <code>
     * <a href="../Enumerations/CfgDNRegisterFlag.html">CfgDNRegisterFlag</a>
     * </code>.
     *
     * @param value new property value
     * @see #getRegisterAll()
     */
    public final void setRegisterAll(final CfgDNRegisterFlag value) {
        setProperty("registerAll", value);
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
     * A unique identifier of the <code>
     * <a href="CfgDNGroup.html">DN Group</a>
     * </code>
     *  used in number translation.
     *
     * @param value new property value
     * @see #getGroup()
     */
    public final void setGroup(final CfgDNGroup value) {
        setProperty("groupDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgDNGroup.html">DN Group</a>
     * </code>
     *  used in number translation.
     *
     * @param dbid DBID identifier of referred object
     * @see #getGroup()
     */
    public final void setGroupDBID(final int dbid) {
        setProperty("groupDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Group property.
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
     * Number of trunks associated with
     * this DN. Makes sense only if <code>type</code> is set to <code>CFGDestinationLabel</code>.
     *
     * @param value new property value
     * @see #getTrunks()
     */
    public final void setTrunks(final Integer value) {
        setProperty("trunks", value);
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
     * Type of routing that applies
     * to this DN. See type <code>
     * <a href="../Enumerations/CfgRouteType.html">CfgRouteType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getRouteType()
     */
    public final void setRouteType(final CfgRouteType value) {
        setProperty("routeType", value);
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
     * The number used as a substitute
     * of a regular directory number in certain types of routing.
     *
     * @param value new property value
     * @see #getOverride()
     */
    public final void setOverride(final String value) {
        setProperty("override", value);
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
     * Name of this DN required if the DN is planned to be used as a
     * target in routing strategies. If specified, must be unique within the tenant
     * this DN belongs to. It is strongly recommended to give names to DNs of the
     * following types:
     * <code>CFGACDQueue, CFGRoutingPoint, CFGVirtualACDQueue, CFGVirtualRoutingPoint,</code> and
     * <code>CFGRoutingQueue</code>.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
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
     * An indicator of whether the
     * <code>override</code> value shall be used instead of
     * the <code>number</code> or <code>name</code> value for accessing this
     * DN in certain types of routing. Recommended to be set to <code>CFGTrue</code> by default.
     * See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>.
     *
     * @param value new property value
     * @see #getUseOverride()
     */
    public final void setUseOverride(final CfgFlag value) {
        setProperty("useOverride", value);
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
     * An integer that corresponds to a combination of switch-specific
     * settings for this DN. Cannot be set to a zero or negative value.
     *
     * @param value new property value
     * @see #getSwitchSpecificType()
     */
    public final void setSwitchSpecificType(final Integer value) {
        setProperty("switchSpecificType", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgDNAccessNumber> getAccessNumbers() {
        return (Collection<CfgDNAccessNumber>) getProperty("accessNumbers");
    }

    /**
     * A pointer to the list of structures that specify the numbers
     * to be dialed from different switches to get this DN. Makes sense
     * only if <code>type</code> is set to <code>CFGExtRoutingPoint</code> and
     * <code> CFGAccessResource</code>. See <code>
     * <a href="CfgDNAccessNumber.html">CfgDNAccessNumber</a>
     * </code>.
     *
     * @param value new property value
     * @see #getAccessNumbers()
     */
    public final void setAccessNumbers(final Collection<CfgDNAccessNumber> value) {
        setProperty("accessNumbers", value);
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
     * A unique identifier of <code>
     * <a href="CfgFolder.html">Site</a>
     * </code> (CfgFolder) with which this DN is associated.
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
     * </code> (CfgFolder) with which this DN is associated.
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
     * </code> (CfgObjectiveTable) with which this DN is associated.
     *
     * @return instance of referred object or null
     */
    public final CfgObjectiveTable getContract() {
        return (CfgObjectiveTable) getProperty(CfgObjectiveTable.class, "contractDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgObjectiveTable.html">Cost Contract</a>
     * </code> (CfgObjectiveTable) with which this DN is associated.
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
     * </code> (CfgObjectiveTable) with which this DN is associated.
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
