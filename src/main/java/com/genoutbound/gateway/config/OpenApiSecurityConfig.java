package com.genoutbound.gateway.config;

import com.genoutbound.gateway.config.policy.BlockedApiPolicy;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiSecurityConfig {

    private final BlockedApiPolicy blockedApiPolicy;

    public OpenApiSecurityConfig(BlockedApiPolicy blockedApiPolicy) {
        this.blockedApiPolicy = blockedApiPolicy;
    }

    @Bean
    public OpenApiCustomizer hideBlockedApisCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null || openApi.getPaths().isEmpty()) {
                return;
            }

            List<String> toRemove = new ArrayList<>();
            openApi.getPaths().keySet().forEach(path -> {
                if (blockedApiPolicy.isBlockedApiPath(path)) {
                    toRemove.add(path);
                }
            });

            toRemove.forEach(path -> openApi.getPaths().remove(path));
        };
    }
}
