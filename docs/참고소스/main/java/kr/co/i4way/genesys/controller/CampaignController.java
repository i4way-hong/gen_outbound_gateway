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

import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingList;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFilter;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTreatment;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.co.i4way.genesys.cfgserver.ConfigCallingList;
import kr.co.i4way.genesys.cfgserver.ConfigCommon;
import kr.co.i4way.genesys.cfgserver.ConfigFolder;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.CallingListInfoVo;
import kr.co.i4way.genesys.model.FilterInfoVo;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.MoveInfoVo;
import kr.co.i4way.genesys.model.QueryVo;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * Campaign용 Controller
 * @author jkhong
 *
 */
@RestController
@CrossOrigin(origins = "*")
public class CampaignController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private GenesysInfoVo genesysinfovo;
	initConfigService initconfigservice = initConfigService.getInstance();
	ConfigCallingList cfgcallinglist = null;
	ConfigFolder cfgfolder = null;
	ConfigCommon configcommon = null;

	public CampaignController(){
		configcommon = new ConfigCommon();
	}
	
	/**
	 * 전체CfgCallingList 정보를 조회한다.
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/campaign/getCallingLists")
	public JSONObject getCallingLists(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgCallingList> callinglists = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgcallinglist = new ConfigCallingList();
				callinglists = cfgcallinglist.getCallingList(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid());				
				rtnobj = convertCallingLists_jsonObj(callinglists);
			}		
			rtnobj.put("command", "getCallingLists");
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	

	/**
	 * CallingList 정보를 조회한다.
	 * @param request("qry_type") : 쿼리타입 name : name으로 쿼리, dbid : dbid로 쿼리
	 * @param request("qry_str") : name or dbid
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/campaign/getCallingList")
	public JSONObject getCallingList(@RequestBody QueryVo vo) throws Exception{
		CfgCallingList callinglist = null;
		JSONObject rtnobj = new JSONObject();
		//Map<String, Object> map = new HashMap<>(); 

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgcallinglist = new ConfigCallingList();
				if(vo.getQry_type().equals("name")) {
					callinglist = cfgcallinglist.getCallingList(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getQry_str());
				}else if(vo.getQry_type().equals("dbid")) {
					callinglist = cfgcallinglist.getCallingList(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), Integer.parseInt(vo.getQry_str()));
				}

				rtnobj = convertCallingList_jsonObj(callinglist);

				rtnobj.put("command", "getCallingList");
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
	
	@RequestMapping("/campaign/createCallingList")
	public JSONObject createCallingList(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgCallingList callinglist = null;
		CallingListInfoVo callinglistvo = null;
		cfgcallinglist = null;

		try {
			callinglistvo = getParseCallingList(param);
			cfgcallinglist = new ConfigCallingList();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				callinglist = cfgcallinglist.getCallingList(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), callinglistvo.getDbid());

				if (callinglist != null) { // 수정모드
					callinglist = cfgcallinglist.modifyCallingList(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), callinglist, callinglistvo);
					logger.info("Modify CallingList~");
				} else {
					callinglist = cfgcallinglist.createCallingList(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), callinglistvo);
					logger.info("Create CallingList~");
				}
				rtnobj = convertCallingList_jsonObj(callinglist);
			}
			rtnobj.put("command", "createDN");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/campaign/modifyCallingList")
	public JSONObject modifyCallingList(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgCallingList callinglist = null;
		CallingListInfoVo callinglistvo = null;
		cfgcallinglist = null;

		try {
			callinglistvo = getParseCallingList(param);
			cfgcallinglist = new ConfigCallingList();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				callinglist = cfgcallinglist.getCallingList(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), callinglistvo.getDbid());

				if (callinglist != null) { // 수정모드
					callinglist = cfgcallinglist.modifyCallingList(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), callinglist, callinglistvo);
					logger.info("Modify CallingList~");
				} else {
					callinglist = cfgcallinglist.createCallingList(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), callinglistvo);
					logger.info("Create CallingList~");
				}
				rtnobj = convertCallingList_jsonObj(callinglist);
			}
			rtnobj.put("command", "createDN");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/campaign/deleteCallingList")
	public JSONObject deleteCallingList(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgCallingList callinglist = null;
		cfgcallinglist = null;
		boolean del_result = false;

		try {
			cfgcallinglist = new ConfigCallingList();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				callinglist = cfgcallinglist.getCallingList(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getSrc_dbid());

				if (callinglist != null) { // 삭제
					logger.info("Delete callinglist~");
					del_result = cfgcallinglist.deleteCallingList(initconfigservice.service, callinglist);
				}
			}
			rtnobj.put("command", "deleteCallingList");
			rtnobj.put("del_result", del_result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

















	/**
	 * 전체CfgFilter 정보를 조회한다.
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/campaign/getFilters")
	public JSONObject getFilters(@RequestBody QueryVo vo) throws Exception{
		Collection<CfgFilter> filters = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgcallinglist = new ConfigCallingList();
				filters = cfgcallinglist.getFilter(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid());			
				rtnobj = convertFilters_jsonObj(filters);
			}		
			rtnobj.put("command", "getFilters");
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	

	/**
	 * Filter 정보를 조회한다.
	 * @param request("qry_type") : 쿼리타입 name : name으로 쿼리, dbid : dbid로 쿼리
	 * @param request("qry_str") : name or dbid
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/campaign/getFilter")
	public JSONObject getFilter(@RequestBody QueryVo vo) throws Exception{
		CfgFilter filter = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgcallinglist = new ConfigCallingList();
				if(vo.getQry_type().equals("name")) {
					filter = cfgcallinglist.getFilter(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getQry_str()); 
				}else if(vo.getQry_type().equals("dbid")) {
					filter = cfgcallinglist.getFilter(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), Integer.parseInt(vo.getQry_str())); 
				}

				rtnobj = convertFilter_jsonObj(filter);

				rtnobj.put("command", "getFilter");
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
	
	@RequestMapping("/campaign/createFilter")
	public JSONObject createFilter(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgFilter filter = null;
		FilterInfoVo filtervo = null;
		cfgcallinglist = null;

		try {
			filtervo = getParseFilter(param);
			cfgcallinglist = new ConfigCallingList();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				filter = cfgcallinglist.getFilter(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), filtervo.getDbid());

				if (filter != null) { // 수정모드
					filter = cfgcallinglist.modifyFilter(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), filter, filtervo);
					logger.info("Modify Filter~");
				} else {
					filter = cfgcallinglist.createFilter(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), filtervo);
					logger.info("Create Filter~");
				}
				rtnobj = convertFilter_jsonObj(filter);
			}
			rtnobj.put("command", "createFilter");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/campaign/modifyFilter")
	public JSONObject modifyFilter(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgFilter filter = null;
		FilterInfoVo filtervo = null;
		cfgcallinglist = null;

		try {
			filtervo = getParseFilter(param);
			cfgcallinglist = new ConfigCallingList();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				filter = cfgcallinglist.getFilter(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), filtervo.getDbid());

				if (filter != null) { // 수정모드
					filter = cfgcallinglist.modifyFilter(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), filter, filtervo);
					logger.info("Modify Filter~");
				} else {
					filter = cfgcallinglist.createFilter(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), filtervo);
					logger.info("Create Filter~");
				}
				rtnobj = convertFilter_jsonObj(filter);
			}
			rtnobj.put("command", "createFilter");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/campaign/deleteFilter")
	public JSONObject deleteFilter(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgFilter filter = null;
		cfgcallinglist = null;
		boolean del_result = false;

		try {
			cfgcallinglist = new ConfigCallingList();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				filter = cfgcallinglist.getFilter(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getDbid());

				if (filter != null) { // 삭제
					logger.info("Delete filter~");
					del_result = cfgcallinglist.deleteFilter(initconfigservice.service, filter);
				}
			}
			rtnobj.put("command", "deleteFilter");
			rtnobj.put("del_result", del_result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	private FilterInfoVo getParseFilter(Map<String, Object> param){
		FilterInfoVo filtervo = new FilterInfoVo();
		JsonParser jparser = new JsonParser();
		
		try {
			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));

			filtervo.setDbid(Integer.parseInt(getParam(param, "dbid", "I")));
			filtervo.setDescription(getParam(param, "description", "S"));
			filtervo.setFolderId(Integer.parseInt(getParam(param, "folderId", "I")));
			filtervo.setFormat_dbid(Integer.parseInt(getParam(param, "format_dbid", "I")));
			filtervo.setName(getParam(param, "name", "S"));
			filtervo.setAnnex(annex);
		}catch(Exception ex){
			filtervo = null;
		}
		return filtervo;
	}
	
	private JSONObject convertFilters_jsonObj(Collection<CfgFilter> filters) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		
		if(filters != null) {
			for (CfgFilter filter : filters) {
				jsonObj = new JSONObject();
				jsonObj.put("dbid", filter.getDBID());
				jsonObj.put("description", filter.getDescription());
				jsonObj.put("name", filter.getName());
				jsonObj.put("format_dbid", filter.getFormatDBID());
				jsonObj.put("state", filter.getState().ordinal());
				jsonObj.put("folderId", filter.getFolderId());
				jsonObj.put("annex", configcommon.getAnnexInfo(filter.getUserProperties()));
				jsonObj.put("obj", "CfgFilter");
				jsonObj.put("objectType", filter.getObjectType().ordinal());

				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("Filters", jsonArray);
		}else {
			finalJsonObj.put("Filters", null);
		}
		return finalJsonObj;
	}

	private JSONObject convertFilter_jsonObj(CfgFilter filter) {
		JSONObject jsonObj = new JSONObject();
		
		if(filter != null) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", filter.getDBID());
			jsonObj.put("description", filter.getDescription());
			jsonObj.put("name", filter.getName());
			jsonObj.put("format_dbid", filter.getFormatDBID());
			jsonObj.put("state", filter.getState().ordinal());
			jsonObj.put("folderId", filter.getFolderId());
			jsonObj.put("annex", configcommon.getAnnexInfo(filter.getUserProperties()));
			jsonObj.put("obj", "CfgFilter");
			jsonObj.put("objectType", filter.getObjectType().ordinal());
		}else {
			jsonObj = null;
		}
		return jsonObj;
	}

	private CallingListInfoVo getParseCallingList(Map<String, Object> param){
		CallingListInfoVo callinglistvo = new CallingListInfoVo();
		JsonParser jparser = new JsonParser();
		
		try {

			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));
			JsonElement treatments = jparser.parse(getParam(param, "treatments", "S"));

			callinglistvo.setDbid(Integer.parseInt(getParam(param, "dbid", "I")));
			callinglistvo.setDescription(getParam(param, "description", "S"));
			callinglistvo.setName(getParam(param, "name", "S"));
			callinglistvo.setTable_access_dbid(Integer.parseInt(getParam(param, "table_access_dbid", "I")));
			callinglistvo.setLog_table_access_dbid(Integer.parseInt(getParam(param, "log_table_access_dbid", "I")));
			callinglistvo.setFilter_dbid(Integer.parseInt(getParam(param, "filter_dbid", "I")));
			callinglistvo.setScript_dbid(Integer.parseInt(getParam(param, "script_dbid", "I")));
			callinglistvo.setCalling_time_from(Integer.parseInt(getParam(param, "calling_time_from", "I")));
			callinglistvo.setCalling_time_to(Integer.parseInt(getParam(param, "calling_time_to", "I")));
			callinglistvo.setMax_attampt(Integer.parseInt(getParam(param, "max_attampt", "I")));
			callinglistvo.setState(Integer.parseInt(getParam(param, "state", "I")));
			callinglistvo.setFolderId(Integer.parseInt(getParam(param, "folderId", "I")));
			callinglistvo.setTreatments(treatments);
			callinglistvo.setAnnex(annex);
		}catch(Exception ex){
			callinglistvo = null;
		}
		return callinglistvo;
	}
	
	private JSONObject convertCallingLists_jsonObj(Collection<CfgCallingList> callinglists) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		
		if(callinglists != null) {
			for (CfgCallingList callinglist : callinglists) {
				jsonObj = new JSONObject();
				jsonObj.put("dbid", callinglist.getDBID());
				jsonObj.put("description", callinglist.getDescription());
				jsonObj.put("name", callinglist.getName());
				jsonObj.put("table_access_dbid", callinglist.getTableAccess().getDBID());
				jsonObj.put("table_access_name", callinglist.getTableAccess().getName());
				jsonObj.put("log_table_access_dbid", callinglist.getLogTableAccess().getDBID());
				jsonObj.put("log_table_access_name", callinglist.getLogTableAccess().getName());
				jsonObj.put("filter_dbid", callinglist.getFilter().getDBID());
				jsonObj.put("filter_name", callinglist.getFilter().getName());
				jsonObj.put("script_dbid", callinglist.getScript().getDBID());
				jsonObj.put("script_name", callinglist.getScript().getName());
				jsonObj.put("calling_time_from", callinglist.getTimeFrom());
				jsonObj.put("calling_time_to", callinglist.getTimeUntil());
				jsonObj.put("max_attampt", callinglist.getMaxAttempts());
				jsonObj.put("state", callinglist.getState().ordinal());
				jsonObj.put("treatments", getCfgTreatmentCollectionInfo(callinglist.getTreatments()));
				jsonObj.put("folderId", callinglist.getFolderId());
				jsonObj.put("annex", configcommon.getAnnexInfo(callinglist.getUserProperties()));
				jsonObj.put("obj", "CfgCallingList");
				jsonObj.put("objectType", callinglist.getObjectType().ordinal());

				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("CallingLists", jsonArray);
		}else {
			finalJsonObj.put("CallingLists", null);
		}
		return finalJsonObj;
	}

	private JSONObject convertCallingList_jsonObj(CfgCallingList callinglist) {
		JSONObject jsonObj = new JSONObject();
		
		if(callinglist != null) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", callinglist.getDBID());
			jsonObj.put("description", callinglist.getDescription());
			jsonObj.put("name", callinglist.getName());
			jsonObj.put("table_access_dbid", callinglist.getTableAccess().getDBID());
			jsonObj.put("table_access_name", callinglist.getTableAccess().getName());
			jsonObj.put("log_table_access_dbid", callinglist.getLogTableAccess().getDBID());
			jsonObj.put("log_table_access_name", callinglist.getLogTableAccess().getName());
			jsonObj.put("filter_dbid", callinglist.getFilter().getDBID());
			jsonObj.put("filter_name", callinglist.getFilter().getName());
			jsonObj.put("script_dbid", callinglist.getScript().getDBID());
			jsonObj.put("script_name", callinglist.getScript().getName());
			jsonObj.put("calling_time_from", callinglist.getTimeFrom());
			jsonObj.put("calling_time_to", callinglist.getTimeUntil());
			jsonObj.put("max_attampt", callinglist.getMaxAttempts());
			jsonObj.put("state", callinglist.getState().ordinal());
			jsonObj.put("treatments", getCfgTreatmentCollectionInfo(callinglist.getTreatments()));
			jsonObj.put("folderId", callinglist.getFolderId());
			jsonObj.put("annex", configcommon.getAnnexInfo(callinglist.getUserProperties()));
			jsonObj.put("obj", "CfgCallingList");
			jsonObj.put("objectType", callinglist.getObjectType().ordinal());
		}else {
			jsonObj = null;
		}
		return jsonObj;
	}
	
	public JSONArray getCfgTreatmentCollectionInfo(Collection<CfgTreatment> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		try {
			for (CfgTreatment cfgtreatment : coll) {
				jsonObj = new JSONObject();
				jsonObj.put("treatment_dbid", cfgtreatment.getDBID());
				jsonObj.put("treatment_name", cfgtreatment.getName());
				rtnArray.add(jsonObj);
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
