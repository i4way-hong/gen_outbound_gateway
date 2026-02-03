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
 * The base interface for all queries.
 */
public interface ICfgQuery<TT extends ICfgObject> {

    /**
     * Executes the query and returns a list of objects read from the configuration server.
     * It may return null if no objects read.
     *
     * @param <T> The type of configuration object returned
     * @param clazz class used for generic matching
     * @return A collection of configuration objects or null
     * @Deprecated Use {@link IConfService#retrieveMultipleObjects} directly.
     */
	@Deprecated
    <T extends ICfgObject> Collection<T> execute(
            final Class<T> clazz)
                throws ConfigException, InterruptedException;

    /**
     * Begins the asynchronous execution of the current query.
     *
     * @param clazz class used for generic matching
     * @param callback The method to be called when query results are available
     * @param state a user-defined object that qualifies or contains information about an asynchronous operation
     * @return IAsyncResult describing the current operation
     * @Deprecated Use {@link IConfService#beginRetrieveMultipleObjects} directly.
     */
	@Deprecated
    <T extends ICfgObject> AsyncRequestResult<T> beginExecute(
            final Class<T> clazz,
            final Action<AsyncRequestResult<T>> callback,
            final Object state)
                throws ConfigException;

    /**
     * Called to retrieve the result of asynchronous BeginExecute operation.
     * Should be called on execution of AsyncCallback passed to BeginExecute. Will block
     * calling thread until results are received if called before operation is completed.
     *
     * @param asyncResult The IAsyncResult object used to track the current request
     * @return A list of retrieved objects or null
     * @Deprecated Use {@link IConfService#endRetrieveMultipleObjects} directly.
     */
	@Deprecated
    <T extends ICfgObject> Collection<T> endExecute(
            final AsyncRequestResult<T> asyncResult)
                throws ConfigException, InterruptedException;

    /**
     * Executes a query the result of which is a single object of the specified type.
     * Exception will be thrown if multiple objects are returned by the configuration server.
     *
     * @param <T> the type of object returned
     * @param clazz class used for generic matching
     * @return a configuration object or null
     * @Deprecated Use {@link IConfService#retrieveObject} directly.
     */
	@Deprecated
    <T extends ICfgObject> T executeSingleResult(
            final Class<T> clazz)
                throws ConfigException;
}
