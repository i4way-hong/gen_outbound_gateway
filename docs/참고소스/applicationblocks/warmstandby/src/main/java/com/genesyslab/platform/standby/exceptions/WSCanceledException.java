/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby.exceptions;

/**
 * Throws when opening operation has been canceled. See the reason in cause.
 **/
public class WSCanceledException extends WSException {

    /**
     * Creates a WSCanceledException instance.
     * @param message description.
     * @param cause of the event.
     */
    public WSCanceledException(String message, Throwable cause) {
        super(message, cause);
    }

}
