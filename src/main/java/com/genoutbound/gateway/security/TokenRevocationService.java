package com.genoutbound.gateway.security;

import io.jsonwebtoken.JwtException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TokenRevocationService {

    private static final Logger log = LoggerFactory.getLogger(TokenRevocationService.class);

    private final JwtTokenProvider tokenProvider;
    private final Map<String, Instant> revokedTokens = new ConcurrentHashMap<>();

    public TokenRevocationService(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public void revoke(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        try {
            Instant expiresAt = tokenProvider.getExpiration(token);
            if (expiresAt == null || expiresAt.isBefore(Instant.now())) {
                return;
            }
            revokedTokens.put(token, expiresAt);
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("유효하지 않은 토큰 폐기 요청: {}", ex.getMessage());
        }
    }

    public boolean isRevoked(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }
        Instant expiresAt = revokedTokens.get(token);
        if (expiresAt == null) {
            return false;
        }
        if (expiresAt.isBefore(Instant.now())) {
            revokedTokens.remove(token);
            return false;
        }
        return true;
    }
}