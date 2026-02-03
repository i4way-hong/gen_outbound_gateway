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
 * <a href="CfgAlarmCondition.html">CfgAlarmCondition</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDeltaAlarmCondition extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaAlarmCondition(final IConfService confService) {
        super(confService, "CfgDeltaAlarmCondition");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaAlarmCondition(
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
    public CfgDeltaAlarmCondition(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgAlarmCondition configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgAlarmCondition configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgAlarmCondition retrieveCfgAlarmCondition() throws ConfigException {
        return (CfgAlarmCondition) (super.retrieveObject());
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
     * A pointer to the description
     * of the alarm condition.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
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
     * Retrieves the dbid of the object that is being linked to by the AlarmDetectScript property.
     * Configuration server provides it only if the property value has been changed.
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
    public final Collection<CfgScript> getAddedReactionScripts() {
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
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedReactionScriptDBIDs() {
        return getLinkListCollection("reactionScriptDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedReactionScriptDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getReactionScriptDBIDs() {
        return getLinkListCollection("reactionScriptDBIDs");
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
    public final Collection<CfgScript> getAddedClearanceScripts() {
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
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getAddedClearanceScriptDBIDs() {
        return getLinkListCollection("clearanceScriptDBIDs");
    }

    /**
     * @deprecated
     * @see #getAddedClearanceScriptDBIDs()
     */
    @Deprecated
    public final Collection<Integer> getClearanceScriptDBIDs() {
        return getLinkListCollection("clearanceScriptDBIDs");
    }

    /**
     * A pointer to a list of identifiers of the scripts excluded
     * from reactions to the alarm based on this alarm condition.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgScript> getDeletedReactionScripts() {
        return (Collection<CfgScript>) getProperty("deletedReactionScriptDBIDs");
    }

    /**
     * A pointer to a list of identifiers of the scripts excluded
     * from reactions to the alarm based on this alarm condition.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedReactionScriptDBIDs() {
        return getLinkListCollection("deletedReactionScriptDBIDs");
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

    /**
     * A pointer to a list of identifiers of the scripts excluded
     * from clearance to the alarm based on this alarm condition.
     *
     * @return list of configuration objects or null
     */
    public final Collection<CfgScript> getDeletedClearanceScripts() {
        return (Collection<CfgScript>) getProperty("deletedClearanceScriptDBIDs");
    }

    /**
     * A pointer to a list of identifiers of the scripts excluded
     * from clearance to the alarm based on this alarm condition.
     *
     * @return collection of DBID identifiers of referred objects or null
     */
    public final Collection<Integer> getDeletedClearanceScriptDBIDs() {
        return getLinkListCollection("deletedClearanceScriptDBIDs");
    }
}
