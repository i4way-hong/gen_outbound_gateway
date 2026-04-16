package com.genoutbound.gateway.genesys.common;

/**
 * Genesys 연동 timeout/retry 관련 설정값 검증 유틸입니다.
 */
public final class GenesysConnectionConfigValidator {

    private static final int MIN_ADDP_TIMEOUT = 1;
    private static final int MAX_ADDP_TIMEOUT = 300;
    private static final int MIN_REQUEST_TIMEOUT_MS = 100;
    private static final int MAX_REQUEST_TIMEOUT_MS = 120000;
    private static final int MIN_HEALTH_CHECK_INTERVAL_MS = 1000;
    private static final int MAX_HEALTH_CHECK_INTERVAL_MS = 600000;
    private static final int MIN_RETRY_DELAY_MS = 0;
    private static final int MAX_RETRY_DELAY_MS = 10000;

    private GenesysConnectionConfigValidator() {
    }

    public static void validateConfigClientTimeouts(int addpClientTimeout,
                                                    int addpServerTimeout,
                                                    int healthCheckIntervalMs) {
        requireRange("app.genesys.addp-client-timeout", addpClientTimeout,
            MIN_ADDP_TIMEOUT, MAX_ADDP_TIMEOUT);
        requireRange("app.genesys.addp-server-timeout", addpServerTimeout,
            MIN_ADDP_TIMEOUT, MAX_ADDP_TIMEOUT);
        requireRange("app.genesys.health-check-interval-ms", healthCheckIntervalMs,
            MIN_HEALTH_CHECK_INTERVAL_MS, MAX_HEALTH_CHECK_INTERVAL_MS);
    }

    public static void validateStatClientTimeouts(int timeoutMs,
                                                  int delayMs,
                                                  int addpClientTimeout,
                                                  int addpServerTimeout) {
        requireRange("app.stat.timeout-ms", timeoutMs,
            MIN_REQUEST_TIMEOUT_MS, MAX_REQUEST_TIMEOUT_MS);
        requireRange("app.stat.delay-ms", delayMs,
            MIN_RETRY_DELAY_MS, MAX_RETRY_DELAY_MS);
        requireRange("app.stat.addp-client-timeout", addpClientTimeout,
            MIN_ADDP_TIMEOUT, MAX_ADDP_TIMEOUT);
        requireRange("app.stat.addp-server-timeout", addpServerTimeout,
            MIN_ADDP_TIMEOUT, MAX_ADDP_TIMEOUT);
    }

    public static void validateTServerClientTimeouts(int addpClientTimeout,
                                                     int addpServerTimeout) {
        requireRange("app.tserver.addp-client-timeout", addpClientTimeout,
            MIN_ADDP_TIMEOUT, MAX_ADDP_TIMEOUT);
        requireRange("app.tserver.addp-server-timeout", addpServerTimeout,
            MIN_ADDP_TIMEOUT, MAX_ADDP_TIMEOUT);
    }

    private static void requireRange(String propertyName, int value, int min, int max) {
        if (value < min || value > max) {
            throw new IllegalStateException(
                propertyName + " 설정이 허용 범위를 벗어났습니다. "
                    + "현재값=" + value + ", 허용범위=" + min + ".." + max);
        }
    }
}