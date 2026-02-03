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

import com.genesyslab.platform.apptemplate.util.ConfigurationUtil;

import com.genesyslab.platform.commons.connection.tls.PropertyReader;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.util.*;


/**
 * Property reader that extracts option values from configuration objects. It is intended to be used
 * together with {@link com.genesyslab.platform.commons.connection.tls.TLSConfigurationParser}.
 * @see com.genesyslab.platform.commons.connection.tls.TLSConfigurationParser
 */
public class GConfigPropertyReader implements PropertyReader {


    private String defaultSectionName;

    protected IGApplicationConfiguration appConfig;
    protected IGApplicationConfiguration.IGPortInfo portConfig;
    protected IGApplicationConfiguration.IGAppConnConfiguration connConfig;
    protected IGApplicationConfiguration.IGHost hostConfig;

    private Map<String, String> optionsSnapshot = new HashMap<>();

    /**
     * Creates configuration reader for server application. Host configuration object is taken from
     * Application object property.
     * @param appConfig            Server Application configuration object
     * @param portConfig           Port configuration object
     * @param defaultSectionName   Name of section to look into for options, must not be null, can be empty string
     */
    public GConfigPropertyReader(
            final IGApplicationConfiguration appConfig,
            final IGApplicationConfiguration.IGPortInfo portConfig,
            final String defaultSectionName) {
        this.appConfig = appConfig;
        this.portConfig = portConfig;
        if ((null != appConfig) && (null != appConfig.getServerInfo())) {
            hostConfig = appConfig.getServerInfo().getHost();
        }
        this.defaultSectionName = defaultSectionName;
    }

    /**
     * Creates configuration reader for client application.
     * @param appConfig            Client Application configuration object
     * @param connConfig           Connection configuration object, which connects Client to target Server
     * @param defaultSectionName   Name of section to look into for options, must not be null, can be empty string
     */
    public GConfigPropertyReader(
            final IGApplicationConfiguration appConfig,
            final IGApplicationConfiguration.IGAppConnConfiguration connConfig,
            final String defaultSectionName) {
        this.appConfig = appConfig;
        this.connConfig = connConfig;
        if ((null != appConfig) && (null != appConfig.getServerInfo())) {
            hostConfig = appConfig.getServerInfo().getHost();
        }
        this.defaultSectionName = defaultSectionName;
    }

    /**
     * @deprecated use {@link #GConfigPropertyReader(IGApplicationConfiguration, IGApplicationConfiguration.IGAppConnConfiguration, String)}
     */
    @Deprecated
    public GConfigPropertyReader(
            final IGApplicationConfiguration appConfig,
            final IGApplicationConfiguration.IGAppConnConfiguration connConfig,
            final IGApplicationConfiguration targetServerConfig,
            final String defaultSectionName) {
        this(appConfig, connConfig, defaultSectionName);
    }


    void setOptionsReadableFromSameLayer(final Set<String> options) {

    	try {
			// 1. portConfig/connConfig
			if (null != portConfig) {
				for(String optionName : options) {
					String optionValue = getPortOption(optionName, portConfig);
					if (optionValue != null) {
						optionsSnapshot.put(optionName, optionValue);
					}
				}
			}
			else if (null != connConfig) {
				for(String optionName : options) {
					String optionValue = getConnOption(optionName, connConfig);
					if (optionValue != null) {
						optionsSnapshot.put(optionName, optionValue);
					}
				}
			}
			if (!optionsSnapshot.isEmpty()) {
				return;
			}

			if (null != appConfig) {
				// 2. appConfig options/section
				for(String optionName : options) {
					String optionValue = getAppOption(optionName, defaultSectionName, appConfig);
					if (optionValue != null) {
						optionsSnapshot.put(optionName, optionValue);
					}
				}
				if (!optionsSnapshot.isEmpty()) {
					return;
				}

				// 3. appConfig Annex/section
				for(String optionName : options) {
					String optionValue = getAppAnnexOption(optionName, defaultSectionName, appConfig);
					if (optionValue != null) {
						optionsSnapshot.put(optionName, optionValue);
					}
				}
				if (!optionsSnapshot.isEmpty()) {
					return;
				}
			}

			// 4. hostConfig Annex/section
			if (null != hostConfig) {
				for(String optionName : options) {
					String optionValue = getHostAnnexOption(optionName, defaultSectionName, hostConfig);
					if (optionValue != null) {
						optionsSnapshot.put(optionName, optionValue);
					}
				}
				if (!optionsSnapshot.isEmpty()) {
					return;
				}
			}
		}
		finally {
			for(String optionName : options) {
				optionsSnapshot.putIfAbsent(optionName, null);
			}
    	}
	}

