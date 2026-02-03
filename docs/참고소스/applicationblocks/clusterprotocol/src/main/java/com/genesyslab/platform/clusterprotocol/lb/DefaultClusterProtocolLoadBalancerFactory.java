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

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Default implementation of Cluster Protocol Load Balancer.
 */
public class DefaultClusterProtocolLoadBalancerFactory
        implements ClusterProtocolLoadBalancerFactory {

    private static final AtomicReference<ClusterProtocolLoadBalancerFactory> DEFAULT_FACTORY =
            new AtomicReference<ClusterProtocolLoadBalancerFactory>();

    private static final ILogger log = Log.getLogger(DefaultClusterProtocolLoadBalancerFactory.class);


    /**
     * Returns Platform SDK default Load Balancer Factory.
     *
     * @return load balancer factory singleton instance.
     */
    public static ClusterProtocolLoadBalancerFactory getFactory() {
        ClusterProtocolLoadBalancerFactory factory = DEFAULT_FACTORY.get();
        if (factory == null) {
            synchronized (DEFAULT_FACTORY) {
                factory = DEFAULT_FACTORY.get();
                if (factory == null) {
                    final ServiceLoader<ClusterProtocolLoadBalancerFactory> srvLoader =
                            ServiceLoader.load(ClusterProtocolLoadBalancerFactory.class);
                    final Iterator<ClusterProtocolLoadBalancerFactory> factories = srvLoader.iterator();
                    if (factories != null) {
                        if (factories.hasNext()) {
                            factory = factories.next();
                        }
                        if (factory != null) {
                            if (factories.hasNext()) {
                                log.infoFormat(
                                        "Got multiple implementations of ClusterProtocolLoadBalancerFactory. Using {0}",
                                        factory.getClass().getName());
                            } else {
                                log.infoFormat(
                                        "ClusterProtocolLoadBalancerFactory implementation in use: {0}",
                                        factory.getClass().getName());
                            }
                        }
                    }

                    if (factory == null) {
                        factory = new DefaultClusterProtocolLoadBalancerFactory();
                        log.warnFormat(
                                "Using following ClusterProtocolLoadBalancerFactory - {0}",
                                factory.getClass().getName());
                    }

                    DEFAULT_FACTORY.set(factory);
                }
            }
        }

        return factory;
    }

    /**
     * Sets Platform SDK default Load Balancer Factory.
     *
     * @param factory custom load balancer factory instance.
     */
    public static void setFactory(
            final ClusterProtocolLoadBalancerFactory factory) {
        synchronized (DEFAULT_FACTORY) {
            DEFAULT_FACTORY.set(factory);
        }
    }


    /**
     * Creates and returns new instance of Default Cluster Protocol Load Balancer.
     *
     * @return new instance of default load balancer.
     */
    @Override
    public ClusterProtocolLoadBalancer getLoadBalancer() {
        return new DefaultClusterLoadBalancer();
    }
}
