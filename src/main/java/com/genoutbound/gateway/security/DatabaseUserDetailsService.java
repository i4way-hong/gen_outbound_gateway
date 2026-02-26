package com.genoutbound.gateway.security;

import com.genoutbound.gateway.security.permission.PermissionCodes;
import com.genoutbound.gateway.security.role.AppPermission;
import com.genoutbound.gateway.security.role.AppPermissionRepository;
import com.genoutbound.gateway.security.role.AppRole;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.genoutbound.gateway.config.SecurityProperties;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;
    private final AppPermissionRepository permissionRepository;

    public DatabaseUserDetailsService(AppUserRepository userRepository,
                                      PasswordEncoder passwordEncoder,
                                      SecurityProperties securityProperties,
                                      AppPermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityProperties = securityProperties;
        this.permissionRepository = permissionRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String adminUsername = securityProperties.getAdminUsername();
        if (adminUsername != null && !adminUsername.isBlank() && adminUsername.equals(username)) {
            String adminPassword = securityProperties.getAdminPassword();
            if (adminPassword == null || adminPassword.isBlank()) {
                throw new IllegalStateException("ADMIN_PASSWORD가 설정되지 않았습니다.");
            }
            List<GrantedAuthority> authorities = buildAdminAuthorities();
            return User.withUsername(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .authorities(authorities)
                .build();
        }

        AppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<GrantedAuthority> authorities = buildUserAuthorities(user);
        return User.withUsername(user.getUsername())
            .password(user.getPasswordHash())
            .authorities(authorities)
            .disabled(!user.isEnabled())
            .build();
    }

    private List<GrantedAuthority> buildAdminAuthorities() {
        Set<String> authorities = new LinkedHashSet<>();
        authorities.add("ROLE_ADMIN");
        authorities.add(PermissionCodes.ADMIN_UI);
        List<AppPermission> permissions = permissionRepository.findAll();
        for (AppPermission permission : permissions) {
            if (permission.getCode() != null && !permission.getCode().isBlank()) {
                authorities.add(permission.getCode());
            }
        }
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    private List<GrantedAuthority> buildUserAuthorities(AppUser user) {
        Set<String> authorities = new LinkedHashSet<>();
        for (AppRole role : user.getRoleEntities()) {
            if (role.getName() != null && !role.getName().isBlank()) {
                authorities.add(role.getName().startsWith("ROLE_") ? role.getName() : "ROLE_" + role.getName());
            }
            if (role.getPermissions() != null) {
                for (AppPermission permission : role.getPermissions()) {
                    if (permission.getCode() != null && !permission.getCode().isBlank()) {
                        authorities.add(permission.getCode());
                    }
                }
            }
        }
        if (authorities.isEmpty() && user.getRoles() != null && !user.getRoles().isBlank()) {
            authorities.addAll(parseLegacyRoles(user.getRoles()));
        }
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    private Set<String> parseLegacyRoles(String roles) {
        if (roles == null || roles.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(roles.split(","))
            .map(String::trim)
            .filter(role -> !role.isBlank())
            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
