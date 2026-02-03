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
package com.genesyslab.platform.applicationblocks.com.runtime;

import com.genesyslab.platform.applicationblocks.com.*;
import com.genesyslab.platform.applicationblocks.com.cache.IConfCache;

import com.genesyslab.platform.applicationblocks.commons.Action;
import com.genesyslab.platform.applicationblocks.commons.Predicate;
import com.genesyslab.platform.applicationblocks.commons.broker.Subscriber;
import com.genesyslab.platform.applicationblocks.commons.broker.SubscriptionService;

import com.genesyslab.platform.configuration.protocol.confserver.events.EventError;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventBriefInfo;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectsRead;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectsSent;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectCreated;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectUpdated;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectDeleted;

import com.genesyslab.platform.configuration.protocol.obj.ConfObject;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectDelta;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectsCollection;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;

import com.genesyslab.platform.commons.protocol.*;

import com.genesyslab.platform.commons.timer.TimerAction;
import com.genesyslab.platform.commons.threading.AsyncInvoker;

import com.genesyslab.platform.commons.GEnum;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.EventObject;


/**
 * Internal service for asynchronous receiving of Configuration Protocol Events
 * and rising of corresponding COM Configuration Events.
 *
 * <p/><b><i>Note:</i></b> It is designed for COM AB internal use only.<br/>
 * It may be a subject of structural and functional changes without notice.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public final class EventService
        implements SubscriptionService<ConfEvent>,
                ChannelListener, AsyncInvokerSupport {

    private COMBrokerService<ConfEvent> myBrokerService;
    private IConfService             confService;
    private Protocol                 confProtocol;
    private AsyncInvoker             invoker = null;

    private final Map<Object, AsyncRequestResultImpl<? extends ICfgObject>> wmAsyncRequests =
            new HashMap<Object, AsyncRequestResultImpl<? extends ICfgObject>>();

    private static final ILogger log = Log.getLogger(EventService.class);

    @Deprecated
    public EventService(final ConfService cfgService) {
        this((IConfService) cfgService);
    }

    public EventService(final IConfService cfgService) {
        this.myBrokerService = new COMBrokerService<ConfEvent>();
        this.confService = cfgService;
        this.confProtocol = cfgService.getProtocol();

        // if (confProtocol != null) {
        //     confProtocol.addChannelListener(this);
        // }
    }


    public void register(final Subscriber<ConfEvent> subscriber) {
        myBrokerService.register(subscriber);
    }

    public void register(
            final Action<ConfEvent> handler,
            final Predicate<ConfEvent> actionFilter) {
        myBrokerService.register(handler, actionFilter);
    }

    public void unregister(final Subscriber<ConfEvent> subscriber) {
        myBrokerService.unregister(subscriber);
    }

    public void unregister(final Action<ConfEvent> handler) {
        myBrokerService.unregister(handler);
    }


    public void handle(final Message obj) {
        if (obj == null) {
            return;
        }
        if (log.isDebug()) {
            log.debug("Received message " + obj);
        }

        ConfEventImpl newEvent;
        Integer refId;

        switch (obj.messageId()) {
            case EventObjectsRead.ID:
            case EventObjectsSent.ID:
            case EventBriefInfo.ID:
            case EventError.ID:
                updateAsyncReadRequest(obj);
                break;

            case EventObjectUpdated.ID:
                final EventObjectUpdated objUpdMessage = (EventObjectUpdated) obj;
                final ConfObjectDelta confObjDelta = objUpdMessage.getObjectDelta();

                if (confObjDelta != null) {
                    newEvent = new ConfEventImpl(
                            confObjDelta.getObjectDbid(),
                            (CfgObjectType) GEnum.getValue(CfgObjectType.class,
                                    objUpdMessage.getObjectType()),
                            ConfEvent.EventType.ObjectUpdated,
                            (ICfgObject) CfgObjectActivator.createInstance(confService, confObjDelta, null),
                            getUnsolisitedEventNumber(objUpdMessage));
                } else {
                    newEvent = new ConfEventImpl(
                            -1,
                            (CfgObjectType) GEnum.getValue(CfgObjectType.class,
                                    objUpdMessage.getObjectType()),
                            ConfEvent.EventType.ObjectUpdated,
                            null,
                            getUnsolisitedEventNumber(objUpdMessage));
                }

                refId = objUpdMessage.getReferenceId();
                newEvent.setUnsolicited(refId == null || refId.intValue() == 0);
                publish(newEvent);
                break;

            case EventObjectCreated.ID:
                final EventObjectCreated upMessage = (EventObjectCreated) obj;
                final ConfObject confObj = upMessage.getObject();
                final Integer folderDbid = upMessage.getFolderDbid();
                String objectPath = null;
                try {
                    KeyValueCollection param = new KeyValueCollection();
                    if (folderDbid != null) {
                        param.addInt(MiscConstants.FolderDbidName, folderDbid);
                    }

                    final ICfgObject cfgObj = (ICfgObject) CfgObjectActivator.createInstance(
                                confService, confObj, new Object[] { param, objectPath });

                    newEvent = new ConfEventImpl(
                            confObj.getObjectDbid(),
                            confObj.getObjectType(),
                            ConfEvent.EventType.ObjectCreated,
                            cfgObj,
                            getUnsolisitedEventNumber(upMessage)
                    );

                    refId = upMessage.getReferenceId();
                    newEvent.setUnsolicited(refId == null || refId.intValue() == 0);
                    publish(newEvent);
                } catch (Exception e) {
                    log.error("Error instantiating configuration object", e);
                    log.debug(confObj);
                }

                break;

            case EventObjectDeleted.ID:
                final EventObjectDeleted objDelMessage = (EventObjectDeleted) obj;

                newEvent = new ConfEventImpl(
                        objDelMessage.getDbid(),
                        (CfgObjectType) GEnum.getValue(
                                CfgObjectType.class,
                                objDelMessage.getObjectType()
                        ),
                        ConfEvent.EventType.ObjectDeleted,
                        null,
                        getUnsolisitedEventNumber(objDelMessage)
                );

                refId = objDelMessage.getReferenceId();
                newEvent.setUnsolicited(refId == null || refId.intValue() == 0);
                publish(newEvent);
                break;

            default:
                break;
        }
    }

    private int getUnsolisitedEventNumber(Message message) {
        Integer id = (Integer) message.getMessageAttribute("UnsolisitedEventNumber");
        if (id != null) {
            return id;
        }
        else {
            return 0;
        }
    }

    private void publish(final ConfEvent newEvent) {
        if (invoker != null) {
            invoker.invoke(new AsyncConfEventPublisher(newEvent));
        } else {
            myBrokerService.publish(newEvent);
        }
    }

    public void setInvoker(final AsyncInvoker invoker) {
        this.invoker = invoker;
    }

    public void dispose() {
        // if (confProtocol != null) {
        //     confProtocol.removeChannelListener(this);
        // }
        closeWaitingRequests(null);
        confProtocol = null;
        // myBrokerService = null;
        confService = null;
        invoker = null;
    }

    public void registerAsyncRequest(
            final Object ref,
            final AsyncRequestResultImpl<? extends ICfgObject> asyncReq) {
        synchronized (wmAsyncRequests) {
            wmAsyncRequests.put(ref, asyncReq);
            asyncReq.setTimeoutAction(new TimeoutTask(ref));
        }
    }

    @SuppressWarnings("rawtypes")
    protected void updateAsyncReadRequest(final Message incoming) {
        Object msgRef = ((Referenceable) incoming).retreiveReference();

        if (msgRef != null) {
            AsyncRequestResultImpl rqI;

            synchronized (wmAsyncRequests) {
                rqI = wmAsyncRequests.get(msgRef);
                if ((rqI != null) && (rqI.isCancelled())) {
                    // If original request is canceled - forget about this request
                    // and do not process this and, may be, further messages:
                    wmAsyncRequests.remove(msgRef);
                    rqI = null;
                    if (log.isDebug()) {
                        log.debug("Got incoming message on canceled request: " + msgRef);
                    }
                }
                if (rqI != null) {
                    // Remove reference to request if we got not a "partial data" message
                    if (incoming.messageId() != EventObjectsRead.ID
                            && incoming.messageId() != EventBriefInfo.ID) {
                        wmAsyncRequests.remove(msgRef);
                    } else {
                        if (log.isDebug()) {
                            log.debug("Got incoming message with partial data for " + msgRef);
                        }
                    }
                }
            }

            if ((rqI != null) && !rqI.isCancelled()) {
                if (incoming.messageId() == EventObjectsSent.ID) {
                    // All data parts are already sent/read - activate classes:
                    if (!rqI.isDone()) { // if there was no exception
                        rqI.finishResultSet();
                    }
                } else if (incoming.messageId() == EventObjectsRead.ID) {
                    createObjectsFromReceivedMessages(
                            rqI, (EventObjectsRead) incoming);
                } else if (incoming.messageId() == EventBriefInfo.ID) {
                    createObjectsFromReceivedMessages(
                            rqI, (EventBriefInfo) incoming);
                } else if (incoming.messageId() == EventError.ID) {
                    log.error("Error objects reading: " + incoming.toString());
                    rqI.setException(
                            ProtocolOperationsHelper.createConfigServerException(
                                    (EventError) incoming)
                    );
                }
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void createObjectsFromReceivedMessages(
            final AsyncRequestResultImpl requestImpl,
            final EventObjectsRead dataReadMsg) {
        Collection<ICfgObject> resultCollection = null;
        Exception exception = null;
        try {
            int[] iFolders = null;
            long[] lFolders = dataReadMsg.getFolderDbids();
            if (lFolders != null) {
                iFolders = new int[lFolders.length];
                for (int i = 0; i < lFolders.length; i++) {
                    iFolders[i] = (int) lFolders[i];
                }
            }
            ConfObjectsCollection objectsData = dataReadMsg.getObjects();
            if (objectsData == null) {
                throw new ConfigRuntimeException(
                        "Got no configuration objects data in 'EventObjectsRead'");
            }
            resultCollection = confService.createMultipleObjects(
                    objectsData, dataReadMsg.getObjectPaths(), iFolders);
        } catch (Throwable throwable) {
            log.error("Error objects activation", throwable);
            if ((throwable instanceof ConfigException)
                    || (throwable instanceof ConfigRuntimeException)) {
                exception = (Exception) throwable;
            } else {
                exception = new ConfigRuntimeException(
                            "Unexpected exception", throwable);
            }
        }
        if (resultCollection != null) {
            final IConfCache confCache = confService.getCache();
            final IConfServicePolicy confPolicy = confService.getPolicy();
            if (confCache != null && confPolicy != null) {
                for (final ICfgObject object: resultCollection) {
                    if (object != null && confPolicy.getCacheOnRetrieve(object)) {
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
                        } catch (final Exception ex) {
                            if (log.isDebug()) {
                                log.debug("Error adding to cache [" + object.getObjectType()
                                        + "], dbid: [" + object.getObjectDbid() + "]", ex);
                            }
                        }
                    }
                }
            }
            requestImpl.addToResultSet(resultCollection);
        } else {
            requestImpl.setException(exception);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void createObjectsFromReceivedMessages(
            final AsyncRequestResultImpl requestImpl,
            final EventBriefInfo dataReadMsg) {
        ICfgObject object = null;
        Exception exception = null;
        try {
            object = confService.createObject(dataReadMsg.getBriefObject(), true);
        } catch (Throwable throwable) {
            log.error("Error objects activation", throwable);
            if ((throwable instanceof ConfigException)
                    || (throwable instanceof ConfigRuntimeException)) {
                exception = (Exception) throwable;
            } else {
                exception = new ConfigRuntimeException(
                            "Unexpected exception", throwable);
            }
        }
        if (object != null) {
            requestImpl.addToResultSet(object);
        } else {
            requestImpl.setException(exception);
        }
    }


    public void onChannelOpened(final EventObject event) {
    }

    public void onChannelClosed(final ChannelClosedEvent event) {
        Throwable cause = null;
        if (event != null) {
            cause = event.getCause();
        }
        closeWaitingRequests(cause);
    }

    private void closeWaitingRequests(
            final Throwable cause) {
        synchronized (wmAsyncRequests) {
            if (!wmAsyncRequests.isEmpty()) {
                log.info("There are waiting async reading clients while channel get closed");
                Exception exception = null;
                if (cause != null) {
                    if (cause instanceof Exception) {
                        exception = (Exception) cause;
                    } else {
                        exception = new ConfigRuntimeException(
                                "Unexpected channel error", cause);
                    }
                }
                if (exception == null) {
                    exception = new ConfigRuntimeException(
                            "Connection channel has been closed");
                }
                for (AsyncRequestResultImpl<? extends ICfgObject> elem :
                            wmAsyncRequests.values()) {
                    elem.setException(exception);
                }
                wmAsyncRequests.clear();
            }
        }
    }

    public void onChannelError(final ChannelErrorEvent event) {
        log.warn("onChannelError - " + event);
    }

    private class AsyncConfEventPublisher implements Runnable {
        private ConfEvent theEvent;

        AsyncConfEventPublisher(final ConfEvent event) {
            theEvent = event;
        }

        public void run() {
            myBrokerService.publish(theEvent);
        }
    }

    private class TimeoutTask implements TimerAction {
        private final Object msgRef;

        TimeoutTask(final Object msgRef) {
            this.msgRef = msgRef;
        }

        public void onTimer() {
            AsyncRequestResultImpl<? extends ICfgObject> asyncFuture = null;
            synchronized (wmAsyncRequests) {
                asyncFuture = wmAsyncRequests.remove(msgRef);
            }
            if (asyncFuture != null) {
                asyncFuture.timeout();
            }
        }

        @Override
        public String toString() {
            return "EventService.TimeoutTask for " + confProtocol;
        }
    }
}
