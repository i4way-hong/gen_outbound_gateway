//===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:

// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.

// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.

// THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
// ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
// DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
// WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
// NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
// NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
// EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
// CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
// NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).

// Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.com.cache;

import com.genesyslab.platform.applicationblocks.com.runtime.MiscConstants;

import com.genesyslab.platform.applicationblocks.commons.Predicate;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;


/**
 * A filter for ConfCacheEvents. Note that the filter properties are evaluated
 * using the "and" operator. For example, if ObjectDbid and ObjectType are set,
 * events regarding objects with the specified dbid AND the specified type
 * will be sent to the subscriber.
 */
public final class ConfCacheFilter
        implements Predicate<ConfCacheEvent> {

    private int objectDbid = -1;
    private CfgObjectType objectType = CfgObjectType.CFGNoObject;
    private ConfCacheUpdateType updateType = ConfCacheUpdateType.Unspecified;

    /**
     * The dbid of the object regarding which events
     * are to be sent to the subscriber.
     *
     * @return dbid filter value or -1
     */
    public int getObjectDbid() {
        return objectDbid;
    }

    /**
     * The dbid of the object regarding which events
     * are to be sent to the subscriber.
     *
     * @param value dbid filter value or -1
     */
    public void setObjectDbid(final int value) {
        objectDbid = value;
    }

    /**
     * The type of object regarding which events
     * are to be sent to the subscriber.
     *
     * @return object type filter value
     */
    public CfgObjectType getObjectType() {
        return objectType;
    }

    /**
     * The type of object regarding which events
     * are to be sent to the subscriber.
     *
     * @param value object type filter value
     */
    public void setObjectType(final CfgObjectType value) {
        objectType = value;
    }

    /**
     * The type of update regarding which events
     * are to be sent to the subscriber.
     *
     * @return cache event type
     */
    public ConfCacheUpdateType getUpdateType() {
        return updateType;
    }

    /**
     * The type of update regarding which events
     * are to be sent to the subscriber.
     *
     * @param value cache event type
     */
    public void setUpdateType(final ConfCacheUpdateType value) {
        updateType = value;
    }


    /**
     * Evaluate filter's condition on truth or false.
     *
     * @param cacheEvent event instance to be used for checking the filter condition
     * @return the truth or false of the filter's condition
     */
    public boolean invoke(final ConfCacheEvent cacheEvent) {
        if (cacheEvent == null) {
            return false;
        }

        // this is a case when no filter is specified.
        // In this case, all objects are fine
        if ((getObjectType() == CfgObjectType.CFGNoObject)
            && (getObjectDbid() == -1)
            && (getUpdateType() == ConfCacheUpdateType.Unspecified)) {
            return true;
        }

        if ((getObjectType() != CfgObjectType.CFGNoObject)
                && (getObjectType() != cacheEvent.getConfObject().getObjectType())) {
            return false;
        }

        if (getObjectDbid() != -1) {
            Integer dbid = (Integer) cacheEvent.getConfObject()
                    .getProperty(MiscConstants.DbidPropertyName);
            if ((dbid == null)
                || (getObjectDbid() != dbid)) {
                return false;
            }
        }

        if ((getUpdateType() != ConfCacheUpdateType.Unspecified)
                && (getUpdateType() != cacheEvent.getUpdateType())) {
            return false;
        }

        return true;
    }
}
