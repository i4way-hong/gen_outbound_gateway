package com.genoutbound.gateway.genesys.cfg.service;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genoutbound.gateway.genesys.cfg.config.GenesysProperties;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.genesys.common.GenesysUnavailableException;
import jakarta.annotation.PreDestroy;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Genesys Config Server 연결을 관리하고 재시도/헬스체크를 수행합니다.
 */
@Component
public class GenesysConfigClient {

    private static final Logger log = LoggerFactory.getLogger(GenesysConfigClient.class);
    private final GenesysProperties properties;
    private final Object connectionLock = new Object();

    private PropertyConfiguration config;
    private ConfServerProtocol protocol;
    private IConfService service;

    public GenesysConfigClient(GenesysProperties properties) {
        this.properties = properties;
    }

    /**
     * 설정 서비스 연결을 확보한 뒤 전달된 작업을 실행합니다.
     *
     * @param action 실행할 작업
     * @param <T> 반환 타입
     * @return 작업 결과
     */
    public <T> T withConfService(Function<IConfService, T> action) {
        log.debug("withConfService 요청: action={}", action == null ? null : action.getClass().getName());
        if (action == null) {
            throw new ApiException(org.springframework.http.HttpStatus.BAD_REQUEST, "action 설정이 필요합니다.");
        }
        if (!properties.isEnabled()) {
            throw new GenesysUnavailableException("Genesys 설정이 비활성화되어 있습니다.");
        }
        IConfService currentService;
        synchronized (connectionLock) {
            ensureConnected();
            currentService = service;
        }
        try {
            T result = action.apply(currentService);
            log.debug("withConfService 응답: result={}", result);
            return result;
        } catch (Exception ex) {
            if (isConnectionFailure(ex)) {
                log.warn("Config Server 연결 이상 감지. 재연결을 시도합니다.", ex);
                synchronized (connectionLock) {
                    resetConnection();
                    ensureConnected();
                    currentService = service;
                }
                T result = action.apply(currentService);
                log.debug("withConfService 재시도 응답: result={}", result);
                return result;
            }
            if (ex instanceof ApiException apiException) {
                throw apiException;
            }
            if (ex instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new GenesysUnavailableException("요청 처리 중 오류가 발생했습니다.", ex);
        }
    }

    /**
     * Config Server 상태를 주기적으로 점검합니다.
     */
    @Scheduled(fixedDelayString = "${app.genesys.health-check-interval-ms:30000}")
    public void healthCheck() {
        log.debug("healthCheck 요청");
        if (!properties.isEnabled()) {
            log.debug("healthCheck 중단: genesys 비활성화");
            return;
        }
        synchronized (connectionLock) {
            try {
                if (protocol == null || service == null || protocol.getState() != ChannelState.Opened) {
                    log.warn("Config Server 연결이 끊어져 재연결을 시도합니다.");
                    resetConnection();
                    ensureConnected();
                }
            } catch (Exception ex) {
                log.warn("Config Server 헬스체크 실패. 재연결을 시도합니다.", ex);
                resetConnection();
                ensureConnected();
            }
        }
        log.debug("healthCheck 응답: 완료");
    }

    private void ensureConnected() {
        if (service != null && protocol != null && protocol.getState() == ChannelState.Opened) {
            return;
        }
        if (config == null) {
            config = buildConfig();
        }
        if (protocol == null) {
            protocol = new ConfServerProtocol(buildEndpoint(properties.getPrimary(), config));
            protocol.setClientName(properties.getClientName());
            protocol.setClientApplicationType(CfgAppType.CFGSCE.ordinal());
            protocol.setUserName(properties.getUsername());
            protocol.setUserPassword(properties.getPassword());
        }
        if (service == null) {
            service = ConfServiceFactory.createConfService(protocol);
        }
        connectWithFailover();
    }

    private void connectWithFailover() {
        try {
            open(protocol, properties.getPrimary(), config);
        } catch (GenesysUnavailableException primaryError) {
            log.warn("Primary Config Server 연결 실패. Backup으로 재시도합니다.", primaryError);
            try {
                open(protocol, properties.getBackup(), config);
            } catch (GenesysUnavailableException backupError) {
                throw new GenesysUnavailableException("Config Server 연결에 실패했습니다.", backupError);
            }
        }
    }

    private void open(ConfServerProtocol protocol, GenesysProperties.ConfigServer server, PropertyConfiguration config) {
        protocol.setEndpoint(buildEndpoint(server, config));
        log.info("Config Server 연결 시도: {}:{}", server.getIp(), server.getPort());
        try {
            protocol.open();
            Integer serverEncoding = protocol.getServerContext().getServerEncoding();
            if (serverEncoding != null && serverEncoding == 1) {
                config.setStringsEncoding("UTF-8");
                protocol.close();
                protocol.setEndpoint(buildEndpoint(server, config));
                protocol.open();
            }
            log.info("Config Server 연결 성공: {}:{}", server.getIp(), server.getPort());
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            throw new GenesysUnavailableException("Config Server 연결 실패", ex);
        }
    }

    private PropertyConfiguration buildConfig() {
        PropertyConfiguration configuration = new PropertyConfiguration();
        configuration.setUseAddp(properties.isAddpEnabled());
        configuration.setAddpClientTimeout(properties.getAddpClientTimeout());
        configuration.setAddpServerTimeout(properties.getAddpServerTimeout());
        configuration.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, properties.getCharset());
        return configuration;
    }

