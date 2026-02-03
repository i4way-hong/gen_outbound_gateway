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

import com.genesyslab.platform.standby.WSConfig;

import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.ChannelOpenedEvent;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;

import com.genesyslab.platform.commons.threading.CompletionHandler;

import java.util.List;


/**
 * <p>Interface representing a client protocol connection on top of
 * encapsulated multiple protocol connections to a cluster
 * of similarly configured servers.</p>
 */
public interface ClusterProtocol
        extends Protocol {

    /**
     * Returns reference to the actual cluster protocol policy.
     *
     * @return the cluster protocol policy reference.
     */
    ClusterProtocolPolicy policy();


    /**
     * Sets or resets actual cluster nodes WarmStandby connections configurations.
     *
     * @param nodes list of cluster nodes configuration.
     * @throws IllegalArgumentException in the following cases:
     *     given nodes list is <code>null</code>, it contains <code>null</code> value,
     *     or there is an unnamed WSConfig.
     */
    void setNodes(WSConfig... nodes);

    /**
     * Sets or resets actual cluster nodes WarmStandby connections configurations.
     *
     * @param nodes collection of cluster nodes configuration.
     * @throws IllegalArgumentException in the following cases:
     *     given nodes collection is <code>null</code>, it contains <code>null</code> value,
     *     or there is an unnamed WSConfig.
     */
    void setNodes(Iterable<WSConfig> nodes);

    /**
     * Sets or resets actual cluster nodes Endpoint's list.
     *
     * @param endpoints list of cluster nodes Endpoint's.
     * @throws IllegalArgumentException in the following cases:
     *     given Endpoint's list is <code>null</code>, it contains <code>null</code> value,
     *     or there is an unnamed Endpoint.
     */
    void setNodesEndpoints(Endpoint... endpoints);

    /**
     * Sets or resets actual cluster nodes Endpoint's list.
     *
     * @param endpoints collection of cluster nodes Endpoint's.
     * @throws IllegalArgumentException in the following cases:
     *     given Endpoint's collection is <code>null</code>, it contains <code>null</code> value,
     *     or there is an unnamed Endpoint.
     */
    void setNodesEndpoints(Iterable<Endpoint> endpoints);


    /**
     * Adds list of WarmStandby configurations as additional cluster protocol nodes.
     *
     * @param nodes list of cluster nodes configuration.
     * @throws IllegalArgumentException in the following cases:
     *     given nodes list is <code>null</code>, it contains <code>null</code> value,
     *     or there is an unnamed WSConfig.
     */
    void addNodes(WSConfig... nodes);

    /**
     * Adds collection of WarmStandby configurations as additional cluster protocol nodes.
     *
     * @param nodes connection of cluster nodes configurations.
     * @throws IllegalArgumentException in the following cases:
     *     given nodes collection is <code>null</code>, it contains <code>null</code> value,
     *     or there is an unnamed WSConfig.
     */
    void addNodes(Iterable<WSConfig> nodes);

    /**
     * Adds Endpoint's list as additional cluster protocol nodes.
     *
     * @param endpoints list of cluster nodes Endpoint's.
     * @throws IllegalArgumentException in the following cases:
     *     given Endpoint's list is <code>null</code>, it contains <code>null</code> value,
     *     or there is an unnamed Endpoint.
     */
    void addNodesEndpoints(Endpoint... endpoints);

    /**
     * Adds Endpoint's collection as additional cluster protocol nodes.
     *
     * @param endpoints collection of cluster nodes Endpoint's.
     * @throws IllegalArgumentException in the following cases:
     *     given Endpoint's collection is <code>null</code>, it contains <code>null</code> value,
     *     or there is an unnamed Endpoint.
     */
    void addNodesEndpoints(Iterable<Endpoint> endpoints);


    /**
     * Removes cluster protocol node(s) by given nodes names.
     *
     * @param names list of Endpoint's/WSConfig's names.
     */
    void removeNodes(String... names);

    /**
     * Removes cluster protocol node(s) by given nodes names.
     *
     * @param names collection of Endpoint's/WSConfig's names.
     */
    void removeNodes(Iterable<String> names);


    /**
     * Returns list of cluster nodes connections configurations.
     *
     * @return list of cluster protocol nodes connections configurations.
     */
    List<WSConfig> getNodesConfig();


    /**
     * Initiates connections to the initialized set of cluster nodes.<br/>
     * The completion handler will be notified by the first connection open event.
     *
     * @param handler the completion handler to be notified with the operation result.
     * @param attachment user defined optional attachment for the handler.
     */
    <A> void openAsync(
            CompletionHandler<ChannelOpenedEvent, ? super A> handler,
            A attachment)
                    throws ProtocolException;

    /**
     * Initiates connections close to all of the cluster nodes.<br/>
     * Provided Completion Handler will be notified when all cluster nodes connections got closed.
     *
     * @param handler the completion handler to be notified with the operation result.
     * @param attachment user defined optional attachment for the handler.
     */
    <A> void closeAsync(
            CompletionHandler<ChannelClosedEvent, ? super A> handler,
            A attachment);

    /**
     * Returns reference to some opened instance of protocol in the cluster nodes pool
     * in accordance to the LB strategy.
     *
     * @return reference to a protocol instance wrapper.
     * @see com.genesyslab.platform.clusterprotocol.lb.ClusterProtocolLoadBalancer
     *          ClusterProtocolLoadBalancer
     */
    Protocol getNextAvailableProtocol();

    /**
     * Returns reference to some opened instance of protocol in the cluster nodes pool
     * in accordance to the LB strategy for handling of the given request message.
     *
     * @param message the protocol message.
     * @return reference to a protocol instance wrapper.
     * @see com.genesyslab.platform.clusterprotocol.lb.ClusterProtocolLoadBalancer
     *          ClusterProtocolLoadBalancer
     * @see com.genesyslab.platform.clusterprotocol.lb.ClusterProtocolLoadBalancer#chooseNode(
     *          com.genesyslab.platform.commons.protocol.Message)
     *          ClusterProtocolLoadBalancer.chooseNode(Message)
     */
    Protocol getNextAvailableProtocol(Message message);


    /**
     * Returns snapshot list of cluster protocol nodes,
     * which are tracked as <code>Opened</code> at the moment.
     *
     * @return list with cluster nodes protocols.
     */
    List<Protocol> getOpenedNodesProtocols();

    /**
     * Returns snapshot list of all cluster protocol nodes.
     *
     * @return list with cluster nodes protocols.
     */
    List<Protocol> getAllNodesProtocols();


    /**
     * Returns cluster protocol node instance by PSDK Protocol Id.<br/>
     * It may be useful in cases, when it is required to get particular server connection
     * by <code>ProtocolId</code> of specific response, or unsolicited event message.
     *
     * @param protocolId the protocol id.
     * @return the cluster protocol node with the given id, or <code>null</code>.
     */
    Protocol getNodeProtocol(int protocolId);

    /**
     * Returns cluster protocol node instance by name of configured
     * <code>Endpoint</code> or <code>WSConfig</code>.
     *
     * @param nodeName name of configured <code>Endpoint</code> or <code>WSConfig</code>.
     * @return the cluster protocol node, or <code>null</code>.
     */
    Protocol getNodeProtocol(String nodeName);
}
