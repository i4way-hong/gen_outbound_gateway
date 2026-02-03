/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby.exceptions;

/**
 * Throws when endpoint pool is empty.
 */
public class WSEmptyEndpointPoolException extends WSNoAvailableServersException {

    /**
     * Creates a WSEmptyEndpointPoolException instance.
     * @param message description.
     * @param cause of the event.
     */
    public WSEmptyEndpointPoolException(String message, Throwable cause) {
        super(message, cause);
    }

}
