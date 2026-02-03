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
package com.genesyslab.platform.apptemplate.filtering.impl.configuration;

import com.genesyslab.platform.apptemplate.filtering.impl.ValueList;

import java.util.regex.Pattern;


/**
 * Parses filter option and creates list of constant values
 * {@link com.genesyslab.platform.apptemplate.filtering.impl.ValueList ValueList}
 * that can be used in filter conditions
 * {@link com.genesyslab.platform.apptemplate.filtering.impl.FilterCondition FilterCondition} or filter actions
 * {@link com.genesyslab.platform.apptemplate.filtering.impl.FilterAction FilterAction}.<br/>
 * For example:
 * <pre><code>
 *  &#64;AddresType : Position, Extension, Routing Point
 * </code></pre>
 * The ConstantValueParser will create list of string constants <em>Position, Extension, Routing Point</em> 
 */
public class ConstantValueParser {

    /**
     * Converts string constants to the  {@link ValueList} objects<br>
     * Possible wildcards: "Event*" - means any string with "Event" prefix. <br>
     * The "5???" means any 4-symbol string starting with "5".<br> 
     * @param value One or more string constants, delimited with ',' symbol.
     * @return parsed constants
     */
    public ValueList parse(String value) {

        ValueList list = new ValueList();
        if (value == null) {
            return list;
        }

        String[] items = value.split(Pattern.quote(","));

        for (int i = 0; i < items.length; i++) {
            String item = items[i].trim();
            parse(item, list);                
        }
        return list;
    }


    private void parse(String value, ValueList list) {
        boolean isPattern = false;
        boolean patternEscaped = false;

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);

            if ((value.charAt(i) == '\\') && !patternEscaped) {
                patternEscaped = true;
                continue;
            }

            if (patternEscaped) {
                result.append(c);
                patternEscaped = false;
            } else if (c == '?') {
                result.append(".");
                isPattern = true;
            } else if (c == '*') {
                result.append(".*");
                isPattern = true;
            } else {
                result.append(c);
            }
        }

        if (isPattern) {
            list.put(Pattern.compile(result.toString()));
        }
        else {
            list.put(result.toString());
        }
    }
}
