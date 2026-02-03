// ===============================================================================
//  Genesys Platform SDK Application Blocks
// ===============================================================================
//
//  Any authorized distribution of any copy of this code (including any related
//  documentation) must reproduce the following restrictions, disclaimer and copyright
//  notice:
//
//  The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
//  (even as a part of another name), endorse and/or promote products derived from
//  this code without prior written permission from Genesys Telecommunications
//  Laboratories, Inc.
//
//  The use, copy, and/or distribution of this code is subject to the terms of the Genesys
//  Developer License Agreement.  This code shall not be used, copied, and/or
//  distributed under any other license agreement.
//
//  THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
//  ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
//  DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
//  WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
//  NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
//  NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
//  EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
//  CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
//  NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
//  Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.apptemplate.util;

import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.net.InetAddress;


public class ConfigurationUtil {

    private static final String PARAM_SEPARATOR = ";";

    /**
     * Searches transportParameters for an occurrence of specified parameter name, returns parameter value.
     * @param transportParameters    Semicolon-separated string of parameter pairs "name=value"
     * @param paramName              Name of the parameter
     * @return Parameter value (may be empty string) or null if parameter name is not found
     */
    public static String findTransportParameter(
            final String transportParameters,
            final String paramName) {
        if ((null == transportParameters) || (transportParameters.trim().length() == 0)) {
            return null;
        }
        if (null == paramName) {
            throw new IllegalArgumentException("paramName must not be null");
        }
        String[] params = transportParameters.split(PARAM_SEPARATOR);
        final String prefix = paramName + "=";
        for (String param : params) {
            if (param.startsWith(prefix)) {
                return param.substring(prefix.length());
            }
        }
        return null;
    }

    /**
     * Searches applicationtParameters for an occurrence of specified parameter name, returns parameter value.
     * Truncates spaces before and after pair. 
     * @param transportParameters    Semicolon-separated string of parameter pairs "name=value"
     * @param paramName              Name of the parameter
     * @return Parameter value (may be empty string) or null if parameter name is not found
     */
    public static String findAppParameter(
            final String applicationtParameters,
            final String paramName) {
        if ((null == applicationtParameters) || (applicationtParameters.trim().length() == 0)) {
            return null;
        }
        if (null == paramName) {
            throw new IllegalArgumentException("paramName must not be null");
        }
        String[] params = applicationtParameters.split(PARAM_SEPARATOR);
        for(int i=0; i<params.length; i++) {
        	params[i] = params[i].replaceAll("\n", "").replaceAll("\r", "").trim();
        }
        final String prefix = paramName + "=";
        for (String param : params) {
            if (param.startsWith(prefix)) {
                return param.substring(prefix.length());
            }
        }
        return null;
    }

    public static boolean isTrue(final String value) {
        return "1".equals(value) || "on".equalsIgnoreCase(value)
                || "yes".equalsIgnoreCase(value)
                || "true".equalsIgnoreCase(value);
    }

    public static boolean isFalse(final String value) {
        return "0".equals(value) || "off".equalsIgnoreCase(value)
                || "no".equalsIgnoreCase(value)
                || "false".equalsIgnoreCase(value);
    }


    private static volatile String LOCALHOST_NAME = null;

    public static String getLocalhostName() {
        if (LOCALHOST_NAME == null) {
            synchronized (ConfigurationUtil.class) {
                if (LOCALHOST_NAME == null) {
                    String lhname;
                    try {
                        lhname = InetAddress.getLocalHost().getHostName();
                    } catch (final Exception ex) {
                        lhname = "localhost";
                    }
                    LOCALHOST_NAME = lhname;
                }
            }
        }
        return LOCALHOST_NAME;
    }

    public static KeyValueCollection deepKVListClone(final KeyValueCollection list) {
        if (list != null) {
            try {
                final KeyValueCollection ret = (KeyValueCollection) list.clone();
                for (final Object op: ret) {
                    final KeyValuePair pair = (KeyValuePair) op;
                    final Object val = pair.getValue();
                    if (val instanceof KeyValueCollection) {
                        pair.setTKVValue(deepKVListClone((KeyValueCollection) val));
                    }
                }
                return ret;
            } catch (final CloneNotSupportedException ex) { /* unexpected */ }
        }
        return list;
    }
}
