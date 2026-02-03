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
 * <em>Treatments</em>, which are most often used in automated outbound campaigns,
 * tell Outbound Contact Server (OCS) how to respond to an unsuccessful call result
 * (a call that does not reach the intended party). For example, the response to
 * an unsuccessful connection may be to redial, and the response to a successful
 * connection may be to play a message.
 * <p/>
 * A Treatment <em>Sequence</em> is a group of treatment objects that all contain
 * the same Call Result value. Treatments in a sequence are applied to a call in
 * their numerical order (see the Treatment property Number in Sequence).
 *
 * <p/>
 * One Treatment can be associated with several CallingLists
 * (see <code>
 * <a href="CfgCallingList.html">CfgCallingList</a>
 * </code>).
 * <p/>
 * Deletion of Treatment X will cause the following events set
 * out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>treatmentDBIDs</code> of all calling
 * lists that included Treatment X
 * </li>
 * <li>
 * Deletion of Treatment X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaTreatment.html">
 * <b>CfgDeltaTreatment</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgCallingList.html">
 * <b>CfgCallingList</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgTreatment
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGTreatment;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgTreatment(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgTreatment - "
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
    public CfgTreatment(
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
    public CfgTreatment(final IConfService confService) {
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
            if (getMetaData().getAttribute("recActionCode") != null) {
                if (getProperty("recActionCode") == null) {
                    throw new ConfigException("Mandatory property 'recActionCode' not set.");
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
     * to which this treatment action is allocated. Mandatory.Once specified,
     * cannot be changed.
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
     * to which this treatment action is allocated. Mandatory.Once specified,
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
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">CfgTenant</a>
     * </code>
     * to which this treatment action is allocated. Mandatory.Once specified,
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
     * A pointer to treatment action name.
     * Mandatory.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to treatment action name.
     * Mandatory.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * A pointer to treatment action
     * description.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A pointer to treatment action
     * description.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * A call result related to this
     * treatment. Refer to <code>
     * <a href="../Enumerations/GctiCallState.html">GctiCallState</a>
     * </code> in Variable Types of
     * <code>Common</code> APIs. Mandatory.
     *
     * @return property value or null
     */
    public final GctiCallState getCallResult() {
        return (GctiCallState) getProperty(GctiCallState.class, "callResult");
    }

    /**
     * A call result related to this
     * treatment. Refer to <code>
     * <a href="../Enumerations/GctiCallState.html">GctiCallState</a>
     * </code> in Variable Types of
     * <code>Common</code> APIs. Mandatory.
     *
     * @param value new property value
     * @see #getCallResult()
     */
    public final void setCallResult(final GctiCallState value) {
        setProperty("callResult", value);
    }

    /**
     * A record action code. Refer
     * to <code>
     * <a href="../Enumerations/CfgRecActionCode.html">CfgRecActionCode</a>
     * </code> in User Defined Variable Types.
     *
     * @return property value or null
     */
    public final CfgRecActionCode getRecActionCode() {
        return (CfgRecActionCode) getProperty(CfgRecActionCode.class, "recActionCode");
    }

    /**
     * A record action code. Refer
     * to <code>
     * <a href="../Enumerations/CfgRecActionCode.html">CfgRecActionCode</a>
     * </code> in User Defined Variable Types.
     *
     * @param value new property value
     * @see #getRecActionCode()
     */
    public final void setRecActionCode(final CfgRecActionCode value) {
        setProperty("recActionCode", value);
    }

    /**
     * An attempt number to which the
     * action should be performed.
     *
     * @return property value or null
     */
    public final Integer getAttempts() {
        return (Integer) getProperty("attempts");
    }

    /**
     * An attempt number to which the
     * action should be performed.
     *
     * @param value new property value
     * @see #getAttempts()
     */
    public final void setAttempts(final Integer value) {
        setProperty("attempts", value);
    }

    /**
     * A time and date when another attempt
     * must be applied again to dn. The parameter is used if recActionCode
     * is set to <code>CFGRACRetryAtDate</code>. Refer to time_t from time.h
     * of ANSI C library.
     *
     * @return property value or null
     */
    public final Calendar getDateTime() {
        return (Calendar) getProperty("dateTime");
    }

    /**
     * A time and date when another attempt
     * must be applied again to dn. The parameter is used if recActionCode
     * is set to <code>CFGRACRetryAtDate</code>. Refer to time_t from time.h
     * of ANSI C library.
     *
     * @param value new property value
     * @see #getDateTime()
     */
    public final void setDateTime(final Calendar value) {
        setProperty("dateTime", value);
    }

    /**
     * An maximum number of sequential
     * attempts the treatment can be applied to dn. The parameter is used
     * if recActionCode is set to <code>CFGRACCycle</code>.
     *
     * @return property value or null
     */
    public final Integer getCycleAttempt() {
        return (Integer) getProperty("cycleAttempt");
    }

    /**
     * An maximum number of sequential
     * attempts the treatment can be applied to dn. The parameter is used
     * if recActionCode is set to <code>CFGRACCycle</code>.
     *
     * @param value new property value
     * @see #getCycleAttempt()
     */
    public final void setCycleAttempt(final Integer value) {
        setProperty("cycleAttempt", value);
    }

    /**
     * A time interval in minutes between
     * attempts. The parameter is used if
     * <code>recActionCode</code> is set either to <code>CFGRACCycle</code> or
     * <code>CFGRACRetryIn</code>.
     *
     * @return property value or null
     */
    public final Integer getInterval() {
        return (Integer) getProperty("interval");
    }

    /**
     * A time interval in minutes between
     * attempts. The parameter is used if
     * <code>recActionCode</code> is set either to <code>CFGRACCycle</code> or
     * <code>CFGRACRetryIn</code>.
     *
     * @param value new property value
     * @see #getInterval()
     */
    public final void setInterval(final Integer value) {
        setProperty("interval", value);
    }

    /**
     * The time in interval in minutes
     * which increments the interval after each attempt. The parameter
     * is used if <code>recActionCode</code> is set either to <code>CFGRACCycle</code>.
     *
     * @return property value or null
     */
    public final Integer getIncrement() {
        return (Integer) getProperty("increment");
    }

    /**
     * The time in interval in minutes
     * which increments the interval after each attempt. The parameter
     * is used if <code>recActionCode</code> is set either to <code>CFGRACCycle</code>.
     *
     * @param value new property value
     * @see #getIncrement()
     */
    public final void setIncrement(final Integer value) {
        setProperty("increment", value);
    }

    /**
     * A call action code. Refer to
     * <code>
     * <a href="../Enumerations/CfgCallActionCode.html">CfgCallActionCode</a>
     * </code> in User Defined Variable
     * Types. The <code>callActionCode</code> can be applied to following
     * call results only. (Refer to <code>GctiCallState</code> in Variable Types
     * of <code>Common</code> APIs):
     *
     * @return property value or null
     */
    public final CfgCallActionCode getCallActionCode() {
        return (CfgCallActionCode) getProperty(CfgCallActionCode.class, "callActionCode");
    }

    /**
     * A call action code. Refer to
     * <code>
     * <a href="../Enumerations/CfgCallActionCode.html">CfgCallActionCode</a>
     * </code> in User Defined Variable
     * Types. The <code>callActionCode</code> can be applied to following
     * call results only. (Refer to <code>GctiCallState</code> in Variable Types
     * of <code>Common</code> APIs):
     *
     * @param value new property value
     * @see #getCallActionCode()
     */
    public final void setCallActionCode(final CfgCallActionCode value) {
        setProperty("callActionCode", value);
    }

    public final CfgDN getDestDN() {
        return (CfgDN) getProperty(CfgDN.class, "destDNDBID");
    }

    public final void setDestDN(final CfgDN value) {
        setProperty("destDNDBID", value);
    }

    public final void setDestDNDBID(final int dbid) {
        setProperty("destDNDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the DestDN property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getDestDNDBID() {
        return getLinkValue("destDNDBID");
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
     * Parameter defining a time range.
     *
     * @return property value or null
     */
    public final Integer getRange() {
        return (Integer) getProperty("range");
    }

    /**
     * Parameter defining a time range.
     *
     * @param value new property value
     * @see #getRange()
     */
    public final void setRange(final Integer value) {
        setProperty("range", value);
    }
}
