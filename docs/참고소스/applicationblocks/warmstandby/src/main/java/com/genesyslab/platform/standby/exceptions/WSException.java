/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby.exceptions;

import com.genesyslab.platform.commons.PlatformException;

/**
 * It's base class for all warm standby exceptions.
 */
public abstract class WSException extends PlatformException {

    WSException(String message, Throwable cause) {
        super(message, cause);
    }

}
