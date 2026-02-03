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

import com.genesyslab.platform.applicationblocks.commons.PredicateBase;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolDescription;


/**
 * <code>MessageFilter</code> class is designed for filtering messages using protocol description and endpoint
 * name as evaluation criteria.
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public class MessageFilter extends PredicateBase<Message> {

    private ProtocolDescription protocolDescription;
    private String endpointName;
    private Integer protocolId;

    /**
     * Creates an instance of <code>MessageFilter</code> class.
     *
     * @param isNegated If <code>true</code> - the predicate is considered as negated:
     *            an analog of the logical 'NOT' operation
     * @param theProtocolDescription Description of protocol
     * @param theEndpointName Name of endpoint
     */
    public MessageFilter(
            final boolean isNegated,
            final ProtocolDescription theProtocolDescription,
            final String theEndpointName) {
        super(isNegated);
        this.protocolDescription = theProtocolDescription;
        this.endpointName = theEndpointName;
    }

    /**
     * Creates an instance of <code>MessageFilter</code> class.
     *
     * @param theProtocolDescription Description of protocol
     * @param theEndpointName Name of endpoint
     */
    public MessageFilter(
            final ProtocolDescription theProtocolDescription,
            final String theEndpointName) {
        this(false, theProtocolDescription, theEndpointName);
    }

    /**
     * Creates an instance of <code>MessageFilter</code> class.
     *
     * @param isNegated If <code>true</code> - the predicate is considered as negated:
     *            an analog of the logical 'NOT' operation
     * @param theProtocolDescription Description of protocol
     */
    public MessageFilter(
            final boolean isNegated,
            final ProtocolDescription theProtocolDescription) {
        this(isNegated, theProtocolDescription, null);
    }

    /**
     * Creates an instance of <code>MessageFilter</code> class.
     *
     * @param theProtocolDescription Description of protocol
     */
    public MessageFilter(final ProtocolDescription theProtocolDescription) {
        this(false, theProtocolDescription, null);
    }

    /**
     * Creates an instance of <code>MessageFilter</code> class.
     *
     * @param protocolId The protocol's unique identifier as specified by its ProtocolId property
     */
    public MessageFilter(final int protocolId) {
        this.protocolId = protocolId;
    }

    /**
     * Creates an instance of <code>MessageFilter</code> class.
     *
     * @param isNegated If <code>true</code> - the predicate is considered as negated:
     *            an analog of the logical 'NOT' operation
     */
    public MessageFilter(final boolean isNegated) {
        super(isNegated);
    }

    /**
     * Creates an instance of <code>MessageFilter</code> class.
     */
    public MessageFilter() {
    }


    /**
     * Gets the protocol description.
     *
     * @return protocol description or null
     */
    public ProtocolDescription getProtocolDescription() {
        return this.protocolDescription;
    }

    /**
     * Sets protocol description for filtering.
     *
     * @param theProtocolDescription new value for protocol description or null
     */
    public void setProtocolDescription(
            final ProtocolDescription theProtocolDescription) {
        this.protocolDescription = theProtocolDescription;
    }

    /**
     * Gets endpoint name.
     *
     * @return endpoint name or null
     */
    public String getEndpointName() {
        return this.endpointName;
    }

    /**
     * Sets endpoint name for filtering.
     *
     * @param theEndpointName new value for endpoint name or null
     */
    public void setEndpointName(final String theEndpointName) {
        this.endpointName = theEndpointName;
    }

    /**
     * Gets the value of the protocol's unique identifier.
     */
    public Integer getProtocolId() {
        return this.protocolId;
    }

    /**
     * Sets the value of the protocol's unique identifier.
     *
     * @param theProtocolId new value for protocolId or null
     */
    public void setProtocolId(final Integer theProtocolId) {
        this.protocolId = theProtocolId;
    }

    /**
     * Evaluates a message using protocol description, endpoint name, and protocolId as criteria.
     *
     * The filter is evaluated as an "and" comparison of those of the above attributes which
     * have been assigned a value. For example, if the protocol description and endpoint are
     * specified while protocolId isn't, protocolId will be ignored in the evaluation and this method
     * will return true if both the endpoint and protocol description match the values
     * in IMessage.
     *
     * @param message A message targeted for evaluation
     * @return <code>true</code> if the filter's properties match their counterparts in
     *             Message, otherwise returns <code>false</code>
     */
    protected boolean evaluate(final Message message) {
        if (message == null) {
            throw new NullPointerException("message can't be null");
        }

        if ((protocolId != null)
                && !protocolId.equals(message.getProtocolId())) {
            return false;
        }

        if ((protocolDescription != null)
             && !protocolDescription.equals(
                 message.getProtocolDescription())) {
            return false;
        }

        if ((endpointName != null) && endpointName.length() > 0
             && (!endpointName.equals(message.getEndpoint().getName()))) {
            return false;
        }

        return true;
    }
}