    private void resetConnection() {
        closeService(service);
        service = null;
        try {
            closeProtocol(protocol);
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            log.warn("Config Protocol 종료 중 오류", ex);
        }
        protocol = null;
    }

    private boolean isConnectionFailure(Throwable ex) {
        return hasCause(ex, ProtocolException.class)
            || hasCause(ex, IllegalStateException.class)
            || ex instanceof GenesysUnavailableException;
    }

    private boolean hasCause(Throwable ex, Class<? extends Throwable> type) {
        Throwable current = ex;
        while (current != null) {
            if (type.isInstance(current)) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private Endpoint buildEndpoint(GenesysProperties.ConfigServer server, PropertyConfiguration config) {
        return new Endpoint(server.getEndpoint(), server.getIp(), server.getPort(), config);
    }

    private void closeService(IConfService service) {
        if (service == null) {
            return;
        }
        try {
            closeProtocol(service.getProtocol());
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            log.warn("Config Service 종료 중 오류", ex);
        } finally {
            ConfServiceFactory.releaseConfService(service);
        }
    }

    private void closeProtocol(com.genesyslab.platform.commons.protocol.Protocol protocol)
            throws ProtocolException, InterruptedException {
        if (protocol != null && protocol.getState() != ChannelState.Closed) {
            protocol.close();
        }
    }

    @PreDestroy
    /**
     * 애플리케이션 종료 시 연결을 정리합니다.
     */
    public void shutdown() {
        log.debug("shutdown 요청");
        synchronized (connectionLock) {
            resetConnection();
        }
        log.debug("shutdown 응답: 완료");
    }

    /**
     * 기본 테넌트 DBID를 반환합니다.
     */
    public int getTenantDbid() {
        int result = properties.getTenantDbid();
        log.debug("getTenantDbid 응답: {}", result);
        return result;
    }

    /**
     * Primary 스위치 DBID를 반환합니다.
     */
    public int getPrimarySwitchDbid() {
        int result = properties.getSwitchDbidPrimary();
        log.debug("getPrimarySwitchDbid 응답: {}", result);
        return result;
    }

    /**
     * Secondary 스위치 DBID를 반환합니다.
     */
    public int getSecondarySwitchDbid() {
        int result = properties.getSwitchDbidSecondary();
        log.debug("getSecondarySwitchDbid 응답: {}", result);
        return result;
    }
}
