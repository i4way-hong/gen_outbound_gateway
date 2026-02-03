// ===============================================================================
//
//  Any authorized distribution of any copy of this code (including any related
//  documentation) must reproduce the following restrictions, disclaimer and copyright
//  notice:
//
//  The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
//  (even as a part of another name), endorse and/or promote products derived from
//  this code without prior written permission from Genesys Telecommunications
//  Laboratories, Inc.
//
//  The use, copy, and/or distribution of this code is subject to the terms of the Genesys
//  Developer License Agreement.  This code shall not be used, copied, and/or
//  distributed under any other license agreement.
//
//  THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
//  ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
//  DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
//  WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
//  NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
//  NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
//  EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
//  CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
//  NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
//  Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.applicationblocks.commons.protocols;

import com.genesyslab.platform.contacts.protocol.UniversalContactServerProtocol;


/**
 * Protocol Manager Application Block is going to be deprecated.
 *
 * @see com.genesyslab.platform.contacts.protocol.UniversalContactServerProtocolHandshakeOptions
 * @see com.genesyslab.platform.commons.connection.configuration.ClientConnectionOptions
 * @see com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions
 * @see com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration
 * @see com.genesyslab.platform.commons.connection.configuration.KeyValueConfiguration
 * @see com.genesyslab.platform.commons.protocol.Endpoint
 * @see com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration
 * @see com.genesyslab.platform.apptemplate.configuration.ClientConfigurationHelper
 * @deprecated
 * Use {@link com.genesyslab.platform.commons.protocol.Endpoint Endpoint} to manage protocol's configuration.
 */
public class UcsConfiguration extends ProtocolConfiguration {

    private String clientName;
    private String clientType = null;
    private Boolean useUtfForRequests = null;
    private Boolean useUtfForResponses = null;

    public UcsConfiguration(final String name) {
        super(name, UniversalContactServerProtocol.class);
    }

    /**
     * Returns client name for the Universal Contact Server connection handshake.
     * Usually it represents application name of this Universal Contact Server client.
     *
     * @return client name
     * @see #setClientName(String)
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Sets the Universal Contact Server client name for connection handshake procedure.
     * Usually it represents application name of this Universal Contact Server client.
     *
     * @param clientName client name
     */
    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    /**
     * Returns client application type for the Universal Contact Server connection.
     * Usually it represents application type in Configuration Server.
     *
     * @return client application type
     * @see #setClientApplicationType(String)
     */
    public String getClientApplicationType() {
        return clientType;
    }

    /**
     * Sets client application type for the Universal Contact Server connection.
     * Usually it represents application type in Configuration Server.
     *
     * @param clientAppType client application type
     */
    public void setClientApplicationType(final String clientAppType) {
        this.clientType = clientAppType;
    }

    /**
     * Optional configuration option. If set to true, all string values of each KVlist will be packed
     * as UtfStrings (in "UTF-16BE" encoding), instead of common strings. It is true by default.
     *
     * @return the useUtfForRequests
     */
    public Boolean getUseUtfForRequests() {
        return useUtfForRequests;
    }

    /**
     * Optional configuration option. If set to true, all string values of each KVlist will be packed
     * as UtfStrings (in "UTF-16BE" encoding), instead of common strings. It is true by default.
     *
     * @param useUtfForRequests the useUtfForRequests to set
     */
    public void setUseUtfForRequests(final Boolean useUtfForRequests) {
        this.useUtfForRequests = useUtfForRequests;
    }

    /**
     * Optional configuration option. If set to false, UCSprotocol will add 'tkv.multibytes'='false' pair
     * in Request KVlist of the message. It is false by default.
     *
     * @return the useUtfForResponses
     */
    public Boolean getUseUtfForResponses() {
        return useUtfForResponses;
    }

    /**
     * Optional configuration option. If set to false, UCSprotocol will add 'tkv.multibytes'='false' pair
     * in Request KVlist of the message. It is false by default.
     *
     * @param useUtfForResponses the useUtfForResponses to set
     */
    public void setUseUtfForResponses(final Boolean useUtfForResponses) {
        this.useUtfForResponses = useUtfForResponses;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ClientName: ").append(clientName).append("\n");
        if (clientType != null) {
            sb.append("ClientType: ").append(clientType).append("\n");
        }
        if (useUtfForRequests != null) {
            sb.append("UseUtfForRequests: ").append(useUtfForRequests).append("\n");
        }
        if (useUtfForResponses != null) {
            sb.append("UseUtfForResponses: ").append(useUtfForResponses).append("\n");
        }
        return sb.toString();
    }
}
