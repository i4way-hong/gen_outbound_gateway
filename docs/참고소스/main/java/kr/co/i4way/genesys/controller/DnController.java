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
import com.genesyslab.platform.applicationblocks.com.objects.CfgDN;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.objects.CfgObjectID;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPlace;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTransaction;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.co.i4way.genesys.cfgserver.ConfigCommon;
import kr.co.i4way.genesys.cfgserver.ConfigDn;
import kr.co.i4way.genesys.cfgserver.ConfigFolder;
import kr.co.i4way.genesys.cfgserver.ConfigPlace;
import kr.co.i4way.genesys.cfgserver.ConfigTransaction;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.DnVo;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.MoveInfoVo;
import kr.co.i4way.genesys.model.QueryVo;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * DN용 Controller
 * @author jkhong
 *
 */
@RestController
@CrossOrigin(origins = "*")
public class DnController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private GenesysInfoVo genesysinfovo;
	initConfigService initconfigservice = initConfigService.getInstance();
	ConfigDn cfgdn = null;
	ConfigFolder cfgfolder = null;
	ConfigPlace cfgplace = null;
	ConfigTransaction cfgtransaction = null;
	ConfigCommon configcommon = null;
	Collection<CfgAgentLogin> llgs = null;

	public DnController(){
		configcommon = new ConfigCommon();
	}
	
	/**
	 * 전체CfgDn 정보를 조회한다.
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/dn/getDns")
	public JSONObject getDns(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgDN> dn = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgdn = new ConfigDn();
				dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid());
				rtnobj = convertDns_jsonObj(dn);
			}		
			rtnobj.put("command", "getDns");
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	

		/**
	 * getExtensions 정보를 조회한다.
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/dn/getExtensions")
	public JSONObject getExtensions(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgDN> dn = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgdn = new ConfigDn();
				dn = cfgdn.getExtensions(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid());
				rtnobj = convertDns_jsonObj(dn);
			}		
			rtnobj.put("command", "getExtensions");
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	

	/**
	 * 전체CfgDn 정보를 조회한다.
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/dn/getDn_DialPlan")
	public JSONObject getDn_DialPlan(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgDN> dn = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgdn = new ConfigDn();
				dn = cfgdn.getDn_DialPlan(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getQry_str());
				rtnobj = convertDns_jsonObj(dn);
			}		
			rtnobj.put("command", "getDns");
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	

	/**
	 * 전체CfgDn 정보를 조회한다.
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/dn/getDialPlan")
	public JSONObject getDialPlan(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgDN> dn = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgdn = new ConfigDn();
				dn = cfgdn.getDialPlan(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid());
				rtnobj = convertDns_jsonObj(dn);

			}		
			rtnobj.put("command", "getDialPlan");
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	
	
	/**
	 * DN 정보를 조회한다.
	 * @param request("qry_type") : 쿼리타입 name : name으로 쿼리, dbid : dbid로 쿼리
	 * @param request("qry_str") : name or dbid
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/dn/getDn")
	public JSONObject getDn(@RequestBody QueryVo vo) throws Exception{
		CfgDN dn = null;
		Collection<CfgPlace> place = null;
		JSONObject rtnobj = new JSONObject();
		//Map<String, Object> map = new HashMap<>(); 

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgdn = new ConfigDn();
				cfgplace = new ConfigPlace();
				if(vo.getQry_type().equals("name")) {
					dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getQry_str());
				}else if(vo.getQry_type().equals("dbid")) {
					dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), Integer.parseInt(vo.getQry_str()));
				}

				if(dn != null){
					place = cfgplace.getDnDependency(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dn.getDBID());
				}

				rtnobj = convertDn_jsonObj(dn);

				if(place != null){
					rtnobj.put("dependency",getDependencyInfo(place));
				}

				rtnobj.put("command", "getDn");
				rtnobj.put("qry_type", vo.getQry_type());
				rtnobj.put("qry_str", vo.getQry_str());
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/dn/setDialPlan")
	public JSONObject setDialPlan(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgDN dn = null;

		try {
			cfgdn = new ConfigDn();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getDbid());

				if (dn != null) { // dn 널이 아니면
					logger.info("set setDialPlan~");
					dn = cfgdn.setDialPlan(initconfigservice.service, dn, vo.getFlag(), vo.getDialplan_name());
				}
			}
			rtnobj.put("command", "setDialPlan");
			rtnobj.put("state", dn.getState().name());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}
	
	private JSONObject getDependencyInfo(Collection<CfgPlace> places) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();

		if (places != null) {
			for (CfgPlace place : places) {
				jsonObj = new JSONObject();
				jsonObj.put("dbid", place.getDBID());
				jsonObj.put("name", place.getName());
				jsonObj.put("state", place.getState().ordinal());
				jsonObj.put("type", place.getObjectType().toString());
				jsonObj.put("obj", "CfgDn");
				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("dns", jsonArray);
		}
		return finalJsonObj;
	}

	private JSONObject getDn(int dbid) throws Exception {
		CfgDN dn = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgdn = new ConfigDn();
				dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), dbid);
				rtnobj = convertDn_jsonObj(dn);
			}
			rtnobj.put("command", "getDn");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}
	
	@RequestMapping("/dn/createDn")
	public JSONObject createDn(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgDN dn = null;
		DnVo dnvo = null;
		cfgdn = null;

		try {
			dnvo = getParseDn(param);
			cfgdn = new ConfigDn();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo.getSwitch_dbid(), dnvo.getDbid());

				if (dn != null) { // 수정모드
					dn = cfgdn.modifyDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dn, dnvo);
					logger.info("Modify Dn~");
				} else {
					dn = cfgdn.createDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo);
					logger.info("Create Dn~");
				}
				rtnobj = convertDn_jsonObj(dn);
			}
			rtnobj.put("command", "createDN");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/dn/createDn_magic")
	public JSONObject createDn_magic(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgDN dn = null;
		DnVo dnvo = null;
		cfgdn = null;
		String creationResult = "";
		try {
			dnvo = getParseDn(param);
			cfgdn = new ConfigDn();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo.getSwitch_dbid(), dnvo.getDbid());

				if (dn != null) { // 수정모드
					creationResult = "2";	//DN생성실패(이미 생성되어있는DN임)
					logger.info("Modify Dn~");
					
				} else {
					dn = cfgdn.createDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo);
					creationResult = "1";	//DN생성성공
					logger.info("Create Dn~");
					if(dn != null){
						if(!dnvo.getPlace_name().equals("")){
							cfgplace = new ConfigPlace();
							CfgPlace tmpplace = cfgplace.getPlace(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo.getPlace_name());
							if(tmpplace == null){
								cfgplace.createPlace(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo.getSwitch_dbid(), dnvo.getPlace_name(), dn);
								creationResult = "3";	//DN생성성공, Place생성성공
								logger.info("Create Place~");
							}else{
								creationResult = "4";	//DN생성성공, Place생성실패(이미 생성되어있는 Place임)
								logger.info("Create Place Fail~");
							}
						}
					}
				}
				rtnobj = convertDn_jsonObj(dn);
			}
			rtnobj.put("command", "createDN");
			rtnobj.put("result", creationResult);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/dn/createDn_batch")
	public JSONObject createDn_batch(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgDN dn = null;
		DnVo dnvo = null;
		cfgdn = null;
		String creationResult = "";
		try {
			dnvo = getParseDnBatch(param);
			cfgdn = new ConfigDn();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo.getSwitch_dbid(), dnvo.getDbid());

				if (dn != null) { // 수정모드
					creationResult = "2";	//DN생성실패(이미 생성되어있는DN임)
					logger.info("Modify Dn~");
					
				} else {
					dn = cfgdn.createDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo);
					creationResult = "1";	//DN생성성공
					logger.info("Create Dn~");
					if(dn != null){
						if(!dnvo.getPlace_name().equals("")){
							cfgplace = new ConfigPlace();
							CfgPlace tmpplace = cfgplace.getPlace(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo.getPlace_name());
							if(tmpplace == null){
								cfgplace.createPlace(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo.getSwitch_dbid(), dnvo.getPlace_name(), dn);
								creationResult = "3";	//DN생성성공, Place생성성공
								logger.info("Create Place~");
							}else{
								creationResult = "4";	//DN생성성공, Place생성실패(이미 생성되어있는 Place임)
								logger.info("Create Place Fail~");
							}
						}

						if(!dnvo.getAnnex_name().equals("")){
							CfgTransaction transaction = null;
							cfgtransaction = new ConfigTransaction();
							transaction = cfgtransaction.getTransactionInfo_name(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), "T_Server_option");
							KeyValueCollection obj_kv = transaction.getUserProperties();
							if(obj_kv != null){
								for (Object selectionObj : obj_kv) {
									KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
									if(sectionKvp.getStringKey().equals(dnvo.getAnnex_name())){
										dn = cfgdn.setTsvrOption(initconfigservice.service, dn, sectionKvp);
									}
								}
							}
							creationResult = "5";	//DN생성성공, TServer옵션적용성공
							logger.info("Set Option~");
						}

						
						if(!dnvo.getDialplan().equals("")){
							dn = cfgdn.setDialPlan(initconfigservice.service, dn, true, dnvo.getDialplan());
							creationResult = "6";	//DN생성성공, DialPlan생성성공
							logger.info("Set DialPlan~");
						}

					}
				}
				rtnobj = convertDn_jsonObj(dn);
			}
			rtnobj.put("command", "createDn_batch");
			rtnobj.put("result", creationResult);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/dn/modifyDn")
	public JSONObject modifyDn(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgDN dn = null;
		DnVo dnvo = null;
		cfgdn = null;

		try {
			dnvo = getParseDn(param);
			cfgdn = new ConfigDn();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo.getSwitch_dbid(), dnvo.getDbid());

				if (dn != null) { // 수정모드
					dn = cfgdn.modifyDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dn, dnvo);
					logger.info("Modify Dn~"); 
				} else {
					dn = cfgdn.createDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dnvo);
					logger.info("Create Dn~");
				}
				rtnobj = convertDn_jsonObj(dn);
			}
			rtnobj.put("command", "modifyDn");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/dn/deleteDn")
	public JSONObject deleteDn(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgDN dn = null;
		boolean del_result = false;
		cfgdn = null;

		try {
			cfgdn = new ConfigDn();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getSwitch_dbid(), vo.getSrc_dbid());

				if (dn != null) { // 삭제
					logger.info("Delete Dn~");
					del_result = cfgdn.deleteDn(initconfigservice.service, dn);
				}
			}
			rtnobj.put("command", "deleteDn");
			rtnobj.put("del_result", del_result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	private DnVo getParseDn(Map<String, Object> param){
		DnVo dnvo = new DnVo();
		JsonParser jparser = new JsonParser();
		
		try {

			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));

			dnvo.setDbid(Integer.parseInt(getParam(param, "dbid", "I")));
			dnvo.setAssociation(getParam(param, "association", "S"));
			dnvo.setDn_login_id(getParam(param, "dn_login_id", "S"));
			dnvo.setFolderId(Integer.parseInt(getParam(param, "folderId", "I")));
			dnvo.setGroup_dbid(Integer.parseInt(getParam(param, "group_dbid", "I")));
			dnvo.setName(getParam(param, "name", "S"));
			dnvo.setNumber(getParam(param, "number", "S"));
			dnvo.setPlace_name(getParam(param, "place_name", "S"));
			dnvo.setRegister(Integer.parseInt(getParam(param, "register", "I")));
			dnvo.setRoute_type(Integer.parseInt(getParam(param, "route_type", "I")));
			dnvo.setState(Integer.parseInt(getParam(param, "state", "I")));
			dnvo.setSwitch_dbid(Integer.parseInt(getParam(param, "switch_dbid", "I")));
			dnvo.setSwitch_specific_type(Integer.parseInt(getParam(param, "switch_specific_type", "I")));
			dnvo.setTrunks(Integer.parseInt(getParam(param, "trunks", "I")));
			dnvo.setType(Integer.parseInt(getParam(param, "type", "I")));
			dnvo.setUse_override(Integer.parseInt(getParam(param, "use_override", "I")));
			dnvo.setAnnex(annex);
		}catch(Exception ex){
			dnvo = null;
		}
		return dnvo;
	}

	private DnVo getParseDnBatch(Map<String, Object> param){
		DnVo dnvo = new DnVo();
		JsonParser jparser = new JsonParser();
		
		try {

			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));

			dnvo.setDbid(Integer.parseInt(getParam(param, "dbid", "I")));
			dnvo.setAssociation(getParam(param, "association", "S"));
			dnvo.setDn_login_id(getParam(param, "dn_login_id", "S"));
			dnvo.setFolderId(Integer.parseInt(getParam(param, "folderId", "I")));
			dnvo.setGroup_dbid(Integer.parseInt(getParam(param, "group_dbid", "I")));
			dnvo.setName(getParam(param, "name", "S"));
			dnvo.setNumber(getParam(param, "number", "S"));
			dnvo.setPlace_name(getParam(param, "place_name", "S"));
			dnvo.setRegister(Integer.parseInt(getParam(param, "register", "I")));
			dnvo.setRoute_type(Integer.parseInt(getParam(param, "route_type", "I")));
			dnvo.setState(Integer.parseInt(getParam(param, "state", "I")));
			dnvo.setSwitch_dbid(Integer.parseInt(getParam(param, "switch_dbid", "I")));
			dnvo.setSwitch_specific_type(Integer.parseInt(getParam(param, "switch_specific_type", "I")));
			dnvo.setTrunks(Integer.parseInt(getParam(param, "trunks", "I")));
			dnvo.setType(Integer.parseInt(getParam(param, "type", "I")));
			dnvo.setUse_override(Integer.parseInt(getParam(param, "use_override", "I")));
			dnvo.setDialplan(getParam(param, "dialplan", "S"));
			dnvo.setAnnex_name(getParam(param, "annex_name", "S"));
			dnvo.setAnnex(annex);
		}catch(Exception ex){
			dnvo = null;
		}
		return dnvo;
	}

	@RequestMapping("/dn/setDnState")
	public JSONObject setDnState(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgDN dn = null;

		try {
			cfgdn = new ConfigDn();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				dn = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), vo.getFolder_dbid());

				if (dn != null) { // dn 널이 아니면
					logger.info("set dn State~");
					if(dn.getState().equals(CfgObjectState.CFGEnabled)){
						dn = cfgdn.setDnState(initconfigservice.service, dn, false);
					}else if(dn.getState().equals(CfgObjectState.CFGDisabled)){
						dn = cfgdn.setDnState(initconfigservice.service, dn, true);
					}
				}
			}
			rtnobj.put("command", "setDnState");
			rtnobj.put("state", dn.getState().name());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}
	
	private JSONObject convertDns_jsonObj(Collection<CfgDN> dns) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		
		if(dns != null) {
			for (CfgDN dn : dns) {
				jsonObj = new JSONObject();
				jsonObj.put("dbid", dn.getDBID());
				jsonObj.put("number", dn.getNumber());
				jsonObj.put("type_name", dn.getType().name());
				jsonObj.put("type", dn.getType().ordinal());
				jsonObj.put("switch_dbid", dn.getSwitchDBID());
				jsonObj.put("switch_name", dn.getSwitch().getName());
				jsonObj.put("association", dn.getAssociation());
				jsonObj.put("register_name", dn.getRegisterAll().name());
				jsonObj.put("register", dn.getRegisterAll().ordinal());
				jsonObj.put("state", dn.getState().ordinal());
				jsonObj.put("name", dn.getName());
				jsonObj.put("route_type_name", dn.getRouteType().name());
				jsonObj.put("route_type", dn.getRouteType().ordinal());
				jsonObj.put("group_dbid", dn.getGroupDBID());
				jsonObj.put("use_override_name", dn.getUseOverride().name());
				jsonObj.put("use_override", dn.getUseOverride().ordinal());
				jsonObj.put("dn_login_id", dn.getDNLoginID());
				jsonObj.put("switch_specific_type", dn.getSwitchSpecificType());
				jsonObj.put("trunks", dn.getTrunks());
				jsonObj.put("folderId", dn.getFolderId());
				jsonObj.put("annex", configcommon.getAnnexInfo(dn.getUserProperties()));
				jsonObj.put("obj", "CfgDn");

				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("Dns", jsonArray);
		}else {
			finalJsonObj.put("Dns", null);
		}
		return finalJsonObj;
	}
	
	private JSONObject convertDn_jsonObj(CfgDN dn) {
		JSONObject jsonObj = new JSONObject();
		
		if(dn != null) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", dn.getDBID());
			jsonObj.put("number", dn.getNumber());
			jsonObj.put("type_name", dn.getType().name());
			jsonObj.put("type", dn.getType().ordinal());
			jsonObj.put("switch_dbid", dn.getSwitchDBID());
			jsonObj.put("switch_name", dn.getSwitch().getName());
			jsonObj.put("association", dn.getAssociation());
			jsonObj.put("register_name", dn.getRegisterAll().name());
			jsonObj.put("register", dn.getRegisterAll().ordinal());
			jsonObj.put("state", dn.getState().ordinal());
			jsonObj.put("name", dn.getName());
			jsonObj.put("route_type_name", dn.getRouteType().name());
			jsonObj.put("route_type", dn.getRouteType().ordinal());
			jsonObj.put("group_dbid", dn.getGroupDBID());
			jsonObj.put("use_override_name", dn.getUseOverride().name());
			jsonObj.put("use_override", dn.getUseOverride().ordinal());
			jsonObj.put("dn_login_id", dn.getDNLoginID());
			jsonObj.put("switch_specific_type", dn.getSwitchSpecificType());
			jsonObj.put("trunks", dn.getTrunks());
			jsonObj.put("folderId", dn.getFolderId());
			jsonObj.put("annex", configcommon.getAnnexInfo(dn.getUserProperties()));
			jsonObj.put("obj", "CfgDn");
			jsonObj.put("objectType", dn.getObjectType().ordinal());
		}else {
			jsonObj = null;
		}
		return jsonObj;
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
				if (cfgobjectid.getType().name().equals("CFGDN")) {
					jsonObj = getDn(cfgobjectid.getDBID());
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
				if (cfgobjectid.getType().name().equals("CFGDN")) {
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

	@RequestMapping("/dn/getFolderInfo")
	public JSONObject getDnFolderInfo(@RequestBody QueryVo vo) throws Exception {
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

	@RequestMapping("/dn/getFolderInfo_tree")
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

	@RequestMapping("/dn/moveFolder")
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
