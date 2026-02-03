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
 * <a href="CfgGVPIVRProfile.html">CfgGVPIVRProfile</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDeltaGVPIVRProfile extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaGVPIVRProfile(final IConfService confService) {
        super(confService, "CfgDeltaGVPIVRProfile");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaGVPIVRProfile(
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
    public CfgDeltaGVPIVRProfile(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgGVPIVRProfile configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgGVPIVRProfile configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgGVPIVRProfile retrieveCfgGVPIVRProfile() throws ConfigException {
        return (CfgGVPIVRProfile) (super.retrieveObject());
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this GVP Voice Application Profile is allocated. Populated upon creaton. Read-only
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
     * This property is obsolete and is not in use.
     *
     * @return instance of referred object or null
     */
    public final CfgGVPCustomer getCustomer() {
        return (CfgGVPCustomer) getProperty(CfgGVPCustomer.class, "customerDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Customer property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getCustomerDBID() {
        return getLinkValue("customerDBID");
    }

    /**
     * This property is obsolete and is not in use.
     *
     * @return instance of referred object or null
     */
    public final CfgGVPReseller getReseller() {
        return (CfgGVPReseller) getProperty(CfgGVPReseller.class, "resellerDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Reseller property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getResellerDBID() {
        return getLinkValue("resellerDBID");
    }

    /**
     * A pointer to name of the GVP Voice Application Profile. Mandatory.
     * Must be unique within the Tenant.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A name for this GVP IVR Profile that appears on the console display.
     *
     * @return property value or null
     */
    public final String getDisplayName() {
        return (String) getProperty("displayName");
    }

    /**
     * This property is obsolete.
     *
     * @return property value or null
     */
    public final CfgIVRProfileType getType() {
        return (CfgIVRProfileType) getProperty(CfgIVRProfileType.class, "type");
    }

    /**
     * Optional notes and information relevant for this GVP Voice Application Profile.
     *
     * @return property value or null
     */
    public final String getNotes() {
        return (String) getProperty("notes");
    }

    /**
     * An optional short description of this GVP Voice Application Profile.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * This property is obsolete.
     *
     * @return property value or null
     */
    public final Calendar getStartServiceDate() {
        return (Calendar) getProperty("startServiceDate");
    }

    /**
     * The date when this GVP IVR Profile ends. This date must be the same as, or later than, the Start of Service.
     *
     * @return property value or null
     */
    public final Calendar getEndServiceDate() {
        return (Calendar) getProperty("endServiceDate");
    }

    /**
     * This property is obsolete.
     *
     * @return property value or null
     */
    public final CfgFlag getIsProvisioned() {
        return (CfgFlag) getProperty(CfgFlag.class, "isProvisioned");
    }

    /**
     * This property is obsolete.
     *
     * @return property value or null
     */
    public final String getTfn() {
        return (String) getProperty("tfn");
    }

    /**
     * This property is obsolete.
     *
     * @return property value or null
     */
    public final String getStatus() {
        return (String) getProperty("status");
    }

    /**
     * This property is obsolete and is not in use.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgDN> getAddedDIDs() {
        return (Collection<CfgDN>) getProperty("DIDDBIDs");
    }

    /**
     * This property is obsolete and is not in use.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedDIDDBIDs() {
        return getLinkListCollection("DIDDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedDIDDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getDIDDBIDs() {
        return getLinkListCollection("DIDDBIDs");
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

    public final Collection<CfgObjectResource> getAddedResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
    }

    /**
     * A pointer to the list of deleted DNs.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgDN> getDeletedDIDs() {
        return (Collection<CfgDN>) getProperty("deletedDIDDBIDs");
    }

    /**
     * A pointer to the list of deleted DNs.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedDIDDBIDs() {
        return getLinkListCollection("deletedDIDDBIDs");
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

    public final Collection<CfgResourceID> getDeletedResources() {
        return (Collection<CfgResourceID>) getProperty("deletedResources");
    }

    public final Collection<CfgObjectResource> getChangedResources() {
        return (Collection<CfgObjectResource>) getProperty("changedResources");
    }
}
