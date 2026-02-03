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

import com.genesyslab.platform.applicationblocks.com.objects.CfgObjectID;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSkill;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSkillLevel;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.co.i4way.genesys.cfgserver.ConfigCommon;
import kr.co.i4way.genesys.cfgserver.ConfigFolder;
import kr.co.i4way.genesys.cfgserver.ConfigPerson;
import kr.co.i4way.genesys.cfgserver.ConfigSkill;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.MoveInfoVo;
import kr.co.i4way.genesys.model.QueryVo;
import kr.co.i4way.genesys.model.SkillInfoVo;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * Skill용 Controller
 * @author jkhong
 *
 */
@RestController
@CrossOrigin(origins = "*")
public class SkillController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private GenesysInfoVo genesysinfovo;
	initConfigService initconfigservice = initConfigService.getInstance();
	ConfigSkill cfgskill = null;
	ConfigPerson cfgperson = null;
	ConfigFolder cfgfolder = null;
	ConfigCommon configcommon = null;
	
	public SkillController(){
		configcommon = new ConfigCommon();
	}

	/**
	 * 전체Skill 정보를 조회한다.
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/skill/getSkills")
	public JSONObject getSkills(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgSkill> skillinfo = null;
		JSONObject rtnobj = new JSONObject();
		//Map<String, Object> map = new HashMap<>(); 

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgskill = new ConfigSkill();
				skillinfo = cfgskill.getSkillInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid());
				rtnobj = convertSkills_jsonObj(skillinfo);
			}		
			rtnobj.put("command", "getSkills");
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	

	@RequestMapping("/skill/getSkillInfo")
	public JSONObject getSkillInfo(@RequestBody QueryVo vo) throws Exception {
		CfgSkill skill = null;
		Collection<CfgPerson> persons = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgskill = new ConfigSkill();
				cfgperson = new ConfigPerson();
				skill = cfgskill.getSkillInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), Integer.parseInt(vo.getQry_str()));
				if(skill != null){
					persons = cfgperson.getSkillDependency(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), skill.getDBID());
				}

				rtnobj = convertSkill_jsonObj(skill);
				if(persons != null){
					rtnobj.put("dependency",getDependencyInfo(persons, skill.getDBID(), vo.getSkill_level()));
				}
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getSkillInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	private JSONObject getDependencyInfo(Collection<CfgPerson> persons, int skill_dbid, int skill_level) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();

		if (persons != null) {
			for (CfgPerson person : persons) {
				Collection<CfgSkillLevel> skilllevels = person.getAgentInfo().getSkillLevels();
				int level = 0;
				for (CfgSkillLevel skilllevel : skilllevels) {
					if(skilllevel.getSkillDBID() == skill_dbid){
						level = skilllevel.getLevel();
					}
				}
				if(skill_level==level || skill_level==-1){
					jsonObj = new JSONObject();
					jsonObj.put("dbid", person.getDBID());
					jsonObj.put("first_name", person.getFirstName());
					jsonObj.put("last_name", person.getLastName());
					jsonObj.put("user_name", person.getUserName());
					jsonObj.put("employee_id", person.getEmployeeID());
					jsonObj.put("skill_level", level);
					jsonObj.put("state", person.getState().ordinal());
					jsonObj.put("obj", "CfgPerson");
					jsonArray.add(jsonObj);
				}
			}
			finalJsonObj.put("persons", jsonArray);
		}
		return finalJsonObj;
	}

	private JSONObject getSkillInfo(int dbid) throws Exception {
		CfgSkill skill = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgskill = new ConfigSkill();
				skill = cfgskill.getSkillInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dbid);
				rtnobj = convertSkill_jsonObj(skill);
				// logger.info(map.toString());
			}
			rtnobj.put("command", "getSkillInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	@RequestMapping("/skill/getFolderInfo")
	public JSONObject getSkillFolderInfo(@RequestBody QueryVo vo) throws Exception {
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

	@RequestMapping("/skill/getFolderInfo_tree")
	public JSONObject getSkillFolderInfoTree(@RequestBody QueryVo vo) throws Exception {
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

	private SkillInfoVo getParseSkillInfo(Map<String, Object> param){
		SkillInfoVo skillinfovo = new SkillInfoVo();
		// jsonPaserPser 클래스 객체를 만들고 해당 객체에 
		JsonParser jparser = new JsonParser();
		
		try {

			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));

			skillinfovo.setName(getParam(param, "name", "S"));
			skillinfovo.setDbid(Integer.parseInt(getParam(param, "dbid", "S")));
			skillinfovo.setState(Integer.parseInt(getParam(param, "state", "S")));
			skillinfovo.setFolderId(Integer.parseInt(getParam(param, "folderId", "S")));

			skillinfovo.setAnnex(annex);
		}catch(Exception ex){
			skillinfovo = null;
		}
		return skillinfovo;
	}

	@RequestMapping("/skill/createSkillInfo")
	public JSONObject createSkillInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		SkillInfoVo skillinfovo = null;
		CfgSkill skill = null;

		try {
			skillinfovo = getParseSkillInfo(param);
			cfgskill = new ConfigSkill();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(skillinfovo.getDbid() > 0){
					skill = cfgskill.getSkillInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), skillinfovo.getDbid());
				}

				if (skill != null) { // 수정모드
					skill = cfgskill.modifySkill(initconfigservice.service, skill, skillinfovo);
					logger.info("Modify Skill~"); 
				} else {
					logger.info("Create Skill~");
					skill = cfgskill.createSkill(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), skillinfovo);
				}
				rtnobj = convertSkill_jsonObj(skill);
			}
			rtnobj.put("command", "createSkillInfo");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/skill/modifySkillInfo")
	public JSONObject modifySkillInfo(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		SkillInfoVo skillinfovo = null;
		CfgSkill skill = null;

		try {
			skillinfovo = getParseSkillInfo(param);
			cfgskill = new ConfigSkill();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(skillinfovo.getDbid() > 0){
					skill = cfgskill.getSkillInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), skillinfovo.getDbid());
				}

				if (skill != null) { // 수정모드
					skill = cfgskill.modifySkill(initconfigservice.service, skill, skillinfovo);
					logger.info("Modify Skill~"); 
				} else {
					logger.info("Create Skill~");
					skill = cfgskill.createSkill(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), skillinfovo);
				}
				rtnobj = convertSkill_jsonObj(skill);
			}
			rtnobj.put("command", "modifySkillInfo");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/skill/deleteSkillInfo")
	public JSONObject deleteSkillInfo(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		boolean del_skill_result = false;
		CfgSkill skill = null;

		try {
			cfgskill = new ConfigSkill();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				skill = cfgskill.getSkillInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getFolder_dbid());

				if (skill != null) { // 삭제
					logger.info("Delete Skill~");
					del_skill_result = cfgskill.deleteSkill(initconfigservice.service, skill);
				}
			}
			rtnobj.put("command", "deleteSkillInfo");
			rtnobj.put("del_result", del_skill_result);
			rtnobj.put("dbid", vo.getSrc_dbid());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/skill/setSkillState")
	public JSONObject setSkillState(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgSkill skill = null;

		try {
			cfgskill = new ConfigSkill();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				skill = cfgskill.getSkillInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getFolder_dbid());

				if (skill != null) { // skill이 널이 아니면
					logger.info("set Skill State~");
					if(skill.getState().equals(CfgObjectState.CFGEnabled)){
						skill = cfgskill.setSkillState(initconfigservice.service, skill, false);
					}else if(skill.getState().equals(CfgObjectState.CFGDisabled)){
						skill = cfgskill.setSkillState(initconfigservice.service, skill, true);
					}
				}
			}
			rtnobj.put("command", "setSkillState");
			rtnobj.put("state", skill.getState().name());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}
			
	private JSONObject convertSkill_jsonObj(CfgSkill skill) {
		JSONObject jsonObj = new JSONObject();
		
		if(skill != null) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", skill.getDBID());
			jsonObj.put("name", skill.getName());
			jsonObj.put("state", skill.getState().ordinal());
			jsonObj.put("obj", "CfgSkill");
			jsonObj.put("annex", configcommon.getAnnexInfo(skill.getUserProperties()));
		}
		return jsonObj;
	}
	
	private JSONObject convertSkills_jsonObj(Collection<CfgSkill> skills) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		
		if(skills != null) {
			for (CfgSkill skill : skills) {
				jsonObj = new JSONObject();
				jsonObj = convertSkill_jsonObj(skill);
				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("skills", jsonArray);
		}else {
			finalJsonObj.put("skills", null);
		}
		return finalJsonObj;
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
				if (cfgobjectid.getType().name().equals("CFGSkill")) {
					jsonObj = getSkillInfo(cfgobjectid.getDBID());
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

	private MoveInfoVo getParseMoveFolder(Map<String, Object> param){
		MoveInfoVo moveinfovo = new MoveInfoVo();
		JsonParser jparser = new JsonParser(); 
		try {
			JsonElement moveObj = jparser.parse(getParam(param, "moveObj", "S"));

			moveinfovo.setTgt_dbid(Integer.parseInt(getParam(param, "tgt_dbid", "S")));
			moveinfovo.setClick_dbid(Integer.parseInt(getParam(param, "click_dbid", "S")));
			moveinfovo.setMoveObj(moveObj);

		}catch(Exception ex){
			logger.error("Exception", ex);
		}
		return moveinfovo;
	}

	@RequestMapping("/skill/moveFolder")
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
