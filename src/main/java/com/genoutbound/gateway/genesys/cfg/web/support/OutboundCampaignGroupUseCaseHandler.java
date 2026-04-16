package com.genoutbound.gateway.genesys.cfg.web.support;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupUpdateCommand;
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
public class OutboundCampaignGroupUseCaseHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboundCampaignGroupUseCaseHandler.class);

    private final OutboundConfigService outboundService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 서비스 참조를 유스케이스 처리 호출에만 사용하며 외부로 노출하지 않습니다.")
    public OutboundCampaignGroupUseCaseHandler(OutboundConfigService outboundService) {
        this.outboundService = outboundService;
    }

    public ApiResponse<List<CampaignGroupSummary>> listCampaignGroups(TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listCampaignGroups 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<CampaignGroupSummary>> response = ApiResponse.ok("CampaignGroup 목록",
            outboundService.listCampaignGroups(tenantDbid));
        log.debug("listCampaignGroups 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    public ApiResponse<CampaignGroupSummary> getCampaignGroup(DbidTenantRequest request) {
        int groupDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCampaignGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 조회",
            outboundService.getCampaignGroup(groupDbid, tenantDbid));
        log.debug("getCampaignGroup 응답: {}", response);
        return response;
    }

    public ApiResponse<CampaignGroupSummary> getCampaignGroupByName(NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCampaignGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 조회",
            outboundService.getCampaignGroupByName(name, tenantDbid));
        log.debug("getCampaignGroupByName 응답: {}", response);
        return response;
    }

    public ApiResponse<CampaignGroupSummary> createCampaignGroup(@Valid CampaignGroupRequest request) {
        log.debug("createCampaignGroup 요청: {}", SensitiveLogMasker.masked(request));
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 생성",
            outboundService.createCampaignGroup(request));
        log.debug("createCampaignGroup 응답: {}", response);
        return response;
    }

    public ApiResponse<CampaignGroupSummary> updateCampaignGroup(@Valid CampaignGroupUpdateCommand command) {
        log.debug("updateCampaignGroup 요청: groupDbid={}, request={}",
            command.groupDbid(), SensitiveLogMasker.masked(command.payload()));
        ApiResponse<CampaignGroupSummary> response = ApiResponse.ok("CampaignGroup 수정",
            outboundService.updateCampaignGroup(command.groupDbid(), command.payload()));
        log.debug("updateCampaignGroup 응답: {}", response);
        return response;
    }

    public ApiResponse<Void> deleteCampaignGroup(DbidTenantRequest request) {
        int groupDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("deleteCampaignGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        outboundService.deleteCampaignGroup(groupDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("CampaignGroup 삭제", null);
        log.debug("deleteCampaignGroup 응답: {}", response);
        return response;
    }
}