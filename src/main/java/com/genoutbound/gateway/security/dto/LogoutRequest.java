package com.genoutbound.gateway.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LogoutRequest(
    @Schema(description = "리프레시 토큰(선택)", example = "eyJhbGciOiJIUzI1NiJ9...")
    String refreshToken
) {
}