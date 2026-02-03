package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLoginInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSkillLevel;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ConfigCommon {
    
	public JSONArray getAnnexInfo(KeyValueCollection kv) {
		JSONObject jsonObj = null;
		JSONArray rtnArray_section = new JSONArray();
		JSONArray rtnArray__value = null;
		JSONObject jsonObj2 = null;
		KeyValuePair tmppair;

		for (Object selectionObj : kv) {
			jsonObj2 = new JSONObject();
			rtnArray__value = new JSONArray();
			KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
			if(!sectionKvp.getStringKey().equals("")){
				for (Object recordObj : sectionKvp.getTKVValue()) {
					jsonObj = new JSONObject();
					tmppair = (KeyValuePair) recordObj;
					jsonObj.put(tmppair.getStringKey(), tmppair.getStringValue());
					rtnArray__value.add(jsonObj);
				}
				jsonObj2.put(sectionKvp.getStringKey(), rtnArray__value);
				rtnArray_section.add(jsonObj2);
			}
		}
		return rtnArray_section;
	} 

	public Boolean checkScript(KeyValueCollection kv) {
		KeyValuePair tmppair;
		Boolean rtnbool = false;

		for (Object selectionObj : kv) {
			KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
			if(sectionKvp.getStringKey().equals("virtual")){
				for (Object recordObj : sectionKvp.getTKVValue()) {
					tmppair = (KeyValuePair) recordObj;
					if(tmppair.getStringKey().equals("script")){
						rtnbool = true;
						break;
					}
				}
			}
		}
		return rtnbool;
	}

	public Boolean checkDialPlan(KeyValueCollection kv) {
		KeyValuePair tmppair;
		Boolean rtnbool = false;

		for (Object selectionObj : kv) {
			KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
			if(sectionKvp.getStringKey().equals("TServer")){
				for (Object recordObj : sectionKvp.getTKVValue()) {
					tmppair = (KeyValuePair) recordObj;
					if(tmppair.getStringKey().equals("service-type") && tmppair.getStringValue().equals("dial-plan")){
						rtnbool = true;
						break;
					}
				}
			}
		}
		return rtnbool;
	}

	public Boolean checkDn_DialPlan(KeyValueCollection kv, String dialplan) {
		KeyValuePair tmppair;
		Boolean rtnbool = false;

		for (Object selectionObj : kv) {
			KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
			if(sectionKvp.getStringKey().equals("TServer")){
				for (Object recordObj : sectionKvp.getTKVValue()) {
					tmppair = (KeyValuePair) recordObj;
					if(tmppair.getStringKey().equals("dial-plan") && tmppair.getStringValue().equals(dialplan)){
						rtnbool = true;
						break;
					}
				}
			}
		}
		return rtnbool;
	}

	public JSONArray getCfgAgentInfoCollectionInfo(Collection<CfgAgentLoginInfo> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		for (CfgAgentLoginInfo cfgagentinfo : coll) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", cfgagentinfo.getAgentLoginDBID());
			jsonObj.put("loginCode", cfgagentinfo.getAgentLogin().getLoginCode());
			jsonObj.put("warpupTime", cfgagentinfo.getWrapupTime());
			jsonObj.put("obj", "CfgAgentLoginInfo");
			rtnArray.add(jsonObj);
		}
		return rtnArray;
	}

	public JSONArray getCfgSkillLevelCollectionInfo(Collection<CfgSkillLevel> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		for (CfgSkillLevel cfgskilllevel : coll) {
			jsonObj = new JSONObject();
			jsonObj.put("dbid", cfgskilllevel.getSkillDBID());
			jsonObj.put("skillName", cfgskilllevel.getSkill().getName());
			jsonObj.put("level", cfgskilllevel.getLevel());
			jsonObj.put("obj", "CfgSkillLevel");
			rtnArray.add(jsonObj);
		}
		return rtnArray;
	}

	public JSONArray getCfgAgentInfoCollectionInfo_HD(Collection<CfgAgentLoginInfo> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		for (CfgAgentLoginInfo cfgagentinfo : coll) {
			jsonObj = new JSONObject();
			jsonObj.put("DBID", cfgagentinfo.getAgentLoginDBID());
			jsonObj.put("LOGIN_CODE", cfgagentinfo.getAgentLogin().getLoginCode());
			jsonObj.put("WARPUP_TIME", cfgagentinfo.getWrapupTime());
			jsonObj.put("OBJECT_TYPE", "CfgAgentLoginInfo");
			rtnArray.add(jsonObj);
		}
		return rtnArray;
	}

	public JSONArray getCfgSkillLevelCollectionInfo_HD(Collection<CfgSkillLevel> coll) {
		JSONArray rtnArray = new JSONArray();
		JSONObject jsonObj;
		for (CfgSkillLevel cfgskilllevel : coll) {
			jsonObj = new JSONObject();
			jsonObj.put("DBID", cfgskilllevel.getSkillDBID());
			jsonObj.put("SKILL_NAME", cfgskilllevel.getSkill().getName());
			jsonObj.put("ELVEL", cfgskilllevel.getLevel());
			jsonObj.put("OBJECT_TYPE", "CfgSkillLevel");
			rtnArray.add(jsonObj);
		}
		return rtnArray;
	}
    
}
