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
 * Solutions are sets of functions that applications provide. Solutions accomplish particular business tasks in contact centers.
 *
 * <p/>
 * The <code>components</code> list of a solution can be
 * populated based on the list of the componentDefinitions the solution
 * is based on. Then, an application for each component within the
 * solution should be assigned according to the type of application
 * specified in <code>SolutionComponentDefinition</code> this
 * solution component is based on and whether or not a
 * solution component is mandatory or optional. Parameters <code>appType,
 * appVersion, and startupPriority</code> are <code>READONLY</code>
 * within solution.
 * <p/>
 * It shall be possible to edit the <code>components</code> list
 * of a solution in such a way that one or more copies of a solution
 * component already defined within the solution can be created. If
 * such a copy is made, a unique identifier of an application with
 * the type suitable for the newly created component could be assigned
 * to <code>appDBID</code> parameter in this component.
 * <p/>
 * The fact that more than one solution component within a solution may have the
 * same <code>appType, appVersion</code> and <code>startupPriority</code> implies
 * that the order of activation of corresponding applications within the solution
 * may be chosen arbitrarily.
 * <p/>
 * For compatibility purposes between 5.1.xx and 5.9.xxx, objects
 * of type <code>
 * <a href="CfgService.html">CfgService</a>
 * </code> that exist in 5.1.xxx release will
 * have <code>solutionType=CFGSTDefaultSolutionType.</code>
 * <p/>
 * If one solution component is used by two different solutions,
 * both solutions should refer to same application of <code>CFGSCS</code> type
 * (<code>SCSDBID</code>)
 *
 * <br/><br/><font size="+1"><strong>See also:</strong></font><p>
 * <a href="CfgDeltaService.html">
 * <b>CfgDeltaService</b>
 * </a>
 * </p>
 *
 * <p>
 * <a href="CfgTenant.html">
 * <b>CfgTenant</b>
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
public class CfgService
            extends CfgObject {

    public static final CfgObjectType OBJECT_TYPE = CfgObjectType.CFGService;

    /**
     * This constructor is intended for creation of objects from configuration protocol messages.
     * It is internally used by COM AB for objects deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol object data structure
     * @param isSaved indicator of the object saved state
     * @param additionalParameters additional parameters from configuration protocol message
     */
    public CfgService(
            final IConfService confService,
            final ConfObject objData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objData, isSaved, additionalParameters);
        if (objData.getObjectType() != OBJECT_TYPE) {
            throw new IllegalArgumentException(
                    "Incompatible object type for CfgService - "
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
    public CfgService(
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
    public CfgService(final IConfService confService) {
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
            if (getMetaData().getAttribute("components") != null) {
                Collection<CfgSolutionComponent> components = (Collection<CfgSolutionComponent>) getProperty("components");
                if (components != null) {
                    for (CfgSolutionComponent item : components) {
                        item.checkPropertiesForSave();
                    }
                }
            }
            if (getMetaData().getAttribute("componentDefinitions") != null) {
                Collection<CfgSolutionComponentDefinition> componentDefinitions = (Collection<CfgSolutionComponentDefinition>) getProperty("componentDefinitions");
                if (componentDefinitions != null) {
                    for (CfgSolutionComponentDefinition item : componentDefinitions) {
                        item.checkPropertiesForSave();
                    }
                }
            }
            if (getMetaData().getAttribute("resources") != null) {
                Collection<CfgObjectResource> resources = (Collection<CfgObjectResource>) getProperty("resources");
                if (resources != null) {
                    for (CfgObjectResource item : resources) {
                        item.checkPropertiesForSave();
                    }
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
     * A pointer to the name of the service/solution. Mandatory.
     * Must be unique within the Configuration Database.
     *
     * @return property value or null
     */
    public final String getName() {
        return (String) getProperty("name");
    }

    /**
     * A pointer to the name of the service/solution. Mandatory.
     * Must be unique within the Configuration Database.
     *
     * @param value new property value
     * @see #getName()
     */
    public final void setName(final String value) {
        setProperty("name", value);
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
     * <p/>
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
     * <p/>
     * The type of the solution.
     * Mandatory. Once specified cannot be changed. See <code>
     * <a href="../Enumerations/CfgSolutionType.html">CfgSolutionType</a>
     * </code>.
     *
     * @param value new property value
     * @see #getSolutionType()
     */
    public final void setSolutionType(final CfgSolutionType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("solutionType", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgSolutionComponent> getComponents() {
        return (Collection<CfgSolutionComponent>) getProperty("components");
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
     * @param value new property value
     * @see #getComponents()
     */
    public final void setComponents(final Collection<CfgSolutionComponent> value) {
        setProperty("components", value);
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
     * <p/>
     * A unique identifier of an <code>
     * <a href="CfgApplication.html">Application</a>
     * </code>
     * of <code>CFGSCS</code>type which is supposed to control
     * the solution. See comment number 4. See also <code>
     * <a href="CfgSolutionComponent.html">CfgSolutionComponent</a>
     * </code>.
     *
     * @param value new property value
     * @see #getSCS()
     */
    public final void setSCS(final CfgApplication value) {
        setProperty("SCSDBID", value);
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
     * @param dbid DBID identifier of referred object
     * @see #getSCS()
     */
    public final void setSCSDBID(final int dbid) {
        setProperty("SCSDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the SCS property.
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
     * <p/>
     * A unique identifier of the <code>
     * <a href="CfgTenant.html">Tenant</a>
     * </code>
     * the solution is assigned to. Optional. If <code>assignedTenantDBID is managed </code> (added/modified/removed)
     * by configuration wizard , the same action (add/modify/remove) has
     * to be initiated by wizard for property <code>tenantDBIDs</code> of CfgApplication
     * if an application belongs to this solution.
     *
     * @param value new property value
     * @see #getAssignedTenant()
     */
    public final void setAssignedTenant(final CfgTenant value) {
        setProperty("assignedTenantDBID", value);
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
     * @param dbid DBID identifier of referred object
     * @see #getAssignedTenant()
     */
    public final void setAssignedTenantDBID(final int dbid) {
        setProperty("assignedTenantDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the AssignedTenant property.
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
     * A pointer to the version of the
     * solution. Mandatory.
     *
     * @param value new property value
     * @see #getVersion()
     */
    public final void setVersion(final String value) {
        setProperty("version", value);
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
    @SuppressWarnings("unchecked")
    public final Collection<CfgSolutionComponentDefinition> getComponentDefinitions() {
        return (Collection<CfgSolutionComponentDefinition>) getProperty("componentDefinitions");
    }

    /**
     * <p/>
     * A pointer to a list of predefined solution components for
     * this solution. (Every item of this list is structured as a
     * <code>
     * <a href="CfgSolutionComponentDefinition.html">CfgSolutionComponentDefinition</a>
     * </code>.)
     *
     * @param value new property value
     * @see #getComponentDefinitions()
     */
    public final void setComponentDefinitions(final Collection<CfgSolutionComponentDefinition> value) {
        setProperty("componentDefinitions", value);
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
     * @param value new property value
     * @see #getStartupType()
     */
    public final void setStartupType(final CfgStartupType value) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't set this property because containing object has been saved.");
        }
        setProperty("startupType", value);
    }

    @SuppressWarnings("unchecked")
    public final Collection<CfgObjectResource> getResources() {
        return (Collection<CfgObjectResource>) getProperty("resources");
    }

    public final void setResources(final Collection<CfgObjectResource> value) {
        setProperty("resources", value);
    }
}
