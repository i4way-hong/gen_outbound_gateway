package kr.co.i4way.genesys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Dn용 Controller
 * @author jkhong
 *
 */
@Controller
@CrossOrigin(origins = "*")
public class DnController_backup {

	// @Autowired
	// private GenesysInfoVo genesysinfovo;
	// initConfigService initconfigservice = initConfigService.getInstance();
	// ConfigDn cfgdn = null;
	
	// /**
	//  * CfgDN정보를 조회한다.
	//  * @param request
	//  * @return
	//  * @throws Exception
	//  */
	// @ResponseBody
	// @RequestMapping("/dn/getDnsInfo")
	// public JSONObject getDnsInfo(HttpServletRequest request) throws Exception{
	// 	Collection<CfgDN> dninfo = null;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			cfgdn = new ConfigDn();
	// 			dninfo = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid());
	// 			rtnobj = convertDns_jsonObj(dninfo);
	// 		}
	// 		rtnobj.put("command", "getDnsInfo");
	// 	}catch(Exception e) {
	// 	}finally {
	// 		//initconfigservice.closeConfigService();
	// 	}
	// 	return rtnobj;
	// }	
	
	// /**
	//  * CfgDN정보를 조회한다.
	//  * @param request
	//  * @return
	//  * @throws Exception
	//  */
	// @ResponseBody
	// @RequestMapping("/dn/getDnInfo")
	// public JSONObject getDnInfo(HttpServletRequest request) throws Exception{
	// 	CfgDN dninfo = null;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		String dn_number = request.getParameter("dn_number") != null ? request.getParameter("dn_number") : "";
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			cfgdn = new ConfigDn();
	// 			dninfo = cfgdn.getDn(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), dn_number);
	// 			rtnobj = convertDns_jsonObj(dninfo);
	// 		}
	// 		rtnobj.put("command", "getDnInfo");
	// 	}catch(Exception e) {
	// 	}finally {
	// 		//initconfigservice.closeConfigService();
	// 	}
	// 	return rtnobj;
	// }	
	
	// @ResponseBody
	// @RequestMapping("/dn/createDn")
	// public JSONObject createDn(HttpServletRequest request) throws Exception{
	// 	CfgDN dn = null;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		String dn_number = request.getParameter("dn_number") != null ? request.getParameter("dn_number") : "";
	// 		String dn_type = request.getParameter("dn_type") != null ? request.getParameter("dn_type") : "";
	// 		String route_type = request.getParameter("route_type") != null ? request.getParameter("route_type") : "";
	// 		int switch_spec_type = request.getParameter("switch_spec_type") != null ? Integer.parseInt(request.getParameter("switch_spec_type")) : 0;
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			cfgdn = new ConfigDn();
	// 			dn = cfgdn.createDN(initconfigservice.service
	// 								, genesysinfovo.getCfg_tenant_dbid()
	// 								, genesysinfovo.getCfg_switch_dbid()
	// 								, dn_number
	// 								, dn_type
	// 								, route_type
	// 								, switch_spec_type);
	// 			rtnobj = convertDns_jsonObj(dn);				
	// 		}			
	// 		rtnobj.put("command", "createDn");
	// 		rtnobj.put("dn_number", dn_number);
	// 	}catch(Exception e) {
	// 	}finally {
	// 		//initconfigservice.closeConfigService();
	// 	}
	// 	return rtnobj;
	// }	
	
	// @ResponseBody
	// @RequestMapping("/dn/deleteDn")
	// public JSONObject deleteDn(HttpServletRequest request) throws Exception{
	// 	boolean rtnval = false;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		String dn_number = request.getParameter("dn_number") != null ? request.getParameter("dn_number") : "";
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			cfgdn = new ConfigDn();
	// 			rtnval = cfgdn.deleteDN(initconfigservice.service
	// 								, genesysinfovo.getCfg_tenant_dbid()
	// 								, genesysinfovo.getCfg_switch_dbid()
	// 								, dn_number);
	// 		}			
	// 		rtnobj.put("command", "deleteDn");
	// 		rtnobj.put("result", rtnval);
	// 	}catch(Exception e) {
	// 	}finally {
	// 		//initconfigservice.closeConfigService();
	// 	}
	// 	return rtnobj;
	// }	
	
	
	// /**
	//  * CfgDnGroup정보를 조회한다.
	//  * @param request
	//  * @return
	//  * @throws Exception
	//  */
	// @ResponseBody
	// @RequestMapping("/dn/getDnGroupsInfo")
	// public JSONObject getDnGroupsInfo(HttpServletRequest request) throws Exception{
	// 	Collection<CfgDNGroup> dngroupinfo = null;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			cfgdn = new ConfigDn();
	// 			dngroupinfo = cfgdn.getDnGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid());
	// 			rtnobj = convertDnGroup_jsonObj(dngroupinfo);
	// 		}
	// 		rtnobj.put("command", "getDnGroupsInfo");
	// 	}catch(Exception e) {
	// 	}finally {
	// 		//initconfigservice.closeConfigService();
	// 	}
	// 	return rtnobj;
	// }	
	
