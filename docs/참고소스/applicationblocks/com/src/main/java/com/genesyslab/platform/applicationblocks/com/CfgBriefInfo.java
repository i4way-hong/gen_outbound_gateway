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

import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBrief;

import org.w3c.dom.Node;


/**
 * The base class for all Configuration Server brief info structures.
 */
public abstract class CfgBriefInfo
        extends CfgObject
        implements ICfgBriefInfo {

    /**
     * Main constructor designed for usage from generated classes in deserializing constructors.
     *
     * @param confService configuration service instance
     * @param objData configuration protocol data
     * @param additionalParameters additional attributes
     */
    protected CfgBriefInfo(
            final IConfService confService,
            final ConfObjectBrief objData,
            final Object[] additionalParameters) {
        super(confService, objData, true, additionalParameters);
    }

    /**
     * Main constructor designed for usage from generated classes in deserializing constructors.
     *
     * @param confService configuration service instance
     * @param xmlData bound data from Configuration SDK message
     * @param additionalParameters additional attributes
     */
    protected CfgBriefInfo(
            final IConfService confService,
            final Node xmlData,
            final Object[] additionalParameters) {
        super(confService, xmlData, additionalParameters);
    }


    @Override
    protected void ensureDataBackup() {
        // do nothing
    }


    /**
     * Brief Info structures do not have "parent" containers.
     *
     * @return null
     */
    protected ICfgObject getParent() {
        return null;
    }

    /**
     * Read "full" version of the configuration object from the server.
     *
     * @return configuration object
     * @throws ConfigException in case of problem while object reading
     */
    public final ICfgObject retrieveObject() throws ConfigException {
        return getConfigurationService().retrieveObject(
                getObjectType(), getObjectDbid());
    }

    @Override
    protected void updateChildrenSavedState() {
        // do nothing
    }

    @Override
    public void save()
            throws ConfigException {
        throw new ConfigException("BriefInfo structures can't be saved");
    }

    @Override
    public void update(final ICfgDelta deltaObject) {
        throw new ConfigRuntimeException("BriefInfo structures can't be updated with delta");
    }
}
