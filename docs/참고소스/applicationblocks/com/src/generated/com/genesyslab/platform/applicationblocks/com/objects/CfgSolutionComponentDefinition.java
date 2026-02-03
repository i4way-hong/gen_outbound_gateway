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
 * <em>CfgSolutionComponentDefinition</em> lists the types of applications whose
 * functionality this solution uses.
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgSolutionComponentDefinition extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgSolutionComponentDefinition(
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
    public CfgSolutionComponentDefinition(
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
    public CfgSolutionComponentDefinition(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgSolutionComponentDefinition")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
            if (getMetaData().getAttribute("startupPriority") != null) {
                if (getProperty("startupPriority") == null) {
                    throw new ConfigException("Mandatory property 'startupPriority' not set.");
                }
            }
            if (getMetaData().getAttribute("isOptional") != null) {
                if (getProperty("isOptional") == null) {
                    throw new ConfigException("Mandatory property 'isOptional' not set.");
                }
            }
            if (getMetaData().getAttribute("type") != null) {
                if (getProperty("type") == null) {
                    throw new ConfigException("Mandatory property 'type' not set.");
                }
            }
            if (getMetaData().getAttribute("version") != null) {
                if (getProperty("version") == null) {
                    throw new ConfigException("Mandatory property 'version' not set.");
                }
            }
    }

    /**
     * <p/>
     * The default number of the solution component in a component
     * startup sequence within solution. StartupPriority value should be
     * used to determine the order in which components should be started
     * and stopped. The value defined as default could be changed at time
     * of SolutionComponent definition. Must be defined as positive integer.
     * Mandatory. Must be shown as the <code>READONLY</code> property in an object
     * of type <code>
     * <a href="CfgService.html">CfgService</a>
     * </code> (Solution).
     *
     * @return property value or null
     */
    public final Integer getStartupPriority() {
        return (Integer) getProperty("startupPriority");
    }

    /**
     * <p/>
     * The default number of the solution component in a component
     * startup sequence within solution. StartupPriority value should be
     * used to determine the order in which components should be started
     * and stopped. The value defined as default could be changed at time
     * of SolutionComponent definition. Must be defined as positive integer.
     * Mandatory. Must be shown as the <code>READONLY</code> property in an object
     * of type <code>
     * <a href="CfgService.html">CfgService</a>
     * </code> (Solution).
     *
     * @param value new property value
     * @see #getStartupPriority()
     */
    public final void setStartupPriority(final Integer value) {
        setProperty("startupPriority", value);
    }

    /**
     * <p/>
     * Determines whether this solution
     * component default value is optional. Recommended to be set to
     * <code>CFGFalse</code> by default. Must be shown as <code>READONLY</code> property
     * in object of type <code>
     * <a href="CfgService.html">CfgService</a>
     * </code> (Solution).
     * Refers to <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getIsOptional() {
        return (CfgFlag) getProperty(CfgFlag.class, "isOptional");
    }

    /**
     * <p/>
     * Determines whether this solution
     * component default value is optional. Recommended to be set to
     * <code>CFGFalse</code> by default. Must be shown as <code>READONLY</code> property
     * in object of type <code>
     * <a href="CfgService.html">CfgService</a>
     * </code> (Solution).
     * Refers to <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getIsOptional()
     */
    public final void setIsOptional(final CfgFlag value) {
        setProperty("isOptional", value);
    }

    /**
     * <p/>
     * Type of the application that is used
     * as solution component. Mandatory. Once specified, cannot be changed.
     * See <code>
     * <a href="../Enumerations/CfgAppType.html">CfgAppType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgAppType getType() {
        return (CfgAppType) getProperty(CfgAppType.class, "type");
    }

    /**
     * <p/>
     * Type of the application that is used
     * as solution component. Mandatory. Once specified, cannot be changed.
     * See <code>
     * <a href="../Enumerations/CfgAppType.html">CfgAppType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgAppType value) {
        setProperty("type", value);
    }

    /**
     * A pointer to the application version
     * that is used as solution component. Once specified, cannot be changed.
     *
     * @return property value or null
     */
    public final String getVersion() {
        return (String) getProperty("version");
    }

    /**
     * A pointer to the application version
     * that is used as solution component. Once specified, cannot be changed.
     *
     * @param value new property value
     * @see #getVersion()
     */
    public final void setVersion(final String value) {
        setProperty("version", value);
    }
}
