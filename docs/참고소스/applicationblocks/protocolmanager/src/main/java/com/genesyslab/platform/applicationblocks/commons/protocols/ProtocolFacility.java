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

import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyState;
import com.genesyslab.platform.commons.connection.tls.SSLExtendedOptions;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.MessageReceiverSupport;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.UriInfo;
import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.ConnectionConfiguration;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.connection.interceptor.AddpInterceptor;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import javax.net.ssl.SSLContext;

import java.net.URI;


abstract class ProtocolFacility {

    private ILogger log = Log.getLogger(ProtocolFacility.class);


    public void initialize(
            final ProtocolInstance instance,
            final MessageReceiverSupport receiver) {
        instance.getProtocol().setReceiver(receiver);
    }

    public void uninitialize(
            final ProtocolInstance instance) {
        beginCloseProtocol(instance);
        instance.waitForStableChannelState();
        instance.dispose();
    }

    public void applyConfiguration(
            final ProtocolInstance instance,
            final ProtocolConfiguration conf) {
        ConnectionConfiguration connConf = applyChannelConfiguration(instance, conf);
        applyWarmStandbyConfiguration(instance, conf, connConf);
    }

    public void beginOpenProtocol(
            final ProtocolInstance instance) throws ProtocolException {
        WarmStandbyService warmStandby = instance.getWarmStandby();
        if (warmStandby != null && warmStandby.getState() != WarmStandbyState.OFF) {
            warmStandby.stop();
        }

        instance.waitForStableChannelState();

        if (warmStandby != null && warmStandby.getState() == WarmStandbyState.OFF) {
            warmStandby.start();
        }

        if (instance.getProtocol().getState() == ChannelState.Closed) {
            try {
                instance.getProtocol().beginOpen();
            } catch (ProtocolException ex) {
                if (log.isWarn()) {
                    log.warn("Exception on Channel opening", ex);
                }
                if (warmStandby == null) {
                    throw ex;
                }
            }
        }
    }

    public void beginCloseProtocol(
            final ProtocolInstance instance) {
        WarmStandbyService warmStandby = instance.getWarmStandby();
        if (warmStandby != null && warmStandby.getState() != WarmStandbyState.OFF) {
            warmStandby.stop();
        }

        instance.waitForStableChannelState();

        if (instance.getProtocol().getState() == ChannelState.Opened) {
            instance.getProtocol().beginClose();
        }
    }

    public abstract Protocol createProtocol(String name, URI uri,
			boolean tlsEnabled, SSLContext sslContext, SSLExtendedOptions sslOptions);


    private void applyWarmStandbyConfiguration(
            final ProtocolInstance instance,
            final ProtocolConfiguration conf,
            final ConnectionConfiguration defConnConf)
	{
        // warm standby
        FaultToleranceMode faultTolerance = conf.getFaultTolerance();

        if (faultTolerance == null) {
            return;
        }

        WarmStandbyService warmStandby = instance.getWarmStandby();
        if (faultTolerance == FaultToleranceMode.WarmStandby) {
            if (warmStandby == null) {
                warmStandby = new WarmStandbyService(instance.getProtocol());
                instance.setWarmStandby(warmStandby);
            }

            String host = null;
            int port = -1;

            {
                UriInfo uriData = UriInfo.get(conf.getUri()) ;
                if (uriData != null) {
                    host = uriData.host;
                    port = uriData.port;
                }
            }
            
            if (host == null) {
                Endpoint endpoint = instance.getProtocol().getEndpoint();
                host = endpoint.getHost();
                port = endpoint.getPort();
            }
                    
			ConnectionConfiguration connConfPrimary = createPrimaryConfiguration(conf);
            Endpoint primaryEndpoint = new Endpoint(conf.getName(), host, port, connConfPrimary,
					conf.isPrimaryTLSEnabled(), conf.getPrimarySSLContext(), conf.getPrimarySSLOptions());

            String wsHost = null;
            int wsPort = -1;
            
            {
                UriInfo uriData = UriInfo.get(conf.getWarmStandbyUri());
                if (uriData != null) {
                    wsHost = uriData.host;
                    wsPort = uriData.port;
                }
            }
			
            if (wsHost == null) {
                Endpoint endpoint = instance.getProtocol().getEndpoint();
                wsHost = endpoint.getHost();
                wsPort = endpoint.getPort();
            }
			
			ConnectionConfiguration connConfBackup = createBackupConfiguration(conf);
			Endpoint backupEndpoint = new Endpoint(conf.getName(), wsHost, wsPort, connConfBackup,
					conf.isBackupTLSEnabled(), conf.getBackupSSLContext(), conf.getBackupSSLOptions());

            WarmStandbyConfiguration wsConf = new WarmStandbyConfiguration(primaryEndpoint, backupEndpoint);
            wsConf.setAttempts(conf.getWarmStandbyAttempts());
            wsConf.setSwitchovers(conf.getWarmStandbySwitchovers());
            wsConf.setTimeout(conf.getWarmStandbyTimeout());

            warmStandby.applyConfiguration(wsConf);

            if (warmStandby.getState() == WarmStandbyState.OFF) {
                warmStandby.start();
            }
        } else if (warmStandby != null) {
            warmStandby.stop();
            instance.setWarmStandby(null);
        }
    }

