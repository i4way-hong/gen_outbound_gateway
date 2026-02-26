package com.genoutbound.gateway.security;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Sort;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    @EntityGraph(attributePaths = "roleEntities")
    List<AppUser> findAll(Sort sort);

    @EntityGraph(attributePaths = "roleEntities")
    Optional<AppUser> findWithRolesById(Long id);
}
