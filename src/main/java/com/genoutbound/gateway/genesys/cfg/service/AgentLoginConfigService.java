package com.genoutbound.gateway.genesys.cfg.service;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLogin;
import com.genesyslab.platform.applicationblocks.com.queries.CfgAgentLoginQuery;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginSummary;
import com.genoutbound.gateway.genesys.cfg.dto.AgentLoginUpdateRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * AgentLogin 관련 작업을 담당합니다.
 */
@Service
public class AgentLoginConfigService extends GenesysConfigSupport {

    private static final Logger log = LoggerFactory.getLogger(AgentLoginConfigService.class);

    public AgentLoginConfigService(GenesysConfigClient configClient) {
        super(configClient);
    }

    public List<AgentLoginSummary> listAgentLogins(Integer tenantDbid, Integer switchDbid, Boolean assignable) {
        log.debug("listAgentLogins 요청: tenantDbid={}, switchDbid={}, assignable={}", tenantDbid, switchDbid, assignable);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        int resolvedSwitch = resolveSwitchDbid(switchDbid);
        List<AgentLoginSummary> result = configClient.withConfService(service -> {
            try {
                CfgAgentLoginQuery query = new CfgAgentLoginQuery();
                query.setTenantDbid(resolvedTenant);
                query.setSwitchDbid(resolvedSwitch);
                if (Boolean.TRUE.equals(assignable)) {
                    query.setNoPersonDbid(2);
                }
                Collection<CfgAgentLogin> logins = service.retrieveMultipleObjects(CfgAgentLogin.class, query);
                Set<Integer> unassignedDbids = new HashSet<>();
                if (!Boolean.TRUE.equals(assignable)) {
                    CfgAgentLoginQuery unassignedQuery = new CfgAgentLoginQuery();
                    unassignedQuery.setTenantDbid(resolvedTenant);
                    unassignedQuery.setSwitchDbid(resolvedSwitch);
                    unassignedQuery.setNoPersonDbid(2);
                    Collection<CfgAgentLogin> unassigned = service.retrieveMultipleObjects(CfgAgentLogin.class, unassignedQuery);
                    for (CfgAgentLogin login : safeCollection(unassigned)) {
                        if (login.getDBID() != null) {
                            unassignedDbids.add(login.getDBID());
                        }
                    }
                }
                List<AgentLoginSummary> summaries = new ArrayList<>();
                for (CfgAgentLogin login : safeCollection(logins)) {
                    boolean isAssignable = Boolean.TRUE.equals(assignable)
                        || (login.getDBID() != null && unassignedDbids.contains(login.getDBID()));
                    summaries.add(new AgentLoginSummary(login.getDBID(), login.getLoginCode(),
                            login.getSwitchDBID(), login.getState() == CfgObjectState.CFGEnabled, isAssignable));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 조회 실패");
            }
        });
        log.debug("listAgentLogins 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public AgentLoginSummary getAgentLogin(int loginDbid, Integer tenantDbid, Integer switchDbid) {
        log.debug("getAgentLogin 요청: loginDbid={}, tenantDbid={}, switchDbid={}", loginDbid, tenantDbid, switchDbid);
        if (loginDbid <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "loginDbid는 1 이상의 값이어야 합니다.");
        }
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        int resolvedSwitch = resolveSwitchDbid(switchDbid);
        AgentLoginSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentLoginQuery query = new CfgAgentLoginQuery();
                query.setTenantDbid(resolvedTenant);
                query.setSwitchDbid(resolvedSwitch);
                query.setDbid(loginDbid);
                CfgAgentLogin login = retrieveSingleAgentLogin(service, query, "AgentLogin DBID");
                if (login == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "AgentLogin을 찾을 수 없습니다.");
                }
                boolean isAssignable = isAgentLoginAssignable(service, resolvedTenant, resolvedSwitch, login.getDBID());
                return new AgentLoginSummary(login.getDBID(), login.getLoginCode(), login.getSwitchDBID(),
                    login.getState() == CfgObjectState.CFGEnabled, isAssignable);
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 조회 실패");
            }
        });
        log.debug("getAgentLogin 응답: {}", result);
        return result;
    }

    public AgentLoginSummary getAgentLoginByCode(String loginCode, Integer tenantDbid, Integer switchDbid) {
        log.debug("getAgentLoginByCode 요청: loginCode={}, tenantDbid={}, switchDbid={}", loginCode, tenantDbid, switchDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        int resolvedSwitch = resolveSwitchDbid(switchDbid);
        AgentLoginSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentLoginQuery query = new CfgAgentLoginQuery();
                query.setTenantDbid(resolvedTenant);
                query.setSwitchDbid(resolvedSwitch);
                query.setLoginCode(loginCode);
                CfgAgentLogin login = retrieveSingleAgentLogin(service, query, "AgentLogin 코드");
                if (login == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "AgentLogin을 찾을 수 없습니다.");
                }
                boolean isAssignable = isAgentLoginAssignable(service, resolvedTenant, resolvedSwitch, login.getDBID());
                return new AgentLoginSummary(login.getDBID(), login.getLoginCode(), login.getSwitchDBID(),
                    login.getState() == CfgObjectState.CFGEnabled, isAssignable);
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 조회 실패");
            }
        });
        log.debug("getAgentLoginByCode 응답: {}", result);
        return result;
    }

    public AgentLoginSummary createAgentLogin(AgentLoginRequest request) {
        log.debug("createAgentLogin 요청: {}", request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        int resolvedSwitch = resolveSwitchDbid(request.switchDbid());
        AgentLoginSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentLoginQuery duplicateQuery = new CfgAgentLoginQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setSwitchDbid(resolvedSwitch);
                duplicateQuery.setLoginCode(request.loginCode());
                CfgAgentLogin existing = retrieveSingleAgentLogin(service, duplicateQuery, "AgentLogin 코드");
                ensureNotExists(existing, "이미 존재하는 AgentLogin입니다.");

                CfgAgentLogin login = new CfgAgentLogin(service);
                login.setTenantDBID(resolvedTenant);
                login.setSwitchDBID(resolvedSwitch);
                login.setLoginCode(request.loginCode());
                login.setSwitchSpecificType(1);
                login.setState(request.enabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                login.save();
                boolean isAssignable = isAgentLoginAssignable(service, resolvedTenant, resolvedSwitch, login.getDBID());
                return new AgentLoginSummary(login.getDBID(), login.getLoginCode(), login.getSwitchDBID(),
                    login.getState() == CfgObjectState.CFGEnabled, isAssignable);
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 생성 실패");
            }
        });
        log.debug("createAgentLogin 응답: {}", result);
        return result;
    }

    public AgentLoginSummary updateAgentLogin(int loginDbid, AgentLoginUpdateRequest request) {
        log.debug("updateAgentLogin 요청: loginDbid={}, request={}", loginDbid, request);
        int resolvedTenant = resolveTenantDbid(null);
        int resolvedSwitch = resolveSwitchDbid(null);
        AgentLoginSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentLoginQuery query = new CfgAgentLoginQuery();
                query.setTenantDbid(resolvedTenant);
                query.setSwitchDbid(resolvedSwitch);
                query.setDbid(loginDbid);
                CfgAgentLogin login = retrieveSingleAgentLogin(service, query, "AgentLogin DBID");
                if (login == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "AgentLogin을 찾을 수 없습니다.");
                }
                login.setState(request.enabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                login.save();
                boolean isAssignable = isAgentLoginAssignable(service, resolvedTenant, resolvedSwitch, login.getDBID());
                return new AgentLoginSummary(login.getDBID(), login.getLoginCode(), login.getSwitchDBID(),
                    login.getState() == CfgObjectState.CFGEnabled, isAssignable);
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 수정 실패");
            }
        });
        log.debug("updateAgentLogin 응답: {}", result);
        return result;
    }

    public AgentLoginSummary updateAgentLoginByCode(String loginCode, Integer tenantDbid, Integer switchDbid,
            AgentLoginUpdateRequest request) {
        log.debug("updateAgentLoginByCode 요청: loginCode={}, tenantDbid={}, switchDbid={}, request={}",
            loginCode, tenantDbid, switchDbid, request);
        if (loginCode == null || loginCode.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "loginCode 설정이 필요합니다.");
        }
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        int resolvedSwitch = resolveSwitchDbid(switchDbid);
        AgentLoginSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentLoginQuery query = new CfgAgentLoginQuery();
                query.setTenantDbid(resolvedTenant);
                query.setSwitchDbid(resolvedSwitch);
                query.setLoginCode(loginCode);
                CfgAgentLogin login = retrieveSingleAgentLogin(service, query, "AgentLogin 코드");
                if (login == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "AgentLogin을 찾을 수 없습니다.");
                }
                login.setState(request.enabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                login.save();
                boolean isAssignable = isAgentLoginAssignable(service, resolvedTenant, resolvedSwitch, login.getDBID());
                return new AgentLoginSummary(login.getDBID(), login.getLoginCode(), login.getSwitchDBID(),
                    login.getState() == CfgObjectState.CFGEnabled, isAssignable);
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 수정 실패");
            }
        });
        log.debug("updateAgentLoginByCode 응답: {}", result);
        return result;
    }

    public void deleteAgentLogin(int loginDbid, Integer tenantDbid, Integer switchDbid) {
        log.debug("deleteAgentLogin 요청: loginDbid={}, tenantDbid={}, switchDbid={}", loginDbid, tenantDbid, switchDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        int resolvedSwitch = resolveSwitchDbid(switchDbid);
        configClient.withConfService(service -> {
            try {
                CfgAgentLoginQuery query = new CfgAgentLoginQuery();
                query.setTenantDbid(resolvedTenant);
                query.setSwitchDbid(resolvedSwitch);
                query.setDbid(loginDbid);
                CfgAgentLogin login = retrieveSingleAgentLogin(service, query, "AgentLogin DBID");
                if (login == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "AgentLogin을 찾을 수 없습니다.");
                }
                login.delete();
                return null;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 삭제 실패");
            }
        });
        log.debug("deleteAgentLogin 응답: 완료");
    }

    public void deleteAgentLoginByCode(String loginCode, Integer tenantDbid, Integer switchDbid) {
        log.debug("deleteAgentLoginByCode 요청: loginCode={}, tenantDbid={}, switchDbid={}", loginCode, tenantDbid, switchDbid);
        if (loginCode == null || loginCode.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "loginCode 설정이 필요합니다.");
        }
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        int resolvedSwitch = resolveSwitchDbid(switchDbid);
        configClient.withConfService(service -> {
            try {
                CfgAgentLoginQuery query = new CfgAgentLoginQuery();
                query.setTenantDbid(resolvedTenant);
                query.setSwitchDbid(resolvedSwitch);
                query.setLoginCode(loginCode);
                CfgAgentLogin login = retrieveSingleAgentLogin(service, query, "AgentLogin 코드");
                if (login == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "AgentLogin을 찾을 수 없습니다.");
                }
                login.delete();
                return null;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 삭제 실패");
            }
        });
        log.debug("deleteAgentLoginByCode 응답: 완료");
    }

    private CfgAgentLogin retrieveSingleAgentLogin(IConfService service, CfgAgentLoginQuery query, String context)
            throws ConfigException, InterruptedException {
        Collection<CfgAgentLogin> results = service.retrieveMultipleObjects(CfgAgentLogin.class, query);
        List<CfgAgentLogin> logins = new ArrayList<>(safeCollection(results));
        if (logins.isEmpty()) {
            return null;
        }
        if (logins.size() > 1) {
            throw new ApiException(HttpStatus.CONFLICT, context + " 조회 결과가 중복되었습니다.");
        }
        return logins.get(0);
    }

    private boolean isAgentLoginAssignable(IConfService service, int tenantDbid, int switchDbid, Integer loginDbid)
            throws ConfigException {
        if (loginDbid == null) {
            return false;
        }
        CfgAgentLoginQuery query = new CfgAgentLoginQuery();
        query.setTenantDbid(tenantDbid);
        query.setSwitchDbid(switchDbid);
        query.setDbid(loginDbid);
        query.setNoPersonDbid(2);
        try {
            Collection<CfgAgentLogin> logins = service.retrieveMultipleObjects(CfgAgentLogin.class, query);
            return !safeCollection(logins).isEmpty();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 조회 실패");
        }
    }
}
