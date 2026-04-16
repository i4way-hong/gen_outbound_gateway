package com.genoutbound.gateway.genesys.cfg.web.support;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.cfg.dto.DbidTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.NameTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TableAccessSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TenantDbidRequest;
import com.genoutbound.gateway.genesys.cfg.service.OutboundConfigService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OutboundTableAccessUseCaseHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboundTableAccessUseCaseHandler.class);

    private final OutboundConfigService outboundService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 서비스 참조를 유스케이스 처리 호출에만 사용하며 외부로 노출하지 않습니다.")
    public OutboundTableAccessUseCaseHandler(OutboundConfigService outboundService) {
        this.outboundService = outboundService;
    }

    public ApiResponse<List<TableAccessSummary>> listTableAccess(TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listTableAccess 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<TableAccessSummary>> response = ApiResponse.ok("TableAccess 목록",
            outboundService.listTableAccess(tenantDbid));
        log.debug("listTableAccess 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    public ApiResponse<TableAccessSummary> getTableAccess(DbidTenantRequest request) {
        int tableAccessDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getTableAccess 요청: tableAccessDbid={}, tenantDbid={}", tableAccessDbid, tenantDbid);
        ApiResponse<TableAccessSummary> response = ApiResponse.ok("TableAccess 조회",
            outboundService.getTableAccess(tableAccessDbid, tenantDbid));
        log.debug("getTableAccess 응답: {}", response);
        return response;
    }

    public ApiResponse<TableAccessSummary> getTableAccessByName(NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getTableAccessByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<TableAccessSummary> response = ApiResponse.ok("TableAccess 조회",
            outboundService.getTableAccessByName(name, tenantDbid));
        log.debug("getTableAccessByName 응답: {}", response);
        return response;
    }
}