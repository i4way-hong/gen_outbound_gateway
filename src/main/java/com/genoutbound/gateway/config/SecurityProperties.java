package com.genoutbound.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private String adminUsername;
    private String adminPassword;
    private boolean authEnabled = true;
    private boolean jwtEnabled = true;
    private boolean allowInsecure = false;
    private boolean allowSwagger = false;
    private boolean allowCryptoTest = false;
    private boolean seedEnabled = false;
    private boolean allowAdminUi = false;

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public void setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
    }

    public boolean isJwtEnabled() {
        return jwtEnabled;
    }

    public void setJwtEnabled(boolean jwtEnabled) {
        this.jwtEnabled = jwtEnabled;
    }

    public boolean isAllowInsecure() {
        return allowInsecure;
    }

    public void setAllowInsecure(boolean allowInsecure) {
        this.allowInsecure = allowInsecure;
    }

    public boolean isAllowSwagger() {
        return allowSwagger;
    }

    public void setAllowSwagger(boolean allowSwagger) {
        this.allowSwagger = allowSwagger;
    }

    public boolean isAllowCryptoTest() {
        return allowCryptoTest;
    }

    public void setAllowCryptoTest(boolean allowCryptoTest) {
        this.allowCryptoTest = allowCryptoTest;
    }

    public boolean isSeedEnabled() {
        return seedEnabled;
    }

    public void setSeedEnabled(boolean seedEnabled) {
        this.seedEnabled = seedEnabled;
    }

    public boolean isAllowAdminUi() {
        return allowAdminUi;
    }

    public void setAllowAdminUi(boolean allowAdminUi) {
        this.allowAdminUi = allowAdminUi;
    }
}
