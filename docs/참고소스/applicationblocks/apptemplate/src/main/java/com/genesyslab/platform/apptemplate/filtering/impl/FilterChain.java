/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. 
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.apptemplate.filtering.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageFilter;


/**
 * This class represents composition of message filters.  
 */
public final class FilterChain implements MessageFilter, Cloneable {

    private transient FilterContext context;
    private transient ArrayList<FilterChainEntry> entries = new ArrayList<FilterChainEntry>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private volatile boolean canTrace = true;


    /**
     * Creates instance of FilterChain.
     */
    public FilterChain() throws NullPointerException{
        this(new FilterContext());
    }

    /**
     * Creates instance of FilterChain with specified context.
     * @param context filter context stores filter variables.
     * @throws NullPointerException if context argument is null.
     */
    public FilterChain(FilterContext context) throws NullPointerException{
        super();
        if (context == null) {
            throw new NullPointerException("context argument is null");
        }
        this.context = context;
    }

    /**
     * Gets filter context.
     * @return filter context.
     */
    public FilterContext getContext() {
        return context;
    }

    /**
     * Evaluates whether message is accepted by filter.
     * @param message Message to evaluate
     * @return true if message is accepted by filter else return false.
     */
    public boolean isMessageAccepted(final Message message) {
        readLock.lock();
        try {
            if (entries.size() == 0) {
                return true;
            }
            FilterResult lastOp = FilterResult.Accept;

            for (FilterChainEntry entry : entries) {
                if (entry != null) {
                    lastOp = entry.result;
                    if (entry.negation ^ entry.filter.isMessageAccepted(message, context)) {
                        switch (entry.result) {
                            case Accept:
                                return true;
                            case Deny:
                                return false;
                        }
                    }
                }
            }

            //if last 'accept' filter returned 'false' - forbid log.
            //if last 'deny' filter returned 'false' - allow log.
            return lastOp != FilterResult.Accept;            
            
        } finally {
            readLock.unlock();
        }
    }


    /**
     * Applies new filters (the old filters will be replaced).
     *
     * @param newFilters new list of filters.
     * @throws NullPointerException if argument filters is null.
     */
    public void applyFilters(ArrayList<FilterChainEntry> newFilters) throws NullPointerException {
        if (entries == null) {
            throw new NullPointerException("filters argument is null");
        }
        
        List<FilterChainEntry> old = entries;
        
        @SuppressWarnings("unchecked")
        ArrayList<FilterChainEntry> copy = (ArrayList<FilterChainEntry>) newFilters.clone();

        for(FilterChainEntry entry : copy) {
            if (entry != null) {
                entry.filter.register(context);
            }
        }

        writeLock.lock();
        try {
            entries = copy;
        } finally {
            writeLock.unlock();
        }
        
        for(FilterChainEntry entry : old) {
            if (entry != null) {
                entry.filter.unregister(context);
            }
        }
    }

    /**
     * Gets ArrayList of filters.
     * @return filters.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<BaseFilter> getFilters() {
        return (ArrayList<BaseFilter>) entries.clone();
    }

    /**
     * Removes all filters.
     */
    public void removeAllFilters() {
        applyFilters(new ArrayList<FilterChainEntry>());
    }
    
    public enum FilterResult { Accept, Deny };
    
    
    public static class FilterChainEntry {
        
        private final BaseFilter filter;
        private final boolean negation;
        private final FilterResult result;
        
        public FilterChainEntry(BaseFilter filter, boolean negation, FilterResult result) {
            super();
            this.filter = filter;
            this.negation = negation;
            this.result = result;
        }
        
        BaseFilter getFilter() {
            return filter;
        }
        
        boolean isNegative() {
            return negation;
        }
        
        FilterResult getResult() {
            return result;
        }
    }



    public boolean canTrace() {
        return canTrace;
    }
    
    
    public boolean setCanTrace(boolean value) {
        return canTrace = value;
    }

}
