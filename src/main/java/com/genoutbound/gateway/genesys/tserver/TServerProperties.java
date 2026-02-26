package com.genoutbound.gateway.genesys.tserver;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.tserver")
public class TServerProperties {

    private boolean enabled = true;
    private String endpoint;
    private String ip;
    private int port;
    private String backupEndpoint;
    private String backupIp;
    private int backupPort;
    private String clientName = "tserver-client";
    private boolean addpEnabled = true;
    private int addpClientTimeout = 10;
    private int addpServerTimeout = 10;
    private String charset = "UTF-8";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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

    public String getBackupEndpoint() {
        return backupEndpoint;
    }

    public void setBackupEndpoint(String backupEndpoint) {
        this.backupEndpoint = backupEndpoint;
    }

    public String getBackupIp() {
        return backupIp;
    }

    public void setBackupIp(String backupIp) {
        this.backupIp = backupIp;
    }

    public int getBackupPort() {
        return backupPort;
    }

    public void setBackupPort(int backupPort) {
        this.backupPort = backupPort;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean hasBackup() {
        return backupIp != null && !backupIp.isBlank() && backupPort > 0;
    }
}
