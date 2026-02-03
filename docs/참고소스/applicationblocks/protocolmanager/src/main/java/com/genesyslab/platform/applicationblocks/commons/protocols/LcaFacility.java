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

import com.genesyslab.platform.commons.connection.tls.SSLExtendedOptions;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.management.protocol.LocalControlAgentProtocol;

import javax.net.ssl.SSLContext;
import java.net.URI;


final class LcaFacility
        extends ProtocolFacility {

    public Protocol createProtocol(final String name, final URI uri,
			final boolean tlsEnabled, final SSLContext sslContext, final SSLExtendedOptions sslOptions) {
        try {
            return new LocalControlAgentProtocol(new Endpoint(name, uri, tlsEnabled, sslContext, sslOptions));
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
    }


    public void applyConfiguration(
                final ProtocolInstance instance, final ProtocolConfiguration conf) {
        super.applyConfiguration(instance, conf);

        LcaConfiguration lcaConf = (LcaConfiguration) conf;
        LocalControlAgentProtocol lcaProtocol =
                    (LocalControlAgentProtocol) instance.getProtocol();

        if (lcaConf.getClientName() != null) {
            lcaProtocol.setClientName(lcaConf.getClientName());
        }
        if (lcaConf.getClientId() != null) {
            lcaProtocol.setClientId(lcaConf.getClientId());
        }
        if (lcaConf.getProcessId() != null) {
            lcaProtocol.setProcessId(lcaConf.getProcessId());
        }
        if (lcaConf.getExecutionMode() != null) {
            lcaProtocol.setExecutionMode(lcaConf.getExecutionMode());
        }
        if (lcaConf.getControlStatus() != null) {
            lcaProtocol.setControlStatus(lcaConf.getControlStatus());
        }
    }
}