	// /**
	//  * CfgDnGroup정보를 조회한다.
	//  * @param request
	//  * @return
	//  * @throws Exception
	//  */
	// @ResponseBody
	// @RequestMapping("/dn/getDnGroupInfo")
	// public JSONObject getDnGroupInfo(HttpServletRequest request) throws Exception{
	// 	CfgDNGroup dninfo = null;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		String group_name = request.getParameter("group_name") != null ? request.getParameter("group_name") : "";
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			cfgdn = new ConfigDn();
	// 			dninfo = cfgdn.getDnGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), group_name);
	// 			rtnobj = convertDnGroup_jsonObj(dninfo);
	// 		}
	// 		rtnobj.put("command", "getDnGroupInfo");
	// 	}catch(Exception e) {
	// 	}finally {
	// 		//initconfigservice.closeConfigService();
	// 	}
	// 	return rtnobj;
	// }	
	
	// @ResponseBody
	// @RequestMapping("/dn/createDnGroup")
	// public JSONObject createDnGroup(HttpServletRequest request) throws Exception{
	// 	CfgDNGroup dngroup = null;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		String group_name = request.getParameter("group_name") != null ? request.getParameter("group_name") : "";
	// 		String group_type = request.getParameter("group_type") != null ? request.getParameter("group_type") : "";
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			cfgdn = new ConfigDn();
	// 			dngroup = cfgdn.createDNGroup(initconfigservice.service
	// 								, genesysinfovo.getCfg_tenant_dbid()
	// 								, group_name
	// 								, group_type);
	// 			rtnobj = convertDnGroup_jsonObj(dngroup);				
	// 		}			
	// 		rtnobj.put("command", "createDnGroup");
	// 		rtnobj.put("group_name", group_name);
	// 		rtnobj.put("group_type", group_type);
	// 	}catch(Exception e) {
	// 	}finally {
	// 		//initconfigservice.closeConfigService();
	// 	}
	// 	return rtnobj;
	// }	
	
	// @ResponseBody
	// @RequestMapping("/dn/deleteDnGroup")
	// public JSONObject deleteDnGroup(HttpServletRequest request) throws Exception{
	// 	boolean rtnval = false;
	// 	JSONObject rtnobj = new JSONObject();

	// 	try {
	// 		String group_name = request.getParameter("group_name") != null ? request.getParameter("group_name") : "";
	// 		if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
	// 			cfgdn = new ConfigDn();
	// 			rtnval = cfgdn.deleteDNGroup(initconfigservice.service
	// 								, genesysinfovo.getCfg_tenant_dbid()
	// 								, group_name);
	// 		}			
	// 		rtnobj.put("command", "deleteDnGroup");
	// 		rtnobj.put("result", rtnval);
	// 	}catch(Exception e) {
	// 	}finally {
	// 		//initconfigservice.closeConfigService();
	// 	}
	// 	return rtnobj;
	// }	
	
	
	
	
	// private JSONObject convertDns_jsonObj(Collection<CfgDN> dns) {
	// 	JSONObject jsonObj = new JSONObject();
	// 	JSONArray jsonArray = new JSONArray();
	// 	JSONObject finalJsonObj = new JSONObject();      
	// 	String warpuptm = "";
		
