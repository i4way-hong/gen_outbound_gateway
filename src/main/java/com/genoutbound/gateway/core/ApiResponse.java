package com.genoutbound.gateway.core;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;

@Schema(description = "공통 API 응답")
public record ApiResponse<T>(
    @Schema(description = "성공 여부", example = "true")
    boolean success,
    @Schema(description = "응답 메시지", example = "요청 성공")
    String message,
    @Schema(description = "응답 데이터")
    T data,
    @Schema(description = "응답 시각", example = "2026-01-22T10:00:00+09:00")
    OffsetDateTime timestamp) {

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, OffsetDateTime.now());
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, message, null, OffsetDateTime.now());
    }
}
