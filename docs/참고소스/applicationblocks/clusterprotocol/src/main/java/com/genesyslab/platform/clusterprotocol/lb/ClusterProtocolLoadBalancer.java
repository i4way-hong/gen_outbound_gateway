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
package com.genesyslab.platform.clusterprotocol.lb;

import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.connection.configuration.ConnectionConfiguration;


/**
 * <p/>Common interface for Cluster Protocol Load Balancer component.
 *
 * <p/>It allows user applications to implement custom load balancing strategies.
 *
 * @see com.genesyslab.platform.clusterprotocol.ClusterProtocolBuilder#withLoadBalancer(ClusterProtocolLoadBalancer)
 *         ClusterProtocolBuilder.withLoadBalancer(ClusterProtocolLoadBalancer)
 */
public interface ClusterProtocolLoadBalancer {

    /**
     * This method is to choose and apply load balancer specific options
     * from the cluster connection configuration.
     *
     * @param config connection configuration from the Cluster Protocol base Endpoint.
     * @see com.genesyslab.platform.clusterprotocol.ClusterProtocol#setEndpoint(
     *         com.genesyslab.platform.commons.protocol.Endpoint) ClusterProtocol.setEndpoint(Endpoint)
     */
    void configure(ConnectionConfiguration config);

    /**
     * This method is to add just connected Cluster Protocol Node
     * to load balancing strategy.
     *
     * @param node cluster protocol node.
     */
    void addNode(Protocol node);

    /**
     * This method is to remove Cluster Protocol Node from load balancing strategy.<br/>
     * Its called when node is removed from the cluster, or its protocol connection is lost.
     *
     * @param node cluster protocol node.
     */
    void removeNode(Protocol node);

    /**
     * This method is called when Cluster Protocol is being closed.
     */
    void clear();

    /**
     * This method is to apply load balancing strategy on available cluster nodes.
     *
     * @param message user specified protocol message for sending (may be <code>null</code>).
     * @return selected cluster protocol node.
     */
    Protocol chooseNode(Message message);
}
