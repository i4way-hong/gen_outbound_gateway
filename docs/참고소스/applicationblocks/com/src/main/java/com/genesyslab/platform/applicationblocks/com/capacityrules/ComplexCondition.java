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

// Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.com.capacityrules;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.genesyslab.platform.applicationblocks.com.capacityrules.ComplexCondition.ConditionType;

class ComplexCondition extends AbstractRule {

    private final String ATTR_CONDITION_TYPE = "type";

    public static enum ConditionType { OR, AND, NOT  };

	private ConditionType Condition;
	private ArrayList<AbstractRule> ChildRules;
	
	public ComplexCondition(AbstractRule parent, ConditionType condition) {
		super(parent);
		Condition = condition;
		ChildRules = new ArrayList<AbstractRule>();
	}

    public ComplexCondition(AbstractRule parent, ConditionType condition, int capacity) {
		super(parent);
		Condition = condition;
		ChildRules = new ArrayList<AbstractRule>(capacity);
	}

    public ComplexCondition(AbstractRule parent, Element element) 
    	throws CapacityRuleException
	{
		super(parent);
		String sCondition = element.getAttribute(ATTR_CONDITION_TYPE);
		
		Exception ex = null;
		try
		{
			Condition = ConditionType.valueOf(sCondition.toUpperCase());
			if (Condition != null) {
				ChildRules = new ArrayList<AbstractRule>();
				return;
			}
		}
		catch(Exception e) {
			ex = e;
		}
		
		throw new CapacityRuleException("Capacity rule XML contains invalid attribute value. Attribute '" +
                ATTR_CONDITION_TYPE + " value ='" + Condition + "'", ex);
	}

	
	

	@SuppressWarnings("unchecked")
	@Override
	protected Object clone() throws CloneNotSupportedException {
		ComplexCondition o = (ComplexCondition) super.clone();
		o.ChildRules = new ArrayList<AbstractRule>();
		for(AbstractRule childRule : ChildRules) {
			o.ChildRules.add((AbstractRule) childRule.clone());
		}
		return o;
	}

	public ConditionType getContrCondition() {
		switch (Condition) {
			case OR:
				return ConditionType.AND;
			case AND:
				return ConditionType.OR;
		}
		return null;
	}

	public ConditionType getCondition() {
		return Condition;
	}

	
	public void setCondition(ConditionType condition) {
		Condition = condition;
	}
	
	public ArrayList<AbstractRule> getChildRules() {
		return ChildRules;
	}

	public AbstractRule addRule(AbstractRule rule) {
		if (rule == this) {
			int error = 1;
		}
		ChildRules.add(rule);
		return this;
	}

	public AbstractRule addRuleAt(AbstractRule rule, int index) {
		ChildRules.add(index, rule);
		return this;
	}

	public AbstractRule removeRule(AbstractRule rule) {
		ChildRules.remove(rule);
		return this;
	}

	public AbstractRule removeRule(int index) {
		ChildRules.remove(index);
		return this;
	}

	
	@Override
	public void assignMediaTypes(CRMediaMap map) throws CapacityRuleException {
		if (ChildRules == null) {
			return;
		}
		for(AbstractRule childRule : ChildRules) {
			childRule.assignMediaTypes(map);
		}
	}

	@Override
	public void expandAny(CRMediaMap map) throws CapacityRuleException {
		if (ChildRules == null) {
			return;
		}
		
		for(AbstractRule childRule : ChildRules.toArray(new AbstractRule[ChildRules.size()])) {
			childRule.expandAny(map);
		}
	}
	
	@Override
	public boolean calculate(byte[] values) {
      boolean result = Condition == ConditionType.AND;
      if (ChildRules == null) {
    	  return result;
      }
      for(AbstractRule rule : ChildRules) {
        if (Condition == ConditionType.AND) {
        	result &= rule.calculate(values);
        }
        else {
        	result |= rule.calculate(values);
        }
      }
      return result;
    }	

	@Override
	public CRRuleBody createBinaryStructure(CRRuleBlockEntry parent) 
		throws CapacityRuleException
		{
		CRRuleBody body = new CRRuleBody(parent);
        for(AbstractRule childRule : ChildRules) {
        	CRAndExprBlock ab = new CRAndExprBlock(body);
        	if (childRule instanceof ComplexCondition) {
        		ComplexCondition crule = (ComplexCondition)childRule;
        		for(AbstractRule rule : crule.getChildRules()) {
        			if (rule instanceof ElementaryRule) {
        				ElementaryRule erule = (ElementaryRule)rule;
        				ab.add(new CRElemExpr(ab, erule.getCapacity(), erule.getMediaType()));
        			}
        		}
        	}
        	else if (childRule instanceof ElementaryRule) {
        		ElementaryRule erule = (ElementaryRule)childRule;
   				ab.add(new CRElemExpr(ab, erule.getCapacity(), erule.getMediaType()));
       		}
        	body.add(ab);
        }
        return body;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Condition.name());
		sb.append('(');
		for(AbstractRule rule : ChildRules) {
			sb.append(rule);
			sb.append(',');
		}
		if (!ChildRules.isEmpty()) {
			sb.setLength(sb.length()-1);
		}
		sb.append(')');
		return sb.toString();
	}


	static ComplexCondition castOrNull(AbstractRule rule) {
		return rule instanceof ComplexCondition ? (ComplexCondition)rule : null;
	}

	public void add(CRAndExprBlock expr) throws CapacityRuleException {
		ComplexCondition and = new ComplexCondition(this, ConditionType.AND);
		CRMediaMap map = expr.getMap();
		if (map == null) {
		    throw new IllegalArgumentException("arument exepr dosen't contain map");
		}
		for(CRElemExpr op : expr.getList()) {
			CRMediaMapEntry mapEntry = map.getEntries().get(op.mediaType);
			and.addRule( new ElementaryRule(and, mapEntry.getMediaName(), op.mediaType, op.capacity) );
			mapEntry.MaxCapacity = Math.max(op.capacity, mapEntry.MaxCapacity);
		}
		addRule(and);
	}
	
	
}
