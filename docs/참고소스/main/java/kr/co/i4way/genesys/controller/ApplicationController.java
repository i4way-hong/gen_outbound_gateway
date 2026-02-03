package kr.co.i4way.genesys.controller;

import kr.co.i4way.genesys.cfgserver.ConfigApplication;
import kr.co.i4way.genesys.cfgserver.ConfigCommon;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgServer;
import com.genesyslab.platform.commons.protocol.ChannelState;

import kr.co.i4way.genesys.cfgserver.ConfigFolder;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.QueryVo;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@CrossOrigin(origins = "*")
public class ApplicationController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	@Autowired
	private GenesysInfoVo genesysinfovo;
	initConfigService initconfigservice = initConfigService.getInstance();
	ConfigFolder cfgfolder = null;
	ConfigApplication cfgapplication = null;
	ConfigCommon configcommon = null;

	public ApplicationController(){
		configcommon = new ConfigCommon();
	}

	@RequestMapping("/application/getApplicationInfo")
	public JSONObject getApplicationInfo(@RequestBody QueryVo vo) throws Exception {
		CfgApplication application = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgapplication = new ConfigApplication();
				int dbid = Integer.parseInt(vo.getQry_str());
				application = cfgapplication.getApplicationInfo_dbid(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), dbid);
				
				rtnobj = convertApplicationInfo_jsonObj(application);
			}
			rtnobj.put("command", "getTransactionInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	@RequestMapping("/application/getApplicationInfoAll")
	public JSONObject getApplicationInfoAll(@RequestBody QueryVo vo) throws Exception {
		Collection<CfgApplication> application = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgapplication = new ConfigApplication();
				application = cfgapplication.getApplicationInfo(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid());
				
				rtnobj = convertApplicationInfo_jsonObj(application);
			}
			rtnobj.put("command", "getTransactionInfo");
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		return rtnobj;
	}

	private JSONObject convertApplicationInfo_jsonObj(Collection<CfgApplication> applications) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		//Collection<CfgPerson> persons;
		
		if(applications != null) {
			for (CfgApplication application : applications) {
				//persons = null;
				jsonObj = new JSONObject();
				jsonObj.put("dbid", application.getDBID());
				jsonObj.put("name", application.getName());

				jsonObj.put("type", application.getType().toString().replaceAll("CFG", ""));
				jsonObj.put("version", application.getVersion());
				jsonObj.put("is_server", application.getIsServer().name());
				jsonObj.put("state", application.getState().name());
				jsonObj.put("work_dir", application.getWorkDirectory());
				jsonObj.put("cmd_line", application.getCommandLine());
				jsonObj.put("cmd_line_arg", application.getCommandLineArguments());
				String rtnport = "";
				String backup_server = "";
				Collection<CfgPortInfo> portinfo = application.getPortInfos();
				for (CfgPortInfo info : portinfo) {
					rtnport += info.getPort() + ",";
				}
				jsonObj.put("port", rtnport);
				if(application.getServerInfo() != null){
					if(application.getServerInfo().getBackupServer() != null){
						backup_server = application.getServerInfo().getBackupServer().getName();
					}
				}

				jsonObj.put("backup_server", backup_server);

				jsonObj.put("auto_restart", application.getAutoRestart().name());
				jsonObj.put("startup_timeout", application.getStartupTimeout());
				jsonObj.put("shutdown_timeout", application.getShutdownTimeout());
				jsonObj.put("rdndncy_type", application.getRedundancyType().name());
				jsonObj.put("is_primary", application.getIsPrimary().name());
				if(application.getServerInfo() != null){
					jsonObj.put("host_name", application.getServerInfo().getHost().getName());
				}

				jsonArray.add(jsonObj);

			}
			finalJsonObj.put("applications", jsonArray);
		}else {
			finalJsonObj.put("applications", null);
		}
		return finalJsonObj;
	}

	private JSONObject convertApplicationInfo_jsonObj(CfgApplication application) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		//Collection<CfgPerson> persons;
		
		if(application != null) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", application.getDBID());
				jsonObj.put("name", application.getName());

				jsonObj.put("type", application.getType().name());
				jsonObj.put("version", application.getVersion());
				jsonObj.put("is_server", application.getIsServer().name());
				jsonObj.put("state", application.getState().name());
				jsonObj.put("work_dir", application.getWorkDirectory());
				jsonObj.put("cmd_line", application.getCommandLine());
				jsonObj.put("cmd_line_arg", application.getCommandLineArguments());

				jsonObj.put("auto_restart", application.getAutoRestart().name());
				jsonObj.put("startup_timeout", application.getStartupTimeout());
				jsonObj.put("shutdown_timeout", application.getShutdownTimeout());
				jsonObj.put("rdndncy_type", application.getRedundancyType().name());
				jsonObj.put("is_primary", application.getIsPrimary().name());
				jsonObj.put("host_name", application.getServerInfo().getHost().getName());

			jsonArray.add(jsonObj);
			finalJsonObj.put("applications", jsonArray);
		}else {
			finalJsonObj.put("applications", null);
		}
		return finalJsonObj;
	}



}