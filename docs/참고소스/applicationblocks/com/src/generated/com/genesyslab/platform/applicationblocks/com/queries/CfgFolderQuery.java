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
import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgFolder object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgFolder
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgFolderQuery extends CfgFilterBasedQuery<CfgFolder> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgFolder type. This query will not be executable.
     */
    public CfgFolderQuery() {
        super(CfgObjectType.CFGFolder);
        setObjectClass(CfgFolder.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgFolder type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgFolderQuery(final IConfService service) {
        super(CfgObjectType.CFGFolder, service);
        setObjectClass(CfgFolder.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgFolderQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgFolderQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgFolder object retrieved as a result of this operation
     */
    public CfgFolder executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgFolder.class);
    }

    /**
     * Executes the query and returns a list of CfgFolder objects.
     *
     * @return A collection of CfgFolder objects
     */
    public Collection<CfgFolder> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgFolder.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgFolder> beginExecute(
                final Action<AsyncRequestResult<CfgFolder>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgFolder.class, callback, state);
    }

    /**
     * A unique identifier of a folder. If
     * specified, Configuration Server will return information only about
     * this folder.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of a folder. If
     * specified, Configuration Server will return information only about
     * this folder.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }

    /**
     * Name of a folder. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the folder(s) with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Name of a folder. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the folder(s) with that name.
     *
     * @return filter value or null (if applicable)
     * @see #setName(String)
     */
    public final String getName() {
        return (getString("name"));
    }

    /**
     * A unique identifier of an owner
     * object. If specified, Configuration Server will return information
     * only about the folders that belong to this object. Must be used
     * in conjunction with the owner_type filter.
     *
     * @param value filter value on field "owner_dbid".
     */
    public final void setOwnerDbid(final int value) {
        setProperty("owner_dbid", value);
    }

    /**
     * A unique identifier of an owner
     * object. If specified, Configuration Server will return information
     * only about the folders that belong to this object. Must be used
     * in conjunction with the owner_type filter.
     *
     * @return filter value or null (if applicable)
     * @see #setOwnerDbid(int)
     */
    public final int getOwnerDbid() {
        return (getInt("owner_dbid"));
    }

    /**
     * A type of an owner object. Must
     * be used in conjunction with the owner_dbid filter.
     *
     * @param value filter value on field "owner_type".
     */
    public final void setOwnerType(final int value) {
        setProperty("owner_type", value);
    }

    /**
     * A type of an owner object. Must
     * be used in conjunction with the owner_dbid filter.
     *
     * @return filter value or null (if applicable)
     * @see #setOwnerType(int)
     */
    public final int getOwnerType() {
        return (getInt("owner_type"));
    }

    /**
     * A type of a folder. If specified,
     * Configuration Server will return information only about folders
     * of this type.
     *
     * @param value filter value on field "type".
     */
    public final void setType(final int value) {
        setProperty("type", value);
    }

    /**
     * A type of a folder. If specified,
     * Configuration Server will return information only about folders
     * of this type.
     *
     * @return filter value or null (if applicable)
     * @see #setType(int)
     */
    public final int getType() {
        return (getInt("type"));
    }

    /**
     * A flag which selects among the folders belonging to some
     * owner object the topmost one, e.g. that which does not have a parent
     * folder above. Must be used in conjunction with
     * owner_type and owner_dbid filters.
     * Most likely will be used with type filter.
     *
     * @param value filter value on field "default_folder".
     */
    public final void setDefaultFolder(final int value) {
        setProperty("default_folder", value);
    }

    /**
     * A flag which selects among the folders belonging to some
     * owner object the topmost one, e.g. that which does not have a parent
     * folder above. Must be used in conjunction with
     * owner_type and owner_dbid filters.
     * Most likely will be used with type filter.
     *
     * @return filter value or null (if applicable)
     * @see #setDefaultFolder(int)
     */
    public final int getDefaultFolder() {
        return (getInt("default_folder"));
    }

    /**
     * A unique identifier of a subordinate
     * object. If specified, Configuration Server will return information
     * only about the folder that contains this object. Must be used in
     * conjunction with the object_type filter.
     *
     * @param value filter value on field "object_dbid".
     */
    public final void setObjectDbid(final int value) {
        setProperty("object_dbid", value);
    }

    /**
     * A unique identifier of a subordinate
     * object. If specified, Configuration Server will return information
     * only about the folder that contains this object. Must be used in
     * conjunction with the object_type filter.
     *
     * @return filter value or null (if applicable)
     * @see #setObjectDbid(int)
     */
    public final int getObjectDbid() {
        return (getInt("object_dbid"));
    }

    /**
     * A type of a subordinate object.
     * Must be used in conjunction with the object_dbid filter.
     *
     * @param value filter value on field "object_type".
     */
    public final void setObjectType(final int value) {
        setProperty("object_type", value);
    }

    /**
     * A type of a subordinate object.
     * Must be used in conjunction with the object_dbid filter.
     *
     * @return filter value or null (if applicable)
     * @see #setObjectType(int)
     */
    public final int getObjectType() {
        return (getInt("object_type"));
    }

    /**
     * Current state of a folder (see type
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about folders that are currently in
     * this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of a folder (see type
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about folders that are currently in
     * this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * A class of a folder. If specified,
     * Configuration Server will return information only about folders
     * of this class.
     *
     * @param value filter value on field "folder_class".
     */
    public final void setFolderClass(final int value) {
        setProperty("folder_class", value);
    }

    /**
     * A class of a folder. If specified,
     * Configuration Server will return information only about folders
     * of this class.
     *
     * @return filter value or null (if applicable)
     * @see #setFolderClass(int)
     */
    public final int getFolderClass() {
        return (getInt("folder_class"));
    }
}
