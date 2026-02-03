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
 * <em>Campaigns</em> are structures for organizing and managing an automated
 * process of making outbound calls to the destinations specified in Calling Lists.
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaCampaign.html">
 * <b>CfgDeltaCampaign</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgCampaignGroup.html">
 * <b>CfgCampaignGroup</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgCallingList.html">
 * <b>CfgCallingList</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgCampaign
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGCampaign;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgCampaign(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgCampaign - "
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
    public CfgCampaign(
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
    public CfgCampaign(final IConfService confService) {
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
            if (getMetaData().getAttribute("callingLists") != null) {
                Collection<CfgCallingListInfo> callingLists = (Collection<CfgCallingListInfo>) getProperty("callingLists");
                if (callingLists != null) {
                    for (CfgCallingListInfo item : callingLists) {
                        item.checkPropertiesForSave();
                    }
                }
            }
            if (getMetaData().getAttribute("campaignGroups") != null) {
                Collection<CfgCampaignGroupInfo> campaignGroups = (Collection<CfgCampaignGroupInfo>) getProperty("campaignGroups");
                if (campaignGroups != null) {
                    for (CfgCampaignGroupInfo item : campaignGroups) {
                        item.checkPropertiesForSave();
                    }
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
     * to which this campaign is allocated. Mandatory. Once specified,
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
     * to which this campaign is allocated. Mandatory. Once specified,
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
     * to which this campaign is allocated. Mandatory. Once specified,
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
     * A pointer to the campaign name. Mandatory.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the campaign name. Mandatory.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * A pointer to the campaign description.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A pointer to the campaign description.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * A pointer to list of calling
     * lists info dedicated to campaign(every item of this list is structured
     * as <code>
     * <a href="CfgCallingListInfo.html">CfgCallingListInfo</a>
     * </code>).
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgCallingListInfo> getCallingLists() {
        return (Collection<CfgCallingListInfo>) getProperty("callingLists");
    }

    /**
     * A pointer to list of calling
     * lists info dedicated to campaign(every item of this list is structured
     * as <code>
     * <a href="CfgCallingListInfo.html">CfgCallingListInfo</a>
     * </code>).
     *
     * @param value new property value
     * @see #getCallingLists()
     */
    public final void setCallingLists(final Collection<CfgCallingListInfo> value) {
        setProperty("callingLists", value);
    }

    /**
     * Not in use starting from 7.5 release. Campaign Groups are represented as
     * <code>
     * <a href="CfgCampaignGroup.html">CfgCampaignGroup</a>
     * </code> objects under the Campaigns
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgCampaignGroupInfo> getCampaignGroups() {
        return (Collection<CfgCampaignGroupInfo>) getProperty("campaignGroups");
    }

    /**
     * Not in use starting from 7.5 release. Campaign Groups are represented as
     * <code>
     * <a href="CfgCampaignGroup.html">CfgCampaignGroup</a>
     * </code> objects under the Campaigns
     *
     * @param value new property value
     * @see #getCampaignGroups()
     */
    public final void setCampaignGroups(final Collection<CfgCampaignGroupInfo> value) {
        setProperty("campaignGroups", value);
    }

    /**
     * A unique identifier of the campaign
     * <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>. Optional.
     *
     * @return instance of referred object or null
     */
    public final CfgScript getScript() {
        return (CfgScript) getProperty(CfgScript.class, "scriptDBID");
    }

    /**
     * A unique identifier of the campaign
     * <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>. Optional.
     *
     * @param value new property value
     * @see #getScript()
     */
    public final void setScript(final CfgScript value) {
        setProperty("scriptDBID", value);
    }

    /**
     * A unique identifier of the campaign
     * <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>. Optional.
     *
     * @param dbid DBID identifier of referred object
     * @see #getScript()
     */
    public final void setScriptDBID(final int dbid) {
        setProperty("scriptDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Script property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getScriptDBID() {
        return getLinkValue("scriptDBID");
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
