//===============================================================================
// Genesys Platform SDK Application Blocks
//===============================================================================

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
package com.genesyslab.platform.applicationblocks.warmstandbyquickstart;

import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyState;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyStateChangedEvent;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyListener;

import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.ClientChannel;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;

import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.EventObject;

import java.io.IOException;


public class MainForm extends JFrame {

    private JTextField jtfApplicationName;
    private JTextField jtfApplicationProtocol;

    private JButton jbOpen;
    private JButton jbClose;

    private JTextField jtfPrimaryServer;
    private JTextField jtfBackupServer;

    private JSpinner jsAttempts;
    private JSpinner jsTimeout;
    private JSpinner jsSwitchovers;

    private JButton jbStart;
    private JButton jbStop;
    private JButton jbReconfigure;

    private JTextField jtfStatusServer;
    private JTextField jtfStatusConnection;
    private JTextField jtfStatusWarmStanby;
    private JTextField jtfStatusAttempt;


    private ClientChannel channel;
    private WarmStandbyService warmStandby;
    private ProtocolFactory factory = new ProtocolFactory();
    private WarmStandbyQuickStartConfiguration appConfiguration;

    private static ILogger log = Log.getLogger(MainForm.class);


    /**
     * The main entry point for the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        MainForm mainApp = new MainForm();
        mainApp.setVisible(true);
        mainApp.mainFormLoad();
    }


    protected MainForm() {
        setTitle("Genesys Warm Standby");

        jtfApplicationName = new JTextField();
        jtfApplicationName.setEditable(false);
        jtfApplicationProtocol = new JTextField();
        jtfApplicationProtocol.setPreferredSize(new Dimension(120, 1));
        jtfApplicationProtocol.setEditable(false);
        jtfPrimaryServer = new JTextField();
        jtfBackupServer = new JTextField();

        jsAttempts = new JSpinner();
        jsTimeout = new JSpinner();
        jsSwitchovers = new JSpinner();

        jtfStatusServer = new JTextField();
        jtfStatusServer.setEditable(false);
        jtfStatusServer.setBorder(BorderFactory.createLoweredBevelBorder());
        jtfStatusConnection = new JTextField();
        jtfStatusConnection.setEditable(false);
        jtfStatusConnection.setBorder(BorderFactory.createLoweredBevelBorder());
        jtfStatusWarmStanby = new JTextField();
        jtfStatusWarmStanby.setEditable(false);
        jtfStatusWarmStanby.setBorder(BorderFactory.createLoweredBevelBorder());
        jtfStatusAttempt = new JTextField();
        jtfStatusAttempt.setEditable(false);
        jtfStatusAttempt.setBorder(BorderFactory.createLoweredBevelBorder());

        jbOpen = new JButton(new AbstractAction("Open") {
            public void actionPerformed(final ActionEvent e) {
                btnOpenClick();
            }
        });
        jbClose = new JButton(new AbstractAction("Close") {
            public void actionPerformed(final ActionEvent e) {
                btnCloseClick();
            }
        });
        jbStart = new JButton(new AbstractAction("Start") {
            public void actionPerformed(final ActionEvent e) {
                btnStartClick();
            }
        });
        jbStop = new JButton(new AbstractAction("Stop") {
            public void actionPerformed(final ActionEvent e) {
                btnStopClick();
            }
        });
        jbReconfigure = new JButton(new AbstractAction("Reconfigure") {
            public void actionPerformed(final ActionEvent e) {
                btnReconfigureClick();
            }
        });

        JPanel panelRoot = new JPanel();
        panelRoot.setLayout(new BorderLayout());

        JPanel panel0 = new JPanel();
        panel0.setBorder(new EmptyBorder(5, 10, 5, 10));
        panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));

        JPanel panel1 = new JPanel();
        panel1.setBorder(new CompoundBorder(
                new TitledBorder("Application"),
                new EmptyBorder(5, 10, 5, 10)));
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayout(2, 2));
        panel11.add(new JLabel("Name"), 0);
        panel11.add(jtfApplicationName, 1);
        panel11.add(new JLabel("Protocol"), 2);
        panel11.add(jtfApplicationProtocol, 3);
        panel1.add(panel11);
        JPanel panel12 = new JPanel();
        panel12.setBorder(new CompoundBorder(
                new TitledBorder("Connection"),
                new EmptyBorder(5, 10, 5, 10)));
        panel12.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel12.add(jbOpen);
        panel12.add(jbClose);
        panel1.add(panel12);
        panel0.add(panel1);

        JPanel panel2 = new JPanel();
        panel2.setBorder(new CompoundBorder(
                new TitledBorder("Warm Standby"),
                new EmptyBorder(5, 10, 5, 10)));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        JPanel panel21 = new JPanel();
        panel21.setLayout(new GridLayout(2, 2));
        panel21.add(new JLabel("Primary Server"), 0);
        panel21.add(jtfPrimaryServer, 1);
        panel21.add(new JLabel("Backup Server"), 2);
        panel21.add(jtfBackupServer, 3);
        panel2.add(panel21);
        JPanel panel22 = new JPanel();
        panel22.setLayout(new GridLayout(3, 3));
        panel22.add(new JLabel("Attempts"), 0);
        panel22.add(jsAttempts, 1);
        panel22.add(jbStart, 2);
        panel22.add(new JLabel("Timeout (sec)"), 3);
        panel22.add(jsTimeout, 4);
        panel22.add(jbStop, 5);
        panel22.add(new JLabel("Switchovers"), 6);
        panel22.add(jsSwitchovers, 7);
        panel22.add(jbReconfigure, 8);
        panel2.add(panel22);
        panel0.add(panel2);

        JPanel panelStatusBar = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panelStatusBar.setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 3.5;
        gridbag.setConstraints(jtfStatusServer, c);
        panelStatusBar.add(jtfStatusServer);
        c.weightx = 2;
        gridbag.setConstraints(jtfStatusConnection, c);
        panelStatusBar.add(jtfStatusConnection);
        c.weightx = 2;
        gridbag.setConstraints(jtfStatusWarmStanby, c);
        panelStatusBar.add(jtfStatusWarmStanby);
        c.weightx = 1;
        gridbag.setConstraints(jtfStatusAttempt, c);
        panelStatusBar.add(jtfStatusAttempt);

        panelRoot.add(panel0, BorderLayout.CENTER);
        panelRoot.add(panelStatusBar, BorderLayout.SOUTH);

        setContentPane(panelRoot);

        setResizable(false);
        setEnabled(true);
        pack();

        addWindowListener(new WindowListener() {
            public void windowOpened(final WindowEvent e) { }
            public void windowClosing(final WindowEvent e) {
                mainFormUnload();
            }
            public void windowClosed(final WindowEvent e) { }
            public void windowIconified(final WindowEvent e) { }
            public void windowDeiconified(final WindowEvent e) { }
            public void windowActivated(final WindowEvent e) { }
            public void windowDeactivated(final WindowEvent e) { }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void mainFormLoad() {
        // set the visual control properties

        try {
            loadConfiguration();
        } catch (Exception ex) {
            log.error("Exception application configuration reading", ex);
            dispose();
            throw new RuntimeException("Application Configuration Error", ex);
        }

        try {
            initializeChannel();
        } catch (Exception ex) {
            log.error("Exception in the protocol connection", ex);
            dispose();
            throw new RuntimeException("Error Connection to the Configuration Server", ex);
        }

        try {
            initializeWarmstandby();
        } catch (Exception ex) {
            log.error("Exception in the warmstandby initialization", ex);
            dispose();
            throw new RuntimeException("Error WarmStandby Initialization", ex);
        }

        jbClose.setEnabled(false);
        jbStop.setEnabled(false);
        //grpWarmStandby.Enabled = true;

        setStatusServer(channel.getEndpoint());
        setStatusConnection(channel.getState());
        setStatusWarmStandby(warmStandby.getState());
        setStatusAttempt(null);
    }

    private void mainFormUnload() {
        log.info("Exception on channel close operation");
        if (channel != null && channel.getState() == ChannelState.Opened) {
            try {
                if (!warmStandby.getState().equals(WarmStandbyState.OFF)) {
                    warmStandby.stop();
                }
                channel.close();
            } catch (Exception ex) {
                log.error("Exception on channel close operation", ex);
            }
        }
    }


    /**
     * Loads Warm Standby Quick Start Sample Application's Configuration.
     *
     * @throws IOException in case of configuration read error
     */
    private void loadConfiguration() throws IOException {
        appConfiguration = new WarmStandbyQuickStartConfiguration();
    }

