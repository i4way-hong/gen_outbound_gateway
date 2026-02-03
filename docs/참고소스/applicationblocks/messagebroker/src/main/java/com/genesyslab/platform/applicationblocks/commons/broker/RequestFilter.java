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

import com.genesyslab.platform.applicationblocks.commons.Predicate;
import com.genesyslab.platform.applicationblocks.commons.PredicateBase;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.RequestContext;


/**
 * <code>RequestFilter</code> class is designed for filtering requests.
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public class RequestFilter extends PredicateBase<RequestContext> {

    private Predicate<Message> messageFilter;


    /**
     * Creates an instance of RequestFilter class.
     *
     * @param filter initializes MessageFilter
     */
    public RequestFilter(final Predicate<Message> filter) {
        messageFilter = filter;
    }


    /**
     * Gets message filter.
     *
     * @return message filter
     */
    public Predicate<Message> getMessageFilter() {
        return this.messageFilter;
    }

    /**
     * Sets message filter.
     *
     * @param value message filter
     */
    public void setMessageFilter(final Predicate<Message> value) {
        this.messageFilter = value;
    }


    /**
     * Evaluates a request using protocol description and endpoint name as criterias.
     *
     * @param context A request targeted for evaluation
     * @return <code>true</code> if protocol description and endpoint name of the request
     *         are equal to the protocol description
     *         and endpoint name of the message filter property,
     *         otherwise returns <code>false</code>
     */
    protected boolean evaluate(final RequestContext context) {
        if (context == null) {
            throw new NullPointerException("context");
        }

        if (messageFilter != null) {
            return messageFilter.invoke(context.getRequestMessage());
        }

        return true;
    }
}
