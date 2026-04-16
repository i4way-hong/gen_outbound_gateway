package com.genoutbound.gateway.genesys.cfg.web.support;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.genesys.cfg.dto.DbidTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.FormatSummary;
import com.genoutbound.gateway.genesys.cfg.dto.NameTenantRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TenantDbidRequest;
import com.genoutbound.gateway.genesys.cfg.service.OutboundConfigService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OutboundFormatUseCaseHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboundFormatUseCaseHandler.class);

    private final OutboundConfigService outboundService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 서비스 참조를 유스케이스 처리 호출에만 사용하며 외부로 노출하지 않습니다.")
    public OutboundFormatUseCaseHandler(OutboundConfigService outboundService) {
        this.outboundService = outboundService;
    }

    public ApiResponse<List<FormatSummary>> listFormats(TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listFormats 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<FormatSummary>> response = ApiResponse.ok("Format 목록",
            outboundService.listFormats(tenantDbid));
        log.debug("listFormats 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    public ApiResponse<FormatSummary> getFormat(DbidTenantRequest request) {
        int formatDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getFormat 요청: formatDbid={}, tenantDbid={}", formatDbid, tenantDbid);
        ApiResponse<FormatSummary> response = ApiResponse.ok("Format 조회",
            outboundService.getFormat(formatDbid, tenantDbid));
        log.debug("getFormat 응답: {}", response);
        return response;
    }

    public ApiResponse<FormatSummary> getFormatByName(NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getFormatByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<FormatSummary> response = ApiResponse.ok("Format 조회",
            outboundService.getFormatByName(name, tenantDbid));
        log.debug("getFormatByName 응답: {}", response);
        return response;
    }
}