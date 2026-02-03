package com.genesyslab.platform.apptemplate.filtering.impl;

import com.genesyslab.platform.commons.protocol.Message;


/**
 * FilterOperand is an argument of the filter predicate. Returned value of
 * the predicate is used to evaluate whether message can be logged or not.
 * See {@link FilterCondition}.
 */
public abstract class FilterOperand implements Cloneable {

    public abstract ValueList evaluate(Message message, FilterContext context); 

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError("it shouldn't be happend");
        }
    }


    /**
     * Registers filter variables and so on.
     *
     * @param context filter context.
     */
    public void register(FilterContext context) {
    }

    /**
     * Unregisters filter variables and so on.
     *
     * @param context filter context.
     */
    public void unregister(FilterContext context) {
    }
}
