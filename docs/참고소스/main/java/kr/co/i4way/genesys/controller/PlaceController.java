package kr.co.i4way.genesys.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.genesyslab.platform.applicationblocks.com.objects.CfgDN;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPlace;
import com.genesyslab.platform.commons.protocol.ChannelState;

import kr.co.i4way.genesys.cfgserver.ConfigPlace;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * Place용 Controller
 * @author jkhong
 *
 */
@Controller
@CrossOrigin(origins = "*")
public class PlaceController {
	@Autowired
	private GenesysInfoVo genesysinfovo;
	initConfigService initconfigservice = initConfigService.getInstance();
	ConfigPlace cfgplace = null;
	
	/**
	 * CfgPlace정보를 조회한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/place/getPlacesInfo")
	public JSONObject getPlacesInfo(HttpServletRequest request) throws Exception{
		Collection<CfgPlace> placeinfo = null;
		JSONObject rtnobj = new JSONObject();

		try {
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgplace = new ConfigPlace();
				placeinfo = cfgplace.getPlace(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid());
				rtnobj = convertPlace_jsonObj(placeinfo);
			}
			rtnobj.put("command", "getDnsInfo");
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	
	
	/**
	 * CfgPlace정보를 조회한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/place/getPlaceInfo")
	public JSONObject getDnInfo(HttpServletRequest request) throws Exception{
		CfgPlace placeinfo = null;
		JSONObject rtnobj = new JSONObject();

		try {
			String place_name = request.getParameter("place_name") != null ? request.getParameter("place_name") : "";
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				cfgplace = new ConfigPlace();
				placeinfo = cfgplace.getPlace(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), place_name);
				rtnobj = convertPlace_jsonObj(placeinfo);
			}
			rtnobj.put("command", "getDnInfo");
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	
	
	@ResponseBody
	@RequestMapping("/place/createPlace")
	public JSONObject createPlace(HttpServletRequest request) throws Exception{
		CfgPlace placeinfo = null;
		JSONObject rtnobj = new JSONObject();

		try {
			String place_name = request.getParameter("place_name") != null ? request.getParameter("place_name") : "";
			String dn = request.getParameter("dn") != null ? request.getParameter("dn") : "";
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				String[] dns = dn.split(",");
				cfgplace = new ConfigPlace();
				placeinfo = cfgplace.createPlace(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), place_name, dns);
				rtnobj = convertPlace_jsonObj(placeinfo);				
			}			
			rtnobj.put("command", "createPlace");
			rtnobj.put("place_name", place_name);
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	
	
	@ResponseBody
	@RequestMapping("/place/modifyPlace")
	public JSONObject modifyPlace(HttpServletRequest request) throws Exception{
		CfgPlace placeinfo = null;
		JSONObject rtnobj = new JSONObject();

		try {
			String old_place_name = request.getParameter("old_place_name") != null ? request.getParameter("old_place_name") : "";
			String new_place_name = request.getParameter("new_place_name") != null ? request.getParameter("new_place_name") : "";
			String dn = request.getParameter("dn") != null ? request.getParameter("dn") : "";
			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				String[] dns = dn.split(",");
				cfgplace = new ConfigPlace();
				placeinfo = cfgplace.modifyPlace(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), genesysinfovo.getCfg_switch_dbid(), old_place_name, new_place_name, dns);
				rtnobj = convertPlace_jsonObj(placeinfo);				
			}			
			rtnobj.put("command", "modifyPlace");
			rtnobj.put("old_place_name", old_place_name);
			rtnobj.put("new_place_name", new_place_name);
		}catch(Exception e) {
		}finally {
			//initconfigservice.closeConfigService();
		}
		return rtnobj;
	}	
	
//	@ResponseBody
//	@RequestMapping("/dn/deleteDn")
//	public JSONObject deleteDn(HttpServletRequest request) throws Exception{
//		boolean rtnval = false;
//		JSONObject rtnobj = new JSONObject();
//
//		try {
//			String dn_number = request.getParameter("dn_number") != null ? request.getParameter("dn_number") : "";
//			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
//				cfgdn = new ConfigDn();
//				rtnval = cfgdn.deleteDN(initconfigservice.service
//									, genesysinfovo.getCfg_tenant_dbid()
//									, genesysinfovo.getCfg_switch_dbid()
//									, dn_number);
//			}			
//			rtnobj.put("command", "deleteDn");
//			rtnobj.put("result", rtnval);
//		}catch(Exception e) {
//		}finally {
//			//initconfigservice.closeConfigService();
//		}
//		return rtnobj;
//	}	
//	
//	
//	/**
//	 * CfgDnGroup정보를 조회한다.
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@ResponseBody
//	@RequestMapping("/dn/getDnGroupsInfo")
//	public JSONObject getDnGroupsInfo(HttpServletRequest request) throws Exception{
//		Collection<CfgDNGroup> dngroupinfo = null;
//		JSONObject rtnobj = new JSONObject();
//
//		try {
//			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
//				cfgdn = new ConfigDn();
//				dngroupinfo = cfgdn.getDnGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid());
//				rtnobj = convertDnGroup_jsonObj(dngroupinfo);
//			}
//			rtnobj.put("command", "getDnGroupsInfo");
//		}catch(Exception e) {
//		}finally {
//			//initconfigservice.closeConfigService();
//		}
//		return rtnobj;
//	}	
//	
//	/**
//	 * CfgDnGroup정보를 조회한다.
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@ResponseBody
//	@RequestMapping("/dn/getDnGroupInfo")
//	public JSONObject getDnGroupInfo(HttpServletRequest request) throws Exception{
//		CfgDNGroup dninfo = null;
//		JSONObject rtnobj = new JSONObject();
//
//		try {
//			String group_name = request.getParameter("group_name") != null ? request.getParameter("group_name") : "";
//			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
//				cfgdn = new ConfigDn();
//				dninfo = cfgdn.getDnGroup(initconfigservice.service, genesysinfovo.getCfg_tenant_dbid(), group_name);
//				rtnobj = convertDnGroup_jsonObj(dninfo);
//			}
//			rtnobj.put("command", "getDnGroupInfo");
//		}catch(Exception e) {
//		}finally {
//			//initconfigservice.closeConfigService();
//		}
//		return rtnobj;
//	}	
//	
//	@ResponseBody
//	@RequestMapping("/dn/createDnGroup")
//	public JSONObject createDnGroup(HttpServletRequest request) throws Exception{
//		CfgDNGroup dngroup = null;
//		JSONObject rtnobj = new JSONObject();
//
//		try {
//			String group_name = request.getParameter("group_name") != null ? request.getParameter("group_name") : "";
//			String group_type = request.getParameter("group_type") != null ? request.getParameter("group_type") : "";
//			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
//				cfgdn = new ConfigDn();
//				dngroup = cfgdn.createDNGroup(initconfigservice.service
//									, genesysinfovo.getCfg_tenant_dbid()
//									, group_name
//									, group_type);
//				rtnobj = convertDnGroup_jsonObj(dngroup);				
//			}			
//			rtnobj.put("command", "createDnGroup");
//			rtnobj.put("group_name", group_name);
//			rtnobj.put("group_type", group_type);
//		}catch(Exception e) {
//		}finally {
//			//initconfigservice.closeConfigService();
//		}
//		return rtnobj;
//	}	
//	
//	@ResponseBody
//	@RequestMapping("/dn/deleteDnGroup")
//	public JSONObject deleteDnGroup(HttpServletRequest request) throws Exception{
//		boolean rtnval = false;
//		JSONObject rtnobj = new JSONObject();
//
//		try {
//			String group_name = request.getParameter("group_name") != null ? request.getParameter("group_name") : "";
//			if(initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
//				cfgdn = new ConfigDn();
//				rtnval = cfgdn.deleteDNGroup(initconfigservice.service
//									, genesysinfovo.getCfg_tenant_dbid()
//									, group_name);
//			}			
//			rtnobj.put("command", "deleteDnGroup");
//			rtnobj.put("result", rtnval);
//		}catch(Exception e) {
//		}finally {
//			//initconfigservice.closeConfigService();
//		}
//		return rtnobj;
//	}	
	
	
	
	
	private JSONObject convertPlace_jsonObj(Collection<CfgPlace> places) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		Collection<CfgDN> dns;
		String warpuptm = "";
		
		if(places != null) {
			for (CfgPlace place : places) {
				warpuptm = "";
				jsonObj = new JSONObject();
				jsonObj.put("dbid", place.getDBID());
				jsonObj.put("name", place.getName());
				dns = place.getDNs();
				if(dns != null) {
					jsonObj.put("dns", getCfgDnInfoCollectionInfo(dns));
				}
				jsonArray.add(jsonObj);
			}
			finalJsonObj.put("Places", jsonArray);
		}else {
			finalJsonObj.put("Places", null);
		}
		return finalJsonObj;
	}
	
	private JSONObject convertPlace_jsonObj(CfgPlace place) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject finalJsonObj = new JSONObject();      
		Collection<CfgDN> dns;
		
		if(place != null) {
			finalJsonObj.put("dbid", place.getDBID());
			finalJsonObj.put("name", place.getName());
			dns = place.getDNs();
			if(dns != null) {
				finalJsonObj.put("dns", getCfgDnInfoCollectionInfo(dns));
			}
		}
		return finalJsonObj;
	}
		
	private JSONArray getCfgDnInfoCollectionInfo(Collection<CfgDN> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		for (CfgDN dn : coll) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", dn.getDBID());
			jsonObj.put("number", dn.getNumber());
			jsonObj.put("switch_dbid", dn.getSwitchDBID());
			jsonObj.put("tenant_dbid", dn.getTenantDBID());
			jsonObj.put("object_type", dn.getType().toString());
			rtnArray.add(jsonObj);
		}
		return rtnArray;
	}
	
}
