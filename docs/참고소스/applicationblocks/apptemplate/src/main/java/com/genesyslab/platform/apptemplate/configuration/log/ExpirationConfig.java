// ===============================================================================
//  Genesys Platform SDK
// ===============================================================================
//
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
// Copyright (c) 2009 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.apptemplate.configuration.log;

import com.genesyslab.platform.apptemplate.util.ConfigurationUtil;

import com.genesyslab.platform.commons.log.ILogger;

import java.util.Locale;
import java.io.Serializable;


/**
 * This class is used for configuring the expiration of log files (and log segments).
 *
 * @see LogOptionsDescriptions#EXPIRE_OPT
 */
public final class ExpirationConfig
        implements Serializable {

    private static final long serialVersionUID = 6439465849407098549L;

    /**
     * Describes the possible type of log file expiration strategy.
     */
    public enum ExpirationStrategy {

        /**
         * The expiration is turned off. No limit either for log file number or for
         * log file age is set.
         * The {@link ExpirationConfig#getExpire()} property is ignored.
         */
        OFF,

        /**
         * The expiration is based on the number of files with the same log name in
         * the folder. I.e. the number of files with pattern <code>"&lt;logfilename&gt;.*.log"</code> is counted.<br/>
         * If the number of such files exceeds the value, set via {@link ExpirationConfig#getExpire()},
         * the oldest files will be deleted. The number of deleted file is equal
         * to 'currentFilesNumber - maxAllowedFilesNumber'.
         */
        NUMBER_OF_FILES,

        /**
         * The expiration is based on the file age. I.e. among all files with
         * pattern 'logfilename'.*.log the inactive files which are older than
         * {@link ExpirationConfig#getExpire()} days are deleted.
         */
        TIME_BASED
    }


    public static final ExpirationConfig NO_EXPIRATION =
            new ExpirationConfig(ExpirationStrategy.OFF, -1);

    public static final ExpirationConfig DEFAULT_EXPIRATION =
            new ExpirationConfig(ExpirationStrategy.NUMBER_OF_FILES, 10);

    private final ExpirationStrategy strategy;
    private final int expire;


    /**
     * Constructor.
     *
     * @param theStrategy the expiration strategy.
     * @param theExpire the expiration value.
     */
    public ExpirationConfig(
            final ExpirationStrategy theStrategy,
            final int theExpire) {
        if (theStrategy == null) {
            throw new IllegalArgumentException(
                    "Log Expiration Strategy is null");
        }
        this.strategy = theStrategy;
        if (theStrategy != ExpirationStrategy.OFF) {
            if (theExpire < 1) {
                throw new IllegalArgumentException(
                        "Log Expiration value is invalid: " + theExpire);
            }
            this.expire = theExpire;
        } else {
            this.expire = -1;
        }
    }


    /**
     * The {@link ExpirationStrategy} which describes the strategy of expiration.
     */
    public ExpirationStrategy getStrategy() {
        return strategy;
    }

    /**
     * The meaning of this property depends on the value of
     * {@link #getStrategy()}. See {@link ExpirationStrategy} for more details.
     */
    public int getExpire() {
        return expire;
    }

    /**
     * Returns readable string representation of the Expiration option value.
     */
    @Override
    public String toString() {
        return "ExpirationConfig(Strategy='" + strategy + "',Expire='" + expire + "')";
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ExpirationConfig)) {
            return false;
        }
        final ExpirationConfig argCfg = (ExpirationConfig) obj;
        if (argCfg.getStrategy() != strategy) {
            return false;
        }
        if (strategy == ExpirationStrategy.OFF) {
            return true;
        }
        return expire == argCfg.getExpire();
    }

    @Override
    public int hashCode() {
        int hash = getClass().hashCode();
        hash = hash ^ (strategy.hashCode() << 16);
        if (strategy != ExpirationStrategy.OFF) {
            hash = hash ^ expire;
        }
        return hash;
    }


    /**
     * Parses string option value in format of the expiration option {@link LogOptionsDescriptions#EXPIRE_OPT}.
     *
     * @param optionVal the option value for parsing.
     * @param logger optional "status" logger for parsing errors printing.
     * @return Parsed value of Expiration configuration or null.
     * @see LogOptionsDescriptions#EXPIRE_OPT
     */
    public static ExpirationConfig parse(
            final String optionVal,
            final ILogger logger) {
        if (optionVal == null) {
            return null;
        }
        String optionString = optionVal.trim();
        if (optionString.isEmpty()) {
            return null;
        }

        if (ConfigurationUtil.isFalse(optionString)) {
            return NO_EXPIRATION;
        }
        optionString = optionString.toLowerCase(Locale.ENGLISH);

        int expireval;
        ExpirationStrategy strategy;

        int i = optionString.indexOf("day");
        if (i > -1) {
            strategy = ExpirationStrategy.TIME_BASED;
            optionString = optionString.substring(0, i).trim();
            try {
                expireval = Integer.parseInt(optionString);
            } catch (Exception e) {
                if (logger != null) {
                    logger.errorFormat(
                            "The segment option has invalid value '{0}'", optionVal);
                }
                return null;
            }
            if (expireval < 1 || expireval > 100) {
                if (logger != null) {
                    logger.errorFormat(
                            "The segment option has invalid value '{0} days', set to '10 days'",
                            expireval);
                }
                expireval = 10;
            }
            return new ExpirationConfig(strategy, expireval);
        }

        i = optionString.indexOf("file");
        if (i > -1) {
            strategy = ExpirationStrategy.NUMBER_OF_FILES;
            optionString = optionString.substring(0, i).trim();
            try {
                expireval = Integer.parseInt(optionString);
            } catch (final Exception e) {
                return null;
            }
            if (expireval < 1 || expireval > 1000) {
                if (logger != null) {
                    logger.errorFormat(
                            "The segment option has invalid value '{0} files', set to '10 files'",
                            expireval);
                }
                expireval = 10;
            }
            return new ExpirationConfig(strategy, expireval);
        }

        // default specifier is 'files':
        strategy = ExpirationStrategy.NUMBER_OF_FILES;
        try {
            expireval = Integer.parseInt(optionString);
        } catch (final Exception e) {
            return null;
        }
        if (expireval >= 1 && expireval <= 100) {
            return new ExpirationConfig(strategy, expireval);
        }

        return null;
    }
}
