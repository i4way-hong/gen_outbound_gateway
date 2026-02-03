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
 * <p/>External Service Cluster Protocol.
 *
 * <p/>Usage sample:<pre><code>
 * final EspClusterProtocol espNProtocol =
 *         new EspClusterProtocolBuilder().build();
 *
 * espNProtocol.setNodesEndpoints(
 *         new Endpoint("srv1", ESP_1_HOST, ESP_1_PORT),
 *         new Endpoint("srv2", ESP_2_HOST, ESP_2_PORT),
 *         new Endpoint("srv3", ESP_3_HOST, ESP_3_PORT));
 *
 * try {
 *     espNProtocol.open();
 *     // ...
 *     Event3rdServerResponse ev1 = (Event3rdServerResponse) espNProtocol.request(Request3rdServer.create());
 *     System.out.println("Response from " + ev1.getEndpoint() + " - " + ev1);
 *     Event3rdServerResponse ev2 = (Event3rdServerResponse) espNProtocol.request(Request3rdServer.create());
 *     System.out.println("Response from " + ev2.getEndpoint() + " - " + ev2);
 *     Event3rdServerResponse ev3 = (Event3rdServerResponse) espNProtocol.request(Request3rdServer.create());
 *     System.out.println("Response from " + ev3.getEndpoint() + " - " + ev3);
 *     // ...
 * } finally {
 *     espNProtocol.close();
 * }
 * </code></pre>
 *
 * @see com.genesyslab.platform.clusterprotocol.esp.EspClusterProtocolBuilder EspClusterProtocolBuilder
 * @see com.genesyslab.platform.openmedia.protocol.ExternalServiceProtocol ExternalServiceProtocol
 * @see com.genesyslab.platform.openmedia.protocol.externalservice.request
 */
package com.genesyslab.platform.clusterprotocol.esp;
