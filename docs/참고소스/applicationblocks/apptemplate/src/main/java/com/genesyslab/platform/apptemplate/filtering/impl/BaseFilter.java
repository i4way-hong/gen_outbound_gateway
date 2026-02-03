/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. 
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.apptemplate.filtering.impl;

import java.util.concurrent.atomic.AtomicInteger;

import com.genesyslab.platform.commons.protocol.Message;


/**
 * Base class for filters that can be processed by a {@link FilterChain}
 */
public abstract class BaseFilter implements Cloneable {

    private String key;

    private transient AtomicInteger registrationCounter = new AtomicInteger(); 


    /**
     * Creates message filter.
     * @param key message filter key.
     * @trows NullPointerException if key argument is null.
     */
    public BaseFilter(String key) {
        super();
        if (key == null) {
            throw new NullPointerException("key argument is null");
        }
        this.key = key;
    }


    /**
     * Gets filter name.
     */
    public String getKey() {
        return key;
    }

    /**
     * Evaluates whether message is accepted by filter.
     * @param message The message to evaluate
     * @param context Filter context, where data for message evaluation, like filter variables, can be stored.
     * @return true if message should be accepted else - false.
     */
    public abstract boolean isMessageAccepted(Message message, FilterContext context);


    /**
     * Clones this message filter.
     * @return clone of this message filter.
     */
    public Object clone() {
        BaseFilter obj;
        try {
            obj = (BaseFilter) super.clone();
            obj.registrationCounter = new AtomicInteger();
        } catch(CloneNotSupportedException ex) {
            throw new InternalError("FilterOperand.clone: it's should be happend");
        }
        return obj;
    }    

    public boolean isRegistered() {
        return registrationCounter.get() > 0;
    }

    /**
     * Registers filter variables and so on.
     *
     * @param context filter context.
     * @return filter variable
     */
    final void register(FilterContext context) {
        registrationCounter.incrementAndGet();
        doRegister(context);
    }

    /**
     * Unregisters filter variables and so on. 
     * @param context filter context.
     */
    final void unregister(FilterContext context) {
        try
        {
            doUnregister(context);
        }
        finally
        {
            registrationCounter.decrementAndGet();
        }
    }

    /**
     * Registers filter variables and so on.
     *
     * @param context filter context.
     */
    protected void doRegister(FilterContext context) {
    }

    /**
     * Unregisters filter variables and so on.
     *
     * @param context filter context.
     */
    protected void doUnregister(FilterContext context) {
    }
}
