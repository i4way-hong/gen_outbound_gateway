// ===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:
//
// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.
//
// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.
//
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
//
// Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.applicationblocks.com.cache.IConfCache;
import com.genesyslab.platform.applicationblocks.com.cache.DefaultConfCache;
import com.genesyslab.platform.applicationblocks.com.runtime.EventService;
import com.genesyslab.platform.applicationblocks.com.runtime.MiscConstants;
import com.genesyslab.platform.applicationblocks.com.runtime.CfgObjectActivator;
import com.genesyslab.platform.applicationblocks.com.runtime.AsyncRequestResultImpl;
import com.genesyslab.platform.applicationblocks.com.runtime.ProtocolOperationsHelper;

import com.genesyslab.platform.applicationblocks.commons.Action;
import com.genesyslab.platform.applicationblocks.commons.Predicate;
import com.genesyslab.platform.applicationblocks.commons.broker.Subscriber;

import com.genesyslab.platform.commons.protocol.*;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.metadata.CfgMetadata;
import com.genesyslab.platform.configuration.protocol.obj.ConfObject;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBase;
import com.genesyslab.platform.configuration.protocol.obj.ConfDataCollection;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectsCollection;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventError;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventBriefInfo;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectsRead;
import com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestReadObjects;
import com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestReadObjects2;
import com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestGetBriefInfo;

import com.genesyslab.platform.commons.GEnum;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.concurrent.ExecutionException;

import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Collection;
import java.util.EventObject;
import java.lang.reflect.Field;


