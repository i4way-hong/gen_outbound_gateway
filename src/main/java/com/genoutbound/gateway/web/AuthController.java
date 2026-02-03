package com.genoutbound.gateway.web;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.security.AuthService;
import com.genoutbound.gateway.security.dto.AuthRequest;
import com.genoutbound.gateway.security.dto.RefreshRequest;
import com.genoutbound.gateway.security.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "JWT 인증 토큰 발급 API")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "토큰 발급", description = "아이디/비밀번호로 JWT 액세스/리프레시 토큰을 발급합니다.")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TokenResponse> login(@Valid @RequestBody AuthRequest request) {
        return ApiResponse.ok("토큰 발급", authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 새로운 액세스/리프레시 토큰을 발급합니다.")
    public ApiResponse<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ApiResponse.ok("토큰 갱신", authService.refresh(request));
    }
}
