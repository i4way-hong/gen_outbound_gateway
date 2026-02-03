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

import com.genesyslab.platform.applicationblocks.commons.Action;

import com.genesyslab.platform.commons.protocol.ClientChannel;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.runtime.ChannelNotificationThreadsControl;
import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.timer.Scheduler;
import com.genesyslab.platform.commons.timer.TimerAction;
import com.genesyslab.platform.commons.timer.TimerActionTicket;
import com.genesyslab.platform.commons.timer.TimerFactory;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeoutException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;


/**
 * A <tt>AsyncRequestResult</tt> as an implementation of <tt>Future</tt> represents
 * the result of an asynchronous read operation of configuration data.
 * Methods are provided to check if the read operation is complete,
 * to wait for its completion, and to retrieve the result of
 * the operation.  The result can only be retrieved using method
 * <tt>get</tt> when the read operation has completed, blocking if
 * necessary until it is ready.  Cancellation is performed by the
 * <tt>cancel</tt> method.  Additional methods are provided to
 * determine if the task completed normally or was canceled. Once an
 * operation has completed, it cannot be canceled.
 *
 * @param <T> Java generic class of objects requested
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public final class AsyncRequestResultImpl<T extends ICfgObject>
        implements AsyncRequestResult<T> {
    private ICfgQuery<T>                  query;
    private Message                       requestMessage;
    private Class<T>                      objClass;
    private Action<Collection<T>>         dataCallback;
    private Action<AsyncRequestResult<T>> finishCallback;
    private AsyncInvoker                  callbackAsyncInvoker;
    private TimerAction                   timeoutTask = null;
    private long                          timeout;
    private TimerActionTicket             timeoutTicket = null;

    private List<Message>   incomings   = new ArrayList<Message>();
    private Collection<T>   resultSet   = null;
    private Exception       exception   = null;

    private boolean isDone          = false;
    private boolean isCanceled      = false;
    private boolean isCompletedSync = false;

    private final Object lockObject = new Object();

    private static final ILogger log = Log.getLogger(EventService.class);

    private ClientChannel protocol;

    public AsyncRequestResultImpl(
            final Class<T>                       clazz,
            final ICfgQuery<T>                   origQuery,
            final Message                        rqMessage,
            final Action<Collection<T>>          dataCallback,
            final Action<AsyncRequestResult<T>>  finishCallback,
            final AsyncInvoker                   callbackInvoker,
            final long                           timeout) {
        this.objClass       = clazz;
        this.query          = origQuery;
        this.requestMessage = rqMessage;
        this.dataCallback   = dataCallback;
        this.finishCallback = finishCallback;
        this.callbackAsyncInvoker = callbackInvoker;
        this.timeout        = timeout;

        if (callbackInvoker == null
                && (dataCallback != null || finishCallback != null)) {
            throw new NullPointerException("callbackAsyncInvoker");
        }
    }

    /* It is for wrapping of objects taken from cache without server request.
     */
    public AsyncRequestResultImpl(
            final Class<T>                       clazz,
            final ICfgQuery<T>                   origQuery,
            final Collection<T>                  objects,
            final Action<Collection<T>>          dataCallback,
            final Action<AsyncRequestResult<T>>  finishCallback,
            final AsyncInvoker                   callbackInvoker) {
        this(clazz, origQuery, (Message) null,
                dataCallback, finishCallback, callbackInvoker, 0);

        this.isCompletedSync = true;
        this.addToResultSet(objects);
        this.finishResultSet();
    }

    public AsyncRequestResultImpl<T> withProtocol(Protocol protocol) {
        if (protocol instanceof ClientChannel) {
            this.protocol = (ClientChannel) protocol;
        }
        else {
            this.protocol = null;
        }
        return this;
    }


    /**
     * Returns <tt>true</tt> if this task completed.
     * <p/>
     * Completion may be due to normal termination, an exception, or
     * cancellation -- in all of these cases, this method will return
     * <tt>true</tt>.
     *
     * @return <tt>true</tt> if this task completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns <tt>true</tt> if this task finished with some error.
     * <p/>
     * Failure may be due to exception, or cancellation -- in any of these cases,
     * this method will return <tt>true</tt>.
     *
     * @return <tt>true</tt> if this task failed.
     */
    public boolean isFailed() {
        return isDone() && (exception != null) || isCancelled();
    }

    public boolean isCompletedSynchronously() {
        return isCompletedSync;
    }

    public void setCompletedSynchronously(boolean val) {
        isCompletedSync = val;
    }
    /**
     * This function returns raised exception when operation is failed.
     *
     * @return async operation execution exception or null
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Attempts to cancel execution of this task.  This attempt will
     * fail if the task has already completed, already been canceled,
     * or could not be canceled for some other reason. If successful,
     * and this task has not started when <tt>cancel</tt> is called,
     * this task should never run.  If the task has already started,
     * then the <tt>mayInterruptIfRunning</tt> parameter determines
     * whether the thread executing this task should be interrupted in
     * an attempt to stop the task.
     *
     * Note: Actual request procession (network traffic for this data)
     * is not interrupted/stopped.  This function cancels the request on
     * ConfService level and notifies all listeners that are waiting for the result.
     * ConfService will skip further configuration server messages on this request.
     *
     * @param mayInterruptIfRunning <tt>true</tt> if the thread executing this
     *                              task should be interrupted;
     *                              otherwise, in-progress tasks are allowed to complete
     * @return <tt>false</tt> if the task could not be canceled,
     *         typically because it has already completed normally;
     *         <tt>true</tt> otherwise
     */
    public boolean cancel(final boolean mayInterruptIfRunning) {
        synchronized (lockObject) {
            if (isDone()) {
                return false;
            }
            isCanceled = true;
            setException(
                    new CancellationException("Request has been canceled")
            );
            return true;
        }
    }

    /**
     * Returns <tt>true</tt> if this task was canceled before it completed
     * normally.
     *
     * @return <tt>true</tt> if task was canceled before it completed
     */
    public boolean isCancelled() {
        return isCanceled;
    }

    /**
     * Returns accumulated read data immediately.
     * Do Not wait for the read operation to complete.
     * This method ignores exceptions and returns partial data
     * even if operation is canceled or failed (unfinished).
     *
     * @return the computed result or null
     */
    public Collection<T> partialGet() {
        Collection<T> data = null;

        synchronized (lockObject) {
            if (resultSet != null) {
                data = new ArrayList<T>();
                if (resultSet.size() != 0) {
                    data.addAll(resultSet);
                }
            }
        }

        return data;
    }

    /**
     * Waits if necessary for the read operation to complete, and then
     * returns its result.
     *
     * @return the computed result or null
     * @throws java.util.concurrent.CancellationException
     *                              if the computation was canceled
     * @throws java.util.concurrent.ExecutionException
     *                              if the computation threw an
     *                              exception
     * @throws InterruptedException if the current thread was interrupted
     *                              while waiting
     */
    public Collection<T> get()
            throws InterruptedException, ExecutionException {
        try {
            return get(0, TimeUnit.MILLISECONDS);
        } catch (TimeoutException tex) {
            throw new ExecutionException(tex);
        }
    }

    /**
     * Waits if necessary for at most the given time for the request
     * to complete, and then returns its result, if available.
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @return list containing requested configuration objects or null
     * @throws java.util.concurrent.CancellationException
     *                              if the computation was canceled
     * @throws java.util.concurrent.ExecutionException
     *                              if the computation threw an
     *                              exception
     * @throws java.util.concurrent.TimeoutException
     *                              if the wait timed out
     * @throws InterruptedException if the current thread was interrupted
     *                              while waiting
     */
    public Collection<T> get(final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {

        ChannelNotificationThreadsControl.checkIfBlockedChannelNotificationThread(protocol);

        synchronized (lockObject) {
            long tmt = timeout;
            if (unit != null) {
                tmt = unit.toMillis(timeout); // we drops "excessNanos"
            }
            if (tmt > 0) {
                long timeoutMark = System.currentTimeMillis() + tmt;
                long waitTime = tmt;
                do {
                    if (isDone()) break;
                    lockObject.wait(waitTime);
                    waitTime = timeoutMark - System.currentTimeMillis();
                } while (waitTime > 0);
            } else {
                while (!isDone()) {
                    lockObject.wait();
                }
            }
            if (!isDone()) {
                throw new TimeoutException("No response got in given timeframe");
            }
        }

        if (isFailed()) {
            if (exception != null) {
                if (exception instanceof CancellationException) {
                    throw (CancellationException) exception;
                }
                throw new ExecutionException(exception);
            }
            throw new ExecutionException(
                    new ConfigException("Configuration reading is failed")
            );
        }

        return resultSet;
    }

    Class<T> getObjectClass() {
        return objClass;
    }

    ICfgQuery<T> getQuery() {
        return query;
    }

    Message getRequestMessage() {
        return requestMessage;
    }

    void setTimeoutAction(final TimerAction action) {
        synchronized (lockObject) {
            timeoutTask = action;
            resetTimeoutTask(true);
        }
    }

    List<Message> getResponses() {
        return incomings;
    }

    @SuppressWarnings("deprecation")
    void addToResultSet(final Collection<T> value) {
        synchronized (lockObject) {
            if (isDone()) {
                throw new ConfigRuntimeException("AsyncRequestResult is already finished");
            } else {
                if (resultSet == null) {
                    resultSet = value;
                } else {
                    resultSet.addAll(value);
                }

                resetTimeoutTask(true);

                if (dataCallback != null) {
                    try {
                        callbackAsyncInvoker.invoke(
                                new AsyncNotifier<Collection<T>>(
                                        dataCallback, value));
                    }
                    finally {
                        if (callbackAsyncInvoker == protocol.getInvoker()) {
                            // release default invoker if the channel have been closed already
                            callbackAsyncInvoker.dispose();
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    void addToResultSet(final T object) {
        synchronized (lockObject) {
            if (isDone()) {
                throw new ConfigRuntimeException("AsyncRequestResult is already finished");
            } else {
                if (resultSet == null) {
                    resultSet = new ArrayList<T>();
                }
                resultSet.add(object);

                resetTimeoutTask(true);

                if (dataCallback != null) {
                    ArrayList<T> deltaList = new ArrayList<T>(1);
                    deltaList.add(object);

                    try {
                        callbackAsyncInvoker.invoke(
                                new AsyncNotifier<Collection<T>>(
                                        dataCallback, deltaList));
                    }
                    finally {
                        if (callbackAsyncInvoker == protocol.getInvoker()) {
                            // release default invoker if the channel have been closed already
                            callbackAsyncInvoker.dispose();
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    void finishResultSet() {
        synchronized (lockObject) {
            if (isDone()) {
                throw new ConfigRuntimeException(
                        "AsyncRequestResult is already finished");
            } else {
                isDone = true;

                resetTimeoutTask(false);

                if (finishCallback != null) {
                    try {
                        callbackAsyncInvoker.invoke(
                                new AsyncNotifier<AsyncRequestResult<T>>(
                                        finishCallback, this));
                    }
                    finally {
                        if (callbackAsyncInvoker == protocol.getInvoker()) {
                            // release default invoker if the channel have been closed already
                            callbackAsyncInvoker.dispose();
                        }
                    }
                }
                lockObject.notifyAll();
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void setException(final Exception except) {
        synchronized (lockObject) {
            if (isDone()) {
                throw new ConfigRuntimeException(
                        "AsyncRequestResult is already finished");
            } else {
                exception = except;
                isDone = true;

                resetTimeoutTask(false);

                if (finishCallback != null) {
                    try {
                        callbackAsyncInvoker.invoke(
                                new AsyncNotifier<AsyncRequestResult<T>>(
                                        finishCallback, this));
                    }
                    finally {
                        if (callbackAsyncInvoker == protocol.getInvoker()) {
                            // release default invoker if the channel have been closed already
                            callbackAsyncInvoker.dispose();
                        }
                    }
                }

                lockObject.notifyAll();
            }
        }
    }


    void timeout() {
        synchronized (lockObject) {
            if (!isDone()) {
                setException(new ConfigRuntimeException(
                        "Timeout waiting for server response message"));
            }
        }
    }

    private class AsyncNotifier<O>
                implements Runnable {
        private Action<O> notifAction;
        private O notifArg;

        AsyncNotifier(
                final Action<O> action, final O argument) {
            notifAction = action;
            notifArg = argument;
        }

        public void run() {
            ClientChannel protocol = AsyncRequestResultImpl.this.protocol;
            if (protocol != null) {
                ChannelNotificationThreadsControl.markChannelNotificationThread(protocol);
            }
            try {
                notifAction.handle(notifArg);
            } catch (Exception ex) {
                log.error("Exception in notification callback", ex);
            }
            finally {
                if (protocol != null) {
                    ChannelNotificationThreadsControl.unmarkChannelNotificationThread(protocol);
                }
            }
        }
    }

    private void resetTimeoutTask(final boolean scheduleNext) {
        if (timeoutTicket != null) {
            timeoutTicket.cancel();
            timeoutTicket = null;
        }
        if (scheduleNext && timeoutTask != null && timeout > 0) {
            Scheduler scheduler = getTimer();
            timeoutTicket = scheduler.schedule(timeout, timeoutTask);
        }
    }

    private Scheduler getTimer() {
        AsyncInvoker invoker = protocol.getInvoker();
        if (invoker instanceof Scheduler) {
            return (Scheduler)invoker;
        }
        return TimerFactory.getTimer();
    }
}
