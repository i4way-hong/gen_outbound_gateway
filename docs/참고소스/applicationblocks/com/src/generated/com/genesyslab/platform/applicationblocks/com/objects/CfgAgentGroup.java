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
 * <em>Groups</em> of Agents are typically set up to provide particular sets of
 * contact center services.
 *
 * <p/>
 * The name of an agent group must be unique within the
 * tenant, but can coincide with the name of either a place group or
 * a DN group of the same tenant.
 * <p/>
 * The name of an agent group cannot be changed until there is
 * at least one agent listed in this group. <code>See agentDBIDs property</code>
 * <p/>
 * Deletion of Agent Group X will cause the following events
 * set out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>campaignGroups</code> of all campaigns
 * that included Agent Group X
 * </li>
 * <li>
 * Deletion of Agent Group X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaAgentGroup.html">
 * <b>CfgDeltaAgentGroup</b>
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
public class CfgAgentGroup
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGAgentGroup;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgAgentGroup(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgAgentGroup - "
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
    public CfgAgentGroup(
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
    public CfgAgentGroup(final IConfService confService) {
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
     * A pointer to the list of identifiers
     * of the <code>
     * <a href="CfgPerson.html">Agents</a>
     * </code> that form this group.
     *
     * @return list of configuration objects or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgPerson> getAgents() {
        return (Collection<CfgPerson>) getProperty("agentDBIDs");
    }

    /**
     * A pointer to the list of identifiers
     * of the <code>
     * <a href="CfgPerson.html">Agents</a>
     * </code> that form this group.
     *
     * @param value new property value
     * @see #getAgents()
     */
    public final void setAgents(final Collection<CfgPerson> value) {
        setProperty("agentDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the Agents property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAgentDBIDs() {
        return getLinkListCollection("agentDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the Agents property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setAgentDBIDs(final Collection<Integer> value) {
        setProperty("agentDBIDs", value);
    }
}
