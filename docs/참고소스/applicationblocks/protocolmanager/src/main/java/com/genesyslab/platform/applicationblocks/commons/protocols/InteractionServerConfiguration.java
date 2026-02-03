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

import com.genesyslab.platform.openmedia.protocol.InteractionServerProtocol;
import com.genesyslab.platform.openmedia.protocol.interactionserver.InteractionClient;
import com.genesyslab.platform.commons.collections.KeyValueCollection;


/**
 * Protocol Manager Application Block is deprecated.
 *
 * @see com.genesyslab.platform.openmedia.protocol.InteractionServerProtocolHandshakeOptions
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
@Deprecated
public final class InteractionServerConfiguration
        extends ProtocolConfiguration {

    private String clientName;
    private InteractionClient clientType;
    private Integer proxyId;
    private KeyValueCollection registrationExtensions;


    public InteractionServerConfiguration(final String name) {
        super(name, InteractionServerProtocol.class);
    }

    public InteractionServerConfiguration(
                final String name,
                final String host,
                final int port,
                final String inxClientName,
                final InteractionClient inxClientType) {
        this(name);

        setUri(host, port);

        setClientName(inxClientName);
        setClientType(inxClientType);
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(final String value) {
        clientName = value;
    }

    public InteractionClient getClientType() {
        return clientType;
    }

    public void setClientType(final InteractionClient value) {
        clientType = value;
    }

    public Integer getProxyId() {
        return proxyId;
    }

    public void setProxyId(final Integer value) {
        proxyId = value;
    }

    public KeyValueCollection getRegistrationExtensions() {
        return registrationExtensions;
    }

    public void setRegistrationExtensions(final KeyValueCollection value) {
        registrationExtensions = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());

        sb.append("ClientName: ").append(clientName).append("\n");
        sb.append("ClientType: ").append(clientType).append("\n");
        sb.append("ProxyId: ").append(proxyId).append("\n");

        sb.append("RegistrationExtensions: ").append(registrationExtensions).append("\n");

        return sb.toString();
    }
}
