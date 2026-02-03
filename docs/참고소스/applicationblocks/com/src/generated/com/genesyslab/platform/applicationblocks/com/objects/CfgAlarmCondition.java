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
 * <em>Alarm Conditions</em> specify events that you might want to know
 * about and manage as soon as they occur.
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaAlarmCondition.html">
 * <b>CfgDeltaAlarmCondition</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgScript.html">
 * <b>CfgScript</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgApplication.html">
 * <b>CfgApplication</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgAlarmCondition
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGAlarmCondition;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgAlarmCondition(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgAlarmCondition - "
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
    public CfgAlarmCondition(
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
    public CfgAlarmCondition(final IConfService confService) {
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
            if (getMetaData().getAttribute("name") != null) {
                if (getProperty("name") == null) {
                    throw new ConfigException("Mandatory property 'name' not set.");
                }
            }
            if (getMetaData().getAttribute("category") != null) {
                if (getProperty("category") == null) {
                    setProperty("category", 2);
                }
            }
            if (getMetaData().getAttribute("alarmDetectEvent") != null) {
                CfgDetectEvent alarmDetectEvent = (CfgDetectEvent) getProperty("alarmDetectEvent");
                if (alarmDetectEvent != null) {
                    alarmDetectEvent.checkPropertiesForSave();
                }
            }
            if (getMetaData().getAttribute("alarmRemovalEvent") != null) {
                CfgRemovalEvent alarmRemovalEvent = (CfgRemovalEvent) getProperty("alarmRemovalEvent");
                if (alarmRemovalEvent != null) {
                    alarmRemovalEvent.checkPropertiesForSave();
                }
            }
            if (getMetaData().getAttribute("isMasked") != null) {
                if (getProperty("isMasked") == null) {
                    setProperty("isMasked", 1);
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
     * A pointer to the name of the alarm
     * condition. Mandatory. Must be unique within the Configuration Database.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the name of the alarm
     * condition. Mandatory. Must be unique within the Configuration Database.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * A pointer to the description
     * of the alarm condition.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * A pointer to the description
     * of the alarm condition.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * The category of the alarm condition.
     * Mandatory. See type
     * <code>
     * <a href="../Enumerations/CfgAlarmCategory.html">CfgAlarmCategory</a>
     * </code>. Default value is <code>CFGACMajor.</code>
     *
     * @return property value or null
     */
    public final CfgAlarmCategory getCategory() {
        return (CfgAlarmCategory) getProperty(CfgAlarmCategory.class, "category");
    }

    /**
     * The category of the alarm condition.
     * Mandatory. See type
     * <code>
     * <a href="../Enumerations/CfgAlarmCategory.html">CfgAlarmCategory</a>
     * </code>. Default value is <code>CFGACMajor.</code>
     *
     * @param value new property value
     * @see #getCategory()
     */
    public final void setCategory(final CfgAlarmCategory value) {
        setProperty("category", value);
    }

    /**
     * A pointer to the <code>
     * <a href="CfgDetectEvent.html">CfgDetectEvent</a>
     * </code> structure which
     * is used to describe a log event upon which an alarm based on this
     * alarm condition should be detected. Mandatory.
     *
     * @return property value or null
     */
    public final CfgDetectEvent getAlarmDetectEvent() {
        return (CfgDetectEvent) getProperty(CfgDetectEvent.class, "alarmDetectEvent");
    }

    /**
     * A pointer to the <code>
     * <a href="CfgDetectEvent.html">CfgDetectEvent</a>
     * </code> structure which
     * is used to describe a log event upon which an alarm based on this
     * alarm condition should be detected. Mandatory.
     *
     * @param value new property value
     * @see #getAlarmDetectEvent()
     */
    public final void setAlarmDetectEvent(final CfgDetectEvent value) {
        setProperty("alarmDetectEvent", value);
    }

    /**
     * A pointer to the <code>
     * <a href="CfgRemovalEvent.html">CfgRemovalEvent</a>
     * </code> structure which
     * is used to describe a log event upon which an alarm based on this
     * alarm condition should be removed.
     *
     * @return property value or null
     */
    public final CfgRemovalEvent getAlarmRemovalEvent() {
        return (CfgRemovalEvent) getProperty(CfgRemovalEvent.class, "alarmRemovalEvent");
    }

    /**
     * A pointer to the <code>
     * <a href="CfgRemovalEvent.html">CfgRemovalEvent</a>
     * </code> structure which
     * is used to describe a log event upon which an alarm based on this
     * alarm condition should be removed.
     *
     * @param value new property value
     * @see #getAlarmRemovalEvent()
     */
    public final void setAlarmRemovalEvent(final CfgRemovalEvent value) {
        setProperty("alarmRemovalEvent", value);
    }

    /**
     * A unique identifier of a script which describes the logic
     * to be applied to detect an alarm based on this alarm condition.
     * Only a script whose type is
     * <code>CFGAlarmDetection</code> can be specified. Reserved
     * for future use. See <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>.
     *
     * @return instance of referred object or null
     */
    public final CfgScript getAlarmDetectScript() {
        return (CfgScript) getProperty(CfgScript.class, "alarmDetectScriptDBID");
    }

    /**
     * A unique identifier of a script which describes the logic
     * to be applied to detect an alarm based on this alarm condition.
     * Only a script whose type is
     * <code>CFGAlarmDetection</code> can be specified. Reserved
     * for future use. See <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>.
     *
     * @param value new property value
     * @see #getAlarmDetectScript()
     */
    public final void setAlarmDetectScript(final CfgScript value) {
        setProperty("alarmDetectScriptDBID", value);
    }

    /**
     * A unique identifier of a script which describes the logic
     * to be applied to detect an alarm based on this alarm condition.
     * Only a script whose type is
     * <code>CFGAlarmDetection</code> can be specified. Reserved
     * for future use. See <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>.
     *
     * @param dbid DBID identifier of referred object
     * @see #getAlarmDetectScript()
     */
    public final void setAlarmDetectScriptDBID(final int dbid) {
        setProperty("alarmDetectScriptDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the AlarmDetectScript property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getAlarmDetectScriptDBID() {
        return getLinkValue("alarmDetectScriptDBID");
    }

    /**
     * The period of time, in seconds, upon which an alarm based
     * on this alarm condition has to be cleared since the moment it was
     * detected. Default value is 24 hours (24*60*60 = 86400).
     *
     * @return property value or null
     */
    public final Integer getClearanceTimeout() {
        return (Integer) getProperty("clearanceTimeout");
    }

    /**
     * The period of time, in seconds, upon which an alarm based
     * on this alarm condition has to be cleared since the moment it was
     * detected. Default value is 24 hours (24*60*60 = 86400).
     *
     * @param value new property value
     * @see #getClearanceTimeout()
     */
    public final void setClearanceTimeout(final Integer value) {
        setProperty("clearanceTimeout", value);
    }

    /**
     * A pointer to a list of identifiers of the <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code> that describe
     * reactions to an alarm based on this alarm condition. Only scripts
     * whose type is
     * <code>CFGAlarmReaction</code> can be specified. When
     * used as an entry in <code>CfgDeltaAlarmCondition</code> (see below),
     * it is a pointer to a list of identifiers of the scripts added to
     * the existing list. See <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>.
     *
     * @return list of configuration objects or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgScript> getReactionScripts() {
        return (Collection<CfgScript>) getProperty("reactionScriptDBIDs");
    }

    /**
     * A pointer to a list of identifiers of the <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code> that describe
     * reactions to an alarm based on this alarm condition. Only scripts
     * whose type is
     * <code>CFGAlarmReaction</code> can be specified. When
     * used as an entry in <code>CfgDeltaAlarmCondition</code> (see below),
     * it is a pointer to a list of identifiers of the scripts added to
     * the existing list. See <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code>.
     *
     * @param value new property value
     * @see #getReactionScripts()
     */
    public final void setReactionScripts(final Collection<CfgScript> value) {
        setProperty("reactionScriptDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the ReactionScripts property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getReactionScriptDBIDs() {
        return getLinkListCollection("reactionScriptDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the ReactionScripts property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setReactionScriptDBIDs(final Collection<Integer> value) {
        setProperty("reactionScriptDBIDs", value);
    }

    /**
     * Determines whether an alarm which
     * is based on this alarm condition should be communicated to Solution
     * Control Interface and reactions to the alarm should be performed.
     * This corresponds to the default value of <code>CFGFalse</code>.
     * Mandatory. See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgFlag getIsMasked() {
        return (CfgFlag) getProperty(CfgFlag.class, "isMasked");
    }

    /**
     * Determines whether an alarm which
     * is based on this alarm condition should be communicated to Solution
     * Control Interface and reactions to the alarm should be performed.
     * This corresponds to the default value of <code>CFGFalse</code>.
     * Mandatory. See <code>
     * <a href="../Enumerations/CfgFlag.html">CfgFlag</a>
     * </code>
     *
     * @param value new property value
     * @see #getIsMasked()
     */
    public final void setIsMasked(final CfgFlag value) {
        setProperty("isMasked", value);
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
     * A pointer to a list of identifiers of the <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code> that describe
     * clearance to an alarm based on this alarm condition. Only scripts
     * whose type is <code>CFGAlarmReaction</code> can be specified. When
     * used as an entry in <code>CfgDeltaAlarmCondition</code> (see below),
     * it is a pointer to a list of identifiers of the scripts added to
     * the existing list. See <code>
     * <a href="CfgScript.html">CfgScript</a>.</code>.
     *
     * @return list of configuration objects or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgScript> getClearanceScripts() {
        return (Collection<CfgScript>) getProperty("clearanceScriptDBIDs");
    }

    /**
     * A pointer to a list of identifiers of the <code>
     * <a href="CfgScript.html">CfgScript</a>
     * </code> that describe
     * clearance to an alarm based on this alarm condition. Only scripts
     * whose type is <code>CFGAlarmReaction</code> can be specified. When
     * used as an entry in <code>CfgDeltaAlarmCondition</code> (see below),
     * it is a pointer to a list of identifiers of the scripts added to
     * the existing list. See <code>
     * <a href="CfgScript.html">CfgScript</a>.</code>.
     *
     * @param value new property value
     * @see #getClearanceScripts()
     */
    public final void setClearanceScripts(final Collection<CfgScript> value) {
        setProperty("clearanceScriptDBIDs", value);
    }

    /**
     * Retrieves dbids of objects that are being linked to by the ClearanceScripts property.
     * It's a snapshot collection containing original values.
     * Modification of this collection instance will not affect actual value of the objects' property.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getClearanceScriptDBIDs() {
        return getLinkListCollection("clearanceScriptDBIDs");
    }

    /**
     * Sets dbids collection of objects that are being linked to by the ClearanceScripts property.
     *
     * @param value collection of DBID identifiers of referred objects
     */
    public final void setClearanceScriptDBIDs(final Collection<Integer> value) {
        setProperty("clearanceScriptDBIDs", value);
    }
}
