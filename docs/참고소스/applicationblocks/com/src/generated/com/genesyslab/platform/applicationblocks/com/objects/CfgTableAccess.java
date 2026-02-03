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
 * <em>Table Access</em> objects describe database tables of a specified Format
 * and explain how to access these tables through Database Access Points.
 *
 * <p/>
 * One TableAccess can be dedicated to several objects
 * of CfgCallingList type.
 * <p/>
 * A TableAccess cannot be deleted as long as it is associated
 * with at least one CallingList (see <code>
 * <a href="CfgCallingList.html">CfgCallingList</a>
 * </code>).
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaTableAccess.html">
 * <b>CfgDeltaTableAccess</b>
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
public class CfgTableAccess
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGTableAccess;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgTableAccess(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgTableAccess - "
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
    public CfgTableAccess(
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
    public CfgTableAccess(final IConfService confService) {
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
            if (getMetaData().getAttribute("dbAccessDBID") != null) {
                if (getLinkValue("dbAccessDBID") == null) {
                    throw new ConfigException("Mandatory property 'dbAccessDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("dbTableName") != null) {
                if (getProperty("dbTableName") == null) {
                    throw new ConfigException("Mandatory property 'dbTableName' not set.");
                }
            }
            if (getMetaData().getAttribute("isCachable") != null) {
                if (getProperty("isCachable") == null) {
                    throw new ConfigException("Mandatory property 'isCachable' not set.");
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
     * to which this table access is allocated. Mandatory. Once specified,
     * cannot be changed.
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
     * to which this table access is allocated. Mandatory. Once specified,
     * cannot be changed.
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
     * to which this table access is allocated. Mandatory. Once specified,
     * cannot be changed.
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
     * A pointer to the mnemonic name of
     * table access. Mandatory. Must be unique within the Tenant.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the mnemonic name of
     * table access. Mandatory. Must be unique within the Tenant.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * A type of table. Refer to <code>
     * <a href="../Enumerations/CfgTableType.html">CfgTableType</a>
     * </code> of
     * User Defined Variable Types. Mandatory. Once specified, cannot be
     * changed.
     *
     * @return property value or null
     */
    public final CfgTableType getType() {
        return (CfgTableType) getProperty(CfgTableType.class, "type");
    }

    /**
     * A type of table. Refer to <code>
     * <a href="../Enumerations/CfgTableType.html">CfgTableType</a>
     * </code> of
     * User Defined Variable Types. Mandatory. Once specified, cannot be
     * changed.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgTableType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
    }

    /**
     * A list pointer to the description
     * of table.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A list pointer to the description
     * of table.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgApplication.html">CfgApplication</a>
     * </code>
     * of type <code>CFGDBServer</code> (DB Access Point) through which the table can be accessed.
     * Mandatory.
     *
     * @return instance of referred object or null
     */
    public final CfgApplication getDbAccess() {
        return (CfgApplication) getProperty(CfgApplication.class, "dbAccessDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgApplication.html">CfgApplication</a>
     * </code>
     * of type <code>CFGDBServer</code> (DB Access Point) through which the table can be accessed.
     * Mandatory.
     *
     * @param value new property value
     * @see #getDbAccess()
     */
    public final void setDbAccess(final CfgApplication value) {
        setProperty("dbAccessDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgApplication.html">CfgApplication</a>
     * </code>
     * of type <code>CFGDBServer</code> (DB Access Point) through which the table can be accessed.
     * Mandatory.
     *
     * @param dbid DBID identifier of referred object
     * @see #getDbAccess()
     */
    public final void setDbAccessDBID(final int dbid) {
        setProperty("dbAccessDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the DbAccess property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getDbAccessDBID() {
        return getLinkValue("dbAccessDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFormat.html">CfgFormat</a>
     * </code>
     * of this table. Once specified cannot be changed. The property is
     * mandatory for all table types except
     * <code>CFGTTLogTable</code>. See <code>CfgTableType</code>.
     *
     * @return instance of referred object or null
     */
    public final CfgFormat getFormat() {
        return (CfgFormat) getProperty(CfgFormat.class, "formatDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFormat.html">CfgFormat</a>
     * </code>
     * of this table. Once specified cannot be changed. The property is
     * mandatory for all table types except
     * <code>CFGTTLogTable</code>. See <code>CfgTableType</code>.
     *
     * @param value new property value
     * @see #getFormat()
     */
    public final void setFormat(final CfgFormat value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("formatDBID", value);
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFormat.html">CfgFormat</a>
     * </code>
     * of this table. Once specified cannot be changed. The property is
     * mandatory for all table types except
     * <code>CFGTTLogTable</code>. See <code>CfgTableType</code>.
     *
     * @param dbid DBID identifier of referred object
     * @see #getFormat()
     */
    public final void setFormatDBID(final int dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("formatDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Format property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getFormatDBID() {
        return getLinkValue("formatDBID");
    }

    /**
     * A pointer to the name of table
     * in data base. Due to restrictions of some database engines the recommended
     * length for property name is 1 to 12 characters. Mandatory.
     *
     * @return property value or null
     */
    public final String getDbTableName() {
        return (String) getProperty("dbTableName");
    }

    /**
     * A pointer to the name of table
     * in data base. Due to restrictions of some database engines the recommended
     * length for property name is 1 to 12 characters. Mandatory.
     *
     * @param value new property value
     * @see #getDbTableName()
     */
    public final void setDbTableName(final String value) {
        setProperty("dbTableName", value);
    }

    /**
     * An indicator of whether the
     * table data shell be mirrored in application memory. See type <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgFlag getIsCachable() {
        return (CfgFlag) getProperty(CfgFlag.class, "isCachable");
    }

    /**
     * An indicator of whether the
     * table data shell be mirrored in application memory. See type <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>.
     *
     * @param value new property value
     * @see #getIsCachable()
     */
    public final void setIsCachable(final CfgFlag value) {
        setProperty("isCachable", value);
    }

    /**
     * A timeout between updates
     * of table data in application memory. Active if parameter <code>isCachable</code> set
     * to true only.
     *
     * @return property value or null
     */
    public final Integer getUpdateTimeout() {
        return (Integer) getProperty("updateTimeout");
    }

    /**
     * A timeout between updates
     * of table data in application memory. Active if parameter <code>isCachable</code> set
     * to true only.
     *
     * @param value new property value
     * @see #getUpdateTimeout()
     */
    public final void setUpdateTimeout(final Integer value) {
        setProperty("updateTimeout", value);
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
