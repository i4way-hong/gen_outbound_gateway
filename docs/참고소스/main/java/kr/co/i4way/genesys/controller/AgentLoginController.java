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

import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLogin;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLoginInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.objects.CfgObjectID;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSwitch;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.co.i4way.genesys.cfgserver.ConfigAgentLogin;
import kr.co.i4way.genesys.cfgserver.ConfigCommon;
import kr.co.i4way.genesys.cfgserver.ConfigFolder;
import kr.co.i4way.genesys.cfgserver.ConfigPerson;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.AgentLoginInfoVo;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.MoveInfoVo;
import kr.co.i4way.genesys.model.QueryVo;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * AgentLogin용 Controller
 * @author jkhong
 *
 */
@RestController
@CrossOrigin(origins = "*")
public class AgentLoginController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private GenesysInfoVo genesysinfovo;
	initConfigService initconfigservice = initConfigService.getInstance();
	ConfigAgentLogin cfgagentlogin = null;
	ConfigPerson cfgperson = null;
	ConfigFolder cfgfolder = null;
	ConfigCommon configcommon = null;
	Collection<CfgAgentLogin> llgs = null;

	public AgentLoginController(){
		configcommon = new ConfigCommon();
	}
	
	/**
	 * CfgPerson에 Assign되어있는 AgentLogin정보를 조회한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/agentLogin/getPersonAgentLoginInfo")
	public JSONObject getPersonAgentLoginInfo(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgAgentLoginInfo> agentlogininfo = null;
		JSONObject rtnobj = new JSONObject();
		//Map<String, Object> map = new HashMap<>(); 

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentlogin = new ConfigAgentLogin();
				agentlogininfo = cfgagentlogin.getAgentLoginInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getEmployee_id());
				rtnobj = convertAgentLoginInfos_jsonObj(agentlogininfo);
				//map = JsonUtil.getMapFromJsonObject(rtnobj);
			}
			rtnobj.put("command", "getPersonAgentLoginInfo");
			rtnobj.put("employee_id", vo.getEmployee_id());
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	
	
	/**
	 * 전체CfgSwitch 정보를 조회한다.
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/agentLogin/getSwitchs")
	public JSONObject getSwitchs(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgSwitch> switchs = null;
		JSONObject rtnobj = new JSONObject();
		//Map<String, Object> map = new HashMap<>(); 

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentlogin = new ConfigAgentLogin();
				switchs = cfgagentlogin.getSwitchs(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid());
				rtnobj = convertSwitchs_jsonObj(switchs);
				//map = JsonUtil.getMapFromJsonObject(rtnobj);
			}		
			rtnobj.put("command", "getSwitchs");
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	

	/**
	 * 전체CfgAgentLogin 정보를 조회한다.
	 * @param request("assignable") : Assign여부 true : assign된 Object
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/agentLogin/getAgentLogins")
	public JSONObject getAgentLogins(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgAgentLogin> agentlogininfo = null;
		JSONObject rtnobj = new JSONObject();
		//Map<String, Object> map = new HashMap<>(); 

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentlogin = new ConfigAgentLogin();
				agentlogininfo = cfgagentlogin.getAgentLogins(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getAssignable());
				rtnobj = convertAgentLogins_jsonObj(agentlogininfo);
				//map = JsonUtil.getMapFromJsonObject(rtnobj);
			}		
			rtnobj.put("command", "getAgentLogins");
			rtnobj.put("assignable", vo.getAssignable());
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	
	
	/**
	 * CfgAgentLogin 정보를 조회한다.
	 * @param request("qry_type") : 쿼리타입 loginCode : loginCode로 쿼리, dbid : dbid로 쿼리
	 * @param request("qry_str") : Agent Login ID
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/agentLogin/getAgentLogin")
	public JSONObject getAgentLogin(@RequestBody QueryVo vo) throws Exception{
		CfgAgentLogin agentlogininfo = null;
		Collection<CfgPerson> persons = null;
		JSONObject rtnobj = new JSONObject();
		//Map<String, Object> map = new HashMap<>(); 

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentlogin = new ConfigAgentLogin();
				cfgperson = new ConfigPerson();
				if(vo.getQry_type().equals("loginCode")) {
					agentlogininfo = cfgagentlogin.getAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getQry_str());
					persons = cfgperson.getAgentLoginDependency(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentlogininfo.getSwitchDBID(), agentlogininfo.getDBID());
				}else if(vo.getQry_type().equals("dbid")) {
					agentlogininfo = cfgagentlogin.getAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getAgent_login_id(), Integer.parseInt(vo.getQry_str()));
					persons = cfgperson.getAgentLoginDependency(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentlogininfo.getSwitchDBID(), agentlogininfo.getDBID());
				}
				rtnobj = convertAgentLogin_jsonObj(agentlogininfo);
				rtnobj.put("command", "getAgentLogin");
				rtnobj.put("qry_type", vo.getQry_type());
				rtnobj.put("qry_str", vo.getQry_str());
				rtnobj.put("agent_login_id", vo.getAgent_login_id());
				if(persons != null){
					rtnobj.put("dependency",getDependencyInfo(persons));
				}
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	

	private JSONObject getDependencyInfo(Collection<CfgPerson> persons) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();

		if (persons != null) {
			for (CfgPerson person : persons) {
				jsonObj = new JSONObject();
				jsonObj.put("dbid", person.getDBID());
				jsonObj.put("firstName", person.getFirstName());
				jsonObj.put("lastName", person.getLastName());
				jsonObj.put("userName", person.getUserName());
				jsonObj.put("employeeId", person.getEmployeeID());
				jsonObj.put("isAgent", person.getIsAgent().ordinal());
				jsonObj.put("state", person.getState().ordinal());
				jsonObj.put("obj", "CfgPerson");
				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("persons", jsonArray);
		}
		return finalJsonObj;
	}

	private JSONObject getAgentLogin(int dbid, String agent_login_id) throws Exception {
		CfgAgentLogin agentlogin = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentlogin = new ConfigAgentLogin();
				agentlogin = cfgagentlogin.getAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), agent_login_id, dbid);
				rtnobj = convertAgentLogin_jsonObj(agentlogin);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getSkillInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}
	

	@RequestMapping("/agentLogin/checkAgentLogin")
	public JSONObject checkAgentLogin(@RequestBody QueryVo vo) throws Exception{
		CfgAgentLogin agentlogininfo = null;
		JSONObject rtnobj = new JSONObject();
		//Map<String, Object> map = new HashMap<>(); 
		String warpuptm = "";

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentlogin = new ConfigAgentLogin();
				agentlogininfo = cfgagentlogin.getAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getAgent_login_id());
				rtnobj.put("command", "checkAgentLogin");
				if(agentlogininfo != null){
					KeyValueCollection appOptions = agentlogininfo.getUserProperties();
						
					for(Object selectionObj : appOptions){
						KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
						
				        for (Object recordObj : sectionKvp.getTKVValue()) {
				            KeyValuePair recordKvp = (KeyValuePair) recordObj;
				            if(recordKvp.getStringKey().equals("wrap-up-time")){
				            	warpuptm = recordKvp.getStringValue();
				            }
				        }
					}
					if(warpuptm.equals("")){
						rtnobj.put("result", false);
				  	}else{
				  		rtnobj.put("result", true);
				  	}
					rtnobj.put("loginCode", agentlogininfo.getLoginCode());
				}else {
					rtnobj.put("result", false);
					rtnobj.put("loginCode", null);
				}
				
			}			
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	
	
	@RequestMapping("/agentLogin/createAgentLogin")
	public JSONObject createAgentLogin(@RequestBody QueryVo vo) throws Exception{
		CfgAgentLogin agentlogininfo = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentlogin = new ConfigAgentLogin();
				agentlogininfo = cfgagentlogin.createAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getAgent_login_id());
				//rtnobj = convertAgentLogins_jsonObj(agentlogininfo);				
			}
			rtnobj.put("command", "checkAgentLogin");
			rtnobj.put("login_code", vo.getAgent_login_id());
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/agentLogin/createAgentLoginInfo")
	public JSONObject createAgentLoginInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgAgentLogin agentlogininfo = null;
		AgentLoginInfoVo agentlogininfovo = null;
		ConfigAgentLogin cfgagentlogin = null;

		try {
			agentlogininfovo = getParseAgentLoginInfo(param);
			cfgagentlogin = new ConfigAgentLogin();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				agentlogininfo = cfgagentlogin.getAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentlogininfovo.getSwitch_dbid(), agentlogininfovo.getCode());

				if (agentlogininfo != null) { // 수정모드
					agentlogininfo = cfgagentlogin.modifyAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), 
					agentlogininfo, agentlogininfovo);
					logger.info("Modify AgentLogin~"); 
				} else {
					agentlogininfo = cfgagentlogin.createAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(),  agentlogininfovo);
					logger.info("Create AgentLogin~");
				}
				rtnobj = convertAgentLogin_jsonObj(agentlogininfo);
			}
			rtnobj.put("command", "createAgentLoginInfo");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/agentLogin/modifyAgentLoginInfo")
	public JSONObject modifyAgentLoginInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgAgentLogin agentlogininfo = null;
		AgentLoginInfoVo agentlogininfovo = null;
		ConfigAgentLogin cfgagentlogin = null;

		try {
			agentlogininfovo = getParseAgentLoginInfo(param);
			cfgagentlogin = new ConfigAgentLogin();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				agentlogininfo = cfgagentlogin.getAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentlogininfovo.getSwitch_dbid(), agentlogininfovo.getCode());

				if (agentlogininfo != null) { // 수정모드
					agentlogininfo = cfgagentlogin.modifyAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), 
					agentlogininfo, agentlogininfovo);
					logger.info("Modify AgentLogin~"); 
				} else {
					agentlogininfo = cfgagentlogin.createAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(),  agentlogininfovo);
					logger.info("Create AgentLogin~");

				}
				rtnobj = convertAgentLogin_jsonObj(agentlogininfo);
			}
			rtnobj.put("command", "modifyAgentLoginInfo");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/agentLogin/deleteAgentLoginInfo")
	public JSONObject deleteAgentLoginInfo(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		boolean del_agentlogin_result = false;
		CfgAgentLogin agentlogin = null;

		try {
			cfgagentlogin = new ConfigAgentLogin();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				agentlogin = cfgagentlogin.getAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getAgent_login_id());

				if (agentlogin != null) { // 삭제
					logger.info("Delete AgentLogin~");
					del_agentlogin_result = cfgagentlogin.deleteAgentLogin(initconfigservice.service, agentlogin);
				}
			}
			rtnobj.put("command", "deleteAgentLoginInfo");
			rtnobj.put("del_result", del_agentlogin_result);
			rtnobj.put("agent_login_id", vo.getAgent_login_id());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	private AgentLoginInfoVo getParseAgentLoginInfo(Map<String, Object> param){
		AgentLoginInfoVo agentlogininfovo = new AgentLoginInfoVo();
		// jsonPaserPser 클래스 객체를 만들고 해당 객체에 
		JsonParser jparser = new JsonParser();
		
		try {

			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));

			agentlogininfovo.setCode(getParam(param, "code", "S"));
			agentlogininfovo.setDbid(Integer.parseInt(getParam(param, "dbid", "I")));
			agentlogininfovo.setState(Integer.parseInt(getParam(param, "state", "I")));
			agentlogininfovo.setFolderId(Integer.parseInt(getParam(param, "folderId", "I")));
			agentlogininfovo.setSwitch_dbid(Integer.parseInt(getParam(param, "switch_dbid", "I")));
			agentlogininfovo.setSwitchSpecificType(Integer.parseInt(getParam(param, "switchSpecificType", "I")));

			agentlogininfovo.setAnnex(annex);
		}catch(Exception ex){
			agentlogininfovo = null;
		}
		return agentlogininfovo;
	}


	@RequestMapping("/agentLogin/setAgentLoginState")
	public JSONObject setAgentLoginState(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgAgentLogin agentlogin = null;

		try {
			cfgagentlogin = new ConfigAgentLogin();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				agentlogin = cfgagentlogin.getAgentLogin(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getAgent_login_id(), vo.getFolder_dbid());

				if (agentlogin != null) { // agentlogin 널이 아니면
					logger.info("set agentlogin State~");
					if(agentlogin.getState().equals(CfgObjectState.CFGEnabled)){
						agentlogin = cfgagentlogin.setAgentLoginState(initconfigservice.service, agentlogin, false);
					}else if(agentlogin.getState().equals(CfgObjectState.CFGDisabled)){
						agentlogin = cfgagentlogin.setAgentLoginState(initconfigservice.service, agentlogin, true);
					}
				}
			}
			rtnobj.put("command", "setAgentLoginState");
			rtnobj.put("state", agentlogin.getState().name());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}
	
	

	private JSONObject convertSwitchs_jsonObj(Collection<CfgSwitch> switchs) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		
		if(switchs != null) {
			for (CfgSwitch sw : switchs) {
				jsonObj = new JSONObject();
				jsonObj.put("dbid", sw.getDBID());
				jsonObj.put("tenant_dbid", sw.getTenantDBID());
				jsonObj.put("name", sw.getName());
				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("switchs", jsonArray);
		}else {
			finalJsonObj.put("switchs", null);
		}
		return finalJsonObj;
	}


	private JSONObject convertAgentLogins_jsonObj(Collection<CfgAgentLogin> agentlogins) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		String warpuptm = "";
		
		if(agentlogins != null) {
			for (CfgAgentLogin agentlogin : agentlogins) {
				warpuptm = "";
				jsonObj = new JSONObject();
				jsonObj.put("dbid", agentlogin.getDBID());
				jsonObj.put("loginCode", agentlogin.getLoginCode());
				jsonObj.put("SwitchName", agentlogin.getSwitch().getName());
				jsonObj.put("SwitchDbid", agentlogin.getSwitch().getDBID());
				
				if(agentlogin != null){
					jsonObj.put("annex", configcommon.getAnnexInfo(agentlogin.getUserProperties()));
					KeyValueCollection appOptions = agentlogin.getUserProperties();
						
					for(Object selectionObj : appOptions){
						KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
						
				        for (Object recordObj : sectionKvp.getTKVValue()) {
				            KeyValuePair recordKvp = (KeyValuePair) recordObj;
				            if(recordKvp.getStringKey().equals("wrap-up-time")){
				            	warpuptm = recordKvp.getStringValue();
				            }
				        }
					}
				}
				
				if(warpuptm.equals("")){
					jsonObj.put("isAssign", false);
			  	 }else{
			  		jsonObj.put("isAssign", true);
			  	 }
				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("agentLogins", jsonArray);
		}else {
			finalJsonObj.put("agentLogins", null);
		}
		return finalJsonObj;
	}
	
	private JSONObject convertAgentLogin_jsonObj(CfgAgentLogin agentlogin) {
		JSONObject jsonObj = new JSONObject();
		String warpuptm = "";
		
		if(agentlogin != null) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", agentlogin.getDBID());
			jsonObj.put("loginCode", agentlogin.getLoginCode());
			jsonObj.put("SwitchName", agentlogin.getSwitch().getName());
			jsonObj.put("SwitchDbid", agentlogin.getSwitch().getDBID());
			jsonObj.put("state", agentlogin.getState().ordinal());
			jsonObj.put("obj", "CfgAgentLogin");
			if(agentlogin != null){
				jsonObj.put("annex", configcommon.getAnnexInfo(agentlogin.getUserProperties()));
				KeyValueCollection appOptions = agentlogin.getUserProperties();
					
				for(Object selectionObj : appOptions){
					KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
					
			        for (Object recordObj : sectionKvp.getTKVValue()) {
			            KeyValuePair recordKvp = (KeyValuePair) recordObj;
			            if(recordKvp.getStringKey().equals("wrap-up-time")){
			            	warpuptm = recordKvp.getStringValue();
			            }
			        }
				}
			}
			
			if(warpuptm.equals("")){
				jsonObj.put("isAssign", false);
		  	 }else{
		  		jsonObj.put("isAssign", true);
		  	 }
		}else {
			jsonObj = null;
		}
		return jsonObj;
	}
	
	private JSONObject convertAgentLoginInfos_jsonObj(Collection<CfgAgentLoginInfo> agentlogininfos) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		
		if(agentlogininfos != null) {
			for (CfgAgentLoginInfo agentlogininfo : agentlogininfos) {
				jsonObj = new JSONObject();
				jsonObj.put("dbid", agentlogininfo.getAgentLogin().getDBID());
				jsonObj.put("loginCode", agentlogininfo.getAgentLogin().getLoginCode());
				jsonObj.put("SwitchName", agentlogininfo.getAgentLogin().getSwitch().getName());
				jsonObj.put("SwitchDbid", agentlogininfo.getAgentLogin().getSwitch().getDBID());
	
				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("agentLogins", jsonArray);
		}else {
			finalJsonObj.put("agentLogins", null);
		}
		return finalJsonObj;
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

	public JSONArray getCfgObjectIDCollectionInfo(Collection<CfgObjectID> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		try {
			for (CfgObjectID cfgobjectid : coll) {
				jsonObj = new JSONObject();
				if (cfgobjectid.getType().name().equals("CFGAgentLogin")) {
					if(llgs != null){
						for (CfgAgentLogin login : llgs) {
							if(login.getDBID().equals(cfgobjectid.getDBID())){
								jsonObj = convertAgentLogin_jsonObj(login);
								break;
							}
						}
					}else{
						//jsonObj = getAgentLogin(cfgobjectid.getDBID(), null);
					}
					jsonObj.put("obj", "CfgAgentLogin");
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

	@RequestMapping("/agentLogin/getFolderInfo")
	public JSONObject getAgentLoginFolderInfo(@RequestBody QueryVo vo) throws Exception {
		CfgFolder folder = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentlogin = new ConfigAgentLogin();
				llgs = cfgagentlogin.getAgentLogins(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), false);
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

	@RequestMapping("/agentLogin/getFolderInfo_tree")
	public JSONObject getAgentLoginFolderInfoTree(@RequestBody QueryVo vo) throws Exception {
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

	@RequestMapping("/agentLogin/moveFolder")
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
