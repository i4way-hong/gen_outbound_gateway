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

import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.MessageReceiver;
import com.genesyslab.platform.commons.protocol.Receiver;
import com.genesyslab.platform.commons.threading.SingleThreadInvoker;


/**
 * <code>EventBrokerService</code> class is designed to work with event/response messages.
 *
 * <example>
 * <code><pre>[Java]
 *     final String protocolName = "Configuration";
 *     ConfServerProtocol protocol;
 *     EventBrokerService eventBroker;
 *
 *     public void initialize() {
 *         Endpoint endpoint = new Endpoint(
 *                 protocolName,
 *                 "hostname", 9999);
 *         protocol = new ConfServerProtocol(endpoint);
 *         protocol.setClientApplicationType(CfgAppType.CFGSCE.ordinal());
 *		   protocol.setClientName("clientname");
 *		   protocol.setUserName("user1");
 *		   protocol.setUserPassword("user1-password");
 *
 *         eventBroker = new EventBrokerService(protocol);
 *         eventBroker.activate();
 *         eventBroker.register(
 *             new MyAction(),
 *             new MessageFilter(protocol.getProtocolId())
 *         );
 *
 *         protocol.open();
 *     }
 *
 *     public void deinitialize() {
 *         if (eventBroker != null) {
 *             eventBroker.dispose();
 *             eventBroker = null;
 *         }
 *         if (protocol != null) {
 *             if (protocol.getState() == ChannelState.Opened) {
 *                 protocol.close();
 *             }
 *             protocol = null;
 *         }
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
 * This class has a specific behavior - it performs synchronous wait in AsyncInvoker for incoming events,
 * so, it requires dedicated invoker to be permanently busy with reading task.
 * The alternative for broker service functionality without fully occupied invoker is to use
 * EventReceivingBrokerService which is a receiver by itself and can be used as "external receiver"
 * in protocol connections to handle messages directly without intermediate queues.
 *
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public class EventBrokerService
        extends MessageBrokerService<Message> {

    private MessageReceiver<Message> receiver;


    /**
     * Creates an instance of <code>EventBrokerService</code> class.
     * When using this constructor the following additional steps should be done
     * to set the service in working state:
     * setting the invoker -  {@link #setInvoker(com.genesyslab.platform.commons.threading.AsyncInvoker)};
     * setting the receiver - {@link #setReceiver(com.genesyslab.platform.commons.protocol.MessageReceiver)};
     * calling {@link #activate()} method.
     */
    public EventBrokerService() {
    }

    /**
     * Creates an instance of <code>EventBrokerService</code> class.
     *
     * @param receiver message receiver
     */
    public EventBrokerService(final MessageReceiver<Message> receiver) {
        super(new SingleThreadInvoker("eventBroker.defaultInvoker"));
        setReceiver(receiver);
    }

    /**
     * @deprecated
     */
    public static EventBrokerService activate(
            final MessageReceiver<Message> receiver) {
        EventBrokerService service = new EventBrokerService(receiver);
        service.activate();
        return service;
    }

    public MessageReceiver<Message> getReceiver() {
        return this.receiver;
    }

    public void setReceiver(final MessageReceiver<Message> value) {
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


    public void dispose() {
        super.dispose();
        this.receiver = null;
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

    /**
     * Receives event messages.
     *
     * @return new incoming message or null on timeout
     * @throws InterruptedException
     */
    protected Message receive() throws InterruptedException {
        return receiver.receive();
    }
}