    private ConnectionConfiguration applyChannelConfiguration(
			final ProtocolInstance instance, final ProtocolConfiguration protConf)
	{
		PropertyConfiguration connConf = createPrimaryConfiguration(protConf);

		UriInfo uriInfo = UriInfo.get(protConf.getUri()); 
        if (uriInfo != null) {
			Endpoint ep = new Endpoint(
					protConf.getName(), uriInfo.host, uriInfo.port, connConf,
					protConf.isPrimaryTLSEnabled(), protConf.getPrimarySSLContext(), protConf.getPrimarySSLOptions());
            instance.getProtocol().setEndpoint(ep);
        }

		if (connConf.isEmpty()) {
			return null;
		}

		instance.getProtocol().configure(connConf);
		return connConf;
	}

	protected PropertyConfiguration createPrimaryConfiguration(ProtocolConfiguration protConf) {
		PropertyConfiguration connConf;

		String query = UriInfo.get(protConf.getUri()).query;
		if (null == query) {
			connConf = new PropertyConfiguration();
		} else {
			connConf = new PropertyConfiguration(query);
		}

		fillConnectionOptions(connConf, protConf);

		return connConf;
	}

	protected PropertyConfiguration createBackupConfiguration(ProtocolConfiguration protConf) {
		PropertyConfiguration connConf = null;

		String query = UriInfo.get(protConf.getWarmStandbyUri()).query;
		if (null == query) {
			connConf = new PropertyConfiguration();
		} else {
			connConf = new PropertyConfiguration(query);
		}

		fillConnectionOptions(connConf, protConf);

		return connConf;
	}

	protected void fillConnectionOptions(ConnectionConfiguration connConf, ProtocolConfiguration protConf) {
		boolean addpActive = (Boolean.TRUE == protConf.isUseAddp());
		if (addpActive) {
			connConf.setOption(AddpInterceptor.PROTOCOL_NAME_KEY, "addp");

			Integer addpServerTimeout = protConf.getAddpServerTimeout();
			if ((addpServerTimeout != null)) {
				connConf.setOption(AddpInterceptor.REMOTE_TIMEOUT_KEY, addpServerTimeout.toString());
			}

			Integer addpClientTimeout = protConf.getAddpClientTimeout();
			if ((addpClientTimeout != null)) {
				connConf.setOption(AddpInterceptor.TIMEOUT_KEY, addpClientTimeout.toString());
			}
		} else {
			connConf.setOption(AddpInterceptor.REMOTE_TIMEOUT_KEY, "-1");
			connConf.setOption(AddpInterceptor.TIMEOUT_KEY, "-1");
		}

		String addpTrace = protConf.getAddpTrace();
		if ((addpTrace != null) && (addpTrace.length() > 0)) {
			connConf.setOption(AddpInterceptor.TRACE_KEY, addpTrace);
		}

		if (Boolean.TRUE == protConf.isPrimaryTLSEnabled()) {
			connConf.setOption(Connection.TLS_KEY, "1");
		}

		String localBindHost = protConf.getLocalBindingHost();
		if ((localBindHost != null) && (localBindHost.length() > 0)) {
			connConf.setOption(Connection.BIND_HOST_KEY, localBindHost);
		}

		Integer localBindPort = protConf.getLocalBindingPort();
		if ((localBindPort != null) && (localBindPort > 0)) {
			connConf.setInteger(Connection.BIND_PORT_KEY, localBindPort);
		}
	}
}
