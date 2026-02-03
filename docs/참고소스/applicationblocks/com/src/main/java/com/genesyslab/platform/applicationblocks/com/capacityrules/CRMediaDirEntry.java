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

import org.w3c.dom.Element;

/**
 * CRMediaDirEntry class is represent a media directory entry
 */
class CRMediaDirEntry extends CapacityRuleAbstractElement {
	static final String TAG_ATTRIBUTE_NAME = "media";
	static final String TAG_ATTRIBUTE_PRIORITY = "priority";

	private int mediaType = -1;
	private CRRuleBlock ruleBlock;

	private CRMediaDir parent;
	
	private String mediaName;
	
	public CRMediaDirEntry(CRMediaDir parent)
	{
		this.parent = parent;
	}

	/**
	 * 
	 * @param parent
	 * @param reader
	 * @param element
	 */
	public CRMediaDirEntry(CRMediaDir parent, ICapacityRuleReader reader, Element element)
	{
		String attrNValue = element.getAttribute(TAG_ATTRIBUTE_NAME);
		this.parent = parent;
		mediaName = (attrNValue != null) ? attrNValue : "";
		ruleBlock = new CRRuleBlock(this, reader, element);
	}
	

	public CRMediaDirEntry(CRMediaDir parent, int mediaType, ByteBuffer buf, int positionBase) 
			throws CapacityRuleException {
		this(parent);
		setMediType(mediaType);
		read(buf, positionBase);
	}

	/**
	 * Gets parent object. 
	 * @return parent object that contains it 
	 */
	@Override
	public Object getParent() {
		return parent;
	}


	/**
	 * Gets media type
	 * @return media type
	 */
	public int getMediaType() {
		return mediaType;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMediaName() {
		return mediaName;
	}
	
	
	
	/**
	 * Add new rule block
	 * @param reader
	 * @param element
	 */
	public void addNewRuleBlock(ICapacityRuleReader reader, Element element) {
		if (ruleBlock != null) {
			ruleBlock.addNewRule(reader, element);
		}
	}

	
	/**
	 * Process contained rule block and setup media type
	 * @param map
	 * @throws CapacityRuleException 
	 */
	public void updateMediaMapType(CRMediaMap map) throws CapacityRuleException {
		if (mediaName != null) {
			mediaType = map.getMediaType(mediaName);
		} else {
			if (mediaType == -1) {
				throw new CapacityRuleException("undefined media dir entry media type");
			}
			try
			{
				mediaName = map.getEntries().get(mediaType).getMediaName();
			}
			catch(Throwable e) {
				throw new CapacityRuleException("invalid media dir entry media type: " + mediaType);
			}
		}
	}	

	/**
	 * Process contained rule block and setup media type
	 * @param map
	 * @throws CapacityRuleException 
	 */
	public void process(CRMediaMap map) throws CapacityRuleException {
		if (map == null) {
			return;
		}
		if (ruleBlock != null) {
			ruleBlock.process(map);
		}
	}


	/**
	 * Stores instance data.
	 * @param writer that will write data
	 */
	public void write(ICapacityRuleWriter writer) {
		if (ruleBlock == null) {
			return;
		}
		ruleBlock.write(writer);
	}


	private void read(ByteBuffer buf, int positionBase) throws CapacityRuleException {
		ruleBlock = new CRRuleBlock(this, buf, positionBase);
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[MediaDir:");
		sb.append(mediaName);
		sb.append("]\n");
		if (ruleBlock != null) {
			sb.append(ruleBlock).append("\n");
		}
		return sb.toString();
	}


	public static CRMediaDirEntry create(CRMediaDir parent, ICapacityRuleReader reader, Element element) {
		String attrNValue = element.getAttribute(TAG_ATTRIBUTE_NAME);
		if (attrNValue == null) {
			return null;
		}
		return new CRMediaDirEntry(parent, reader, element);
	}


	void setMediType(int mediaType) {
		this.mediaType = mediaType;
	}

	public CRRuleBlock getRuleBlock() {
		return ruleBlock;
	}

	@Override
	public int hashCode() {
		int hashCode = CRMediaDirEntry.class.hashCode();
		
		hashCode = hashCode * 31 + mediaType;
		if (ruleBlock != null)
			hashCode = hashCode * 31 + ruleBlock.hashCode();  

		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof CRMediaDirEntry))
			return false;		
		CRMediaDirEntry o = (CRMediaDirEntry)obj;

		CRMediaMap map = getMap();
		CRMediaMap map2 = o.getMap();
		
		if (map != null && map2 != null && map != map2) {
			String mediaName1 = map.getEntries().get(mediaType).getMediaName();
			String mediaName2 = map2.getEntries().get(o.mediaType).getMediaName();
			if (mediaName1 == null || mediaName2 == null) {
				return false;
			}
			if (!mediaName1.equals(mediaName2)) {
				return false;
			}
		} else {
			if (mediaType != o.mediaType) {
				return false;
			}
		}
		

		return ruleBlock == null ? o.ruleBlock == null : ruleBlock.equals(o.ruleBlock);
	}

	public CRMediaMap getMap() {
		return parent == null ? null : parent.getMap();
	}

	public boolean match(CRMediaDirEntry entry) {
		if (entry == null)
			return false;

		CRMediaMap map = getMap();
		CRMediaMap map2 = entry.getMap();
		
		if (map != null && map2 != null && map != map2) {
			String mediaName1 = map.getEntries().get(mediaType).getMediaName();
			String mediaName2 = map2.getEntries().get(entry.mediaType).getMediaName();
			if (mediaName1 == null || mediaName2 == null) {
				return false;
			}
			if (!mediaName1.equals(mediaName2)) {
				return false;
			}
		} else {
			if (mediaType != entry.mediaType) {
				return false;
			}
		}

		return ruleBlock == null ? entry.ruleBlock == null : ruleBlock.match(entry.ruleBlock);
	}
}
