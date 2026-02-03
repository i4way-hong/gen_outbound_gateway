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

import com.genesyslab.platform.apptemplate.filtering.impl.BaseFilter;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterGroup;
import com.genesyslab.platform.apptemplate.filtering.impl.filters.DefaultFilter;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import com.genesyslab.platform.commons.collections.Pair;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * Factory for the {@link FilterGroup} objects. This is default filter implementation.
 * Accepts filter options in a {@link KeyValueCollection}.
 * <br>
 * Uses predefined {@link FilterConfigurationParser} objects to handle each option.
 * The result Filter Group may contain one or several {@link DefaultFilter} sub filters.
 * <br>
 * It is possible to customize factory by registering custom {@link FilterConfigurationParser} parser:
 * {@link DefaultFilterFactory#setCustomParser(String, FilterConfigurationParser) register}
 * 
 */
public class DefaultFilterFactory extends FilterFactory {

    public static final String attribute_key = "@";
    public static final String message_name_key = "message-type";
    public static final String inverse_key = "#not";
    public static final String trace_on_key = "trace-on-attribute";
    public static final String trace_off_key = "trace-until-message-type";
    public static final String trace_timeout = "trace-timeout";
    public static final String default_filter = "default-filter";

    private static final ILogger log = Log.getMessageFilteringLogger();

    private static HashMap<String, FilterConfigurationParser> registeredParsers = new HashMap<String, FilterConfigurationParser>();

    static {
        ConditionParser cp = new  ConditionParser();
        registeredParsers.put(attribute_key, cp);
        registeredParsers.put(message_name_key, cp);
        registeredParsers.put(inverse_key, cp);

        TraceAttributeMacrosParser tp = new TraceAttributeMacrosParser();
        registeredParsers.put(trace_on_key, tp);
        registeredParsers.put(trace_off_key, tp);
    }

    /**
     * Sets parser for the custom option. 
     * Expected the the option string must start with unique key that identifies this option.
     *
     * @param key Option identifier. 
     * @param parser Option parser for the provided key.
     */
    public static void setCustomParser(String key, FilterConfigurationParser parser) {
        if (key == null) {
            throw new IllegalArgumentException("parser key can't be null");
        }
        if (parser == null) {
            throw new IllegalArgumentException("parser can't be null");
        }
        if(registeredParsers.containsKey(key)) {
            if(log.isWarn()) {
                log.warn(key + "parser already registered. Will overwrite existing.");
            }
        }
        registeredParsers.put(key, parser);
        
    }
    
    @Override
    public FilterGroup create(String filterName, KeyValueCollection options) {
        if (filterName == null) {
            throw new IllegalArgumentException("filterName can't be null");
        }
        if (options == null) {
            throw new IllegalArgumentException("options can't be null");
        }

        FilterGroup filterGroup = new FilterGroup(filterName);
        DefaultFilter filter = new DefaultFilter(default_filter);
        filterGroup.putFilter(filterName, filter);

        Iterator<?> iterator = options.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            Pair pair = obj instanceof Pair ? (Pair) obj : null;
            if (pair == null) {
                if(log.isWarn()) {
                    log.warn("Expected that KeyValueCollection contains pair object");
                }
                continue;
            }
            String key = pair.getStringKey();
            String value = pair.getStringValue();

            if (key == null || value == null) {
                if(log.isWarn()) {
                    log.warn("Expected that KeyValueCollection options list contains String key and value");
                }
                continue;
            }
            applyKeyValueOption(filterGroup, key, value);
        }
        return filterGroup;
    }

    @Override
    public boolean applyKeyValueOption(BaseFilter filter, String key, String value) {
        if (filter == null) {
            throw new IllegalArgumentException("filter argument can't be null ");
        }
        
        if(filter instanceof FilterGroup) {
            return applyKeyValueOption((FilterGroup)filter, default_filter, key, value);
        }
        else {
            if(log.isWarn()) {
                log.warn("Expected that FilterGroup object, but got " + filter.getClass());
            }
            return false;
        }
    }
    
    @Override
    public boolean removeKeyValueOption(BaseFilter filter, String key) {
        if (filter == null) {
            throw new IllegalArgumentException("filter argument can't be null ");
        }
        
        if(filter instanceof FilterGroup) {
            return removeKeyValueOption((FilterGroup)filter, default_filter, key);
        }
        else {
            if(log.isWarn()) {
                log.warn("Expected that FilterGroup object, but got " + filter.getClass());
            }
            return false;
        }       
    }

    /**
     * Creates or modifies filter options.
     * @param filterGroup The filter group with subfilters.
     * @param subFilter Name of subfilter where option should be stored.
     * @param key Option name
     * @param value Option value
     * @return True if option was successfully parsed and applied to subfilter.
     */
    public boolean applyKeyValueOption(FilterGroup filterGroup, String subFilter, String key,
            String value) {
        if (filterGroup == null) {
            throw new IllegalArgumentException("filter argument can't be null ");
        }
        if (key == null) {
            throw new IllegalArgumentException("key argument can't be null ");
        }
        if (value == null) {
            throw new IllegalArgumentException("value argument can't be null ");
        }

        key = key.trim();

        Iterator<Entry<String, FilterConfigurationParser>> iter = registeredParsers.entrySet()
                .iterator();
        while (iter.hasNext()) {
            Entry<String, FilterConfigurationParser> entry = iter.next();
            String registeredWord = entry.getKey();
            if (key.startsWith(registeredWord)) {
                FilterConfigurationParser parser = entry.getValue();
                parser.apply(filterGroup, subFilter, key, value);                
                return true;
            }
        }
        
        if(log.isWarn()) {
            log.warn("Unknown filter option "+key+ ":"+value);
        }
        return false;

    }

    /**
     * Removes configuration option from the provided filter.
     * @param filterGroup The filter group with subfilters.
     * @param subFilter Name of subfilter from which option should be stored.
     * @param key Option name
     * @return True if option was successfully removed from subfilter.
     */
    public boolean removeKeyValueOption(FilterGroup filterGroup, String subFilter, String key) {
        if (filterGroup == null) {
            throw new IllegalArgumentException("filter argument can't be null ");
        }
        if (key == null) {
            throw new IllegalArgumentException("key argument can't be null ");
        }
        key = key.trim();

        Iterator<Entry<String, FilterConfigurationParser>> iter = registeredParsers.entrySet()
                .iterator();
        while (iter.hasNext()) {
            Entry<String, FilterConfigurationParser> entry = iter.next();
            String registeredWord = entry.getKey();
            if (key.startsWith(registeredWord)) {
                FilterConfigurationParser parser = entry.getValue();
                parser.remove(filterGroup, subFilter, key);
               return true;
            }
        }
        if(log.isWarn()) {
            log.warn("Unknown filter option "+key);
        }
        return false;
    }

}
