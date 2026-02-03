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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.genesyslab.platform.apptemplate.filtering.impl.BaseFilter;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterChain.FilterResult;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.Pair;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;

/**
 * This class is used internally by the
 * {@link com.genesyslab.platform.apptemplate.filtering.FilterConfigurationHelper
 * FilterConfigurationHelper} to create or upgrade filter configuration.
 * 
 * Creates or upgrades {@link FilterChainConfiguration} upon notifications. Uses
 * {@link DefaultFilterFactory} to create filter or upgrade filter
 * configuration. <br>
 * It is possible to set custom filter factory:
 * <pre><code>
 * FilterChainFactory.setCustomFilterFactory("custom-filter", new CustomFilterFactory);
 * </code></pre>
 * The
 * {@link FilterChainFactory#setEnabledFilters(FilterChainConfiguration, String)
 * setEnabledFilters} puts available filter instances to the chain. 
 * Corresponding configuration string located in CME under
 * "Application Parameters" of the client connection that was listed in
 * application "Connections" tab, for example:
 * <em>"log-filter =  name1,name2, nameN" ....</em> <br>
 * <br>
 * The
 * {@link FilterChainFactory#applyFilterOptions(FilterChainConfiguration, KeyValueCollection, KeyValueCollection,KeyValueCollection)
 * applyFilterOptions} creates or upgrades filter instances upon filter
 * definition, specified in CME, in the application "Options" tab.
 */
public final class FilterChainFactory {

    public static final String filter_key = "log-filter";
    public static final String no_trace = "skip-trace-msg";

    static ILogger log = Log.getMessageFilteringLogger();

    private static ConcurrentHashMap<String, FilterFactory> customFactories = new ConcurrentHashMap<String, FilterFactory>();
    private static final DefaultFilterFactory defaultFilterFactory = new DefaultFilterFactory();

    /**
     * Creates {@link FilterChainConfiguration}.
     * 
     * @param appDbid
     *            The application dbid where filter definitions are stored
     *            ("Options" tab in CME).
     * @param targetAppDbid
     *            Client connection id, for which filters are configured. Server
     *            dbid is used to identify the client connection. ("Connections"
     *            tab in CME)
     * @return Configured filter chain. 
     */
    public static FilterChainConfiguration create(int appDbid, int targetAppDbid) {
        FilterChainConfiguration chain = new FilterChainConfiguration(appDbid, targetAppDbid);
        return chain;
    }

    /**
     * Creates or upgrades filter instances upon filter definition, specified in
     * CME, in the application "Options" tab.
     * 
     * @param chain
     *            Filter Chain configuration, where available filter instances
     *            are stored.
     * @param addedOptions
     *            Added filter configuration options.
     * @param changedOptions
     *            Changed filter configuration options.
     * @param removedOptions
     *            Removed filter configuration options.
     * @return true if configuration option was applied.
     */
    public static boolean applyFilterOptions(FilterChainConfiguration chain,
            KeyValueCollection addedOptions, KeyValueCollection changedOptions,
            KeyValueCollection removedOptions) {

        if (chain == null) {
            throw new IllegalArgumentException("FilterChainConfiguration can't be null");
        }
        boolean updated = false;

        FilterCache filterCache = new FilterCache(chain.getFilterList());

        if (removedOptions != null) {
            updated = removeOptions(filterCache, removedOptions);
        }
        if (addedOptions != null) {
            updated |= applyOptions(filterCache, addedOptions);
        }
        if (changedOptions != null) {
            updated |= applyOptions(filterCache, changedOptions);
        }

        if (updated) {
            chain.applyFilterList(filterCache.getFilters());
        }
        return updated;

    }

    /**
     * Puts available filter instances to the chain. 
     * Corresponding configuration string located in CME under
     * "Application Parameters" of the client connection that was listed in
     * application "Connections" tab, for example:
     * <em>"log-filter =  name1,name2, nameN" ....</em> <br>
     * @param chain
     *            Chain configuration, where available filter instances are
     *            stored.
     * @param filterList List of filters, that should be put to the chain
     * @return true if filterList was parsed successfully
     */
    public static boolean setEnabledFilters(FilterChainConfiguration chain, String filterList) {

        if (chain == null) {
            throw new IllegalArgumentException("FilterChainConfiguration can't be null");
        }
                        
        List<EntryConfiguration> newList = parseFilterList(filterList);
        
        processSpecialFilter(newList, chain);
        
        return chain.setEnabledFilters(newList);

    }

    private static boolean applyOptions(FilterCache filterCache, KeyValueCollection section) {

        boolean updated = false;
        KeyValueCollection options;

        for (Object obj : section) {
            Pair pair = (Pair) obj;
            String name = FilterFactory.parseName(pair.getStringKey());
            if (name != null) {
                options = getOptions(pair);
                if (options != null) {
                    updated = modifyFilter(filterCache, name, options, false);
                    if (!updated) {
                        updated = createFilter(filterCache, name, options);
                    }
                }
            }
        }
        return updated;
    }

