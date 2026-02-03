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

import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.QueueMessageReceiver;
import com.genesyslab.platform.commons.protocol.MessageReceiverSupport;
import com.genesyslab.platform.commons.protocol.ListenerHelper;
import com.genesyslab.platform.commons.protocol.Listener;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.MessageReceiver;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;


/**
 * Protocol Manager Application Block is going to be deprecated.
 * <p/>
 * - ConnectionConfiguration interfaces has been extended with specific configuration options.<br/>
 * - Endpoint class has been extended with constructor to accept ConnectionConfiguration parameter.<br/>
 * - New ClientConfigurationHelper class has been added to AppTemplate Application Block to help
 * configuration initialization from Configuration Server data.
 * <p/>
 * So, user should create WarmStanbyService and manage its' state directly if it is required.
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
 * Use protocol object directly to open and close connection. 
 * Use WarmStandby service directly.
 */
@Deprecated
public class ProtocolManagementServiceImpl
        implements ProtocolManagementService {

    private final MessageReceiverSupport receiver;

    private Map<String, ProtocolInstance> protocols =
            new HashMap<String, ProtocolInstance>(8);
    private Map<String, ProtocolFacility> protocolsFacilities =
            new HashMap<String, ProtocolFacility>(16);

    private final Object protocolsLock = new Object();

    private ListenerHelper listeners = new ListenerHelper();
    private ChannelListener channelListener = new PMSChannelListener();

    public ProtocolManagementServiceImpl() {
        this(new QueueMessageReceiver(1024, true));
    }

    public ProtocolManagementServiceImpl(
            final MessageReceiverSupport messageReceiver) {
        if (messageReceiver == null) {
            throw new NullPointerException("messageReceiver");
        }
        this.receiver = messageReceiver;
    }

    public final Protocol getProtocol(final String name) {
        ProtocolInstance instance = getProtocolEntry(name);
        return instance.getProtocol();
    }

    public final WarmStandbyService getWarmStandbyService(
            final String name) {
        ProtocolInstance instance = getProtocolEntry(name);
        return instance.getWarmStandby();
    }

    public final Protocol register(final ProtocolConfiguration protocolConfiguration) {
        if (protocolConfiguration == null) {
            throw new NullPointerException(
                    "protocolConfiguration: ProtocolConfiguration is null.");
        }

        return registerInternal(protocolConfiguration.getName(), protocolConfiguration);
    }

    public final void unregister(final String name) {
        if (name == null || name.length()==0) {
            throw new NullPointerException("name: Name is null.");
        }

        ProtocolInstance instance;
        synchronized (protocolsLock) {
            instance = protocols.remove(name);
            if (instance == null) {
                throw new IllegalArgumentException(
                        "name: Protocol is not registered.");
            }
        }

        unregisterInternal(instance);
    }

    public void applyConfiguration(
            final ProtocolConfiguration protocolConfiguration) {
        if (protocolConfiguration == null) {
            throw new NullPointerException(
                    "protocolConfiguration: ProtocolConfiguration is null.");
        }
        if (protocolConfiguration.getName() == null) {
            throw new NullPointerException(
                    "protocolConfiguration: Protocol name is null.");
        }

        ProtocolInstance instance = getProtocolEntry(
                protocolConfiguration.getName());
        applyConfiguration(instance, protocolConfiguration);
    }

    public final MessageReceiver getReceiver() {
        return receiver;
    }

    public void beginOpen() throws ProtocolException {
        synchronized (protocolsLock) {
            for (ProtocolInstance instance : protocols.values()) {
                beginOpenProtocol(instance);
            }
        }
    }

    public void beginClose() {
        synchronized (protocolsLock) {
            for (ProtocolInstance instance : protocols.values()) {
                beginCloseProtocol(instance);
            }
        }
    }

    public void beginOpenProtocol(final String name) throws ProtocolException {
        ProtocolInstance instance = getProtocolEntry(name);
        beginOpenProtocol(instance);
    }

    public void beginCloseProtocol(final String name) {
        ProtocolInstance instance = getProtocolEntry(name);
        beginCloseProtocol(instance);
    }

    public void addChannelListener(final ChannelListener listener) {
        listeners.addListener(listener);
    }

    public void removeChannelListener(final ChannelListener listener) {
        listeners.removeListener(listener);
    }


    private Protocol registerInternal(
            final String name, final ProtocolConfiguration protocolConfiguration) {

        if (name == null || name.length()==0) {
            throw new NullPointerException(
                    "Protocol name can't be null or empty.");
        }

        ProtocolInstance instance;

        synchronized (this.protocolsLock) {
            if (this.protocols.containsKey(name)) {
                throw new IllegalArgumentException("Protocol is already registered.");
            }

            ProtocolFacility protocolFacility = getFacility(
                    protocolConfiguration.getProtocolType());
            if (protocolFacility == null) {
                throw new IllegalStateException("Protocol is not supported");
            }
            Protocol protocol = protocolFacility.createProtocol(
					protocolConfiguration.getName(),
					protocolConfiguration.getUri(),
					protocolConfiguration.isPrimaryTLSEnabled(),
					protocolConfiguration.getPrimarySSLContext(),
					protocolConfiguration.getPrimarySSLOptions());

			instance = new ProtocolInstance(protocol);

            applyConfiguration(instance, protocolConfiguration);

            protocol.addChannelListener(channelListener);

            protocolFacility.initialize(instance, receiver);

            this.protocols.put(name, instance);
        }

        return instance.getProtocol();
    }

    private void applyConfiguration(
            final ProtocolInstance instance,
            final ProtocolConfiguration conf) {
        if (conf == null) {
            return;
        }

        ProtocolFacility facility = getFacility(
                conf.getProtocolType());

        synchronized (instance.getProtocolLock()) {
            facility.applyConfiguration(instance, conf);
        }
    }

    private void beginCloseProtocol(
            final ProtocolInstance instance) {
        ProtocolFacility facility = getFacility(
                instance.getProtocol().getClass());

        synchronized (instance.getProtocolLock()) {
            facility.beginCloseProtocol(instance);
        }
    }

    private void beginOpenProtocol(final ProtocolInstance instance)
            throws ProtocolException {
        ProtocolFacility facility = getFacility(instance.getProtocol().getClass());

        synchronized (instance.getProtocolLock()) {
            facility.beginOpenProtocol(instance);
        }
    }


    private ProtocolInstance getProtocolEntry(final String name) {
        if (name == null || name.length()==0) {
            throw new IllegalArgumentException("name is null or empty");
        }

        ProtocolInstance instance;

        synchronized (protocolsLock) {
            instance = protocols.get(name);
            if (instance == null) {
                throw new IllegalArgumentException("name: Protocol is not registered.");
            }
        }
        return instance;
    }

    private void unregisterInternal(final ProtocolInstance instance) {
        synchronized (instance.getProtocolLock()) {
            ProtocolFacility facility = getFacility(instance.getProtocol().getClass());
            facility.uninitialize(instance);
        }
    }

    private ProtocolFacility getFacility(final Class clazz) {
        final String clazzName = clazz.getSimpleName();
        ProtocolFacility facility = protocolsFacilities.get(clazzName);
        if (facility == null) {
            if ("ConfServerProtocol".equals(clazzName)) {
                facility = new ConfServerFacility();
            } else if ("TServerProtocol".equals(clazzName)) {
                facility = new TServerFacility();
            } else if ("InteractionServerProtocol".equals(clazzName)) {
                facility = new InteractionServerFacility();
            } else if ("LocalControlAgentProtocol".equals(clazzName)) {
                facility = new LcaFacility();
            } else if ("MessageServerProtocol".equals(clazzName)) {
                facility = new MessageServerFacility();
            } else if ("OutboundServerProtocol".equals(clazzName)) {
                facility = new OutboundServerFacility();
            } else if ("SolutionControlServerProtocol".equals(clazzName)) {
                facility = new ScsFacility();
            } else if ("StatServerProtocol".equals(clazzName)) {
                facility = new StatServerFacility();
            } else if ("UniversalContactServerProtocol".equals(clazzName)) {
                facility = new UcsFacility();
            } else if ("UrsCustomProtocol".equals(clazzName)) {
                facility = new RoutingCustomServerFacility();
            } else if ("BasicChatProtocol".equals(clazzName)) {
                facility = new BasicChatFacility();
            } else if ("FlexChatProtocol".equals(clazzName)) {
                facility = new FlexChatFacility();
            } else if ("CallbackProtocol".equals(clazzName)) {
                facility = new CallbackFacility();
            } else if ("EmailProtocol".equals(clazzName)) {
                facility = new EmailFacility();
            } else if ("EspEmailProtocol".equals(clazzName)) {
                facility = new EspEmailFacility();
            } else {
                throw new IllegalArgumentException(
                        "Protocol '" + clazz.getName() + "' is not supported");
            }
            protocolsFacilities.put(clazzName, facility);
        }
        return facility;
    }


    private class PMSChannelListener implements ChannelListener {

        public void onChannelOpened(final EventObject event) {
            listeners.notifyListeners(new OpenedVisitor(event));
        }

        public void onChannelClosed(final ChannelClosedEvent event) {
            listeners.notifyListeners(new ClosedVisitor(event));
        }

        public void onChannelError(final ChannelErrorEvent event) {
            listeners.notifyListeners(new ErrorVisitor(event));
        }
    }

    private class OpenedVisitor implements ListenerHelper.NotificationVisitor {
        private EventObject event;

        public OpenedVisitor(final EventObject event) {
            this.event = event;
        }

        public void visitListener(final Listener listener) {
            ((ChannelListener) listener).onChannelOpened(event);
        }
    }

    private class ClosedVisitor implements ListenerHelper.NotificationVisitor {
        private ChannelClosedEvent event;

        public ClosedVisitor(final ChannelClosedEvent event) {
            this.event = event;
        }

        public void visitListener(final Listener listener) {
            ((ChannelListener) listener).onChannelClosed(event);
        }
    }

    private class ErrorVisitor implements ListenerHelper.NotificationVisitor {
        private ChannelErrorEvent event;

        public ErrorVisitor(final ChannelErrorEvent event) {
            this.event = event;
        }

        public void visitListener(final Listener listener) {
            ((ChannelListener) listener).onChannelError(event);
        }
    }
}
