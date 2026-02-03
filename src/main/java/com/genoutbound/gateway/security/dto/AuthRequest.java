package com.genoutbound.gateway.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @Schema(description = "로그인 아이디", example = "appadm")
    @NotBlank String username,
    @Schema(description = "로그인 비밀번호", example = "secret")
    @NotBlank String password
) {
}
