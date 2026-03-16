package com.genoutbound.gateway.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
        "/api/v1/configuration/transactions",
        "/api/v1/outbound/health",
        "/api/v1/outbound/desktop",
        "/api/status",
        //"/api/v1/crypto",
        "/api/v1/scs",
        "/api/v1/stat",
        "/api/v1/voice",
        "/favicon.ico"
    );

    private static final List<String> BLOCKED_TAGS = List.of(
        "Api Status",
        "Crypto Secure",
        //"Crypto Test",
        "SCS SSE",
        "Stat",
        "Tserver",
        "Favicon"
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
            openApi.setTags(filterTags(openApi.getTags(), paths));
        };
    }

    private List<Tag> filterTags(List<Tag> tags, Paths paths) {
        if (tags == null || tags.isEmpty()) {
            return tags;
        }
        Set<String> usedTags = new LinkedHashSet<>();
        paths.values().forEach(pathItem -> {
            addOperationTags(pathItem.getGet(), usedTags);
            addOperationTags(pathItem.getPut(), usedTags);
            addOperationTags(pathItem.getPost(), usedTags);
            addOperationTags(pathItem.getDelete(), usedTags);
            addOperationTags(pathItem.getOptions(), usedTags);
            addOperationTags(pathItem.getHead(), usedTags);
            addOperationTags(pathItem.getPatch(), usedTags);
            addOperationTags(pathItem.getTrace(), usedTags);
        });
        return tags.stream()
            .filter(tag -> tag.getName() != null)
            .filter(tag -> usedTags.contains(tag.getName()))
            .filter(tag -> !BLOCKED_TAGS.contains(tag.getName()))
            .collect(Collectors.toList());
    }

    private void addOperationTags(Operation operation, Set<String> usedTags) {
        if (operation == null || operation.getTags() == null) {
            return;
        }
        usedTags.addAll(operation.getTags());
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