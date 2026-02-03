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


abstract class AbstractRule implements Cloneable {

	AbstractRule Parent;
	private ArrayList<OnExpandAnyHandler> OnExpandAnyHandlers = new ArrayList<OnExpandAnyHandler>();
	
	
	AbstractRule(AbstractRule parent) {
		super();
		this.Parent = parent;
	}

	public AbstractRule getParent() {
		return Parent;
	}
	
	public void registerOnExpandAny(OnExpandAnyHandler handler) 
			throws CapacityRuleException 
	{
		if (handler == null) {
			throw new CapacityRuleException("null handler argument");
		}
		if (!OnExpandAnyHandlers.contains(handler)) {
			OnExpandAnyHandlers.add(handler);
		}
	}

	public void invokeOnExpandAnyHandlers(AbstractRule source, CRMediaMap map, int capacity) 
			throws CapacityRuleException {
		for(OnExpandAnyHandler handler : OnExpandAnyHandlers) {
			handler.onExpandAnyHandler(source, map, capacity);
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
    	final AbstractRule o = (AbstractRule) super.clone();
		o.OnExpandAnyHandlers = new ArrayList<OnExpandAnyHandler>();
		return o;
	}
	
	
	public abstract void assignMediaTypes(CRMediaMap map) throws CapacityRuleException;
	public abstract void expandAny(CRMediaMap map) throws CapacityRuleException;
    public abstract boolean calculate(byte[] values);
	public abstract CRRuleBody createBinaryStructure(CRRuleBlockEntry parent) throws CapacityRuleException;

}
