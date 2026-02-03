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
 * <em>CfgSkillLevel</em> indicates an Agent's level of proficiency in a
 * particular skill.
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgSkillLevel extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgSkillLevel(
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
    public CfgSkillLevel(
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
    public CfgSkillLevel(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgSkillLevel")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
            if (getMetaData().getAttribute("skillDBID") != null) {
                if (getLinkValue("skillDBID") == null) {
                    throw new ConfigException("Mandatory property 'skillDBID' not set.");
                }
            }
    }

    /**
     * A unique identifier of the skill
     * the level relates to. See <code>
     * <a href="CfgSkill.html">CfgSkill</a>
     * </code>. Mandatory.
     * Once specified, cannot be changed. This skill must belong to the
     * tenant that this agent belongs to. The same value cannot be repeated
     * within one list.
     *
     * @return instance of referred object or null
     */
    public final CfgSkill getSkill() {
        return (CfgSkill) getProperty(CfgSkill.class, "skillDBID");
    }

    /**
     * A unique identifier of the skill
     * the level relates to. See <code>
     * <a href="CfgSkill.html">CfgSkill</a>
     * </code>. Mandatory.
     * Once specified, cannot be changed. This skill must belong to the
     * tenant that this agent belongs to. The same value cannot be repeated
     * within one list.
     *
     * @param value new property value
     * @see #getSkill()
     */
    public final void setSkill(final CfgSkill value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("skillDBID", value);
    }

    /**
     * A unique identifier of the skill
     * the level relates to. See <code>
     * <a href="CfgSkill.html">CfgSkill</a>
     * </code>. Mandatory.
     * Once specified, cannot be changed. This skill must belong to the
     * tenant that this agent belongs to. The same value cannot be repeated
     * within one list.
     *
     * @param dbid DBID identifier of referred object
     * @see #getSkill()
     */
    public final void setSkillDBID(final int dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("skillDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Skill property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getSkillDBID() {
        return getLinkValue("skillDBID");
    }

    /**
     * Level of the skill. Cannot be a negative
     * value.
     *
     * @return property value or null
     */
    public final Integer getLevel() {
        return (Integer) getProperty("level");
    }

    /**
     * Level of the skill. Cannot be a negative
     * value.
     *
     * @param value new property value
     * @see #getLevel()
     */
    public final void setLevel(final Integer value) {
        setProperty("level", value);
    }
}
