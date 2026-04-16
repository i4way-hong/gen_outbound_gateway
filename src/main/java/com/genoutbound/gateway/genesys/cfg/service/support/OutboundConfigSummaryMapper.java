package com.genoutbound.gateway.genesys.cfg.service.support;

import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingList;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaignGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgField;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFilter;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFormat;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTableAccess;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTreatment;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FieldSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FilterSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FormatSummary;
import com.genoutbound.gateway.genesys.cfg.dto.ServerSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TableAccessSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentSummary;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class OutboundConfigSummaryMapper {

    private OutboundConfigSummaryMapper() {
    }

    public static CampaignGroupSummary toCampaignGroupSummary(
            CfgCampaignGroup group,
            Function<KeyValueCollection, Map<String, Object>> userPropertiesMapper) {
        String state = group.getState() == null ? null : group.getState().name();
        String dialMode = group.getDialMode() == null ? null : group.getDialMode().name();
        String operationMode = group.getOperationMode() == null ? null : group.getOperationMode().name();
        String optMethod = group.getOptMethod() == null ? null : group.getOptMethod().name();
        Integer campaignDbid = group.getCampaignDBID();
        Integer groupDbid = group.getGroupDBID();
        String groupType = group.getGroupType() == null ? null : group.getGroupType().name();
        Integer minRecBuffSize = group.getMinRecBuffSize();
        Integer optRecBuffSize = group.getOptRecBuffSize();

        List<ServerSummary> servers = new ArrayList<>();
        if (group.getServers() != null) {
            for (CfgApplication server : group.getServers()) {
                if (server != null) {
                    servers.add(new ServerSummary(server.getDBID(), server.getName()));
                }
            }
        }

        String origDnNumber = group.getOrigDN() == null ? null : group.getOrigDN().getNumber();
        String trunkGroupDnNumber = group.getTrunkGroupDN() == null ? null : group.getTrunkGroupDN().getNumber();

        return new CampaignGroupSummary(
            group.getDBID(),
            group.getName(),
            group.getState() == CfgObjectState.CFGEnabled,
            group.getTenantDBID(),
            campaignDbid,
            groupDbid,
            groupType,
            group.getDescription(),
            state,
            dialMode,
            operationMode,
            group.getNumOfChannels(),
            optMethod,
            group.getOptMethodValue(),
            minRecBuffSize,
            optRecBuffSize,
            group.getOrigDNDBID(),
            group.getTrunkGroupDNDBID(),
            group.getScriptDBID(),
            group.getInteractionQueueDBID(),
            group.getIVRProfileDBID(),
            servers,
            origDnNumber,
            trunkGroupDnNumber,
            userPropertiesMapper.apply(group.getUserProperties())
        );
    }

    public static TableAccessSummary toTableAccessSummary(
            CfgTableAccess tableAccess,
            Function<KeyValueCollection, Map<String, Object>> userPropertiesMapper) {
        String type = tableAccess.getType() == null ? null : tableAccess.getType().name();
        String state = tableAccess.getState() == null ? null : tableAccess.getState().name();
        String isCachable = tableAccess.getIsCachable() == null ? null : tableAccess.getIsCachable().name();
        String dbAccessName = tableAccess.getDbAccess() == null ? null : tableAccess.getDbAccess().getName();
        String formatName = tableAccess.getFormat() == null ? null : tableAccess.getFormat().getName();
        return new TableAccessSummary(
            tableAccess.getDBID(),
            tableAccess.getName(),
            tableAccess.getTenantDBID(),
            tableAccess.getDescription(),
            type,
            tableAccess.getDbAccessDBID(),
            dbAccessName,
            tableAccess.getFormatDBID(),
            formatName,
            tableAccess.getDbTableName(),
            isCachable,
            tableAccess.getUpdateTimeout(),
            state,
            userPropertiesMapper.apply(tableAccess.getUserProperties())
        );
    }

    public static TreatmentSummary toTreatmentSummary(
            CfgTreatment treatment,
            Function<KeyValueCollection, Map<String, Object>> userPropertiesMapper,
            Function<java.util.Calendar, String> dateFormatter) {
        String callResult = treatment.getCallResult() == null ? null : treatment.getCallResult().name();
        String recActionCode = treatment.getRecActionCode() == null ? null : treatment.getRecActionCode().name();
        String callActionCode = treatment.getCallActionCode() == null ? null : treatment.getCallActionCode().name();
        String state = treatment.getState() == null ? null : treatment.getState().name();
        return new TreatmentSummary(
            treatment.getDBID(),
            treatment.getName(),
            treatment.getTenantDBID(),
            treatment.getDescription(),
            callResult,
            recActionCode,
            treatment.getAttempts(),
            dateFormatter.apply(treatment.getDateTime()),
            treatment.getCycleAttempt(),
            treatment.getInterval(),
            treatment.getIncrement(),
            callActionCode,
            treatment.getDestDNDBID(),
            state,
            userPropertiesMapper.apply(treatment.getUserProperties())
        );
    }

    public static FilterSummary toFilterSummary(
            CfgFilter filter,
            Function<KeyValueCollection, Map<String, Map<String, String>>> userPropertiesMapper) {
        String formatName = filter.getFormat() == null ? null : filter.getFormat().getName();
        return new FilterSummary(
            filter.getDBID(),
            filter.getName(),
            filter.getDescription(),
            filter.getState() == CfgObjectState.CFGEnabled,
            filter.getFormatDBID(),
            formatName,
            userPropertiesMapper.apply(filter.getUserProperties())
        );
    }

    public static FormatSummary toFormatSummary(
            CfgFormat format,
            Function<KeyValueCollection, Map<String, Map<String, String>>> userPropertiesMapper) {
        List<FieldSummary> fieldSummaries = new ArrayList<>();
        if (format.getFields() != null) {
            for (CfgField field : format.getFields()) {
                fieldSummaries.add(toFieldSummary(field));
            }
        }
        return new FormatSummary(
            format.getDBID(),
            format.getName(),
            format.getDescription(),
            format.getState() == CfgObjectState.CFGEnabled,
            format.getTenantDBID(),
            fieldSummaries,
            userPropertiesMapper.apply(format.getUserProperties())
        );
    }

    public static CallingListDetailSummary toCallingListDetailSummary(
            CfgCallingList callingList,
            Function<KeyValueCollection, Map<String, Map<String, String>>> userPropertiesMapper) {
        List<Integer> treatmentDbids = new ArrayList<>();
        if (callingList.getTreatmentDBIDs() != null) {
            treatmentDbids.addAll(callingList.getTreatmentDBIDs());
        }
        return new CallingListDetailSummary(
            callingList.getDBID(),
            callingList.getName(),
            callingList.getDescription(),
            callingList.getFilterDBID(),
            callingList.getLogTableAccessDBID(),
            callingList.getMaxAttempts(),
            callingList.getScriptDBID(),
            callingList.getTableAccessDBID(),
            callingList.getTimeFrom(),
            callingList.getTimeUntil(),
            callingList.getState() == CfgObjectState.CFGEnabled,
            treatmentDbids,
            userPropertiesMapper.apply(callingList.getUserProperties())
        );
    }

    private static FieldSummary toFieldSummary(CfgField field) {
        String fieldType = field.getFieldType() == null ? null : field.getFieldType().name();
        return new FieldSummary(
            field.getDBID(),
            field.getName(),
            fieldType,
            field.getDescription()
        );
    }
}