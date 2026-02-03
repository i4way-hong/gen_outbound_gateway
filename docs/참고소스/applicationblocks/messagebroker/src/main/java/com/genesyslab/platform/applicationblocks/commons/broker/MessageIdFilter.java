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
import com.genesyslab.platform.commons.protocol.ProtocolDescription;


/**
 * <code>MessageIdFilter</code> uses message id as evaluation criteria.
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public final class MessageIdFilter extends MessageFilter {

    private int messageId;


    /**
     * Creates an instance of <code>MessageIdFilter</code> class.
     *
     * @param theMessageId initializes message id
     */
    public MessageIdFilter(final int theMessageId) {
        super();
        this.messageId = theMessageId;
    }

    /**
     * Creates an instance of <code>MessageIdFilter</code> class.
     *
     * @param isNegated if <code>true</code> - the predicate is considered as negated:
     *            an analog of the logical 'NOT' operation
     * @param theMessageId initializes message id
     */
    public MessageIdFilter(
            final boolean isNegated,
            final int theMessageId) {
        super(isNegated);
        this.messageId = theMessageId;
    }

    /**
     * Creates an instance of <code>MessageIdFilter</code> class.
     *
     * @param theProtocolDescription initializes protocol description
     * @param theMessageId initializes message id property
     */
    public MessageIdFilter(
            final ProtocolDescription theProtocolDescription,
            final int theMessageId) {
        super(theProtocolDescription);
        this.messageId = theMessageId;
    }

    /**
     * Creates an instance of <code>MessageIdFilter</code> class.
     *
     * @param isNegated if <code>true</code> - the predicate is considered as negated:
     *            an analog of the logical 'NOT' operation
     * @param theProtocolDescription initializes protocol description
     * @param theMessageId initializes message id
     */
    public MessageIdFilter(
            final boolean isNegated,
            final ProtocolDescription theProtocolDescription,
            final int theMessageId) {
        super(isNegated, theProtocolDescription);

        this.messageId = theMessageId;
    }


    /**
     * Gets message id.
     *
     * @return message id
     */
    public int getMessageId() {
        return this.messageId;
    }

    /**
     * Sets message id.
     *
     * @param value new value for message id
     */
    public void setMessageId(final int value) {
        this.messageId = value;
    }


    /**
     * Evaluates a message using message id as evaluation criteria.
     *
     * @param message A message targeted for evaluation
     * @return <code>true</code> if message id, protocol description and endpoint name of the message are equal
     * to the message id protocol description and endpoint name of the message id filter, otherwise
     * returns <code>false</code>.
     */
    @Override
    protected boolean evaluate(final Message message) {
        if (!super.evaluate(message)) {
            return false;
        }

        return (this.messageId == message.messageId());
    }
}
