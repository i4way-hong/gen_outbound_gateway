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
 * <code>CfgEnumerator</code> objects are available enumerations, such as
 * MediaType, Service Type, and others.
 *
 * <p/>
 * The table below provides a description of predefined <code>
 * <a href="CfgEnumerator.html">CfgEnumerator</a>
 * </code> objects (some
 * fields are omitted for simplicity):
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <caption><font size="+1"><strong>Predefined CfgEnumerator Objects
 * </strong></font></caption>
 * <thead>
 * <tr bgcolor="#ccccff" class="tableheadingcolor">
 * <th align="left">DBID</th>
 * <th align="left">Tenant</th>
 * <th align="left">Name</th>
 * <th align="left">Display name</th>
 * <th align="left">Type</th>
 * <th align="left">Description</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>101</td>
 * <td>1(101)</td>
 * <td>MediaType</td>
 * <td>Media Type</td>
 * <td>1</td>
 * <td>Defines all available Media Types</td>
 * </tr>
 * <tr>
 * <td>102</td>
 * <td>1(101)</td>
 * <td>ServiceType</td>
 * <td>Service Type</td>
 * <td>1</td>
 * <td>Defines all available Service Types</td>
 * </tr>
 * <tr>
 * <td>103</td>
 * <td>1(101)</td>
 * <td>CustomerSegment</td>
 * <td>Customer Segment</td>
 * <td>1</td>
 * <td>Defines all available Customer Segments</td>
 * </tr>
 * <tr>
 * <td>104</td>
 * <td>1(101)</td>
 * <td>IVR Text To Speech Used</td>
 * <td>IVR Text To Speech Used</td>
 * <td>1</td>
 * <td>Defines IVR Text To Speech Used</td>
 * </tr>
 * <tr>
 * <td>105</td>
 * <td>1(101)</td>
 * <td>IVR Speech Recognition Used</td>
 * <td>IVR Speech Recognition Used</td>
 * <td>1</td>
 * <td>Defines IVR Speech Recognition Used</td>
 * </tr>
 * <tr>
 * <td>106</td>
 * <td>1(101)</td>
 * <td>IVR Application Name</td>
 * <td>IVR Application Name</td>
 * <td>1</td>
 * <td>Defines IVR Application Name</td>
 * </tr>
 * <tr>
 * <td>107</td>
 * <td>1(101)</td>
 * <td>IVR Technical Result</td>
 * <td>IVR Technical Result</td>
 * <td>1</td>
 * <td>Defines IVR Technical Result</td>
 * </tr>
 * <tr>
 * <td>108</td>
 * <td>1(101)</td>
 * <td>IVR Technical Result Reason</td>
 * <td>IVR Technical Result Reason</td>
 * <td>1</td>
 * <td>Defines IVR Technical Result Reason</td>
 * </tr>
 * <tr>
 * <td>109</td>
 * <td>1(101)</td>
 * <td>Case ID</td>
 * <td>Case ID</td>
 * <td>1</td>
 * <td>Defines Case ID</td>
 * </tr>
 * <tr>
 * <td>110</td>
 * <td>1(101)</td>
 * <td>Business Result</td>
 * <td>Business Result</td>
 * <td>1</td>
 * <td>Defines Business Result</td>
 * </tr>
 * <tr>
 * <td>111</td>
 * <td>1(101)</td>
 * <td>Root Interaction ID</td>
 * <td>Root Interaction ID</td>
 * <td>1</td>
 * <td>Root Interaction ID</td>
 * </tr>
 * <tr>
 * <td>112</td>
 * <td>1(101)</td>
 * <td>InteractionType</td>
 * <td>Interaction Type</td>
 * <td>1</td>
 * <td>Predefined list of interaction types supported by
 * contact center</td>
 * </tr>
 * <tr>
 * <td>113</td>
 * <td>1(101)</td>
 * <td>InteractionSubtype</td>
 * <td>Interaction Subtype</td>
 * <td>1</td>
 * <td>Predefined list of interaction subtypes supported
 * by contact center</td>
 * </tr>
 * <tr>
 * <td>114</td>
 * <td>1(101)</td>
 * <td>CategoryStructure</td>
 * <td>Category Structure</td>
 * <td>4</td>
 * <td>Customer defined</td>
 * </tr>
 * <tr>
 * <td>115</td>
 * <td>1(101)</td>
 * <td>ScreeningRules</td>
 * <td>Screening Rules</td>
 * <td>4</td>
 * <td>Customer defined</td>
 * </tr>
 * <tr>
 * <td>116</td>
 * <td>1(101)</td>
 * <td>EmailAccounts</td>
 * <td>Email Accounts</td>
 * <td>4</td>
 * <td>Customer defined</td>
 * </tr>
 * <tr>
 * <td>117</td>
 * <td>1(101)</td>
 * <td>StopProcessingReason</td>
 * <td>StopProcessing Reason</td>
 * <td>1</td>
 * <td>Predefined, extended by customer</td>
 * </tr>
 * <tr>
 * <td>118</td>
 * <td>1(101)</td>
 * <td>Language</td>
 * <td>Language</td>
 * <td>1</td>
 * <td>Extended by customer</td>
 * </tr>
 * <tr>
 * <td>119</td>
 * <td>1(101)</td>
 * <td>DispositionCode</td>
 * <td>Disposition Code</td>
 * <td>1</td>
 * <td>Customer defined</td>
 * </tr>
 * <tr>
 * <td>120</td>
 * <td>1(101)</td>
 * <td>ReasonCode</td>
 * <td>Reason Code</td>
 * <td>1</td>
 * <td>Customer defined</td>
 * </tr>
 * <tr>
 * <td>121</td>
 * <td>1(101)</td>
 * <td>InteractionAttributes</td>
 * <td>Interaction Attributes</td>
 * <td>1</td>
 * <td>List of predefined interaction attributes</td>
 * </tr>
 * <tr>
 * <td>122</td>
 * <td>1(101)</td>
 * <td>ContactAttributes</td>
 * <td>Contact Attributes</td>
 * <td>1</td>
 * <td>List of predefined contact attributes</td>
 * </tr>
 * </tbody>
 * </table><br/>
 * <p/>
 * In the GUI representation, the name ___Enumerator___ was replaced
 * with ___Business Attribute___
 * <p/>
 * Deletion of Enumerator X will cause the following events set
 * out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Deletion of all Enumerator values of Enumerator X
 * </li>
 * <li>
 * Deletion of all folders that had Enumerator X defined as the
 * parent object
 * </li>
 * <li>
 * Deletion of Enumerator X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaEnumerator.html">
 * <b>CfgDeltaEnumerator</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgEnumeratorValue.html">
 * <b>CfgEnumeratorValue</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgEnumerator
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGEnumerator;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgEnumerator(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgEnumerator - "
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
    public CfgEnumerator(
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
    public CfgEnumerator(final IConfService confService) {
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
            if (getMetaData().getAttribute("tenantDBID") != null) {
                if (getLinkValue("tenantDBID") == null) {
                    throw new ConfigException("Mandatory property 'tenantDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("name") != null) {
                if (getProperty("name") == null) {
                    throw new ConfigException("Mandatory property 'name' not set.");
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
                }
            }
            if (getMetaData().getAttribute("type") != null) {
                if (getProperty("type") == null) {
                    throw new ConfigException("Mandatory property 'type' not set.");
                }
            }
            if (getMetaData().getAttribute("displayName") != null) {
                if (getProperty("displayName") == null) {
                    throw new ConfigException("Mandatory property 'displayName' not set.");
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
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code> to which this enumerator is allocated. Mandatory. Once specified,
     * cannot be changed.
     *
     * @return instance of referred object or null
     */
    public final CfgTenant getTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "tenantDBID");
    }

    /**
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code> to which this enumerator is allocated. Mandatory. Once specified,
     * cannot be changed.
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
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code> to which this enumerator is allocated. Mandatory. Once specified,
     * cannot be changed.
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
     * A pointer to the system name of the
     * enumerator to be used primarily by Genesys applications. Mandatory.
     * Must be unique within the tenant environment. Once specified, can
     * not be changed.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the system name of the
     * enumerator to be used primarily by Genesys applications. Mandatory.
     * Must be unique within the tenant environment. Once specified, can
     * not be changed.
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
     * A pointer to the description
     * of the enumerator.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A pointer to the description
     * of the enumerator.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
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
     * A type of the enumerator that enumerator
     * belongs to. See <code>
     * <a href="../Enumerations/CfgEnumeratorType.html">CfgEnumeratorType</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgEnumeratorType getType() {
        return (CfgEnumeratorType) getProperty(CfgEnumeratorType.class, "type");
    }

    /**
     * A type of the enumerator that enumerator
     * belongs to. See <code>
     * <a href="../Enumerations/CfgEnumeratorType.html">CfgEnumeratorType</a>
     * </code>
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgEnumeratorType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
    }

    /**
     * A pointer to the display name
     * of the enumerator to be shown on GUI and in reports. Mandatory.
     *
     * @return property value or null
     */
    public final String getDisplayName() {
        return (String) getProperty("displayName");
    }

    /**
     * A pointer to the display name
     * of the enumerator to be shown on GUI and in reports. Mandatory.
     *
     * @param value new property value
     * @see #getDisplayName()
     */
    public final void setDisplayName(final String value) {
        setProperty("displayName", value);
    }
}
