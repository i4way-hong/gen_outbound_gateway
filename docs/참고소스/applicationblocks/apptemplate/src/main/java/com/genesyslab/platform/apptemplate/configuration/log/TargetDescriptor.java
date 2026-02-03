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


/**
 * Base class for description of a configured logging target.
 */
public class TargetDescriptor {

    private final TargetType targetType;
    private final VerboseLevel supportedVerboseLevel;


    /**
     * The target description object constructor.
     *
     * @param targetType the logging target type.
     * @param supportedVerboseLevel the verbose level for the logging target.
     */
    public TargetDescriptor(
            final TargetType targetType,
            final VerboseLevel supportedVerboseLevel) {
        this.targetType = targetType;
        this.supportedVerboseLevel = supportedVerboseLevel;
    }


    /**
     * Returns logging target type.
     *
     * @return the target type.
     */
    public TargetType getTargetType() {
        return targetType;
    }

    /**
     * Returns verbose level for the log target.
     *
     * @return the verbose level.
     */
    public VerboseLevel getSupportedVerboseLevel() {
        return supportedVerboseLevel;
    }


    /**
     * Returns readable string representation of the logging target.
     */
    @Override
    public String toString() {
        return "LoggingTarget(type=" + targetType + ",level=" + supportedVerboseLevel + ")";
    }

    /**
     * Compares types of logging targets. It does not take into account logging verbose level -
     * it is required for targets consolidation (normalization) functions.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TargetDescriptor)) {
            return false;
        }
        final TargetDescriptor od = (TargetDescriptor) obj;
        if (od.getTargetType() != this.getTargetType()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = getClass().hashCode();
        if (targetType != null) {
            h = h * 31 + targetType.hashCode();
        }
        return h;
    }
}
