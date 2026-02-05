package com.genoutbound.gateway.genesys.cfg.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.genesys")
public class GenesysProperties {

    private boolean enabled;
    private String clientName = "default";
    private String username = "default";
    private String password = "password";
    private int tenantDbid;
    private int switchDbidPrimary;
    private int switchDbidSecondary;
    private String charset = "UTF-8";
    private boolean addpEnabled = true;
    private int addpClientTimeout = 5;
    private int addpServerTimeout = 5;
    private int healthCheckIntervalMs = 30000;
    private ConfigServer primary = new ConfigServer();
    private ConfigServer backup = new ConfigServer();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTenantDbid() {
        return tenantDbid;
    }

    public void setTenantDbid(int tenantDbid) {
        this.tenantDbid = tenantDbid;
    }

    public int getSwitchDbidPrimary() {
        return switchDbidPrimary;
    }

    public void setSwitchDbidPrimary(int switchDbidPrimary) {
        this.switchDbidPrimary = switchDbidPrimary;
    }

    public int getSwitchDbidSecondary() {
        return switchDbidSecondary;
    }

    public void setSwitchDbidSecondary(int switchDbidSecondary) {
        this.switchDbidSecondary = switchDbidSecondary;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isAddpEnabled() {
        return addpEnabled;
    }

    public void setAddpEnabled(boolean addpEnabled) {
        this.addpEnabled = addpEnabled;
    }

    public int getAddpClientTimeout() {
        return addpClientTimeout;
    }

    public void setAddpClientTimeout(int addpClientTimeout) {
        this.addpClientTimeout = addpClientTimeout;
    }

    public int getAddpServerTimeout() {
        return addpServerTimeout;
    }

    public void setAddpServerTimeout(int addpServerTimeout) {
        this.addpServerTimeout = addpServerTimeout;
    }

    public int getHealthCheckIntervalMs() {
        return healthCheckIntervalMs;
    }

    public void setHealthCheckIntervalMs(int healthCheckIntervalMs) {
        this.healthCheckIntervalMs = healthCheckIntervalMs;
    }

    public ConfigServer getPrimary() {
        return primary;
    }

    public void setPrimary(ConfigServer primary) {
        this.primary = primary;
    }

    public ConfigServer getBackup() {
        return backup;
    }

    public void setBackup(ConfigServer backup) {
        this.backup = backup;
    }

    public static class ConfigServer {
        private String endpoint;
        private String ip;
        private int port;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
