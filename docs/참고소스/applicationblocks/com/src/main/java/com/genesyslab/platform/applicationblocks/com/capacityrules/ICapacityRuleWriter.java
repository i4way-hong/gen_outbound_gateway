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

/**
 * Class provide dynamically growth output buffer 
 * with possibility update already written bytes
 */
interface ICapacityRuleWriter {
	
	/**
	 * Append one byte value.
	 * @param value byte that will appended
	 */
    void addByte(byte value);
    
    /**
	 * Update one byte value by a specified offset.
     * @param offset offset of byte that will updated
	 * @param value new byte value
     */
    void setByte(int offset, byte value);
    
    /**
	 * Append one integer value.
	 * @param value byte that will appended
     */
    void addInt(int value);
    
    /**
	 * Update one integer value by a specified offset.
     * @param offset offset of integer value that will updated
	 * @param value new integer value
     */
    void setInt(int offset, int value);
    
    /**
     * Gets copy off buffer.
     * @return byte buffer
     */
    byte[] getBuffer();
    
    
    /**
     * Gets buffer size.
     * @return buffer size
     */
    int size();
    
    /**
     * Remove all data stored in buffer (make buffer empty).
     */
    void reset();
}