/**
 * This class holds a reference to the connection to Configuration Server (<code>ConfServerProtocol</code>).
 * Please, use this class to create new instances of configuration objects, subscribe to events, register on
 * notifications.
 *
 * @see ConfServiceFactory
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
@SuppressWarnings("rawtypes")
public class ConfService
        implements IConfService {

    /**
     * The Configuration protocol instance.
     */
    private Protocol confProtocol;

    /**
     * Internal event service for reading of asynchronous events from the server.
     * It reads incoming protocol Messages and converts them to ConfigurationEvent's.
     */
    private EventService eventService;

    private AsyncInvoker asyncInvoker = null;

    private IConfServicePolicy confPolicy = new DefaultConfServicePolicy();

    private IConfCache confCache = null;

    private CfgMetadata metaData = null;

    private MessageHandler ownMessageHandler = null;
    private MessageHandler userMessageHandler = null;

    private boolean isDisposed = false;
    private final Object lifecycleGuard = new Object();

    /**
     * Private logger for this class.
     */
    private static final ILogger log = Log.getLogger(ConfService.class);

    /**
     * Creates a new instance of the class.
     *
     * @param protocol A reference to a connection to Configuration Server
     */
    protected ConfService(final Protocol protocol) {
        if (protocol == null) {
            throw new NullPointerException("protocol");
        }
        if (!ConfServerProtocol.PROTOCOL_DESCRIPTION.equals(
                protocol.getProtocolDescription())) {
            throw new IllegalArgumentException(
                    "Illegal protocol - should be ConfServerProtocol");
        }

        this.confProtocol = protocol;
        initLogging();

        this.confProtocol.addChannelListener(new COMChannelListener());

        this.eventService = new EventService((IConfService) this);

        if (protocol instanceof ConfServerProtocol) {
            metaData = ((ConfServerProtocol) protocol).getServerContext().getMetadata();
        } else {
            try {
                metaData = new CfgMetadata();
            } catch (final Exception e) {
                throw new RuntimeException("Unexpected internal exception", e);
            }
        }
    }

    /**
     * Creates and sets up internal MessageHandler on the protocol connection.<br/>
     * It is used for asynchronous messages receiving instead of MessageBroker/SubscriptionService interface.<br/>
     * It is to be called from ConfServiceFactory if no message subscription service provided by user.
     */
    void setupInternalMessageHandler() {
        synchronized (lifecycleGuard) {
            if (confProtocol != null) {
                MessageHandler hndl = new COMMessageHandler();
                confProtocol.setMessageHandler(hndl);
                ownMessageHandler = hndl;
            } else {
                throw new IllegalArgumentException(
                        "Only InputChannel has MessageHandler interface support");
            }
        }
    }

    void dispose() {
        synchronized (lifecycleGuard) {
            if (!isDisposed) {
                isDisposed = true;
                if (ownMessageHandler != null) {
                    if (confProtocol != null) {
                        try {
                            confProtocol.setMessageHandler(null);
                        } catch (Exception ex) {
                            log.error("Exception removing private MessageHandler from protocol", ex);
                        }
                    }
                    ownMessageHandler = null;
                }
                eventService.dispose();
                // eventService = null;
                confProtocol = null;
                metaData = null;
            }
        }
    }


    public boolean isDisposed() {
        return isDisposed;
    }

    /**
     * Ensures that this ConfService instance is not disposed.
     *
     * @throws IllegalStateException if service is disposed.
     */
    protected void exceptionIfDisposed() {
        if (isDisposed) {
            throw new IllegalStateException("ConfService is already disposed");
        }
    }

    public CfgMetadata getMetaData() {
        return metaData;
    }

    protected ILogger getLogger() {
        return log;
    }

    /**
     * Returns a reference to the protocol connection to Configuration Server.
     *
     * @return Configuration protocol instance
     */
    public Protocol getProtocol() {
        return confProtocol;
    }

    /**
     * Sets extra MessageHandler for custom handling of incoming asynchronous protocol
     * messages on the channel.<br/>
     * It will be called in the protocol invoker thread after procession by the ConfService internal handler.
     *
     * @param msgHandler user handler
     */
    public void setUserMessageHandler(final MessageHandler msgHandler) {
        userMessageHandler = msgHandler;
    }

    @SuppressWarnings("deprecation")
    protected AsyncInvoker getInvokerNotNull() {
        if (asyncInvoker == null) {
            Protocol confProtocol = this.confProtocol;
            if (confProtocol instanceof ClientChannel) {
                return ((ClientChannel)confProtocol).getInvoker();
            }
            throw new ConfigRuntimeException(
                    "AsyncInvoker has not been set. Use ConfService.setInvoker()");
        }
        return asyncInvoker;
    }

    /**
     * Sets user defined custom invoker for callback notifications on asynchronous
     * multiple objects reading operations. Invoker life cycle is under user control,
     * so, user should dispose invoker separately from ConfService.
     *
     * @param invoker user defined invoker
     * @see #beginRetrieveMultipleObjects(Class, ICfgQuery, Action)
     * @see #beginRetrieveMultipleObjects(Class, ICfgQuery, Action, Action)
     * @throws IllegalStateException if service is already disposed.
     */
    public void setInvoker(final AsyncInvoker invoker) {
        exceptionIfDisposed();
        asyncInvoker = invoker;
    }

    /**
     * Sets user defined custom invoker for ConfEvent broker.
     * Invoker life cycle is under user control,
     * so, user should dispose invoker separately from ConfService.
     *
     * @param invoker user defined invoker
     * @throws IllegalStateException if service is already disposed.
     */
    public void setBrokerInvoker(final AsyncInvoker invoker) {
        exceptionIfDisposed();
        eventService.setInvoker(invoker);
    }

    public IConfCache getCache() {
        return confCache;
    }

    void setCache(final IConfCache cache) {
        if (cache instanceof DefaultConfCache) {
            final DefaultConfCache defCache = (DefaultConfCache) cache;
            final Endpoint endpoint = confProtocol.getEndpoint();
            if (endpoint != null) {
                defCache.addEndpoint(endpoint);
            }
        }
        confCache = cache;
        getLogger().infoFormat("Caching enabled: {0}", (confCache != null));
    }

    public IConfServicePolicy getPolicy() {
        return confPolicy;
    }

    void setPolicy(final IConfServicePolicy policy) {
        if (policy == null) {
            throw new NullPointerException("policy");
        }
        confPolicy = policy;
    }


    /**
     * Reads configuration object from server by object type and DBID.
     *
     * @param objectType type of configuration object to be read
     * @param dbId object DBID
     * @return configuration object read or null
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalStateException if service is already disposed.
     */
    public ICfgObject retrieveObject(
            final CfgObjectType objectType,
            final int dbId)
                throws ConfigException {

        CfgFilterBasedQuery<?> query = new CfgFilterBasedQuery<CfgObject>(objectType);
        query.setProperty(MiscConstants.FilterDbidName, dbId);

        return retrieveObject((CfgObject) null, query, false);
    }

    /**
     * Retrieves an object based on the specified query.
     *
     * @param query the query by which to retrieve the object
     * @return The retrieved object or null
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalArgumentException if query type is unsupported. 
     * @throws IllegalStateException if service is already disposed.
     */
    public <T extends ICfgObject> T retrieveObject(final ICfgQuery<T> query)
            throws ConfigException {
        return retrieveObject((T)null, query, false);
    }

    /**
     * Reads configuration object from server.
     *
     * @param cls class used for generic matching
     * @param query query for object reading
     * @param <T> specific wrapping class of particular configuration object type
     * @return configuration object read or null
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalArgumentException if query type is unsupported.
     * @throws IllegalStateException if service is already disposed.
     * @see #retrieveObject(com.genesyslab.platform.configuration.protocol.types.CfgObjectType, int)
     */
    public <T extends ICfgObject> T retrieveObject(
            final Class<T> cls, final ICfgQuery query)
                throws ConfigException {
        checkObjectClass(cls, query);
        return retrieveObject(
                (T)null,
                query,
                (cls != null) && ICfgBriefInfo.class.isAssignableFrom(cls));
    }

    /**
     * Reads collection of configuration objects from server.
     * 
     * <br><br><b>Note:</b> Don't call the method inside any PSDK callback handler.
     * It locks config server channel's notification mechanism 
     * (because the mechanism is based on a single thread executor 
     * and the synchronous method won't get the completion notification and you will get the ProtocolTimeoutException). 
     * You should call the method from a separate thread or use 
     * the asynchronous version {@link #beginRetrieveMultipleObjects(Class, ICfgQuery, Action)}. 
     * <br><br>
     *
     * @param cls class used for generic matching
     * @param query query for objects reading
     * @param <T> specific wrapping class of particular configuration objects type
     * @return configuration objects read or null
     * @throws ConfigException in case of problems while reading data from server.
     * @throws InterruptedException in case of forced interrupt.
     * @throws IllegalStateException if service is already disposed.
     * @throws IllegalArgumentException if query type is unsupported.
     */
    public <T extends ICfgObject> Collection<T> retrieveMultipleObjects(
            final Class<T> cls, final ICfgQuery query)
                throws ConfigException, InterruptedException {

        return endRetrieveMultipleObjects(
                beginRetrieveMultipleObjects(cls, query, null));
    }

    /**
     * Retrieves a list of typed objects based on the specified query.
     *
     * @param <T> The type of objects to retrieve
     * @param query the query by which to retrieve the objects
     * @param timeout timeout in milliseconds which will be used for waiting messages from server
     *                (excluding parsing time of messages)
     * @return a list of retrieved objects or null
     * @throws IllegalStateException if service is already disposed.
     * @throws IllegalArgumentException if query type is unsupported.
     */
    public <T extends ICfgObject> Collection<T> retrieveMultipleObjects(
            final Class<T> cls,
            final ICfgQuery query,
            final long timeout)
                throws ConfigException, InterruptedException {

        return endRetrieveMultipleObjects(
                beginRetrieveMultipleObjects(cls, query, null, null, timeout));
    }

    private <T extends ICfgObject> Collection<T> retrieveMultipleFromCache(
            final Class<T> cls,
            final ICfgQuery query)
                throws ConfigException {
        if (getLogger().isDebug()) {
            getLogger().debug("Querying cache...");
        }

        Iterable<T> objs = confCache.retrieveMultiple(cls, query);

        List<T> ret = null;
        if (objs != null) {
            ret = new ArrayList<T>();
            for (T obj : objs) {
                ret.add(obj);
            }

            if (getLogger().isInfo()) {
                getLogger().info(
                        "Retrieved " + ret.size() + " objects from cache.");
            }
        } else {
            getLogger().info("No data returned from cache.");
        }

        return ret;
    }


    /**
     * Saves local object modifications to the configuration server or
     * create new one if it has been created locally.
     *
     * @param cfgObject configuration object instance
     * @throws ConfigException in case of problems on object update or create operations.
     * @throws IllegalStateException if service is already disposed.
     */
    public void saveObject(final ICfgObject cfgObject)
                throws ConfigException {
        exceptionIfDisposed();
        if (cfgObject == null) {
            throw new NullPointerException("cfgObject");
        }

        cfgObject.save();
    }

    /**
     * Deletes configuration object from the Configuration Server.
     * Object is been removed using object type and DBID properties, so
     * provided configuration object should have DBID initialized.
     *
     * @param cfgObject configuration object instance
     * @throws ConfigException in case of problems on object remove operation.
     * @throws IllegalStateException if service is already disposed.
     */
    public void deleteObject(final ICfgObject cfgObject)
                throws ConfigException {
        exceptionIfDisposed();
        if (cfgObject == null) {
            throw new NullPointerException("cfgObject");
        }

        cfgObject.delete();
    }

    /**
     * Reloads configuration object data from the Configuration Server.
     * Object is been refreshed using object type and DBID properties, so
     * provided configuration object should have DBID initialized.
     *
     * @param cfgObject configuration object instance
     * @throws ConfigException in case of problems on object read operation.
     * @throws IllegalStateException if service is already disposed.
     */
    public void refreshObject(final ICfgObject cfgObject)
                throws ConfigException {
        exceptionIfDisposed();
        if (cfgObject == null) {
            throw new NullPointerException("cfgObject");
        }

        CfgFilterBasedQuery<?> query = new CfgFilterBasedQuery<CfgObject>(
                cfgObject.getObjectType());
        query.setProperty(MiscConstants.FilterDbidName,
                cfgObject.getObjectDbid()
        );

        if (retrieveObject(cfgObject, query, false) == null) {
            throw new ConfigException(
                "Configuration server does not contain object to refresh from");
        }
    }


    /**
     * Registers custom handler for ConfigurationEvent's handling.
     *
     * @param subscriber user defined events handler
     * @throws IllegalStateException if service is already disposed.
     * @see #unregister(Subscriber)
     */
    public void register(
            final Subscriber<ConfEvent> subscriber) {
        exceptionIfDisposed();
        if (log.isDebug() && subscriber != null) {
            log.debug("Registering subscriber " + subscriber.toString());
        }
        eventService.register(subscriber);
    }

    /**
     * Registers custom handler for ConfigurationEvent's handling.
     *
     * @param handler user defined events handler
     * @param filter configuration events filter
     * @throws IllegalStateException if service is already disposed.
     * @see #unregister(Action)
     */
    public void register(
            final Action<ConfEvent> handler,
            final Predicate<ConfEvent> filter) {
        exceptionIfDisposed();
        if (log.isDebug() && handler != null) {
            log.debugFormat("Registering handler [{0}] with filer [{1}]",
                    new Object[] {handler.toString(), filter});
        }
        eventService.register(handler, filter);
    }

    /**
     * Unregisters custom ConfigurationEvent's handler.
     *
     * @param subscriber user defined events handler
     * @see #register(Subscriber)
     * @see #register(Action, Predicate)
     */
    public void unregister(
            final Subscriber<ConfEvent> subscriber) {
        if (log.isDebug() && subscriber != null) {
            log.debug("Unregistering subscriber " + subscriber.toString());
        }
        EventService es = eventService;
        if (es != null) {
            es.unregister(subscriber);
        } else {
            log.warn("Using ConfService after its disposal");
        }
    }

    /**
     * Unregisters custom ConfigurationEvent's handler.
     *
     * @param handler user defined events handler
     * @see #register(Subscriber)
     * @see #register(Action, Predicate)
     */
    public void unregister(
            final Action<ConfEvent> handler) {
        if (log.isDebug() && handler != null) {
            log.debugFormat("Unregistering handler [{0}]",
                    new Object[] {handler.toString()});
        }
        EventService es = eventService;
        if (es != null) {
            es.unregister(handler);
        } else {
            log.warn("Using ConfService after its disposal");
        }
    }

    /**
     * Subscribes to receiving notifications from Configuration Server.
     * Should be used in conjunction with <code>Register</code> method
     * so that events reach the event handler.
     *
     * @param query query for subscription
     * @return subscription keeping object
     * @throws ConfigException exception while working with server side.
     * @throws IllegalStateException if service is already disposed.
     * @see #unsubscribe(Subscription)
     */
    public Subscription subscribe(final NotificationQuery query)
            throws ConfigException {
        exceptionIfDisposed();
        return ProtocolOperationsHelper.subscribe(confProtocol, query);
    }

    /**
     * Subscribes to receiving notifications from Configuration Server.
     * Should be used in conjunction with <code>Register</code> method
     * so that events reach the event handler.
     *
     * @param obj object to listen events on
     * @return subscription keeping object
     * @throws ConfigException exception while working with server side.
     * @throws IllegalStateException if service is already disposed.
     * @see #unsubscribe(Subscription)
     */
    public Subscription subscribe(final ICfgObject obj)
            throws ConfigException {
        exceptionIfDisposed();
        return ProtocolOperationsHelper.subscribe(confProtocol, obj);
    }

    /**
     * Unsubscribes from the subscription on the Configuration server.
     *
     * @param subscription subscription holder got with call to
     *         {@link #subscribe(ICfgObject)}
     *         or {@link #subscribe(NotificationQuery)}.
     * @throws ConfigException exception while working with server side.
     * @throws IllegalStateException if service is already disposed.
     * @see #subscribe(ICfgObject)
     * @see #subscribe(NotificationQuery)
     */
    public void unsubscribe(final Subscription subscription)
            throws ConfigException {
        exceptionIfDisposed();
        ProtocolOperationsHelper.unsubscribe(confProtocol, subscription);
    }

    /**
     * Reads or refreshes configuration object from server.
     * It's for internal usage only.
     *
     * @param confObj object instance to refresh or null
     * @param query query for object reading
     * @return configuration object read/refreshed (or null)
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalStateException if service is already disposed.
     * @see #retrieveObject(com.genesyslab.platform.configuration.protocol.types.CfgObjectType, int)
     * @deprecated
     * @see #retrieveObject(ICfgObject, ICfgQuery, boolean)
     */
    protected <T extends ICfgObject> T retrieveObject(
            final T confObj, final ICfgQuery query)
                throws ConfigException {
        return retrieveObject(confObj, query, false);
    }

    /**
     * Reads or refreshes configuration object from server.
     * It's for internal usage only.
     *
     * @param confObj object instance to refresh or null
     * @param query query for object reading
     * @param isBriefInfo flag for objects' brief info request (is used when confObj is null)
     * @return configuration object read/refreshed (or null)
     * @throws ConfigException in case of problems while reading data from server
     * @throws IllegalStateException if service is already disposed.
     * @see #retrieveObject(com.genesyslab.platform.configuration.protocol.types.CfgObjectType, int)
     */
    @SuppressWarnings({ "unchecked" })
    protected <T extends ICfgObject> T retrieveObject(
            final T confObj,
            final ICfgQuery query,
            final boolean isBriefInfo)
                throws ConfigException {
        exceptionIfDisposed();

        ConfObject confObject = null;
        T result = confObj;

        boolean isBriefInfoRequest = isBriefInfo;
        if (confObj != null) {
            isBriefInfoRequest = (confObj instanceof ICfgBriefInfo);
        }

        if (getLogger().isInfo()) {
            getLogger().info("Retrieve object using query: \r\n\r\n" + query);
        }

        if (confObj == null && !isBriefInfoRequest) {
            if (confPolicy.getQueryCacheOnRetrieve(query)) {
                if (confCache == null) {
                    throw new NullPointerException("ConfCache");
                }
                if (getLogger().isDebug()) {
                    getLogger().debug("Querying cache...");
                }

                result = (T) confCache.retrieve(ICfgObject.class, query); 
                
                if (result != null) {
                    return result;
                }
            }
        }

        Message message = isBriefInfoRequest
                ? prepareRequestReadBriefInfoMessage(query)
                : prepareRequestReadObjectsMessage(query);
        if (message == null) {
            throw new ConfigException(
                    "Got null RequestReadObjects message");
        }

        Message response;

        if (getLogger().isDebug()) {
            getLogger().debug("Querying configuration server...");
        }

        try {
            response = getProtocol().request(message);
        } catch (Exception ex) {
            getLogger().error("Exception on request handling", ex);
            throw new ConfigException("Exception on reading configuration data", ex);
        }

        if (response == null) {
            getLogger().error("Null response got on MSG: " + message);
            throw new ConfigException("Error reading configuration data - "
                    + "timeout waiting for server response");
        }

        if (response instanceof EventError) {
            throw ProtocolOperationsHelper.createConfigServerException(
                    (EventError) response);
        }

        EventObjectsRead readResponse = null;
        EventBriefInfo briefInfoResponse = null;
        if (response.messageId() == EventObjectsRead.ID) {
            readResponse = (EventObjectsRead) response;
            ConfObjectsCollection objs = readResponse.getObjects();
            if (objs != null && !objs.isEmpty()) {
                if (objs.size() > 1) {
                    throw new IllegalArgumentException("Query returned invalid "
                            + "number of objects. Should be 1. "
                            + "Use retrieveMultipleObjects() for this query.");
                }
                confObject = objs.get(0);
            }
        } else if (response.messageId() == EventBriefInfo.ID) {
            briefInfoResponse = (EventBriefInfo) response;
            confObject = briefInfoResponse.getBriefObject();
        } else {
            return null;
        }

        if (confObject == null) {
            return null;
        }

        long[]   foldersDbids = null;
        String[] objectPaths  = null;

        if (readResponse != null) {
            foldersDbids = readResponse.getFolderDbids();

            if (foldersDbids != null && foldersDbids.length != 1) {
                throw new ConfigException("There is a wrong number of folder dbids "
                        + " in the Read request result");
            }

            objectPaths = readResponse.getObjectPaths();

            if (objectPaths == null) {
                if (foldersDbids != null) {
                    objectPaths = new String[] {"/"};
                }
            } else if (objectPaths.length != 1) {
                throw new ConfigException(
                        "There is a wrong number of object paths in the Read request result");
            }
        }

        Integer folderDbid = null;
        String objectPath = null;
        if (foldersDbids != null) {
            folderDbid = (int) foldersDbids[0];
        }
        if (objectPaths != null) {
            objectPath = objectPaths[0];
        }

        if (result == null) {
            result = (T) createObject(
                    confObject, objectPath,
                    folderDbid != null ? folderDbid : 0, true);
            if (!isBriefInfoRequest) {
                addObjectOnRetrieve(result);
            }

        } else {
            ((CfgObject) result).reloadObjectWithNewData(
                    confObject, objectPath, folderDbid);
        }

        if (getLogger().isInfo() && result != null) {
            getLogger().infoFormat(
                    "Retrieved object [{0}], dbid: [{1}]",
                    new Object[] {result.getObjectType(),
                            result.getObjectDbid()});
        }

        if (getLogger().isDebug() && result != null) {
            getLogger().debug("Object content: \r\n\r\n" + result.toString());
        }

        return result;
    }


    protected KeyValueCollection prepareReadObjectsFilter(
            final ICfgFilterBasedQuery<?> query) {
        KeyValueCollection objectFilter = new KeyValueCollection();
        if (query != null) {
            Hashtable<String, Object> filter = query.getFilter();
            for (String key : filter.keySet()) {
                Object value = filter.get(key);
                if (value instanceof GEnum) {
                    objectFilter.addInt(key, ((GEnum) value).asInteger());
                } else if (value instanceof Integer) {
                    objectFilter.addInt(key, (Integer) value);
                } else {
                    objectFilter.addObject(key, value);
                }
            }
            filter = query.getExtraFilter();
            if (filter != null) {
                for (String key : filter.keySet()) {
                    Object value = filter.get(key);
                    if (value instanceof GEnum) {
                        objectFilter.addInt(key, ((GEnum) value).asInteger());
                    } else if (value instanceof Integer) {
                        objectFilter.addInt(key, (Integer) value);
                    } else {
                        objectFilter.addObject(key, value);
                    }
                }
            }
        }

        return objectFilter;
    }

    protected Message prepareRequestReadBriefInfoMessage(
            final ICfgQuery query) {

        int objType = CfgObjectType.CFGNoObject.ordinal();
        if (query instanceof ICfgFilterBasedQuery) {
            KeyValueCollection objectFilter = prepareReadObjectsFilter(
                    (ICfgFilterBasedQuery) query);
            objType = ((ICfgFilterBasedQuery) query).getCfgObjectType().ordinal();
            return RequestGetBriefInfo.create(
                    objType,
                    objectFilter
            );
        }
        
    	throw new IllegalArgumentException("unsupported query type");
    }

    /**
     * Starts reading of configuration objects collection from server.
     * Callback notifications are done with additional invoker, so,
     * async invoker must be set with {@link #setInvoker(AsyncInvoker) setInvoker(AsyncInvoker)}
     * before this method invocation if we have non-null callback.
     * <br><br>Example<br>
     * <pre>
     * // application initialization
     * AsyncInvoker inv = InvokerFactory.namedInvoker("my");
     * confService.setInvoker(inv);
     * // …
     * Action&lt;AsyncRequestResult&lt;CfgApplication&gt;&gt; callback =
     *                 new Action&lt;AsyncRequestResult&lt;CfgApplication&gt;&gt;() {
     *                     public void handle(final AsyncRequestResult&lt;CfgApplication&gt; obj) {
     *                         try {
     *                             Collection&lt;CfgApplication&gt; applications = obj.get();
     *                             // todo : process the result
     *                         } catch (Exception e) {  e.printStackTrace();  }
     *                     }
     *                 };
     *         // Start application configuration objects reading:
     *         AsyncRequestResult&lt;CfgApplication&gt; asyncRequest =
     *                 confService.beginRetrieveMultipleObjects(CfgApplication.class,
     *                         new CfgApplicationQuery(), callback);
     * 
     * // application deinitialization
     * confService.setInvoker(null);
     * InvokerFactory. releaseInvoker("my");
     * </pre>
     *
     * @param cls class used for generic matching
     * @param query query for objects reading
     * @param finishCallback user defined callback action for data readiness notification or null
     * @param <T> specific wrapping class of particular configuration objects type
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalArgumentException if query type is unsupported.
     * @throws IllegalStateException if service is already disposed.
     * @see #setInvoker(AsyncInvoker)
     **/
    public <T extends ICfgObject> AsyncRequestResult<T> beginRetrieveMultipleObjects(
            final Class<T> cls,
            final ICfgQuery query,
            final Action<AsyncRequestResult<T>> finishCallback)
                throws ConfigException {
        return beginRetrieveMultipleObjects(
                cls, query, null, finishCallback,
                getProtocol().getTimeout());
    }

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
     * @param <T> specific wrapping class of particular configuration objects type
     * @throws ConfigException in case of problems while reading data from server.
     * @throws IllegalArgumentException if query type is unsupported.
     * @throws IllegalStateException if service is already disposed.
     * @see #endRetrieveMultipleObjects(AsyncRequestResult)
     * @see #setInvoker(AsyncInvoker)
     */
    public <T extends ICfgObject> AsyncRequestResult<T> beginRetrieveMultipleObjects(
            final Class<T> cls,
            final ICfgQuery query,
            final Action<Collection<T>> dataCallback,
            final Action<AsyncRequestResult<T>> finishCallback)
                throws ConfigException {
        return beginRetrieveMultipleObjects(
                cls, query, dataCallback, finishCallback,
                getProtocol().getTimeout());
    }

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
    @SuppressWarnings("unchecked")
    public <T extends ICfgObject> AsyncRequestResult<T> beginRetrieveMultipleObjects(
            final Class<T> cls,
            final ICfgQuery query,
            final Action<Collection<T>> dataCallback,
            final Action<AsyncRequestResult<T>> finishCallback,
            final long timeout)
                throws ConfigException {
        exceptionIfDisposed();
        checkObjectClass(cls, query);

        boolean isBriefInfoRequest = (cls != null) && (ICfgBriefInfo.class.isAssignableFrom(cls));

        if (getLogger().isInfo()) {
            getLogger().info(
                "Beginning multiple retrieve operation using query: \r\n\r\n"
                        + query);
        }

        if ((!isBriefInfoRequest)
                && confPolicy.getQueryCacheOnRetrieveMultiple(query)) {
            Collection<T> objs = retrieveMultipleFromCache(cls, query);

            if ((objs != null) && (objs.size() > 0)) {
                AsyncInvoker invoker = null;
                if (dataCallback != null
                        || finishCallback != null) {
                    invoker = getInvokerNotNull();
                }
                return new AsyncRequestResultImpl<T>(
                        cls, query, objs,
                        dataCallback, finishCallback,
                        invoker).withProtocol(confProtocol);
            }
        }

        if (getLogger().isDebug()) {
            getLogger().debug("Querying configuration server...");
        }

        try {
            final Message rqMsg = isBriefInfoRequest
                    ? prepareRequestReadBriefInfoMessage(query)
                    : prepareRequestReadObjectsMessage(query);
            if (rqMsg == null) {
                throw new ConfigException(
                        "Got null RequestReadObjects message");
            }
            AsyncInvoker invoker = null;
            if (dataCallback != null
                    || finishCallback != null) {
                invoker = getInvokerNotNull();
            }
            AsyncRequestResultImpl<T> ret = new AsyncRequestResultImpl<T>(
                    cls, query, rqMsg,
                    dataCallback, finishCallback,
                    invoker, timeout).withProtocol(confProtocol);

            eventService.registerAsyncRequest(
                    getProtocol().getReferenceBuilder().updateReference(rqMsg),
                    ret);

            getProtocol().send(rqMsg);

            return ret;
        } catch (final Exception ex) {
            getLogger().error("Exception on sending request", ex);
            throw new ConfigException("Exception on sending request", ex);
        }
    }

    /**
     * Waits until asynchronous reading operation done and return requested data.
     *
     * @param rqResult future object instance created on asynchronous operation start
     * @param <T> specific wrapping class of particular configuration object type
     * @return configuration data read or null
     * @throws ConfigException in case of exception in configuration information reading.
     * @throws InterruptedException if operation was interrupted in some way.
     * @throws IllegalArgumentException if query type is unsupported.
     */
    public <T extends ICfgObject> Collection<T> endRetrieveMultipleObjects(
            final AsyncRequestResult<T> rqResult)
                throws ConfigException, InterruptedException {
        try {
            return rqResult.get();
        } catch (ExecutionException e) {
            Throwable throwable = e.getCause();
            if (!(throwable instanceof ConfigException)
                    && !(throwable instanceof ConfigRuntimeException)) {
                throwable = e;
            }
            getLogger().error("Error processing async request", throwable);
            if (throwable instanceof ConfigException) {
                throw (ConfigException) throwable;
            }
            if (throwable instanceof ConfigRuntimeException) {
                throw (ConfigRuntimeException) throwable;
            }
            throw new ConfigException("Error processing async request", e);
        }
    }

    /**
     * Creates a single COM configuration object using the passed parameters.
     *
     * @param confObject The XML node describing a single configuration object
     *            as received from Configuration Server
     * @param objectPath The folder path of the object in Configuration Server
     * @param folderDbid The DBID of the folder in which the object resides
     * @param isSaved Specifies whether the object has been previously saved
     *            in the configuration database
     * @return the newly created object
     * @throws ConfigException in case of object instantiation problems.
     * @throws IllegalStateException if service is already disposed.
     */
    public ICfgObject createObjectFromXML(
            final Node    confObject,
            final String  objectPath,
            final int     folderDbid,
            final boolean isSaved)
                throws ConfigException {
        exceptionIfDisposed();
        if ((confObject == null)
            || (confObject.getChildNodes().getLength() == 0)) {
            throw new IllegalArgumentException("confObject");
        }

        String objTypeName = confObject.getNodeName();

        try {
            KeyValueCollection param = new KeyValueCollection();
            if (folderDbid > 0) {
                param.addInt(MiscConstants.FolderDbidName, folderDbid);
            }

            CfgObject ret = (CfgObject) CfgObjectActivator.createInstance(
                        objTypeName,
                        this,
                        confObject,
                        new Object[] {
                                param, objectPath
                        }
            );

            ret.setIsSaved(isSaved);

            return ret;
        } catch (Exception ex) {
            getLogger().error("Error creating object from XML", ex);
            throw new ConfigException(
                    "Unknown object type '" + objTypeName + "'", ex);
        }
    }

    /**
     * Creates a single COM configuration object using the passed parameters.
     *
     * @param confObject The XML node describing a single configuration object as received from Configuration Server.
     * @param isSaved Specifies whether the object has been previously saved in the configuration database.
     *
     * @return the newly created object
     * @throws ConfigException in case of object instantiation problems.
     * @throws IllegalStateException if service is already disposed.
     */
    public ICfgObject createObjectFromXML(
            final Node confObject,
            final boolean isSaved)
                throws ConfigException {
        return createObjectFromXML(confObject, null, 0, isSaved);
    }

    /**
     * Creates a list of COM configuration objects using the passed parameters.
     *
     * @param confObjectsContainer Parent node containing set of XML nodes
     *            describing configuration objects as received from Configuration Server
     * @param objectsPaths The folders paths of objects in Configuration Server
     * @param foldersDbids The DBIDs of folders where objects reside
     *
     * @return a list of newly created objects
     * @throws ConfigException in case of objects instantiation problems.
     * @throws IllegalStateException if service is already disposed.
     */
    public <T extends ICfgObject> Collection<T> createMultipleObjectsFromXML(
            final Node     confObjectsContainer,
            final String[] objectsPaths,
            final int[]    foldersDbids)
                throws ConfigException {
        if (confObjectsContainer == null) {
            throw new NullPointerException("confObjectsContainer");
        }

        return createMultipleObjectsFromXML(
                confObjectsContainer.getChildNodes(),
                objectsPaths,
                foldersDbids
        );
    }

    /**
     * Creates a list of COM configuration objects using the passed parameters.
     *
     * @param confObjects List of XML nodes describing configuration
     *            objects as received from Configuration Server
     * @param objectsPaths The folders paths of objects in Configuration Server
     * @param foldersDbids The DBIDs of folders where objects reside
     *
     * @return a list of newly created objects
     * @throws ConfigException in case of objects instantiation problems.
     * @throws IllegalStateException if service is already disposed.
     */
    public <T extends ICfgObject> Collection<T> createMultipleObjectsFromXML(
            final NodeList confObjects,
            final String[] objectsPaths,
            final int[]    foldersDbids)
                throws ConfigException {
        exceptionIfDisposed();
        if (confObjects == null) {
            throw new NullPointerException("confObjectsContainer");
        }

        String[] pathes  = objectsPaths;
        int[]    folderIds = foldersDbids;

        int elemCount = confObjects.getLength();
        int objectsCount = 0;
        for (int i = 0; i < elemCount; i++) {
            Node node = confObjects.item(i);
            if (node instanceof Element) {
                objectsCount++;
            }
        }

        if (folderIds != null && folderIds.length != objectsCount) {
            log.warn("folderDbid array size does not match received objects list - skipping folderDbid's");
            folderIds = null;
        }
        if (pathes != null && pathes.length != objectsCount) {
            log.warn("objectPaths array size does not match received objects list - skipping objectPaths");
            pathes = null;
        }

        int objIdx = 0;
        List<T> result = new ArrayList<T>(objectsCount);
        for (int i = 0; i < elemCount; i++) {
            Node node = confObjects.item(i);
            if (node instanceof Element) {
                int objFolderDbid = 0;
                String objPath = null;
                if (folderIds != null && objIdx < folderIds.length) {
                    objFolderDbid = folderIds[objIdx];
                }
                if (pathes != null && objIdx < pathes.length) {
                    objPath = pathes[objIdx];
                }
                @SuppressWarnings("unchecked")
                T obj = (T) createObjectFromXML( // todo check type conversion to 'T'
                        node, objPath, objFolderDbid, true);
                result.add(obj);
                objIdx++;
            }
        }

        return result;
    }


    public ICfgObject createObject(
            final ConfObjectBase confObject,
            final String         objectPath,
            final int            folderDbid,
            final boolean        isSaved)
                throws ConfigException {
        exceptionIfDisposed();
        if (confObject == null) {
            throw new IllegalArgumentException("confObject");
        }

        try {
            KeyValueCollection param = new KeyValueCollection();
            if (folderDbid != 0) {
                param.addInt(MiscConstants.FolderDbidName, folderDbid);
            }

            CfgObject ret = (CfgObject) CfgObjectActivator.createInstance(
                        this,
                        confObject,
                        new Object[] {
                                param, objectPath
                        }
            );

            ret.setIsSaved(isSaved);

            return ret;
        } catch (Exception ex) {
            getLogger().error("Exception wrapping confObject", ex);
            throw new ConfigException(
                    "Exception wrapping confObject", ex);
        }
    }

    /**
     * Wraps a single configuration structure with COM configuration object using the passed parameters.
     *
     * @param confObject initial content of the object to be created
     * @param isSaved specifies whether the object has been previously saved in the configuration database
     * @return newly created configuration object.
     * @throws ConfigException in case of problems in the given structure.
     * @throws IllegalStateException if service is already disposed.
     * @throws IllegalArgumentException if given objects structure is null or invalid.
     */
    public ICfgObject createObject(
            final ConfObjectBase confObject,
            final boolean isSaved)
                throws ConfigException {
        return createObject(confObject, null, 0, isSaved);
    }

    /**
     * Wraps a single configuration structure with COM configuration object using the passed parameters.
     *
     * @param confObjects collection with initial contents of objects to be created
     * @return newly created configuration object.
     * @throws ConfigException in case of problems in the given structure.
     * @throws IllegalStateException if service is already disposed.
     * @throws NullPointerException if given collection is null.
     * @throws IllegalArgumentException if given object structure in the collection is null or invalid.
     */
    public <T extends ICfgObject> Collection<T> createMultipleObjects(
            final ConfDataCollection<? extends ConfObjectBase> confObjects,
            final String[] objectsPaths,
            final int[]    foldersDbids)
                throws ConfigException {
        exceptionIfDisposed();
        if (confObjects == null) {
            throw new NullPointerException("confObjects");
        }

        String[] pathes  = objectsPaths;
        int[]    folderIds = foldersDbids;

        int objectsCount = confObjects.size();

        if (folderIds != null && folderIds.length != objectsCount) {
            log.warn("folderDbid array size does not match received objects list - skipping folderDbid's");
            folderIds = null;
        }
        if (pathes != null && pathes.length != objectsCount) {
            log.warn("objectPaths array size does not match received objects list - skipping objectPaths");
            pathes = null;
        }

        List<T> result = new ArrayList<T>(objectsCount);
        for (int i = 0; i < confObjects.size(); i++) {
            ConfObjectBase obj = confObjects.get(i);
            int objFolderDbid = 0;
            String objPath = null;
            if (folderIds != null) {
                objFolderDbid = folderIds[i];
            }
            if (pathes != null) {
                objPath = pathes[i];
            }
            @SuppressWarnings("unchecked")
            T cfgObj = (T) createObject( // todo check type conversion to 'T'
                    obj, objPath, objFolderDbid, true);
            result.add(cfgObj);
        }

        return result;
    }

    /**
     * Creates collection of COM configuration objects using the passed parameters.
     *
     * @param confObjects collection of objects declarations to be created
     * @return newly created configuration objects.
     * @throws ConfigException in case of problems in communication with server.
     * @throws IllegalStateException if service is already disposed.
     * @throws IllegalArgumentException if given object structure in the collection is null or invalid.
     */
    public Collection<ICfgObject> createMultipleObjects(
            final ConfObjectsCollection confObjects)
                throws ConfigException {
        return createMultipleObjects(confObjects, null, null);
    }

    /**
     * Creates a list of configuration objects based on XML received
     * from Configuration Server.<br/>
     * Usage sample:
     * <code><pre>
     *      List&lt;CfgObject&gt; objs = cfgService.createMultipleObjectsFromXML(
     *              eventObjectsRead.getConfObject().getDocumentElement()
     *      );
     * </pre></code>
     *
     * @param confObjectsContainer parent node containing set of XML nodes
     *            describing configuration objects as received from
     *            Configuration Server
     * @return a list of newly created objects
     * @throws ConfigException in case of objects instantiation problems.
     * @throws IllegalStateException if service is already disposed.
     */
    public Collection<ICfgObject> createMultipleObjectsFromXML(
            final Node confObjectsContainer)
                throws ConfigException {
        return createMultipleObjectsFromXML(confObjectsContainer, null, null);
    }

    /**
     * Creates a list of configuration objects based on XML received from
     * Configuration Server.
     *
     * @param confObjects list of XML nodes describing
     *            configuration objects as received from Configuration Server
     * @return a list of newly created objects
     * @throws ConfigException in case of objects instantiation problems
     */
    public Collection<ICfgObject> createMultipleObjectsFromXML(
            final NodeList confObjects)
                throws ConfigException {
        return createMultipleObjectsFromXML(confObjects, null, null);
    }

    /**
     * Create Configuration Protocol Request Message
     * for configuration objects reading.
     *
     * @param query query for configuration object(s)
     * @return Configuration Protocol Message with proper filter initialized
     * @throws IllegalArgumentException if query type is unsupported 
     */
    protected Message prepareRequestReadObjectsMessage(
            final ICfgQuery query) {

        if (query instanceof CfgXPathBasedQuery) {
            return RequestReadObjects2.create(
                    ((CfgXPathBasedQuery<?>) query).getObjectType(),
                    ((CfgXPathBasedQuery<?>) query).getXPath()
            );
        }

        int objType = CfgObjectType.CFGNoObject.ordinal();
        if (query instanceof ICfgFilterBasedQuery) {
            KeyValueCollection objectFilter = prepareReadObjectsFilter(
                    (ICfgFilterBasedQuery) query);
            objType = ((ICfgFilterBasedQuery) query).getCfgObjectType().ordinal();
            return RequestReadObjects.create(
                    objType,
                    objectFilter
            );
        }

        throw new IllegalArgumentException("unsupported query type");
    }


    private void initLogging() {
        getLogger().info("COM Logging started.");

        if (getLogger().isInfo()) {
            getLogger().infoFormat("Protocol state: {0}",
                    (confProtocol != null) ? getProtocol().getState() : null);
        }
    }

    /**
     * This private method checks that expected configuration object class
     * corresponds to object type defined by the given query.
     * 
     * @param cls configuration object class.
     * @param query objects reading/getting query.
     * @throws ConfigException in case when given query does not correspond to the given objects class.
     * @throws IllegalArgumentException in case of unsupported query type.
     */
    private <T extends ICfgObject> void checkObjectClass(
            final Class<T> cls,
            final ICfgQuery query) throws ConfigException {
        CfgObjectType qotype = null;
        if (query instanceof CfgFilterBasedQuery) {
            qotype = ((CfgFilterBasedQuery<?>) query).getCfgObjectType();
        }
        else if (query instanceof CfgXPathBasedQuery) {
            qotype = ((CfgXPathBasedQuery<?>) query).getCfgObjectType();
        }
        else {
        	throw new IllegalArgumentException("unsupported query type");
        }
        if (qotype != null) {
            Field typeField = null;
            CfgObjectType objtype = null;
            try {
                typeField = cls.getDeclaredField("OBJECT_TYPE");
            } catch (Exception e) { /* ignore it */; }
            if (typeField != null) {
                try {
                    objtype = (CfgObjectType) typeField.get(null);
                } catch (Exception e) { /* ignore it */; }
            }
            if (objtype != null) {
                if (objtype != qotype) {
                    throw new ConfigException(
                            "Incompatible result and query types");
                }
            }
        }
    }

    protected boolean addObjectOnRetrieve(
            final ICfgObject object) {
        if (object == null) {
            throw new NullPointerException("object");
        }

        IConfCache confCache = this.confCache;
        if (confCache == null) {
            return false;
        }

        if (confPolicy.getCacheOnRetrieve(object)) {
            try {
                if (confCache.contains(object)) {
                    if (confPolicy.getOverwriteCachedVersionOnRetrieve(object)) {
                        if (log.isDebug()) {
                            log.debugFormat("Updating object in cache [{0}], dbid: [{1}]",
                                    new Object[] {object.getObjectType(), object.getObjectDbid()});
                        }
                        confCache.update(object);
                    }
                } else {
                    if (log.isDebug()) {
                        log.debugFormat("Adding object to cache [{0}], dbid: [{1}]",
                                new Object[] {object.getObjectType(), object.getObjectDbid()});
                    }
                    confCache.add(object);
                }
                return true;
            } catch (Exception ex) {
                if (log.isDebug()) {
                    log.debug("Error adding to cache [" + object.getObjectType()
                            + "], dbid: [" + object.getObjectDbid() + "]", ex);
                }
                return false;
            }
        }

        return false;
    }

    private class COMMessageHandler implements MessageHandler {

        public void onMessage(final Message message) {
            if (!isDisposed) {
                final Runnable handler = () -> {
                    try {
                        eventService.handle(message);
                    } catch (final Exception ex) {
                        log.error("Exception incoming message handling in the COM EventService", ex);
                    } finally {
                        final MessageHandler userHndl = userMessageHandler;
                        if (userHndl != null) {
                            try {
                                userHndl.onMessage(message);
                            } catch (final Exception ex) {
                                log.error("Exception incoming message handling in User MessageHandler", ex);
                            }
                        }
                    }
                };
                if (asyncInvoker != null) {
                    asyncInvoker.invoke(handler);
                } else {
                    handler.run();
                }
            }
        }
    }

    private class COMChannelListener implements ChannelListener {

        @Override
        public void onChannelOpened(final EventObject event) {
            final IConfCache cache = confCache;
            final Protocol protocol = confProtocol;
            if (cache instanceof DefaultConfCache && protocol != null) {
                final DefaultConfCache defCache = (DefaultConfCache) cache;
                final Endpoint endpoint = protocol.getEndpoint();
                if (endpoint != null) {
                    defCache.addEndpoint(endpoint);
                }
            }
            if (asyncInvoker != null) {
                asyncInvoker.invoke(() -> {
                    eventService.onChannelOpened(event);
                });
            } else {
                eventService.onChannelOpened(event);
            }
        }

        @Override
        public void onChannelClosed(final ChannelClosedEvent event) {
            if (asyncInvoker != null) {
                asyncInvoker.invoke(() -> {
                    eventService.onChannelClosed(event);
                });
            } else {
                eventService.onChannelClosed(event);
            }
        }

        @Override
        public void onChannelError(final ChannelErrorEvent event) {
            if (asyncInvoker != null) {
                asyncInvoker.invoke(() -> {
                    eventService.onChannelError(event);
                });
            } else {
                eventService.onChannelError(event);
            }
        }
    }
}
