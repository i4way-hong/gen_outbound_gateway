package com.genoutbound.gateway.genesys.outbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

public record OutboundDesktopPayloadResponse(
        @Schema(description = "메시지 타입", example = "AddRecord")
        String messageType,
        @Schema(description = "OutboundDesktop KV payload")
        Map<String, Object> userData,
        @Schema(description = "POC용 시뮬레이션 여부", example = "true")
        boolean simulated
) {
}