    /**
     * Initializes the Protocol Channel.
     *
     * @throws URISyntaxException if configuration contains bad value for the protocol endpoint
     */
    private void initializeChannel() throws URISyntaxException {
        jtfApplicationProtocol.setText(appConfiguration.getProtocol().toString());
        jtfApplicationName.setText(appConfiguration.getClientName());

        jtfPrimaryServer.setText(appConfiguration.getPrimaryServer());

        Endpoint activeEndpoint = new Endpoint(new URI(appConfiguration.getPrimaryServer()));

        channel = factory.createProtocol(appConfiguration.getProtocol(), activeEndpoint);

        if (appConfiguration.getProtocol() == ProtocolType.ConfigurationServer) {
            ConfServerProtocol confChannel = (ConfServerProtocol) channel;
            confChannel.setClientName(appConfiguration.getClientName());
            confChannel.setUserName(appConfiguration.getUserName());
            confChannel.setUserPassword(appConfiguration.getUserPassword());
            confChannel.setClientApplicationType(appConfiguration.getClientType());
            //confChannel.setLogger(logger);
        }
        /*else if (appConfiguration.getProtocol() == ProtocolType.InteractionServer) {
            InteractionServerProtocol interactionChannel = (InteractionServerProtocol) channel;
            interactionChannel.setClientType(appConfiguration.getClientType());
            interactionChannel.setClientName(appConfiguration.getClientName());
        }
        else if (appConfiguration.getProtocol() == ProtocolType.StatServer) {
            StatServerProtocol statChannel = (StatServerProtocol) channel;
            statChannel.setClientName(appConfiguration.getClientName());
        }*/

        channel.setTimeout(30000);

        channel.addChannelListener(new ChannelListener() {
            public void onChannelOpened(final EventObject event) {
                onProtocolChannelOpened(event);
            }

            public void onChannelClosed(final ChannelClosedEvent event) {
                onProtocolChannelClosed(event);
            }

            public void onChannelError(final ChannelErrorEvent event) {
                onProtocolChannelError(event);
            }
        });
    }

