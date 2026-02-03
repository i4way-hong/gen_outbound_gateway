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
//  Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.applicationblocks.commons.broker;

import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.Receiver;
import com.genesyslab.platform.commons.protocol.RequestContext;
import com.genesyslab.platform.commons.protocol.RequestReceiver;
import com.genesyslab.platform.commons.threading.SingleThreadInvoker;


/**
 * <code>RequestBrokerService</code> class is designed to work with request messages.
 *
 * <example>
 * <code><pre>[Java]
 *     private ExternalServiceProtocolListener requestListener = null;
 *     private RequestBrokerService requestBroker = null;
 *
 *     public void initialize() {
 *         this.requestListener = new ExternalServiceProtocolListener(
 *                 new Endpoint("MyESPServer", "localhost", 9999));
 *         this.requestBroker = new RequestBrokerService(requestListener);
 *         this.requestBroker.activate();
 *         this.requestBroker.register(new RequestHandle(), null);
 *         this.requestListener.open();
 *     }
 *
 *     public void deinitialize() {
 *         if (this.requestBroker != null) {
 *             this.requestBroker.dispose();
 *             this.requestBroker = null;
 *         }
 *         if (this.requestListener != null) {
 *             if (this.requestListener.getState() == ChannelState.Opened) {
 *                 this.requestListener.close();
 *             }
 *             this.requestListener = null;
 *         }
 *     }
 *
 *     class RequestHandle implements Action&lt;RequestContext&gt; {
 *         public void handle(final RequestContext context) {
 *             Message request = context.getRequestMessage();
 *             // ...
 *             Message response = EventSomeResponse.create(
 *                         "Operation result");
 *             context.respond(response);
 *         }
 *     }
 * </pre></code>
 * </example>
 *
 * This class has a specific behavior - it performs synchronous wait in AsyncInvoker for incoming events,
 * so, it requires dedicated invoker to be permanently busy with reading task.
 * The alternative for broker service functionality without fully occupied invoker is to use
 * RequestReceivingBrokerService which is a receiver by itself and can be used as "external receiver"
 * in protocol connections to handle messages directly without intermediate queues.
 *
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public class RequestBrokerService
        extends MessageBrokerService<RequestContext> {

    private RequestReceiver receiver;


    /**
     * Creates an instance of <code>RequestBrokerService</code> class.
     * When using this constructor the following additional steps
     * should be done to set the service in working state:
     *   setting the invoker - property <code>Invoker</code>;
     *   setting the receiver - property <code>Receiver</code>;
     *   calling <code>Activation</code> method.
     */
    public RequestBrokerService() {
    }

    /**
     * Creates an instance of <code>RequestBrokerService</code> class.
     *
     * @param theReceiver Initializes request receiver
     */
    public RequestBrokerService(final RequestReceiver theReceiver) {
        super(new SingleThreadInvoker("requestBroker.defaultInvoker"));

        if (theReceiver == null) {
            throw new NullPointerException("theReceiver can't be null");
        }
        this.receiver = theReceiver;
    }


    /**
     * Gets request receiver.
     *
     * @return receiver
     */
    public RequestReceiver getReceiver() {
        return this.receiver;
    }

    /**
     * Sets request receiver.
     *
     * @param value request receiver
     */
    public void setReceiver(final RequestReceiver value) {
        if (value == null) {
            throw new NullPointerException("receiver can't be null.");
        }

        synchronized (lifecycleLock) {
            if (status != LifecycleStage.Initialized) {
                throw new IllegalStateException(
                        "Can't change receiver when broker is active or disposed.");
            }

            this.receiver = value;
        }
    }

    /**
     * Receives request messages.
     *
     * @return request context of received message
     * @throws InterruptedException
     */
    protected RequestContext receive() throws InterruptedException {
        return receiver.receiveRequest();
    }

    /**
     * Gets generic receiver.
     * Generic receiver is able to receive both: event and request messages.
     *
     * @return generic receiver
     */
    protected Receiver getGenericReceiver() {
        return this.getReceiver();
    }
}
