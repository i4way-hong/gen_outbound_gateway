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
 * You can group Places if, according to the call-processing algorithms, the calls
 * have to be distributed among a set of Places under the control of CTI
 * applications rather than through the ACD mechanisms of the Switch.
 * <p/>
 * As an example, consider a call-parking service, where a routing application
 * transfers a call to one of the ports assigned to a call-parking Place Group
 * and attaches the information about the treatment to be applied to that call
 * while it is parked.
 *
 * <p/>
 * The name of a place group must be unique within the
 * tenant, but can coincide with the name of either an agent group
 * or a DN group of the same tenant.
 * <p/>
 * The name of a place group cannot be changed until there is
 * at least one place listed in this group. <code>See placeDBIDs property</code>
 * <p/>
 * Deletion of Place Group X will cause the following events
 * set out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>campaignGroups</code> of all campaigns
 * that included Place Group X
 * </li>
 * <li>
 * Deletion of Place Group X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaPlaceGroup.html">
 * <b>CfgDeltaPlaceGroup</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgPlace.html">
 * <b>CfgPlace</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgPlaceGroup
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGPlaceGroup;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgPlaceGroup(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgPlaceGroup - "
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
    public CfgPlaceGroup(
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
    public CfgPlaceGroup(final IConfService confService) {
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
     * <a href="CfgPlace.html">Places</a>
     * </code> that form this group. NOTE: Configuration Server
     * does not place any restrictions regarding the types of DNs in the
     * places that form a group.
     *
     * @return list of configuration objects or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgPlace> getPlaces() {
        return (Collection<CfgPlace>) getProperty("placeDBIDs");
    }

    /**
     * A pointer to the list of identifiers
     * of the <code>
     * <a href="CfgPlace.html">Places</a>
     * </code> that form this group. NOTE: Configuration Server
     * does not place any restrictions regarding the types of DNs in the
     * places that form a group.
     *
     * @param value new property value
     * @see #getPlaces()
     */
    public final void setPlaces(final Collection<CfgPlace> value) {
        setProperty("placeDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the Places property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getPlaceDBIDs() {
        return getLinkListCollection("placeDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the Places property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setPlaceDBIDs(final Collection<Integer> value) {
        setProperty("placeDBIDs", value);
    }
}
