package com.genoutbound.gateway.genesys.outbound.service;

import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.outbound.protocol.OutboundServerProtocol;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.genesys.outbound.OutboundProperties;
import com.genoutbound.gateway.genesys.common.GenesysUnavailableException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Genesys Outbound Client 호출을 담당합니다.
 */
@Component
public class OutboundClient {

    private static final Logger log = LoggerFactory.getLogger(OutboundClient.class);
    private final OutboundProperties properties;

    /**
     * OutboundClient 생성자.
     *
     * @param properties Outbound 설정
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI로 주입된 설정 빈 참조를 연결 구성 조회 용도로만 사용하며 외부로 노출하지 않습니다.")
    public OutboundClient(OutboundProperties properties) {
        this.properties = properties;
    }

    /**
     * Outbound 프로토콜을 열고 작업을 실행합니다.
     *
     * @param action 실행할 작업
     * @param <T> 반환 타입
     * @return 작업 결과
     */
    public <T> T withProtocol(Function<OutboundServerProtocol, T> action) {
        if (!properties.isEnabled()) {
            throw new GenesysUnavailableException("Outbound 서버 연동이 비활성화되어 있습니다.");
        }
        log.info("Outbound 작업 실행: clientName={}, uri={}", properties.getClientName(), properties.getUri());
        OutboundServerProtocol protocol = openProtocol();
        try {
            return action.apply(protocol);
        } catch (ApiException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new GenesysUnavailableException("Outbound 요청 처리 실패", ex);
        } finally {
            closeProtocol(protocol);
        }
    }

    /**
     * Outbound 서버 연결 상태를 반환합니다.
     */
    public Map<String, Object> getConnectionStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("enabled", properties.isEnabled());
        status.put("endpoints", buildEndpoints());
        status.put("connectionPool", buildPoolStatus(false, "per-request"));
    status.put("info", buildInfo());

        if (!properties.isEnabled()) {
            status.put("connected", false);
            status.put("state", "disabled");
            return status;
        }

        boolean connected = false;
        String state = "UNKNOWN";
        String error = null;
        OutboundServerProtocol protocol = null;
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

    private List<Map<String, Object>> buildEndpoints() {
        Map<String, Object> primary = new LinkedHashMap<>();
        primary.put("role", "primary");
        primary.put("uri", properties.getUri());
        if (!properties.hasBackup()) {
            return List.of(primary);
        }
        Map<String, Object> backup = new LinkedHashMap<>();
        backup.put("role", "backup");
        backup.put("uri", properties.getBackupUri());
        return List.of(primary, backup);
    }

    private Map<String, Object> buildInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("clientName", properties.getClientName());
        info.put("appName", properties.getAppName());
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

    /**
     * Outbound 서버 프로토콜을 연결합니다.
     */
    private OutboundServerProtocol openProtocol() {
        try {
            return openProtocol(properties.getUri(), "primary");
        } catch (GenesysUnavailableException primaryError) {
            if (!properties.hasBackup()) {
                throw primaryError;
            }
            log.warn("Primary Outbound 연결 실패. Backup으로 재시도합니다.", primaryError);
            return openProtocol(properties.getBackupUri(), "backup");
        }
    }

    private OutboundServerProtocol openProtocol(String uriValue, String role) {
        try {
            URI uri = new URI(uriValue);
            OutboundServerProtocol protocol = new OutboundServerProtocol(new Endpoint(uri));
            protocol.setClientName(properties.getClientName());
            protocol.setClientPassword(properties.getClientPassword());
            protocol.setUserName(properties.getAppName());
            protocol.setUserPassword(properties.getAppPassword());
            protocol.open();
            log.info("Outbound 서버 연결 성공({}): {}", role, uriValue);
            return protocol;
        } catch (URISyntaxException ex) {
            throw new GenesysUnavailableException("Outbound 서버 URI가 올바르지 않습니다. (" + role + ")", ex);
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            throw new GenesysUnavailableException("Outbound 서버 연결 실패(" + role + ")", ex);
        }
    }

    /**
     * Outbound 프로토콜을 종료합니다.
     */
    private void closeProtocol(OutboundServerProtocol protocol) {
        if (protocol == null) {
            return;
        }
        if (protocol.getState() != ChannelState.Opened) {
            return;
        }
        try {
            protocol.close();
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            log.warn("Outbound 프로토콜 종료 실패", ex);
        }
    }
}
