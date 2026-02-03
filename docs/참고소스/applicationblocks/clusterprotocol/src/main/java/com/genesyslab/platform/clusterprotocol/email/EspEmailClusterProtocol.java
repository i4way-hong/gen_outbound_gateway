// ===============================================================================
//  Genesys Platform SDK Application Blocks
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
//  Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.clusterprotocol.email;

import com.genesyslab.platform.clusterprotocol.ProtocolBuilder;
import com.genesyslab.platform.clusterprotocol.ClusterProtocolImpl;
import com.genesyslab.platform.clusterprotocol.ClusterProtocolPolicy;
import com.genesyslab.platform.clusterprotocol.lb.ClusterProtocolLoadBalancer;

import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.openmedia.protocol.externalservice.RequestorInfo;
import com.genesyslab.platform.openmedia.protocol.externalservice.RequestorInfoSupport;
import com.genesyslab.platform.webmedia.protocol.EspEmailProtocol;
import com.genesyslab.platform.webmedia.protocol.EspEmailProtocolHandshakeOptions;


/**
 * Cluster Protocol for ESP Email Server protocol.
 * It may be instantiated with the cluster protocol builder
 * {@link EspEmailClusterProtocolBuilder}.
 *
 * @see EspEmailClusterProtocolBuilder
 * @see EspEmailProtocol
 * @see com.genesyslab.platform.webmedia.protocol.espemail.requests
 */
public class EspEmailClusterProtocol
        extends ClusterProtocolImpl
                <EspEmailProtocol,
                 EspEmailProtocolBuilder>
        implements EspEmailProtocolHandshakeOptions {

    private String clientName = null;
    private String clientAppType = null;
    private RequestorInfo requestorInfo;

    EspEmailClusterProtocol(
            final ProtocolBuilder<EspEmailProtocol, EspEmailProtocolBuilder> protoBuilder,
            final ClusterProtocolPolicy protocolPolicy,
            final ClusterProtocolLoadBalancer loadBalancer) {
        super(protoBuilder != null ? protoBuilder : new EspEmailProtocolBuilder(),
              protocolPolicy, loadBalancer);
    }


    @Override
    public void setClientName(final String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String getClientName() {
        return clientName;
    }

    @Override
    public void setClientApplicationType(final String clientAppType) {
        this.clientAppType = clientAppType;
    }

    @Override
    public String getClientApplicationType() {
        return clientAppType;
    }


    @Override
    protected EspEmailProtocol createProtocol() {
        final EspEmailProtocol protocol = super.createProtocol();
        protocol.setClientName(clientName);
        protocol.setClientApplicationType(clientAppType);
        if (requestorInfo!=null)
            protocol.setRequestorInfo(requestorInfo);
        return protocol;
    }

    /**
     * Gets assigned requestor info information.
     * @return {@link RequestorInfo} instance or null if it isn't set.
     */
    public RequestorInfo getRequestorInfo(){
        return requestorInfo;
    }

    /**
     * Sets requestor info information.
     * @param info {@link RequestorInfo} instance or null to clear.
     */
    public void setRequestorInfo(RequestorInfo info){
        requestorInfo = info;
        for (Protocol protocol:getAllNodesProtocols()){
            if (protocol instanceof RequestorInfoSupport)
                ((RequestorInfoSupport)protocol).setRequestorInfo(requestorInfo);
        }
    }
}
