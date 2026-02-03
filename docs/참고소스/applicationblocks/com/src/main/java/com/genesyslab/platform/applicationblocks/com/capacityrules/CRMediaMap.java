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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

/**
 * 
 */
class CRMediaMap extends CapacityRuleAbstractElement 
						implements ICapacityRuleProcessElement {

	static final String TAG_NAME = "MediaTypes";
	private CRHeader parent;
	private final List<CRMediaMapEntry> entries = new ArrayList<CRMediaMapEntry>();
    private final HashMap<String, Integer> simpleMap = new HashMap<String, Integer>(0);

	public CRMediaMap(CRHeader parent) {
		super();
		this.parent = parent;
	}

	public CRMediaMap(CRHeader parent, ICapacityRuleReader reader) {
		super();
		this.parent = parent;
		reader.registerProcessElement(this);
	}

	public CRMediaMap(CRHeader parent, ByteBuffer buf, int positionBase) throws CapacityRuleException {
		this(parent);
		read(buf, positionBase);
	}


	public HashMap<String, Integer> getSimpleMediaMap() {
		return simpleMap;
	}


	/**
	 * Gets parent object. 
	 * @return parent object that contains it 
	 */
	@Override
	public CRHeader getParent() {
		return parent;
	}	


	/**
	 * Gets media type count
	 * @return media type count
	 */
	public int getMediaTypeCount() {
		return entries== null ? 0 : entries.size();
	}


	public List<CRMediaMapEntry> getEntries() {
		return entries;
	}

	/**
	 * Gets media type id by name.
	 * @param mediaName media name
	 * @return
	 * @throws CapacityRuleException 
	 */
	public int getMediaType(String mediaName) throws CapacityRuleException {
		if (entries == null) {
			throw new CapacityRuleException("No media types in capacity rules");
		}
		for(CRMediaMapEntry entry : entries) {
			if (entry.getMediaName().equalsIgnoreCase(mediaName)) {
				return entry.getMediaType(); 
			}
		}
		throw new CapacityRuleException("Unknown media name: '"+mediaName+"'");
	}

	public void onProcessElement(Object sender, Object args) throws CapacityRuleException {
		if (args instanceof EnterElementArgs) // enter element
		{
			EnterElementArgs eargs = (EnterElementArgs)args;
			Element element = eargs.getElement();
			if (element != null) {
				String localName = CRXmlUtils.getLocalName(element);
				if (localName.equalsIgnoreCase(CRMediaMapEntry.TAG_NAME)) {
					CRMediaMapEntry item = CRMediaMapEntry.create(this, element);
					add(item);
				}
			}
		}
		if (args instanceof LeaveElementArgs) // enter element
		{
			LeaveElementArgs eargs = (LeaveElementArgs)args;
			Element element = eargs.getElement();
			if (element != null) {
				if ((element.getLocalName().equalsIgnoreCase(TAG_NAME)) && (sender instanceof ICapacityRuleReader)) {
		            ((ICapacityRuleReader)sender).unregisterProcessElement(this);
				}
	        }
		}
	}

	private void add(CRMediaMapEntry entry) throws CapacityRuleException {
		if (entry != null) {
			if (null != simpleMap.put(entry.getMediaName(), entry.getMediaType())) {
				throw new CapacityRuleException("duplicate media map entry : " + entry.getMediaName());
			}
			entries.add(entry);
		}
	}


	/**
	 * Stores instance data.
	 * @param writer that will write data.
	 */
	public void write(ICapacityRuleWriter writer) {
		if (entries == null) {
			return;
		}

		int count = entries.size();
		// stores item count
		writer.addByte((byte)count);

		int offsetsTable = writer.size();
		for(int i=0; i<count; i++) {
			CRMediaMapEntry item = entries.get(i);
			writer.addByte((byte)item.getMediaType());
			writer.addInt(0xffffffff);	// reserves space for item's body global offset 
		}

		for(int i=0; i<count; i++) {
			CRMediaMapEntry item = entries.get(i);
			if (item != null) {
				// stores item's body global offset in reserved space
				writer.setInt(offsetsTable+i*5+1, writer.size());
				// stores item body
				item.write(writer);
			}
		}
	}

	private void read(ByteBuffer buf, int positionBase) throws CapacityRuleException {
		int count = buf.get() & 0xff;
		CRMediaMapEntry[] a = new CRMediaMapEntry[count];
		int index = 0;
		while(index < count) {
			int mediaType = buf.get() & 0xff;
			if (mediaType >= a.length) {
				throw new CapacityRuleException("invalid media rule type: " + mediaType + " ( index:" + index + " )");
			}
			if (a[mediaType] != null) {
				throw new CapacityRuleException("duplcate media rule type: " + mediaType + " ( index:" + index + " )");
			}

			int mediaOffset = buf.getInt() & 0x7fffffff;
			int posBackup = ((java.nio.Buffer) buf).position();

			try{
				((java.nio.Buffer) buf).position(positionBase + mediaOffset);
			}
			catch(IllegalArgumentException e) {
				throw new CapacityRuleException("invalid media rule entry offset: " + mediaOffset + " ( index:" + index + " )");
			}
			String mediaName;
			try{
				mediaName = readZEString(buf);
			}
			catch(Throwable e) {
				throw new CapacityRuleException("fail read media rule name. mediaType=" + mediaOffset + " ( index:" + index + " )");
			}
			CRMediaMapEntry entry = new CRMediaMapEntry(this, mediaName, mediaType);

			a[mediaType] = entry;

			add(entry);

			((java.nio.Buffer) buf).position(posBackup);

			index++;
		}
	}


	private String readZEString(ByteBuffer buf) {
		StringBuilder sb = new StringBuilder();
		while(true) {
			int c = buf.get()&0xff;
			if (c == 0) {
				break;
			}
			sb.append((char)c);
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		final String leftMargin = "  ";
		StringBuilder sb = new StringBuilder();
		sb.append(leftMargin).append("[CRMediaMap]\n");
		for(CRMediaMapEntry entry : entries) {
			sb.append(leftMargin).append(" ").append(entry).append('\n');
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		int hashCode = CRMediaMap.class.hashCode();
		if (entries != null) {
			for(CRMediaMapEntry entry : entries) {
				hashCode ^= entry.getMediaName().hashCode();  
			}
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CRMediaMap)) {
			return false;
		}
		CRMediaMap o = (CRMediaMap)obj;

		if (simpleMap != null) {
			if (simpleMap.size() != o.simpleMap.size()) {
				return false;
			}
			for(String key : simpleMap.keySet()) {
				if (!o.simpleMap.containsKey(key)) {
					return false;
				}
			}
		} else if (o.simpleMap != null) {
			return false;
		}
		return true;
	}
}
