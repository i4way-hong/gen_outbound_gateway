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
package com.genesyslab.platform.apptemplate.application;

import com.genesyslab.platform.apptemplate.log4j2.GFAppLog4j2Updater;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor;

import com.genesyslab.platform.applicationblocks.com.IConfService;

import com.genesyslab.platform.standby.WSConfig;
import com.genesyslab.platform.standby.WarmStandby;

import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Protocol;

import com.genesyslab.platform.commons.log.Log4J2Checker;

import java.util.List;
import java.util.LinkedList;


/**
 * Abstract base class for application configuration manager builders.
 * It is to share building logic between manager builders.
 * <p/>
 * Each specific configuration manager class contains own extension of this builder with
 * required logic for particular manager creation and configuration.
 *
 * @see GFApplicationConfigurationManager#newBuilder()
 */
@SuppressWarnings("unchecked")
public abstract class AbstractManagerBuilder
        <M extends GFApplicationConfigurationManager, B extends AbstractManagerBuilder<M, B>> {

    protected IConfService confService;

    protected LinkedList<Endpoint> csEndpoints = new LinkedList<Endpoint>();

    protected String       clientName;
    protected CfgAppType   clientType;
    protected String       username;
    protected String       password;

    protected Boolean disableSessionUsage;
    protected Boolean doCSSubscription;
    protected Boolean readTenantsInfo;

    protected Boolean  useWarmStandby;
    protected WSConfig wsConfig;

    protected LmsMessageConveyor lmsConveyor = null;

    protected Boolean doLoggingAutoconfig = true;


    /**
     * Initializes custom instance of <code>LmsMessageConveyor</code>.<br/>
     * If this property is not specified (usual case), the application configuration manager
     * will create default instance of it ({@link LmsMessageConveyor#LmsMessageConveyor()}).
     *
     * @param lms user defined LMS messages conveyor.
     * @return this builder instance reference.
     */
    public B withLmsConveyor(final LmsMessageConveyor lms) {
        lmsConveyor = lms;
        return (B) this;
    }

    public B withLoggingAutoconfig(final Boolean doAutoconfig) {
        doLoggingAutoconfig = doAutoconfig;
        return (B) this;
    }

    public B withConfService(final IConfService confService) {
        if (csEndpoints.size() > 0 || wsConfig != null) {
            throw new IllegalArgumentException(
                  "GFApplicationConfigurationManager Builder can't use ConfService and Endpoint at the same time");
        }

        if (confService == null) {
            throw new IllegalArgumentException("IConfService is null");
        }

        Protocol protocol = confService.getProtocol();
        if (!(protocol instanceof ConfServerProtocol)) {
            throw new IllegalArgumentException("No ConfServerProtocol in the IConfService");
        }

        String applicationName = ((ConfServerProtocol) protocol).getClientName();
        if (applicationName == null || applicationName.isEmpty()) {
            throw new IllegalArgumentException("No client application name provided");
        }

        this.confService = confService;
        this.clientName = applicationName;

        return (B) this;
    }

    public B withCSEndpoint(final Endpoint endpoint) {
        if (confService != null) {
            throw new IllegalArgumentException(
                    "GFApplicationConfigurationManager Builder can't use ConfService and Endpoint at the same time");
        }
        this.csEndpoints.add(endpoint);
        return (B) this;
    }

    public B withClientId(
            final CfgAppType clientType,
            final String     clientName) {
        if (confService != null) {
            throw new IllegalArgumentException(
                    "GFApplicationConfigurationManager Builder with ConfService initialized "
                    + "does not accept client registration parameters");
        }
        this.clientType = clientType;
        this.clientName = clientName;
        return (B) this;
    }

    public B withUserId(
            final String username,
            final String passwd) {
        if (confService != null) {
            throw new IllegalArgumentException(
                    "GFApplicationConfigurationManager Builder with ConfService initialized "
                    + "does not accept client registration parameters");
        }
        this.username = username;
        this.password = passwd;
        return (B) this;
    }

    public B withSessionDisabled(
            final Boolean disableSession) {
        if (confService != null) {
            throw new IllegalArgumentException(
                    "GFApplicationConfigurationManager Builder with ConfService initialized "
                    + "does not manage protocol connection");
        }
        this.disableSessionUsage = disableSession;
        return (B) this;
    }

    public B withDoCSSubscription(
            final Boolean doSubscription) {
        this.doCSSubscription = doSubscription;
        return (B) this;
    }

    public B withTenantsInfoReading(
            final Boolean readTenantsInfo) {
        this.readTenantsInfo = readTenantsInfo;
        return (B) this;
    }

    public B withWarmStandbyEnabled(
            final Boolean enableWS) {
        if (confService != null) {
            throw new IllegalArgumentException(
                    "GFApplicationConfigurationManager Builder with ConfService initialized "
                    + "does not manage protocol connection");
        }
        this.useWarmStandby = enableWS;
        return (B) this;
    }

    public B withWarmStandby(
            final WSConfig wsConfig) {
        if (confService != null) {
            throw new IllegalArgumentException(
                    "GFApplicationConfigurationManager Builder can't use ConfService and WarmStandby at the same time");
        }
        this.wsConfig = wsConfig;
        return (B) this;
    }


    protected void checkRequiredParameters() {
        if (confService == null && csEndpoints.size() == 0 && wsConfig == null) {
            throw new IllegalArgumentException(
                    "GFApplicationConfigurationManager.Builder: no required parameters given "
                    + "('ConfService', 'WSConfig', or 'CSEndpoint' must be provided)");
        }

        if (confService == null) {
            if (clientName == null || clientName.isEmpty()) {
                throw new IllegalArgumentException("ClientName is required for ConfService initialization");
            }
        }
    }

    protected GFApplicationConfigurationManager setupContext(
            final GFApplicationConfigurationManager manager) {
        if (doLoggingAutoconfig != null && doLoggingAutoconfig) {
            if (Log4J2Checker.isAvailable() && Log4J2Checker.isCoreAvailable()) {
                manager.register(new GFAppLog4j2Updater());
            } else {
                throw new IllegalArgumentException(
                        "Can't initialize LoggingAutoconfig - no Log4j2 available");
            }
        }
        manager.initLmsFactory(lmsConveyor);
        return manager;
    }


    protected ConfServerProtocol createProtocol() {
        Endpoint csEndpoint = null;
        if (csEndpoints.size() > 0) {
            csEndpoint = csEndpoints.get(0);
        } else if (wsConfig != null) {
            List<Endpoint> eps = wsConfig.getEndpoints();
            if (eps != null && eps.size() > 0) {
                csEndpoint = eps.get(0);
            }
        }
        if (csEndpoint == null) {
            throw new IllegalArgumentException(
                    "GFApplicationConfigurationManager.Builder: no CS Endpoint given to create ConfService");
        }

        ConfServerProtocol protocol = new ConfServerProtocol(csEndpoint);
        if (disableSessionUsage == null || !disableSessionUsage) {
            protocol.setUseSession(true);
        }

        protocol.setClientName(clientName);
        if (clientType != null) {
            protocol.setClientApplicationType(clientType.ordinal());
        }

        if (username != null) {
            protocol.setUserName(username);
        }
        if (password != null) {
            protocol.setUserPassword(password);
        }

        return protocol;
    }

    protected WarmStandby createWarmStandby(final ConfServerProtocol protocol) {
        if (protocol != null) {
            if (wsConfig != null) {
                WarmStandby warmStandby = new WarmStandby(clientName + "@WS.CS", protocol);
                warmStandby.setConfig(wsConfig);
                return warmStandby;

            } else if (useWarmStandby == null || useWarmStandby) {
                if (csEndpoints.size() > 0) {
                    return new WarmStandby(clientName + "@WS.CS", protocol, csEndpoints);
                }
            }
        }

        return null;
    }

    public abstract M build();
}
