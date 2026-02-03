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
 * A <em>GVP Voice Application Profile</em> is used by a GVP to define an voice application that can be executed upon request. Each GVP Voice Application is associated with one Tenant.
 * <br/>&lt;cfg-errors&gt;<br/>
 * &nbsp;&nbsp;&lt;cfg-error id="CFGObjectNotFound"&gt;Specified CfgGVPIVRProfile is not found (change/delete)&lt;/cfg-error&gt;<br/>
 * &nbsp;&nbsp;&lt;cfg-error id="CFGUniquenessViolation"&gt;CfgGVPIVRProfile already exists (Add)&lt;/cfg-error&gt;<br/>
 * &nbsp;&nbsp;&lt;cfg-error id="CFGIncompleteObjectData"&gt;submited CfgGVPIVRProfile does not have any or all of mandatory fields filled (add/change)&lt;/cfg-error&gt;<br/>
 * &lt;/cfg-errors&gt;
 *
 * 
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaGVPIVRProfile.html">
 * <b>CfgDeltaGVPIVRProfile</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgGVPIVRProfile
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGGVPIVRProfile;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgGVPIVRProfile(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgGVPIVRProfile - "
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
    public CfgGVPIVRProfile(
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
    public CfgGVPIVRProfile(final IConfService confService) {
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
            if (getMetaData().getAttribute("displayName") != null) {
                if (getProperty("displayName") == null) {
                    throw new ConfigException("Mandatory property 'displayName' not set.");
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
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
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this GVP Voice Application Profile is allocated. Populated upon creaton. Read-only
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
     * to which this GVP Voice Application Profile is allocated. Populated upon creaton. Read-only
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
     * to which this GVP Voice Application Profile is allocated. Populated upon creaton. Read-only
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
     * This property is obsolete and is not in use.
     *
     * @return instance of referred object or null
     */
    public final CfgGVPCustomer getCustomer() {
        return (CfgGVPCustomer) getProperty(CfgGVPCustomer.class, "customerDBID");
    }

    /**
     * This property is obsolete and is not in use.
     *
     * @return instance of referred object or null
     */
    public final CfgGVPReseller getReseller() {
        return (CfgGVPReseller) getProperty(CfgGVPReseller.class, "resellerDBID");
    }

    /**
     * A pointer to name of the GVP Voice Application Profile. Mandatory.
     * Must be unique within the Tenant.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to name of the GVP Voice Application Profile. Mandatory.
     * Must be unique within the Tenant.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * A name for this GVP IVR Profile that appears on the console display.
     *
     * @return property value or null
     */
    public final String getDisplayName() {
        return (String) getProperty("displayName");
    }

    /**
     * A name for this GVP IVR Profile that appears on the console display.
     *
     * @param value new property value
     * @see #getDisplayName()
     */
    public final void setDisplayName(final String value) {
        setProperty("displayName", value);
    }

    /**
     * This property is obsolete.
     *
     * @return property value or null
     */
    public final CfgIVRProfileType getType() {
        return (CfgIVRProfileType) getProperty(CfgIVRProfileType.class, "type");
    }

    /**
     * This property is obsolete.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgIVRProfileType value) {
        setProperty("type", value);
    }

    /**
     * Optional notes and information relevant for this GVP Voice Application Profile.
     *
     * @return property value or null
     */
    public final String getNotes() {
        return (String) getProperty("notes");
    }

    /**
     * Optional notes and information relevant for this GVP Voice Application Profile.
     *
     * @param value new property value
     * @see #getNotes()
     */
    public final void setNotes(final String value) {
        setProperty("notes", value);
    }

    /**
     * An optional short description of this GVP Voice Application Profile.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * An optional short description of this GVP Voice Application Profile.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * This property is obsolete.
     *
     * @return property value or null
     */
    public final Calendar getStartServiceDate() {
        return (Calendar) getProperty("startServiceDate");
    }

    /**
     * The date when this GVP IVR Profile ends. This date must be the same as, or later than, the Start of Service.
     *
     * @return property value or null
     */
    public final Calendar getEndServiceDate() {
        return (Calendar) getProperty("endServiceDate");
    }

    /**
     * This property is obsolete.
     *
     * @return property value or null
     */
    public final CfgFlag getIsProvisioned() {
        return (CfgFlag) getProperty(CfgFlag.class, "isProvisioned");
    }

    /**
     * This property is obsolete.
     *
     * @return property value or null
     */
    public final String getTfn() {
        return (String) getProperty("tfn");
    }

    /**
     * This property is obsolete.
     *
     * @return property value or null
     */
    public final String getStatus() {
        return (String) getProperty("status");
    }

    /**
     * This property is obsolete and is not in use.
     *
     * @return list of configuration objects or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgDN> getDIDs() {
        return (Collection<CfgDN>) getProperty("DIDDBIDs");
    }

    /**
     * This property is obsolete and is not in use.
     *
     * @param value new property value
     * @see #getDIDs()
     */
    public final void setDIDs(final Collection<CfgDN> value) {
        setProperty("DIDDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the DIDs property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDIDDBIDs() {
        return getLinkListCollection("DIDDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the DIDs property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setDIDDBIDs(final Collection<Integer> value) {
        setProperty("DIDDBIDs", value);
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

    @SuppressWarnings("unchecked")
    public final Collection<CfgObjectResource> getResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
    }

    public final void setResources(final Collection<CfgObjectResource> value) {
        setProperty("resources", value);
    }
}
