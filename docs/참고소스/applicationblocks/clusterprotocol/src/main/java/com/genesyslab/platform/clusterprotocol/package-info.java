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

/**
 * <h2>Cluster Protocol Application Block</h2>
 * <p>The Cluster Protocol component is to provide usual Platform SDK
 * {@link com.genesyslab.platform.commons.protocol.Protocol Protocol} interface
 * for communication with multiple servers configured as a cluster.<br/>
 * It supports following features:<ul>
 * <li>it uses multiple simultaneous protocol connections behind single <code>Protocol</code>
 *   object instance;</li>
 * <li>it uses <code>WarmStandby</code> service to keep the protocol connections alive;</li>
 * <li>it supports ability to work with backup <code>Endpoint</code>s of Cluster Nodes;</li>
 * <li>it keeps list of active connections and applies Load Balancing strategy
 *   to spread application requests on them;</li>
 * <li>it allows user applications to substitute Load Balancing strategy by providing
 *   custom implementation of the
 *   {@link com.genesyslab.platform.clusterprotocol.lb.ClusterProtocolLoadBalancer Load Balancer}
 *   interface.</li>
 * </ul></p>
 * <p>It's implemented as a set of generic classes and several customizations for specific
 * servers protocols.<br/>
 * Due to the nature of the <code>Protocol</code> interface, Cluster Protocol does not support
 * usual automatic handshakes and does not have special handling logic for client sessions.<br/>
 * Following protocols are supported and included in this Application Block:<ul>
 * <li>{@link com.genesyslab.platform.clusterprotocol.esp ESP} - External Service Protocol;</li>
 * <li>{@link com.genesyslab.platform.clusterprotocol.ucs UCS} - Universal Contact Server Protocol;</li>
 * <li>{@link com.genesyslab.platform.clusterprotocol.email EspEmail} - ESP based Genesys Email Server Protocol;</li>
 * <li>{@link com.genesyslab.platform.clusterprotocol.chat Chat} - Genesys Chat Server Flex Chat Protocol.</li>
 * </ul></p>
 * <p>Usage sample:<pre><code>
 * final UcsClusterProtocol ucsNProtocol =
 *         new UcsClusterProtocolBuilder().build();
 *
 * ucsNProtocol.setClientName(myClientName);
 * ucsNProtocol.setClientApplicationType(myApplicationType);
 * ucsNProtocol.setNodesEndpoints(clusterEndpointsList);
 *
 * ucsNProtocol.setNodesEndpoints(
 *         new Endpoint("ucs1", UCS_1_HOST, UCS_1_PORT),
 *         new Endpoint("ucs2", UCS_2_HOST, UCS_2_PORT),
 *         new Endpoint("ucs3", UCS_3_HOST, UCS_3_PORT));
 *
 * try {
 *     ucsNProtocol.open();
 *     // ...
 *     EventGetVersion ev1 = (EventGetVersion) ucsNProtocol.request(RequestGetVersion.create());
 *     System.out.println("UCS version from " + ev1.getEndpoint() + " - " + ev1.getVersion());
 *     EventGetVersion ev2 = (EventGetVersion) ucsNProtocol.request(RequestGetVersion.create());
 *     System.out.println("UCS version from " + ev2.getEndpoint() + " - " + ev2.getVersion());
 *     EventGetVersion ev3 = (EventGetVersion) ucsNProtocol.request(RequestGetVersion.create());
 *     System.out.println("UCS version from " + ev3.getEndpoint() + " - " + ev3.getVersion());
 *     // ...
 * } finally {
 *     ucsNProtocol.close();
 * }
 * </code></pre></p>
 */
package com.genesyslab.platform.clusterprotocol;
