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


/**
 * Default implementation of Cluster Protocol policy.
 *
 * @see ClusterProtocolBuilder#withClusterProtocolPolicy(ClusterProtocolPolicy)
 */
public class DefaultClusterProtocolPolicy
        implements ClusterProtocolPolicy {

    private boolean waitOnChannelOpening = false;
    private boolean useRequestProtocolId = false;

    /**
     * This option indicates that the Cluster Protocol should hold on user thread
     * on messages sending to wait for connection restoration.
     * <p/>
     * If protocol is <code>Opening</code> and this option is <code>true</code>,
     * the Cluster Protocol will hold user thread on
     * <code>send()</code>/<code>request()</code>/<code>beginRequest()</code>/<code>requestAsync()</code>
     * calls instead of throwing immediate exception.<br/>
     * It will use the protocol timeout value as maximum wait time.
     * <p/>
     * If Cluster Protocol is <code>Opening</code>, and it got no opened connections in the timeout frame,
     * {@link com.genesyslab.platform.commons.protocol.ChannelClosedOnSendException ChannelClosedOnSendException}
     * will be thrown.<br/>
     * If this option is <code>false</code>, the exception will be thrown immediately.
     *
     * @return <code>true</code> - if the hold on feature is enabled,
     *     <code>false</code> - if it's not.
     */
    @Override
    public boolean waitOnChannelOpening() {
        return waitOnChannelOpening;
    }

    /**
     * This option indicates that the Cluster Protocol should hold on user thread
     * on messages sending to wait for connection restoration.
     * <p/>
     * If protocol is <code>Opening</code> and this option is <code>true</code>,
     * the Cluster Protocol will hold user thread on
     * <code>send()</code>/<code>request()</code>/<code>beginRequest()</code>/<code>requestAsync()</code>
     * calls instead of throwing immediate exception.<br/>
     * It will use the protocol timeout value as maximum wait time.
     * <p/>
     * If Cluster Protocol is <code>Opening</code>, and it got no opened connections in the timeout frame,
     * {@link com.genesyslab.platform.commons.protocol.ChannelClosedOnSendException ChannelClosedOnSendException}
     * will be thrown.<br/>
     * If this option is <code>false</code>, the exception will be thrown immediately.
     *
     * @param waitOnChannelOpening the "hold on" feature enabling option.
     */
    public DefaultClusterProtocolPolicy waitOnChannelOpening(
            final boolean waitOnChannelOpening) {
        this.waitOnChannelOpening = waitOnChannelOpening;
        return this;
    }

    /**
     * Reserved for future use.
     */
    @Override
    public boolean useRequestProtocolId() {
        return useRequestProtocolId;
    }

    /**
     * Reserved for future use.
     */
    public DefaultClusterProtocolPolicy useRequestProtocolId(
            final boolean useRequestProtocolId) {
        this.useRequestProtocolId = useRequestProtocolId;
        return this;
    }
}
