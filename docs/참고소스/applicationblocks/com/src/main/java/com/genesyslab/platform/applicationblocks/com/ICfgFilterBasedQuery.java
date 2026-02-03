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
 * Each filter-based query object should implement this general interface.
 */
public interface ICfgFilterBasedQuery<TT extends ICfgObject> extends ICfgQuery<TT> {

    /**
     * Set the new value of the filter key-value pair.
     *
     * @param name Filter key
     * @param newValue New filter value
     */
    void setProperty(String name, Object newValue);

    /**
     * Retrieves the value of the filter key-value pair.
     *
     * @param name Filter key
     * @return filter value
     */
    Object getProperty(String name);

    /**
     * Retrieves the value of the filter key-value pair as integer value.
     *
     * @param name Filter key
     * @return filter value
     */
    int getInt(String name);

    /**
     * Retrieves the value of the filter key-value pair as string value.
     *
     * @param name Filter key
     * @return filter value
     */
    String getString(String name);

    /**
     * The Configuration Server object type that the query corresponds to.
     *
     * @return object type
     */
    CfgObjectType getCfgObjectType();

    /**
     * Returns map with filter properties set.
     *
     * @return filter map.
     * @see #getExtraFilter()
     */
    Hashtable<String, Object> getFilter();

    /**
     * Returns additional request filter properties for server object(s) read request.
     *
     * @return extra filter map.
     */
    Hashtable<String, Object> getExtraFilter();

    /**
     * Sets filter option for notification of configuration server about need
     * of the object path information to be collected and sent with the main objects data.
     *
     * <p/>If this option value is <code>true</code>, ConfService will add filter
     * parameter "object_path" to the read request.<br/>
     * After the configuration object(s) are read, the resulting object path value(s) will be accessible as
     * {@link CfgObject#getObjectPath()}.
     *
     * <p/>It is enabled by default.
     *
     * @param value the object path request enabling flag value.
     * @see CfgObject#getObjectPath()
     */
    public void setDoRequestObjectPath(boolean value);

    /**
     * Gets filter option for notification of configuration server about need
     * of the object path information to be collected and sent with the main objects data.
     *
     * <p/>If this option value is <code>true</code>, ConfService will add filter
     * parameter "object_path" to the read request.<br/>
     * After the configuration object(s) are read, the resulting object path value(s) will be accessible as
     * {@link CfgObject#getObjectPath()}.
     *
     * @return the object path request enabling flag value.
     * @see CfgObject#getObjectPath()
     */
    public boolean getDoRequestObjectPath();

    /**
     * Sets filter option for notification of configuration server about need
     * of the folder DBID information to be sent with the main objects data.
     *
     * <p/>If this option value is <code>true</code>, ConfService will add filter
     * parameter "read_folder_dbid" to the read request.<br/>
     * After the configuration object(s) are read, the resulting folder DBID value(s) will be accessible as
     * {@link CfgObject#getFolderId()}.
     *
     * <p/>It is enabled by default.
     *
     * @param value the folder DBID request enabling flag value.
     * @see CfgObject#getFolderId()
     */
    public void setDoRequestFolderId(boolean value);

    /**
     * Gets filter option for notification of configuration server about need
     * of the folder DBID information to be sent with the main objects data.
     *
     * <p/>If this option value is <code>true</code>, ConfService will add filter
     * parameter "read_folder_dbid" to the read request.<br/>
     * After the configuration object(s) are read, the resulting folder DBID value(s) will be accessible as
     * {@link CfgObject#getFolderId()}.
     *
     * @see CfgObject#getFolderId()
     */
    public boolean getDoRequestFolderId();
}
