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
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;


class DefaultConfServicePolicy
        implements IConfServicePolicy {

    private boolean validateBeforeSave = true;

    /**
     * Determines whether the cache should be queried before a retrieve operation
     * is performed.
     *
     * @param query The query for the retrieve operation
     * @return always false
     */
    public boolean getQueryCacheOnRetrieve(final ICfgQuery query) {
        return false;
    }

    /**
     * Determines whether the cache should be queried before a retrieve multiple
     * operation is performed.
     *
     * @param query The query for the retrieve operation
     * @return always false
     */
    public boolean getQueryCacheOnRetrieveMultiple(final ICfgQuery query) {
        return false;
    }

    /**
     * Determines whether the specified object should be cached upon being
     * retrieved from the configuration server.
     *
     * @param obj The retrieved configuration object
     * @return always false
     */
    public boolean getCacheOnRetrieve(final ICfgObject obj) {
        return false;
    }

    /**
     * Determines whether the specified object should be cached upon being
     * saved in the configuration server.
     *
     * @param obj The configuration object being saved
     * @return always false
     */
    public boolean getCacheOnSave(final ICfgObject obj) {
        return false;
    }

    /**
     * Determines whether link resolution should be attempted through the cache
     * before querying the configuration server (link resolution refers to
     * the automatic retrieval of configuration objects when certain
     * properties are accessed).
     *
     * @param objectType The type of object for which link resolution
     *            is to be attempted
     * @return always false
     */
    public boolean getAttemptLinkResolutionThroughCache(final CfgObjectType objectType) {
        return false;
    }

    /**
     * Determines whether an existing cached object should be overwritten
     * if a newer copy is retrieved from configuration server.
     *
     * @param newObj the new version of a cached object
     * @return always false
     */
    public boolean getOverwriteCachedVersionOnRetrieve(final ICfgObject newObj) {
        return false;
    }

    /**
     * Determines whether the properties of an object should be validated
     * for correctness before a save operation is attempted.
     *
     * @return true if objects should be validated before save to server
     */
    public boolean getValidateBeforeSave() {
        return validateBeforeSave;
    }

    /**
     * Determines whether the properties of an object should be validated
     * for correctness before a save operation is attempted.
     *
     * @param value new value for the validation enabling property
     */
    public void setValidateBeforeSave(final boolean value) {
        validateBeforeSave = value;
    }
}
