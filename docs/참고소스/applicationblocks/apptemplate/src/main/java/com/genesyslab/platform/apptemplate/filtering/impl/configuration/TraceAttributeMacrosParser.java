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


import com.genesyslab.platform.apptemplate.filtering.impl.FilterAction;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterGroup;
import com.genesyslab.platform.apptemplate.filtering.impl.actions.FilterDelAction;
import com.genesyslab.platform.apptemplate.filtering.impl.actions.FilterPutAction;
import com.genesyslab.platform.apptemplate.filtering.impl.conditions.ContainsAny;
import com.genesyslab.platform.apptemplate.filtering.impl.filters.DefaultFilter;
import com.genesyslab.platform.apptemplate.filtering.impl.operands.AttributeOperand;
import com.genesyslab.platform.apptemplate.filtering.impl.operands.VariableOperand;

/**
 * Parses "trace-on-attribute" and "trace-until-message-type"
 * options and store under FilterGroup a set of sub filters.
 * This complex structure allows to store attribute value from one message
 * and use it to trace another messages.
 * Options sample:
 * <pre><code>
 *  message-type = RequestOpenStatistic
 *  ...
 *  trace-on-attribute = ReferenceId
 *  trace-until-message-type = EventStatisticClosed
 * </code></pre>
 *  Result {@link FilterGroup} structure:
 *  <pre><code>
 *      /FilterGroup
 *          /filter: default-filter
 *              /condition: check for RequestOpenStatistic message 
 *              /action:    store ReferenceId value
 *          /filter: trace-on-attribute
 *              /condition: check for saved ReferenceId attribute
 *          /filter: trace-until-message-type 
 *              /condition: check for EventStatisticClosed message 
 *              /action:    clear stored ReferenceId value 
 *  </code></pre>
 */
class TraceAttributeMacrosParser extends FilterConfigurationParser {

    private ConditionParser conditionParser;
    
    /**
     * Creates parser
     */
    public TraceAttributeMacrosParser() {                
        conditionParser = new ConditionParser();
    }
    
    /**
     * Creates with custom {@link ConstantValueParser} and {@link MessageTypeParser}
     * @param constantParser custom constants parser.
     * @param messageTypeParser custom message type parser.
     */
    public TraceAttributeMacrosParser(ConstantValueParser constantParser,
            ConditionParser conditionParser) { 
        super(constantParser);
        this.conditionParser = conditionParser;
    }

    @Override
    public void apply(FilterGroup filterGroup, String filterName, String key, String value) {
        key = key.trim();
        value = value.trim();

        if (key.equals(DefaultFilterFactory.trace_on_key)) {
            applyVariableActionPut(filterGroup, value);
            applyFilterTraceCondition(filterGroup, value);
            applyVariableActionDel(filterGroup);
        }

        if (key.equals(DefaultFilterFactory.trace_off_key)) {
            applyFilterClearCondition(filterGroup, value);
            applyVariableActionDel(filterGroup);
        }
    }

    @Override
    public void remove(FilterGroup filterGroup, String filterName, String key) {
        if (key.equals(DefaultFilterFactory.trace_on_key)) {
            removeVariableActionPut(filterGroup);
            removeFilterTraceCondition(filterGroup);
            removeVariableActionDel(filterGroup);
        }
        if (key.equals(DefaultFilterFactory.trace_off_key)) {
            removeClearFilter(filterGroup);
        }
    }

    private void applyFilterTraceCondition(FilterGroup filterGroup, String traceAttributeName) {
        DefaultFilter traceFilter = findOrRegisterNew(filterGroup,
                DefaultFilterFactory.trace_on_key);
        VariableOperand variable = new VariableOperand(traceAttributeName);
        AttributeOperand attribute = new AttributeOperand(traceAttributeName);
        ContainsAny traceCondition = new ContainsAny(DefaultFilterFactory.trace_on_key, attribute,
                variable, false);

        putCondition(traceFilter, traceCondition);

    }

    private void removeFilterTraceCondition(FilterGroup filterGroup) {
        filterGroup.removeFilter(DefaultFilterFactory.trace_on_key);
    }

    private void applyFilterClearCondition(FilterGroup filterGroup, String messageName) {
        
        conditionParser.apply(filterGroup, DefaultFilterFactory.trace_off_key, "message-type", messageName);
        
    }

    private void applyVariableActionPut(FilterGroup filterGroup, String traceAttributeName) {

        DefaultFilter defaultFilter = findOrRegisterNew(filterGroup,
                DefaultFilterFactory.default_filter);
        traceAttributeName = traceAttributeName.replaceAll("@", "");
        AttributeOperand attribute = new AttributeOperand(traceAttributeName);
        FilterPutAction actionPut = new FilterPutAction(DefaultFilterFactory.trace_on_key,
                traceAttributeName, attribute);
        putAction(defaultFilter, actionPut);
    }

    private void removeVariableActionPut(FilterGroup filterGroup) {
        DefaultFilter defaultFilter = find(filterGroup, DefaultFilterFactory.default_filter);
        if (defaultFilter != null) {
            removeAction(defaultFilter, DefaultFilterFactory.trace_on_key);
        }
    }

    private void applyVariableActionDel(FilterGroup filterGroup) {

        String traceVarID = getVariableName(filterGroup);

        if (traceVarID != null) {
            DefaultFilter clearFilter = find(filterGroup, DefaultFilterFactory.trace_off_key);
            if (clearFilter != null) {
                FilterDelAction variableClearAction = new FilterDelAction(
                        DefaultFilterFactory.trace_off_key, traceVarID, new AttributeOperand(
                                traceVarID)); // var name equals attribute name
                putAction(clearFilter, variableClearAction);
            }
        }
    }

    private String getVariableName(FilterGroup filterGroup) {
        String varID = null;
        DefaultFilter defaultFilter = find(filterGroup, DefaultFilterFactory.default_filter);

        if (defaultFilter != null) {
            FilterAction act = findAction(defaultFilter, DefaultFilterFactory.trace_on_key);
         
            if(act instanceof FilterPutAction) {
                varID = ((FilterPutAction)act).getVarId();                
            }               
        }
        return varID;
    }

    private void removeVariableActionDel(FilterGroup filterGroup) {
        DefaultFilter clearFilter = find(filterGroup, DefaultFilterFactory.trace_off_key);
        if (clearFilter != null) {
            removeAction(clearFilter, DefaultFilterFactory.trace_off_key);
        }
    }

    private void removeClearFilter(FilterGroup filterGroup) {
        filterGroup.removeFilter(DefaultFilterFactory.trace_off_key);
    }

}
