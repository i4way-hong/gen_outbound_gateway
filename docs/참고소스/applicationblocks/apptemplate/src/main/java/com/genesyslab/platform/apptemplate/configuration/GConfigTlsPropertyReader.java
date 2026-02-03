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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Property reader that extracts TLS-related option values from configuration objects. It is intended to be used
 * together with {@link com.genesyslab.platform.commons.connection.tls.TLSConfigurationParser}.
 *
 * <p>Searches application, host, and port or connection objects for TLS-related options and provides values to
 * TLSConfigurationParser.</p>
 * <p>Section names, option names and specific values are taken from Genesys 8.0 Security Deployment Guide</p>
 *
 * <p>Example for server side:<example><code><pre>
 * String appName = "&lt;my-app-name&gt;";
 * CfgApplication cfgApplication = confService.retrieveObject(
 *         CfgApplication.class, new CfgApplicationQuery(appName));
 * GCOMApplicationConfiguration appConfiguration =
 *         new GCOMApplicationConfiguration(cfgApplication);
 * IGApplicationConfiguration.IGPortInfo portConfig =
 *         appConfiguration.getPortInfo("secure");
 * TLSConfiguration tlsConfiguration = TLSConfigurationParser.parseTlsConfiguration(
 *         new <b>GConfigTlsPropertyReader</b>(appConfiguration, portConfig), <b>false</b>);
 * </pre></code></example></p>
 *
 * <p>Example for client side:<example><code><pre>
 * String appName = "&lt;my-app-name&gt;";
 * CfgApplication cfgApplication = confService.retrieveObject(
 *         CfgApplication.class, new CfgApplicationQuery(appName));
 * GCOMApplicationConfiguration appConfiguration =
 *         new GCOMApplicationConfiguration(cfgApplication);
 * IGApplicationConfiguration.IGAppConnConfiguration connConfig =
 *         appConfiguration.getAppServer(CfgAppType.CFGTServer);
 * TLSConfiguration tlsConfiguration = TLSConfigurationParser.parseTlsConfiguration(
 *         new <b>GConfigTlsPropertyReader</b>(appConfiguration, connConfig), <b>true</b>);
 * </pre></code></example></p>
 *
 * @see com.genesyslab.platform.commons.connection.tls.TLSConfigurationParser
 */
public class GConfigTlsPropertyReader extends GConfigPropertyReader {

	private static final String SECURITY_SECTION_NAME = "security";
	private static final String TLS_KEY_NAME = "tls";

	private static final Set<String> SECURITY_OPTIONS = new HashSet<>(
			Arrays.asList(
//					TLS_KEY_NAME,
					"provider",
					"certificate",
					"certificate-key",
					"trusted-ca",
//					"tls-mutual",
//					"tls-crl",
//					"tls-target-name-check",
					"fips140-enabled"
//					"cipher-list",
//					"sec-protocol",
//					"tls-version",
//					"protocol-list"
			)
	);


	/**
	 * Creates configuration reader for server application. Host configuration object is taken from
	 * Application object property.
	 * @param appConfig     Server Application configuration object
	 * @param portConfig    Port configuration object
	 */
	public GConfigTlsPropertyReader(IGApplicationConfiguration appConfig,
			IGApplicationConfiguration.IGPortInfo portConfig)
	{
		super(appConfig, portConfig, SECURITY_SECTION_NAME);
		setOptionsReadableFromSameLayer(SECURITY_OPTIONS);
	}

	/**
	 * Creates configuration reader for client application. Host configuration object, if available,
	 * is taken from Application object property.
	 * @param appConfig     Client Application configuration object
	 * @param connConfig    Connection configuration object, which connects Client to target Server
	 */
	public GConfigTlsPropertyReader(IGApplicationConfiguration appConfig,
			IGApplicationConfiguration.IGAppConnConfiguration connConfig)
	{
		super(appConfig, connConfig, SECURITY_SECTION_NAME);
		setOptionsReadableFromSameLayer(SECURITY_OPTIONS);
	}

	/**
	 * @deprecated use {@link #GConfigTlsPropertyReader(IGApplicationConfiguration, IGApplicationConfiguration.IGAppConnConfiguration)}
	 */
    @Deprecated
    public GConfigTlsPropertyReader(IGApplicationConfiguration appConfig,
            IGApplicationConfiguration.IGAppConnConfiguration connConfig,
            IGApplicationConfiguration targetServerConfig)
    {
        this(appConfig, connConfig);
    }
	
	public String getProperty(String optionName) {
		String value = super.getProperty(optionName);

		if (TLS_KEY_NAME.equalsIgnoreCase(optionName)) {
			boolean tls = ConfigurationUtil.isTrue(value) || isTargetPortSecure();
			value = Boolean.toString(tls);
		}
		return value;
	}

	/**
	 * Works only for client options (if <code>connConfig</code> is specified).
	 * Looks into connection server port properties to determine if TLS should be used to connect to it.
	 *
	 * @return <code>true</code> if the connection port has property <code>tls</code> set to 1, <code>false</code> otherwise
	 */
	private boolean isTargetPortSecure() {
		if (null == connConfig) {
			return false;
		}

		final IGApplicationConfiguration server = connConfig.getTargetServerConfiguration();
		if (null == server) {
			return false;
		}

		List<IGApplicationConfiguration.IGPortInfo> ports = server.getPortInfos();
		if (null == ports) {
			return false;
		}

		final String portId = connConfig.getPortId();
		IGApplicationConfiguration.IGPortInfo port = null;
		for (IGApplicationConfiguration.IGPortInfo p : ports) {
			if (p.getId().equals(portId)) {
				port = p;
				break;
			}
		}

		return (null != port) && ConfigurationUtil.isTrue(
				ConfigurationUtil.findTransportParameter(port.getTransportParams(), TLS_KEY_NAME));
	}
}
