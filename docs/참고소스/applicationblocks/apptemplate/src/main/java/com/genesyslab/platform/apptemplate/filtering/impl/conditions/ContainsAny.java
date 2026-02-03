package com.genesyslab.platform.apptemplate.filtering.impl.conditions;

import com.genesyslab.platform.apptemplate.filtering.impl.FilterCondition;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterContext;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterOperand;
import com.genesyslab.platform.commons.protocol.Message;

public class ContainsAny extends FilterCondition {

    public ContainsAny(String key, FilterOperand op1,
            FilterOperand op2, boolean negative) {
        super(key, op1, op2, negative);
    }

    /**
     * Evaluate condition for specified message and context.
     * @param message message for filtering.
     * @param context which contans filter variables. 
     * @return true if condition completed sucessfully.
     */
    @Override
    public boolean evaluate(Message message, FilterContext context) {
        return isNegative() 
                ^ getOp1().evaluate(message, context).containsAny(getOp2().evaluate(message, context));
    }

    
    @Override
    public Object clone() {
        ContainsAny obj = (ContainsAny) super.clone();
        return obj;
    }    
}
