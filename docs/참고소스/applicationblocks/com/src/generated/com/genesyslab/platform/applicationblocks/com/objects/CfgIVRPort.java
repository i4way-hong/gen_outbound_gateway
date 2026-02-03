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
 * An <em>Interactive Voice Response</em> (IVR) <em>Port</em> is a telephony object
 * uniquely identified by the numbers within an IVR at which telephone calls may
 * reside and be handled.
 *
 * <p/>
 * An IVR port can only be associated with one IVR.
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaIVRPort.html">
 * <b>CfgDeltaIVRPort</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgIVR.html">
 * <b>CfgIVR</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgDN.html">
 * <b>CfgDN</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgIVRPort
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGIVRPort;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgIVRPort(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgIVRPort - "
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
    public CfgIVRPort(
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
    public CfgIVRPort(final IConfService confService) {
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
            if (getMetaData().getAttribute("portNumber") != null) {
                if (getProperty("portNumber") == null) {
                    throw new ConfigException("Mandatory property 'portNumber' not set.");
                }
            }
            if (getMetaData().getAttribute("IVRDBID") != null) {
                if (getLinkValue("IVRDBID") == null) {
                    throw new ConfigException("Mandatory property 'IVRDBID' not set.");
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
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this IVR port belongs. Read-only (set automatically according
     * to the current value of
     * <code>tenantDBID</code> of the IVR specified in <code>IVRDBID</code>).
     * See type <code>
     * <a href="CfgIVR.html">CfgIVR</a>
     * </code>.
     *
     * @return instance of referred object or null
     */
    public final CfgTenant getTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "tenantDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this IVR port belongs. Read-only (set automatically according
     * to the current value of
     * <code>tenantDBID</code> of the IVR specified in <code>IVRDBID</code>).
     * See type <code>
     * <a href="CfgIVR.html">CfgIVR</a>
     * </code>.
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
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this IVR port belongs. Read-only (set automatically according
     * to the current value of
     * <code>tenantDBID</code> of the IVR specified in <code>IVRDBID</code>).
     * See type <code>
     * <a href="CfgIVR.html">CfgIVR</a>
     * </code>.
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
     * A pointer to the string, representing
     * number associated with channel on IVR. Mandatory. Must be unique
     * within one IVR. Once specified cannot be changed.
     *
     * @return property value or null
     */
    public final String getPortNumber() {
        return (String) getProperty("portNumber");
    }

    /**
     * A pointer to the string, representing
     * number associated with channel on IVR. Mandatory. Must be unique
     * within one IVR. Once specified cannot be changed.
     *
     * @param value new property value
     * @see #getPortNumber()
     */
    public final void setPortNumber(final String value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("portNumber", value);
    }

    /**
     * A pointer to the string describing
     * IVR port.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A pointer to the string describing
     * IVR port.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgIVR.html">CfgIVR</a>
     * </code> this
     * IVR port belongs to. Mandatory. Once specified, can not be changed
     *
     * @return instance of referred object or null
     */
    public final CfgIVR getIVR() {
        return (CfgIVR) getProperty(CfgIVR.class, "IVRDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgIVR.html">CfgIVR</a>
     * </code> this
     * IVR port belongs to. Mandatory. Once specified, can not be changed
     *
     * @param value new property value
     * @see #getIVR()
     */
    public final void setIVR(final CfgIVR value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("IVRDBID", value);
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgIVR.html">CfgIVR</a>
     * </code> this
     * IVR port belongs to. Mandatory. Once specified, can not be changed
     *
     * @param dbid DBID identifier of referred object
     * @see #getIVR()
     */
    public final void setIVRDBID(final int dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("IVRDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the IVR property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getIVRDBID() {
        return getLinkValue("IVRDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgDN.html">CfgDN</a>
     * </code> associated
     * with this IVR port.
     *
     * @return instance of referred object or null
     */
    public final CfgDN getDN() {
        return (CfgDN) getProperty(CfgDN.class, "DNDBID");
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgDN.html">CfgDN</a>
     * </code> associated
     * with this IVR port.
     *
     * @param value new property value
     * @see #getDN()
     */
    public final void setDN(final CfgDN value) {
        setProperty("DNDBID", value);
    }

    /**
     * A unique identifier of <code>
     * <a href="CfgDN.html">CfgDN</a>
     * </code> associated
     * with this IVR port.
     *
     * @param dbid DBID identifier of referred object
     * @see #getDN()
     */
    public final void setDNDBID(final int dbid) {
        setProperty("DNDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the DN property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getDNDBID() {
        return getLinkValue("DNDBID");
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
