package com.genoutbound.gateway.security;

import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.security.dto.AuthRequest;
import com.genoutbound.gateway.security.dto.RefreshRequest;
import com.genoutbound.gateway.security.dto.TokenResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserDetailsService userDetailsService,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse login(AuthRequest request) {
        UserDetails userDetails = authenticate(request);
        return issueTokens(userDetails);
    }

    public TokenResponse refresh(RefreshRequest request) {
        try {
            if (!tokenProvider.isRefreshToken(request.refreshToken())) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
            }
            UserDetails userDetails = (UserDetails) tokenProvider.getAuthentication(request.refreshToken()).getPrincipal();
            return issueTokens(userDetails);
        } catch (JwtException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
        }
    }

    private UserDetails authenticate(AuthRequest request) {
        if (request.username() == null || request.username().isBlank()
            || request.password() == null || request.password().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "아이디/비밀번호를 입력하세요.");
        }
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(request.username());
        } catch (Exception ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "인증 실패");
        }
        if (!passwordEncoder.matches(request.password(), userDetails.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "인증 실패");
        }
        return userDetails;
    }

    private TokenResponse issueTokens(UserDetails userDetails) {
        String accessToken = tokenProvider.createAccessToken(userDetails);
        String refreshToken = tokenProvider.createRefreshToken(userDetails);
        return new TokenResponse(
            "Bearer",
            accessToken,
            refreshToken,
            tokenProvider.getAccessTokenExpiresInSeconds(),
            tokenProvider.getRefreshTokenExpiresInSeconds()
        );
    }
}
