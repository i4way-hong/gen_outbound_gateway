package com.genoutbound.gateway.genesys.cfg.service;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.queries.CfgAgentGroupQuery;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genoutbound.gateway.core.ApiException;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupAssignEmployeeRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupAssignPersonRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupMemberSummary;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupRequest;
import com.genoutbound.gateway.genesys.cfg.dto.AgentGroupSummary;
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
 * 상담사 그룹(AgentGroup) 관련 작업을 담당합니다.
 */
@Service
public class AgentGroupConfigService extends GenesysConfigSupport {

    private static final Logger log = LoggerFactory.getLogger(AgentGroupConfigService.class);

    public AgentGroupConfigService(GenesysConfigClient configClient) {
        super(configClient);
    }

    public List<AgentGroupSummary> listAgentGroups(Integer tenantDbid) {
        log.debug("listAgentGroups 요청: tenantDbid={}", tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<AgentGroupSummary> result = configClient.withConfService(service -> {
            try {
                CfgAgentGroupQuery query = new CfgAgentGroupQuery();
                query.setTenantDbid(resolvedTenant);
                Collection<CfgAgentGroup> groups = service.retrieveMultipleObjects(CfgAgentGroup.class, query);
                List<AgentGroupSummary> summaries = new ArrayList<>();
                if (groups == null || groups.isEmpty()) {
                    return summaries;
                }
                for (CfgAgentGroup group : groups) {
                    summaries.add(toAgentGroupSummary(group, true));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 목록 조회 실패");
            }
        });
        log.debug("listAgentGroups 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public AgentGroupSummary getAgentGroup(int groupDbid, Integer tenantDbid) {
        log.debug("getAgentGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        AgentGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentGroup group = getAgentGroupByDbid(service, resolvedTenant, groupDbid);
                return toAgentGroupSummary(group, true);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 조회 실패");
            }
        });
        log.debug("getAgentGroup 응답: {}", result);
        return result;
    }

    public List<AgentGroupSummary> listAgentGroupsByName(String name, Integer tenantDbid) {
        log.debug("listAgentGroupsByName 요청: name={}, tenantDbid={}", name, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        List<AgentGroupSummary> result = configClient.withConfService(service -> {
            try {
                CfgAgentGroupQuery query = new CfgAgentGroupQuery();
                query.setTenantDbid(resolvedTenant);
                query.setName(name);
                Collection<CfgAgentGroup> groups = service.retrieveMultipleObjects(CfgAgentGroup.class, query);
                List<AgentGroupSummary> summaries = new ArrayList<>();
                if (groups == null || groups.isEmpty()) {
                    return summaries;
                }
                for (CfgAgentGroup group : groups) {
                    summaries.add(toAgentGroupSummary(group, true));
                }
                return summaries;
            } catch (ConfigException | InterruptedException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 조회 실패");
            }
        });
        log.debug("listAgentGroupsByName 응답: count={}", result == null ? 0 : result.size());
        return result;
    }

    public AgentGroupSummary createAgentGroup(AgentGroupRequest request) {
        log.debug("createAgentGroup 요청: {}", request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        AgentGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentGroupQuery duplicateQuery = new CfgAgentGroupQuery();
                duplicateQuery.setTenantDbid(resolvedTenant);
                duplicateQuery.setName(request.name());
                CfgAgentGroup existing = service.retrieveObject(CfgAgentGroup.class, duplicateQuery);
                ensureNotExists(existing, "이미 존재하는 그룹입니다.");

                CfgGroup groupInfo = new CfgGroup(service, null);
                groupInfo.setTenantDBID(resolvedTenant);
                groupInfo.setName(request.name());

                CfgAgentGroup group = new CfgAgentGroup(service);
                group.setGroupInfo(groupInfo);
                group.getGroupInfo().setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                group.save();

                return toAgentGroupSummary(group, true);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 생성 실패");
            }
        });
        log.debug("createAgentGroup 응답: {}", result);
        return result;
    }

