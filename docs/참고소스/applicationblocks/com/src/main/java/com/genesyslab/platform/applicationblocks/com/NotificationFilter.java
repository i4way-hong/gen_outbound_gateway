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

import com.genesyslab.platform.applicationblocks.commons.Predicate;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;


/**
 * This class is used to retrieve events from the Configuration Server.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public class NotificationFilter implements Predicate<ConfEvent> {
    private NotificationQuery query;

    /**
     * Creates a new instance of the NotificationFilter.
     *
     * @param notifQuery the query which this filter is based on
     */
    public NotificationFilter(final NotificationQuery notifQuery) {
        this.query = notifQuery;
    }

    /**
     * Used to evaluate whether the specified event passes this filter.
     *
     * @param obj the event to evaluate
     * @return true if the event passes the filter, false otherwise
     */
    public boolean invoke(final ConfEvent obj) {
        if (obj == null) {
            return false;
        }

        // this is a case when no filter is specified. In this case, all objects are fine
        if (query == null) {
            return true;
        }

        final CfgObjectType obj_type = query.getObjectType();
        if ((obj_type != null) && (!obj_type.equals(CfgObjectType.CFGNoObject))
                && (!obj_type.equals(obj.getObjectType()))) {
            return false;
        }

        final Integer obj_dbid = query.getObjectDbid();
        if ((obj_dbid != null) && (obj_dbid.intValue() != -1)
                && (obj_dbid.intValue() != obj.getObjectId())) {
            return false;
        }

        // with tenant dbids we don't know what the tenantdbid of the object is
        // and we just pass all events. TODO! Describe it in the documentation

        return true;
    }
}
