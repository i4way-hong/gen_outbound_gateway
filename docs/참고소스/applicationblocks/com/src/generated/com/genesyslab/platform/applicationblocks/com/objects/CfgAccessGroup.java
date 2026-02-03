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
 * <em>Access Groups</em> are groups of Persons who need to have the same set of permissions with respect to Configuration Database objects
 *
 * <p/>
 * The name of a access group must be unique within the tenant,
 * but can coincide with the names of dn groups, place groups and
 * agent groups of the same tenant.
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaAccessGroup.html">
 * <b>CfgDeltaAccessGroup</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgPerson.html">
 * <b>CfgPerson</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgAccessGroup
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGAccessGroup;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgAccessGroup(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgAccessGroup - "
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
    public CfgAccessGroup(
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
    public CfgAccessGroup(final IConfService confService) {
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
            if (getMetaData().getAttribute("memberIDs") != null) {
                Collection<CfgID> memberIDs = (Collection<CfgID>) getProperty("memberIDs");
                if (memberIDs != null) {
                    for (CfgID item : memberIDs) {
                        item.checkPropertiesForSave();
                    }
                }
            }
            if (getMetaData().getAttribute("type") != null) {
                if (getProperty("type") == null) {
                    setProperty("type", 1);
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
     * A pointer to the list of the Persons that
     * form this group (every item of this list is structured as <code>
     * <a href="CfgID.html">CfgID</a>
     * </code>).
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgID> getMemberIDs() {
        return (Collection<CfgID>) getProperty("memberIDs");
    }

    /**
     * A pointer to the list of the Persons that
     * form this group (every item of this list is structured as <code>
     * <a href="CfgID.html">CfgID</a>
     * </code>).
     *
     * @param value new property value
     * @see #getMemberIDs()
     */
    public final void setMemberIDs(final Collection<CfgID> value) {
        setProperty("memberIDs", value);
    }

    /**
     * Type of this Access Group. Once specified,
     * cannot be changed. Refer to <code>
     * <a href="../Enumerations/CfgAccessGroupType.html">CfgAccessGroupType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgAccessGroupType getType() {
        return (CfgAccessGroupType) getProperty(CfgAccessGroupType.class, "type");
    }

    /**
     * Type of this Access Group. Once specified,
     * cannot be changed. Refer to <code>
     * <a href="../Enumerations/CfgAccessGroupType.html">CfgAccessGroupType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgAccessGroupType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
    }
}
