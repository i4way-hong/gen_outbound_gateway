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

import com.genesyslab.platform.commons.connection.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Contains list of options that are read by Application Template from Configuration Objects.
 * Options related to TLS, IPv6 and ADDP are excluded from this list, they have dedicated reading logic.
 * For uniformity reasons, option names for Config Objects are all lowercase with dashes (ASCII "minus" symbols)
 * used as separators.
 * Names used for ConnectionConfiguration do not have common style, this is a heritage from earlier PSDK versions.
 */
class PsdkDefaultOptions {

	public static final List<OptionDescription> OPTIONS;
	
	public static final String WSTimeout = "warm-standby.open-timeout";
	public static final String WSRetry_Delay = "warm-standby.retry-delay";
	public static final String WSReconnect_Rand_Delay = "warm-standby.reconnection-random-delay-range";
	public static final String WSBackup_Delay = "backup-delay";

	static {
		List<OptionDescription> l = new ArrayList<OptionDescription>();

		List<OptionDescription.OptionLocation> anyApp = new ArrayList<OptionDescription.OptionLocation>();
		anyApp.add(OptionDescription.OptionLocation.APP_OPTIONS);
		anyApp.add(OptionDescription.OptionLocation.CONN_PARAMS);
		anyApp.add(OptionDescription.OptionLocation.PORT_PARAMS);

		List<OptionDescription.OptionLocation> clientApp = new ArrayList<OptionDescription.OptionLocation>();
		clientApp.add(OptionDescription.OptionLocation.APP_OPTIONS);
		clientApp.add(OptionDescription.OptionLocation.CONN_PARAMS);

//		List<OptionDescription.OptionLocation> serverApp = new ArrayList<OptionDescription.OptionLocation>();
//		serverApp.add(OptionDescription.OptionLocation.APP_OPTIONS);
//		serverApp.add(OptionDescription.OptionLocation.PORT_PARAMS);

		// interface Connection
		final String connectionSection = "commons-connection";
		l.add(new OptionDescription("string-attributes-encoding", Connection.STR_ATTR_ENCODING_NAME_KEY, connectionSection, anyApp));
		l.add(new OptionDescription("lazy-parsing-enabled", Connection.LAZY_PARSING_ENABLED_KEY, connectionSection, anyApp));

		l.add(new OptionDescription("address", Connection.BIND_HOST_KEY, connectionSection, clientApp));
		l.add(new OptionDescription("port", Connection.BIND_PORT_KEY, connectionSection, clientApp));
		l.add(new OptionDescription("backup-port", Connection.BACKUP_BIND_PORT_KEY, connectionSection, clientApp));

		l.add(new OptionDescription("operation-timeout", Connection.OPERATION_TIMEOUT_KEY, connectionSection, anyApp));
		l.add(new OptionDescription("connection-timeout", Connection.CONNECTION_TIMEOUT_KEY, connectionSection, anyApp));
		l.add(new OptionDescription("reuse-address", Connection.REUSE_ADDRESS_KEY, connectionSection, anyApp));
		l.add(new OptionDescription("keep-alive", Connection.KEEP_ALIVE_KEY, connectionSection, anyApp));

		// class UniversalContactServerProtocol
		final String ucsSection = "ucs-protocol";
		// USE_UTF_FOR_RESPONSES
		l.add(new OptionDescription("use-utf-for-responses", "UseUtfForResponses", ucsSection, anyApp));
		// USE_UTF_FOR_REQUESTS
		l.add(new OptionDescription("use-utf-for-requests", "UTF_STRING", ucsSection, anyApp));

		// class WebmediaChannel
		final String wmSection = "webmedia-protocol";
		// OPTION_NAME_TARGET_XML_VERSION
		l.add(new OptionDescription("target-xml-version", "target-xml-version", wmSection, anyApp));
		// OPTION_NAME_REPLACE_ILLEGAL_UNICODE_CHARS
		l.add(new OptionDescription("replace-illegal-unicode-chars", "replace-illegal-unicode-chars", wmSection, anyApp));
		// OPTION_NAME_ILLEGAL_UNICODE_CHARS_REPLACEMENT
		l.add(new OptionDescription("illegal-unicode-chars-replacement", "illegal-unicode-chars-replacement", wmSection, anyApp));

		OPTIONS = Collections.unmodifiableList(l);
			
	}
	
}
