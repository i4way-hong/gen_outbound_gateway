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
 * <em>Hosts</em> are the computers that run the various CTI server applications
 * in an environment.
 *
 * <p/>
 * A host cannot be deleted as long as there is at least
 * one server associated with it (see <code>
 * <a href="CfgServer.html">CfgServer</a>
 * </code>
 * and <code>
 * <a href="CfgApplication.html">CfgApplication</a>
 * </code>).
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaHost.html">
 * <b>CfgDeltaHost</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgApplication.html">
 * <b>CfgApplication</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgHost
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGHost;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgHost(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgHost - "
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
    public CfgHost(
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
    public CfgHost(final IConfService confService) {
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
            if (getMetaData().getAttribute("OSinfo") != null) {
                CfgOS OSinfo = (CfgOS) getProperty("OSinfo");
                if (OSinfo != null) {
                    OSinfo.checkPropertiesForSave();
                }
            }
            if (getMetaData().getAttribute("type") != null) {
                if (getProperty("type") == null) {
                    throw new ConfigException("Mandatory property 'type' not set.");
                }
            }
            if (getMetaData().getAttribute("address") != null) {
                CfgAddress address = (CfgAddress) getProperty("address");
                if (address != null) {
                    address.checkPropertiesForSave();
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
                }
            }
            if (getMetaData().getAttribute("LCAPort") != null) {
                if (getProperty("LCAPort") == null) {
                    throw new ConfigException("Mandatory property 'LCAPort' not set.");
                }
            }
            if (getMetaData().getAttribute("resources") != null) {
                Collection<CfgObjectResource> resources = (Collection<CfgObjectResource>) getProperty("resources");
                if (resources != null) {
                    for (CfgObjectResource item : resources) {
                        item.checkPropertiesForSave();
                    }
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
     * A pointer to name of the host. Mandatory.
     * Must be unique within the Configuration Database. Cannot be changed
     * as long as at least one server is assigned to this host.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to name of the host. Mandatory.
     * Must be unique within the Configuration Database. Cannot be changed
     * as long as at least one server is assigned to this host.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * A pointer to the IP address of
     * the host. Optional. Must be unique within the Configuration Database.
     * Max length 64 symbols.
     *
     * @return property value or null
     */
    public final String getIPaddress() {
        return (String) getProperty("IPaddress");
    }

    /**
     * A pointer to the IP address of
     * the host. Optional. Must be unique within the Configuration Database.
     * Max length 64 symbols.
     *
     * @param value new property value
     * @see #getIPaddress()
     */
    public final void setIPaddress(final String value) {
        setProperty("IPaddress", value);
    }

    /**
     * A pointer to the structure containing
     * information about the operating system of this host. Once specified,
     * cannot be set to <code>NULL</code>. See structure <code>
     * <a href="CfgOS.html">CfgOS</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgOS getOSinfo() {
        return (CfgOS) getProperty(CfgOS.class, "OSinfo");
    }

    /**
     * A pointer to the structure containing
     * information about the operating system of this host. Once specified,
     * cannot be set to <code>NULL</code>. See structure <code>
     * <a href="CfgOS.html">CfgOS</a>
     * </code>.
     *
     * @param value new property value
     * @see #getOSinfo()
     */
    public final void setOSinfo(final CfgOS value) {
        setProperty("OSinfo", value);
    }

    /**
     * Type of the host. Mandatory. Once
     * specified, cannot be changed. See type <code>
     * <a href="../Enumerations/CfgHostType.html">CfgHostType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgHostType getType() {
        return (CfgHostType) getProperty(CfgHostType.class, "type");
    }

    /**
     * Type of the host. Mandatory. Once
     * specified, cannot be changed. See type <code>
     * <a href="../Enumerations/CfgHostType.html">CfgHostType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgHostType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
    }

    /**
     * Not in use.
     *
     * @return property value or null
     */
    public final CfgAddress getAddress() {
        return (CfgAddress) getProperty(CfgAddress.class, "address");
    }

    /**
     * Not in use.
     *
     * @param value new property value
     * @see #getAddress()
     */
    public final void setAddress(final CfgAddress value) {
        setProperty("address", value);
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

    /**
     * A port on which the Local Control
     * Agent for this host is supposed to be running. Mandatory. Default
     * value for migration from 5.1.xxx to 5.9.xxx is 4999, for newly created
     * hosts the value must be specified. Allowed value is any positive
     * whole number within 0-9999 range.
     *
     * @return property value or null
     */
    public final String getLCAPort() {
        return (String) getProperty("LCAPort");
    }

    /**
     * A port on which the Local Control
     * Agent for this host is supposed to be running. Mandatory. Default
     * value for migration from 5.1.xxx to 5.9.xxx is 4999, for newly created
     * hosts the value must be specified. Allowed value is any positive
     * whole number within 0-9999 range.
     *
     * @param value new property value
     * @see #getLCAPort()
     */
    public final void setLCAPort(final String value) {
        setProperty("LCAPort", value);
    }

    /**
     * A unique identifier of an <code>
     * <a href="CfgApplication.html">CfgApplication</a>
     * </code>
     * of <code>CFGSCS</code> type which is supposed to monitor/control
     * this host. This property is valid only if Distributed SCS functionality
     * is enabled.
     *
     * @return instance of referred object or null
     */
    public final CfgApplication getSCS() {
        return (CfgApplication) getProperty(CfgApplication.class, "SCSDBID");
    }

    /**
     * A unique identifier of an <code>
     * <a href="CfgApplication.html">CfgApplication</a>
     * </code>
     * of <code>CFGSCS</code> type which is supposed to monitor/control
     * this host. This property is valid only if Distributed SCS functionality
     * is enabled.
     *
     * @param value new property value
     * @see #getSCS()
     */
    public final void setSCS(final CfgApplication value) {
        setProperty("SCSDBID", value);
    }

    /**
     * A unique identifier of an <code>
     * <a href="CfgApplication.html">CfgApplication</a>
     * </code>
     * of <code>CFGSCS</code> type which is supposed to monitor/control
     * this host. This property is valid only if Distributed SCS functionality
     * is enabled.
     *
     * @param dbid DBID identifier of referred object
     * @see #getSCS()
     */
    public final void setSCSDBID(final int dbid) {
        setProperty("SCSDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the SCS property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getSCSDBID() {
        return getLinkValue("SCSDBID");
    }

    /**
     * A pointer to the list of the objects associated with this Host
     * (every item of this list is structured as
     * <code>
     * <a href="CfgObjectResource.html">CfgObjectResource</a>
     * </code>). When used as an entry
     * in <code>
     * <a href="CfgDeltaHost.html">CfgDeltaHost</a>
     * </code>, it is a pointer to a list of resources
     * added to the existing list.
     * Only objects of type <code>
     * <a href="../Enumerations/CfgEnumeratorType.html">CfgEnumeratorType</a>
     * </code>,
     * <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code> and
     * <code>
     * <a href="CfgApplication.html">CfgApplication</a>
     * </code> can be associated with Host object through <code>resources</code>
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgObjectResource> getResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
    }

    /**
     * A pointer to the list of the objects associated with this Host
     * (every item of this list is structured as
     * <code>
     * <a href="CfgObjectResource.html">CfgObjectResource</a>
     * </code>). When used as an entry
     * in <code>
     * <a href="CfgDeltaHost.html">CfgDeltaHost</a>
     * </code>, it is a pointer to a list of resources
     * added to the existing list.
     * Only objects of type <code>
     * <a href="../Enumerations/CfgEnumeratorType.html">CfgEnumeratorType</a>
     * </code>,
     * <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code> and
     * <code>
     * <a href="CfgApplication.html">CfgApplication</a>
     * </code> can be associated with Host object through <code>resources</code>
     *
     * @param value new property value
     * @see #getResources()
     */
    public final void setResources(final Collection<CfgObjectResource> value) {
        setProperty("resources", value);
    }
}
