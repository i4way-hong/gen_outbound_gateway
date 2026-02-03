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

import com.genesyslab.platform.applicationblocks.com.ICfgObject;


/**
 * The interface to be implemented when defining a cache policy.
 */
public interface IConfCachePolicy {

    /**
     * Specifies whether the cache should return copies of stored objects
     * or the objects themselves.
     *
     * @return true if cache should clone objects on retrieve
     */
    boolean getReturnCopies();

    /**
     * Determines if the passed object should be stored in the cache
     * upon receiving notification of its creation in the configuration server.
     *
     * @param obj The configuration object
     * @return true if the object should be cached, false otherwise
     */
    boolean getCacheOnCreate(ICfgObject obj);

    /**
     * Determines if the passed object's state should be synchronized
     * with the configuration server upon receiving an update.
     *
     * @param obj The configuration object
     * @return true if the object should be updated, false otherwise
     */
    boolean getTrackUpdates(ICfgObject obj);

    /**
     * Determines if the passed object should be deleted from the cache if it
     * is deleted from the configuration server.
     *
     * @param obj The Configuration object
     * @return true if the object should be removed, false otherwise
     */
    boolean getRemoveOnDelete(ICfgObject obj);
}
