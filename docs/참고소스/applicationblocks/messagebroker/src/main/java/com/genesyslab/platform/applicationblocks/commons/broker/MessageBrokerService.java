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
package com.genesyslab.platform.applicationblocks.commons.broker;

import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.Receiver;
import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.threading.SingleThreadInvoker;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;


/**
 * <code>MessageBrokerService&lt;T&gt;</code> class implements the Publish/Subscribe pattern.
 * This class has a specific behavior - it performs synchronous wait in AsyncInvoker for incoming events,
 * so, it requires dedicated invoker to be permanently busy with reading task.
 * The alternative for broker service functionality without fully occupied invoker is to use
 * EventReceivingBrokerService and RequestReceivingBrokerService which are
 * receivers by themselves and can be used as "external receivers" in protocol connections
 * to handle messages directly without intermediate queues.
 *
 * @see EventReceivingBrokerService
 * @see RequestReceivingBrokerService
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public abstract class MessageBrokerService<T>
        extends BrokerServiceBase<T> {

    private volatile boolean isActive;

    private final Object invokerLock = new Object();
    private final Semaphore threadReady = new Semaphore(1);
    private AsyncInvoker invoker = null;
    private boolean disposeInvoker = false;

    private Runnable receiverHandler = new HandleMessage();

    protected enum LifecycleStage {
        Initialized,
        Activated,
        Disposed
    }

    protected LifecycleStage status = LifecycleStage.Initialized;
    protected final Object lifecycleLock = new Object();


    /**
     * Creates an instance of <code>MessageBrokerService</code> class.
     */
    protected MessageBrokerService() {
    }

    /**
     * Creates an instance of <code>MessageBrokerService</code> class.
     *
     * @param theInvoker Initializes asynchronous invoker
     */
    protected MessageBrokerService(final AsyncInvoker theInvoker) {
        this.invoker = theInvoker;
        this.disposeInvoker = true;
    }

    /**
     * Gets asynchronous invoker.
     *
     * @return asynchronous invoker
     */
    public AsyncInvoker getInvoker() {
        synchronized (invokerLock) {
            if (invoker == null) {
                disposeInvoker = true;
                invoker = new SingleThreadInvoker("messageBroker.defaultInvoker");
            }
            return invoker;
        }
    }

    /**
     * Sets asynchronous invoker.
     *
     * @param theInvoker asynchronous invoker
     */
    public void setInvoker(final AsyncInvoker theInvoker) {
        if (theInvoker == null) {
            throw new NullPointerException("invoker: Value can't be null.");
        }

        synchronized (lifecycleLock) {
            if (this.status != LifecycleStage.Initialized) {
                throw new IllegalStateException("Can't change Invoker "
                        + "when broker is active or disposed.");
            }

            synchronized (invokerLock) {
                disposeInvoker();
                this.invoker = theInvoker;
            }
        }

    }

    /**
     * Checks if the service is active.
     *
     * @return true if the service is active
     */
    public boolean isActive() {
        synchronized (lifecycleLock) {
            return this.status == LifecycleStage.Activated;
        }
    }

    /**
     * Activates the service.
     */
    public void activate() {
        synchronized (lifecycleLock) {
            if (status == LifecycleStage.Activated) {
                return;
            }

            if (status == LifecycleStage.Disposed) {
                throw new IllegalStateException("Can't activate a disposed object.");
            }
            if (getGenericReceiver() == null) {
                throw new IllegalStateException("Reciver is null");
            }

            // copy-on-write allows users to add/remove subscribers in event handling code
            // while not efficient to add/remove large amount of them
            // so we use it only while active and switch
            // to much more effective linked list otherwise
            switchSubscribersList(new CopyOnWriteArrayList<Subscriber<T>>());
            this.isActive = true;
            threadReady.tryAcquire(); // to be able to wait later
            getInvoker().invoke(this.receiverHandler);

            try {
                threadReady.acquire();
                this.status = LifecycleStage.Activated;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Deactivates the service.
     */
    public void deactivate() {
        synchronized (lifecycleLock) {
            if (this.status == LifecycleStage.Initialized) {
                return;
            }

            if (this.status == LifecycleStage.Disposed) {
                throw new IllegalStateException("Can't deactivate a disposed object.");
            }

            // see comments for switchSubscribersList() in activate()
            switchSubscribersList(new LinkedList<Subscriber<T>>());
            this.isActive = false;
            Receiver genericReceiver = getGenericReceiver();
            if (genericReceiver != null) {
                genericReceiver.releaseReceivers();
            }

            try {
                threadReady.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            this.status = LifecycleStage.Initialized;
        }

    }

    /**
     * Disposes of the service.
     */
    public void dispose() {
        synchronized (lifecycleLock) {
            if (this.status != LifecycleStage.Disposed) {
                if (isActive()) {
                    deactivate();
                }

                disposeInvoker();
                this.status = LifecycleStage.Disposed;
            }

            unregisterAll();
        }

    }

    protected abstract T receive() throws InterruptedException;

    protected abstract Receiver getGenericReceiver();

    private void disposeInvoker() {
        if (disposeInvoker) {
            invoker.dispose();
            disposeInvoker = false;
        }

        invoker = null;
    }


    class HandleMessage implements Runnable {
        public void run() {
            threadReady.release();
            try {
                while (isActive) {
                    T message;
                    try {
                        message = receive();
                        if (message != null) {
                            publish(message);
                        }
                    } catch (InterruptedException e) {
                        // can't do anything about it
                    }
                }
            } finally {
                threadReady.release();
            }
        }
    }
}
