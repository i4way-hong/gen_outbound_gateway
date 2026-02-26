package com.genoutbound.gateway.security.seed;

import com.genoutbound.gateway.security.permission.PermissionCodes;
import com.genoutbound.gateway.security.role.AppPermission;
import com.genoutbound.gateway.security.role.AppPermissionRepository;
import com.genoutbound.gateway.security.role.AppRole;
import com.genoutbound.gateway.security.role.AppRoleRepository;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(prefix = "app.security", name = "seed-enabled", havingValue = "true")
public class SecuritySeedRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SecuritySeedRunner.class);

    private final AppPermissionRepository permissionRepository;
    private final AppRoleRepository roleRepository;

    public SecuritySeedRunner(AppPermissionRepository permissionRepository,
                              AppRoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Map<String, String> permissionMap = buildPermissionMap();
        Map<String, AppPermission> permissions = ensurePermissions(permissionMap);
        ensureRoles(permissions);
    }

    private Map<String, String> buildPermissionMap() {
        Map<String, String> permissionMap = new LinkedHashMap<>();
        permissionMap.put(PermissionCodes.ADMIN_UI, "관리자 UI 접근");
        permissionMap.put(PermissionCodes.STATUS_READ, "상태 API 조회");
        permissionMap.put(PermissionCodes.CONFIG_READ, "Config 조회");
        permissionMap.put(PermissionCodes.CONFIG_WRITE, "Config 변경");
        permissionMap.put(PermissionCodes.OUTBOUND_READ, "Outbound 조회");
        permissionMap.put(PermissionCodes.OUTBOUND_WRITE, "Outbound 제어");
        permissionMap.put(PermissionCodes.STAT_READ, "Stat 조회");
        permissionMap.put(PermissionCodes.TSERVER_WRITE, "T-Server 제어");
        return permissionMap;
    }

    private Map<String, AppPermission> ensurePermissions(Map<String, String> permissionMap) {
        Map<String, AppPermission> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : permissionMap.entrySet()) {
            AppPermission permission = permissionRepository.findByCode(entry.getKey())
                .orElseGet(() -> {
                    AppPermission created = new AppPermission();
                    created.setCode(entry.getKey());
                    created.setDescription(entry.getValue());
                    return permissionRepository.save(created);
                });
            if (permission.getDescription() == null || permission.getDescription().isBlank()) {
                permission.setDescription(entry.getValue());
                permissionRepository.save(permission);
            }
            result.put(entry.getKey(), permission);
        }
        log.info("권한 시드 완료: {}개", result.size());
        return result;
    }

    private void ensureRoles(Map<String, AppPermission> permissions) {
        AppRole adminRole = roleRepository.findByName("ADMIN")
            .orElseGet(() -> {
                AppRole role = new AppRole();
                role.setName("ADMIN");
                role.setDescription("관리자");
                role.setEnabled(true);
                return role;
            });
        Set<AppPermission> adminPerms = new LinkedHashSet<>(permissions.values());
        adminRole.getPermissions().addAll(adminPerms);
        roleRepository.save(adminRole);

        AppRole operatorRole = roleRepository.findByName("OPERATOR")
            .orElseGet(() -> {
                AppRole role = new AppRole();
                role.setName("OPERATOR");
                role.setDescription("운영자");
                role.setEnabled(true);
                return role;
            });
        operatorRole.getPermissions().addAll(resolveOperatorPermissions(permissions));
        roleRepository.save(operatorRole);
    }

    private Set<AppPermission> resolveOperatorPermissions(Map<String, AppPermission> permissions) {
        Set<AppPermission> result = new LinkedHashSet<>();
        addIfPresent(result, permissions, PermissionCodes.STATUS_READ);
        addIfPresent(result, permissions, PermissionCodes.CONFIG_READ);
        addIfPresent(result, permissions, PermissionCodes.OUTBOUND_READ);
        addIfPresent(result, permissions, PermissionCodes.STAT_READ);
        return result;
    }

    private void addIfPresent(Set<AppPermission> target, Map<String, AppPermission> permissions, String code) {
        AppPermission permission = permissions.get(code);
        if (permission != null) {
            target.add(permission);
        }
    }
}
