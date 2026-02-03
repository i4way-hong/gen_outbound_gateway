package com.genesyslab.platform.apptemplate.filtering.impl;

import com.genesyslab.platform.commons.protocol.Message;


/**
 * FilterCondition is a filter predicate. Returned value of
 * the predicate is used to evaluate whether message can be logged or not.
 * FilterCondition has two operands. Fore example, 
 * one operand can be a message name or an attribute value, 
 * second can be a constant value or a filter variable. See {@link FilterOperand}.
 */
public abstract class FilterCondition implements Cloneable{

    private String key;
    private FilterOperand op1;
    private FilterOperand op2;
    private boolean negative;

    /**
     * Creates message filter condition.
     *
     * @param key message filter key.
     * @trows NullPointerException if key argument is null.
     */
    public FilterCondition(String key, FilterOperand op1, FilterOperand op2, boolean negative) {
        super();
        if (key == null) {
            throw new NullPointerException("key argument is null");
        }
        if (op1 == null) {
            throw new NullPointerException("op1 is null");
        }
        if (op2 == null) {
            throw new NullPointerException("op2 is null");
        }
        this.key = key;
        this.op1 = op1;
        this.op2 = op2;
        this.negative = negative;
    }


    /**
     * Gets message filter condition key.
     * @return message filter condition key.
     */
    public String getKey() {
        return key;
    }


    /**
     * The result of condition can be inverted.
     *
     * @return <code>true</code> if result is inverted.
     */
    public boolean isNegative() {
        return negative;
    }


    /**
     * First operand (argument) of the logical operator (condition).
     *
     * @return FilterOperand
     */
    public FilterOperand getOp1() {
        return op1;
    }

    /**
     * Second operand (argument) of the logical operator (condition).
     *
     * @return FilterOperand
     */
    public FilterOperand getOp2() {
        return op2;
    }


    /**
     * Evaluates condition for specified message and context.
     *
     * @param message message for filtering.
     * @param context the context containing filter variables.
     * @return <code>true</code> if condition completed successfully.
     */
    public abstract boolean evaluate(Message message, FilterContext context);


    @Override
    public Object clone() {
        FilterCondition obj;
        try {
            obj = (FilterCondition) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError("it shouldn't be happend");
        }
        obj.op1 = (FilterOperand) op1.clone(); 
        obj.op2 = (FilterOperand) op2.clone(); 
        return obj;
    }


    /**
     * Registers filter variables and so on.
     *
     * @param context filter context.
     */
    public void register(FilterContext context) {
        op1.register(context);
        op2.register(context);
    }

    /**
     * Unregisters filter variables and so on.
     *
     * @param context filter context.
     */
    public void unregister(FilterContext context) {
        op1.unregister(context);
        op2.unregister(context);
    }
}
