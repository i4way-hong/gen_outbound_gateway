package com.genoutbound.gateway.genesys.outbound.service;

import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.outbound.protocol.OutboundServerProtocol;
import com.genesyslab.platform.outbound.protocol.outboundserver.DialMode;
import com.genesyslab.platform.outbound.protocol.outboundserver.OptimizationMethod;
import com.genesyslab.platform.outbound.protocol.outboundserver.events.EventCampaignLoaded;
import com.genesyslab.platform.outbound.protocol.outboundserver.events.EventCampaignStatus;
import com.genesyslab.platform.outbound.protocol.outboundserver.events.EventCampaignUnloaded;
import com.genesyslab.platform.outbound.protocol.outboundserver.events.EventDialingStarted;
import com.genesyslab.platform.outbound.protocol.outboundserver.events.EventDialingStopped;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestForceUnloadCampaign;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestGetCampaignStatus;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestLoadCampaign;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestStartDialing;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestStopDialing;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestUnloadCampaign;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundCommandRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDialRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundStatusResponse;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Outbound API 비즈니스 로직을 처리합니다.
 */
@Service
public class OutboundService {

    private static final Logger log = LoggerFactory.getLogger(OutboundService.class);
    private final OutboundClient outboundClient;

    /**
     * OutboundService 생성자.
     *
     * @param outboundClient Outbound 클라이언트
     */
    public OutboundService(OutboundClient outboundClient) {
        this.outboundClient = outboundClient;
    }

    /**
     * 캠페인을 로드합니다.
     */
    public OutboundStatusResponse loadCampaign(OutboundCommandRequest request) {
        log.info("Outbound 캠페인 로드 요청: campaignDbid={}, groupDbid={}", request.campaignDbid(), request.groupDbid());
        return outboundClient.withProtocol(protocol -> {
            RequestLoadCampaign req = RequestLoadCampaign.create();
            req.setCampaignId(request.campaignDbid());
            req.setGroupId(request.groupDbid());
            return toStatusResponse(request, send(protocol, req));
        });
    }

    /**
     * 캠페인을 언로드합니다.
     */
    public OutboundStatusResponse unloadCampaign(OutboundCommandRequest request) {
        log.info("Outbound 캠페인 언로드 요청: campaignDbid={}, groupDbid={}", request.campaignDbid(), request.groupDbid());
        return outboundClient.withProtocol(protocol -> {
            RequestUnloadCampaign req = RequestUnloadCampaign.create();
            req.setCampaignId(request.campaignDbid());
            req.setGroupId(request.groupDbid());
            return toStatusResponse(request, send(protocol, req));
        });
    }

    /**
     * 캠페인을 강제 언로드합니다.
     */
    public OutboundStatusResponse forceUnloadCampaign(OutboundCommandRequest request) {
        log.info("Outbound 캠페인 강제 언로드 요청: campaignDbid={}, groupDbid={}", request.campaignDbid(), request.groupDbid());
        return outboundClient.withProtocol(protocol -> {
            RequestForceUnloadCampaign req = RequestForceUnloadCampaign.create();
            req.setCampaignId(request.campaignDbid());
            req.setGroupId(request.groupDbid());
            return toStatusResponse(request, send(protocol, req));
        });
    }

    /**
     * 다이얼링을 시작합니다.
     */
    public OutboundStatusResponse startDialing(OutboundDialRequest request) {
        log.info("Outbound 다이얼 시작 요청: campaignDbid={}, groupDbid={}, dialMode={}, optimizeMethod={}",
                request.campaignDbid(), request.groupDbid(), request.dialMode(), request.optimizeMethod());
        return outboundClient.withProtocol(protocol -> {
            RequestStartDialing req = RequestStartDialing.create();
            req.setCampaignId(request.campaignDbid());
            req.setGroupId(request.groupDbid());
            req.setDialMode(resolveDialMode(request.dialMode()));
            req.setOptimizeBy(resolveOptimizationMethod(request.optimizeMethod()));
            req.setOptimizeGoal(request.resolvedOptimizeGoal());
            return toStatusResponse(request, send(protocol, req));
        });
    }

    /**
     * 다이얼링을 중지합니다.
     */
    public OutboundStatusResponse stopDialing(OutboundCommandRequest request) {
        log.info("Outbound 다이얼 중지 요청: campaignDbid={}, groupDbid={}", request.campaignDbid(), request.groupDbid());
        return outboundClient.withProtocol(protocol -> {
            RequestStopDialing req = RequestStopDialing.create();
            req.setCampaignId(request.campaignDbid());
            req.setGroupId(request.groupDbid());
            return toStatusResponse(request, send(protocol, req));
        });
    }

    /**
     * 캠페인 상태를 조회합니다.
     */
    public OutboundStatusResponse getCampaignStatus(OutboundCommandRequest request) {
        log.info("Outbound 캠페인 상태 요청: campaignDbid={}, groupDbid={}", request.campaignDbid(), request.groupDbid());
        return outboundClient.withProtocol(protocol -> {
            RequestGetCampaignStatus req = RequestGetCampaignStatus.create();
            req.setCampaignId(request.campaignDbid());
            req.setGroupId(request.groupDbid());
            return toStatusResponse(request, send(protocol, req));
        });
    }

    private Message send(OutboundServerProtocol protocol, Message request) {
        try {
            return protocol.request(request);
        } catch (ProtocolException ex) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, "Outbound 서버 요청 실패");
        } catch (IllegalStateException ex) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "Outbound 서버 연결 상태가 올바르지 않습니다.");
        }
    }

    private OutboundStatusResponse toStatusResponse(OutboundCommandRequest request, Message message) {
        return new OutboundStatusResponse(request.campaignDbid(), request.groupDbid(), extractStatus(message));
    }

    private OutboundStatusResponse toStatusResponse(OutboundDialRequest request, Message message) {
        return new OutboundStatusResponse(request.campaignDbid(), request.groupDbid(), extractStatus(message));
    }

    private String extractStatus(Message message) {
        if (message == null) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, "Outbound 서버 응답이 없습니다.");
        }
        if (message.messageId() == EventCampaignLoaded.ID) {
            return ((EventCampaignLoaded) message).getGroupCampaignStatus().toString();
        }
        if (message.messageId() == EventDialingStarted.ID) {
            return ((EventDialingStarted) message).getGroupCampaignStatus().toString();
        }
        if (message.messageId() == EventDialingStopped.ID) {
            return ((EventDialingStopped) message).getGroupCampaignStatus().toString();
        }
        if (message.messageId() == EventCampaignUnloaded.ID) {
            return ((EventCampaignUnloaded) message).getGroupCampaignStatus().toString();
        }
        if (message.messageId() == EventCampaignStatus.ID) {
            return ((EventCampaignStatus) message).getGroupCampaignStatus().toString();
        }
        return "UNKNOWN";
    }

    private DialMode resolveDialMode(String mode) {
        String normalized = mode == null ? "" : mode.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "predict" -> DialMode.Predict;
            case "predictandseize" -> DialMode.PredictAndSeize;
            case "preview" -> DialMode.Preview;
            case "progress" -> DialMode.Progress;
            case "progressandseize" -> DialMode.ProgressAndSeize;
            default -> DialMode.NoDialMode;
        };
    }

    private OptimizationMethod resolveOptimizationMethod(String method) {
        String normalized = method == null ? "" : method.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "busyfactor" -> OptimizationMethod.BusyFactor;
            case "overdialrate" -> OptimizationMethod.OverdialRate;
            case "waittime" -> OptimizationMethod.WaitTime;
            default -> OptimizationMethod.NoOptimizationMethod;
        };
    }
}
