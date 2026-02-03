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
package com.genesyslab.platform.clusterprotocol;

import com.genesyslab.platform.clusterprotocol.lb.ClusterProtocolLoadBalancer;

import com.genesyslab.platform.commons.protocol.ClientChannel;


/**
 * Abstract generic builder base for Cluster Protocols construction.
 *
 * @param <P> cluster protocol node protocol type.
 * @param <CP> cluster protocol type.
 * @param <PB> cluster protocol node builder type.
 * @param <B> cluster protocol builder type.
 */
public abstract class ClusterProtocolBuilder
        <P extends ClientChannel,
         CP extends ClusterProtocol,
         PB extends ProtocolBuilder<P, PB>,
         B extends ClusterProtocolBuilder<P, CP, PB, B>> {

    protected PB protocolBuilder = null;
    protected ClusterProtocolLoadBalancer loadBalancer = null;
    protected ClusterProtocolPolicy protocolPolicy = null;


    /**
     * Initializes cluster protocol builder with custom protocol nodes builder.
     *
     * @param protocolBuilder the protocol cluster nodes builder.
     * @return self reference to this cluster protocol builder.
     */
    @SuppressWarnings("unchecked")
    public B withProtocolBuilder(
            final PB protocolBuilder) {
        this.protocolBuilder = protocolBuilder;
        return (B) this;
    }

    /**
     * Initializes cluster protocol builder with custom load balancer.
     * <p/>
     * <i><b>Note:</b> Cluster Protocol Load Balancer component is stateful,
     * so, it is not supposed to share same instance of Load Balancer between
     * different Cluster Protocol instances. I.e. do not build more than one
     * cluster protocol with single instance of load balancer.</i>
     *
     * @param loadBalancer user defined instance of Cluster Protocol Load Balancer.
     * @return self reference to this cluster protocol builder.
     */
    @SuppressWarnings("unchecked")
    public B withLoadBalancer(
            final ClusterProtocolLoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
        return (B) this;
    }

    /**
     * Initializes cluster protocol builder with custom cluster protocol policy.
     *
     * @param protocolPolicy user defined cluster protocol policy.
     * @return self reference to this cluster protocol builder.
     */
    @SuppressWarnings("unchecked")
    public B withClusterProtocolPolicy(
            final ClusterProtocolPolicy protocolPolicy) {
        this.protocolPolicy = protocolPolicy;
        return (B) this;
    }

    /**
     * Creates and returns new instance of Cluster Protocol.
     *
     * @return new instance of Cluster Protocol.
     */
    public abstract CP build();

}
