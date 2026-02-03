package com.genoutbound.gateway.genesys.cfg.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GenesysProperties.class)
public class GenesysConfig {
}
