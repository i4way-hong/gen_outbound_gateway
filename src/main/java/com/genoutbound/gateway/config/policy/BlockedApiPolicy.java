package com.genoutbound.gateway.config.policy;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

@Component
public class BlockedApiPolicy {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final String API_STATUS_PATH = "/api/status";

    private static final List<Rule> RULES = List.of(
        //API 상태(API 서버, Genesys Process) 확인
        new Rule(API_STATUS_PATH, MatchType.PATTERN, RuleScope.BLOCKED_API),
        //암복호화 테스트
        //new Rule("/api/v1/crypto/**", MatchType.PATTERN, RuleScope.BLOCKED_API),
        //아웃바운드 캠페인 제어
        new Rule("/api/v1/outbound/**", MatchType.PATTERN, RuleScope.BLOCKED_API),
        //config
        new Rule("/api/v1/configuration/agent-groups", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/agent-logins", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/dn-groups", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/dns", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/persons", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/place-groups", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/places", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/transactions", MatchType.PREFIX, RuleScope.BLOCKED_API),
        //outbound
        new Rule("/api/v1/configuration/batch-create", MatchType.PREFIX, RuleScope.BLOCKED_API),
        //new Rule("/api/v1/configuration/calling-lists", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/campaign-groups", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/campaigns", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/filters", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/formats", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/table-access", MatchType.PREFIX, RuleScope.BLOCKED_API),
        new Rule("/api/v1/configuration/treatment", MatchType.PREFIX, RuleScope.BLOCKED_API)
    );

    public String[] blockedPatternsArray() {
        return RULES.stream()
            .filter(rule -> rule.scope() == RuleScope.BLOCKED_API && rule.matchType() == MatchType.PATTERN)
            .map(Rule::path)
            .toArray(String[]::new);
    }

    public boolean isBlockedApiPath(HttpServletRequest request) {
        return isBlockedApiPath(normalizePath(request));
    }

    public boolean isBlockedApiPath(String path) {
        String normalizedPath = normalizePath(path);

        for (Rule rule : RULES) {
            if (rule.scope() == RuleScope.BLOCKED_API && rule.matches(normalizedPath)) {
                return true;
            }
        }
        return false;
    }

    public boolean isConfigurationApiBlocked(HttpServletRequest request) {
        return isConfigurationApiBlocked(normalizePath(request));
    }

    public boolean isConfigurationApiBlocked(String path) {
        String normalizedPath = normalizePath(path);
        for (Rule rule : RULES) {
            if (rule.scope() == RuleScope.CONFIGURATION_BLOCKED && rule.matches(normalizedPath)) {
                return true;
            }
        }
        return false;
    }

    public String normalizePath(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();

        if (StringUtils.hasText(contextPath) && requestUri.startsWith(contextPath)) {
            requestUri = requestUri.substring(contextPath.length());
        }

        return normalizePath(requestUri);
    }

    public String normalizePath(String path) {
        if (!StringUtils.hasText(path)) {
            return "/";
        }

        return path.startsWith("/") ? path : "/" + path;
    }

    private enum MatchType {
        PREFIX,
        PATTERN
    }

    private enum RuleScope {
        BLOCKED_API,
        CONFIGURATION_BLOCKED
    }

    private record Rule(String path, MatchType matchType, RuleScope scope) {
        private boolean matches(String normalizedPath) {
            if (matchType == MatchType.PREFIX) {
                return normalizedPath.startsWith(path);
            }
            return PATH_MATCHER.match(path, normalizedPath);
        }
    }
}
