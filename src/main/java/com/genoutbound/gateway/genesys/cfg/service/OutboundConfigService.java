package com.genoutbound.gateway.genesys.cfg.service;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingList;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingListInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaign;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaignGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaignGroupInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgField;
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
import com.genesyslab.platform.configuration.protocol.types.CfgDialMode;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.types.CfgOptimizationMethod;
import com.genesyslab.platform.configuration.protocol.types.CfgOperationMode;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CallingListDetailSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignRequest;
import com.genoutbound.gateway.genesys.cfg.dto.CampaignSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FieldSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FilterRequest;
import com.genoutbound.gateway.genesys.cfg.dto.FilterSummary;
import com.genoutbound.gateway.genesys.cfg.dto.FormatSummary;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateRequest;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateResponse;
import com.genoutbound.gateway.genesys.cfg.dto.OutboundBatchCreateSummary;
import com.genoutbound.gateway.genesys.cfg.dto.ServerSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TableAccessSummary;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        log.debug("createFilter 요청: {}", request);
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
                filter.setDescription(request.description());
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
        log.debug("updateFilter 요청: filterDbid={}, request={}", filterDbid, request);
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
                filter.setDescription(request.description());
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
        log.debug("createCallingList 요청: resolvedTenant={}, request={}", resolvedTenant, request);
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
                callingList.setDescription(request.description());
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
        log.debug("updateCallingList 요청: callingListDbid={}, resolvedTenant={}, request={}", callingListDbid, resolvedTenant, request);
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
                callingList.setDescription(request.description());
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
        log.debug("createCampaignGroup 요청: {}", request);
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
                applyCampaignGroupRequest(group, request, true);
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
        log.debug("updateCampaignGroup 요청: groupDbid={}, request={}", groupDbid, request);
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
                applyCampaignGroupRequest(group, request, false);
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
        log.debug("createCampaign 요청: {}", request);
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
                attachCallingListsIfPresent(service, resolvedTenant, campaign, request.callingListNames());
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
        log.debug("createOutboundBatch 요청: {}", request);
        if (request == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다.");
        }
        FilterSummary filter = null;
        CallingListDetailSummary callingList = null;
        CampaignSummary campaign = null;
        CampaignGroupSummary campaignGroup = null;
        try {
            filter = createFilter(request.filter());
            CallingListDetailRequest callingListRequest = withFilterDbidIfMissing(request.callingList(), filter.dbid());
            callingList = createCallingList(callingListRequest);
            CampaignRequest campaignRequest = withCallingListIfMissing(request.campaign(), callingList.name());
            campaign = createCampaign(campaignRequest);
            CampaignGroupRequest campaignGroupRequest = withCampaignDbidIfMissing(request.campaignGroup(), campaign.dbid());
            campaignGroup = createCampaignGroup(campaignGroupRequest);
            OutboundBatchCreateResponse response = new OutboundBatchCreateResponse(filter, callingList, campaign, campaignGroup);
            log.debug("createOutboundBatch 응답: {}", response);
            return response;
        } catch (RuntimeException ex) {
            rollbackBatch(request, filter, callingList, campaign, campaignGroup);
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
        log.debug("updateCampaign 요청: campaignDbid={}, request={}", campaignDbid, request);
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
                attachCallingListsIfPresent(service, resolvedTenant, campaign, request.callingListNames());
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
        for (CfgApplication server : safeCollection(group.getServers())) {
            if (server != null) {
                servers.add(new ServerSummary(server.getDBID(), server.getName()));
            }
        }
        String origDnNumber = group.getOrigDN() == null ? null : group.getOrigDN().getNumber();
        String trunkGroupDnNumber = group.getTrunkGroupDN() == null ? null : group.getTrunkGroupDN().getNumber();
        return new CampaignGroupSummary(group.getDBID(), group.getName(),
                group.getState() == CfgObjectState.CFGEnabled, group.getTenantDBID(),
        campaignDbid, groupDbid, groupType, group.getDescription(), state, dialMode, operationMode, group.getNumOfChannels(),
                optMethod, group.getOptMethodValue(), minRecBuffSize, optRecBuffSize, group.getOrigDNDBID(), group.getTrunkGroupDNDBID(),
                group.getScriptDBID(), group.getInteractionQueueDBID(), group.getIVRProfileDBID(),
                servers, origDnNumber, trunkGroupDnNumber,
                toMap(group.getUserProperties()));
    }

    private CallingListDetailRequest withFilterDbidIfMissing(CallingListDetailRequest request, int filterDbid) {
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

    private CampaignRequest withCallingListIfMissing(CampaignRequest request, String callingListName) {
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

    private CampaignGroupRequest withCampaignDbidIfMissing(CampaignGroupRequest request, int campaignDbid) {
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

    private void rollbackBatch(OutboundBatchCreateRequest request,
                               FilterSummary filter,
                               CallingListDetailSummary callingList,
                               CampaignSummary campaign,
                               CampaignGroupSummary campaignGroup) {
        log.warn("createOutboundBatch 실패. 보상 삭제를 시도합니다.");
        Integer tenantDbid = request.filter() == null ? null : request.filter().tenantDbid();
        if (campaignGroup != null) {
            try {
                deleteCampaignGroup(campaignGroup.dbid(), tenantDbid);
            } catch (RuntimeException rollbackEx) {
                log.warn("CampaignGroup 롤백 실패: groupDbid={}", campaignGroup.dbid(), rollbackEx);
            }
        }
        if (campaign != null) {
            try {
                deleteCampaign(campaign.dbid(), tenantDbid);
            } catch (RuntimeException rollbackEx) {
                log.warn("Campaign 롤백 실패: campaignDbid={}", campaign.dbid(), rollbackEx);
            }
        }
        if (callingList != null) {
            try {
                deleteCallingList(callingList.dbid(), tenantDbid);
            } catch (RuntimeException rollbackEx) {
                log.warn("CallingList 롤백 실패: callingListDbid={}", callingList.dbid(), rollbackEx);
            }
        }
        if (filter != null) {
            try {
                deleteFilter(filter.dbid(), tenantDbid);
            } catch (RuntimeException rollbackEx) {
                log.warn("Filter 롤백 실패: filterDbid={}", filter.dbid(), rollbackEx);
            }
        }
    }

    private TableAccessSummary toTableAccessSummary(CfgTableAccess tableAccess) {
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
            toMap(tableAccess.getUserProperties())
        );
    }

    private FilterSummary toFilterSummary(CfgFilter filter) {
        String formatName = filter.getFormat() == null ? null : filter.getFormat().getName();
        return new FilterSummary(
            filter.getDBID(),
            filter.getName(),
            filter.getDescription(),
            filter.getState() == CfgObjectState.CFGEnabled,
            filter.getFormatDBID(),
            formatName,
            fromUserProperties(filter.getUserProperties())
        );
    }

    private FormatSummary toFormatSummary(CfgFormat format) {
        List<FieldSummary> fieldSummaries = new ArrayList<>();
        for (CfgField field : safeCollection(format.getFields())) {
            fieldSummaries.add(toFieldSummary(field));
        }
        return new FormatSummary(
            format.getDBID(),
            format.getName(),
            format.getDescription(),
            format.getState() == CfgObjectState.CFGEnabled,
            format.getTenantDBID(),
            fieldSummaries,
            fromUserProperties(format.getUserProperties())
        );
    }

    private FieldSummary toFieldSummary(CfgField field) {
        String fieldType = field.getFieldType() == null ? null : field.getFieldType().name();
        return new FieldSummary(
            field.getDBID(),
            field.getName(),
            fieldType,
            field.getDescription()
        );
    }

    private <E> E parseEnum(String value, Class<E> enumType, String fieldName) {
        try {
            if (enumType.isEnum()) {
                @SuppressWarnings({"rawtypes", "unchecked"})
                E result = (E) Enum.valueOf((Class) enumType, value);
                if (result == null) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, fieldName + " 값이 올바르지 않습니다.");
                }
                return result;
            }
            E result = enumType.cast(enumType.getMethod("valueOf", String.class).invoke(null, value));
            if (result == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, fieldName + " 값이 올바르지 않습니다.");
            }
            return result;
        } catch (IllegalArgumentException | ReflectiveOperationException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, fieldName + " 값이 올바르지 않습니다.");
        }
    }

    private void applyCampaignGroupRequest(CfgCampaignGroup group, CampaignGroupRequest request, boolean isCreate) {
        if (isCreate && request.campaignDbid() != null) {
            group.setCampaignDBID(request.campaignDbid());
        }
        if (isCreate && request.groupDbid() != null) {
            group.setGroupDBID(request.groupDbid());
        }
        if (isCreate && request.groupType() != null && !request.groupType().isBlank()) {
            CfgObjectType groupType = parseEnum(request.groupType(), CfgObjectType.class, "groupType");
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
            CfgDialMode dialMode = parseEnum(request.dialMode(), CfgDialMode.class, "dialMode");
            if (dialMode != null) {
                group.setDialMode(dialMode);
            }
        }
        if (request.operationMode() != null && !request.operationMode().isBlank()) {
            CfgOperationMode operationMode = parseEnum(request.operationMode(), CfgOperationMode.class, "operationMode");
            if (operationMode != null) {
                group.setOperationMode(operationMode);
            }
        }
        if (request.numOfChannels() != null) {
            group.setNumOfChannels(request.numOfChannels());
        }
        if (request.optMethod() != null && !request.optMethod().isBlank()) {
            CfgOptimizationMethod optMethod = parseEnum(request.optMethod(), CfgOptimizationMethod.class, "optMethod");
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
            group.setUserProperties(toUserProperties(request.userProperties()));
        }
    }

    private CampaignSummary toCampaignSummary(IConfService service, CfgCampaign campaign) {
        List<CallingListDetailSummary> callingLists = new ArrayList<>();
        for (CfgCallingListInfo info : safeCollection(campaign.getCallingLists())) {
            CallingListDetailSummary summary = resolveCallingListSummary(service, campaign, info);
            if (summary != null) {
                callingLists.add(summary);
            }
        }
        List<CampaignGroupSummary> campaignGroups = resolveCampaignGroups(service, campaign);
        String state = campaign.getState() == null ? null : campaign.getState().name();
        return new CampaignSummary(campaign.getDBID(), campaign.getName(), campaign.getDescription(),
                campaign.getState() == CfgObjectState.CFGEnabled, campaign.getTenantDBID(),
                campaign.getScriptDBID(), state, callingLists,
                toMap(campaign.getUserProperties()),
                campaignGroups);
    }

    private CallingListDetailSummary resolveCallingListSummary(IConfService service, CfgCampaign campaign, CfgCallingListInfo info) {
        CfgCallingList callingList = info.getCallingList();
        if (callingList != null) {
            return toCallingListDetailSummary(callingList);
        }
        if (info.getCallingListDBID() == null) {
            return null;
        }
        try {
            CfgCallingListQuery query = new CfgCallingListQuery();
            query.setTenantDbid(campaign.getTenantDBID());
            query.setDbid(info.getCallingListDBID());
            CfgCallingList fetched = service.retrieveObject(CfgCallingList.class, query);
            return fetched == null ? null : toCallingListDetailSummary(fetched);
        } catch (ConfigException ex) {
            log.warn("CallingList 상세 조회 실패: callingListDbid={}", info.getCallingListDBID(), ex);
            return null;
        }
    }

    private CampaignGroupSummary resolveCampaignGroupSummary(IConfService service, CfgCampaign campaign, CfgCampaignGroupInfo info) {
        if (info.getGroupDBID() == null) {
            return null;
        }
        try {
            CfgCampaignGroupQuery query = new CfgCampaignGroupQuery();
            query.setTenantDbid(campaign.getTenantDBID());
            query.setDbid(info.getGroupDBID());
            CfgCampaignGroup fetched = service.retrieveObject(CfgCampaignGroup.class, query);
            return fetched == null ? null : toCampaignGroupSummary(fetched);
        } catch (ConfigException ex) {
            log.warn("CampaignGroup 상세 조회 실패: groupDbid={}", info.getGroupDBID(), ex);
            return null;
        }
    }

    private List<CampaignGroupSummary> resolveCampaignGroups(IConfService service, CfgCampaign campaign) {
        List<CampaignGroupSummary> campaignGroups = new ArrayList<>();
        List<CfgCampaignGroupInfo> groupInfos = new ArrayList<>(safeCollection(campaign.getCampaignGroups()));
        if (groupInfos.isEmpty()) {
            log.debug("CampaignGroupInfo 비어있음: campaignDbid={}", campaign.getDBID());
        } else {
            for (CfgCampaignGroupInfo info : groupInfos) {
                CampaignGroupSummary summary = resolveCampaignGroupSummary(service, campaign, info);
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
                campaignGroups.add(toCampaignGroupSummary(group));
            }
        } catch (ConfigException | InterruptedException ex) {
            log.warn("CampaignGroup 목록 조회 실패: campaignDbid={}", campaign.getDBID(), ex);
        }
        return campaignGroups;
    }

    private Map<String, Object> toMap(KeyValueCollection collection) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (collection == null) {
            return result;
        }
        for (Object obj : collection) {
            if (!(obj instanceof KeyValuePair pair)) {
                continue;
            }
            Object value = pair.getValue();
            if (value instanceof KeyValueCollection nested) {
                result.put(pair.getStringKey(), toMap(nested));
            } else {
                result.put(pair.getStringKey(), value);
            }
        }
        return result;
    }

    private CallingListDetailSummary toCallingListDetailSummary(CfgCallingList callingList) {
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
                fromUserProperties(callingList.getUserProperties())
        );
    }

    @SuppressWarnings("unused")
    private void attachTableAccessIfPresent(IConfService service, int tenantDbid, CfgCallingList list, String tableName)
            throws ConfigException {
        if (tableName == null || tableName.isBlank()) {
            return;
        }
        CfgTableAccessQuery query = new CfgTableAccessQuery();
        query.setTenantDbid(tenantDbid);
        query.setName(tableName);
        CfgTableAccess tableAccess = service.retrieveObject(CfgTableAccess.class, query);
        if (tableAccess == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "TableAccess를 찾을 수 없습니다.");
        }
        list.setTableAccess(tableAccess);
    }

    @SuppressWarnings("unused")
    private void attachTreatmentsIfPresent(IConfService service, int tenantDbid, CfgCallingList list,
                                           List<String> treatmentNames) throws ConfigException {
        if (treatmentNames == null || treatmentNames.isEmpty()) {
            return;
        }
        Collection<CfgTreatment> treatments = new HashSet<>();
        for (String name : treatmentNames) {
            if (name == null || name.isBlank()) {
                continue;
            }
            CfgTreatmentQuery query = new CfgTreatmentQuery();
            query.setTenantDbid(tenantDbid);
            query.setName(name);
            CfgTreatment treatment = service.retrieveObject(CfgTreatment.class, query);
            if (treatment != null) {
                treatments.add(treatment);
            }
        }
        list.setTreatments(treatments);
    }

    private void attachCallingListsIfPresent(IConfService service, int tenantDbid, CfgCampaign campaign,
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
