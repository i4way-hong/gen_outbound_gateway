package com.genoutbound.gateway.genesys.cfg.service.support;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingList;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingListInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaign;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaignGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaignGroupInfo;
import com.genesyslab.platform.applicationblocks.com.queries.CfgCallingListQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgCampaignGroupQuery;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignSummary;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;

public final class OutboundCampaignSummaryAssembler {

    private OutboundCampaignSummaryAssembler() {
    }

    public static CampaignSummary toCampaignSummary(
            Logger log,
            IConfService service,
            CfgCampaign campaign,
            Function<CfgCallingList, CallingListDetailSummary> callingListMapper,
            Function<CfgCampaignGroup, CampaignGroupSummary> campaignGroupMapper,
            Function<KeyValueCollection, Map<String, Object>> userPropertiesMapper) {

        List<CallingListDetailSummary> callingLists = new ArrayList<>();
        for (CfgCallingListInfo info : safeCollection(campaign.getCallingLists())) {
            CallingListDetailSummary summary = resolveCallingListSummary(log, service, campaign, info, callingListMapper);
            if (summary != null) {
                callingLists.add(summary);
            }
        }

        List<CampaignGroupSummary> campaignGroups = resolveCampaignGroups(log, service, campaign, campaignGroupMapper);
        String state = campaign.getState() == null ? null : campaign.getState().name();

        return new CampaignSummary(
            campaign.getDBID(),
            campaign.getName(),
            campaign.getDescription(),
            campaign.getState() == CfgObjectState.CFGEnabled,
            campaign.getTenantDBID(),
            campaign.getScriptDBID(),
            state,
            callingLists,
            userPropertiesMapper.apply(campaign.getUserProperties()),
            campaignGroups
        );
    }

    private static CallingListDetailSummary resolveCallingListSummary(
            Logger log,
            IConfService service,
            CfgCampaign campaign,
            CfgCallingListInfo info,
            Function<CfgCallingList, CallingListDetailSummary> callingListMapper) {

        CfgCallingList callingList = info.getCallingList();
        if (callingList != null) {
            return callingListMapper.apply(callingList);
        }

        if (info.getCallingListDBID() == null) {
            return null;
        }

        try {
            CfgCallingListQuery query = new CfgCallingListQuery();
            query.setTenantDbid(campaign.getTenantDBID());
            query.setDbid(info.getCallingListDBID());
            CfgCallingList fetched = service.retrieveObject(CfgCallingList.class, query);
            return fetched == null ? null : callingListMapper.apply(fetched);
        } catch (ConfigException ex) {
            log.warn("CallingList 상세 조회 실패: callingListDbid={}", info.getCallingListDBID(), ex);
            return null;
        }
    }

    private static CampaignGroupSummary resolveCampaignGroupSummary(
            Logger log,
            IConfService service,
            CfgCampaign campaign,
            CfgCampaignGroupInfo info,
            Function<CfgCampaignGroup, CampaignGroupSummary> campaignGroupMapper) {

        if (info.getGroupDBID() == null) {
            return null;
        }

        try {
            CfgCampaignGroupQuery query = new CfgCampaignGroupQuery();
            query.setTenantDbid(campaign.getTenantDBID());
            query.setDbid(info.getGroupDBID());
            CfgCampaignGroup fetched = service.retrieveObject(CfgCampaignGroup.class, query);
            return fetched == null ? null : campaignGroupMapper.apply(fetched);
        } catch (ConfigException ex) {
            log.warn("CampaignGroup 상세 조회 실패: groupDbid={}", info.getGroupDBID(), ex);
            return null;
        }
    }

    private static List<CampaignGroupSummary> resolveCampaignGroups(
            Logger log,
            IConfService service,
            CfgCampaign campaign,
            Function<CfgCampaignGroup, CampaignGroupSummary> campaignGroupMapper) {

        List<CampaignGroupSummary> campaignGroups = new ArrayList<>();
        List<CfgCampaignGroupInfo> groupInfos = new ArrayList<>(safeCollection(campaign.getCampaignGroups()));

        if (groupInfos.isEmpty()) {
            log.debug("CampaignGroupInfo 비어있음: campaignDbid={}", campaign.getDBID());
        } else {
            for (CfgCampaignGroupInfo info : groupInfos) {
                CampaignGroupSummary summary = resolveCampaignGroupSummary(log, service, campaign, info, campaignGroupMapper);
                if (summary != null) {
                    campaignGroups.add(summary);
                }
            }
        }

        if (!campaignGroups.isEmpty()) {
            return campaignGroups;
        }

        try {
            CfgCampaignGroupQuery query = new CfgCampaignGroupQuery();
            query.setTenantDbid(campaign.getTenantDBID());
            query.setCampaignDbid(campaign.getDBID());
            Collection<CfgCampaignGroup> groups = service.retrieveMultipleObjects(CfgCampaignGroup.class, query);
            for (CfgCampaignGroup group : safeCollection(groups)) {
                campaignGroups.add(campaignGroupMapper.apply(group));
            }
        } catch (ConfigException | InterruptedException ex) {
            log.warn("CampaignGroup 목록 조회 실패: campaignDbid={}", campaign.getDBID(), ex);
        }

        return campaignGroups;
    }

    private static <T> Collection<T> safeCollection(Collection<T> source) {
        return source == null ? List.of() : source;
    }
}