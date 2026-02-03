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

@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgCampaignGroupInfo extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgCampaignGroupInfo(
            final IConfService confService,
            final ConfStructure objData,
            final ICfgObject parent) {
        super(confService, objData, parent);
    }

    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param xmlData XML object containing structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgCampaignGroupInfo(
            final IConfService confService,
            final Node xmlData,
            final ICfgObject parent) {
        super(confService, xmlData, parent);
    }

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgCampaignGroupInfo(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgCampaignGroupInfo")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
    }

    public final Integer getGroupDBID() {
        return (Integer) getProperty("groupDBID");
    }

    public final void setGroupDBID(final Integer value) {
        setProperty("groupDBID", value);
    }

    public final CfgObjectType getGroupType() {
        return (CfgObjectType) getProperty(CfgObjectType.class, "groupType");
    }

    public final void setGroupType(final CfgObjectType value) {
        setProperty("groupType", value);
    }

    public final String getDescription() {
        return (String) getProperty("description");
    }

    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    public final CfgApplication getDialer() {
        return (CfgApplication) getProperty(CfgApplication.class, "dialerDBID");
    }

    public final void setDialer(final CfgApplication value) {
        setProperty("dialerDBID", value);
    }

    public final void setDialerDBID(final int dbid) {
        setProperty("dialerDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Dialer property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getDialerDBID() {
        return getLinkValue("dialerDBID");
    }

    public final CfgApplication getStatServer() {
        return (CfgApplication) getProperty(CfgApplication.class, "statServerDBID");
    }

    public final void setStatServer(final CfgApplication value) {
        setProperty("statServerDBID", value);
    }

    public final void setStatServerDBID(final int dbid) {
        setProperty("statServerDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the StatServer property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getStatServerDBID() {
        return getLinkValue("statServerDBID");
    }

    public final CfgFlag getIsActive() {
        return (CfgFlag) getProperty(CfgFlag.class, "isActive");
    }

    public final void setIsActive(final CfgFlag value) {
        setProperty("isActive", value);
    }

    public final CfgDialMode getDialMode() {
        return (CfgDialMode) getProperty(CfgDialMode.class, "dialMode");
    }

    public final void setDialMode(final CfgDialMode value) {
        setProperty("dialMode", value);
    }

    public final CfgDN getOrigDN() {
        return (CfgDN) getProperty(CfgDN.class, "origDNDBID");
    }

    public final void setOrigDN(final CfgDN value) {
        setProperty("origDNDBID", value);
    }

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

    public final Integer getNumOfChannels() {
        return (Integer) getProperty("numOfChannels");
    }

    public final void setNumOfChannels(final Integer value) {
        setProperty("numOfChannels", value);
    }

    public final CfgOperationMode getOperationMode() {
        return (CfgOperationMode) getProperty(CfgOperationMode.class, "operationMode");
    }

    public final void setOperationMode(final CfgOperationMode value) {
        setProperty("operationMode", value);
    }

    public final Integer getMinRecBuffSize() {
        return (Integer) getProperty("minRecBuffSize");
    }

    public final void setMinRecBuffSize(final Integer value) {
        setProperty("minRecBuffSize", value);
    }

    public final Integer getOptRecBuffSize() {
        return (Integer) getProperty("optRecBuffSize");
    }

    public final void setOptRecBuffSize(final Integer value) {
        setProperty("optRecBuffSize", value);
    }

    public final CfgOptimizationMethod getOptMethod() {
        return (CfgOptimizationMethod) getProperty(CfgOptimizationMethod.class, "optMethod");
    }

    public final void setOptMethod(final CfgOptimizationMethod value) {
        setProperty("optMethod", value);
    }

    public final Integer getOptMethodValue() {
        return (Integer) getProperty("optMethodValue");
    }

    public final void setOptMethodValue(final Integer value) {
        setProperty("optMethodValue", value);
    }

    public final CfgScript getScript() {
        return (CfgScript) getProperty(CfgScript.class, "scriptDBID");
    }

    public final void setScript(final CfgScript value) {
        setProperty("scriptDBID", value);
    }

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
