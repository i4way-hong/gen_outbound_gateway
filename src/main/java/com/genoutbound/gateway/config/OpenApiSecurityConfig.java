package com.genoutbound.gateway.config;

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

    @Bean
    public OpenApiCustomizer hideBlockedApisCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null || openApi.getPaths().isEmpty()) {
                return;
            }

            List<String> toRemove = new ArrayList<>();
            openApi.getPaths().keySet().forEach(path -> {
                if ("/api/status".equals(path)
                        || path.startsWith("/api/v1/configuration")
                        || path.startsWith("/api/v1/crypto")
                        || path.startsWith("/api/v1/outbound")) {
                    toRemove.add(path);
                }
            });

            toRemove.forEach(path -> openApi.getPaths().remove(path));
        };
    }
}
