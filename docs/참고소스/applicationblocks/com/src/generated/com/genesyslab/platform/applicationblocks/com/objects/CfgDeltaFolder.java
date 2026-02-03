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
 * <a href="CfgFolder.html">CfgFolder</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDeltaFolder extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaFolder(final IConfService confService) {
        super(confService, "CfgDeltaFolder");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaFolder(
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
    public CfgDeltaFolder(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgFolder configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgFolder configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgFolder retrieveCfgFolder() throws ConfigException {
        return (CfgFolder) (super.retrieveObject());
    }

    /**
     * A pointer to name of the folder. Mandatory.
     * Must be unique within the parent object.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * Type of the objects this folder may
     * contain. A folder may contain either objects of this type or subfolders
     * with the same value of <code>type</code> property. See the <code>
     * <a href="../Enumerations/CfgObjectType.html">CfgObjectType</a>
     * </code>
     * enumeration.
     *
     * @return property value or null
     */
    public final CfgObjectType getType() {
        return (CfgObjectType) getProperty(CfgObjectType.class, "type");
    }

    /**
     * A structure containing the object type
     * and DBID of the folder's owner object. Unlike
     * <code>parentID</code>, this field defines the folder's logical
     * affiliation rather than its hierarchical affiliation. An owner may be an object of
     * the following types: <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>,
     * <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>,
     * <code>
     * <a href="CfgIVR.html">CfgIVR</a>
     * </code>,
     * or <code>
     * <a href="CfgEnumerator.html">CfgEnumerator</a>
     * </code>.
     * See <code>
     * <a href="CfgID.html">CfgID</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgOwnerID getOwnerID() {
        return (CfgOwnerID) getProperty(CfgOwnerID.class, "ownerID");
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
     * Pointer to the list of
     * <code>
     * <a href="CfgObjectID.html">CfgObjectID</a>
     * </code> objects containing the type and DBID of the objects
     * subordinate to this folder. Only objects of the type equal to the
     * folder's type <code>property</code> or subfolders of this type may be contained in this list.
     *
     * @return list of structures or null
     */
    public final Collection<CfgObjectID> getAddedObjectIDs() {
        return (Collection<CfgObjectID>) getProperty("objectIDs");
    }

    /**
     * A structure containing object
     * type and DBID of the folder's parent, e.g. object which stands higher
     * in the hierarchy. This may be another folder, if this folder is
     * a subfolder, or this field may coincide with ownerID field if this
     * folder is a topmost (default) one. A parent may an object of the
     * following types:
     * <code>
     * <a href="CfgFolder.html">CfgFolder</a>
     * </code>,
     * <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>,
     * <code>
     * <a href="CfgSwitch.html">CfgSwitch</a>
     * </code>,
     * <code>
     * <a href="CfgIVR.html">CfgIVR</a>
     * </code>, or
     * <code>
     * <a href="CfgEnumerator.html">CfgEnumerator</a>
     * </code>.
     * See <code>
     * <a href="CfgID.html">CfgID</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgParentID getParentID() {
        return (CfgParentID) getProperty(CfgParentID.class, "parentID");
    }

    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * The class of the Folder.Refer to <code>
     * <a href="../Enumerations/CfgFolderClass.html">CfgFolderClass</a>
     * </code>
     * enumeration.
     *
     * @return property value or null
     */
    public final CfgFolderClass getFolderClass() {
        return (CfgFolderClass) getProperty(CfgFolderClass.class, "folderClass");
    }

    /**
     * User classificator of the Folder. Optional.
     *
     * @return property value or null
     */
    public final Integer getCustomType() {
        return (Integer) getProperty("customType");
    }

    /**
     * A pointer to the list of the objects associated with this Folder
     * (every item of this list is structured as
     * <code>
     * <a href="CfgObjectResource.html">CfgObjectResource</a>
     * </code>). When used as an entry
     * in <code>
     * <a href="CfgDeltaFolder.html">CfgDeltaFolder</a>
     * </code>, it is a pointer to a list of resources
     * added to the existing list.
     * Only objects of type <code>
     * <a href="CfgFolder.html">CfgFolder</a>
     * </code>,
     * <code>
     * <a href="CfgObjectiveTable.html">CfgObjectiveTable</a>
     * </code>,
     * <code>
     * <a href="CfgGVPIVRProfile.html">CfgGVPIVRProfile</a>
     * </code>,
     * <code>
     * <a href="CfgGVPCustomer.html">CfgGVPCustomer</a>
     * </code>,
     * <code>
     * <a href="CfgTimeZone.html">CfgTimeZone</a>
     * </code>,
     * <code>
     * <a href="CfgHost.html">CfgHost</a>
     * </code> can be associated with Folder object through <code>resources</code>
     *
     * @return list of structures or null
     */
    public final Collection<CfgObjectResource> getAddedResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
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

    /**
     * A pointer to a list of subordinate objects excluded from
     * the folder's container list. Contains structures of type <code>
     * <a href="CfgID.html">CfgID</a>
     * </code>.
     *
     * @return list of structures or null
     */
    public final Collection<CfgObjectID> getDeletedObjectIDs() {
        return (Collection<CfgObjectID>) getProperty("deletedObjectIDs");
    }

    /**
     * A pointer to the list of deleted resources (every
     * item of this list is structured as <code>
     * <a href="CfgID.html">CfgID</a>
     * </code>).
     *
     * @return list of structures or null
     */
    public final Collection<CfgResourceID> getDeletedResources() {
        return (Collection<CfgResourceID>) getProperty("deletedResources");
    }

    /**
     * A pointer to the list of structures
     * <code>
     * <a href="CfgObjectResource.html">CfgObjectResource</a>
     * </code> type. Each structure contains
     * information about the resource parameters that have been changed.
     *
     * @return list of structures or null
     */
    public final Collection<CfgObjectResource> getChangedResources() {
        return (Collection<CfgObjectResource>) getProperty("changedResources");
    }
}
