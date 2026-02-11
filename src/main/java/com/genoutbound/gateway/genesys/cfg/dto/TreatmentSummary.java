package com.genoutbound.gateway.genesys.cfg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

public record TreatmentSummary(
	@Schema(description = "Treatment DBID", example = "14001")
	int dbid,
	@Schema(description = "Treatment 이름", example = "TREATMENT_A")
	String name,
	@Schema(description = "테넌트 DBID", example = "101")
	Integer tenantDbid,
	@Schema(description = "설명", example = "Treatment 설명")
	String description,
	@Schema(description = "콜 결과", example = "Answer")
	String callResult,
	@Schema(description = "녹취 액션 코드", example = "CFGRACRetryIn")
	String recActionCode,
	@Schema(description = "시도 횟수", example = "3")
	Integer attempts,
	@Schema(description = "재시도 시각(ISO-8601)", example = "2026-02-09T10:30:00+09:00")
	String dateTime,
	@Schema(description = "순환 시도 횟수", example = "5")
	Integer cycleAttempt,
	@Schema(description = "간격(분)", example = "10")
	Integer interval,
	@Schema(description = "증분(분)", example = "5")
	Integer increment,
	@Schema(description = "콜 액션 코드", example = "CFGCACTreatment")
	String callActionCode,
	@Schema(description = "대상 DN DBID", example = "15001")
	Integer destDnDbid,
	@Schema(description = "상태", example = "CFGEnabled")
	String state,
	@Schema(description = "사용자 속성")
	Map<String, Object> userProperties
) {
}
