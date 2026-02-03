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

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;


final class CacheKey {

    private int objectDbid;
    private CfgObjectType objectType;

    public CacheKey(final CfgObjectType objectType, final int objectDbid) {
        this.objectDbid = objectDbid;
        this.objectType = objectType;
    }

    public int getObjectDbid() {
        return this.objectDbid;
    }

    public void setObjectDbid(final int value) {
        this.objectDbid = value;
    }

    public CfgObjectType getObjectType() {
        return this.objectType;
    }

    public void setObjectType(final CfgObjectType value) {
        this.objectType = value;
    }

    public int hashCode() {
        int hash = getClass().hashCode();
        hash ^= this.objectDbid;
        if (this.objectType != null) {
            hash ^= this.objectType.ordinal();
        }
        return hash;
    }

    public boolean equals(final Object obj) {
        if (!(obj instanceof CacheKey)) {
            return false;
        }
        CacheKey o = (CacheKey) obj;
        return (this.objectDbid == o.objectDbid)
                && (this.objectType == o.objectType);
    }
}
