/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby.exceptions;

/**
 * Throws when all endpoints have been tried unsuccessfully.
 */
public class WSNoAvailableServersException extends WSException {

    /**
     * Creates a WSNoAvailableServersException instance.
     * @param message description.
     * @param cause of the event.
     */
    public WSNoAvailableServersException(String message, Throwable cause) {
        super(message, cause);
    }

}
