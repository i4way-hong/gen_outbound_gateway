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
 * <em>Ranks</em> allow Applications to control which of their functions to make
 * available to the currently logged-in Person.
 *
 * <p/>
 * Due to the introduction of a flexible access control
 * system in Configuration Server version 5.1.100, the only purpose
 * left for application ranks is to control what functionality of a
 * certain application is available to the currently logged-on person.
 * The decision on whether to use rank-based access to application's functions,
 * and what functions to block/enable for what rank shall be determined
 * exclusively by the feature requirements to that application type
 * and made part of the functional specification for that application
 * type. Level of access of a particular person to the Configuration
 * Database objects does not depend in any way on the set of application
 * ranks of that person.
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgAppRank extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgAppRank(
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
    public CfgAppRank(
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
    public CfgAppRank(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgPersonRank")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
    }

    /**
     * Type of the application this rank
     * relates to. May make sense for certain applications of the GUI type.
     * (See <code>
     * <a href="../Enumerations/CfgAppType.html">CfgAppType</a>
     * </code> and the comment below.) Mandatory.
     * Once specified, cannot be changed. The same value cannot be repeated
     * within one list.
     *
     * @return property value or null
     */
    public final CfgAppType getAppType() {
        return (CfgAppType) getProperty(CfgAppType.class, "appType");
    }

    /**
     * Type of the application this rank
     * relates to. May make sense for certain applications of the GUI type.
     * (See <code>
     * <a href="../Enumerations/CfgAppType.html">CfgAppType</a>
     * </code> and the comment below.) Mandatory.
     * Once specified, cannot be changed. The same value cannot be repeated
     * within one list.
     *
     * @param value new property value
     * @see #getAppType()
     */
    public final void setAppType(final CfgAppType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("appType", value);
    }

    /**
     * Application rank. See type
     * <code>
     * <a href="../Enumerations/CfgRank.html">CfgRank</a>
     * </code>. Rank <code>CFGTenantAdministrator</code> cannot
     * be assigned to a person whose <code>tenantDBID</code> is <code>1</code>.
     * Ranks <code>CFGServiceAdministrator</code> and <code>CFGSuperAdministrator</code> cannot
     * be assigned to a person whose <code>tenantDBID</code> is not <code>1</code>.
     * See type <code>CfgRank</code>.
     *
     * @return property value or null
     */
    public final CfgRank getAppRank() {
        return (CfgRank) getProperty(CfgRank.class, "appRank");
    }

    /**
     * Application rank. See type
     * <code>
     * <a href="../Enumerations/CfgRank.html">CfgRank</a>
     * </code>. Rank <code>CFGTenantAdministrator</code> cannot
     * be assigned to a person whose <code>tenantDBID</code> is <code>1</code>.
     * Ranks <code>CFGServiceAdministrator</code> and <code>CFGSuperAdministrator</code> cannot
     * be assigned to a person whose <code>tenantDBID</code> is not <code>1</code>.
     * See type <code>CfgRank</code>.
     *
     * @param value new property value
     * @see #getAppRank()
     */
    public final void setAppRank(final CfgRank value) {
        setProperty("appRank", value);
    }
}
