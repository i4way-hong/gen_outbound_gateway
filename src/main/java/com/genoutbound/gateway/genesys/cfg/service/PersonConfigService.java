package com.genoutbound.gateway.genesys.cfg.service;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLogin;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLoginInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSkillLevel;
import com.genesyslab.platform.applicationblocks.com.queries.CfgAgentGroupQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgAgentLoginQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPersonQuery;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.genesys.cfg.dto.PersonAgentLoginCodeRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonSkillRequest;
import com.genoutbound.gateway.genesys.cfg.dto.PersonSummary;
import com.genoutbound.gateway.genesys.cfg.dto.PersonUpdateRequest;
import com.genoutbound.gateway.genesys.cfg.dto.SkillAssignment;
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
 * 상담사(Person) 관련 작업을 담당합니다.
 */
@Service
public class PersonConfigService extends GenesysConfigSupport {

    private static final Logger log = LoggerFactory.getLogger(PersonConfigService.class);

    public PersonConfigService(GenesysConfigClient configClient) {
        super(configClient);
    }

    public List<PersonSummary> listPersons(Integer tenantDbid) {
        log.debug("listPersons 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<PersonSummary> result = configClient.withConfService(service -> {
            try {
                CfgPersonQuery query = new CfgPersonQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgPerson> persons = service.retrieveMultipleObjects(CfgPerson.class, query);
                List<PersonSummary> summaries = new ArrayList<>();
                for (CfgPerson person : safeCollection(persons)) {
                    summaries.add(toSummary(person));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "상담사 목록 조회 실패");
            }
        });
        log.debug("listPersons 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public PersonSummary getPerson(int personDbid, Integer tenantDbid) {
        log.debug("getPerson 요청: personDbid={}, tenantDbid={}", personDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        PersonSummary result = configClient.withConfService(service -> {
            try {
                return toSummary(getPersonByDbid(service, resolvedTenant, personDbid));
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "상담사 조회 실패");
            }
        });
        log.debug("getPerson 응답: {}", result);
        return result;
    }

    public PersonSummary getPersonByEmployeeId(String employeeId, Integer tenantDbid) {
        log.debug("getPersonByEmployeeId 요청: employeeId={}, tenantDbid={}", employeeId, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        PersonSummary result = configClient.withConfService(service -> {
            try {
                CfgPersonQuery query = new CfgPersonQuery();
                query.setTenantDbid(resolvedTenant);
                query.setEmployeeId(employeeId);
                CfgPerson person = service.retrieveObject(CfgPerson.class, query);
                if (person == null) {
                    throw new ApiException(HttpStatus.NOT_FOUND, "상담사를 찾을 수 없습니다.");
                }
                return toSummary(person);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "상담사 조회 실패");
            }
        });
        log.debug("getPersonByEmployeeId 응답: {}", result);
        return result;
    }

    public PersonSummary createPerson(PersonRequest request) {
        log.debug("createPerson 요청: {}", request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        PersonSummary result = configClient.withConfService(service -> {
            try {
                CfgPersonQuery duplicateQuery = new CfgPersonQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setEmployeeId(request.employeeId());
                CfgPerson existing = service.retrieveObject(CfgPerson.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 상담사입니다.");

                CfgPerson person = new CfgPerson(service);
                person.setTenantDBID(resolvedTenant);
                person.setEmployeeID(request.employeeId());
                person.setUserName(request.userName());
                person.setFirstName(request.firstName());
                person.setLastName(request.lastName());
                person.setIsAgent(request.isAgent() ? CfgFlag.CFGTrue : CfgFlag.CFGFalse);
                person.setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                person.save();

                assignAgentLogin(service, resolvedTenant, person, request.agentLoginId());
                assignPersonToAgentGroups(service, resolvedTenant, person, request.agentGroupNames());
                return toSummary(person);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "상담사 생성 실패");
            }
        });
        log.debug("createPerson 응답: {}", result);
        return result;
    }

    public PersonSummary updatePerson(int personDbid, PersonUpdateRequest request) {
        log.debug("updatePerson 요청: personDbid={}, request={}", personDbid, request);
        int resolvedTenant = resolveTenantDbid(null);
        PersonSummary result = configClient.withConfService(service -> {
            try {
                CfgPerson person = getPersonByDbid(service, resolvedTenant, personDbid);
                person.setUserName(request.userName());
                person.setFirstName(request.firstName());
                person.setLastName(request.lastName());
                person.setIsAgent(request.isAgent() ? CfgFlag.CFGTrue : CfgFlag.CFGFalse);
                person.setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                person.save();

                assignAgentLogin(service, resolvedTenant, person, request.agentLoginId());
                assignPersonToAgentGroups(service, resolvedTenant, person, request.agentGroupNames());
                return toSummary(person);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "상담사 수정 실패");
            }
        });
        log.debug("updatePerson 응답: {}", result);
        return result;
    }

