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
 * An interface which defines the contract for a configuration cache
 * storage module.
 */
public interface IConfCacheStorage {

    /**
     * Adds the configuration object to the storage.
     *
     * @param obj The configuration object
     */
    void add(ICfgObject obj);

    /**
     * Updates an existing configuration object in the storage
     * with the passed copy.
     *
     * @param obj The new version of a cached configuration object
     */
    void update(ICfgObject obj);

    /**
     * Removes the specified configuration object from the storage.
     *
     * @param obj The configuration object to remove
     * @return true if object successfully deleted, false otherwise
     */
    boolean remove(ICfgObject obj);

    /**
     * Removes all items in storage.
     */
    void clear();

    /**
     * Retrieves a list of all objects in the storage.
     *
     * @param <T> The type of object in the resulting list
     * @param cls class of objects to be retrieved
     * @return An enumerable list of the requested objects
     */
    <T extends ICfgObject> Iterable<T> retrieve(Class<T> cls);

    /**
     * Retrieves a list of objects in storage utilizing a "helper" parameter.
     *
     * @param <T> The type of object in the resulting list
     * @param cls class of objects to be retrieved
     * @param helper A helper parameter to be defined in each implementation
     * @return An enumerable list of the requested objects
     */
    <T extends ICfgObject> Iterable<T> retrieve(Class<T> cls, Object helper);
}
