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

class ElementaryRule extends AbstractRule {
	
	private static final int CAPACITY_MIN = 1;
	private static final int CAPACITY_MAX = 255;
	
	private static final String ATTR_MEDIA = "media";
	private static final String ATTR_CAPACITY = "capacity";
	
	private String MediaName;
	private int MediaType = -1;
	private int Capacity;

	public ElementaryRule(AbstractRule parent, String mediaName, int mediaType, int capacity) 
		throws CapacityRuleException 
	{
		super(parent);
		if (mediaType < 0 || mediaType > 0xff) {
			throw new CapacityRuleException("invalid mediaType (0..255) : " + mediaType);
		}
		if (mediaName == null) {
			throw new CapacityRuleException("null media name");
		}
		if (capacity < CAPACITY_MIN || capacity > CAPACITY_MAX) {
			throw new CapacityRuleException("invalid capacity ("+CAPACITY_MIN+".."+CAPACITY_MAX+") : " + capacity);
		}
		MediaName = mediaName;
		MediaType = mediaType;
		Capacity = capacity;
	}


	public ElementaryRule(AbstractRule parent, Element elem) 
		throws CapacityRuleException 
	{
		super(parent);
		MediaName = elem.getAttribute(ATTR_MEDIA);
		if (MediaName == null) {
			throw new CapacityRuleException("no media name");
		}
		String sCapacity = elem.getAttribute(ATTR_CAPACITY);
		if (sCapacity == null) {
			throwInvalidCapacity(sCapacity); 
		}
		try {
			int capacity = Integer.parseInt(sCapacity);
			if (capacity < CAPACITY_MIN || capacity > CAPACITY_MAX) {
				throwInvalidCapacity(sCapacity); 
			}
			Capacity = capacity;
		} catch (NumberFormatException e) {
			throwInvalidCapacity(sCapacity); 
		}
	}
	
    private void throwInvalidCapacity(String val) 
    		throws CapacityRuleException
    {
      throw new CapacityRuleException("Capacity rule XML contains invalid attribute value. Attribute 'capacity', media name ='" + MediaName + "' value = '"+val+"'");
    }
	

	public int getCapacity() {
		return Capacity;
	}

	public int getMediaType() {
		return MediaType;
	}


	@Override
	public void assignMediaTypes(CRMediaMap map) throws CapacityRuleException 
	{
		if (MediaName == null) {
			MediaName = map.getEntries().get(MediaType).getMediaName();
			return;
		}
		
        for(CRMediaMapEntry entry : map.getEntries())
        {
          if (entry.getMediaName().equalsIgnoreCase(MediaName)) {
            MediaType = entry.getMediaType();
            if (Capacity > entry.MaxCapacity) {
            	entry.MaxCapacity = Capacity;
            }
            return;
          }
        }
        if (MediaName.length()==0) {
        	return; // MediaName
        }
        throw new CapacityRuleException("CapacityRule process error: Unknown media name: '"+MediaName+"'");
    }


	@Override
	public void expandAny(CRMediaMap map) throws CapacityRuleException {
        if (MediaName.length()>0) {
        	return;
        }
        invokeOnExpandAnyHandlers(this, map, Capacity);
	}


	@Override
	public boolean calculate(byte[] values) {
		return (values[MediaType] & 0xff) >= Capacity;
	}


	@Override
	public CRRuleBody createBinaryStructure(CRRuleBlockEntry parent) throws CapacityRuleException {
		ComplexCondition compexCond = new ComplexCondition(null, ComplexCondition.ConditionType.OR);
		compexCond.addRule(this);
		return compexCond.createBinaryStructure(parent);
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		ElementaryRule o = (ElementaryRule)super.clone();
		return o;
	}


	@Override
	public String toString() {
		return MediaName + ':' + Capacity;
	}


	public static ElementaryRule castOrNull(AbstractRule abstractRule) {
		return abstractRule instanceof ElementaryRule ? (ElementaryRule)abstractRule : null;
	}

}
