package com.genoutbound.gateway.genesys.cfg.service;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingList;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaign;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaignGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFilter;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFormat;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTableAccess;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTreatment;
import com.genesyslab.platform.applicationblocks.com.queries.CfgCallingListQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgCampaignGroupQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgCampaignQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgFilterQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgFormatQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgTableAccessQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgTreatmentQuery;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.core.logging.SensitiveLogMasker;
import com.genoutbound.gateway.genesys.cfg.service.support.OutboundBatchRequestDefaults;
import com.genoutbound.gateway.genesys.cfg.service.support.OutboundCampaignSummaryAssembler;
import com.genoutbound.gateway.genesys.cfg.service.support.OutboundConfigMutator;
import com.genoutbound.gateway.genesys.cfg.service.support.OutboundConfigScenarioSupport;
import com.genoutbound.gateway.genesys.cfg.service.support.OutboundConfigSummaryMapper;
import com.genoutbound.gateway.genesys.cfg.service.support.CfgValueFormatter;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FilterRequest;
import com.genoutbound.gateway.genesys.cfg.dto.FilterSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FormatSummary;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateRequest;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateResponse;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TableAccessSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TreatmentSummary;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * 아웃바운드 관련 설정(Filter/CallingList/Campaign 등)을 담당합니다.
 */
@Service
public class OutboundConfigService extends GenesysConfigSupport {

    private static final Logger log = LoggerFactory.getLogger(OutboundConfigService.class);

    public OutboundConfigService(GenesysConfigClient configClient) {
        super(configClient);
    }

