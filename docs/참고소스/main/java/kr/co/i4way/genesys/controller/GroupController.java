package kr.co.i4way.genesys.controller;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.platform.applicationblocks.com.ConfigServerException;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.objects.CfgObjectID;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.co.i4way.genesys.cfgserver.ConfigAgentGroup;
import kr.co.i4way.genesys.cfgserver.ConfigCommon;
import kr.co.i4way.genesys.cfgserver.ConfigFolder;
import kr.co.i4way.genesys.cfgserver.ConfigPerson;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.AgentGroupInfoVo;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.MoveInfoVo;
import kr.co.i4way.genesys.model.QueryVo;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "*")
public class GroupController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private GenesysInfoVo genesysinfovo;
	initConfigService initconfigservice = initConfigService.getInstance();
	ConfigAgentGroup ag = null;
	ConfigPerson ps = null;
	ConfigFolder cfgfolder = null;
	ConfigCommon configcommon = null;

	public GroupController(){
		configcommon = new ConfigCommon();
	}
	
	@RequestMapping("/group/getAgentGroupInfo_Name")
	public JSONObject getAgentGroupInfo_Name(@RequestBody QueryVo vo) throws Exception{
		CfgAgentGroup agentgroup = null;
		JSONObject rtnobj = new JSONObject();

		try {
			//그룹명으로 조회
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				ag = new ConfigAgentGroup();
				//agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), group_dbid);
				agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getGroup_name());
				rtnobj = convertGroupInfo_jsonObj(agentgroup);
			}			
			rtnobj.put("command", "getAgentGroupInfo");
		}catch(Exception e) {
			logger.error("Exception", e);
		}finally {
		}
		return rtnobj;
	}	

	@RequestMapping("/group/getAgentGroupInfo_DBID")
	public JSONObject getAgentGroupInfo_DBID(@RequestBody QueryVo vo) throws Exception{
		CfgAgentGroup agentgroup = null;
		JSONObject rtnobj = new JSONObject();

		try {
			//그룹명으로 조회
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				ag = new ConfigAgentGroup();
				agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getFolder_dbid());
				//agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getGroup_name());
				rtnobj = convertGroupInfo_jsonObj(agentgroup);
			}			
			rtnobj.put("command", "getAgentGroupInfo");
		}catch(Exception e) {
			logger.error("Exception", e);
		}finally {
		}
		return rtnobj;
	}	
	
	@RequestMapping("/group/getAllAgentGroupInfo")
	public JSONObject getAgentGroupInfoAll(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgAgentGroup> agentgroups = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				ag = new ConfigAgentGroup();
				agentgroups = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), "FALSE", "");
				rtnobj = convertGroupInfo_jsonObj(agentgroups);
			}			
			rtnobj.put("command", "getAgentGroupInfoAll");
		}catch(Exception e) {
			logger.error("Exception", e);
		}finally {
		}
		return rtnobj;
	}	

	@RequestMapping("/group/getFolderInfo")
	public JSONObject getFolderInfo(@RequestBody QueryVo vo) throws Exception {
		CfgFolder folder = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgfolder = new ConfigFolder();
				folder = cfgfolder.getFolderInfo(initconfigservice.service, vo.getFolder_type(), vo.getFolder_dbid());
				rtnobj = convertFolderInfo_jsonObj(folder);

				// map = JsonUtil.getMapFromJsonObject(rtnobj);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getFolderInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	@RequestMapping("/group/getFolderInfo_tree")
	public JSONObject getFolderInfoTree(@RequestBody QueryVo vo) throws Exception {
		CfgFolder folder = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgfolder = new ConfigFolder();
				folder = cfgfolder.getFolderInfo(initconfigservice.service, vo.getFolder_type(), vo.getFolder_dbid());
				rtnobj = convertFolderInfoTree_jsonObj(folder);
;
			}
			rtnobj.put("command", "getFolderInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	
	@RequestMapping("/group/createAgentGroupInfo")
	public JSONObject createAgentGroupInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		AgentGroupInfoVo agentgroupinfovo = null;
		CfgAgentGroup agentgroup = null; 

		try { 
			agentgroupinfovo = getParseAgentGroupInfo(param);
			//logger.info("agentgroupinfovo == > " + agentgroupinfovo.toString());
			ag = new ConfigAgentGroup();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(agentgroupinfovo.getDbid() > 0){
					agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroupinfovo.getDbid());
				}

				if (agentgroup != null) { // 수정모드
					agentgroup = ag.modifyAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroup, agentgroupinfovo);
					logger.info("Modify AgentGroup~"); 
				} else {
					agentgroup = ag.createAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroupinfovo);
					logger.info("Create AgentGroup~");
				}
				rtnobj = convertGroupInfo_jsonObj(agentgroup);
			}
			rtnobj.put("command", "createAgentGroupInfo");
			rtnobj.put("result", "success");
		}  catch (ConfigServerException ex){
			rtnobj.put("error_message", ex.getMessage());
			rtnobj.put("error_type", ex.getErrorType().toString());
			rtnobj.put("result", "error");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/group/modifyAgentGroupInfo")
	public JSONObject modifyAgentGroupInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		AgentGroupInfoVo agentgroupinfovo = null;
		CfgAgentGroup agentgroup = null; 

		try {
			agentgroupinfovo = getParseAgentGroupInfo(param);
			ag = new ConfigAgentGroup();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(agentgroupinfovo.getDbid() > 0){
					agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroupinfovo.getDbid());
				}

				if (agentgroup != null) { // 수정모드
					agentgroup = ag.modifyAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroup, agentgroupinfovo);
					logger.info("Modify AgentGroup~"); 
				} else {
					agentgroup = ag.createAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroupinfovo);
					logger.info("Create AgentGroup~");
				}
				rtnobj = convertGroupInfo_jsonObj(agentgroup);
			}
			rtnobj.put("command", "modifyAgentGroupInfo");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/group/deleteAgentGroupInfo")
	public JSONObject deleteAgentGroupInfo(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		boolean del_agentgroup_result = false;
		CfgAgentGroup agentgroup = null;

		try {
			ag = new ConfigAgentGroup();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getQry_str());

				if (agentgroup != null) { // 삭제
					del_agentgroup_result = ag.deleteAgentGroupInfo(initconfigservice.service, agentgroup);
					if(del_agentgroup_result){
						logger.info("Delete AgentGroup~");
					}else{
						logger.info("Delete AgentGroup Fail~");
					}
				}
			}
			rtnobj.put("command", "deleteAgentGroupInfo");
			rtnobj.put("del_result", del_agentgroup_result);
			rtnobj.put("name", vo.getGroup_name());
		} catch (Exception e) { 
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/group/setAgentGroupState")
	public JSONObject setAgentGroupState(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgAgentGroup agentgroup = null;

		try {
			ag = new ConfigAgentGroup();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getQry_str());

				if (agentgroup != null) { // agentgroup이 널이 아니면
					logger.info("set AgentGroup State~");
					if(agentgroup.getGroupInfo().getState().equals(CfgObjectState.CFGEnabled)){
						agentgroup = ag.setAgentGroupState(initconfigservice.service, agentgroup, false);
					}else if(agentgroup.getGroupInfo().getState().equals(CfgObjectState.CFGDisabled)){
						agentgroup = ag.setAgentGroupState(initconfigservice.service, agentgroup, true);
					}
				}
			}
			rtnobj.put("command", "setAgentGroupState");
			rtnobj.put("state", agentgroup.getGroupInfo().getState().name());
			rtnobj.put("name", agentgroup.getGroupInfo().getName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}
	private AgentGroupInfoVo getParseAgentGroupInfo(Map<String, Object> param){
		AgentGroupInfoVo agentgroupinfovo = new AgentGroupInfoVo();
		// jsonPaserPser 클래스 객체를 만들고 해당 객체에 
		JsonParser jparser = new JsonParser();

		try {
			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));

			agentgroupinfovo.setName(getParam(param, "name", "S"));
			agentgroupinfovo.setDbid(Integer.parseInt(getParam(param, "dbid", "I")));
			agentgroupinfovo.setState(Integer.parseInt(getParam(param, "state", "I")));
			agentgroupinfovo.setFolderId(Integer.parseInt(getParam(param, "folderId", "I")));

			agentgroupinfovo.setAnnex(annex);
		}catch(Exception ex){
			agentgroupinfovo = null;
		}
		return agentgroupinfovo;
	}

	private JSONObject getAgentGroupInfo(int dbid) throws Exception {
		CfgAgentGroup agentgroup = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				ag = new ConfigAgentGroup();
				agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dbid);
				rtnobj = convertGroupInfo_jsonObj(agentgroup);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getAgentGroupInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	private JSONObject getFolderInfo(int owner_type, int dbid) throws Exception {
		CfgFolder folder = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgfolder = new ConfigFolder();
				folder = cfgfolder.getFolderInfo(initconfigservice.service, owner_type, dbid);
				rtnobj = convertFolderInfo_jsonObj(folder);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getFolderInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {

		}
		return rtnobj;
	}

	private JSONObject getFolderInfo_tree(int owner_type, int dbid) throws Exception {
		CfgFolder folder = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgfolder = new ConfigFolder();
				folder = cfgfolder.getFolderInfo(initconfigservice.service, owner_type, dbid);
				rtnobj = convertFolderInfoTree_jsonObj(folder);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getFolderInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {

		}
		return rtnobj;
	}

	private JSONObject getAgentGroupInfo_tree(int dbid) throws Exception {
		CfgAgentGroup agentgroup = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				ag = new ConfigAgentGroup();
				agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dbid);
				rtnobj = convertAgentGroupTree_jsonObj(agentgroup);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getAgentGroupInfo_tree");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	private JSONObject convertGroupInfo_jsonObj(CfgAgentGroup group) {
		JSONObject finalJsonObj = new JSONObject();      
		
		if(group != null) {
			/* Agent Group별 상담사 목록*/
			Collection<CfgPerson> persons = group.getAgents();
			if(persons != null) {
				finalJsonObj.put("agents", getCfgPersonCollectionInfo(persons));
			}
			/**/
			finalJsonObj.put("dbid", group.getDBID());
			finalJsonObj.put("name", group.getGroupInfo().getName());
			finalJsonObj.put("folderDbid", group.getFolderId());
			finalJsonObj.put("state", group.getGroupInfo().getState().ordinal());
			finalJsonObj.put("annex", configcommon.getAnnexInfo(group.getGroupInfo().getUserProperties()));
			finalJsonObj.put("virtual", configcommon.checkScript(group.getGroupInfo().getUserProperties()));
			finalJsonObj.put("obj", "CfgAgentGroup");
		}
		return finalJsonObj;
	}
	
	private JSONObject convertGroupInfo_jsonObj(Collection<CfgAgentGroup> groups) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		//Collection<CfgPerson> persons;
		
		if(groups != null) {
			for (CfgAgentGroup agentgroup : groups) {

				//persons = null;
				jsonObj = new JSONObject();
				jsonObj.put("dbid", agentgroup.getDBID());
				jsonObj.put("name", agentgroup.getGroupInfo().getName());
				jsonObj.put("folderDbid", agentgroup.getFolderId());

				/* Agent Group별 상담사 목록
				persons = agentgroup.getAgents();
				if(persons != null) {
					jsonObj.put("agents", getCfgPersonCollectionInfo(persons));
				}
				*/
				jsonArray.add(jsonObj);

			}
			finalJsonObj.put("agentGroups", jsonArray);
		}else {
			finalJsonObj.put("agentGroups", null);
		}
		return finalJsonObj;
	}
	
	private JSONArray getCfgPersonCollectionInfo(Collection<CfgPerson> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		for (CfgPerson person : coll) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", person.getDBID());
			jsonObj.put("firstName", person.getFirstName());
			jsonObj.put("lastName", person.getLastName());
			jsonObj.put("userName", person.getUserName());
			jsonObj.put("employeeId", person.getEmployeeID());
			jsonObj.put("isAgent", person.getIsAgent().ordinal());
			jsonObj.put("state", person.getState().ordinal());
			jsonObj.put("obj", "CfgPerson");
			rtnArray.add(jsonObj);
		}
		return rtnArray;
	}

	public JSONObject convertFolderInfo_jsonObj(CfgFolder folder) {
		JSONObject finalJsonObj = new JSONObject();

		if (folder != null) {

			finalJsonObj.put("dbid", folder.getDBID());
			finalJsonObj.put("name", folder.getName());
			finalJsonObj.put("objectType", folder.getObjectType().ordinal());
			finalJsonObj.put("folderId", folder.getFolderId());
			finalJsonObj.put("objectDbid", folder.getObjectDbid());
			finalJsonObj.put("objectPath", folder.getObjectPath());
			finalJsonObj.put("objectIds", getCfgObjectIDCollectionInfo(folder.getObjectIDs()));
			finalJsonObj.put("ownerDbid", folder.getOwnerID().getDBID());
			finalJsonObj.put("ownerType", folder.getOwnerID().getType().ordinal());
			finalJsonObj.put("type", folder.getType().ordinal());
			finalJsonObj.put("state", folder.getState().ordinal());
			finalJsonObj.put("annex", configcommon.getAnnexInfo(folder.getUserProperties()));
			finalJsonObj.put("obj", "CfgFolder");

		}
		return finalJsonObj;
	}

	public JSONObject convertFolderInfoTree_jsonObj(CfgFolder folder) {
		JSONObject finalJsonObj = new JSONObject();

		if (folder != null) {
			finalJsonObj.put("id", 2000 + folder.getDBID());
			finalJsonObj.put("dbid", folder.getDBID());
			finalJsonObj.put("text", folder.getName());
			finalJsonObj.put("name", folder.getName());
			finalJsonObj.put("name_sort", folder.getName());
			finalJsonObj.put("objectType", folder.getObjectType().ordinal());
			finalJsonObj.put("folderId", folder.getFolderId());
			finalJsonObj.put("objectDbid", folder.getObjectDbid());
			finalJsonObj.put("objectPath", folder.getObjectPath());
			JSONArray arry = getCfgObjectIDCollectionInfoTree(folder.getObjectIDs());
			if(arry != null && arry.size() > 0){
				finalJsonObj.put("children", arry);
				finalJsonObj.put("state", "open");
			}else{
				finalJsonObj.put("children", new JSONArray());
				//finalJsonObj.put("iconCls", "myfolder");
				//finalJsonObj.put("state", "open");
			}
			finalJsonObj.put("ownerDbid", folder.getOwnerID().getDBID());
			finalJsonObj.put("ownerType", folder.getOwnerID().getType().ordinal());
			finalJsonObj.put("type", folder.getType().ordinal());
			finalJsonObj.put("folder_state", folder.getState().ordinal());
			finalJsonObj.put("annex", configcommon.getAnnexInfo(folder.getUserProperties()));
			finalJsonObj.put("obj", "CfgFolder");

		}
		return finalJsonObj;
	}

	public JSONObject convertAgentGroupTree_jsonObj(CfgAgentGroup agentgroup) {
		JSONObject finalJsonObj = new JSONObject();

		if (agentgroup != null) {
			finalJsonObj.put("id", 1000 + agentgroup.getDBID());
			finalJsonObj.put("dbid", agentgroup.getDBID());
			finalJsonObj.put("text", agentgroup.getGroupInfo().getName());
			finalJsonObj.put("name", agentgroup.getGroupInfo().getName());
			finalJsonObj.put("name_sort", agentgroup.getGroupInfo().getName());
			finalJsonObj.put("objectType", agentgroup.getObjectType().ordinal());
			finalJsonObj.put("folderId", agentgroup.getFolderId());
			finalJsonObj.put("objectDbid", agentgroup.getObjectDbid());
			finalJsonObj.put("objectPath", agentgroup.getObjectPath());
			finalJsonObj.put("virtual", configcommon.checkScript(agentgroup.getGroupInfo().getUserProperties()));
			finalJsonObj.put("children", new JSONArray());
			finalJsonObj.put("ownerDbid", 0);
			finalJsonObj.put("ownerType", 0);
			finalJsonObj.put("type", 0);
			finalJsonObj.put("folder_state", agentgroup.getGroupInfo().getState().ordinal());
			finalJsonObj.put("annex", configcommon.getAnnexInfo(agentgroup.getGroupInfo().getUserProperties()));
		}
		return finalJsonObj;
	}

	public JSONArray getCfgObjectIDCollectionInfo(Collection<CfgObjectID> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		try {
			for (CfgObjectID cfgobjectid : coll) {
				jsonObj = new JSONObject();
				if (cfgobjectid.getType().name().equals("CFGAgentGroup")) {
					jsonObj = getAgentGroupInfo(cfgobjectid.getDBID());
					jsonObj.put("obj_order", "2");
				} else if (cfgobjectid.getType().name().equals("CFGFolder")) {
					jsonObj = getFolderInfo(cfgobjectid.getType().ordinal(), cfgobjectid.getDBID());
					jsonObj.put("obj", "CfgFolder");
					jsonObj.put("obj_order", "1");
				}
				rtnArray.add(jsonObj);
			}
		} catch (Exception ex) {
		}
		return rtnArray;
	}

	public JSONArray getCfgObjectIDCollectionInfoTree(Collection<CfgObjectID> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		try {
			for (CfgObjectID cfgobjectid : coll) {
				jsonObj = new JSONObject();
				if (cfgobjectid.getType().name().equals("CFGAgentGroup")) {
					jsonObj = getAgentGroupInfo_tree(cfgobjectid.getDBID());
					jsonObj.put("id", 1000 + cfgobjectid.getDBID());
					//jsonObj.put("state", "closed");
					//jsonObj.put("iconCls", "myfolder");
					jsonObj.put("obj", "CfgAgentGroup");
					jsonObj.put("obj_order", "2");
					rtnArray.add(jsonObj);
				} else if (cfgobjectid.getType().name().equals("CFGFolder")) {
					jsonObj = getFolderInfo_tree(cfgobjectid.getType().ordinal(), cfgobjectid.getDBID());
					jsonObj.put("id", 2000 + cfgobjectid.getDBID());
					jsonObj.put("state", "closed");
					jsonObj.put("iconCls", "myfolder");
					jsonObj.put("obj", "CfgFolder");
					jsonObj.put("obj_order", "1");
					rtnArray.add(jsonObj);
				}
			}
		} catch (Exception ex) {
		}
		return rtnArray;
	}

	private MoveInfoVo getParseMoveFolder(Map<String, Object> param){
		MoveInfoVo moveinfovo = new MoveInfoVo();
		JsonParser jparser = new JsonParser(); 
		try {
			JsonElement moveObj = jparser.parse(getParam(param, "moveObj", "S"));

			moveinfovo.setTgt_dbid(Integer.parseInt(getParam(param, "tgt_dbid", "I")));
			moveinfovo.setClick_dbid(Integer.parseInt(getParam(param, "click_dbid", "I")));
			moveinfovo.setMoveObj(moveObj);

		}catch(Exception ex){
			logger.error("Exception", ex);
		}
		return moveinfovo;
	}

	private MoveInfoVo getParseMovePerson(Map<String, Object> param){
		MoveInfoVo moveinfovo = new MoveInfoVo();
		JsonParser jparser = new JsonParser(); 
		try {
			JsonElement moveObj = jparser.parse(getParam(param, "moveObj", "S"));

			moveinfovo.setTgt_name(getParam(param, "tgt_name", "S"));
			moveinfovo.setClick_dbid(Integer.parseInt(getParam(param, "click_dbid", "I")));
			moveinfovo.setMoveObj(moveObj);

		}catch(Exception ex){
			logger.error("Exception", ex);
		}
		return moveinfovo;
	}

	@RequestMapping("/group/moveFolder")
	public JSONObject moveFolder(@RequestBody Map<String, Object> param) throws Exception {
		MoveInfoVo moveinfovo = new MoveInfoVo();
		CfgFolder folder = null;
		JSONObject rtnobj = new JSONObject();
		try {
			moveinfovo = getParseMoveFolder(param);
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgfolder = new ConfigFolder();
				folder = cfgfolder.moveFolder(initconfigservice.service, moveinfovo.getMoveObj(), moveinfovo.getTgt_dbid());
				if(!folder.equals(null)){
					folder = cfgfolder.getFolderInfo(initconfigservice.service, 22, moveinfovo.getClick_dbid());
					rtnobj = convertFolderInfo_jsonObj(folder);
				}
				// map = JsonUtil.getMapFromJsonObject(rtnobj);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "moveFolder");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	@RequestMapping("/group/assignPerson")
	public JSONObject assignPerson(@RequestBody Map<String, Object> param) throws Exception {
		MoveInfoVo moveinfovo = new MoveInfoVo();
		CfgAgentGroup agentgroup = null;
		JSONObject rtnobj = new JSONObject();
		try {
			moveinfovo = getParseMovePerson(param);
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				ag = new ConfigAgentGroup();
				agentgroup = ag.assignPersonToAgentGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), moveinfovo.getMoveObj(), moveinfovo.getTgt_name());

				if(!agentgroup.equals(null)){
					agentgroup = ag.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), moveinfovo.getClick_dbid());
					rtnobj = convertGroupInfo_jsonObj(agentgroup);
				}
				// map = JsonUtil.getMapFromJsonObject(rtnobj);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "movePerson");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	@RequestMapping("/group/unAssignPerson")
	public JSONObject unAssignPerson(@RequestBody Map<String, Object> param) throws Exception {
		MoveInfoVo moveinfovo = new MoveInfoVo();
		CfgAgentGroup agentgroup = null;
		JSONObject rtnobj = new JSONObject();
		try {
			moveinfovo = getParseMovePerson(param);
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				ag = new ConfigAgentGroup();
				agentgroup = ag.unAssignPersonToAgentGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), moveinfovo.getMoveObj(), moveinfovo.getTgt_name());

				if(!agentgroup.equals(null)){
					rtnobj = convertGroupInfo_jsonObj(agentgroup);
				}
				// map = JsonUtil.getMapFromJsonObject(rtnobj);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "unAssignPerson");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	private String getParam(Map<String, Object> param, String qryStr, String type){
		String rtnstr = "";
		if(type.equals("I")){
			rtnstr = "0";
		}
		try{
			rtnstr = param.get(qryStr).toString();
		}catch(Exception e){ 
			//rtnstr = "";
		}
		return rtnstr;
	}

}
