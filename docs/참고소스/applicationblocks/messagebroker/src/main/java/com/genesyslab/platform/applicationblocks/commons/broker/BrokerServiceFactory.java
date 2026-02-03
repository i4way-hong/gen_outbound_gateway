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
import com.genesyslab.platform.commons.protocol.MessageReceiver;
import com.genesyslab.platform.commons.protocol.MessageReceiverManagement;
import com.genesyslab.platform.commons.protocol.RequestReceiver;
import com.genesyslab.platform.commons.protocol.RequestReceiverManagement;
import com.genesyslab.platform.commons.threading.AsyncInvoker;


/**
 * <code>BrokerServiceFactory</code> class implements factory for broker services,
 * <code>EventReceivingBrokerService</code> and <code>RequestReceivingBrokerService</code> in particular.
 * <p/>
 * Example:
 * <example>
 * <code><pre>[Java]
 *     StatServerProtocol statProtocol = ...;
 *     AsyncInvoker invoker = new SingleThreadInvoker("statEventHandler");
 *     EventReceivingBrokerService eventBroker =
 *             BrokerServiceFactory.createEventBroker(statProtocol, invoker);
 *
 *     // register handler for the stat protocol messages:
 *     eventBroker.register(
 *         new MyStatAction(),
 *         new MessageFilter(statProtocol.getProtocolId())
 *     );
 * </pre></code>
 * </example>
 *
 * @see EventReceivingBrokerService
 * @see RequestReceivingBrokerService
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public final class BrokerServiceFactory {

    /**
     * Creates and activates instance of <code>EventBrokerService</code> class.
     * Activated service contains internal running thread (SingleThreadInvoker) for
     * messages handling purposes. So, when user application creates this service,
     * it must deactivate and dispose it properly. Following methods can be used for this:
     * {@link MessageBrokerService#deactivate() MessageBrokerService.deactivate()} and
     * {@link MessageBrokerService#dispose() MessageBrokerService.dispose()}.
     *
     * @deprecated
     * @param receiver Initializes event message receiver
     * @return Activated EventBrokerService instance
     * @see MessageBrokerService#deactivate()
     * @see MessageBrokerService#dispose()
     */
    @Deprecated
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static EventBrokerService CreateEventBroker(
            final MessageReceiver receiver) {
        EventBrokerService eventBroker = new EventBrokerService(receiver);
        eventBroker.activate();
        return eventBroker;
    }

    /**
     * Creates and activates instance of <code>RequestBrokerService</code> class.
     * Activated service contains internal running thread (SingleThreadInvoker) for
     * messages handling purposes. So, when user application creates this service,
     * it must deactivate and dispose it properly. Following methods can be used for this:
     * {@link MessageBrokerService#deactivate() MessageBrokerService.deactivate()} and
     * {@link MessageBrokerService#dispose() MessageBrokerService.dispose()}.
     *
     * @deprecated
     * @param receiver Initializes event message receiver
     * @return Activated ReqestBrokerService instance
     * @see MessageBrokerService#deactivate()
     * @see MessageBrokerService#dispose()
     */
    @Deprecated
    public static RequestBrokerService CreateRequestBroker(
            final RequestReceiver receiver) {
        RequestBrokerService requestBroker = new RequestBrokerService(receiver);
        requestBroker.activate();
        return requestBroker;
    }

    /**
     * Creates broker service and initializes client protocol with it as with "external receiver".
     * <p/>
     * It is possible to use created service for other protocol connections:
     * <example>
     * <code><pre>[Java]
     *     EventReceivingBrokerService eventBroker =
     *             BrokerServiceFactory.createEventBroker(statProtocol, invoker);
     *
     *     ixnProtocol.setReceiver(eventBroker);
     *     routingProtocol.setReceiver(eventBroker);
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
     * @param protocol client protocol connection to distribute messages from (usually it's some protocol connection)
     * @param invoker invoker to be used for messages publishing
     * @return initialized broker service instance
     * @see MessageReceiverManagement#setReceiver(com.genesyslab.platform.commons.protocol.MessageReceiverSupport)
     */
    public static EventReceivingBrokerService createEventBroker(
            final MessageReceiverManagement protocol,
            final AsyncInvoker invoker) {
        EventReceivingBrokerService broker = new EventReceivingBrokerService(invoker);
        protocol.setReceiver(broker);
        return broker;
    }

    /**
     * Creates broker service and initializes server channel with it as with "external receiver".
     * <p/>
     * When the brokers' invoker handles some client message, all other clients/requests are waiting,
     * so, server side handler must execute request messages fast and schedule long tasks to other threads.
     *
     * @param protocol server channel to distribute clients requests from (it can be some ServerChannel instance)
     * @param invoker invoker to be used for requests messages handling
     * @return initialized broker service instance
     * @see RequestReceiverManagement#setReceiver(com.genesyslab.platform.commons.protocol.RequestReceiverSupport)
     */
    public static RequestReceivingBrokerService createRequestBroker(
            final RequestReceiverManagement protocol,
            final AsyncInvoker invoker) {
        RequestReceivingBrokerService broker = new RequestReceivingBrokerService(invoker);
        protocol.setReceiver(broker);
        return broker;
    }
}