    public List<CallingListDetailSummary> listCallingLists(Integer tenantDbid) {
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        log.debug("listCallingLists 요청: tenantDbid={}, resolvedTenant={}", tenantDbid, resolvedTenant);
        return configClient.withConfService(service -> {
            try {
                CfgCallingListQuery query = new CfgCallingListQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgCallingList> lists = service.retrieveMultipleObjects(CfgCallingList.class, query);
                List<CallingListDetailSummary> summaries = new ArrayList<>();
                for (CfgCallingList list : safeCollection(lists)) {
                    summaries.add(toCallingListDetailSummary(list));
                }
                log.debug("listCallingLists 응답: count={}", summaries.size());
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "콜링리스트 조회 실패");
            }
        });
    }

    public List<FilterSummary> listFilters(Integer tenantDbid) {
        log.debug("listFilters 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<FilterSummary> result = configClient.withConfService(service -> {
            try {
                CfgFilterQuery query = new CfgFilterQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgFilter> filters = service.retrieveMultipleObjects(CfgFilter.class, query);
                List<FilterSummary> summaries = new ArrayList<>();
                for (CfgFilter filter : safeCollection(filters)) {
                    summaries.add(toFilterSummary(filter));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Filter 조회 실패");
            }
        });
        log.debug("listFilters 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public FilterSummary getFilter(int filterDbid, Integer tenantDbid) {
        log.debug("getFilter 요청: filterDbid={}, tenantDbid={}", filterDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        FilterSummary result = configClient.withConfService(service -> {
            try {
                CfgFilterQuery query = new CfgFilterQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(filterDbid);
                CfgFilter filter = service.retrieveObject(CfgFilter.class, query);
                if (filter == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Filter를 찾을 수 없습니다.");
                }
                return toFilterSummary(filter);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Filter 조회 실패");
            }
        });
        log.debug("getFilter 응답: {}", result);
        return result;
    }

    public FilterSummary getFilterByName(String name, Integer tenantDbid) {
        log.debug("getFilterByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        FilterSummary result = configClient.withConfService(service -> {
            try {
                CfgFilterQuery query = new CfgFilterQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgFilter filter = service.retrieveObject(CfgFilter.class, query);
                if (filter == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Filter를 찾을 수 없습니다.");
                }
                return toFilterSummary(filter);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Filter 조회 실패");
            }
        });
        log.debug("getFilterByName 응답: {}", result);
        return result;
    }

    public FilterSummary createFilter(FilterRequest request) {
    log.debug("createFilter 요청: {}", SensitiveLogMasker.masked(request));
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        FilterSummary result = configClient.withConfService(service -> {
            try {
                CfgFilterQuery duplicateQuery = new CfgFilterQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setName(request.name());
                CfgFilter existing = service.retrieveObject(CfgFilter.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 Filter입니다.");

                CfgFilter filter = new CfgFilter(service);
                filter.setTenantDBID(resolvedTenant);
                filter.setName(request.name());
                if (request.description() != null) {
                    filter.setDescription(request.description());
                }
                if (request.formatDbid() != null) {
                    filter.setFormatDBID(request.formatDbid());
                }
                if (request.userProperties() != null && !request.userProperties().isEmpty()) {
                    filter.setUserProperties(toUserProperties(request.userProperties()));
                }
                filter.setState(request.enabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                filter.save();
                return toFilterSummary(filter);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Filter 생성 실패");
            }
        });
        log.debug("createFilter 응답: {}", result);
        return result;
    }

    public FilterSummary updateFilter(int filterDbid, FilterRequest request) {
    log.debug("updateFilter 요청: filterDbid={}, payload={}", filterDbid, SensitiveLogMasker.masked(request));
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        FilterSummary result = configClient.withConfService(service -> {
            try {
                CfgFilterQuery query = new CfgFilterQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(filterDbid);
                CfgFilter filter = service.retrieveObject(CfgFilter.class, query);
                if (filter == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Filter를 찾을 수 없습니다.");
                }
                filter.setName(request.name());
                if (request.description() != null) {
                    filter.setDescription(request.description());
                }
                if (request.userProperties() != null && !request.userProperties().isEmpty()) {
                    filter.setUserProperties(toUserProperties(request.userProperties()));
                }
                filter.setState(request.enabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                filter.save();
                return toFilterSummary(filter);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Filter 수정 실패");
            }
        });
        log.debug("updateFilter 응답: {}", result);
        return result;
    }

    public void deleteFilter(int filterDbid, Integer tenantDbid) {
        log.debug("deleteFilter 요청: filterDbid={}, tenantDbid={}", filterDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        configClient.withConfService(service -> {
            try {
                CfgFilterQuery query = new CfgFilterQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(filterDbid);
                CfgFilter filter = service.retrieveObject(CfgFilter.class, query);
                if (filter == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Filter를 찾을 수 없습니다.");
                }
                filter.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Filter 삭제 실패");
            }
        });
        log.debug("deleteFilter 응답: 완료");
    }

    public List<FormatSummary> listFormats(Integer tenantDbid) {
        log.debug("listFormats 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<FormatSummary> result = configClient.withConfService(service -> {
            try {
                CfgFormatQuery query = new CfgFormatQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgFormat> formats = service.retrieveMultipleObjects(CfgFormat.class, query);
                List<FormatSummary> summaries = new ArrayList<>();
                for (CfgFormat format : safeCollection(formats)) {
                    summaries.add(toFormatSummary(format));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Format 조회 실패");
            }
        });
        log.debug("listFormats 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public FormatSummary getFormat(int formatDbid, Integer tenantDbid) {
        log.debug("getFormat 요청: formatDbid={}, tenantDbid={}", formatDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        FormatSummary result = configClient.withConfService(service -> {
            try {
                CfgFormatQuery query = new CfgFormatQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(formatDbid);
                CfgFormat format = service.retrieveObject(CfgFormat.class, query);
                if (format == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Format을 찾을 수 없습니다.");
                }
                return toFormatSummary(format);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Format 조회 실패");
            }
        });
        log.debug("getFormat 응답: {}", result);
        return result;
    }

    public FormatSummary getFormatByName(String name, Integer tenantDbid) {
        log.debug("getFormatByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        FormatSummary result = configClient.withConfService(service -> {
            try {
                CfgFormatQuery query = new CfgFormatQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgFormat format = service.retrieveObject(CfgFormat.class, query);
                if (format == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Format을 찾을 수 없습니다.");
                }
                return toFormatSummary(format);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Format 조회 실패");
            }
        });
        log.debug("getFormatByName 응답: {}", result);
        return result;
    }

    public CallingListDetailSummary getCallingList(int callingListDbid, Integer tenantDbid) {
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        log.debug("getCallingList 요청: callingListDbid={}, tenantDbid={}, resolvedTenant={}", callingListDbid, tenantDbid, resolvedTenant);
        return configClient.withConfService(service -> {
            try {
                CfgCallingListQuery query = new CfgCallingListQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(callingListDbid);
                CfgCallingList callingList = service.retrieveObject(CfgCallingList.class, query);
                if (callingList == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "콜링리스트를 찾을 수 없습니다.");
                }
                CallingListDetailSummary summary = toCallingListDetailSummary(callingList);
                log.debug("getCallingList 응답: {}", summary);
                return summary;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "콜링리스트 조회 실패");
            }
        });
    }

    public CallingListDetailSummary getCallingListByName(String name, Integer tenantDbid) {
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        log.debug("getCallingListByName 요청: name={}, tenantDbid={}, resolvedTenant={}", name, tenantDbid, resolvedTenant);
        return configClient.withConfService(service -> {
            try {
                CfgCallingListQuery query = new CfgCallingListQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgCallingList callingList = service.retrieveObject(CfgCallingList.class, query);
                if (callingList == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "콜링리스트를 찾을 수 없습니다.");
                }
                CallingListDetailSummary summary = toCallingListDetailSummary(callingList);
                log.debug("getCallingListByName 응답: {}", summary);
                return summary;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "콜링리스트 조회 실패");
            }
        });
    }

    public CallingListDetailSummary createCallingList(CallingListDetailRequest request) {
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
    log.debug("createCallingList 요청: resolvedTenant={}, payload={}", resolvedTenant, SensitiveLogMasker.masked(request));
        return configClient.withConfService(service -> {
            try {
                CfgCallingListQuery duplicateQuery = new CfgCallingListQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setName(request.name());
                CfgCallingList existing = service.retrieveObject(CfgCallingList.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 콜링리스트입니다.");

                CfgCallingList callingList = new CfgCallingList(service);
                callingList.setTenantDBID(resolvedTenant);
                callingList.setName(request.name());
                if (request.description() != null) {
                    callingList.setDescription(request.description());
                }
                if (request.filterDbid() != null) {
                    callingList.setFilterDBID(request.filterDbid());
                }
                if (request.logTableAccessDbid() != null) {
                    callingList.setLogTableAccessDBID(request.logTableAccessDbid());
                }
                callingList.setMaxAttempts(request.resolvedMaxAttempts());
                if (request.scriptDbid() != null) {
                    callingList.setScriptDBID(request.scriptDbid());
                }
                if (request.tableAccessDbid() != null) {
                    callingList.setTableAccessDBID(request.tableAccessDbid());
                }
                if (request.timeFrom() != null) {
                    callingList.setTimeFrom(request.timeFrom());
                }
                if (request.timeTo() != null) {
                    callingList.setTimeUntil(request.timeTo());
                }
                callingList.setState(request.resolvedEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                callingList.save();

                if (request.treatmentDbids() != null && !request.treatmentDbids().isEmpty()) {
                    callingList.setTreatmentDBIDs(new HashSet<>(request.treatmentDbids()));
                }
                if (request.userProperties() != null && !request.userProperties().isEmpty()) {
                    callingList.setUserProperties(toUserProperties(request.userProperties()));
                }
                callingList.save();
                CallingListDetailSummary summary = toCallingListDetailSummary(callingList);
                log.debug("createCallingList 응답: {}", summary);
                return summary;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "콜링리스트 생성 실패");
            }
        });
    }

    public CallingListDetailSummary updateCallingList(int callingListDbid, CallingListDetailRequest request) {
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        log.debug("updateCallingList 요청: callingListDbid={}, resolvedTenant={}, payload={}",
            callingListDbid, resolvedTenant, SensitiveLogMasker.masked(request));
        return configClient.withConfService(service -> {
            try {
                CfgCallingListQuery query = new CfgCallingListQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(callingListDbid);
                CfgCallingList callingList = service.retrieveObject(CfgCallingList.class, query);
                if (callingList == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "콜링리스트를 찾을 수 없습니다.");
                }
                callingList.setName(request.name());
                if (request.description() != null) {
                    callingList.setDescription(request.description());
                }
                if (request.filterDbid() != null) {
                    callingList.setFilterDBID(request.filterDbid());
                }
                if (request.logTableAccessDbid() != null) {
                    callingList.setLogTableAccessDBID(request.logTableAccessDbid());
                }
                callingList.setMaxAttempts(request.resolvedMaxAttempts());
                if (request.scriptDbid() != null) {
                    callingList.setScriptDBID(request.scriptDbid());
                }
                if (request.tableAccessDbid() != null) {
                    callingList.setTableAccessDBID(request.tableAccessDbid());
                }
                if (request.timeFrom() != null) {
                    callingList.setTimeFrom(request.timeFrom());
                }
                if (request.timeTo() != null) {
                    callingList.setTimeUntil(request.timeTo());
                }
                callingList.setState(request.resolvedEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                if (request.treatmentDbids() != null) {
                    callingList.setTreatmentDBIDs(new HashSet<>(request.treatmentDbids()));
                }
                if (request.userProperties() != null && !request.userProperties().isEmpty()) {
                    callingList.setUserProperties(toUserProperties(request.userProperties()));
                }
                callingList.save();
                CallingListDetailSummary summary = toCallingListDetailSummary(callingList);
                log.debug("updateCallingList 응답: {}", summary);
                return summary;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "콜링리스트 수정 실패");
            }
        });
    }

    public void deleteCallingList(int callingListDbid, Integer tenantDbid) {
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        log.debug("deleteCallingList 요청: callingListDbid={}, tenantDbid={}, resolvedTenant={}", callingListDbid, tenantDbid, resolvedTenant);
        configClient.withConfService(service -> {
            try {
                CfgCallingListQuery query = new CfgCallingListQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(callingListDbid);
                CfgCallingList callingList = service.retrieveObject(CfgCallingList.class, query);
                if (callingList == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "콜링리스트를 찾을 수 없습니다.");
                }
                callingList.delete();
                log.debug("deleteCallingList 완료: callingListDbid={}", callingListDbid);
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "콜링리스트 삭제 실패");
            }
        });
    }

    public TableAccessSummary getTableAccessByName(String name, Integer tenantDbid) {
        log.debug("getTableAccessByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        TableAccessSummary result = configClient.withConfService(service -> {
            try {
                CfgTableAccessQuery query = new CfgTableAccessQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgTableAccess tableAccess = service.retrieveObject(CfgTableAccess.class, query);
                if (tableAccess == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "TableAccess를 찾을 수 없습니다.");
                }
                return toTableAccessSummary(tableAccess);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "TableAccess 조회 실패");
            }
        });
        log.debug("getTableAccessByName 응답: {}", result);
        return result;
    }

    public TableAccessSummary getTableAccess(int tableAccessDbid, Integer tenantDbid) {
        log.debug("getTableAccess 요청: tableAccessDbid={}, tenantDbid={}", tableAccessDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        TableAccessSummary result = configClient.withConfService(service -> {
            try {
                CfgTableAccessQuery query = new CfgTableAccessQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(tableAccessDbid);
                CfgTableAccess tableAccess = service.retrieveObject(CfgTableAccess.class, query);
                if (tableAccess == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "TableAccess를 찾을 수 없습니다.");
                }
                return toTableAccessSummary(tableAccess);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "TableAccess 조회 실패");
            }
        });
        log.debug("getTableAccess 응답: {}", result);
        return result;
    }

    public List<TableAccessSummary> listTableAccess(Integer tenantDbid) {
        log.debug("listTableAccess 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<TableAccessSummary> result = configClient.withConfService(service -> {
            try {
                CfgTableAccessQuery query = new CfgTableAccessQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgTableAccess> tableAccess = service.retrieveMultipleObjects(CfgTableAccess.class, query);
                List<TableAccessSummary> summaries = new ArrayList<>();
                for (CfgTableAccess ta : safeCollection(tableAccess)) {
                    summaries.add(toTableAccessSummary(ta));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "TableAccess 목록 조회 실패");
            }
        });
        log.debug("listTableAccess 응답: count={}", result == null ? 0 : result.size());
        return result;
    }


    public TreatmentSummary getTreatmentByName(String name, Integer tenantDbid) {
        log.debug("getTableTreatmentByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        TreatmentSummary result = configClient.withConfService(service -> {
            try {
                CfgTreatmentQuery query = new CfgTreatmentQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgTreatment treatment = service.retrieveObject(CfgTreatment.class, query);
                if (treatment == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Treatment을 찾을 수 없습니다.");
                }
                return toTreatmentSummary(treatment);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Treatment 조회 실패");
            }
        });
        log.debug("getTableTreatmentByName 응답: {}", result);
        return result;
    }

    public TreatmentSummary getTreatment(int treatmentDbid, Integer tenantDbid) {
        log.debug("getTreatment 요청: treatmentDbid={}, tenantDbid={}", treatmentDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        TreatmentSummary result = configClient.withConfService(service -> {
            try {
                CfgTreatmentQuery query = new CfgTreatmentQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(treatmentDbid);
                CfgTreatment treatment = service.retrieveObject(CfgTreatment.class, query);
                if (treatment == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Treatment을 찾을 수 없습니다.");
                }
                return toTreatmentSummary(treatment);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Treatment 조회 실패");
            }
        });
        log.debug("getTreatment 응답: {}", result);
        return result;
    }

    public List<TreatmentSummary> listTreatment(Integer tenantDbid) {
        log.debug("listTreatment 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<TreatmentSummary> result = configClient.withConfService(service -> {
            try {
                CfgTreatmentQuery query = new CfgTreatmentQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgTreatment> treatments = service.retrieveMultipleObjects(CfgTreatment.class, query);
                List<TreatmentSummary> summaries = new ArrayList<>();
                for (CfgTreatment treatment : safeCollection(treatments)) {
                    summaries.add(toTreatmentSummary(treatment));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Treatment 목록 조회 실패");
            }
        });
        log.debug("listtreatment 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public TreatmentSummary createTreatment(TreatmentRequest request) {
    log.debug("createTreatment 요청: {}", SensitiveLogMasker.masked(request));
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        TreatmentSummary result = configClient.withConfService(service -> {
            try {
                CfgTreatmentQuery duplicateQuery = new CfgTreatmentQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setName(request.name());
                CfgTreatment existing = service.retrieveObject(CfgTreatment.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 Treatment입니다.");

                CfgTreatment treatment = new CfgTreatment(service);
                treatment.setTenantDBID(resolvedTenant);
                OutboundConfigMutator.applyTreatmentRequest(treatment, request, this::toUserProperties);
                treatment.setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                treatment.save();
                return toTreatmentSummary(treatment);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Treatment 생성 실패");
            }
        });
        log.debug("createTreatment 응답: {}", result);
        return result;
    }

    public TreatmentSummary updateTreatment(int treatmentDbid, TreatmentRequest request) {
    log.debug("updateTreatment 요청: treatmentDbid={}, payload={}", treatmentDbid, SensitiveLogMasker.masked(request));
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        TreatmentSummary result = configClient.withConfService(service -> {
            try {
                CfgTreatmentQuery query = new CfgTreatmentQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(treatmentDbid);
                CfgTreatment treatment = service.retrieveObject(CfgTreatment.class, query);
                if (treatment == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Treatment을 찾을 수 없습니다.");
                }
                OutboundConfigMutator.applyTreatmentRequest(treatment, request, this::toUserProperties);
                treatment.setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                treatment.save();
                return toTreatmentSummary(treatment);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Treatment 수정 실패");
            }
        });
        log.debug("updateTreatment 응답: {}", result);
        return result;
    }

    public void deleteTreatment(int treatmentDbid, Integer tenantDbid) {
        log.debug("deleteTreatment 요청: treatmentDbid={}, tenantDbid={}", treatmentDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        configClient.withConfService(service -> {
            try {
                CfgTreatmentQuery query = new CfgTreatmentQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(treatmentDbid);
                CfgTreatment treatment = service.retrieveObject(CfgTreatment.class, query);
                if (treatment == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Treatment을 찾을 수 없습니다.");
                }
                treatment.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Treatment 삭제 실패");
            }
        });
        log.debug("deleteTreatment 응답: 완료");
    }

    public List<CampaignSummary> listCampaigns(Integer tenantDbid) {
        log.debug("listCampaigns 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<CampaignSummary> result = configClient.withConfService(service -> {
            try {
                CfgCampaignQuery query = new CfgCampaignQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgCampaign> campaigns = service.retrieveMultipleObjects(CfgCampaign.class, query);
                List<CampaignSummary> summaries = new ArrayList<>();
                for (CfgCampaign campaign : safeCollection(campaigns)) {
                    summaries.add(toCampaignSummary(service, campaign));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "캠페인 조회 실패");
            }
        });
        log.debug("listCampaigns 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public List<CampaignGroupSummary> listCampaignGroups(Integer tenantDbid) {
        log.debug("listCampaignGroups 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<CampaignGroupSummary> result = configClient.withConfService(service -> {
            try {
                CfgCampaignGroupQuery query = new CfgCampaignGroupQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgCampaignGroup> groups = service.retrieveMultipleObjects(CfgCampaignGroup.class, query);
                List<CampaignGroupSummary> summaries = new ArrayList<>();
                for (CfgCampaignGroup group : safeCollection(groups)) {
                    summaries.add(toCampaignGroupSummary(group));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "CampaignGroup 조회 실패");
            }
        });
        log.debug("listCampaignGroups 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public CampaignGroupSummary getCampaignGroup(int groupDbid, Integer tenantDbid) {
        log.debug("getCampaignGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        CampaignGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgCampaignGroupQuery query = new CfgCampaignGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(groupDbid);
                CfgCampaignGroup group = service.retrieveObject(CfgCampaignGroup.class, query);
                if (group == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "CampaignGroup을 찾을 수 없습니다.");
                }
                return toCampaignGroupSummary(group);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "CampaignGroup 조회 실패");
            }
        });
        log.debug("getCampaignGroup 응답: {}", result);
        return result;
    }

    public CampaignGroupSummary getCampaignGroupByName(String name, Integer tenantDbid) {
        log.debug("getCampaignGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        CampaignGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgCampaignGroupQuery query = new CfgCampaignGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgCampaignGroup group = service.retrieveObject(CfgCampaignGroup.class, query);
                if (group == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "CampaignGroup을 찾을 수 없습니다.");
                }
                return toCampaignGroupSummary(group);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "CampaignGroup 조회 실패");
            }
        });
        log.debug("getCampaignGroupByName 응답: {}", result);
        return result;
    }

    public CampaignGroupSummary createCampaignGroup(CampaignGroupRequest request) {
    log.debug("createCampaignGroup 요청: {}", SensitiveLogMasker.masked(request));
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        CampaignGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgCampaignGroupQuery duplicateQuery = new CfgCampaignGroupQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setName(request.name());
                CfgCampaignGroup existing = service.retrieveObject(CfgCampaignGroup.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 CampaignGroup입니다.");

                CfgCampaignGroup group = new CfgCampaignGroup(service);
                group.setTenantDBID(resolvedTenant);
                OutboundConfigMutator.applyCampaignGroupRequest(group, request, true, this::toUserProperties);
                group.setState(request.enabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                group.save();
                return toCampaignGroupSummary(group);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "CampaignGroup 생성 실패");
            }
        });
        log.debug("createCampaignGroup 응답: {}", result);
        return result;
    }

    public CampaignGroupSummary updateCampaignGroup(int groupDbid, CampaignGroupRequest request) {
    log.debug("updateCampaignGroup 요청: groupDbid={}, payload={}", groupDbid, SensitiveLogMasker.masked(request));
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        CampaignGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgCampaignGroupQuery query = new CfgCampaignGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(groupDbid);
                CfgCampaignGroup group = service.retrieveObject(CfgCampaignGroup.class, query);
                if (group == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "CampaignGroup을 찾을 수 없습니다.");
                }
                OutboundConfigMutator.applyCampaignGroupRequest(group, request, false, this::toUserProperties);
                group.setState(request.enabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                group.save();
                return toCampaignGroupSummary(group);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "CampaignGroup 수정 실패");
            }
        });
        log.debug("updateCampaignGroup 응답: {}", result);
        return result;
    }

    public void deleteCampaignGroup(int groupDbid, Integer tenantDbid) {
        log.debug("deleteCampaignGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        configClient.withConfService(service -> {
            try {
                CfgCampaignGroupQuery query = new CfgCampaignGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(groupDbid);
                CfgCampaignGroup group = service.retrieveObject(CfgCampaignGroup.class, query);
                if (group == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "CampaignGroup을 찾을 수 없습니다.");
                }
                group.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "CampaignGroup 삭제 실패");
            }
        });
        log.debug("deleteCampaignGroup 응답: 완료");
    }

    public CampaignSummary getCampaign(int campaignDbid, Integer tenantDbid) {
        log.debug("getCampaign 요청: campaignDbid={}, tenantDbid={}", campaignDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        CampaignSummary result = configClient.withConfService(service -> {
            try {
                CfgCampaignQuery query = new CfgCampaignQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(campaignDbid);
                CfgCampaign campaign = service.retrieveObject(CfgCampaign.class, query);
                if (campaign == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "캠페인을 찾을 수 없습니다.");
                }
                return toCampaignSummary(service, campaign);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "캠페인 조회 실패");
            }
        });
        log.debug("getCampaign 응답: {}", result);
        return result;
    }

    public CampaignSummary getCampaignByName(String name, Integer tenantDbid) {
        log.debug("getCampaignByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        CampaignSummary result = configClient.withConfService(service -> {
            try {
                CfgCampaignQuery query = new CfgCampaignQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgCampaign campaign = service.retrieveObject(CfgCampaign.class, query);
                if (campaign == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "캠페인을 찾을 수 없습니다.");
                }
                return toCampaignSummary(service, campaign);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "캠페인 조회 실패");
            }
        });
        log.debug("getCampaignByName 응답: {}", result);
        return result;
    }

    public CampaignSummary createCampaign(CampaignRequest request) {
    log.debug("createCampaign 요청: {}", SensitiveLogMasker.masked(request));
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        CampaignSummary result = configClient.withConfService(service -> {
            try {
                CfgCampaignQuery duplicateQuery = new CfgCampaignQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setName(request.name());
                CfgCampaign existing = service.retrieveObject(CfgCampaign.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 캠페인입니다.");

                CfgCampaign campaign = new CfgCampaign(service);
                campaign.setTenantDBID(resolvedTenant);
                campaign.setName(request.name());
                if (request.description() != null) {
                    campaign.setDescription(request.description());
                }
                if (request.scriptDbid() != null) {
                    campaign.setScriptDBID(request.scriptDbid());
                }
                if (request.userProperties() != null && !request.userProperties().isEmpty()) {
                    campaign.setUserProperties(toUserProperties(request.userProperties()));
                }
                campaign.setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                OutboundConfigScenarioSupport.attachCallingListsIfPresent(service, resolvedTenant, campaign,
                    request.callingListNames());
                campaign.save();
                return toCampaignSummary(service, campaign);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "캠페인 생성 실패");
            }
        });
        log.debug("createCampaign 응답: {}", result);
        return result;
    }

    public OutboundBatchCreateResponse createOutboundBatch(OutboundBatchCreateRequest request) {
    log.debug("createOutboundBatch 요청: {}", SensitiveLogMasker.masked(request));
        if (request == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다.");
        }
        FilterSummary filter = null;
        CallingListDetailSummary callingList = null;
        CampaignSummary campaign = null;
        CampaignGroupSummary campaignGroup = null;
        try {
            filter = createFilter(request.filter());
            CallingListDetailRequest callingListRequest = OutboundBatchRequestDefaults.withFilterDbidIfMissing(request.callingList(), filter.dbid());
            callingList = createCallingList(callingListRequest);
            CampaignRequest campaignRequest = OutboundBatchRequestDefaults.withCallingListIfMissing(request.campaign(), callingList.name());
            campaign = createCampaign(campaignRequest);
            CampaignGroupRequest campaignGroupRequest = OutboundBatchRequestDefaults.withCampaignDbidIfMissing(request.campaignGroup(), campaign.dbid());
            campaignGroup = createCampaignGroup(campaignGroupRequest);
            OutboundBatchCreateResponse response = new OutboundBatchCreateResponse(filter, callingList, campaign, campaignGroup);
            log.debug("createOutboundBatch 응답: {}", response);
            return response;
        } catch (RuntimeException ex) {
            OutboundConfigScenarioSupport.rollbackBatch(
                log,
                request,
                filter,
                callingList,
                campaign,
                campaignGroup,
                this::deleteCampaignGroup,
                this::deleteCampaign,
                this::deleteCallingList,
                this::deleteFilter
            );
            throw ex;
        }
    }

    public OutboundBatchCreateSummary summarizeBatch(OutboundBatchCreateResponse response) {
        if (response == null) {
            return null;
        }
        return new OutboundBatchCreateSummary(
            toIdName(response.filter()),
            toIdName(response.callingList()),
            toIdName(response.campaign()),
            toIdName(response.campaignGroup())
        );
    }

    public CampaignSummary updateCampaign(int campaignDbid, CampaignRequest request) {
    log.debug("updateCampaign 요청: campaignDbid={}, payload={}", campaignDbid, SensitiveLogMasker.masked(request));
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        CampaignSummary result = configClient.withConfService(service -> {
            try {
                CfgCampaignQuery query = new CfgCampaignQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(campaignDbid);
                CfgCampaign campaign = service.retrieveObject(CfgCampaign.class, query);
                if (campaign == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "캠페인을 찾을 수 없습니다.");
                }
                campaign.setName(request.name());
                if (request.description() != null) {
                    campaign.setDescription(request.description());
                }
                if (request.scriptDbid() != null) {
                    campaign.setScriptDBID(request.scriptDbid());
                }
                if (request.userProperties() != null && !request.userProperties().isEmpty()) {
                    campaign.setUserProperties(toUserProperties(request.userProperties()));
                }
                campaign.setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                OutboundConfigScenarioSupport.attachCallingListsIfPresent(service, resolvedTenant, campaign,
                    request.callingListNames());
                campaign.save();
                return toCampaignSummary(service, campaign);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "캠페인 수정 실패");
            }
        });
        log.debug("updateCampaign 응답: {}", result);
        return result;
    }

    public void deleteCampaign(int campaignDbid, Integer tenantDbid) {
        log.debug("deleteCampaign 요청: campaignDbid={}, tenantDbid={}", campaignDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        configClient.withConfService(service -> {
            try {
                CfgCampaignQuery query = new CfgCampaignQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(campaignDbid);
                CfgCampaign campaign = service.retrieveObject(CfgCampaign.class, query);
                if (campaign == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "캠페인을 찾을 수 없습니다.");
                }
                campaign.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "캠페인 삭제 실패");
            }
        });
        log.debug("deleteCampaign 응답: 완료");
    }

    private CampaignGroupSummary toCampaignGroupSummary(CfgCampaignGroup group) {
        return OutboundConfigSummaryMapper.toCampaignGroupSummary(group, CfgValueFormatter::toMap);
    }

    private OutboundBatchCreateSummary.IdName toIdName(FilterSummary summary) {
        return summary == null ? null : new OutboundBatchCreateSummary.IdName(summary.dbid(), summary.name());
    }

    private OutboundBatchCreateSummary.IdName toIdName(CallingListDetailSummary summary) {
        return summary == null ? null : new OutboundBatchCreateSummary.IdName(summary.dbid(), summary.name());
    }

    private OutboundBatchCreateSummary.IdName toIdName(CampaignSummary summary) {
        return summary == null ? null : new OutboundBatchCreateSummary.IdName(summary.dbid(), summary.name());
    }

    private OutboundBatchCreateSummary.IdName toIdName(CampaignGroupSummary summary) {
        return summary == null ? null : new OutboundBatchCreateSummary.IdName(summary.dbid(), summary.name());
    }

    private TableAccessSummary toTableAccessSummary(CfgTableAccess tableAccess) {
        return OutboundConfigSummaryMapper.toTableAccessSummary(tableAccess, CfgValueFormatter::toMap);
    }


    private TreatmentSummary toTreatmentSummary(CfgTreatment treatment) {
        return OutboundConfigSummaryMapper.toTreatmentSummary(treatment, CfgValueFormatter::toMap,
            CfgValueFormatter::toIsoString);
    }

    private FilterSummary toFilterSummary(CfgFilter filter) {
        return OutboundConfigSummaryMapper.toFilterSummary(filter, this::fromUserProperties);
    }

    private FormatSummary toFormatSummary(CfgFormat format) {
        return OutboundConfigSummaryMapper.toFormatSummary(format, this::fromUserProperties);
    }

    private CampaignSummary toCampaignSummary(IConfService service, CfgCampaign campaign) {
        return OutboundCampaignSummaryAssembler.toCampaignSummary(
            log,
            service,
            campaign,
            this::toCallingListDetailSummary,
            this::toCampaignGroupSummary,
            CfgValueFormatter::toMap
        );
    }

    private CallingListDetailSummary toCallingListDetailSummary(CfgCallingList callingList) {
        return OutboundConfigSummaryMapper.toCallingListDetailSummary(callingList, this::fromUserProperties);
    }

}
