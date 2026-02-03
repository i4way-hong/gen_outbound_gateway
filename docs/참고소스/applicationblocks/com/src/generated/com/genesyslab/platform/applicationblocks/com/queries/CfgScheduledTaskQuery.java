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
import com.genesyslab.platform.applicationblocks.com.objects.CfgScheduledTask;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgScheduledTask object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgScheduledTask
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgScheduledTaskQuery extends CfgFilterBasedQuery<CfgScheduledTask> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgScheduledTask type. This query will not be executable.
     */
    public CfgScheduledTaskQuery() {
        super(CfgObjectType.CFGScheduledTask);
        setObjectClass(CfgScheduledTask.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgScheduledTask type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgScheduledTaskQuery(final IConfService service) {
        super(CfgObjectType.CFGScheduledTask, service);
        setObjectClass(CfgScheduledTask.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgScheduledTaskQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgScheduledTaskQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgScheduledTask object retrieved as a result of this operation
     */
    public CfgScheduledTask executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgScheduledTask.class);
    }

    /**
     * Executes the query and returns a list of CfgScheduledTask objects.
     *
     * @return A collection of CfgScheduledTask objects
     */
    public Collection<CfgScheduledTask> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgScheduledTask.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgScheduledTask> beginExecute(
                final Action<AsyncRequestResult<CfgScheduledTask>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgScheduledTask.class, callback, state);
    }

    /**
     * Type of the ScheduledTask (see
     * CfgTaskType
     * ).
     * If specified, Configuration Server will return information only
     * about the ScheduledTasks of this type.
     *
     * @param value filter value on field "task_type".
     */
    public final void setTaskType(final CfgTaskType value) {
        setProperty("task_type", value);
    }

    /**
     * Type of the ScheduledTask (see
     * CfgTaskType
     * ).
     * If specified, Configuration Server will return information only
     * about the ScheduledTasks of this type.
     *
     * @return filter value or null (if applicable)
     * @see #setTaskType(CfgTaskType)
     */
    public final CfgTaskType getTaskType() {
        return (CfgTaskType) CfgTaskType.getValue(CfgTaskType.class, getInt("task_type"));
    }

    /**
     * Current state of a ScheduledTask (see
     * CfgObjectState
     * ).
     * If specified, Configuration Server will return information only
     * about ScheduledTasks that are currently in this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of a ScheduledTask (see
     * CfgObjectState
     * ).
     * If specified, Configuration Server will return information only
     * about ScheduledTasks that are currently in this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * Name of a ScheduledTask. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the ScheduledTask(s) with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Name of a ScheduledTask. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the ScheduledTask(s) with that name.
     *
     * @return filter value or null (if applicable)
     * @see #setName(String)
     */
    public final String getName() {
        return (getString("name"));
    }

    /**
     * A unique identifier of a ScheduledTask. If
     * specified, Configuration Server will return information only about
     * this ScheduledTask.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of a ScheduledTask. If
     * specified, Configuration Server will return information only about
     * this ScheduledTask.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }
}
