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

package com.genesyslab.platform.apptemplate.filtering.impl.configuration;

import java.util.Collection;
import java.util.EventObject;

import com.genesyslab.platform.applicationblocks.com.ConfEvent;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaApplication;
import com.genesyslab.platform.applicationblocks.commons.Action;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.DuplexChannel;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageFilter;

/**
 * Implements {@link MessageFilter} to decide whether message should be logged
 * or not upon internal message filter chain. Instance of this class should be
 * assigned to the protocol object using 
 * {@link com.genesyslab.platform.apptemplate.filtering.FilterConfigurationHelper#bind(DuplexChannel, String, IConfService) FilterConfigurationHelper.bind()}
 * helper.<br> 
 * Implements {@link Action Action&lt;T&gt;} to receive notifications from Configuration
 * Server. Creates, deletes or reconfigures message filters in a filter chain
 * upon received notifications.<br> 
 * Implements {@link ChannelListener}. Handles
 * channel closed event to discard filter context (clears variables, etc.)
 */
public class FilterChainNotificationHandler implements MessageFilter, Action<ConfEvent>,
        ChannelListener {

    private final FilterChainConfiguration chain;

    static ILogger log = Log.getMessageFilteringLogger();

    /**
     * Creates object that can handle configuration notifications and apply them
     * to {@link FilterChainConfiguration} object.
     * 
     * @param chain
     */
    public FilterChainNotificationHandler(FilterChainConfiguration chain) {
        if (chain == null) {
            throw new IllegalArgumentException("Filter Chain Configuration can't be null");
        }
        this.chain = chain;
    }

    /**
     * 
     * @return {@link FilterChainConfiguration} object
     */
    public FilterChainConfiguration getChainConfiguration() {
        return chain;
    }

    public void onChannelOpened(EventObject event) {
    }

    /**
     * Handles channel close event to clear filter context.
     */
    public void onChannelClosed(ChannelClosedEvent event) {
        try {
            this.chain.resetContext();
        } catch (Throwable e) {
            if(log.isWarn()) {
                log.warn("Exception occured on filter context clear", e);
            }
        }
    }

    public void onChannelError(ChannelErrorEvent event) {
    }

    /**
     * Handles notifications from Configuration server.
     */
    public void handle(ConfEvent obj) {
        try {
            Object result = obj.getCfgObject();

            if (result instanceof CfgDeltaApplication) {

                CfgDeltaApplication delta = (CfgDeltaApplication) result;

                int dbid = chain.getApplicationDBID();
                int targetDbid = chain.getTargetApplicationDBID();
                Integer deltaDbid = delta.getDBID();

                if (deltaDbid != null && deltaDbid.intValue() == dbid) {
                    if (log.isDebug()) {
                        log.debugFormat("Filter chain received update for application dbid {0}",
                                dbid);
                    }

                    boolean updated = FilterChainFactory.applyFilterOptions(chain,
                            delta.getAddedOptions(), delta.getChangedOptions(),
                            delta.getDeletedOptions());

                    Collection<CfgConnInfo> changedConnections = delta.getChangedAppServers();
                    Collection<CfgConnInfo> deletedConnections = delta.getDeletedAppServers();
                    // for added connection user should first configure new
                    // protocol
                    // object
                    // and then register FilterChainConfiguration with
                    // FilterConfigurationHelper.bind()

                    if (deletedConnections != null) {
                        for (CfgConnInfo conn : deletedConnections) {
                            Integer connDbid = conn.getAppServerDBID();
                            if (connDbid != null && connDbid.equals(targetDbid)) {
                                if (log.isDebug()) {
                                    log.debugFormat(
                                            "Connection dbid {0} was removed, will clean filter chain for corresponding protocol",
                                            connDbid);
                                }
                                chain.clear();
                                updated = true;
                            }
                        }
                    } else if (changedConnections != null) {
                        for (CfgConnInfo conn : changedConnections) {
                            Integer connDbid = conn.getAppServerDBID();
                            if (connDbid != null && connDbid.equals(targetDbid)) {
                                String appParams = conn.getAppParams();
                                if (log.isDebug()) {
                                    log.debugFormat(
                                            "Filter chain recieved update for connection dbid {0}",
                                            connDbid);
                                    updated |= FilterChainFactory.setEnabledFilters(chain, appParams);
                                }
                            }
                        }
                    }

                    if (updated) {
                        chain.save();
                    }
                }
            }
        } catch (Throwable e) {
            if (log.isWarn()) {
                log.warn("Exception occured during filter configuration update", e);
            }
        }

    }

    /**
     * Delegates message evaluation to internal Filter Chain witch is configured
     * upon application settings, defined in CME.
     */
    public boolean isMessageAccepted(Message message) {
        try {
            return chain.isMessageAccepted(message);
        } catch (Throwable e) {
            if (log.isDebug()) {
                log.debug(
                        "Exception occured during evaluation if message content can be logged: \n",
                        e);
            }
            return false;
        }
    }

    public boolean canTrace() {
        try {
            return chain.canTrace();
        } catch (Throwable e) {
            if (log.isDebug()) {
                log.debug("Exception occured during evaluation if message trace can be logged: \n",
                        e);
            }
            return false;
        }
    }

}