	// 	if(dns != null) {
	// 		for (CfgDN dn : dns) {
	// 			warpuptm = "";
	// 			jsonObj = new JSONObject();
	// 			jsonObj.put("dbid", dn.getDBID());
	// 			jsonObj.put("number", dn.getNumber());
	// 			jsonObj.put("switch_dbid", dn.getSwitchDBID());
	// 			jsonObj.put("tenant_dbid", dn.getTenantDBID());
	// 			jsonObj.put("object_type", dn.getType().toString());
	// 			jsonArray.add(jsonObj);
	// 		}
	// 		finalJsonObj.put("Dns", jsonArray);
	// 	}else {
	// 		finalJsonObj.put("Dns", null);
	// 	}
	// 	return finalJsonObj;
	// }
	
	// private JSONObject convertDns_jsonObj(CfgDN dn) {
	// 	JSONObject jsonObj = new JSONObject();
	// 	JSONArray jsonArray = new JSONArray();
	// 	JSONObject finalJsonObj = new JSONObject();      
		
	// 	if(dn != null) {
	// 		finalJsonObj.put("dbid", dn.getDBID());
	// 		finalJsonObj.put("number", dn.getNumber());
	// 		finalJsonObj.put("switch_dbid", dn.getSwitchDBID());
	// 		finalJsonObj.put("tenant_dbid", dn.getTenantDBID());
	// 		finalJsonObj.put("object_type", dn.getType().toString());
	// 	}
	// 	return finalJsonObj;
	// }
	
	// private JSONObject convertDnGroup_jsonObj(Collection<CfgDNGroup> dngroups) {
	// 	JSONObject jsonObj = new JSONObject();
	// 	JSONArray jsonArray = new JSONArray();
	// 	JSONObject finalJsonObj = new JSONObject();      
	// 	Collection<CfgDNInfo> dns;
	// 	String warpuptm = "";
		
	// 	if(dngroups != null) {
	// 		for (CfgDNGroup dngroup : dngroups) {
	// 			warpuptm = "";
	// 			jsonObj = new JSONObject();
	// 			jsonObj.put("dbid", dngroup.getDBID());
	// 			jsonObj.put("name", dngroup.getGroupInfo().getName());
	// 			jsonObj.put("object_type", dngroup.getType().toString());
	// 			dns = dngroup.getDNs();
	// 			if(dns != null) {
	// 				jsonObj.put("dns", getCfgDnInfoCollectionInfo(dns));
	// 			}
	// 			jsonArray.add(jsonObj);
	// 		}
	// 		finalJsonObj.put("Groups", jsonArray);
	// 	}else {
	// 		finalJsonObj.put("Groups", null);
	// 	}
	// 	return finalJsonObj;
	// }
	
	// private JSONArray getCfgDnInfoCollectionInfo(Collection<CfgDNInfo> coll) {
	// 	JSONArray rtnArray = new JSONArray();
	// 	JSONObject jsonObj;
	// 	for (CfgDNInfo dninfo : coll) {
	// 		jsonObj = new JSONObject();
	// 		jsonObj.put("dbid", dninfo.getDNDBID());
	// 		jsonObj.put("number", dninfo.getDN().getNumber());
	// 		jsonObj.put("switch_dbid", dninfo.getDN().getSwitchDBID());
	// 		jsonObj.put("tenant_dbid", dninfo.getDN().getTenantDBID());
	// 		jsonObj.put("object_type", dninfo.getDN().getType().toString());
	// 		rtnArray.add(jsonObj);
	// 	}
	// 	return rtnArray;
	// }
	
	// private JSONObject convertDnGroup_jsonObj(CfgDNGroup dngroup) {
	// 	JSONObject jsonObj = new JSONObject();
	// 	JSONArray jsonArray = new JSONArray();
	// 	JSONObject finalJsonObj = new JSONObject();      
	// 	Collection<CfgDNInfo> dns;
		
	// 	if(dngroup != null) {
	// 		finalJsonObj.put("dbid", dngroup.getDBID());
	// 		finalJsonObj.put("name", dngroup.getGroupInfo().getName());
	// 		finalJsonObj.put("object_type", dngroup.getType().toString());
	// 		dns = dngroup.getDNs();
	// 		if(dns != null) {
	// 			finalJsonObj.put("dns", getCfgDnInfoCollectionInfo(dns));
	// 		}
	// 	}
	// 	return finalJsonObj;
	// }
}
