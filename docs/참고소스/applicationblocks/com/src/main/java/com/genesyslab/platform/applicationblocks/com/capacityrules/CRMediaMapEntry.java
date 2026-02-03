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

import org.w3c.dom.Element;

/**
 *
 */
class CRMediaMapEntry extends CapacityRuleAbstractElement {

	static final String TAG_NAME = "Media";
	static final String TAG_ATTRIBUTE = "name";
	
	private int MediaType;
	private String MediaName;
	int MaxCapacity;
	
	private CRMediaMap Parent;
	

	
	CRMediaMapEntry(CRMediaMap parent, String mediaName, int mediaType) 
		throws CapacityRuleException 
	{
		super();
		
		if (mediaName == null) {
			throw new CapacityRuleException("null media name for media map entry");
		}
		
		this.Parent = parent;
		this.MediaType = mediaType;
		this.MediaName = mediaName;
		MaxCapacity = 0;
	}

	public CRMediaMapEntry(CRMediaMap parent, String mediaName) {
		super();
		this.Parent = parent;
		this.MediaType = parent.getMediaTypeCount();
		this.MediaName = mediaName;
		MaxCapacity = 0;
	}

	

	/**
	 * Gets parent object. 
	 * @return parent object that contains it. 
	 */
	@Override
	public CRMediaMap getParent() {
		return Parent;
	}	
	
	
	/**
	 * Gets media name.
	 * @return media name.
	 */
	public String getMediaName() {
		return MediaName;
	}

	/**
	 * Gets media type.
	 * @return media type.
	 */
	public int getMediaType() {
		return MediaType;
	}

	/**
	 * Create instance of CRMediaMapEntry from xml element. 
	 * @param parent parent map.
	 * @param element xml element.
	 * @return new instance of CRMediaMapEntry.
	 */
	public static CRMediaMapEntry create(CRMediaMap parent, Element element) {
	      String attrValue = element.getAttribute(TAG_ATTRIBUTE);
	      if (attrValue != null) {
	        return new CRMediaMapEntry(parent, attrValue);
	      }
	      return null;
	}

	/**
	 * Stores instance data.
	 * @param writer that will write data.
	 */
	public void write(ICapacityRuleWriter writer) {
		if (MediaName == null) {
			return;
		}
		
		for(char c : MediaName.toCharArray()) {
			writer.addByte((byte)c);
		}
		writer.addByte((byte)0);
	}


	@Override
	public String toString() {
	      return "[CRMediaMapEntry] name=\"" + MediaName + "\"";
	}

}
