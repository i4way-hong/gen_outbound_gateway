package kr.co.i4way.genesys.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class GroupController_backup {
	// private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	// @Autowired
	// private GenesysInfoVo genesysinfovo;
	// initConfigService initconfigservice = initConfigService.getInstance();
	// ConfigAgentGroup cfg = null;
	// ConfigPerson ps = null;
	// ConfigCommon configcommon = null;

	// public GroupController(){
	// 	ps = new ConfigPerson();
	// 	cfg = new ConfigAgentGroup();
	// 	configcommon = new ConfigCommon();
	// }
	
	// @RequestMapping("/group/getAgentGroupInfo")
	// public JSONObject getAgentGroupInfo(@RequestBody QueryVo vo) throws Exception{
	// 	CfgAgentGroup agentgroup = null;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		//그룹명으로 조회
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			cfg = new ConfigAgentGroup();
	// 			//agentgroup = cfg.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), group_dbid);
	// 			agentgroup = cfg.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getGroup_name());
	// 			rtnobj = convertGroupInfo_jsonObj(agentgroup);
	// 		}			
	// 		rtnobj.put("command", "getAgentGroupInfo");
	// 	}catch(Exception e) {
	// 		logger.error("Exception", e);
	// 	}finally {
	// 	}
	// 	return rtnobj;
	// }	
	
	// @RequestMapping("/group/getAllAgentGroupInfo")
	// public JSONObject getAgentGroupInfoAll(@RequestBody QueryVo vo) throws Exception{
	// 	Collection<CfgAgentGroup> agentgroups = null;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			cfg = new ConfigAgentGroup();
	// 			agentgroups = cfg.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid());
	// 			rtnobj = convertGroupInfo_jsonObj(agentgroups);
	// 		}			
	// 		rtnobj.put("command", "getAgentGroupInfoAll");
	// 	}catch(Exception e) {
	// 		logger.error("Exception", e);
	// 	}finally {
	// 	}
	// 	return rtnobj;
	// }	
	
	// @RequestMapping("/group/getAssignGroupInfo")
	// public JSONObject getAssignGroupInfo(@RequestBody QueryVo vo) throws Exception{
	// 	Collection<CfgAgentGroup> agentgroups = null;
	// 	CfgPerson person = null;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			ps = new ConfigPerson();
	// 			person = ps.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), "emp", vo.getEmployee_id());

	// 			cfg = new ConfigAgentGroup();
	// 			agentgroups = cfg.getAssignAgentGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), person.getDBID());

	// 			rtnobj = convertGroupInfo_jsonObj(agentgroups);
	// 		}			
	// 		rtnobj.put("command", "getAssignGroupInfo");
	// 	}catch(Exception e) {
	// 		logger.error("Exception", e);
	// 	}finally {
	// 	}
	// 	return rtnobj;
	// }	
	
	// @RequestMapping("/group/createAgentGroup")
	// public JSONObject createAgentGroup(@RequestBody QueryVo vo) throws Exception{
	// 	JSONObject rtnobj = new JSONObject();
	// 	CfgAgentGroup agentgroup = null;

	// 	try {
	// 		cfg = new ConfigAgentGroup();
			
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			agentgroup = cfg.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getGroup_name());
				
	// 			if(agentgroup == null) {
	// 				logger.info("Create Person~");
	// 				agentgroup = cfg.createAgentGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getGroup_name());
	// 				rtnobj = convertGroupInfo_jsonObj(agentgroup);
	// 			}
	// 		}		
	// 		rtnobj.put("command", "createAgentGroup");
	// 	}catch(Exception e) {
	// 		logger.error("Exception", e);
	// 	}
	// 	return rtnobj;
	// }	
	
	// @RequestMapping("/group/modifyAgentGroup")
	// public JSONObject modifyAgentGroup(@RequestBody QueryVo vo) throws Exception{
	// 	CfgAgentGroup agentgroup = null;
	// 	JSONObject rtnobj = new JSONObject();
	// 	try {
	// 		cfg = new ConfigAgentGroup();
			
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			agentgroup = cfg.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getGroup_dbid());
				
	// 			if(agentgroup != null) {	//수정모드
	// 				agentgroup = cfg.modifyAgentGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroup.getDBID(), vo.getGroup_name());
	// 				logger.info("Modify AgentGroup~");
	// 			}
	// 			rtnobj = convertGroupInfo_jsonObj(agentgroup);
	// 		}		
	// 		rtnobj.put("command", "modifyAgentGroup");
	// 	}catch(Exception e) {
	// 		logger.error("Exception", e);
	// 	}
	// 	return rtnobj;
	// }	
	
	// @RequestMapping("/group/deleteAgentGroup")
	// public JSONObject deleteAgentGroup(@RequestBody QueryVo vo) throws Exception{
	// 	JSONObject rtnobj = new JSONObject();
	// 	boolean del_agentgroup_result =  false;
	// 	CfgAgentGroup agentgroup = null;

	// 	try {
	// 		cfg = new ConfigAgentGroup();
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			agentgroup = cfg.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getGroup_dbid());
				
	// 			if(agentgroup != null) {	//삭제모드
	// 				del_agentgroup_result = cfg.deleteAgentGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroup.getDBID());
	// 				logger.info("delete AgentGroup~");
	// 			}
	// 		}		
	// 		rtnobj.put("command", "deleteAgentGroup");
	// 		rtnobj.put("result_delete_agentgroup", del_agentgroup_result);
	// 	}catch(Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return rtnobj;
	// }	
	
	// @RequestMapping("/group/assignPersonToAgentGroup")
	// public JSONObject assignPersonToAgentGroup(@RequestBody QueryVo vo) throws Exception{
	// 	JSONObject rtnobj = new JSONObject();
	// 	CfgAgentGroup agentgroup = null;

	// 	try {
	// 		cfg = new ConfigAgentGroup();
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			agentgroup = cfg.assignPersonToAgentGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getGroup_name(), vo.getEmployee_id());
	// 			rtnobj = convertGroupInfo_jsonObj(agentgroup);
	// 		}		
	// 		rtnobj.put("command", "assignPersonToAgentGroup");
	// 	}catch(Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return rtnobj;
	// }	
	
	// @RequestMapping("/group/removePersonFromAgentGroup")
	// public JSONObject removePersonFromAgentGroup(@RequestBody QueryVo vo) throws Exception{
	// 	JSONObject rtnobj = new JSONObject();
	// 	CfgAgentGroup agentgroup = null;

	// 	try {
	// 		cfg = new ConfigAgentGroup();
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			agentgroup = cfg.removePersonFromAgentGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getGroup_name(), vo.getEmployee_id());
	// 			rtnobj = convertGroupInfo_jsonObj(agentgroup);
	// 		}		
	// 		rtnobj.put("command", "removePersonFromAgentGroup");
	// 	}catch(Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return rtnobj;
	// }	
	
	// @RequestMapping("/group/movePersonFromAgentGroup")
	// public JSONObject movePersonFromAgentGroup(@RequestBody QueryVo vo) throws Exception{
	// 	JSONObject rtnobj = new JSONObject();
	// 	CfgAgentGroup agentgroup = null;

	// 	try {
	// 		cfg = new ConfigAgentGroup();
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			agentgroup = cfg.movePerson2(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getGroup_name(), vo.getTgt_group_name(), vo.getEmployee_id(), vo.getDelete_yn());
	// 			rtnobj = convertGroupInfo_jsonObj(agentgroup);
	// 		}		
	// 		rtnobj.put("command", "movePersonFromAgentGroup");
	// 	}catch(Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return rtnobj;
	// }	
	
	// private JSONObject convertGroupInfo_jsonObj(CfgAgentGroup group) {
	// 	JSONObject finalJsonObj = new JSONObject();      
		
	// 	if(group != null) {
	// 		Collection<CfgPerson> persons = group.getAgents();
	// 		if(persons != null) {
	// 			finalJsonObj.put("agents", getCfgPersonCollectionInfo(persons));
	// 		}
			
	// 		finalJsonObj.put("dbid", group.getDBID());
	// 		finalJsonObj.put("name", group.getGroupInfo().getName());
	// 		finalJsonObj.put("folderDbid", group.getFolderId());
	// 		finalJsonObj.put("annex", configcommon.getAnnexInfo(group.getGroupInfo().getUserProperties()));
	// 	}
	// 	return finalJsonObj;
	// }
	
	// private JSONObject convertGroupInfo_jsonObj(Collection<CfgAgentGroup> groups) {
	// 	JSONObject jsonObj = new JSONObject();
	// 	JSONArray jsonArray = new JSONArray();
	// 	JSONObject finalJsonObj = new JSONObject();      
	// 	Collection<CfgPerson> persons;
	// 	String warpuptm = "";
		
	// 	if(groups != null) {
	// 		for (CfgAgentGroup agentgroup : groups) {
	// 			warpuptm = "";
	// 			persons = null;
	// 			jsonObj = new JSONObject();
	// 			jsonObj.put("dbid", agentgroup.getDBID());
	// 			jsonObj.put("name", agentgroup.getGroupInfo().getName());
	// 			jsonObj.put("folderDbid", agentgroup.getFolderId());
	// 			persons = agentgroup.getAgents();
	// 			if(persons != null) {
	// 				jsonObj.put("agents", getCfgPersonCollectionInfo(persons));
	// 			}
	// 			jsonArray.add(jsonObj);
	// 		}
	// 		finalJsonObj.put("agentGroups", jsonArray);
	// 	}else {
	// 		finalJsonObj.put("agentGroups", null);
	// 	}
	// 	return finalJsonObj;
	// }
	
	// private JSONArray getCfgPersonCollectionInfo(Collection<CfgPerson> coll) {
	// 	JSONArray rtnArray = new JSONArray();
	// 	JSONObject jsonObj;
	// 	for (CfgPerson person : coll) {
	// 		jsonObj = new JSONObject();
	// 		jsonObj.put("dbid", person.getDBID());
	// 		jsonObj.put("firstName", person.getFirstName());
	// 		jsonObj.put("lastName", person.getLastName());
	// 		jsonObj.put("userName", person.getUserName());
	// 		jsonObj.put("employeeId", person.getEmployeeID());
	// 		jsonObj.put("isAgent", person.getIsAgent().ordinal());
	// 		jsonObj.put("state", person.getState().ordinal());
	// 		rtnArray.add(jsonObj);
	// 	}
	// 	return rtnArray;
	// }
}
