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

import com.genesyslab.platform.commons.GEnum;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;


public class WarmStandbyQuickStartConfiguration {

    private ProtocolType protocol;
    private String clientName;

    private String primaryServer;
    private String backupServer;
    private short attempts;
    private int timeout;
    private short switchovers = -1;

    private String userName;
    private String userPassword;
    private int clientType;


    public WarmStandbyQuickStartConfiguration() {
        this(null);
    }

    public WarmStandbyQuickStartConfiguration(
            final Properties custProps) {
        Properties props = custProps;
        if (props == null) {
            props = new Properties();
            File fl = new File("quickstart.properties");
            try {
                props.load(new FileInputStream(fl));
            } catch (IOException e) {
                throw new RuntimeException(
                        "Exception reading properties: " + fl.getAbsolutePath(), e);
            }
        }

        String protName = props.getProperty("Channel.ProtocolName", "ConfigurationServer");
        protocol = (ProtocolType) GEnum.getValue(ProtocolType.class, protName);
        if (protocol == null) {
            throw new RuntimeException("Configuration exception: unrecognized protocol name "
                    + protName);
        }
        clientName = props.getProperty("Channel.ClientName", "default");
        clientType = Integer.parseInt(props.getProperty("Channel.ClientType", "0"));

        primaryServer = props.getProperty("WarmStandby.PrimaryServer");
        if (primaryServer == null || primaryServer.equals("")) {
            throw new RuntimeException("'WarmStandby.PrimaryServer' is not defined");
        }
        backupServer = props.getProperty("WarmStandby.BackupServer");
        if (backupServer == null || backupServer.equals("")) {
            throw new RuntimeException("'WarmStandby.BackupServer' is not defined");
        }
        attempts = Short.parseShort(props.getProperty("WarmStandby.Attempts", "3"));
        timeout = Integer.parseInt(props.getProperty("WarmStandby.Timeout", "10"));
        switchovers = Short.parseShort(props.getProperty("WarmStandby.Switchovers", "5"));

        if (protocol == ProtocolType.ConfigurationServer) {
            userName = props.getProperty("ConfServer.UserName", "default");
            userPassword = props.getProperty("ConfServer.UserPassword", "");
        }
    }


    /**
     * Gets the protocol type.
     */
    public ProtocolType getProtocol() {
        return protocol;
    }

    /**
     * Gets the client name.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Gets the Primary/Active server's Uri in string format.
     */
    public String getPrimaryServer() {
        return primaryServer;
    }

    /**
     * Gets the Backup/Standby server's Uri in string format.
     */
    public String getBackupServer() {
        return backupServer;
    }

    /**
     * Gets the number of reconnection (connection reopening) attempts.
     */
    public short getAttempts() {
        return attempts;
    }

    /**
     * Gets the value of timeout (in sec) between reconnection (connection reopening) attempts.
     */
    public int getTimeout() {
        return this.timeout;
    }

    /**
     * Gets user name (Configuration Protocol specific property).
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets user password (Configuration Protocol specific property).
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Gets client (application) type (Configuration Protocol specific property).
     */
    public int getClientType() {
        return clientType;
    }

    /**
     * Gets the number of primary/backup switchovers.
     */
    public short getSwitchovers() {
        return switchovers;
    }

    /**
     * Sets the number of primary/backup switchovers.
     */
    public void setSwitchovers(final short value) {
        switchovers = value;
    }
}
