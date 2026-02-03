/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.protocol.ConnectionOperations;
import com.genesyslab.platform.commons.protocol.runtime.ChannelNotificationThreadsControl;
import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.threading.CompletionHandler;
import com.genesyslab.platform.commons.timer.Scheduler;
import com.genesyslab.platform.commons.timer.TimerAction;
import com.genesyslab.platform.commons.timer.TimerActionTicket;
import com.genesyslab.platform.commons.timer.TimerFactory;


abstract class WSFuture<ResultType, FutureType> implements Future<ResultType> {
    private static final ILogger log = Log.getLogger(WSFuture.class);

    final static SecureRandom rnd = new SecureRandom();
    final int stateCounter;
    
    protected final WarmStandbyImpl warmStandbyImpl;
    protected final Object guard;
    protected final boolean needNotification;
    protected final ConnectionOperations connectionOperations;

    private final AtomicReference<Thread> channelInvokerThreadRef;
//    private volatile boolean readyToNotify;
    private volatile boolean canceled;
    private boolean result;
    private ResultType resultValue;
    private Throwable resultError;
    private TimerActionTicket ticket;
    private Runnable ticketTask;
    private long ticketDelay;
    private long ticketTime;
    private WSDelayType ticketDelayType;
    private Future<FutureType> future;
    private final ArrayList<CompletionHandlerEntry> completionHandlers = new ArrayList<CompletionHandlerEntry>();
//    private final ArrayList<CompletionHandlerEntry> completionHandlersLate = new ArrayList<CompletionHandlerEntry>();

    public WSFuture(WarmStandbyImpl warmStandby, boolean needNotification) {
        super();
        this.warmStandbyImpl = warmStandby;
        this.needNotification = needNotification;
        this.connectionOperations = warmStandby.connectionOperations;
        this.guard = warmStandby.getChannel();
        this.channelInvokerThreadRef = warmStandby.channelInvokerThreadRef;
        this.stateCounter = warmStandby.stateCounter;
    }

    protected abstract Throwable wrapException(Throwable ex);
    
    protected abstract ExecutionException executionException(Throwable ex);

    protected abstract CancellationException cancelationException();

    WSConfig config() {
        return warmStandbyImpl.getConfig();
    }

    void handleInternalFutureException(Throwable ex) throws InterruptedException {
    }
    
    protected void handleSuccess(ResultType reuslt) {
    }

    protected void handleFailed(Throwable cause) {
    }

    protected void handleCanceled() {
    }
    
    private void endProgress() {
        endProgress(false);
    }

    private void endProgress(boolean mayInterruptIfRunning) {
        if (ticket != null) {
            ticket.cancel();
            ticket = null;
            ticketTask = null;
            ticketDelay = -1;
            ticketDelayType = null;
            ticketTime = 0;
        }
        if (future != null) {
//            future.cancel(false);
            future = null;
        }
    }

    void schedule(final Runnable task, long delay, WSDelayType delayType) {
        synchronized (guard) {
            if (result) {
                return;
            }

            if (this.ticket != null) {
                this.ticket.cancel();
            }

            this.ticketTask = task;
            this.ticketDelay = delay;
            this.ticketDelayType = delayType;
            this.ticketTime = System.currentTimeMillis();
            this.ticket = createTimerAction(delay);
        }
    }

    private TimerActionTicket createTimerAction(long delay) {
        return getTimer().schedule(delay, new TimerAction() {
            @Override
            @SuppressWarnings("deprecation")
            public void onTimer() {
                synchronized (guard) {
                    final Runnable task = ticketTask;
                    if (task != null) {
                        ticket = null;
                        ticketTask = null;
                        ticketDelay = -1;
                        ticketDelayType = null;
                        ticketTime = 0;
                        warmStandbyImpl.getChannel().getInvoker().invoke(task);
                    }
                }
            }

            @Override
            public String toString() {
                return "WarmStandby." + ticketTask + " for " + warmStandbyImpl.getChannel();
            }
        });
    }

