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
 * <a href="CfgService.html">CfgService</a>
 * </code> object.
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgDeltaService extends CfgDelta {

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     */
    public CfgDeltaService(final IConfService confService) {
        super(confService, "CfgDeltaService");
    }

    /**
     * This constructor is intended for creation of delta objects from configuration protocol messages.
     * It is internally used by COM AB for delta objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object delta data
     */
    public CfgDeltaService(
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
    public CfgDeltaService(
            final IConfService confService,
            final Node         xmlData) {
        super(confService, xmlData, null);
    }

    /**
     * The DBID of the target CfgService configuration object.
     *
     * @return target object DBID
     */
    public final Integer getDBID() {
        return (Integer) getProperty("DBID");
    }

    /**
     * Read base CfgService configuration object caused this event.
     *
     * @return configuration object read
     * @throws ConfigException in case of problem while object reading
     */
    public final CfgService retrieveCfgService() throws ConfigException {
        return (CfgService) (super.retrieveObject());
    }

    /**
     * A pointer to the name of the service/solution. Mandatory.
     * Must be unique within the Configuration Database.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * Not in use.
     *
     * @return property value or null
     */
    public final String getAbbr() {
        return (String) getProperty("abbr");
    }

    /**
     * Not in use.
     *
     * @return property value or null
     */
    public final CfgChargeType getType() {
        return (CfgChargeType) getProperty(CfgChargeType.class, "type");
    }

    /**
     * Not in use.
     *
     * @return list of structures or null
     */
    public final Collection<CfgAppServicePermission> getAddedAppServicePermissions() {
        return (Collection<CfgAppServicePermission>) getProperty("appServicePermissions");
    }

    /**
     * <p/>
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
     * <p/>
     * The type of the solution.
     * Mandatory. Once specified cannot be changed. See <code>
     * <a href="../Enumerations/CfgSolutionType.html">CfgSolutionType</a>
     * </code>.
     *
     * @return property value or null
     */
    public final CfgSolutionType getSolutionType() {
        return (CfgSolutionType) getProperty(CfgSolutionType.class, "solutionType");
    }

    /**
     * A pointer to a list of solution
     * components defined for this solution (every item of this list is
     * structured as <code>
     * <a href="CfgSolutionComponent.html">CfgSolutionComponent</a>
     * </code>).
     * When used as an entry in <code>CfgDeltaService</code>, it is a pointer to a list of solution
     * components added to the existing list.
     *
     * @return list of structures or null
     */
    public final Collection<CfgSolutionComponent> getAddedComponents() {
        return (Collection<CfgSolutionComponent>) getProperty("components");
    }

    /**
     * <p/>
     * A unique identifier of an <code>
     * <a href="CfgApplication.html">Application</a>
     * </code>
     * of <code>CFGSCS</code>type which is supposed to control
     * the solution. See comment number 4. See also <code>
     * <a href="CfgSolutionComponent.html">CfgSolutionComponent</a>
     * </code>.
     *
     * @return instance of referred object or null
     */
    public final CfgApplication getSCS() {
        return (CfgApplication) getProperty(CfgApplication.class, "SCSDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the SCS property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getSCSDBID() {
        return getLinkValue("SCSDBID");
    }

    /**
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code>
     * the solution is assigned to. Optional. If <code>assignedTenantDBID is managed </code> (added/modified/removed)
     * by configuration wizard , the same action (add/modify/remove) has
     * to be initiated by wizard for property <code>tenantDBIDs</code> of CfgApplication
     * if an application belongs to this solution.
     *
     * @return instance of referred object or null
     */
    public final CfgTenant getAssignedTenant() {
        return (CfgTenant) getProperty(CfgTenant.class, "assignedTenantDBID");
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the AssignedTenant property.
     * Configuration server provides it only if the property value has been changed.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getAssignedTenantDBID() {
        return getLinkValue("assignedTenantDBID");
    }

    /**
     * A pointer to the version of the
     * solution. Mandatory.
     *
     * @return property value or null
     */
    public final String getVersion() {
        return (String) getProperty("version");
    }

    /**
     * <p/>
     * A pointer to a list of predefined solution components for
     * this solution. (Every item of this list is structured as a
     * <code>
     * <a href="CfgSolutionComponentDefinition.html">CfgSolutionComponentDefinition</a>
     * </code>.)
     *
     * @return list of structures or null
     */
    public final Collection<CfgSolutionComponentDefinition> getAddedComponentDefinitions() {
        return (Collection<CfgSolutionComponentDefinition>) getProperty("componentDefinitions");
    }

    /**
     * <p/>
     * A type of solution/service
     * startup. Specifies whether this solution/service have to be started
     * by Management Layer. See <code>
     * <a href="../Enumerations/CfgStartupType.html">CfgStartupType</a>.</code>
     * Read-only. Specified during application prototype definition. The value is
     * associated with solution type <code>
     * <a href="../Enumerations/CfgSolutionType.html">CfgSolutionType</a>.</code>
     * The value for the solutions of
     * <code>CFGSTDefaultSolutionType</code> and <code>CFGSTFramework</code> type
     * is set to <code>CFGSUTManual,</code> and for other applications
     * is set to<code> CFGSUTAutomatic.</code>
     *
     * @return property value or null
     */
    public final CfgStartupType getStartupType() {
        return (CfgStartupType) getProperty(CfgStartupType.class, "startupType");
    }

    public final Collection<CfgObjectResource> getAddedResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
    }

    /**
     * Not in use.
     *
     * @return list of numeric values or null
     */
    public final Collection<CfgAppServicePermission> getDeletedAppServicePermissions() {
        return (Collection<CfgAppServicePermission>) getProperty("deletedAppServicePermissions");
    }

    /**
     * Not in use.
     *
     * @return list of structures or null
     */
    public final Collection<CfgAppServicePermission> getChangedAppServicePermissions() {
        return (Collection<CfgAppServicePermission>) getProperty("changedAppServicePermissions");
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
     * <p/>
     * A pointer to a list of structures of type <code>
     * <a href="CfgSolutionComponent.html">CfgSolutionComponent</a>
     * </code>
     * that contain information about solution components that are no longer
     * defined within this solution.
     *
     * @return list of structures or null
     */
    public final Collection<CfgSolutionComponent> getDeletedComponents() {
        return (Collection<CfgSolutionComponent>) getProperty("deletedComponents");
    }

    /**
     * <p/>
     * A pointer to a list of structures of type <code>
     * <a href="CfgSolutionComponent.html">CfgSolutionComponent</a>
     * </code>
     * that contain information about solution components within this solution
     * whose parameters have been changed.
     *
     * @return list of structures or null
     */
    public final Collection<CfgSolutionComponent> getChangedComponents() {
        return (Collection<CfgSolutionComponent>) getProperty("changedComponents");
    }

    /**
     * <p/>
     * A pointer to a list of structures of type
     * <code>
     * <a href="CfgSolutionComponentDefinition.html">CfgSolutionComponentDefinition</a>
     * </code>
     * that contain information about solution component definition that
     * are no longer defined within this solution.
     *
     * @return list of structures or null
     */
    public final Collection<CfgSolutionComponentDefinition> getDeletedComponentDefinitions() {
        return (Collection<CfgSolutionComponentDefinition>) getProperty("deletedComponentDefinitions");
    }

    /**
     * <p/>
     * A pointer to a list of structures of type
     * <code>
     * <a href="CfgSolutionComponentDefinition.html">CfgSolutionComponentDefinition</a>
     * </code>
     * that contain information about solution component definition within
     * this solution whose parameters have been changed.
     *
     * @return list of structures or null
     */
    public final Collection<CfgSolutionComponentDefinition> getChangedComponentDefinitions() {
        return (Collection<CfgSolutionComponentDefinition>) getProperty("changedComponentDefinitions");
    }

    public final Collection<CfgResourceID> getDeletedResources() {
        return (Collection<CfgResourceID>) getProperty("deletedResources");
    }

    public final Collection<CfgObjectResource> getChangedResources() {
        return (Collection<CfgObjectResource>) getProperty("changedResources");
    }
}
