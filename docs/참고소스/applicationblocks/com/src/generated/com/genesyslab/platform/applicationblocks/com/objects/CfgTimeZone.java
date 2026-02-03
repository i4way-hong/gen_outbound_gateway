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
 * <em>Time Zones</em> are predefined objects that provide CTI applications with
 * information about world time zones. Each object describes one time zone.
 *
 * <p/>
 * <code>time_t = int</code> or <code>long</code> i.e.
 * 32 bit value at least
 * <p/>
 * The <code>time_t</code> type logically divided into several
 * sections, following bit-mask should be used to get an info:
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <caption><font size="+1"><strong>Time_t Bit Mask Description</strong></font></caption>
 * <thead>
 * <tr bgcolor="#ccccff" class="tableheadingcolor">
 * <th align="left">Bits</th>
 * <th align="left">Description</th>
 * <th align="left">Range</th>
 * <th align="left">Comments</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>0-3</td>
 * <td>Month,</td>
 * <td>0-12;</td>
 * <td>DST is not Observed=0</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>Jan=1</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>Dec=12</td>
 * </tr>
 * <tr>
 * <td>#######</td>
 * <td>Week</td>
 * <td>0-5, 7</td>
 * <td>DST is not observed or week is not specified= 0</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>Last week of month = 7</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>Note:</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>The day of last week of month=week will be set to
 * 7, if the day of week does not occur on last week</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>The day of last week of month=week will be set to
 * 1, if the day of week does not occur on first week</td>
 * </tr>
 * <tr>
 * <td>#######</td>
 * <td>Day</td>
 * <td>0-31, 63</td>
 * <td>DST is not observed = 0</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>Last day of month = 63</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>If week is specified (week!=0) the range should be
 * 1-7</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>Sun=1</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>Sat=7</td>
 * </tr>
 * <tr>
 * <td>13-18</td>
 * <td>Start_time, Stop_Time</td>
 * <td>0-47 in 30 minute units</td>
 * <td>1:00 am = 2</td>
 * </tr>
 * <tr>
 * <td>19-24</td>
 * <td>Year (shift from 2000)</td>
 * <td>0, 1-38, 39-63</td>
 * <td>Only if Time Zone is defined for specific Year. 2001
 * = 1</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>DST is not observed or year is not specified=0</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>Values within range 39-63 are not valid</td>
 * </tr>
 * <tr>
 * <td>25-30</td>
 * <td>reserved</td>
 * <td>reserved</td>
 * <td>reserved</td>
 * </tr>
 * <tr>
 * <td>#######</td>
 * <td>A flag to recognize custom/6.0 time zone</td>
 * <td>reserved</td>
 * <td>Has to be used to distinguish custom time zones and
 * time zones created before release 6.1:</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>New Style = 1</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>Old Style or custom time zone = 0</td>
 * </tr>
 * </tbody>
 * </table><br/>
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <caption><font size="+1"><strong>Definition of Time Zones for Calculation
 * Schema</strong></font></caption>
 * <thead>
 * <tr bgcolor="#ccccff" class="tableheadingcolor">
 * <th align="left">name</th>
 * <th align="left">Description</th>
 * <th align="left">offset</th>
 * <th align="left">IsDST</th>
 * <th align="left">Month</th>
 * <th align="left">Week</th>
 * <th align="left">Date</th>
 * <th align="left">Time_start</th>
 * <th align="left">Month</th>
 * <th align="left">Week</th>
 * <th align="left">Date</th>
 * <th align="left">Time_stop</th>
 * <th align="left">Year</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>GMT</td>
 * <td>Greenwich Mean Time</td>
 * <td>0</td>
 * <td>TRUE</td>
 * <td>3</td>
 * <td>7</td>
 * <td>1</td>
 * <td>4</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>6</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>ECT</td>
 * <td>European Central Time</td>
 * <td>2</td>
 * <td>TRUE</td>
 * <td>3</td>
 * <td>7</td>
 * <td>1</td>
 * <td>4</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>6</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>EET</td>
 * <td>Eastern European Time</td>
 * <td>4</td>
 * <td>TRUE</td>
 * <td>3</td>
 * <td>7</td>
 * <td>1</td>
 * <td>6</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>8</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>ART</td>
 * <td>(Arabic) Egypt Standard Time</td>
 * <td>4</td>
 * <td>TRUE</td>
 * <td>4</td>
 * <td>7</td>
 * <td>6</td>
 * <td>0</td>
 * <td>9</td>
 * <td>7</td>
 * <td>6</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>EAT</td>
 * <td>Eastern African Time</td>
 * <td>6</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>MET</td>
 * <td>Middle East Time</td>
 * <td>7</td>
 * <td>TRUE</td>
 * <td>3</td>
 * <td>0</td>
 * <td>20</td>
 * <td>0</td>
 * <td>9</td>
 * <td>0</td>
 * <td>22</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>NET</td>
 * <td>Near East Time</td>
 * <td>8</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>PLT</td>
 * <td>Pakistan Lahore Time</td>
 * <td>10</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>IST</td>
 * <td>India Standard Time</td>
 * <td>11</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>BST</td>
 * <td>Bangladesh Standard Time</td>
 * <td>12</td>
 * <td>TRUE</td>
 * <td>3</td>
 * <td>7</td>
 * <td>1</td>
 * <td>0</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>VST</td>
 * <td>Vietnam Standard Time</td>
 * <td>14</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>CTT</td>
 * <td>China Taiwan Time</td>
 * <td>16</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>JST</td>
 * <td>Japan Standard Time</td>
 * <td>18</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>KST</td>
 * <td>Korea Standard Time</td>
 * <td>18</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>ACT</td>
 * <td>Australia Central Time</td>
 * <td>19</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>AET</td>
 * <td>Australia Eastern Time</td>
 * <td>20</td>
 * <td>TRUE</td>
 * <td>8</td>
 * <td>7</td>
 * <td>7</td>
 * <td>4</td>
 * <td>3</td>
 * <td>7</td>
 * <td>1</td>
 * <td>4</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>SST</td>
 * <td>Solomon Standard Time</td>
 * <td>22</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>NST</td>
 * <td>New Zealand Standard Time</td>
 * <td>24</td>
 * <td>TRUE</td>
 * <td>10</td>
 * <td>1</td>
 * <td>1</td>
 * <td>4</td>
 * <td>3</td>
 * <td>3</td>
 * <td>1</td>
 * <td>6</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>MIT</td>
 * <td>Midway Islands Time</td>
 * <td>-22</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>HST</td>
 * <td>Hawaii Standard Time</td>
 * <td>-20</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>AST</td>
 * <td>Alaska Standard Time</td>
 * <td>-18</td>
 * <td>TRUE</td>
 * <td>4</td>
 * <td>1</td>
 * <td>1</td>
 * <td>4</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>4</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>PST</td>
 * <td>Pacific Standard Time</td>
 * <td>-16</td>
 * <td>TRUE</td>
 * <td>4</td>
 * <td>1</td>
 * <td>1</td>
 * <td>4</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>4</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>PNT</td>
 * <td>Phoenix Standard Time</td>
 * <td>-14</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>MST</td>
 * <td>Mountain Standard Time</td>
 * <td>-14</td>
 * <td>TRUE</td>
 * <td>4</td>
 * <td>1</td>
 * <td>1</td>
 * <td>4</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>4</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>CST</td>
 * <td>Central Standard Time</td>
 * <td>-12</td>
 * <td>TRUE</td>
 * <td>4</td>
 * <td>1</td>
 * <td>1</td>
 * <td>4</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>4</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>EST</td>
 * <td>Eastern Standard Time</td>
 * <td>-10</td>
 * <td>TRUE</td>
 * <td>4</td>
 * <td>1</td>
 * <td>1</td>
 * <td>4</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>4</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>IET</td>
 * <td>Indiana Eastern Standard</td>
 * <td>-10</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>PRT</td>
 * <td>Puerto Rico and US Virgin Islands Time</td>
 * <td>-8</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>CNT</td>
 * <td>Canada Newfoundland Time</td>
 * <td>-7</td>
 * <td>TRUE</td>
 * <td>4</td>
 * <td>1</td>
 * <td>1</td>
 * <td>4</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>4</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>AGT</td>
 * <td>Argentina Standard Time</td>
 * <td>-6</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>BET</td>
 * <td>Brazil Eastern Time</td>
 * <td>-6</td>
 * <td>TRUE</td>
 * <td>10</td>
 * <td>2</td>
 * <td>1</td>
 * <td>0</td>
 * <td>2</td>
 * <td>7</td>
 * <td>1</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>CAT</td>
 * <td>Central African Time</td>
 * <td>-2</td>
 * <td>FALSE</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>AtlST</td>
 * <td>Atlantic Standard Time</td>
 * <td>-8</td>
 * <td>TRUE</td>
 * <td>4</td>
 * <td>1</td>
 * <td>1</td>
 * <td>4</td>
 * <td>10</td>
 * <td>7</td>
 * <td>1</td>
 * <td>4</td>
 * <td>0</td>
 * </tr>
 * </tbody>
 * </table><br/>
 * <p/>
 * The <code>DSTStartTime / DSTStopTime</code> can be
 * calculated using function <code>CfgUtilities.ConfCalculateTimeZone()</code>.
 * <br/><b>Note:</b><br/>
 * All calculated values of <code>DSTStartTime / DSTStopTime</code> represent a calendar value, not a local time at any particular timezone. In order to use this calculated value one can call <code>gmtime()</code> function in C/C++ runtime library to get the value parsed into <code>tm</code> structure. Note, however, that resulted value is not a GMT time of transition, but again a calendar value, applicable for the configured timezone. In this case <code>tm</code> structure and <code>gmtime()</code> function have being reused only to parse configuration, without inheriting their meaning from runtime library. You should be careful when applying any translations, because functions of runtime library that accept a <code>time_t</code> value usually perform additional interpretation of that value as either local or universal time (while actual value in this case is, in fact, just a calendar item, not a time). For instance, you cannot use <code>mktime()</code> or even <code>_mkgmtime() - windows</code> to convert this value back into <code>time_t</code>, because first assume you have a local time that need to be recalculated to GMT by changing timezone, while second give you much closer result (because it will not count for timezone) but might add/subtract one hour from result, due to its DST-awareness. You should not attempt to convert it back and you should use content of <code>tm</code> structure as a configuration information (including day, month, year fields) and treat this information as applicable for a timezone for that this information is supplied.
 * <br/>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaTimeZone.html">
 * <b>CfgDeltaTimeZone</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgTimeZone
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGTimeZone;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgTimeZone(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgTimeZone - "
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
    public CfgTimeZone(
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
    public CfgTimeZone(final IConfService confService) {
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
            if (getMetaData().getAttribute("isDSTObserved") != null) {
                if (getProperty("isDSTObserved") == null) {
                    throw new ConfigException("Mandatory property 'isDSTObserved' not set.");
                }
            }
            if (getMetaData().getAttribute("nameNetscape") != null) {
                if (getProperty("nameNetscape") == null) {
                    throw new ConfigException("Mandatory property 'nameNetscape' not set.");
                }
            }
            if (getMetaData().getAttribute("nameMSExplorer") != null) {
                if (getProperty("nameMSExplorer") == null) {
                    throw new ConfigException("Mandatory property 'nameMSExplorer' not set.");
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
     * to which this time zone is allocated. Mandatory. Once specified,
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
     * to which this time zone is allocated. Mandatory. Once specified,
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
     * to which this time zone is allocated. Mandatory. Once specified,
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
     * A pointer to time zone name. Mandatory.
     * Must be unique within tenant environment.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to time zone name. Mandatory.
     * Must be unique within tenant environment.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * A pointer to time zone description.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A pointer to time zone description.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * A time zone offset. Any integer
     * value from -24 to 24. Must be considered as value -12 to 12 with
     * 0.5 hour step.
     *
     * @return property value or null
     */
    public final Integer getOffset() {
        return (Integer) getProperty("offset");
    }

    /**
     * A time zone offset. Any integer
     * value from -24 to 24. Must be considered as value -12 to 12 with
     * 0.5 hour step.
     *
     * @param value new property value
     * @see #getOffset()
     */
    public final void setOffset(final Integer value) {
        setProperty("offset", value);
    }

    public final CfgFlag getIsDSTObserved() {
        return (CfgFlag) getProperty(CfgFlag.class, "isDSTObserved");
    }

    public final void setIsDSTObserved(final CfgFlag value) {
        setProperty("isDSTObserved", value);
    }

    /**
     * DST start date. The value
     * is: measured in seconds if 6.0 definition schema is uses. Refer
     * to time_t from time.h of ANSI C library. Year value range 0-2038;
     * performed based on calculation schema. (See comments.)
     *
     * @return property value or null
     */
    public final Calendar getDSTStartDate() {
        return (Calendar) getProperty("DSTStartDate");
    }

    /**
     * DST start date. The value
     * is: measured in seconds if 6.0 definition schema is uses. Refer
     * to time_t from time.h of ANSI C library. Year value range 0-2038;
     * performed based on calculation schema. (See comments.)
     *
     * @param value new property value
     * @see #getDSTStartDate()
     */
    public final void setDSTStartDate(final Calendar value) {
        setProperty("DSTStartDate", value);
    }

    /**
     * DST stop date. The value is:
     * measured in seconds if 6.0 definition schema is uses. Refer to time_t
     * from time.h of ANSI C library. Year value range 0-2038; performed
     * based on calculation schema. (See comments.)
     *
     * @return property value or null
     */
    public final Calendar getDSTStopDate() {
        return (Calendar) getProperty("DSTStopDate");
    }

    /**
     * DST stop date. The value is:
     * measured in seconds if 6.0 definition schema is uses. Refer to time_t
     * from time.h of ANSI C library. Year value range 0-2038; performed
     * based on calculation schema. (See comments.)
     *
     * @param value new property value
     * @see #getDSTStopDate()
     */
    public final void setDSTStopDate(final Calendar value) {
        setProperty("DSTStopDate", value);
    }

    /**
     * A pointer to the time zone
     * name used by Netscape Navigator browser. Mandatory.
     *
     * @return property value or null
     */
    public final String getNameNetscape() {
        return (String) getProperty("nameNetscape");
    }

    /**
     * A pointer to the time zone
     * name used by Netscape Navigator browser. Mandatory.
     *
     * @param value new property value
     * @see #getNameNetscape()
     */
    public final void setNameNetscape(final String value) {
        setProperty("nameNetscape", value);
    }

    /**
     * A pointer to the time zone name used by Microsoft browser.
     * Mandatory
     *
     * @return property value or null
     */
    public final String getNameMSExplorer() {
        return (String) getProperty("nameMSExplorer");
    }

    /**
     * A pointer to the time zone name used by Microsoft browser.
     * Mandatory
     *
     * @param value new property value
     * @see #getNameMSExplorer()
     */
    public final void setNameMSExplorer(final String value) {
        setProperty("nameMSExplorer", value);
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
     * The value of DST offset. Default is 60 (minutes).
     *
     * @return property value or null
     */
    public final Integer getDSTOffset() {
        return (Integer) getProperty("DSTOffset");
    }

    /**
     * The value of DST offset. Default is 60 (minutes).
     *
     * @param value new property value
     * @see #getDSTOffset()
     */
    public final void setDSTOffset(final Integer value) {
        setProperty("DSTOffset", value);
    }
}
