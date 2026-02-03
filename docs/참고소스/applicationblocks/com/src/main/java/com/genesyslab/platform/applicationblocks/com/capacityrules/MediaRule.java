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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

import com.genesyslab.platform.applicationblocks.com.capacityrules.ComplexCondition.ConditionType;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;

class MediaRule<it> implements ICapacityRuleProcessElement {

    private static final ILogger log = Log.getLogger(CapacityRule.class);

    private static final boolean DEBUG = false;
	
    static final String ELEMENTARY_RULE_NAME = "ElementaryRule";
    static final String COMPLEX_CONDITION_NMAE = "ComplexCondition";	
    
    private OnMediaRuleChanged OnRuleChanged;
    private ICapacityRuleReader reader;
    private AbstractRule Rule;
    private AbstractRule OriginalRule;
    private AbstractRule CurrentRule;
    private CRRuleBlockEntry parent;
    
	
	public MediaRule(ICapacityRuleReader reader, CRRuleBlockEntry parent) {
		this.parent = parent;
		this.reader = reader;
		reader.registerProcessElement(this);
	}

	public MediaRule(CRRuleBlockEntry parent, CRRuleBody ruleBody) 
			throws CapacityRuleException 
	{
		this.parent = parent;
		read(ruleBody);	
	}

	private void read(CRRuleBody ruleBody) 
			throws CapacityRuleException 
	{
		ComplexCondition rule = new ComplexCondition(null, ComplexCondition.ConditionType.OR);
		for(CRAndExprBlock expr : ruleBody.getList()) {
			rule.add(expr);
		}
		Rule = OriginalRule = rule;
		createRuleBody();
	}

	public void onProcessElement(Object sender, Object args)
			throws CapacityRuleException {
		if (args instanceof EnterElementArgs) {
			EnterElementArgs eArgs = (EnterElementArgs)args;
			Element elem = eArgs.getElement();
			if (elem != null) {
				String localName = CRXmlUtils.getLocalName(elem);
				if (localName.equalsIgnoreCase(ELEMENTARY_RULE_NAME)) {
					if (Rule == null) {
						Rule = CurrentRule = new ComplexCondition(null, ComplexCondition.ConditionType.OR);
					}
					AbstractRule rule = new ElementaryRule(CurrentRule, elem);
					if (CurrentRule instanceof ComplexCondition) {
						rule.registerOnExpandAny(new OnExpandAnyHandler() {
							public void onExpandAnyHandler(AbstractRule source, CRMediaMap map, int capacity) 
									throws CapacityRuleException 
							{
								ComplexCondition newRule = (ComplexCondition)source.getParent();
								CRHeader mapParent = map.getParent();
								if (mapParent == null) {
									return;
								}
								CRHeader header = mapParent;
								CRMediaDir mediaDir = header.getMediaDir();
								if  (mediaDir == null) {
									return;
								}
								List<CRMediaDirEntry> entries = mediaDir.getEntries();
								if (entries == null) {
									return;
								}
								if (newRule.getCondition() == ComplexCondition.ConditionType.OR) {
									for(CRMediaDirEntry entry : entries) {
										newRule.addRule(new ElementaryRule(newRule, entry.getMediaName(), entry.getMediaType(), capacity));
									}
									newRule.removeRule(source);
								}
								else if (newRule.getCondition() == ComplexCondition.ConditionType.AND) {
									ComplexCondition addRule = new ComplexCondition(newRule, ComplexCondition.ConditionType.OR);
									for(CRMediaDirEntry entry : entries) {
										addRule.addRule(new ElementaryRule(newRule, entry.getMediaName(), entry.getMediaType(), capacity));
									}
									newRule.removeRule(source);
									newRule.addRule(addRule);
								}
							}
						});
						((ComplexCondition)CurrentRule).addRule(rule);
					}
				}
				if (localName.equalsIgnoreCase(COMPLEX_CONDITION_NMAE)) {
					AbstractRule rule = new ComplexCondition(CurrentRule, elem);
					if (Rule == null) {
						Rule = CurrentRule = rule;
					}
					else {
						if (CurrentRule instanceof ComplexCondition) {
							((ComplexCondition)CurrentRule).addRule(rule);
							CurrentRule = rule;
						}
					}
					
				}
			}
		}
		else if (args instanceof LeaveElementArgs) {
			LeaveElementArgs eArgs = (LeaveElementArgs)args;
			Element elem = eArgs.getElement();
			if (elem != null) {
				String localName = CRXmlUtils.getLocalName(elem);
				if (localName.equalsIgnoreCase(COMPLEX_CONDITION_NMAE)) {
					if (CurrentRule == null) {
						throw new CapacityRuleException("Capacity rule contains wrong MediaRule");
					}
					CurrentRule = CurrentRule.getParent();
				}
				if (localName.equalsIgnoreCase(CRMediaDir.TAG_NAME)) {
					if (sender instanceof ICapacityRuleReader) {
						((ICapacityRuleReader)sender).unregisterProcessElement(this);
					}
				}
			}
		}
		
	}

	
	public void setOnRuleChanged(OnMediaRuleChanged onChanged) {
		OnRuleChanged = onChanged;
	}