    private static boolean removeOptions(FilterCache filterCache, KeyValueCollection section) {

        boolean updated = false;
        KeyValueCollection options;

        for (Object obj : section) {
            Pair pair = (Pair) obj;
            String name = FilterFactory.parseName(pair.getStringKey());
            if (name != null) {
                options = getOptions(pair);
                if (options == null) {
                    continue;
                }
                if (options.size() > 0) {
                    updated = modifyFilter(filterCache, name, options, true);
                } else {
                    updated = removeFilter(filterCache, name);
                }
            }
        }
        return updated;
    }

    private static boolean createFilter(FilterCache filterCache, String name,
            KeyValueCollection options) {
        boolean created = false;

        FilterFactory factory = resolve(options);
        if (factory != null) {
            BaseFilter filter = factory.create(name, options);
            if (filter != null) {
                filterCache.put(filter);
                created = true;
            } else if (log.isWarn()) {
                log.warn("Can't resolve factory for filter " + name);
            }
        } else {
            if (log.isWarn()) {
                log.warn("Can't resolve factory for filter " + name);
            }

        }
        return created;
    }

    private static boolean modifyFilter(FilterCache filterCache, String name,
            KeyValueCollection options, boolean removeOptions) {
        boolean modified = false;
        BaseFilter filter = filterCache.get(name);
        if (filter != null) {
            modified = proccessOptions(filter, options, removeOptions);
            if (modified && log.isDebug()) {
                log.debugFormat("Modified filter {0} options", name);
            }
        }
        return modified;
    }

    private static boolean removeFilter(FilterCache filterCache, String name) {
        boolean removed = filterCache.remove(name);
        if (removed && log.isDebug()) {
            log.debugFormat("Remove filter {0}", name);
        }
        return removed;
    }

    private static KeyValueCollection getOptions(Pair pair) {
        Object value = pair.getValue();
        KeyValueCollection options = value instanceof KeyValueCollection ? (KeyValueCollection) value
                : null;
        if (options == null) {
            if (log.isWarn()) {
                log.warn("Expected KeyValueCollection options list in pair " + pair);
            }
        }
        return options;
    }

    private static boolean proccessOptions(BaseFilter filter, KeyValueCollection options,
            boolean remove) {
        boolean processed = false;
        FilterFactory factory = resolve(filter);
        if (factory != null) {
            for (Object obj : options) {
                Pair pair = (Pair) obj;
                String key = pair.getStringKey();
                String value = pair.getStringValue();
                if (key == null || value == null) {
                    if (log.isWarn()) {
                        log.warn("Expected string key adn value in pair " + pair.toString());
                    }
                } else {
                    if (remove) {
                        processed = factory.removeKeyValueOption(filter, key);
                    } else {
                        processed = factory.applyKeyValueOption(filter, key, value);
                    }
                }
            }
        } else {
            if (log.isWarn()) {
                log.warn("Can't resolve factory for filter " + filter.getKey());
            }
        }

        return processed;
    }
 

    /**
     * Parses string that defines what filters assigned to the protocol in the
     * Connections tab: log-filter = filter_1, filter_2, !filter_3;
     * Creates {@link EntryConfiguration} for every name.<br/>
     * Used by {@link #setEnabledFilters(FilterChainConfiguration, String)}.
     *
     * @param filterList the filters list.
     * @return list of parsed filter entries.
     */
    public static List<EntryConfiguration> parseFilterList(String filterList) {
        List<EntryConfiguration> result = new ArrayList<EntryConfiguration>();
        if (filterList == null)
            return result;

        int index = filterList.indexOf(filter_key);
        if (index == -1)
            return result;
        filterList = filterList.substring(index + filter_key.length());

        boolean beginFound = false;

        StringBuilder filterName = new StringBuilder();
        boolean negative = false;
        FilterResult filterResult = FilterResult.Accept;

        Token token = Token.NONE;
        while (index < filterList.length() && token!=Token.END) {

            char ch = filterList.charAt(index);
            token = getToken(ch);
                         
            if (beginFound) {
                if (token == Token.CHAR ||
                        ((filterName.length() > 0)
                            && (token == Token.NEGATIVE || token == Token.DENY))) {
                    filterName.append(ch);
                } 
                else if (token == Token.SEPARATOR || token == Token.END) {
                    if (filterName.length() > 0) {
                        result.add(new EntryConfiguration(filterName.toString(), negative, filterResult));
                        filterName.setLength(0);
                    }
                    negative = false;
                    filterResult = FilterResult.Accept;
                } 
                else if (token == Token.NEGATIVE) {
                    negative = true;
                } 
                else if (token == Token.DENY) {
                    filterResult = FilterResult.Deny;
                }
            }
            else if (token == Token.BEGIN) {
                beginFound = true;
            }
            index++;
        }

        if  (filterName.length() > 0) {
            result.add(new EntryConfiguration(filterName.toString(), negative, filterResult));
        }

        return result;
    }