    private Scheduler getTimer() {
        @SuppressWarnings("deprecation")
        final AsyncInvoker invoker = warmStandbyImpl.getChannel().getInvoker();
        if (invoker instanceof Scheduler) {
            return (Scheduler)invoker;
        }
        return TimerFactory.getTimer();
    }

    public void reschedule(WSDelayType delayType) {
        synchronized (guard) {
            if (ticketDelayType != delayType) {
                return;
            }
            int delay;
            switch (delayType) {
                case ReconnectionDelay:
                    delay = config().getReconnectionRandomDelayRange();
                    if (ticketDelay < delay) {
                        return;
                    }
                    break;
    
                case BackupDelay:
                    delay = config().getBackupDelay();
                    break;

                case RetryDelay:
                    delay = config().getRetryDelay(warmStandbyImpl.retryNo);
                    break;
                    
                default:
                    return;
            }

            long time = System.currentTimeMillis();
            long elapsedTime = time - ticketTime;
            long remainedTime = delay - elapsedTime;
            if (remainedTime > 0) {
                ticket.cancel();
                ticketDelay = delay;
                ticket = createTimerAction(remainedTime);
            } else {
                final Runnable task = ticketTask;
                endProgress();
                @SuppressWarnings("deprecation")
                final AsyncInvoker invoker = warmStandbyImpl.getChannel().getInvoker();
                invoker.invoke(task);
            }
        }
    }
    
    
    Future<FutureType> getFuture() {
        return future;
    }

    void setFuture(Future<FutureType> future) {
        synchronized (guard) {
            if (result) {
                return;
            }
            endProgress();
            this.future = future;
            guard.notifyAll();
        }
    }

    public boolean isInProgress() {
        return ticket != null || future != null;
    }

    void success(ResultType value) {
        synchronized (guard) {
            if (result) {
                return;
            }
            this.resultValue = value;
            this.result = true;
            endProgress();
            if (needNotification) {
                handleSuccess(value);
            }
            guard.notifyAll();
        }
    }

    public void hintFailed(Throwable cause) {
        synchronized (guard) {
            if (result) {
                return;
            }
            this.resultError = wrapException(cause);
            endProgress();
        }
    }    
    
