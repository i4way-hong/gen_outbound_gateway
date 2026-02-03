// ===============================================================================
//
//  Any authorized distribution of any copy of this code (including any related
//  documentation) must reproduce the following restrictions, disclaimer and copyright
//  notice:
//
//  The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
//  (even as a part of another name), endorse and/or promote products derived from
//  this code without prior written permission from Genesys Telecommunications
//  Laboratories, Inc.
//
//  The use, copy, and/or distribution of this code is subject to the terms of the Genesys
//  Developer License Agreement.  This code shall not be used, copied, and/or
//  distributed under any other license agreement.
//
//  THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
//  ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
//  DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
//  WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
//  NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
//  NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
//  EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
//  CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
//  NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
//  Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.applicationblocks.com.runtime;

import com.genesyslab.platform.applicationblocks.commons.Action;
import com.genesyslab.platform.applicationblocks.commons.Predicate;
import com.genesyslab.platform.applicationblocks.commons.broker.Subscriber;
import com.genesyslab.platform.applicationblocks.commons.broker.PublishingService;
import com.genesyslab.platform.applicationblocks.commons.broker.SubscriptionService;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * <code>BrokerServiceBase</code> class implements the Publish/Subscribe pattern.
 */
public class COMBrokerService<T>
        implements PublishingService<T>, SubscriptionService<T> {

    private CopyOnWriteArrayList<Subscriber<T>> subscribers;

    private final Object subscribersLock = new Object();
    private static final ILogger logger = Log.getLogger(COMBrokerService.class);


    public COMBrokerService() {
        subscribers = new CopyOnWriteArrayList<Subscriber<T>>();
    }


    /**
     * Publishes an event.
     *
     * @param obj Object that is used for event processing
     */
    public void publish(final T obj) {
        if (logger.isDebug()) {
            logger.debugFormat("Publishing {0} ...", obj);
        }

        notify(obj);

        if (logger.isDebug()) {
            logger.debugFormat("Publishing {0} is completed", obj);
        }
    }

    /**
     * Registers a subscriber for notifications about and processing of publishing events.
     * Implements subscription functionality of the Publish/Subscribe pattern.
     *
     * @param subscriber subscriber object being registered
     */
    public void register(final Subscriber<T> subscriber) {
        if (subscriber == null) {
            throw new NullPointerException("Subscriber is null.");
        }

        synchronized (subscribersLock) {
            if (subscribers.contains(subscriber)) {
                throw new IllegalArgumentException(
                        "subscriber: Subscriber is already registered.");
            }

            subscribers.add(subscriber);
        }
    }

    /**
     * Subscribes an action to be performed when a publishing event occurs.
     * Implements subscription functionality of the Publish/Subscribe pattern.
     * The method also processes the filter custom attributes which allows a user to define
     * filters externally.
     *
     * @param handler the method that performs an action on the specified object
     *      when a publishing event occurs
     * @param filter Filter predicate that allows checking whether publishing event
     *      should be processed or ignored
     */
    public void register(final Action<T> handler, final Predicate<T> filter) {
        if (handler == null) {
            throw new NullPointerException("handler is null.");
        }

        synchronized (subscribersLock) {
            for (Subscriber<T> s : subscribers) {
                if (s != null && s instanceof DelegateSubscriber) {
                    DelegateSubscriber<T> s1 = ((DelegateSubscriber<T>) s);
                    if (s1.getHandler() == handler) {
                        throw new IllegalArgumentException(
                                "handler is already registered.");
                    }
                }
            }

            DelegateSubscriber<T> subscriber =
                    new DelegateSubscriber<T>(handler, filter);

            subscribers.add(subscriber);
        }
    }


    /**
     * Unregisters a subscriber from notifications about publishing activities.
     * Implements subscription functionality of the Publish/Subscribe pattern.
     *
     * @param subscriber subscriber object being unregistered
     */
    public void unregister(final Subscriber<T> subscriber) {
        if (subscriber == null) {
            throw new NullPointerException("subscriber: Subscriber is null.");
        }

        synchronized (subscribersLock) {
            if (!subscribers.remove(subscriber)) {
                throw new IllegalArgumentException("subscriber: Subscriber is not registered.");
            }
        }
    }

    /**
     * Unregisters a subscriber from notifications about publishing activities.
     * Implements subscription functionality of the Publish/Subscribe pattern.
     *
     * @param handler the method that performs an action on the specified object
     *      when a publishing event occurs
     */
    public void unregister(final Action<T> handler) {
        if (handler == null) {
            throw new NullPointerException(
                    "handler: Handler is null.");
        }

        synchronized (subscribersLock) {
            for (Subscriber<T> s : subscribers) {
                if (s instanceof DelegateSubscriber) {
                    DelegateSubscriber<T> s1 = ((DelegateSubscriber<T>) s);
                    if (s1.getHandler() == handler) {
                        subscribers.remove(s);
                        return;
                    }
                }
            }

            throw new IllegalArgumentException(
                    "handler:Handler is not registered.");
        }
    }

    protected void unregisterAll() {
        synchronized (subscribersLock) {
            subscribers.clear();
        }
    }

    /**
     * Calls subscribers' processing/handling methods of to process publishing event.
     *
     * @param obj object that is used for a publishing event processing
     */
    protected void notify(final T obj) {
        synchronized (subscribersLock) {
            final int numSubs = subscribers.size();
            if (logger.isDebug()) {
                logger.debugFormat("Notifying {0} subscribers...", numSubs);
            }

            for (Subscriber<T> subscriber : subscribers) {
                if (subscriber != null) {
                    Predicate<T> filter = subscriber.getFilter();
                    if (filter == null || filter.invoke(obj)) {
                        try {
                            subscriber.handle(obj);
                        } catch (Exception e) {
                            logger.errorFormat("Subscriber {0} with filter {1} had exception {2}.",
                                    new Object[] {subscriber, filter, e});

                            onNotificationException(subscriber, e);
                        }
                    }
                }
            }

            if (logger.isDebug()) {
                logger.debugFormat("{0} subscribers notified", numSubs);
            }
        }
    }

    /**
     * Allows to handle exceptions thrown by subscribers' publishing event processing methods.
     *
     * @param subscriber exception originator
     * @param e thrown exception
     */
    protected void onNotificationException(
            final Subscriber<T> subscriber, final Exception e) {
        // default behavior, override to do more intelligent exception handling
        throw new RuntimeException("Rethrowing notification exception", e);
    }


    private static class DelegateSubscriber<T>
            implements Subscriber<T> {
        private final Action<T> handler;
        private final Predicate<T> filter;

        public DelegateSubscriber(
                final Action<T> theHandler,
                final Predicate<T> theFilter) {
            this.handler = theHandler;
            this.filter = theFilter;
        }

        public Action<T> getHandler() {
            return this.handler;
        }

        public Predicate<T> getFilter() {
            return this.filter;
        }

        public void handle(final T obj) {
            handler.handle(obj);
        }
    }
}
