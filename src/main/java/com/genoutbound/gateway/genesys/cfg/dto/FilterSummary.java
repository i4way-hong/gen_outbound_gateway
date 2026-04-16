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

        public FilterSummary {
                userProperties = copyNestedMap(userProperties);
        }

        @Override
        public java.util.Map<String, java.util.Map<String, String>> userProperties() {
                return copyNestedMap(userProperties);
        }

        private static java.util.Map<String, java.util.Map<String, String>> copyNestedMap(
                        java.util.Map<String, java.util.Map<String, String>> source) {
                if (source == null) {
                        return null;
                }
                java.util.Map<String, java.util.Map<String, String>> copy = new java.util.HashMap<>();
                source.forEach((key, value) -> copy.put(key, value == null ? null : java.util.Map.copyOf(value)));
                return java.util.Map.copyOf(copy);
        }
}
