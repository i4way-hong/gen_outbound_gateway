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
package com.genesyslab.platform.applicationblocks.commons;

import com.genesyslab.platform.commons.protocol.MessageHandler;


/**
 * <code>PredicateBase</code> class is an abstract base class that implements <code>Predicate</code> interface.
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public abstract class PredicateBase<T>
        implements Predicate<T> {

    private boolean isNegated = false;


    protected PredicateBase() {
    }

    /**
     * Creates an instance of predicate.
     *
     * @param isNegated if true the predicate is considered as negated:
     *      an analog of the logical 'NOT' operation
     */
    protected PredicateBase(final boolean isNegated) {
        this.isNegated = isNegated;
    }

    /**
     * Negated state of the predicate.
     * If an object is in negated state a logical 'NOT' is applied to all the object's
     * logical operations.
     *
     * @return if NOT is applied
     */
    public boolean isNegated() {
        return this.isNegated;
    }

    /**
     * Negated state of the predicate.
     * If an object is in negated state a logical 'NOT' is applied to all the object's
     * logical operations.
     *
     * @param value if NOT is applied
     */
    public void setNegated(final boolean value) {
        this.isNegated = value;
    }

    /**
     * Evaluates predicate's condition of being true or false. Makes the result inversion
     * in case the predicate is negated.
     *
     * @param obj object to be used for checking the predicate condition
     * @return the true or false of the predicate's condition
     */
    public boolean invoke(final T obj) {
        if (isNegated()) {
            return !evaluate(obj);
        }

        return evaluate(obj);
    }

    /**
     * Evaluates predicate's condition on true or false.
     *
     * @param obj object to be used for checking the predicate condition
     * @return the true or false of the predicate's condition
     */
    protected abstract boolean evaluate(final T obj);
}
