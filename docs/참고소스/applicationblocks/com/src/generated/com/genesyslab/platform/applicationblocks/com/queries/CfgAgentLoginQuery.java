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
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLogin;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgAgentLogin object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLogin
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgAgentLoginQuery extends CfgFilterBasedQuery<CfgAgentLogin> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgAgentLogin type. This query will not be executable.
     */
    public CfgAgentLoginQuery() {
        super(CfgObjectType.CFGAgentLogin);
        setObjectClass(CfgAgentLogin.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgAgentLogin type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgAgentLoginQuery(final IConfService service) {
        super(CfgObjectType.CFGAgentLogin, service);
        setObjectClass(CfgAgentLogin.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgAgentLoginQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgAgentLogin object retrieved as a result of this operation
     */
    public CfgAgentLogin executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgAgentLogin.class);
    }

    /**
     * Executes the query and returns a list of CfgAgentLogin objects.
     *
     * @return A collection of CfgAgentLogin objects
     */
    public Collection<CfgAgentLogin> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgAgentLogin.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgAgentLogin> beginExecute(
                final Action<AsyncRequestResult<CfgAgentLogin>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgAgentLogin.class, callback, state);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the agent logins that belong to this tenant.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the agent logins that belong to this tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * A unique identifier of a switch.
     * If specified, Configuration Server will return information only
     * about the agent logins that belong to this switch.
     *
     * @param value filter value on field "switch_dbid".
     */
    public final void setSwitchDbid(final int value) {
        setProperty("switch_dbid", value);
    }

    /**
     * A unique identifier of a switch.
     * If specified, Configuration Server will return information only
     * about the agent logins that belong to this switch.
     *
     * @return filter value or null (if applicable)
     * @see #setSwitchDbid(int)
     */
    public final int getSwitchDbid() {
        return (getInt("switch_dbid"));
    }

    /**
     * Current state of an agent login (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about agent logins that are currently
     * in this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of an agent login (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about agent logins that are currently
     * in this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * Agent login code. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the agent login(s) with this code.
     *
     * @param value filter value on field "login_code".
     */
    public final void setLoginCode(final String value) {
        setProperty("login_code", value);
    }

    /**
     * Agent login code. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the agent login(s) with this code.
     *
     * @return filter value or null (if applicable)
     * @see #setLoginCode(String)
     */
    public final String getLoginCode() {
        return (getString("login_code"));
    }

    /**
     * A unique identifier of an agent login.
     * If specified, Configuration Server will return information only
     * about this agent login.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of an agent login.
     * If specified, Configuration Server will return information only
     * about this agent login.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }

    /**
     * Configuration Server will return information only about agent
     * logins that currently are not associated with any person.
     *
     * @param value filter value on field "no_person_dbid".
     */
    public final void setNoPersonDbid(final int value) {
        setProperty("no_person_dbid", value);
    }

    /**
     * Configuration Server will return information only about agent
     * logins that currently are not associated with any person.
     *
     * @return filter value or null (if applicable)
     * @see #setNoPersonDbid(int)
     */
    public final int getNoPersonDbid() {
        return (getInt("no_person_dbid"));
    }
}
