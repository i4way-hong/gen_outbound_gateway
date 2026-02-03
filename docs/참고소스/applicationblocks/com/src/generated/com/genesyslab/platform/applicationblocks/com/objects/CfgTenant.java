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
 * A <em>Tenant</em> is a business whose customer interactions are enabled or
 * enhanced through services offered by a third party, typically a
 * telecommunications service provider.
 * <p/>
 * From a functional standpoint, each tenant in a multi-tenant environment is a
 * contact center (single or multi-site) completely equipped to process customer
 * interactions.
 * <p/>
 * From an architectural standpoint, however, most of the hardware and software
 * that tenants use to enable or enhance those interactions belongs to the service
 * provider.
 *
 * <p/>
 * Deletion of Tenant X will cause the following events
 * set out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of<code> tenantDBIDs</code>of all applications
 * associated with Tenant X (see <code>
 * <a href="CfgApplication.html">CfgApplication</a>
 * </code>)
 * </li>
 * <li>
 * Deletion of all agent groups of Tenant X
 * </li>
 * <li>
 * Deletion of all place groups of Tenant X
 * </li>
 * <li>
 * Deletion of all DN groups of Tenant X
 * </li>
 * <li>
 * Deletion of all access groups of Tenant X
 * </li>
 * <li>
 * Deletion of all scripts of Tenant X
 * </li>
 * <li>
 * Deletion of all persons of Tenant X (see comments to <code>
 * <a href="CfgPerson.html">CfgPerson</a>
 * </code> for
 * details)
 * </li>
 * <li>
 * Deletion of all places of Tenant X
 * </li>
 * <li>
 * Deletion of all switches of Tenant X (see comments to <code>
 * <a href="CfgSwitch.html">CfgSwitch</a>
 * </code> for
 * details)
 * </li>
 * <li>
 * Deletion of all skills of Tenant X
 * </li>
 * <li>
 * Deletion of all action codes of Tenant X
 * </li>
 * <li>
 * Deletion of all stat tables of Tenant X
 * </li>
 * <li>
 * Deletion of all stat days of Tenant X
 * </li>
 * <li>
 * Deletion of all transactions of Tenant X
 * </li>
 * <li>
 * Deletion of all fields of Tenant X
 * </li>
 * <li>
 * Deletion of all formats of Tenant X
 * </li>
 * <li>
 * Deletion of all filters of Tenant X
 * </li>
 * <li>
 * Deletion of all table accesses of Tenant X
 * </li>
 * <li>
 * Deletion of all treatments of Tenant X
 * </li>
 * <li>
 * Deletion of all calling lists of Tenant X
 * </li>
 * <li>
 * Deletion of all campaigns of Tenant X
 * </li>
 * <li>
 * Deletion of all time zones of Tenant X
 * </li>
 * <li>
 * Deletion of all voice prompts of Tenant X
 * </li>
 * <li>
 * Deletion of all enumerators of Tenant X (see comments to <code>
 * <a href="CfgEnumerator.html">CfgEnumerator</a>
 * </code> for details)
 * </li>
 * <li>
 * Deletion of all IVRs of Tenant X (see comments to <code>
 * <a href="CfgIVR.html">CfgIVR</a>
 * </code> for
 * details)
 * </li>
 * <li>
 * Deletion of all objective tables of Tenant X
 * </li>
 * <li>
 * Deletion of all folders that had Tenant X defined as the parent
 * object
 * </li>
 * <li>
 * Deletion of Tenant X
 * </li>
 * </ul>
 * <p/>
 * The following tenant is pre-defined (scripted) in the
 * Configuration Database before Configuration Server is started for
 * the first time:
 * <code><pre>
 * <snippet>
 * dbid = 1
 * isServiceProvider = CFGTrue
 * name = _Environment_
 * password = empty string
 * isSuperTenant = CFGFalse
 * state = CFGEnabled
 * </snippet>
 * </pre></code>
 * <p/>
 * This tenant (with <code>DBID=1</code>) cannot be deleted
 * or modified in any way
 * <p/>
 * The association between a solution and a tenant should be
 * made using <code>assignedTenantDBID</code>within the
 * <code>
 * <a href="CfgService.html">CfgService</a>
 * </code>object.
 * <p/>
 * The tenant can not be deleted as long as contains persons
 * serving as accounts for some server objects
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaTenant.html">
 * <b>CfgDeltaTenant</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgTenant
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGTenant;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgTenant(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgTenant - "
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
    public CfgTenant(
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
    public CfgTenant(final IConfService confService) {
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
            if (getMetaData().getAttribute("name") != null) {
                if (getProperty("name") == null) {
                    throw new ConfigException("Mandatory property 'name' not set.");
                }
            }
            if (getMetaData().getAttribute("address") != null) {
                CfgAddress address = (CfgAddress) getProperty("address");
                if (address != null) {
                    address.checkPropertiesForSave();
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
     * An indicator of whether the tenant belongs to the Service
     * Provider. (see
     * <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>)
     *  Read-only (set automatically when a tenant is created).
     *
     * @param value new property value
     * @see #getIsServiceProvider()
     */
    public final void setIsServiceProvider(final CfgFlag value) {
        setProperty("isServiceProvider", value);
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
     * A pointer to name of the tenant. Mandatory.
     * Must be unique within the Configuration Database.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
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
     * A pointer to the tenant password.
     * Max length 64 symbols.
     *
     * @param value new property value
     * @see #getPassword()
     */
    public final void setPassword(final String value) {
        setProperty("password", value);
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
     * Not in use.
     *
     * @param value new property value
     * @see #getAddress()
     */
    public final void setAddress(final CfgAddress value) {
        setProperty("address", value);
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
     * A pointer to the string value that is used for service charges
     * of this tenant. Max length 64 symbols.
     *
     * @param value new property value
     * @see #getChargeableNumber()
     */
    public final void setChargeableNumber(final String value) {
        setProperty("chargeableNumber", value);
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
     * <p/>
     * Current object state. Mandatory. Refer to
     * <code>
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
     * <p/>
     * A unique identifier of the capacity rule (<code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>)
     * associated with this tenant.
     *
     * @param value new property value
     * @see #getDefaultCapacityRule()
     */
    public final void setDefaultCapacityRule(final CfgScript value) {
        setProperty("defaultCapacityRuleDBID", value);
    }

    /**
     * <p/>
     * A unique identifier of the capacity rule (<code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>)
     * associated with this tenant.
     *
     * @param dbid DBID identifier of referred object
     * @see #getDefaultCapacityRule()
     */
    public final void setDefaultCapacityRuleDBID(final int dbid) {
        setProperty("defaultCapacityRuleDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the DefaultCapacityRule property.
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
     * A unique identifier of the cost contract (<code>
     * <a href="CfgObjectiveTable.html">CfgObjectiveTable</a>
     * </code>)
     * associated with this tenant.
     *
     * @param value new property value
     * @see #getDefaultContract()
     */
    public final void setDefaultContract(final CfgObjectiveTable value) {
        setProperty("defaultContractDBID", value);
    }

    /**
     * A unique identifier of the cost contract (<code>
     * <a href="CfgObjectiveTable.html">CfgObjectiveTable</a>
     * </code>)
     * associated with this tenant.
     *
     * @param dbid DBID identifier of referred object
     * @see #getDefaultContract()
     */
    public final void setDefaultContractDBID(final int dbid) {
        setProperty("defaultContractDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the DefaultContract property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getDefaultContractDBID() {
        return getLinkValue("defaultContractDBID");
    }

    public final CfgTenant getParentTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "parentTenantDBID");
    }

    public final void setParentTenant(final CfgTenant value) {
        setProperty("parentTenantDBID", value);
    }

    public final void setParentTenantDBID(final int dbid) {
        setProperty("parentTenantDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the ParentTenant property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getParentTenantDBID() {
        return getLinkValue("parentTenantDBID");
    }
}
