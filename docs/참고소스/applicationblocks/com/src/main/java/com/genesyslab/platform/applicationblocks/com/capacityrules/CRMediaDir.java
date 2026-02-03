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
import java.util.List;

import org.w3c.dom.Element;

/**
 * 
 * @author mpopel
 *
 */
class CRMediaDir extends CapacityRuleAbstractElement {

	static final String TAG_NAME = "MediaRule";
	private CRHeader parent;
	private final ArrayList<CRMediaDirEntry> entries = new ArrayList<CRMediaDirEntry>();	

	/**
	 * 
	 * @param parent
	 */
	public CRMediaDir(CRHeader parent) {
		super();
		this.parent = parent;
	}

	public CRMediaDir(CRHeader parent, ByteBuffer buf, int positionBase) throws CapacityRuleException {
		this(parent);
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


	public List<CRMediaDirEntry> getEntries() {
		return entries;
	}

	public void add(Element element, ICapacityRuleReader reader) {
		String attrNValue = element.getAttribute(CRMediaDirEntry.TAG_ATTRIBUTE_NAME);
		if (attrNValue == null) {
			return;
		}
		
		CRMediaDirEntry dirEntry = null;
		for(CRMediaDirEntry entry : entries) {
			if (!entry.getMediaName().equalsIgnoreCase(attrNValue)) {
				continue;
			}
			dirEntry = entry;
			break;	
		}
		if (dirEntry == null) {
			CRMediaDirEntry item = CRMediaDirEntry.create(this, reader, element);
			if (item != null) {
				entries.add(item);
			}
			return;
		}
		dirEntry.addNewRuleBlock(reader, element);
	}

	/**
	 * 
	 * @param map
	 * @throws CapacityRuleException 
	 */
	void process(CRMediaMap map) throws CapacityRuleException {
		if (entries == null) {
			return;
		}
		for(CRMediaDirEntry entry : entries) {
			entry.updateMediaMapType(map);
		}
		for(CRMediaDirEntry entry : entries) {
			entry.process(map);
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
			CRMediaDirEntry item = entries.get(i);
			writer.addByte((byte)item.getMediaType());
			writer.addInt(0xffffffff);	// reserves space for item's body global offset 
		}

		for(int i=0; i<count; i++) {
			CRMediaDirEntry item = entries.get(i);
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
		for (int index=0; index<count; index++) {
			int mediaType = buf.get() & 0xff;
			int itemOffset = buf.getInt();
			int posBackup = ((java.nio.Buffer) buf).position();
			try {
				((java.nio.Buffer) buf).position(positionBase + itemOffset);
			} catch (IllegalArgumentException E) {
				throw new CapacityRuleException("invalid media dir entry offset: " + itemOffset + " ( index:" + index + " )");
			}
			add(new CRMediaDirEntry(this, mediaType, buf, positionBase));
			((java.nio.Buffer) buf).position(posBackup);		
		}
	}

	private void add(CRMediaDirEntry item) {
		entries.add(item);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		sb.append("  [MediaDir]\n");
		if (entries != null) {
			for(CRMediaDirEntry entry : entries) {
				sb.append(entry);
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		int hashCode = CRMediaDir.class.hashCode();

		for(CRMediaDirEntry entry : entries) {
			hashCode = hashCode*31 + entry.hashCode(); 
		}

		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof CRMediaDir))
			return false;		
		CRMediaDir o = (CRMediaDir)obj;

		if (entries.size() != o.entries.size()) {
			return false;
		}

		@SuppressWarnings("unchecked")
		ArrayList<CRMediaDirEntry> tmp = (ArrayList<CRMediaDirEntry>)o.entries.clone();

		entry:
		for(CRMediaDirEntry entry : entries) {
			int size = tmp.size();
			for(int j=0; j<size; j++) {
				CRMediaDirEntry entry2 = tmp.get(j);
				if (entry.equals(entry2)) {
					tmp.remove(j);
					continue entry;
				}
			}
			return false;
		}
		return true;
	}

	public CRMediaMap getMap() {
		return parent == null ? null : parent.getMediaMap();
	}

	public boolean match(CRMediaDir mediaDir) {
		if (mediaDir == null) {
			return false;
		}
		if (entries.size() != mediaDir.entries.size()) {
			return false;
		}
		ArrayList<CRMediaDirEntry> tmp = (ArrayList<CRMediaDirEntry>)mediaDir.entries.clone();
		
		entry:
		for(CRMediaDirEntry entry : entries) {
			int size = tmp.size();
			for(int j=0; j<size; j++) {
				CRMediaDirEntry entry2 = tmp.get(j);
				if (entry.match(entry2)) {
					tmp.remove(j);
					continue entry;
				}
			}
			return false;
		}
		return true;
	}
}
