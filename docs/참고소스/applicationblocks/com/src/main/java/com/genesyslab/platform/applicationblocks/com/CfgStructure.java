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

import com.genesyslab.platform.configuration.protocol.obj.ConfStructure;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBase;
import com.genesyslab.platform.configuration.protocol.xml.ConfDataSaxHandler;

import com.genesyslab.platform.commons.xmlfactory.XmlFactories;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.w3c.dom.Node;

import java.util.Collection;


/**
 * The base class for all Configuration Server structures.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public abstract class CfgStructure
        extends CfgBase
        implements ICfgStructure {

    /**
     * Main constructor designed for usage from generated classes in deserializing constructors.
     *
     * @param confService configuration service instance
     * @param objData bound data from Configuration SDK message
     * @param parent configuration object containing this structure
     */
    protected CfgStructure(
            final IConfService  confService,
            final ConfStructure objData,
            final ICfgObject    parent) {
        super(confService, objData,
                false,
                (CfgObject) parent);
    }

    /**
     * Main constructor designed for usage from generated classes in deserializing constructors.
     *
     * @param confService configuration service instance
     * @param xmlData bound data from Configuration SDK message
     * @param parent configuration object containing this structure
     */
    protected CfgStructure(
            final IConfService confService,
            final Node xmlData,
            final ICfgObject parent) {
        this(confService, parseDom(confService, xmlData, parent), (CfgObject) parent);
    }


    private static ConfStructure parseDom(
            final IConfService confService,
            final Node xmlData,
            final ICfgObject parent) {
        ConfDataSaxHandler handler = new ConfDataSaxHandler(confService.getMetaData());
        SAXResult saxRes = new SAXResult(handler);

        try {
            XmlFactories.newTransformer().transform(new DOMSource(xmlData), saxRes);
        } catch (Exception e) {
            throw new ConfigRuntimeException("XML parsing exception", e);
        }

        Collection<ConfObjectBase> objs = handler.getParsedData();
        if (objs != null && !objs.isEmpty()) {
            return (ConfStructure) objs.iterator().next();
        }

        throw new ConfigRuntimeException("Failed to find ConfStructure in "
                + ((xmlData == null) ? "null" : ("<" + xmlData.getNodeName() + ">")));
    }


    /**
     * Returns the structure's parent object. Should be a configuration class.
     *
     * @return Structure parent
     */
    public ICfgObject getParent() {
        return super.getParent();
    }

    @Override
    protected void updateChildrenSavedState() {
        // do nothing
    }
}
