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
 * Describes the rule of segmentation of log files.
 *
 * @see LogOptionsDescriptions#SEGMENT_OPT
 */
public final class SegmentationConfig
        implements Serializable {

    private static final long serialVersionUID = -3954418617682392448L;

    /**
     * Describes type of segmentation.
     */
    public enum SegmentationStrategy {

        /**
         * No segmentation of log file.
         */
        OFF,

        /**
         * The log file is divided into segments by size, which length (in KB) is
         * set by {@link SegmentationConfig#getSegment()}.
         */
        SIZE_KB,

        /**
         * The log file is divided into segments by size, which length (in MB) is
         * set by {@link SegmentationConfig#getSegment()}.
         */
        SIZE_MB,

        /**
         * The log file is divided into segments by time, the old segment is closed
         * and new is started each {@link SegmentationConfig#getSegment()} hours.
         */
        TIME_BASED
    }


    public static final SegmentationConfig NO_SEGMENTATION =
            new SegmentationConfig(SegmentationStrategy.OFF, -1);

    public static final SegmentationConfig DEFAULT_SEGMENTATION =
            new SegmentationConfig(SegmentationStrategy.SIZE_MB, 100);


    private final SegmentationStrategy strategy;
    private final int segment;


    /**
     * Constructor.
     *
     * @param theStrategy the value for {@link SegmentationConfig#getStrategy()}.
     * @param theSegment the value for {@link SegmentationConfig#getSegment()}.
     */
    public SegmentationConfig(
            final SegmentationStrategy theStrategy,
            final int theSegment) {
        if (theStrategy == null) {
            throw new IllegalArgumentException(
                    "Log Segmentation Strategy is null");
        }
        this.strategy = theStrategy;
        if (theStrategy != SegmentationStrategy.OFF) {
            if (theSegment < 1) {
                throw new IllegalArgumentException(
                        "Log Segmentation value is invalid: " + theSegment);
            }
            this.segment = theSegment;
        } else {
            this.segment = -1;
        }
    }

    /**
     * Describes the type of segmentation used.
     *
     * @return type of segmentation used 
     */
    public SegmentationStrategy getStrategy() {
        return strategy;
    }

    /**
     * Numeric value which meaning depends on the value of {@link SegmentationConfig#getStrategy()}.
     *
     * @return segment numeric value
     */
    public int getSegment() {
        return segment;
    }


    /**
     * Parses string option value in format of the segmentation option {@link LogOptionsDescriptions#SEGMENT_OPT}.
     *
     * @param optionVal the option value for parsing.
     * @param logger optional "status" logger for parsing errors printing.
     * @return Parsed value of Segmentation configuration or null.
     * @see LogOptionsDescriptions#SEGMENT_OPT
     */
    public static SegmentationConfig parse(
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
            return NO_SEGMENTATION;
        }
        optionString = optionString.toLowerCase(Locale.ENGLISH);

        SegmentationStrategy segmentationStrategy;
        int segmentval;
        String valueString;

        int i = optionString.indexOf("hr");
        if (i > -1) {
            segmentationStrategy = SegmentationStrategy.TIME_BASED;
            valueString = optionString.substring(0, i).trim();
            try {
                segmentval = Integer.parseInt(valueString);
            } catch (final NumberFormatException e) {
                if (logger != null) {
                    logger.errorFormat(
                        "The segment option has invalid value '{0}'", optionVal);
                }
                return null;
            }
            if (segmentval < 1) {
                if (logger != null) {
                    logger.errorFormat(
                        "The segment option has invalid value '{0} hrs'",
                        segmentval);
                }
                return null;
            }
            return new SegmentationConfig(segmentationStrategy, segmentval);
        }

        i = optionString.indexOf("mb");
        if (i > -1) {
            segmentationStrategy = SegmentationStrategy.SIZE_MB;
            valueString = optionString.substring(0, i).trim();
            try {
                segmentval = Integer.parseInt(valueString);
            } catch (final NumberFormatException e) {
                if (logger != null) {
                    logger.errorFormat(
                        "The segment option has invalid value '{0}'", optionVal);
                }
                return null;
            }
            if (segmentval < 1) {
                if (logger != null) {
                    logger.errorFormat(
                        "The segment option has invalid value '{0} MBs'",
                        segmentval);
                }
                return null;
            }
            return new SegmentationConfig(segmentationStrategy, segmentval);
        }

        i = optionString.indexOf("kb");
        if (i > -1) {
            segmentationStrategy = SegmentationStrategy.SIZE_KB;
            valueString = optionString.substring(0, i).trim();
            try {
                segmentval = Integer.parseInt(valueString);
            } catch (final NumberFormatException e) {
                if (logger != null) {
                    logger.errorFormat(
                        "The segment option has invalid value '{0}'", optionVal);
                }
                return null;
            }
            if (segmentval < 100) {
                if (logger != null) {
                    logger.errorFormat(
                        "The segment option has invalid value '{0} KBs' (min is '100 KBs')",
                        segmentval);
                }
                return null;
            }
            return new SegmentationConfig(segmentationStrategy, segmentval);
        }

        // Default specifier is 'KB'
        segmentationStrategy = SegmentationStrategy.SIZE_KB;
        try {
            segmentval = Integer.parseInt(optionString);
        } catch (final Exception e) {
            if (logger != null) {
                logger.errorFormat(
                        "The segment option has invalid value '{0}', set to '100 KBs'",
                        optionVal);
            }
            return null;
        }
        if (segmentval < 100) {
            if (logger != null) {
                logger.errorFormat(
                        "The segment option has invalid value '{0}', set to '100 KBs'",
                        optionVal);
            }
            segmentval = 100;
        }

        return new SegmentationConfig(segmentationStrategy, segmentval);
    }

    /**
     * Returns readable string representation of the Segmentation option value.
     */
    @Override
    public String toString() {
        return "SegmentationConfig(Strategy='" + strategy + "',Segment='" + segment + "')";
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SegmentationConfig)) {
            return false;
        }
        SegmentationConfig argCfg = (SegmentationConfig) obj;
        if (argCfg.getStrategy() != strategy) {
            return false;
        }
        if (strategy == SegmentationStrategy.OFF) {
            return true;
        }
        return segment == argCfg.getSegment();
    }

    @Override
    public int hashCode() {
        int hash = getClass().hashCode();
        hash = hash ^ (strategy.hashCode() << 16);
        if (strategy != SegmentationStrategy.OFF) {
            hash = hash ^ segment;
        }
        return hash;
    }
}
