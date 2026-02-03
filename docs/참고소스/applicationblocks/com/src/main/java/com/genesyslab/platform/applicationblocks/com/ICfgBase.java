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

import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBase;

import com.genesyslab.platform.commons.protocol.Endpoint;

import org.w3c.dom.Node;


/**
 * The base interface for all configuration server objects.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public interface ICfgBase {

    /**
     * A general method for obtaining the value of an arbitrary property of the object.
     *
     * @param propertyName the property name.
     * @return the property value.
     */
    Object getProperty(String propertyName);

    /**
     * A general method for setting value of an arbitrary property of the object.
     *
     * @param propertyName the property name.
     * @param value the property value.
     */
    void setProperty(String propertyName, Object value);

    /**
     * Returns reference to the underlying configuration protocol object structure.
     *
     * <p/><b><i>Note:</i></b> It is designed for internal usage.<br/>
     * Any modifications in the underlying structures may bring inconsistency
     * with covering COM AB classes.
     *
     * @return original configuration protocol object instance reference.
     */
    public ConfObjectBase getRawObjectData();

    /**
     * Provides an XML representation of the current object.
     *
     * @return an XML "snapshot" of the object.
     */
    Node toXml();

    /**
     * Provides the location of the server to which this object belongs.
     *
     * @return configuration server endpoint.
     */
    Endpoint getEndpoint();
}
