/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. 
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.apptemplate.filtering.impl;

import com.genesyslab.platform.commons.protocol.Message;


/**
 * Filter action. 
 */
public abstract class FilterAction implements Cloneable {

    private final String key;


    public FilterAction(String key) {
        super();
        if (key == null) {
            throw new NullPointerException("key argument is null");
        }
        this.key = key;
    }

    /**
     * Gets action idetification key.
     *
     * @return action idetification key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Executes action.
     * @param message Message is used by actions to initialize filter variables.
     * @param context Filter context where variables are stored.
     */
    public abstract void execute(Message message, FilterContext context);


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


    @Override
    public Object clone() {
        FilterAction obj;
        try {
            obj = (FilterAction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError("it shouldn't be happend");
        }
        return obj;
    }
}
