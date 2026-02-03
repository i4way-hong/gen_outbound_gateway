package com.genesyslab.platform.apptemplate.filtering.impl.actions;

import com.genesyslab.platform.apptemplate.filtering.impl.FilterAction;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterContext;
import com.genesyslab.platform.apptemplate.filtering.impl.FilterOperand;
import com.genesyslab.platform.apptemplate.filtering.impl.ValueList;
import com.genesyslab.platform.commons.protocol.Message;

/**
 * Removes filter variable from the {@link FilterContext}.
 *
 */
public class FilterDelAction extends FilterAction {

    private final String varId;
    private FilterOperand op;
    
    public FilterDelAction(String key, String varId, FilterOperand op) {
        super(key);
        if (key == null) {
            throw new NullPointerException("varId argument is null");
        }
        if (op == null) {
            throw new NullPointerException("op argument is null");
        }
        this.varId = varId;
        this.op = op;
    }        

    /**
     * Returns variable unique name.
     */
    public String getVarId() {
    	return varId;
    }
    
    @Override
    public void execute(Message message, FilterContext context) {
        ValueList variable = context.getVariable(varId);
        if (variable == null) {
            throw new NullPointerException("variable " + varId + " doesn't exists");
        }
        variable.remove(op.evaluate(message, context));
    }

    @Override
    public void register(FilterContext context) {
        super.register(context);
        context.registerVariable(varId);
        op.register(context);
    }

    @Override
    public void unregister(FilterContext context) {
        op.unregister(context);
        context.unregisterVariable(varId);
        super.unregister(context);
    }

    @Override
    public Object clone() {
        FilterDelAction obj = (FilterDelAction) super.clone();
        obj.op = (FilterOperand) op.clone();
        return obj;
    }
}
