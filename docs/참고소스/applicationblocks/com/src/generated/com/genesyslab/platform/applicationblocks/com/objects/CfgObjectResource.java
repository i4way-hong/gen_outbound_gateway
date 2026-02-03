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
 * <em>CfgObjectResource</em> contains information about object's resource and related optional parameters.
 * The role of the resource and content of optional parameter is dictated by the value <code>resourceType</code>
 * property. See <code>
 * <a href="../Enumerations/CfgResourceType.html">CfgResourceType</a>
 * </code>.
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgObjectResource extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgObjectResource(
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
    public CfgObjectResource(
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
    public CfgObjectResource(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgObjectResource")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
    }

    /**
     * Type of the resource. See
     * type <code>
     * <a href="../Enumerations/CfgResourceType.html">CfgResourceType</a>
     * </code>. Mandatory.
     *
     * @return property value or null
     */
    public final CfgResourceType getResourceType() {
        return (CfgResourceType) getProperty(CfgResourceType.class, "resourceType");
    }

    /**
     * Type of the resource. See
     * type <code>
     * <a href="../Enumerations/CfgResourceType.html">CfgResourceType</a>
     * </code>. Mandatory.
     *
     * @param value new property value
     * @see #getResourceType()
     */
    public final void setResourceType(final CfgResourceType value) {
        setProperty("resourceType", value);
    }

    /**
     * An identifier of the object referred by this resource. Type of the object is defined by <code>objectType</code> property
     *
     * @return property value or null
     */
    public final Integer getObjectDBID() {
        return (Integer) getProperty("objectDBID");
    }

    /**
     * An identifier of the object referred by this resource. Type of the object is defined by <code>objectType</code> property
     *
     * @param value new property value
     * @see #getObjectDBID()
     */
    public final void setObjectDBID(final Integer value) {
        setProperty("objectDBID", value);
    }

    /**
     * Type of the object referred by this resource. See
     * type <code>
     * <a href="../Enumerations/CfgObjectType.html">CfgObjectType</a>
     * </code>. Mandatory.
     *
     * @return property value or null
     */
    public final CfgObjectType getObjectType() {
        return (CfgObjectType) getProperty(CfgObjectType.class, "objectType");
    }

    /**
     * Type of the object referred by this resource. See
     * type <code>
     * <a href="../Enumerations/CfgObjectType.html">CfgObjectType</a>
     * </code>. Mandatory.
     *
     * @param value new property value
     * @see #getObjectType()
     */
    public final void setObjectType(final CfgObjectType value) {
        setProperty("objectType", value);
    }

    /**
     * Optional description of the resource.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * Optional description of the resource.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * Optional text field #1.
     *
     * @return property value or null
     */
    public final String getCharField1() {
        return (String) getProperty("charField1");
    }

    /**
     * Optional text field #1.
     *
     * @param value new property value
     * @see #getCharField1()
     */
    public final void setCharField1(final String value) {
        setProperty("charField1", value);
    }

    /**
     * Optional text field #2.
     *
     * @return property value or null
     */
    public final String getCharField2() {
        return (String) getProperty("charField2");
    }

    /**
     * Optional text field #2.
     *
     * @param value new property value
     * @see #getCharField2()
     */
    public final void setCharField2(final String value) {
        setProperty("charField2", value);
    }

    /**
     * Optional text field #3.
     *
     * @return property value or null
     */
    public final String getCharField3() {
        return (String) getProperty("charField3");
    }

    /**
     * Optional text field #3.
     *
     * @param value new property value
     * @see #getCharField3()
     */
    public final void setCharField3(final String value) {
        setProperty("charField3", value);
    }

    /**
     * Optional text field #4.
     *
     * @return property value or null
     */
    public final String getCharField4() {
        return (String) getProperty("charField4");
    }

    /**
     * Optional text field #4.
     *
     * @param value new property value
     * @see #getCharField4()
     */
    public final void setCharField4(final String value) {
        setProperty("charField4", value);
    }

    /**
     * Optional integer field #1.
     *
     * @return property value or null
     */
    public final Integer getLongField1() {
        return (Integer) getProperty("longField1");
    }

    /**
     * Optional integer field #1.
     *
     * @param value new property value
     * @see #getLongField1()
     */
    public final void setLongField1(final Integer value) {
        setProperty("longField1", value);
    }

    /**
     * Optional integer field #2.
     *
     * @return property value or null
     */
    public final Integer getLongField2() {
        return (Integer) getProperty("longField2");
    }

    /**
     * Optional integer field #2.
     *
     * @param value new property value
     * @see #getLongField2()
     */
    public final void setLongField2(final Integer value) {
        setProperty("longField2", value);
    }

    /**
     * Optional integer field #3.
     *
     * @return property value or null
     */
    public final Integer getLongField3() {
        return (Integer) getProperty("longField3");
    }

    /**
     * Optional integer field #3.
     *
     * @param value new property value
     * @see #getLongField3()
     */
    public final void setLongField3(final Integer value) {
        setProperty("longField3", value);
    }

    /**
     * Optional integer field #4.
     *
     * @return property value or null
     */
    public final Integer getLongField4() {
        return (Integer) getProperty("longField4");
    }

    /**
     * Optional integer field #4.
     *
     * @param value new property value
     * @see #getLongField4()
     */
    public final void setLongField4(final Integer value) {
        setProperty("longField4", value);
    }
}
