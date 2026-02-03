/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. 
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.apptemplate.filtering.impl.filters;

import com.genesyslab.platform.apptemplate.filtering.impl.BaseFilter;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterAction;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterCondition;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterContext;

import com.genesyslab.platform.commons.protocol.Message;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Default message filter implementation (used in FilterChain).
 */
public final class DefaultFilter extends BaseFilter {

    private ArrayList<FilterCondition> conditions = new ArrayList<FilterCondition>();

    private ArrayList<FilterAction> actions = new ArrayList<FilterAction>();
    private boolean executeActionsOnMatch = true;


    public DefaultFilter(String key) {
        super(key);
    }


    @Override
    public boolean isMessageAccepted(Message message, FilterContext context) {

        boolean messageAccepted = conditions.size() > 0 ? true : false;

        Iterator<FilterCondition> it = conditions.iterator();
        while (it.hasNext() && messageAccepted == true) {
            FilterCondition condition = it.next();
            messageAccepted = condition.evaluate(message, context);
        }

        if (messageAccepted ^ !getExecuteActionsOnMatch()) {
            execActions(message, context);
        }

        return messageAccepted;
    }
    
    /**
     * Gets conditions.
     * @return conditions.
     */
    public ArrayList<FilterCondition> getConditions() {
        return (ArrayList<FilterCondition>) conditions;
    }
    
    /**
     * Applies new conditions.
     * @param newCondition new conditions.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalStateException when filter already registered (used).
     */
    public void applyConditions(ArrayList<FilterCondition> newConditions) throws NullPointerException {
        if (newConditions == null) {
            throw new NullPointerException("newConditions argument is null");
        }
        if (isRegistered()) {
            throw new IllegalStateException("can't modify used filter");
        }
        this.conditions = newConditions; 
    }
    
    /**
     * Gets current filter actions executions behaviour (executes on match/mismatch).
     * @return current filter actions executions behaviour.
     */
    public boolean getExecuteActionsOnMatch() {
        return executeActionsOnMatch;
    }
    
    /**
     * Executes filter actions.
     * @param context
     */
    public void execActions(Message message, FilterContext context) {
        for(FilterAction action : actions) {
            action.execute(message, context);
        }
    }
        
    
    /**
     * Gets actions list.
     * @return actions.
     */
    public ArrayList<FilterAction> getActions() {
        return (ArrayList<FilterAction>) actions;
    }
    
    
    /**
     * Applies new actions. 
     * @param newActions new actions.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalStateException when filter already registered (used).
     */
    public void applyActions(ArrayList<FilterAction> newActions) throws NullPointerException {
        
        if (newActions == null) {
            throw new NullPointerException("newActions argument is null");
        }
        if (isRegistered()) {
            throw new IllegalStateException("can't modify used filter");
        }
        
        this.actions = newActions;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        DefaultFilter obj = (DefaultFilter) super.clone();
        obj.conditions = (ArrayList<FilterCondition>) conditions.clone();
        int count = obj.conditions.size();
        for(int i=count-1; i>=0; i--) {
            FilterCondition condition = obj.conditions.get(i);
            obj.conditions.set(i, (FilterCondition) condition.clone());
        }

        obj.actions = new ArrayList<FilterAction>(actions.size());
        count = obj.actions.size();
        for(int i=0; i<count; i++) {
            FilterAction action = obj.actions.get(i);
            obj.actions.add((FilterAction) action.clone());
        }

        return obj;
    }

    
    /**
     * Registers filter variables and so on.
     * @param context filter context.
     * @return filter variable
     */
    protected void doRegister(FilterContext context) {
        super.doRegister(context);
        for(FilterCondition condition : conditions) {
            condition.register(context);
        }
        for(FilterAction action : actions) {
            action.register(context);
        }
    }
    
    
    /**
     * Unregisters filter variables and so on. 
     * @param context filter context.
     */
    protected void doUnregister(FilterContext context) {
        for(FilterCondition condition : conditions) {
            condition.unregister(context);
        }
        for(FilterAction action : actions) {
            action.unregister(context);
        }
        super.doUnregister(context);
    }      
}
