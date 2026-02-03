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

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Default Cluster Protocol Load Balancer.<br/>
 * It implements simple "round robin" strategy on available cluster nodes.
 */
public class DefaultClusterLoadBalancer
        implements ClusterProtocolLoadBalancer {

    private final Lock lock = new ReentrantLock(true);
    private final List<Protocol> nodes = new ArrayList<Protocol>();
    private volatile int position = 0;


    public DefaultClusterLoadBalancer() {
    }

    @Override
    public void configure(final ConnectionConfiguration config) {
        // it does not have specific configuration options so far...
    }

    @Override
    public Protocol chooseNode(final Message request) {
        lock.lock();
        try {
            final int size = nodes.size();
            if (size > 0) {
                if (position >= size) {
                    position = 0;
                }
                return nodes.get(position++);
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addNode(final Protocol item) {
        if (item == null) {
            throw new IllegalArgumentException("LB item is null");
        }
        lock.lock();
        try {
            nodes.add(item);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeNode(final Protocol item) {
        if (item == null) {
            throw new IllegalArgumentException("LB item is null");
        }
        lock.lock();
        try {
            nodes.remove(item);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            nodes.clear();
        } finally {
            lock.unlock();
        }
    }
}
