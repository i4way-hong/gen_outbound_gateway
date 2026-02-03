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
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgPerson object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgPerson
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgPersonQuery extends CfgFilterBasedQuery<CfgPerson> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgPerson type. This query will not be executable.
     */
    public CfgPersonQuery() {
        super(CfgObjectType.CFGPerson);
        setObjectClass(CfgPerson.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgPerson type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgPersonQuery(final IConfService service) {
        super(CfgObjectType.CFGPerson, service);
        setObjectClass(CfgPerson.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgPersonQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgPerson object retrieved as a result of this operation
     */
    public CfgPerson executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgPerson.class);
    }

    /**
     * Executes the query and returns a list of CfgPerson objects.
     *
     * @return A collection of CfgPerson objects
     */
    public Collection<CfgPerson> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgPerson.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgPerson> beginExecute(
                final Action<AsyncRequestResult<CfgPerson>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgPerson.class, callback, state);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the persons that belong to this tenant.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of a tenant.
     * If specified, Configuration Server will return information only
     * about the persons that belong to this tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * Indicator of whether a person
     * is an agent. If set to CFGTrue, Configuration Server will return
     * information only about the persons who are agents. If set to CFGFalse,
     * Configuration Server will return information only about the persons
     * who are not agents.
     *
     * @param value filter value on field "is_agent".
     */
    public final void setIsAgent(final int value) {
        setProperty("is_agent", value);
    }

    /**
     * Indicator of whether a person
     * is an agent. If set to CFGTrue, Configuration Server will return
     * information only about the persons who are agents. If set to CFGFalse,
     * Configuration Server will return information only about the persons
     * who are not agents.
     *
     * @return filter value or null (if applicable)
     * @see #setIsAgent(int)
     */
    public final int getIsAgent() {
        return (getInt("is_agent"));
    }

    /**
     * A unique identifier of a skill.
     * If specified, Configuration Server will return information only
     * about the agents who have this skill.
     *
     * @param value filter value on field "skill_dbid".
     */
    public final void setSkillDbid(final int value) {
        setProperty("skill_dbid", value);
    }

    /**
     * A unique identifier of a skill.
     * If specified, Configuration Server will return information only
     * about the agents who have this skill.
     *
     * @return filter value or null (if applicable)
     * @see #setSkillDbid(int)
     */
    public final int getSkillDbid() {
        return (getInt("skill_dbid"));
    }

    /**
     * A unique identifier of an agent
     * group. If specified, Configuration Server will return information
     * only about the agents who form this group.
     *
     * @param value filter value on field "group_dbid".
     */
    public final void setGroupDbid(final int value) {
        setProperty("group_dbid", value);
    }

    /**
     * A unique identifier of an agent
     * group. If specified, Configuration Server will return information
     * only about the agents who form this group.
     *
     * @return filter value or null (if applicable)
     * @see #setGroupDbid(int)
     */
    public final int getGroupDbid() {
        return (getInt("group_dbid"));
    }

    /**
     * Current state of a person (see type
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about persons that are currently in
     * this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of a person (see type
     * CfgObjectState
     * ). If specified, Configuration Server
     * will return information only about persons that are currently in
     * this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * Employee ID of a person. Shall
     * be specified as a character string. If specified, Configuration
     * Server will return information only about the person(s) with this
     * employee ID.
     *
     * @param value filter value on field "employee_id".
     */
    public final void setEmployeeId(final String value) {
        setProperty("employee_id", value);
    }

    /**
     * Employee ID of a person. Shall
     * be specified as a character string. If specified, Configuration
     * Server will return information only about the person(s) with this
     * employee ID.
     *
     * @return filter value or null (if applicable)
     * @see #setEmployeeId(String)
     */
    public final String getEmployeeId() {
        return (getString("employee_id"));
    }

    /**
     * A unique identifier of an agent
     * login. If specified, Configuration Server will return information
     * only about the agent this login is currently assigned to.
     *
     * @param value filter value on field "login_dbid".
     */
    public final void setLoginDbid(final int value) {
        setProperty("login_dbid", value);
    }

    /**
     * A unique identifier of an agent
     * login. If specified, Configuration Server will return information
     * only about the agent this login is currently assigned to.
     *
     * @return filter value or null (if applicable)
     * @see #setLoginDbid(int)
     */
    public final int getLoginDbid() {
        return (getInt("login_dbid"));
    }

    /**
     * User name of a person. Shall
     * be specified as a character string. If specified, Configuration
     * Server will return information only about the person with this user
     * name.
     *
     * @param value filter value on field "user_name".
     */
    public final void setUserName(final String value) {
        setProperty("user_name", value);
    }

    /**
     * User name of a person. Shall
     * be specified as a character string. If specified, Configuration
     * Server will return information only about the person with this user
     * name.
     *
     * @return filter value or null (if applicable)
     * @see #setUserName(String)
     */
    public final String getUserName() {
        return (getString("user_name"));
    }

    /**
     * A unique identifier of a person. If
     * specified, Configuration Server will return information only about
     * this person.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of a person. If
     * specified, Configuration Server will return information only about
     * this person.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }

    /**
     * Configuration Server will
     * return information only about the agent(s) without login is currently
     * assigned to.
     *
     * @param value filter value on field "no_login_dbid".
     */
    public final void setNoLoginDbid(final int value) {
        setProperty("no_login_dbid", value);
    }

    /**
     * Configuration Server will
     * return information only about the agent(s) without login is currently
     * assigned to.
     *
     * @return filter value or null (if applicable)
     * @see #setNoLoginDbid(int)
     */
    public final int getNoLoginDbid() {
        return (getInt("no_login_dbid"));
    }

    /**
     * Configuration Server will return the information only about
     * the agents that do not have default places associated with.
     *
     * @param value filter value on field "no_place_dbid".
     */
    public final void setNoPlaceDbid(final int value) {
        setProperty("no_place_dbid", value);
    }

    /**
     * Configuration Server will return the information only about
     * the agents that do not have default places associated with.
     *
     * @return filter value or null (if applicable)
     * @see #setNoPlaceDbid(int)
     */
    public final int getNoPlaceDbid() {
        return (getInt("no_place_dbid"));
    }

    /**
     * The name of a person. Shall
     * be specified as a character string. If specified, Configuration
     * Server will return information only about the person with this name.
     *
     * @param value filter value on field "first_name".
     */
    public final void setFirstName(final String value) {
        setProperty("first_name", value);
    }

    /**
     * The name of a person. Shall
     * be specified as a character string. If specified, Configuration
     * Server will return information only about the person with this name.
     *
     * @return filter value or null (if applicable)
     * @see #setFirstName(String)
     */
    public final String getFirstName() {
        return (getString("first_name"));
    }

    /**
     * The last name of a person. Shall
     * be specified as a character string. If specified, Configuration
     * Server will return information only about the person with this last
     * name.
     *
     * @param value filter value on field "last_name".
     */
    public final void setLastName(final String value) {
        setProperty("last_name", value);
    }

    /**
     * The last name of a person. Shall
     * be specified as a character string. If specified, Configuration
     * Server will return information only about the person with this last
     * name.
     *
     * @return filter value or null (if applicable)
     * @see #setLastName(String)
     */
    public final String getLastName() {
        return (getString("last_name"));
    }

    /**
     * A unique identifier of a Switch.
     * If specified, Configuration Server will return information only
     * about the agent(s) that have associated Agent Logins belonged to
     * that Switch.
     *
     * @param value filter value on field "switch_dbid".
     */
    public final void setSwitchDbid(final int value) {
        setProperty("switch_dbid", value);
    }

    /**
     * A unique identifier of a Switch.
     * If specified, Configuration Server will return information only
     * about the agent(s) that have associated Agent Logins belonged to
     * that Switch.
     *
     * @return filter value or null (if applicable)
     * @see #setSwitchDbid(int)
     */
    public final int getSwitchDbid() {
        return (getInt("switch_dbid"));
    }
}
