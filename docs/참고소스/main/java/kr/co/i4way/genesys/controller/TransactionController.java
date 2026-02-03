package kr.co.i4way.genesys.controller;

import kr.co.i4way.genesys.cfgserver.ConfigCommon;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.objects.CfgObjectID;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTransaction;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.co.i4way.genesys.cfgserver.ConfigFolder;
import kr.co.i4way.genesys.cfgserver.ConfigTransaction;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.QueryVo;
import kr.co.i4way.genesys.model.TransactionVo;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "*")
public class TransactionController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private GenesysInfoVo genesysinfovo;
	initConfigService initconfigservice = initConfigService.getInstance();
	ConfigFolder cfgfolder = null;
	ConfigTransaction cfgtransaction = null;
	ConfigCommon configcommon = null;

	public TransactionController(){
		configcommon = new ConfigCommon();
	}

	@RequestMapping("/transaction/getTransactionInfo")
	public JSONObject getTransactionInfo(@RequestBody QueryVo vo) throws Exception {
		CfgTransaction transaction = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgtransaction = new ConfigTransaction(); 
				if(vo.getQry_type().equals("name")){
					transaction = cfgtransaction.getTransactionInfo_name(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getQry_str());
				}else{
					transaction = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), Integer.parseInt(vo.getQry_str()));
				}
				
				rtnobj = convertTransactionInfo_jsonObj(transaction);
			}
			rtnobj.put("command", "getTransactionInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	@RequestMapping("/transaction/getFolderInfo")
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

	private JSONObject convertTransactionInfo_jsonObj(CfgTransaction transaction) {
		JSONObject finalJsonObj = new JSONObject();

		if (transaction != null) {
			finalJsonObj.put("dbid", transaction.getDBID());
			finalJsonObj.put("name", transaction.getName());
			finalJsonObj.put("folderId", transaction.getFolderId());
			finalJsonObj.put("type", transaction.getType().name());
			finalJsonObj.put("typeId", transaction.getType().ordinal());
			finalJsonObj.put("alias", transaction.getAlias());
			finalJsonObj.put("recordingPeriod", transaction.getRecordPeriod());
			finalJsonObj.put("state", transaction.getState().ordinal());
			finalJsonObj.put("annex", configcommon.getAnnexInfo(transaction.getUserProperties()));
			finalJsonObj.put("obj", "CfgTransaction");
		}
		return finalJsonObj;
	}

	public JSONArray getCfgObjectIDCollectionInfo(Collection<CfgObjectID> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		try {
			for (CfgObjectID cfgobjectid : coll) {
				jsonObj = new JSONObject();
				if (cfgobjectid.getType().name().equals("CFGTransaction")) {
					jsonObj = getTransactionInfo(cfgobjectid.getDBID());
				} else if (cfgobjectid.getType().name().equals("CFGFolder")) {
					jsonObj = getFolderInfo(cfgobjectid.getType().ordinal(), cfgobjectid.getDBID());
					jsonObj.put("obj", "CfgFolder");
				}
				rtnArray.add(jsonObj);
			}
		} catch (Exception ex) {
		}
		return rtnArray;
	}

	private JSONObject getTransactionInfo(int dbid) throws Exception {
		CfgTransaction transaction = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgtransaction = new ConfigTransaction();
				transaction = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(),
						dbid);
				rtnobj = convertTransactionInfo_jsonObj(transaction);
			}
			rtnobj.put("command", "getTransactionInfo");
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

    public JSONObject convertFolderInfo_jsonObj(CfgFolder folder) {
		JSONObject finalJsonObj = new JSONObject();

		if (folder != null) {

			finalJsonObj.put("dbid", folder.getDBID());
			finalJsonObj.put("name", folder.getName());
			finalJsonObj.put("objectType", folder.getObjectType().ordinal());
			finalJsonObj.put("folderId", folder.getFolderId());
			finalJsonObj.put("objectDbid", folder.getObjectDbid());
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

	
	@RequestMapping("/transaction/createTransaction")
	public JSONObject createTransaction(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgTransaction tr = null;
		TransactionVo transactionvo = null;
		cfgtransaction = null;

		try {
			transactionvo = getParseTransaction(param);
			cfgtransaction = new ConfigTransaction();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(transactionvo.getDbid() > 0){
					tr = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo.getDbid());
				}

				if (tr != null) { // 수정모드 
					tr = cfgtransaction.modifyTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), tr, transactionvo);
					logger.info("Modify Transaction~"); 
				} else {
					tr = cfgtransaction.createTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo);
					logger.info("Create Transaction~");
				}
				rtnobj = convertTransaction_jsonObj(tr);
			}
			rtnobj.put("command", "createTransaction");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/transaction/modifyTransaction")
	public JSONObject modifyTransaction(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgTransaction tr = null;
		TransactionVo transactionvo = null;
		cfgtransaction = null;

		try {
			transactionvo = getParseTransaction(param);
			cfgtransaction = new ConfigTransaction();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(transactionvo.getDbid() > 0){
					tr = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo.getDbid());
				}

				if (tr != null) { // 수정모드 
					tr = cfgtransaction.modifyTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), tr, transactionvo);
					logger.info("Modify Transaction~"); 
				} else {
					tr = cfgtransaction.createTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo);
					logger.info("Create Transaction~");
				}
				rtnobj = convertTransaction_jsonObj(tr);
			}
			rtnobj.put("command", "createTransaction");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/transaction/deleteTransaction")
	public JSONObject deleteTransaction(@RequestBody QueryVo vo) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgTransaction tr = null;
		CfgFolder folder = null;
		boolean del_result = false;
		cfgtransaction = null;

		try {
			cfgtransaction = new ConfigTransaction();
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				tr = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), vo.getDbid());

				if (tr != null) { // 삭제
					logger.info("Delete Transaction~");
					del_result = cfgtransaction.deleteTransaction(initconfigservice.service, tr);
				}

				cfgfolder = new ConfigFolder();
				folder = cfgfolder.getFolderInfo(initconfigservice.service, vo.getFolder_type(), vo.getFolder_dbid());
				rtnobj = convertFolderInfo_jsonObj(folder);
			}
			rtnobj.put("command", "deleteTransaction");
			rtnobj.put("del_result", del_result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/transaction/addSection")
	public JSONObject addSection(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgTransaction tr = null;
		TransactionVo transactionvo = null;
		cfgtransaction = null;

		try {
			transactionvo = getParseTransaction(param);
			cfgtransaction = new ConfigTransaction();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(transactionvo.getDbid() > 0){
					tr = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo.getDbid());
				}

				if (tr != null) { // 수정모드 
					tr = cfgtransaction.addSection(initconfigservice.service, tr, transactionvo);
					logger.info("addSection~"); 
				} else {
					//tr = cfgtransaction.createTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo);
					logger.info("Transaction Is null~");
				}
				rtnobj = convertTransaction_jsonObj(tr);
			}
			rtnobj.put("command", "addSection");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/transaction/modifySection")
	public JSONObject modifySection(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgTransaction tr = null;
		TransactionVo transactionvo = null;
		cfgtransaction = null;

		try {
			transactionvo = getParseTransaction(param);
			cfgtransaction = new ConfigTransaction();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(transactionvo.getDbid() > 0){
					tr = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo.getDbid());
				}

				if (tr != null) { // 수정모드 
					tr = cfgtransaction.modifySection(initconfigservice.service, tr, transactionvo);
					logger.info("modifySection~"); 
				} else {
					//tr = cfgtransaction.createTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo);
					logger.info("Transaction Is null~");
				}
				rtnobj = convertTransaction_jsonObj(tr);
			}
			rtnobj.put("command", "modifySection");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/transaction/removeSection")
	public JSONObject removeSection(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgTransaction tr = null;
		TransactionVo transactionvo = null;
		cfgtransaction = null;

		try {
			transactionvo = getParseTransaction(param);
			cfgtransaction = new ConfigTransaction();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(transactionvo.getDbid() > 0){
					tr = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo.getDbid());
				}

				if (tr != null) { // 삭제모드 
					tr = cfgtransaction.removeSection(initconfigservice.service, tr, transactionvo);
					logger.info("removeSection~"); 
				} else {
					//tr = cfgtransaction.createTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo);
					logger.info("Transaction Is null~");
				}
				rtnobj = convertTransaction_jsonObj(tr);
			}
			rtnobj.put("command", "removeSection");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	
	@RequestMapping("/transaction/addOption")
	public JSONObject addOption(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgTransaction tr = null;
		TransactionVo transactionvo = null;
		cfgtransaction = null;

		try {
			transactionvo = getParseTransaction(param);
			cfgtransaction = new ConfigTransaction();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(transactionvo.getDbid() > 0){
					tr = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo.getDbid());
				}

				if (tr != null) { 
					tr = cfgtransaction.addOption(initconfigservice.service, tr, transactionvo);
					logger.info("addOption~"); 
				} else {
					//tr = cfgtransaction.createTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo);
					logger.info("Transaction Is null~");
				}
				rtnobj = convertTransaction_jsonObj(tr);
			}
			rtnobj.put("command", "addOption");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/transaction/modifyOption")
	public JSONObject modifyOption(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgTransaction tr = null;
		TransactionVo transactionvo = null;
		cfgtransaction = null;

		try {
			transactionvo = getParseTransaction(param);
			cfgtransaction = new ConfigTransaction();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(transactionvo.getDbid() > 0){
					tr = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo.getDbid());
				}

				if (tr != null) { 
					tr = cfgtransaction.modifyOption(initconfigservice.service, tr, transactionvo);
					logger.info("modifyOption~"); 
				} else {
					//tr = cfgtransaction.createTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo);
					logger.info("Transaction Is null~");
				}
				rtnobj = convertTransaction_jsonObj(tr);
			}
			rtnobj.put("command", "modifyOption");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/transaction/removeOption")
	public JSONObject removeOption(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgTransaction tr = null;
		TransactionVo transactionvo = null;
		cfgtransaction = null;

		try {
			transactionvo = getParseTransaction(param);
			cfgtransaction = new ConfigTransaction();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(transactionvo.getDbid() > 0){
					tr = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo.getDbid());
				}

				if (tr != null) { // 삭제모드 
					tr = cfgtransaction.removeOption(initconfigservice.service, tr, transactionvo);
					logger.info("removeOption~"); 
				} else {
					//tr = cfgtransaction.createTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo);
					logger.info("Transaction Is null~");
				}
				rtnobj = convertTransaction_jsonObj(tr);
			}
			rtnobj.put("command", "removeOption");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	@RequestMapping("/transaction/saveOptions")
	public JSONObject saveOptions(@RequestBody Map<String, Object> param) throws Exception {
		JSONObject rtnobj = new JSONObject();
		CfgTransaction tr = null;
		TransactionVo transactionvo = null;
		cfgtransaction = null;

		try {
			transactionvo = getParseSaveOptions(param);
			cfgtransaction = new ConfigTransaction();

			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				if(transactionvo.getDbid() > 0){
					tr = cfgtransaction.getTransactionInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo.getDbid());
				}

				if (tr != null) { 
					tr = cfgtransaction.saveOptions(initconfigservice.service, tr, transactionvo);
					logger.info("saveOptions~"); 
				} else {
					//tr = cfgtransaction.createTransaction(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), transactionvo);
					logger.info("Transaction Is null~");
				}
				rtnobj = convertTransaction_jsonObj(tr);
			}
			rtnobj.put("command", "saveOptions");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// initconfigservice.closeConfigService();
		}
		return rtnobj;
	}

	private JSONObject convertTransaction_jsonObj(CfgTransaction tr) {
		JSONObject jsonObj = new JSONObject();
		
		if(tr != null) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", tr.getDBID());
			jsonObj.put("type_name", tr.getType().name());
			jsonObj.put("type", tr.getType().ordinal());
			jsonObj.put("state", tr.getState().ordinal());
			jsonObj.put("name", tr.getName());
			jsonObj.put("alias", tr.getAlias());
			jsonObj.put("folderId", tr.getFolderId());
			jsonObj.put("annex", configcommon.getAnnexInfo(tr.getUserProperties()));
			jsonObj.put("obj", "CfgTransaction");
			jsonObj.put("objectType", tr.getObjectType().ordinal());
		}else {
			jsonObj = null;
		}
		return jsonObj;
	}

	private TransactionVo getParseTransaction(Map<String, Object> param){
		TransactionVo vo = new TransactionVo();
		JsonParser jparser = new JsonParser();
		
		try {

			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement annex = jparser.parse(getParam(param, "annex", "S"));

			vo.setDbid(Integer.parseInt(getParam(param, "dbid", "I")));
			vo.setFolderId(Integer.parseInt(getParam(param, "folderId", "I")));
			vo.setName(getParam(param, "name", "S"));
			vo.setSection_name(getParam(param, "section", "S"));
			vo.setChange_section_name(getParam(param, "change_section", "S"));
			vo.setKey(getParam(param, "key", "S"));
			vo.setChange_key(getParam(param, "change_key", "S"));
			vo.setValue(getParam(param, "value", "S"));
			vo.setAlias(getParam(param, "alias", "S"));
			vo.setOption_alias(getParam(param, "option_alias", "S"));
			vo.setData_type(getParam(param, "data_type", "S"));
			vo.setType(Integer.parseInt(getParam(param, "type", "I")));
			vo.setState(Integer.parseInt(getParam(param, "state", "I")));
			vo.setAnnex(annex);
		}catch(Exception ex){
			vo = null;
		}
		return vo;
	}

	private TransactionVo getParseSaveOptions(Map<String, Object> param){
		TransactionVo vo = new TransactionVo();
		JsonParser jparser = new JsonParser();
		
		try {

			// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
			JsonElement options = jparser.parse(getParam(param, "options", "S"));

			vo.setDbid(Integer.parseInt(getParam(param, "dbid", "I")));
			vo.setSection_name(getParam(param, "section", "S"));
			vo.setAnnex(options);
		}catch(Exception ex){
			vo = null;
		}
		return vo;
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