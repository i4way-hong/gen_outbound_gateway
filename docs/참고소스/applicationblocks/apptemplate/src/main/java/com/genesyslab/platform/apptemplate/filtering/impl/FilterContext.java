/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. 
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.apptemplate.filtering.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;


/**
 * Storage for filter variables. Variables can be compared with message attribute values,
 * to evaluate if message can be logged or not.
 */
public class FilterContext implements Cloneable {

    static ILogger log = Log.getMessageFilteringLogger();

    private static class FilterVariable {
        final ValueList values = new ValueList();
        int refs;
    }

    private final Object syncRegVars = new Object();
    private ConcurrentHashMap<String, FilterVariable> vars = new ConcurrentHashMap<String, FilterVariable>();


    /**
     * Gets filter variable by id.
     * @param id of filter variable.
     * @return filter variable if exists. 
     */
    public ValueList getVariable(String id) {
        FilterVariable fvar = vars.get(id);
        if (fvar != null) {
            return fvar.values;
        }
        return null;
    }

    public void clearAllVariables() {
        synchronized(syncRegVars) {
            for(FilterVariable v : vars.values()) {
                v.values.clear();
            }
        }
    }

    /**
     * Registers filter variable.
     * @param id of filter variable.
     */
    public void registerVariable(String id) {
        synchronized(syncRegVars) {
            FilterVariable var = vars.get(id);
            if (var == null) {
                var = new FilterVariable();
                vars.put(id, var);
            }
            var.refs++;
        }
    }

    /**
     * Unregisters filter variable. 
     * @param id of filter variable.
     */
    public void unregisterVariable(String id) {
        synchronized(syncRegVars) {
            FilterVariable var = vars.get(id);
            if (var != null) {
                if (var.refs == 0) {
                    if (log.isDebug()) {
                        log.errorFormat("unpaired call of register/unregister for variable {0} in filter context", id);
                    }
                }
                if (--var.refs == 0) {
                    vars.remove(id);
                }
            }
            else {
                if (log.isDebug()) {
                    log.errorFormat("removing unexists variable {0} form filter context", id);
                }
            }
        }
    }

    @Override
    protected Object clone() {
        synchronized (syncRegVars) {
            FilterContext obj;
            try {
                obj = (FilterContext)super.clone();
            }
            catch(CloneNotSupportedException ex) {
                throw new InternalError("it shouldn't be happend");
            }
            return obj;
        }
    }
}
