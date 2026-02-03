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

import com.genesyslab.platform.applicationblocks.com.ICfgQuery;
import com.genesyslab.platform.applicationblocks.com.ICfgObject;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;

import java.util.Collection;


/**
 * An interface defining a cache query engine contract.
 */
public interface IConfCacheQueryEngine {

    /**
     * Determines whether the query engine can execute the specified query.
     *
     * @param query The query to execute
     * @return true if the query engine can execute this query, false otherwise
     */
    boolean canExecute(
            final ICfgQuery query);

    /**
     * Retrieves an object based on the specified query.
     *
     * @param <T> The type of object to retrieve
     * @param cls class of object to be retrieved
     * @param query The query by which to retrieve the object
     * @return The object specified by the query or null if the object is not found
     */
    <T extends ICfgObject> T retrieveObject(
            final Class<T> cls,
            final ICfgQuery query);

    /**
     * Retrieves a collection of objects based on the specified query.
     *
     * @param <T> The type of object to retrieve
     * @param cls class of objects to be retrieved
     * @param query The query by which to retrieve the objects
     * @return The objects specified by the query or an empty collection
     *             if no results are matching objects are found
     */
    <T extends ICfgObject> Collection<T> retrieveMultipleObjects(
            final Class<T> cls,
            final ICfgQuery query);

    /**
     * Retrieves an object using the specified parameters.
     *
     * @param <T> The type of object to retrieve
     * @param cls class of object to be retrieved
     * @param type The type of object (CfgObjectType)
     * @param dbid The dbid of the object
     * @return A configuration object with the requested dbid and type or
     *             null if the object is not found
     */
    <T extends ICfgObject> T retrieveObject(
            final Class<T> cls,
            final CfgObjectType type,
            final int dbid);
}
