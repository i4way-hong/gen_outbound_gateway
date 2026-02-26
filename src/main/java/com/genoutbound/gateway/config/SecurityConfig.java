package com.genoutbound.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.genoutbound.gateway.security.JwtAuthenticationFilter;
import com.genoutbound.gateway.security.admin.AdminSessionAuthenticationFilter;
import com.genoutbound.gateway.security.permission.PermissionCodes;
import jakarta.annotation.PostConstruct;

@Configuration
@EnableMethodSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final SecurityProperties securityProperties;

    public SecurityConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @PostConstruct
    public void logSecurityProperties() {
        log.info("Security 설정: authEnabled={}, jwtEnabled={}, allowInsecure={}, allowSwagger={}, allowCryptoTest={}, allowAdminUi={}",
            securityProperties.isAuthEnabled(),
            securityProperties.isJwtEnabled(),
            securityProperties.isAllowInsecure(),
            securityProperties.isAllowSwagger(),
            securityProperties.isAllowCryptoTest(),
            securityProperties.isAllowAdminUi());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        if (!securityProperties.isAuthEnabled() || !securityProperties.isJwtEnabled()) {
            if (!securityProperties.isAuthEnabled()) {
                log.warn("인증이 비활성화되어 모든 요청을 허용합니다. app.security.auth-enabled=true 설정 필요.");
            }
            if (!securityProperties.isJwtEnabled()) {
                log.warn("JWT 인증이 비활성화되어 모든 요청을 허용합니다. app.security.jwt-enabled=true 설정 필요.");
            }
            if (!securityProperties.isAllowInsecure()) {
                throw new IllegalStateException("보안 설정이 비활성화되었습니다. app.security.allow-insecure=true 설정 시에만 허용됩니다.");
            }
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }

        http.authorizeHttpRequests(auth -> {
        auth.requestMatchers("/", "/favicon.ico", "/css/**", "/js/**", "/images/**", "/actuator/health",
            "/auth/login", "/auth/refresh", "/admin/login", "/admin/logout")
                .permitAll();

            auth.requestMatchers("/api/status")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.STATUS_READ);

            if (securityProperties.isAllowSwagger()) {
                auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
            } else {
                auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN");
            }

            if (securityProperties.isAllowCryptoTest()) {
                auth.requestMatchers("/api/v1/crypto/**").permitAll();
            } else {
                auth.requestMatchers("/api/v1/crypto/**").hasRole("ADMIN");
            }

            if (securityProperties.isAllowAdminUi()) {
                auth.requestMatchers("/admin/**").permitAll();
            } else {
                auth.requestMatchers("/admin/**")
                    .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.ADMIN_UI);
            }

            auth.requestMatchers(
                    "/admin/permissions/new",
                    "/admin/permissions/*")
                .denyAll();

            auth.requestMatchers(HttpMethod.GET, "/api/v1/configuration/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.CONFIG_READ, PermissionCodes.CONFIG_WRITE);
            auth.requestMatchers(HttpMethod.POST, "/api/v1/configuration/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.CONFIG_WRITE);
            auth.requestMatchers(HttpMethod.PUT, "/api/v1/configuration/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.CONFIG_WRITE);
            auth.requestMatchers(HttpMethod.DELETE, "/api/v1/configuration/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.CONFIG_WRITE);

            auth.requestMatchers(HttpMethod.GET, "/api/v1/outbound/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.OUTBOUND_READ, PermissionCodes.OUTBOUND_WRITE);
            auth.requestMatchers(HttpMethod.POST, "/api/v1/outbound/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.OUTBOUND_WRITE);
            auth.requestMatchers(HttpMethod.PUT, "/api/v1/outbound/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.OUTBOUND_WRITE);
            auth.requestMatchers(HttpMethod.DELETE, "/api/v1/outbound/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.OUTBOUND_WRITE);

            auth.requestMatchers("/api/v1/stat/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.STAT_READ);

            auth.requestMatchers("/api/v1/voice/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.TSERVER_WRITE);

            auth.requestMatchers("/api/v1/scs/**")
                .hasAnyAuthority("ROLE_ADMIN", PermissionCodes.SCS_READ);

            auth.anyRequest()
                .authenticated();
        }).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(adminSessionAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AdminSessionAuthenticationFilter adminSessionAuthenticationFilter() {
        return new AdminSessionAuthenticationFilter();
    }
}
