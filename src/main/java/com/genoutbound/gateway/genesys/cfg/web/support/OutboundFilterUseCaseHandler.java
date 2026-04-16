package com.genoutbound.gateway.genesys.cfg.web.support;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.dto.DbidTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.FilterRequest;
import com.genoutbound.gateway.genesys.cfg.dto.FilterSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FilterUpdateCommand;
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
public class OutboundFilterUseCaseHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboundFilterUseCaseHandler.class);

    private final OutboundConfigService outboundService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 서비스 참조를 유스케이스 처리 호출에만 사용하며 외부로 노출하지 않습니다.")
    public OutboundFilterUseCaseHandler(OutboundConfigService outboundService) {
        this.outboundService = outboundService;
    }

    public ApiResponse<List<FilterSummary>> listFilters(TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listFilters 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<FilterSummary>> response = ApiResponse.ok("Filter 목록", outboundService.listFilters(tenantDbid));
        log.debug("listFilters 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    public ApiResponse<FilterSummary> getFilter(DbidTenantRequest request) {
        int filterDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getFilter 요청: filterDbid={}, tenantDbid={}", filterDbid, tenantDbid);
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 조회", outboundService.getFilter(filterDbid, tenantDbid));
        log.debug("getFilter 응답: {}", response);
        return response;
    }

    public ApiResponse<FilterSummary> getFilterByName(NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getFilterByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 조회", outboundService.getFilterByName(name, tenantDbid));
        log.debug("getFilterByName 응답: {}", response);
        return response;
    }

    public ApiResponse<FilterSummary> createFilter(@Valid FilterRequest request) {
        log.debug("createFilter 요청: {}", SensitiveLogMasker.masked(request));
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 생성", outboundService.createFilter(request));
        log.debug("createFilter 응답: {}", response);
        return response;
    }

    public ApiResponse<FilterSummary> updateFilter(@Valid FilterUpdateCommand command) {
        log.debug("updateFilter 요청: filterDbid={}, request={}",
            command.filterDbid(), SensitiveLogMasker.masked(command.payload()));
        ApiResponse<FilterSummary> response = ApiResponse.ok("Filter 수정",
            outboundService.updateFilter(command.filterDbid(), command.payload()));
        log.debug("updateFilter 응답: {}", response);
        return response;
    }

    public ApiResponse<Void> deleteFilter(DbidTenantRequest request) {
        int filterDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("deleteFilter 요청: filterDbid={}, tenantDbid={}", filterDbid, tenantDbid);
        outboundService.deleteFilter(filterDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("Filter 삭제", null);
        log.debug("deleteFilter 응답: {}", response);
        return response;
    }
}