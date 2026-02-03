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
//
// ===============================================================================
package com.genesyslab.platform.applicationblocks.warmstandby;

import com.genesyslab.platform.commons.protocol.Endpoint;


/**
 * The WarmStandbyConfiguration class contains redundancy configuration options, including:<ul>
 * <li>two server endpoints - one for the active Endpoint, and one for a backup Endpoint</li>
 * <li>the number of reconnection attempts to be made</li>
 * <li>timeout between the reconnection attempts (in milliseconds)</li>
 * <li>the number of primary/backup switchovers allowed</li>
 * </ul>
 */
public class WarmStandbyConfiguration {

    /**
     * Maximum timeout/delay between channel reopening attempts.
     */
    public static final int MAX_TIMEOUT = 30 * 60 * 1000; // 30 min

    private Endpoint activeEndpoint;
    private Endpoint standbyEndpoint;
    private Integer timeout;
    private Short attempts;
    private Short switchovers;
    private Integer firstReconnectTimeout;


    /**
     * Creates a WarmStandbyConfiguration instance.
     *
     * @param activeEndpoint channel's Active Endpoint
     * @param standbyEndpoint channel's Standby Endpoint
     */
    public WarmStandbyConfiguration(
                final Endpoint activeEndpoint,
                final Endpoint standbyEndpoint) {
        if (activeEndpoint == null) {
            throw new NullPointerException("activeEndpoint is null.");
        }
        if (standbyEndpoint == null) {
            throw new NullPointerException("standbyEndpoint is null.");
        }
        this.activeEndpoint = activeEndpoint;
        this.standbyEndpoint = standbyEndpoint;
    }

    /**
     * Creates a WarmStandbyConfiguration instance.
     *
     * @param activeEndpoint channel's Active Endpoint
     * @param standbyEndpoint channel's Standby Endpoint
     * @param timeout timeout between reconnection attempts (in milliseconds)
     * @param attempts number of reconnection attempts
     */
    public WarmStandbyConfiguration(
                final Endpoint activeEndpoint,
                final Endpoint standbyEndpoint,
                final int timeout,
                final short attempts) {
        this(activeEndpoint, standbyEndpoint);

        if (timeout < 0 || timeout > MAX_TIMEOUT) {
            throw new IllegalArgumentException("timeout is out of range");
        }
        if (attempts < 0) {
            throw new IllegalArgumentException("attempts is out of range");
        }
        this.timeout = timeout;
        this.attempts = attempts;
    }

    /**
     * Creates a WarmStandbyConfiguration instance.
     *
     * @param activeEndpoint channel's Active Endpoint
     * @param standbyEndpoint channel's Standby Endpoint
     * @param timeout timeout between reconnection attempts (in milliseconds)
     * @param attempts number of reconnection attempts
     * @param switchovers number of primary/backup switchovers
     */
    public WarmStandbyConfiguration(
                final Endpoint activeEndpoint,
                final Endpoint standbyEndpoint,
                final int timeout,
                final short attempts,
                final short switchovers) {
        this(activeEndpoint, standbyEndpoint, timeout, attempts);

        if (switchovers < 0) {
            throw new IllegalArgumentException("switchovers is out of range");
        }
        this.switchovers = switchovers;
    }

    /**
     * Creates a WarmStandbyConfiguration instance.
     *
     * @param activeEndpoint channel's Active Endpoint
     * @param standbyEndpoint channel's Standby Endpoint
     * @param timeout timeout between reconnection attempts (in milliseconds)
     * @param attempts number of reconnection attempts
     * @param switchovers number of primary/backup switchovers
     * @param firstReconnectTimeout timeout of first fast reconnect
     */
    public WarmStandbyConfiguration(
                final Endpoint activeEndpoint,
                final Endpoint standbyEndpoint,
                final int timeout,
                final short attempts,
                final short switchovers,
                final int firstReconnectTimeout
                ) {
        this(activeEndpoint, standbyEndpoint, timeout, attempts, switchovers);
        
		if (firstReconnectTimeout < 0 
				|| firstReconnectTimeout>WarmStandbyConfiguration.MAX_TIMEOUT) {
            throw new IllegalArgumentException("firstReconnectTimeout is out of range");
        }
        this.firstReconnectTimeout = firstReconnectTimeout;
    }
    

