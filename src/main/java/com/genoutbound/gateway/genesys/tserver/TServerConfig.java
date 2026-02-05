package com.genoutbound.gateway.genesys.tserver;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TServerProperties.class)
public class TServerConfig {
}