    /**
     * Initializes the Warm Standby.
     *
     * @throws URISyntaxException if configuration contains bad value for the protocol endpoint
     */
    private void initializeWarmstandby() throws URISyntaxException {
        jtfBackupServer.setText(appConfiguration.getBackupServer());
        jsAttempts.setValue(Integer.valueOf(appConfiguration.getAttempts()));
        jsTimeout.setValue(Integer.valueOf(appConfiguration.getTimeout()));
        jsSwitchovers.setValue(Integer.valueOf(appConfiguration.getSwitchovers()));

        URI standbyUri = new URI(appConfiguration.getBackupServer());

        WarmStandbyConfiguration config = new WarmStandbyConfiguration(
                channel.getEndpoint(), new Endpoint(standbyUri),
                appConfiguration.getTimeout() * 1000, appConfiguration.getAttempts());
        config.setSwitchovers(appConfiguration.getSwitchovers());
        warmStandby = new WarmStandbyService(channel);
        warmStandby.applyConfiguration(config);
        warmStandby.addListener(new WarmStandbyListener() {
            public void onStateChanged(final WarmStandbyStateChangedEvent event) {
                onWarmStandbyStateChanged(event);
            }

            public void onSwitchover(final EventObject event) {
                onWarmStandbySwitchedOver(event);
            }
        });

        setStatusWarmStandby(warmStandby.getState());
        setStatusAttempt(null);

        //warmStandby.enableLogging(this.logger);
    }


