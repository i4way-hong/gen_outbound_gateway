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

import com.genesyslab.platform.apptemplate.filtering.impl.ValueList;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterContext;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterOperand;

import com.genesyslab.platform.commons.protocol.Message;


public class VariableOperand extends FilterOperand {

    private final String id;

    public VariableOperand(final String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    @Override
    public ValueList evaluate(Message message, FilterContext context) {
        return context.getVariable(id);
    }

    @Override
    public Object clone() {
        VariableOperand obj = (VariableOperand) super.clone();
        return obj;
    }

    @Override
    public void register(FilterContext context) {
        super.register(context);
        context.registerVariable(id);
    }

    @Override
    public void unregister(FilterContext context) {
        context.unregisterVariable(id);
        super.unregister(context);
    }
}
