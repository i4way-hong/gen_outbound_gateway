package com.genoutbound.gateway.sse;

import java.time.OffsetDateTime;

public record AppStatusEvent(
    Integer appDbid,
    String appName,
    Integer controlStatus,
    String status,
    String executionMode,
    String description,
    OffsetDateTime receivedAt
) {
}
