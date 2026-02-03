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
package com.genesyslab.platform.apptemplate.lmslogger.impl;

import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration.IGAppConnConfiguration;

import com.genesyslab.platform.configuration.protocol.types.CfgAppType;

import com.genesyslab.platform.management.protocol.messageserver.LogLevel;
import com.genesyslab.platform.management.protocol.messageserver.LogCategory;
import com.genesyslab.platform.management.protocol.messageserver.AttributeList;
import com.genesyslab.platform.management.protocol.messageserver.requests.RequestLogMessage;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Map;
import java.util.Date;
import java.util.List;


/**
 * Default implementation of the Message Server Delivery component.
 * <p/>
 * <i><b>Note:</b> This class is a part of PSDK internal LMS Events delivery mechanism.<br/>
 * It is not supposed for direct usage from applications.</i>
 */
public class MSEventSenderImpl implements MSEventSender {

    private final String name;

    private final IGApplicationConfiguration appConfig;

    private final String serverName;
    private final String host;
    private final int    port;
    private final String backupHost;
    private final int    backupPort;

    private final Map<String, String> connProperties;

    private volatile Thread worker = null;

    private final BlockingDeque<RequestLogMessage> outputQueue;

    private final ReentrantLock LOCK = new ReentrantLock();


    /**
     * This constructor is used by the Delivery Manager implementation in case of Message Server
     * Appender initialization from a logging configuration file.
     *
     * @param name the name of sender.
     * @param host the Message Server host name or address.
     * @param port the Message Server TCP/IP port number.
     * @param backupHost host name or address of backup Message Server or null.
     * @param backupPort TCP/IP port number of the backup Message Server.
     * @param properties parameters of the Message Server connection.
     * @throws IllegalArgumentException if given parameters contain invalid values.
     * @see com.genesyslab.platform.apptemplate.lmslogger.log4j2.GMessageServerDeliveryManagerImpl
     *          GMessageServerDeliveryManagerImpl
     * @see #MSEventSenderImpl(String, IGApplicationConfiguration)
     */
    public MSEventSenderImpl(
            final String name,
            final String host,
            final int    port,
            final String backupHost,
            final int    backupPort,
            final Map<String, String> properties) {
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("MessageServer host name is not specified");
        }
        if (port <= 0) {
            throw new IllegalArgumentException("MessageServer port number is not valid: " + port);
        }
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("MessageServer sender properties are not provided");
        }

        this.name = name;
        this.host = host;
        this.port = port;
        this.backupHost = backupHost;
        this.backupPort = backupPort;
        this.connProperties = properties;
        this.appConfig = null;
        this.serverName = (name != null && !name.isEmpty()) ? name : "unknown";

        this.outputQueue = new BoundedDeque();
    }

    /**
     * This constructor is used by the AppTemplate configuration helper methods.
     *
     * @param name the name of sender.
     * @param appConfig the application configuration.
     * @throws IllegalArgumentException if given application configuration parameters contain invalid values.
     * @see com.genesyslab.platform.apptemplate.log4j2.Log4j2Configurator Log4j2Configurator
     * @see DirectLmsLoggerFactory
     */
    public MSEventSenderImpl(
            final String name,
            final IGApplicationConfiguration appConfig) {
        if (appConfig == null) {
            throw new IllegalArgumentException("MessageServer sender appConfig parameter is null");
        }
        IGAppConnConfiguration msConn = null;
        final List<IGAppConnConfiguration> conns = appConfig.getAppServers();
        if (conns != null) {
            for (final IGAppConnConfiguration conn: conns) {
                final IGApplicationConfiguration app = conn.getTargetServerConfiguration();
                if (app != null && CfgAppType.CFGMessageServer.equals(app.getApplicationType())) {
                    msConn = conn;
                    break;
                }
            }
        }
        if (msConn == null) {
            throw new IllegalArgumentException("No MessageServer in the application connections");
        }
        final IGApplicationConfiguration tgtServer = msConn.getTargetServerConfiguration();
        if (tgtServer == null) {
            throw new IllegalArgumentException("No MessageServer app configuration in the application connection");
        }

        this.name = name;
        this.host = null;
        this.port = 0;
        this.backupHost = null;
        this.backupPort = 0;
        this.connProperties = null;

        this.appConfig = appConfig;

        this.serverName = (tgtServer.getApplicationName() != null) ? tgtServer.getApplicationName() : "unknown";

        this.outputQueue = new BoundedDeque();
    }


    protected String getName() {
        return name;
    }

    protected IGApplicationConfiguration getAppConfig() {
        return appConfig;
    }

    protected String getServerName() {
        return serverName;
    }

    protected String getHost() {
        return host;
    }

    protected int getPort() {
        return port;
    }

    protected String getBackupHost() {
        return backupHost;
    }

    protected int getBackupPort() {
        return backupPort;
    }

    protected Map<String, String> getConnProperties() {
        return connProperties;
    }


    @Override
    public void start() {
        LOCK.lock();
        try {
            if (worker == null || !worker.isAlive()) {
                String tname = "MSQueue";
                if (name != null && !name.isEmpty()) {
                    tname += '(' + name + ')';
                }
                worker = new Thread(null, new Worker(), tname);
                worker.setDaemon(true);
                worker.start();
            }
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void stop() {
        LOCK.lock();
        try {
            final Thread worker = this.worker;
            if (worker != null) {
                new Thread(worker.getName() + "-stopper") {
                    @Override
                    public void run() {
                        try {
                            worker.interrupt();
                            worker.join();
                        } catch (final InterruptedException ex) {
                            return;
                        }
                    }
                }.start();
            }
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void push(
            final Integer       entryId,
            final LogCategory   entryCategory,
            final LogLevel      level,
            final String        entryText,
            final Date          date,
            final AttributeList attributes) {
        final RequestLogMessage msg = RequestLogMessage.create();

        msg.setEntryId(entryId);
        msg.setEntryCategory(entryCategory);
        msg.setLevel(level);
        msg.setEntryText(entryText);
        msg.setTime(date != null ? date : new Date());

        if (attributes != null) {
            msg.setAttributes(attributes);
        }

        outputQueue.offer(msg);

        start();
    }

    @Override
    public String toString() {
        return getClass().getName() + '(' + name + ')';
    }


    protected class Worker implements Runnable {

        @Override
        public void run() {
            try {
                MSEventSenderCore.handleQueue(
                        MSEventSenderImpl.this, outputQueue);
            } catch (final Throwable thr) {
                // log.debug("MSEventSenderImpl exception at " + protocol, thr);
            }
        }
    }


    protected static class BoundedDeque extends LinkedBlockingDeque<RequestLogMessage> {

        private static final long serialVersionUID = 2330645800728538853L;

        public static final int MIN_LENGTH = 200;
        public static final int MAX_LENGTH = 20000;
        public static final int DEFAULT_LENGTH = 1000;

        private final int size;

        public BoundedDeque() {
            this(DEFAULT_LENGTH);
        }

        public BoundedDeque(final int size) {
            if (size < MIN_LENGTH) {
                this.size = MIN_LENGTH;
            } else if (size > MAX_LENGTH) {
                this.size = MAX_LENGTH;
            } else {
                this.size = size;
            }
        }

        public boolean offer(final RequestLogMessage event) {
            if (super.offer(event)) {
                while (size() > size) {
                    poll();
                }
                return true;
            }
            return false;
        }
    }
}
