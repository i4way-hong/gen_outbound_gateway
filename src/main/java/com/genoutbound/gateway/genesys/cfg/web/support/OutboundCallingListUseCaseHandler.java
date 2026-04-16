package com.genoutbound.gateway.genesys.cfg.web.support;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListUpdateCommand;
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
public class OutboundCallingListUseCaseHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboundCallingListUseCaseHandler.class);

    private final OutboundConfigService outboundService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 서비스 참조를 유스케이스 처리 호출에만 사용하며 외부로 노출하지 않습니다.")
    public OutboundCallingListUseCaseHandler(OutboundConfigService outboundService) {
        this.outboundService = outboundService;
    }

    public ApiResponse<List<CallingListDetailSummary>> listCallingLists(TenantDbidRequest request) {
        Integer tenantDbid = request == null ? null : request.tenantDbid();
        log.debug("listCallingLists 요청: tenantDbid={}", tenantDbid);
        ApiResponse<List<CallingListDetailSummary>> response = ApiResponse.ok("콜링리스트 목록",
            outboundService.listCallingLists(tenantDbid));
        log.debug("listCallingLists 응답: count={}", response.data() == null ? 0 : response.data().size());
        return response;
    }

    public ApiResponse<CallingListDetailSummary> getCallingList(DbidTenantRequest request) {
        int callingListDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCallingList 요청: callingListDbid={}, tenantDbid={}", callingListDbid, tenantDbid);
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 조회",
            outboundService.getCallingList(callingListDbid, tenantDbid));
        log.debug("getCallingList 응답: {}", response);
        return response;
    }

    public ApiResponse<CallingListDetailSummary> getCallingListByName(NameTenantRequest request) {
        String name = request.name();
        Integer tenantDbid = request.tenantDbid();
        log.debug("getCallingListByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 조회",
            outboundService.getCallingListByName(name, tenantDbid));
        log.debug("getCallingListByName 응답: {}", response);
        return response;
    }

    public ApiResponse<CallingListDetailSummary> createCallingList(@Valid CallingListDetailRequest request) {
        log.debug("createCallingList 요청: {}", SensitiveLogMasker.masked(request));
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 생성",
            outboundService.createCallingList(request));
        log.debug("createCallingList 응답: {}", response);
        return response;
    }

    public ApiResponse<CallingListDetailSummary> updateCallingList(@Valid CallingListUpdateCommand command) {
        log.debug("updateCallingList 요청: callingListDbid={}, request={}",
            command.callingListDbid(), SensitiveLogMasker.masked(command.payload()));
        ApiResponse<CallingListDetailSummary> response = ApiResponse.ok("콜링리스트 수정",
            outboundService.updateCallingList(command.callingListDbid(), command.payload()));
        log.debug("updateCallingList 응답: {}", response);
        return response;
    }

    public ApiResponse<Void> deleteCallingList(DbidTenantRequest request) {
        int callingListDbid = request.dbid();
        Integer tenantDbid = request.tenantDbid();
        log.debug("deleteCallingList 요청: callingListDbid={}, tenantDbid={}", callingListDbid, tenantDbid);
        outboundService.deleteCallingList(callingListDbid, tenantDbid);
        ApiResponse<Void> response = ApiResponse.ok("콜링리스트 삭제", null);
        log.debug("deleteCallingList 응답: {}", response);
        return response;
    }
}