	/**
	 * Combined code for both client and server cases.
	 * For client, it is expected that only <code>connConfig</code> and <code>appConfig</code> are non-<code>null</code>;
	 * for server, only <code>portConfig</code>, <code>appConfig</code> and <code>hostConfig</code>.
	 *
	 * @param optionName Name of option to find in the specified configuration objects
	 * @return Option value
	 */
	public String getProperty(final String optionName) {

		if (optionsSnapshot.containsKey(optionName)) {
			return optionsSnapshot.get(optionName);
		}

		String optionValue = null;

		// 1. portConfig/connConfig
		if (null != portConfig) {
			optionValue = getPortOption(optionName, portConfig);
		}
		else if (null != connConfig) {
			optionValue = getConnOption(optionName, connConfig);
		}

		if (null == optionValue) {
			if (null != appConfig) {
				// 2. appConfig options/section
				optionValue = getAppOption(optionName, defaultSectionName, appConfig);

				// 3. appConfig Annex/section
				if (null == optionValue) {
					optionValue = getAppAnnexOption(optionName, defaultSectionName, appConfig);
				}
			}
		}

		// 4. hostConfig Annex/section
		if (null == optionValue) {
			if (null != hostConfig) {
				optionValue = getHostAnnexOption(optionName, defaultSectionName, hostConfig);
			}
		}

		return optionValue;
	}

	public String getProperty(final OptionDescription optionDescription) {
		String optionValue = null;

		String optionName = optionDescription.getConfigOptionName();
		String sectionName = optionDescription.getSectionName();
		List<OptionDescription.OptionLocation> locations = optionDescription.getLocations();

		if (locations.contains(OptionDescription.OptionLocation.PORT_PARAMS) && (null != portConfig)) {
			optionValue = getPortOption(optionName, portConfig);
		}
		if (null != optionValue) {
			return optionValue;
		}

		if (locations.contains(OptionDescription.OptionLocation.CONN_PARAMS) && (null != connConfig)) {
			optionValue = getConnOption(optionName, connConfig);
		}
		if (null != optionValue) {
			return optionValue;
		}

		if (locations.contains(OptionDescription.OptionLocation.APP_OPTIONS) && (null != appConfig)) {
			optionValue = getAppOption(optionName, sectionName, appConfig);
		}
		if (null != optionValue) {
			return optionValue;
		}

		if (locations.contains(OptionDescription.OptionLocation.APP_ANNEX) && (null != appConfig)) {
			optionValue = getAppAnnexOption(optionName, sectionName, appConfig);
		}
		if (null != optionValue) {
			return optionValue;
		}

		if (locations.contains(OptionDescription.OptionLocation.HOST_ANNEX) && (null != hostConfig)) {
			optionValue = getHostAnnexOption(optionName, sectionName, hostConfig);
		}

		return optionValue;
	}

	/**
	 * First, looks into options for an entry with given section name and value of type <code>KeyValueCollection</code>.
	 * Then looks in that collection for an entry with name specified in <code>optionName</code> and
	 * returns value from the entry.
	 *
	 * @param optionName    Name of option to find
	 * @param options       KeyValueCollection containing specific section with options
	 * @return Value for the specified option
	 */
	public static String findSectionOption(
			final String optionName,
			final String sectionName,
			final KeyValueCollection options) {
		String result = null;
		if (null != options) {
			KeyValueCollection section = options.getList(sectionName);
			if (null != section) {
				result = section.getString(optionName);
			}
		}
		return result;
	}

	public static String getPortOption(
			final String optionName,
			final IGApplicationConfiguration.IGPortInfo portConfig) {
		return ConfigurationUtil.findTransportParameter(portConfig.getTransportParams(), optionName);
	}

	public static String getConnOption(
			final String optionName,
			final IGApplicationConfiguration.IGAppConnConfiguration connConfig) {
		return ConfigurationUtil.findTransportParameter(connConfig.getTransportParams(), optionName);
	}

	public static String getAppOption(
			final String optionName,
			final String sectionName,
			final IGApplicationConfiguration appConfig) {
		return findSectionOption(optionName, sectionName, appConfig.getOptions());
	}

	public static String getAppAnnexOption(
			final String optionName,
			final String sectionName,
			final IGApplicationConfiguration appConfig) {
		return findSectionOption(optionName, sectionName, appConfig.getUserProperties());
	}

	public static String getHostAnnexOption(
			final String optionName,
			final String sectionName,
			final IGApplicationConfiguration.IGHost hostConfig) {
		return findSectionOption(optionName, sectionName, hostConfig.getUserProperties());
	}
}
