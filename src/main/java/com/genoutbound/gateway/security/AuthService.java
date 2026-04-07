package com.genoutbound.gateway.security;

import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.security.dto.AuthRequest;
import com.genoutbound.gateway.security.dto.RefreshRequest;
import com.genoutbound.gateway.security.dto.TokenResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final TokenRevocationService tokenRevocationService;
    private final TokenVersionService tokenVersionService;

    public AuthService(UserDetailsService userDetailsService,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       TokenRevocationService tokenRevocationService,
                       TokenVersionService tokenVersionService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.tokenRevocationService = tokenRevocationService;
        this.tokenVersionService = tokenVersionService;
    }

    public TokenResponse login(AuthRequest request) {
        UserDetails userDetails = authenticate(request);
        long version = tokenVersionService.bumpVersion(userDetails.getUsername());
        return issueTokens(userDetails, version);
    }

    public TokenResponse refresh(RefreshRequest request) {
        try {
            if (!tokenProvider.isRefreshToken(request.refreshToken())) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
            }
            if (tokenRevocationService.isRevoked(request.refreshToken())) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "폐기된 리프레시 토큰입니다.");
            }
            UserDetails userDetails = (UserDetails) tokenProvider.getAuthentication(request.refreshToken()).getPrincipal();
            long tokenVersion = tokenProvider.getTokenVersion(request.refreshToken());
            long currentVersion = tokenVersionService.getCurrentVersion(userDetails.getUsername());
            if (tokenVersion != currentVersion) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "폐기된 리프레시 토큰입니다.");
            }
            tokenRevocationService.revoke(request.refreshToken());
            return issueTokens(userDetails, currentVersion);
        } catch (JwtException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
        }
    }

    public void logout(String accessToken, String refreshToken) {
        String username = null;
        if (accessToken != null && !accessToken.isBlank()) {
            try {
                if (!tokenProvider.isAccessToken(accessToken)) {
                    throw new ApiException(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다.");
                }
                username = tokenProvider.getAuthentication(accessToken).getName();
                tokenRevocationService.revoke(accessToken);
            } catch (ExpiredJwtException ex) {
                log.debug("logout 요청에서 만료된 액세스 토큰은 무시합니다.");
            } catch (JwtException ex) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다.");
            }
        }

        if (refreshToken != null && !refreshToken.isBlank()) {
            try {
                if (!tokenProvider.isRefreshToken(refreshToken)) {
                    throw new ApiException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
                }
                if (tokenRevocationService.isRevoked(refreshToken)) {
                    throw new ApiException(HttpStatus.UNAUTHORIZED, "폐기된 리프레시 토큰입니다.");
                }
                if (username == null) {
                    username = tokenProvider.getAuthentication(refreshToken).getName();
                }
                tokenRevocationService.revoke(refreshToken);
            } catch (JwtException ex) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
            }
        }

        if (username != null && !username.isBlank()) {
            tokenVersionService.bumpVersion(username);
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

        String normalizedRequestPassword = normalizeBcryptPrefix(request.password());
        String normalizedStoredPassword = normalizeBcryptPrefix(userDetails.getPassword());

        boolean authenticated;
        if (isEncodedPassword(normalizedRequestPassword)) {
            authenticated = normalizedRequestPassword.equals(normalizedStoredPassword);
        } else {
            authenticated = passwordEncoder.matches(request.password(), normalizedStoredPassword);
        }

        if (!authenticated) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "인증 실패");
        }
        return userDetails;
    }

    private boolean isEncodedPassword(String password) {
        return password != null && password.startsWith("{") && password.contains("}");
    }

    private String normalizeBcryptPrefix(String password) {
        if (password == null || password.isBlank()) {
            return password;
        }
        if (password.startsWith("{")) {
            return password;
        }
        if (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$")) {
            return "{bcrypt}" + password;
        }
        return password;
    }

    private TokenResponse issueTokens(UserDetails userDetails, long tokenVersion) {
        String accessToken = tokenProvider.createAccessToken(userDetails, tokenVersion);
        String refreshToken = tokenProvider.createRefreshToken(userDetails, tokenVersion);
        return new TokenResponse(
            "Bearer",
            accessToken,
            refreshToken,
            tokenProvider.getAccessTokenExpiresInSeconds(),
            tokenProvider.getRefreshTokenExpiresInSeconds()
        );
    }
}
