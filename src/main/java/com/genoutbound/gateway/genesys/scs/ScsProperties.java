package com.genoutbound.gateway.genesys.scs;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.scs")
public class ScsProperties {

    private boolean enabled;
    private String clientName = "scs-client";
    private Integer clientId = 0;
    private String username;
    private String charset = "UTF-8";
    private int timeoutMs = 5000;
    private boolean addpEnabled = true;
    private int addpClientTimeout = 10;
    private int addpServerTimeout = 10;
    private int warmStandbyAttempts = 5;
    private int warmStandbyTimeoutMs = 2000;
    private Server primary = new Server();
    private Server backup = new Server();
    private List<Integer> applications = new ArrayList<>();
    private Sse sse = new Sse();

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

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
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

    public int getWarmStandbyAttempts() {
        return warmStandbyAttempts;
    }

    public void setWarmStandbyAttempts(int warmStandbyAttempts) {
        this.warmStandbyAttempts = warmStandbyAttempts;
    }

    public int getWarmStandbyTimeoutMs() {
        return warmStandbyTimeoutMs;
    }

    public void setWarmStandbyTimeoutMs(int warmStandbyTimeoutMs) {
        this.warmStandbyTimeoutMs = warmStandbyTimeoutMs;
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

    public List<Integer> getApplications() {
        return applications;
    }

    public void setApplications(List<Integer> applications) {
        this.applications = applications;
    }

    public Sse getSse() {
        return sse;
    }

    public void setSse(Sse sse) {
        this.sse = sse;
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

    public static class Sse {
        private long emitterTimeoutMs = 0;
        private long heartbeatMs = 25000;

        public long getEmitterTimeoutMs() {
            return emitterTimeoutMs;
        }

        public void setEmitterTimeoutMs(long emitterTimeoutMs) {
            this.emitterTimeoutMs = emitterTimeoutMs;
        }

        public long getHeartbeatMs() {
            return heartbeatMs;
        }

        public void setHeartbeatMs(long heartbeatMs) {
            this.heartbeatMs = heartbeatMs;
        }
    }
}
