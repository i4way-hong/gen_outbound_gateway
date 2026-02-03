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
 * You can set up <em>Groups</em> of DNs for use in network-level routing
 * algorithms and in some types of statistics. Consult solution-specific
 * documentation to see if you need to set up DN Groups.
 * <p/>
 * When you are specifying a DN Group, remember that the DNs comprising this group
 * must have the same telephony event model.
 *
 * <p/>
 * The name of a DN group must be unique within the tenant,
 * but can coincide with the names of access groups, place groups and
 * agent groups of the same tenant.
 * <p/>
 * The name of a DN group cannot be changed until there is at
 * least one DN listed in this group. <code>See DNs property</code>
 * <p/>
 * Deletion of DN Group X will cause the following events set
 * out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>groupDBID</code> of all DNs that used
 * DN Group X for number translation
 * </li>
 * <li>
 * Deletion of DN Group X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaDNGroup.html">
 * <b>CfgDeltaDNGroup</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgDN.html">
 * <b>CfgDN</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDNGroup
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGDNGroup;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgDNGroup(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgDNGroup - "
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
    public CfgDNGroup(
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
    public CfgDNGroup(final IConfService confService) {
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
            if (getMetaData().getAttribute("groupInfo") != null) {
                CfgGroup groupInfo = (CfgGroup) getProperty("groupInfo");
                if (groupInfo != null) {
                    groupInfo.checkPropertiesForSave();
                }
            }
            if (getMetaData().getAttribute("type") != null) {
                if (getProperty("type") == null) {
                    throw new ConfigException("Mandatory property 'type' not set.");
                }
            }
            if (getMetaData().getAttribute("DNs") != null) {
                Collection<CfgDNInfo> DNs = (Collection<CfgDNInfo>) getProperty("DNs");
                if (DNs != null) {
                    for (CfgDNInfo item : DNs) {
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
     * A pointer to the structure <code>
     * <a href="CfgGroup.html">CfgGroup</a>
     * </code> containing
     * general information about this group. Mandatory.
     *
     * @return property value or null
     */
    public final CfgGroup getGroupInfo() {
        return (CfgGroup) getProperty(CfgGroup.class, "groupInfo");
    }

    /**
     * A pointer to the structure <code>
     * <a href="CfgGroup.html">CfgGroup</a>
     * </code> containing
     * general information about this group. Mandatory.
     *
     * @param value new property value
     * @see #getGroupInfo()
     */
    public final void setGroupInfo(final CfgGroup value) {
        setProperty("groupInfo", value);
    }

    /**
     * Type of this DN group. Once specified,
     * cannot be changed. Refer to <code>
     * <a href="../Enumerations/CfgDNGroupType.html">CfgDNGroupType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgDNGroupType getType() {
        return (CfgDNGroupType) getProperty(CfgDNGroupType.class, "type");
    }

    /**
     * Type of this DN group. Once specified,
     * cannot be changed. Refer to <code>
     * <a href="../Enumerations/CfgDNGroupType.html">CfgDNGroupType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgDNGroupType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
    }

    /**
     * A pointer to the list of the DNs that
     * form this group (every item of this list is structured as <code>
     * <a href="CfgDNInfo.html">CfgDNInfo</a>
     * </code>).
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgDNInfo> getDNs() {
        return (Collection<CfgDNInfo>) getProperty("DNs");
    }

    /**
     * A pointer to the list of the DNs that
     * form this group (every item of this list is structured as <code>
     * <a href="CfgDNInfo.html">CfgDNInfo</a>
     * </code>).
     *
     * @param value new property value
     * @see #getDNs()
     */
    public final void setDNs(final Collection<CfgDNInfo> value) {
        setProperty("DNs", value);
    }
}
