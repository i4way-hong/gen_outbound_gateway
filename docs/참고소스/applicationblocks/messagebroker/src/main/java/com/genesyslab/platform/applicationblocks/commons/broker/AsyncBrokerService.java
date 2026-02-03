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
import com.genesyslab.platform.commons.threading.AsyncInvoker;


/**
 * <code>AsyncBrokerService</code> class implements a general purpose asynchronous publishing broker service.
 * <p/>Usage sample:<pre><code>[Java]
 *     AsyncInvoker invoker = new SingleThreadInvoker(
 *                                    "AsyncBrokerServiceTest-1");
 *     AsyncBrokerService&lt;SomePublishedEvent&gt; service =
 *               new AsyncBrokerService&lt;SomePublishedEvent&gt;(invoker);
 *     Action&lt;SomePublishedEvent&gt; action = Action&lt;SomePublishedEvent&gt;() {
 *         public void handle(final SomePublishedEvent event) {
 *             // do something with event
 *         }
 *     }
 *     service.register(action, null);
 *
 *     SomePublishedEvent event = new SomePublishedEvent(...);
 *     service.publish(event);
 * </code></pre>
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public final class AsyncBrokerService<T> extends AsyncBrokerServiceBase<T> {

    /**
     * Creates an instance.
     * Asynchronous event broker service must be initialized with asynchronous
     * invoker to be able to handle events.
     *
     * @see #setInvoker(AsyncInvoker)
     */
    public AsyncBrokerService() {
        super();
    }

    /**
     * Creates an instance.
     *
     * @param invoker asynchronous invoker to publish events with
     */
    public AsyncBrokerService(final AsyncInvoker invoker) {
        super(invoker);
    }
}
