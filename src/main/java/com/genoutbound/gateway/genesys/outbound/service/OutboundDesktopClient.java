package com.genoutbound.gateway.genesys.outbound.service;

import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.CommonProperties;
import com.genesyslab.platform.voice.protocol.tserver.requests.special.RequestDistributeUserEvent;
import com.genoutbound.gateway.genesys.common.GenesysUnavailableException;
import com.genoutbound.gateway.genesys.outbound.OutboundDesktopProperties;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Outbound Desktop(T-Server) 프로토콜 호출을 담당합니다.
 */
@Component
public class OutboundDesktopClient {

    private static final Logger log = LoggerFactory.getLogger(OutboundDesktopClient.class);
    private final OutboundDesktopProperties properties;

    public OutboundDesktopClient(OutboundDesktopProperties properties) {
        this.properties = properties;
    }

    public <T> T withProtocol(Function<TServerProtocol, T> action) {
        if (!properties.isEnabled()) {
            throw new GenesysUnavailableException("Outbound Desktop 연동이 비활성화되어 있습니다.");
        }
        TServerProtocol protocol = openProtocol();
        try {
            return action.apply(protocol);
        } catch (Exception ex) {
            throw new GenesysUnavailableException("Outbound Desktop 요청 처리 실패", ex);
        } finally {
            closeProtocol(protocol);
        }
    }

    public Message sendUserEvent(String communicationDn, CommonProperties userEvent) {
        return withProtocol(protocol -> {
            RequestDistributeUserEvent request = RequestDistributeUserEvent.create();
            request.setCommunicationDN(communicationDn);
            request.setUserEvent(userEvent);
            try {
                return protocol.request(request);
            } catch (ProtocolException | IllegalStateException ex) {
                throw new GenesysUnavailableException("Outbound Desktop 전송 실패", ex);
            }
        });
    }

    private TServerProtocol openProtocol() {
        if (properties.getEndpoint() == null || properties.getEndpoint().isBlank()
                || properties.getIp() == null || properties.getIp().isBlank()
                || properties.getPort() == null) {
            throw new GenesysUnavailableException("T-Server 연결 설정(app.outbound.desktop.*)이 필요합니다.");
        }
        Endpoint endpoint = new Endpoint(properties.getEndpoint(), properties.getIp(), properties.getPort(), buildConfig());
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

    private PropertyConfiguration buildConfig() {
        PropertyConfiguration configuration = new PropertyConfiguration();
        configuration.setUseAddp(properties.isAddpEnabled());
        configuration.setAddpClientTimeout(properties.getAddpClientTimeout());
        configuration.setAddpServerTimeout(properties.getAddpServerTimeout());
        configuration.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, properties.getCharset());
        return configuration;
    }

    private void closeProtocol(TServerProtocol protocol) {
        if (protocol == null) {
            return;
        }
        if (protocol.getState() == ChannelState.Closed) {
            return;
        }
        try {
            protocol.close();
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            log.warn("T-Server 종료 실패", ex);
        }
    }
}
