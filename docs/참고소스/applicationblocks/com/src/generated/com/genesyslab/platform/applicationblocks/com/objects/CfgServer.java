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

package com.genesyslab.platform.applicationblocks.com.objects;

import com.genesyslab.platform.applicationblocks.com.*;
import com.genesyslab.platform.configuration.protocol.obj.*;
import com.genesyslab.platform.configuration.protocol.types.*;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionStructure;

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import javax.annotation.Generated;

import java.util.Calendar;
import java.util.Collection;

import org.w3c.dom.Node;


/**
 * <p/>
 * A server.
 *
 * <p/>
 * When an application is designated as a backup server
 * for another server, values of the following parameters of this application
 * will be automatically changed to match the values of the same parameters
 * of the primary server:
 * <ul type="bullet">
 * <li>
 * <code>appServerDBIDs</code>
 * </li>
 * <li>
 * <code>tenantDBIDs</code>
 * </li>
 * <li>
 * <code>flexibleProperties</code>.
 * </li>
 * </ul>
 * <p/>
 * As long as this application is associated with the primary server,
 * these parameters will be treated as read-only, and their values
 * will be changed only when changes are applied to the corresponding
 * parameters of the primary server.
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgServer extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgServer(
            final IConfService confService,
            final ConfStructure objData,
            final ICfgObject parent) {
        super(confService, objData, parent);
    }

    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param xmlData XML object containing structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgServer(
            final IConfService confService,
            final Node xmlData,
            final ICfgObject parent) {
        super(confService, xmlData, parent);
    }

    /**
     * This constructor is intended for creation of detached objects.
     *
     * @param confService configuration service instance
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgServer(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgServerInfo")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgHost.html">CfgHost</a>
     * </code>
     * where this server resides. Cannot be changed as long as the server
     * is associated with at least one client application or a primary
     * server.
     *
     * @return instance of referred object or null
     */
    public final CfgHost getHost() {
        return (CfgHost) getProperty(CfgHost.class, "hostDBID");
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgHost.html">CfgHost</a>
     * </code>
     * where this server resides. Cannot be changed as long as the server
     * is associated with at least one client application or a primary
     * server.
     *
     * @param value new property value
     * @see #getHost()
     */
    public final void setHost(final CfgHost value) {
        setProperty("hostDBID", value);
    }

    /**
     * A unique identifier of the <code>
     * <a href="CfgHost.html">CfgHost</a>
     * </code>
     * where this server resides. Cannot be changed as long as the server
     * is associated with at least one client application or a primary
     * server.
     *
     * @param dbid DBID identifier of referred object
     * @see #getHost()
     */
    public final void setHostDBID(final int dbid) {
        setProperty("hostDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the Host property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getHostDBID() {
        return getLinkValue("hostDBID");
    }

    /**
     * A pointer to the name of the port
     * which client applications should use to open communication sessions
     * to this server. Populated for backward compatibility purpose from <code>portInfos</code> list.
     *
     * @return property value or null
     */
    public final String getPort() {
        return (String) getProperty("port");
    }

    /**
     * A pointer to the name of the port
     * which client applications should use to open communication sessions
     * to this server. Populated for backward compatibility purpose from <code>portInfos</code> list.
     *
     * @param value new property value
     * @see #getPort()
     */
    public final void setPort(final String value) {
        setProperty("port", value);
    }

    /**
     * An identifier of the <code>
     * <a href="CfgApplication.html">Server</a>
     * </code> which is to be contacted if connection
     * to this server fails. The backup server must be associated with
     * the same account (see
     * <code>ConfSetAccount</code>) and have the same application
     * type(<code>CfgAppType</code>). One backup server cannot be associated with more
     * than one primary server. See comments.
     *
     * @return instance of referred object or null
     */
    public final CfgApplication getBackupServer() {
        return (CfgApplication) getProperty(CfgApplication.class, "backupServerDBID");
    }

    /**
     * An identifier of the <code>
     * <a href="CfgApplication.html">Server</a>
     * </code> which is to be contacted if connection
     * to this server fails. The backup server must be associated with
     * the same account (see
     * <code>ConfSetAccount</code>) and have the same application
     * type(<code>CfgAppType</code>). One backup server cannot be associated with more
     * than one primary server. See comments.
     *
     * @param value new property value
     * @see #getBackupServer()
     */
    public final void setBackupServer(final CfgApplication value) {
        setProperty("backupServerDBID", value);
    }

    /**
     * An identifier of the <code>
     * <a href="CfgApplication.html">Server</a>
     * </code> which is to be contacted if connection
     * to this server fails. The backup server must be associated with
     * the same account (see
     * <code>ConfSetAccount</code>) and have the same application
     * type(<code>CfgAppType</code>). One backup server cannot be associated with more
     * than one primary server. See comments.
     *
     * @param dbid DBID identifier of referred object
     * @see #getBackupServer()
     */
    public final void setBackupServerDBID(final int dbid) {
        setProperty("backupServerDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the BackupServer property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getBackupServerDBID() {
        return getLinkValue("backupServerDBID");
    }

    /**
     * Time-out in seconds that the application
     * should run before making a re-connect attempt after a communication
     * session with this server has failed. May not be set to a negative
     * value. Recommended to be set to <code>10</code> by default.
     *
     * @return property value or null
     */
    public final Integer getTimeout() {
        return (Integer) getProperty("timeout");
    }

    /**
     * Time-out in seconds that the application
     * should run before making a re-connect attempt after a communication
     * session with this server has failed. May not be set to a negative
     * value. Recommended to be set to <code>10</code> by default.
     *
     * @param value new property value
     * @see #getTimeout()
     */
    public final void setTimeout(final Integer value) {
        setProperty("timeout", value);
    }

    /**
     * Number of attempts to connect
     * to this server before trying to connect to the backup server. Makes
     * sense only if <code>backupServerDBID</code> is specified. May not be
     * set to a negative value. Recommended to be set to <code>1</code> by default.
     *
     * @return property value or null
     */
    public final Integer getAttempts() {
        return (Integer) getProperty("attempts");
    }

    /**
     * Number of attempts to connect
     * to this server before trying to connect to the backup server. Makes
     * sense only if <code>backupServerDBID</code> is specified. May not be
     * set to a negative value. Recommended to be set to <code>1</code> by default.
     *
     * @param value new property value
     * @see #getAttempts()
     */
    public final void setAttempts(final Integer value) {
        setProperty("attempts", value);
    }
}
