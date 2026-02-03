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
package com.genesyslab.platform.apptemplate.filtering.impl.operands;

import java.util.List;
import java.util.regex.Pattern;

import com.genesyslab.platform.apptemplate.filtering.impl.FilterContext;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterOperand;
import com.genesyslab.platform.apptemplate.filtering.impl.ValueList;
import com.genesyslab.platform.commons.GEnum;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.runtime.DataSupport;


public class AttributeOperand extends FilterOperand {
    
    private final String[] path;
    
    public AttributeOperand(String path) throws NullPointerException {
        super();
        if (path == null) {
            throw new NullPointerException("path argument is null");
        }
        if (path.trim().length() == 0) {
            throw new IllegalArgumentException("path argument is empty");
        }
        this.path = path.split(Pattern.quote("."));
    }

    @Override
    public ValueList evaluate(Message message, FilterContext context) {
        ValueList result = new ValueList();
        
        evaluate(result, message.getMessageAttribute(path[0]), 1);
        
        return result;
    }

    private void evaluate(ValueList result, Object obj, int level) {
        for(int i=level; i<path.length; i++) {
            String id = path[level];
            Object child;
            if (obj instanceof DataSupport) {
                final DataSupport dataSupport = (DataSupport)obj;
                child = dataSupport.attributes().get(id);
                if (child == null) {
                    child = dataSupport.getCompounds().get(id); 
                }
            }
            else if (obj instanceof KeyValueCollection) {
                child = ((KeyValueCollection)obj).getPair(id).getValue();
            }
            else if (obj instanceof List) {
                for(Object it : (List<?>) obj) {
                    evaluate(result, it, level+1);
                }
                return;
            }
            else {
                return;
            }
            if (child == null) {
                return;
            }
            obj = child;
            level++;
        }
        
        if (obj instanceof String) {
            result.put((String)obj);
        }
        else if (obj instanceof Integer) {
            result.put((Integer)obj);
        }
        else if (obj instanceof GEnum) {
            result.put((GEnum)obj);
        }
    }

    @Override
    public Object clone() {
        AttributeOperand obj = (AttributeOperand) super.clone();
        return obj;
    }
}
