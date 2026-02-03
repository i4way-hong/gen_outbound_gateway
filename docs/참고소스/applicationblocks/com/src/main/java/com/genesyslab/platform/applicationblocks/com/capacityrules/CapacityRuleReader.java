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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;

class CapacityRuleReader implements ICapacityRuleReader {
	
	static final ILogger log = Log.getLogger(CapacityRuleReader.class); 

	private Document document;
	
	private final List<ICapacityRuleProcessElement> eventHandlers = new ArrayList<ICapacityRuleProcessElement>();
	
	
	public void registerProcessElement(ICapacityRuleProcessElement processElement) {
		synchronized (eventHandlers) {
			if (!eventHandlers.contains(processElement)) {
				eventHandlers.add(processElement);
			}
		}
	}

	public void unregisterProcessElement(ICapacityRuleProcessElement processElement) {
		synchronized (eventHandlers) {
			eventHandlers.remove(processElement);
		}
	}

	public void process() throws CapacityRuleException {
		if (document == null) {
			throw new IllegalStateException("source not assigned");
		}
		Element documentElement = document.getDocumentElement();
		if (documentElement == null
				|| !documentElement.getNodeName().equalsIgnoreCase("CapacityRule")) {
			return;
		}
		processNode(documentElement);
	}


	public ICapacityRuleReader setSource(Object source) 
		throws CapacityRuleException 
	{
		if (source instanceof Document) {
			document = (Document)source;
		}
		else {
			throw new CapacityRuleException("Illegal source : " + source);
		}
		return this;
	}
	
	
	private void processNode(Node node) throws CapacityRuleException {
		if (node == null) {
			return;
		}
		if (!(node instanceof Element)) {
			return;
		}
		Element element = (Element)node;
		EnterElementArgs args = new EnterElementArgs(element);
		invokeHandlers(this, args);
		NodeList children = element.getChildNodes();
		int length = children.getLength();
		for(int i=0; i<length; i++) {
			Node child = children.item(i);
			processNode(child);
		}
		LeaveElementArgs lArgs = new LeaveElementArgs(element);
		invokeHandlers(this, lArgs);
	}

	private void invokeHandlers(CapacityRuleReader sender, Object args) throws CapacityRuleException {
		ICapacityRuleProcessElement[] handlers;
		synchronized (eventHandlers) {
			handlers = new ICapacityRuleProcessElement[eventHandlers.size()];
			eventHandlers.toArray(handlers);
		}
		for(ICapacityRuleProcessElement handler : handlers) {
			handler.onProcessElement(sender, args);
		}
		
	}
	

}
