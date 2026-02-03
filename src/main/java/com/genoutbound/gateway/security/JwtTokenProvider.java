package com.genoutbound.gateway.security;

import com.genoutbound.gateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final JwtProperties properties;
    private final UserDetailsService userDetailsService;
    private final Key signingKey;

    public JwtTokenProvider(JwtProperties properties, UserDetailsService userDetailsService) {
        this.properties = properties;
        this.userDetailsService = userDetailsService;
        this.signingKey = createSigningKey(properties.getSecret());
    }

    public String createAccessToken(UserDetails userDetails) {
        return createToken(userDetails, "access", properties.getAccessTokenMinutes() * 60);
    }

    public String createRefreshToken(UserDetails userDetails) {
        return createToken(userDetails, "refresh", properties.getRefreshTokenDays() * 24 * 60 * 60);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String username = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public boolean isAccessToken(String token) {
        return isTokenType(token, "access");
    }

    public boolean isRefreshToken(String token) {
        return isTokenType(token, "refresh");
    }

    public long getAccessTokenExpiresInSeconds() {
        return properties.getAccessTokenMinutes() * 60;
    }

    public long getRefreshTokenExpiresInSeconds() {
        return properties.getRefreshTokenDays() * 24 * 60 * 60;
    }

    private String createToken(UserDetails userDetails, String type, long expiresInSeconds) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expiresInSeconds);
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuer(properties.getIssuer())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .claim("typ", type)
            .claim("roles", roles)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
    }

    private boolean isTokenType(String token, String type) {
        Claims claims = parseClaims(token);
        String tokenType = claims.get("typ", String.class);
        return type.equals(tokenType);
    }

    private Claims parseClaims(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
        if (properties.getIssuer() != null && !properties.getIssuer().isBlank()
            && !properties.getIssuer().equals(claims.getIssuer())) {
            throw new JwtException("Invalid token issuer");
        }
        return claims;
    }

    private Key createSigningKey(String secret) {
        if (secret == null || secret.isBlank()) {
            log.warn("JWT 시크릿이 비어 있어 임시 키를 생성합니다. 운영 환경에서는 JWT_SECRET을 설정하세요.");
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalStateException("JWT 시크릿 길이가 부족합니다. 최소 32바이트 이상이어야 합니다.");
        }
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
