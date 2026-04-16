package com.genoutbound.gateway.security.seed;

import com.genoutbound.gateway.config.SecurityProperties;
import com.genoutbound.gateway.security.AppUser;
import com.genoutbound.gateway.security.AppUserRepository;
import com.genoutbound.gateway.security.role.AppRole;
import com.genoutbound.gateway.security.role.AppRoleRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(prefix = "app.security", name = "seed-enabled", havingValue = "true")
public class AdminUserSeedRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminUserSeedRunner.class);

    private final SecurityProperties securityProperties;
    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 의존성 참조를 시드 실행 시 내부 처리 용도로만 사용하며 외부로 노출하지 않습니다.")
    public AdminUserSeedRunner(SecurityProperties securityProperties,
                               AppUserRepository userRepository,
                               AppRoleRepository roleRepository,
                               PasswordEncoder passwordEncoder) {
        this.securityProperties = securityProperties;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        String username = securityProperties.getAdminUsername();
        String password = securityProperties.getAdminPassword();
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            log.info("ADMIN_USERNAME/ADMIN_PASSWORD가 없어 관리자 시드를 건너뜁니다.");
            return;
        }
        Optional<AppUser> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            log.info("관리자 계정이 이미 존재합니다: {}", username);
            return;
        }
        AppRole adminRole = roleRepository.findByName("ADMIN")
            .orElse(null);
        AppUser admin = new AppUser();
        admin.setUsername(username);
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setEnabled(true);
        if (adminRole != null) {
            Set<AppRole> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoleEntities(roles);
            admin.setRoles("ADMIN");
        }
        userRepository.save(admin);
        log.info("관리자 계정을 생성했습니다: {}", username);
    }
}