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

import java.util.ArrayList;

import com.genesyslab.platform.commons.protocol.MessageHandler;


/**
 * <code>CompositePredicateBase</code> class is an abstract base class that can consist of a set
 * of atomic predicates.
 * @deprecated
 * Message Broker Application Block is deprecated. 
 * Use {@link com.genesyslab.platform.commons.protocol.DuplexChannel#setMessageHandler(MessageHandler) setMessageHandler(handler)} to handle incoming messages asynchronously.
*/
@Deprecated
public abstract class CompositePredicateBase<T>
        extends PredicateBase<T> {

    private ArrayList<Predicate<T>> predicates = new ArrayList<Predicate<T>>();


    protected CompositePredicateBase() {
        super();
    }

    /**
     * Creates an instance of <code>CompositePredicateBase</code> class.
     *
     * @param isNegated if true the predicate is considered as negated:
     *      an analog of the logical '!' operation
     */
    protected CompositePredicateBase(final boolean isNegated) {
        super(isNegated);
    }

    /**
     * Creates an instance of <code>CompositePredicateBase</code> class.
     * Composite predicate created with this constructor is an analog of a binary logical operation.
     *
     * @param p0 first predicate
     * @param p1 second predicate
     */
    protected CompositePredicateBase(
            final Predicate<T> p0, final Predicate<T> p1) {
        addPredicate(p0);
        addPredicate(p1);
    }

    /**
     * Gets list of atomic predicates.
     *
     * @return list of predicates
     */
    protected ArrayList<Predicate<T>> getPredicates() {
        return predicates;
    }


    /**
     * Adds a predicate to the predicates' set.
     *
     * @param predicate predicate to be added to the predicates' set
     */
    public void addPredicate(final Predicate<T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is null.");
        }

        if (predicates.contains(predicate)) {
            throw new IllegalArgumentException("predicate was already added.");
        }

        predicates.add(predicate);
    }

    /**
     * Removes a predicate from the predicates' set.
     *
     * @param predicate predicate to be removed from the predicates' set
     */
    public void removePredicate(final Predicate<T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is null.");
        }

        if (!predicates.contains(predicate)) {
            throw new IllegalArgumentException("predicate was not added.");
        }

        predicates.remove(predicate);
    }
}
