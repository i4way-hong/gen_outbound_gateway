package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

public record FilterRequest(
        @Schema(description = "테넌트 DBID", example = "101")
        Integer tenantDbid,
        @Schema(description = "Filter 이름", example = "FILTER_A")
        String name,
        @Schema(description = "Filter 설명", example = "설명")
        String description,
        @Schema(description = "Format DBID", example = "2001")
        Integer formatDbid,
        //@Schema(description = "사용자 속성", example = "{\"Section\":{\"Key\":\"Value\"}}")

        @Schema(description = "사용자 속성", example = "{\"default\": {\"criteria\": \"HCC_CAMPAIGN_NO = \\\"12345\\\"\",\"order_by\": \"\"        }}")

        java.util.Map<String, java.util.Map<String, String>> userProperties,
        @Schema(description = "활성화 여부", example = "true")
        boolean enabled
) implements Serializable {

        public FilterRequest {
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
