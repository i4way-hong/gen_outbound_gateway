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

import com.genesyslab.platform.commons.GEnum;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;

/**
 * Provides utility methods for enumerations.
 */
public class EnumFactory {

    /**
     * Returns the GEnum value described by the String 'value' parameter.
     *
     * @param classFullName the full name of the enumeration class
     * @param value the <code>String</code> representation of the enumeration value
     * @return the GEnum value described by the value parameter
     * @throws ClassNotFoundException
     *             if the class specified by the <code>classFullName</code>
     *             parameter doesn't exist in the class path
     */
    public static GEnum getGEnumValue(
            final String classFullName,
            final String value)
                throws ClassNotFoundException {
        Class<?> enumClass = Class.forName(classFullName);
        return GEnum.getValue(enumClass, value);
    }

    /**
     * Returns the enumeration value described by the String 'value' parameter.
     *
     * @param <T> the type of the enumeration
     * @param classFullName the full name of the enumeration class
     * @param value the <code>String</code> representation of the enumeration value
     * @return the enumeration value
     * @throws ClassNotFoundException
     *             if the class specified by the <code>classFullName</code>
     *             parameter doesn't exist in the class path
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T getEnumValue(
            final String classFullName,
            final String value)
                throws ClassNotFoundException {
        Class<T> enumClass = (Class<T>) Class.forName(classFullName);
        return Enum.valueOf(enumClass, value);
    }

    public static Boolean CfgFlag2Boolean(final CfgFlag flag) {
        if (flag == CfgFlag.CFGTrue) {
            return Boolean.TRUE;
        } else if (flag == CfgFlag.CFGFalse) {
            return Boolean.FALSE;
        }
        return null;
    }
}
