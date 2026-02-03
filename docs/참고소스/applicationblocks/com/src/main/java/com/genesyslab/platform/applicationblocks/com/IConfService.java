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
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.applicationblocks.com.cache.IConfCache;

import com.genesyslab.platform.applicationblocks.commons.Action;
import com.genesyslab.platform.applicationblocks.commons.broker.SubscriptionService;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.metadata.CfgMetadata;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBase;
import com.genesyslab.platform.configuration.protocol.obj.ConfDataCollection;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectsCollection;

import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.AsyncInvokerSupport;

import com.genesyslab.platform.commons.threading.AsyncInvoker;

import org.w3c.dom.Node;

import java.util.Collection;


/**
 * It's a main Configuration Service interface of Configuration Object Model Application Block.<br/>
 * It declares operations like:<ul>
 *   <li>synchronous/asynchronous objects reading,</li>
 *   <li>creation of a new object or saving of modifications in existing one,</li>
 *   <li>server subscription for changes on configuration server objects,</li>
 *   <li>creation of configuration objects from given XML/DOM structures,</li>
 *   <li>accessing of cache, underlying configuration protocol, etc</li></ul>
 *
 * <p/>Configuration service factory {@link ConfServiceFactory} is the starting point for initialization
 * and getting of configuration service instances.
 *
 * @see ConfServiceFactory
 */