    /**
     * Event Handler for the event triggered by the Warm Standby when it switches over its configuration.
     *
     * @param event the event details object
     */
    private void onWarmStandbySwitchedOver(final EventObject event) {
        log.info("onWarmStandbySwitchedOver: " + event);

        setStatusServer(channel.getEndpoint());
        if (warmStandby.getState() != WarmStandbyState.OFF
                && warmStandby.getState() != WarmStandbyState.IDLE) {
            setStatusAttempt(warmStandby.getAttempt());
        }
        jtfPrimaryServer.setText(warmStandby.getConfiguration().getActiveEndpoint().toString());
        jtfBackupServer.setText(warmStandby.getConfiguration().getStandbyEndpoint().toString());
    }

    /**
     * Event Handler for the event triggered by the Warm Standby upon its stat change.
     *
     * @param event the event details object
     */
    private void onWarmStandbyStateChanged(
            final WarmStandbyStateChangedEvent event) {
        log.info("onWarmStandbyStateChanged: " + event);

        jbStart.setEnabled(warmStandby.getState() == WarmStandbyState.OFF);
        jbStop.setEnabled(warmStandby.getState() != WarmStandbyState.OFF);

        setStatusWarmStandby(warmStandby.getState());

        if (warmStandby.getState() == WarmStandbyState.WAITING) {
            setStatusAttempt(warmStandby.getAttempt());
        } else if (warmStandby.getState() == WarmStandbyState.RECONNECTING) {
            setStatusConnection(channel.getState());
        } else if (warmStandby.getState() == WarmStandbyState.OFF) {
            setStatusAttempt(null);
            jbOpen.setEnabled(true);
        } else if (warmStandby.getState() == WarmStandbyState.IDLE) {
            setStatusAttempt(null);
        }
    }

    /**
     * Event Handler for the event triggered by the Client Channel when it gets into the Opened state.
     *
     * @param event the event details object
     */
    private void onProtocolChannelOpened(final EventObject event) {
        log.info("onProtocolChannelOpened: " + event);

        jbOpen.setEnabled(false);
        jbClose.setEnabled(true);

        setStatusConnection(channel.getState());
    }

    /**
     * Event Handler for event triggered when the Client Channel gets into the Closed state.
     *
     * @param event the event details object
     */
    private void onProtocolChannelClosed(final ChannelClosedEvent event) {
        log.info("onProtocolChannelClosed: " + event);

        if (warmStandby.getState() == WarmStandbyState.OFF) {
            jbClose.setEnabled(false);
            jbOpen.setEnabled(true);
        } else {
            jbClose.setEnabled(false);
            jbOpen.setEnabled(false);
        }

        setStatusServer(channel.getEndpoint());
        setStatusConnection(channel.getState());
    }

    /**
     * Event Handler for event triggered when the Client Channel gets error.
     *
     * @param event the event details object
     */
    private void onProtocolChannelError(final ChannelErrorEvent event) {
        log.info("onProtocolChannelError: " + event);

        //setStatusConnection(channel.getState());
    }


