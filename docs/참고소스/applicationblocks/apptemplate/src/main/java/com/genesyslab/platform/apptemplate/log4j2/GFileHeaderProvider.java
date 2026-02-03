// ===============================================================================
// Genesys Platform SDK Samples
// ===============================================================================
//
// ===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:
//
// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.
//
// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.
//
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
//
// Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.apptemplate.log4j2;

import com.genesyslab.platform.apptemplate.log4j2plugin.DefaultFileHeaderProvider;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;

import com.genesyslab.platform.commons.util.PsdkVersionInfo;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;


/**
 * Extension of the default file header provider with information about available
 * PSDK components' versions.
 */
public class GFileHeaderProvider extends DefaultFileHeaderProvider {

    private static final long serialVersionUID = 7260049342483164166L;

    private IGApplicationConfiguration appConfig = null;


    /**
     * This method is used for writing version info about loaded PSDK jars to file header.
     * Override this method to change its behavior.
     *
     * @param targetBuilder The target {@link StringBuilder}
     * @param lineFormat The proposed formatting line. Current version: "{0,-35}{1}"
     */
    @Override
    protected void writeAssemblyUseInfo(
            final StringBuilder targetBuilder,
            final String lineFormat) {
        String[][] versions = PsdkVersionInfo.getJarsVersions();
        if (versions != null) {
            for (String[] version: versions) {
                targetBuilder.append(String.format(lineFormat, "Lib " + version[0] + ": ", ""));
                if (version[1] != null || version[2] != null || version[3] != null) {
                    targetBuilder.append((version[2] != null) ? version[2] : "undefined").append(',');
                    targetBuilder.append((version[1] != null) ? version[1] : "undefined").append(',');
                    if ((version[3] != null)) {
                        targetBuilder.append(version[3]);
                    }
                } else {
                    targetBuilder.append("unknown");
                }
                targetBuilder.append(LINE_SEPARATOR);
            }
        }
        versions = PsdkVersionInfo.get3rdPartiesJarsVersions();
        if (versions != null) {
            for (String[] version: versions) {
                targetBuilder.append(String.format(lineFormat, "Lib " + version[0] + ": ", ""));
                if (version[1] != null || version[2] != null || version[3] != null) {
                    targetBuilder.append((version[2] != null) ? version[2] : "undefined").append(',');
                    targetBuilder.append((version[1] != null) ? version[1] : "undefined").append(',');
                    if ((version[3] != null)) {
                        targetBuilder.append(version[3]);
                    }
                } else {
                    targetBuilder.append("unknown");
                }
                targetBuilder.append(LINE_SEPARATOR);
            }
        }
    }


    void setAppConfiguration(final IGApplicationConfiguration appConfig) {
        this.appConfig = appConfig;
        if (appConfig != null) {
            setApplicationName(appConfig.getApplicationName());
            final CfgAppType apptype = appConfig.getApplicationType();
            if (apptype != null) {
                setApplicationType(apptype.name());
            }
            final Integer dbid = appConfig.getDbid();
            if (dbid != null) {
                setApplicationId(dbid.toString());
            }
        }
    }

    protected IGApplicationConfiguration getAppConfiguration() {
        return appConfig;
    }
}
