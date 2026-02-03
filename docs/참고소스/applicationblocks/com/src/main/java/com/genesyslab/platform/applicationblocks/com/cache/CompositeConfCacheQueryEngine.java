//===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:

// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.

// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.

// THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
// ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
// DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
// WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
// NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
// NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
// EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
// CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
// NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).

// Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.com.cache;

import com.genesyslab.platform.applicationblocks.com.ICfgQuery;
import com.genesyslab.platform.applicationblocks.com.ICfgObject;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;


/**
 * An implementation of the query engine interface which allows for
 * new query interpretation modules to be added at run time.
 */
public final class CompositeConfCacheQueryEngine
        implements IConfCacheQueryEngine {

    private final List<IConfCacheQueryEngine> queryEngines =
            new ArrayList<IConfCacheQueryEngine>();

    /**
     * Looks for a query engine in its list which is capable of executing
     * the specified query.
     *
     * @param query the query to execute
     * @return true if a capable query engine is found, false otherwise
     */
    public boolean canExecute(final ICfgQuery query) {
        for (IConfCacheQueryEngine queryEngine : queryEngines) {
            if (queryEngine.canExecute(query)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Looks for a query engine which is capable of executing the
     * specified query, and if found, uses it to retrieve an
     * object based on that query.
     *
     * @param <T> The type of object to retrieve
     * @param cls class of object to be retrieved
     * @param query The query to execute
     * @return An object matching the passed query
     * @throws IllegalArgumentException Thrown if no query engine available to execute the query
     */
    public <T extends ICfgObject> T retrieveObject(
            final Class<T> cls, final ICfgQuery query) {
        for (IConfCacheQueryEngine queryEngine : queryEngines) {
            if (queryEngine.canExecute(query)) {
                return queryEngine.retrieveObject(cls, query);
            }
        }
        throw new IllegalArgumentException("Unsupported query");
    }

    /**
     * Looks for a query engine which is capable of executing the
     * specified query, and if found, uses it to retrieve a
     * list of objects based on that query.
     *
     * @param <T> The type of object to retrieve
     * @param cls class of objects to be retrieved
     * @param query The query to execute
     * @return A list of objects matching the passed query
     * @throws IllegalArgumentException Thrown if no query engine available to execute the query
     */
    public <T extends ICfgObject> Collection<T> retrieveMultipleObjects(
            final Class<T> cls, final ICfgQuery query) {
        for (IConfCacheQueryEngine queryEngine : queryEngines) {
            if (queryEngine.canExecute(query)) {
                return queryEngine.retrieveMultipleObjects(cls, query);
            }
        }
        throw new IllegalArgumentException("Unsupported query");
    }

    /**
     * Looks for a query engine which returns a result using the parameters
     * passed. Returns the first obtained result.
     *
     * @param <T> The type of object to retrieve
     * @param cls class of object to be retrieved
     * @param type The type of object (CfgObjectType)
     * @param dbid The dbid of the object
     * @return A configuration object with the requested dbid and type
     */
    public <T extends ICfgObject> T retrieveObject(
            final Class<T> cls, final CfgObjectType type, final int dbid) {
        T obj = null;

        for (IConfCacheQueryEngine queryEngine : queryEngines) {
            try {
                obj = queryEngine.retrieveObject(cls, type, dbid);
            } catch (Exception e) {
                continue;
            }

            if (obj != null) {
                break;
            }
        }
        return obj;
    }


    /**
     * Registers a query engine module.
     *
     * @param queryEngine query engine module to register
     */
    public void register(final IConfCacheQueryEngine queryEngine) {
        synchronized (queryEngines) {
            queryEngines.add(queryEngine);
        }
    }

    /**
     * Unregisters a query engine module.
     *
     * @param queryEngine query engine module to unregister
     */
    public void unregister(final IConfCacheQueryEngine queryEngine) {
        synchronized (queryEngines) {
            queryEngines.remove(queryEngine);
        }
    }
}
