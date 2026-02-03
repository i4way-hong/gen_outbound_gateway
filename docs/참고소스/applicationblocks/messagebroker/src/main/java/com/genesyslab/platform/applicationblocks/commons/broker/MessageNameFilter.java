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
 * <code>MessageNameFilter</code> uses message name as evaluation criteria.
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public class MessageNameFilter extends MessageFilter {

    private String messageName;

    /**
     * Creates an instance of <code>MessageNameFilter</code> class.
     *
     * @param theMessageName initializes message name
     */
    public MessageNameFilter(final String theMessageName) {
        if (theMessageName == null) {
            throw new NullPointerException("theMessageName is null.");
        }

        this.messageName = theMessageName;
    }

    /**
     * Creates an instance of <code>MessageNameFilter</code> class.
     *
     * @param isNegated if <code>true</code> - the predicate is considered as negated:
     *            an analog of the logical 'NOT' operation
     * @param theMessageName initializes message name
     */
    public MessageNameFilter(
            final boolean isNegated,
            final String theMessageName) {
        super(isNegated);
        if (theMessageName == null) {
            throw new NullPointerException("theMessageName is null.");
        }

        this.messageName = theMessageName;
    }

    /**
     * Creates an instance of <code>MessageNameFilter</code> class.
     *
     * @param theProtocolDescription initializes protocol description
     * @param theMessageName initializes message name
     */
    public MessageNameFilter(
            final ProtocolDescription theProtocolDescription,
            final String theMessageName) {
        super(theProtocolDescription);
        if (theMessageName == null) {
            throw new NullPointerException("theMessageName is null.");
        }

        this.messageName = theMessageName;
    }

    /**
     * Creates an instance of <code>MessageNameFilter</code> class.
     *
     * @param isNegated if <code>true</code> - the predicate is considered as negated:
     *            an analog of the logical 'NOT' operation
     * @param theProtocolDescription initializes protocol description
     * @param theMessageName initializes message name
     */
    public MessageNameFilter(
            final boolean isNegated,
            final ProtocolDescription theProtocolDescription,
            final String theMessageName) {
        super(isNegated, theProtocolDescription);
        if (theMessageName == null) {
            throw new NullPointerException("theMessageName is null.");
        }

        this.messageName = theMessageName;
    }


    /**
     * Gets message name.
     *
     * @return message name
     */
    public String getMessageName() {
        return this.messageName;
    }

    /**
     * Sets message name.
     *
     * @param value new value for message name
     */
    public void setMessageName(final String value) {
        this.messageName = value;
    }

    /**
     * @deprecated
     * @see #setMessageName(String)
     */
    public void setMessageId(final String value) {
        this.messageName = value;
    }


    /**
     * Evaluates a message name, protocol description and endpoint name as criteria.
     *
     * @param message a message targeted for evaluation
     * @return <code>true</code> if message name, protocol description and endpoint name of the message are equal
     *         to the message name, protocol description and endpoint name of the message name filter,
     *         otherwise returns <code>false</code>.
     */
    protected boolean evaluate(final Message message) {
        if (!super.evaluate(message)) {
            return false;
        }

        return (messageName.equals(message.messageName()));
    }
}
