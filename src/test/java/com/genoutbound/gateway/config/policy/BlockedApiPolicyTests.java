package com.genoutbound.gateway.config.policy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class BlockedApiPolicyTests {

    private final BlockedApiPolicy blockedApiPolicy = new BlockedApiPolicy();

    @Test
    void blockedPatternsArray_includesPatternAndConvertedPrefixRules() {
        List<String> patterns = Arrays.asList(blockedApiPolicy.blockedPatternsArray());

        assertTrue(patterns.contains("/api/status"));
        assertTrue(patterns.contains("/api/v1/outbound/**"));
        assertTrue(patterns.contains("/api/v1/configuration/agent-groups/**"));
    }

    @Test
    void isBlockedApiPath_matchesPatternAndPrefixRules() {
        assertTrue(blockedApiPolicy.isBlockedApiPath("/api/status"));
        assertTrue(blockedApiPolicy.isBlockedApiPath("/api/v1/configuration/agent-groups/123"));
        assertTrue(blockedApiPolicy.isBlockedApiPath("api/v1/configuration/persons/88"));
    }

    @Test
    void isBlockedApiPath_allowsNonBlockedPath() {
        assertFalse(blockedApiPolicy.isBlockedApiPath("/auth/login"));
        assertFalse(blockedApiPolicy.isBlockedApiPath("/swagger-ui/index.html"));
    }
}
