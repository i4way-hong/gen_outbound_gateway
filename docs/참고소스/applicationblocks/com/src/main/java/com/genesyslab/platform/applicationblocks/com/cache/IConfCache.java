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

import com.genesyslab.platform.applicationblocks.com.*;

import com.genesyslab.platform.applicationblocks.commons.Action;
import com.genesyslab.platform.applicationblocks.commons.broker.Subscriber;

import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import java.util.concurrent.Future;


/**
 * Interface for the configuration object cache.
 */
public interface IConfCache
        extends Subscriber<ConfEvent> {

    /**
     * Adds a new configuration object to the cache.
     *
     * @param obj A configuration object
     */
    void add(ICfgObject obj);

    /**
     * Overwrites a configuration object which already exists in the cache
     * with a new copy.
     *
     * @param obj A configuration object
     */
    void update(ICfgObject obj);

    /**
     * Removes the specified configuration object from the cache.
     *
     * @param obj A configuration object
     */
    void remove(ICfgObject obj);

    /**
     * Removes the configuration object with the specified type and dbid
     * from the cache.
     *
     * @param type The type of configuration object
     * @param dbid The dbid of the configuration object
     */
    void remove(CfgObjectType type, int dbid);

    /**
     * Removes all cache contents.
     */
    void clear();

    /**
     * Retrieves a configuration object from the cache.
     *
     * @param <T> The type of configuration object that should be returned
     * @param cls class of object to be retrieved
     * @param type The type of configuration object
     * @param dbid The dbid of the configuration object
     * @return A configuration object of the requested type
     */
    <T extends ICfgObject> T retrieve(
            Class<T> cls,
            CfgObjectType type,
            int dbid);

    /**
     * Retrieves a configuration object from the cache.
     *
     * @param <T> The type of configuration object that should be returned
     * @param cls class of object to be retrieved
     * @param query A query based on which the result is obtained
     * @return A configuration object matching the specified query
     */
    <T extends ICfgObject> T retrieve(
            Class<T> cls,
            ICfgQuery query);

    /**
     * Retrieves an enumerable list of objects from the cache.
     *
     * @param <T> The types of configuration objects to be included in the list.
     * @param cls class of objects to be retrieved
     * @param query A query based on which the result is obtained
     * @return An enumerable list of configuration objects matching the specified query
     */
    <T extends ICfgObject> Iterable<T> retrieveMultiple(
            Class<T> cls,
            ICfgQuery query);

    /**
     * Retrieves an enumerable list of objects from the cache.
     *
     * @param <T> The types of configuration objects to be included in the list.
     * @param cls class of objects to be retrieved
     * @return An enumerable list of configuration objects matching the specified query
     */
    <T extends ICfgObject> Iterable<T> retrieveMultiple(
            Class<T> cls);

    /**
     * Determines whether the cache contains the specified object.
     *
     * @param  obj The configuration object to look for
     * @return true if the object is in the cache, false otherwise
     */
    boolean contains(ICfgObject obj);

    /**
     * Serializes the cache to the specified result.
     *
     * @param result The result into which the cache is to be written
     * @see javax.xml.transform.stream.StreamResult
     * @see javax.xml.transform.dom.DOMResult
     */
    void serialize(Result result);

    /**
     * Deserializes the cache from the specified source.
     *
     * @param source The source from which the cache is to be read
     * @see javax.xml.transform.stream.StreamSource
     * @see javax.xml.transform.dom.DOMSource
     */
    void deserialize(Source source);

    /**
     * Synchronously updates all configuration objects which are currently in the cache.
     *
     * @throws ConfigException is thrown in case of object(s) reload exception
     * @throws InterruptedException is thrown in case of working thread interrupted
     */
    void refresh() throws ConfigException, InterruptedException;

    /**
     * Asynchronously updates all configuration objects in the cache.
     * Refresh operation will be executed in provided sync invoker
     * as single task, so, invoker will be busy for all this time.
     * Usage sample:
     * <code><pre>
     *   AsyncInvoker invoker =
     *           new SingleThreadInvoker("CacheAsyncRefresh");
     *   Future<IConfCache> asyncRefresh =
     *           cache.beginRefresh(invoker, null);
     *   // DO SOMETHING...
     *   // To check that refresh is done: 
     *   if (asyncRefresh.isDone()) {
     *       // ...
     *   }
     *   // To wait for refresh is done:
     *   asyncRefresh.get();
     *   // After refresh is done and invoker is not needed:
     *   invoker.dispose();
     * </pre></code>
     *
     * @param asyncInvoker invoker for refresh task execution
     * @param finishCallback The callback method to be invoked when the operation completes
     * @return The async result associated with this operation
     */
    Future<IConfCache> beginRefresh(
            AsyncInvoker asyncInvoker,
            Action<Future<IConfCache>> finishCallback);

    /**
     * To be called when the asynchronous refresh operation is complete.
     * If called before the refresh operation completes,
     * this method will block until completion.
     *
     * @param asyncResult The async result associated with the current operation
     */
    void endRefresh(
            Future<IConfCache> asyncResult);

    /**
     * Returns the current policy associated with this cache.
     *
     * @return current policy associated with this cache
     */
    IConfCachePolicy getPolicy();
}
