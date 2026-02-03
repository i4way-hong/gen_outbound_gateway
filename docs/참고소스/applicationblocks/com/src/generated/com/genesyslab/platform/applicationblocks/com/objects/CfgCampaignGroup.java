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
 * <code>CfgCampaignGroup</code> objects contain information about Campaign
 * groups.
 *
 * 
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaCampaignGroup.html">
 * <b>CfgDeltaCampaignGroup</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgCampaign.html">
 * <b>CfgCampaign</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgAgentGroup.html">
 * <b>CfgAgentGroup</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgPlaceGroup.html">
 * <b>CfgPlaceGroup</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgCampaignGroup
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGCampaignGroup;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgCampaignGroup(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgCampaignGroup - "
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
    public CfgCampaignGroup(
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
    public CfgCampaignGroup(final IConfService confService) {
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
            if (getMetaData().getAttribute("campaignDBID") != null) {
                if (getLinkValue("campaignDBID") == null) {
                    throw new ConfigException("Mandatory property 'campaignDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("name") != null) {
                if (getProperty("name") == null) {
                    throw new ConfigException("Mandatory property 'name' not set.");
                }
            }
            if (getMetaData().getAttribute("dialMode") != null) {
                if (getProperty("dialMode") == null) {
                    setProperty("dialMode", 1);
                }
            }
            if (getMetaData().getAttribute("operationMode") != null) {
                if (getProperty("operationMode") == null) {
                    setProperty("operationMode", 1);
                }
            }
            if (getMetaData().getAttribute("minRecBuffSize") != null) {
                if (getProperty("minRecBuffSize") == null) {
                    setProperty("minRecBuffSize", 3);
                }
            }
            if (getMetaData().getAttribute("optMethod") != null) {
                if (getProperty("optMethod") == null) {
                    setProperty("optMethod", 1);
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
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgCampaign.html">CfgCampaign</a>
     * </code>
     * to which this campaign group is allocated. Mandatory. Once specified,
     * cannot be changed.
     *
     * @return instance of referred object or null
     */
    public final CfgCampaign getCampaign() {
        return (CfgCampaign) getProperty(CfgCampaign.class, "campaignDBID");
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgCampaign.html">CfgCampaign</a>
     * </code>
     * to which this campaign group is allocated. Mandatory. Once specified,
     * cannot be changed.
     *
     * @param value new property value
     * @see #getCampaign()
     */
    public final void setCampaign(final CfgCampaign value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("campaignDBID", value);
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgCampaign.html">CfgCampaign</a>
     * </code>
     * to which this campaign group is allocated. Mandatory. Once specified,
     * cannot be changed.
     *
     * @param dbid DBID identifier of referred object
     * @see #getCampaign()
     */
    public final void setCampaignDBID(final int dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("campaignDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Campaign property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getCampaignDBID() {
        return getLinkValue("campaignDBID");
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this campaign group is allocated. Read-only. Its value is populated from the associated Campaign object
     *
     * @return instance of referred object or null
     */
    public final CfgTenant getTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "tenantDBID");
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this campaign group is allocated. Read-only. Its value is populated from the associated Campaign object
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
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this campaign group is allocated. Read-only. Its value is populated from the associated Campaign object
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
     * A pointer to the campaign group name.
     * Mandatory. Must be unique within the Campaign.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the campaign group name.
     * Mandatory. Must be unique within the Campaign.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * <p/>
     * A unique identifier of the group
     * of <code>
     * <a href="CfgAgentGroup.html">Agents</a>
     * </code> or group of <code>
     * <a href="CfgPlaceGroup.html">Places</a>
     * </code>. Mandatory.
     *
     * @return property value or null
     */
    public final Integer getGroupDBID() {
        return (Integer) getProperty("groupDBID");
    }

    /**
     * <p/>
     * A unique identifier of the group
     * of <code>
     * <a href="CfgAgentGroup.html">Agents</a>
     * </code> or group of <code>
     * <a href="CfgPlaceGroup.html">Places</a>
     * </code>. Mandatory.
     *
     * @param value new property value
     * @see #getGroupDBID()
     */
    public final void setGroupDBID(final Integer value) {
        setProperty("groupDBID", value);
    }

    /**
     * <p/>
     * A group type. Read only. See <code>
     * <a href="../Enumerations/CfgObjectType.html">CfgObjectType</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgObjectType getGroupType() {
        return (CfgObjectType) getProperty(CfgObjectType.class, "groupType");
    }

    /**
     * <p/>
     * A group type. Read only. See <code>
     * <a href="../Enumerations/CfgObjectType.html">CfgObjectType</a>
     * </code>
     *
     * @param value new property value
     * @see #getGroupType()
     */
    public final void setGroupType(final CfgObjectType value) {
        setProperty("groupType", value);
    }

    /**
     * A pointer to campaign group
     * description.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A pointer to campaign group
     * description.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * <p/>
     * A pointer to the list of unique identifiers to <code>
     * <a href="CfgApplication.html">Application</a>
     * </code> objects. Optional. Will be used to configure connectivity to Servers associated with this Campaign.
     * Only one Application of specific application type (<code>CfgAppType</code>) is allowed in the list.
     *
     * @return list of configuration objects or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgApplication> getServers() {
        return (Collection<CfgApplication>) getProperty("serverDBIDs");
    }

    /**
     * <p/>
     * A pointer to the list of unique identifiers to <code>
     * <a href="CfgApplication.html">Application</a>
     * </code> objects. Optional. Will be used to configure connectivity to Servers associated with this Campaign.
     * Only one Application of specific application type (<code>CfgAppType</code>) is allowed in the list.
     *
     * @param value new property value
     * @see #getServers()
     */
    public final void setServers(final Collection<CfgApplication> value) {
        setProperty("serverDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the Servers property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getServerDBIDs() {
        return getLinkListCollection("serverDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the Servers property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setServerDBIDs(final Collection<Integer> value) {
        setProperty("serverDBIDs", value);
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgGVPIVRProfile.html"></a>
     * </code> object
     * Optional.
     *
     * @return instance of referred object or null
     */
    public final CfgGVPIVRProfile getIVRProfile() {
        return (CfgGVPIVRProfile) getProperty(CfgGVPIVRProfile.class, "IVRProfileDBID");
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgGVPIVRProfile.html"></a>
     * </code> object
     * Optional.
     *
     * @param value new property value
     * @see #getIVRProfile()
     */
    public final void setIVRProfile(final CfgGVPIVRProfile value) {
        setProperty("IVRProfileDBID", value);
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgGVPIVRProfile.html"></a>
     * </code> object
     * Optional.
     *
     * @param dbid DBID identifier of referred object
     * @see #getIVRProfile()
     */
    public final void setIVRProfileDBID(final int dbid) {
        setProperty("IVRProfileDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the IVRProfile property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getIVRProfileDBID() {
        return getLinkValue("IVRProfileDBID");
    }

    /**
     * <p/>
     * A dial mode dedicated for this
     * group. Default value is <code>CFGDMPredict.</code> See type <code>
     * <a href="../Enumerations/CfgDialMode.html">CfgDialMode</a>
     * </code>.
     * Mandatory.
     *
     * @return property value or null
     */
    public final CfgDialMode getDialMode() {
        return (CfgDialMode) getProperty(CfgDialMode.class, "dialMode");
    }

    /**
     * <p/>
     * A dial mode dedicated for this
     * group. Default value is <code>CFGDMPredict.</code> See type <code>
     * <a href="../Enumerations/CfgDialMode.html">CfgDialMode</a>
     * </code>.
     * Mandatory.
     *
     * @param value new property value
     * @see #getDialMode()
     */
    public final void setDialMode(final CfgDialMode value) {
        setProperty("dialMode", value);
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgDN.html">DN</a>
     * </code>
     * where the dialing should be performed from. DNs of following types
     * can be used to specify this parameter:
     * <code>CFGACDQueue</code> and<code> CFGRoutingPoint.</code> Refer
     * to <code>CfgDNType</code> of User Defined Variable Types. Optional.
     *
     * @return instance of referred object or null
     */
    public final CfgDN getOrigDN() {
        return (CfgDN) getProperty(CfgDN.class, "origDNDBID");
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgDN.html">DN</a>
     * </code>
     * where the dialing should be performed from. DNs of following types
     * can be used to specify this parameter:
     * <code>CFGACDQueue</code> and<code> CFGRoutingPoint.</code> Refer
     * to <code>CfgDNType</code> of User Defined Variable Types. Optional.
     *
     * @param value new property value
     * @see #getOrigDN()
     */
    public final void setOrigDN(final CfgDN value) {
        setProperty("origDNDBID", value);
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgDN.html">DN</a>
     * </code>
     * where the dialing should be performed from. DNs of following types
     * can be used to specify this parameter:
     * <code>CFGACDQueue</code> and<code> CFGRoutingPoint.</code> Refer
     * to <code>CfgDNType</code> of User Defined Variable Types. Optional.
     *
     * @param dbid DBID identifier of referred object
     * @see #getOrigDN()
     */
    public final void setOrigDNDBID(final int dbid) {
        setProperty("origDNDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the OrigDN property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getOrigDNDBID() {
        return getLinkValue("origDNDBID");
    }

    /**
     * Maximum number of outbound channels that can be used by this
     * group at one time. Default value is 10. Mandatory.
     *
     * @return property value or null
     */
    public final Integer getNumOfChannels() {
        return (Integer) getProperty("numOfChannels");
    }

    /**
     * Maximum number of outbound channels that can be used by this
     * group at one time. Default value is 10. Mandatory.
     *
     * @param value new property value
     * @see #getNumOfChannels()
     */
    public final void setNumOfChannels(final Integer value) {
        setProperty("numOfChannels", value);
    }

    /**
     * <p/>
     * An operation mode. Default value is <code>CFGOMManual</code> .
     * Refer to <code>
     * <a href="../Enumerations/CfgOperationMode.html">CfgOperationMode</a>
     * </code> of User Defined Variable Types. Mandatory.
     *
     * @return property value or null
     */
    public final CfgOperationMode getOperationMode() {
        return (CfgOperationMode) getProperty(CfgOperationMode.class, "operationMode");
    }

    /**
     * <p/>
     * An operation mode. Default value is <code>CFGOMManual</code> .
     * Refer to <code>
     * <a href="../Enumerations/CfgOperationMode.html">CfgOperationMode</a>
     * </code> of User Defined Variable Types. Mandatory.
     *
     * @param value new property value
     * @see #getOperationMode()
     */
    public final void setOperationMode(final CfgOperationMode value) {
        setProperty("operationMode", value);
    }

    /**
     * A record buffering parameter. Default value is
     * <code>4.</code> Cannot be set to <code>0.</code> Mandatory.
     *
     * @return property value or null
     */
    public final Integer getMinRecBuffSize() {
        return (Integer) getProperty("minRecBuffSize");
    }

    /**
     * A record buffering parameter. Default value is
     * <code>4.</code> Cannot be set to <code>0.</code> Mandatory.
     *
     * @param value new property value
     * @see #getMinRecBuffSize()
     */
    public final void setMinRecBuffSize(final Integer value) {
        setProperty("minRecBuffSize", value);
    }

    /**
     * A record buffering parameter. Default value is 6. Mandatory.
     * The value of this property must always be greater than <code>minRecBuffSize</code>.
     *
     * @return property value or null
     */
    public final Integer getOptRecBuffSize() {
        return (Integer) getProperty("optRecBuffSize");
    }

    /**
     * A record buffering parameter. Default value is 6. Mandatory.
     * The value of this property must always be greater than <code>minRecBuffSize</code>.
     *
     * @param value new property value
     * @see #getOptRecBuffSize()
     */
    public final void setOptRecBuffSize(final Integer value) {
        setProperty("optRecBuffSize", value);
    }

    /**
     * Maximal number of unprocessed Interactions submitted to Interaction Server or GVP OBN Manager. Optional.
     *
     * @return property value or null
     */
    public final Integer getMaxQueueSize() {
        return (Integer) getProperty("maxQueueSize");
    }

    /**
     * Maximal number of unprocessed Interactions submitted to Interaction Server or GVP OBN Manager. Optional.
     *
     * @param value new property value
     * @see #getMaxQueueSize()
     */
    public final void setMaxQueueSize(final Integer value) {
        setProperty("maxQueueSize", value);
    }

    /**
     * An optimization method. Default
     * value is <code>CFGOMBusyFactor</code>. Refer to <code>
     * <a href="../Enumerations/CfgOptimizationMethod.html">CfgOptimizationMethod</a>
     * </code>
     * of User Defined Variable Types. Mandatory.
     *
     * @return property value or null
     */
    public final CfgOptimizationMethod getOptMethod() {
        return (CfgOptimizationMethod) getProperty(CfgOptimizationMethod.class, "optMethod");
    }

    /**
     * An optimization method. Default
     * value is <code>CFGOMBusyFactor</code>. Refer to <code>
     * <a href="../Enumerations/CfgOptimizationMethod.html">CfgOptimizationMethod</a>
     * </code>
     * of User Defined Variable Types. Mandatory.
     *
     * @param value new property value
     * @see #getOptMethod()
     */
    public final void setOptMethod(final CfgOptimizationMethod value) {
        setProperty("optMethod", value);
    }

    /**
     * The value of optimization method specified by optMethod property.
     * Refer to <code>CFGOptimizationMethod</code> of User Defined Variable
     * Types for ranges and default values.
     *
     * @return property value or null
     */
    public final Integer getOptMethodValue() {
        return (Integer) getProperty("optMethodValue");
    }

    /**
     * The value of optimization method specified by optMethod property.
     * Refer to <code>CFGOptimizationMethod</code> of User Defined Variable
     * Types for ranges and default values.
     *
     * @param value new property value
     * @see #getOptMethodValue()
     */
    public final void setOptMethodValue(final Integer value) {
        setProperty("optMethodValue", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgScript.html">Script</a>
     * </code>
     * of type <code>CFGInteractionQueue</code> for this campaign group. Optional.
     *
     * @return instance of referred object or null
     */
    public final CfgScript getInteractionQueue() {
        return (CfgScript) getProperty(CfgScript.class, "interactionQueueDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgScript.html">Script</a>
     * </code>
     * of type <code>CFGInteractionQueue</code> for this campaign group. Optional.
     *
     * @param value new property value
     * @see #getInteractionQueue()
     */
    public final void setInteractionQueue(final CfgScript value) {
        setProperty("interactionQueueDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgScript.html">Script</a>
     * </code>
     * of type <code>CFGInteractionQueue</code> for this campaign group. Optional.
     *
     * @param dbid DBID identifier of referred object
     * @see #getInteractionQueue()
     */
    public final void setInteractionQueueDBID(final int dbid) {
        setProperty("interactionQueueDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the InteractionQueue property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getInteractionQueueDBID() {
        return getLinkValue("interactionQueueDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgScript.html">Script</a>
     * </code>
     * for group/campaign. Optional.
     *
     * @return instance of referred object or null
     */
    public final CfgScript getScript() {
        return (CfgScript) getProperty(CfgScript.class, "scriptDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgScript.html">Script</a>
     * </code>
     * for group/campaign. Optional.
     *
     * @param value new property value
     * @see #getScript()
     */
    public final void setScript(final CfgScript value) {
        setProperty("scriptDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgScript.html">Script</a>
     * </code>
     * for group/campaign. Optional.
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

    public final CfgDN getTrunkGroupDN() {
        return (CfgDN) getProperty(CfgDN.class, "trunkGroupDNDBID");
    }

    public final void setTrunkGroupDN(final CfgDN value) {
        setProperty("trunkGroupDNDBID", value);
    }

    public final void setTrunkGroupDNDBID(final int dbid) {
        setProperty("trunkGroupDNDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the TrunkGroupDN property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getTrunkGroupDNDBID() {
        return getLinkValue("trunkGroupDNDBID");
    }
}
