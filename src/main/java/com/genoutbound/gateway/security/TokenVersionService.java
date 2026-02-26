package com.genoutbound.gateway.security;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenVersionService {

    private final AppUserRepository userRepository;
    private final Map<String, Long> externalUserVersions = new ConcurrentHashMap<>();

    public TokenVersionService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public long bumpVersion(String username) {
        Optional<AppUser> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            long current = Optional.ofNullable(user.getTokenVersion()).orElse(0L);
            long next = current + 1;
            user.setTokenVersion(next);
            userRepository.save(user);
            return next;
        }
    Long next = externalUserVersions.merge(username, 1L, (left, right) -> (left == null ? 0L : left) + right);
    return next == null ? 1L : next;
    }

    @Transactional(readOnly = true)
    public long getCurrentVersion(String username) {
        Optional<AppUser> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return Optional.ofNullable(userOptional.get().getTokenVersion()).orElse(0L);
        }
        return externalUserVersions.getOrDefault(username, 0L);
    }
}