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
import com.genesyslab.platform.commons.protocol.RequestContext;
import com.genesyslab.platform.commons.protocol.RequestReceiverSupport;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.threading.AsyncInvoker;

import java.util.EventObject;


/**
 * <code>RequestReceivingBrokerService</code> class is designed to work with request messages on server side.
 *
 * <example>
 * <code><pre>[Java]
 *     final String protocolName = "CustomRouter";
 *     UrsCustomProtocolListener ursServer;
 *     RequestReceivingBrokerService requestBroker;
 *
 *     public void initialize() {
 *         Endpoint endpoint = new Endpoint(
 *                 protocolName,
 *                 "localhost", 9999);
 *         ursServer = new UrsCustomProtocolListener(endpoint);
 *
 *         requestBroker = new RequestReceivingBrokerService(
 *                             new SingleThreadInvoker("RequestReceivingBrokerService-1"));
 *         requestBroker.register(new MyHandler(), null);
 *         ursServer.setReceiver(requestBroker);
 *         ursServer.open();
 *     }
 *
 *     class MyHandler implements Action&lt;RequestContext&gt; {
 *         public void handle(final RequestContext context) {
 *             Message request = context.getRequestMessage();
 *             // ...
 *             Message response = EventSomeResponse.create(
 *                         "Operation result");
 *             // ...
 *             context.respond(response);
 *         }
 *     }
 * </pre></code>
 * </example>
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public class RequestReceivingBrokerService
        extends AsyncBrokerServiceBase<RequestContext>
        implements RequestReceiverSupport {

    /**
     * Creates an instance of <code>RequestReceivingBrokerService</code> class.
     *
     * @see #setInvoker(AsyncInvoker)
     */
    public RequestReceivingBrokerService() {
    }

    /**
     * Creates an instance of <code>RequestReceivingBrokerService</code> class.
     */
    public RequestReceivingBrokerService(final AsyncInvoker invoker) {
        super(invoker);
    }


    public void processRequest(final RequestContext request) {
        publish(request);
    }


    /**
     * It is not supposed to receive requests directly.
     * Messages are delivered by subscriptions.
     *
     * @throws UnsupportedOperationException
     * @see #register(Subscriber)
     * @see #register(Action, Predicate)
     */
    public RequestContext receiveRequest()
                throws InterruptedException, IllegalStateException {
        throw new UnsupportedOperationException(
                "BrokerService delivers messages by subscription");
    }

    /**
     * It is not supposed to receive requests directly.
     * Messages are delivered by subscriptions.
     *
     * @throws UnsupportedOperationException
     * @see #register(Subscriber)
     * @see #register(Action, Predicate)
     */
    public RequestContext receiveRequest(final long timeout)
            throws InterruptedException, IllegalStateException {
        throw new UnsupportedOperationException(
                "BrokerService delivers messages by subscription");
    }


    public void clearInput() {
        // We have no own messages queue to clear, so - do nothing
    }

    public int getInputSize() {
        // We have no own queue - all messages are passed through
        return 0;
    }

    public void releaseReceivers() {
    }

    public void setInputSize(final int arg0) {
        // We have no own queue
    }

    public void onChannelClosed(final ChannelClosedEvent event) {
    }

    public void onChannelError(final ChannelErrorEvent event) {
    }

    public void onChannelOpened(final EventObject event) {
    }
}
