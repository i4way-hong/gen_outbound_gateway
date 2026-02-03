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

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import javax.annotation.Generated;

import java.util.Calendar;
import java.util.Collection;

import org.w3c.dom.Node;


/**
 * <p/>
 * The changes to make to a <code>
 * <a href="CfgHost.html">CfgHost</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDeltaHost extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaHost(final IConfService confService) {
        super(confService, "CfgDeltaHost");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaHost(
            final IConfService confService,
            final ConfObjectDelta objData) {
        super(confService, objData, null);
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param xmlData XML object containing object delta data
     */
    public CfgDeltaHost(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgHost configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgHost configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgHost retrieveCfgHost() throws ConfigException {
        return (CfgHost) (super.retrieveObject());
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
     * Not in use.
     *
     * @return property value or null
     */
    public final String getHWID() {
        return (String) getProperty("HWID");
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
     * @return instance of referred object or null
     */
    public final CfgPerson getContactPerson() {
        return (CfgPerson) getProperty(CfgPerson.class, "contactPersonDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the ContactPerson property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getContactPersonDBID() {
        return getLinkValue("contactPersonDBID");
    }

    /**
     * Not in use.
     *
     * @return property value or null
     */
    public final String getComment() {
        return (String) getProperty("comment");
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
     * A pointer to the list of user-defined properties.Parameter userProperties has the following structure: Each key-value pair of the primary list (TKVList *userProperties) uses the key for the name of a user-defined section, and the value for a secondary list, that also has the TKVList structure and specifies the properties defined within that section.
     *
     * @return property value or null
     */
    public final KeyValueCollection getAddedUserProperties() {
        return (KeyValueCollection) getProperty("userProperties");
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
     * Retrieves the dbid of the object that is being linked to by the SCS property.
     * Configuration server provides it only if the property value has been changed.
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
    public final Collection<CfgObjectResource> getAddedResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
    }

    /**
     * A pointer to the list of deleted user-defined properties. Has the same structure as parameter userProperties.
     *
     * @return property value or null
     */
    public final KeyValueCollection getDeletedUserProperties() {
        return (KeyValueCollection) getProperty("deletedUserProperties");
    }

    /**
     * A pointer to the list of user-defined properties whose values have been changed. Has the same structure as parameter userProperties
     *
     * @return property value or null
     */
    public final KeyValueCollection getChangedUserProperties() {
        return (KeyValueCollection) getProperty("changedUserProperties");
    }

    /**
     * A pointer to the list of deleted resources (every
     * item of this list is structured as <code>
     * <a href="CfgID.html">CfgID</a>
     * </code>).
     *
     * @return list of structures or null
     */
    public final Collection<CfgResourceID> getDeletedResources() {
        return (Collection<CfgResourceID>) getProperty("deletedResources");
    }

    /**
     * A pointer to the list of structures
     * <code>
     * <a href="CfgObjectResource.html">CfgObjectResource</a>
     * </code> type. Each structure contains
     * information about the resource parameters that have been changed.
     *
     * @return list of structures or null
     */
    public final Collection<CfgObjectResource> getChangedResources() {
        return (Collection<CfgObjectResource>) getProperty("changedResources");
    }
}
