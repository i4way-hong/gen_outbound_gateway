package com.genoutbound.gateway.genesys.cfg.web.support;

import com.genoutbound.gateway.core.ApiResponse;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateCommand;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateRequest;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateResponse;
import com.genoutbound.gateway.genesys.cfg.service.OutboundConfigService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class OutboundBatchCreateUseCaseHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboundBatchCreateUseCaseHandler.class);

    private final OutboundConfigService outboundService;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
        justification = "Spring DI 서비스 참조를 유스케이스 처리 호출에만 사용하며 외부로 노출하지 않습니다.")
    public OutboundBatchCreateUseCaseHandler(OutboundConfigService outboundService) {
        this.outboundService = outboundService;
    }

    public ApiResponse<Object> createOutboundBatch(@Valid OutboundBatchCreateCommand command) {
        boolean detail = command.detail() == null || command.detail();
        OutboundBatchCreateRequest request = command.request();
        log.debug("createOutboundBatch 요청: request={}, detail={}", SensitiveLogMasker.masked(request), detail);

        OutboundBatchCreateResponse fullResponse = outboundService.createOutboundBatch(request);
        Object body = detail ? fullResponse : outboundService.summarizeBatch(fullResponse);

        ApiResponse<Object> response = ApiResponse.ok("아웃바운드 배치 생성", body);
        log.debug("createOutboundBatch 응답: {}", response);
        return response;
    }
}