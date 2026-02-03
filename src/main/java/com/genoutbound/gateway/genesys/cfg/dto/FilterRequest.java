package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;

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
) {
}
