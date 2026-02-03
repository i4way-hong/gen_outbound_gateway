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
 * <em>Fields</em> are single pieces of data within more complex data structures
 * (for example, database records).
 * <p/>
 * Use Fields to define characteristics of the data in Formats. (Refer to the
 * Outbound Contact documentation set for more information.)
 *
 * <p/>
 * A field cannot be deleted as long as it is associated
 * with at least one format (see <code>
 * <a href="CfgFormat.html">CfgFormat</a>
 * </code>).
 * <p/>
 * Uniqueness of object is defined by combination of name and <code>fieldType</code> properties.
 * <p/>
 * The table below lists the outbound mandatory fields and
 * their default settings. Fields should be created as default objects
 * in Configuration Server under the <code>Environment</code> folder.
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <caption><font size="+1"><strong>Outbound Mandatory Fields &amp; Settings
 * </strong></font></caption>
 * <thead>
 * <tr bgcolor="#ccccff" class="tableheadingcolor">
 * <th align="left">fieldType</th>
 * <th align="left">name</th>
 * <th align="left">description</th>
 * <th align="left">isPrimary Key</th>
 * <th align="left">isUnique</th>
 * <th align="left">isNullable</th>
 * <th align="left">default Value</th>
 * <th align="left">type</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>CFGFTRecordID</td>
 * <td>record_id</td>
 * <td>Unique record identifier</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTPhone</td>
 * <td>contact_info</td>
 * <td>Contact Info</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>VARCHAR (128)</td>
 * </tr>
 * <tr>
 * <td>CFGFTPhoneType</td>
 * <td>contact_info_type</td>
 * <td>Contact Info Type</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>1= GctiCtTyHomePhone from GctiContactType of Gcti.h</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTRecordType</td>
 * <td>record_type</td>
 * <td>Record type</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>2= GctiRecTyGeneral from GctiRecordType of Gcti.h</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTRecordStatus</td>
 * <td>record_status</td>
 * <td>Record status</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>1= GctiRecStReady from GctiRecordStatus of Gcti.h</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTDialResult</td>
 * <td>call_result</td>
 * <td>Dial result</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>28 = GctiCSUnknown from GctiCallState of Gcti.h</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTNumberOfAttempts</td>
 * <td>attempt</td>
 * <td>Number of attempts has been made, excluding re-dials
 * in case of errors</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>0</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTScheduledTime</td>
 * <td>dial_sched_time</td>
 * <td>Time, when scheduled call must be done, seconds since midnight
 * of 01/01/1970</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTCallTime</td>
 * <td>call_time</td>
 * <td>Time when last call or dial attempt has been done, seconds
 * since midnight of 01/01/1970</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTFrom</td>
 * <td>daily_from</td>
 * <td>Earliest time to perform the call. Seconds from midnight.</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>28800 = 8AM</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTUntil</td>
 * <td>daily_till</td>
 * <td>Latest time to perform the call. Seconds from midnight</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>64800 = 6PM</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTTimeZone</td>
 * <td>tz_dbid</td>
 * <td>Time zone. DBID from Configuration Data Base.</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>122= ___PST___ DBID</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTCampaignID</td>
 * <td>campaign_id</td>
 * <td>DBID of the campaign with respect to the last dial
 * attempt has been made.</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTAgentID</td>
 * <td>agent_id</td>
 * <td>Agent login identifier</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>VARCHAR (32)</td>
 * </tr>
 * <tr>
 * <td>CFGFTChainID</td>
 * <td>chain_id</td>
 * <td>Unique identifier of chain</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTNumberInChain</td>
 * <td>chain_n</td>
 * <td>Unique identifier of record within chain</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>No</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTGroupDBID</td>
 * <td>group_id</td>
 * <td>AgentGroup or PlaceGroup unique identifier (DBID)</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTAppDBID</td>
 * <td>app_id</td>
 * <td>Application unique identifier(DBID)</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTTreatments</td>
 * <td>treatments</td>
 * <td>Treatments History</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>VARCHAR(255)</td>
 * </tr>
 * <tr>
 * <td>CFGFTMediaRefference</td>
 * <td>media_ref</td>
 * <td>Reference to media body to be sent in case of treatment</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTEmailSubject</td>
 * <td>email_subject</td>
 * <td>Email Subject</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>VARCHAR(255)</td>
 * </tr>
 * <tr>
 * <td>CFGFTEmailTemplateID</td>
 * <td>email_template_id</td>
 * <td>Email Template ID</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * <tr>
 * <td>CFGFTSwitchID</td>
 * <td>switch_id</td>
 * <td>Switch ID</td>
 * <td>No</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>No</td>
 * <td>INT</td>
 * </tr>
 * </tbody>
 * </table><br/>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaField.html">
 * <b>CfgDeltaField</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgFormat.html">
 * <b>CfgFormat</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgField
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGField;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgField(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgField - "
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
    public CfgField(
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
    public CfgField(final IConfService confService) {
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
            if (getMetaData().getAttribute("type") != null) {
                if (getProperty("type") == null) {
                    throw new ConfigException("Mandatory property 'type' not set.");
                }
            }
            if (getMetaData().getAttribute("fieldType") != null) {
                if (getProperty("fieldType") == null) {
                    throw new ConfigException("Mandatory property 'fieldType' not set.");
                }
            }
            if (getMetaData().getAttribute("isPrimaryKey") != null) {
                if (getProperty("isPrimaryKey") == null) {
                    throw new ConfigException("Mandatory property 'isPrimaryKey' not set.");
                }
            }
            if (getMetaData().getAttribute("isUnique") != null) {
                if (getProperty("isUnique") == null) {
                    throw new ConfigException("Mandatory property 'isUnique' not set.");
                }
            }
            if (getMetaData().getAttribute("isNullable") != null) {
                if (getProperty("isNullable") == null) {
                    throw new ConfigException("Mandatory property 'isNullable' not set.");
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
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * that this field belongs to. Mandatory. Once specified, cannot be
     * changed.
     *
     * @return instance of referred object or null
     */
    public final CfgTenant getTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "tenantDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * that this field belongs to. Mandatory. Once specified, cannot be
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
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * that this field belongs to. Mandatory. Once specified, cannot be
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
     * A field name in data base. Due to
     * restrictions of database engine the recommended length for property
     * name is 1-12 characters. Mandatory, once specified cannot be changed.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A field name in data base. Due to
     * restrictions of database engine the recommended length for property
     * name is 1-12 characters. Mandatory, once specified cannot be changed.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("name", value);
    }

    /**
     * A data type of field in data base.
     * Mandatory, once specified cannot be changed. See <code>
     * <a href="../Enumerations/CfgDataType.html">CfgDataType</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgDataType getType() {
        return (CfgDataType) getProperty(CfgDataType.class, "type");
    }

    /**
     * A data type of field in data base.
     * Mandatory, once specified cannot be changed. See <code>
     * <a href="../Enumerations/CfgDataType.html">CfgDataType</a>
     * </code>
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgDataType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
    }

    /**
     * A pointer to the description
     * of field. Optional
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A pointer to the description
     * of field. Optional
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * A length of field in data base.
     * Optional, once specified cannot be changed.
     *
     * @return property value or null
     */
    public final Integer getLength() {
        return (Integer) getProperty("length");
    }

    /**
     * A length of field in data base.
     * Optional, once specified cannot be changed.
     *
     * @param value new property value
     * @see #getLength()
     */
    public final void setLength(final Integer value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("length", value);
    }

    /**
     * A field type. Refer to <code>
     * <a href="../Enumerations/CfgFieldType.html">CfgFieldType</a>
     * </code> of
     * User Defined Variable types. Mandatory, once specified cannot be
     * changed.
     *
     * @return property value or null
     */
    public final CfgFieldType getFieldType() {
        return (CfgFieldType) getProperty(CfgFieldType.class, "fieldType");
    }

    /**
     * A field type. Refer to <code>
     * <a href="../Enumerations/CfgFieldType.html">CfgFieldType</a>
     * </code> of
     * User Defined Variable types. Mandatory, once specified cannot be
     * changed.
     *
     * @param value new property value
     * @see #getFieldType()
     */
    public final void setFieldType(final CfgFieldType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("fieldType", value);
    }

    /**
     * A pointer to the default
     * value of field. Specify what value to insert when a user does not
     * enter a value. Optional.
     *
     * @return property value or null
     */
    public final String getDefaultValue() {
        return (String) getProperty("defaultValue");
    }

    /**
     * A pointer to the default
     * value of field. Specify what value to insert when a user does not
     * enter a value. Optional.
     *
     * @param value new property value
     * @see #getDefaultValue()
     */
    public final void setDefaultValue(final String value) {
        setProperty("defaultValue", value);
    }

    /**
     * A flag which determines whether
     * or not a field is used as primary key. Once specified cannot be
     * changed. See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getIsPrimaryKey() {
        return (CfgFlag) getProperty(CfgFlag.class, "isPrimaryKey");
    }

    /**
     * A flag which determines whether
     * or not a field is used as primary key. Once specified cannot be
     * changed. See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getIsPrimaryKey()
     */
    public final void setIsPrimaryKey(final CfgFlag value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("isPrimaryKey", value);
    }

    /**
     * A flag which determines whether
     * or not a field is used as unique. Once specified cannot be changed.
     * See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getIsUnique() {
        return (CfgFlag) getProperty(CfgFlag.class, "isUnique");
    }

    /**
     * A flag which determines whether
     * or not a field is used as unique. Once specified cannot be changed.
     * See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getIsUnique()
     */
    public final void setIsUnique(final CfgFlag value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("isUnique", value);
    }

    /**
     * A flag which determines whether
     * or not a field can allow null values ( NULLs ) for the data in that
     * field. Once specified cannot be changed.
     * See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getIsNullable() {
        return (CfgFlag) getProperty(CfgFlag.class, "isNullable");
    }

    /**
     * A flag which determines whether
     * or not a field can allow null values ( NULLs ) for the data in that
     * field. Once specified cannot be changed.
     * See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getIsNullable()
     */
    public final void setIsNullable(final CfgFlag value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("isNullable", value);
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
}
