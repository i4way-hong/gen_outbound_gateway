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
 * Base logging target description extension for file logging target type to support filename property.
 */
public class GFileTargetDescriptor extends TargetDescriptor {

    private final String filename;


    /**
     * The logfile logging target description constructor.
     *
     * @param filename the log file name.
     * @param supportedVerboseLevel the log target verbose level.
     */
    public GFileTargetDescriptor(
            final String filename,
            final VerboseLevel supportedVerboseLevel) {
        super(TargetType.FILELOGGER, supportedVerboseLevel);
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("No filename given for GFileTargetDescriptor");
        }
        this.filename = filename;
    }


    /**
     * Returns the log file name as it was defined in the log options configuration.
     *
     * @return the log file name.
     */
    public String getFilename() {
        return filename;
    }


    /**
     * Returns readable string representation of the logging target.
     */
    @Override
    public String toString() {
        return "LoggingTarget(type=" + getTargetType() + ",level=" + getSupportedVerboseLevel()
                + ",filename=" + filename + ")";
    }


    /**
     * Compares types and filenames of logging targets.
     * It does not take into account logging verbose level -
     * it is required for targets consolidation (normalization) functions.
     */
    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof GFileTargetDescriptor)) {
            return false;
        }
        GFileTargetDescriptor od = (GFileTargetDescriptor) obj;
        return filename.equalsIgnoreCase(od.filename);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = hash * 31 + filename.hashCode();
        return hash;
    }
}
