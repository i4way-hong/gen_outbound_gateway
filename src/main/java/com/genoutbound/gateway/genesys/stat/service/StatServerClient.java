package com.genoutbound.gateway.genesys.stat.service;

import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.reporting.protocol.StatServerProtocol;
import com.genesyslab.platform.reporting.protocol.statserver.AgentGroup;
import com.genesyslab.platform.reporting.protocol.statserver.AgentStatus;
import com.genesyslab.platform.reporting.protocol.statserver.AgentStatusesCollection;
import com.genesyslab.platform.reporting.protocol.statserver.Notification;
import com.genesyslab.platform.reporting.protocol.statserver.NotificationMode;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticMetric;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticObject;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticObjectType;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventInfo;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestCloseStatistic;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestOpenStatistic;
import com.genoutbound.gateway.genesys.stat.StatServerProperties;
import com.genoutbound.gateway.genesys.common.GenesysUnavailableException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StatServerClient {

    private static final Logger log = LoggerFactory.getLogger(StatServerClient.class);
    private final StatServerProperties properties;

    public StatServerClient(StatServerProperties properties) {
        this.properties = properties;
    }

    public List<Map<String, Object>> getSkillGroupAgentStatuses(String skillGroupName) {
        if (!properties.isEnabled()) {
            throw new GenesysUnavailableException("Stat Server 연동이 비활성화되어 있습니다.");
        }
        EventInfo eventInfo = fetchGroupAgentState(skillGroupName);
        if (eventInfo == null) {
            return List.of();
        }
        Object stateValue = eventInfo.getStateValue();
        if (!(stateValue instanceof AgentGroup group)) {
            return List.of();
        }
        AgentStatusesCollection agents = group.getAgents();
        if (agents == null || group.getAgentCount() <= 0) {
            return List.of();
        }
        List<Map<String, Object>> results = new ArrayList<>();
        for (int i = 0; i < agents.getCount(); i++) {
            AgentStatus status = (AgentStatus) agents.getItem(i);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("EMPLOYEE_ID", status.getAgentId());
            item.put("STAT", String.valueOf(status.getStatus()));
            results.add(item);
        }
        return results;
    }

    private EventInfo fetchGroupAgentState(String skillGroupName) {
        AtomicReference<EventInfo> eventInfoRef = new AtomicReference<>();
        StatServerProtocol protocol = openProtocol(eventInfoRef);
        int referenceId = ThreadLocalRandom.current().nextInt(100000, 999999);
        RequestCloseStatistic closeRequest = RequestCloseStatistic.create();
        closeRequest.setReferenceId(referenceId);
        closeRequest.setStatisticId(referenceId);
        try {
            RequestOpenStatistic request = buildOpenStatistic(skillGroupName, referenceId);

            if (protocol.getState() != ChannelState.Opened) {
                throw new GenesysUnavailableException("Stat Server 연결 상태가 올바르지 않습니다.");
            }

            Message response = protocol.request(request);
            log.debug("StatServer 응답: {}", response == null ? null : response.messageName());
            Thread.sleep(properties.getDelayMs());
            protocol.send(closeRequest);
            return eventInfoRef.get();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new GenesysUnavailableException("Stat Server 응답 대기 중 중단되었습니다.", ex);
        } catch (ProtocolException | IllegalStateException ex) {
            throw new GenesysUnavailableException("Stat Server 요청 실패", ex);
        } finally {
            if (protocol != null && protocol.getState() == ChannelState.Opened) {
                try {
                    protocol.send(closeRequest);
                } catch (ProtocolException | IllegalStateException ex) {
                    log.debug("Stat Server 통계 종료 요청 실패", ex);
                }
            }
            closeProtocol(protocol);
        }
    }

    private StatServerProtocol openProtocol(AtomicReference<EventInfo> eventInfoRef) {
        StatServerProtocol protocol = buildProtocol(properties.getPrimary(), eventInfoRef);
        try {
            protocol.open();
            log.info("Stat Server 연결 성공: {}:{}", properties.getPrimary().getIp(), properties.getPrimary().getPort());
            return protocol;
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            log.warn("Primary Stat Server 연결 실패. Backup으로 재시도합니다.", ex);
            closeProtocol(protocol);
            StatServerProtocol backup = buildProtocol(properties.getBackup(), eventInfoRef);
            try {
                backup.open();
                log.info("Stat Server(backup) 연결 성공: {}:{}", properties.getBackup().getIp(), properties.getBackup().getPort());
                return backup;
            } catch (ProtocolException | IllegalStateException | InterruptedException backupEx) {
                closeProtocol(backup);
                throw new GenesysUnavailableException("Stat Server 연결 실패", backupEx);
            }
        }
    }

    public Map<String, Object> getConnectionStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("enabled", properties.isEnabled());
        //status.put("endpoints", List.of(
        //    buildEndpoint("primary", properties.getPrimary()),
        //    buildEndpoint("backup", properties.getBackup())));
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
        StatServerProtocol protocol = null;
        try {
            protocol = openProtocol(new AtomicReference<>());
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

    private Map<String, Object> buildEndpoint(String role, StatServerProperties.Server server) {
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
        info.put("tenantName", properties.getTenantName());
        info.put("defaultStatistic", properties.getDefaultStatistic());
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

    private StatServerProtocol buildProtocol(StatServerProperties.Server server,
                                             AtomicReference<EventInfo> eventInfoRef) {
        PropertyConfiguration config = new PropertyConfiguration();
        config.setUseAddp(properties.isAddpEnabled());
        config.setAddpClientTimeout(properties.getAddpClientTimeout());
        config.setAddpServerTimeout(properties.getAddpServerTimeout());
        config.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, properties.getCharset());

        Endpoint endpoint = new Endpoint(server.getEndpoint(), server.getIp(), server.getPort(), config);
        StatServerProtocol protocol = new StatServerProtocol(endpoint);
        protocol.setClientName(properties.getClientName());
        protocol.setTimeout(properties.getTimeoutMs());
        protocol.setMessageHandler(message -> {
            if (message instanceof EventInfo info) {
                eventInfoRef.set(info);
            }
        });
        return protocol;
    }

    private RequestOpenStatistic buildOpenStatistic(String groupName, int referenceId) {
        RequestOpenStatistic request = RequestOpenStatistic.create();
        StatisticObject object = StatisticObject.create();
        object.setObjectId(groupName);
        object.setObjectType(StatisticObjectType.GroupAgents);
    object.setTenantName(properties.getTenantName());
        object.setTenantPassword("");

        StatisticMetric metric = StatisticMetric.create();
    metric.setStatisticType(properties.getDefaultStatistic());
        metric.setTimeProfile("Default");
        metric.setTimeRange("");
        metric.setFilter("");

        Notification notification = Notification.create();
        notification.setMode(NotificationMode.Periodical);
        notification.setFrequency(10);

        request.setStatisticObject(object);
        request.setStatisticMetric(metric);
        request.setNotification(notification);
        request.setReferenceId(referenceId);
        return request;
    }

    private void closeProtocol(StatServerProtocol protocol) {
        if (protocol == null) {
            return;
        }
        try {
            if (protocol.getState() != ChannelState.Closed) {
                protocol.close();
            }
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            log.warn("Stat Server 종료 실패", ex);
        }
    }
}
