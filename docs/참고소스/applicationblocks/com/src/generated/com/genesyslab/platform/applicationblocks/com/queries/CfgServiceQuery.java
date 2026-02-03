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
import com.genesyslab.platform.applicationblocks.com.objects.CfgService;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgService object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgService
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgServiceQuery extends CfgFilterBasedQuery<CfgService> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgService type. This query will not be executable.
     */
    public CfgServiceQuery() {
        super(CfgObjectType.CFGService);
        setObjectClass(CfgService.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgService type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgServiceQuery(final IConfService service) {
        super(CfgObjectType.CFGService, service);
        setObjectClass(CfgService.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgServiceQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgServiceQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgService object retrieved as a result of this operation
     */
    public CfgService executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgService.class);
    }

    /**
     * Executes the query and returns a list of CfgService objects.
     *
     * @return A collection of CfgService objects
     */
    public Collection<CfgService> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgService.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgService> beginExecute(
                final Action<AsyncRequestResult<CfgService>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgService.class, callback, state);
    }

    /**
     * A unique identifier of a service.
     * If specified, Configuration Server will return information only
     * about this service.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of a service.
     * If specified, Configuration Server will return information only
     * about this service.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }

    /**
     * Name of a service. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the service(s) with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Name of a service. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the service(s) with that name.
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
     * about the services this tenant is subscribed to.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the services this tenant is subscribed to.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * Type of the application (see
     * CfgAppType
     * ). If specified, Configuration Server will return information
     * only about the services that involve this application type.
     *
     * @param value filter value on field "app_type".
     */
    public final void setAppType(final CfgAppType value) {
        setProperty("app_type", value);
    }

    /**
     * Type of the application (see
     * CfgAppType
     * ). If specified, Configuration Server will return information
     * only about the services that involve this application type.
     *
     * @return filter value or null (if applicable)
     * @see #setAppType(CfgAppType)
     */
    public final CfgAppType getAppType() {
        return (CfgAppType) CfgAppType.getValue(CfgAppType.class, getInt("app_type"));
    }

    /**
     * Current state of a service (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about services that are currently in
     * this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of a service (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about services that are currently in
     * this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * A unique identifier of an application.
     * If specified, Configuration Server will return information only
     * about the Solutions that involve this application.
     *
     * @param value filter value on field "app_dbid".
     */
    public final void setAppDbid(final int value) {
        setProperty("app_dbid", value);
    }

    /**
     * A unique identifier of an application.
     * If specified, Configuration Server will return information only
     * about the Solutions that involve this application.
     *
     * @return filter value or null (if applicable)
     * @see #setAppDbid(int)
     */
    public final int getAppDbid() {
        return (getInt("app_dbid"));
    }

    /**
     * A unique identifier of a Service
     * Control Server. If specified, Configuration Server will return
     * information only about the solutions controlled by this SCS.
     *
     * @param value filter value on field "scs_dbid".
     */
    public final void setScsDbid(final int value) {
        setProperty("scs_dbid", value);
    }

    /**
     * A unique identifier of a Service
     * Control Server. If specified, Configuration Server will return
     * information only about the solutions controlled by this SCS.
     *
     * @return filter value or null (if applicable)
     * @see #setScsDbid(int)
     */
    public final int getScsDbid() {
        return (getInt("scs_dbid"));
    }

    /**
     * The type of the solution. If specified,
     * Configuration Server will return information only about the solutions
     * of specified type.
     *
     * @param value filter value on field "type".
     */
    public final void setType(final int value) {
        setProperty("type", value);
    }

    /**
     * The type of the solution. If specified,
     * Configuration Server will return information only about the solutions
     * of specified type.
     *
     * @return filter value or null (if applicable)
     * @see #setType(int)
     */
    public final int getType() {
        return (getInt("type"));
    }

    /**
     * A unique identifier of a folder.
     * If specified, Configuration Server will return information only
     * about the services located immediately under this folder.
     *
     * @param value filter value on field "folder_dbid".
     */
    public final void setFolderDbid(final int value) {
        setProperty("folder_dbid", value);
    }

    /**
     * A unique identifier of a folder.
     * If specified, Configuration Server will return information only
     * about the services located immediately under this folder.
     *
     * @return filter value or null (if applicable)
     * @see #setFolderDbid(int)
     */
    public final int getFolderDbid() {
        return (getInt("folder_dbid"));
    }
}
