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


package com.genesyslab.platform.applicationblocks.com.queries;

import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.CfgFilterBasedQuery;
import com.genesyslab.platform.applicationblocks.com.objects.CfgScript;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgScript object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgScript
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgScriptQuery extends CfgFilterBasedQuery<CfgScript> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgScript type. This query will not be executable.
     */
    public CfgScriptQuery() {
        super(CfgObjectType.CFGScript);
        setObjectClass(CfgScript.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgScript type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgScriptQuery(final IConfService service) {
        super(CfgObjectType.CFGScript, service);
        setObjectClass(CfgScript.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgScriptQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgScriptQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgScript object retrieved as a result of this operation
     */
    public CfgScript executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgScript.class);
    }

    /**
     * Executes the query and returns a list of CfgScript objects.
     *
     * @return A collection of CfgScript objects
     */
    public Collection<CfgScript> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgScript.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgScript> beginExecute(
                final Action<AsyncRequestResult<CfgScript>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgScript.class, callback, state);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the script(s) that belong to this tenant.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the script(s) that belong to this tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * Type of the script (see
     * CfgScriptType
     * ).
     * If specified, Configuration Server will return information only
     * about the script(s) of this type.
     *
     * @param value filter value on field "script_type".
     */
    public final void setScriptType(final CfgScriptType value) {
        setProperty("script_type", value);
    }

    /**
     * Type of the script (see
     * CfgScriptType
     * ).
     * If specified, Configuration Server will return information only
     * about the script(s) of this type.
     *
     * @return filter value or null (if applicable)
     * @see #setScriptType(CfgScriptType)
     */
    public final CfgScriptType getScriptType() {
        return (CfgScriptType) CfgScriptType.getValue(CfgScriptType.class, getInt("script_type"));
    }

    /**
     * Current state of a script (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about scripts that are currently in
     * this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of a script (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about scripts that are currently in
     * this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * A unique identifier of a script. If
     * specified, Configuration Server will return information only about
     * this script.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of a script. If
     * specified, Configuration Server will return information only about
     * this script.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }

    /**
     * Name of a script. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the script(s) with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Name of a script. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the script(s) with that name.
     *
     * @return filter value or null (if applicable)
     * @see #setName(String)
     */
    public final String getName() {
        return (getString("name"));
    }

    /**
     * A unique identifier of Tenant. If specified, Configuration
     * Server will return information only about the script that is associated
     * with specified tenant.
     *
     * @param value filter value on field "capacity_tenant_dbid".
     */
    public final void setCapacityTenantDbid(final int value) {
        setProperty("capacity_tenant_dbid", value);
    }

    /**
     * A unique identifier of Tenant. If specified, Configuration
     * Server will return information only about the script that is associated
     * with specified tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setCapacityTenantDbid(int)
     */
    public final int getCapacityTenantDbid() {
        return (getInt("capacity_tenant_dbid"));
    }

    /**
     * A unique identifier of Person/Agent. If specified, Configuration
     * Server will return information only about the script that is associated
     * with specified agent.
     *
     * @param value filter value on field "capacity_agent_dbid".
     */
    public final void setCapacityAgentDbid(final int value) {
        setProperty("capacity_agent_dbid", value);
    }

    /**
     * A unique identifier of Person/Agent. If specified, Configuration
     * Server will return information only about the script that is associated
     * with specified agent.
     *
     * @return filter value or null (if applicable)
     * @see #setCapacityAgentDbid(int)
     */
    public final int getCapacityAgentDbid() {
        return (getInt("capacity_agent_dbid"));
    }

    /**
     * Unique identifier of an AgentGroup. If specified, Configuration
     * Server will return information only about the script that associated
     * with agent group.
     *
     * @param value filter value on field "capacity_agent_group_dbid".
     */
    public final void setCapacityAgentGroupDbid(final int value) {
        setProperty("capacity_agent_group_dbid", value);
    }

    /**
     * Unique identifier of an AgentGroup. If specified, Configuration
     * Server will return information only about the script that associated
     * with agent group.
     *
     * @return filter value or null (if applicable)
     * @see #setCapacityAgentGroupDbid(int)
     */
    public final int getCapacityAgentGroupDbid() {
        return (getInt("capacity_agent_group_dbid"));
    }

    /**
     * Unique identifier of a Place. If specified, Configuration
     * Server will return information only about the script that is associated
     * with specified place.
     *
     * @param value filter value on field "capacity_place_dbid".
     */
    public final void setCapacityPlaceDbid(final int value) {
        setProperty("capacity_place_dbid", value);
    }

    /**
     * Unique identifier of a Place. If specified, Configuration
     * Server will return information only about the script that is associated
     * with specified place.
     *
     * @return filter value or null (if applicable)
     * @see #setCapacityPlaceDbid(int)
     */
    public final int getCapacityPlaceDbid() {
        return (getInt("capacity_place_dbid"));
    }

    /**
     * Unique identifier of a PlaceGroup. If specified, Configuration
     * Server will return information only about the script that is associated
     * with specified place group.
     *
     * @param value filter value on field "capacity_place_group_dbid".
     */
    public final void setCapacityPlaceGroupDbid(final int value) {
        setProperty("capacity_place_group_dbid", value);
    }

    /**
     * Unique identifier of a PlaceGroup. If specified, Configuration
     * Server will return information only about the script that is associated
     * with specified place group.
     *
     * @return filter value or null (if applicable)
     * @see #setCapacityPlaceGroupDbid(int)
     */
    public final int getCapacityPlaceGroupDbid() {
        return (getInt("capacity_place_group_dbid"));
    }

    /**
     * A flag controlling how the bytecode
     * binary option from the userProperties field will be returned. If
     * set in the filter, Configuration Server will return an empty list under this
     * option without regard to the actual content.
     *
     * @param value filter value on field "exclude_bytecode".
     */
    public final void setExcludeBytecode(final int value) {
        setProperty("exclude_bytecode", value);
    }

    /**
     * A flag controlling how the bytecode
     * binary option from the userProperties field will be returned. If
     * set in the filter, Configuration Server will return an empty list under this
     * option without regard to the actual content.
     *
     * @return filter value or null (if applicable)
     * @see #setExcludeBytecode(int)
     */
    public final int getExcludeBytecode() {
        return (getInt("exclude_bytecode"));
    }

    /**
     * A flag controlling how the string options longer that 255 chars userProperties field ( or options for objects that support them) field will be returned. If
     * set in the filter, Configuration Server will return an empty string under this
     * option if value is changed to more than 255
     * Note: this option is also applicable to all configuration objects, not only Script
     *
     * @param value filter value on field "exclude_longstrings".
     */
    public final void setExcludeLongstrings(final int value) {
        setProperty("exclude_longstrings", value);
    }

    /**
     * A flag controlling how the string options longer that 255 chars userProperties field ( or options for objects that support them) field will be returned. If
     * set in the filter, Configuration Server will return an empty string under this
     * option if value is changed to more than 255
     * Note: this option is also applicable to all configuration objects, not only Script
     *
     * @return filter value or null (if applicable)
     * @see #setExcludeLongstrings(int)
     */
    public final int getExcludeLongstrings() {
        return (getInt("exclude_longstrings"));
    }
}
