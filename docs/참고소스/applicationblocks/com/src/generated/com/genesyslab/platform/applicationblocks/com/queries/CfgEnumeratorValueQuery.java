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
import com.genesyslab.platform.applicationblocks.com.objects.CfgEnumeratorValue;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgEnumeratorValue object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgEnumeratorValue
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgEnumeratorValueQuery extends CfgFilterBasedQuery<CfgEnumeratorValue> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgEnumeratorValue type. This query will not be executable.
     */
    public CfgEnumeratorValueQuery() {
        super(CfgObjectType.CFGEnumeratorValue);
        setObjectClass(CfgEnumeratorValue.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgEnumeratorValue type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgEnumeratorValueQuery(final IConfService service) {
        super(CfgObjectType.CFGEnumeratorValue, service);
        setObjectClass(CfgEnumeratorValue.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgEnumeratorValueQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgEnumeratorValueQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgEnumeratorValue object retrieved as a result of this operation
     */
    public CfgEnumeratorValue executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgEnumeratorValue.class);
    }

    /**
     * Executes the query and returns a list of CfgEnumeratorValue objects.
     *
     * @return A collection of CfgEnumeratorValue objects
     */
    public Collection<CfgEnumeratorValue> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgEnumeratorValue.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgEnumeratorValue> beginExecute(
                final Action<AsyncRequestResult<CfgEnumeratorValue>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgEnumeratorValue.class, callback, state);
    }

    /**
     * A unique identifier of an enumerator. If specified, Configuration
     * Server will return information only about enumerator values for
     * this enumerator.
     *
     * @param value filter value on field "enumerator_dbid".
     */
    public final void setEnumeratorDbid(final int value) {
        setProperty("enumerator_dbid", value);
    }

    /**
     * A unique identifier of an enumerator. If specified, Configuration
     * Server will return information only about enumerator values for
     * this enumerator.
     *
     * @return filter value or null (if applicable)
     * @see #setEnumeratorDbid(int)
     */
    public final int getEnumeratorDbid() {
        return (getInt("enumerator_dbid"));
    }

    /**
     * A default value of an enumerator. If specified, Configuration
     * Server will return information only about enumerator values with
     * this flag set to CFGTrue.
     *
     * @param value filter value on field "default_value".
     */
    public final void setDefaultValue(final int value) {
        setProperty("default_value", value);
    }

    /**
     * A default value of an enumerator. If specified, Configuration
     * Server will return information only about enumerator values with
     * this flag set to CFGTrue.
     *
     * @return filter value or null (if applicable)
     * @see #setDefaultValue(int)
     */
    public final int getDefaultValue() {
        return (getInt("default_value"));
    }

    /**
     * A unique identifier of an enumerator
     * value. If specified, Configuration Server will return information
     * only about this enumerator value.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of an enumerator
     * value. If specified, Configuration Server will return information
     * only about this enumerator value.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }

    /**
     * System name of an enumerator value.
     * Shall be specified as a character string. If specified, Configuration
     * Server will return information only about the enumerator value(s)
     * with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * System name of an enumerator value.
     * Shall be specified as a character string. If specified, Configuration
     * Server will return information only about the enumerator value(s)
     * with that name.
     *
     * @return filter value or null (if applicable)
     * @see #setName(String)
     */
    public final String getName() {
        return (getString("name"));
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the enumerator values that belong to this tenant.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the enumerator values that belong to this tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * Current state of a enumerator value(see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about enumerator values that are currently
     * in this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of a enumerator value(see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about enumerator values that are currently
     * in this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * Display name of an enumerator
     * value. Shall be specified as a character string. If specified, Configuration
     * Server will return information only about the enumerator(s) with
     * that display name.
     *
     * @param value filter value on field "display_name".
     */
    public final void setDisplayName(final String value) {
        setProperty("display_name", value);
    }

    /**
     * Display name of an enumerator
     * value. Shall be specified as a character string. If specified, Configuration
     * Server will return information only about the enumerator(s) with
     * that display name.
     *
     * @return filter value or null (if applicable)
     * @see #setDisplayName(String)
     */
    public final String getDisplayName() {
        return (getString("display_name"));
    }
}
