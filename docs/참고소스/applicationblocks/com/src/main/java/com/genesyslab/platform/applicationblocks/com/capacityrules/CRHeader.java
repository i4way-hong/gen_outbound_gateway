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
import java.nio.ByteOrder;
import java.util.HashMap;

import org.w3c.dom.Element;


class CRHeader extends CapacityRuleAbstractElement 
						implements ICapacityRuleProcessElement {

	static final String TAG_NAME = "CapacityRule";
	static final int MAGIC_NUMBER = 5450453;
	private CRMediaMap mediaMap;
	private CRMediaDir mediaDir;
	private CapacityRule parent;


	CRHeader(ICapacityRuleReader reader, CapacityRule parent) {
		this.parent = parent;
		reader.registerProcessElement(this);
	}


	public CRHeader(byte[] binary) throws CapacityRuleException {
		ByteBuffer buf = ByteBuffer.wrap(binary);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		read(buf);
	}


	public HashMap<String, Integer> getSimpleMediaMap() {
		return mediaMap == null ? null : mediaMap.getSimpleMediaMap();
	}


	/**
	 * Gets parent object.
	 * @return parent object that contains it
	 */
	@Override
	public CapacityRule getParent() {
		return parent;
	}

	/**
	 * Get media directory
	 * @return
	 */
	public CRMediaDir getMediaDir() {
		return mediaDir;
	}

	/**
	 * Get media map
	 * @return
	 */
	public CRMediaMap getMediaMap() {
		return mediaMap;
	}

	/**
	 * @throws PlatformException
	 */
	public void onProcessElement(Object sender, Object args) throws CapacityRuleException {
		if (args instanceof EnterElementArgs)  { 
			Element element = ((EnterElementArgs)args).getElement();
			if (element != null) {
				String localName = CRXmlUtils.getLocalName(element);
				if (localName.equalsIgnoreCase(CRMediaMap.TAG_NAME)) {
					if (mediaMap == null) {
						mediaMap = new CRMediaMap(this, (ICapacityRuleReader)sender);
					}
				}
				if (localName.equalsIgnoreCase(CRMediaDir.TAG_NAME)) {
					if (mediaDir == null) {
						mediaDir = new CRMediaDir(this);
					}
					mediaDir.add(element, (ICapacityRuleReader)sender);
				}
			}
		}
		if (args instanceof LeaveElementArgs) {
			Element element = ((LeaveElementArgs)args).getElement();
			if (element != null) {
				String localName = element.getLocalName();
				if (localName.equalsIgnoreCase(TAG_NAME)) {
					ICapacityRuleReader reader = (ICapacityRuleReader)sender;
					if (reader != null) {
						reader.registerProcessElement(this);
					}
					process();
				}
			}
		}
	}

	void process() throws CapacityRuleException {
		if (mediaDir != null) {
			mediaDir.process(mediaMap);
		}
	}

	/**
	 * Stores instance data.
	 * @param writer that will write data.
	 */
	public void write(ICapacityRuleWriter writer) {
		writer.addInt(MAGIC_NUMBER);
		writer.addInt(0xffffffff); // reserve space for size
		writer.addInt(0xffffffff); // reserve space for media directory offset
		writer.addInt(0xffffffff); // reserve space for media map offset
		writer.addInt(0); // extension  offset

		if (mediaDir != null) {
			writer.setInt(8, writer.size()); // update media directory offset
			mediaDir.write(writer);
		}

		if (mediaMap != null) {
			writer.setInt(12, writer.size()); // update media map offset
			mediaMap.write(writer);
		}

		writer.setInt(4, writer.size()); // update size 
	}

	public void read(ByteBuffer buf) throws CapacityRuleException {

		int positionBase = ((java.nio.Buffer) buf).position();

		int mn = buf.getInt();
		if (mn != MAGIC_NUMBER) {
			throw new CapacityRuleException("invalid magic number: " + Integer.toHexString(mn));
		}
		int size = buf.getInt();
		if (size < 16) {
			throw new CapacityRuleException("invalid size: " + size);
		}

		int oMediaDir = buf.getInt();
		int oMediaMap = buf.getInt();

		if (oMediaMap > 0) {
			((java.nio.Buffer) buf).position(positionBase + oMediaMap);
			mediaMap = new CRMediaMap(this, buf, positionBase);
		}

		if (oMediaDir > 0) {
			((java.nio.Buffer) buf).position(positionBase + oMediaDir);
			mediaDir = new CRMediaDir(this, buf, positionBase);
		}
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		sb.append(" [CRHeader]");
//	      sb.append(" MagicNumber=").append(MAGIC_NUMBER).append("\n");;
	      if (mediaMap != null) {
	        sb.append(mediaMap);
	      }
	      if (mediaDir != null) {
	        sb.append(mediaDir);
	      }
	      return sb.toString();
	}

	@Override
	public int hashCode() {
		int hashCode = CRHeader.class.hashCode();
		if (mediaMap != null) {
			hashCode = 31*hashCode + mediaMap.hashCode();
		}
		if (mediaDir != null) {
			hashCode = 31*hashCode + mediaDir.hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CRHeader)) {
			return false;
		}
		CRHeader o = (CRHeader) obj;
		if (mediaMap != null) {
			if (!mediaMap.equals(o.mediaMap))
				return false;
		} else if (o.mediaMap != null) {
			return false;
		}
		if (mediaDir != null) {
			if (!mediaDir.equals(o.mediaDir))
				return false;
		} else if (o.mediaDir != null) {
			return false;
		}
		return true;
	}

	public boolean match(CRHeader h2) {
		if (mediaMap != null) {
			if (!mediaMap.equals(h2.mediaMap))
				return false;
		} else if (h2.mediaMap != null) {
			return false;
		}
		return mediaDir != null ? mediaDir.match(h2.mediaDir) : h2.mediaDir == null;
	}
}
