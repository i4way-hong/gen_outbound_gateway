package com.genoutbound.gateway.genesys.outbound;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Outbound 설정 바인딩을 활성화합니다.
 */
@Configuration
@EnableConfigurationProperties({OutboundProperties.class, OutboundDesktopProperties.class})
public class OutboundConfig {
}
