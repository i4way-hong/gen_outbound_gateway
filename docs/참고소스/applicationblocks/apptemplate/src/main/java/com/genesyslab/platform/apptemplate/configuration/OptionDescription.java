// ===============================================================================
//  Genesys Platform SDK Application Blocks
// ===============================================================================
//
//  Any authorized distribution of any copy of this code (including any related
//  documentation) must reproduce the following restrictions, disclaimer and copyright
//  notice:
//
//  The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
//  (even as a part of another name), endorse and/or promote products derived from
//  this code without prior written permission from Genesys Telecommunications
//  Laboratories, Inc.
//
//  The use, copy, and/or distribution of this code is subject to the terms of the Genesys
//  Developer License Agreement.  This code shall not be used, copied, and/or
//  distributed under any other license agreement.
//
//  THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
//  ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
//  DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
//  WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
//  NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
//  NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
//  EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
//  CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
//  NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
//  Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.apptemplate.configuration;

import java.util.List;
import java.util.ArrayList;


/**
 * Contains definition of application option, including option name in ConnectionConfiguration, option name in
 * Config Object options list, section in Config Object option list, and a list of Configuration Objects which
 * may contain the option.
 * Immutable.
 */
public class OptionDescription {

	/**
	 * Supported locations for options.
	 * Precedence of option locations:
	 * 1. CONN_PARAMS/PORT_PARAMS
	 * 2. APP_OPTIONS
	 * 3. APP_ANNEX
	 * 4. HOST_ANNEX
	 */
	public enum OptionLocation {
		HOST_ANNEX, APP_OPTIONS, APP_ANNEX, PORT_PARAMS, CONN_PARAMS
	}

	// As appears in Config objects
	private String configOptionName;
	// As appears in ConnectionConfiguration
	private String optionName;
	private String sectionName;
	private List<OptionLocation> locations;

	/**
	 *
	 * @param configOptionName    Name of option as appears in Config objects
	 * @param optionName          Name of option as appears in ConnectionConfiguration
	 * @param sectionName         Section of Options or Annex to look into for the option
	 * @param locations           List of possible locations of the option. OptionDescription makes a copy of the list.
	 */
	protected OptionDescription(
			final String configOptionName,
			final String optionName,
			final String sectionName,
			final List<OptionLocation> locations) {
		this.configOptionName = configOptionName;
		this.optionName = optionName;
		this.sectionName = sectionName;
		this.locations = new ArrayList<OptionLocation>(locations);
	}

	public String getConfigOptionName() {
		return configOptionName;
	}

	public String getOptionName() {
		return optionName;
	}

	public String getSectionName() {
		return sectionName;
	}

	/**
	 * @return copy of locations list
	 */
	public List<OptionLocation> getLocations() {
		return new ArrayList<OptionLocation>(locations);
	}
}
