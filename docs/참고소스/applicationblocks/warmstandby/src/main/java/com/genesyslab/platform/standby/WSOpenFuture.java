/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.protocol.ChannelOpenedEvent;
import com.genesyslab.platform.standby.exceptions.WSCanceledException;
import com.genesyslab.platform.standby.exceptions.WSException;


class WSOpenFuture extends WSFuture<ChannelOpenedEvent, ChannelOpenedEvent> {

    private ILogger log = Log.getLogger(WSOpenFuture.class);
    private WSCloseFuture closeFuture;

    
    private WSOpenFuture(WarmStandbyImpl warmStandby, boolean needNotification) {
        super(warmStandby, needNotification);
        closeFuture = WSCloseFuture.create(warmStandby);
    }
    

    public WSCloseFuture getCloseFuture() {
        return closeFuture;
    }

    @Override
    protected Throwable wrapException(Throwable ex) {
        return ex instanceof WSException ? ex : new WSCanceledException(
                warmStandbyImpl + " open operation has been canceled", ex);
    }

    protected ExecutionException executionException(Throwable ex) {
        return new ExecutionException(warmStandbyImpl + " open operation has been canceled", ex);
    }

    protected CancellationException cancelationException() {
        return new CancellationException(warmStandbyImpl + " open operation has been canceled");
    }
    
    

//    @Override
//    protected ChannelOpenedEvent toResult(ChannelOpenedEvent futureResult) {
//        return futureResult;
//    }
//    
//    @Override
//    void handleInternalFutureException(Throwable ex) throws InterruptedException {
//        super.handleInternalFutureException(ex);
//        synchronized (guard) {
////            warmStandbyImpl.skipNextChannelEvent();
//            ChannelClosedEvent event;
//            try {
//                event = warmStandbyImpl.getChannel().closeAsync().get();
//                warmStandbyImpl.proccessClosedEvent(event);
//            } catch (ExecutionException e) {
//                if (log.isError()) {
//                    log.error(warmStandbyImpl + " unexpected exception.", e);
//                }
//                failed(e);
//            }
//        }
//    }
    
    @Override
    protected void handleCanceled() {
        super.handleCanceled();
        if (log.isDebug()) {
            log.debug(warmStandbyImpl + " open canceling.");
        }
        synchronized (guard) {
            warmStandbyImpl.connectionOperations.closeAsync();
        }
    }

    @Override
    protected void handleSuccess(ChannelOpenedEvent reuslt) {
        super.handleSuccess(reuslt);
        synchronized (guard) {
            warmStandbyImpl.handleOpenSuccess(this, reuslt);
        }
    }
    
    
    @Override
    protected void handleFailed(Throwable cause) {
        super.handleFailed(cause);
        synchronized (guard) {
            warmStandbyImpl.handleOpenFailed(this);
        }
    }

    public static WSOpenFuture createFailed(WarmStandbyImpl warmStandbyImpl) {
        WSOpenFuture f = new WSOpenFuture(warmStandbyImpl, false);
        f.closeFuture = WSCloseFuture.createSuccess(warmStandbyImpl);
        f.failed(null);
        return f;
    }    

    public static WSOpenFuture create(WarmStandbyImpl warmStandbyImpl) {
        WSOpenFuture f = new WSOpenFuture(warmStandbyImpl, true);
        f.closeFuture = WSCloseFuture.create(warmStandbyImpl);
        return f;
    }

    @Override
    public ChannelOpenedEvent get() throws InterruptedException,
            ExecutionException {
        if (isChannelInvokerThread()) {
            throw new RuntimeException(warmStandbyImpl + " methods open() and openAsync().get() can't be called inside the handler", null);
        }
        return super.get();
    }

    @Override
    public ChannelOpenedEvent get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (isChannelInvokerThread()) {
            throw new RuntimeException(warmStandbyImpl + " methods open() and openAsync().get() can't be called inside thehandler", null);
        }
        return super.get(timeout, unit);
    }
}
