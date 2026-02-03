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
 * <code>CfgEnumeratorValue</code> objects contain all of the values for all
 * available enumerators.
 *
 * <p/>
 * The Following table specifies predefined <code>
 * <a href="CfgEnumeratorValue.html">CfgEnumeratorValue</a>
 * </code> objects
 * (some fields are omitted for simplicity) and ranges for each Enumerator:
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <caption><font size="+1"><strong>Predefined CfgEnumeratorValue Objects and Their Ranges</strong></font></caption>
 * <thead>
 * <tr bgcolor="#ccccff" class="tableheadingcolor">
 * <th align="left">DBID</th>
 * <th align="left">Enumerator</th>
 * <th align="left">Name</th>
 * <th align="left">Display name</th>
 * <th align="left">Is default?</th>
 * <th align="left">Description</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>100</td>
 * <td>101</td>
 * <td>Voice</td>
 * <td>voice</td>
 * <td>No</td>
 * <td>Media Voice</td>
 * </tr>
 * <tr>
 * <td>101</td>
 * <td>101</td>
 * <td>Voip</td>
 * <td>voip</td>
 * <td>No</td>
 * <td>Media Voice over IP</td>
 * </tr>
 * <tr>
 * <td>102</td>
 * <td>101</td>
 * <td>Email</td>
 * <td>email</td>
 * <td>No</td>
 * <td>Media EMail</td>
 * </tr>
 * <tr>
 * <td>103</td>
 * <td>101</td>
 * <td>Vmail</td>
 * <td>vmail</td>
 * <td>No</td>
 * <td>Media Voice Mail</td>
 * </tr>
 * <tr>
 * <td>104</td>
 * <td>101</td>
 * <td>Smail</td>
 * <td>smail</td>
 * <td>No</td>
 * <td>Media Scanned Mail</td>
 * </tr>
 * <tr>
 * <td>105</td>
 * <td>101</td>
 * <td>Chat</td>
 * <td>chat</td>
 * <td>No</td>
 * <td>Media Chat</td>
 * </tr>
 * <tr>
 * <td>106</td>
 * <td>101</td>
 * <td>Video</td>
 * <td>video</td>
 * <td>No</td>
 * <td>Media Video</td>
 * </tr>
 * <tr>
 * <td>107</td>
 * <td>101</td>
 * <td>Cobrowsing</td>
 * <td>cobrowsing</td>
 * <td>No</td>
 * <td>Media Cobrowsing</td>
 * </tr>
 * <tr>
 * <td>108</td>
 * <td>101</td>
 * <td>Whiteboard</td>
 * <td>whiteboard</td>
 * <td>No</td>
 * <td>Media Whiteboard</td>
 * </tr>
 * <tr>
 * <td>109</td>
 * <td>101</td>
 * <td>Appsharing</td>
 * <td>appsharing</td>
 * <td>No</td>
 * <td>Media Application Sharing</td>
 * </tr>
 * <tr>
 * <td>110</td>
 * <td>101</td>
 * <td>Webform</td>
 * <td>webform</td>
 * <td>No</td>
 * <td>Media Web Form</td>
 * </tr>
 * <tr>
 * <td>111</td>
 * <td>101</td>
 * <td>Workitem</td>
 * <td>workitem</td>
 * <td>No</td>
 * <td>Media Workitem</td>
 * </tr>
 * <tr>
 * <td>112</td>
 * <td>101</td>
 * <td>Callback</td>
 * <td>callback</td>
 * <td>No</td>
 * <td>Media Callback</td>
 * </tr>
 * <tr>
 * <td>113</td>
 * <td>101</td>
 * <td>Fax</td>
 * <td>fax</td>
 * <td>No</td>
 * <td>Media Fax</td>
 * </tr>
 * <tr>
 * <td>114</td>
 * <td>101</td>
 * <td>Imchat</td>
 * <td>imchat</td>
 * <td>No</td>
 * <td>Media IMChat</td>
 * </tr>
 * <tr>
 * <td>115</td>
 * <td>101</td>
 * <td>Busevent</td>
 * <td>busevent</td>
 * <td>No</td>
 * <td>Media Business Event</td>
 * </tr>
 * <tr>
 * <td>116</td>
 * <td>101</td>
 * <td>Alert</td>
 * <td>alert</td>
 * <td>No</td>
 * <td>Media Alert</td>
 * </tr>
 * <tr>
 * <td>117</td>
 * <td>101</td>
 * <td>Sms</td>
 * <td>sms</td>
 * <td>No</td>
 * <td>Media SMS</td>
 * </tr>
 * <tr>
 * <td>118</td>
 * <td>101</td>
 * <td>Any</td>
 * <td>any</td>
 * <td>Yes</td>
 * <td>Media Any</td>
 * </tr>
 * <tr>
 * <td>119</td>
 * <td>101</td>
 * <td>Auxwork</td>
 * <td>auxwork</td>
 * <td>No</td>
 * <td>Media AuxWork</td>
 * </tr>
 * <tr>
 * <td>200</td>
 * <td>102</td>
 * <td>Default</td>
 * <td>default</td>
 * <td>Yes</td>
 * <td>Service Type Default</td>
 * </tr>
 * <tr>
 * <td>300</td>
 * <td>103</td>
 * <td>Default</td>
 * <td>default</td>
 * <td>Yes</td>
 * <td>Customer Segment Default</td>
 * </tr>
 * <tr>
 * <td>350</td>
 * <td>104</td>
 * <td>Unknown</td>
 * <td>unknown</td>
 * <td>Yes</td>
 * <td>IVR Text To Speech Used Default</td>
 * </tr>
 * <tr>
 * <td>353</td>
 * <td>104</td>
 * <td>Unknown</td>
 * <td>unknown</td>
 * <td>Yes</td>
 * <td>IVR Speech Recognition Used Default</td>
 * </tr>
 * <tr>
 * <td>400</td>
 * <td>112</td>
 * <td>Inbound</td>
 * <td>Inbound</td>
 * <td>No</td>
 * <td>Interactions received by contact center from outside clients</td>
 * </tr>
 * <tr>
 * <td>401</td>
 * <td>112</td>
 * <td>Outbound</td>
 * <td>Outbound</td>
 * <td>No</td>
 * <td>Interactions sent from contact center to external clients</td>
 * </tr>
 * <tr>
 * <td>402</td>
 * <td>112</td>
 * <td>Internal</td>
 * <td>Internal</td>
 * <td>No</td>
 * <td>Internal interactions between contact center correspondents</td>
 * </tr>
 * <tr>
 * <td>403</td>
 * <td>113</td>
 * <td>InboundNew</td>
 * <td>Inbound New</td>
 * <td>No</td>
 * <td>New incoming email. Starts a new thread</td>
 * </tr>
 * <tr>
 * <td>404</td>
 * <td>113</td>
 * <td>InboundCustomerReply</td>
 * <td>Customer Reply</td>
 * <td>No</td>
 * <td>Reply from a customer</td>
 * </tr>
 * <tr>
 * <td>405</td>
 * <td>113</td>
 * <td>InboundCollaborationReply</td>
 * <td>Inbound Collaboration Reply</td>
 * <td>No</td>
 * <td>Reply from an external source</td>
 * </tr>
 * <tr>
 * <td>406</td>
 * <td>113</td>
 * <td>InboundNDR</td>
 * <td>NDR</td>
 * <td>No</td>
 * <td>Error message sent back to the system by SMTP server chain</td>
 * </tr>
 * <tr>
 * <td>407</td>
 * <td>113</td>
 * <td>OutboundNew</td>
 * <td>Outbound New</td>
 * <td>No</td>
 * <td>New message from Contact Center to a customer</td>
 * </tr>
 * <tr>
 * <td>408</td>
 * <td>113</td>
 * <td>OutboundReply</td>
 * <td>Reply</td>
 * <td>No</td>
 * <td>Reply from Contact Center to a customer</td>
 * </tr>
 * <tr>
 * <td>409</td>
 * <td>113</td>
 * <td>OutboundAcknowledgement</td>
 * <td>Acknowledgement</td>
 * <td>No</td>
 * <td>Ack message sent to customer by Contact Center</td>
 * </tr>
 * <tr>
 * <td>410</td>
 * <td>113</td>
 * <td>OutboundAutoResponse</td>
 * <td>Auto Response</td>
 * <td>No</td>
 * <td>Automated reply from Contact Center to a customer</td>
 * </tr>
 * <tr>
 * <td>411</td>
 * <td>113</td>
 * <td>OutboundRedirect</td>
 * <td>Redirect</td>
 * <td>No</td>
 * <td>Message redirect to an external resource. No reply expected.</td>
 * </tr>
 * <tr>
 * <td>412</td>
 * <td>113</td>
 * <td>OutboundCollaborationInvite</td>
 * <td>Outbound Collaboration Invite</td>
 * <td>No</td>
 * <td>New message to an external resource</td>
 * </tr>
 * <tr>
 * <td>413</td>
 * <td>113</td>
 * <td>InternalCollaborationInvite</td>
 * <td>Internal Collaboration Invite</td>
 * <td>No</td>
 * <td>New message to an internal resource</td>
 * </tr>
 * <tr>
 * <td>414</td>
 * <td>113</td>
 * <td>InternalCollaborationReply</td>
 * <td>Internal Collaboration Reply</td>
 * <td>No</td>
 * <td>Reply from an internal resource</td>
 * </tr>
 * <tr>
 * <td>415</td>
 * <td>117</td>
 * <td>Normal</td>
 * <td>Normal</td>
 * <td>No</td>
 * <td>StopProcessing Reason Normal</td>
 * </tr>
 * <tr>
 * <td>416</td>
 * <td>117</td>
 * <td>AutoResponded</td>
 * <td>Auto Responded</td>
 * <td>No</td>
 * <td>StopProcessing Reason Auto Responded</td>
 * </tr>
 * <tr>
 * <td>417</td>
 * <td>117</td>
 * <td>Terminated</td>
 * <td>Terminated</td>
 * <td>No</td>
 * <td>StopProcessing Reason Terminated</td>
 * </tr>
 * <tr>
 * <td>418</td>
 * <td>117</td>
 * <td>Sent</td>
 * <td>Sent</td>
 * <td>No</td>
 * <td>StopProcessing Reason Sent</td>
 * </tr>
 * <tr>
 * <td>419</td>
 * <td>117</td>
 * <td>Forwarded</td>
 * <td>Forwarded</td>
 * <td>No</td>
 * <td>StopProcessing Reason Forwarded</td>
 * </tr>
 * <tr>
 * <td>420</td>
 * <td>117</td>
 * <td>Re-directed</td>
 * <td>Re-directed</td>
 * <td>No</td>
 * <td>StopProcessing Reason Re-directed</td>
 * </tr>
 * <tr>
 * <td>421</td>
 * <td>118</td>
 * <td>English</td>
 * <td>English</td>
 * <td>No</td>
 * <td>Language English</td>
 * </tr>
 * <tr>
 * <td>422</td>
 * <td>121</td>
 * <td>Priority</td>
 * <td>Priority</td>
 * <td>No</td>
 * <td>Interaction Attributes Priority</td>
 * </tr>
 * <tr>
 * <td>423</td>
 * <td>121</td>
 * <td>Category</td>
 * <td>Category</td>
 * <td>No</td>
 * <td>Interaction Attributes Category</td>
 * </tr>
 * <tr>
 * <td>424</td>
 * <td>121</td>
 * <td>ServiceType</td>
 * <td>Service Type</td>
 * <td>No</td>
 * <td>Interaction Attributes Service Type</td>
 * </tr>
 * <tr>
 * <td>425</td>
 * <td>121</td>
 * <td>MediaType</td>
 * <td>Media Type</td>
 * <td>No</td>
 * <td>Interaction Attributes Media Type</td>
 * </tr>
 * <tr>
 * <td>426</td>
 * <td>121</td>
 * <td>InteractionType</td>
 * <td>Interaction Type</td>
 * <td>No</td>
 * <td>Interaction Attributes Interaction Type</td>
 * </tr>
 * <tr>
 * <td>427</td>
 * <td>121</td>
 * <td>InteractionSubtype</td>
 * <td>Interaction Subtype</td>
 * <td>No</td>
 * <td>Interaction Attributes Interaction Subtype</td>
 * </tr>
 * <tr>
 * <td>428</td>
 * <td>121</td>
 * <td>Language</td>
 * <td>Language</td>
 * <td>No</td>
 * <td>Interaction Attributes Language</td>
 * </tr>
 * <tr>
 * <td>429</td>
 * <td>121</td>
 * <td>StopProcessing-Reason</td>
 * <td>StopProcessing Reason</td>
 * <td>No</td>
 * <td>Interaction Attributes StopProcessing Reason</td>
 * </tr>
 * <tr>
 * <td>430</td>
 * <td>121</td>
 * <td>DispositionCode</td>
 * <td>Disposition Code</td>
 * <td>No</td>
 * <td>Interaction Attributes Disposition Code</td>
 * </tr>
 * <tr>
 * <td>431</td>
 * <td>121</td>
 * <td>ReasonCode</td>
 * <td>Reason Code</td>
 * <td>No</td>
 * <td>Interaction Attributes Reason Code</td>
 * </tr>
 * <tr>
 * <td>432</td>
 * <td>122</td>
 * <td>FirstName</td>
 * <td>First Name</td>
 * <td>No</td>
 * <td>Contact Attributes First Name</td>
 * </tr>
 * <tr>
 * <td>433</td>
 * <td>122</td>
 * <td>LastName</td>
 * <td>Last Name</td>
 * <td>No</td>
 * <td>Contact Attributes Last Name</td>
 * </tr>
 * <tr>
 * <td>434</td>
 * <td>122</td>
 * <td>Title</td>
 * <td>Title</td>
 * <td>No</td>
 * <td>Contact Attributes Title</td>
 * </tr>
 * <tr>
 * <td>435</td>
 * <td>122</td>
 * <td>EmailAddress</td>
 * <td>Email Address</td>
 * <td>No</td>
 * <td>Contact Attributes E-mail Address</td>
 * </tr>
 * <tr>
 * <td>436</td>
 * <td>122</td>
 * <td>PhoneNumber</td>
 * <td>Phone Number</td>
 * <td>No</td>
 * <td>Contact Attributes Phone Number</td>
 * </tr>
 * <tr>
 * <td>437</td>
 * <td>122</td>
 * <td>AccountNumber</td>
 * <td>Account Number</td>
 * <td>No</td>
 * <td>Contact Attributes Account Number</td>
 * </tr>
 * <tr>
 * <td>438</td>
 * <td>122</td>
 * <td>ContactId</td>
 * <td>Contact ID</td>
 * <td>No</td>
 * <td>Contact Attributes Contact ID</td>
 * </tr>
 * <tr>
 * <td>439</td>
 * <td>122</td>
 * <td>CustomerSegment</td>
 * <td>Customer Segment</td>
 * <td>No</td>
 * <td>Contact Attributes Customer Segment</td>
 * </tr>
 * <tr>
 * <td>440</td>
 * <td>122</td>
 * <td>PIN</td>
 * <td>PIN</td>
 * <td>No</td>
 * <td>Contact Attributes PIN</td>
 * </tr>
 * </tbody>
 * </table><br/>
 * <p/>
 * On GUI representation the name ___EnumeratorValue___ was
 * replaced on ___Attribute Value___
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaEnumeratorValue.html">
 * <b>CfgDeltaEnumeratorValue</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgEnumerator.html">
 * <b>CfgEnumerator</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgEnumeratorValue
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGEnumeratorValue;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgEnumeratorValue(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgEnumeratorValue - "
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
    public CfgEnumeratorValue(
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
    public CfgEnumeratorValue(final IConfService confService) {
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
            if (getMetaData().getAttribute("enumeratorDBID") != null) {
                if (getLinkValue("enumeratorDBID") == null) {
                    throw new ConfigException("Mandatory property 'enumeratorDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("name") != null) {
                if (getProperty("name") == null) {
                    throw new ConfigException("Mandatory property 'name' not set.");
                }
            }
            if (getMetaData().getAttribute("isDefault") != null) {
                if (getProperty("isDefault") == null) {
                    setProperty("isDefault", 1);
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
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
     * A unique identifier of the <code>
     * <a href="CfgEnumerator.html">CfgEnumerator</a>
     * </code> to which this value
     * is allocated. Mandatory. Once specified, cannot be changed.
     *
     * @return instance of referred object or null
     */
    public final CfgEnumerator getEnumerator() {
        return (CfgEnumerator) getProperty(CfgEnumerator.class, "enumeratorDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumerator.html">CfgEnumerator</a>
     * </code> to which this value
     * is allocated. Mandatory. Once specified, cannot be changed.
     *
     * @param value new property value
     * @see #getEnumerator()
     */
    public final void setEnumerator(final CfgEnumerator value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("enumeratorDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumerator.html">CfgEnumerator</a>
     * </code> to which this value
     * is allocated. Mandatory. Once specified, cannot be changed.
     *
     * @param dbid DBID identifier of referred object
     * @see #getEnumerator()
     */
    public final void setEnumeratorDBID(final int dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("enumeratorDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Enumerator property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getEnumeratorDBID() {
        return getLinkValue("enumeratorDBID");
    }

    /**
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code> to which this enumerator value is allocated. Mandatory. Once
     * specified, cannot be changed.
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
     * </code> to which this enumerator value is allocated. Mandatory. Once
     * specified, cannot be changed.
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
     * </code> to which this enumerator value is allocated. Mandatory. Once
     * specified, cannot be changed.
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
     * Must be unique within the enumerator object. Once specified, can
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
     * Must be unique within the enumerator object. Once specified, can
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
     * of the enumerator value.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A pointer to the description
     * of the enumerator value.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * An indicator whether the value
     * is default. See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getIsDefault() {
        return (CfgFlag) getProperty(CfgFlag.class, "isDefault");
    }

    /**
     * An indicator whether the value
     * is default. See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getIsDefault()
     */
    public final void setIsDefault(final CfgFlag value) {
        setProperty("isDefault", value);
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
