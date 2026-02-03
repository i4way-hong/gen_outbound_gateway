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
package com.genesyslab.platform.apptemplate.lmslogger.log4j2;

import com.genesyslab.platform.apptemplate.lmslogger.impl.MSEventSender;
import com.genesyslab.platform.apptemplate.lmslogger.impl.MSEventSenderImpl;

import com.genesyslab.platform.apptemplate.log4j2plugin.GMessageServerDeliveryManagerBase;
import com.genesyslab.platform.apptemplate.log4j2plugin.GMessageServerDeliveryManagerCtrl;

import com.genesyslab.platform.management.protocol.messageserver.LogLevel;
import com.genesyslab.platform.management.protocol.messageserver.LogCategory;
import com.genesyslab.platform.management.protocol.messageserver.AttributeList;

import org.apache.logging.log4j.status.StatusLogger;

import java.util.Map;
import java.util.Date;
import java.util.Queue;


/**
 * Functional implementation of the delivery manager for
 * {@link com.genesyslab.platform.apptemplate.log4j2plugin.GMessageServerAppender GMessageServerAppender}.
 * <p/>
 * <i><b>Note:</b> This class is internal and is not supposed for explicit usage by applications.</i>
 */
public class GMessageServerDeliveryManagerImpl
        extends GMessageServerDeliveryManagerBase
        implements GMessageServerDeliveryManagerCtrl {

    private final String name;

    private String host;
    private int    port;
    private String backupHost;
    private int    backupPort;

    private Map<String, String> connProperties;

    private MSEventSender eventSender;

    protected static StatusLogger LOG = StatusLogger.getLogger();


    public GMessageServerDeliveryManagerImpl() {
        this.name = "MessageServer";
    }

    public GMessageServerDeliveryManagerImpl(
            final String        name,
            final MSEventSender eventSender) {
        this.name = name;
        this.eventSender = eventSender;
    }


    protected MSEventSender getEventSender() {
        if (eventSender == null && host != null && connProperties != null) {
            eventSender = new MSEventSenderImpl(
                    name, host, port, backupHost, backupPort, connProperties);
        }
        return eventSender;
    }


    @Override
    public void setEndpointParameters(
            final String host,
            final int    port,
            final String backupHost,
            final int    backupPort,
            final Map<String, String> properties) {
        this.host = host;
        this.port = port;
        this.backupHost = backupHost;
        this.backupPort = backupPort;
        this.connProperties = properties;
    }


    @Override
    public void setEventsQueue(final Queue<EventData> queue) {
        super.setEventsQueue(queue);
    }


    @Override
    public void send(
            final long date,
            final int entryId,
            final int entryCategory,
            final int level,
            final String entryText,
            final Map<String, String> attributes,
            final Throwable throwable) {
        final MSEventSender sender = eventSender;

        if (sender == null) {
            super.send(date, entryId, entryCategory, level, entryText, attributes, throwable);
        } else {
            AttributeList attribs = null;
            if (attributes != null) {
                attribs = new AttributeList();
                if (!attributes.isEmpty()) {
                    attribs.putAll(attributes);
                }
            }

            sender.push(
                    entryId,
                    LogCategory.valueOf(entryCategory),
                    LogLevel.valueOf(level),
                    entryText,
                    (date != 0) ? new Date(date) : null,
                    attribs);
        }
    }

    @Override
    public void onStart() {
        final MSEventSender sender = getEventSender();
        if (sender != null) {
            try {
                sender.start();
            } catch (final Exception ex) {
                LOG.error("Failed to start MSEventSender", ex);
            }
        }
    }

    @Override
    public void onStop() {
        final MSEventSender sender = eventSender;
        if (sender != null) {
            try {
                sender.stop();
            } catch (final Exception ex) {
                LOG.error("Failed to stop MSEventSender", ex);
            }
        }
    }
}
