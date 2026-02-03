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
import com.genesyslab.platform.applicationblocks.com.objects.CfgDN;
import com.genesyslab.platform.applicationblocks.com.AsyncRequestResult;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.configuration.protocol.types.*;

import javax.annotation.Generated;

import java.util.Collection;


/**
 * Class designed to construct queries to the Configuration Service for reading CfgDN object(s).
 *
 * @see com.genesyslab.platform.applicationblocks.com.ConfService
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgDN
 */
@SuppressWarnings({"unused", "unchecked"})
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public class CfgDNQuery extends CfgFilterBasedQuery<CfgDN> {

    /**
     * Create query object with default filter value for configuration objects
     * of CfgDN type. This query will not be executable.
     */
    public CfgDNQuery() {
        super(CfgObjectType.CFGDN);
        setObjectClass(CfgDN.class);
    }

    /**
     * Create query object with default filter value for configuration objects
     * of CfgDN type. If an instance of the configuration service
     * is provided, the query will be executable.
     *
     * @param service The configuration service to use when executing this query
     */
    public CfgDNQuery(final IConfService service) {
        super(CfgObjectType.CFGDN, service);
        setObjectClass(CfgDN.class);
    }

    /**
     * Create query object with filter initialized for the object DBID.
     * Note: if dbid is specified, Configuration Server ignores other filter keys.
     *
     * @param dbid value for filter on object dbid
     * @see #setDbid(int)
     */
    public CfgDNQuery(final int dbid) {
        this();
        setDbid(dbid);
    }

    /**
     * Create query object with filter initialized for object name value.
     *
     * @param name value for filter on object name
     * @see #setName(String)
     */
    public CfgDNQuery(final String name) {
        this();
        setName(name);
    }

    /**
     * Executes a query the result of which is a single object. Exception will
     * be thrown if multiple objects are returned by the configuration server.
     *
     * @return the CfgDN object retrieved as a result of this operation
     */
    public CfgDN executeSingleResult()
                    throws ConfigException {
        return super.executeSingleResult(CfgDN.class);
    }

    /**
     * Executes the query and returns a list of CfgDN objects.
     *
     * @return A collection of CfgDN objects
     */
    public Collection<CfgDN> execute()
                    throws ConfigException, InterruptedException {
        return super.execute(CfgDN.class);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     */
    public AsyncRequestResult<CfgDN> beginExecute(
                final Action<AsyncRequestResult<CfgDN>> callback,
                final Object state)
                    throws ConfigException {
        return super.beginExecute(CfgDN.class, callback, state);
    }

    /**
     * A unique identifier of a DN. If specified, Configuration Server
     * will return information only about this DN.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @param value filter value on field "dbid".
     */
    public final void setDbid(final int value) {
        setProperty("dbid", value);
    }

    /**
     * A unique identifier of a DN. If specified, Configuration Server
     * will return information only about this DN.
     * Note: if dbid is specified, Configuration Server ignores other filter keys
     *
     * @return filter value or null (if applicable)
     * @see #setDbid(int)
     */
    public final int getDbid() {
        return (getInt("dbid"));
    }

    /**
     * A unique identifier of a tenant. If specified, Configuration
     * Server will return information only about the DNs that belong to this
     * tenant.
     *
     * @param value filter value on field "tenant_dbid".
     */
    public final void setTenantDbid(final int value) {
        setProperty("tenant_dbid", value);
    }

    /**
     * A unique identifier of a tenant. If specified, Configuration
     * Server will return information only about the DNs that belong to this
     * tenant.
     *
     * @return filter value or null (if applicable)
     * @see #setTenantDbid(int)
     */
    public final int getTenantDbid() {
        return (getInt("tenant_dbid"));
    }

    /**
     * A unique identifier of a switch. If specified, Configuration
     * Server will return information only about the DNs that belong to this
     * switch.
     *
     * @param value filter value on field "switch_dbid".
     */
    public final void setSwitchDbid(final int value) {
        setProperty("switch_dbid", value);
    }

    /**
     * A unique identifier of a switch. If specified, Configuration
     * Server will return information only about the DNs that belong to this
     * switch.
     *
     * @return filter value or null (if applicable)
     * @see #setSwitchDbid(int)
     */
    public final int getSwitchDbid() {
        return (getInt("switch_dbid"));
    }

    /**
     * Type of the DN (see
     * CfgDNType
     * ).
     * If specified, Configuration Server will return information only
     * about the DNs of this type.
     *
     * @param value filter value on field "dn_type".
     */
    public final void setDnType(final CfgDNType value) {
        setProperty("dn_type", value);
    }

    /**
     * Type of the DN (see
     * CfgDNType
     * ).
     * If specified, Configuration Server will return information only
     * about the DNs of this type.
     *
     * @return filter value or null (if applicable)
     * @see #setDnType(CfgDNType)
     */
    public final CfgDNType getDnType() {
        return (CfgDNType) CfgDNType.getValue(CfgDNType.class, getInt("dn_type"));
    }

    /**
     * A unique identifier of a place. If specified, Configuration
     * Server will return information only about the DNs that are associated
     * with this place.
     *
     * @param value filter value on field "place_dbid".
     */
    public final void setPlaceDbid(final int value) {
        setProperty("place_dbid", value);
    }

    /**
     * A unique identifier of a place. If specified, Configuration
     * Server will return information only about the DNs that are associated
     * with this place.
     *
     * @return filter value or null (if applicable)
     * @see #setPlaceDbid(int)
     */
    public final int getPlaceDbid() {
        return (getInt("place_dbid"));
    }

    /**
     * Configuration Server will return information only about the DN(s)
     * that ) that are allowed to be assigned to the place and not associated
     * with any place.
     *
     * @param value filter value on field "no_place_dbid".
     */
    public final void setNoPlaceDbid(final int value) {
        setProperty("no_place_dbid", value);
    }

    /**
     * Configuration Server will return information only about the DN(s)
     * that ) that are allowed to be assigned to the place and not associated
     * with any place.
     *
     * @return filter value or null (if applicable)
     * @see #setNoPlaceDbid(int)
     */
    public final int getNoPlaceDbid() {
        return (getInt("no_place_dbid"));
    }

    /**
     * A unique identifier of the group of DNs.
     * If specified, Configuration Server will return information only
     * about the DNs that are associated with this group.
     *
     * @param value filter value on field "group_dbid".
     */
    public final void setGroupDbid(final int value) {
        setProperty("group_dbid", value);
    }

    /**
     * A unique identifier of the group of DNs.
     * If specified, Configuration Server will return information only
     * about the DNs that are associated with this group.
     *
     * @return filter value or null (if applicable)
     * @see #setGroupDbid(int)
     */
    public final int getGroupDbid() {
        return (getInt("group_dbid"));
    }

    /**
     * An entity associated with a DN. Shall be
     * specified as a character string. If specified, Configuration Server
     * will return information only about the DN(s) that are associated
     * with this entity.
     *
     * @param value filter value on field "association".
     */
    public final void setAssociation(final String value) {
        setProperty("association", value);
    }

    /**
     * An entity associated with a DN. Shall be
     * specified as a character string. If specified, Configuration Server
     * will return information only about the DN(s) that are associated
     * with this entity.
     *
     * @return filter value or null (if applicable)
     * @see #setAssociation(String)
     */
    public final String getAssociation() {
        return (getString("association"));
    }

    /**
     * Current state of a DN (see
     * CfgObjectState
     * ).
     * If specified, Configuration Server will return information only
     * about DNs that are currently in this state.
     *
     * @param value filter value on field "state".
     */
    public final void setState(final CfgObjectState value) {
        setProperty("state", value);
    }

    /**
     * Current state of a DN (see
     * CfgObjectState
     * ).
     * If specified, Configuration Server will return information only
     * about DNs that are currently in this state.
     *
     * @return filter value or null (if applicable)
     * @see #setState(CfgObjectState)
     */
    public final CfgObjectState getState() {
        return (CfgObjectState) CfgObjectState.getValue(CfgObjectState.class, getInt("state"));
    }

    /**
     * Directory number of a DN. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the DN(s) with that number.
     *
     * @param value filter value on field "dn_number".
     */
    public final void setDnNumber(final String value) {
        setProperty("dn_number", value);
    }

    /**
     * Directory number of a DN. Shall be specified
     * as a character string. If specified, Configuration Server will return
     * information only about the DN(s) with that number.
     *
     * @return filter value or null (if applicable)
     * @see #setDnNumber(String)
     */
    public final String getDnNumber() {
        return (getString("dn_number"));
    }

    /**
     * Name of a DN. Shall be specified as a character
     * string. If specified, Configuration Server will return information
     * only about the DN(s) with that name.
     *
     * @param value filter value on field "name".
     */
    public final void setName(final String value) {
        setProperty("name", value);
    }

    /**
     * Name of a DN. Shall be specified as a character
     * string. If specified, Configuration Server will return information
     * only about the DN(s) with that name.
     *
     * @return filter value or null (if applicable)
     * @see #setName(String)
     */
    public final String getName() {
        return (getString("name"));
    }

    /**
     * A unique identifier of an IVR object (see
     * CfgIVR
     * ). If
     * specified, Configuration Server will return information only about
     * the DN(s) which assigned to IVR Ports (see
     * CfgIVRPort
     * )
     * of this IVR object.
     *
     * @param value filter value on field "ivr_dbid".
     */
    public final void setIvrDbid(final int value) {
        setProperty("ivr_dbid", value);
    }

    /**
     * A unique identifier of an IVR object (see
     * CfgIVR
     * ). If
     * specified, Configuration Server will return information only about
     * the DN(s) which assigned to IVR Ports (see
     * CfgIVRPort
     * )
     * of this IVR object.
     *
     * @return filter value or null (if applicable)
     * @see #setIvrDbid(int)
     */
    public final int getIvrDbid() {
        return (getInt("ivr_dbid"));
    }
}
