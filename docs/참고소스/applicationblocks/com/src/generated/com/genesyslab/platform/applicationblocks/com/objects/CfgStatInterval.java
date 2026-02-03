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
 * Use a <em>Statistical Interval</em> to associate each Statistical Interval with
 * certain Statistical Values.
 *
 * <p/>
 * For the missing intervals, the stat values shall be assumed to be zero.
 * <p/>
 * The current version of Configuration Server does not verify
 * correspondence between the values of <code>intervalCount</code> and
 * the current settings of <code>intervalLength</code>, <code>startTime</code>
 * and <code>endTime</code> in <code>
 * <a href="CfgStatDay.html">CfgStatDay</a>
 * </code>. Such verification may be
 * added in one of the next releases. At the moment, it is users' responsibility
 * to make sure the values of <code>intervalCount</code> make sense.
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgStatInterval extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgStatInterval(
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
    public CfgStatInterval(
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
    public CfgStatInterval(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgStatInterval")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
            if (getMetaData().getAttribute("intervalCount") != null) {
                if (getProperty("intervalCount") == null) {
                    throw new ConfigException("Mandatory property 'intervalCount' not set.");
                }
            }
    }

    /**
     * Interval's number. Once specified,
     * cannot be changed. The first interval has number 1. First interval
     * is always counted from the start of business time set by <code>startTime</code>
     * in <code>
     * <a href="CfgStatDay.html">CfgStatDay</a>
     * </code>.
     *
     * @return property value or null
     */
    public final Integer getIntervalCount() {
        return (Integer) getProperty("intervalCount");
    }

    /**
     * Interval's number. Once specified,
     * cannot be changed. The first interval has number 1. First interval
     * is always counted from the start of business time set by <code>startTime</code>
     * in <code>
     * <a href="CfgStatDay.html">CfgStatDay</a>
     * </code>.
     *
     * @param value new property value
     * @see #getIntervalCount()
     */
    public final void setIntervalCount(final Integer value) {
        setProperty("intervalCount", value);
    }

    public final Integer getStatValue1() {
        return (Integer) getProperty("statValue1");
    }

    public final void setStatValue1(final Integer value) {
        setProperty("statValue1", value);
    }

    public final Integer getStatValue2() {
        return (Integer) getProperty("statValue2");
    }

    public final void setStatValue2(final Integer value) {
        setProperty("statValue2", value);
    }

    public final Integer getStatValue3() {
        return (Integer) getProperty("statValue3");
    }

    public final void setStatValue3(final Integer value) {
        setProperty("statValue3", value);
    }

    public final Integer getStatValue4() {
        return (Integer) getProperty("statValue4");
    }

    public final void setStatValue4(final Integer value) {
        setProperty("statValue4", value);
    }
}
