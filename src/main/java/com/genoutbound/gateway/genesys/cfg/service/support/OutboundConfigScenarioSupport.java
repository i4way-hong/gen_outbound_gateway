package com.genoutbound.gateway.genesys.cfg.service.support;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingList;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingListInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaign;
import com.genesyslab.platform.applicationblocks.com.queries.CfgCallingListQuery;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FilterSummary;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.slf4j.Logger;

public final class OutboundConfigScenarioSupport {

    private OutboundConfigScenarioSupport() {
    }

    @FunctionalInterface
    public interface DbidTenantDeleteAction {
        void delete(int dbid, Integer tenantDbid);
    }

    public static void rollbackBatch(Logger log,
                                     OutboundBatchCreateRequest request,
                                     FilterSummary filter,
                                     CallingListDetailSummary callingList,
                                     CampaignSummary campaign,
                                     CampaignGroupSummary campaignGroup,
                                     DbidTenantDeleteAction deleteCampaignGroup,
                                     DbidTenantDeleteAction deleteCampaign,
                                     DbidTenantDeleteAction deleteCallingList,
                                     DbidTenantDeleteAction deleteFilter) {
        log.warn("createOutboundBatch 실패. 보상 삭제를 시도합니다.");
        Integer tenantDbid = request.filter() == null ? null : request.filter().tenantDbid();

        if (campaignGroup != null) {
            try {
                deleteCampaignGroup.delete(campaignGroup.dbid(), tenantDbid);
            } catch (RuntimeException rollbackEx) {
                log.warn("CampaignGroup 롤백 실패: groupDbid={}", campaignGroup.dbid(), rollbackEx);
            }
        }

        if (campaign != null) {
            try {
                deleteCampaign.delete(campaign.dbid(), tenantDbid);
            } catch (RuntimeException rollbackEx) {
                log.warn("Campaign 롤백 실패: campaignDbid={}", campaign.dbid(), rollbackEx);
            }
        }

        if (callingList != null) {
            try {
                deleteCallingList.delete(callingList.dbid(), tenantDbid);
            } catch (RuntimeException rollbackEx) {
                log.warn("CallingList 롤백 실패: callingListDbid={}", callingList.dbid(), rollbackEx);
            }
        }

        if (filter != null) {
            try {
                deleteFilter.delete(filter.dbid(), tenantDbid);
            } catch (RuntimeException rollbackEx) {
                log.warn("Filter 롤백 실패: filterDbid={}", filter.dbid(), rollbackEx);
            }
        }
    }

    public static void attachCallingListsIfPresent(IConfService service,
                                                   int tenantDbid,
                                                   CfgCampaign campaign,
                                                   List<String> callingListNames) throws ConfigException {
        if (callingListNames == null || callingListNames.isEmpty()) {
            return;
        }

        Collection<CfgCallingListInfo> listInfos = new HashSet<>();
        for (String name : callingListNames) {
            if (name == null || name.isBlank()) {
                continue;
            }
            CfgCallingListQuery query = new CfgCallingListQuery();
            query.setTenantDbid(tenantDbid);
            query.setName(name);
            CfgCallingList callingList = service.retrieveObject(CfgCallingList.class, query);
            if (callingList != null) {
                CfgCallingListInfo info = new CfgCallingListInfo(service, callingList);
                info.setCallingList(callingList);
                info.setIsActive(com.genesyslab.platform.configuration.protocol.types.CfgFlag.CFGTrue);
                info.setShare(10);
                listInfos.add(info);
            }
        }
        campaign.setCallingLists(listInfos);
    }
}