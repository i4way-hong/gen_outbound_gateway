package com.genoutbound.gateway.security.role;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPermissionRepository extends JpaRepository<AppPermission, Long> {

    Optional<AppPermission> findByCode(String code);
}
