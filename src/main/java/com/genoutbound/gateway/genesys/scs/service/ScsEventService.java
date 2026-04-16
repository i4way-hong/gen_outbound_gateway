package com.genoutbound.gateway.genesys.scs.service;

import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyListener;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyStateChangedEvent;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.management.protocol.ApplicationExecutionMode;
import com.genesyslab.platform.management.protocol.ApplicationStatus;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.ControlObjectType;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.events.EventInfo;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.requests.applications.RequestGetApplicationInfo;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.requests.RequestSubscribe;
import com.genesyslab.platform.management.protocol.SolutionControlServerProtocol;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.genoutbound.gateway.genesys.cfg.service.GenesysConfigClient;
import com.genoutbound.gateway.genesys.scs.ScsProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScsEventService {

    private static final Logger log = LoggerFactory.getLogger(ScsEventService.class);
    private static final String APP_NAME_UNKNOWN = "__UNKNOWN__";

    private final ScsProperties properties;
    private final GenesysConfigClient configClient;
    private final Map<Integer, String> appNameMap = new ConcurrentHashMap<>();
    private final Map<Integer, EventSignature> lastEventMap = new ConcurrentHashMap<>();

    private SolutionControlServerProtocol protocol;
    private WarmStandbyService warmStandbyService;
    private final AtomicReference<String> lastError = new AtomicReference<>();

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 설정/서비스 참조를 내부 이벤트 처리 및 설정 조회에만 사용하며 외부로 노출하지 않습니다.")
    public ScsEventService(ScsProperties properties, GenesysConfigClient configClient) {
        this.properties = properties;
        this.configClient = configClient;
    }

    @PostConstruct
    public void start() {
        if (!properties.isEnabled()) {
            log.info("SCS listener 비활성화");
            return;
        }
        if (!isValidServer(properties.getPrimary())) {
            log.warn("SCS primary 접속 설정이 필요합니다.");
            return;
        }
        properties.getApplications().forEach(dbid -> {
            if (dbid != null) {
                appNameMap.put(dbid, "");
            }
        });
        loadAppNames();
        protocol = buildProtocol();
        configureWarmStandby();
        registerListeners();
        openProtocol();
    }

    public Map<String, Object> getConnectionStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("enabled", properties.isEnabled());
        status.put("endpoints", List.of(
            buildEndpoint("primary", properties.getPrimary()),
            buildEndpoint("backup", properties.getBackup())));
        status.put("connectionPool", buildPoolStatus(isConnected(), "singleton"));
        status.put("info", buildInfo());

        if (!properties.isEnabled()) {
            status.put("connected", false);
            status.put("state", "disabled");
            return status;
        }

        if (!isValidServer(properties.getPrimary()) && !isValidServer(properties.getBackup())) {
            status.put("connected", false);
            status.put("state", "unconfigured");
            status.put("error", "SCS 접속 설정이 필요합니다.");
            return status;
        }

        String state = "UNKNOWN";
        if (protocol != null) {
            state = protocol.getState().name();
        }
        status.put("connected", isConnected());
        status.put("state", state);
        if (lastError.get() != null) {
            status.put("error", lastError.get());
        }
        return status;
    }

    private void registerListeners() {
        protocol.addChannelListener(new ChannelListener() {
            @Override
            public void onChannelOpened(java.util.EventObject event) {
                log.info("SCS 채널 오픈");
                subscribeAll();
            }

            @Override
            public void onChannelClosed(ChannelClosedEvent event) {
                log.warn("SCS 채널 종료: {}", event);
            }

            @Override
            public void onChannelError(ChannelErrorEvent event) {
                log.warn("SCS 채널 오류: {}", event);
            }
        });

        MessageHandler handler = new MessageHandler() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof EventInfo info) {
                    handleEventInfo(info);
                }
            }
        };
        protocol.setMessageHandler(handler);
    }

    private void configureWarmStandby() {
        if (!isValidServer(properties.getBackup())) {
            return;
        }
        Endpoint primary = buildEndpoint(properties.getPrimary());
        Endpoint backup = buildEndpoint(properties.getBackup());
        WarmStandbyConfiguration wsConf = new WarmStandbyConfiguration(primary, backup);
        wsConf.setAttempts((short) properties.getWarmStandbyAttempts());
        wsConf.setTimeout(properties.getWarmStandbyTimeoutMs());
        warmStandbyService = new WarmStandbyService(protocol);
        warmStandbyService.applyConfiguration(wsConf);
        warmStandbyService.addListener(new WarmStandbyListener() {
            @Override
            public void onStateChanged(WarmStandbyStateChangedEvent event) {
                log.info("SCS WarmStandby 상태 변경: {}", event.getState());
            }

            @Override
            public void onSwitchover(java.util.EventObject event) {
                log.warn("SCS WarmStandby Switchover 발생");
                subscribeAll();
            }
        });
        warmStandbyService.start();
    }

    private void openProtocol() {
        try {
            protocol.open();
            log.info("SCS 연결 성공: {}:{}", properties.getPrimary().getIp(), properties.getPrimary().getPort());
            subscribeAll();
    } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            lastError.set(ex.getMessage());
            log.warn("SCS 연결 실패", ex);
        }
    }

    private SolutionControlServerProtocol buildProtocol() {
        PropertyConfiguration config = new PropertyConfiguration();
        config.setUseAddp(properties.isAddpEnabled());
        config.setAddpClientTimeout(properties.getAddpClientTimeout());
        config.setAddpServerTimeout(properties.getAddpServerTimeout());
        config.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, properties.getCharset());

        Endpoint endpoint = buildEndpoint(properties.getPrimary(), config);
        SolutionControlServerProtocol protocol = new SolutionControlServerProtocol(endpoint);
        protocol.setClientName(properties.getClientName());
        protocol.setTimeout(properties.getTimeoutMs());
        if (properties.getClientId() != null && properties.getClientId() > 0) {
            protocol.setClientId(properties.getClientId());
        }
        if (properties.getUsername() != null) {
            protocol.setUserName(properties.getUsername());
        }
        return protocol;
    }

    private void subscribeAll() {
        if (protocol == null || protocol.getState() != ChannelState.Opened) {
            return;
        }
        if (properties.getApplications().isEmpty()) {
            log.warn("SCS 구독 대상 Application DBID가 비어 있습니다.");
            return;
        }
        for (Integer dbid : properties.getApplications()) {
            if (dbid == null) {
                continue;
            }
            try {
                RequestSubscribe request = RequestSubscribe.create();
                request.setControlObjectType(ControlObjectType.Application);
                request.setControlObjectId(dbid);
                protocol.send(request);
                log.info("SCS 구독 등록: appDbid={}", dbid);
            } catch (ProtocolException | IllegalStateException ex) {
                log.warn("SCS 구독 등록 실패: appDbid={}", dbid, ex);
            }
        }
    }

    private void handleEventInfo(EventInfo info) {
        if (info.getControlObjectType() != ControlObjectType.Application) {
            return;
        }
        Integer appDbid = info.getControlObjectId();
        if (appDbid == null) {
            return;
        }
        EventSignature signature = buildSignature(info);
        EventSignature previous = lastEventMap.put(appDbid, signature);
        if (signature.equals(previous)) {
            return;
        }
        log.debug("SCS 상태 이벤트 수신: appDbid={}, appName={}, status={}, executionMode={}",
            appDbid,
            resolveAppName(appDbid),
            resolveStatusName(info.getControlStatus()),
            signature.executionMode());
    }

    private String resolveStatusName(Integer controlStatus) {
        if (controlStatus == null) {
            return "Unknown";
        }
    ApplicationStatus status = (ApplicationStatus) ApplicationStatus.getValue(ApplicationStatus.class, controlStatus);
    return status == null ? "Unknown" : status.name();
    }

    public void refreshCurrentStatuses() {
        if (protocol == null || protocol.getState() != ChannelState.Opened) {
            return;
        }
        for (Integer dbid : properties.getApplications()) {
            if (dbid == null) {
                continue;
            }
            try {
                RequestGetApplicationInfo request = RequestGetApplicationInfo.create(dbid);
                Message response = protocol.request(request);
                if (response instanceof EventInfo info) {
                    Integer appDbid = info.getControlObjectId();
                    if (appDbid != null) {
                        lastEventMap.put(appDbid, buildSignature(info));
                    }
                }
            } catch (ProtocolException | IllegalStateException ex) {
                log.warn("SCS 현재 상태 조회 실패: appDbid={}", dbid, ex);
            }
        }
    }

    private EventSignature buildSignature(EventInfo info) {
        ApplicationExecutionMode executionMode = info.getExecutionMode();
        String executionModeName = executionMode == null ? null : executionMode.name();
        return new EventSignature(info.getControlStatus(), executionModeName, info.getDescription());
    }

    private void loadAppNames() {
        if (appNameMap.isEmpty()) {
            return;
        }
        int resolved = 0;
        for (Integer dbid : appNameMap.keySet()) {
            if (dbid == null) {
                continue;
            }
            if (resolveAppName(dbid) != null) {
                resolved++;
            }
        }
        if (resolved > 0) {
            log.info("SCS appName 조회 완료: {}건", resolved);
        }
    }

    private String resolveAppName(Integer appDbid) {
        if (appDbid == null) {
            return null;
        }
        String cached = appNameMap.get(appDbid);
        if (cached != null && !cached.isBlank() && !APP_NAME_UNKNOWN.equals(cached)) {
            return cached;
        }
        if (APP_NAME_UNKNOWN.equals(cached)) {
            return null;
        }
        String resolved = fetchAppName(appDbid);
        if (resolved == null || resolved.isBlank()) {
            appNameMap.put(appDbid, APP_NAME_UNKNOWN);
            return null;
        }
        appNameMap.put(appDbid, resolved);
        return resolved;
    }

    private String fetchAppName(Integer appDbid) {
        try {
            return configClient.withConfService(service -> {
                try {
                    CfgApplicationQuery query = new CfgApplicationQuery();
                    query.setDbid(appDbid);
                    CfgApplication app = service.retrieveObject(CfgApplication.class, query);
                    return app == null ? null : app.getName();
                } catch (ConfigException ex) {
                    log.warn("SCS appName 조회 실패: appDbid={}", appDbid, ex);
                    return null;
                }
            });
        } catch (RuntimeException ex) {
            log.debug("SCS appName 조회 불가: appDbid={}, reason={}", appDbid, ex.getMessage());
            return null;
        }
    }

    private record EventSignature(Integer controlStatus, String executionMode, String description) {
    }

    private boolean isConnected() {
        return protocol != null && protocol.getState() == ChannelState.Opened;
    }

    private Map<String, Object> buildEndpoint(String role, ScsProperties.Server server) {
        Map<String, Object> endpoint = new LinkedHashMap<>();
        endpoint.put("role", role);
        if (server != null) {
            endpoint.put("endpoint", server.getEndpoint());
            endpoint.put("ip", server.getIp());
            endpoint.put("port", server.getPort());
        }
        return endpoint;
    }

    private Map<String, Object> buildInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("clientName", properties.getClientName());
        info.put("charset", properties.getCharset());
        info.put("addpEnabled", properties.isAddpEnabled());
        info.put("subscriptions", properties.getApplications().size());
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

    private Endpoint buildEndpoint(ScsProperties.Server server) {
        return buildEndpoint(server, buildConfig());
    }

    private Endpoint buildEndpoint(ScsProperties.Server server, PropertyConfiguration config) {
        return new Endpoint(server.getEndpoint(), server.getIp(), server.getPort(), config);
    }

    private PropertyConfiguration buildConfig() {
        PropertyConfiguration configuration = new PropertyConfiguration();
        configuration.setUseAddp(properties.isAddpEnabled());
        configuration.setAddpClientTimeout(properties.getAddpClientTimeout());
        configuration.setAddpServerTimeout(properties.getAddpServerTimeout());
        configuration.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, properties.getCharset());
        return configuration;
    }

    private boolean isValidServer(ScsProperties.Server server) {
        return server != null
            && server.getIp() != null
            && !server.getIp().isBlank()
            && server.getPort() > 0;
    }

    @PreDestroy
    public void shutdown() {
        if (warmStandbyService != null) {
            try {
                warmStandbyService.stop();
            } catch (RuntimeException ex) {
                log.warn("WarmStandby 종료 실패", ex);
            }
        }
        if (protocol == null) {
            return;
        }
        try {
            if (protocol.getState() != ChannelState.Closed) {
                protocol.close(true);
            }
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            log.warn("SCS 프로토콜 종료 실패", ex);
        }
    }
}
