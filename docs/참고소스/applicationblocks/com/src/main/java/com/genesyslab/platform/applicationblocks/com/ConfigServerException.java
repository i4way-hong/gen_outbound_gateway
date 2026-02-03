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
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.configuration.protocol.types.CfgErrorType;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectProperty;

import java.io.Serializable;


/**
 * Exception class used to show configuration server error detail.
 */
public class ConfigServerException
        extends ConfigException
        implements Serializable {
    private static final long serialVersionUID = -1254972435290915079L;

    /**
     * The type of error that has occurred.
     */
    private CfgErrorType errorType;

    /**
     * The type of configuration object this error applies to.
     */
    private CfgObjectType objectType;

    /**
     * The property of the configuration object which has caused the error.
     */
    private CfgObjectProperty objectProperty;

    /**
     * Creates the exception class.
     *
     * @param errType the type of error that has occurred
     * @param objType the type of configuration object this error applies to
     * @param objProperty the property of the configuration object which has caused the error
     * @param message exception message
     */
    public ConfigServerException(
            final CfgErrorType errType,
            final CfgObjectType objType,
            final CfgObjectProperty objProperty,
            final String message) {
        super(message);

        this.errorType = errType;
        this.objectType = objType;
        this.objectProperty = objProperty;
    }

    /**
     * Creates the exception class.
     *
     * @param message Exception Message
     */
    public ConfigServerException(final String message) {
        super(message);
    }

    /**
     * Creates the exception class.
     *
     * @param innerException the inner exception
     * @param message exception message
     */
    protected ConfigServerException(
            final String message, final Exception innerException) {
        super(message, innerException);
    }

    /**
     * The type of error that has occurred.
     *
     * @return error type
     */
    public final CfgErrorType getErrorType() {
        return errorType;
    }

    /**
     * The type of configuration object this error applies to.
     *
     * @return object type
     */
    public final CfgObjectType getObjectType() {
        return objectType;
    }

    /**
     * The property of the configuration object which has caused the error.
     *
     * @return property identifier
     */
    public final CfgObjectProperty getObjectProperty() {
        return objectProperty;
    }

    /**
     * Returns a short description of this exception.
     *
     * @return a string representation of this exception.
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(super.toString());

        ret.append(" (ErrorType=")
                .append(errorType)
                .append(", ObjectType=")
                .append(objectType)
                .append(", ObjectProperty=")
                .append(objectProperty)
                .append(")");

        return ret.toString();
    }
}
