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
import com.genesyslab.platform.applicationblocks.com.objects.CfgDNGroup;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgDNGroup object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgDNGroup
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDNGroupQuery extends CfgFilterBasedQuery<CfgDNGroup> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgDNGroup type. This query will not be executable.
     */
    public CfgDNGroupQuery() {
        super(CfgObjectType.CFGDNGroup);
        setObjectClass(CfgDNGroup.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgDNGroup type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgDNGroupQuery(final IConfService service) {
        super(CfgObjectType.CFGDNGroup, service);
        setObjectClass(CfgDNGroup.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgDNGroupQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgDNGroupQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgDNGroup object retrieved as a result of this operation
     */
    public CfgDNGroup executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgDNGroup.class);
    }

    /**
     * Executes the query and returns a list of CfgDNGroup objects.
     *
     * @return A collection of CfgDNGroup objects
     */
    public Collection<CfgDNGroup> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgDNGroup.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgDNGroup> beginExecute(
                final Action<AsyncRequestResult<CfgDNGroup>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgDNGroup.class, callback, state);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the DN groups that belong to this tenant.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the DN groups that belong to this tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * Type of a DN group (see
     * CfgDNGroupType
     * ). If
     * specified, Configuration Server will return information only about
     * the DN groups of this type.
     *
     * @param value filter value on field "dn_group_type".
     */
    public final void setDnGroupType(final CfgDNGroupType value) {
        setProperty("dn_group_type", value);
    }

    /**
     * Type of a DN group (see
     * CfgDNGroupType
     * ). If
     * specified, Configuration Server will return information only about
     * the DN groups of this type.
     *
     * @return filter value or null (if applicable)
     * @see #setDnGroupType(CfgDNGroupType)
     */
    public final CfgDNGroupType getDnGroupType() {
        return (CfgDNGroupType) CfgDNGroupType.getValue(CfgDNGroupType.class, getInt("dn_group_type"));
    }

    /**
     * A unique identifier of a DN. If
     * specified, Configuration Server will return information only about
     * the DN groups this DN is assigned to.
     *
     * @param value filter value on field "dn_dbid".
     */
    public final void setDnDbid(final int value) {
        setProperty("dn_dbid", value);
    }

    /**
     * A unique identifier of a DN. If
     * specified, Configuration Server will return information only about
     * the DN groups this DN is assigned to.
     *
     * @return filter value or null (if applicable)
     * @see #setDnDbid(int)
     */
    public final int getDnDbid() {
        return (getInt("dn_dbid"));
    }

    /**
     * Current state of a DN group (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about DN groups that are currently
     * in this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of a DN group (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about DN groups that are currently
     * in this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * Name of a DN group. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the agent group(s) with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Name of a DN group. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the agent group(s) with that name.
     *
     * @return filter value or null (if applicable)
     * @see #setName(String)
     */
    public final String getName() {
        return (getString("name"));
    }

    /**
     * A unique identifier of a group. If
     * specified, Configuration Server will return information only about
     * this group.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of a group. If
     * specified, Configuration Server will return information only about
     * this group.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }
}
