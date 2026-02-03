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
package com.genesyslab.platform.applicationblocks.com;

import java.util.Collection;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


/**
 * A <tt>AsyncRequestResult</tt> as an extension of <tt>Future</tt> represents
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
 * @param <T> specific wrapping class of particular configuration objects type
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public interface AsyncRequestResult<T extends ICfgObject>
        extends Future<Collection<T>> {

    /**
     * Returns <tt>true</tt> if this task completed.
     * <p/>
     * Completion may be due to normal termination, an exception, or
     * cancellation -- in all of these cases, this method will return
     * <tt>true</tt>.
     *
     * @return <tt>true</tt> if this task completed.
     */
    boolean isDone();

    /**
     * Returns <tt>true</tt> if this task finished with some error.
     * <p/>
     * Failure may be due to exception, or cancellation -- in any of these cases,
     * this method will return <tt>true</tt>.
     *
     * @return <tt>true</tt> if this task failed.
     */
    boolean isFailed();

    boolean isCompletedSynchronously();

    /**
     * This function returns raised exception when operation is failed.
     *
     * @return asynchronous operation execution exception or null
     */
    Exception getException();

    /**
     * Attempts to cancel execution of this task. This attempt will
     * fail if the task has already completed, already been canceled,
     * or could not be canceled for some other reason. If successful,
     * and this task has not started when <tt>cancel</tt> is called,
     * this task should never run. If the task has already started,
     * then the <tt>mayInterruptIfRunning</tt> parameter determines
     * whether the thread executing this task should be interrupted in
     * an attempt to stop the task.
     * <p/>
     * <b>Note</b>: Actual request procession (network traffic for this data)
     * is not interrupted/stopped. This function cancels the request on
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
    boolean cancel(boolean mayInterruptIfRunning);

    /**
     * Returns <tt>true</tt> if this task was canceled before it completed
     * normally.
     *
     * @return <tt>true</tt> if task was canceled before it completed
     */
    boolean isCancelled();

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
    Collection<T> get() throws InterruptedException, ExecutionException;

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
    Collection<T> get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException;

    /**
     * Returns accumulated read data immediately.
     * Do Not wait for the read operation to complete.
     * This method ignores exceptions and returns partial data
     * even if operation is canceled or failed (unfinished).
     *
     * @return the computed result or null
     */
    Collection<T> partialGet();
}
