package com.genoutbound.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genoutbound.gateway.core.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ConfigurationApiBlockFilter extends OncePerRequestFilter {

    private static final List<String> BLOCKED_PREFIXES = List.of(
        "/api/v1/configuration/agent-groups",
        "/api/v1/configuration/agent-logins",
        "/api/v1/configuration/dn-groups",
        "/api/v1/configuration/dns",
        "/api/v1/configuration/persons",
        "/api/v1/configuration/place-groups",
        "/api/v1/configuration/places",
        "/api/v1/configuration/transactions"
    );

    private final ObjectMapper objectMapper;

    public ConfigurationApiBlockFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String path = request.getRequestURI();
        if (isBlockedPath(path)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), ApiResponse.error("해당 API는 비활성화되었습니다."));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isBlockedPath(String path) {
        for (String prefix : BLOCKED_PREFIXES) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