    public void deletePerson(int personDbid) {
        log.debug("deletePerson 요청: personDbid={}", personDbid);
        int resolvedTenant = resolveTenantDbid(null);
        configClient.withConfService(service -> {
            try {
                CfgPerson person = getPersonByDbid(service, resolvedTenant, personDbid);
                person.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "상담사 삭제 실패");
            }
        });
        log.debug("deletePerson 응답: 완료");
    }

    public void assignPersonAgentLoginsByCode(int personDbid, PersonAgentLoginCodeRequest request) {
        log.debug("assignPersonAgentLoginsByCode 요청: personDbid={}, request={}", personDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        int resolvedSwitch = resolveSwitchDbid(request.switchDbid());
        configClient.withConfService(service -> {
            try {
                CfgPerson person = getPersonByDbid(service, resolvedTenant, personDbid);
                ensureAgentInfo(person);
                Set<CfgAgentLoginInfo> logins = ensureAgentLoginInfos(person);
                if (request.loginCodes() != null) {
                    for (String loginCode : request.loginCodes()) {
                        if (loginCode == null || loginCode.isBlank()) {
                            continue;
                        }
                        CfgAgentLogin login = getOrCreateAgentLogin(service, resolvedTenant, resolvedSwitch, loginCode);
                        if (!containsAgentLogin(logins, login.getDBID())) {
                            CfgAgentLoginInfo info = new CfgAgentLoginInfo(service, null);
                            info.setAgentLogin(login);
                            info.setAgentLoginDBID(login.getDBID());
                            info.setWrapupTime(0);
                            logins.add(info);
                        }
                    }
                }
                person.getAgentInfo().setAgentLogins(logins);
                person.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 연결 실패");
            }
        });
        log.debug("assignPersonAgentLoginsByCode 응답: 완료");
    }

    public void unassignPersonAgentLoginsByCode(int personDbid, PersonAgentLoginCodeRequest request) {
        log.debug("unassignPersonAgentLoginsByCode 요청: personDbid={}, request={}", personDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgPerson person = getPersonByDbid(service, resolvedTenant, personDbid);
                ensureAgentInfo(person);
                Set<CfgAgentLoginInfo> logins = ensureAgentLoginInfos(person);
                Set<String> removeTargets = new HashSet<>();
                if (request.loginCodes() != null) {
                    removeTargets.addAll(request.loginCodes());
                }
                logins.removeIf(info -> info.getAgentLogin() != null
                        && removeTargets.contains(info.getAgentLogin().getLoginCode()));
                person.getAgentInfo().setAgentLogins(logins);
                person.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 해제 실패");
            }
        });
        log.debug("unassignPersonAgentLoginsByCode 응답: 완료");
    }

    public void setPersonSkills(int personDbid, PersonSkillRequest request) {
        log.debug("setPersonSkills 요청: personDbid={}, request={}", personDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgPerson person = getPersonByDbid(service, resolvedTenant, personDbid);
                if (person.getAgentInfo() == null) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "상담사 AgentInfo가 없습니다.");
                }
                Collection<CfgSkillLevel> skillLevels = new HashSet<>();
                if (request.skills() != null) {
                    for (SkillAssignment assignment : request.skills()) {
                        CfgSkillLevel level = new CfgSkillLevel(service, null);
                        level.setSkillDBID(assignment.skillDbid());
                        level.setLevel(assignment.level());
                        skillLevels.add(level);
                    }
                }
                person.getAgentInfo().setSkillLevels(skillLevels);
                person.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "상담사 스킬 설정 실패");
            }
        });
        log.debug("setPersonSkills 응답: 완료");
    }

    public void removePersonSkills(int personDbid, PersonSkillRequest request) {
        log.debug("removePersonSkills 요청: personDbid={}, request={}", personDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        configClient.withConfService(service -> {
            try {
                CfgPerson person = getPersonByDbid(service, resolvedTenant, personDbid);
                if (person.getAgentInfo() == null) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "상담사 AgentInfo가 없습니다.");
                }
                Collection<CfgSkillLevel> current = person.getAgentInfo().getSkillLevels();
                Collection<CfgSkillLevel> updated = new HashSet<>();
                if (current != null) {
                    Set<Integer> removeTargets = new HashSet<>();
                    if (request.skills() != null) {
                        for (SkillAssignment assignment : request.skills()) {
                            removeTargets.add(assignment.skillDbid());
                        }
                    }
                    for (CfgSkillLevel level : current) {
                        if (!removeTargets.contains(level.getSkillDBID())) {
                            updated.add(level);
                        }
                    }
                }
                person.getAgentInfo().setSkillLevels(updated);
                person.save();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "상담사 스킬 삭제 실패");
            }
        });
        log.debug("removePersonSkills 응답: 완료");
    }

    private void assignPersonToAgentGroups(IConfService service,
                                           int tenantDbid,
                                           CfgPerson person,
                                           List<String> agentGroupNames) throws ConfigException {
        if (agentGroupNames == null || agentGroupNames.isEmpty()) {
            return;
        }
        for (String groupName : agentGroupNames) {
            if (groupName == null || groupName.isBlank()) {
                continue;
            }
            CfgAgentGroup group = getAgentGroupByName(service, tenantDbid, groupName);
            Collection<CfgPerson> agents = new HashSet<>();
            if (group.getAgents() != null) {
                agents.addAll(group.getAgents());
            }
            agents.add(person);
            group.setAgents(agents);
            group.save();
        }
    }

    private CfgAgentGroup getAgentGroupByName(IConfService service, int tenantDbid, String name) throws ConfigException {
        CfgAgentGroupQuery query = new CfgAgentGroupQuery();
        query.setTenantDbid(tenantDbid);
        query.setName(name);
        CfgAgentGroup group = service.retrieveObject(CfgAgentGroup.class, query);
        if (group == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다.");
        }
        return group;
    }

    private void ensureAgentInfo(CfgPerson person) {
        if (person.getAgentInfo() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "상담사 AgentInfo가 없습니다.");
        }
    }

    private Set<CfgAgentLoginInfo> ensureAgentLoginInfos(CfgPerson person) {
        Collection<CfgAgentLoginInfo> logins = person.getAgentInfo().getAgentLogins();
        return logins == null ? new HashSet<>() : new HashSet<>(logins);
    }

    private boolean containsAgentLogin(Set<CfgAgentLoginInfo> logins, int loginDbid) {
        for (CfgAgentLoginInfo info : logins) {
            if (info.getAgentLoginDBID() == loginDbid) {
                return true;
            }
        }
        return false;
    }

    private void assignAgentLogin(IConfService service, int tenantDbid, CfgPerson person, String loginId)
            throws ConfigException {
        if (loginId == null || loginId.isBlank()) {
            return;
        }
        int switchDbid = configClient.getPrimarySwitchDbid();
        if (switchDbid <= 0) {
            log.warn("switchDbidPrimary 값이 없어 agent login 연결을 생략합니다.");
            return;
        }

        CfgAgentLogin login = getOrCreateAgentLogin(service, tenantDbid, switchDbid, loginId);
        CfgAgentLoginInfo loginInfo = new CfgAgentLoginInfo(service, null);
        loginInfo.setAgentLogin(login);
        loginInfo.setAgentLoginDBID(login.getDBID());
        loginInfo.setWrapupTime(0);
        if (person.getAgentInfo().getAgentLogins() == null) {
            person.getAgentInfo().setAgentLogins(new HashSet<>());
        }
        person.getAgentInfo().getAgentLogins().add(loginInfo);
        person.save();
    }

    private CfgAgentLogin getOrCreateAgentLogin(IConfService service, int tenantDbid, int switchDbid, String loginId)
            throws ConfigException {
        CfgAgentLoginQuery query = new CfgAgentLoginQuery();
        query.setTenantDbid(tenantDbid);
        query.setSwitchDbid(switchDbid);
        query.setLoginCode(loginId);
        CfgAgentLogin login;
        try {
            login = retrieveSingleAgentLogin(service, query, "AgentLogin 코드");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "AgentLogin 조회 실패");
        }
        if (login != null) {
            return login;
        }
        login = new CfgAgentLogin(service);
        login.setTenantDBID(tenantDbid);
        login.setSwitchDBID(switchDbid);
        login.setLoginCode(loginId);
        login.setSwitchSpecificType(1);
        login.save();
        return login;
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

    private PersonSummary toSummary(CfgPerson person) {
        return new PersonSummary(
                person.getDBID(),
                person.getEmployeeID(),
                person.getUserName(),
                person.getFirstName(),
                person.getLastName(),
                person.getState() == CfgObjectState.CFGEnabled
        );
    }
}
