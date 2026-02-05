package com.genoutbound.gateway.genesys.stat;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StatServerProperties.class)
public class StatServerConfig {
}
