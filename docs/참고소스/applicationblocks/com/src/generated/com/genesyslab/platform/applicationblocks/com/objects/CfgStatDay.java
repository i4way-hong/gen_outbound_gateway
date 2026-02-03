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
 * A <em>Statistical Day</em> is a numerically-expressed workload that a particular
 * Agent Group is expected to handle during a particular business day.
 *
 * <p/>
 * Deletion of Stat Day X will cause the following events
 * set out in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>statDayDBIDs</code> of all stat tables
 * that included Stat Day X
 * </li>
 * <li>
 * Deletion of Stat Day X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaStatDay.html">
 * <b>CfgDeltaStatDay</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgStatTable.html">
 * <b>CfgStatTable</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgStatDay
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGStatDay;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgStatDay(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgStatDay - "
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
    public CfgStatDay(
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
    public CfgStatDay(final IConfService confService) {
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
            if (getMetaData().getAttribute("isDayOfWeek") != null) {
                if (getProperty("isDayOfWeek") == null) {
                    throw new ConfigException("Mandatory property 'isDayOfWeek' not set.");
                }
            }
            if (getMetaData().getAttribute("day") != null) {
                if (getProperty("day") == null) {
                    throw new ConfigException("Mandatory property 'day' not set.");
                }
            }
            if (getMetaData().getAttribute("statIntervals") != null) {
                Collection<CfgStatInterval> statIntervals = (Collection<CfgStatInterval>) getProperty("statIntervals");
                if (statIntervals != null) {
                    for (CfgStatInterval item : statIntervals) {
                        item.checkPropertiesForSave();
                    }
                }
            }
            if (getMetaData().getAttribute("state") != null) {
                if (getProperty("state") == null) {
                    setProperty("state", 1);
                }
            }
            if (getMetaData().getAttribute("type") != null) {
                if (getProperty("type") == null) {
                    setProperty("type", 1);
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
     * <a href="CfgTenant.html">Tenant</a>
     * </code> that this day is defined for. Mandatory. Once specified,
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
     * <a href="CfgTenant.html">Tenant</a>
     * </code> that this day is defined for. Mandatory. Once specified,
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
     * <a href="CfgTenant.html">Tenant</a>
     * </code> that this day is defined for. Mandatory. Once specified,
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
     * A pointer to the name of the day.
     * Mandatory. Must be unique within the tenant.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the name of the day.
     * Mandatory. Must be unique within the tenant.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Indicator of whether this day
     * is identified as a day of week (
     * <code>CFGTrue</code>) or a day of year (<code>CFGFalse</code>).
     * Mandatory. Once specified, cannot be changed. The parameter is ignored
     * if value of parameter <code>date</code> is specified.
     * Refer to <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getIsDayOfWeek() {
        return (CfgFlag) getProperty(CfgFlag.class, "isDayOfWeek");
    }

    /**
     * Indicator of whether this day
     * is identified as a day of week (
     * <code>CFGTrue</code>) or a day of year (<code>CFGFalse</code>).
     * Mandatory. Once specified, cannot be changed. The parameter is ignored
     * if value of parameter <code>date</code> is specified.
     * Refer to <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getIsDayOfWeek()
     */
    public final void setIsDayOfWeek(final CfgFlag value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("isDayOfWeek", value);
    }

    /**
     * Day's number. If
     * <code>isDayOfWeek</code> is set to <code>CFGTrue</code>,
     * the allowable range is from <code>1</code> to <code>7</code> (where
     * <code>1</code> stands for <code>Sunday</code>). If <code>isDayOfWeek</code> is
     * set to <code>CFGFalse</code>, the allowable range is from <code>1</code> to
     * <code>366</code> and <code>1</code> (where <code>1</code> stands
     * for<code> January 1</code> and <code>-1</code> stands for<code> any
     * day</code>). Mandatory. Once specified, cannot be changed. The parameter
     * is ignored if value of parameter <code>date</code> is specified.
     *
     * @return property value or null
     */
    public final Integer getDay() {
        return (Integer) getProperty("day");
    }

    /**
     * Day's number. If
     * <code>isDayOfWeek</code> is set to <code>CFGTrue</code>,
     * the allowable range is from <code>1</code> to <code>7</code> (where
     * <code>1</code> stands for <code>Sunday</code>). If <code>isDayOfWeek</code> is
     * set to <code>CFGFalse</code>, the allowable range is from <code>1</code> to
     * <code>366</code> and <code>1</code> (where <code>1</code> stands
     * for<code> January 1</code> and <code>-1</code> stands for<code> any
     * day</code>). Mandatory. Once specified, cannot be changed. The parameter
     * is ignored if value of parameter <code>date</code> is specified.
     *
     * @param value new property value
     * @see #getDay()
     */
    public final void setDay(final Integer value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("day", value);
    }

    /**
     * Start of business time of day
     * measured in minutes from 00:00. Cannot be negative and can not be
     * greater than 1440.
     *
     * @return property value or null
     */
    public final Integer getStartTime() {
        return (Integer) getProperty("startTime");
    }

    /**
     * Start of business time of day
     * measured in minutes from 00:00. Cannot be negative and can not be
     * greater than 1440.
     *
     * @param value new property value
     * @see #getStartTime()
     */
    public final void setStartTime(final Integer value) {
        setProperty("startTime", value);
    }

    /**
     * End of business time of day measured
     * in minutes from 00:00. Cannot be negative and can not be greater
     * than 1440. If set to be less than the setting for <code>startTime</code>,
     * implies the time of the next calendar day (night shift).
     *
     * @return property value or null
     */
    public final Integer getEndTime() {
        return (Integer) getProperty("endTime");
    }

    /**
     * End of business time of day measured
     * in minutes from 00:00. Cannot be negative and can not be greater
     * than 1440. If set to be less than the setting for <code>startTime</code>,
     * implies the time of the next calendar day (night shift).
     *
     * @param value new property value
     * @see #getEndTime()
     */
    public final void setEndTime(final Integer value) {
        setProperty("endTime", value);
    }

    /**
     * Minimum statistical value for
     * the day. Cannot be negative.
     *
     * @return property value or null
     */
    public final Integer getMinValue() {
        return (Integer) getProperty("minValue");
    }

    /**
     * Minimum statistical value for
     * the day. Cannot be negative.
     *
     * @param value new property value
     * @see #getMinValue()
     */
    public final void setMinValue(final Integer value) {
        setProperty("minValue", value);
    }

    /**
     * Maximum statistical value for
     * the day. Cannot be negative or less than the setting for <code>minValue</code>.
     *
     * @return property value or null
     */
    public final Integer getMaxValue() {
        return (Integer) getProperty("maxValue");
    }

    /**
     * Maximum statistical value for
     * the day. Cannot be negative or less than the setting for <code>minValue</code>.
     *
     * @param value new property value
     * @see #getMaxValue()
     */
    public final void setMaxValue(final Integer value) {
        setProperty("maxValue", value);
    }

    /**
     * Target statistical value for
     * the day. Cannot be less than the setting for
     * <code>minVlaue</code> or greater than the setting for <code>maxValue</code>.
     *
     * @return property value or null
     */
    public final Integer getTargetValue() {
        return (Integer) getProperty("targetValue");
    }

    /**
     * Target statistical value for
     * the day. Cannot be less than the setting for
     * <code>minVlaue</code> or greater than the setting for <code>maxValue</code>.
     *
     * @param value new property value
     * @see #getTargetValue()
     */
    public final void setTargetValue(final Integer value) {
        setProperty("targetValue", value);
    }

    /**
     * Statistical interval in minutes. Must be a multiple of five.
     * Once specified, cannot be changed. Recommended to be set to 15 by
     * default.
     *
     * @return property value or null
     */
    public final Integer getIntervalLength() {
        return (Integer) getProperty("intervalLength");
    }

    /**
     * Statistical interval in minutes. Must be a multiple of five.
     * Once specified, cannot be changed. Recommended to be set to 15 by
     * default.
     *
     * @param value new property value
     * @see #getIntervalLength()
     */
    public final void setIntervalLength(final Integer value) {
        setProperty("intervalLength", value);
    }

    /**
     * A pointer to the list of
     * the structures of <code>
     * <a href="CfgStatInterval.html">CfgStatInterval</a>
     * </code> type that associate intervals
     * with certain statistical values. When used as an entry in <code>
     * <a href="CfgDeltaStatDay.html">CfgDeltaStatDay</a>
     * </code>
     * (see below), it is a pointer to a list of structures of type <code>
     * <a href="CfgStatInterval.html">CfgStatInterval</a>
     * </code>
     * added to the existing list.
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgStatInterval> getStatIntervals() {
        return (Collection<CfgStatInterval>) getProperty("statIntervals");
    }

    /**
     * A pointer to the list of
     * the structures of <code>
     * <a href="CfgStatInterval.html">CfgStatInterval</a>
     * </code> type that associate intervals
     * with certain statistical values. When used as an entry in <code>
     * <a href="CfgDeltaStatDay.html">CfgDeltaStatDay</a>
     * </code>
     * (see below), it is a pointer to a list of structures of type <code>
     * <a href="CfgStatInterval.html">CfgStatInterval</a>
     * </code>
     * added to the existing list.
     *
     * @param value new property value
     * @see #getStatIntervals()
     */
    public final void setStatIntervals(final Collection<CfgStatInterval> value) {
        setProperty("statIntervals", value);
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
     * Day of specific year calculated since
     * 00:00:00 GMT Jan 1, 1970 measured in seconds. Once specified, cannot
     * be changed. If value is specified the values of parameters
     * <code>isDayOfWeek</code> and <code>day</code> must be ignored.
     *
     * @return property value or null
     */
    public final Calendar getDate() {
        return (Calendar) getProperty("date");
    }

    /**
     * Day of specific year calculated since
     * 00:00:00 GMT Jan 1, 1970 measured in seconds. Once specified, cannot
     * be changed. If value is specified the values of parameters
     * <code>isDayOfWeek</code> and <code>day</code> must be ignored.
     *
     * @param value new property value
     * @see #getDate()
     */
    public final void setDate(final Calendar value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("date", value);
    }

    /**
     * Statistical Day type. Refer to <code>
     * <a href="../Enumerations/CfgStatDayType.html">CfgStatDayType</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgStatDayType getType() {
        return (CfgStatDayType) getProperty(CfgStatDayType.class, "type");
    }

    /**
     * Statistical Day type. Refer to <code>
     * <a href="../Enumerations/CfgStatDayType.html">CfgStatDayType</a>
     * </code>
     *
     * @param value new property value
     * @see #getType()
     */
    public final void setType(final CfgStatDayType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("type", value);
    }

    /**
     * Flag determining whether flatRate (CFGTrue) should be selected. Refer to <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getUseFlatRate() {
        return (CfgFlag) getProperty(CfgFlag.class, "useFlatRate");
    }

    /**
     * Flag determining whether flatRate (CFGTrue) should be selected. Refer to <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getUseFlatRate()
     */
    public final void setUseFlatRate(final CfgFlag value) {
        setProperty("useFlatRate", value);
    }

    /**
     * Amount to be charged for processing the forecast volume of interactions.
     *
     * @return property value or null
     */
    public final Integer getFlatRate() {
        return (Integer) getProperty("flatRate");
    }

    /**
     * Amount to be charged for processing the forecast volume of interactions.
     *
     * @param value new property value
     * @see #getFlatRate()
     */
    public final void setFlatRate(final Integer value) {
        setProperty("flatRate", value);
    }
}
