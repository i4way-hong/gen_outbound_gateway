package com.genoutbound.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genoutbound.gateway.config.policy.BlockedApiPolicy;
import com.genoutbound.gateway.core.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Predicate;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ConfigurationApiBlockFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final Predicate<HttpServletRequest> blockedRequestPredicate;

    public ConfigurationApiBlockFilter(ObjectMapper objectMapper,
                                       BlockedApiPolicy blockedApiPolicy) {
        this.objectMapper = objectMapper.copy();
        this.blockedRequestPredicate = blockedApiPolicy::isConfigurationApiBlocked;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if (blockedRequestPredicate.test(request)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), ApiResponse.error("해당 API는 비활성화되었습니다."));
            return;
        }
        filterChain.doFilter(request, response);
    }
}
