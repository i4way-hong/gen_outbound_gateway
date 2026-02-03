package kr.co.i4way.genesys.controller;

import kr.co.i4way.genesys.cfgserver.ConfigCommon;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.platform.applicationblocks.com.ConfigServerException;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLoginInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.objects.CfgObjectID; 
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genesyslab.platform.reporting.protocol.statserver.AgentGroup;
import com.genesyslab.platform.reporting.protocol.statserver.AgentStatus;
import com.genesyslab.platform.reporting.protocol.statserver.AgentStatusesCollection;
import com.genesyslab.platform.reporting.protocol.statserver.DnStatus;
import com.genesyslab.platform.reporting.protocol.statserver.DnStatusesCollection;
import com.genesyslab.platform.reporting.protocol.statserver.PlaceStatus;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.co.i4way.common.util.JsonUtil;
import kr.co.i4way.genesys.cfgserver.ConfigAgentGroup;
import kr.co.i4way.genesys.cfgserver.ConfigAgentLogin;
import kr.co.i4way.genesys.cfgserver.ConfigFolder;
import kr.co.i4way.genesys.cfgserver.ConfigPerson;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.AgentGroupInfoVo;
import kr.co.i4way.genesys.model.AssignInfoVo;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.HundaiModelVo;
import kr.co.i4way.genesys.model.MoveInfoVo;
import kr.co.i4way.genesys.model.PersonInfoVo; 
import kr.co.i4way.genesys.model.QueryVo;
import kr.co.i4way.genesys.model.SkillStatInfoVo;
import kr.co.i4way.genesys.statserver.StatCtrl;
import kr.co.i4way.genesys.statserver.StatCtrl2;
import kr.co.i4way.genesys.statserver.initStatService;
import kr.co.i4way.util.AES256;
import kr.co.i4way.util.HttpUtil;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@RestController
@CrossOrigin(origins = "*")
public class HyundaiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private GenesysInfoVo genesysinfovo;
	initConfigService initconfigservice = initConfigService.getInstance();
	initStatService initstatservice = initStatService.getInstance();
	ConfigPerson cfgperson = null;
	ConfigFolder cfgfolder = null;
	ConfigAgentLogin cfgagentlogin = null;
	ConfigAgentGroup cfgagentgroup = null;
	ConfigCommon configcommon = null;
	
	@Value("${ccc-service.enc-iv}")
    private String enc_iv;
	
	@Value("${ccc-service.enc-key}")
    private String enc_key;
	
	@Value("${ccc-service.enc-yn}")
    private String enc_yn;
	
	public HyundaiController(){
		configcommon = new ConfigCommon();
	}

	/**
	 * Person정보파싱
	 * @param param
	 * @return
	 */
	private PersonInfoVo getParsePersonInfo(Map<String, Object> param){
		PersonInfoVo personinfovo = new PersonInfoVo();
		JsonParser jparser = new JsonParser();
		
		try {
			JsonElement agentLogins = jparser.parse(getParam(param, "agentLogins", "S"));

			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			personinfovo.setTenant_dbid(Integer.parseInt(getParam(param, "TENANT_DBID", "I")));
			personinfovo.setDbid(Integer.parseInt(getParam(param, "DBID", "I")));
			personinfovo.setFirstName(getParam(param, "FIRST_NAME", "S"));
			personinfovo.setLastName(getParam(param, "LAST_NAME", "S"));
			personinfovo.setUserName(getParam(param, "USER_NAME", "S"));
			personinfovo.setEmployeeId(getParam(param, "EMPLOYEE_ID", "S"));
			personinfovo.setAgentLoginId(getParam(param, "AGENT_LOGINS", "S"));
			personinfovo.setUsePossibleYn(getParam(param, "USE_PSBL_YN", "S"));

			personinfovo.setIsAgent(convertTrueFalse("isagent", getParam(param, "IS_AGENT", "S")));
			personinfovo.setState(convertTrueFalse("state", getParam(param, "STATE", "S")));

			personinfovo.setAutoMakeAgentLogin("true");
			personinfovo.setAgentLogins(agentLogins);
		}catch(Exception ex){
			personinfovo = null;
		}
		return personinfovo;
	}

	/**
	 * AgentGroup정보 파싱
	 * @param param
	 * @return
	 */
	private AgentGroupInfoVo getParseAgentGroupInfo(Map<String, Object> param){
		AgentGroupInfoVo agentgroupinfovo = new AgentGroupInfoVo();
		// jsonPaserPser 클래스 객체를 만들고 해당 객체에 
		JsonParser jparser = new JsonParser();

		try { 
			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));
			agentgroupinfovo.setTenant_dbid(Integer.parseInt(getParam(param, "TENANT_DBID", "I")));
			
			agentgroupinfovo.setName(getParam(param, "NAME", "S"));
			agentgroupinfovo.setDbid(Integer.parseInt(getParam(param, "DBID", "I")));
			agentgroupinfovo.setFolderId(Integer.parseInt(getParam(param, "FOLDER_DBID", "I")));

			agentgroupinfovo.setAnnex(annex);
		}catch(Exception ex){
			agentgroupinfovo = null;
		}
		return agentgroupinfovo;
	}

	/**
	 * AgentGroup간 이동할 Person정보 파싱
	 * @param param
	 * @return
	 */
	private MoveInfoVo getParseMovePerson(Map<String, Object> param){
		MoveInfoVo moveinfovo = new MoveInfoVo();
		JsonParser jparser = new JsonParser();
		try {
			JsonElement empids = jparser.parse(getParam(param, "EMPLOYEE_ID", "S"));
			moveinfovo.setName(getParam(param, "NAME", "S"));
			moveinfovo.setTenant_dbid(Integer.parseInt(getParam(param, "TENANT_DBID", "I")));
			moveinfovo.setEmp_ids(empids);
		}catch(Exception ex){
			logger.error("Exception", ex);
		}
		return moveinfovo;
	}
	
	/**
	 * skill정보 파싱
	 * @param param
	 * @return
	 */
	private SkillStatInfoVo getParseSkillStat(Map<String, Object> param){
		SkillStatInfoVo skillstatinfovo = new SkillStatInfoVo();
		try {
			skillstatinfovo.setTenant_dbid(Integer.parseInt(getParam(param, "TENANT_DBID", "I")));
			skillstatinfovo.setDbid(Integer.parseInt(getParam(param, "DBID", "I")));
			skillstatinfovo.setName(getParam(param, "NAME", "S"));
		}catch(Exception ex){
			logger.error("Exception", ex);
		}
		return skillstatinfovo;
	}

	/**
	 * 상담사 생성 송신 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC1561")
	public JSONObject createPersonInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		PersonInfoVo personinfovo = null;
		HundaiModelVo personmodelvo = null;
		CfgPerson person = null;

		try {
			personinfovo = getParsePersonInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("personinfovo == > " + personinfovo.toString());

			cfgperson = new ConfigPerson();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(personinfovo.getDbid() > 0){
					person = cfgperson.getPersonInfo_single(initconfigservice.service, personinfovo.getTenant_dbid(), personinfovo.getDbid());
				}

				if (person != null) { 
					rtnobj.put("RESULT_MSG", "Person이 존재합니다");
					rtnobj.put("RESULT", "F");
					rtnobj.put("EMPLOYEE_ID", personinfovo.getEmployeeId());
					rtnobj.put("DBID", personinfovo.getDbid());
				} else {
					personmodelvo = cfgperson.createPerson_HD(initconfigservice.service, personinfovo.getTenant_dbid(),
							genesysinfovo.getCfg_switch_dbid(), genesysinfovo.getCfg_switch_dbid2(), personinfovo);
					logger.info("Create Person~");
					rtnobj.put("RESULT_MSG", personmodelvo.getRtnMessage1());
					rtnobj.put("RESULT", personmodelvo.getRtnMessage2());
					rtnobj.put("EMPLOYEE_ID", personmodelvo.getRtnMessage3());
					rtnobj.put("DBID", personmodelvo.getRtnMessage4());
				}
				//rtnobj = convertPersonInfo_jsonObj(person);
			}else{
				rtnobj.put("RESULT_MSG", "Config Server에 연결할 수 없습니다");
				rtnobj.put("RESULT", "F");
				rtnobj.put("EMPLOYEE_ID", "");
				rtnobj.put("DBID", "0");
			}
		} catch (ConfigServerException e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person 생성 실패(ConfigServerException)");
			rtnobj.put("EMPLOYEE_ID", "");
			rtnobj.put("DBID", "");
		}catch (Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person 생성 실패(Exception)");
			rtnobj.put("EMPLOYEE_ID", "");
			rtnobj.put("DBID", "");
		} finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}

	/**
	 * 상담사 수정 송신 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC1571")
	public JSONObject modifyPersonInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		PersonInfoVo personinfovo = null;
		HundaiModelVo personmodelvo = null;
		CfgPerson person = null;

		try {
			personinfovo = getParsePersonInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("personinfovo == > " + personinfovo.toString());
			cfgperson = new ConfigPerson();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(personinfovo.getDbid() > 0){
					person = cfgperson.getPersonInfo_single(initconfigservice.service, personinfovo.getTenant_dbid(), personinfovo.getDbid());
				}

				if (person != null) { // 수정모드
					personmodelvo = cfgperson.modifyPerson_HD(initconfigservice.service, genesysinfovo.getCfg_switch_dbid(), genesysinfovo.getCfg_switch_dbid2(),
					personinfovo.getTenant_dbid(),
					person, personinfovo);
					logger.info("Modify Person~"); 
					rtnobj.put("RESULT_MSG", personmodelvo.getRtnMessage1());
					rtnobj.put("RESULT", personmodelvo.getRtnMessage2());
					rtnobj.put("EMPLOYEE_ID", personmodelvo.getRtnMessage3());
				} else {
					rtnobj.put("RESULT_MSG", "수정할 Person이 없습니다");
					rtnobj.put("RESULT", "F");
					rtnobj.put("EMPLOYEE_ID", personinfovo.getEmployeeId());
				}
			}else{
				rtnobj.put("RESULT_MSG", "Config Server에 연결할 수 없습니다");
				rtnobj.put("RESULT", "F");
				rtnobj.put("EMPLOYEE_ID", "");
			}
		} catch (ConfigServerException e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person 수정 실패(ConfigServerException)");
			rtnobj.put("EMPLOYEE_ID", "");
		}catch (Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person 수정 실패(Exception)");
			rtnobj.put("EMPLOYEE_ID", "");
		} finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}

	/**
	 * 상담사 삭제 송신 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC1580")
	public JSONObject deletePersonInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		PersonInfoVo personinfovo = null;
		HundaiModelVo personmodelvo = null;
		CfgPerson person = null;

		try {
			personinfovo = getParsePersonInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("personinfovo == > " + personinfovo.toString());
			cfgperson = new ConfigPerson();
			cfgagentlogin = new ConfigAgentLogin();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), "emp", personinfovo.getEmployeeId());

				if (person != null) { // 삭제
					if (person.getAgentInfo().getAgentLogins() != null) {
						Collection<CfgAgentLoginInfo> aglo = person.getAgentInfo().getAgentLogins();
						for (CfgAgentLoginInfo al : aglo) {
							cfgagentlogin.deleteAgentLogin(initconfigservice.service, al.getAgentLogin());
						}
					}
					logger.info("Delete Person~");
					personmodelvo = cfgperson.deletePerson_HD(initconfigservice.service, person);
					rtnobj.put("RESULT_MSG", personmodelvo.getRtnMessage1());
					rtnobj.put("RESULT", personmodelvo.getRtnMessage2());
					rtnobj.put("EMPLOYEE_ID", personmodelvo.getRtnMessage3());
		
				}else{
					rtnobj.put("RESULT_MSG", "삭제할 Person이 없습니다");
					rtnobj.put("RESULT", "F");
					rtnobj.put("EMPLOYEE_ID", personinfovo.getEmployeeId());
				}
			}else{
				rtnobj.put("RESULT_MSG", "Config Server에 연결할 수 없습니다");
				rtnobj.put("RESULT", "F");
				rtnobj.put("EMPLOYEE_ID", "");
			}
		} catch (ConfigServerException e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person 삭제 실패(ConfigServerException)");
			rtnobj.put("EMPLOYEE_ID", "");
		}catch (Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person 삭제 실패(Exception)");
			rtnobj.put("EMPLOYEE_ID", "");
		} finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}
	
	/**
	 * 상담사 상태(Enable, Disable) 변경 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC2320")
	public JSONObject setPersonState(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		PersonInfoVo personinfovo = null;
		HundaiModelVo personmodelvo = null;
		CfgPerson person = null;

		try {
			personinfovo = getParsePersonInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("personinfovo == > " + personinfovo.toString());
			cfgperson = new ConfigPerson();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), "emp", personinfovo.getEmployeeId());

				if (person != null) { // 상태변경
					if(personinfovo.getUsePossibleYn().equals("Y")){
						person = cfgperson.setPersonState(initconfigservice.service, person, true);
					}else if(personinfovo.getUsePossibleYn().equals("N")){
						person = cfgperson.setPersonState(initconfigservice.service, person, false);
					}
					logger.info("change Person state~"); 
					rtnobj.put("RESULT_MSG", "");
					rtnobj.put("RESULT", "T");
				} else {
					rtnobj.put("RESULT_MSG", "상태변경할 Person이 없습니다");
					rtnobj.put("RESULT", "F");
				}
			}else{
				rtnobj.put("RESULT_MSG", "Config Server에 연결할 수 없습니다");
				rtnobj.put("RESULT", "F");
				rtnobj.put("EMPLOYEE_ID", "");
			}
		} catch (ConfigServerException e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person 상태변경 실패(ConfigServerException)");
			rtnobj.put("EMPLOYEE_ID", "");
		}catch (Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person 상태변경 실패(Exception)");
			rtnobj.put("EMPLOYEE_ID", "");
		} finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}
	
	/**
	 * 상담사 상태(Enable, Disable) 확인 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC2330")
	public JSONObject getPersonState(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		PersonInfoVo personinfovo = null;
		HundaiModelVo personmodelvo = null;
		CfgPerson person = null;

		try {
			personinfovo = getParsePersonInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("personinfovo == > " + personinfovo.toString());
			cfgperson = new ConfigPerson();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				person = cfgperson.getPersonInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), "emp", personinfovo.getEmployeeId());

				if (person != null) { //사용가능여부 확인
					if(person.getState().equals(CfgObjectState.CFGEnabled)){
						rtnobj.put("USE_PSBL_YN", "Y");
					}else if(person.getState().equals(CfgObjectState.CFGDisabled)){
						rtnobj.put("USE_PSBL_YN", "N");
					}
					logger.info("get Person state~"); 
					rtnobj.put("RESULT_MSG", "");
					rtnobj.put("RESULT", "T");
				} else {
					rtnobj.put("RESULT_MSG", "사용가능여부를 확인할 Person이 없습니다");
					rtnobj.put("RESULT", "F");
				}
			}else{
				rtnobj.put("RESULT_MSG", "Config Server에 연결할 수 없습니다");
				rtnobj.put("RESULT", "F");
				rtnobj.put("EMPLOYEE_ID", "");
			}
		} catch (ConfigServerException e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person 사용가능여부확인 실패(ConfigServerException)");
			rtnobj.put("EMPLOYEE_ID", "");
		}catch (Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person 사용가능여부확인 실패(Exception)");
			rtnobj.put("EMPLOYEE_ID", "");
		} finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}

	/**
	 * 스킬그룹 생성 송신 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC1600")
	public JSONObject createAgentGroupInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		AgentGroupInfoVo agentgroupinfovo = null;
		CfgAgentGroup agentgroup = null; 
		HundaiModelVo agentgroupmodelvo = null;

		try { 
			agentgroupinfovo = getParseAgentGroupInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("agentgroupinfovo == > " + agentgroupinfovo.toString());
			cfgagentgroup = new ConfigAgentGroup();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(agentgroupinfovo.getDbid() > 0){
					agentgroup = cfgagentgroup.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroupinfovo.getDbid());
				}

				if (agentgroup != null) { 
					rtnobj.put("RESULT_MSG", "AgentGroup이 존재합니다");
					rtnobj.put("RESULT", "F");
					rtnobj.put("NAME", agentgroup.getGroupInfo().getName());
					rtnobj.put("DBID", agentgroup.getDBID());
				} else {
					agentgroupmodelvo = cfgagentgroup.createAgentGroupInfo_HD(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroupinfovo);
					logger.info("Create AgentGroup~");
					rtnobj.put("RESULT_MSG", agentgroupmodelvo.getRtnMessage1());
					rtnobj.put("RESULT", agentgroupmodelvo.getRtnMessage2());
					rtnobj.put("NAME", agentgroupmodelvo.getRtnMessage3());
					rtnobj.put("DBID", agentgroupmodelvo.getRtnMessage4());
				}
				//rtnobj = convertPersonInfo_jsonObj(person);
			}else{
				rtnobj.put("RESULT_MSG", "Config Server에 연결할 수 없습니다");
				rtnobj.put("RESULT", "F");
				rtnobj.put("NAME", "");
				rtnobj.put("DBID", "0");
			}
		}  catch (ConfigServerException e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "AgentGroup 생성 실패(ConfigServerException)");
			rtnobj.put("NAME", "");
			rtnobj.put("DBID", "0");
			e.printStackTrace();
		} catch (Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "AgentGroup 생성 실패(Exception)");
			rtnobj.put("NAME", "");
			rtnobj.put("DBID", "0");
			e.printStackTrace();
		}
		
		finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}

	/**
	 * 스킬그룹 변경 송신 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC1610")
	public JSONObject modifyAgentGroupInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		AgentGroupInfoVo agentgroupinfovo = null;
		CfgAgentGroup agentgroup = null; 
		HundaiModelVo agentgroupmodelvo = null;

		try {
			agentgroupinfovo = getParseAgentGroupInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("agentgroupinfovo == > " + agentgroupinfovo.toString());
			cfgagentgroup = new ConfigAgentGroup();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(agentgroupinfovo.getDbid() > 0){
					agentgroup = cfgagentgroup.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroupinfovo.getDbid());
				}

				if (agentgroup != null) { // 수정모드
					agentgroupmodelvo = cfgagentgroup.modifyAgentGroupInfo_HD(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroup, agentgroupinfovo);
					logger.info("Modify AgentGroup~"); 
					rtnobj.put("RESULT_MSG", agentgroupmodelvo.getRtnMessage1());
					rtnobj.put("RESULT", agentgroupmodelvo.getRtnMessage2());
					rtnobj.put("NAME", agentgroupmodelvo.getRtnMessage3());
				} else {
					rtnobj.put("RESULT_MSG", "수정할 AgentGroup이 없습니다");
					rtnobj.put("RESULT", "F");
					rtnobj.put("NAME", agentgroupinfovo.getName());
				}
			}else{
				rtnobj.put("RESULT_MSG", "Config Server에 연결할 수 없습니다");
				rtnobj.put("RESULT", "F");
				rtnobj.put("NAME", "");
			}
		} catch (ConfigServerException e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "AgentGroup 수정 실패(ConfigServerException)");
			rtnobj.put("NAME", "");
		}catch (Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "AgentGroup 수정 실패(Exception)");
			rtnobj.put("NAME", "");
		} finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}

	/**
	 * 스킬그룹 삭제 송신 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC1620")
	public JSONObject deleteAgentGroupInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		AgentGroupInfoVo agentgroupinfovo = null;
		CfgAgentGroup agentgroup = null;
		HundaiModelVo agentgroupmodelvo = null;
		try {
			agentgroupinfovo = getParseAgentGroupInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("agentgroupinfovo == > " + agentgroupinfovo.toString());
			cfgagentgroup = new ConfigAgentGroup();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				agentgroup = cfgagentgroup.getAgentGroupInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), agentgroupinfovo.getDbid());

				if (agentgroup != null) { // 삭제
					logger.info("Delete AgentGroup~");
					agentgroupmodelvo = cfgagentgroup.deleteAgentGroupInfo_HD(initconfigservice.service, agentgroup);
					rtnobj.put("RESULT_MSG", agentgroupmodelvo.getRtnMessage1());
					rtnobj.put("RESULT", agentgroupmodelvo.getRtnMessage2());
					rtnobj.put("NAME", agentgroupmodelvo.getRtnMessage3());
				}else{
					rtnobj.put("RESULT_MSG", "삭제할 AgentGroup이 없습니다");
					rtnobj.put("RESULT", "F");
					rtnobj.put("NAME", "");
				}
			}else{
				rtnobj.put("RESULT_MSG", "Config Server에 연결할 수 없습니다");
				rtnobj.put("RESULT", "F");
				rtnobj.put("NAME", "");
			}
		} catch (ConfigServerException e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "AgentGroup 삭제 실패(ConfigServerException)");
			rtnobj.put("NAME", "");
		}catch (Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "AgentGroup 삭제 실패(Exception)");
			rtnobj.put("NAME", "");
		} finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}

	/**
	 * 스킬그룹 조회 송신 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC1590")
	public JSONObject getAgentGroupInfo(@RequestBody Map<String, Object> param) throws Exception{
		JSONObject rtnobj = new JSONObject();
		AgentGroupInfoVo agentgroupinfovo = null;
		CfgAgentGroup agentgroup = null;

		try {
			agentgroupinfovo = getParseAgentGroupInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("agentgroupinfovo == > " + agentgroupinfovo.toString());
			cfgagentgroup = new ConfigAgentGroup();
			//그룹명으로 조회
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentgroup = new ConfigAgentGroup();
				if(agentgroupinfovo.getDbid() > 0){
					agentgroup = cfgagentgroup.getAgentGroupInfo(initconfigservice.service, agentgroupinfovo.getTenant_dbid(), agentgroupinfovo.getDbid());
					rtnobj = convertGroupInfo_jsonObj(agentgroup);
				}else{
					agentgroup = cfgagentgroup.getAgentGroupInfo(initconfigservice.service, agentgroupinfovo.getTenant_dbid(), agentgroupinfovo.getName());
					rtnobj = convertGroupInfo_jsonObj(agentgroup);
				}
			}			
			//rtnobj.put("command", "getAgentGroupInfo");
		}catch(Exception e) {
			logger.error("Exception", e);
		}finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}	

	/**
	 * 상담사 정보 조회 송신 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/PGICCC1551")
	public JSONObject getPersonInfo(@RequestBody Map<String, Object> param) throws Exception {
		CfgPerson person = null;
		Collection<CfgAgentGroup> groups = null;
		PersonInfoVo personinfovo = null;
		JSONObject rtnobj = new JSONObject();
		try {
			personinfovo = getParsePersonInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("personinfovo == > " + personinfovo.toString());
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgperson = new ConfigPerson();
				cfgagentgroup = new ConfigAgentGroup();
				person = cfgperson.getPersonInfo(initconfigservice.service, personinfovo.getTenant_dbid(),
						"emp", personinfovo.getEmployeeId());
				if(person != null){
					groups = cfgagentgroup.getPersonDependency(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), person.getDBID());
				}
				rtnobj = convertPersonInfo_jsonObj(person);
				if(groups != null){
					rtnobj.put("SKILLS",getDependencyInfo(groups));
				}
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getPersonInfo");
		} catch (Exception e) {
			rtnobj.put("RESULT_MSG", "Person정보를 조회하지 못했습니다(ConfigServerException)");
			rtnobj.put("EMPLOYEE_ID", personinfovo.getEmployeeId());
			rtnobj.put("FAIL_EMPLOYEE_ID", personinfovo.getEmployeeId());
		} finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}	
		
	/**
	 * 스킬그룹 상담사 할당 송신 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC1630")
	public JSONObject assignPerson(@RequestBody Map<String, Object> param) throws Exception {
		MoveInfoVo moveinfovo = new MoveInfoVo();
		AssignInfoVo assigninfovo = new AssignInfoVo();
		JSONObject rtnobj = new JSONObject();
		String[] fail_empArr = null;
		try {
			moveinfovo = getParseMovePerson(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("moveinfovo == > " + moveinfovo.toString());
			cfgagentgroup = new ConfigAgentGroup();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentgroup = new ConfigAgentGroup();
				assigninfovo = cfgagentgroup.assignPersonToAgentGroup_HD(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), moveinfovo.getName(), moveinfovo.getEmp_ids());
			}
			JsonArray emps = moveinfovo.getEmp_ids().getAsJsonArray();
			String[] empArr = new String[emps.size()];
			for(int i=0; i<emps.size(); i++) {
				empArr[i] = emps.get(i).getAsString();
			}

			fail_empArr = new String[assigninfovo.getFail_emp_id().size()];
			for(int i=0; i<assigninfovo.getFail_emp_id().size(); i++) {
				fail_empArr[i] = assigninfovo.getFail_emp_id().get(i);
			}
			
			if(assigninfovo.getAgentgroup() != null){
				if(assigninfovo.getFail_emp_id().size() == 0) {
					rtnobj.put("RESULT", "T");
					rtnobj.put("RESULT_MSG", "");
					rtnobj.put("EMPLOYEE_ID", empArr);
				}else {
					rtnobj.put("RESULT", "F");
					rtnobj.put("RESULT_MSG", "Person을 할당하지 못했습니다(1)");
					rtnobj.put("EMPLOYEE_ID", fail_empArr);
				}
			}else{
				rtnobj.put("RESULT", "F");
				rtnobj.put("RESULT_MSG", "Person을 할당하지 못했습니다(2)");
				rtnobj.put("EMPLOYEE_ID", empArr);
			}
		}catch (ConfigServerException e) {
			String[] empArr = null;
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person을 할당하지 못했습니다(ConfigServerException)");
			rtnobj.put("EMPLOYEE_ID", empArr);
			rtnobj.put("FAIL_EMPLOYEE_ID", fail_empArr);
		}catch (Exception e) {
			String[] empArr = null;
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person을 할당하지 못했습니다(Exception)");
			rtnobj.put("EMPLOYEE_ID", empArr);
			rtnobj.put("FAIL_EMPLOYEE_ID", fail_empArr);
		} finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}

	/**
	 * 스킬그룹 상담사 해제 송신 IF
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC1640")
	public JSONObject unAssignPerson(@RequestBody Map<String, Object> param) throws Exception {
		MoveInfoVo moveinfovo = new MoveInfoVo();
		AssignInfoVo assigninfovo = new AssignInfoVo();
		JSONObject rtnobj = new JSONObject();
		String[] fail_empArr = null;
		try {
			moveinfovo = getParseMovePerson(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			logger.info("moveinfovo == > " + moveinfovo.toString());
			cfgagentgroup = new ConfigAgentGroup();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgagentgroup = new ConfigAgentGroup();
				assigninfovo = cfgagentgroup.unAssignPersonToAgentGroup_HD(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), moveinfovo.getName(), moveinfovo.getEmp_ids());
			}
			JsonArray emps = moveinfovo.getEmp_ids().getAsJsonArray();
			String[] empArr = new String[emps.size()];
			for(int i=0; i<emps.size(); i++) {
				empArr[i] = emps.get(i).getAsString();
			}
			
			fail_empArr = new String[assigninfovo.getFail_emp_id().size()];
			for(int i=0; i<assigninfovo.getFail_emp_id().size(); i++) {
				fail_empArr[i] = assigninfovo.getFail_emp_id().get(i);
			}
			
			if(assigninfovo.getAgentgroup() != null){
				if(assigninfovo.getFail_emp_id().size() == 0) {
					rtnobj.put("RESULT", "T");
					rtnobj.put("RESULT_MSG", "Person이 회수됐습니다");
					rtnobj.put("EMPLOYEE_ID", empArr);
				}else {
					rtnobj.put("RESULT", "F");
					rtnobj.put("RESULT_MSG", "Person을 회수하지 못했습니다(1)");
					rtnobj.put("EMPLOYEE_ID", fail_empArr);
				}
			}else{
				rtnobj.put("RESULT", "F");
				rtnobj.put("RESULT_MSG", "Person을 회수하지 못했습니다(2)");
				rtnobj.put("EMPLOYEE_ID", empArr);
			}
		}catch (ConfigServerException e) {
			String[] empArr = null;
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person을 회수하지 못했습니다(ConfigServerException)");
			rtnobj.put("EMPLOYEE_ID", empArr);
		}catch (Exception e) {
			String[] empArr = null;
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", "Person을 회수하지 못했습니다(Exception)");
			rtnobj.put("EMPLOYEE_ID", empArr);
		} finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
		}
		return rtnobj;
	}

	/**
	 * 스킬그룹별 상담사 상세조회 IF (New Ver)
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC2060")
	public JSONObject getSkillGrpStat(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		SkillStatInfoVo skillinfovo = null;
		AgentStatusesCollection as_list;
		StatCtrl2 stat = new StatCtrl2();
		
		skillinfovo = getParseSkillStat(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
		logger.info("skillinfovo == > " + skillinfovo.toString());
		String CurrValue = "";
		String CurrTime = "";
		JSONArray jsonarryArray = new JSONArray();

		try {
			if (initstatservice.openStatService(genesysinfovo) == ChannelState.Opened) {
				initstatservice.eventinfo = null;
				stat.setStatistics_GA(initstatservice.protocol, genesysinfovo, skillinfovo.getName(), "CurrentAllAgentState");
			}
			
			EventInfo eventinfo = initstatservice.eventinfo;
			if(eventinfo != null) {
				AgentGroup group = (AgentGroup)eventinfo.getStateValue();
				if(group == null)
					return null;
				if(group.getAgents() == null || group.getAgentCount() <= 0) return null;
				as_list = group.getAgents();
				logger.info("return object size = " + as_list.getCount());
				for(int j=0; j< as_list.getCount();j++){
					JSONObject tmpobj = new JSONObject();
					AgentStatus as = (AgentStatus)as_list.getItem(j);
					CurrValue = as.getStatus() + "";
					CurrTime = (eventinfo.getTimestamp() - as.getTime()) + "";
					tmpobj.put("EMPLOYEE_ID", as.getAgentId());
					tmpobj.put("STAT", CurrValue);
					jsonarryArray.add(tmpobj);
				}
			}
			rtnobj.put("USERS", jsonarryArray);
			//logger.info(eventinfo.toString());
		//}
		}catch(Exception ex) {
			logger.error("HyundaiController.getSkillGrpStat", ex);
		}finally {
			//logger.info(rtnobj.toJSONString());
			//logger.info("return object size = " + rtnobj.size());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
			//stat.closeStat(initstatservice.protocol);
		}			
		return rtnobj;
	}
	
	/**
	 * 스킬그룹별 상담사 상세조회 IF (Old Ver)
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/PGICCC20600")
	public JSONObject getSkillGrpStat2(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		SkillStatInfoVo skillinfovo = null;
		AgentStatusesCollection as_list;
		
		skillinfovo = getParseSkillStat(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
		logger.info("skillinfovo == > " + skillinfovo.toString());
		String CurrValue = "";
		String CurrTime = "";
		JSONArray jsonarryArray = new JSONArray();

		StatCtrl stat = new StatCtrl();
		try {
			stat.initalize(genesysinfovo.getStat_ip_p(), genesysinfovo.getStat_ip_b(), genesysinfovo.getStat_endpoint1()
						, genesysinfovo.getStat_endpoint2(), genesysinfovo.getStat_port_p(), genesysinfovo.getStat_port_b()
						, genesysinfovo.getStat_clientname(), genesysinfovo.getStat_clientname()
						, genesysinfovo.getStat_charset(), skillinfovo.getName(), "CurrentAllAgentState", genesysinfovo.getStat_timeout(), genesysinfovo.getStat_delay());
			stat.openProtocol();
			//if(stat.protocol.getState() == ChannelState.Opened) {
				stat.setStatistics_GA(skillinfovo.getName(), "CurrentAllAgentState");
				//stat.closeProtocol();

				//Thread.sleep(100);
				EventInfo eventinfo = stat.eventinfo;
				if(eventinfo != null) {
					AgentGroup group = (AgentGroup)eventinfo.getStateValue();
					if(group == null)
						return null;
					if(group.getAgents() == null || group.getAgentCount() <= 0) return null;
					as_list = group.getAgents();
					logger.info("" + as_list.getCount());
					for(int j=0; j< as_list.getCount();j++){
						JSONObject tmpobj = new JSONObject();
						AgentStatus as = (AgentStatus)as_list.getItem(j);
						CurrValue = as.getStatus() + "";
						CurrTime = (eventinfo.getTimestamp() - as.getTime()) + "";
						tmpobj.put("EMPLOYEE_ID", as.getAgentId());
						tmpobj.put("STAT", CurrValue);
						jsonarryArray.add(tmpobj);
					}
				}
				rtnobj.put("USERS", jsonarryArray);
				//logger.info(eventinfo.toString());
			//}
		}catch(Exception ex) {
			logger.error("HyundaiController.getSkillGrpStat",ex);
			if(stat.protocol.getState() == ChannelState.Opened) {
				stat.closeProtocol();
			}
		}finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
			if(stat.protocol.getState() == ChannelState.Opened) {
				stat.closeProtocol();
			}
			stat = null;
		}			
		return rtnobj;
	}
	
	/**
	 * AgentGroup의 dependency 정보 조회
	 * @param groups
	 * @return
	 */
	private JSONArray getDependencyInfo(Collection<CfgAgentGroup> groups) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		if (groups != null) {
			for (CfgAgentGroup group : groups) {
				jsonObj = new JSONObject();
				jsonObj.put("DBID", group.getDBID());
				jsonObj.put("NAME", group.getGroupInfo().getName());
				jsonObj.put("STATE", group.getGroupInfo().getState().ordinal());
				jsonObj.put("OBJECT_TYPE", "CfgAgentGroup");
				jsonArray.add(jsonObj);
			}
		}
		return jsonArray;
	}

	/**
	 * Param의 특정요소 String값으로 추출
	 * @param param
	 * @param qryStr
	 * @param type
	 * @return
	 */
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

	/**
	 * 상담사 여부 
	 * @param args
	 * @param tf
	 * @return
	 */
	private int convertTrueFalse(String args, String tf){
		int rtnval = 0;
		if(args.equals("isagent")){
			if(tf.equals("T")){
				rtnval = 2;
			}else{
				rtnval = 1;
			}
		}else{
			if(tf.equals("T")){
				rtnval = 1;
			}
		}
		return rtnval;
	}

	/**
	 * CfgPerson을 json형태로 변경
	 * @param person
	 * @return
	 */
	private JSONObject convertPersonInfo_jsonObj(CfgPerson person) {
		JSONObject finalJsonObj = new JSONObject();

		if (person != null) {
			if (person.getIsAgent() == CfgFlag.CFGTrue) {
				finalJsonObj.put("SKILL_INFOS", configcommon.getCfgSkillLevelCollectionInfo_HD(person.getAgentInfo().getSkillLevels()));
				finalJsonObj.put("AGENT_LOGINS", configcommon.getCfgAgentInfoCollectionInfo_HD(person.getAgentInfo().getAgentLogins()));
			}
			finalJsonObj.put("TENANT_DBID", person.getTenantDBID());
			finalJsonObj.put("DBID", person.getDBID());
			finalJsonObj.put("FIRST_NAME", person.getFirstName());
			finalJsonObj.put("LAST_NAME", person.getLastName());
			finalJsonObj.put("USER_NAME", person.getUserName());
			finalJsonObj.put("EMPLOYEE_ID", person.getEmployeeID());
			if(person.getIsAgent() == CfgFlag.CFGTrue){
				finalJsonObj.put("IS_AGENT", "T");
			}else{
				finalJsonObj.put("IS_AGENT", "F");
			}
			if(person.getState() == CfgObjectState.CFGEnabled){
				finalJsonObj.put("STATE", "T");
			}else{
				finalJsonObj.put("STATE", "F");
			}
			finalJsonObj.put("OBJECT_TYPE", "CfgPerson");
		}
		return finalJsonObj;
	}

	/**
	 * CfgAgentGroup을 json형태로 변경
	 * @param group
	 * @return
	 */
	private JSONObject convertGroupInfo_jsonObj(CfgAgentGroup group) {
		JSONObject finalJsonObj = new JSONObject();      
		
		if(group != null) {
			/* Agent Group별 상담사 목록*/
			Collection<CfgPerson> persons = group.getAgents();
			if(persons != null) {
				finalJsonObj.put("USERS", getCfgPersonCollectionInfo(persons));
			}
			/**/
			finalJsonObj.put("DBID", group.getDBID());
			finalJsonObj.put("NAME", group.getGroupInfo().getName());
			finalJsonObj.put("FOLDER_DBID", group.getFolderId());
	
			if(group.getGroupInfo().getState() == CfgObjectState.CFGEnabled){
				finalJsonObj.put("STATE", "T");
			}else{
				finalJsonObj.put("STATE", "F");
			}

			finalJsonObj.put("OBJECT_TYPE", "CfgAgentGroup");
		}
		return finalJsonObj;
	}

	/**
	 * Collection<CfgPerson>을 JSONArray형태로 변경
	 * @param coll
	 * @return
	 */
	private JSONArray getCfgPersonCollectionInfo(Collection<CfgPerson> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		for (CfgPerson person : coll) {
			jsonObj = new JSONObject();
			jsonObj.put("TENANT_DBID", person.getTenantDBID());
			jsonObj.put("DBID", person.getDBID());
			jsonObj.put("FIRST_NAME", person.getFirstName());
			jsonObj.put("LAST_NAME", person.getLastName());
			jsonObj.put("USER_NAME", person.getUserName());
			jsonObj.put("EMPLOYEE_ID", person.getEmployeeID());
			if(person.getIsAgent() == CfgFlag.CFGTrue){
				jsonObj.put("IS_AGENT", "T");
			}else{
				jsonObj.put("IS_AGENT", "F");
			}
			if(person.getState() == CfgObjectState.CFGEnabled){
				jsonObj.put("STATE", "T");
			}else{
				jsonObj.put("STATE", "F");
			}
			jsonObj.put("OBJECT_TYPE", "CfgPerson");
			rtnArray.add(jsonObj);
		}
		return rtnArray;
	}
}