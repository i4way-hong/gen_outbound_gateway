package com.genoutbound.gateway.genesys.stat;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.genesys.stat")
public class StatServerProperties {

    private boolean enabled = true;
    private String clientName = "stat-client";
    private String charset = "UTF-8";
    private String tenantName = "Environment";
    private String defaultStatistic = "CurrentAllAgentState";
    private int timeoutMs = 5000;
    private int delayMs = 200;
    private boolean addpEnabled = true;
    private int addpClientTimeout = 5;
    private int addpServerTimeout = 5;
    private Server primary = new Server();
    private Server backup = new Server();

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

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getDefaultStatistic() {
        return defaultStatistic;
    }

    public void setDefaultStatistic(String defaultStatistic) {
        this.defaultStatistic = defaultStatistic;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public int getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(int delayMs) {
        this.delayMs = delayMs;
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

    public Server getPrimary() {
        return primary;
    }

    public void setPrimary(Server primary) {
        this.primary = primary;
    }

    public Server getBackup() {
        return backup;
    }

    public void setBackup(Server backup) {
        this.backup = backup;
    }

    public static class Server {
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
