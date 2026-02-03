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

// Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.applicationblocks.commons.Action;

import java.util.Collection;


/**
 * The base class for all types of Configuration Server queries.
 */
public class CfgQuery<TT extends ICfgObject> implements ICfgQuery<TT> {

    IConfService confService = null;

    /**
     * Creates a new instance of the class.
     */
    public CfgQuery() {
    }

    /**
     * Creates a new instance of the class.
     *
     * @param confService reference to IConfService to be used for query execution.
     * @deprecated
     */
    @Deprecated
    public CfgQuery(final IConfService confService) {
        this.confService = confService;
    }

    /**
     * Executes the query and returns a list of objects read from the configuration server.
     * It may return null if no objects read.
     *
     * @param <T> The type of configuration object returned
     * @return A collection of configuration objects or null
     * @deprecated Use {@link IConfService#retrieveMultipleObjects} directly.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends ICfgObject> Collection<T> execute(
            final Class<T> clazz)
                throws ConfigException, InterruptedException {
        if (confService == null) {
            throw new IllegalArgumentException(
                    "The query hasn't been initialized with a reference to IConfService");
        }

        return confService.retrieveMultipleObjects(clazz, (CfgQuery<T>)this);
    }

    /**
     * Executes the query and returns a list of objects read from the configuration server.
     * It may return null if no objects read.
     *
     * @return A collection of configuration objects or null
     */
    public Collection<? extends ICfgObject> execute()
                throws ConfigException, InterruptedException {
        if (confService == null) {
            throw new IllegalArgumentException(
                    "The query hasn't been initialized with a reference to IConfService");
        }

        return confService.retrieveMultipleObjects(null, this);
    }

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param <T> the type of object returned
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return AsyncRequestResult describing the current operation
     * @deprecated Use {@link IConfService#beginRetrieveMultipleObjects} directly.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends ICfgObject> AsyncRequestResult<T> beginExecute(
            final Class<T> clazz,
            final Action<AsyncRequestResult<T>> callback,
            final Object state)
                throws ConfigException {
        // todo handle 'state'?
        if (confService == null) {
            throw new IllegalArgumentException(
                    "The query hasn't been initialized with a reference to IConfService");
        }

        return confService.beginRetrieveMultipleObjects(clazz, (CfgQuery<T>)this, callback);
    }

    /**
     * Called to retrieve the result of asynchronous BeginExecute operation.
     * Should be called on execution of AsyncCallback passed to BeginExecute.
     * Will block calling thread until results are received if called before operation is completed.
     *
     * @param <T> the type of object returned
     * @param asyncResult The AsyncRequestResult object used to track the current request
     * @return A list of retrieved objects or null
     * @deprecated Use {@link IConfService#endRetrieveMultipleObjects} directly.
     */
    @Deprecated
    public <T extends ICfgObject> Collection<T> endExecute(
            final AsyncRequestResult<T> asyncResult)
                throws ConfigException, InterruptedException {
        if (confService == null) {
            throw new IllegalArgumentException(
                    "The query hasn't been initialized with a reference to IConfService");
        }

        return confService.endRetrieveMultipleObjects(asyncResult);
    }

    /**
     * Executes a query the result of which is a single object of the specified type.
     * Exception will be thrown if multiple objects are returned by the configuration server.
     *
     * @param <T> the type of object returned
     * @return a configuration object or null
     * @deprecated Use {@link IConfService#retrieveObject} directly.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public <T extends ICfgObject> T executeSingleResult(
            final Class<T> clazz)
                throws ConfigException {
        if (confService == null) {
            throw new IllegalArgumentException(
                    "The query hasn't been initialized with a reference to IConfService");
        }

        return confService.retrieveObject(clazz, (CfgQuery<T>)this);
    }

    /**
     * Executes a query the result of which is a single object of the specified type.
     * Exception will be thrown if multiple objects are returned by the configuration server.
     *
     * @return a configuration object or null
     */
    public ICfgObject executeSingleResult()
                throws ConfigException {
        if (confService == null) {
            throw new IllegalArgumentException(
                    "The query hasn't been initialized with a reference to IConfService");
        }

        return confService.retrieveObject(this);
    }
}
