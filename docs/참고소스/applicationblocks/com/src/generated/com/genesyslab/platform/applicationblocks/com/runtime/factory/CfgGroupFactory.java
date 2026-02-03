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

/* ATTENTION! DO NOT MODIFY THIS FILE - IT IS AUTOMATICALLY GENERATED! */

package com.genesyslab.platform.applicationblocks.com.runtime.factory;

import com.genesyslab.platform.applicationblocks.com.objects.CfgGroup;

import com.genesyslab.platform.applicationblocks.com.CfgBase;
import com.genesyslab.platform.applicationblocks.com.ICfgObject;
import com.genesyslab.platform.applicationblocks.com.IConfService;

import com.genesyslab.platform.applicationblocks.com.runtime.ICfgObjectFactory;

import com.genesyslab.platform.configuration.protocol.obj.*;

import javax.annotation.Generated;

import org.w3c.dom.Node;


/**
 * Internal objects creation factory for CfgGroup type.
 *
 * <p/><b><i>Note:</i></b> It is designed for COM AB internal use only.<br/>
 * It may be a subject of structural and functional changes without notice.
 */
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public final class CfgGroupFactory
        implements ICfgObjectFactory {

    public Class<CfgGroup> getObjectType() {
        return CfgGroup.class;
    }

    public CfgBase create(
            IConfService   confService,
            ConfObjectBase objData,
            Object[]       additionalParams) {
        if ((additionalParams == null) || (additionalParams.length < 1)
            || !(additionalParams[0] instanceof ICfgObject)) {
            throw new IllegalArgumentException("Object Creator: invalid parameters");
        }

        return new CfgGroup(
                   confService, (ConfStructure) objData,
                   (ICfgObject) additionalParams[0]);
    }

    public CfgBase create(
            final IConfService confService,
            final Node         xmlData,
            final Object[]     additionalParams) {
        if ((additionalParams == null) || (additionalParams.length < 1)
            || !(additionalParams[0] instanceof ICfgObject)) {
            throw new IllegalArgumentException("Object Creator: invalid parameters");
        }

        return new CfgGroup(
                   confService,
                   xmlData,
                   (ICfgObject) additionalParams[0]);
    }
}