    public AgentGroupSummary updateAgentGroup(int groupDbid, AgentGroupRequest request) {
        log.debug("updateAgentGroup 요청: groupDbid={}, request={}", groupDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        AgentGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentGroup group = getAgentGroupByDbid(service, resolvedTenant, groupDbid);
                group.getGroupInfo().setName(request.name());
                group.getGroupInfo().setState(request.isEnabled() ? CfgObjectState.CFGEnabled : CfgObjectState.CFGDisabled);
                group.save();
                return toAgentGroupSummary(group, true);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 수정 실패");
            }
        });
        log.debug("updateAgentGroup 응답: {}", result);
        return result;
    }

    public void deleteAgentGroup(int groupDbid, Integer tenantDbid) {
        log.debug("deleteAgentGroup 요청: groupDbid={}, tenantDbid={}", groupDbid, tenantDbid);
        int resolvedTenant = resolveTenantDbid(tenantDbid);
        configClient.withConfService(service -> {
            try {
                CfgAgentGroup group = getAgentGroupByDbid(service, resolvedTenant, groupDbid);
                group.delete();
                return null;
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 삭제 실패");
            }
        });
        log.debug("deleteAgentGroup 응답: 완료");
    }

    public AgentGroupSummary assignAgentGroupByEmployeeIds(int groupDbid, AgentGroupAssignEmployeeRequest request) {
        log.debug("assignAgentGroupByEmployeeIds 요청: groupDbid={}, request={}", groupDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        AgentGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentGroup group = getAgentGroupByDbid(service, resolvedTenant, groupDbid);
                Collection<CfgPerson> agents = new HashSet<>();
                if (group.getAgents() != null) {
                    agents.addAll(group.getAgents());
                }
                if (request.employeeIds() != null) {
                    for (String employeeId : request.employeeIds()) {
                        if (employeeId == null || employeeId.isBlank()) {
                            continue;
                        }
                        agents.add(findPersonByEmployeeId(service, resolvedTenant, employeeId));
                    }
                }
                group.setAgents(agents);
                group.save();
                return toAgentGroupSummary(group, true);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 배치 실패");
            }
        });
        log.debug("assignAgentGroupByEmployeeIds 응답: {}", result);
        return result;
    }

    public AgentGroupSummary assignAgentGroupByPersonDbids(int groupDbid, AgentGroupAssignPersonRequest request) {
        log.debug("assignAgentGroupByPersonDbids 요청: groupDbid={}, request={}", groupDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        AgentGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentGroup group = getAgentGroupByDbid(service, resolvedTenant, groupDbid);
                Collection<CfgPerson> agents = new HashSet<>();
                if (group.getAgents() != null) {
                    agents.addAll(group.getAgents());
                }
                if (request.personDbids() != null) {
                    for (Integer personDbid : request.personDbids()) {
                        if (personDbid == null) {
                            continue;
                        }
                        agents.add(getPersonByDbid(service, resolvedTenant, personDbid));
                    }
                }
                group.setAgents(agents);
                group.save();
                return toAgentGroupSummary(group, true);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 배치 실패");
            }
        });
        log.debug("assignAgentGroupByPersonDbids 응답: {}", result);
        return result;
    }

    public AgentGroupSummary unassignAgentGroupByEmployeeIds(int groupDbid, AgentGroupAssignEmployeeRequest request) {
        log.debug("unassignAgentGroupByEmployeeIds 요청: groupDbid={}, request={}", groupDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        AgentGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentGroup group = getAgentGroupByDbid(service, resolvedTenant, groupDbid);
                Collection<CfgPerson> current = group.getAgents();
                if (current == null || current.isEmpty()) {
                    return toAgentGroupSummary(group, true);
                }
                Set<String> removeTargets = new HashSet<>();
                if (request.employeeIds() != null) {
                    removeTargets.addAll(request.employeeIds());
                }
                Collection<CfgPerson> updated = new HashSet<>();
                for (CfgPerson person : current) {
                    if (!removeTargets.contains(person.getEmployeeID())) {
                        updated.add(person);
                    }
                }
                group.setAgents(updated);
                group.save();
                return toAgentGroupSummary(group, true);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 배치 해제 실패");
            }
        });
        log.debug("unassignAgentGroupByEmployeeIds 응답: {}", result);
        return result;
    }

    public AgentGroupSummary unassignAgentGroupByPersonDbids(int groupDbid, AgentGroupAssignPersonRequest request) {
        log.debug("unassignAgentGroupByPersonDbids 요청: groupDbid={}, request={}", groupDbid, request);
        int resolvedTenant = resolveTenantDbid(request.tenantDbid());
        AgentGroupSummary result = configClient.withConfService(service -> {
            try {
                CfgAgentGroup group = getAgentGroupByDbid(service, resolvedTenant, groupDbid);
                Collection<CfgPerson> current = group.getAgents();
                if (current == null || current.isEmpty()) {
                    return toAgentGroupSummary(group, true);
                }
                Set<Integer> removeTargets = new HashSet<>();
                if (request.personDbids() != null) {
                    removeTargets.addAll(request.personDbids());
                }
                Collection<CfgPerson> updated = new HashSet<>();
                for (CfgPerson person : current) {
                    if (!removeTargets.contains(person.getDBID())) {
                        updated.add(person);
                    }
                }
                group.setAgents(updated);
                group.save();
                return toAgentGroupSummary(group, true);
            } catch (ConfigException ex) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "그룹 배치 해제 실패");
            }
        });
        log.debug("unassignAgentGroupByPersonDbids 응답: {}", result);
        return result;
    }

    private CfgAgentGroup getAgentGroupByDbid(com.genesyslab.platform.applicationblocks.com.IConfService service,
            int tenantDbid, int groupDbid) throws ConfigException {
        CfgAgentGroupQuery query = new CfgAgentGroupQuery();
        query.setTenantDbid(tenantDbid);
        query.setDbid(groupDbid);
        CfgAgentGroup group = service.retrieveObject(CfgAgentGroup.class, query);
        if (group == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다.");
        }
        return group;
    }

    private AgentGroupSummary toAgentGroupSummary(CfgAgentGroup group, boolean includeMembers) {
        List<AgentGroupMemberSummary> members = includeMembers ? toAgentGroupMembers(group) : List.of();
        return new AgentGroupSummary(
                group.getDBID(),
                group.getGroupInfo().getName(),
                group.getGroupInfo().getState() == CfgObjectState.CFGEnabled,
                members
        );
    }

    private List<AgentGroupMemberSummary> toAgentGroupMembers(CfgAgentGroup group) {
        List<AgentGroupMemberSummary> members = new ArrayList<>();
        for (CfgPerson person : safeCollection(group.getAgents())) {
            members.add(new AgentGroupMemberSummary(
                    person.getDBID(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getEmployeeID()
            ));
        }
        return members;
    }
}
