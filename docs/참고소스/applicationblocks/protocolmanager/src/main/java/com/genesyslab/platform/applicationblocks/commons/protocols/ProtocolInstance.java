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
package com.genesyslab.platform.applicationblocks.commons.protocols;

import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyState;
import com.genesyslab.platform.commons.protocol.*;

import java.util.EventObject;


final class ProtocolInstance {

    private Protocol protocol;
    private WarmStandbyService warmStandby;

    private final Object protocolLock = new Object();
    private final Object lifecycleLock = new Object();
    private final Object stateLock = new Object();
    private PIChannelListener channelListener = new PIChannelListener();
    private boolean isDisposed = false;

    public ProtocolInstance(final Protocol protocol) {
        this(protocol, null);
    }

    public ProtocolInstance(final Protocol protocol, final WarmStandbyService warmStandby) {
        this.protocol = protocol;
        this.warmStandby = warmStandby;
        protocol.addChannelListener(channelListener);
    }


    public Protocol getProtocol() {
        return protocol;
    }

    public Object getProtocolLock() {
        return protocolLock;
    }

    public WarmStandbyService getWarmStandby() {
        return warmStandby;
    }

    public void setWarmStandby(final WarmStandbyService value) {
        warmStandby = value;
    }

    public boolean waitForStableChannelState() {
        boolean ret = false;
        if (!isDisposed) {
            synchronized (stateLock) {
                try {
                    long timeout = protocol.getTimeout();
                    long timeoutMark = System.currentTimeMillis() + timeout;
                    long waitTime = timeout;
                    do {
                        ChannelState state = protocol.getState();
                        if ((state == ChannelState.Opened)
                                || (state == ChannelState.Closed)) {
                            ret = true;
                            break;
                        }
                        if (waitTime <= 0) {
                            break;
                        }
                        stateLock.wait(waitTime);
                        waitTime = timeoutMark - System.currentTimeMillis();
                    } while (true);
                } catch (InterruptedException e) { /* 'false' is going to be returned */ }
            }
        }
        return ret;
    }

    public void dispose() {
        synchronized (lifecycleLock) {
            if (isDisposed) {
                return;
            }
            protocol.removeChannelListener(channelListener);
            protocol.resetReceiver();

            if (warmStandby != null) {
                if (warmStandby.getState() != WarmStandbyState.OFF) {
                    warmStandby.stop();
                }
                warmStandby = null;
            }
            // ? protocol.dispose();
            isDisposed = true;
        }
    }


    private class PIChannelListener implements ChannelListener {

        public void onChannelOpened(final EventObject event) {
            synchronized (stateLock) {
                stateLock.notifyAll();
            }
        }

        public void onChannelClosed(final ChannelClosedEvent event) {
            synchronized (ProtocolInstance.this.lifecycleLock) {
                if (ProtocolInstance.this.isDisposed) {
                    return;
                }
            }
            synchronized (stateLock) {
                stateLock.notifyAll();
            }
        }

        public void onChannelError(final ChannelErrorEvent event) {
        }
    }
}
