package kr.co.i4way.genesys.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.reporting.protocol.statserver.AgentGroup;
import com.genesyslab.platform.reporting.protocol.statserver.AgentStatus;
import com.genesyslab.platform.reporting.protocol.statserver.AgentStatusesCollection;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventInfo;
import com.google.gson.JsonParser;

import kr.co.i4way.common.util.JsonUtil;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.HundaiModelVo;
import kr.co.i4way.genesys.model.SkillStatInfoVo;
import kr.co.i4way.genesys.ocserver.OutboundModule;
import kr.co.i4way.genesys.statserver.StatCtrl;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * Place용 Controller
 * @author jkhong
 *
 */
@Controller
@CrossOrigin(origins = "*")
public class StatController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	private StatCtrl stat;
	@Autowired
	private GenesysInfoVo genesysinfovo;
	
	private SkillStatInfoVo getParseSkillStat(Map<String, Object> param){
		SkillStatInfoVo skillstatinfovo = new SkillStatInfoVo();
		JsonParser jparser = new JsonParser();
		try {
			skillstatinfovo.setTenant_dbid(Integer.parseInt(getParam(param, "TENANT_DBID", "I")));
			skillstatinfovo.setDbid(Integer.parseInt(getParam(param, "DBID", "I")));
			skillstatinfovo.setName(getParam(param, "NAME", "S"));
		}catch(Exception ex){
			logger.error("Exception", ex);
		}
		return skillstatinfovo;
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
	
	/**
	 * Campaign을 로드한다.
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/1234567890")
	public JSONObject getStat(@RequestBody Map<String, Object> param) throws Exception{
		JSONObject rtnobj = new JSONObject();
		AgentStatusesCollection as_list;
		SkillStatInfoVo skillinfovo = null;
		String CurrValue = "";
		
		skillinfovo = getParseSkillStat(param);
		JSONArray jsonarryArray = new JSONArray();
		stat = new StatCtrl();
		try {
			stat.initalize(genesysinfovo.getStat_ip_p(), genesysinfovo.getStat_ip_b(), genesysinfovo.getStat_endpoint1()
					, genesysinfovo.getStat_endpoint2(), genesysinfovo.getSc_port_p(), genesysinfovo.getSc_port_b()
					, genesysinfovo.getStat_clientname(), genesysinfovo.getStat_clientname()
					, genesysinfovo.getCfg_charset(), skillinfovo.getName(), "CurrentAllAgentState", genesysinfovo.getStat_timeout(), genesysinfovo.getStat_delay());

			//stat.initalize("10.10.61.29", "10.10.61.29", "statserver_rtm", "statserver_rtm2", 2032, 2032, "TEST", "TEST", "MS949", "테스트1", "CurrentAllAgentState");
			stat.openProtocol();
			if(stat.protocol.getState() == ChannelState.Opened) {
				stat.setStatistics_GA("테스트1", "CurrentAllAgentState");
				EventInfo eventinfo = stat.eventinfo;
				if(eventinfo != null) {
					AgentGroup group = (AgentGroup)eventinfo.getStateValue();
					if(group == null)
						return null;
					if(group.getAgents() == null || group.getAgentCount() <= 0) return null;
					as_list = group.getAgents();
					for(int j=0; j< as_list.getCount();j++){
						JSONObject tmpobj = new JSONObject();
						AgentStatus as = (AgentStatus)as_list.getItem(j);
						CurrValue = as.getStatus() + "";
						tmpobj.put("EMPLOYEE_ID", as.getAgentId());
						tmpobj.put("STAT", CurrValue);
						jsonarryArray.add(tmpobj);
					}
				}
				rtnobj.put("USERS", jsonarryArray);
			}
		}catch(Exception ex) {
			logger.error("StatController.getStat", ex);
			if(stat.protocol.getState() == ChannelState.Opened) {
				stat.closeProtocol();
			}
		}finally {
			logger.info(rtnobj.toJSONString());
			if(stat.protocol.getState() == ChannelState.Opened) {
				stat.closeProtocol();
			}
			stat = null;
		}	
		return rtnobj;
	}	
}
