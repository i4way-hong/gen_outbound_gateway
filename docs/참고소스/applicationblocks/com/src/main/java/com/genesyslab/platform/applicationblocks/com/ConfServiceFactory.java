// ===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:
//
// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.
//
// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.
//
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
//
// Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.applicationblocks.com.cache.IConfCache;
import com.genesyslab.platform.applicationblocks.com.cache.IConfCachePolicy;
import com.genesyslab.platform.applicationblocks.com.cache.DefaultConfCache;
import com.genesyslab.platform.applicationblocks.com.cache.DefaultConfCacheStorage;
import com.genesyslab.platform.applicationblocks.com.cache.DefaultConfCacheQueryEngine;

import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.Endpoint;

import java.util.Hashtable;


/**
 * This class is used to create an instance of the Configuration Service.
 * An instance of the cfgService must be created before any Configuration
 * Server requests can be made. For example:
 * <code><pre>
 * <i>// Initialize ConfService:</i>
 * PropertyConfiguration       config;
 * ConfServerProtocol          protocol;
 * IConfService                cfgService;
 *
 * config = new PropertyConfiguration();
 * config.setUseAddp(true);
 * config.setAddpClientTimeout(15);
 *
 * protocol = new ConfServerProtocol(new Endpoint("ConfigServer", csHost, csPort, config));
 * protocol.setUserName(userName);
 * protocol.setUserPassword(password);
 * protocol.setClientName(clientName);
 * protocol.setClientApplicationType(clientType.ordinal());
 *
 * cfgService = ConfServiceFactory.createConfService(protocol);
 * protocol.open();
 *
 * ...
 *
 * <i>// Deinitialize ConfService:</i>
 * protocol.close();
 * ConfServiceFactory.releaseConfService(cfgService);
 * cfgService = null;
 * </pre></code>
 * <i><b>Note:</b></i> ConfService may be created on closed protocol instance only.
 * Also, to dispose (to 'release') such ConfService instance,
 * it is recommended to close the protocol before it.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public final class ConfServiceFactory {

    private static Hashtable<Integer, IConfService> createdServices =
            new Hashtable<Integer, IConfService>();

    private static ConfServiceCreator serviceCreator =
        new ConfServiceCreator() {
            public ConfService createInstance(Protocol protocol) {
                return new ConfService(protocol);
            }
        };

    /**
     * Empty private constructor - we do not need instance creation.
     */
    private ConfServiceFactory() {
    }

    /**
     * This method is intended for usage in exclusive cases
     * by advanced users with deep understanding of COM AB architecture.
     *
     * @exclude
     */
    public static void setConfServiceCreator(
            final ConfServiceCreator creator) {
        if (creator == null) {
            throw new NullPointerException("ConfServiceCreator");
        }
        serviceCreator = creator;
    }

    /**
     * This method creates an instance of a Configuration Service based on
     * the specified protocol.
     *
     * @param protocol A configuration cfgService protocol
     * @return a new Configuration Service
     */
    public static IConfService createConfService(
            final Protocol protocol) {
        return createConfServiceInternal(protocol);
    }

    /**
     * This method creates an instance of a Configuration Service based on
     * the specified protocol. If caching is enabled, the default caching policy
     * will be used. If caching is disabled, all policy flags related to caching will
     * be "false".
     *
     * @param protocol The configuration service protocol
     * @param enableCaching If set to true caching functionality will be turned on
     * @return a new Configuration Service
     */
    public static IConfService createConfService(
            final Protocol protocol,
            final boolean enableCaching) {

        ConfService confService = createConfServiceInternal(protocol);

        if (enableCaching) {
            DefaultConfCacheStorage storage = new DefaultConfCacheStorage();
            DefaultConfCache cache = new DefaultConfCache(
                    null, storage, new DefaultConfCacheQueryEngine(storage));

            confService.setCache(cache);
            confService.setPolicy(new CachingConfServicePolicy());

            confService.register(cache);
        }

        return confService;
    }

    /**
     * Creates a configuration service with the specified
     * policy information. The created service will
     * have caching enabled by default with the cache using
     * the specified cache policy.
     *
     * @param protocol Configuration Server protocol
     * @param confServicePolicy The policy for the created configuration service
     * @param confCachePolicy The policy for the cache
     * @return The created configuration service
     */
    public static IConfService createConfService(
            final Protocol protocol,
            final IConfServicePolicy confServicePolicy,
            final IConfCachePolicy confCachePolicy) {

        ConfService confService = createConfServiceInternal(protocol);
        if (confServicePolicy != null) {
            confService.setPolicy(confServicePolicy);
        }

        DefaultConfCacheStorage storage = new DefaultConfCacheStorage();

        DefaultConfCache confCache = new DefaultConfCache(
                confCachePolicy, storage, new DefaultConfCacheQueryEngine(storage));

        confService.setCache(confCache);

        confService.register(confCache);

        return confService;
    }

    /**
     * Creates a configuration service with the specified
     * policy information. The created service will
     * have caching enabled if a cache object is passed as a parameter.
     *
     * @param protocol Configuration Server protocol
     * @param confServicePolicy The policy for the service
     * @param cache An object implementing the IConfCache interface
     * @return The created configuration service
     */
    public static IConfService createConfService(
            final Protocol protocol,
            final IConfServicePolicy confServicePolicy,
            final IConfCache cache) {

        ConfService confService = createConfServiceInternal(protocol);

        if (confServicePolicy != null) {
            confService.setPolicy(confServicePolicy);
        }
        confService.setCache(cache);

        if (cache != null) {
            confService.register(cache);
        }

        return confService;
    }


    /**
     * Retrieves an instance of the Configuration Service based on the specified protocol.
     *
     * @param protocol An instance of configuration protocol associated with a previously created
     *            configuration service
     * @return An instance of the associated Configuration Service, or null if not found
     */
    public static IConfService retrieveConfService(
            final Protocol protocol) {
        if (protocol == null) {
            return null;
        }
        return createdServices.get(protocol.getProtocolId());
    }

    /**
     * Retrieves an instance of the Configuration Service based on the specified endpoint.<br/>
     * It checks registered ConfService instances for active Endpoint usage.
     * <p/>
     * <i><b>Note:</b> This method is not recommended for usage. It does not track WarmStandby
     * switchovers, so, in some cases it may be unable to work properly.</i>
     *
     * @param endpoint An endpoint identifier.
     * @return An instance of the associated Configuration Service, or <code>null</code> if it was not found.
     * @deprecated
     */
    @Deprecated
    public static IConfService retrieveConfService(final Endpoint endpoint) {
        if (endpoint == null) {
            return null;
        }
        for (final IConfService csi : createdServices.values()) {
            if (endpoint.equals(csi.getProtocol().getEndpoint())) {
                return csi;
            }
        }
        return null;
    }

    /**
     * Removes the specified configuration service from the internal list and unregisters
     * it using the subscription service with which it had been registered. This method
     * should be called when a configuration service which had been created using one of the factory's
     * "Create" methods is no longer needed.
     *
     * @param confService the configuration service to release.
     * @throws IllegalArgumentException if given ConfService value is not valid.
     */
    public static void releaseConfService(
            final IConfService confService) {
        if (confService == null) {
            throw new IllegalArgumentException(
                    "Given configuration service reference is null");
        }
        if (!(confService instanceof ConfService)) {
            throw new IllegalArgumentException(
                    "This type of configuration service is not supported by the factory");
        }

        ConfService cs = (ConfService) confService;

        if (cs.getProtocol() == null) {
            throw new IllegalArgumentException(
                    "The configuration service does not have an associated protocol");
        }

        int key = cs.getProtocol().getProtocolId();

        if (!createdServices.containsKey(key)) {
            throw new IllegalArgumentException(
                "The specified object has either already been released or was not created with this factory.");
        }

        IConfService service = createdServices.get(key);
        if (service != confService) {
            throw new IllegalArgumentException(
                "The specified object has not been created with this factory");
        }

        createdServices.remove(key);

        cs.dispose();
    }


    /**
     * This method creates an instance of a Configuration Service based on
     * the specified protocol.
     *
     * @param protocol A configuration cfgService protocol
     * @param subscriptionService The subscription service with which to register the created service
     *        for configuration server events (see example in the description of the ConfServiceFactory class)
     * @return a new Configuration Service
     */
    private static ConfService createConfServiceInternal(
            final Protocol protocol) {

        if (protocol == null) {
            throw new IllegalArgumentException("Protocol can't be null");
        }

        if (retrieveConfService(protocol) != null) {
            throw new IllegalArgumentException(
                    "ConfService has already been created for this protocol.");
        }

        ConfService confService =
            serviceCreator.createInstance(protocol);

        if (confService == null) {
            throw new NullPointerException(
                    "ConfServiceCreator result is null");
        }

        confService.setupInternalMessageHandler();

        createdServices.put(protocol.getProtocolId(), confService);

        return confService;
    }
}
