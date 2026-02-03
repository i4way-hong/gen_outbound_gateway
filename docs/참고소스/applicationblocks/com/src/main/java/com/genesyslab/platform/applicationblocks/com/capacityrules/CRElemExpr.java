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
 * CRElemExpr class is represent a simple expression.
 */
final class CRElemExpr extends CapacityRuleAbstractElement  {
	private CRAndExprBlock Parent;
	int mediaType;
	int capacity;

	/**
	 * Create a instance of CRElemExpr class.
	 * @param parent object that will contains it 
	 */
	public CRElemExpr(CRAndExprBlock parent) {
		super();
		Parent = parent;
	}

	public CRElemExpr(CRAndExprBlock ab, int capacity, int mediaType) 
		throws CapacityRuleException
	{
		if (mediaType < 0 || mediaType > 0xff) {
			throw new CapacityRuleException("invalid mediaType (0..255) : " + mediaType);
		}
		if (capacity < 1 || capacity > 0xff) {
			throw new CapacityRuleException("invalid capacity (1..255) : " + capacity);
		}
		this.mediaType = mediaType;
		this.capacity = capacity;
	}

	public CRElemExpr(CRAndExprBlock parent, ByteBuffer buf) {
		this(parent);
		read(buf);
	}

	/**
	 * Gets parent object. 
	 * @return parent object that contains it 
	 */
	public CRAndExprBlock getParent() {
		return Parent;
	}

	/**
	 * Stores the instance data.
	 * @param writer that will write data 
	 */
	public void write(ICapacityRuleWriter writer) {
		writer.addByte((byte)mediaType);
		writer.addByte((byte)capacity);
	}

	private void read(ByteBuffer buf) {
		mediaType = buf.get() & 0xff;
		capacity = buf.get() & 0xff;
	}

	@Override
	public int hashCode() {
		int h = CRElemExpr.class.hashCode();
		h = h*31 + mediaType;
		h = h*31 + capacity;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof CRElemExpr))
			return false;		
		CRElemExpr o = (CRElemExpr)obj;
		
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
		}
		else {
			if (mediaType != o.mediaType) {
				return false;
			}
		}
		
		if (capacity != o.capacity) {
			return false;
		}
		return true;
	}

	
	private CRMediaMap getMap() {
		return Parent != null ? Parent.getMap() : null;
	}

	@Override
	public String toString() {
		CRMediaMap map = getMap();
		return (map != null ? map.getEntries().get(mediaType).getMediaName() :  "#"+mediaType) + ">" + capacity;  
	}

	
	
}
