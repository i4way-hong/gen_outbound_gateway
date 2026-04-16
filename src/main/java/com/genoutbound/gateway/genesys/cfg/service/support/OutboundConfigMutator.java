package com.genoutbound.gateway.genesys.cfg.service.support;

import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaignGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTreatment;
import com.genesyslab.platform.configuration.protocol.types.CfgCallActionCode;
import com.genesyslab.platform.configuration.protocol.types.CfgDialMode;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.types.CfgOptimizationMethod;
import com.genesyslab.platform.configuration.protocol.types.CfgOperationMode;
import com.genesyslab.platform.configuration.protocol.types.CfgRecActionCode;
import com.genesyslab.platform.configuration.protocol.types.GctiCallState;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

public final class OutboundConfigMutator {

    private OutboundConfigMutator() {
    }

    public static void applyCampaignGroupRequest(CfgCampaignGroup group,
                                                 CampaignGroupRequest request,
                                                 boolean isCreate,
                                                 Function<Map<String, Map<String, String>>, KeyValueCollection> userPropsConverter) {
        if (isCreate && request.campaignDbid() != null) {
            group.setCampaignDBID(request.campaignDbid());
        }
        if (isCreate && request.groupDbid() != null) {
            group.setGroupDBID(request.groupDbid());
        }
        if (isCreate && request.groupType() != null && !request.groupType().isBlank()) {
            CfgObjectType groupType = CfgValueParser.parseEnum(request.groupType(), CfgObjectType.class, "groupType");
            if (groupType != null) {
                group.setGroupType(groupType);
            }
        }
        if (request.name() != null && !request.name().isBlank()) {
            group.setName(request.name());
        }
        if (request.description() != null) {
            group.setDescription(request.description());
        }
        if (request.dialMode() != null && !request.dialMode().isBlank()) {
            CfgDialMode dialMode = CfgValueParser.parseEnum(request.dialMode(), CfgDialMode.class, "dialMode");
            if (dialMode != null) {
                group.setDialMode(dialMode);
            }
        }
        if (request.operationMode() != null && !request.operationMode().isBlank()) {
            CfgOperationMode operationMode = CfgValueParser.parseEnum(request.operationMode(), CfgOperationMode.class,
                "operationMode");
            if (operationMode != null) {
                group.setOperationMode(operationMode);
            }
        }
        if (request.numOfChannels() != null) {
            group.setNumOfChannels(request.numOfChannels());
        }
        if (request.optMethod() != null && !request.optMethod().isBlank()) {
            CfgOptimizationMethod optMethod = CfgValueParser.parseEnum(request.optMethod(), CfgOptimizationMethod.class,
                "optMethod");
            if (optMethod != null) {
                group.setOptMethod(optMethod);
            }
        }
        if (request.optMethodValue() != null) {
            group.setOptMethodValue(request.optMethodValue());
        }
        if (request.minRecBuffSize() != null) {
            group.setMinRecBuffSize(request.minRecBuffSize());
        }
        if (request.optRecBuffSize() != null) {
            group.setOptRecBuffSize(request.optRecBuffSize());
        }
        if (request.origDnDbid() != null) {
            group.setOrigDNDBID(request.origDnDbid());
        }
        if (request.trunkGroupDnDbid() != null) {
            group.setTrunkGroupDNDBID(request.trunkGroupDnDbid());
        }
        if (request.scriptDbid() != null) {
            group.setScriptDBID(request.scriptDbid());
        }
        if (request.interactionQueueDbid() != null) {
            group.setInteractionQueueDBID(request.interactionQueueDbid());
        }
        if (request.ivrProfileDbid() != null) {
            group.setIVRProfileDBID(request.ivrProfileDbid());
        }
        if (request.serverDbids() != null) {
            group.setServerDBIDs(new HashSet<>(request.serverDbids()));
        }
        if (request.userProperties() != null && !request.userProperties().isEmpty()) {
            group.setUserProperties(userPropsConverter.apply(request.userProperties()));
        }
    }

    public static void applyTreatmentRequest(CfgTreatment treatment,
                                             TreatmentRequest request,
                                             Function<Map<String, Map<String, String>>, KeyValueCollection> userPropsConverter) {
        if (request.name() != null && !request.name().isBlank()) {
            treatment.setName(request.name());
        }
        if (request.description() != null) {
            treatment.setDescription(request.description());
        }
        if (request.callResult() != null && !request.callResult().isBlank()) {
            GctiCallState callResult = CfgValueParser.parseEnum(request.callResult(), GctiCallState.class,
                "callResult");
            if (callResult != null) {
                treatment.setCallResult(callResult);
            }
        }
        if (request.recActionCode() != null && !request.recActionCode().isBlank()) {
            CfgRecActionCode recActionCode = CfgValueParser.parseEnum(request.recActionCode(), CfgRecActionCode.class,
                "recActionCode");
            if (recActionCode != null) {
                treatment.setRecActionCode(recActionCode);
            }
        }
        if (request.attempts() != null) {
            treatment.setAttempts(request.attempts());
        }
        if (request.dateTime() != null) {
            treatment.setDateTime(CfgValueParser.parseIsoDateTime(request.dateTime(), "dateTime"));
        }
        if (request.cycleAttempt() != null) {
            treatment.setCycleAttempt(request.cycleAttempt());
        }
        if (request.interval() != null) {
            treatment.setInterval(request.interval());
        }
        if (request.increment() != null) {
            treatment.setIncrement(request.increment());
        }
        if (request.callActionCode() != null && !request.callActionCode().isBlank()) {
            CfgCallActionCode callActionCode = CfgValueParser.parseEnum(request.callActionCode(),
                CfgCallActionCode.class, "callActionCode");
            if (callActionCode != null) {
                treatment.setCallActionCode(callActionCode);
            }
        }
        if (request.destDnDbid() != null) {
            treatment.setDestDNDBID(request.destDnDbid());
        }
        if (request.userProperties() != null && !request.userProperties().isEmpty()) {
            treatment.setUserProperties(userPropsConverter.apply(request.userProperties()));
        }
    }
}
