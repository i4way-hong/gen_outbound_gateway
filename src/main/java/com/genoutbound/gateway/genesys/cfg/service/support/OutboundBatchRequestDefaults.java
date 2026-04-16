package com.genoutbound.gateway.genesys.cfg.service.support;

import java.util.List;

import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignRequest;

public final class OutboundBatchRequestDefaults {

    private OutboundBatchRequestDefaults() {
    }

    public static CallingListDetailRequest withFilterDbidIfMissing(CallingListDetailRequest request, int filterDbid) {
        if (request == null || request.filterDbid() != null) {
            return request;
        }
        return new CallingListDetailRequest(
            request.tenantDbid(),
            request.name(),
            request.description(),
            filterDbid,
            request.logTableAccessDbid(),
            request.maxAttempts(),
            request.scriptDbid(),
            request.tableAccessDbid(),
            request.timeFrom(),
            request.timeTo(),
            request.enabled(),
            request.treatmentDbids(),
            request.userProperties()
        );
    }

    public static CampaignRequest withCallingListIfMissing(CampaignRequest request, String callingListName) {
        if (request == null || (request.callingListNames() != null && !request.callingListNames().isEmpty())) {
            return request;
        }
        return new CampaignRequest(
            request.tenantDbid(),
            request.name(),
            request.description(),
            request.scriptDbid(),
            List.of(callingListName),
            request.userProperties(),
            request.enabled()
        );
    }

    public static CampaignGroupRequest withCampaignDbidIfMissing(CampaignGroupRequest request, int campaignDbid) {
        if (request == null || request.campaignDbid() != null) {
            return request;
        }
        return new CampaignGroupRequest(
            request.tenantDbid(),
            campaignDbid,
            request.groupDbid(),
            request.groupType(),
            request.name(),
            request.description(),
            request.dialMode(),
            request.operationMode(),
            request.numOfChannels(),
            request.optMethod(),
            request.optMethodValue(),
            request.minRecBuffSize(),
            request.optRecBuffSize(),
            request.origDnDbid(),
            request.trunkGroupDnDbid(),
            request.scriptDbid(),
            request.interactionQueueDbid(),
            request.ivrProfileDbid(),
            request.serverDbids(),
            request.userProperties(),
            request.enabled()
        );
    }
}