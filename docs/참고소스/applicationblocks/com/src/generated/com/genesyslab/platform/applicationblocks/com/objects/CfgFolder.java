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
 * <em>Folders</em> can be used to create hierarchies of other types of objects.
 * Each folder can contain a collection of objects of a single type.
 *
 * <ul type="bullet">
 * <li>A folder may contain objects of the type equal
 * to the folder's <code>type</code> property or subfolders of this
 * type.
 * </li>
 * <li>An object may be contained in one and only one folder (has
 * only one parent).
 * </li>
 * <li>A folder may be a subfolder of only one parent folder or does
 * not have a parent folder at all (be a default folder under some
 * parent object)
 * </li>
 * <li>There can not be more than one default folder of particular
 * type for some parent object.</li>
 * <li>A folder can not be removed as long as it has at least one
 * subordinate object.</li>
 * <li>A Configuration Unit is a GUI name for the folder of type <code>
 * <a href="CfgFolder.html">CfgFolder</a>
 * </code>.
 * Unlike other folders, this folder can not contain ordinary objects,
 * but may contain folders of any type including folders of type <code>
 * <a href="CfgFolder.html">CfgFolder</a>
 * </code> (Configuration
 * Units)</li>
 * <li>Configuration Units can only be created under the Tenant object
 * or other Configuration Unit.</li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaFolder.html">
 * <b>CfgDeltaFolder</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgFolder
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGFolder;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgFolder(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgFolder - "
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
    public CfgFolder(
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
    public CfgFolder(final IConfService confService) {
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
            if (getMetaData().getAttribute("ownerID") != null) {
                CfgOwnerID ownerID = (CfgOwnerID) getProperty("ownerID");
                if (ownerID != null) {
                    ownerID.checkPropertiesForSave();
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
                }
            }
            if (getMetaData().getAttribute("objectIDs") != null) {
                Collection<CfgObjectID> objectIDs = (Collection<CfgObjectID>) getProperty("objectIDs");
                if (objectIDs != null) {
                    for (CfgObjectID item : objectIDs) {
                        item.checkPropertiesForSave();
                    }
                }
            }
            if (getMetaData().getAttribute("parentID") != null) {
                CfgParentID parentID = (CfgParentID) getProperty("parentID");
                if (parentID != null) {
                    parentID.checkPropertiesForSave();
                }
            }
            if (getMetaData().getAttribute("folderClass") != null) {
                if (getProperty("folderClass") == null) {
                    setProperty("folderClass", 1);
                }
            }
            if (getMetaData().getAttribute("resources") != null) {
                Collection<CfgObjectResource> resources = (Collection<CfgObjectResource>) getProperty("resources");
                if (resources != null) {
                    for (CfgObjectResource item : resources) {
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
     * A pointer to name of the folder. Mandatory.
     * Must be unique within the parent object.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to name of the folder. Mandatory.
     * Must be unique within the parent object.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
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
     * Type of the objects this folder may
     * contain. A folder may contain either objects of this type or subfolders
     * with the same value of <code>type</code> property. See the <code>
     * <a href="../Enumerations/CfgObjectType.html">CfgObjectType</a>
     * </code>
     * enumeration.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgObjectType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
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
     * @param value new property value
     * @see #getOwnerID()
     */
    public final void setOwnerID(final CfgOwnerID value) {
        setProperty("ownerID", value);
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
     * Pointer to the list of
     * <code>
     * <a href="CfgObjectID.html">CfgObjectID</a>
     * </code> objects containing the type and DBID of the objects
     * subordinate to this folder. Only objects of the type equal to the
     * folder's type <code>property</code> or subfolders of this type may be contained in this list.
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgObjectID> getObjectIDs() {
        return (Collection<CfgObjectID>) getProperty("objectIDs");
    }

    /**
     * Pointer to the list of
     * <code>
     * <a href="CfgObjectID.html">CfgObjectID</a>
     * </code> objects containing the type and DBID of the objects
     * subordinate to this folder. Only objects of the type equal to the
     * folder's type <code>property</code> or subfolders of this type may be contained in this list.
     *
     * @param value new property value
     * @see #getObjectIDs()
     */
    public final void setObjectIDs(final Collection<CfgObjectID> value) {
        setProperty("objectIDs", value);
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

    public final void setDescription(final String value) {
        setProperty("description", value);
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
     * The class of the Folder.Refer to <code>
     * <a href="../Enumerations/CfgFolderClass.html">CfgFolderClass</a>
     * </code>
     * enumeration.
     *
     * @param value new property value
     * @see #getFolderClass()
     */
    public final void setFolderClass(final CfgFolderClass value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("folderClass", value);
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
     * User classificator of the Folder. Optional.
     *
     * @param value new property value
     * @see #getCustomType()
     */
    public final void setCustomType(final Integer value) {
        setProperty("customType", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgObjectResource> getResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
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
     * @param value new property value
     * @see #getResources()
     */
    public final void setResources(final Collection<CfgObjectResource> value) {
        setProperty("resources", value);
    }
}
