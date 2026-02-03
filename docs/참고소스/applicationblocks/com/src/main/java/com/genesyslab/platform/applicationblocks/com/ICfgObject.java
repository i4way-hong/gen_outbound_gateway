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


/**
 * An interface that should be supported by all configuration classes (objects that can be independently
 * retrieved from Config Server).
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public interface ICfgObject
        extends ICfgBase,
                Cloneable {

    /**
     * Get this configuration object type.
     *
     * @return Configuration object type
     */
    CfgObjectType getObjectType();

    /**
     * Returns the dbid of the current object, or 0 if object has not been saved.
     *
     * @return Configuration object dbid
     */
    int getObjectDbid();

    /**
     * Synchronizes changes in a class with Configuration Server.
     * Internally generates the correct delta object and sends it to Configuration Server.
     *
     * @throws ConfigException in case of protocol level exception, data transformation,
     *                         or server side constraints
     */
    void save() throws ConfigException;

    /**
     * Updates the current object with the latest state from the configuration server.
     *
     * @throws ConfigException in case of protocol level exception or data transformation
     */
    void refresh() throws ConfigException;

    /**
     * Deletes the current object from the configuration.
     *
     * @throws ConfigException in case of protocol level exception or server side constraints
     */
    void delete() throws ConfigException;

    /**
     * Updates the current object from the passed delta object.
     *
     * @param deltaObject the delta object representing the changes to be made
     */
    void update(ICfgDelta deltaObject);

    /**
     * Creates and returns a copy of this object.
     *
     * @return     a clone of this instance.
     * @exception  CloneNotSupportedException  subclasses that override
     *               the <code>clone</code> method can throw this exception
     *               to indicate that an instance cannot be cloned.
     * @see java.lang.Cloneable
     */
    Object clone() throws CloneNotSupportedException;
}
