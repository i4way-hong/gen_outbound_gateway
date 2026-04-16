package com.genoutbound.gateway.genesys.cfg.web.support;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.dto.DbidTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.NameTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TenantDbidRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentUpdateCommand;
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
public class OutboundTreatmentUseCaseHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboundTreatmentUseCaseHandler.class);

    private final OutboundConfigService outboundService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 서비스 참조를 유스케이스 처리 호출에만 사용하며 외부로 노출하지 않습니다.")
    public OutboundTreatmentUseCaseHandler(OutboundConfigService outboundService) {
        this.outboundService = outboundService;
    }

    public ApiResponse<List<TreatmentSummary>> listTreatment(TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listTreatment 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<TreatmentSummary>> response = ApiResponse.ok("Treatment 목록",
            outboundService.listTreatment(tenantDbid));
        log.debug("listTreatment 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    public ApiResponse<TreatmentSummary> getTreatment(DbidTenantRequest request) {
        int treatmentDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getTreatment 요청: treatmentDbid={}, tenantDbid={}", treatmentDbid, tenantDbid);
        ApiResponse<TreatmentSummary> response = ApiResponse.ok("Treatment 조회",
            outboundService.getTreatment(treatmentDbid, tenantDbid));
        log.debug("getTreatment 응답: {}", response);
        return response;
    }

    public ApiResponse<TreatmentSummary> getTreatmentByName(NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getTreatmentByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<TreatmentSummary> response = ApiResponse.ok("Treatment 조회",
            outboundService.getTreatmentByName(name, tenantDbid));
        log.debug("getTreatmentByName 응답: {}", response);
        return response;
    }

    public ApiResponse<TreatmentSummary> createTreatment(@Valid TreatmentRequest request) {
        log.debug("createTreatment 요청: {}", SensitiveLogMasker.masked(request));
        ApiResponse<TreatmentSummary> response = ApiResponse.ok("Treatment 생성",
            outboundService.createTreatment(request));
        log.debug("createTreatment 응답: {}", response);
        return response;
    }

    public ApiResponse<TreatmentSummary> updateTreatment(@Valid TreatmentUpdateCommand command) {
        log.debug("updateTreatment 요청: treatmentDbid={}, request={}",
            command.treatmentDbid(), SensitiveLogMasker.masked(command.payload()));
        ApiResponse<TreatmentSummary> response = ApiResponse.ok("Treatment 수정",
            outboundService.updateTreatment(command.treatmentDbid(), command.payload()));
        log.debug("updateTreatment 응답: {}", response);
        return response;
    }

    public ApiResponse<Void> deleteTreatment(DbidTenantRequest request) {
        int treatmentDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("deleteTreatment 요청: treatmentDbid={}, tenantDbid={}", treatmentDbid, tenantDbid);
        outboundService.deleteTreatment(treatmentDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("Treatment 삭제", null);
        log.debug("deleteTreatment 응답: {}", response);
        return response;
    }
}