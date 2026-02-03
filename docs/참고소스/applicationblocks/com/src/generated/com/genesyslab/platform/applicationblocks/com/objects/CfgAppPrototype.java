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
 * An application prototype.
 *
 * <p/>
 * An application prototype cannot be deleted as long as
 * there is at least one application associated with it.
 * <p/>
 * Parameter <code>options</code> has the following structure:
 * Each key-value pair of the primary list (<code>TKVList *options</code>) uses
 * the key for the name of a configuration section, and the value for a secondary
 * list, that also has the <code>TKVList</code> structure and specifies the
 * configuration options defined within that section. Each key-value pair of the
 * secondary list uses the key for the name of a configuration option, and the
 * value for its current setting. Configuration options can be defined as variables
 * of integer, character, or binary type. Names of sections must be
 * unique within the primary list. Names of options must be unique
 * within the secondary list. NOTE: Configuration Server is not concerned with
 * logical meanings of application-specific configuration sections,
 * options, or their values.
 * <p/>
 * An application prototype of <code>CFGConfigServer</code> type
 * with <code>DBID</code> set to <code>99</code> shall be pre-defined (scripted)
 * in the Configuration Database before Configuration
 * Server is started for the first time. The object that represents
 * this application prototype cannot be deleted.
 * <p/>
 * An application prototype of <code>CFGSCE</code> type with
 * <code>DBID</code> set to <code>100</code> shall be pre-defined (scripted)
 * in the Configuration Database before Configuration
 * Server is started for the first time. The object that represents
 * this application prototype cannot be deleted.
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaAppPrototype.html">
 * <b>CfgDeltaAppPrototype</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgAppPrototype
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGAppPrototype;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgAppPrototype(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgAppPrototype - "
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
    public CfgAppPrototype(
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
    public CfgAppPrototype(final IConfService confService) {
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
            if (getMetaData().getAttribute("name") != null) {
                if (getProperty("name") == null) {
                    throw new ConfigException("Mandatory property 'name' not set.");
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
     * A pointer to the name of the application
     * prototype. Mandatory. Once specified, cannot be changed. Must be
     * unique within the Configuration Database.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the name of the application
     * prototype. Mandatory. Once specified, cannot be changed. Must be
     * unique within the Configuration Database.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("name", value);
    }

    /**
     * Type of the application. Mandatory.
     * Once specified, cannot be changed. See <code>
     * <a href="../Enumerations/CfgAppType.html">CfgAppType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgAppType getType() {
        return (CfgAppType) getProperty(CfgAppType.class, "type");
    }

    /**
     * Type of the application. Mandatory.
     * Once specified, cannot be changed. See <code>
     * <a href="../Enumerations/CfgAppType.html">CfgAppType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgAppType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
    }

    /**
     * A pointer to the application version.
     * Once specified, cannot be changed.
     *
     * @return property value or null
     */
    public final String getVersion() {
        return (String) getProperty("version");
    }

    /**
     * A pointer to the application version.
     * Once specified, cannot be changed.
     *
     * @param value new property value
     * @see #getVersion()
     */
    public final void setVersion(final String value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("version", value);
    }

    /**
     * A pointer to the list of application-specific configuration
     * options with default values where appropriate (see the comments below).
     * When used as an entry in <code>CfgDeltaApplication</code>, it is
     * a pointer to a list of options added to the existing list.
     *
     * @return property value or null
     */
    public final KeyValueCollection getOptions() {
        return (KeyValueCollection) getProperty("options");
    }

    /**
     * A pointer to the list of application-specific configuration
     * options with default values where appropriate (see the comments below).
     * When used as an entry in <code>CfgDeltaApplication</code>, it is
     * a pointer to a list of options added to the existing list.
     *
     * @param value new property value
     * @see #getOptions()
     */
    public final void setOptions(final KeyValueCollection value) {
        setProperty("options", value);
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
