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

import java.nio.ByteBuffer;


/**
 * CRRuleBlockEntry class is represent a rule block entry
 */
class CRRuleBlockEntry extends CapacityRuleAbstractElement {
	
	private static final int DEFAULT_PRIORITY = 0xff;

	private CRRuleBlock parent;
	private int priority = Integer.MIN_VALUE;
	private CRRuleBody ruleBody;
	private MediaRule mediaRule;
	

	/**
	 * Create a instance of CRRuleBody class.
	 * @param parent object that will contains it 
	 */
	public CRRuleBlockEntry(CRRuleBlock parent, ICapacityRuleReader reader, String priority) {
		super();
		this.parent = parent;
		try {
			this.priority =  Integer.parseInt(priority);
			
		} catch(NumberFormatException e) {
			this.priority = DEFAULT_PRIORITY;
		}
		mediaRule = new MediaRule(reader, this);
	}
	
	
	public CRRuleBlockEntry(CRRuleBlock parent, int priority, ByteBuffer buf, int positionBase) 
			throws CapacityRuleException {
		super();
		this.parent = parent;
		this.priority = priority;
		read(buf, positionBase);
		mediaRule = new MediaRule(this, ruleBody); 
	}


	/**
	 * Process media rule
	 * @param map
	 */
	public void process(CRMediaMap map) throws CapacityRuleException {
		if (mediaRule == null) { 
			return;
		}
		
		OnMediaRuleChanged onChanged = null;
		Object parent = this.parent;
		while (parent != null) {
			if (parent instanceof CapacityRule) {
				onChanged = ((CapacityRule)parent).getOnRuleChanged();
				break;
			}
			else if (parent instanceof CapacityRuleAbstractElement) {
				parent = ((CapacityRuleAbstractElement)parent).getParent();
			}
			else {
				break;
			}
		}
		mediaRule.setOnRuleChanged(onChanged);
		ruleBody = mediaRule.process(map);
	}

	/**
	 * Gets parent object. 
	 * @return parent object that contains it 
	 */
	public CRRuleBlock getParent() {
		return parent;
	}


	/**
	 * Gets priority
	 * @return priority
	 */
	public int getPriority() {
		return priority;
	}	

	/**
	 * Stores instance data.
	 * @param writer that will write data
	 */
	public void write(ICapacityRuleWriter writer) {
		if (ruleBody == null) {
			return;
		}
		ruleBody.write(writer);
	}


	private void read(ByteBuffer buf, int positionBase) throws CapacityRuleException {
		ruleBody = new CRRuleBody(this, buf, positionBase);
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (priority != Integer.MIN_VALUE) {
			sb.append("priority=");
			sb.append('"');
			sb.append(priority);
			sb.append('"');
			sb.append(' ');
		}
		if (mediaRule != null) {
			sb.append(mediaRule.toString());
		} 
		else if (ruleBody != null) {
			sb.append(ruleBody.toString());
		}
		return sb.toString();
	}


	public MediaRule getMediaRule() {
		return mediaRule;
	}


	@Override
	public int hashCode() {
		int hashCode = CRRuleBlockEntry.class.hashCode();
		
		hashCode = hashCode * 31 + priority;
		if (ruleBody != null)
			hashCode = hashCode * 31 + ruleBody.hashCode();  

		return hashCode;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof CRRuleBlockEntry))
			return false;		
		CRRuleBlockEntry o = (CRRuleBlockEntry)obj;

		if (priority != o.priority) {
			return false;
		}

		return ruleBody == null ? o.ruleBody == null : ruleBody.equals(o.ruleBody);
	}


	public CRMediaMap getMap() {
		return parent == null ? null : parent.getMap();
	}


	public boolean match(CRRuleBlockEntry entry) {
		if (entry == null)
			return false;

		return mediaRule == null ? entry.mediaRule == null : mediaRule.match(entry.mediaRule);
	}


	
	
}
