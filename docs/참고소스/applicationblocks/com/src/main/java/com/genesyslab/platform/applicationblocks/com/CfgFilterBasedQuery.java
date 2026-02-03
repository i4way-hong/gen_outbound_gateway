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

import com.genesyslab.platform.applicationblocks.com.runtime.MiscConstants;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.commons.GEnum;
import java.util.Hashtable;


/**
 * A general class that can hold arbitrary values of filter keys and values.<br/>
 * It contains a collection of
 * filter key-value pairs. If you have a need to work with multiple queries for objects, type of which
 * is not known in compile-time, use this class.<br/>
 * If you know the type of the objects, please, use more
 * specific <code>Cfg<i>***</i>Query</code> classes.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public class CfgFilterBasedQuery<TT extends ICfgObject>
        extends CfgQuery<TT>
        implements ICfgFilterBasedQuery<TT>, ICfgQueryObjectClass<TT> {

    private CfgObjectType               objectType;
    private Class<TT> objectClass;

    private final Hashtable<String, Object> filter = new Hashtable<String, Object>();
    private final Hashtable<String, Object> extraFilter = new Hashtable<String, Object>();

    /**
     * Creates a new instance of the class.
     *
     * @param objectType type of object returned by this query.
     * @param confService reference to IConfService to be used for query execution.
     * @deprecated
     */
    @Deprecated
    public CfgFilterBasedQuery(
            final CfgObjectType objectType,
            final IConfService confService) {
        super(confService);
        this.objectType = objectType;

        this.extraFilter.put(MiscConstants.ReadFolderInfoFilterName, 0);
        this.extraFilter.put(MiscConstants.ReadObjectPathFilterName, 0);
    }

    /**
     * Creates a new instance of the class.
     *
     * @param objType configuration server object type to read.
     */
    public CfgFilterBasedQuery(final CfgObjectType objType) {
        this(objType, null);
    }


    protected void setObjectClass(final Class<TT> clazz) {
        this.objectClass = clazz;
    }


    public void setDoRequestObjectPath(final boolean value) {
        if (value) {
            extraFilter.put(MiscConstants.ReadObjectPathFilterName, 0);
        } else {
            extraFilter.remove(MiscConstants.ReadObjectPathFilterName);
        }
    }

    public boolean getDoRequestObjectPath() {
        return extraFilter.get(MiscConstants.ReadObjectPathFilterName) != null;
    }

    public void setDoRequestFolderId(final boolean value) {
        if (value) {
            extraFilter.put(MiscConstants.ReadFolderInfoFilterName, 0);
        } else {
            extraFilter.remove(MiscConstants.ReadFolderInfoFilterName);
        }
    }

    public boolean getDoRequestFolderId() {
        return extraFilter.get(MiscConstants.ReadFolderInfoFilterName) != null;
    }

    /**
     * Set the new value of the filter key-value pair.
     *
     * @param name Filter key
     * @param newValue New filter value
     */
    public void setProperty(final String name, final Object newValue) {
        if (newValue == null) {
            filter.remove(name);
        } else {
            filter.put(name, newValue);
        }
    }

    /**
     * Retrieves the value of the filter key-value pair.
     *
     * @param name Filter key
     * @return filter value
     */
    public Object getProperty(final String name) {
        return filter.get(name);
    }

    public int getInt(final String name) {
        Object obj = getProperty(name);

        if (obj == null) {
            return 0;
        }

        if (obj instanceof Integer) {
            return (Integer) obj;
        }

        if (obj instanceof GEnum) {
            return ((GEnum) obj).ordinal();
        }

        return Integer.parseInt(obj.toString());
    }

    public String getString(final String name) {
        Object obj = getProperty(name);

        if (obj == null) {
            return null;
        }

        return obj.toString();
    }

    public CfgObjectType getCfgObjectType() {
        return this.objectType;
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public <T extends ICfgObject> Class<T> getCfgObjectClass() {
        return (Class<T>) this.objectClass;
    }
    
    public Class<TT> getQueryObjectClass() {
        return objectClass;
    }
    

    public Hashtable<String, Object> getFilter() {
        return this.filter;
    }

    public Hashtable<String, Object> getExtraFilter() {
        return this.extraFilter;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append("Query object type: ").append(objectType).append("\n");
        res.append("Query filters:\n");

        for (String key : filter.keySet()) {
            res.append("  Key: ").append(key)
                    .append(", Value: ").append(filter.get(key))
                    .append(";");
        }

        for (String key : extraFilter.keySet()) {
            res.append("  ExtraKey: ").append(key)
                    .append(", Value: ").append(extraFilter.get(key))
                    .append(";");
        }

        return res.toString();
    }

    @Override
    public int hashCode() {
        int code = getClass().hashCode();
        if (objectType!=null)
          code ^= 13*objectType.hashCode();
        code ^= 17* CfgBase.calcMapHashCode(this.filter);
        code ^= 19* CfgBase.calcMapHashCode(this.extraFilter);
        return code;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        final CfgFilterBasedQuery<?> query = (CfgFilterBasedQuery<?>) obj;
        if (objectType != null && query.objectType != null) {
            if (!objectType.equals(query.objectType)) {
                return false;
            }
        } else if (objectType == null || query.objectType == null) {
            return false;
        }
        if (!CfgBase.compareMap(filter, query.filter)) {
            return false;
        }
        if (!CfgBase.compareMap(extraFilter, query.extraFilter)) {
            return false;
        }
        return true;
    }
}
