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
 * <a href="CfgSwitch.html">CfgSwitch</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDeltaSwitch extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaSwitch(final IConfService confService) {
        super(confService, "CfgDeltaSwitch");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaSwitch(
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
    public CfgDeltaSwitch(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgSwitch configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgSwitch configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgSwitch retrieveCfgSwitch() throws ConfigException {
        return (CfgSwitch) (super.retrieveObject());
    }

    /**
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code> to which this switch is allocated. Mandatory. Once specified,
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
     * A unique identifier of the <code>
     * <a href="CfgPhysicalSwitch.html">Physical Switch</a>
     * </code> within which this
     * switch is defined. Mandatory. Once specified, cannot be changed.
     *
     * @return instance of referred object or null
     */
    public final CfgPhysicalSwitch getPhysSwitch() {
        return (CfgPhysicalSwitch) getProperty(CfgPhysicalSwitch.class, "physSwitchDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the PhysSwitch property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getPhysSwitchDBID() {
        return getLinkValue("physSwitchDBID");
    }

    /**
     * Type of the physical switch to which
     * this switch belongs. Read-only (set automatically according to the
     * current value of <code>type</code> of the physical switch specified in <code>physSwitchDBID</code>).
     * See <code>
     * <a href="../Enumerations/CfgSwitchType.html">CfgSwitchType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgSwitchType getType() {
        return (CfgSwitchType) getProperty(CfgSwitchType.class, "type");
    }

    /**
     * A pointer to the name of the switch.
     * Mandatory. Must be unique within the tenant and the physical switch.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A unique identifier of the
     * T-Server <code>
     * <a href="CfgApplication.html">Application</a>
     * </code> through which the telephony objects
     * of this switch are controlled. Parameter
     * <code>tenantDBIDs</code> of the T-Server must be specified
     * and match the setting of <code>tenantDBID</code> of this switch.
     * One T-Server cannot be associated with more than one switch unless
     * the switch is of type <code>CFGMultimediaSwitch</code>. The property
     * is applicable for 5.1 applications only, for compatibility. Starting
     * from release 6.0 the association between T-Server and switch have
     * to be configured using CfgApplication (T-Server) object. See <code>flexibleProperties</code>
     * in CfgApplication.
     *
     * @return instance of referred object or null
     */
    public final CfgApplication getTServer() {
        return (CfgApplication) getProperty(CfgApplication.class, "TServerDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the TServer property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getTServerDBID() {
        return getLinkValue("TServerDBID");
    }

    /**
     * Type of the CTI link of this switch.
     * Optional. See <code>
     * <a href="../Enumerations/CfgLinkType.html">CfgLinkType</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgLinkType getLinkType() {
        return (CfgLinkType) getProperty(CfgLinkType.class, "linkType");
    }

    /**
     * A pointer to the list of access codes of the switches that
     * this switch can access (every item of this list is structured as
     * <code>
     * <a href="CfgSwitchAccessCode.html">CfgSwitchAccessCode</a>
     * </code>). When used as an entry
     * in <code>
     * <a href="CfgDeltaSwitch.html">CfgDeltaSwitch</a>
     * </code>, it is a pointer to a list of switch access
     * codes added to the existing list.
     *
     * @return list of structures or null
     */
    public final Collection<CfgSwitchAccessCode> getAddedSwitchAccessCodes() {
        return (Collection<CfgSwitchAccessCode>) getProperty("switchAccessCodes");
    }

    /**
     * A pointer to a string that describes
     * the numbering plan of the switch. Use a hyphen to specify a range
     * of numbers; use commas to specify a series of stand-alone numbers
     * or ranges (e.g., <code>1100-1179, 1190-1195, 1199</code>).
     *
     * @return property value or null
     */
    public final String getDNRange() {
        return (String) getProperty("DNRange");
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
     * A pointer to the list of deleted switch access codes (every
     * item of this list is structured as <code>
     * <a href="CfgSwitchAccessCode.html">CfgSwitchAccessCode</a>
     * </code>).
     *
     * @return list of structures or null
     */
    public final Collection<CfgSwitchAccessCode> getDeletedSwitchAccessCodes() {
        return (Collection<CfgSwitchAccessCode>) getProperty("deletedSwitchAccessCodes");
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
