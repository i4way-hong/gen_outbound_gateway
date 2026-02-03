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
 * <em>CfgSolutionComponent</em> displays a list of applications whose
 * functionality this solution uses.
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgSolutionComponent extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgSolutionComponent(
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
    public CfgSolutionComponent(
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
    public CfgSolutionComponent(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgSolutionComponent")), parent);
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
    }

    /**
     * The startup priority of the solution component in a component
     * sequence. Component numbers should be used to determine the order
     * in which components should be started and stopped. Must be defined
     * as positive integer. Mandatory. Once specified cannot be changed.
     * Must be shown as
     * <code>READONLY</code> property in object of type <code>
     * <a href="CfgService.html">CfgService</a>
     * </code> (Solution).
     *
     * @return property value or null
     */
    public final Integer getStartupPriority() {
        return (Integer) getProperty("startupPriority");
    }

    /**
     * The startup priority of the solution component in a component
     * sequence. Component numbers should be used to determine the order
     * in which components should be started and stopped. Must be defined
     * as positive integer. Mandatory. Once specified cannot be changed.
     * Must be shown as
     * <code>READONLY</code> property in object of type <code>
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
     * component is optional. Recommended to be set to
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
     * component is optional. Recommended to be set to
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
     * A unique identifier of an <code>
     * <a href="CfgApplication.html">Application</a>
     * </code>
     * with type <code>appType</code> and version <code>appVersion</code>
     * Mandatory (application must be specified within solution) for solution
     * components within a solution if <code>isOptional</code> is set to
     * <code>CFGFalse</code>. The application could be chosen from the
     * list of applications based on application template/prototype specified
     * in <code>appPrototypeDBID</code>.
     *
     * @return instance of referred object or null
     */
    public final CfgApplication getApp() {
        return (CfgApplication) getProperty(CfgApplication.class, "appDBID");
    }

    /**
     * <p/>
     * A unique identifier of an <code>
     * <a href="CfgApplication.html">Application</a>
     * </code>
     * with type <code>appType</code> and version <code>appVersion</code>
     * Mandatory (application must be specified within solution) for solution
     * components within a solution if <code>isOptional</code> is set to
     * <code>CFGFalse</code>. The application could be chosen from the
     * list of applications based on application template/prototype specified
     * in <code>appPrototypeDBID</code>.
     *
     * @param value new property value
     * @see #getApp()
     */
    public final void setApp(final CfgApplication value) {
        setProperty("appDBID", value);
    }

    /**
     * <p/>
     * A unique identifier of an <code>
     * <a href="CfgApplication.html">Application</a>
     * </code>
     * with type <code>appType</code> and version <code>appVersion</code>
     * Mandatory (application must be specified within solution) for solution
     * components within a solution if <code>isOptional</code> is set to
     * <code>CFGFalse</code>. The application could be chosen from the
     * list of applications based on application template/prototype specified
     * in <code>appPrototypeDBID</code>.
     *
     * @param dbid DBID identifier of referred object
     * @see #getApp()
     */
    public final void setAppDBID(final int dbid) {
        setProperty("appDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the App property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getAppDBID() {
        return getLinkValue("appDBID");
    }
}
