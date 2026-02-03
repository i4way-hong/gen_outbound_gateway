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
 * <code>CfgDeltaCampaignGroup</code> is applicable for
 * Configuration Library/Server release 7.5 and later.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDeltaCampaignGroup extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaCampaignGroup(final IConfService confService) {
        super(confService, "CfgDeltaCampaignGroup");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaCampaignGroup(
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
    public CfgDeltaCampaignGroup(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgCampaignGroup configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgCampaignGroup configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgCampaignGroup retrieveCfgCampaignGroup() throws ConfigException {
        return (CfgCampaignGroup) (super.retrieveObject());
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
     * Retrieves the dbid of the object that is being linked to by the Campaign property.
     * Configuration server provides it only if the property value has been changed.
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
     * Retrieves the dbid of the object that is being linked to by the Tenant property.
     * Configuration server provides it only if the property value has been changed.
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
     * A pointer to campaign group
     * description.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
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
    public final Collection<CfgApplication> getAddedServers() {
        return (Collection<CfgApplication>) getProperty("serverDBIDs");
    }

    /**
     * <p/>
     * A pointer to the list of unique identifiers to <code>
     * <a href="CfgApplication.html">Application</a>
     * </code> objects. Optional. Will be used to configure connectivity to Servers associated with this Campaign.
     * Only one Application of specific application type (<code>CfgAppType</code>) is allowed in the list.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedServerDBIDs() {
        return getLinkListCollection("serverDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedServerDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getServerDBIDs() {
        return getLinkListCollection("serverDBIDs");
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
     * Retrieves the dbid of the object that is being linked to by the IVRProfile property.
     * Configuration server provides it only if the property value has been changed.
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
     * Retrieves the dbid of the object that is being linked to by the OrigDN property.
     * Configuration server provides it only if the property value has been changed.
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
     * A record buffering parameter. Default value is
     * <code>4.</code> Cannot be set to <code>0.</code> Mandatory.
     *
     * @return property value or null
     */
    public final Integer getMinRecBuffSize() {
        return (Integer) getProperty("minRecBuffSize");
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
     * Maximal number of unprocessed Interactions submitted to Interaction Server or GVP OBN Manager. Optional.
     *
     * @return property value or null
     */
    public final Integer getMaxQueueSize() {
        return (Integer) getProperty("maxQueueSize");
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
     * Retrieves the dbid of the object that is being linked to by the InteractionQueue property.
     * Configuration server provides it only if the property value has been changed.
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
     * Retrieves the dbid of the object that is being linked to by the Script property.
     * Configuration server provides it only if the property value has been changed.
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
     * A pointer to the list of user-defined properties.Parameter userProperties has the following structure: Each key-value pair of the primary list (TKVList *userProperties) uses the key for the name of a user-defined section, and the value for a secondary list, that also has the TKVList structure and specifies the properties defined within that section.
     *
     * @return property value or null
     */
    public final KeyValueCollection getAddedUserProperties() {
        return (KeyValueCollection) getProperty("userProperties");
    }

    public final CfgDN getTrunkGroupDN() {
        return (CfgDN) getProperty(CfgDN.class, "trunkGroupDNDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the TrunkGroupDN property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getTrunkGroupDNDBID() {
        return getLinkValue("trunkGroupDNDBID");
    }

    /**
     * A pointer to the list of deleted applications.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgApplication> getDeletedServers() {
        return (Collection<CfgApplication>) getProperty("deletedServerDBIDs");
    }

    /**
     * A pointer to the list of deleted applications.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedServerDBIDs() {
        return getLinkListCollection("deletedServerDBIDs");
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
