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

import com.genesyslab.platform.applicationblocks.com.CfgBase;
import com.genesyslab.platform.applicationblocks.com.ICfgObject;
import com.genesyslab.platform.applicationblocks.com.ICfgStructure;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.ConfigRuntimeException;

import com.genesyslab.platform.configuration.protocol.metadata.CfgTypeMask;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionObject;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionAttribute;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionAttributeReference;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBase;
import com.genesyslab.platform.configuration.protocol.obj.ConfIntegerCollection;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import java.util.List;
import java.util.ArrayList;


/**
 * Internal helper class for manipulations with configuration objects structures and
 * correspondent COM AB wrappers.
 *
 * <p/><b><i>Note:</i></b> It is designed for COM AB internal use only.<br/>
 * It may be a subject of structural and functional changes without notice.
 */
public class PropertiesAccessHelper {

    private static final ILogger log = Log.getLogger(PropertiesAccessHelper.class);


    public static ICfgObject resolveLink(
            final IConfService confService,
            final CfgObjectType objectType,
            final int dbid) throws ConfigException {
        ICfgObject obj = null;

        if (confService.getPolicy()
                .getAttemptLinkResolutionThroughCache(objectType)) {
            if (log.isDebug()) {
                log.debugFormat(
                        "Resolving link through cache... [{0}], dbid: [{1}]",
                        new Object[] {objectType, dbid});
            }
            obj = confService.getCache()
                    .retrieve(ICfgObject.class, objectType, dbid);
        }

        if (obj == null) {
            if (log.isDebug()) {
                log.debugFormat(
                        "Resolving link through configuration server... "
                                + "[{0}], dbid: [{1}]",
                        new Object[] {objectType, dbid});
            }
            try {
                obj = confService.retrieveObject(
                        objectType, dbid);
            } catch (ConfigException ex) {
                if (log.isError()) {
                    log.error("Exception on linked object retrieve", ex);
                }
                throw new ConfigRuntimeException(
                        "Exception on linked object retrieve", ex);
            }
        }

        return obj;
    }

    public static List<?> resolveLinksList(
            final CfgBase cfgBase,
            final ConfObjectBase confStruct,
            final CfgDescriptionAttribute attrInfo) {
        if (!attrInfo.isCfgType(CfgTypeMask.List)
                || !attrInfo.isCfgType(CfgTypeMask.Link)) {
            throw new IllegalArgumentException(
                    "The property " + attrInfo.getName()
                            + " is not a link list type.");
        }

        if (((CfgDescriptionAttributeReference) attrInfo)
                .getCfgClass().isCfgType(CfgTypeMask.Structure)) {
            ConfIntegerCollection ids = (ConfIntegerCollection)
                    confStruct.getPropertyValue(attrInfo.getIndex());
            if (ids != null) {
                return new CfgStructListWrapper<ICfgStructure>(confStruct, attrInfo, cfgBase);
            }
            return null;
        }

        List<ICfgObject> links = null;
        IConfService confService = cfgBase.getConfigurationService();

        ConfIntegerCollection dbids = (ConfIntegerCollection) confStruct
                .getPropertyValue(attrInfo.getIndex());
        if (dbids != null) {
            CfgObjectType objectType = ((CfgDescriptionObject)
                    ((CfgDescriptionAttributeReference) attrInfo).getCfgClass()).getCfgEnum();
            links = new ArrayList<ICfgObject>();

            for (Integer dbid: dbids) {
                try {
                    ICfgObject obj = PropertiesAccessHelper.resolveLink(confService, objectType, dbid);
                    if (obj != null) {
                        links.add(obj);
                    }
                } catch (ConfigException e) {
                    log.error("Exception getting linked object", e);
                    throw new ConfigRuntimeException(
                            "Exception getting linked object", e);
                }
            }
        }

        return links;
    }
}
