package com.genoutbound.gateway.genesys.cfg.web.support;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignUpdateCommand;
import com.genoutbound.gateway.genesys.cfg.dto.DbidTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.NameTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TenantDbidRequest;
import com.genoutbound.gateway.genesys.cfg.service.OutboundConfigService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class OutboundCampaignUseCaseHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboundCampaignUseCaseHandler.class);

    private final OutboundConfigService outboundService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 서비스 참조를 유스케이스 처리 호출에만 사용하며 외부로 노출하지 않습니다.")
    public OutboundCampaignUseCaseHandler(OutboundConfigService outboundService) {
        this.outboundService = outboundService;
    }

    public ApiResponse<List<CampaignSummary>> listCampaigns(TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listCampaigns 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<CampaignSummary>> response = ApiResponse.ok("캠페인 목록",
            outboundService.listCampaigns(tenantDbid));
        log.debug("listCampaigns 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    public ApiResponse<CampaignSummary> getCampaign(DbidTenantRequest request) {
        int campaignDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCampaign 요청: campaignDbid={}, tenantDbid={}", campaignDbid, tenantDbid);
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 조회",
            outboundService.getCampaign(campaignDbid, tenantDbid));
        log.debug("getCampaign 응답: {}", response);
        return response;
    }

    public ApiResponse<CampaignSummary> getCampaignByName(NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCampaignByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 조회",
            outboundService.getCampaignByName(name, tenantDbid));
        log.debug("getCampaignByName 응답: {}", response);
        return response;
    }

    public ApiResponse<CampaignSummary> createCampaign(@Valid CampaignRequest request) {
        log.debug("createCampaign 요청: {}", SensitiveLogMasker.masked(request));
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 생성",
            outboundService.createCampaign(request));
        log.debug("createCampaign 응답: {}", response);
        return response;
    }

    public ApiResponse<CampaignSummary> updateCampaign(@Valid CampaignUpdateCommand command) {
        log.debug("updateCampaign 요청: campaignDbid={}, request={}",
            command.campaignDbid(), SensitiveLogMasker.masked(command.payload()));
        ApiResponse<CampaignSummary> response = ApiResponse.ok("캠페인 수정",
            outboundService.updateCampaign(command.campaignDbid(), command.payload()));
        log.debug("updateCampaign 응답: {}", response);
        return response;
    }

    public ApiResponse<Void> deleteCampaign(DbidTenantRequest request) {
        int campaignDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("deleteCampaign 요청: campaignDbid={}, tenantDbid={}", campaignDbid, tenantDbid);
        outboundService.deleteCampaign(campaignDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("캠페인 삭제", null);
        log.debug("deleteCampaign 응답: {}", response);
        return response;
    }
}