    private static Token getToken(char ch) {
        if(ch=='=') {
            return Token.BEGIN;
        }
        else if(ch==','||ch==' ') {
            return Token.SEPARATOR;
        }
        else if(ch=='!') {
            return Token.NEGATIVE;
        }
        else if(ch=='-') {
            return Token.DENY;
        }
        else if(ch==';') {
            return Token.END;
        }
        else {
            return Token.CHAR;
        }
    }

    private enum Token {
        BEGIN,
        SEPARATOR,
        NEGATIVE,
        DENY,
        CHAR,
        END,
        NONE
    }

    private static void processSpecialFilter(
            final List<EntryConfiguration> newList,
            final FilterChainConfiguration chain) {
        if (newList != null) {
            Iterator<EntryConfiguration> it = newList.iterator();
            while (it.hasNext()) {
                EntryConfiguration entry = it.next();
                if (no_trace.equals(entry.getFilterName())) {
                    it.remove();
                    chain.setCanTrace(false);
                    // do not trace if extra filter is added
                    return;
                }
            }
        }
        chain.setCanTrace(true);
    }

    /**
     * Registers custom filter factory.
     * @param name Name of the factory
     * @param factory Factory instance
     */
    public static void setCustomFilterFactory(String name, FilterFactory factory) {
        if (name == null)
            throw new IllegalArgumentException("factory name can't be null");
        if (factory == null)
            throw new IllegalArgumentException("factory can't be null");

        customFactories.put(name, new DefaultFilterFactory());
    }


    /**
     * Resolves factory for a filter definition.
     * @param options Filter definition from CME
     */
    public static FilterFactory resolve(KeyValueCollection options) {
        if (options != null) {
            String factoryName = options.getString(FilterFactory.filter_type);
            if (factoryName != null) {
                return customFactories.get(factoryName);
            }
        }
        return defaultFilterFactory;
    }

    /**
     * Resolves factory for a filter object.
     * @param filter Filter object
     */
    public static FilterFactory resolve(BaseFilter filter) {
        if (filter == null)
            return null;
        if (filter instanceof CustomTypeSupport) {
            CustomTypeSupport ct = (CustomTypeSupport) filter;
            String type = ct.getCustomType();
            if (type != null) {
                return customFactories.get(type);
            } else {
                return null;
            }
        }
        return defaultFilterFactory;
    }

    /**
     * Helps to avoid repeatable filter cloning
     * 
     */
    private static class FilterCache {
        private List<BaseFilter> filters;
        private List<String> updated = new ArrayList<String>();

        public FilterCache(List<BaseFilter> filters) {
            this.filters = filters;
        }

        public BaseFilter get(String name) {

            for (int i = 0; i < filters.size(); i++) {
                BaseFilter filter = filters.get(i);
                if (filter.getKey().equals(name)) {
                    if (updated.contains(name)) {
                        return filter;
                    } else {
                        filter = (BaseFilter) filter.clone();
                        updated.add(name);
                        filters.set(i, filter);
                        return filter;
                    }
                }
            }
            return null;
        }

        public void put(BaseFilter filter) {
            updated.add(filter.getKey());
            filters.add(filter);
        }

        public boolean remove(String name) {
            boolean removed = false;
            Iterator<BaseFilter> it = filters.iterator();
            while (it.hasNext()) {
                BaseFilter filter = it.next();
                if (filter.getKey().equals(name)) {
                    it.remove();
                    updated.remove(name);
                    removed = true;
                }
            }
            return removed;
        }

        public List<BaseFilter> getFilters() {
            return filters;
        }

    }

    /**
     * Represents entry from the list of filters,
     * assigned to the protocol in the "Connection" tab of application configuration window.
     * In addition to the filter name, entry can have prefix '!' and '-'.
     * The '!' inverts result, returned by the {@link BaseFilter#isMessageAccepted(com.genesyslab.platform.commons.protocol.Message, com.genesyslab.platform.apptemplate.filtering.impl.FilterContext) isMessageAccepted()}.
     * The '-' means that filter will be processed as 'deny' filter in the chain.
     */
    public static class EntryConfiguration {

        private final String name;
        private final boolean negation;
        private final FilterResult result;

        public EntryConfiguration(
                final String filterName,
                final boolean negation,
                final FilterResult result) {
            super();
            if (filterName == null) {
                throw new IllegalArgumentException("The filterName can't be null");
            }
            this.name = filterName;
            this.negation = negation;
            this.result = result;
        }

        public String getFilterName() {
            return name;
        }

        public boolean isNegative() {
            return negation;
        }

        public FilterResult getResult() {
            return result;
        }

        @Override
        public int hashCode() {
            int hash = 7 * name.hashCode();
            if (result != null) {
                hash ^= 11 * (result.ordinal() + 1);
            }
            if (negation) {
                hash *= 13;
            }
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof EntryConfiguration)) {
                return false;
            }

            EntryConfiguration entry = (EntryConfiguration) obj;

            return name.equals(entry.getFilterName())
                    && (negation == entry.isNegative())
                    && result.equals(entry.getResult());
        }
    }
}
