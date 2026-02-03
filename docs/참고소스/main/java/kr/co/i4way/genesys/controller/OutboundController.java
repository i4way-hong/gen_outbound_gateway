package kr.co.i4way.genesys.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.ocserver.OutboundModule;
import net.minidev.json.JSONObject;

/**
 * Place용 Controller
 * @author jkhong
 *
 */
@Controller
@CrossOrigin(origins = "*")
public class OutboundController {
	@Autowired
	private GenesysInfoVo genesysinfovo;
	private OutboundModule om = null;
	
	/**
	 * Campaign을 로드한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/outbound/loadCampaign")
	public JSONObject loadCampaign(HttpServletRequest request) throws Exception{
		JSONObject rtnobj = new JSONObject();
		String rtnval = "";
		try {
			int campaign_dbid = request.getParameter("campaign_dbid") != null ? Integer.parseInt(request.getParameter("campaign_dbid")) : 0;
			int group_dbid = request.getParameter("group_dbid") != null ? Integer.parseInt(request.getParameter("group_dbid")) : 0;
			rtnobj.put("campaign_dbid", campaign_dbid);
			rtnobj.put("group_dbid", group_dbid);
			rtnobj.put("command", "loadCampaign");

			om = new OutboundModule();
			om.setOcServerInfo(genesysinfovo.getOc_ip_p() , genesysinfovo.getOc_port_p(), genesysinfovo.getOc_charset());
			if(om.connectOcServer()) {
				rtnval = om.loadCampaign(campaign_dbid, group_dbid);
				rtnobj.put("result", rtnval);
			}
		}catch(Exception e) {
		}finally {
			if(om.disConnectOcServer()) {
				om = null;
			}
		}
		return rtnobj;
	}	
	
	/**
	 * Campaign을 언로드한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/outbound/unloadCampaign")
	public JSONObject unloadCampaign(HttpServletRequest request) throws Exception{
		JSONObject rtnobj = new JSONObject();
		String rtnval = "";
		try {
			int campaign_dbid = request.getParameter("campaign_dbid") != null ? Integer.parseInt(request.getParameter("campaign_dbid")) : 0;
			int group_dbid = request.getParameter("group_dbid") != null ? Integer.parseInt(request.getParameter("group_dbid")) : 0;
			rtnobj.put("campaign_dbid", campaign_dbid);
			rtnobj.put("group_dbid", group_dbid);
			rtnobj.put("command", "unloadCampaign");

			om = new OutboundModule();
			om.setOcServerInfo(genesysinfovo.getOc_ip_p() , genesysinfovo.getOc_port_p(), genesysinfovo.getOc_charset());
			if(om.connectOcServer()) {
				rtnval = om.unloadCampaign(campaign_dbid, group_dbid);
				rtnobj.put("result", rtnval);
			}
		}catch(Exception e) {
		}finally {
			if(om.disConnectOcServer()) {
				om = null;
			}
		}
		return rtnobj;
	}	
	
	/**
	 * 다이얼링을 시작한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/outbound/startDial")
	public JSONObject startDial(HttpServletRequest request) throws Exception{
		JSONObject rtnobj = new JSONObject();
		String rtnval = "";
		try {
			int campaign_dbid = request.getParameter("campaign_dbid") != null ? Integer.parseInt(request.getParameter("campaign_dbid")) : 0;
			int group_dbid = request.getParameter("group_dbid") != null ? Integer.parseInt(request.getParameter("group_dbid")) : 0;
			String dial_mode = request.getParameter("dial_mode") != null ? request.getParameter("dial_mode") : "";
			String opti_method = request.getParameter("opti_method") != null ? request.getParameter("opti_method") : "";
			int opti_goal = request.getParameter("opti_goal") != null ? Integer.parseInt(request.getParameter("opti_goal")) : 0;
			rtnobj.put("campaign_dbid", campaign_dbid);
			rtnobj.put("group_dbid", group_dbid);
			
			rtnobj.put("dialingMode", dial_mode);
			rtnobj.put("optimizeMethod", opti_method);
			rtnobj.put("optimizeGoal", opti_goal);
			
			rtnobj.put("command", "startDial");

			om = new OutboundModule();
			om.setOcServerInfo(genesysinfovo.getOc_ip_p() , genesysinfovo.getOc_port_p(), genesysinfovo.getOc_charset());
			if(om.connectOcServer()) {
				rtnval = om.startDialing(campaign_dbid, group_dbid, dial_mode, opti_method, opti_goal);
				rtnobj.put("result", rtnval);
			}
		}catch(Exception e) {
		}finally {
			if(om.disConnectOcServer()) {
				om = null;
			}
		}
		return rtnobj;
	}	
	
	/**
	 * 다이얼링을 정지한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/outbound/stopDial")
	public JSONObject stopDial(HttpServletRequest request) throws Exception{
		JSONObject rtnobj = new JSONObject();
		String rtnval = "";
		try {
			int campaign_dbid = request.getParameter("campaign_dbid") != null ? Integer.parseInt(request.getParameter("campaign_dbid")) : 0;
			int group_dbid = request.getParameter("group_dbid") != null ? Integer.parseInt(request.getParameter("group_dbid")) : 0;
			rtnobj.put("campaign_dbid", campaign_dbid);
			rtnobj.put("group_dbid", group_dbid);
			rtnobj.put("command", "stopDial");

			om = new OutboundModule();
			om.setOcServerInfo(genesysinfovo.getOc_ip_p() , genesysinfovo.getOc_port_p(), genesysinfovo.getOc_charset());
			if(om.connectOcServer()) {
				rtnval = om.stopDialing(campaign_dbid, group_dbid);
				rtnobj.put("result", rtnval);
			}
		}catch(Exception e) {
		}finally {
			if(om.disConnectOcServer()) {
				om = null;
			}
		}
		return rtnobj;
	}	
	
	/**
	 * Campaign 상태를 체크한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/outbound/checkCampaignStatus")
	public JSONObject checkCampaignStatus(HttpServletRequest request) throws Exception{
		JSONObject rtnobj = new JSONObject();
		String rtnval = "";
		try {
			int campaign_dbid = request.getParameter("campaign_dbid") != null ? Integer.parseInt(request.getParameter("campaign_dbid")) : 0;
			int group_dbid = request.getParameter("group_dbid") != null ? Integer.parseInt(request.getParameter("group_dbid")) : 0;
			rtnobj.put("campaign_dbid", campaign_dbid);
			rtnobj.put("group_dbid", group_dbid);
			rtnobj.put("command", "checkCampaignStatus");

			om = new OutboundModule();
			om.setOcServerInfo(genesysinfovo.getOc_ip_p() , genesysinfovo.getOc_port_p(), genesysinfovo.getOc_charset());
			if(om.connectOcServer()) {
				rtnval = om.getCampaignStat(campaign_dbid, group_dbid);
				rtnobj.put("result", rtnval);
			}
		}catch(Exception e) {
		}finally {
			if(om.disConnectOcServer()) {
				om = null;
			}
		}
		return rtnobj;
	}	
	
	
}
