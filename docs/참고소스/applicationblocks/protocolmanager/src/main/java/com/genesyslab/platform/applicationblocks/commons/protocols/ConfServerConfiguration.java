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

import com.genesyslab.platform.commons.protocol.auth.AuthTicketAcquirer;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.runtime.channel.AesUtil;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;

import java.text.MessageFormat;


/**
 * Protocol Manager Application Block is deprecated.
 *
 * @see com.genesyslab.platform.configuration.protocol.ConfServerProtocolHandshakeOptions
 * @see com.genesyslab.platform.commons.connection.configuration.ClientConnectionOptions
 * @see com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions
 * @see com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration
 * @see com.genesyslab.platform.commons.connection.configuration.KeyValueConfiguration
 * @see com.genesyslab.platform.commons.protocol.Endpoint
 * @see com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration
 * 
 * @see com.genesyslab.platform.apptemplate.configuration.ClientConfigurationHelper
 * @deprecated
 * Use {@link com.genesyslab.platform.commons.protocol.Endpoint Endpoint} to manage protocol's configuration.
 */
@Deprecated
public final class ConfServerConfiguration
        extends ProtocolConfiguration {

    private String clientName;
    private CfgAppType clientType;
    private String userName;
//    private ConfServerSecureBytes userPassword;
    private String user_ps_wrd;
	private AuthTicketAcquirer ticketAcquirer;

    public ConfServerConfiguration(final String name) {
        super(name, ConfServerProtocol.class);
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(final String value) {
        this.clientName = value;
    }

    /**
     * Get value of the application type of the configuration server client.
     *
     * @return application type of the configuration server client
     */
    public CfgAppType getClientApplicationType() {
        return this.clientType;
    }

    /**
     * Set value of the application type of the configuration server client.
     *
     * @param value configuration server client application type
     */
    public void setClientApplicationType(final CfgAppType value) {
        this.clientType = value;
    }


    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String value) {
        this.userName = value;
    }

  @Deprecated
  public String getUserPassword() {
      return this.user_ps_wrd != null ? "" : null;
  }

  /**
   * @deprecated for internal use only
   */
  @Deprecated 
  String _getUserPassword() {
      return this.user_ps_wrd;
  }
  
  public void setUserPassword(final String value) {
      this.user_ps_wrd = value;
  }
    
    
//    @Deprecated
//    public String getUserPassword() {
//        return "";
//    }
//    
//    @Deprecated
//    public void setUserPassword(final String value) {
//        this.userPassword = AesUtil.toSecuredBytes(value);
//    }



//    ConfServerSecureBytes getEncryptedUserPassword() {
//        return this.userPassword;
//    }

//    public void setSecuredUserPassword(final CharSequence value) {
//        this.userPassword = AesUtil.toSecuredBytes(value);
//    }

    
	public AuthTicketAcquirer getTicketAcquirer() {
		return ticketAcquirer;
	}

	public void setTicketAcquirer(AuthTicketAcquirer ticketAcquirer) {
		this.ticketAcquirer = ticketAcquirer;
	}


	public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());

        sb.append(MessageFormat.format("ClientName: {0}\n", clientName));
        sb.append(MessageFormat.format("ClientType: {0}\n", clientType));
        sb.append(MessageFormat.format("UserName: {0}\n", userName));
        sb.append("UserPassword: *******\n");

        if (ticketAcquirer != null) {
            sb.append(MessageFormat.format("TicketAcquirer: {0}\n", ticketAcquirer));
        }
        
        return sb.toString();
    }
}
