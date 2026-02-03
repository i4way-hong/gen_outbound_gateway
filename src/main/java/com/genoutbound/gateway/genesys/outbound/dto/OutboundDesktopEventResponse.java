package com.genoutbound.gateway.genesys.outbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.Map;

public record OutboundDesktopEventResponse(
        @Schema(description = "수신 시각")
        OffsetDateTime receivedAt,
        @Schema(description = "UserEvent ID")
        Integer userEventId,
        @Schema(description = "메시지 타입")
        String messageType,
        @Schema(description = "OutboundDesktop KV payload")
        Map<String, Object> userData
) {
}
