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
 * <a href="CfgTenant.html">CfgTenant</a>
 * </code> object.
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgTenant.html">
 * <b>CfgTenant</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDeltaTenant extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaTenant(final IConfService confService) {
        super(confService, "CfgDeltaTenant");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaTenant(
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
    public CfgDeltaTenant(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgTenant configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgTenant configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgTenant retrieveCfgTenant() throws ConfigException {
        return (CfgTenant) (super.retrieveObject());
    }

    /**
     * An indicator of whether the tenant belongs to the Service
     * Provider. (see
     * <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>)
     *  Read-only (set automatically when a tenant is created).
     *
     * @return property value or null
     */
    public final CfgFlag getIsServiceProvider() {
        return (CfgFlag) getProperty(CfgFlag.class, "isServiceProvider");
    }

    /**
     * A pointer to name of the tenant. Mandatory.
     * Must be unique within the Configuration Database.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the tenant password.
     * Max length 64 symbols.
     *
     * @return property value or null
     */
    public final String getPassword() {
        return (String) getProperty("password");
    }

    /**
     * Not in use.
     *
     * @return property value or null
     */
    public final CfgAddress getAddress() {
        return (CfgAddress) getProperty(CfgAddress.class, "address");
    }

    /**
     * A pointer to the string value that is used for service charges
     * of this tenant. Max length 64 symbols.
     *
     * @return property value or null
     */
    public final String getChargeableNumber() {
        return (String) getProperty("chargeableNumber");
    }

    /**
     * Not in use.
     *
     * @return instance of referred object or null
     */
    public final CfgPerson getTenantPerson() {
        return (CfgPerson) getProperty(CfgPerson.class, "tenantPersonDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the TenantPerson property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getTenantPersonDBID() {
        return getLinkValue("tenantPersonDBID");
    }

    /**
     * Not in use.
     *
     * @return instance of referred object or null
     */
    public final CfgPerson getProviderPerson() {
        return (CfgPerson) getProperty(CfgPerson.class, "providerPersonDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the ProviderPerson property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getProviderPersonDBID() {
        return getLinkValue("providerPersonDBID");
    }

    /**
     * Not in use.
     *
     * @return list of structures or null
     */
    public final Collection<CfgServiceInfo> getAddedServiceInfo() {
        return (Collection<CfgServiceInfo>) getProperty("serviceInfo");
    }

    /**
     * Not in use.
     *
     * @return property value or null
     */
    public final CfgFlag getIsSuperTenant() {
        return (CfgFlag) getProperty(CfgFlag.class, "isSuperTenant");
    }

    /**
     * Not in use.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgTenant> getAddedTenants() {
        return (Collection<CfgTenant>) getProperty("tenantDBIDs");
    }

    /**
     * Not in use.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedTenantDBIDs() {
        return getLinkListCollection("tenantDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedTenantDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getTenantDBIDs() {
        return getLinkListCollection("tenantDBIDs");
    }

    /**
     * <p/>
     * Current object state. Mandatory. Refer to
     * <code>
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
     * <p/>
     * A unique identifier of the capacity rule (<code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>)
     * associated with this tenant.
     *
     * @return instance of referred object or null
     */
    public final CfgScript getDefaultCapacityRule() {
        return (CfgScript) getProperty(CfgScript.class, "defaultCapacityRuleDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the DefaultCapacityRule property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getDefaultCapacityRuleDBID() {
        return getLinkValue("defaultCapacityRuleDBID");
    }

    /**
     * A unique identifier of the cost contract (<code>
     * <a href="CfgObjectiveTable.html">CfgObjectiveTable</a>
     * </code>)
     * associated with this tenant.
     *
     * @return instance of referred object or null
     */
    public final CfgObjectiveTable getDefaultContract() {
        return (CfgObjectiveTable) getProperty(CfgObjectiveTable.class, "defaultContractDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the DefaultContract property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getDefaultContractDBID() {
        return getLinkValue("defaultContractDBID");
    }

    public final CfgTenant getParentTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "parentTenantDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the ParentTenant property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getParentTenantDBID() {
        return getLinkValue("parentTenantDBID");
    }

    /**
     * Not in use.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgServiceInfo> getDeletedServices() {
        return (Collection<CfgServiceInfo>) getProperty("deletedServiceDBIDs");
    }

    /**
     * Not in use.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedServiceDBIDs() {
        return getLinkListCollection("deletedServiceDBIDs");
    }

    /**
     * Not in use.
     *
     * @return list of structures or null
     */
    public final Collection<CfgServiceInfo> getChangedServiceInfo() {
        return (Collection<CfgServiceInfo>) getProperty("changedServiceInfo");
    }

    /**
     * Not in use.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgTenant> getDeletedTenants() {
        return (Collection<CfgTenant>) getProperty("deletedTenantDBIDs");
    }

    /**
     * Not in use.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedTenantDBIDs() {
        return getLinkListCollection("deletedTenantDBIDs");
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
