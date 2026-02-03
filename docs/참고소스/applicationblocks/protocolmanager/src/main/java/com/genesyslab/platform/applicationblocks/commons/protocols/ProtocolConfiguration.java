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
//  Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.applicationblocks.commons.protocols;

import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.PsdkCustomization.PsdkOption;
import com.genesyslab.platform.commons.connection.tls.SSLExtendedOptions;
import com.genesyslab.platform.commons.protocol.UriInfo;

import javax.net.ssl.SSLContext;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Protocol Manager Application Block is deprecated.
 *
 * @see com.genesyslab.platform.commons.connection.configuration.ClientConnectionOptions
 * @see com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions
 * @see com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration
 * @see com.genesyslab.platform.commons.connection.configuration.KeyValueConfiguration
 * @see com.genesyslab.platform.commons.protocol.Endpoint
 * @see com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration
 * @see com.genesyslab.platform.apptemplate.configuration.ClientConfigurationHelper
 * @deprecated
 * Use {@link com.genesyslab.platform.commons.protocol.Endpoint Endpoint} to manage protocol's configuration.
 */
@Deprecated
public abstract class ProtocolConfiguration {

    private Class protocolType;
    private String name;
    private UriInfo uriInfo;
    private FaultToleranceMode faultTolerance;
    private UriInfo warmStandbyUriInfo;
    private Short warmStandbyAttempts;
    private Short warmStandbySwitchovers;
    private Integer warmStandbyTimeout;
    private Integer addpClientTimeout;
    private Integer addpServerTimeout;
    private Boolean useAddp = Boolean.FALSE;
    private String addpTrace;
    private String localBindingHost;
    private Integer localBindingPort;
	private boolean primaryTLSEnabled;
	private SSLContext primarySSLContext;
	private SSLExtendedOptions primarySSLOptions;
	private boolean backupTLSEnabled;
	private SSLContext backupSSLContext;
	private SSLExtendedOptions backupSSLOptions;

    protected ProtocolConfiguration(
            final String name, final Class protocolType) {
        if (name == null || name.length()==0) {
            throw new IllegalArgumentException("name: Name is null or empty.");
        }
        if (protocolType == null) {
            throw new NullPointerException(
                    "protocolType: ProtocolType is null.");
        }
        this.name = name;
        this.protocolType = protocolType;
    }


