package com.genesyslab.platform.apptemplate.filtering.impl.configuration;

import com.genesyslab.platform.apptemplate.filtering.impl.FilterGroup;
import com.genesyslab.platform.apptemplate.filtering.impl.conditions.ContainsAny;
import com.genesyslab.platform.apptemplate.filtering.impl.filters.DefaultFilter;
import com.genesyslab.platform.apptemplate.filtering.impl.operands.AttributeOperand;
import com.genesyslab.platform.apptemplate.filtering.impl.operands.ConstantOperand;
import com.genesyslab.platform.apptemplate.filtering.impl.operands.MessageTypeOperand;


/**
 * Creates condition {@link ContainsAny} with message attribute {@link AttributeOperand}
 * or {@link MessageTypeOperand}
 * and constant value {@link ConstantOperand}. Condition is stored in subfilter of the
 * {@link FilterGroup}.
 * <br>
 * Options sample:
 * <pre><code>
 *  &#64;UserData.AgentName : ag100
 * </code></pre>
 *  Result {@link FilterGroup} structure:
 *  <pre><code>
 *      /FilterGroup
 *          /filter: default-filter
 *              /condition: check UserData attribute
 *  </code></pre>
 */
public class ConditionParser extends FilterConfigurationParser {

    public ConditionParser() {
        super();
    }

    public ConditionParser(ConstantValueParser constantParser) {
        super(constantParser);
    }

    @Override
    public void apply(FilterGroup filter, String subfilter, String key, String value) {
        if (subfilter == null || subfilter.length() == 0) {
            subfilter = DefaultFilterFactory.default_filter;
        }
        DefaultFilter df = findOrRegisterNew(filter, subfilter);

        boolean isNegative = false;        
        if (key.startsWith(DefaultFilterFactory.inverse_key)) {
            isNegative = true;
        }

        ConstantOperand constant = new ConstantOperand(constantParser.parse(value.trim()));

        int index;
        ContainsAny condition;
        if ((index = key.indexOf(DefaultFilterFactory.attribute_key)) >= 0) {
            AttributeOperand attributeOperand = new AttributeOperand(key.substring(
                    index + DefaultFilterFactory.attribute_key.length()).trim());
            condition = new ContainsAny(key, attributeOperand, constant, isNegative);
            FilterConfigurationParser.putCondition(df, condition);
        } else if (key.indexOf(DefaultFilterFactory.message_name_key) >= 0) {
            condition = new ContainsAny(key, new MessageTypeOperand(), constant, isNegative);
            if (df.getConditions().size() > 0) {
                FilterConfigurationParser.setCondition(df, condition, 0);
            } else {
                FilterConfigurationParser.putCondition(df, condition);
            }
        }
    }

    @Override
    public void remove(FilterGroup filter, String subfilter, String key) {
        if (subfilter == null || subfilter.length() == 0) {
            subfilter = DefaultFilterFactory.default_filter;
        }
        DefaultFilter df = find(filter, subfilter);
        if (df != null) {
            FilterConfigurationParser.removeCondition(df, key);
        }
    }
}
