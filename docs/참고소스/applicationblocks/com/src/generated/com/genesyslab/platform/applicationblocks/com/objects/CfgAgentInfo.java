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
 * <em>CfgAgentInfo</em> contains information about an Agent.
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgAgentInfo extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgAgentInfo(
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
    public CfgAgentInfo(
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
    public CfgAgentInfo(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgAgentInfo")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
            if (getMetaData().getAttribute("skillLevels") != null) {
                Collection<CfgSkillLevel> skillLevels = (Collection<CfgSkillLevel>) getProperty("skillLevels");
                if (skillLevels != null) {
                    for (CfgSkillLevel item : skillLevels) {
                        item.checkPropertiesForSave();
                    }
                }
            }
            if (getMetaData().getAttribute("agentLogins") != null) {
                Collection<CfgAgentLoginInfo> agentLogins = (Collection<CfgAgentLoginInfo>) getProperty("agentLogins");
                if (agentLogins != null) {
                    for (CfgAgentLoginInfo item : agentLogins) {
                        item.checkPropertiesForSave();
                    }
                }
            }
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgPlace.html">Place</a>
     * </code>
     * assigned to this agent by default. The place must belong to the
     * same tenant as the person in question unless this Agent belongs
     * to the tenant Environment (with DBID=1)
     *
     * @return instance of referred object or null
     */
    public final CfgPlace getPlace() {
        return (CfgPlace) getProperty(CfgPlace.class, "placeDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgPlace.html">Place</a>
     * </code>
     * assigned to this agent by default. The place must belong to the
     * same tenant as the person in question unless this Agent belongs
     * to the tenant Environment (with DBID=1)
     *
     * @param value new property value
     * @see #getPlace()
     */
    public final void setPlace(final CfgPlace value) {
        setProperty("placeDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgPlace.html">Place</a>
     * </code>
     * assigned to this agent by default. The place must belong to the
     * same tenant as the person in question unless this Agent belongs
     * to the tenant Environment (with DBID=1)
     *
     * @param dbid DBID identifier of referred object
     * @see #getPlace()
     */
    public final void setPlaceDBID(final int dbid) {
        setProperty("placeDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Place property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getPlaceDBID() {
        return getLinkValue("placeDBID");
    }

    /**
     * A pointer to the list of the
     * agent's skill levels (every item of this list is structured as
     * <code>
     * <a href="CfgSkillLevel.html">CfgSkillLevel</a>
     * </code>). When used as an
     * entry in <code>
     * <a href="CfgDeltaAgentInfo.html">CfgDeltaAgentInfo</a>
     * </code>
     * (see below), it is a pointer to a list of skill levels added to the existing list.
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgSkillLevel> getSkillLevels() {
        return (Collection<CfgSkillLevel>) getProperty("skillLevels");
    }

    /**
     * A pointer to the list of the
     * agent's skill levels (every item of this list is structured as
     * <code>
     * <a href="CfgSkillLevel.html">CfgSkillLevel</a>
     * </code>). When used as an
     * entry in <code>
     * <a href="CfgDeltaAgentInfo.html">CfgDeltaAgentInfo</a>
     * </code>
     * (see below), it is a pointer to a list of skill levels added to the existing list.
     *
     * @param value new property value
     * @see #getSkillLevels()
     */
    public final void setSkillLevels(final Collection<CfgSkillLevel> value) {
        setProperty("skillLevels", value);
    }

    /**
     * A pointer to the list of the
     * agent logins assigned to this agent (every item of this list is
     * structured as <code>
     * <a href="CfgAgentLoginInfo.html">CfgAgentLoginInfo</a>
     * </code>).
     * When used as an entry in <code>
     * <a href="CfgDeltaAgentInfo.html">CfgDeltaAgentInfo</a>
     * </code>
     * (see below), it is a pointer to a list of agent logins added to the existing list.
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgAgentLoginInfo> getAgentLogins() {
        return (Collection<CfgAgentLoginInfo>) getProperty("agentLogins");
    }

    /**
     * A pointer to the list of the
     * agent logins assigned to this agent (every item of this list is
     * structured as <code>
     * <a href="CfgAgentLoginInfo.html">CfgAgentLoginInfo</a>
     * </code>).
     * When used as an entry in <code>
     * <a href="CfgDeltaAgentInfo.html">CfgDeltaAgentInfo</a>
     * </code>
     * (see below), it is a pointer to a list of agent logins added to the existing list.
     *
     * @param value new property value
     * @see #getAgentLogins()
     */
    public final void setAgentLogins(final Collection<CfgAgentLoginInfo> value) {
        setProperty("agentLogins", value);
    }

    /**
     * A unique identifier of the capacity rule (<code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>)
     * associated with this agent.
     *
     * @return instance of referred object or null
     */
    public final CfgScript getCapacityRule() {
        return (CfgScript) getProperty(CfgScript.class, "capacityRuleDBID");
    }

    /**
     * A unique identifier of the capacity rule (<code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>)
     * associated with this agent.
     *
     * @param value new property value
     * @see #getCapacityRule()
     */
    public final void setCapacityRule(final CfgScript value) {
        setProperty("capacityRuleDBID", value);
    }

    /**
     * A unique identifier of the capacity rule (<code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>)
     * associated with this agent.
     *
     * @param dbid DBID identifier of referred object
     * @see #getCapacityRule()
     */
    public final void setCapacityRuleDBID(final int dbid) {
        setProperty("capacityRuleDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the CapacityRule property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getCapacityRuleDBID() {
        return getLinkValue("capacityRuleDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFolder.html">Site</a>
     * </code> (CfgFolder) with which this Agent is associated.
     *
     * @return instance of referred object or null
     */
    public final CfgFolder getSite() {
        return (CfgFolder) getProperty(CfgFolder.class, "siteDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFolder.html">Site</a>
     * </code> (CfgFolder) with which this Agent is associated.
     *
     * @param value new property value
     * @see #getSite()
     */
    public final void setSite(final CfgFolder value) {
        setProperty("siteDBID", value);
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgFolder.html">Site</a>
     * </code> (CfgFolder) with which this Agent is associated.
     *
     * @param dbid DBID identifier of referred object
     * @see #getSite()
     */
    public final void setSiteDBID(final int dbid) {
        setProperty("siteDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Site property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getSiteDBID() {
        return getLinkValue("siteDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgObjectiveTable.html">Cost Contract</a>
     * </code> (CfgObjectiveTable) with which this Agent is associated.
     *
     * @return instance of referred object or null
     */
    public final CfgObjectiveTable getContract() {
        return (CfgObjectiveTable) getProperty(CfgObjectiveTable.class, "contractDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgObjectiveTable.html">Cost Contract</a>
     * </code> (CfgObjectiveTable) with which this Agent is associated.
     *
     * @param value new property value
     * @see #getContract()
     */
    public final void setContract(final CfgObjectiveTable value) {
        setProperty("contractDBID", value);
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgObjectiveTable.html">Cost Contract</a>
     * </code> (CfgObjectiveTable) with which this Agent is associated.
     *
     * @param dbid DBID identifier of referred object
     * @see #getContract()
     */
    public final void setContractDBID(final int dbid) {
        setProperty("contractDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Contract property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getContractDBID() {
        return getLinkValue("contractDBID");
    }
}
