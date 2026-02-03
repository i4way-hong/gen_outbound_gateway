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
 * A <em>Switch</em> is an aggregate of telephony resources within a Switching
 * Office.
 * <p/>
 * Most enterprise-level configurations have a one-to-one match between switches
 * and switching offices. However, there may be instances when it is desirable to
 * partition an office into more than one switch, perhaps due to CTI_link capacity
 * limitations, or to create a more efficient and secure numbering plan. In that
 * case, you must define these switches within a switching office.
 *
 * <p/>
 * The current version of Configuration Server does not
 * verify correspondence between the switch numbering plan defined
 * by <code>DNRange</code> and the actual DN numbers defined within
 * this switch by <code>number</code> in <code>
 * <a href="CfgDN.html">CfgDN</a>
 * </code>. Such
 * verification may be implemented in one of the
 * next versions. In versions 5.1.1XX and earlier, it is users' responsibility
 * to make sure the range covers all actual DNs of the switch in question.
 * <p/>
 * Deletion of Switch X will cause the following events set out
 * in the order of arrival:
 * <ul type="bullet">
 * <li>
 * Modification of <code>switchAccessCodes</code> of all switches
 * that were interfaced with S<code>witch X</code>
 * </li>
 * <li>
 * Modification of voice prompt objects which had <code>switchDBID</code> field
 * set to Switch X
 * </li>
 * <li>
 * Modification of <code>accessNumbers</code> of all DNs which
 * were connected with Switch X
 * </li>
 * <li>
 * Modifications of <code>flexibleProperties</code> of all T-Servers
 * which were connected with Switch X
 * </li>
 * <li>
 * Deletion of all DNs of Switch X (see comments to <code>
 * <a href="CfgDN.html">CfgDN</a>
 * </code> for
 * details)
 * </li>
 * <li>
 * Deletion of all agent logins of Switch X (see comments to <code>
 * <a href="CfgAgentLogin.html">CfgAgentLogin</a>
 * </code> for details)
 * </li>
 * <li>
 * Deletion of all folders that had Switch X defined as the parent
 * object
 * </li>
 * <li>
 * Deletion of Switch X
 * </li>
 * </ul>
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaSwitch.html">
 * <b>CfgDeltaSwitch</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgDN.html">
 * <b>CfgDN</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgAgentLogin.html">
 * <b>CfgAgentLogin</b>
 * </a>
 * </p>
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgSwitch
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGSwitch;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgSwitch(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgSwitch - "
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
    public CfgSwitch(
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
    public CfgSwitch(final IConfService confService) {
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
            if (getMetaData().getAttribute("physSwitchDBID") != null) {
                if (getLinkValue("physSwitchDBID") == null) {
                    throw new ConfigException("Mandatory property 'physSwitchDBID' not set.");
                }
            }
            if (getMetaData().getAttribute("name") != null) {
                if (getProperty("name") == null) {
                    throw new ConfigException("Mandatory property 'name' not set.");
                }
            }
            if (getMetaData().getAttribute("switchAccessCodes") != null) {
                Collection<CfgSwitchAccessCode> switchAccessCodes = (Collection<CfgSwitchAccessCode>) getProperty("switchAccessCodes");
                if (switchAccessCodes != null) {
                    for (CfgSwitchAccessCode item : switchAccessCodes) {
                        item.checkPropertiesForSave();
                    }
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
     * A unique identifier of the
     * <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code> to which this switch is allocated. Mandatory. Once specified,
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
     * </code> to which this switch is allocated. Mandatory. Once specified,
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
     * </code> to which this switch is allocated. Mandatory. Once specified,
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
     * A unique identifier of the <code>
     * <a href="CfgPhysicalSwitch.html">Physical Switch</a>
     * </code> within which this
     * switch is defined. Mandatory. Once specified, cannot be changed.
     *
     * @return instance of referred object or null
     */
    public final CfgPhysicalSwitch getPhysSwitch() {
        return (CfgPhysicalSwitch) getProperty(CfgPhysicalSwitch.class, "physSwitchDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgPhysicalSwitch.html">Physical Switch</a>
     * </code> within which this
     * switch is defined. Mandatory. Once specified, cannot be changed.
     *
     * @param value new property value
     * @see #getPhysSwitch()
     */
    public final void setPhysSwitch(final CfgPhysicalSwitch value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("physSwitchDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgPhysicalSwitch.html">Physical Switch</a>
     * </code> within which this
     * switch is defined. Mandatory. Once specified, cannot be changed.
     *
     * @param dbid DBID identifier of referred object
     * @see #getPhysSwitch()
     */
    public final void setPhysSwitchDBID(final int dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("physSwitchDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the PhysSwitch property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getPhysSwitchDBID() {
        return getLinkValue("physSwitchDBID");
    }

    /**
     * Type of the physical switch to which
     * this switch belongs. Read-only (set automatically according to the
     * current value of <code>type</code> of the physical switch specified in <code>physSwitchDBID</code>).
     * See <code>
     * <a href="../Enumerations/CfgSwitchType.html">CfgSwitchType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgSwitchType getType() {
        return (CfgSwitchType) getProperty(CfgSwitchType.class, "type");
    }

    /**
     * A pointer to the name of the switch.
     * Mandatory. Must be unique within the tenant and the physical switch.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the name of the switch.
     * Mandatory. Must be unique within the tenant and the physical switch.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * A unique identifier of the
     * T-Server <code>
     * <a href="CfgApplication.html">Application</a>
     * </code> through which the telephony objects
     * of this switch are controlled. Parameter
     * <code>tenantDBIDs</code> of the T-Server must be specified
     * and match the setting of <code>tenantDBID</code> of this switch.
     * One T-Server cannot be associated with more than one switch unless
     * the switch is of type <code>CFGMultimediaSwitch</code>. The property
     * is applicable for 5.1 applications only, for compatibility. Starting
     * from release 6.0 the association between T-Server and switch have
     * to be configured using CfgApplication (T-Server) object. See <code>flexibleProperties</code>
     * in CfgApplication.
     *
     * @return instance of referred object or null
     */
    public final CfgApplication getTServer() {
        return (CfgApplication) getProperty(CfgApplication.class, "TServerDBID");
    }

    /**
     * A unique identifier of the
     * T-Server <code>
     * <a href="CfgApplication.html">Application</a>
     * </code> through which the telephony objects
     * of this switch are controlled. Parameter
     * <code>tenantDBIDs</code> of the T-Server must be specified
     * and match the setting of <code>tenantDBID</code> of this switch.
     * One T-Server cannot be associated with more than one switch unless
     * the switch is of type <code>CFGMultimediaSwitch</code>. The property
     * is applicable for 5.1 applications only, for compatibility. Starting
     * from release 6.0 the association between T-Server and switch have
     * to be configured using CfgApplication (T-Server) object. See <code>flexibleProperties</code>
     * in CfgApplication.
     *
     * @param value new property value
     * @see #getTServer()
     */
    public final void setTServer(final CfgApplication value) {
        setProperty("TServerDBID", value);
    }

    /**
     * A unique identifier of the
     * T-Server <code>
     * <a href="CfgApplication.html">Application</a>
     * </code> through which the telephony objects
     * of this switch are controlled. Parameter
     * <code>tenantDBIDs</code> of the T-Server must be specified
     * and match the setting of <code>tenantDBID</code> of this switch.
     * One T-Server cannot be associated with more than one switch unless
     * the switch is of type <code>CFGMultimediaSwitch</code>. The property
     * is applicable for 5.1 applications only, for compatibility. Starting
     * from release 6.0 the association between T-Server and switch have
     * to be configured using CfgApplication (T-Server) object. See <code>flexibleProperties</code>
     * in CfgApplication.
     *
     * @param dbid DBID identifier of referred object
     * @see #getTServer()
     */
    public final void setTServerDBID(final int dbid) {
        setProperty("TServerDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the TServer property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getTServerDBID() {
        return getLinkValue("TServerDBID");
    }

    /**
     * Type of the CTI link of this switch.
     * Optional. See <code>
     * <a href="../Enumerations/CfgLinkType.html">CfgLinkType</a>
     * </code>
     *
     * @return property value or null
     */
    public final CfgLinkType getLinkType() {
        return (CfgLinkType) getProperty(CfgLinkType.class, "linkType");
    }

    /**
     * Type of the CTI link of this switch.
     * Optional. See <code>
     * <a href="../Enumerations/CfgLinkType.html">CfgLinkType</a>
     * </code>
     *
     * @param value new property value
     * @see #getLinkType()
     */
    public final void setLinkType(final CfgLinkType value) {
        setProperty("linkType", value);
    }

    /**
     * A pointer to the list of access codes of the switches that
     * this switch can access (every item of this list is structured as
     * <code>
     * <a href="CfgSwitchAccessCode.html">CfgSwitchAccessCode</a>
     * </code>). When used as an entry
     * in <code>
     * <a href="CfgDeltaSwitch.html">CfgDeltaSwitch</a>
     * </code>, it is a pointer to a list of switch access
     * codes added to the existing list.
     *
     * @return list of structures or null
     */
    @SuppressWarnings("unchecked")
    public final Collection<CfgSwitchAccessCode> getSwitchAccessCodes() {
        return (Collection<CfgSwitchAccessCode>) getProperty("switchAccessCodes");
    }

    /**
     * A pointer to the list of access codes of the switches that
     * this switch can access (every item of this list is structured as
     * <code>
     * <a href="CfgSwitchAccessCode.html">CfgSwitchAccessCode</a>
     * </code>). When used as an entry
     * in <code>
     * <a href="CfgDeltaSwitch.html">CfgDeltaSwitch</a>
     * </code>, it is a pointer to a list of switch access
     * codes added to the existing list.
     *
     * @param value new property value
     * @see #getSwitchAccessCodes()
     */
    public final void setSwitchAccessCodes(final Collection<CfgSwitchAccessCode> value) {
        setProperty("switchAccessCodes", value);
    }

    /**
     * A pointer to a string that describes
     * the numbering plan of the switch. Use a hyphen to specify a range
     * of numbers; use commas to specify a series of stand-alone numbers
     * or ranges (e.g., <code>1100-1179, 1190-1195, 1199</code>).
     *
     * @return property value or null
     */
    public final String getDNRange() {
        return (String) getProperty("DNRange");
    }

    /**
     * A pointer to a string that describes
     * the numbering plan of the switch. Use a hyphen to specify a range
     * of numbers; use commas to specify a series of stand-alone numbers
     * or ranges (e.g., <code>1100-1179, 1190-1195, 1199</code>).
     *
     * @param value new property value
     * @see #getDNRange()
     */
    public final void setDNRange(final String value) {
        setProperty("DNRange", value);
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
}
