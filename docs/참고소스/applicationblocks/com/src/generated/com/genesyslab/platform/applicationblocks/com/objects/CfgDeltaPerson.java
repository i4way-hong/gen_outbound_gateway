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
 * <a href="CfgPerson.html">CfgPerson</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDeltaPerson extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaPerson(final IConfService confService) {
        super(confService, "CfgDeltaPerson");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaPerson(
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
    public CfgDeltaPerson(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgPerson configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgPerson configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgPerson retrieveCfgPerson() throws ConfigException {
        return (CfgPerson) (super.retrieveObject());
    }

    /**
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code> whose employee this person is. Once specified, cannot be
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
     * A pointer to the person's last
     * name. Max length 64 symbols.
     *
     * @return property value or null
     */
    public final String getLastName() {
        return (String) getProperty("lastName");
    }

    /**
     * A pointer to the person's first
     * name. Max length 64 symbols.
     *
     * @return property value or null
     */
    public final String getFirstName() {
        return (String) getProperty("firstName");
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
     * @return property value or null
     */
    public final CfgPhones getPhones() {
        return (CfgPhones) getProperty(CfgPhones.class, "phones");
    }

    /**
     * Not in use.
     *
     * @return property value or null
     */
    public final String getBirthdate() {
        return (String) getProperty("birthdate");
    }

    /**
     * Not in use.
     *
     * @return property value or null
     */
    public final String getComment() {
        return (String) getProperty("comment");
    }

    /**
     * A pointer to the code identifying
     * this person within the tenant staff. Mandatory. Must be unique within
     * the tenant. Max length 64 symbols.
     *
     * @return property value or null
     */
    public final String getEmployeeID() {
        return (String) getProperty("employeeID");
    }

    /**
     * A pointer to the name the person
     * uses to log into a CTI system. Mandatory. Must be unique within
     * the Configuration Database.
     *
     * @return property value or null
     */
    public final String getUserName() {
        return (String) getProperty("userName");
    }

    /**
     * A pointer to the password the
     * person uses to log into a CTI system. Max length 64 symbols.
     *
     * @return property value or null
     */
    public final String getPassword() {
        return (String) getProperty("password");
    }

    /**
     * A pointer to the list of the person's
     * ranks with respect to applications (every item of this list is structured
     * as <code>
     * <a href="CfgAppRank.html">CfgAppRank</a>
     * </code>). When used as an
     * entry in <code>CfgDeltaPerson</code>
     * (see below), it is a pointer to a list of the ranks added to the existing list.
     *
     * @return list of structures or null
     */
    public final Collection<CfgAppRank> getAddedAppRanks() {
        return (Collection<CfgAppRank>) getProperty("appRanks");
    }

    /**
     * An indicator of whether the person
     * is an agent. Read-only (set automatically according to the current
     * value of <code>agentInfo</code> below). See type <code>CfgFlag</code>.
     *
     * @return property value or null
     */
    public final CfgFlag getIsAgent() {
        return (CfgFlag) getProperty(CfgFlag.class, "isAgent");
    }

    /**
     * A pointer to the structure containing
     * agent-specific information. See structure
     * <code>
     * <a href="CfgAgentInfo.html">CfgAgentInfo</a>
     * </code>. Shall be specified if the
     * person is an agent and shall be set to <code>NULL</code> otherwise.
     * Once specified, cannot be set to <code>NULL</code>.
     *
     * @return property value or null
     */
    public final CfgAgentInfo getAgentInfo() {
        return (CfgAgentInfo) getProperty(CfgAgentInfo.class, "agentInfo");
    }

    /**
     * Not in use.
     *
     * @return property value or null
     */
    public final CfgFlag getIsAdmin() {
        return (CfgFlag) getProperty(CfgFlag.class, "isAdmin");
    }

    /**
     * Not in use.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgTenant> getAddedAssignedTenants() {
        return (Collection<CfgTenant>) getProperty("assignedTenantDBIDs");
    }

    /**
     * Not in use.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedAssignedTenantDBIDs() {
        return getLinkListCollection("assignedTenantDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedAssignedTenantDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getAssignedTenantDBIDs() {
        return getLinkListCollection("assignedTenantDBIDs");
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
     * A pointer to the email address
     * of this person. Max length 255 symbols.
     *
     * @return property value or null
     */
    public final String getEmailAddress() {
        return (String) getProperty("emailAddress");
    }

    /**
     * A pointer to the string used
     * to identify this person in the external systems. In particular,
     * this field used to store an identifier processed during the authentication
     * in the LDAP repositories.Max length 255 symbols.
     *
     * @return property value or null
     */
    public final String getExternalID() {
        return (String) getProperty("externalID");
    }

    /**
     * This field should reflect authentication method that used for the Person.
     * If external authentication is used this flag should be equal to CFGTrue.
     *
     * @return property value or null
     */
    public final CfgFlag getIsExternalAuth() {
        return (CfgFlag) getProperty(CfgFlag.class, "isExternalAuth");
    }

    /**
     * This field reflects pending request for user to reset his password on next login.
     *             If user has reset its password this field set to CFGTrue.
     *
     * @return property value or null
     */
    public final CfgFlag getChangePasswordOnNextLogin() {
        return (CfgFlag) getProperty(CfgFlag.class, "changePasswordOnNextLogin");
    }

    /**
     * This is internal field and it should not be changed. Server ignores attempt to modify this field.
     *
     * @return property value or null
     */
    public final Integer getPasswordHashAlgorithm() {
        return (Integer) getProperty("passwordHashAlgorithm");
    }

    /**
     * This field contains a timestamp of last known password change, made by user, 0 otherwise.
     *
     * @return property value or null
     */
    public final Integer getPasswordUpdatingDate() {
        return (Integer) getProperty("PasswordUpdatingDate");
    }

    /**
     * A pointer to the structure containing changes made to the
     * agent-specific information of the person. Makes sense only if the
     * person is an agent and shall be set to
     * <code>NULL</code> otherwise. See structure <code>
     * <a href="CfgDeltaAgentInfo.html">CfgDeltaAgentInfo</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgDeltaAgentInfo getDeltaAgentInfo() {
        return (CfgDeltaAgentInfo) getProperty(CfgDeltaAgentInfo.class, "deltaAgentInfo");
    }

    /**
     * A pointer to the list of deleted application types (every
     * item of this list has variable type <code>CfgAppType</code>).
     *
     * @return list of numeric values or null
     */
    public final Collection<CfgAppRank> getDeletedAppRanks() {
        return (Collection<CfgAppRank>) getProperty("deletedAppRanks");
    }

    /**
     * A pointer to the list of changed application ranks (every
     * item of this list is structured as <code>
     * <a href="CfgAppRank.html">CfgAppRank</a>
     * </code>).
     *
     * @return list of structures or null
     */
    public final Collection<CfgAppRank> getChangedAppRanks() {
        return (Collection<CfgAppRank>) getProperty("changedAppRanks");
    }

    /**
     * Not in use.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgTenant> getDeletedAssignedTenants() {
        return (Collection<CfgTenant>) getProperty("deletedAssignedTenantDBIDs");
    }

    /**
     * Not in use.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedAssignedTenantDBIDs() {
        return getLinkListCollection("deletedAssignedTenantDBIDs");
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
