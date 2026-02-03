package com.genoutbound.gateway.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
    @Schema(description = "토큰 타입", example = "Bearer")
    String tokenType,
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    String accessToken,
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    String refreshToken,
    @Schema(description = "액세스 토큰 만료(초)", example = "900")
    long accessTokenExpiresIn,
    @Schema(description = "리프레시 토큰 만료(초)", example = "604800")
    long refreshTokenExpiresIn
) {
}
