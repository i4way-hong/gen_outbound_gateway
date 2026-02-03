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
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgApplication object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgApplication
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgApplicationQuery extends CfgFilterBasedQuery<CfgApplication> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgApplication type. This query will not be executable.
     */
    public CfgApplicationQuery() {
        super(CfgObjectType.CFGApplication);
        setObjectClass(CfgApplication.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgApplication type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgApplicationQuery(final IConfService service) {
        super(CfgObjectType.CFGApplication, service);
        setObjectClass(CfgApplication.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgApplicationQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgApplicationQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgApplication object retrieved as a result of this operation
     */
    public CfgApplication executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgApplication.class);
    }

    /**
     * Executes the query and returns a list of CfgApplication objects.
     *
     * @return A collection of CfgApplication objects
     */
    public Collection<CfgApplication> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgApplication.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgApplication> beginExecute(
                final Action<AsyncRequestResult<CfgApplication>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgApplication.class, callback, state);
    }

    /**
     * Type of the application (see
     * CfgAppType
     * ). If specified, Configuration Server will
     * return information only about the applications of this type.
     *
     * @param value filter value on field "app_type".
     */
    public final void setAppType(final CfgAppType value) {
        setProperty("app_type", value);
    }

    /**
     * Type of the application (see
     * CfgAppType
     * ). If specified, Configuration Server will
     * return information only about the applications of this type.
     *
     * @return filter value or null (if applicable)
     * @see #setAppType(CfgAppType)
     */
    public final CfgAppType getAppType() {
        return (CfgAppType) CfgAppType.getValue(CfgAppType.class, getInt("app_type"));
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the applications that are associated with this tenant.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the applications that are associated with this tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * An indicator of whether this
     * application can be a server to some other applications. Depending
     * on value specified, Configuration Server will return information
     * only about the application(s) that are either servers or non-servers.
     * See
     * CfgFlag
     * .
     *
     * @param value filter value on field "is_server".
     */
    public final void setIsServer(final CfgFlag value) {
        setProperty("is_server", value);
    }

    /**
     * An indicator of whether this
     * application can be a server to some other applications. Depending
     * on value specified, Configuration Server will return information
     * only about the application(s) that are either servers or non-servers.
     * See
     * CfgFlag
     * .
     *
     * @return filter value or null (if applicable)
     * @see #setIsServer(CfgFlag)
     */
    public final CfgFlag getIsServer() {
        return (CfgFlag) CfgFlag.getValue(CfgFlag.class, getInt("is_server"));
    }

    /**
     * A unique identifier of an application.
     * If specified, Configuration Server will return information only
     * about the applications that are clients to this application.
     *
     * @param value filter value on field "server_dbid".
     */
    public final void setServerDbid(final int value) {
        setProperty("server_dbid", value);
    }

    /**
     * A unique identifier of an application.
     * If specified, Configuration Server will return information only
     * about the applications that are clients to this application.
     *
     * @return filter value or null (if applicable)
     * @see #setServerDbid(int)
     */
    public final int getServerDbid() {
        return (getInt("server_dbid"));
    }

    /**
     * A unique identifier of an application. If specified, Configuration
     * Server will return information only about the application that is
     * backup to this application.
     *
     * @param value filter value on field "primary_server_dbid".
     */
    public final void setPrimaryServerDbid(final int value) {
        setProperty("primary_server_dbid", value);
    }

    /**
     * A unique identifier of an application. If specified, Configuration
     * Server will return information only about the application that is
     * backup to this application.
     *
     * @return filter value or null (if applicable)
     * @see #setPrimaryServerDbid(int)
     */
    public final int getPrimaryServerDbid() {
        return (getInt("primary_server_dbid"));
    }

    /**
     * A unique identifier of an application. If specified, Configuration
     * Server will return information only about the application that is
     * primary to this application.
     *
     * @param value filter value on field "backup_server_dbid".
     */
    public final void setBackupServerDbid(final int value) {
        setProperty("backup_server_dbid", value);
    }

    /**
     * A unique identifier of an application. If specified, Configuration
     * Server will return information only about the application that is
     * primary to this application.
     *
     * @return filter value or null (if applicable)
     * @see #setBackupServerDbid(int)
     */
    public final int getBackupServerDbid() {
        return (getInt("backup_server_dbid"));
    }

    /**
     * A unique identifier of an application prototype. If specified,
     * Configuration Server will return information only about the applications
     * that are based on this prototype.
     *
     * @param value filter value on field "app_prototype_dbid".
     */
    public final void setAppPrototypeDbid(final int value) {
        setProperty("app_prototype_dbid", value);
    }

    /**
     * A unique identifier of an application prototype. If specified,
     * Configuration Server will return information only about the applications
     * that are based on this prototype.
     *
     * @return filter value or null (if applicable)
     * @see #setAppPrototypeDbid(int)
     */
    public final int getAppPrototypeDbid() {
        return (getInt("app_prototype_dbid"));
    }

    /**
     * Type of the object that may be used as an account for a daemon application
     * (see
     * CfgObjectType
     * ).
     * Makes sense only if used with the filter account_dbid (see below).
     * If both account_type and account_dbid
     * are specified, Configuration Server will return information only
     * about the applications associated with this account. Such information
     * will only be provided to the clients that have privileges to read
     * access control list of this application.
     *
     * @param value filter value on field "account_type".
     */
    public final void setAccountType(final CfgObjectType value) {
        setProperty("account_type", value);
    }

    /**
     * Type of the object that may be used as an account for a daemon application
     * (see
     * CfgObjectType
     * ).
     * Makes sense only if used with the filter account_dbid (see below).
     * If both account_type and account_dbid
     * are specified, Configuration Server will return information only
     * about the applications associated with this account. Such information
     * will only be provided to the clients that have privileges to read
     * access control list of this application.
     *
     * @return filter value or null (if applicable)
     * @see #setAccountType(CfgObjectType)
     */
    public final CfgObjectType getAccountType() {
        return (CfgObjectType) CfgObjectType.getValue(CfgObjectType.class, getInt("account_type"));
    }

    /**
     * A unique identifier of the
     * object that may be used as an account for a daemon application (see
     * type CfgObjectType). Makes sense only if used
     * with the filter account_type (see below). Makes sense
     * only if used with the filter account_dbid (see below).
     * If both account_type and account_dbid
     * are specified, Configuration Server will return information only
     * about the applications associated with this account. Such information
     * will only be provided to the clients that have privileges to read
     * access control list of this application.
     *
     * @param value filter value on field "account_dbid".
     */
    public final void setAccountDbid(final int value) {
        setProperty("account_dbid", value);
    }

    /**
     * A unique identifier of the
     * object that may be used as an account for a daemon application (see
     * type CfgObjectType). Makes sense only if used
     * with the filter account_type (see below). Makes sense
     * only if used with the filter account_dbid (see below).
     * If both account_type and account_dbid
     * are specified, Configuration Server will return information only
     * about the applications associated with this account. Such information
     * will only be provided to the clients that have privileges to read
     * access control list of this application.
     *
     * @return filter value or null (if applicable)
     * @see #setAccountDbid(int)
     */
    public final int getAccountDbid() {
        return (getInt("account_dbid"));
    }

    /**
     * A unique identifier of a host.
     * If specified, Configuration Server will return information only
     * about the applications currently assigned to this host.
     *
     * @param value filter value on field "host_dbid".
     */
    public final void setHostDbid(final int value) {
        setProperty("host_dbid", value);
    }

    /**
     * A unique identifier of a host.
     * If specified, Configuration Server will return information only
     * about the applications currently assigned to this host.
     *
     * @return filter value or null (if applicable)
     * @see #setHostDbid(int)
     */
    public final int getHostDbid() {
        return (getInt("host_dbid"));
    }

    /**
     * A server communication port. If specified, Configuration
     * Server will return information only about the applications currently
     * registered at ports with this number. Consider using this filter
     * with filter host_dbid (see above).
     *
     * @param value filter value on field "port".
     */
    public final void setPort(final int value) {
        setProperty("port", value);
    }

    /**
     * A server communication port. If specified, Configuration
     * Server will return information only about the applications currently
     * registered at ports with this number. Consider using this filter
     * with filter host_dbid (see above).
     *
     * @return filter value or null (if applicable)
     * @see #setPort(int)
     */
    public final int getPort() {
        return (getInt("port"));
    }

    /**
     * Current state of an application (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about applications that are currently
     * in this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of an application (see
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about applications that are currently
     * in this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * Name of an application. Shall be
     * specified as a character string. If specified, Configuration Server
     * will return information only about the application with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Name of an application. Shall be
     * specified as a character string. If specified, Configuration Server
     * will return information only about the application with that name.
     *
     * @return filter value or null (if applicable)
     * @see #setName(String)
     */
    public final String getName() {
        return (getString("name"));
    }

    /**
     * A unique identifier of an application.
     * If specified, Configuration Server will return information only
     * about this application.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of an application.
     * If specified, Configuration Server will return information only
     * about this application.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }

    /**
     * Configuration Server will return information only about applications
     * currently registered on same host and port. Filter is intended to
     * avoid the configuration collisions.
     *
     * @param value filter value on field "same_host_and_port".
     */
    public final void setSameHostAndPort(final int value) {
        setProperty("same_host_and_port", value);
    }

    /**
     * Configuration Server will return information only about applications
     * currently registered on same host and port. Filter is intended to
     * avoid the configuration collisions.
     *
     * @return filter value or null (if applicable)
     * @see #setSameHostAndPort(int)
     */
    public final int getSameHostAndPort() {
        return (getInt("same_host_and_port"));
    }

    /**
     * A unique identifier of the
     * switch. If specified, Configuration Server will return information
     * only about T-Servers/HAProxies associated with this switch (see
     * flexibleProperties above). The filter makes
     * sense for application types T-Server and High Availability Proxy
     * (CFGTServer; CFGHAProxy.)
     *
     * @param value filter value on field "switch_dbid".
     */
    public final void setSwitchDbid(final int value) {
        setProperty("switch_dbid", value);
    }

    /**
     * A unique identifier of the
     * switch. If specified, Configuration Server will return information
     * only about T-Servers/HAProxies associated with this switch (see
     * flexibleProperties above). The filter makes
     * sense for application types T-Server and High Availability Proxy
     * (CFGTServer; CFGHAProxy.)
     *
     * @return filter value or null (if applicable)
     * @see #setSwitchDbid(int)
     */
    public final int getSwitchDbid() {
        return (getInt("switch_dbid"));
    }

    /**
     * A version of the application. Shall be
     * specified as a character string. If specified, Configuration
     * Server will return information only about applications with
     * that version.
     *
     * @param value filter value on field "version".
     */
    public final void setVersion(final String value) {
        setProperty("version", value);
    }

    /**
     * A version of the application. Shall be
     * specified as a character string. If specified, Configuration
     * Server will return information only about applications with
     * that version.
     *
     * @return filter value or null (if applicable)
     * @see #setVersion(String)
     */
    public final String getVersion() {
        return (getString("version"));
    }

    /**
     * If specified, Configuration Server will return information
     * only about T-Servers/ HAProxies that are not associated with any
     * switches (see
     * flexibleProperties above). The filter makes
     * sense for application types T-Server and High Availability Proxy
     * ( CFGTServer; CFGHAProxy.)
     *
     * @param value filter value on field "no_switch_dbid".
     */
    public final void setNoSwitchDbid(final int value) {
        setProperty("no_switch_dbid", value);
    }

    /**
     * If specified, Configuration Server will return information
     * only about T-Servers/ HAProxies that are not associated with any
     * switches (see
     * flexibleProperties above). The filter makes
     * sense for application types T-Server and High Availability Proxy
     * ( CFGTServer; CFGHAProxy.)
     *
     * @return filter value or null (if applicable)
     * @see #setNoSwitchDbid(int)
     */
    public final int getNoSwitchDbid() {
        return (getInt("no_switch_dbid"));
    }

    /**
     * If specified, Configuration Server will return information
     * only about applications/servers which do not have any clients (there
     * is no connection to this applications) configured.
     *
     * @param value filter value on field "no_client_dbid".
     */
    public final void setNoClientDbid(final int value) {
        setProperty("no_client_dbid", value);
    }

    /**
     * If specified, Configuration Server will return information
     * only about applications/servers which do not have any clients (there
     * is no connection to this applications) configured.
     *
     * @return filter value or null (if applicable)
     * @see #setNoClientDbid(int)
     */
    public final int getNoClientDbid() {
        return (getInt("no_client_dbid"));
    }

    /**
     * Startup type of the application (see
     * CfgStartupType
     * ). If specified, Configuration Server will
     * return information only about the applications of this startup type.
     *
     * @param value filter value on field "startup_type".
     */
    public final void setStartupType(final CfgStartupType value) {
        setProperty("startup_type", value);
    }

    /**
     * Startup type of the application (see
     * CfgStartupType
     * ). If specified, Configuration Server will
     * return information only about the applications of this startup type.
     *
     * @return filter value or null (if applicable)
     * @see #setStartupType(CfgStartupType)
     */
    public final CfgStartupType getStartupType() {
        return (CfgStartupType) CfgStartupType.getValue(CfgStartupType.class, getInt("startup_type"));
    }
}
