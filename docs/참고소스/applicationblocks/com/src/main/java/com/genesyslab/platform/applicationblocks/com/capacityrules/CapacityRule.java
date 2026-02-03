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

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;

/**
 * Helper class to create binary presentation of CapacityRule from XML. 
 */
class CapacityRule {

    private static final ILogger log = Log.getLogger(CapacityRule.class);

    private ICapacityRuleReader reader;
	private ICapacityRuleWriter writer;
	private Document document;
	private OnMediaRuleChanged onRuleChanged;
	private CRHeader header;
	private byte[] binaryData;
	
	private CapacityRule(Document document) {
		super();
		this.document = document;
	}

	byte[] getBinaryData() {
		return binaryData;
	}

	
	HashMap<String, Integer> getSimpleMediaMap() {
		return header == null ? null : header.getSimpleMediaMap();
	}

	/**
	 * Creates binary presentation of CapacityRule from XML.
	 * @param document XML document presentation of capacity rule. 
	 * @return binary presentation of CapacityRule.
	 * @throws CapacityRuleException when occurs some problem in XML parsing.
	 */
	public static CapacityRule createFromXML(Document document) throws CapacityRuleException {
		return createFromXML(document, null, null);		
	}
	
	/**
	 * Creates binary presentation of CapacityRule from XML.
	 * @param document XML document presentation of capacity rule. 
	 * @param factory ICapacityRuleFactory implementation.
	 * @param onChanged xml parsing event handler.
	 * @return binary presentation of CapacityRule.
	 * @throws CapacityRuleException when occurs some problem in XML parsing.
	 */
	static CapacityRule createFromXML(Document document, ICapacityRuleFactory factory, 
			OnMediaRuleChanged onChanged) throws CapacityRuleException {
		
		if (factory == null) {
			factory = new CapacityRuleInternalFactory();
		}
		CapacityRule rule = new CapacityRule(document);
		rule.reader = factory.getReader();
		rule.writer = factory.getWriter();
		rule.onRuleChanged = onChanged;
		
		rule.create();
		return rule;
	}

	
	private void create() throws CapacityRuleException {
		reader.setSource(document);
		reader.registerProcessElement( new ICapacityRuleProcessElement() {
			
			public void onProcessElement(Object sender, Object args) throws CapacityRuleException {
				if (args instanceof EnterElementArgs) {
					EnterElementArgs eArgs = (EnterElementArgs)args;
					Element elem = eArgs.getElement();
					if (elem != null) {
						String localName = CRXmlUtils.getLocalName(elem);
						if (localName.equalsIgnoreCase(CRHeader.TAG_NAME)) {
							header = new CRHeader((ICapacityRuleReader)sender, CapacityRule.this);
						}
					}
				}
			}
		} );
		reader.process();
		if ((header != null) && (writer != null)) {
			header.write(writer);
			binaryData = writer.getBuffer();
		}
	}

	@Override
	public String toString() {
		return (header != null ? header.toString() : "");
	}

	OnMediaRuleChanged getOnRuleChanged() {
		return onRuleChanged;
	}

	
	/**
	 * Validates rule.
	 * @return true if validation is successfully otherwise - false
	 */
    public boolean validate()
    {
        if (header == null) {
      	  log.error("No header");
      	  return false;
        }
        CRMediaMap mediaMap = header.getMediaMap();
        if (mediaMap == null) {
      	  log.error("No mediamap");
      	  return false;
        }
        CRMediaDir mediaDir = header.getMediaDir();
        if (mediaDir == null) {
    	  log.warn("No mediaDir");
      	  return false;
        }
        List<CRMediaDirEntry> mediaDirEntries = mediaDir.getEntries();
        if (mediaDirEntries == null) {
      	  log.error("Error in media dir entry");
      	  return false;
        }
        for(CRMediaDirEntry entry : mediaDirEntries) {
        	CRRuleBlock ruleBlock = entry.getRuleBlock();
  			if (ruleBlock == null) {
  				log.errorFormat("Error in rule block: {0}", entry);
  				return false;
  			}
  			List<CRRuleBlockEntry> ruleBlockRntries = ruleBlock.getEntries();
  			if (ruleBlockRntries == null) {
  				log.errorFormat("Error in rule block entry: {0}", ruleBlock);
  				return false;
  			}
  			for (CRRuleBlockEntry blockEntry : ruleBlockRntries) {
  				MediaRule mediaRule = blockEntry.getMediaRule();
  				if (mediaRule == null) {
  					log.errorFormat("Error in media rule: {0}", ruleBlock);
  					return false;
  				}
            
  				if (log.isDebug())
  					log.debugFormat("checking rule {0}", mediaRule);
            
  				if (!mediaRule.validateRule(mediaMap)) {
  					return false;
  				}
  			}    
       }
       return true;
    }
}
