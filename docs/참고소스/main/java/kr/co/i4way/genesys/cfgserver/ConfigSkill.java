package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.i4way.genesys.model.SkillInfoVo;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSkill;
import com.genesyslab.platform.applicationblocks.com.queries.CfgSkillQuery;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigSkill {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	public ConfigSkill() {
	}
			
	public Collection<CfgSkill> getSkillInfo(final IConfService service, int tenant_dbid) {
		Collection<CfgSkill> rtnskills = null;
		CfgSkillQuery skillquery = new CfgSkillQuery();
		skillquery.setTenantDbid(tenant_dbid);
		try {
			rtnskills = service.retrieveMultipleObjects(CfgSkill.class, skillquery);
		} catch (ConfigException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rtnskills;
	}

	public CfgSkill getSkillInfo(final IConfService service, int tenant_dbid, int skill_dbid) {
		CfgSkill rtnskill = null;
		CfgSkillQuery skillquery = new CfgSkillQuery();
		skillquery.setTenantDbid(tenant_dbid);
		skillquery.setDbid(skill_dbid);
		try {
			rtnskill = service.retrieveObject(CfgSkill.class, skillquery);
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return rtnskill; 
	}

	public CfgSkill getSkillInfo(final IConfService service, int tenant_dbid, String skill_name) {
		CfgSkill rtnskill = null;
		CfgSkillQuery skillquery = new CfgSkillQuery();
		skillquery.setTenantDbid(tenant_dbid);
		skillquery.setName(skill_name);
		try {
			rtnskill = service.retrieveObject(CfgSkill.class, skillquery);
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return rtnskill;
	}
	
	/**
	 * Skill을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param SkillInfoVo
	 * @return
	 */
	public CfgSkill createSkill(
			final IConfService service, 
			int tenant_dbid, 
			SkillInfoVo skillinfovo) {
		CfgSkill skill = null;
		try{
			// Read configuration objects:
			skill = new CfgSkill(service);
			skill.setTenantDBID(tenant_dbid);
			skill.setName(skillinfovo.getName());
			skill.setFolderId(skillinfovo.getFolderId());
			if(skillinfovo.getState() == 1){		//State
				skill.setState(CfgObjectState.CFGEnabled);
			}else{
				skill.setState(CfgObjectState.CFGDisabled);
			}

			skill.save();		
			
			if(skill != null) {
				JsonArray psarry = null;
				if(skillinfovo.getAnnex() != null){
					psarry = skillinfovo.getAnnex().getAsJsonArray();
					KeyValueCollection obj_kv = new KeyValueCollection();
					KeyValueCollection insert_kv = null;
					KeyValuePair tmpkvp = null;
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						Set<String> keyarr = ob.keySet();
						Iterator<String> keys = keyarr.iterator();
						String key = keys.next();
						JsonElement ob2 = ob.get(key);
						JsonArray tmparry = ob2.getAsJsonArray();
						insert_kv = new KeyValueCollection();
						for(int j=0; j<tmparry.size(); j++){
							JsonObject tmpob =  tmparry.get(j).getAsJsonObject();
							Set<String> tmpkeyarr = tmpob.keySet();
							Iterator<String> tmpkeys = tmpkeyarr.iterator();
							String tmpkey = tmpkeys.next();
							String tmpValue = tmpob.get(tmpkey).getAsString();
							tmpValue = tmpValue.replaceAll("\\^\\^61\\^", "=");
							tmpValue = tmpValue.replaceAll("\\^\\^at\\^", "@");
							tmpValue = tmpValue.replaceAll("\\^\\^gt\\^", ">");
							tmpValue = tmpValue.replaceAll("\\^\\^lt\\^", "<");
							tmpValue = tmpValue.replaceAll("\\^\\^58\\^", ":");
							tmpValue = tmpValue.replaceAll("\\^\\^123\\^", "{");
							tmpValue = tmpValue.replaceAll("\\^\\^125\\^", "}");
							tmpValue = tmpValue.replaceAll("\\^\\^quot\\^", "\"");
							tmpValue = tmpValue.replaceAll("\\^\\^126\\^", "~");
							tmpValue = tmpValue.replaceAll("\\^\\^coma\\^", ",");
							tmpValue = tmpValue.replaceAll("\\^\\^47\\^", "/");
							insert_kv.addString(tmpkey, tmpValue); 
						}
						tmpkvp = new KeyValuePair(key, insert_kv);
						obj_kv.addPair(tmpkvp);
					}
					skill.setUserProperties(obj_kv);
				}
			}
			skill.save();	
		}catch (ConfigException e) {
			e.printStackTrace();
		}	
		return skill;
	}

	/**
	 * Skill을 수정한다.
	 * @param service
	 * @param CfgSkill
	 * @param SkillInfoVo
	 * @return
	 */
	public CfgSkill modifySkill(
			final IConfService service, 
			CfgSkill skill, 
			SkillInfoVo skillinfovo) {
		try{
			skill.setName(skillinfovo.getName());

			if(skillinfovo.getState() == 1){		//State
				skill.setState(CfgObjectState.CFGEnabled);
			}else{
				skill.setState(CfgObjectState.CFGDisabled);
			}

			skill.save();		
			
			if(skill != null) {
				JsonArray psarry = null;
				if(skillinfovo.getAnnex() != null){
					psarry = skillinfovo.getAnnex().getAsJsonArray();
					KeyValueCollection obj_kv = new KeyValueCollection();
					KeyValueCollection insert_kv = null;
					KeyValuePair tmpkvp = null;
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						Set<String> keyarr = ob.keySet();
						Iterator<String> keys = keyarr.iterator();
						String key = keys.next();
						JsonElement ob2 = ob.get(key);
						JsonArray tmparry = ob2.getAsJsonArray();
						insert_kv = new KeyValueCollection();
						for(int j=0; j<tmparry.size(); j++){
							JsonObject tmpob =  tmparry.get(j).getAsJsonObject();
							Set<String> tmpkeyarr = tmpob.keySet();
							Iterator<String> tmpkeys = tmpkeyarr.iterator();
							String tmpkey = tmpkeys.next();
							String tmpValue = tmpob.get(tmpkey).getAsString();
							tmpValue = tmpValue.replaceAll("\\^\\^61\\^", "=");
							tmpValue = tmpValue.replaceAll("\\^\\^at\\^", "@");
							tmpValue = tmpValue.replaceAll("\\^\\^gt\\^", ">");
							tmpValue = tmpValue.replaceAll("\\^\\^lt\\^", "<");
							tmpValue = tmpValue.replaceAll("\\^\\^58\\^", ":");
							tmpValue = tmpValue.replaceAll("\\^\\^123\\^", "{");
							tmpValue = tmpValue.replaceAll("\\^\\^125\\^", "}");
							tmpValue = tmpValue.replaceAll("\\^\\^quot\\^", "\"");
							tmpValue = tmpValue.replaceAll("\\^\\^126\\^", "~");
							tmpValue = tmpValue.replaceAll("\\^\\^coma\\^", ",");
							tmpValue = tmpValue.replaceAll("\\^\\^47\\^", "/");
							insert_kv.addString(tmpkey, tmpValue); 
						}
						tmpkvp = new KeyValuePair(key, insert_kv);
						obj_kv.addPair(tmpkvp);
					}
					skill.setUserProperties(obj_kv);
				}
			}
			skill.save();				
		}catch (ConfigException e) {
			e.printStackTrace();
		}	
		return skill;
	}
	
	/**
	 * Skill을 삭제한다.
	 * @param service
	 * @param skill
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteSkill(
			final IConfService service,
			CfgSkill skill
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			skill.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	
	
	/**
	 * Skill의 상태를 변경한다. 
	 * @param service
	 * @param skill 	상태 변경할 Skill 객체
	 * @param flag 		(true = Enabled, false = Disabled)
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgSkill setSkillState(
			final IConfService service,
			CfgSkill skill,
			boolean flag
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		if(flag){
			skill.setState(CfgObjectState.CFGEnabled);
		}else{
			skill.setState(CfgObjectState.CFGDisabled);
		}
		skill.save();
		return skill;
	}

}
