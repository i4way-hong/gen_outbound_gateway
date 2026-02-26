package com.genoutbound.gateway.security.role;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {

    Optional<AppRole> findByName(String name);

    @EntityGraph(attributePaths = "permissions")
    List<AppRole> findAllBy(Sort sort);

    @EntityGraph(attributePaths = "permissions")
    Optional<AppRole> findWithPermissionsById(Long id);
}
