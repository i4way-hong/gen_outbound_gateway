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
 * <code>AndPredicate</code> class realizes 'AND' logical operation.
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public class AndPredicate<T> extends CompositePredicateBase<T> {

    public AndPredicate() {
        super();
    }

    public AndPredicate(final boolean isNegated) {
        super(isNegated);
    }

    /**
     * Creates an instance of <code>AndPredicate</code> class.
     * AndPredicate predicate created with this constructor is an analog of a 'AND' binary
     * logical operation.
     *
     * @param predicate0 the first predicate.
     * @param predicate1 the second predicate.
     */
    public AndPredicate(final Predicate<T> predicate0, final Predicate<T> predicate1) {
        super(predicate0, predicate1);
    }


    /**
     * Evaluates predicate's condition of being true or false.
     *
     * @param obj Object to be used for checking the predicate condition
     * @return true or false of the predicate's condition
     */
    protected boolean evaluate(final T obj) {
        for (Predicate<T> filter : getPredicates()) {
            if (!filter.invoke(obj)) {
                return false;
            }
        }

        return true;
    }
}
