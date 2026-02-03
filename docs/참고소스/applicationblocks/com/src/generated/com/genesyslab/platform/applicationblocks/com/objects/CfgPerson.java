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
 * <em>Persons</em> correspond to contact center personnel___including
 * agents___who need access to CTI applications.
 * <p/>
 * The Genesys Framework requires that every person who needs such access be
 * registered in the Configuration Database with an appropriate set of privileges.
 *
 * <p/>
 * Whether a new person is an agent or not shall be specified
 * at the time when the corresponding <code>
 * <a href="CfgPerson.html">CfgPerson</a>
 * </code> object
 * is created. It is not possible to change person's status from a
 * non-agent to an agent (or the other way around) once the <code>
 * <a href="CfgPerson.html">CfgPerson</a>
 * </code> object
 * has been created.
 * <p/>
 * Deletion of Person X will cause the following events set out
 * in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>agentDBIDs</code> of all agent groups
 * that included Person X as an agent
 * </li>
 * <li>
 * Modification of <code>memberIDs</code> of all access groups
 * that included Person X as a member
 * </li>
 * <li>
 * Deletion of Person X
 * </li>
 * </ul>
 * <p/>
 * A person cannot be deleted as long as it is associated as
 * an account with at least one daemon application (See <code>
 * <a href="CfgApplication.html">CfgApplication</a>
 * </code> and
 * <code>ConfSetAccount</code>).
 * <p/>
 * By default, access privileges of a new person will be set
 * according to the following rules:
 * <ul type="bullet">
 * <li>
 * Any non-agent of the Service Provider becomes
 * a member of the default access group <code>Administrators</code> of
 * the Service Provider.
 * </li>
 * <li>
 * Any agent of the Service Provider becomes a member of the
 * default access group <code>Users</code> of the Service Provider.
 * </li>
 * <li>
 * Any non-agent of a particular tenant becomes a member of the
 * default access group <code>Administrators</code> of that tenant.
 * </li>
 * <li>
 * Any agent of a particular tenant becomes a member of the default
 * access group <code>Users</code> of that tenant.
 * </li>
 * <li>
 * Any person added to the Configuration Database is also considered
 * a member of the access group <code>Everyone</code>, which cannot
 * be changed. For specification of access privileges of the above
 * default groups, refer to comments to object
 * <code>CfgAccessGroup</code> in section <code>Access Control
 * Functions and Data Types</code>.
 * </li>
 * <li>
 * Person with <code>DBID</code> =
 * <code>100</code> and <code>tenantDBID</code> = <code>1</code> shall
 * be pre-defined (scripted) in the Configuration Database before Configuration
 * Server is started for the first time. This person has <code>Full
 * Control</code>
 * permissions with respect to all objects in the Configuration Database,
 * which cannot be changed. The object that represents this person
 * cannot be deleted.
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaPerson.html">
 * <b>CfgDeltaPerson</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgSkill.html">
 * <b>CfgSkill</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgAgentLogin.html">
 * <b>CfgAgentLogin</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgAgentGroup.html">
 * <b>CfgAgentGroup</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgPerson
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGPerson;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgPerson(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgPerson - "
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
    public CfgPerson(
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
    public CfgPerson(final IConfService confService) {
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
            if (getMetaData().getAttribute("address") != null) {
                CfgAddress address = (CfgAddress) getProperty("address");
                if (address != null) {
                    address.checkPropertiesForSave();
                }
            }
            if (getMetaData().getAttribute("phones") != null) {
                CfgPhones phones = (CfgPhones) getProperty("phones");
                if (phones != null) {
                    phones.checkPropertiesForSave();
                }
            }
            if (getMetaData().getAttribute("employeeID") != null) {
                if (getProperty("employeeID") == null) {
                    throw new ConfigException("Mandatory property 'employeeID' not set.");
                }
            }
            if (getMetaData().getAttribute("userName") != null) {
                if (getProperty("userName") == null) {
                    throw new ConfigException("Mandatory property 'userName' not set.");
                }
            }
            if (getMetaData().getAttribute("appRanks") != null) {
                Collection<CfgAppRank> appRanks = (Collection<CfgAppRank>) getProperty("appRanks");
                if (appRanks != null) {
                    for (CfgAppRank item : appRanks) {
                        item.checkPropertiesForSave();
                    }
                }
            }
            if (getMetaData().getAttribute("agentInfo") != null) {
                CfgAgentInfo agentInfo = (CfgAgentInfo) getProperty("agentInfo");
                if (agentInfo != null) {
                    agentInfo.checkPropertiesForSave();
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
     * </code> whose employee this person is. Once specified, cannot be
     * changed.
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
     * </code> whose employee this person is. Once specified, cannot be
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
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code> whose employee this person is. Once specified, cannot be
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
     * A pointer to the person's last
     * name. Max length 64 symbols.
     *
     * @return property value or null
     */
    public final String getLastName() {
        return (String) getProperty("lastName");
    }

    /**
     * A pointer to the person's last
     * name. Max length 64 symbols.
     *
     * @param value new property value
     * @see #getLastName()
     */
    public final void setLastName(final String value) {
        setProperty("lastName", value);
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
     * A pointer to the person's first
     * name. Max length 64 symbols.
     *
     * @param value new property value
     * @see #getFirstName()
     */
    public final void setFirstName(final String value) {
        setProperty("firstName", value);
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
     * @param value new property value
     * @see #getPhones()
     */
    public final void setPhones(final CfgPhones value) {
        setProperty("phones", value);
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
     * A pointer to the code identifying
     * this person within the tenant staff. Mandatory. Must be unique within
     * the tenant. Max length 64 symbols.
     *
     * @param value new property value
     * @see #getEmployeeID()
     */
    public final void setEmployeeID(final String value) {
        setProperty("employeeID", value);
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
     * A pointer to the name the person
     * uses to log into a CTI system. Mandatory. Must be unique within
     * the Configuration Database.
     *
     * @param value new property value
     * @see #getUserName()
     */
    public final void setUserName(final String value) {
        setProperty("userName", value);
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
     * A pointer to the password the
     * person uses to log into a CTI system. Max length 64 symbols.
     *
     * @param value new property value
     * @see #getPassword()
     */
    public final void setPassword(final String value) {
        setProperty("password", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgAppRank> getAppRanks() {
        return (Collection<CfgAppRank>) getProperty("appRanks");
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
     * @param value new property value
     * @see #getAppRanks()
     */
    public final void setAppRanks(final Collection<CfgAppRank> value) {
        setProperty("appRanks", value);
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
     * An indicator of whether the person
     * is an agent. Read-only (set automatically according to the current
     * value of <code>agentInfo</code> below). See type <code>CfgFlag</code>.
     *
     * @param value new property value
     * @see #getIsAgent()
     */
    public final void setIsAgent(final CfgFlag value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("isAgent", value);
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
     * A pointer to the structure containing
     * agent-specific information. See structure
     * <code>
     * <a href="CfgAgentInfo.html">CfgAgentInfo</a>
     * </code>. Shall be specified if the
     * person is an agent and shall be set to <code>NULL</code> otherwise.
     * Once specified, cannot be set to <code>NULL</code>.
     *
     * @param value new property value
     * @see #getAgentInfo()
     */
    public final void setAgentInfo(final CfgAgentInfo value) {
        setProperty("agentInfo", value);
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
     * A pointer to the email address
     * of this person. Max length 255 symbols.
     *
     * @return property value or null
     */
    public final String getEmailAddress() {
        return (String) getProperty("emailAddress");
    }

    /**
     * A pointer to the email address
     * of this person. Max length 255 symbols.
     *
     * @param value new property value
     * @see #getEmailAddress()
     */
    public final void setEmailAddress(final String value) {
        setProperty("emailAddress", value);
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
     * A pointer to the string used
     * to identify this person in the external systems. In particular,
     * this field used to store an identifier processed during the authentication
     * in the LDAP repositories.Max length 255 symbols.
     *
     * @param value new property value
     * @see #getExternalID()
     */
    public final void setExternalID(final String value) {
        setProperty("externalID", value);
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
     * This field should reflect authentication method that used for the Person.
     * If external authentication is used this flag should be equal to CFGTrue.
     *
     * @param value new property value
     * @see #getIsExternalAuth()
     */
    public final void setIsExternalAuth(final CfgFlag value) {
        setProperty("isExternalAuth", value);
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
     * This field reflects pending request for user to reset his password on next login.
     *             If user has reset its password this field set to CFGTrue.
     *
     * @param value new property value
     * @see #getChangePasswordOnNextLogin()
     */
    public final void setChangePasswordOnNextLogin(final CfgFlag value) {
        setProperty("changePasswordOnNextLogin", value);
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
     * This is internal field and it should not be changed. Server ignores attempt to modify this field.
     *
     * @param value new property value
     * @see #getPasswordHashAlgorithm()
     */
    public final void setPasswordHashAlgorithm(final Integer value) {
        setProperty("passwordHashAlgorithm", value);
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
     * This field contains a timestamp of last known password change, made by user, 0 otherwise.
     *
     * @param value new property value
     * @see #getPasswordUpdatingDate()
     */
    public final void setPasswordUpdatingDate(final Integer value) {
        setProperty("PasswordUpdatingDate", value);
    }
}
