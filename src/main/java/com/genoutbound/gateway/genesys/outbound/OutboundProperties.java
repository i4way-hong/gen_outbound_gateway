package com.genoutbound.gateway.genesys.outbound;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Outbound 연동 설정값을 바인딩합니다.
 */
@ConfigurationProperties(prefix = "app.outbound")
public class OutboundProperties {

    private boolean enabled;
    private String uri;
    private String clientName = "default";
    private String clientPassword = "";
    private String appName = "ocserver";
    private String appPassword;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }
}
