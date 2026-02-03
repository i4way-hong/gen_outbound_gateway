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
package com.genesyslab.platform.applicationblocks.com.runtime;

import com.genesyslab.platform.applicationblocks.com.*;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.configuration.protocol.obj.ConfStructure;
import com.genesyslab.platform.configuration.protocol.obj.ConfStructureCollection;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.types.CfgStructureType;

import java.util.List;
import java.util.ArrayList;


/**
 * This class represents access permissions of an associated Configuration Server object.
 *
 * <p/>It is an internal implementation class for the public interface
 * {@link PermissionDescriptor}.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 * @deprecated
 * @see com.genesyslab.platform.applicationblocks.com.objects.CfgACL CfgACL
 */
@Deprecated
public final class PermissionDescriptorImpl implements PermissionDescriptor {
	
	private static final ILogger log = Log.getLogger(PermissionDescriptorImpl.class);

    private int  objectDbid;
    private int  objectType;
    private int  accessMask;

    PermissionDescriptorImpl(
            final int objectDbid,
            final int objectType,
            final int accessMask) {
        this.objectDbid = objectDbid;
        this.objectType = objectType;
        this.accessMask = accessMask;
    }

    public static List<PermissionDescriptor> wrap(
            final ConfStructure permisionsACL) {
        List<PermissionDescriptor> resultList =
                new ArrayList<PermissionDescriptor>();

        if (permisionsACL.getClassInfo().getCfgEnum() != CfgStructureType.CFGACL) {
            throw new IllegalArgumentException(
                    "Illegal structure type for ACL: "
                    + permisionsACL.getClassInfo().getCfgEnum());
        }

        ConfStructureCollection aces = (ConfStructureCollection)
                permisionsACL.getPropertyValue("ACEs");
        if (aces != null) {
            resultList = new ArrayList<PermissionDescriptor>(aces.size());

            for (ConfStructure ace: aces) {
                try {
                    int dbid;
                    int type;
                    int accessMask;

                    ConfStructure id = (ConfStructure) ace.getPropertyValue("ID");
                    type = (Integer) id.getPropertyValue("type");
                    dbid = (Integer) id.getPropertyValue("DBID");

                    accessMask = (Integer) ace.getPropertyValue("accessMask");

                    resultList.add(new PermissionDescriptorImpl(
                            dbid, type, accessMask));
                } catch (Exception e) {
                    log.error("Internal exception", e);
                }
            }
        }

        return resultList;
    }

    /**
     * Retrieves an instance of the object specified by the dbid and object type
     * properties of this object.
     *
     * @param service The configuration cfgService to use to retrieve the data
     * @return An object retrieved from Configuration Server
     * @throws ConfigException in case of configuration read error
     */
    public ICfgObject retrieveObject(final IConfService service)
                throws ConfigException {
        return service.retrieveObject(
                (CfgObjectType) CfgObjectType.getValue(
                        CfgObjectType.class, objectType),
                objectDbid
        );
    }

    /**
     * The access mask applicable to the specified object.
     *
     * @return access mask
     */
    public int getAccessMask() {
        return accessMask;
    }

    /**
     * The dbid of the specified object.
     *
     * @return object dbid
     */
    public int getObjectDbid() {
        return objectDbid;
    }

    /**
     * The object type of the specified object.
     *
     * @return object type
     */
    public int getObjectType() {
        return objectType;
    }

    /**
     * Returns a string representing the Configuration Server permissions of the specified
     * object.
     *
     * @return string representation of the permission description object for debug/logging purposes
     */
    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();

        output.append("PermissionDescriptor : Access Mask = (")
                .append(accessMask)
                .append("), Object Type = (")
                .append(objectType)
                .append("), Object Id = (")
                .append(objectDbid)
                .append(")");

        return output.toString();
    }
}
