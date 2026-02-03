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

import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;

class WSCloseFuture extends WSFuture<Void, ChannelClosedEvent> {

    private WSCloseFuture(WarmStandbyImpl warmStandby, boolean needNotification) {
        super(warmStandby, needNotification);
    }

    @Override
    protected Throwable wrapException(Throwable ex) {
        return ex;
    }

    protected ExecutionException executionException(Throwable ex) {
        return new ExecutionException(warmStandbyImpl + " close operation failed", ex);
    }

    protected CancellationException cancelationException() {
        return new CancellationException(warmStandbyImpl + " close operation has been canceled");
    }
    
    

    @Override
    protected void handleSuccess(Void reuslt) {
        synchronized (guard) {
            warmStandbyImpl.handleCloseSuccess(this);
        }
    }

    public static WSCloseFuture createSuccess(WarmStandbyImpl warmStandby) {
        WSCloseFuture f = new WSCloseFuture(warmStandby, false);
        f.success(null);
        return f;
    }
    
    public static WSCloseFuture create(WarmStandbyImpl warmStandby) {
        WSCloseFuture f = new WSCloseFuture(warmStandby, true);
        return f;
    }
    
}
