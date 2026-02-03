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

import java.util.Arrays;


/**
 * 
 * @author mpopel
 *
 */
class CapacityRuleWriter implements ICapacityRuleWriter {
	private byte[] buffer;
	private int size;

	public CapacityRuleWriter() {
		buffer = new byte[1024];
	}

	private void ensureCapacity(int capacity) {
		if (size + capacity < buffer.length) {
			return;
		}
		// the capacity always less then the size in current implementation
		//   buffer = Arrays.copyOf(buffer, 2*size); 
		int newSize = size * 2;
		byte[] newBuffer = new byte[newSize];
		System.arraycopy(buffer, 0, newBuffer, 0, size);
		buffer = newBuffer; 
	}

	public void addByte(byte value) {
		ensureCapacity(1);
		buffer[size++] = value;
	}

	public void setByte(int offset, byte value) {
		buffer[offset] = value;
	}

	public void addInt(int value) {
		ensureCapacity(4);
		buffer[size++] = (byte) (value);
		buffer[size++] = (byte) (value>>>8);
		buffer[size++] = (byte) (value>>>16);
		buffer[size++] = (byte) (value>>>24);
	}

	public void setInt(int offset, int value) {
		buffer[offset++] = (byte) (value);
		buffer[offset++] = (byte) (value>>>8);
		buffer[offset++] = (byte) (value>>>16);
		buffer[offset] = (byte) (value>>>24);
	}

	public byte[] getBuffer() {
		byte[] newBuffer = new byte[size];
		System.arraycopy(buffer, 0, newBuffer, 0, size);
		return newBuffer; 
	}

	public int size() {
		return size;
	}

	public void reset() {
		size = 0;
	}
	
}
