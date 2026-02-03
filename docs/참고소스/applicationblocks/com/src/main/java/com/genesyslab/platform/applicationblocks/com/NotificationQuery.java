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

// Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;

import java.util.Hashtable;


/**
 * This class is used to subscribe to notifications from the Configuration Server.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public class NotificationQuery {
    private static final String OBJECT_DBID = "object_dbid";
    private static final String TENANT_DBID = "tenant_id";
    private static final String OBJECT_TYPE = "object_type";

    private Hashtable<String, Object> filter;

    /**
     * Creates a new instance of subscription request query object.
     */
    public NotificationQuery() {
        filter = new Hashtable<String, Object>();
    }

    /**
     * This property is used if the client wants to follow changes in a specific object.
     * To identify particular object it is also required to specify valid object type -
     * {@link #setObjectType(CfgObjectType)}.
     *
     * @param dbid dbid of the object to be monitored
     */
    public void setObjectDbid(final Integer dbid) {
        filter.put(OBJECT_DBID, dbid);
    }

    /**
     * This property is used if the client wants to follow changes in a specific object.
     * To identify particular object it is also required to specify valid object type -
     * {@link #getObjectType()}.
     *
     * @return dbid of the object to be monitored
     * @see #setObjectDbid(Integer)
     */
    public Integer getObjectDbid() {
        Object myValue = filter.get(OBJECT_DBID);

        if (myValue == null) {
            return -1;
        }

        return (Integer) myValue;
    }

    /**
     * This property is used if the client wants to subscribe to all events happening at the specific
     * Tenant.
     *
     * @param dbid dbid of the tenant to be monitored
     */
    public void setTenantDbid(final Integer dbid) {
        filter.put(NotificationQuery.TENANT_DBID, dbid);
    }

    /**
     * This property is used if the client wants to subscribe to all events happening at the specific
     * Tenant.
     *
     * @return dbid of the tenant to be monitored or -1 (if not set)
     * @see #setTenantDbid(Integer)
     */
    public Integer getTenantDbid() {
        Object myValue = filter.get(NotificationQuery.TENANT_DBID);

        if (myValue == null) {
            return -1;
        }

        return (Integer) myValue;
    }

    /**
     * This property is used if the client wants to subscribe to all events happening with all objects
     * of a the specific type.
     *
     * @param type object type for monitoring
     */
    public void setObjectType(final CfgObjectType type) {
        filter.put(NotificationQuery.OBJECT_TYPE, type);
    }

    /**
     * This property is used if the client wants to subscribe to all events happening with all objects
     * of a the specific type.
     *
     * @return object type or "NoObject"
     * @see #setObjectType(CfgObjectType)
     */
    public CfgObjectType getObjectType() {
        Object myValue = filter.get(NotificationQuery.OBJECT_TYPE);

        if (myValue == null) {
            return CfgObjectType.CFGNoObject;
        }

        return (CfgObjectType) myValue;
    }


    public final Hashtable<String, Object> getFilter() {
        return filter;
    }
}
