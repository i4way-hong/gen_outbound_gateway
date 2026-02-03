package com.genoutbound.gateway.genesys.cfg.dto;

public record FilterSummary(
        int dbid,
        String name,
        String description,
        boolean enabled,
        Integer formatDbid,
        String formatName,
        java.util.Map<String, java.util.Map<String, String>> userProperties
) {
}
