package kr.co.i4way.genesys.controller;

import kr.co.i4way.genesys.cfgserver.ConfigCommon;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLoginInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.objects.CfgObjectID;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.co.i4way.genesys.cfgserver.ConfigAgentGroup;
import kr.co.i4way.genesys.cfgserver.ConfigAgentLogin;
import kr.co.i4way.genesys.cfgserver.ConfigFolder;
import kr.co.i4way.genesys.cfgserver.ConfigPerson;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.FolderInfoVo;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.MoveInfoVo;
import kr.co.i4way.genesys.model.PersonInfoVo;
import kr.co.i4way.genesys.model.QueryVo;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "*")
public class PersonController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private GenesysInfoVo genesysinfovo;
	initConfigService initconfigservice = initConfigService.getInstance();
	ConfigPerson cfgperson = null;
	ConfigFolder cfgfolder = null;
	ConfigAgentLogin cfgagentlogin = null;
	ConfigAgentGroup cfgagentgroup = null;
	ConfigCommon configcommon = null;

	public PersonController(){
		configcommon = new ConfigCommon();
	} 

	@PostMapping("/person/getSkillInfo")
	public JSONObject getSkillInfo(@RequestBody QueryVo vo) throws Exception {
		CfgPerson person = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgperson = new ConfigPerson();
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(),
						vo.getQry_type(), vo.getQry_str());
				rtnobj = convertPersonInfo_jsonObj(person);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getPersonInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	@PostMapping("/person/getPersonInfo")
	public JSONObject getPersonInfo(@RequestBody QueryVo vo) throws Exception {
		CfgPerson person = null;
		Collection<CfgAgentGroup> groups = null;
		JSONObject rtnobj = new JSONObject();
		System.out.println("PASS2"+ genesysinfovo.toString());
		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgperson = new ConfigPerson();
				cfgagentgroup = new ConfigAgentGroup();
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(),
						vo.getQry_type(), vo.getQry_str());
				if(person != null){
					groups = cfgagentgroup.getPersonDependency(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), person.getDBID());
				}
				rtnobj = convertPersonInfo_jsonObj(person);
				if(groups != null){
					rtnobj.put("dependency",getDependencyInfo(groups));
				}
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getPersonInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	private JSONObject getDependencyInfo(Collection<CfgAgentGroup> groups) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();

		if (groups != null) {
			for (CfgAgentGroup group : groups) {
				jsonObj = new JSONObject();
				jsonObj.put("dbid", group.getDBID());
				jsonObj.put("name", group.getGroupInfo().getName());
				jsonObj.put("state", group.getGroupInfo().getState().ordinal());
				jsonObj.put("obj", "CfgAgentGroup");
				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("agentgroups", jsonArray);
		}
		return finalJsonObj;
	}

	private JSONObject getPersonInfo(int dbid) throws Exception {
		CfgPerson person = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgperson = new ConfigPerson();
				person = cfgperson.getPersonInfo_single(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(),
						dbid);
				rtnobj = convertPersonInfo_jsonObj(person);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getPersonInfo");
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

	@RequestMapping("/person/getAllPersonInfo")
	public JSONObject getAllPersonInfo() throws Exception {
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgperson = new ConfigPerson();
				Collection<CfgPerson> persons = cfgperson.getPersonInfo(initconfigservice.service,
						genesysinfovo.getCfg_tenant_dbid());
				rtnobj = convertPersonInfo_jsonObj(persons);

				// map = JsonUtil.getMapFromJsonObject(rtnobj);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getAllPersonInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	@RequestMapping("/person/getFolderInfo")
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

	@RequestMapping("/person/getFolderInfo_tree")
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

	@RequestMapping("/person/moveFolder")
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

		/*
		FolderInfoVo folderinfovo = new FolderInfoVo();
		CfgFolder folder = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgfolder = new ConfigFolder();
				folder = cfgfolder.moveFolder(initconfigservice.service, vo.getSrc_obj(), vo.getSrc_dbid(), vo.getTgt_dbid());
				if(!folder.equals(null)){
					folder = cfgfolder.getFolderInfo(initconfigservice.service, 22, vo.getClick_dbid());
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
*/
	}

	@RequestMapping("/person/checkPerson")
	public JSONObject checkPerson(@RequestBody QueryVo vo) throws Exception {
		CfgPerson person = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgperson = new ConfigPerson();
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(),
						vo.getQry_type(), vo.getQry_str());
				if (person == null) {
					rtnobj.put("result", false);
				} else {
					rtnobj.put("result", true);
				}
			}
			rtnobj.put("qry_type", vo.getQry_type());
			rtnobj.put("qry_str", vo.getQry_str());
			rtnobj.put("command", "checkPerson");
		} catch (Exception e) {
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	private PersonInfoVo getParsePersonInfo(Map<String, Object> param){
		PersonInfoVo personinfovo = new PersonInfoVo();
		// jsonPaserPser 클래스 객체를 만들고 해당 객체에 
		JsonParser jparser = new JsonParser();
		
		try {

			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement skills = jparser.parse(getParam(param, "skills", "S"));
			JsonElement agentLogins = jparser.parse(getParam(param, "agentLogins", "S"));
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));

			personinfovo.setFirstName(getParam(param, "firstName", "S"));
			personinfovo.setLastName(getParam(param, "lastName", "S"));
			personinfovo.setIsAgent(Integer.parseInt(getParam(param, "isAgent", "I")));
			personinfovo.setAutoMakeAgentLogin(getParam(param, "autoMakeAgentLogin", "S"));
			personinfovo.setDbid(Integer.parseInt(getParam(param, "dbid", "I")));
			personinfovo.setEmployeeId(getParam(param, "employeeId", "S"));
			personinfovo.setState(Integer.parseInt(getParam(param, "state", "I")));
			personinfovo.setUserName(getParam(param, "userName", "S"));
			personinfovo.setFolderId(Integer.parseInt(getParam(param, "folderId", "I")));
			personinfovo.setAgentGroupName(getParam(param, "agentGroupName", "S"));

			personinfovo.setSkills(skills);
			personinfovo.setAgentLogins(agentLogins);
			personinfovo.setAnnex(annex);
		}catch(Exception ex){
			personinfovo = null;
		}
		return personinfovo;
	}

	
	private FolderInfoVo getParseFolderInfo(Map<String, Object> param){
		FolderInfoVo folderinfovo = new FolderInfoVo();
		// jsonPaserPser 클래스 객체를 만들고 해당 객체에 
		JsonParser jparser = new JsonParser();
		
		try {

			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));

			folderinfovo.setOwnerType(Integer.parseInt(getParam(param, "ownerType", "I")));
			folderinfovo.setOwnerDbid(Integer.parseInt(getParam(param, "ownerDbid", "I")));
			folderinfovo.setFolderId(Integer.parseInt(getParam(param, "folderId", "I")));
			folderinfovo.setObjectType(Integer.parseInt(getParam(param, "objectType", "I")));
			folderinfovo.setType(Integer.parseInt(getParam(param, "type", "I")));
			folderinfovo.setDbid(Integer.parseInt(getParam(param, "dbid", "I")));
			folderinfovo.setName(getParam(param, "name", "S"));
			folderinfovo.setState(Integer.parseInt(getParam(param, "state", "I")));

			folderinfovo.setAnnex(annex);
		}catch(Exception ex){
			folderinfovo = null;
		}
		return folderinfovo;
	}

	@RequestMapping("/person/createFolderInfo")
	public JSONObject createFolderInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		FolderInfoVo folderinfovo = null;
		CfgFolder folder = null;
		try {
			folderinfovo = getParseFolderInfo(param);
			cfgfolder = new ConfigFolder();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(folderinfovo.getDbid() > 0){
					folder = cfgfolder.getFolderInfo(initconfigservice.service, folderinfovo.getOwnerType(), folderinfovo.getDbid());
				}

				if (folder != null) { // 수정모드
					folder = cfgfolder.modifyFolder(initconfigservice.service, folder, folderinfovo);
					logger.info("Modify folder~"); 
				} else { 
					folder = cfgfolder.createFolder(initconfigservice.service, folderinfovo);
					logger.info("Create folder~");
				}
				rtnobj = convertFolderInfo_jsonObj(folder);
			}
			rtnobj.put("command", "createFolderInfo");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/modifyFolderInfo")
	public JSONObject modifyFolderInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		FolderInfoVo folderinfovo = null;
		CfgFolder folder = null;
		try {
			folderinfovo = getParseFolderInfo(param);
			cfgfolder = new ConfigFolder();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(folderinfovo.getDbid() > 0){
					folder = cfgfolder.getFolderInfo(initconfigservice.service, folderinfovo.getOwnerType(), folderinfovo.getDbid());
				}

				if (folder != null) { // 수정모드
					folder = cfgfolder.modifyFolder(initconfigservice.service, folder, folderinfovo);
					logger.info("Modify folder~"); 
				} else { 
					folder = cfgfolder.createFolder(initconfigservice.service, folderinfovo);
					logger.info("Create folder~");
				}
				rtnobj = convertFolderInfo_jsonObj(folder);
			}
			rtnobj.put("command", "createFolderInfo");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/deleteFolderInfo")
	public JSONObject deleteFolderInfo(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		boolean del_folder_result = false;
		CfgFolder folder = null;

		try {
			cfgfolder = new ConfigFolder();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				folder = cfgfolder.getFolderInfo(initconfigservice.service, vo.getFolder_type(), vo.getFolder_dbid());

				if (folder != null) { // 삭제
					Collection<CfgObjectID> cfgobjects = folder.getObjectIDs();
					if (cfgobjects.size() == 0) {
						logger.info("Delete Person~");
						del_folder_result = cfgfolder.deleteFolder(initconfigservice.service, folder);
					}else{
						del_folder_result = false;
					}
				}
			}
			rtnobj.put("command", "deleteFolderInfo");
			rtnobj.put("del_result", del_folder_result);
			rtnobj.put("folder_type", vo.getFolder_type());
			rtnobj.put("dbid", vo.getFolder_dbid());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/setFolderState")
	public JSONObject setFolderState(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgFolder folder = null;

		try {
			cfgfolder = new ConfigFolder();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				folder = cfgfolder.getFolderInfo(initconfigservice.service, vo.getFolder_type(), vo.getFolder_dbid());

				if (folder != null) { // person이 널이 아니면
					logger.info("set folder State~");
					if(folder.getState().equals(CfgObjectState.CFGEnabled)){
						folder = cfgfolder.setFolderState(initconfigservice.service, folder, true);
					}else if(folder.getState().equals(CfgObjectState.CFGDisabled)){
						folder = cfgfolder.setFolderState(initconfigservice.service, folder, false);
					}
				}
			}
			rtnobj.put("command", "setFolderState");
			rtnobj.put("state", folder.getState().name());
			rtnobj.put("name", folder.getName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/createPersonInfo")
	public JSONObject createPersonInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		PersonInfoVo personinfovo = null;
		CfgPerson person = null;

		try {
			personinfovo = getParsePersonInfo(param);
			//logger.info("personinfovo == > " + personinfovo.toString());
			cfgperson = new ConfigPerson();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(personinfovo.getDbid() > 0){
					person = cfgperson.getPersonInfo_single(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), personinfovo.getDbid());
				}

				if (person != null) { // 수정모드
					person = cfgperson.modifyPerson(initconfigservice.service, genesysinfovo.getCfg_switch_dbid(), genesysinfovo.getCfg_tenant_dbid(),
					person, personinfovo);
					logger.info("Modify Person~"); 
				} else {
					person = cfgperson.createPerson(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(),
							genesysinfovo.getCfg_switch_dbid(), personinfovo);
					logger.info("Create Person~");
				}
				rtnobj = convertPersonInfo_jsonObj(person);
			}
			rtnobj.put("command", "createPersonInfo");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/modifyPersonInfo")
	public JSONObject modifyPersonInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		PersonInfoVo personinfovo = null;
		CfgPerson person = null;

		try {
			personinfovo = getParsePersonInfo(param);
			cfgperson = new ConfigPerson();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(personinfovo.getDbid() > 0){
					person = cfgperson.getPersonInfo_single(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), personinfovo.getDbid());
				}

				if (person != null) { // 수정모드
					person = cfgperson.modifyPerson(initconfigservice.service, genesysinfovo.getCfg_switch_dbid(), genesysinfovo.getCfg_tenant_dbid(),
					person, personinfovo);
					logger.info("Modify Person~"); 
				} else {
					person = cfgperson.createPerson(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(),
							genesysinfovo.getCfg_switch_dbid(), personinfovo);
							logger.info("Create Person~");
					}
				rtnobj = convertPersonInfo_jsonObj(person);
			}
			rtnobj.put("command", "modifyPersonInfo");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/deletePersonInfo")
	public JSONObject deletePersonInfo(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		boolean del_login_result = false;
		boolean del_person_result = false;
		CfgPerson person = null;

		try {
			cfgperson = new ConfigPerson();
			cfgagentlogin = new ConfigAgentLogin();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getQry_type(), vo.getQry_str());

				if (person != null) { // 삭제
					if (person.getAgentInfo().getAgentLogins() != null) {
						Collection<CfgAgentLoginInfo> aglo = person.getAgentInfo().getAgentLogins();
						for (CfgAgentLoginInfo al : aglo) {
							cfgagentlogin.deleteAgentLogin(initconfigservice.service, al.getAgentLogin());
						}
					}
					logger.info("Delete Person~");
					del_person_result = cfgperson.deletePerson(initconfigservice.service, person);
				}
			}
			rtnobj.put("command", "deletePersonInfo");
			rtnobj.put("del_login_result", del_login_result);
			rtnobj.put("del_result", del_person_result);
			rtnobj.put("dbid", vo.getSrc_dbid());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/setPersonState")
	public JSONObject setPersonState(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgPerson person = null;

		try {
			cfgperson = new ConfigPerson();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getQry_type(), vo.getQry_str());

				if (person != null) { // person이 널이 아니면
					logger.info("set Person State~");
					if(person.getState().equals(CfgObjectState.CFGEnabled)){
						person = cfgperson.setPersonState(initconfigservice.service, person, false);
					}else if(person.getState().equals(CfgObjectState.CFGDisabled)){
						person = cfgperson.setPersonState(initconfigservice.service, person, true);
					}
				}
			}
			rtnobj.put("command", "setPersonState");
			rtnobj.put("state", person.getState().name());
			rtnobj.put("employee_id", person.getEmployeeID());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/setAgentloginToPerson")
	public JSONObject setAgentloginToPerson(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgPerson person = null;
		boolean assign_result = false;
		try {
			cfgperson = new ConfigPerson();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), "emp",
				vo.getEmployee_id());

				if (person != null) { // person이 널이 아니면
					logger.info("set Agent Login To Person~");
					if (cfgperson.setAgentLoginToPerson(initconfigservice.service, person,
							genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getAgent_login_id())) {
						assign_result = true;
					}
				}
			}
			rtnobj.put("command", "setAgentloginToPerson");
			rtnobj.put("result", assign_result);
			rtnobj.put("agent_login_id", vo.getAgent_login_id());
			rtnobj.put("employee_id", vo.getEmployee_id());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/removeAgentloginToPerson")
	public JSONObject removeAgentloginToPerson(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgPerson person = null;
		boolean remove_result = false;

		try {
			cfgperson = new ConfigPerson();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), "emp",
						vo.getEmployee_id());

				if (person != null) { // person이 널이 아니면
					logger.info("remove Agent Login To Person~");
					if (cfgperson.removeAgentLoginToPerson(initconfigservice.service, person,
							genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getAgent_login_id())) {
						remove_result = true;
					}
				}
			}
			rtnobj.put("command", "removeAgentloginToPerson");
			rtnobj.put("result", remove_result);
			rtnobj.put("employee_id", vo.getEmployee_id());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/setSkillsToPerson")
	public JSONObject setSkillToPerson(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgPerson person = null;
		boolean assign_result = false;
		try {
			String[] skillArry = vo.getSkill_dbids().split(",");
			String[] levelArry = vo.getSkill_levels().split(",");
			cfgperson = new ConfigPerson();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), "emp",
						vo.getEmployee_id());

				if (person != null) { // person이 널이 아니면
					logger.info("set Skill To Person~");
					if (cfgperson.setSkillsToPerson(initconfigservice.service, person, skillArry, levelArry)) {
						assign_result = true;
					}
				}
			}
			rtnobj.put("command", "setSkillsToPerson");
			rtnobj.put("resault", assign_result);
			rtnobj.put("skill_dbids", vo.getSkill_dbids());
			rtnobj.put("skill_levels", vo.getSkill_levels());
			rtnobj.put("employee_id", vo.getEmployee_id());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/person/removeSkillsToPerson")
	public JSONObject removeSkillsToPerson(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgPerson person = null;
		boolean remove_result = false;

		try {
			String[] skillArry = vo.getSkill_dbids().split(",");
			String[] levelArry = vo.getSkill_levels().split(",");
			cfgperson = new ConfigPerson();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), "emp",
					vo.getEmployee_id());

				if (person != null) { // person이 널이 아니면
					logger.info("set Skill To Person~");
					if (cfgperson.removeSkillsToPerson(initconfigservice.service, person, skillArry, levelArry)) {
						remove_result = true;
					}
				}
			}
			rtnobj.put("command", "removeSkillsToPerson");
			rtnobj.put("resault", remove_result);
			rtnobj.put("skill_dbids", vo.getSkill_dbids());
			rtnobj.put("skill_levels", vo.getSkill_levels());
			rtnobj.put("employee_id", vo.getEmployee_id());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	private JSONObject convertPersonInfo_jsonObj(CfgPerson person) {
		JSONObject finalJsonObj = new JSONObject();

		if (person != null) {
			if (person.getIsAgent() == CfgFlag.CFGTrue) {
				finalJsonObj.put("skills", configcommon.getCfgSkillLevelCollectionInfo(person.getAgentInfo().getSkillLevels()));
				finalJsonObj.put("agentLogins", configcommon.getCfgAgentInfoCollectionInfo(person.getAgentInfo().getAgentLogins()));
			}

			finalJsonObj.put("dbid", person.getDBID());
			finalJsonObj.put("firstName", person.getFirstName());
			finalJsonObj.put("lastName", person.getLastName());
			finalJsonObj.put("userName", person.getUserName());
			finalJsonObj.put("employeeId", person.getEmployeeID());
			finalJsonObj.put("isAgent", person.getIsAgent().ordinal());
			finalJsonObj.put("state", person.getState().ordinal());
			finalJsonObj.put("objectPath", person.getObjectPath());
			finalJsonObj.put("folderId", person.getFolderId());
			finalJsonObj.put("annex", configcommon.getAnnexInfo(person.getUserProperties()));
			finalJsonObj.put("obj", "CfgPerson");
		}
		return finalJsonObj;
	}

	private JSONObject convertPersonInfo_jsonObj(Collection<CfgPerson> persons) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();

		if (persons != null) {
			for (CfgPerson person : persons) {
				jsonObj = new JSONObject();
				if (person.getIsAgent() == CfgFlag.CFGTrue) {
					jsonObj.put("skills", configcommon.getCfgSkillLevelCollectionInfo(person.getAgentInfo().getSkillLevels()));
					jsonObj.put("agentLogins", configcommon.getCfgAgentInfoCollectionInfo(person.getAgentInfo().getAgentLogins()));
				}
				jsonObj.put("dbid", person.getDBID());
				jsonObj.put("firstName", person.getFirstName());
				jsonObj.put("lastName", person.getLastName());
				jsonObj.put("userName", person.getUserName());
				jsonObj.put("employeeId", person.getEmployeeID());
				jsonObj.put("isAgent", person.getIsAgent().ordinal());
				jsonObj.put("state", person.getState().ordinal());
				jsonObj.put("objectPath", person.getObjectPath());
				jsonObj.put("folderId", person.getFolderId());
				jsonObj.put("annex", configcommon.getAnnexInfo(person.getUserProperties()));
				jsonObj.put("obj", "CfgPerson");
				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("persons", jsonArray);
		}
		return finalJsonObj;
	}

	public JSONArray getCfgObjectIDCollectionInfo(Collection<CfgObjectID> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		try {
			for (CfgObjectID cfgobjectid : coll) {
				jsonObj = new JSONObject();
				if (cfgobjectid.getType().name().equals("CFGPerson")) {
					jsonObj = getPersonInfo(cfgobjectid.getDBID());
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
				if (cfgobjectid.getType().name().equals("CFGPerson")) {
				} else if (cfgobjectid.getType().name().equals("CFGFolder")) {
					jsonObj = getFolderInfo_tree(cfgobjectid.getType().ordinal(), cfgobjectid.getDBID());
					jsonObj.put("id", cfgobjectid.getDBID());
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
			finalJsonObj.put("id", folder.getDBID());
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
				//finalJsonObj.put("children", new JSONArray());
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