    /**
     * Gets the active endpoint.
     *
     * @return active endpoint
     */
    public final Endpoint getActiveEndpoint() {
        return activeEndpoint;
    }

    /**
     * Sets the active endpoint.
     *
     * @param newActiveEndpoint active endpoint
     */
    public final void setActiveEndpoint(final Endpoint newActiveEndpoint) {
        if (newActiveEndpoint == null) {
            throw new NullPointerException("endpoint is null");
        }
        this.activeEndpoint = newActiveEndpoint;
    }

    /**
     * Gets the standby endpoint.
     *
     * @return standby endpoint
     */
    public final Endpoint getStandbyEndpoint() {
        return standbyEndpoint;
    }

    /**
     * Sets the standby endpoint.
     *
     * @param newStandbyEndpoint standby endpoint
     */
    public final void setStandbyEndpoint(final Endpoint newStandbyEndpoint) {
        if (newStandbyEndpoint == null) {
            throw new NullPointerException("endpoint is null");
        }
        this.standbyEndpoint = newStandbyEndpoint;
    }

    /**
     * Gets the number of primary/backup switchovers.
     *
     * @return the number of primary/backup switchovers
     */
    public final Short getSwitchovers() {
        return switchovers;
    }

    /**
     * Sets the number of primary/backup switchovers. If this parameter is not set,
     * or if it is set to null, then an unlimited number of switchovers are allowed.
     * If this parameter is set to 0 then switchover will not happen.
     *
     * @param switchoversNum the number of primary/backup switchovers
     */
    public final void setSwitchovers(final Short switchoversNum) {
        if (switchoversNum != null && switchoversNum < 0) {
            throw new IllegalArgumentException("switchovers is out of range");
        }
        this.switchovers = switchoversNum;
    }

    /**
     * Gets the timeout (in milliseconds) between the reconnection attempts.
     *
     * @return the timeout value in milliseconds
     */
    public final Integer getTimeout() {
        return timeout;
    }

    /**
     * Sets the newTimeout (in milliseconds) between the reconnection attempts.
     *
     * @param newTimeout the new timeout value in milliseconds
     */
    public final void setTimeout(final Integer newTimeout) {
        if (newTimeout != null) {
            if ((newTimeout < 0)
                || (newTimeout > WarmStandbyConfiguration.MAX_TIMEOUT)) {
                throw new IllegalArgumentException("newTimeout is out of range");
            }
        }
        this.timeout = newTimeout;
    }

    /**
     * Gets the maximum number of reconnection attempts.
     *
     * @return the number of reconnection attempts
     */
    public final Short getAttempts() {
        return attempts;
    }

    /**
     * Sets the maximum number of reconnection attempts.
     *
     * @param attemptsNum the number of reconnection attempts
     */
    public final void setAttempts(final Short attemptsNum) {
        if (attemptsNum != null && attemptsNum < 0) {
            throw new IllegalArgumentException("'attemptsNum' is out of range");
        }
        this.attempts = attemptsNum;
    }
    
    
    
    /**
     * Gets timeout (in milliseconds) of first fast reconnect.
     * @return timeout (in milliseconds) of first fast reconnect.
     */
    public Integer getFirstReconnectTimeout() {
		return firstReconnectTimeout;
	}

    /**
     * Sets timeout of first fast reconnect.
     * @param firstReconnectTimeout timeout of first fast reconnect in milliseconds.
     */
	public void setFirstReconnectTimeout(Integer firstReconnectTimeout) {
		if (firstReconnectTimeout != null) {
			if (firstReconnectTimeout < 0 
					|| firstReconnectTimeout>WarmStandbyConfiguration.MAX_TIMEOUT) {
	            throw new IllegalArgumentException("firstReconnectTimeout is out of range");
			}
		}
		this.firstReconnectTimeout = firstReconnectTimeout;
	}

	/**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(getClass().getName()).append(":\n");
        sb.append("  ActiveEndpoint  : ").append(activeEndpoint).append("\n");
        sb.append("  StandbyEndpoint : ").append(standbyEndpoint).append("\n");
        sb.append("  Timeout         : ").append(timeout).append("\n");
        sb.append("  Attempts        : ").append(attempts).append("\n");
        sb.append("  Switchovers     : ").append(switchovers).append("\n");

        return sb.toString();
    }
}