    void failed(Throwable cause) {
        synchronized (guard) {
            if (result) {
                return;
            }
            this.resultError = wrapException(cause);
            this.result = true;
            endProgress();
            if (needNotification) {
                handleFailed(cause);
            }
            guard.notifyAll();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (guard) {
            if (result) {
                return false;
            }
            if (canceled ) {
                return false;
            }
            canceled = true;
            endProgress();
            handleCanceled();
            guard.notifyAll();
            return true;
        }
    }


    boolean isChannelInvokerThread() {
        return Thread.currentThread() == channelInvokerThreadRef.get();
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public boolean isDone() {
        return result;
    }

    protected ResultType toResult(FutureType internalFutureResult) {
        return null;
    }

    protected void handleInternalFutureResult(ResultType event) {
        success(event);
    }

    @Override
    public ResultType get() throws InterruptedException, ExecutionException {

        if (!isDone()) {
            if (isChannelInvokerThread()) {
                synchronized (guard) {
                    while (resultError == null && !result && future == null) {
                        guard.wait();
                    }
                }
                while (!result) {
                    if (resultError != null) {
                        throw new ExecutionException(resultError);
                    }
                    try {
                        final Future<FutureType> f = future;
                        if (f != null) {
                            ResultType event = toResult(future.get());
                            handleInternalFutureResult( event);
                        }
                    } catch (CancellationException ex) {
                        cancel(false);
                    } catch (ExecutionException ex) {
                        handleInternalFutureException(ex.getCause());
                    }
                }
            }
        }

        if (!isDone()) {
            synchronized (guard) {
                while (!isDone()) {
                    guard.wait();
                }
            }
        }

        if (canceled) {
            throw cancelationException();
        }

        if (resultError != null) {
            throw executionException(resultError);
        }

        return resultValue;
    }

    @Override
    public ResultType get(final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {

        if (unit == null) {
            throw new IllegalArgumentException(warmStandbyImpl + " null timeUnit", null);
        }
        if (timeout < 0) {
            throw new IllegalArgumentException(warmStandbyImpl + " negative timeout", null);
        }
        final long timeStart = System.currentTimeMillis();
        long t = unit.toMillis(timeout);

        if (!isDone()) {
            if (isChannelInvokerThread()) {
                synchronized (guard) {
                    while (!result && future == null) {
                        long remained = t - (System.currentTimeMillis() - timeStart);
                        if (remained <= 0) {
                            if (!result) {
                                throw new TimeoutException();
                            }
                            break;
                        }
                        guard.wait(remained);
                    }
                }
                while (!result) {
                    try {
                        long remained = t - (System.currentTimeMillis() - timeStart);
                        if (remained >= 0) {
                            Future<FutureType> f = future;
                            if (f != null) {
                                ResultType event = toResult(f.get(remained, TimeUnit.MILLISECONDS));
                                handleInternalFutureResult(event);
                            }
                        } else {
                            if (!result) {
                                throw new TimeoutException();
                            }
                        }
                    } catch (TimeoutException ex) {
                        throw new TimeoutException();
                    } catch (CancellationException ex) {
                        cancel(false);
                    } catch (ExecutionException ex) {
                        handleInternalFutureException(ex.getCause());
                    }
                }
            }
        }

        if (!isDone()) {
            synchronized (guard) {
                while (!isDone()) {
                    guard.wait();
                }
            }
        }

        if (canceled) {
            throw cancelationException();
        }

        if (resultError != null) {
            throw executionException(resultError);
        }

        return resultValue;
    }

    private static class CompletionHandlerEntry {
        @SuppressWarnings("rawtypes")
        private final CompletionHandler handler;
        private final Object attachment;

        @SuppressWarnings("rawtypes")
        public CompletionHandlerEntry(final CompletionHandler handler, final Object attachment) {
            super();
            this.handler = handler;
            this.attachment = attachment;
        }
    }

    public <A> void addCompletionHandler(final CompletionHandler<ResultType, A> handler, final A attachment) {
        if (handler == null) {
            return;
        }
        synchronized (guard) {
            if (result) {
                @SuppressWarnings("deprecation")
                final AsyncInvoker invoker = warmStandbyImpl.getChannel().getInvoker();
                invoker.invoke(new Runnable() {
                    @Override
                    public void run() {
                        ChannelNotificationThreadsControl.markChannelNotificationThread(warmStandbyImpl.getChannel());
                        try {
                            if (resultValue != null) {
                                handler.completed(resultValue, attachment);
                            } else {
                                handler.failed(resultError, attachment);
                            }
                        } catch (final Throwable ex) {
                            if (log.isError()) {
                                log.error(warmStandbyImpl + " exception in handler " + handler, ex);
                            }
                        } finally {
                            ChannelNotificationThreadsControl.unmarkChannelNotificationThread(warmStandbyImpl.getChannel());
                        }
                    }
                });
            } else {
                completionHandlers.add(new CompletionHandlerEntry(handler, attachment));
            }
        }
    }


    @SuppressWarnings("unchecked")
    public void notifySuccess() {
        for (final CompletionHandlerEntry entry : completionHandlers) {
            @SuppressWarnings("rawtypes")
            final CompletionHandler h = entry.handler;
            try {
                h.completed(resultValue, entry.attachment);
            } catch (final Throwable ex) {
                if (log.isError()) {
                    log.error(warmStandbyImpl + " exception in handler " + h, ex);
                }
            }
        }
        completionHandlers.clear();
        synchronized (guard) {
            guard.notifyAll();
        }
    }

    @SuppressWarnings("unchecked")
    public void notifyFailed() {
        for (final CompletionHandlerEntry entry : completionHandlers) {
            @SuppressWarnings("rawtypes")
            final CompletionHandler h = entry.handler;
            try {
                h.failed(resultError, entry.attachment);
            } catch (final Throwable ex) {
                if (log.isError()) {
                    log.error(warmStandbyImpl + " exception in handler " + h, ex);
                }
            }
        }
        completionHandlers.clear();
        synchronized (guard) {
            guard.notifyAll();
        }
    }

    public boolean isActual() {
        return stateCounter == warmStandbyImpl.stateCounter;
    }
}
