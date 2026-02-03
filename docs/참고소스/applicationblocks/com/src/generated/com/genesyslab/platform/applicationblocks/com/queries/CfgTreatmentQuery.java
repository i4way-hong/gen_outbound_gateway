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
import com.genesyslab.platform.applicationblocks.com.objects.CfgTreatment;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgTreatment object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgTreatment
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgTreatmentQuery extends CfgFilterBasedQuery<CfgTreatment> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgTreatment type. This query will not be executable.
     */
    public CfgTreatmentQuery() {
        super(CfgObjectType.CFGTreatment);
        setObjectClass(CfgTreatment.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgTreatment type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgTreatmentQuery(final IConfService service) {
        super(CfgObjectType.CFGTreatment, service);
        setObjectClass(CfgTreatment.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgTreatmentQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgTreatmentQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgTreatment object retrieved as a result of this operation
     */
    public CfgTreatment executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgTreatment.class);
    }

    /**
     * Executes the query and returns a list of CfgTreatment objects.
     *
     * @return A collection of CfgTreatment objects
     */
    public Collection<CfgTreatment> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgTreatment.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgTreatment> beginExecute(
                final Action<AsyncRequestResult<CfgTreatment>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgTreatment.class, callback, state);
    }

    /**
     * A unique identifier of the treatment.
     * If specified, configuration server will return information only
     * about this treatment.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of the treatment.
     * If specified, configuration server will return information only
     * about this treatment.
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
     * only about the treatment(s) that belong to this tenant.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of the
     * tenant. If specified, Configuration server will return information
     * only about the treatment(s) that belong to this tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * Name of a treatment. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the treatment(s) with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Name of a treatment. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the treatment(s) with that name.
     *
     * @return filter value or null (if applicable)
     * @see #setName(String)
     */
    public final String getName() {
        return (getString("name"));
    }

    /**
     * A call result related to this
     * treatment (See
     * GctiCallState
     * ). If specified, Configuration
     * Server will return information only about the treatments(s) with
     * that call result.
     *
     * @param value filter value on field "call_result".
     */
    public final void setCallResult(final GctiCallState value) {
        setProperty("call_result", value);
    }

    /**
     * A call result related to this
     * treatment (See
     * GctiCallState
     * ). If specified, Configuration
     * Server will return information only about the treatments(s) with
     * that call result.
     *
     * @return filter value or null (if applicable)
     * @see #setCallResult(GctiCallState)
     */
    public final GctiCallState getCallResult() {
        return (GctiCallState) GctiCallState.getValue(GctiCallState.class, getInt("call_result"));
    }

    /**
     * A record action code(See
     * CfgRecActionCode
     * ).
     * If specified, Configuration Server will return information only
     * about the treatments(s) with that record action code.
     *
     * @param value filter value on field "rec_action_code".
     */
    public final void setRecActionCode(final CfgRecActionCode value) {
        setProperty("rec_action_code", value);
    }

    /**
     * A record action code(See
     * CfgRecActionCode
     * ).
     * If specified, Configuration Server will return information only
     * about the treatments(s) with that record action code.
     *
     * @return filter value or null (if applicable)
     * @see #setRecActionCode(CfgRecActionCode)
     */
    public final CfgRecActionCode getRecActionCode() {
        return (CfgRecActionCode) CfgRecActionCode.getValue(CfgRecActionCode.class, getInt("rec_action_code"));
    }

    /**
     * A unique identifier of destination
     * dn. If specified, Configuration Server will return information only
     * about the treatments(s) with that destination dn specified.
     *
     * @param value filter value on field "dest_dn_dbid".
     */
    public final void setDestDnDbid(final int value) {
        setProperty("dest_dn_dbid", value);
    }

    /**
     * A unique identifier of destination
     * dn. If specified, Configuration Server will return information only
     * about the treatments(s) with that destination dn specified.
     *
     * @return filter value or null (if applicable)
     * @see #setDestDnDbid(int)
     */
    public final int getDestDnDbid() {
        return (getInt("dest_dn_dbid"));
    }

    /**
     * A call action code(See
     * CfgCallActionCode
     * ). If
     * specified, Configuration Server will return information only about
     * the treatments(s) with that call action code.
     *
     * @param value filter value on field "call_action_code".
     */
    public final void setCallActionCode(final CfgCallActionCode value) {
        setProperty("call_action_code", value);
    }

    /**
     * A call action code(See
     * CfgCallActionCode
     * ). If
     * specified, Configuration Server will return information only about
     * the treatments(s) with that call action code.
     *
     * @return filter value or null (if applicable)
     * @see #setCallActionCode(CfgCallActionCode)
     */
    public final CfgCallActionCode getCallActionCode() {
        return (CfgCallActionCode) CfgCallActionCode.getValue(CfgCallActionCode.class, getInt("call_action_code"));
    }

    /**
     * Current state of the table access
     * (See
     * CfgObjectState
     * ). If specified, Configuration
     * Server will return information only about the table access(s) that
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
     * Server will return information only about the table access(s) that
     * are currently in this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }
}
