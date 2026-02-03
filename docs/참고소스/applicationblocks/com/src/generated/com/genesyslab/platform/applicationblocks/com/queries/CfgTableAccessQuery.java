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
import com.genesyslab.platform.applicationblocks.com.objects.CfgTableAccess;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgTableAccess object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTableAccess
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgTableAccessQuery extends CfgFilterBasedQuery<CfgTableAccess> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgTableAccess type. This query will not be executable.
     */
    public CfgTableAccessQuery() {
        super(CfgObjectType.CFGTableAccess);
        setObjectClass(CfgTableAccess.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgTableAccess type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgTableAccessQuery(final IConfService service) {
        super(CfgObjectType.CFGTableAccess, service);
        setObjectClass(CfgTableAccess.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgTableAccessQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgTableAccessQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgTableAccess object retrieved as a result of this operation
     */
    public CfgTableAccess executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgTableAccess.class);
    }

    /**
     * Executes the query and returns a list of CfgTableAccess objects.
     *
     * @return A collection of CfgTableAccess objects
     */
    public Collection<CfgTableAccess> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgTableAccess.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgTableAccess> beginExecute(
                final Action<AsyncRequestResult<CfgTableAccess>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgTableAccess.class, callback, state);
    }

    /**
     * A unique identifier of a table access.
     * If specified, configuration server will return information only
     * about this table access.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of a table access.
     * If specified, configuration server will return information only
     * about this table access.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }

    /**
     * A unique identifier of the
     * tenant. If specified, Configuration server will return information
     * only about the table accesses that belong to this tenant.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of the
     * tenant. If specified, Configuration server will return information
     * only about the table accesses that belong to this tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * Name of a table access. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the table access(es) with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Name of a table access. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the table access(es) with that name.
     *
     * @return filter value or null (if applicable)
     * @see #setName(String)
     */
    public final String getName() {
        return (getString("name"));
    }

    /**
     * A type of table (See
     * CfgTableType
     * ). If
     * specified, Configuration Server will return information only about
     * the table(s) of that type.
     *
     * @param value filter value on field "type".
     */
    public final void setType(final CfgTableType value) {
        setProperty("type", value);
    }

    /**
     * A type of table (See
     * CfgTableType
     * ). If
     * specified, Configuration Server will return information only about
     * the table(s) of that type.
     *
     * @return filter value or null (if applicable)
     * @see #setType(CfgTableType)
     */
    public final CfgTableType getType() {
        return (CfgTableType) CfgTableType.getValue(CfgTableType.class, getInt("type"));
    }

    /**
     * A unique identifier of the data base access point through
     * which the table can be accessed. If specified, Configuration Server
     * will return information only about the table access(es) which consists
     * this data base access point.
     *
     * @param value filter value on field "dbaccess_dbid".
     */
    public final void setDbaccessDbid(final int value) {
        setProperty("dbaccess_dbid", value);
    }

    /**
     * A unique identifier of the data base access point through
     * which the table can be accessed. If specified, Configuration Server
     * will return information only about the table access(es) which consists
     * this data base access point.
     *
     * @return filter value or null (if applicable)
     * @see #setDbaccessDbid(int)
     */
    public final int getDbaccessDbid() {
        return (getInt("dbaccess_dbid"));
    }

    /**
     * A unique identifier of format
     * of the table. If specified, Configuration Server will return information
     * only about the table(s) with that format.
     *
     * @param value filter value on field "format_dbid".
     */
    public final void setFormatDbid(final int value) {
        setProperty("format_dbid", value);
    }

    /**
     * A unique identifier of format
     * of the table. If specified, Configuration Server will return information
     * only about the table(s) with that format.
     *
     * @return filter value or null (if applicable)
     * @see #setFormatDbid(int)
     */
    public final int getFormatDbid() {
        return (getInt("format_dbid"));
    }

    /**
     * Name of the table. Shall be specified as a character string.
     * If specified, Configuration Server will return information only
     * about the table access(es) for that table.
     *
     * @param value filter value on field "db_table_name".
     */
    public final void setDbTableName(final String value) {
        setProperty("db_table_name", value);
    }

    /**
     * Name of the table. Shall be specified as a character string.
     * If specified, Configuration Server will return information only
     * about the table access(es) for that table.
     *
     * @return filter value or null (if applicable)
     * @see #setDbTableName(String)
     */
    public final String getDbTableName() {
        return (getString("db_table_name"));
    }

    /**
     * Current state of the table access
     * (See
     * CfgObjectState
     * ). If specified, Configuration
     * Server will return information only about the table access(es) that
     * are currently in this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of the table access
     * (See
     * CfgObjectState
     * ). If specified, Configuration
     * Server will return information only about the table access(es) that
     * are currently in this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }
}
