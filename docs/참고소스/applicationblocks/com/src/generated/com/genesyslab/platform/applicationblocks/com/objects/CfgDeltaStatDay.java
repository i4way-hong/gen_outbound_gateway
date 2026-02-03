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
 * <a href="CfgStatDay.html">CfgStatDay</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDeltaStatDay extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaStatDay(final IConfService confService) {
        super(confService, "CfgDeltaStatDay");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaStatDay(
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
    public CfgDeltaStatDay(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgStatDay configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgStatDay configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgStatDay retrieveCfgStatDay() throws ConfigException {
        return (CfgStatDay) (super.retrieveObject());
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
     * Retrieves the dbid of the object that is being linked to by the Tenant property.
     * Configuration server provides it only if the property value has been changed.
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
     * Minimum statistical value for
     * the day. Cannot be negative.
     *
     * @return property value or null
     */
    public final Integer getMinValue() {
        return (Integer) getProperty("minValue");
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
    public final Collection<CfgStatInterval> getAddedStatIntervals() {
        return (Collection<CfgStatInterval>) getProperty("statIntervals");
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
     * Amount to be charged for processing the forecast volume of interactions.
     *
     * @return property value or null
     */
    public final Integer getFlatRate() {
        return (Integer) getProperty("flatRate");
    }

    /**
     * A pointer to the list of numbers of stat intervals that are
     * no longer defined within this stat day (every item of this list
     * has variable type <code>int</code>).
     *
     * @return list of numeric values or null
     */
    public final Collection<CfgStatInterval> getDeletedStatIntervals() {
        return (Collection<CfgStatInterval>) getProperty("deletedStatIntervals");
    }

    /**
     * A pointer to the list of structures of type
     * <code>
     * <a href="CfgStatInterval.html">CfgStatInterval</a>
     * </code>
     * that contain information about stat intervals whose values have
     * been changed.
     *
     * @return list of structures or null
     */
    public final Collection<CfgStatInterval> getChangedStatIntervals() {
        return (Collection<CfgStatInterval>) getProperty("changedStatIntervals");
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
}
