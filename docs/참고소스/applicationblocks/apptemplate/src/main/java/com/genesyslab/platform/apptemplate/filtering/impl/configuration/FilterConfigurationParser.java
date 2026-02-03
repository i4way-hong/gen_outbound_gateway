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

import com.genesyslab.platform.apptemplate.filtering.impl.BaseFilter;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterAction;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterCondition;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterGroup;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterOperand;
import com.genesyslab.platform.apptemplate.filtering.impl.filters.DefaultFilter;
import com.genesyslab.platform.apptemplate.filtering.impl.operands.MessageTypeOperand;

/**
 * Base class for the filter option parsers. Filter option is a pair of string
 * key and value, for example:
 * 
 * <pre>
 * <code>
 *   message-type : EventRinging
 *   &#64;ThisDN : 100
 *  </code>
 * </pre>
 * 
 * The FilterConfigurationParser can create {@link FilterCondition} or {@link FilterAction} objects
 * upon these strings.
 * 
 */
public abstract class FilterConfigurationParser {

    protected ConstantValueParser constantParser;

    /**
     * Creates FilterConfigurationParser object
     */
    public FilterConfigurationParser() {
        this(new ConstantValueParser());
    }

    /**
     * Creates FilterConfigurationParser with custom {@link ConstantValueParser}
     */
    public FilterConfigurationParser(ConstantValueParser constantParser) {
        this.constantParser = constantParser;
    }

    /**
     * Parses option and applies result to the {@link FilterGroup} object
     * 
     * @param filter The filter group object
     * @param filterName name of subfilter in the filter group where parsed result should be stored.
     * @param key Configuration option key
     * @param value Configuration option value
     */
    public abstract void apply(FilterGroup filter, String subfilter, String key, String Value);

    /**
     * Removes option from the {@link FilterGroup} object
     * 
     * @param filter The filter group object
     * @param filterName name of subfilter from which option should be removed.
     * @param key Configuration option key
     */
    public abstract void remove(FilterGroup filter, String subfilter, String key);

    protected DefaultFilter findOrRegisterNew(FilterGroup filterGroup, String subfilter) {
        DefaultFilter df = find(filterGroup, subfilter);
        if (df != null)
            return df;
        df = new DefaultFilter(subfilter);
        filterGroup.putFilter(subfilter, df);
        return df;
    }

    protected DefaultFilter find(FilterGroup filterGroup, String subfilter) {
        BaseFilter bf = filterGroup.getFilter(subfilter);
        if (bf instanceof DefaultFilter)
            return (DefaultFilter) bf;
        return null;
    }

    protected static void putCondition(DefaultFilter filter, FilterCondition condition) {
        ArrayList<FilterCondition> conditions = filter.getConditions();
        boolean replaced = false;
        for (int i = 0; i < conditions.size(); i++) {
            FilterCondition existing = conditions.get(i);
            String key = existing.getKey();
            if (key != null && key.equals(condition.getKey())) {
                conditions.set(i, condition);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            conditions.add(condition);
        }
    }
    
    protected static void setCondition(DefaultFilter filter, FilterCondition condition, int index) {
        ArrayList<FilterCondition> conditions = filter.getConditions();
        if (conditions.size() > 0) {
            FilterCondition existing = conditions.get(index);
            if (existing != null) {
                FilterOperand op1 = existing.getOp1();
                if (op1 instanceof MessageTypeOperand) {
                    conditions.set(index, condition);
                } else {
                    conditions.add(index, condition);
                }
            }
        } 
    }

    protected static void removeCondition(DefaultFilter filter, String key) {
        ArrayList<FilterCondition> conditions = filter.getConditions();
        Iterator<FilterCondition> it = conditions.iterator();

        while (it.hasNext()) {
            FilterCondition condition = it.next();
            String existingKey = condition.getKey();
            if (existingKey != null && existingKey.equals(key)) {
                it.remove();
            }
        }
    }

    protected static FilterCondition findCondition(DefaultFilter filter, String key) {
        ArrayList<FilterCondition> conditions = filter.getConditions();
        Iterator<FilterCondition> it = conditions.iterator();

        while (it.hasNext()) {
            FilterCondition condition = it.next();
            String existingKey = condition.getKey();
            if (existingKey != null && existingKey.equals(key)) {
                return condition;
            }
        }
        return null;
    }

    protected static void putAction(DefaultFilter filter, FilterAction action) {
        ArrayList<FilterAction> actions = filter.getActions();

        boolean replaced = false;
        for (int i = 0; i < actions.size(); i++) {
            FilterAction existing = actions.get(i);
            String key = existing.getKey();
            if (key != null && key.equals(action.getKey())) {
                actions.set(i, action);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            actions.add(action);
        }       

    }

    protected static void removeAction(DefaultFilter filter, String key) {
        ArrayList<FilterAction> actions = filter.getActions();
        Iterator<FilterAction> it = actions.iterator();

        while (it.hasNext()) {
            FilterAction action = it.next();
            String existingKey = action.getKey();
            if (existingKey != null && existingKey.equals(key)) {
                it.remove();
            }
        }

    }

    protected static FilterAction findAction(DefaultFilter filter, String key) {

        ArrayList<FilterAction> actions = filter.getActions();
        Iterator<FilterAction> it = actions.iterator();

        while (it.hasNext()) {
            FilterAction action = it.next();
            String existingKey = action.getKey();
            if (existingKey != null && existingKey.equals(key)) {
                return action;
            }
        }
        return null;
    }
}
