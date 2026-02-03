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

import java.util.ArrayList;
import java.util.List;

import com.genesyslab.platform.apptemplate.filtering.impl.BaseFilter;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterChain;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterChain.FilterChainEntry;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterContext;
import com.genesyslab.platform.apptemplate.filtering.impl.configuration.FilterChainFactory.EntryConfiguration;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageFilter;


/**
 * This class keeps filter chain {@link com.genesyslab.platform.apptemplate.filtering.impl.FilterChain FilterChain}
 * and it's settings that can be modified at runtime.
 */
public class FilterChainConfiguration implements MessageFilter {

    private final int dbid;
    private final int targetDbid;

    private volatile FilterChain chain;
    private List<EntryConfiguration> enabledFilters = new ArrayList<EntryConfiguration>();
    private List<BaseFilter> availableInstances = new ArrayList<BaseFilter>();

    private final Object configLock = new Object();

    static ILogger log = Log.getMessageFilteringLogger();

    /**
     * Creates FilterChainConfiguration object. The <code>appDBID</code> and
     * <code>targetAppDBID</code> can be used to identify application
     * configuration updates sent by Configuration Server.
     *
     * @param appDBID
     *            The application DBID where filters are declared.
     * @param targetAppDBID
     *            The DBID of application to which {@link com.genesyslab.platform.commons.protocol.DuplexChannel DuplexChannel}
     *            with assigned FilterChainConfiguration is connected.
     */
    public FilterChainConfiguration(int appDBID, int targetAppDBID) {
        dbid = appDBID;
        targetDbid = targetAppDBID;
        chain = new FilterChain();
    }

    /**
     * Creates FilterChainConfiguration object. The <code>appDBID</code> and
     * <code>targetAppDBID</code> should be used to identify application
     * configuration updates sent by Configuration Server.
     *
     * @param appDBID
     *            The application DBID where filters are declared.
     * @param targetAppDBID
     *            The DBID of application to which {@link com.genesyslab.platform.commons.protocol.DuplexChannel DuplexChannel}
     *            with assigned FilterChainConfiguration is connected.
     * @param context
     *            Filter context where filter variables or other data is stored.
     */
    public FilterChainConfiguration(int appDBID, int targetAppDBID, FilterContext context) {
        dbid = appDBID;
        targetDbid = targetAppDBID;
        chain = new FilterChain(context);
    }

    /**
     * Returns DBID of the application, where filters are declared.
     * 
     * @return
     */
    public int getApplicationDBID() {
        return dbid;
    }

    /**
     * Returns DBID of the application, to which client protocol connected.
     * 
     * @return
     */
    public int getTargetApplicationDBID() {
        return targetDbid;
    }


    /**
     * Returns the shallow copy of available filter instances list.
     *
     * @return list of filters
     */
    public List<BaseFilter> getFilterList() {
        synchronized (configLock) {
            return new ArrayList<BaseFilter>(availableInstances);
        }
    }

    /**
     * Registers new available filter objects for this chain.
     *
     * @param filters the filter objects list
     */
    public void applyFilterList(List<BaseFilter> filters) {
        synchronized (configLock) {
            availableInstances = filters;
        }
    }


    /**
     * Filter entry that should be used in this filter chain.
     *
     * @param entryList the list of filter entries, assigned to the protocol
     * @return <code>true</code> if list was updated
     */
    public boolean setEnabledFilters(List<EntryConfiguration> entryList) {
        if (entryList == null) {
            throw new IllegalArgumentException("entryList argument can't be null");
        }
        synchronized (configLock) {
            if (changed(enabledFilters, entryList)) {
                enabledFilters = new ArrayList<EntryConfiguration>(entryList);                
                return true;
            }
            if(log.isDebug()) {
                log.debug("FilterChainConfiguration: filter list wasn't changed for connection dbid="+targetDbid);
            }
            return false;
            
        }
    }

    private boolean changed(List<EntryConfiguration> oldEntries, List<EntryConfiguration> newEntries) {
        if (oldEntries.size() != newEntries.size()) {
            return true;
        }
        for (int i=0; i<oldEntries.size(); i++) {
            EntryConfiguration entry = oldEntries.get(i);
            
            if(entry==null)
                return true;
            
            if(!entry.equals(newEntries.get(i)))
                return true;                
        }
        return false;
    }

    /**
     * Applies changes, made in filter list or in filter definition, to the filter
     * chain object.
     */
    public void save() {
        synchronized (configLock) {
            StringBuilder sb = null;
            if (log.isDebug()) {
                sb = new StringBuilder().append(String.format(
                        "FilterChainConfiguration: applied log filter list to connection dbid %s:\n", targetDbid));
            }
            ArrayList<FilterChainEntry> newFilterList = new ArrayList<FilterChainEntry>();
            for (EntryConfiguration configEntry : enabledFilters) {
                for (BaseFilter filter : availableInstances) {
                    if (configEntry.getFilterName().equals(filter.getKey())) {
                        newFilterList.add(new FilterChainEntry(filter, configEntry.isNegative(),
                                configEntry.getResult()));
                        if (sb != null) {
                            sb.append(String.format("%s, isNegative=%s, result=%s\n",
                                    configEntry.getFilterName(), configEntry.isNegative(),
                                    configEntry.getResult()));
                        }
                    }
                }
            }

            chain.applyFilters(newFilterList);
            if (sb != null) {
                log.debug(sb.toString());
            }           
        }
    }

    /**
     * Removes all filter instances.
     */
    public void clear() {
        synchronized (configLock) {
            enabledFilters.clear();
            availableInstances.clear();
            chain.removeAllFilters();
            chain.setCanTrace(true);
        }
    }

    /**
     * Clears filter context.
     */
    public void resetContext() {
        synchronized (configLock) {
            if (chain != null) {
                FilterContext context = chain.getContext();
                if (context != null) {
                    if(log.isDebug()) {
                       log.debug("FilterChainConfiguration: Clear filter context");
                    }
                    context.clearAllVariables();
                }
            }
        }
    }

    /**
     * This method uses internal {@link FilterChain} object with pre-configured
     * Log Filters to evaluate if message can be logged.
     * 
     * @param message
     *            for filtering
     * @return true if message is accepted by filter else return false.
     */
    public boolean isMessageAccepted(Message message) {
        return chain.isMessageAccepted(message);      
    }


    /**
     * Enables or disables tracing log entries like "New message# " and so on.
     * @param value
     */
    public void setCanTrace(boolean value) {
        chain.setCanTrace(value);
    }


    public boolean canTrace() {
       return chain.canTrace();    
    }
}
