package com.genoutbound.gateway.genesys.cfg.service;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDN;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDNGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPlace;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPlaceGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTransaction;
import com.genesyslab.platform.applicationblocks.com.queries.CfgDNGroupQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgDNQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPlaceGroupQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPlaceQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgTransactionQuery;
import com.genesyslab.platform.configuration.protocol.types.CfgDNRegisterFlag;
import com.genesyslab.platform.configuration.protocol.types.CfgDNType;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genesyslab.platform.configuration.protocol.types.CfgRouteType;
import com.genesyslab.platform.configuration.protocol.types.CfgTransactionType;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.genesys.cfg.dto.DnDialPlanRequest;
import com.genoutbound.gateway.genesys.cfg.dto.DnGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.DnGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.DnRequest;
import com.genoutbound.gateway.genesys.cfg.dto.DnSummary;
import com.genoutbound.gateway.genesys.cfg.dto.DnTServerOptionRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceGroupSummary;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PlaceSummary;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionOptionRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionOptionsSaveRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionSectionRequest;
import com.genoutbound.gateway.genesys.cfg.dto.TransactionSummary;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * DN/Transaction/Place 관련 설정을 담당합니다.
 */
@Service
public class RoutingConfigService extends GenesysConfigSupport {

    private static final Logger log = LoggerFactory.getLogger(RoutingConfigService.class);

    public RoutingConfigService(GenesysConfigClient configClient) {
        super(configClient);
    }

