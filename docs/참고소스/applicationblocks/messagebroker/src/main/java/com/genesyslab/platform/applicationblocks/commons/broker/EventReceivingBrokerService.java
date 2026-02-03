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

import com.genesyslab.platform.commons.protocol.DuplexChannel;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.MessageReceiverSupport;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.threading.AsyncInvoker;

import java.util.EventObject;


/**
 * <code>EventReceivingBrokerService</code> class is designed to work with event/response messages.
 *
 * <example>
 * <code><pre>[Java]
 *     final String protocolName = "Configuration";
 *     ConfServerProtocol protocol;
 *     EventReceivingBrokerService eventBroker;
 *
 *     public void initialize() {
 *         Endpoint endpoint = new Endpoint(
 *                 protocolName,
 *                 "hostname", 9999);
 *         protocol = new ConfServerProtocol(endpoint);
 *         protocol.setClientApplicationType(CfgAppType.CFGSCE.ordinal());
 *         protocol.setClientName("clientname");
 *         protocol.setUserName("user1");
 *         protocol.setUserPassword("user1-password");
 *
 *         eventBroker = new EventReceivingBrokerService(
 *                         new SingleThreadInvoker("EventReceivingBrokerService-1"));
 *         protocol.setMessageHandler(eventBroker);
 *         eventBroker.register(
 *             new MyAction(),
 *             new MessageFilter(protocol.getProtocolId())
 *         );
 *     }
 *
 *     class MyAction implements Action&lt;Message&gt; {
 *         public void handle(final Message msg) {
 *             System.out.println("Incoming message: " + msg);
 *         }
 *     }
 * </pre></code>
 * </example>
 *
 * Broker service can be shared between several protocol connections (may be of different type):
 *
 * <example>
 * <code><pre>[Java]
 *     ...
 *     statProtocol.setMessageHandler(eventBroker);
 *     ixnProtocol.setMessageHandler(eventBroker);
 *     routingProtocol.setMessageHandler(eventBroker);
 *
 *     // register handler for the stat protocol messages:
 *     eventBroker.register(
 *         new MyStatAction(),
 *         new MessageFilter(statProtocol.getProtocolId())
 *     );
 *
 *     // register handler for all of the protocols messages:
 *     eventBroker.register(
 *         new MyAction(), null);
 *     ...
 * </pre></code>
 * </example>
 *
 * By this way <code>eventBroker</code> will handle all incoming messages from all initialized
 * protocols one by one, using its invoker.
 *
 * @see MessageHandler
 * @see com.genesyslab.platform.commons.protocol.ClientChannel#setMessageHandler(MessageHandler)
 *          ClientChannel.setMessageHandler(MessageHandler)
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public class EventReceivingBrokerService
        extends AsyncBrokerServiceBase<Message>
        implements MessageReceiverSupport, MessageHandler {

    /**
     * Creates an instance of <code>EventReceivingBrokerService</code> class.
     *
     * @see #setInvoker(AsyncInvoker)
     */
    public EventReceivingBrokerService() {
    }

    /**
     * Creates an instance of <code>EventReceivingBrokerService</code> class.
     */
    public EventReceivingBrokerService(final AsyncInvoker invoker) {
        super(invoker);
    }


    /**
     * Enqueues message for publishing by the invoker.
     *
     * @param message message for publishing
     * @see DuplexChannel#setMessageHandler(MessageHandler)
     * @see #setInvoker(AsyncInvoker)
     */
    public void onMessage(final Message message) {
        publish(message);
    }

    /**
     * @deprecated this interface is going to be removed from this class
     */
    @Deprecated
    public void processMessage(final Message message) {
        publish(message);
    }

    /**
     * It is not supposed to receive messages directly.
     * Messages are delivered by subscriptions.
     *
     * @throws UnsupportedOperationException
     * @see #register(Subscriber)
     * @see #register(Action, Predicate)
     * @deprecated this interface is going to be removed from this class
     */
    @Deprecated
    public Message receive() throws InterruptedException {
        throw new UnsupportedOperationException(
                "BrokerService delivers messages by subscription");
    }

    /**
     * It is not supposed to receive messages directly.
     * Messages are delivered by subscriptions.
     *
     * @throws UnsupportedOperationException
     * @see #register(Subscriber)
     * @see #register(Action, Predicate)
     * @deprecated this interface is going to be removed from this class
     */
    @Deprecated
    public Message receive(final long arg0)
                throws InterruptedException,
                    IllegalStateException {
        throw new UnsupportedOperationException(
                "BrokerService delivers messages by subscription");
    }

    /**
     * @deprecated this interface is going to be removed from this class
     */
    @Deprecated
    public void clearInput() {
        // We have no own queue to clear, so - do nothing
    }

    /**
     * @deprecated this interface is going to be removed from this class
     */
    @Deprecated
    public int getInputSize() {
        // We have no own queue - all messages are passed through
        return 0;
    }

    /**
     * @deprecated this interface is going to be removed from this class
     */
    @Deprecated
    public void releaseReceivers() {
    }

    /**
     * @deprecated this interface is going to be removed from this class
     */
    @Deprecated
    public void setInputSize(final int arg0) {
        // We have no own queue
    }

    /**
     * @deprecated this interface is going to be removed from this class
     */
    @Deprecated
    public void onChannelClosed(final ChannelClosedEvent event) {
    }

    /**
     * @deprecated this interface is going to be removed from this class
     */
    @Deprecated
    public void onChannelError(final ChannelErrorEvent event) {
    }

    /**
     * @deprecated this interface is going to be removed from this class
     */
    @Deprecated
    public void onChannelOpened(final EventObject event) {
    }
}
