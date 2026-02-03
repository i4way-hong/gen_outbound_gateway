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

import com.genesyslab.platform.configuration.protocol.metadata.CfgTypeMask;
import com.genesyslab.platform.configuration.protocol.metadata.CfgOperation;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionClass;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionStructure;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionAttribute;
import com.genesyslab.platform.configuration.protocol.metadata.ICfgClassOperationalInfo;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionObjectDelta;

import com.genesyslab.platform.configuration.protocol.obj.ConfStructure;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBase;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectDelta;

import com.genesyslab.platform.configuration.protocol.xml.ConfDataSaxHandler;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectProperty;

import com.genesyslab.platform.commons.xmlfactory.XmlFactories;

import org.w3c.dom.Node;

import java.util.Calendar;
import java.util.Collection;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;


/**
 * This is a base class for all Configuration Server delta objects.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public abstract class CfgDelta
        extends CfgObject
        implements ICfgDelta {

    /**
     * This is a constructor of an object with no external data.
     *
     * @param confService A reference to Configuration Service
     * @param className The name of a class
     */
    protected CfgDelta(
            final IConfService confService,
            final String className) {
        super(confService, createBaseClass(confService, className),
                false, null);
    }

    protected CfgDelta(
            final IConfService srvc,
            final ConfObjectBase objData,
            final Object[] additionalParameters) {
        super(srvc, objData, true, additionalParameters);
    }

    protected CfgDelta(
            final IConfService srvc,
            final Node xmlData,
            final Object[] additionalParameters) {
        super(srvc, parseDom(srvc, xmlData, null), true, additionalParameters);
    }


    private static ConfObjectDelta parseDom(
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
            return (ConfObjectDelta) objs.iterator().next();
        }


        throw new ConfigRuntimeException("Failed to find ConfObjectDelta in "
                + ((xmlData == null) ? "null" : ("<" + xmlData.getNodeName() + ">")));
    }

    private static ConfObjectBase createBaseClass(
            final IConfService confService,
            final String className) {
        CfgDescriptionClass info = confService.getMetaData().getCfgClass(className);
        if (info != null) {
            if (info.isCfgType(CfgTypeMask.Structure)) {
                return new ConfStructure((CfgDescriptionStructure) info);
            } else {
                return new ConfObjectDelta((CfgDescriptionObjectDelta) info);
            }
        }

        throw new ConfigRuntimeException(
                "Failed to create delta structure with name " + className);
    }


    @Override
    protected void ensureDataBackup() {
        // do nothing
    }

    /**
     * Read configuration object from server.
     *
     * @return configuration object
     * @throws ConfigException in case of problem while object reading
     */
    public final ICfgObject retrieveObject() throws ConfigException {
        return getConfigurationService().retrieveObject(
                getObjectType(), getObjectDbid());
    }


    @Override
    public void save()
            throws ConfigException {
        throw new ConfigException("Delta objects can't be saved");
    }

    @Override
    public void update(final ICfgDelta deltaObject) {
        throw new ConfigRuntimeException("Delta objects can't be updated with delta");
    }


    @Override
    protected StringBuilder toStringProperties() {
        StringBuilder propertiesOutput = super.toStringProperties();

        CfgDescriptionClass base = null;
        if ((getMetaData().isCfgType(CfgTypeMask.Object) || getMetaData().isCfgType(CfgTypeMask.Group))
                && (getMetaData() instanceof ICfgClassOperationalInfo)) {
            base = ((ICfgClassOperationalInfo) getMetaData()).getSubjectClassDescription();
        }
        if (base != null) {
            for (CfgDescriptionAttribute info : base.getAttributes()) {
                String key = info.getSchemaName();
                Object val = getProperty(null, key, false);

                if (info.getCfgEnum() == CfgObjectProperty.CFGPassword) {
                    val = "[output suppressed]";
                } else if (val instanceof Calendar) {
                    val = java.text.DateFormat.getDateTimeInstance(
                              java.text.DateFormat.LONG, java.text.DateFormat.LONG)
                          .format(((Calendar) val).getTime());
                }

                if (val != null) {
                    if (info.getOperation() == CfgOperation.Add) {
                        propertiesOutput.append("added")
                                .append(Character.toUpperCase(key.charAt(0)))
                                .append(key.substring(1));
                    } else {
                        propertiesOutput.append(key);
                    }
                    propertiesOutput.append(" : ");
                    propertiesOutput.append(val);
                    propertiesOutput.append("\n");
                }
            }
        }

        return propertiesOutput;
    }
}
