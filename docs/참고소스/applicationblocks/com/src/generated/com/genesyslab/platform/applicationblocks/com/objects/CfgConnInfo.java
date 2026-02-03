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
 * <code>CfgConnInfo</code> contains information about a connection.
 *
 * <br/><b>Note:</b><br/>
 * If client and server exchange large processing instructions, that is,
 * packets larger than 1Mbyte, the values for <code>timeoutLocal</code> and
 * <code>timeoutRemote</code> for this connection should not be set to
 * less than 3 seconds. Otherwise, the connection library will be forced to
 * disconnect the client.<br/>
 */
@SuppressWarnings("unused")
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:19-07:00")
public class CfgConnInfo extends CfgStructure {
    /**
     * This constructor is intended for creation of structures from external or imported XML data.
     * It is internally used by COM AB for objects and structures deserialization.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol structure data
     * @param parent instance of configuration object as a parent for this structure instance
     */
    public CfgConnInfo(
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
    public CfgConnInfo(
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
    public CfgConnInfo(final IConfService confService, final ICfgObject parent) {
        super(confService, new ConfStructure(
                (CfgDescriptionStructure) confService.getMetaData()
                        .getCfgClass("CfgConnectionInfo")), parent);
    }


    @SuppressWarnings("unchecked")
    final void checkPropertiesForSave() throws ConfigException {
    }

    /**
     * The unique identifier of the <code>
     * <a href="CfgApplication.html">Server</a>
     * </code> this application shall
     * connect to as a client when it is started.
     *
     * @return instance of referred object or null
     */
    public final CfgApplication getAppServer() {
        return (CfgApplication) getProperty(CfgApplication.class, "appServerDBID");
    }

    /**
     * The unique identifier of the <code>
     * <a href="CfgApplication.html">Server</a>
     * </code> this application shall
     * connect to as a client when it is started.
     *
     * @param value new property value
     * @see #getAppServer()
     */
    public final void setAppServer(final CfgApplication value) {
        setProperty("appServerDBID", value);
    }

    /**
     * The unique identifier of the <code>
     * <a href="CfgApplication.html">Server</a>
     * </code> this application shall
     * connect to as a client when it is started.
     *
     * @param dbid DBID identifier of referred object
     * @see #getAppServer()
     */
    public final void setAppServerDBID(final int dbid) {
        setProperty("appServerDBID", dbid);
    }

    /**
     * Retrieves the dbid of the object that is being linked to by the AppServer property.
     *
     * @return DBID identifier of referred object or null
     */
    public final Integer getAppServerDBID() {
        return getLinkValue("appServerDBID");
    }

    /**
     * A pointer to the name of the
     * connection control protocol. Available values: addp. Default: none.
     *
     * @return property value or null
     */
    public final String getConnProtocol() {
        return (String) getProperty("connProtocol");
    }

    /**
     * A pointer to the name of the
     * connection control protocol. Available values: addp. Default: none.
     *
     * @param value new property value
     * @see #getConnProtocol()
     */
    public final void setConnProtocol(final String value) {
        setProperty("connProtocol", value);
    }

    /**
     * The heart-bit polling interval
     * measured in seconds, on client site.
     *
     * @return property value or null
     */
    public final Integer getTimoutLocal() {
        return (Integer) getProperty("timoutLocal");
    }

    /**
     * The heart-bit polling interval
     * measured in seconds, on client site.
     *
     * @param value new property value
     * @see #getTimoutLocal()
     */
    public final void setTimoutLocal(final Integer value) {
        setProperty("timoutLocal", value);
    }

    /**
     * The heart-bit polling interval
     * measured in seconds, on server site.
     *
     * @return property value or null
     */
    public final Integer getTimoutRemote() {
        return (Integer) getProperty("timoutRemote");
    }

    /**
     * The heart-bit polling interval
     * measured in seconds, on server site.
     *
     * @param value new property value
     * @see #getTimoutRemote()
     */
    public final void setTimoutRemote(final Integer value) {
        setProperty("timoutRemote", value);
    }

    /**
     * The trace mode dedicated for this
     * connection. Refer to <code>
     * <a href="../Enumerations/CfgTraceMode.html">CfgTraceMode</a>
     * </code> below. Default value: CFGTMNoTraceMode.
     *
     * @return property value or null
     */
    public final CfgTraceMode getMode() {
        return (CfgTraceMode) getProperty(CfgTraceMode.class, "mode");
    }

    /**
     * The trace mode dedicated for this
     * connection. Refer to <code>
     * <a href="../Enumerations/CfgTraceMode.html">CfgTraceMode</a>
     * </code> below. Default value: CFGTMNoTraceMode.
     *
     * @param value new property value
     * @see #getMode()
     */
    public final void setMode(final CfgTraceMode value) {
        setProperty("mode", value);
    }

    /**
     * An identifier of the server's listening port. Should correspond to <code>CfgPortInfo.id</code>.
     *
     * @return property value or null
     */
    public final String getId() {
        return (String) getProperty("id");
    }

    /**
     * An identifier of the server's listening port. Should correspond to <code>CfgPortInfo.id</code>.
     *
     * @param value new property value
     * @see #getId()
     */
    public final void setId(final String value) {
        setProperty("id", value);
    }

    /**
     * Connection protocol's transport parameters.
     *
     * @return property value or null
     */
    public final String getTransportParams() {
        return (String) getProperty("transportParams");
    }

    /**
     * Connection protocol's transport parameters.
     *
     * @param value new property value
     * @see #getTransportParams()
     */
    public final void setTransportParams(final String value) {
        setProperty("transportParams", value);
    }

    /**
     * Connection protocol's application parameters.
     *
     * @return property value or null
     */
    public final String getAppParams() {
        return (String) getProperty("appParams");
    }

    /**
     * Connection protocol's application parameters.
     *
     * @param value new property value
     * @see #getAppParams()
     */
    public final void setAppParams(final String value) {
        setProperty("appParams", value);
    }

    /**
     * Connection protocol's proxy parameters.
     *
     * @return property value or null
     */
    public final String getProxyParams() {
        return (String) getProperty("proxyParams");
    }

    /**
     * Connection protocol's proxy parameters.
     *
     * @param value new property value
     * @see #getProxyParams()
     */
    public final void setProxyParams(final String value) {
        setProperty("proxyParams", value);
    }

    /**
     * Optional description of the connection.
     *
     * @return property value or null
     */
    public final String getDescription() {
        return (String) getProperty("description");
    }

    /**
     * Optional description of the connection.
     *
     * @param value new property value
     * @see #getDescription()
     */
    public final void setDescription(final String value) {
        setProperty("description", value);
    }

    /**
     * Optional text field #1.
     *
     * @return property value or null
     */
    public final String getCharField1() {
        return (String) getProperty("charField1");
    }

    /**
     * Optional text field #1.
     *
     * @param value new property value
     * @see #getCharField1()
     */
    public final void setCharField1(final String value) {
        setProperty("charField1", value);
    }

    /**
     * Optional text field #2.
     *
     * @return property value or null
     */
    public final String getCharField2() {
        return (String) getProperty("charField2");
    }

    /**
     * Optional text field #2.
     *
     * @param value new property value
     * @see #getCharField2()
     */
    public final void setCharField2(final String value) {
        setProperty("charField2", value);
    }

    /**
     * Optional text field #3.
     *
     * @return property value or null
     */
    public final String getCharField3() {
        return (String) getProperty("charField3");
    }

    /**
     * Optional text field #3.
     *
     * @param value new property value
     * @see #getCharField3()
     */
    public final void setCharField3(final String value) {
        setProperty("charField3", value);
    }

    /**
     * Optional text field #4.
     *
     * @return property value or null
     */
    public final String getCharField4() {
        return (String) getProperty("charField4");
    }

    /**
     * Optional text field #4.
     *
     * @param value new property value
     * @see #getCharField4()
     */
    public final void setCharField4(final String value) {
        setProperty("charField4", value);
    }

    /**
     * Optional integer field #1.
     *
     * @return property value or null
     */
    public final Integer getLongField1() {
        return (Integer) getProperty("longField1");
    }

    /**
     * Optional integer field #1.
     *
     * @param value new property value
     * @see #getLongField1()
     */
    public final void setLongField1(final Integer value) {
        setProperty("longField1", value);
    }

    /**
     * Optional integer field #2.
     *
     * @return property value or null
     */
    public final Integer getLongField2() {
        return (Integer) getProperty("longField2");
    }

    /**
     * Optional integer field #2.
     *
     * @param value new property value
     * @see #getLongField2()
     */
    public final void setLongField2(final Integer value) {
        setProperty("longField2", value);
    }

    /**
     * Optional integer field #3.
     *
     * @return property value or null
     */
    public final Integer getLongField3() {
        return (Integer) getProperty("longField3");
    }

    /**
     * Optional integer field #3.
     *
     * @param value new property value
     * @see #getLongField3()
     */
    public final void setLongField3(final Integer value) {
        setProperty("longField3", value);
    }

    /**
     * Optional integer field #4.
     *
     * @return property value or null
     */
    public final Integer getLongField4() {
        return (Integer) getProperty("longField4");
    }

    /**
     * Optional integer field #4.
     *
     * @param value new property value
     * @see #getLongField4()
     */
    public final void setLongField4(final Integer value) {
        setProperty("longField4", value);
    }
}
