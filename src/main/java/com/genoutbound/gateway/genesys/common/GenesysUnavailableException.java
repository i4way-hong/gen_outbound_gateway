package com.genoutbound.gateway.genesys.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class GenesysUnavailableException extends RuntimeException {

    public GenesysUnavailableException(String message) {
        super(message);
    }

    public GenesysUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
