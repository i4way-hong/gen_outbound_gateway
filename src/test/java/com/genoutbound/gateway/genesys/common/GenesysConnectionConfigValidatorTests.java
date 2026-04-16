package com.genoutbound.gateway.genesys.common;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class GenesysConnectionConfigValidatorTests {

    @Test
    void validateConfigClientTimeouts_acceptsBoundaryValues() {
        assertDoesNotThrow(() -> GenesysConnectionConfigValidator.validateConfigClientTimeouts(1, 300, 600000));
    }

    @Test
    void validateConfigClientTimeouts_rejectsOutOfRangeHealthCheck() {
        assertThrows(IllegalStateException.class,
            () -> GenesysConnectionConfigValidator.validateConfigClientTimeouts(5, 5, 600001));
    }

    @Test
    void validateStatClientTimeouts_rejectsNegativeDelay() {
        assertThrows(IllegalStateException.class,
            () -> GenesysConnectionConfigValidator.validateStatClientTimeouts(5000, -1, 5, 5));
    }
}
