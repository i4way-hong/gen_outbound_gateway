package com.genoutbound.gateway.genesys.outbound.service;

import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.OutboundDesktopBinding;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.events.EventUserEvent;
import com.genoutbound.gateway.genesys.common.GenesysUnavailableException;
import com.genoutbound.gateway.genesys.outbound.OutboundDesktopProperties;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopEventResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.OffsetDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Outbound Desktop EventUserEvent 수신을 처리합니다.
 */
@Component
public class OutboundDesktopEventListener {

    private static final Logger log = LoggerFactory.getLogger(OutboundDesktopEventListener.class);

    private final OutboundDesktopProperties properties;
    private final Deque<OutboundDesktopEventResponse> buffer = new ArrayDeque<>();
    private TServerProtocol protocol;

    public OutboundDesktopEventListener(OutboundDesktopProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void start() {
        if (!properties.isEnabled() || !properties.isListenerEnabled()) {
            log.info("Outbound Desktop listener 비활성화");
            return;
        }
        protocol = openProtocol();
        protocol.setMessageHandler(new MessageHandler() {
            @Override
            public void onMessage(Message message) {
                if (message == null || message.messageId() != EventUserEvent.ID) {
                    return;
                }
                EventUserEvent evt = (EventUserEvent) message;
                handleUserEvent(evt);
            }
        });
    }

    public List<OutboundDesktopEventResponse> getRecentEvents() {
        synchronized (buffer) {
            return new ArrayList<>(buffer);
        }
    }

    public void clear() {
        synchronized (buffer) {
            buffer.clear();
        }
    }

    private void handleUserEvent(EventUserEvent evt) {
        if (properties.getUserEventId() > 0 && evt.getUserEvent() != properties.getUserEventId()) {
            return;
        }
        KeyValueCollection userData = evt.getUserData();
        String messageType = "UNKNOWN";
        try {
            Object decoded = OutboundDesktopBinding.unmarshal(userData);
            if (decoded != null) {
                messageType = decoded.getClass().getSimpleName();
            }
        } catch (OutboundDesktopBinding.KVBindingException ex) {
            log.warn("OutboundDesktop unmarshal 실패", ex);
        }
        Map<String, Object> payload = toMap(userData);
        OutboundDesktopEventResponse response = new OutboundDesktopEventResponse(
            OffsetDateTime.now(), evt.getUserEvent(), messageType, payload);
        int maxSize = resolveBufferSize();
        synchronized (buffer) {
            if (buffer.size() >= maxSize) {
                buffer.removeFirst();
            }
            buffer.addLast(response);
        }
        log.debug("OutboundDesktop EventUserEvent 수신: {}", response);
    }

    private TServerProtocol openProtocol() {
        if (properties.getEndpoint() == null || properties.getEndpoint().isBlank()
                || properties.getIp() == null || properties.getIp().isBlank()
                || properties.getPort() == null) {
            throw new GenesysUnavailableException("T-Server 연결 설정(app.outbound.desktop.*)이 필요합니다.");
        }
        Endpoint endpoint = new Endpoint(properties.getEndpoint(), properties.getIp(), properties.getPort(), buildConfig());
        TServerProtocol tserver = new TServerProtocol(endpoint);
        tserver.setClientName(properties.getClientName());
        try {
            tserver.open();
            log.info("T-Server listener 연결 성공: {}:{}", properties.getIp(), properties.getPort());
            return tserver;
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            throw new GenesysUnavailableException("T-Server listener 연결 실패", ex);
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

    private int resolveBufferSize() {
        int maxSize = properties.getEventBufferSize();
        if (maxSize <= 0) {
            log.warn("Outbound Desktop eventBufferSize가 0 이하입니다. 기본값 50으로 설정합니다.");
            return 50;
        }
        return maxSize;
    }

    private Map<String, Object> toMap(KeyValueCollection collection) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (collection == null) {
            return result;
        }
        for (Object obj : collection) {
            if (!(obj instanceof KeyValuePair pair)) {
                continue;
            }
            Object value = pair.getValue();
            if (value instanceof KeyValueCollection nested) {
                result.put(pair.getStringKey(), toMap(nested));
            } else {
                result.put(pair.getStringKey(), value);
            }
        }
        return result;
    }

    @PreDestroy
    public void shutdown() {
        if (protocol == null) {
            return;
        }
        if (protocol.getState() == ChannelState.Closed) {
            return;
        }
        try {
            protocol.close();
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            log.warn("T-Server listener 종료 실패", ex);
        }
    }
}
