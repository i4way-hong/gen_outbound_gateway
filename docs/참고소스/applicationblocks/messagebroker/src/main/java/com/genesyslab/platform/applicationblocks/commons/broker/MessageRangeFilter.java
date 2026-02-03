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

import java.util.Arrays;


/**
 * <code>MessageRangeFilter</code> uses message id as evaluation criteria.
 * <code>MessageRangeFilter</code> contains a set of valid message ids that is used for checking if a message's id
 * present in the set.
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public class MessageRangeFilter extends MessageFilter {

    private int[] messages;


    /**
     * Creates an instance of <code>MessageRangeFilter</code> class.
     *
     * @param messagesRange initializes message range
     */
    public MessageRangeFilter(final int[] messagesRange) {
        validate(messagesRange);
        initialize(messagesRange);
    }

    /**
     * Creates an instance of <code>MessageRangeFilter</code> class.
     *
     * @param isNegated if <code>true</code> - the predicate is considered as negated:
     *            an analog of the logical 'NOT' operation
     * @param messagesRange initializes message range
     */
    public MessageRangeFilter(final boolean isNegated, final int[] messagesRange) {
        super(isNegated);
        validate(messagesRange);
        initialize(messagesRange);
    }

    /**
     * Creates an instance of <code>MessageRangeFilter</code> class.
     *
     * @param protocolDescription initializes protocol description
     * @param messagesRange initializes message range
     */
    public MessageRangeFilter(
            final ProtocolDescription protocolDescription,
            final int[] messagesRange) {
        super(protocolDescription);
        validate(messagesRange);
        initialize(messagesRange);
    }

    /**
     * Creates an instance of <code>MessageRangeFilter</code> class.
     *
     * @param isNegated if <code>true</code> - the predicate is considered as negated:
     *            an analog of the logical 'NOT' operation
     * @param protocolDescription initializes protocol description
     * @param messagesRange initializes message range
     */
    public MessageRangeFilter(
            final boolean isNegated,
            final ProtocolDescription protocolDescription,
            final int[] messagesRange) {
        super(isNegated, protocolDescription);
        validate(messagesRange);
        initialize(messagesRange);
    }


    /**
     * Gets the set of message ids.
     *
     * @return set of message ids
     */
    public int[] getMessagesRange() {
        return this.messages;
    }

    /**
     * Sets the set of valid message ids.
     *
     * @param messagesRange set of message ids
     */
    public void setMessagesRange(final int[] messagesRange) {
        validate(messagesRange);
        initialize(messagesRange);
    }


    /**
     * Evaluates a message using message id as evaluation criteria.
     *
     * @param message a message targeted for evaluation
     * @return <code>true</code> if the obj's message id present in the set of the valid message ids,
     *         otherwise returns <code>false</code>
     */
    protected boolean evaluate(final Message message) {
        if (!super.evaluate(message)) {
            return false;
        }

        return (Arrays.binarySearch(this.messages, message.messageId()) >= 0);
    }


    private void validate(final int[] messagesRange) {
        if (messagesRange == null) {
            throw new NullPointerException("messageRange is null.");
        }

        if (messagesRange.length == 0) {
            throw new IllegalArgumentException("messageRange is empty.");
        }
    }

    private void initialize(final int[] messagesRange) {
        this.messages = new int[messagesRange.length];
        System.arraycopy(messagesRange, 0,
                this.messages, 0, messagesRange.length);
        Arrays.sort(this.messages);
    }
}
