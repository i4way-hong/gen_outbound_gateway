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

import com.genesyslab.platform.commons.protocol.ClientChannel;
import com.genesyslab.platform.commons.protocol.ReferenceBuilder;
import com.genesyslab.platform.commons.protocol.ProtocolDescription;


/**
 * Abstract builder base for cluster protocol nodes.
 *
 * @param <P> type of protocol cluster node.
 * @param <B> generic type extension parameter for this builder.
 */
public abstract class ProtocolBuilder
        <P extends ClientChannel, B extends ProtocolBuilder<P, B>> {

    protected ReferenceBuilder refBuilder = null;

    public abstract ProtocolDescription description();

    protected abstract P create();

    /**
     * Creates and returns new instance of cluster protocol node.
     *
     * @return new instance of cluster protocol node.
     */
    public P build() {
        final P protocol = create();
        return protocol;
    }


    /**
     * Initializes PSDK protocols reference builder for sharing between
     * all of the cluster protocol nodes.
     *
     * @param refBuilder the protocols requests reference builder.
     * @return self reference to this builder.
     */
    @SuppressWarnings("unchecked")
    public B withRefBuilder(final ReferenceBuilder refBuilder) {
        this.refBuilder = refBuilder;
        return (B) this;
    }
}