    public List<DnSummary> listDns(Integer tenantDbid) {
        log.debug("listDns 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<DnSummary> result = configClient.withConfService(service -> {
            try {
                CfgDNQuery query = new CfgDNQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgDN> dns = service.retrieveMultipleObjects(CfgDN.class, query);
                List<DnSummary> summaries = new ArrayList<>();
                for (CfgDN dn : safeCollection(dns)) {
                    summaries.add(new DnSummary(dn.getDBID(), dn.getNumber(), dn.getName(),
                            dn.getType().name(), dn.getState() == CfgObjectState.CFGEnabled));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DN 조회 실패");
            }
        });
        log.debug("listDns 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public List<DnGroupSummary> listDnGroups(Integer tenantDbid) {
        log.debug("listDnGroups 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<DnGroupSummary> result = configClient.withConfService(service -> {
            try {
                CfgDNGroupQuery query = new CfgDNGroupQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgDNGroup> groups = service.retrieveMultipleObjects(CfgDNGroup.class, query);
                List<DnGroupSummary> summaries = new ArrayList<>();
                for (CfgDNGroup group : safeCollection(groups)) {
                    summaries.add(new DnGroupSummary(group.getDBID(), group.getGroupInfo().getName(), null));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DNGroup 조회 실패");
            }
        });
        log.debug("listDnGroups 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public DnGroupSummary getDnGroup(int groupDbid, Integer tenantDbid) {
        log.debug("getDnGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        DnGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgDNGroupQuery query = new CfgDNGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(groupDbid);
                CfgDNGroup group = service.retrieveObject(CfgDNGroup.class, query);
                if (group == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "DNGroup을 찾을 수 없습니다.");
                }
                return new DnGroupSummary(group.getDBID(), group.getGroupInfo().getName(), null);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DNGroup 조회 실패");
            }
        });
        log.debug("getDnGroup 응답: {}", result);
        return result;
    }

    public DnGroupSummary getDnGroupByName(String name, Integer tenantDbid) {
        log.debug("getDnGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        DnGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgDNGroupQuery query = new CfgDNGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgDNGroup group = service.retrieveObject(CfgDNGroup.class, query);
                if (group == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "DNGroup을 찾을 수 없습니다.");
                }
                return new DnGroupSummary(group.getDBID(), group.getGroupInfo().getName(), null);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DNGroup 조회 실패");
            }
        });
        log.debug("getDnGroupByName 응답: {}", result);
        return result;
    }

    public DnGroupSummary createDnGroup(DnGroupRequest request) {
        log.debug("createDnGroup 요청: {}", request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        DnGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgDNGroupQuery duplicateQuery = new CfgDNGroupQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setName(request.name());
                CfgDNGroup existing = service.retrieveObject(CfgDNGroup.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 DNGroup입니다.");

                CfgDNGroup group = new CfgDNGroup(service);
                CfgGroup groupInfo = new CfgGroup(service, null);
                groupInfo.setTenantDBID(resolvedTenant);
                groupInfo.setName(request.name());
                group.setGroupInfo(groupInfo);
                group.save();
                return new DnGroupSummary(group.getDBID(), group.getGroupInfo().getName(), null);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DNGroup 생성 실패");
            }
        });
        log.debug("createDnGroup 응답: {}", result);
        return result;
    }

    public void deleteDnGroup(int groupDbid, Integer tenantDbid) {
        log.debug("deleteDnGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        configClient.withConfService(service -> {
            try {
                CfgDNGroupQuery query = new CfgDNGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(groupDbid);
                CfgDNGroup group = service.retrieveObject(CfgDNGroup.class, query);
                if (group == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "DNGroup을 찾을 수 없습니다.");
                }
                group.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DNGroup 삭제 실패");
            }
        });
        log.debug("deleteDnGroup 응답: 완료");
    }

    public DnSummary getDn(int dnDbid, Integer tenantDbid) {
        log.debug("getDn 요청: dnDbid={}, tenantDbid={}", dnDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        DnSummary result = configClient.withConfService(service -> {
            try {
                CfgDNQuery query = new CfgDNQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(dnDbid);
                CfgDN dn = service.retrieveObject(CfgDN.class, query);
                if (dn == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "DN을 찾을 수 없습니다.");
                }
                return new DnSummary(dn.getDBID(), dn.getNumber(), dn.getName(), dn.getType().name(),
                        dn.getState() == CfgObjectState.CFGEnabled);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DN 조회 실패");
            }
        });
        log.debug("getDn 응답: {}", result);
        return result;
    }

    public DnSummary getDnByName(String name, Integer tenantDbid) {
        log.debug("getDnByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        DnSummary result = configClient.withConfService(service -> {
            try {
                CfgDNQuery query = new CfgDNQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgDN dn = service.retrieveObject(CfgDN.class, query);
                if (dn == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "DN을 찾을 수 없습니다.");
                }
                return new DnSummary(dn.getDBID(), dn.getNumber(), dn.getName(), dn.getType().name(),
                        dn.getState() == CfgObjectState.CFGEnabled);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DN 조회 실패");
            }
        });
        log.debug("getDnByName 응답: {}", result);
        return result;
    }

    public DnSummary createDn(DnRequest request) {
        log.debug("createDn 요청: {}", request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        int switchDbid = resolveSwitchDbid(request.switchDbid());
        DnSummary result = configClient.withConfService(service -> {
            try {
                CfgDNQuery duplicateQuery = new CfgDNQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setSwitchDbid(switchDbid);
                duplicateQuery.setName(request.name() != null ? request.name() : request.number());
                CfgDN existing = service.retrieveObject(CfgDN.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 DN입니다.");

                CfgDN dn = new CfgDN(service);
                dn.setTenantDBID(resolvedTenant);
                dn.setSwitchDBID(switchDbid);
                dn.setNumber(request.number());
                if (request.name() != null) {
                    dn.setName(request.name());
                }
                dn.setType(CfgDNType.valueOf(request.type()));
                dn.setRegisterAll(CfgDNRegisterFlag.valueOf(request.registerFlag()));
                dn.setRouteType(CfgRouteType.valueOf(request.routeType()));
                if (request.switchSpecificType() != null) {
                    dn.setSwitchSpecificType(request.switchSpecificType());
                }
                if (request.trunks() != null) {
                    dn.setTrunks(request.trunks());
                }
                dn.setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                dn.save();
                return new DnSummary(dn.getDBID(), dn.getNumber(), dn.getName(), dn.getType().name(),
                        dn.getState() == CfgObjectState.CFGEnabled);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DN 생성 실패");
            }
        });
        log.debug("createDn 응답: {}", result);
        return result;
    }

    public DnSummary updateDn(int dnDbid, DnRequest request) {
        log.debug("updateDn 요청: dnDbid={}, request={}", dnDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        DnSummary result = configClient.withConfService(service -> {
            try {
                CfgDNQuery query = new CfgDNQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(dnDbid);
                CfgDN dn = service.retrieveObject(CfgDN.class, query);
                if (dn == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "DN을 찾을 수 없습니다.");
                }
                dn.setNumber(request.number());
                dn.setName(request.name());
                dn.setType(CfgDNType.valueOf(request.type()));
                dn.setRegisterAll(CfgDNRegisterFlag.valueOf(request.registerFlag()));
                dn.setRouteType(CfgRouteType.valueOf(request.routeType()));
                if (request.switchSpecificType() != null) {
                    dn.setSwitchSpecificType(request.switchSpecificType());
                }
                if (request.trunks() != null) {
                    dn.setTrunks(request.trunks());
                }
                dn.setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                dn.save();
                return new DnSummary(dn.getDBID(), dn.getNumber(), dn.getName(), dn.getType().name(),
                        dn.getState() == CfgObjectState.CFGEnabled);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DN 수정 실패");
            }
        });
        log.debug("updateDn 응답: {}", result);
        return result;
    }

    public void deleteDn(int dnDbid, Integer tenantDbid) {
        log.debug("deleteDn 요청: dnDbid={}, tenantDbid={}", dnDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        configClient.withConfService(service -> {
            try {
                CfgDNQuery query = new CfgDNQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(dnDbid);
                CfgDN dn = service.retrieveObject(CfgDN.class, query);
                if (dn == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "DN을 찾을 수 없습니다.");
                }
                dn.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DN 삭제 실패");
            }
        });
        log.debug("deleteDn 응답: 완료");
    }

    public void setDnDialPlan(int dnDbid, DnDialPlanRequest request) {
        log.debug("setDnDialPlan 요청: dnDbid={}, request={}", dnDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgDNQuery query = new CfgDNQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(dnDbid);
                CfgDN dn = service.retrieveObject(CfgDN.class, query);
                if (dn == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "DN을 찾을 수 없습니다.");
                }
                if (Boolean.TRUE.equals(request.enabled())
                        && (request.dialPlanName() == null || request.dialPlanName().isBlank())) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "DialPlan 이름이 필요합니다.");
                }
                KeyValueCollection props = dn.getUserProperties();
                if (props == null) {
                    props = new KeyValueCollection();
                }
                KeyValuePair tserverPair = props.getPair("TServer");
                KeyValueCollection tserverOptions = tserverPair != null
                        ? tserverPair.getTKVValue()
                        : new KeyValueCollection();
                tserverOptions.remove("dial-plan");
                if (Boolean.TRUE.equals(request.enabled())) {
                    tserverOptions.addString("dial-plan", request.dialPlanName());
                }
                props.remove("TServer");
                props.addPair(new KeyValuePair("TServer", tserverOptions));
                dn.setUserProperties(props);
                dn.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DN DialPlan 설정 실패");
            }
        });
        log.debug("setDnDialPlan 응답: 완료");
    }

    public void setDnTServerOptions(int dnDbid, DnTServerOptionRequest request) {
        log.debug("setDnTServerOptions 요청: dnDbid={}, request={}", dnDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgDNQuery query = new CfgDNQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(dnDbid);
                CfgDN dn = service.retrieveObject(CfgDN.class, query);
                if (dn == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "DN을 찾을 수 없습니다.");
                }
                KeyValueCollection props = dn.getUserProperties();
                if (props == null) {
                    props = new KeyValueCollection();
                }
                KeyValueCollection tserverOptions = new KeyValueCollection();
                if (request.options() != null) {
                    for (Map.Entry<String, String> entry : request.options().entrySet()) {
                        if (entry.getKey() == null || entry.getKey().isBlank()) {
                            continue;
                        }
                        tserverOptions.addString(entry.getKey(), entry.getValue());
                    }
                }
                props.remove("TServer");
                props.addPair(new KeyValuePair("TServer", tserverOptions));
                dn.setUserProperties(props);
                dn.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "DN TServer 옵션 설정 실패");
            }
        });
        log.debug("setDnTServerOptions 응답: 완료");
    }

    public List<TransactionSummary> listTransactions(Integer tenantDbid) {
        log.debug("listTransactions 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<TransactionSummary> result = configClient.withConfService(service -> {
            try {
                CfgTransactionQuery query = new CfgTransactionQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgTransaction> transactions = service.retrieveMultipleObjects(CfgTransaction.class, query);
                List<TransactionSummary> summaries = new ArrayList<>();
                for (CfgTransaction transaction : safeCollection(transactions)) {
                    summaries.add(new TransactionSummary(transaction.getDBID(), transaction.getName(),
                            transaction.getAlias(), transaction.getType().name(),
                            transaction.getState() == CfgObjectState.CFGEnabled));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 조회 실패");
            }
        });
        log.debug("listTransactions 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public TransactionSummary getTransaction(int transactionDbid, Integer tenantDbid) {
        log.debug("getTransaction 요청: transactionDbid={}, tenantDbid={}", transactionDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        TransactionSummary result = configClient.withConfService(service -> {
            try {
                CfgTransactionQuery query = new CfgTransactionQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(transactionDbid);
                CfgTransaction transaction = service.retrieveObject(CfgTransaction.class, query);
                if (transaction == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "트랜잭션을 찾을 수 없습니다.");
                }
                return new TransactionSummary(transaction.getDBID(), transaction.getName(), transaction.getAlias(),
                        transaction.getType().name(), transaction.getState() == CfgObjectState.CFGEnabled);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 조회 실패");
            }
        });
        log.debug("getTransaction 응답: {}", result);
        return result;
    }

    public TransactionSummary getTransactionByName(String name, Integer tenantDbid) {
        log.debug("getTransactionByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        TransactionSummary result = configClient.withConfService(service -> {
            try {
                CfgTransactionQuery query = new CfgTransactionQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgTransaction transaction = service.retrieveObject(CfgTransaction.class, query);
                if (transaction == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "트랜잭션을 찾을 수 없습니다.");
                }
                return new TransactionSummary(transaction.getDBID(), transaction.getName(), transaction.getAlias(),
                        transaction.getType().name(), transaction.getState() == CfgObjectState.CFGEnabled);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 조회 실패");
            }
        });
        log.debug("getTransactionByName 응답: {}", result);
        return result;
    }

    public TransactionSummary createTransaction(TransactionRequest request) {
        log.debug("createTransaction 요청: {}", request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        TransactionSummary result = configClient.withConfService(service -> {
            try {
                CfgTransactionQuery duplicateQuery = new CfgTransactionQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setName(request.name());
                CfgTransaction existing = service.retrieveObject(CfgTransaction.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 트랜잭션입니다.");

                CfgTransaction transaction = new CfgTransaction(service);
                transaction.setTenantDBID(resolvedTenant);
                transaction.setName(request.name());
                transaction.setAlias(request.alias());
                transaction.setType(CfgTransactionType.valueOf(request.type()));
                transaction.setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                transaction.save();
                return new TransactionSummary(transaction.getDBID(), transaction.getName(), transaction.getAlias(),
                        transaction.getType().name(), transaction.getState() == CfgObjectState.CFGEnabled);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 생성 실패");
            }
        });
        log.debug("createTransaction 응답: {}", result);
        return result;
    }

    public TransactionSummary updateTransaction(int transactionDbid, TransactionRequest request) {
        log.debug("updateTransaction 요청: transactionDbid={}, request={}", transactionDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        TransactionSummary result = configClient.withConfService(service -> {
            try {
                CfgTransactionQuery query = new CfgTransactionQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(transactionDbid);
                CfgTransaction transaction = service.retrieveObject(CfgTransaction.class, query);
                if (transaction == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "트랜잭션을 찾을 수 없습니다.");
                }
                transaction.setName(request.name());
                transaction.setAlias(request.alias());
                transaction.setType(CfgTransactionType.valueOf(request.type()));
                transaction.setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                transaction.save();
                return new TransactionSummary(transaction.getDBID(), transaction.getName(), transaction.getAlias(),
                        transaction.getType().name(), transaction.getState() == CfgObjectState.CFGEnabled);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 수정 실패");
            }
        });
        log.debug("updateTransaction 응답: {}", result);
        return result;
    }

    public void deleteTransaction(int transactionDbid, Integer tenantDbid) {
        log.debug("deleteTransaction 요청: transactionDbid={}, tenantDbid={}", transactionDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        configClient.withConfService(service -> {
            try {
                CfgTransactionQuery query = new CfgTransactionQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(transactionDbid);
                CfgTransaction transaction = service.retrieveObject(CfgTransaction.class, query);
                if (transaction == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "트랜잭션을 찾을 수 없습니다.");
                }
                transaction.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 삭제 실패");
            }
        });
        log.debug("deleteTransaction 응답: 완료");
    }

    public void addTransactionSection(int transactionDbid, TransactionSectionRequest request) {
        log.debug("addTransactionSection 요청: transactionDbid={}, request={}", transactionDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgTransaction transaction = getTransactionByDbid(service, resolvedTenant, transactionDbid);
                KeyValueCollection props = ensureTransactionProperties(transaction);
                if (props.getPair(request.sectionName()) != null) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "이미 존재하는 섹션입니다.");
                }
                props.addPair(new KeyValuePair(request.sectionName(), new KeyValueCollection()));
                transaction.setUserProperties(props);
                transaction.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 섹션 추가 실패");
            }
        });
        log.debug("addTransactionSection 응답: 완료");
    }

    public void modifyTransactionSection(int transactionDbid, TransactionSectionRequest request) {
        log.debug("modifyTransactionSection 요청: transactionDbid={}, request={}", transactionDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgTransaction transaction = getTransactionByDbid(service, resolvedTenant, transactionDbid);
                KeyValueCollection props = ensureTransactionProperties(transaction);
                KeyValuePair sectionPair = props.getPair(request.sectionName());
                if (sectionPair == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "섹션을 찾을 수 없습니다.");
                }
                String newName = request.changeSectionName() == null || request.changeSectionName().isBlank()
                        ? request.sectionName()
                        : request.changeSectionName();
                props.remove(request.sectionName());
                props.addPair(new KeyValuePair(newName, sectionPair.getTKVValue()));
                transaction.setUserProperties(props);
                transaction.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 섹션 수정 실패");
            }
        });
        log.debug("modifyTransactionSection 응답: 완료");
    }

    public void removeTransactionSection(int transactionDbid, TransactionSectionRequest request) {
        log.debug("removeTransactionSection 요청: transactionDbid={}, request={}", transactionDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgTransaction transaction = getTransactionByDbid(service, resolvedTenant, transactionDbid);
                KeyValueCollection props = ensureTransactionProperties(transaction);
                props.remove(request.sectionName());
                transaction.setUserProperties(props);
                transaction.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 섹션 삭제 실패");
            }
        });
        log.debug("removeTransactionSection 응답: 완료");
    }

    public void addTransactionOption(int transactionDbid, TransactionOptionRequest request) {
        log.debug("addTransactionOption 요청: transactionDbid={}, request={}", transactionDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgTransaction transaction = getTransactionByDbid(service, resolvedTenant, transactionDbid);
                KeyValueCollection props = ensureTransactionProperties(transaction);
                upsertTransactionOption(props, request.sectionName(), request.key(), request.value(),
                        request.dataType(), request.alias(), false, null);
                transaction.setUserProperties(props);
                transaction.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 옵션 추가 실패");
            }
        });
        log.debug("addTransactionOption 응답: 완료");
    }

    public void modifyTransactionOption(int transactionDbid, TransactionOptionRequest request) {
        log.debug("modifyTransactionOption 요청: transactionDbid={}, request={}", transactionDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgTransaction transaction = getTransactionByDbid(service, resolvedTenant, transactionDbid);
                KeyValueCollection props = ensureTransactionProperties(transaction);
                upsertTransactionOption(props, request.sectionName(), request.key(), request.value(),
                        request.dataType(), request.alias(), true, request.changeKey());
                transaction.setUserProperties(props);
                transaction.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 옵션 수정 실패");
            }
        });
        log.debug("modifyTransactionOption 응답: 완료");
    }

    public void removeTransactionOption(int transactionDbid, TransactionOptionRequest request) {
        log.debug("removeTransactionOption 요청: transactionDbid={}, request={}", transactionDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgTransaction transaction = getTransactionByDbid(service, resolvedTenant, transactionDbid);
                KeyValueCollection props = ensureTransactionProperties(transaction);
                removeTransactionOptionFromProps(props, request.sectionName(), request.key());
                transaction.setUserProperties(props);
                transaction.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 옵션 삭제 실패");
            }
        });
        log.debug("removeTransactionOption 응답: 완료");
    }

    public void saveTransactionOptions(int transactionDbid, TransactionOptionsSaveRequest request) {
        log.debug("saveTransactionOptions 요청: transactionDbid={}, request={}", transactionDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgTransaction transaction = getTransactionByDbid(service, resolvedTenant, transactionDbid);
                KeyValueCollection props = ensureTransactionProperties(transaction);
                KeyValueCollection options = new KeyValueCollection();
                if (request.options() != null) {
                    for (Map.Entry<String, String> entry : request.options().entrySet()) {
                        if (entry.getKey() == null || entry.getKey().isBlank()) {
                            continue;
                        }
                        options.addString(entry.getKey(), entry.getValue());
                    }
                }
                props.remove(request.sectionName());
                props.addPair(new KeyValuePair(request.sectionName(), options));
                transaction.setUserProperties(props);
                transaction.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 옵션 저장 실패");
            }
        });
        log.debug("saveTransactionOptions 응답: 완료");
    }

    public List<PlaceSummary> listPlaces(Integer tenantDbid) {
        log.debug("listPlaces 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<PlaceSummary> result = configClient.withConfService(service -> {
            try {
                CfgPlaceQuery query = new CfgPlaceQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgPlace> places = service.retrieveMultipleObjects(CfgPlace.class, query);
                List<PlaceSummary> summaries = new ArrayList<>();
                for (CfgPlace place : safeCollection(places)) {
                    int dnCount = place.getDNs() == null ? 0 : place.getDNs().size();
                    summaries.add(new PlaceSummary(place.getDBID(), place.getName(), dnCount));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Place 조회 실패");
            }
        });
        log.debug("listPlaces 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public List<PlaceGroupSummary> listPlaceGroups(Integer tenantDbid) {
        log.debug("listPlaceGroups 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<PlaceGroupSummary> result = configClient.withConfService(service -> {
            try {
                CfgPlaceGroupQuery query = new CfgPlaceGroupQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgPlaceGroup> groups = service.retrieveMultipleObjects(CfgPlaceGroup.class, query);
                List<PlaceGroupSummary> summaries = new ArrayList<>();
                for (CfgPlaceGroup group : safeCollection(groups)) {
                    summaries.add(new PlaceGroupSummary(group.getDBID(), group.getGroupInfo().getName()));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "PlaceGroup 조회 실패");
            }
        });
        log.debug("listPlaceGroups 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public PlaceGroupSummary getPlaceGroup(int groupDbid, Integer tenantDbid) {
        log.debug("getPlaceGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        PlaceGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgPlaceGroupQuery query = new CfgPlaceGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(groupDbid);
                CfgPlaceGroup group = service.retrieveObject(CfgPlaceGroup.class, query);
                if (group == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "PlaceGroup을 찾을 수 없습니다.");
                }
                return new PlaceGroupSummary(group.getDBID(), group.getGroupInfo().getName());
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "PlaceGroup 조회 실패");
            }
        });
        log.debug("getPlaceGroup 응답: {}", result);
        return result;
    }

    public PlaceGroupSummary getPlaceGroupByName(String name, Integer tenantDbid) {
        log.debug("getPlaceGroupByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        PlaceGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgPlaceGroupQuery query = new CfgPlaceGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgPlaceGroup group = service.retrieveObject(CfgPlaceGroup.class, query);
                if (group == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "PlaceGroup을 찾을 수 없습니다.");
                }
                return new PlaceGroupSummary(group.getDBID(), group.getGroupInfo().getName());
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "PlaceGroup 조회 실패");
            }
        });
        log.debug("getPlaceGroupByName 응답: {}", result);
        return result;
    }

    public PlaceGroupSummary createPlaceGroup(PlaceGroupRequest request) {
        log.debug("createPlaceGroup 요청: {}", request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        PlaceGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgPlaceGroupQuery duplicateQuery = new CfgPlaceGroupQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setName(request.name());
                CfgPlaceGroup existing = service.retrieveObject(CfgPlaceGroup.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 PlaceGroup입니다.");

                CfgPlaceGroup group = new CfgPlaceGroup(service);
                CfgGroup groupInfo = new CfgGroup(service, null);
                groupInfo.setTenantDBID(resolvedTenant);
                groupInfo.setName(request.name());
                group.setGroupInfo(groupInfo);
                group.save();
                return new PlaceGroupSummary(group.getDBID(), group.getGroupInfo().getName());
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "PlaceGroup 생성 실패");
            }
        });
        log.debug("createPlaceGroup 응답: {}", result);
        return result;
    }

    public void deletePlaceGroup(int groupDbid, Integer tenantDbid) {
        log.debug("deletePlaceGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        configClient.withConfService(service -> {
            try {
                CfgPlaceGroupQuery query = new CfgPlaceGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(groupDbid);
                CfgPlaceGroup group = service.retrieveObject(CfgPlaceGroup.class, query);
                if (group == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "PlaceGroup을 찾을 수 없습니다.");
                }
                group.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "PlaceGroup 삭제 실패");
            }
        });
        log.debug("deletePlaceGroup 응답: 완료");
    }

    public PlaceSummary getPlace(int placeDbid, Integer tenantDbid) {
        log.debug("getPlace 요청: placeDbid={}, tenantDbid={}", placeDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        PlaceSummary result = configClient.withConfService(service -> {
            try {
                CfgPlaceQuery query = new CfgPlaceQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(placeDbid);
                CfgPlace place = service.retrieveObject(CfgPlace.class, query);
                if (place == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Place를 찾을 수 없습니다.");
                }
                int dnCount = place.getDNs() == null ? 0 : place.getDNs().size();
                return new PlaceSummary(place.getDBID(), place.getName(), dnCount);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Place 조회 실패");
            }
        });
        log.debug("getPlace 응답: {}", result);
        return result;
    }

    public PlaceSummary getPlaceByName(String name, Integer tenantDbid) {
        log.debug("getPlaceByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        PlaceSummary result = configClient.withConfService(service -> {
            try {
                CfgPlaceQuery query = new CfgPlaceQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                CfgPlace place = service.retrieveObject(CfgPlace.class, query);
                if (place == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Place를 찾을 수 없습니다.");
                }
                int dnCount = place.getDNs() == null ? 0 : place.getDNs().size();
                return new PlaceSummary(place.getDBID(), place.getName(), dnCount);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Place 조회 실패");
            }
        });
        log.debug("getPlaceByName 응답: {}", result);
        return result;
    }

    public PlaceSummary createPlace(PlaceRequest request) {
        log.debug("createPlace 요청: {}", request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        int switchDbid = resolveSwitchDbid(request.switchDbid());
        PlaceSummary result = configClient.withConfService(service -> {
            try {
                CfgPlaceQuery duplicateQuery = new CfgPlaceQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setName(request.name());
                CfgPlace existing = service.retrieveObject(CfgPlace.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 Place입니다.");

                CfgPlace place = new CfgPlace(service);
                place.setTenantDBID(resolvedTenant);
                place.setName(request.name());
                place.setDNs(resolveDns(service, resolvedTenant, switchDbid, request.dnNumbers()));
                place.save();
                int dnCount = place.getDNs() == null ? 0 : place.getDNs().size();
                return new PlaceSummary(place.getDBID(), place.getName(), dnCount);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Place 생성 실패");
            }
        });
        log.debug("createPlace 응답: {}", result);
        return result;
    }

    public PlaceSummary updatePlace(int placeDbid, PlaceRequest request) {
        log.debug("updatePlace 요청: placeDbid={}, request={}", placeDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        int switchDbid = resolveSwitchDbid(request.switchDbid());
        PlaceSummary result = configClient.withConfService(service -> {
            try {
                CfgPlaceQuery query = new CfgPlaceQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(placeDbid);
                CfgPlace place = service.retrieveObject(CfgPlace.class, query);
                if (place == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Place를 찾을 수 없습니다.");
                }
                place.setName(request.name());
                place.setDNs(resolveDns(service, resolvedTenant, switchDbid, request.dnNumbers()));
                place.save();
                int dnCount = place.getDNs() == null ? 0 : place.getDNs().size();
                return new PlaceSummary(place.getDBID(), place.getName(), dnCount);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Place 수정 실패");
            }
        });
        log.debug("updatePlace 응답: {}", result);
        return result;
    }

    public void deletePlace(int placeDbid, Integer tenantDbid) {
        log.debug("deletePlace 요청: placeDbid={}, tenantDbid={}", placeDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        configClient.withConfService(service -> {
            try {
                CfgPlaceQuery query = new CfgPlaceQuery();
                query.setTenantDbid(resolvedTenant);
                query.setDbid(placeDbid);
                CfgPlace place = service.retrieveObject(CfgPlace.class, query);
                if (place == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "Place를 찾을 수 없습니다.");
                }
                place.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Place 삭제 실패");
            }
        });
        log.debug("deletePlace 응답: 완료");
    }

    private CfgTransaction getTransactionByDbid(com.genesyslab.platform.applicationblocks.com.IConfService service,
            int tenantDbid, int transactionDbid) throws ConfigException {
        CfgTransactionQuery query = new CfgTransactionQuery();
        query.setTenantDbid(tenantDbid);
        query.setDbid(transactionDbid);
        CfgTransaction transaction = service.retrieveObject(CfgTransaction.class, query);
        if (transaction == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "트랜잭션을 찾을 수 없습니다.");
        }
        return transaction;
    }

    private KeyValueCollection ensureTransactionProperties(CfgTransaction transaction) {
        KeyValueCollection props = transaction.getUserProperties();
        return props == null ? new KeyValueCollection() : props;
    }

    private void upsertTransactionOption(KeyValueCollection props, String sectionName, String key, String value,
                                         String dataType, String alias, boolean rename, String changeKey) {
        KeyValueCollection sectionOptions = getSectionOptions(props, sectionName);
        String existingValue = findOptionValue(sectionOptions, key);
        String resolvedValue = value != null ? value : existingValue;
        if (resolvedValue == null) {
            resolvedValue = "";
        }
        String targetKey = rename && changeKey != null && !changeKey.isBlank() ? changeKey : key;
        if (!targetKey.equals(key)) {
            sectionOptions.remove(key);
        }
        sectionOptions.remove(targetKey);
        sectionOptions.addString(targetKey, resolvedValue);
        props.remove(sectionName);
        props.addPair(new KeyValuePair(sectionName, sectionOptions));

        if (dataType != null || alias != null || value != null) {
            String templateName = "Template_" + sectionName;
            KeyValueCollection templateOptions = getSectionOptions(props, templateName);
            String templateValue = buildTemplateValue(dataType, alias, resolvedValue);
            if (!targetKey.equals(key)) {
                templateOptions.remove(key);
            }
            templateOptions.remove(targetKey);
            templateOptions.addString(targetKey, templateValue);
            props.remove(templateName);
            props.addPair(new KeyValuePair(templateName, templateOptions));
        }
    }

    private void removeTransactionOptionFromProps(KeyValueCollection props, String sectionName, String key) {
        KeyValueCollection sectionOptions = getSectionOptions(props, sectionName);
        sectionOptions.remove(key);
        props.remove(sectionName);
        props.addPair(new KeyValuePair(sectionName, sectionOptions));
    }

    private KeyValueCollection getSectionOptions(KeyValueCollection props, String sectionName) {
        KeyValuePair sectionPair = props.getPair(sectionName);
        return sectionPair != null ? sectionPair.getTKVValue() : new KeyValueCollection();
    }

    private String findOptionValue(KeyValueCollection options, String key) {
        if (options == null) {
            return null;
        }
        for (Object obj : options) {
            KeyValuePair pair = (KeyValuePair) obj;
            if (pair.getStringKey().equals(key)) {
                return pair.getStringValue();
            }
        }
        return null;
    }

    private String buildTemplateValue(String dataType, String alias, String value) {
        String resolvedDataType = dataType == null ? "" : dataType;
        String resolvedAlias = alias == null ? "" : alias;
        String resolvedValue = value == null ? "" : value;
        return resolvedDataType + "^@" + resolvedAlias + "^@" + resolvedValue;
    }

    private Set<CfgDN> resolveDns(com.genesyslab.platform.applicationblocks.com.IConfService service, int tenantDbid,
                                 int switchDbid, List<String> dnNumbers) throws ConfigException {
        Set<CfgDN> dns = new HashSet<>();
        for (String number : dnNumbers) {
            if (number == null || number.isBlank()) {
                continue;
            }
            CfgDNQuery query = new CfgDNQuery();
            query.setTenantDbid(tenantDbid);
            query.setDnNumber(number);
            CfgDN dn = service.retrieveObject(CfgDN.class, query);
            if (dn == null) {
                throw new ApiException(HttpStatus.NOT_FOUND, "DN을 찾을 수 없습니다: " + number);
            }
            dns.add(dn);
        }
        return dns;
    }
}