    protected final Class getProtocolType() {
        return this.protocolType;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(final String value) {
        if (value == null || value.length()==0) {
            throw new NullPointerException("value: Value is null or empty.");
        }
        this.name = value;
    }

    public final URI getUri() {
        UriInfo data = uriInfo;
        if (data != null) {
            return data.uri;
        }
        return null;
    }

    public final void setUri(final URI value) {
        if (value == null) {
            throw new NullPointerException("value: Value is null.");
        }
        this.uriInfo = UriInfo.get(value);
    }

    public final void setUri(final String host, final int port) {
        this.uriInfo = UriInfo.get(createURI(host, port));
    }

    /**
     * Get the standby URI for Warm Standby.
     *
     * @return standby URI
     */
    public final URI getWarmStandbyUri() {
        UriInfo data = this.warmStandbyUriInfo;
        if (data != null) {
            return data.uri;
        }
        return null;
    }

    /**
     * Set the standby URI for Warm Standby.
     *
     * @param value URI for the standby endpoint
     */
    public final void setWarmStandbyUri(final URI value) {
        if (value == null) {
            throw new NullPointerException("warmStandbyUri: Value is null.");
        }
        this.warmStandbyUriInfo = UriInfo.get(value);
    }

    /**
     * Set the standby URI for Warm Standby, using the host and port as parameters.
     *
     * @param host host for the standby URI
     * @param port port number for the standby URI
     */
    public final void setWarmStandbyUri(
            final String host, final int port) {
        this.warmStandbyUriInfo = UriInfo.get(createURI(host, port));
    }

    /**
     * Get the number of reconnection attempts for Warm Standby.
     *
     * @return the maximum number of reconnection attempts
     */
    public final Short getWarmStandbyAttempts() {
        return this.warmStandbyAttempts;
    }

    /**
     * Set the number of reconnection attempts for Warm Standby.
     *
     * @param value the maximum number of reconnection attempts
     */
    public final void setWarmStandbyAttempts(final Short value) {
            this.warmStandbyAttempts = value;
    }

    /**
     * Get the number of primary/backup switchovers for Warm Standby.
     *
     * @return the number of allowed primary/backup switchovers
     */
    public final Short getWarmStandbySwitchovers() {
        return this.warmStandbySwitchovers;
    }

    /**
     * Set the number of primary/backup switchovers for Warm Standby. If this parameter
     * is not set, or if it is set to null, then an unlimited number of switchovers
     * are allowed. If this parameter is set to 0 then switchover will not happen.
     *
     * @param value the number of allowed primary/backup switchovers
     */
    public final void setWarmStandbySwitchovers(final Short value) {
        this.warmStandbySwitchovers = value;
    }

    /**
     * Get the timeout (in milliseconds) between the reconnection attempts for Warm Standby.
     *
     * @return the current timeout value (in milliseconds)
     */
    public final Integer getWarmStandbyTimeout() {
        return this.warmStandbyTimeout;
    }

    /**
     * Set the new timeout value (in milliseconds) between the reconnection
     * attempts for Warm Standby.
     *
     * @param value the new timeout value (in milliseconds)
     */
    public final void setWarmStandbyTimeout(final Integer value) {
        this.warmStandbyTimeout = value;
    }

    public final Integer getAddpClientTimeout() {
        return this.addpClientTimeout;
    }

    public final void setAddpClientTimeout(final Integer value) {
        this.addpClientTimeout = value;
    }

    public final Integer getAddpServerTimeout() {
        return this.addpServerTimeout;
    }

    public final void setAddpServerTimeout(final Integer value) {
        this.addpServerTimeout = value;
    }

    public final Boolean isUseAddp() {
        return this.useAddp;
    }

    public final void setUseAddp(final Boolean value) {
        this.useAddp = value;
    }

    public final String getAddpTrace() {
        return this.addpTrace;
    }

    public final void setAddpTrace(final String value) {
        this.addpTrace = value;
    }

    public final FaultToleranceMode getFaultTolerance() {
        return this.faultTolerance;
    }

    public final void setFaultTolerance(final FaultToleranceMode value) {
        this.faultTolerance = value;
    }

    /**
     * @deprecated TLS is configured separately with SSLContext and SSLEngine
	 * @return {@code true} if both {@link #primaryTLSEnabled} and {@link #backupTLSEnabled} are set to {@code true};
	 * {@code false} otherwise.
     * @see com.genesyslab.platform.commons.connection.tls.SSLContextHelper SSLContextHelper
     * @see com.genesyslab.platform.commons.protocol.AbstractChannel#connectionContext()
     *          AbstractChannel.connectionContext()
     * @see com.genesyslab.platform.commons.connection.configuration.ConnectionContext
     *          ConnectionContext
     * @see com.genesyslab.platform.commons.connection.configuration.ConnectionContext#setAttribute(String, Object)
     *          ConnectionContext.setAttribute(String, Object)
     * @see com.genesyslab.platform.commons.connection.configuration.ConnectionContext#SSL_CONTEXT_KEY
     *          ConnectionContext.SSL_CONTEXT_KEY
     */
    @Deprecated
    public Boolean isUseTls() {
        return primaryTLSEnabled && backupTLSEnabled;
    }

    /**
	 * Sets value of {@code useTls} to both {@link #primaryTLSEnabled} and {@link #backupTLSEnabled}.
     * @deprecated TLS is configured separately with SSLContext and SSLEngine
     * @see com.genesyslab.platform.commons.connection.tls.SSLContextHelper SSLContextHelper
     * @see com.genesyslab.platform.commons.protocol.AbstractChannel#connectionContext()
     *          AbstractChannel.connectionContext()
     * @see com.genesyslab.platform.commons.connection.configuration.ConnectionContext
     *          ConnectionContext
     * @see com.genesyslab.platform.commons.connection.configuration.ConnectionContext#setAttribute(String, Object)
     *          ConnectionContext.setAttribute(String, Object)
     * @see com.genesyslab.platform.commons.connection.configuration.ConnectionContext#SSL_CONTEXT_KEY
     *          ConnectionContext.SSL_CONTEXT_KEY
     */
    @Deprecated
    public void setUseTls(Boolean useTls) {
		this.primaryTLSEnabled = useTls;
		this.backupTLSEnabled = useTls;
    }

    public String getLocalBindingHost() {
        return localBindingHost;
    }

    public void setLocalBindingHost(String localBindingHost) {
        this.localBindingHost = localBindingHost;
    }

    public Integer getLocalBindingPort() {
        return localBindingPort;
    }

    public void setLocalBindingPort(Integer localBindingPort) {
        this.localBindingPort = localBindingPort;
    }

	public Boolean isPrimaryTLSEnabled() {
		return primaryTLSEnabled;
	}

	/**
	 * Allows/disables immediate TLS turning on upon connection to primary server.
	 * @param primaryTLSEnabled    If {@code true}, TLS will be immediately turned on upon connection.
	 *                             If {@code false}, TLS is not turned on by default, but
	 *                             can be turned on if autodetect feature is used.
	 * @see	#setFaultTolerance(FaultToleranceMode)
	 * @see #setUri(java.net.URI)
	 * @see #setUri(String, int)
	 */
	public void setPrimaryTLSEnabled(Boolean primaryTLSEnabled) {
		this.primaryTLSEnabled = primaryTLSEnabled;
	}

	public SSLContext getPrimarySSLContext() {
		return primarySSLContext;
	}

	/**
	 * Sets {@code SSLContext} instance for default server connection and/or warm standby
	 * primary connection.
	 * @param primarySSLContext  {@code SSLContext} to use for connection.
	 * @see	#setFaultTolerance(FaultToleranceMode)
	 * @see #setUri(java.net.URI)
	 * @see #setUri(String, int)
	 */
	public void setPrimarySSLContext(SSLContext primarySSLContext) {
		this.primarySSLContext = primarySSLContext;
	}

	public SSLExtendedOptions getPrimarySSLOptions() {
		return primarySSLOptions;
	}

	/**
	 * Sets additional SSL parameters for default server connection and/or warm standby
	 * primary connection.
	 * @param primarySSLOptions    SSLExtendedOptions to use for connection.
	 * @see	#setFaultTolerance(FaultToleranceMode)
	 * @see #setUri(java.net.URI)
	 * @see #setUri(String, int)
	 */
	public void setPrimarySSLOptions(SSLExtendedOptions primarySSLOptions) {
		this.primarySSLOptions = primarySSLOptions;
	}

	public Boolean isBackupTLSEnabled() {
		return backupTLSEnabled;
	}

	/**
	 * Allows/disables immediate TLS turning on upon connection to backup server.
	 * @param backupTLSEnabled    If {@code true}, TLS will be immediately turned on upon connection.
	 *                            If {@code false}, TLS is not turned on by default, but
	 *                            can be turned on if autodetect feature is used.
	 * @see	#setFaultTolerance(FaultToleranceMode)
	 * @see	#setWarmStandbyUri(java.net.URI)
	 * @see	#setWarmStandbyUri(String, int)
	 */
	public void setBackupTLSEnabled(Boolean backupTLSEnabled) {
		this.backupTLSEnabled = backupTLSEnabled;
	}

	public SSLContext getBackupSSLContext() {
		return backupSSLContext;
	}

	/**
	 * Sets {@code SSLContext} instance to be used for warm standby backup connection.
	 * @param backupSSLContext    {@code SSLContext} to use for backup connection.
	 * @see	#setFaultTolerance(FaultToleranceMode)
	 * @see	#setWarmStandbyUri(java.net.URI)
	 * @see	#setWarmStandbyUri(String, int)
	 */
	public void setBackupSSLContext(SSLContext backupSSLContext) {
		this.backupSSLContext = backupSSLContext;
	}

	public SSLExtendedOptions getBackupSSLOptions() {
		return backupSSLOptions;
	}

	/**
	 * Sets additional SSL parameters for warm standby backup connection.
	 * @param backupSSLOptions    SSLExtendedOptions to use for backup connection.
	 * @see	#setFaultTolerance(FaultToleranceMode)
	 * @see	#setWarmStandbyUri(java.net.URI)
	 * @see	#setWarmStandbyUri(String, int)
	 */
	public void setBackupSSLOptions(SSLExtendedOptions backupSSLOptions) {
		this.backupSSLOptions = backupSSLOptions;
	}

	private URI createURI(final String host, final int port) {
        try {
            if (PsdkCustomization.getBoolOption(PsdkOption.SupportURIWithIncorrectSyntax, true)) {
                return new URI("tcp://" + host + ":" + port);
            }
            else {
                return new URI("tcp", null, host, port, null, null, null);
            }
        }
        catch(URISyntaxException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getClass().toString()).append(":\n");
        sb.append("Protocol: ").append(protocolType).append("\n");
        sb.append("Name: ").append(name).append("\n");

        sb.append("UseAddp: ").append(useAddp).append("\n");
        sb.append("AddpTrace: ").append(addpTrace).append("\n");
        sb.append("AddpClientTimeout: ").append(addpClientTimeout).append("\n");
        sb.append("AddpServerTimeout: ").append(addpServerTimeout).append("\n");

        sb.append("FaultToleranceMode: ").append(faultTolerance).append("\n");
        sb.append("Uri: ").append(uriInfo).append("\n");
        sb.append("WarmStandbyUri: ").append(warmStandbyUriInfo).append("\n");
        sb.append("WarmStandbyTimeout: ").append(warmStandbyTimeout).append("\n");
        sb.append("WarmStandbyAttempts: ").append(warmStandbyAttempts).append("\n");
        sb.append("WarmStandbySwitchovers: ").append(warmStandbySwitchovers).append("\n");

        return sb.toString();
    }
}