    private void btnOpenClick() {
        log.debug("btnOpenClick");

        jtfPrimaryServer.setText(warmStandby.getConfiguration().getActiveEndpoint().toString());
        jtfBackupServer.setText(warmStandby.getConfiguration().getStandbyEndpoint().toString());

        jbOpen.setEnabled(false);

        if (this.warmStandby.getState() == WarmStandbyState.OFF) {
            jbClose.setEnabled(true);
        } else {
            jbClose.setEnabled(false);
        }

        setStatusServer(channel.getEndpoint());
        setStatusConnection(ChannelState.Opening);
        setStatusWarmStandby(warmStandby.getState());

        try {
            channel.beginOpen();
        } catch (Exception ex) {
            log.error("Error opening channel", ex);
        }
    }

    private void btnCloseClick() {
        log.debug("btnCloseClick");

        jbClose.setEnabled(false);
        jbOpen.setEnabled(true);

        try {
            channel.close();
        } catch (Exception ex) {
            log.warn("Error closing channel", ex);
        }

        setStatusConnection(ChannelState.Closed);
    }

    private void btnStartClick() {
        log.debug("btnStartClick");

        jtfPrimaryServer.setText(warmStandby.getConfiguration().getActiveEndpoint().toString());
        jtfBackupServer.setText(warmStandby.getConfiguration().getStandbyEndpoint().toString());

        if (channel.getState() == ChannelState.Closed) {
            jbOpen.setEnabled(true);
        } else {
            jbClose.setEnabled(true);
        }

        warmStandby.start();
    }

    private void btnStopClick() {
        log.debug("btnStopClick");

        if (channel.getState() == ChannelState.Closed) {
            jbOpen.setEnabled(true);
        } else {
            jbClose.setEnabled(true);
        }
        warmStandby.stop();
    }

    private void btnReconfigureClick() {
        log.debug("btnReconfigureClick");

        URI activeUri = null;
        URI standbyUri = null;

        try {
            activeUri = new URI(jtfPrimaryServer.getText());
        } catch (Exception ex) {
            log.error("Invalid primary server URI", ex);
        }

        try {
            standbyUri = new URI(jtfBackupServer.getText());
        } catch (Exception ex) {
            log.error("Invalid standby server URI", ex);
        }

        if (activeUri == null || standbyUri == null) {
            return;
        }

        int numericTimeout = 1000 * (Integer) jsTimeout.getValue();
        short numericAttemts = (short) ((Integer) jsAttempts.getValue()).intValue();
        short numericSwitchovers = (short) ((Integer) jsSwitchovers.getValue()).intValue();

        WarmStandbyConfiguration config =
            new WarmStandbyConfiguration(new Endpoint(activeUri), new Endpoint(standbyUri),
                    numericTimeout, numericAttemts);
        config.setSwitchovers(numericSwitchovers);

        warmStandby.applyConfiguration(config, true);

        jtfPrimaryServer.setText(warmStandby.getConfiguration().getActiveEndpoint().toString());
        jtfBackupServer.setText(warmStandby.getConfiguration().getStandbyEndpoint().toString());

        setStatusAttempt(null);
    }


    private void setStatusServer(final Endpoint ep) {
        String val = "Server: ";
        if (ep != null) {
            val += ep.toString();
        } else {
            val += "-";
        }
        log.info("STATUS: " + val);
        jtfStatusServer.setText(val);
    }

    private void setStatusConnection(final ChannelState state) {
        String val = "Connection: ";
        if (state != null) {
            val += state.toString();
        } else {
            val += "-";
        }
        log.info("STATUS: " + val);
        jtfStatusConnection.setText(val);
    }

    private void setStatusWarmStandby(final WarmStandbyState state) {
        String val = "WarmStandby: ";
        if (state != null) {
            val += state.toString();
        } else {
            val += "-";
        }
        log.info("STATUS: " + val);
        jtfStatusWarmStanby.setText(val);
    }

    private void setStatusAttempt(final Short attempt) {
        String val = "Attempt: ";
        if (attempt != null) {
            val += attempt.toString();
        }
        log.info("STATUS: " + val);
        jtfStatusAttempt.setText(val);
    }
}
