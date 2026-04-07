package com.genoutbound.gateway.security;

import com.genoutbound.gateway.core.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider tokenProvider;
    private final TokenRevocationService tokenRevocationService;
    private final TokenVersionService tokenVersionService;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider,
                                   TokenRevocationService tokenRevocationService,
                                   TokenVersionService tokenVersionService,
                                   ObjectMapper objectMapper) {
        this.tokenProvider = tokenProvider;
        this.tokenRevocationService = tokenRevocationService;
        this.tokenVersionService = tokenVersionService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            log.trace("JWT Authorization 헤더가 없어 인증을 건너뜁니다. path={}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            if (!tokenProvider.isAccessToken(token)) {
                sendUnauthorized(response, "유효하지 않은 토큰입니다.");
                return;
            }
            if (tokenRevocationService.isRevoked(token)) {
                sendUnauthorized(response, "폐기된 토큰입니다.");
                return;
            }
            Authentication authentication = tokenProvider.getAuthentication(token);
            long tokenVersion = tokenProvider.getTokenVersion(token);
            long currentVersion = tokenVersionService.getCurrentVersion(authentication.getName());
            if (tokenVersion != currentVersion) {
                sendUnauthorized(response, "폐기된 토큰입니다.");
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("JWT 인증 성공: username={}, authorities={}, path={}",
                authentication.getName(), authentication.getAuthorities(), request.getRequestURI());
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            log.warn("JWT 인증 실패(만료): path={}", request.getRequestURI());
            sendUnauthorized(response, "토큰이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("JWT 인증 실패(유효하지 않음): path={}, reason={}", request.getRequestURI(), ex.getMessage());
            sendUnauthorized(response, "유효하지 않은 토큰입니다.");
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(message)));
    }
}
