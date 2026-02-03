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


/**
 * CRRuleBody class is represent a logical OR expression of AND expressions
 */
class CRRuleBody extends CapacityRuleAbstractElement {
	private CRRuleBlockEntry parent;
	private ArrayList<CRAndExprBlock> list = new ArrayList<CRAndExprBlock>();
	

	/**
	 * Create a instance of CRRuleBody class.
	 * @param parent object that will contains it 
	 */
	public CRRuleBody(CRRuleBlockEntry parent) {
		super();
		this.parent = parent;
	}


	public CRRuleBody(CRRuleBlockEntry parent, ByteBuffer buf, int positionBase)
			throws CapacityRuleException {
		this(parent);
		read(buf, positionBase);
	}


	ArrayList<CRAndExprBlock> getList() {
		return list;
	}


	/**
	 * Gets parent object. 
	 * @return parent object that contains it 
	 */
	public CRRuleBlockEntry getParent() {
		return parent;
	}


	public void add(CRAndExprBlock exprBlock) {
		list.add(exprBlock);
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
		
		// reserves a space for a table of item's global offsets 
		int offsetsTable = writer.size();
		for(int i=0; i<count; i++) {
			writer.addInt(0xffffffff);		// fill invalid offset
		}
		
		for(int i=0; i<count; i++) {
			CRAndExprBlock elem = list.get(i);
			
			// stores item's global offset in reserved offsets table
			writer.setInt(offsetsTable+i*4, writer.size());
			
			// stores item 
			if (elem != null) {
				elem.write(writer);
			}
		}
	}

	private void read(ByteBuffer buf, int positionBase) throws CapacityRuleException {
		int count = buf.get() & 0xff;
		for(int index=0; index<count; index++) {
			int itemOffset = buf.getInt();
			int posBackup = ((java.nio.Buffer) buf).position();
			try {
				((java.nio.Buffer) buf).position(positionBase + itemOffset);
			} catch(IllegalArgumentException ex) {
				throw new CapacityRuleException("invalid rule body item offset : " + itemOffset + " ( index:" +index + " )" );
			}
			CRAndExprBlock item = new CRAndExprBlock(this, buf);
			add(item);
			((java.nio.Buffer) buf).position(posBackup);
		}
	}

	@Override
	public int hashCode() {
		int hashCode = CRMediaDir.class.hashCode();
		for(CRAndExprBlock entry : list) {
			hashCode = hashCode*31 + entry.hashCode(); 
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof CRRuleBody))
			return false;		
		CRRuleBody o = (CRRuleBody)obj;

		if (list.size() != o.list.size()) {
			return false;
		}

		@SuppressWarnings("unchecked")
		ArrayList<CRAndExprBlock> tmp = (ArrayList<CRAndExprBlock>)o.list.clone();

		entry:
		for(CRAndExprBlock entry : list) {
			int size = tmp.size();
			for(int j=0; j<size; j++) {
				CRAndExprBlock entry2 = tmp.get(j);
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
		return parent == null ? null : parent.getMap();
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OR");
		sb.append('(');
		for(CRAndExprBlock expr : list) {
			sb.append(expr);
			sb.append(',');
		}
		if (!list.isEmpty()) {
			sb.setLength(sb.length()-1);
		}
		sb.append(')');
		return sb.toString();
	}
}
