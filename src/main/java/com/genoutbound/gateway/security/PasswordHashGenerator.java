package com.genoutbound.gateway.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordHashGenerator {

    private PasswordHashGenerator() {
    }

    public static void main(String[] args) {
        String password = args.length > 0 && args[0] != null && !args[0].isBlank() ? args[0] : "secret";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        System.out.println("{bcrypt}" + hash);
    }
}
