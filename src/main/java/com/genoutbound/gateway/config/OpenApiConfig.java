package com.genoutbound.gateway.config;

import io.swagger.v3.oas.models.Paths;
import java.util.List;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final List<String> BLOCKED_PREFIXES = List.of(
        "/api/v1/configuration/agent-groups",
        "/api/v1/configuration/agent-logins",
        "/api/v1/configuration/dn-groups",
        "/api/v1/configuration/dns",
        "/api/v1/configuration/persons",
        "/api/v1/configuration/place-groups",
        "/api/v1/configuration/places",
        "/api/v1/configuration/transactions"
    );

    @Bean
    public OpenApiCustomizer configurationApiOpenApiCustomizer() {
        return openApi -> {
            Paths paths = openApi.getPaths();
            if (paths == null || paths.isEmpty()) {
                return;
            }
            for (String path : List.copyOf(paths.keySet())) {
                if (isBlockedPath(path)) {
                    paths.remove(path);
                }
            }
            openApi.setPaths(paths);
        };
    }

    private boolean isBlockedPath(String path) {
        for (String prefix : BLOCKED_PREFIXES) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}