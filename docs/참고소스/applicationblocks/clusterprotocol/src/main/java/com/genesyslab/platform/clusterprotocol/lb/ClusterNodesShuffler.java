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

import com.genesyslab.platform.clusterprotocol.ClusterProtocol;

import com.genesyslab.platform.standby.WSConfig;

import com.genesyslab.platform.commons.timer.TimerAction;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * Helper component for doing cluster load balancing from client type applications.
 * It allows applications to work with servers cluster without creation of live connections
 * to all of its nodes.<br/>
 * This option may be critical in case of tens of thousands clients.
 * <p/>
 * This component uses existing functionality of ClusterProtocol to update its nodes configuration.
 * ClusterProtocol allows dynamic change of nodes configuration:<ul>
 * <li>Newly added nodes will get new instance of usual PSDK protocol class.
 * And its asynchronous open will be started. Node will be added to the load balancer
 * right after its connection.</li>
 * <li>Removed nodes will be immediately excluded from the load balancer, and scheduled for closing
 * after its protocol timeout delay. It will help with delivery of responses on already started requests.</li></ul>
 * The randomizer uses java "Collections.shuffle(nodes)" method to randomize sequence of given nodes.<br/>
 * To update protocol nodes configuration it uses round-robin rotation over randomly sorted
 * whole list of the cluster nodes, choosing subset of given number of elements.<br/>
 * Rotating by one node in the subset, and using more than one connection allows protocol
 * to do not break all active connections at a single moment,
 * and to keep online at the cluster protocol "switchover"s.
 * <p/>
 * The randomizer component will have optional ability to attach truncated nodes' endpoints
 * as backup endpoints for selected ones. It will allow internal WarmStandby service to "switch over"
 * unresponsive node faster.
 * <code><pre>
 * final List&lt;WSConfig&gt; nodes = ...;
 * final UcsClusterProtocol ucsNProtocol =
 *         new UcsClusterProtocolBuilder()
 *                 .build();
 *
 * ucsNProtocol.setClientName("MyClientName");
 * ucsNProtocol.setClientApplicationType("MyAppType");
 *
 * ClusterNodesShuffler shuffler = new ClusterNodesShuffler(ucsNProtocol, 2);
 * TimerActionTicket shufflerTimer = null;
 *
 * try {
 *     shuffler.setNodes(nodes);
 *     ucsNProtocol.open();
 *     shufflerTimer = TimerFactory.getTimer().schedule(3000, 3000, shuffler);
 *
 *     // do the business logic on the cluster protocol...
 *     for (int i = 0; i < 200; i++) {
 *         EventGetVersion resp = (EventGetVersion) ucsNProtocol.request(RequestGetVersion.create());
 *         System.err.println("Resp from: " + resp.getEndpoint());
 *         Thread.sleep(300);
 *     }
 * } finally {
 *     if (shufflerTimer != null) {
 *         shufflerTimer.cancel();
 *         shufflerTimer = null;
 *     }
 *     ucsNProtocol.close();
 * }
 * </pre></code>
 *
 * @see ClusterProtocol
 */
public class ClusterNodesShuffler implements Runnable, TimerAction {

    private final ClusterProtocol protocol;
    private final int             activeNodes;

    private boolean addBackups = true;

    private final List<WSConfig> clusterNodes = new ArrayList<WSConfig>();


    /**
     * Creates nodes shuffler for the given Cluster Protocol instance.
     *
     * @param protocol the cluster protocol reference.
     * @param activeNodes number of active cluster nodes to pass to the protocol.
     * @throws IllegalArgumentException if given protocol reference is <code>null</code>,
     *     or number of active nodes is not a positive value.
     */
    public ClusterNodesShuffler(
            final ClusterProtocol protocol,
            final int             activeNodes) {
        if (protocol == null) {
            throw new IllegalArgumentException("ClusterProtocol instance is null");
        }
        this.protocol = protocol;
        if (activeNodes > 0) {
            this.activeNodes = activeNodes;
        } else {
            throw new IllegalArgumentException(
                    "Number of active nodes should be > 0");
        }
    }


    /**
     * Enables or disables adding to the selected nodes other ones endpoints as backups.
     * If its enabled, it allows internal WarmStandby service to work around unresponsive nodes
     * without waiting for a next rotation(s).<br/>
     * Its enabled by default.
     *
     * @param useBackups
     */
    public void setUseBackups(final boolean useBackups) {
        addBackups = useBackups;
    }


    /**
     * Sets the whole cluster nodes list for selections and rotations.<br/>
     * It shuffles given list, selects "active" nodes subset,
     * and pushes it to the protocol for appliance.
     *
     * @param nodes the whole list of cluster nodes configurations.
     */
    public void setNodes(final Collection<WSConfig> nodes) {
        synchronized (clusterNodes) {
            clusterNodes.clear();
            if (nodes != null) {
                clusterNodes.addAll(nodes);
                Collections.shuffle(clusterNodes);
            }
            setActiveNodes();
        }
    }


    /**
     * Combines "active nodes" list and passes it to the cluster protocol for appliance.
     */
    protected void setActiveNodes() {
        final int listSize = Math.min(activeNodes, clusterNodes.size());
        final List<WSConfig> list = new ArrayList<WSConfig>(listSize);
        for (int i = 0; i < listSize; i++) {
            list.add(clusterNodes.get(i));
        }
        if (addBackups) {
            for (int i = listSize; i < clusterNodes.size(); i++) {
                final int pos = i % listSize;
                final WSConfig orig = clusterNodes.get(pos);
                WSConfig item = list.get(pos);
                if (item == orig) {
                    item = (WSConfig) item.clone();
                }
                item.getEndpoints().addAll(
                        clusterNodes.get(i).getEndpoints());
                list.set(pos, item);
            }
        }
        protocol.setNodes(list);
    }


    /**
     * Performs single rotation of the selected cluster nodes.
     *
     * @see #rotateNodes()
     */
    @Override
    public void run() {
        rotateNodes();
    }

    /**
     * Performs single rotation of the selected cluster nodes.
     *
     * @see #rotateNodes()
     */
    @Override
    public void onTimer() {
        rotateNodes();
    }


    /**
     * Performs single rotation of the selected cluster nodes.<br/>
     * This operation is designed to be executed as periodical task {@link Runnable}.{@link #run()}
     * or {@link TimerAction}.{@link #onTimer()}, though, may be used directly
     * in case of application specific needs.
     */
    public void rotateNodes() {
        synchronized (clusterNodes) {
            // Rotate the shuffled list by one node:
            if (clusterNodes.size() > 1) {
                clusterNodes.add(clusterNodes.remove(0));
            }
            setActiveNodes();
        }
    }
}
