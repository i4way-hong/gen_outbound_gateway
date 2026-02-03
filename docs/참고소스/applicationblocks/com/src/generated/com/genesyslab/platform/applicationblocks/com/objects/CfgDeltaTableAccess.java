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
 * The <code>CfgDeltaTableAccess</code> is applicable for
 * Configuration Library/Server release 5.1.5xx and later.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDeltaTableAccess extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaTableAccess(final IConfService confService) {
        super(confService, "CfgDeltaTableAccess");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaTableAccess(
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
    public CfgDeltaTableAccess(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgTableAccess configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgTableAccess configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgTableAccess retrieveCfgTableAccess() throws ConfigException {
        return (CfgTableAccess) (super.retrieveObject());
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
     * Retrieves the dbid of the object that is being linked to by the Tenant property.
     * Configuration server provides it only if the property value has been changed.
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
     * A list pointer to the description
     * of table.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
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
     * Retrieves the dbid of the object that is being linked to by the DbAccess property.
     * Configuration server provides it only if the property value has been changed.
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
     * Retrieves the dbid of the object that is being linked to by the Format property.
     * Configuration server provides it only if the property value has been changed.
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