	public CRRuleBody process(CRMediaMap map) throws CapacityRuleException  {
		if (Rule == null) {
			return null;
	    }
	    Rule.assignMediaTypes(map);
	    OriginalRule = Rule;
	    
	    Rule.expandAny(map);
	    if (Rule instanceof ComplexCondition) {
	    	Rule = transformRule();
	    }

	    return createRuleBody();
	}

	private CRRuleBody createRuleBody() throws CapacityRuleException {
		if (Rule == null) {
			return null;
		}
		convertStructure();
		return Rule.createBinaryStructure(parent);
	}

	private void convertStructure() {
		if (!isRuleMappedToDNF(Rule, 0)) {
			return;
		}
		ComplexCondition crule = ComplexCondition.castOrNull(Rule);
		if (crule == null || crule.getCondition() != ComplexCondition.ConditionType.OR) {
			Rule = new ComplexCondition(null, ComplexCondition.ConditionType.OR).addRule(Rule);
		}
		
	}

	private boolean isRuleMappedToDNF(AbstractRule rule) {
		return isRuleMappedToDNF(rule, 0);
	}

	private boolean isRuleMappedToDNF(AbstractRule rule, int level) {
		switch (level) {
		case 0:
			if (rule instanceof ComplexCondition) {
				ComplexCondition cRule = (ComplexCondition)rule;
				for(AbstractRule childRule : cRule.getChildRules()) {
					if (!isRuleMappedToDNF(childRule, level + 1)) {
						return false;
					}
				}
			}
			return true;
			
		case 1:
			if (rule instanceof ComplexCondition) {
				ComplexCondition cRule = (ComplexCondition)rule;
				AbstractRule cParent = cRule.getParent();
				if (cParent == null) {
					return false;
				}
				if ( ((ComplexCondition)cParent).getCondition() != ComplexCondition.ConditionType.OR ) {
					return false;
				}
				if (cRule.getCondition() != ComplexCondition.ConditionType.AND) {
					return false;
				}
				for(AbstractRule childRule : cRule.getChildRules()) {
					if (!isRuleMappedToDNF(childRule, level + 1)) {
						return false;
					}
				}
			}
			return true;
			
		case 2:
			if (rule instanceof ComplexCondition) {
				return false;
			}
			return true;

		default:
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (Rule != null) {
			sb.append(Rule);
		}
		if (OriginalRule != null) {
			sb.append("   <=   ");
			sb.append(OriginalRule);
		}
		return sb.toString();
	}
	
	
	private void invokeEvent(String header, AbstractRule rule) {
		if (OnRuleChanged == null) {
			return;
		}
		OnRuleChanged.onRuleChanged(header, rule);//, null);
	}
	
	private void invokeEvent(String header, AbstractRule rule, String sourceRuleInfo) {
		if (OnRuleChanged == null) {
			return;
		}
		OnRuleChanged.onRuleChanged(header, rule);//, sourceRuleInfo);
	}

	private void prepareStructure() {
		if (Rule instanceof ElementaryRule) {
			ComplexCondition newRule = new ComplexCondition(null, ComplexCondition.ConditionType.OR);
			newRule.addRule(Rule);
			Rule = newRule;
		}
	}

	private AbstractRule transformRule() {
		invokeEvent( "Source rule", Rule );
		
		prepareStructure();
		
		AbstractRule newRule;
		try {
			newRule = (AbstractRule)Rule.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}

		invokeEvent( "Clone rule", Rule );
		
		boolean ruleChanged = false;
		
		AbstractRule modifiedRule = reduceRule(newRule);
		if (modifiedRule != null) {
			ruleChanged = true;
			newRule = modifiedRule;
		}

		if (!isRuleMappedToDNF(newRule)) {
			boolean needTransform = true;
	        while (needTransform) {
	        	boolean result = false;
	        	
	        	modifiedRule = distributeRule(newRule);
	  			if (modifiedRule != null) {
	  				result = true;
	  				newRule = modifiedRule;
	  			}
	          
	        	modifiedRule = reduceRule(newRule);
	  			if (modifiedRule != null) {
	  				result = true;
	  				newRule = modifiedRule;
	  			}
	          
	  			needTransform = result && (!isRuleMappedToDNF(newRule));
	  			ruleChanged |= result;
	        }
	      }
	      if (ruleChanged) {
	    	  Rule = newRule; 
	      }
	      
	      invokeEvent("Out rule", Rule);
	      invokeEvent(null, null);
	      
	      return Rule;
	}


	private AbstractRule distributeRule(AbstractRule rule) {
		boolean result = transformByDistributeRule(rule);
	    if (result) {
	    	AbstractRule modifiedRule = simplifyRule(rule);
	    	if (modifiedRule != null) {
	    		rule = modifiedRule;
	    	}
	    }
	    return result ? rule : null;
	}

	

	/**
	 * Applies distribution rule 
     * <br>a && (b || c) = (a && b) || (a && c) 
     * <br>a || (b && c) = (a || b) && (a || c)
     * 	 
     * @param rule Root rule
	 * @return true, if root rule is changed, otherwise - false
	 */
	private boolean transformByDistributeRule(AbstractRule rule) {
		if (!(rule instanceof ComplexCondition)) {
			return false;
		}
		ComplexCondition baseRule = (ComplexCondition)rule;
		
		ConditionType baseRuleContrCondition = baseRule.getContrCondition();
		if (baseRuleContrCondition == null) {
			return false;
		}

		ConditionType baseRuleCondition = baseRule.getCondition();
	    
	    ArrayList<AbstractRule> baseChildRules = baseRule.getChildRules();
	    if (baseChildRules == null) {
	    	return false;
	    }
	    
	    String sourceRuleInfo = DEBUG ? rule.toString() : null;

	    int baseChildCount = baseChildRules.size(); 

	    // collect counter child rules
	    int contrChildRulesCount = 0;
	    int[] contrChildRulesIndexes = new int[baseChildCount];
		for(int i=0; i<baseChildCount; i++) {
			AbstractRule childRule = baseChildRules.get(i);
	        if (childRule instanceof ComplexCondition) {
	        	if (((ComplexCondition)childRule).getContrCondition() == baseRuleCondition) {
                    contrChildRulesIndexes[contrChildRulesCount++] = i;
	        	}
	        }
	    }
	    if (contrChildRulesCount == 0) {
    		// a && b && .. && c = a && b && .. && c  
    		// a || b || .. || c = a || b || .. || c  
	    	return false;
	    }
		
	    if (contrChildRulesCount == 1) {
	    	ComplexCondition child = (ComplexCondition)baseChildRules.get(contrChildRulesIndexes[0]);
	    	ArrayList<AbstractRule> childRules = child.getChildRules();
            if (childRules == null) {
                return false;
            }
            int childRulesCount = childRules.size();
            if (childRulesCount == 0) {
                return false;
            }
			// a && (b || c) ... && d = (b && a ... && d) || (c && a ... && d)   
			// a || (b && c) ... || d = (b || a ... || d) && (c || a ... || d)   
	    	baseRule.setCondition(baseRuleContrCondition);
	    	AbstractRule[] tmpBaseChildRules = new AbstractRule[childRulesCount];  
			for(int i=0; i<childRulesCount; i++) {
				AbstractRule childRule = childRules.get(i);
	    		ComplexCondition tRule = new ComplexCondition(baseRule, baseRuleCondition);
	    		tRule.addRule(childRule);
		        for(int j=0; j<baseChildCount; j++) {
		        	AbstractRule elemRule = baseChildRules.get(j);
		        	ComplexCondition crule = ComplexCondition.castOrNull(elemRule);
		        	if (crule == null || crule.getCondition() == baseRuleCondition) {
			        	tRule.addRule(elemRule);
		        	}
		        }
		        tmpBaseChildRules[i] = tRule;
	    	}
			baseChildRules.clear();   
			for(int i=0; i<childRulesCount; i++) {
				baseChildRules.add(tmpBaseChildRules[i]);
			}
	    	invokeEvent("Distribution rule", rule, sourceRuleInfo);
	    	return true;
	    }
	    
	    // collect not counter child rules
	    int elemRulesCount = 0;
	    int[] elemRulesIndexes = new int[baseChildCount];
		for(int i=0; i<baseChildCount; i++) {
			AbstractRule childRule = baseChildRules.get(i);
	        if (childRule instanceof ElementaryRule) {
	        	elemRulesIndexes[elemRulesCount++] = i;
	        }
	        else if (childRule instanceof ComplexCondition) {
	        	if (((ComplexCondition)childRule).getCondition() == baseRuleCondition) {
		        	elemRulesIndexes[elemRulesCount++] = i;
	        	}
	        }
	    }
	    
	    baseRule.setCondition(baseRuleContrCondition);
    	ArrayList<AbstractRule> tmpNewBaseChildRules = new ArrayList<AbstractRule>(1024);
	    for (int i = 0; i < contrChildRulesCount-1; i++) {
	        for (int j = i + 1; j < contrChildRulesCount; j++) {
	        	ComplexCondition child1 = (ComplexCondition)baseChildRules.get(contrChildRulesIndexes[i]);
	        	ComplexCondition child2 = (ComplexCondition)baseChildRules.get(contrChildRulesIndexes[j]);
	        	ArrayList<AbstractRule> childRules1 = child1.getChildRules();
				ArrayList<AbstractRule> childRules2 = child2.getChildRules();
				if ((childRules1 == null) || (childRules2 == null)) {
	        		continue;
	        	}
				int childRules1Count = childRules1.size(); 
				int childRules2Count = childRules2.size(); 
	        	for(int i1=0; i1<childRules1Count; i1++) {
	            	AbstractRule rule1 = childRules1.get(i1);
		        	for(int i2=0; i2<childRules2Count; i2++) {
		            	AbstractRule rule2 = childRules2.get(i2);
		            	ComplexCondition tcRule = new ComplexCondition(baseRule, baseRuleCondition);
		            	tcRule.addRule(rule1);
		            	tcRule.addRule(rule2);
				        for(int k=0; k<elemRulesCount; k++) {
				        	AbstractRule elemRule = baseChildRules.get(elemRulesIndexes[k]);
				        	tcRule.addRule(elemRule);
				        }
				        
				        for(int k=0; k<baseChildCount; k++) {
				        	if (k == contrChildRulesIndexes[i] || k == contrChildRulesIndexes[j]) {
				        		continue;
				        	}
				        	tcRule.addRule(baseChildRules.get(k));
				        }
				        
				        AbstractRule tmpRule = tcRule;
				        AbstractRule modifiedRule;
				        
				        modifiedRule = reduceRule(tmpRule);
				        if (modifiedRule != null) {
				        	tmpRule = modifiedRule;
				        }
				        
				        transformByDistributeRule(tmpRule);

				        modifiedRule = reduceRule(tmpRule);
				        if (modifiedRule != null) {
				        	tmpRule = modifiedRule;
				        }

				        tmpNewBaseChildRules.add(tmpRule);
				        
		            }
	        	}
	        }
	    }
		baseChildRules.clear();   
		for(int i=0; i<tmpNewBaseChildRules.size(); i++) {
			baseChildRules.add(tmpNewBaseChildRules.get(i));
		}
	    
      	invokeEvent("Distribution rule", rule, sourceRuleInfo);
	    return true;
	}

	/**
	 * Simplifies rule (reduce OR-, AND- operators with one argument).
	 *
	 * @param rule Rule for simplification.
	 * @return Simplified rule (this or some child) or null if the rule isn't simplified. 
	 */
	private AbstractRule simplifyRule(final AbstractRule rule) {
		
		if (!(rule instanceof ComplexCondition)) { // Elementary rule
			return null;
		}
		
	    String sourceRuleInfo = DEBUG ? rule.toString() : null;
		
		ComplexCondition complexRule = (ComplexCondition)rule;
	    ArrayList<AbstractRule> childRules = complexRule.getChildRules();
		if (childRules == null) {
			return null;
	    }
		
		if (childRules.size() == 1) {
	    	AbstractRule childRule = childRules.get(0);
	    	AbstractRule simplifiedChildRule = simplifyRule(childRule);
	    	if (simplifiedChildRule != null) {
		    	childRule = simplifiedChildRule;
	    	}
    		invokeEvent("Simplify rule", childRule, sourceRuleInfo);
	    	return childRule;
	    }
		
	    boolean simplified = false;
	    int childCount = childRules.size(); 
	    for(int i=0; i<childCount; i++) {
	    	AbstractRule childRule = childRules.get(i);
	    	AbstractRule simplifiedChildRule = simplifyRule(childRule);
	    	if (simplifiedChildRule != null) {
	    		if (!simplified) {
		    		simplified = true;
	    		}
	    		childRules.set(i,  simplifiedChildRule);
	    	}
	    }

	    if (simplified) {
    		invokeEvent("Simplify rule", rule, sourceRuleInfo);
    	}
	    
    	return simplified ? rule : null; 
	}

	private AbstractRule reduceRule(AbstractRule rule) {
		boolean needTransform = true;
	    boolean ruleChanged = false;
    	AbstractRule modifiedRule;  
	    while (needTransform) {
	    	boolean result = false;
	    	
	    	modifiedRule = simplifyRule(rule);
	    	if (modifiedRule != null) {
	    		result = true;
	    		rule = modifiedRule;
	    	}
	        
	    	modifiedRule = transformByAssociativeRule(rule);
	    	if (modifiedRule != null) {
	    		result = true;
	    		rule = modifiedRule;
	    	}
	        
	    	modifiedRule = transformByIdempotencyRule(rule);
	    	if (modifiedRule != null) {
	    		result = true;
	    		rule = modifiedRule;
	    	}
	    	
	        modifiedRule = simplifyRule(rule);
	    	if (modifiedRule != null) {
	    		result = true;
	    		rule = modifiedRule;
	    	}
	    	
	    	modifiedRule = transformByAbsorbtionRule(rule);
	    	if (modifiedRule != null) {
	    		result = true;
	    		rule = modifiedRule;
	    	}
	        
	        modifiedRule = simplifyRule(rule);
	    	if (modifiedRule != null) {
	    		result = true;
	    		rule = modifiedRule;
	    	}

	    	needTransform = result;
	        ruleChanged |= result;
	      }
	      return ruleChanged ? rule : null;
	}

	/**
     * Idempotency rule
     * <br>a && a  = a 
     * <br>a || a  = a 
	 * @param rule Root rule.
	 * @return root rule, if the rule is changed, otherwise - null.
	 */
	private AbstractRule transformByIdempotencyRule(AbstractRule rule) {
		ComplexCondition complexRule = ComplexCondition.castOrNull(rule);
		if (complexRule == null) {
			return null;
		}
	    boolean result = false;
	    ArrayList<AbstractRule> childRules = complexRule.getChildRules();
	    if (childRules == null) {
	    	return null;
	    }
	    
	    String sourceRuleInfo = DEBUG ? rule.toString() : null;
	    
		int count = childRules.size();
		for (int i = 0; i < count; i++) {
			AbstractRule childRule = childRules.get(i);
			AbstractRule modifiedRule = transformByIdempotencyRule(childRule);
			if (null != modifiedRule) {
				result = true;
				childRule = modifiedRule;
				modifiedRule = simplifyRule(childRule);
				if (modifiedRule != null) {
					childRule = modifiedRule;
				}
				childRules.set(i, childRule);
			}
	    }
    	ConditionType condition = complexRule.getCondition();
	    while (true) {
			int index = findImpodency(childRules, condition);
	        if (index < 0) {
	        	break;
	        }
	        result = true;
//	    	invokeEvent("Remove rule due Idempotency", childRules.get(index));
	        complexRule.removeRule(index);
	      }
	      if (result) {
	    	  invokeEvent("Impodency rule", rule, sourceRuleInfo);
	      }
	      return result ? rule : null;
	}
	
	
	private int findImpodency(List<AbstractRule> childs, ComplexCondition.ConditionType condition)
    {
		int childCount = childs.size();
		for (int i = 0; i < childCount - 1; i++) {
			ElementaryRule rule1 = ElementaryRule.castOrNull(childs.get(i));
			if (rule1 == null) {
				continue;
			}
			for (int j = i + 1; j < childCount; j++) {
				ElementaryRule rule2 = ElementaryRule.castOrNull(childs.get(j));
				if (rule2 == null) {
					continue;
				}
				if (rule1.getMediaType() != rule2.getMediaType()) {
					continue; // different media types
				}
				int capacity2 = rule2.getCapacity();
				int capacity1 = rule1.getCapacity();
				if (condition == ComplexCondition.ConditionType.OR) {
					return (capacity2 > capacity1) ? j : i;
				}
				if (condition == ComplexCondition.ConditionType.AND) {
					return (capacity2 < capacity1) ? j : i;
				}
			}
		}
		return -1;
    }	
	
	/**
	 * Replace all inner rules with equal condition 
     * <br>
     * <br>a && (b && c) = a && b && c
     * <br>a || (b || c) = a || b || c
     * 
	 * @param rule
	 * @return
	 */
	private AbstractRule transformByAssociativeRule(AbstractRule rule) {
		ComplexCondition baseRule = ComplexCondition.castOrNull(rule);
  		if (baseRule == null) {
  			return null;
  		}
  		
	    String sourceRuleInfo = DEBUG ? rule.toString() : null;
  		
	    boolean result = false;
  		ConditionType baseCondition = baseRule.getCondition();
	    ArrayList<AbstractRule> baseChildRules = baseRule.getChildRules();
	    int baseChildCount = baseChildRules.size();
	    for(int i=baseChildCount-1; i>=0; i--) {
	    	AbstractRule baseChild = baseChildRules.get(i);
	    	ComplexCondition complexChild = ComplexCondition.castOrNull(baseChild);
	        if (complexChild == null) {
	        	continue;
	        }
	        AbstractRule modifiedRule = transformByAssociativeRule(complexChild);
	        if (modifiedRule != null) {
		        result = true;
	        }
	        if (complexChild.getCondition() == baseCondition) {
	        	baseRule.removeRule(i);
	        	ArrayList<AbstractRule> childRules2 = complexChild.getChildRules();
	        	int childRules2Count = childRules2.size();
				for(int j=0; j<childRules2Count; j++) {
					AbstractRule child2 = childRules2.get(j);
					baseRule.addRule(child2);
				}
				result = true;
	        }
	      }
	      if (result) {
	    	  invokeEvent("Associavive rule", rule, sourceRuleInfo);
	      }
	      return result ? rule : null;
	}


	/**
	 * Absorbtion of equals rules
     * <br>
     * <br>a && (a || b) = a 
     * <br>a || (a && b) = a
     *  
	 * @param rule2
	 * @return
	 */
	private AbstractRule transformByAbsorbtionRule(AbstractRule rule) {
		ComplexCondition baseRule = ComplexCondition.castOrNull(rule);
	    if (baseRule == null) {
	    	return null; // Elementary rule
	    }
	    String sourceRuleInfo = DEBUG ? rule.toString() : null;
	    
	    boolean result = false;
	    ArrayList<AbstractRule> baseChildRules = baseRule.getChildRules();
	    int baseChildRulesCount = baseChildRules.size();
		for (int i = 0; i < baseChildRulesCount; i++) {
			AbstractRule baseChild = baseChildRules.get(i);
			AbstractRule modifiedRule = transformByAbsorbtionRule(baseChild);
			if (modifiedRule != null) {
				result = true;
				baseChild = modifiedRule;
				modifiedRule = simplifyRule(baseChild); 
				if (modifiedRule != null) {
					baseChild = modifiedRule;
				}
//		    	invokeEvent("Modified rule", baseChild);
				baseChildRules.set(i, baseChild);
			}
		}
	    while (true) {
	        boolean lResult = false;
	        ConditionType baseCondition = baseRule.getCondition();
			int index = findAbsorbtion(baseChildRules, baseCondition);
	        if (index >= 0) {
	        	lResult = true;
//		    	invokeEvent("Remove rule due absorbtion", baseChildRules.get(index));
	          	baseRule.removeRule(index);
	        }
	        index = findAbsorbtion2(baseChildRules, baseCondition);
	        if (index >= 0) {
	        	lResult = true;
//		    	invokeEvent("Remove rule due absorbtion2", baseChildRules.get(index));
	        	baseRule.removeRule(index);
	        }
	        if (lResult) {
	        	result = true; 
	        } else {
	        	break;
	        }
	    }
	    if (result) {
	    	invokeEvent("Absorbtion rule", rule, sourceRuleInfo);
	    }
	    return result ? rule : null;
  	}

	private int findAbsorbtion(ArrayList<AbstractRule> childs, ConditionType condition) {
		int childCount = childs.size();
		for (int i = childCount-1; i >= 0; i--) {
			ElementaryRule rule1 = ElementaryRule.castOrNull(childs.get(i));
	        if (rule1 == null) {
	        	continue;
	        }
      	  	int mediaType1 = rule1.getMediaType();
            int capacity1 = rule1.getCapacity();
	        for (int j = childCount-1; j >= 0; j--) {
	        	if (i == j) {
	        		continue;
	        	}
	        	ComplexCondition complexRule = ComplexCondition.castOrNull(childs.get(j));
	        	if (complexRule == null) {
	        		continue;
	        	}
	        	ArrayList<AbstractRule> complexChildRules = complexRule.getChildRules();
	        	int complexChildRulesCount = complexChildRules.size();
	        	for(int k=0; k<complexChildRulesCount; k++) {
	        		ElementaryRule rule2 = ElementaryRule.castOrNull(complexChildRules.get(k));
	        		if (rule2 == null) {
	        			continue; // complexRule
	        		}
	        		if (mediaType1 != rule2.getMediaType()) {
	        			continue; // different media types
	        		}
	        		int capacity2 = rule2.getCapacity();
	        		if (condition == ComplexCondition.ConditionType.OR) {
	        			if (capacity1 <= capacity2) {
	        				return j;
	        			}
	        		}
	        		if (condition == ComplexCondition.ConditionType.AND) {
	        			if (capacity1 >= capacity2) {
	        				return j;
	        			}
	        		}
	        	}
        	}
		}
		return -1;
	}
	
	private int findAbsorbtion2(ArrayList<AbstractRule> childs, ConditionType condition) {
		if (condition == ComplexCondition.ConditionType.OR) {
			return findAbsorbtion2Or(childs);
		}
//	    if (condition == ComplexCondition.ConditionType.AND) {
//			return FindAbsorbtion2And(childs);
//		}
	    return -1;
	}
	
	
     
//    /**
//     * (a || b) && (a || b || c) = a || b || c
//     *  
//     * @param childs
//     * @return
//     */
//    private int FindAbsorbtion2And(List<AbstractRule> childs)
//    {
//      return -1;
//    }	

	/**
     * (a && b) || (a && b && c) = (a && b)
	 * 
	 * @param childs
	 * @return
	 */
	private int findAbsorbtion2Or(ArrayList<AbstractRule> childs) {
		int childCount = childs.size();
		for (int i = 0; i < childCount; i++) {
			ComplexCondition cRule1 = ComplexCondition.castOrNull(childs.get(i));
	        if (cRule1 == null) {
	        	continue;
	        }
	        
	        if (cRule1.getChildRules().size() == 0) {
	        	continue;
	        }
	        ArrayList<AbstractRule> cChildRules1 = cRule1.getChildRules();
	        int cChildRulesCount1 = cChildRules1.size();
	        
	        for (int j = 0; j < childCount; j++) {
	        	if (i == j) {
	        		continue; // do not compare self
	        	}
	        	ComplexCondition cRule2 = ComplexCondition.castOrNull(childs.get(j));
	        	if (cRule2 == null) {
	        		continue;
	        	}

	        	ArrayList<AbstractRule> cChildRules2 = cRule2.getChildRules();
	        	int cChildRulesCount2 = cChildRules2.size();
		        if (cChildRulesCount2 == 0) {
		        	continue;
		        }
	          
		        ArrayList<AbstractRule> cChildRules1Clone = (ArrayList<AbstractRule>)cChildRules1.clone();

		        for(int i1=cChildRulesCount1-1; i1>=0; i1--) {
	        		ElementaryRule rule1 = ElementaryRule.castOrNull(cChildRules1.get(i1));
	        		if (rule1 == null) {
	        			continue; // complexRule
	        		}
	        		int mediaType1 = rule1.getMediaType();
	        		int capacity1 = rule1.getCapacity();
	        		for(int i2=cChildRulesCount2-1; i2>=0; i2--) {
	        			ElementaryRule rule2 = ElementaryRule.castOrNull(cChildRules2.get(i2));
	        			if (rule2 == null) {
	        				continue; // complexRule
	        			}
	        			if (mediaType1 != rule2.getMediaType()) {
	        				continue; // different media types
	        			}
	        			if (capacity1 <= rule2.getCapacity()) {
	        				cChildRules1Clone.remove(rule1);
	        			}
	        		}
	        	}
		        
	        	if (cChildRules1Clone.size() == 0) {
	        	  return j;
	        	}
	        }
	    }
    	return -1;
	}
	
	boolean validateRule(CRMediaMap map) {
		List<CRMediaMapEntry> entries = map.getEntries();
		int entriesCount = entries.size();
		if (entriesCount == 0) {
			return false;
		}
		
		byte[] values = new byte[entriesCount];
		
		while(true) {
        	boolean oValue = OriginalRule.calculate(values);
        	boolean cValue = Rule.calculate(values);
        	if (oValue != cValue) {
        		if (log.isError()) {
            		StringBuilder sb = new StringBuilder();
            		sb.append("Error in rule optimization. ");
            		for (int j = 0; j < entriesCount; j++) {
            			sb.append( MessageFormat.format("''{0}''={1} ", entries.get(j).getMediaName(), values[j]) );
            		}
            		sb.append( MessageFormat.format(" |  Original value = {0}, New value = {1}\n", oValue, cValue));
            		log.error(sb);
        		}
        		return false;
        	}
        	
        	// goto next values combination
        	
        	int index = entriesCount-1;
        	for(; index>=0; index--) {
        		int v = values[index] & 0xff;
				int maxCapacity = entries.get(index).MaxCapacity;
				if (v < maxCapacity) {
					values[index]++;
					break;
        		}
				values[index] = 0;
        	}
        	
        	if (index < 0) {
        		break;
        	}
		}
		
		return true;
	}

	public boolean match(MediaRule mediaRule) {
		
		CRMediaMap map1 = parent.getMap();
        if (map1 == null) {
            throw new IllegalArgumentException("this mediaRule doesn't contain map");
        }

        CRMediaMap map2 = mediaRule.parent.getMap();
		if (map2 == null) {
		    throw new IllegalArgumentException("mediaRule doesn't contain map");
		}
		
		List<CRMediaMapEntry> entries1 = map1.getEntries();
		int entriesCount = entries1.size();
		if (entriesCount == 0) {
			return true;
		}

		List<CRMediaMapEntry> entries2 = map2.getEntries();
		
		HashMap<String, Integer> smap2 = map2.getSimpleMediaMap();
		
		byte[] values1 = new byte[entriesCount];
		byte[] values2 = new byte[entriesCount];
		int[] v2table = new int[entriesCount];
		for(int i1=0; i1<entriesCount; i1++) {
			CRMediaMapEntry crMediaMapEntry = map1.getEntries().get(i1);
			if (crMediaMapEntry != null) {
	            Integer intValue = smap2.get(crMediaMapEntry.getMediaName());
	            if (intValue == null) {
	                throw new NullPointerException("no index for " + crMediaMapEntry.getMediaName());
	            }
                v2table[i1] = intValue;
			}
		}
		
		
		while(true) {
        	boolean oValue1 = OriginalRule.calculate(values1);
    		
        	for(int i1=0; i1<entriesCount; i1++) {
    			values2[v2table[i1]] = values1[i1];
    		}
        	boolean oValue2 = mediaRule.OriginalRule.calculate(values2);
        	
        	if (oValue1 != oValue2) {
        		if (log.isDebug()) {
            		StringBuilder sb = new StringBuilder();
            		sb.append("Mismatch between rules");
            		for (int j = 0; j < entriesCount; j++) {
            			sb.append( MessageFormat.format("''{0}''={1} ", entries1.get(j).getMediaName(), values1[j]) );
            		}
            		sb.append( MessageFormat.format(" |  value = {0}, other value = {1}\n", oValue1, oValue2));
            		log.debug(sb);
        		}
        		return false;
        	}
        	
        	// goto next values combination
        	
        	int index = entriesCount-1;
        	for(; index>=0; index--) {
        		int v = values1[index] & 0xff;
				CRMediaMapEntry media = entries1.get(index);
				Integer index2 = smap2.get(media.getMediaName());
				if (index2 == null) {
				    throw new NullPointerException("no index for " + media.getMediaName());
				}
                int maxCapacity = Math.max(
						media.MaxCapacity,
						entries2.get(index2).MaxCapacity
						);
				if (v < maxCapacity) {
					values1[index]++;
					break;
        		}
				values1[index] = 0;
        	}
        	
        	if (index < 0) {
        		break;
        	}
		}
		
		return true;
	}


    
}
