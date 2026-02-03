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

import com.genesyslab.platform.commons.protocol.AsyncInvokerSupport;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.threading.InvokerInfo;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;


/**
 * <code>AsyncBrokerServiceBase</code> class extends base implementation
 * of the Publish/Subscribe pattern with asynchronous publishing.
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public abstract class AsyncBrokerServiceBase<T>
        extends BrokerServiceBase<T>
        implements AsyncInvokerSupport, InvokerInfo {

    private final ILogger logger = Log.getLogger(getClass());

    private AsyncInvoker invoker = null;
    private final Object invokerLock = new Object();


    protected AsyncBrokerServiceBase() {
    }

    protected AsyncBrokerServiceBase(final AsyncInvoker invoker) {
        setInvoker(invoker);
    }


    public void setInvoker(final AsyncInvoker invoker) {
        synchronized (invokerLock) {
            this.invoker = invoker;
        }
    }

    @Override
    public int getQueueMaxSize() {
        synchronized (invokerLock) {
            if (invoker instanceof InvokerInfo) {
                return ((InvokerInfo) invoker).getQueueMaxSize();
            } else {
                throw new IllegalStateException("No AsyncInvoker set or it does not implement InvokerInfo interface");
            }
        }
    }

    @Override
    public int getQueueSize() {
        synchronized (invokerLock) {
            if (invoker instanceof InvokerInfo) {
                return ((InvokerInfo) invoker).getQueueSize();
            } else {
                throw new IllegalStateException("No AsyncInvoker set or it does not implement InvokerInfo interface");
            }
        }
    }

    @Override
    public long getInvokesCount() {
        synchronized (invokerLock) {
            if (invoker instanceof InvokerInfo) {
                return ((InvokerInfo) invoker).getInvokesCount();
            } else {
                throw new IllegalStateException("No AsyncInvoker set or it does not implement InvokerInfo interface");
            }
        }
    }

    @Override
    public long getInvokesRejected() {
        synchronized (invokerLock) {
            if (invoker instanceof InvokerInfo) {
                return ((InvokerInfo) invoker).getInvokesRejected();
            } else {
                throw new IllegalStateException("No AsyncInvoker set or it does not implement InvokerInfo interface");
            }
        }
    }

    /**
     * Enqueues event for publishing by the invoker.
     *
     * @param obj Object that is used for event processing
     * @throws IllegalStateException if no AsyncInvoker initialized
     * @see #setInvoker(AsyncInvoker)
     */
    @Override
    public void publish(final T obj) {
        synchronized (invokerLock) {
            if (invoker == null) {
                throw new IllegalStateException(
                        "No AsyncInvoker set for event publishing");
            }
            if (logger.isDebug()) {
                logger.debugFormat("Enqueue publishing for {0}", obj);
            }
            invoker.invoke(new AsyncPublishTask(obj));
        }
    }


    private class AsyncPublishTask
            implements Runnable {

        private final T object;

        AsyncPublishTask(final T obj) {
            object = obj;
        }

        public void run() {
            AsyncBrokerServiceBase.super.publish(object);
        }
    }
}
