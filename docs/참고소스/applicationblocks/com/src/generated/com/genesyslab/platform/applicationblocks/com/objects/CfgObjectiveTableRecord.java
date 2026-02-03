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
 * <em>Objective Records</em> define records within Objective Table objects. The
 * <code>mediaTypeDBID, serviceTypeDBID</code> and <code>customerSegmentDBID</code>
 * properties are mandatory. They compose a unique key for these records within the
 * Objective Table that contains them.
 *
 * <p/>
 * The following table presents the default objectiveThreshold values
 * for corresponding media types.
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <caption><font size="+1"><strong>Threshold Values for Media Types
 * </strong></font></caption>
 * <thead>
 * <tr bgcolor="#ccccff" class="tableheadingcolor">
 * <th align="left">MediaType</th>
 * <th align="left">objectiveThreshhold (default values)</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>Voice</td>
 * <td>20 sec</td>
 * </tr>
 * <tr>
 * <td>voip</td>
 * <td>20 sec</td>
 * </tr>
 * <tr>
 * <td>Email</td>
 * <td>24 hours (60x60x24=86400 sec)</td>
 * </tr>
 * <tr>
 * <td>Vmail</td>
 * <td>24 hours (60x60x24=86400 sec)</td>
 * </tr>
 * <tr>
 * <td>Smail</td>
 * <td>24 hours (60x60x24=86400 sec)</td>
 * </tr>
 * <tr>
 * <td>Chat</td>
 * <td>20 sec</td>
 * </tr>
 * <tr>
 * <td>Video</td>
 * <td>20 sec</td>
 * </tr>
 * <tr>
 * <td>Cobrowsing</td>
 * <td>-</td>
 * </tr>
 * <tr>
 * <td>Whiteboard</td>
 * <td>-</td>
 * </tr>
 * <tr>
 * <td>Appsharing</td>
 * <td>-</td>
 * </tr>
 * <tr>
 * <td>Webform</td>
 * <td>24 hours (60x60x24=86400 sec)</td>
 * </tr>
 * <tr>
 * <td>Workitem</td>
 * <td>24 hours (60x60x24=86400 sec)</td>
 * </tr>
 * <tr>
 * <td>Callback</td>
 * <td>1 hour (3600 sec)</td>
 * </tr>
 * <tr>
 * <td>Fax</td>
 * <td>24 hours (60x60x24=86400 sec)</td>
 * </tr>
 * <tr>
 * <td>Imchat</td>
 * <td>20 sec</td>
 * </tr>
 * <tr>
 * <td>Busevent</td>
 * <td>1 hour (60x60=3600 sec)</td>
 * </tr>
 * <tr>
 * <td>Alert</td>
 * <td>20 sec</td>
 * </tr>
 * <tr>
 * <td>Sms</td>
 * <td>20 sec</td>
 * </tr>
 * <tr>
 * <td>Any</td>
 * <td>24 hours (60x60x24=86400 sec)</td>
 * </tr>
 * </tbody>
 * </table><br/>
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgObjectiveTableRecord extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgObjectiveTableRecord(
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
    public CfgObjectiveTableRecord(
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
    public CfgObjectiveTableRecord(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgObjectiveTableRecord")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
            if (getMetaData().getAttribute("mediaTypeDBID") != null) {
                if (getLinkValue("mediaTypeDBID") == null) {
                    throw new ConfigException("Mandatory property 'mediaTypeDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("serviceTypeDBID") != null) {
                if (getLinkValue("serviceTypeDBID") == null) {
                    throw new ConfigException("Mandatory property 'serviceTypeDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("customerSegmentDBID") != null) {
                if (getLinkValue("customerSegmentDBID") == null) {
                    throw new ConfigException("Mandatory property 'customerSegmentDBID' not set.");
                }
            }
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumeratorValue.html">Media Type</a>
     * </code> to which this objective table
     * record is allocated. Mandatory. Once specified, cannot be changed.
     * Only enumerator values belonging to the enumerator with name <code>MediaType</code>
     * are allowed in this field.
     *
     * @return instance of referred object or null
     */
    public final CfgEnumeratorValue getMediaType() {
        return (CfgEnumeratorValue) getProperty(CfgEnumeratorValue.class, "mediaTypeDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumeratorValue.html">Media Type</a>
     * </code> to which this objective table
     * record is allocated. Mandatory. Once specified, cannot be changed.
     * Only enumerator values belonging to the enumerator with name <code>MediaType</code>
     * are allowed in this field.
     *
     * @param value new property value
     * @see #getMediaType()
     */
    public final void setMediaType(final CfgEnumeratorValue value) {
        setProperty("mediaTypeDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumeratorValue.html">Media Type</a>
     * </code> to which this objective table
     * record is allocated. Mandatory. Once specified, cannot be changed.
     * Only enumerator values belonging to the enumerator with name <code>MediaType</code>
     * are allowed in this field.
     *
     * @param dbid DBID identifier of referred object
     * @see #getMediaType()
     */
    public final void setMediaTypeDBID(final int dbid) {
        setProperty("mediaTypeDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the MediaType property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getMediaTypeDBID() {
        return getLinkValue("mediaTypeDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumeratorValue.html">Service Type</a>
     * </code> this objective table
     * record is associated with. Mandatory. Once specified, cannot be
     * changed. Only enumerator values belonging to the enumerator with
     * name <code>ServiceType</code> are allowed in this field.
     *
     * @return instance of referred object or null
     */
    public final CfgEnumeratorValue getServiceType() {
        return (CfgEnumeratorValue) getProperty(CfgEnumeratorValue.class, "serviceTypeDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumeratorValue.html">Service Type</a>
     * </code> this objective table
     * record is associated with. Mandatory. Once specified, cannot be
     * changed. Only enumerator values belonging to the enumerator with
     * name <code>ServiceType</code> are allowed in this field.
     *
     * @param value new property value
     * @see #getServiceType()
     */
    public final void setServiceType(final CfgEnumeratorValue value) {
        setProperty("serviceTypeDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumeratorValue.html">Service Type</a>
     * </code> this objective table
     * record is associated with. Mandatory. Once specified, cannot be
     * changed. Only enumerator values belonging to the enumerator with
     * name <code>ServiceType</code> are allowed in this field.
     *
     * @param dbid DBID identifier of referred object
     * @see #getServiceType()
     */
    public final void setServiceTypeDBID(final int dbid) {
        setProperty("serviceTypeDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the ServiceType property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getServiceTypeDBID() {
        return getLinkValue("serviceTypeDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumeratorValue.html">Customer Segment</a>
     * </code> this objective
     * table record is associated with. Mandatory. Once specified, cannot
     * be changed. Only enumerator values belonging to the enumerator with
     * name <code>CustomerSegment</code> are allowed in this field.
     *
     * @return instance of referred object or null
     */
    public final CfgEnumeratorValue getCustomerSegment() {
        return (CfgEnumeratorValue) getProperty(CfgEnumeratorValue.class, "customerSegmentDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumeratorValue.html">Customer Segment</a>
     * </code> this objective
     * table record is associated with. Mandatory. Once specified, cannot
     * be changed. Only enumerator values belonging to the enumerator with
     * name <code>CustomerSegment</code> are allowed in this field.
     *
     * @param value new property value
     * @see #getCustomerSegment()
     */
    public final void setCustomerSegment(final CfgEnumeratorValue value) {
        setProperty("customerSegmentDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgEnumeratorValue.html">Customer Segment</a>
     * </code> this objective
     * table record is associated with. Mandatory. Once specified, cannot
     * be changed. Only enumerator values belonging to the enumerator with
     * name <code>CustomerSegment</code> are allowed in this field.
     *
     * @param dbid DBID identifier of referred object
     * @see #getCustomerSegment()
     */
    public final void setCustomerSegmentDBID(final int dbid) {
        setProperty("customerSegmentDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the CustomerSegment property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getCustomerSegmentDBID() {
        return getLinkValue("customerSegmentDBID");
    }

    public final Integer getObjectiveThreshold() {
        return (Integer) getProperty("objectiveThreshold");
    }

    public final void setObjectiveThreshold(final Integer value) {
        setProperty("objectiveThreshold", value);
    }

    /**
     * An objective delta value for this record. Defines the step
     * of objective threshold deviation
     *
     * @return property value or null
     */
    public final Integer getObjectiveDelta() {
        return (Integer) getProperty("objectiveDelta");
    }

    /**
     * An objective delta value for this record. Defines the step
     * of objective threshold deviation
     *
     * @param value new property value
     * @see #getObjectiveDelta()
     */
    public final void setObjectiveDelta(final Integer value) {
        setProperty("objectiveDelta", value);
    }

    /**
     * An <code>
     * <a href="CfgStatTable.html">IT Contract</a>
     * </code> associated with this Objective Record.
     *
     * @return instance of referred object or null
     */
    public final CfgStatTable getContract() {
        return (CfgStatTable) getProperty(CfgStatTable.class, "contractDBID");
    }

    /**
     * An <code>
     * <a href="CfgStatTable.html">IT Contract</a>
     * </code> associated with this Objective Record.
     *
     * @param value new property value
     * @see #getContract()
     */
    public final void setContract(final CfgStatTable value) {
        setProperty("contractDBID", value);
    }

    /**
     * An <code>
     * <a href="CfgStatTable.html">IT Contract</a>
     * </code> associated with this Objective Record.
     *
     * @param dbid DBID identifier of referred object
     * @see #getContract()
     */
    public final void setContractDBID(final int dbid) {
        setProperty("contractDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Contract property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getContractDBID() {
        return getLinkValue("contractDBID");
    }
}