public interface IConfService
        extends SubscriptionService<ConfEvent>,
            AsyncInvokerSupport {

    /**
     * Returns a reference to the protocol connection to Configuration Server.
     */
    Protocol getProtocol();

    /**
     * Returns reference to the configuration Metadata.
     *
     * @return Metadata reference from the underlying protocol.
     */
    CfgMetadata getMetaData();

    /**
     * Returns an instance of the configuration cache or null if caching is not
     * enabled.
     */
    IConfCache getCache();

    /**
     * Returns the policy associated with this service.
     */
    IConfServicePolicy getPolicy();

    /**
     * Updates the specified object.
     *
     * @param cfgObject the object to update
     * @throws ConfigException in case of problems in communication with server.
     * @throws IllegalStateException if service is already disposed.
     */
    void saveObject(
            final ICfgObject cfgObject)
                throws ConfigException;

    /**
     * Deletes the specified object.
     *
     * @param cfgObject the object to delete
     * @throws ConfigException in case of problems in communication with server.
     * @throws IllegalStateException if service is already disposed.
     */
    void deleteObject(
            final ICfgObject cfgObject)
                throws ConfigException;

    /**
     * Refreshes the specified object with the latest information.
     *
     * @param cfgObject the object to refresh
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalStateException if service is already disposed.
     */
    void refreshObject(
            final ICfgObject cfgObject)
                throws ConfigException;

    /**
     * Retrieves an object based on the specified query.
     *
     * @param query the query by which to retrieve the object
     * @return The retrieved object or null
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalStateException if service is already disposed.
     */
    <T extends ICfgObject> T retrieveObject(
            final ICfgQuery<T> query)
                throws ConfigException;

    /**
     * Retrieves a typed object based on the specified query.
     *
     * @param <T> The type of object to retrieve
     * @param query the query by which to retrieve the object
     * @return The retrieved object or null
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalStateException if service is already disposed.
     */
    <T extends ICfgObject> T retrieveObject(
            final Class<T> cls,
            final ICfgQuery query)
                throws ConfigException;

    /**
     * Retrieves an object based on its dbid and type query.
     *
     * @param dbId the dbid of the object to retrieve
     * @param objectType the object's type
     * @return the retrieved object or null
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalStateException if service is already disposed.
     */
    ICfgObject retrieveObject(
            final CfgObjectType objectType,
            final int dbId)
                throws ConfigException;

    /**
     * Retrieves a list of typed objects based on the specified query.
     *
     * @param <T> The type of objects to retrieve
     * @param query the query by which to retrieve the objects
     * @return a list of retrieved objects or null
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalArgumentException if query type is unsupported.
     * @throws IllegalStateException if service is already disposed.
     */
    <T extends ICfgObject> Collection<T> retrieveMultipleObjects(
            final Class<T> cls,
            final ICfgQuery query)
                throws ConfigException, InterruptedException;

    /**
     * Retrieves a list of typed objects based on the specified query.
     *
     * @param <T> The type of objects to retrieve
     * @param query the query by which to retrieve the objects
     * @param timeout timeout in milliseconds which will be used for waiting messages from server
     *                (excluding parsing time of messages)
     * @return a list of retrieved objects or null
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalArgumentException if query type is unsupported.
     * @throws IllegalStateException if service is already disposed.
     */
    <T extends ICfgObject> Collection<T> retrieveMultipleObjects(
            final Class<T> cls,
            final ICfgQuery query,
            final long timeout)
                throws ConfigException, InterruptedException;

    /**
     * Begins to asynchronously retrieve a list of objects based on the specified query.
     * EndRetrieveMultipleObjects should then be called, either upon callback for asynchronous notification,
     * or immediately, which will block the calling thread until results are available.
     * Callback notifications are done with additional invoker, so,
     * async invoker must be set with {@link #setInvoker(AsyncInvoker) setInvoker(AsyncInvoker)}
     * before this method invocation if we have non-null callback.
     *
     * @param <T> The type of objects to retrieve
     * @param query the query by which to retrieve the objects
     * @param finishCallback the callback to notify when retrieve operation is complete
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalArgumentException if query type is unsupported.
     * @throws IllegalStateException if service is already disposed.
     * @see #endRetrieveMultipleObjects(AsyncRequestResult)
     * @see #setInvoker(AsyncInvoker)
     */
    <T extends ICfgObject> AsyncRequestResult<T> beginRetrieveMultipleObjects(
            final Class<T> cls,
            final ICfgQuery query,
            final Action<AsyncRequestResult<T>> finishCallback)
                throws ConfigException;

    /**
     * Begins to asynchronously retrieve a list of objects based on the specified query.
     * EndRetrieveMultipleObjects should then be called, either upon callback for asynchronous notification,
     * or immediately, which will block the calling thread until results are available.
     * Callback notifications are done with additional invoker, so,
     * async invoker must be set with {@link #setInvoker(AsyncInvoker) setInvoker(AsyncInvoker)}
     * before this method invocation if we have non-null callback.
     *
     * @param <T> The type of objects to retrieve
     * @param query the query by which to retrieve the objects
     * @param dataCallback the callback to notify when partial data arrived
     * @param finishCallback the callback to notify when retrieve operation is complete
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalArgumentException if query type is unsupported.
     * @throws IllegalStateException if service is already disposed.
     * @see #endRetrieveMultipleObjects(AsyncRequestResult)
     * @see #setInvoker(AsyncInvoker)
     */
    <T extends ICfgObject> AsyncRequestResult<T> beginRetrieveMultipleObjects(
            final Class<T> cls,
            final ICfgQuery query,
            final Action<Collection<T>> dataCallback,
            final Action<AsyncRequestResult<T>> finishCallback)
                throws ConfigException;

    /**
     * Starts reading of configuration objects collection from server.
     * Callback notifications are done with additional invoker, so,
     * async invoker must be set with {@link #setInvoker(AsyncInvoker) setInvoker(AsyncInvoker)}
     * before this method invocation if we have non-null callback.
     *
     * @param cls class used for generic matching
     * @param query query for objects reading
     * @param dataCallback user defined callback action for data parts arrival notification or null
     * @param finishCallback user defined callback action for data readiness notification or null
     * @param timeout timeout in milliseconds which will be used for waiting messages from server
     *                (excluding parsing time of messages)
     * @param <T> specific wrapping class of particular configuration objects type
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalArgumentException if query type is unsupported.
     * @throws IllegalStateException if service is already disposed.
     * @see #endRetrieveMultipleObjects(AsyncRequestResult)
     * @see #setInvoker(AsyncInvoker)
     */
    <T extends ICfgObject> AsyncRequestResult<T> beginRetrieveMultipleObjects(
            final Class<T> cls,
            final ICfgQuery query,
            final Action<Collection<T>> dataCallback,
            final Action<AsyncRequestResult<T>> finishCallback,
            final long timeout)
                throws ConfigException;

    /**
     * Called to retrieve the result of asynchronous RetrieveMultipleObjects operation.
     * Should be called on execution of AsyncCallback passed to BeginRetrieveMultiple objects.
     * Will block calling thread until results are received if called before operation is completed.
     *
     * @param asyncResult The IAsyncResult object used to track the current request
     * @return A list of retrieved objects or null
     */
    <T extends ICfgObject> Collection<T> endRetrieveMultipleObjects(
            final AsyncRequestResult<T> asyncResult)
                throws ConfigException, InterruptedException;

    /**
     * Sets user defined custom invoker for callback notifications on asynchronous
     * multiple objects reading operations.
     * Invoker life cycle is under user control,
     * so, user should dispose invoker separately from ConfService.
     *
     * @param invoker user defined invoker
     * @see #beginRetrieveMultipleObjects(Class, ICfgQuery, Action)
     * @see #beginRetrieveMultipleObjects(Class, ICfgQuery, Action, Action)
     */
    void setInvoker(
            final AsyncInvoker invoker);

    /**
     * Sets user defined custom invoker for ConfEvent broker.
     * Invoker life cycle is under user control,
     * so, user should dispose invoker separately from ConfService.
     *
     * @param invoker user defined invoker
     * @throws IllegalStateException if service is already disposed.
     */
    void setBrokerInvoker(
            final AsyncInvoker invoker);

    /**
     * Creates a single COM configuration object using the passed parameters.
     *
     * @param confObject XML describing a single configuration object as received from Configuration Server
     * @param isSaved Specifies whether the object has been previously saved in the configuration database
     * @return the newly created object
     * @throws ConfigException in case of objects instantiation problems.
     * @throws IllegalStateException if service is already disposed.
     */
    ICfgObject createObjectFromXML(
            final Node confObject,
            final boolean isSaved)
                throws ConfigException;

    /**
     * Creates a single COM configuration object using the passed parameters.
     *
     * @param confObject XML describing a single configuration object as received from Configuration Server
     * @param objectPath The folder path of the object in Configuration Server
     * @param folderDbid The DBID of the folder in which the object resides
     * @param isSaved Specifies whether the object has been previously saved in the configuration database
     * @return the newly created object
     * @throws ConfigException in case of objects instantiation problems.
     * @throws IllegalStateException if service is already disposed.
     */
    ICfgObject createObjectFromXML(
            final Node confObject,
            final String objectPath,
            final int folderDbid,
            final boolean isSaved)
                throws ConfigException;

    /**
     * Creates a list of configuration objects based on XML received from Configuration Server.
     *
     * @param receivedObjects the XPathNavigable object in the format of the EventObjectsRead.ConfObject property
     * @param objectPaths Array of strings representing a list of paths for the objects in receivedObjects (should be in the same order)
     * @param folderDbids integer array of folder DBIDs, should be in the same order as the receivedObjects list
     * @return a list of newly created objects
     * @throws ConfigException in case of objects instantiation problems.
     * @throws IllegalStateException if service is already disposed.
     */
    <T extends ICfgObject> Collection<T> createMultipleObjectsFromXML(
            final Node receivedObjects,
            final String[] objectPaths,
            final int[] folderDbids)
                throws ConfigException;

    /**
     * Creates a list of configuration objects based on XML received from Configuration Server
     *
     * @param receivedObjects the XPathNavigable object in the format of the EventObjectsRead.ConfObject property
     * @return a list of newly created objects
     * @throws ConfigException in case of objects instantiation problems.
     * @throws IllegalStateException if service is already disposed.
     */
    <T extends ICfgObject> Collection<T> createMultipleObjectsFromXML(
            final Node receivedObjects)
                throws ConfigException;


    /**
     * Wraps a single configuration structure with COM configuration object using the passed parameters.
     *
     * @param confObject initial content of the object to be created
     * @param objectPath The folder path of the object in Configuration Server
     * @param folderDbid The DBID of the folder in which the object resides
     * @param isSaved specifies whether the object has been previously saved in the configuration database
     * @return newly created configuration object.
     * @throws ConfigException in case of problems in communication with server.
     * @throws IllegalStateException if service is already disposed.
     * @throws IllegalArgumentException if given objects structure is null or invalid.
     */
    public ICfgObject createObject(
            final ConfObjectBase confObject,
            final String         objectPath,
            final int            folderDbid,
            final boolean        isSaved)
                    throws ConfigException;

    /**
     * Creates a single COM configuration object using the passed parameters.
     *
     * @param confObject initial content of the object to be created
     * @param isSaved specifies whether the object has been previously saved in the configuration database
     * @return newly created configuration object.
     * @throws ConfigException in case of problems in communication with server.
     * @throws IllegalStateException if service is already disposed.
     * @throws IllegalArgumentException if given objects structure is null or invalid.
     */
    public ICfgObject createObject(
            final ConfObjectBase confObject,
            final boolean isSaved)
                    throws ConfigException;

    /**
     * Creates collection of COM configuration objects using the passed parameters.
     *
     * @param confObjects collection of objects declarations to be created
     * @return newly created configuration object.
     * @throws ConfigException in case of problems in communication with server.
     * @throws IllegalStateException if service is already disposed.
     * @throws NullPointerException if given collection is null.
     * @throws IllegalArgumentException if given object structure in the collection is null or invalid.
     */
    public <T extends ICfgObject> Collection<T> createMultipleObjects(
            final ConfDataCollection<? extends ConfObjectBase> confObjects,
            final String[] objectsPaths,
            final int[]    foldersDbids)
                    throws ConfigException;

    /**
     * Creates collection of COM configuration objects using the passed parameters.
     *
     * @param confObjects collection of objects declarations to be created
     * @return newly created configuration object.
     * @throws ConfigException in case of problems in communication with server.
     * @throws IllegalStateException if service is already disposed.
     * @throws NullPointerException if given collection is null.
     * @throws IllegalArgumentException if given object structure in the collection is null or invalid.
     */
    public Collection<ICfgObject> createMultipleObjects(
            final ConfObjectsCollection confObjects)
                    throws ConfigException;


    /**
     * Subscribes to receiving notifications. This method
     * allows the user to specify the subscription details using the "NotificationQuery"
     * object passed as a parameter.
     *
     * @param query the query specifying the subscription details
     * @throws ConfigException in case of problems in communication with server.
     * @throws IllegalStateException if service is already disposed.
     * @see #unsubscribe(Subscription)
     */
    Subscription subscribe(
            final NotificationQuery query)
                throws ConfigException;

    /**
     * Subscribes to receiving notifications. This method
     * subscribes to events for the object passed as the parameter.
     *
     * @param obj the object about which we want to receive notifications
     * @throws ConfigException in case of problems in communication with server.
     * @throws IllegalStateException if service is already disposed.
     * @see #unsubscribe(Subscription)
     */
    Subscription subscribe(
            final ICfgObject obj)
                throws ConfigException;

    /**
     * Unsubscribes from receiving notifications from Configuration Server.
     *
     * @param subscription reference to a service subscription.
     * @throws ConfigException in case of problems in communication with server.
     * @throws IllegalStateException if service is already disposed.
     * @see #subscribe(ICfgObject)
     * @see #subscribe(NotificationQuery)
     */
    void unsubscribe(
            final Subscription subscription)
                throws ConfigException;

    /**
     * Sets extra MessageHandler for custom handling of incoming asynchronous protocol
     * messages on the channel.<br/>
     * It will be called in the protocol invoker thread after procession by the ConfService internal handler.
     *
     * @param msgHandler user handler
     */
    public void setUserMessageHandler(MessageHandler msgHandler);
}
