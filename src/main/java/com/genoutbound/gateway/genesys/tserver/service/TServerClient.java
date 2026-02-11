package com.genoutbound.gateway.genesys.tserver.service;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.AddressType;
import com.genesyslab.platform.voice.protocol.tserver.AgentWorkMode;
import com.genesyslab.platform.voice.protocol.tserver.ControlMode;
import com.genesyslab.platform.voice.protocol.tserver.RegisterMode;
import com.genesyslab.platform.voice.protocol.tserver.events.EventRegistered;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentLogin;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentLogout;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentNotReady;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentReady;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestRegisterAddress;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestUnregisterAddress;
import com.genoutbound.gateway.genesys.common.GenesysUnavailableException;
import com.genoutbound.gateway.genesys.tserver.TServerProperties;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TServerClient {

    private static final Logger log = LoggerFactory.getLogger(TServerClient.class);
    private static final AgentWorkMode DEFAULT_WORK_MODE = AgentWorkMode.ManualIn;

    private final TServerProperties properties;

    public TServerClient(TServerProperties properties) {
        this.properties = properties;
    }

    public String register(String dn) {
        return executeWithProtocol(protocol -> register(protocol, dn));
    }

    public String register(TServerProtocol protocol, String dn) {
        RequestRegisterAddress request = RequestRegisterAddress.create(
            dn, RegisterMode.ModeShare, ControlMode.RegisterDefault, AddressType.DN);
        try {
            Message response = protocol.request(request);
            if (response instanceof EventRegistered registered) {
                log.info("T-Server 레지스터 성공: dn={}, agentId={}", dn, registered.getAgentID());
                return registered.getAgentID();
            }
            return "";
        } catch (ProtocolException | IllegalStateException ex) {
            throw new GenesysUnavailableException("T-Server 레지스터 실패", ex);
        }
    }

    public void unregister(String dn) {
        executeWithProtocol(protocol -> {
            unregister(protocol, dn);
            return null;
        });
    }

    public void unregister(TServerProtocol protocol, String dn) {
        RequestUnregisterAddress request = RequestUnregisterAddress.create(dn, ControlMode.RegisterDefault);
        try {
            protocol.request(request);
            log.info("T-Server 언레지스터 성공: dn={}", dn);
        } catch (ProtocolException | IllegalStateException ex) {
            throw new GenesysUnavailableException("T-Server 언레지스터 실패", ex);
        }
    }

    public void login(String dn, String agentId) {
        executeWithProtocol(protocol -> {
            login(protocol, dn, agentId);
            return null;
        });
    }

    public void login(TServerProtocol protocol, String dn, String agentId) {
        RequestAgentLogin request = RequestAgentLogin.create(dn, AgentWorkMode.Unknown, "",
            agentId, "", null, null);
        try {
            protocol.request(request);
            log.info("T-Server 로그인 성공: dn={}, agentId={}", dn, agentId);
        } catch (ProtocolException | IllegalStateException ex) {
            throw new GenesysUnavailableException("T-Server 로그인 실패", ex);
        }
    }

    public void logout(String dn) {
        executeWithProtocol(protocol -> {
            logout(protocol, dn);
            return null;
        });
    }

    public void logout(TServerProtocol protocol, String dn) {
        RequestAgentLogout request = RequestAgentLogout.create(dn, "", null, null);
        try {
            protocol.request(request);
            log.info("T-Server 로그아웃 성공: dn={}", dn);
        } catch (ProtocolException | IllegalStateException ex) {
            throw new GenesysUnavailableException("T-Server 로그아웃 실패", ex);
        }
    }

    public void ready(String dn) {
        executeWithProtocol(protocol -> {
            ready(protocol, dn);
            return null;
        });
    }

    public void ready(TServerProtocol protocol, String dn) {
        RequestAgentReady request = RequestAgentReady.create(dn, DEFAULT_WORK_MODE);
        try {
            protocol.request(request);
            log.info("T-Server 대기 성공: dn={}", dn);
        } catch (ProtocolException | IllegalStateException ex) {
            throw new GenesysUnavailableException("T-Server 대기 요청 실패", ex);
        }
    }

    public void notReady(String dn, String reasonCode) {
        executeWithProtocol(protocol -> {
            notReady(protocol, dn, reasonCode);
            return null;
        });
    }

    public void notReady(TServerProtocol protocol, String dn, String reasonCode) {
        KeyValueCollection reason = new KeyValueCollection();
        reason.addString("ReasonCode", reasonCode == null ? "" : reasonCode);
        RequestAgentNotReady request = RequestAgentNotReady.create(dn, DEFAULT_WORK_MODE, "", reason, reason);
        try {
            protocol.request(request);
            log.info("T-Server 이석 성공: dn={}", dn);
        } catch (ProtocolException | IllegalStateException ex) {
            throw new GenesysUnavailableException("T-Server 이석 요청 실패", ex);
        }
    }

    public Map<String, Object> getConnectionStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("enabled", properties.isEnabled());
        //status.put("endpoints", List.of(buildEndpoint("primary")));
        status.put("connectionPool", buildPoolStatus(false, "per-request"));
        //status.put("info", buildInfo());

        if (!properties.isEnabled()) {
            status.put("connected", false);
            status.put("state", "disabled");
            return status;
        }

        boolean connected = false;
        String state = "UNKNOWN";
        String error = null;
        TServerProtocol protocol = null;
        try {
            protocol = openProtocol();
            connected = protocol.getState() == ChannelState.Opened;
            state = protocol.getState().name();
        } catch (GenesysUnavailableException ex) {
            connected = false;
            state = "ERROR";
            error = ex.getMessage();
        } finally {
            closeProtocol(protocol);
        }

        status.put("connected", connected);
        status.put("state", state);
        status.put("connectionPool", buildPoolStatus(connected, "per-request"));
        if (error != null) {
            status.put("error", error);
        }
        return status;
    }

    public <T> T executeWithProtocol(Function<TServerProtocol, T> action) {
        if (!properties.isEnabled()) {
            throw new GenesysUnavailableException("T-Server 연동이 비활성화되어 있습니다.");
        }
        TServerProtocol protocol = openProtocol();
        try {
            return action.apply(protocol);
        } finally {
            closeProtocol(protocol);
        }
    }

    private TServerProtocol openProtocol() {
        PropertyConfiguration config = new PropertyConfiguration();
        config.setUseAddp(properties.isAddpEnabled());
        config.setAddpClientTimeout(properties.getAddpClientTimeout());
        config.setAddpServerTimeout(properties.getAddpServerTimeout());
        config.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, properties.getCharset());

        Endpoint endpoint = new Endpoint(properties.getEndpoint(), properties.getIp(), properties.getPort(), config);
        TServerProtocol protocol = new TServerProtocol(endpoint);
        protocol.setClientName(properties.getClientName());
        try {
            protocol.open();
            log.info("T-Server 연결 성공: {}:{}", properties.getIp(), properties.getPort());
            return protocol;
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            throw new GenesysUnavailableException("T-Server 연결 실패", ex);
        }
    }

    private Map<String, Object> buildEndpoint(String role) {
        Map<String, Object> endpoint = new LinkedHashMap<>();
        endpoint.put("role", role);
        endpoint.put("endpoint", properties.getEndpoint());
        endpoint.put("ip", properties.getIp());
        endpoint.put("port", properties.getPort());
        return endpoint;
    }

    private Map<String, Object> buildInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("clientName", properties.getClientName());
        info.put("charset", properties.getCharset());
        info.put("addpEnabled", properties.isAddpEnabled());
        return info;
    }

    private Map<String, Object> buildPoolStatus(boolean connected, String mode) {
        Map<String, Object> pool = new LinkedHashMap<>();
        pool.put("mode", mode);
        pool.put("max", 1);
        pool.put("active", connected ? 1 : 0);
        pool.put("idle", 0);
        return pool;
    }

    private void closeProtocol(TServerProtocol protocol) {
        if (protocol == null) {
            return;
        }
        try {
            if (protocol.getState() != ChannelState.Closed) {
                protocol.close();
            }
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            log.warn("T-Server 종료 실패", ex);
        }
    }
}
