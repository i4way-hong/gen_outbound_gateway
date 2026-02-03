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

@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDelSwitchAccess extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgDelSwitchAccess(
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
    public CfgDelSwitchAccess(
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
    public CfgDelSwitchAccess(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgDelSwitchAccess")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
    }

    public final CfgSwitch getSwitch() {
        return (CfgSwitch) getProperty(CfgSwitch.class, "switchDBID");
    }

    public final void setSwitch(final CfgSwitch value) {
        setProperty("switchDBID", value);
    }

    public final void setSwitchDBID(final int dbid) {
        setProperty("switchDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Switch property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getSwitchDBID() {
        return getLinkValue("switchDBID");
    }

    public final String getAccessCode() {
        return (String) getProperty("accessCode");
    }

    public final void setAccessCode(final String value) {
        setProperty("accessCode", value);
    }

    public final CfgTargetType getTargetType() {
        return (CfgTargetType) getProperty(CfgTargetType.class, "targetType");
    }

    public final void setTargetType(final CfgTargetType value) {
        setProperty("targetType", value);
    }

    public final CfgRouteType getRouteType() {
        return (CfgRouteType) getProperty(CfgRouteType.class, "routeType");
    }

    public final void setRouteType(final CfgRouteType value) {
        setProperty("routeType", value);
    }

    public final String getDnSource() {
        return (String) getProperty("dnSource");
    }

    public final void setDnSource(final String value) {
        setProperty("dnSource", value);
    }

    public final String getDestinationSource() {
        return (String) getProperty("destinationSource");
    }

    public final void setDestinationSource(final String value) {
        setProperty("destinationSource", value);
    }

    public final String getLocationSource() {
        return (String) getProperty("locationSource");
    }

    public final void setLocationSource(final String value) {
        setProperty("locationSource", value);
    }

    public final String getDnisSource() {
        return (String) getProperty("dnisSource");
    }

    public final void setDnisSource(final String value) {
        setProperty("dnisSource", value);
    }

    public final String getReasonSource() {
        return (String) getProperty("reasonSource");
    }

    public final void setReasonSource(final String value) {
        setProperty("reasonSource", value);
    }

    public final String getExtensionSource() {
        return (String) getProperty("extensionSource");
    }

    public final void setExtensionSource(final String value) {
        setProperty("extensionSource", value);
    }
}
