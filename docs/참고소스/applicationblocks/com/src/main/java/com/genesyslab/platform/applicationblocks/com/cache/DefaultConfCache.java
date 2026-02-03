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
// Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.com.cache;

import com.genesyslab.platform.applicationblocks.com.*;
import com.genesyslab.platform.applicationblocks.com.runtime.COMBrokerService;

import com.genesyslab.platform.applicationblocks.commons.Action;
import com.genesyslab.platform.applicationblocks.commons.Predicate;
import com.genesyslab.platform.applicationblocks.commons.broker.Subscriber;
import com.genesyslab.platform.applicationblocks.commons.broker.SubscriptionService;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;

import com.genesyslab.platform.commons.protocol.AsyncInvokerSupport;
import com.genesyslab.platform.commons.protocol.Endpoint;

import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.xmlfactory.XmlFactories;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;

import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.*;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.OutputStream;
import java.io.InputStream;


/**
 * The default implementation of the configuration cache interface.
 */
public final class DefaultConfCache
        implements IConfCache,
                SubscriptionService<ConfCacheEvent>,
                AsyncInvokerSupport {

    private IConfCachePolicy policy;
    private AsyncInvoker invoker = null;

    private final IConfCacheStorage storage;
    private final IConfCacheQueryEngine queryEngine;
    private final COMBrokerService<ConfCacheEvent> brokerService;

    private final List<Endpoint> endpoints = new ArrayList<Endpoint>();

    private static DocumentBuilder documentBuilder = null;
    private static Transformer transformer = null;
    private static XPath xpath = null;

    private static final String CONFIG_NS =
            ConfServerProtocol.PROTOCOL_DESCRIPTION.getNS();
    private static final String CACHE_NS =
            "http://schemas.genesyslab.com/Protocols/Configuration/COMCache/2009/";

    private static final ILogger log = Log.getLogger(DefaultConfCache.class);

    static {
        try {
            documentBuilder = XmlFactories.newDocumentBuilderNS();
            transformer = XmlFactories.newTransformer();
            xpath = XmlFactories.newXPath();
            xpath.setNamespaceContext(new NamespaceContext() {
                public String getNamespaceURI(final String prefix) {
                    String uri;
                    if (prefix.length() == 0 || prefix.equals("cache")) {
                        uri = CACHE_NS;
                    } else if (prefix.equals("cfg")) {
                        uri = CONFIG_NS;
                    } else {
                        uri = null;
                    }
                    return uri;
                }

                // Dummy implementation - not used!
                @SuppressWarnings("rawtypes")
                public Iterator getPrefixes(final String val) { return null; }
                // Dummy implementation - not used!
                public String getPrefix(final String uri) { return null; }
            });
        } catch (Exception e) {
            log.error("Exception initializing XML engine", e);
            throw new RuntimeException(e);
        }
    }

    private interface SerializationConstants {
        String CACHE_NODE                = "Cache";
        String CONFIGURATION_NODE        = "CacheConfiguration";
        String CONFIGURATION_SERVER_NODE = "ConfigurationServer";
        String CONFIGURATION_DATA_NODE   = "ConfData";
        String NAME_ATTRIBUTE            = "name";
        String URI_ATTRIBUTE             = "uri";
    }

    /**
     * Creates a cache which will use the default policy, storage and query
     * engine.
     */
    public DefaultConfCache() {
        this(null, null, null);
    }

    /**
     * Creates a new instance of the configuration cache.
     *
     * @param cachePolicy The cache policy
     * @param cacheStorage The storage to be used in this cache
     * @param cacheQueryEngine The query engine to be used to retrieve
     *            data from the storage
     */
    public DefaultConfCache(
            final IConfCachePolicy cachePolicy,
            final IConfCacheStorage cacheStorage,
            final IConfCacheQueryEngine cacheQueryEngine) {

        this.storage = (cacheStorage != null)
                ? cacheStorage : new DefaultConfCacheStorage();
        this.queryEngine = (cacheQueryEngine != null)
                ? cacheQueryEngine : new DefaultConfCacheQueryEngine(this.storage);

        this.brokerService = new COMBrokerService<ConfCacheEvent>();

        this.policy = (cachePolicy != null)
                ? cachePolicy : new DefaultConfCachePolicy();
    }


    public void setInvoker(final AsyncInvoker value) {
        this.invoker = value;
    }

    private void performRefresh()
            throws ConfigException, InterruptedException {
        for (ICfgObject obj : storage.retrieve(ICfgObject.class)) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            obj.refresh();
        }
    }

    private void onObjectUpdated(final ConfEvent configEvent)
            throws ConfigException {

        ICfgObject obj = queryEngine.retrieveObject(
                ICfgObject.class, configEvent.getObjectType(), configEvent.getObjectId());

        if (obj != null && policy.getTrackUpdates(obj)) {

            obj.update((ICfgDelta) configEvent.getCfgObject());

            if (policy.getReturnCopies()) {
                try {
                    obj = (ICfgObject) obj.clone();
                } catch (final CloneNotSupportedException ex) {
                    log.debug("Failed to create objects clone", ex);
                }
            }

            publish(new ConfCacheEvent(obj, ConfCacheUpdateType.ObjectUpdated));
        }
    }

    private void onObjectDeleted(final ConfEvent configEvent) {
        if (!configEvent.isUnsolicited()) {
            return;
        }

        ICfgObject obj = retrieve(ICfgObject.class,
                configEvent.getObjectType(), configEvent.getObjectId());

        if (obj != null && policy.getRemoveOnDelete(obj)) {
            remove(obj);
        }
    }


    private void onObjectCreated(final ConfEvent configEvent) {
        if ((configEvent == null)
                || (configEvent.getCfgObject() == null)) {
            throw new IllegalArgumentException(
                    "Expected 'ConfEvent' with CfgObject attached");
        }

        if (configEvent.isUnsolicited()
                && policy.getCacheOnCreate(configEvent.getCfgObject())) {
            add(configEvent.getCfgObject());
        }
    }
    
    private void publish(final ConfCacheEvent event) {
        if (invoker != null) {
            invoker.invoke(new AsyncCacheEventPublisher(event));
        } else {
            brokerService.publish(event);
        }
    }

    private void validate(final ICfgObject obj) {
        if (obj == null) {
            throw new NullPointerException("obj");
        }
        if (obj instanceof ICfgBriefInfo) {
            throw new IllegalArgumentException(
                    "BriefInfo structures are not to be stored in cache.");
        }
        if (!endpoints.contains(obj.getEndpoint())) {
            throw new IllegalArgumentException(
                    "The specified object does not belong to a configuration server supported by this cache.");
        }
    }

    public int getEndpointCount() {
        return endpoints.size();
    }

    /**
     * Adds a supported endpoint to the list. By default, the endpoint of the configuration
     * service with which this cache is initially associated is added. This method should be used
     * to add any other endpoints at which the objects in this cache are valid -- for instance the
     * endpoint of a backup configuration server or a proxy. Note that ALL objects in the cache are assumed
     * to be valid at ALL endpoints. This means that adding unrelated configuration servers to the list
     * is unsupported and will result in undefined behavior.
     *
     * @param endpoint The endpoint of a configuration server
     */
    public void addEndpoint(final Endpoint endpoint) {
        Endpoint existing = null;
        for (Endpoint ep : endpoints) {
            if (ep != null && ep.equals(endpoint)) {
                existing = ep;
                break;
            }
        }
        if (existing == null) {
            endpoints.add(endpoint);
        }
    }

    public boolean removeEndpoint(final Endpoint endpoint) {
        Endpoint existing = null;
        for (Endpoint ep : endpoints) {
            if (ep != null && ep.equals(endpoint)) {
                existing = ep;
                break;
            }
        }
        if (existing != null) {
            endpoints.remove(existing);
            return true;
        }
        return false;
    }

    /**
     * Adds a new configuration object to the cache.
     *
     * An "ObjectAdded" cache event is fired upon the successful completion of
     * this operation.
     *
     * @param obj A configuration object
     */
    public void add(ICfgObject obj) {
        validate(obj);
        storage.add(obj);
        publish(new ConfCacheEvent(obj,
                ConfCacheUpdateType.ObjectAdded));
    }


    /**
     * Overwrites a configuration object which already exists in the cache
     * with a new copy.
     *
     * An "ObjectUpdated" cache event is fired upon the successful completion of
     * this operation.
     *
     * @param obj A configuration object
     */
    public void update(ICfgObject obj) {
        validate(obj);
        storage.update(obj);
        publish(new ConfCacheEvent(obj,
                ConfCacheUpdateType.ObjectUpdated));
    }

    /**
     * Removes the specified configuration object from the cache.
     *
     * An "ObjectRemoved" cache event is fired upon the successful completion of
     * this operation.
     *
     * @param obj A configuration object
     */
    public void remove(final ICfgObject obj) {
        if (obj == null) {
            throw new NullPointerException("obj");
        }
        if (obj instanceof ICfgBriefInfo) {
            throw new IllegalArgumentException(
                    "BriefInfo structures are not to be stored in cache.");
        }
        if (storage.remove(obj)) {
            publish(new ConfCacheEvent(obj,
                    ConfCacheUpdateType.ObjectRemoved));
        }
    }

    /**
     * Removes the configuration object with the specified type and dbid
     * from the cache.
     *
     * An "ObjectRemoved" cache event is fired upon the successful completion of
     * this operation.
     *
     * @param type The type of configuration object
     * @param dbid The dbid of the configuration object
     */
    public void remove(final CfgObjectType type, final int dbid) {
        for (ICfgObject obj : storage.retrieve(
                                ICfgObject.class, new CacheKey(type, dbid))) {
            if ((obj.getObjectDbid() == dbid)
                    && (obj.getObjectType() == type)) {
                remove(obj);
                break;
            }
        }
    }

    /**
     * Retrieves a configuration object from the cache using the cache's
     * query engine.
     *
     * @param <T> The type of configuration object that should be returned
     * @param cls class of object to be retrieved
     * @param type The type of configuration object
     * @param dbid The dbid of the configuration object
     * @return A configuration object of the requested type
     */
    @SuppressWarnings("unchecked")
    public <T extends ICfgObject> T retrieve(
                final Class<T> cls,
                final CfgObjectType type,
                final int dbid) {
        T obj = queryEngine.retrieveObject(cls, type, dbid);

        if (obj != null && policy.getReturnCopies()) {
            return (T) createClone(obj);
        }

        return obj;
    }

    /**
     * Retrieves a configuration object from the cache using the cache's
     * query engine.
     *
     * @param <T> The type of configuration object that should be returned
     * @param cls class of object to be retrieved
     * @param query A query based on which the result is obtained
     * @return A configuration object matching the specified query
     */
    @SuppressWarnings("unchecked")
    public <T extends ICfgObject> T retrieve(
            final Class<T> cls,
            final ICfgQuery query) {
        T obj = queryEngine.retrieveObject(cls, query);

        if (obj != null && policy.getReturnCopies()) {
            return (T) createClone(obj);
        }

        return obj;
    }

    /**
     * Retrieves an enumerable list of objects from the cache using the cache's query engine.
     *
     * @param <T> The types of configuration objects to be included in the list
     * @param cls class of object to be retrieved
     * @param query A query based on which the result is obtained
     * @return An enumerable list of configuration objects matching the specified query
     */
    public <T extends ICfgObject> Iterable<T> retrieveMultiple(
            final Class<T> cls,
            final ICfgQuery query) {
        return queryEngine.retrieveMultipleObjects(cls, query);
    }

    /**
     * Retrieves an enumerable list of all configuration objects in the storage.
     *
     * Unlike the other Get methods this method does not use the query engine.
     * Instead, all cached objects are returned, filtered by the T parameter.
     *
     * @param <T> The type of configuration object to include
     *                (use ICfgObject to retrieve an enumeration of all stored objects)
     * @param cls class of object to be retrieved
     * @return An enumerable list of configuration objects
     */
    public <T extends ICfgObject> Iterable<T> retrieveMultiple(
            final Class<T> cls) {
        return storage.retrieve(cls);
    }

    /**
     * Serializes the cache into the specified stream.
     *
     * @param stream The stream into which the cache is to be written
     */
    public void serialize(final OutputStream stream) {
        serialize(new StreamResult(stream));
    }

    /**
     * Serializes the cache into the specified result.
     *
     * @param result The result into which the cache is to be written
     * @see javax.xml.transform.stream.StreamResult
     * @see javax.xml.transform.dom.DOMResult
     */
    public void serialize(final Result result) {
        try {
            Document doc = documentBuilder.newDocument();
            Node nodeRoot = doc.createElementNS(CACHE_NS,
                    SerializationConstants.CACHE_NODE);
            Node nodeCacheConf = doc.createElementNS(CACHE_NS,
                    SerializationConstants.CONFIGURATION_NODE);
            Node nodeCacheData = doc.createElementNS(CONFIG_NS,
                    SerializationConstants.CONFIGURATION_DATA_NODE);

            doc.appendChild(nodeRoot);
            nodeRoot.appendChild(nodeCacheConf);
            nodeRoot.appendChild(nodeCacheData);

            serializeCacheConfig(nodeCacheConf);
            serializeCacheData(nodeCacheData);

            transformer.transform(new DOMSource(doc), result);
        } catch (Exception e) {
            throw new ConfigRuntimeException("Can't serialize cache", e);
        }
    }

    private void serializeCacheConfig(final Node container) {
        for (Endpoint ep : endpoints) {
            if (ep.getName() == null || ep.getUri() == null) {
                continue;
            }

            Document doc = container.getOwnerDocument();
            Node child = doc.createElementNS(
                    container.getNamespaceURI(),
                    SerializationConstants.CONFIGURATION_SERVER_NODE);

            Attr attr = doc.createAttribute(SerializationConstants.NAME_ATTRIBUTE);
            attr.setValue(ep.getName());
            child.getAttributes().setNamedItem(attr);

            attr = doc.createAttribute(SerializationConstants.URI_ATTRIBUTE);
            attr.setValue(ep.getUri().toString());
            child.getAttributes().setNamedItem(attr);

            container.appendChild(child);
        }
    }

    private void deserializeCacheConfig(final NodeList options)
            throws XPathExpressionException, URISyntaxException {
        if (options != null) {
            for (int i = 0; i < options.getLength(); i++) {
                Node item = options.item(i);
                if (item.getNodeName().equals(
                        SerializationConstants.CONFIGURATION_SERVER_NODE)) {
                    Node a1 = item.getAttributes()
                            .getNamedItem(SerializationConstants.NAME_ATTRIBUTE);
                    Node a2 = item.getAttributes()
                            .getNamedItem(SerializationConstants.URI_ATTRIBUTE);
                    Endpoint ep = new Endpoint(a1.getNodeValue(),
                            new URI(a2.getNodeValue()));
                    endpoints.add(ep);
                }
            }
        }
    }

    private void serializeCacheData(final Node node) {
        if (storage != null) {
            Document doc = node.getOwnerDocument();

            for (ICfgObject obj : storage.retrieve(ICfgObject.class)) {
                Node objNode = obj.toXml();
                Node child = doc.adoptNode(objNode);
                if (child == null) {
                    child = doc.importNode(objNode, true);
                }
                if (child != null) {
                    node.appendChild(child);
                }
            }
        }
    }

    /**
     * Deserializes the cache from the specified stream.
     *
     * @param stream The stream from which the cache is to be read
     */
    public void deserialize(final InputStream stream) {
        deserialize(new StreamSource(stream));
    }

    /**
     * Deserializes the cache from the specified source.
     *
     * @param source The source from which the cache is to be read
     * @see javax.xml.transform.stream.StreamSource
     * @see javax.xml.transform.dom.DOMSource
     */
    public void deserialize(final Source source) {
        try {
            if (source instanceof StreamSource) {
                Document doc = documentBuilder.parse(((StreamSource) source).getInputStream());
                deserialize(doc);
            } else {
                DOMResult result = new DOMResult(documentBuilder.newDocument());
                transformer.transform(source, result);
                deserialize(result.getNode());
            }
        } catch (Exception e) {
            throw new ConfigRuntimeException("Can't deserialize cache", e);
        }
    }

    /**
     * Deserializes the cache from the specified source.
     *
     * @param source The source from which the cache is to be read
     * @see javax.xml.transform.stream.StreamSource
     * @see javax.xml.transform.dom.DOMSource
     */
    protected void deserialize(final Node source) {
        try {
            IConfService confService = null;

            NodeList options = (NodeList) xpath.evaluate(
                    "cache:" + SerializationConstants.CACHE_NODE + "/cache:"
                            + SerializationConstants.CONFIGURATION_NODE + "/*",
                        source, XPathConstants.NODESET);
            if (options == null) {
                options = (NodeList) xpath.evaluate(
                        SerializationConstants.CACHE_NODE + "/"
                                + SerializationConstants.CONFIGURATION_NODE + "/*",
                            source, XPathConstants.NODESET);
            }
            if (options != null) {
                deserializeCacheConfig(options);
            }

            for (Endpoint ep : endpoints) {
                confService = ConfServiceFactory.retrieveConfService(ep);
                if (confService != null) {
                    break;
                }
            }
            if (confService == null){
                throw new ConfigRuntimeException("Can't deserialize cache. No configured endpoints");
            }

            Node data = (Node) xpath.evaluate(
                    "cache:" + SerializationConstants.CACHE_NODE + "/cfg:"
                        + SerializationConstants.CONFIGURATION_DATA_NODE,
                    source, XPathConstants.NODE);
            if (data == null) {
                data = (Node) xpath.evaluate(
                        SerializationConstants.CACHE_NODE + "/"
                            + SerializationConstants.CONFIGURATION_DATA_NODE,
                        source, XPathConstants.NODE);
            }
            if (data == null) {
                throw new ConfigRuntimeException("Can't deserialize cache. The serialized data is not in a valid format.");
            }
            Collection<ICfgObject> collection = confService.createMultipleObjectsFromXML(data);
            for (ICfgObject obj : collection) {
                try{
                    add(obj);
                } catch(IllegalArgumentException ex) {
                    log.debug("Failed to add object in cache: ", ex);
                }
            }
        } catch (Exception e) {
            throw new ConfigRuntimeException("Can't deserialize cache", e);
        }
    }

    /**
     * Removes all cache contents.
     */
    public void clear() {
        storage.clear();
    }

    /**
     * Synchronously updates all configuration objects which are currently in the cache.
     *
     * @throws ConfigException is thrown in case of object(s) reload exception
     * @throws InterruptedException is thrown if task has been interrupted
     */
    public void refresh() throws ConfigException, InterruptedException {
        performRefresh();
    }

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
    public Future<IConfCache> beginRefresh(
            final AsyncInvoker asyncInvoker,
            final Action<Future<IConfCache>> finishCallback) {
        if (asyncInvoker == null) {
            throw new NullPointerException("AsyncInvoker");
        }
        AsyncRefresh asyncRefresh = new AsyncRefresh(finishCallback);
        asyncInvoker.invoke(asyncRefresh);
        return asyncRefresh;
    }

    /**
     * To be called when the asynchronous refresh operation is complete. If called before
     * the refresh operation completes, this method will block until completion.
     *
     * @param asyncResult The async result associated with the current operation
     */
    public void endRefresh(
            final Future<IConfCache> asyncResult) {
        if (!(asyncResult instanceof AsyncRefresh)) {
            throw new IllegalArgumentException("asyncResult");
        }
        try {
            asyncResult.get();
        } catch (Exception e) {
            log.error("Exception waiting for async refresh done");
        }
    }

    /**
     * Returns the current policy associated with this cache.
     *
     * @return current policy associated with this cache
     */
    public IConfCachePolicy getPolicy() {
        return this.policy;
    }

    public void setPolicy(final IConfCachePolicy cachePolicy) {
        this.policy = cachePolicy;
    }

    /**
     * Determines whether the cache contains the specified object.
     *
     * For the purposes of this method, two objects are considered
     * equal if they have the same dbid and type.
     *
     * @param obj The configuration object to look for
     * @return true if the object is in the cache, false otherwise
     */
    public boolean contains(final ICfgObject obj) {
        if (obj == null) {
            throw new NullPointerException("obj");
        }
        if (obj instanceof ICfgBriefInfo) {
            throw new IllegalArgumentException(
                    "BriefInfo structures are not to be stored in cache.");
        }
        return (retrieve(ICfgObject.class,
                         obj.getObjectType(), obj.getObjectDbid())
                != null);
    }


    /**
     * Registers a subscriber object for receiving notifications from
     * the cache.
     *
     * @param subscriber the "subscriber" object which will handle the notifications
     */
    public void register(final Subscriber<ConfCacheEvent> subscriber) {
        brokerService.register(subscriber);
    }

    /**
     * Registers a delegate for receiving notifications from the cache
     * based on the specified filter.
     *
     * @param handler The delegate which will handle the event
     * @param filter A filter to apply to the event
     */
    public void register(
            final Action<ConfCacheEvent> handler,
            final Predicate<ConfCacheEvent> filter) {
        brokerService.register(handler, filter);
    }

    /**
     * Unregisters the subscriber from event notifications.
     *
     * @param subscriber the subscriber to unregister
     */
    public void unregister(
            final Subscriber<ConfCacheEvent> subscriber) {
        brokerService.unregister(subscriber);
    }

    /**
     * Unregisters the specified delegate from notifications.
     *
     * @param handler the function to unregister
     */
    public void unregister(
            final Action<ConfCacheEvent> handler) {
        brokerService.unregister(handler);
    }


    public Predicate<ConfEvent> getFilter() {
        return null;
    }

    public void handle(final ConfEvent configEvent) {
        try {
            if (configEvent.getEventType()
                    == ConfEvent.EventType.ObjectCreated) {
                onObjectCreated(configEvent);
            } else if (configEvent.getEventType()
                    == ConfEvent.EventType.ObjectDeleted) {
                onObjectDeleted(configEvent);
            } else if (configEvent.getEventType()
                    == ConfEvent.EventType.ObjectUpdated) {
                onObjectUpdated(configEvent);
            }
        } catch (ConfigException ex) {
            log.error("Exception handling ConfEvent", ex);
        }
    }

    private ICfgObject createClone(final ICfgObject obj) {
        try {
            return (ICfgObject) obj.clone();
        } catch (CloneNotSupportedException e) {
            throw new ConfigRuntimeException(
                    "Exception clonning object from cache", e);
        }
    }


    private class AsyncCacheEventPublisher implements Runnable {
        private ConfCacheEvent theEvent;

        AsyncCacheEventPublisher(final ConfCacheEvent event) {
            theEvent = event;
        }

        public void run() {
            brokerService.publish(theEvent);
        }
    }

    private class AsyncRefresh
            implements Future<IConfCache>, Runnable {

        private Action<Future<IConfCache>> callback;
        private boolean done = false;
        private boolean canceled = false;
        private Exception exception = null;

        private final Object syncObj = new Object();

        AsyncRefresh(
                final Action<Future<IConfCache>> finishCallback) {
            this.callback = finishCallback;
        }

        public boolean cancel(final boolean mayInterruptIfRunning) {
            synchronized(syncObj) {
                canceled = true;
                return canceled;
            }
        }

        public boolean isCancelled() {
            synchronized(syncObj) {
                return canceled;
            }
        }

        public boolean isDone() {
            synchronized(syncObj) {
                return done;
            }
        }

        public IConfCache get()
                    throws InterruptedException, ExecutionException {
            try {
                return get(0, null);
            } catch (TimeoutException e) {
                log.error("Timeout waiting for sync refresh", e);
                return null;
            }
        }

        public IConfCache get(
                final long timeout,
                final TimeUnit unit)
                    throws InterruptedException, ExecutionException,
                            TimeoutException {
            synchronized (syncObj) {
                long actTimeout = timeout;
                if (unit != null) {
                    actTimeout = unit.toMillis(timeout); // we drops "excessNanos"
                }
                if (actTimeout > 0) {
                    long timeoutMark = System.currentTimeMillis() + actTimeout;
                    long waitTime = actTimeout;
                    do {
                        if (done) break;
                        syncObj.wait(waitTime);
                        waitTime = timeoutMark - System.currentTimeMillis();
                    } while (waitTime > 0);
                } else {
                    while (!done) {
                        syncObj.wait();
                    }
                }
                if (done) {
                    if (exception == null) {
                        return DefaultConfCache.this;
                    } else {
                        throw new ExecutionException(exception);
                    }
                }
            }
            return null;
        }

        public void run() {
            Exception operationExc = null;
            try {
                //refresh();
                for (ICfgObject obj : storage.retrieve(ICfgObject.class)) {
                    if (isCancelled() || Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    obj.refresh();
                }
            } catch (final Exception ex) {
                operationExc = ex;
                log.error("Exception while Cache.refresh()", ex);
            } finally {
                synchronized (syncObj) {
                    exception = operationExc;
                    done = true;
                    if (callback != null) {
                        try {
                            callback.handle(AsyncRefresh.this);
                        } catch (Exception e) {
                            log.error("Exception in custom handler", e);
                        }
                    }
                    syncObj.notifyAll();
                }
            }
        }
    }
}
