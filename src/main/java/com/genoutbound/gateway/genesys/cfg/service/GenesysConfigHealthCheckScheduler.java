package com.genoutbound.gateway.genesys.cfg.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Genesys Config health-check 스케줄러.
 *
 * <p>app.genesys.health-check-enabled=true 인 경우에만 스케줄 트리거가 등록됩니다.</p>
 */
@Component
@ConditionalOnProperty(prefix = "app.genesys", name = "health-check-enabled", havingValue = "true", matchIfMissing = true)
public class GenesysConfigHealthCheckScheduler {

    private final GenesysConfigClient configClient;

    public GenesysConfigHealthCheckScheduler(GenesysConfigClient configClient) {
        this.configClient = configClient;
    }

    @Scheduled(fixedDelayString = "${app.genesys.health-check-interval-ms:30000}")
    public void scheduleHealthCheck() {
        configClient.healthCheck();
    }
}
