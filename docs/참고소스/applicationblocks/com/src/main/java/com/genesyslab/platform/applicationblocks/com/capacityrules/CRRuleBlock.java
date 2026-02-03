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
 * CRRuleBlock class is represent a rules block
 */
class CRRuleBlock  extends CapacityRuleAbstractElement {

	private CRMediaDirEntry parent;
	private final ArrayList<CRRuleBlockEntry> list = new ArrayList<CRRuleBlockEntry>();
	
	
	/**
	 * 
	 * @param parent
	 * @param reader
	 * @param element
	 */
	public CRRuleBlock(CRMediaDirEntry parent, ICapacityRuleReader reader, Element element) {
		super();
		this.parent = parent;
		addNewRule(reader, element);
	}

	public CRRuleBlock(CRMediaDirEntry parent, ByteBuffer buf, int positionBase) throws CapacityRuleException {
		super();
		this.parent = parent;
		read(buf, positionBase);
	}

	public List<CRRuleBlockEntry> getEntries() {
		return list;
	}

	
	/**
	 * Adds new rule. 
	 * @param reader
	 * @param element in which rule is described 
	 */
	public void addNewRule(ICapacityRuleReader reader, Element element) {
		String attrPiorityValue = element.getAttribute(CRMediaDirEntry.TAG_ATTRIBUTE_PRIORITY);
		CRRuleBlockEntry ruleBlock = new CRRuleBlockEntry(this, reader, attrPiorityValue == null ? "" : attrPiorityValue);
		list.add(ruleBlock);
	}
	
	/**
	 * Process all contained rule block entries
	 * @param map
	 */
	public void process(CRMediaMap map) throws CapacityRuleException {
		if (list != null) {
			for(CRRuleBlockEntry ruleBlockEntry : list) {
				ruleBlockEntry.process(map);
			}
		}
	}

	/**
	 * Gets parent object. 
	 * @return parent object that contains it 
	 */
	public Object getParent() {
		return parent;
	}

	/**
	 * Stores instance data.
	 * @param writer that will write data
	 */
	public void write(ICapacityRuleWriter writer) {
		if (list == null) {
			return;
		}
		
		int count = list.size();
		// stores item count
		writer.addByte((byte)count);
		
		int offsetsTable = writer.size();
		for(int i=0; i<count; i++) {
			CRRuleBlockEntry item = list.get(i);
			writer.addByte((byte)item.getPriority());
			writer.addInt(0xffffffff);	// reserves space for item's body global offset 
		}
		
		for(int i=0; i<count; i++) {
			CRRuleBlockEntry item = list.get(i);
			if (item != null) {
				// stores item's body global offset in reserved space
				writer.setInt(offsetsTable+i*5+1, writer.size());

				// stores item body
				item.write(writer);
			}
		}
	}

	private void read(ByteBuffer buf, int positionBase) throws CapacityRuleException{
		int count = buf.get() & 0xff;
		for(int i=0; i<count; i++) {
			int priority = buf.get() & 0xff;
			int entryOffset = buf.getInt();
			int posBackup = ((java.nio.Buffer) buf).position();
			try{
				((java.nio.Buffer) buf).position(positionBase + entryOffset);
			}catch(IllegalArgumentException e) {
				throw new CapacityRuleException("invalid rule block entry offset : " + entryOffset + " ( index:" + i + " )");
			}
			list.add(new CRRuleBlockEntry(this, priority, buf, positionBase));
			((java.nio.Buffer) buf).position(posBackup);
		}
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (list != null) {
			for(CRRuleBlockEntry item : list) {
				sb.append(" ").append(item).append("\n");
			}
		}
		return sb.toString();
	}

	
	@Override
	public int hashCode() {
		int hashCode = CRRuleBlock.class.hashCode();

		for(CRRuleBlockEntry entry : list) {
			hashCode = hashCode*31 + entry.hashCode(); 
		}

		return hashCode;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof CRRuleBlock))
			return false;		
		CRRuleBlock o = (CRRuleBlock)obj;

		if (list.size() != o.list.size()) {
			return false;
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<CRRuleBlockEntry> tmp = (ArrayList<CRRuleBlockEntry>)o.list.clone();
		
		entry:
		for(CRRuleBlockEntry entry : list) {
			int size = tmp.size();
			for(int j=0; j<size; j++) {
				CRRuleBlockEntry entry2 = tmp.get(j);
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
		return parent != null ? parent.getMap() : null;
	}

	public boolean match(CRRuleBlock ruleBlock) {
		if (ruleBlock == null)
			return false;
		if (list.size() != ruleBlock.list.size()) {
			return false;
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<CRRuleBlockEntry> tmp = (ArrayList<CRRuleBlockEntry>)ruleBlock.list.clone();
		
		entry:
		for(CRRuleBlockEntry entry : list) {
			int size = tmp.size();
			for(int j=0; j<size; j++) {
				CRRuleBlockEntry entry2 = tmp.get(j);
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
