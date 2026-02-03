package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.i4way.genesys.model.PersonInfoVo;
import kr.co.i4way.genesys.model.HundaiModelVo;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLogin;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLoginInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSkillLevel;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPersonQuery;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigPerson {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	ConfigAgentLogin cfgAgentLogin = null;
	ConfigAgentGroup cfgAgentGroup = null;
	public ConfigPerson() {
		cfgAgentLogin = new ConfigAgentLogin();
		cfgAgentGroup = new ConfigAgentGroup();
	}
	
	public CfgPerson getPersonInfo(final IConfService service, int tenant_dbid, String qyery_type, String query_string) {
		CfgPerson rtnCfgPerson = null;
		CfgPersonQuery personquery = new CfgPersonQuery();
		personquery.setTenantDbid(tenant_dbid);
		if(qyery_type.equals("usr")) {
			personquery.setUserName(query_string);
		}else if(qyery_type.equals("emp")) {
			personquery.setEmployeeId(query_string);
		}
		try {
			rtnCfgPerson = service.retrieveObject(CfgPerson.class, personquery);
		} catch (ConfigException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return rtnCfgPerson;
	}

	public Collection<CfgPerson> getSkillDependency(final IConfService service, int tenant_dbid, int skill_dbid) {
		Collection<CfgPerson> rtnpersons = null;
		CfgPersonQuery personquery = new CfgPersonQuery();
		personquery.setTenantDbid(tenant_dbid);
		personquery.setSkillDbid(skill_dbid);

		try {
			rtnpersons = service.retrieveMultipleObjects(CfgPerson.class, personquery);
		} catch (ConfigException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rtnpersons;
	}

	public Collection<CfgPerson> getAgentLoginDependency(final IConfService service, int tenant_dbid, int switch_dbid, int agentLogin_dbid) {
		Collection<CfgPerson> rtnpersons = null;
		CfgPersonQuery personquery = new CfgPersonQuery();
		personquery.setTenantDbid(tenant_dbid);
		personquery.setSwitchDbid(switch_dbid);
		personquery.setLoginDbid(agentLogin_dbid);

		try {
			rtnpersons = service.retrieveMultipleObjects(CfgPerson.class, personquery);
		} catch (ConfigException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rtnpersons;
	}

	public Collection<CfgPerson> getAgentGroupDependency(final IConfService service, int tenant_dbid, int agentGroup_dbid) {
		Collection<CfgPerson> rtnpersons = null;
		CfgPersonQuery personquery = new CfgPersonQuery();
		personquery.setTenantDbid(tenant_dbid);
		personquery.setGroupDbid(agentGroup_dbid);

		try {
			rtnpersons = service.retrieveMultipleObjects(CfgPerson.class, personquery);
		} catch (ConfigException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rtnpersons;
	}
	
	public CfgPerson getPersonInfo_single(final IConfService service, int tenant_dbid, int person_dbid) {
		CfgPerson rtnCfgPerson = null;
		CfgPersonQuery personquery = new CfgPersonQuery();
		personquery.setTenantDbid(tenant_dbid);
		personquery.setDbid(person_dbid);
		try {
			rtnCfgPerson = service.retrieveObject(CfgPerson.class, personquery);
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return rtnCfgPerson;
	}
	
	public Collection<CfgPerson> getPersonInfo(final IConfService service, int tenant_dbid, int person_dbid) {
		Collection<CfgPerson> rtnpersons = null;
		CfgPersonQuery personquery = new CfgPersonQuery();
		personquery.setTenantDbid(tenant_dbid);
		personquery.setDbid(person_dbid);
		try {
			rtnpersons = service.retrieveMultipleObjects(CfgPerson.class, personquery);
		} catch (ConfigException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rtnpersons;
	}
	
	public Collection<CfgPerson> getPersonInfo(final IConfService service, int tenant_dbid) {
		Collection<CfgPerson> rtnpersons = null;
		CfgPersonQuery personquery = new CfgPersonQuery();
		personquery.setIsAgent(2);
		personquery.setTenantDbid(tenant_dbid);
		try {
			rtnpersons = service.retrieveMultipleObjects(CfgPerson.class, personquery);
		} catch (ConfigException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rtnpersons;
	}
	
	public Collection<CfgPerson> getPersonInfo(final IConfService service, int tenant_dbid, int skill_dbid, int skill_level) {
		Collection<CfgPerson> rtnpersons = new HashSet<CfgPerson>();
		Collection<CfgPerson> tmppersons = null;
		CfgPersonQuery personquery = new CfgPersonQuery();
		personquery.setIsAgent(2);
		personquery.setTenantDbid(tenant_dbid);
		personquery.setSkillDbid(skill_dbid);
		try {
			tmppersons = service.retrieveMultipleObjects(CfgPerson.class, personquery);
			for (CfgPerson person : tmppersons){
				Collection<CfgSkillLevel> skill_levels = person.getAgentInfo().getSkillLevels();
				boolean chklevel = false;
				for (CfgSkillLevel skilllevel : skill_levels){
					if(skilllevel.getLevel() == skill_level) {
						chklevel = true;
					}
				}
				if(chklevel) {
					rtnpersons.add(person);
				}
			}
		} catch (ConfigException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rtnpersons;
	}
	
	/**
	 * Person을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param first_name
	 * @param last_name
	 * @param employee_id
	 * @param user_name
	 * @param folder_dbid
	 * @return
	 */
	public CfgPerson createPerson(
			final IConfService service, 
			int tenant_dbid, 
			int switch_dbid,
			String first_name,
			String last_name,
			String employee_id,
			String user_name,
			int folder_dbid) {
		CfgPerson person = null;
		try{
			// Read configuration objects:
			person = new CfgPerson(service);
			person.setTenantDBID(tenant_dbid);
			person.setFirstName(first_name);
			person.setLastName(last_name);
			person.setEmployeeID(employee_id);
			person.setUserName(user_name);
			person.setIsAgent(CfgFlag.CFGTrue);
			if(folder_dbid != 0) {
				person.setFolderId(folder_dbid);
			}

			person.save();		
			
			if(person != null) {
				if(setAgentLoginToPerson(service, person, tenant_dbid, switch_dbid, employee_id)) {
					person = getPersonInfo(service, tenant_dbid, "emp", employee_id);					
				}
			}			
		}catch (ConfigException e) {
			e.printStackTrace();
		}	
		return person;
	}

	/**
	 * Person을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param switch_dbid2
	 * @param PersonInfoVo
	 * @return
	 */
	public CfgPerson createPerson(
			final IConfService service, 
			int tenant_dbid, 
			int switch_dbid,
			int switch_dbid2,
			PersonInfoVo personinfovo) {
		CfgPerson person = null;
		try{
			// Read configuration objects:
			person = new CfgPerson(service);
			person.setTenantDBID(tenant_dbid);	//Tenant DBID
			person.setFirstName(personinfovo.getFirstName());	//First Name
			person.setLastName(personinfovo.getLastName());		//Last Name
			person.setEmployeeID(personinfovo.getEmployeeId());	// Employee ID
			person.setUserName(personinfovo.getUserName());		//UserName

			if(personinfovo.getIsAgent() == 2){		//IS Agent
				person.setIsAgent(CfgFlag.CFGTrue);
			}else{
				person.setIsAgent(CfgFlag.CFGFalse);
			}

			if(personinfovo.getState() == 1){		//State
				person.setState(CfgObjectState.CFGEnabled);
			}else{
				person.setState(CfgObjectState.CFGDisabled);
			}

			if(personinfovo.getFolderId() > 0) {	// Folder DBID
				person.setFolderId(personinfovo.getFolderId());
			}

			person.save();

			if(person != null) {
				JsonArray psarry = null;
				if(personinfovo.getAutoMakeAgentLogin().equals("true")){
					CfgAgentLogin login = null;
					if(switch_dbid > 0){
						login = cfgAgentLogin.getAgentLogin(service, tenant_dbid, switch_dbid, personinfovo.getAgentLoginId());
						if(login != null){
							if(cfgAgentLogin.checkAssignYn(service, tenant_dbid, switch_dbid, login.getLoginCode())){
								CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
								logininfo.setAgentLoginDBID(login.getDBID());
								logininfo.setAgentLogin(login);
								logininfo.setWrapupTime(0);
								person.getAgentInfo().getAgentLogins().add(logininfo);
							}
						}else{
							login = cfgAgentLogin.createAgentLogin(service, tenant_dbid, switch_dbid, personinfovo.getAgentLoginId());
							CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
							logininfo.setAgentLoginDBID(login.getDBID());
							logininfo.setAgentLogin(login);
							logininfo.setWrapupTime(0);
							person.getAgentInfo().getAgentLogins().add(logininfo);
						}
					}
					login = null;
					if(switch_dbid2 > 0){
						login = cfgAgentLogin.getAgentLogin(service, tenant_dbid, switch_dbid2, personinfovo.getAgentLoginId());
						if(login != null){
							if(cfgAgentLogin.checkAssignYn(service, tenant_dbid, switch_dbid2, login.getLoginCode())){
								CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
								logininfo.setAgentLoginDBID(login.getDBID());
								logininfo.setAgentLogin(login);
								logininfo.setWrapupTime(0);
								person.getAgentInfo().getAgentLogins().add(logininfo);
							}
						}else{
							login = cfgAgentLogin.createAgentLogin(service, tenant_dbid, switch_dbid2, personinfovo.getAgentLoginId());
							CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
							logininfo.setAgentLoginDBID(login.getDBID());
							logininfo.setAgentLogin(login);
							logininfo.setWrapupTime(0);
							person.getAgentInfo().getAgentLogins().add(logininfo);
						}
					}
				}else{
					if(personinfovo.getAgentLogins() != null){
						psarry = personinfovo.getAgentLogins().getAsJsonArray();
						//person.getAgentInfo().getAgentLogins().clear();
						for(int i=0; i<psarry.size(); i++){
							JsonObject ob =  psarry.get(i).getAsJsonObject();
							int dbid = ob.get("dbid").getAsInt();
							String logincode = ob.get("loginCode").getAsString();
							int warpupTime = ob.get("warpupTime").getAsInt();
							CfgAgentLogin login = cfgAgentLogin.getAgentLogin(service, tenant_dbid, switch_dbid, logincode, dbid);
							CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
							logininfo.setAgentLoginDBID(login.getDBID());
							logininfo.setAgentLogin(login);
							logininfo.setWrapupTime(warpupTime);
							person.getAgentInfo().getAgentLogins().add(logininfo);
						}
					}
				}

				if(personinfovo.getSkills() != null){
					Collection<CfgSkillLevel> skill = new HashSet<CfgSkillLevel>();
					psarry = personinfovo.getSkills().getAsJsonArray();
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						int level = ob.get("level").getAsInt();
						CfgSkillLevel li = new CfgSkillLevel(service, null);
						li.setSkillDBID(dbid);
						li.setLevel(level);
						skill.add(li);
					}
					person.getAgentInfo().setSkillLevels(skill);
				}

				if(personinfovo.getAnnex() != null){
					psarry = personinfovo.getAnnex().getAsJsonArray();
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
					person.setUserProperties(obj_kv);
				}

				person.save();
			}

			if(person != null && personinfovo.getAgentGroupName() != "" && personinfovo.getAgentGroupName() != null) {
				String[] temparry = personinfovo.getAgentGroupName().split("\\|");
				for(int i=0; i<temparry.length; i++){
					cfgAgentGroup.assignPersonToAgentGroup(service, tenant_dbid, temparry[i], personinfovo.getEmployeeId());
				}
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return person;
	}

		/**
	 * Person을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param PersonInfoVo
	 * @return
	 */
	public CfgPerson createPerson(
			final IConfService service, 
			int tenant_dbid, 
			int switch_dbid,
			PersonInfoVo personinfovo) {
		CfgPerson person = null;
		try{
			// Read configuration objects:
			person = new CfgPerson(service);
			person.setTenantDBID(tenant_dbid);	//Tenant DBID
			person.setFirstName(personinfovo.getFirstName());	//First Name
			person.setLastName(personinfovo.getLastName());		//Last Name
			person.setEmployeeID(personinfovo.getEmployeeId());	// Employee ID
			person.setUserName(personinfovo.getUserName());		//UserName

			if(personinfovo.getIsAgent() == 2){		//IS Agent
				person.setIsAgent(CfgFlag.CFGTrue);
			}else{
				person.setIsAgent(CfgFlag.CFGFalse);
			}

			if(personinfovo.getState() == 1){		//State
				person.setState(CfgObjectState.CFGEnabled);
			}else{
				person.setState(CfgObjectState.CFGDisabled);
			}

			if(personinfovo.getFolderId() > 0) {	// Folder DBID
				person.setFolderId(personinfovo.getFolderId());
			}

			person.save();

			if(person != null) {
				JsonArray psarry = null;
				if(personinfovo.getAutoMakeAgentLogin().equals("true")){
					CfgAgentLogin login = null;
					login = cfgAgentLogin.getAgentLogin(service, tenant_dbid, switch_dbid, personinfovo.getEmployeeId());
					if(login != null){
						if(cfgAgentLogin.checkAssignYn(service, tenant_dbid, switch_dbid, login.getLoginCode())){
							CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
							logininfo.setAgentLoginDBID(login.getDBID());
							logininfo.setAgentLogin(login);
							logininfo.setWrapupTime(0);
							person.getAgentInfo().getAgentLogins().add(logininfo);
						}
					}else{
						login = cfgAgentLogin.createAgentLogin(service, tenant_dbid, switch_dbid, personinfovo.getEmployeeId());
						CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
						logininfo.setAgentLoginDBID(login.getDBID());
						logininfo.setAgentLogin(login);
						logininfo.setWrapupTime(0);
						person.getAgentInfo().getAgentLogins().add(logininfo);
					}
				}else{
					if(personinfovo.getAgentLogins() != null){
						psarry = personinfovo.getAgentLogins().getAsJsonArray();
						//person.getAgentInfo().getAgentLogins().clear();
						for(int i=0; i<psarry.size(); i++){
							JsonObject ob =  psarry.get(i).getAsJsonObject();
							int dbid = ob.get("dbid").getAsInt();
							String logincode = ob.get("loginCode").getAsString();
							int warpupTime = ob.get("warpupTime").getAsInt();
							CfgAgentLogin login = cfgAgentLogin.getAgentLogin(service, tenant_dbid, switch_dbid, logincode, dbid);
							CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
							logininfo.setAgentLoginDBID(login.getDBID());
							logininfo.setAgentLogin(login);
							logininfo.setWrapupTime(warpupTime);
							person.getAgentInfo().getAgentLogins().add(logininfo);
						}
					}
				}

				if(personinfovo.getSkills() != null){
					Collection<CfgSkillLevel> skill = new HashSet<CfgSkillLevel>();
					psarry = personinfovo.getSkills().getAsJsonArray();
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						int level = ob.get("level").getAsInt();
						CfgSkillLevel li = new CfgSkillLevel(service, null);
						li.setSkillDBID(dbid);
						li.setLevel(level);
						skill.add(li);
					}
					person.getAgentInfo().setSkillLevels(skill);
				}

				if(personinfovo.getAnnex() != null){
					psarry = personinfovo.getAnnex().getAsJsonArray();
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
					person.setUserProperties(obj_kv);
				}

				person.save();
			}

			if(person != null && personinfovo.getAgentGroupName() != "" && personinfovo.getAgentGroupName() != null) {
				CfgAgentGroup agentgroup = null;
				String[] temparry = personinfovo.getAgentGroupName().split("\\|");
				for(int i=0; i<temparry.length; i++){
					cfgAgentGroup.assignPersonToAgentGroup(service, tenant_dbid, temparry[i], personinfovo.getEmployeeId());
				}
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return person;
	}

	/**
	 * Person을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param person
	 * @param PersonInfoVo
	 * @return
	 */
	public CfgPerson modifyPerson(
			final IConfService service, 
			int tenant_dbid, 
			int switch_dbid,
			CfgPerson person,
			PersonInfoVo personinfovo){
		// Read configuration objects:
		try {
			person.setFirstName(personinfovo.getFirstName());
			person.setLastName(personinfovo.getLastName());
			person.setUserName(personinfovo.getUserName());
			person.setEmployeeID(personinfovo.getEmployeeId());

			if(personinfovo.getState() == 1){		//State
				person.setState(CfgObjectState.CFGEnabled);
			}else{
				person.setState(CfgObjectState.CFGDisabled);
			}
			
			person.save();

			if(person != null) {
				JsonArray psarry = null;
				if(personinfovo.getAgentLogins() != null){
					psarry = personinfovo.getAgentLogins().getAsJsonArray();
					person.getAgentInfo().getAgentLogins().clear();
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						String logincode = ob.get("loginCode").getAsString();
						int warpupTime = ob.get("warpupTime").getAsInt();
						CfgAgentLogin login = cfgAgentLogin.getAgentLogin(service, tenant_dbid, switch_dbid, logincode, dbid);
						CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
						logininfo.setAgentLoginDBID(login.getDBID());
						logininfo.setAgentLogin(login);
						logininfo.setWrapupTime(warpupTime);
						person.getAgentInfo().getAgentLogins().add(logininfo);
					}
				}

				if(personinfovo.getSkills() != null){
					Collection<CfgSkillLevel> skill = new HashSet<CfgSkillLevel>();
					psarry = personinfovo.getSkills().getAsJsonArray();
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						int level = ob.get("level").getAsInt();
						CfgSkillLevel li = new CfgSkillLevel(service, null);
						li.setSkillDBID(dbid);
						li.setLevel(level);
						skill.add(li);
					}
					person.getAgentInfo().setSkillLevels(skill);
				}

				if(personinfovo.getAnnex() != null){
					psarry = personinfovo.getAnnex().getAsJsonArray();
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
					person.setUserProperties(obj_kv);
				}

				person.save();
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return person;
	}


	/**
	 * Person을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param switch_dbid2
	 * @param person
	 * @param PersonInfoVo
	 * @return
	 */
	public CfgPerson modifyPerson(
			final IConfService service, 
			int tenant_dbid, 
			int switch_dbid,
			int switch_dbid2,
			CfgPerson person,
			PersonInfoVo personinfovo){
		// Read configuration objects:
		try {
			person.setFirstName(personinfovo.getFirstName());
			person.setLastName(personinfovo.getLastName());
			person.setUserName(personinfovo.getUserName());
			person.setEmployeeID(personinfovo.getEmployeeId());

			if(personinfovo.getState() == 1){		//State
				person.setState(CfgObjectState.CFGEnabled);
			}else{
				person.setState(CfgObjectState.CFGDisabled);
			}
			
			person.save();

			if(person != null) {
				JsonArray psarry = null;
				if(personinfovo.getAgentLogins() != null){
					psarry = personinfovo.getAgentLogins().getAsJsonArray();
					person.getAgentInfo().getAgentLogins().clear();
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						String logincode = ob.get("loginCode").getAsString();
						int warpupTime = ob.get("warpupTime").getAsInt();
						CfgAgentLogin login = cfgAgentLogin.getAgentLogin(service, tenant_dbid, switch_dbid, logincode, dbid);
						CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
						logininfo.setAgentLoginDBID(login.getDBID());
						logininfo.setAgentLogin(login);
						logininfo.setWrapupTime(warpupTime);
						person.getAgentInfo().getAgentLogins().add(logininfo);
					}
				}

				if(personinfovo.getSkills() != null){
					Collection<CfgSkillLevel> skill = new HashSet<CfgSkillLevel>();
					psarry = personinfovo.getSkills().getAsJsonArray();
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						int level = ob.get("level").getAsInt();
						CfgSkillLevel li = new CfgSkillLevel(service, null);
						li.setSkillDBID(dbid);
						li.setLevel(level);
						skill.add(li);
					}
					person.getAgentInfo().setSkillLevels(skill);
				}

				if(personinfovo.getAnnex() != null){
					psarry = personinfovo.getAnnex().getAsJsonArray();
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
					person.setUserProperties(obj_kv);
				}

				person.save();
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return person;
	}






		/**
	 * Person을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param switch_dbid2
	 * @param PersonInfoVo
	 * @return
	 */
	public HundaiModelVo createPerson_HD(
			final IConfService service, 
			int tenant_dbid, 
			int switch_dbid,
			int switch_dbid2,
			PersonInfoVo personinfovo) {
		CfgPerson person = null;
		HundaiModelVo personmodelvo = new HundaiModelVo();
		try{
			// Read configuration objects:
			person = new CfgPerson(service);
			person.setTenantDBID(tenant_dbid);	//Tenant DBID
			person.setFirstName(personinfovo.getFirstName());	//First Name
			person.setLastName(personinfovo.getLastName());		//Last Name
			person.setEmployeeID(personinfovo.getEmployeeId());	// Employee ID
			person.setUserName(personinfovo.getUserName());		//UserName

			if(personinfovo.getIsAgent() == 2){		//IS Agent
				person.setIsAgent(CfgFlag.CFGTrue);
			}else{
				person.setIsAgent(CfgFlag.CFGFalse);
			}

			if(personinfovo.getState() == 1){		//State
				person.setState(CfgObjectState.CFGEnabled);
			}else{
				person.setState(CfgObjectState.CFGDisabled);
			}

			if(personinfovo.getFolderId() > 0) {	// Folder DBID
				person.setFolderId(personinfovo.getFolderId());
			}

			person.save();
			personmodelvo.setPerson(person);
			//personmodelvo.setRtnMessage1("Person 생성 성공");
			personmodelvo.setRtnMessage1("");
			personmodelvo.setRtnMessage2("T");
			personmodelvo.setRtnMessage3(person.getEmployeeID());
			personmodelvo.setRtnMessage4(person.getDBID() + "");
			if(person != null) {
				JsonArray psarry = null;
				if(personinfovo.getAutoMakeAgentLogin().equals("true")){
					CfgAgentLogin login = null;
					if(switch_dbid > 0){
						login = cfgAgentLogin.getAgentLogin(service, tenant_dbid, switch_dbid, personinfovo.getAgentLoginId());
						if(login != null){
							if(cfgAgentLogin.checkAssignYn(service, tenant_dbid, switch_dbid, login.getLoginCode())){
								CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
								logininfo.setAgentLoginDBID(login.getDBID());
								logininfo.setAgentLogin(login);
								logininfo.setWrapupTime(0);
								person.getAgentInfo().getAgentLogins().add(logininfo);
							}else{
								personmodelvo.setRtnMessage1("switch_dbid : " + switch_dbid + "의 AGENT_LOGINS " + personinfovo.getAgentLoginId() +"가 이미 할당되어 있습니다.");
							}
						}else{
							login = cfgAgentLogin.createAgentLogin(service, tenant_dbid, switch_dbid, personinfovo.getAgentLoginId());
							CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
							logininfo.setAgentLoginDBID(login.getDBID());
							logininfo.setAgentLogin(login);
							logininfo.setWrapupTime(0);
							person.getAgentInfo().getAgentLogins().add(logininfo);
						}
					}
					login = null;
					if(switch_dbid2 > 0){
						login = cfgAgentLogin.getAgentLogin(service, tenant_dbid, switch_dbid2, personinfovo.getAgentLoginId());
						if(login != null){
							if(cfgAgentLogin.checkAssignYn(service, tenant_dbid, switch_dbid2, login.getLoginCode())){
								CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
								logininfo.setAgentLoginDBID(login.getDBID());
								logininfo.setAgentLogin(login);
								logininfo.setWrapupTime(0);
								person.getAgentInfo().getAgentLogins().add(logininfo);
							}else{
								personmodelvo.setRtnMessage1("switch_dbid : " + switch_dbid + "의 AGENT_LOGINS " + personinfovo.getAgentLoginId() +"가 이미 할당되어 있습니다.");
							}
						}else{
							login = cfgAgentLogin.createAgentLogin(service, tenant_dbid, switch_dbid2, personinfovo.getAgentLoginId());
							CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
							logininfo.setAgentLoginDBID(login.getDBID());
							logininfo.setAgentLogin(login);
							logininfo.setWrapupTime(0);
							person.getAgentInfo().getAgentLogins().add(logininfo);
						}
					}
				}else{
					if(personinfovo.getAgentLogins() != null){
						psarry = personinfovo.getAgentLogins().getAsJsonArray();
						//person.getAgentInfo().getAgentLogins().clear();
						for(int i=0; i<psarry.size(); i++){
							JsonObject ob =  psarry.get(i).getAsJsonObject();
							int dbid = ob.get("dbid").getAsInt();
							String logincode = ob.get("loginCode").getAsString();
							int warpupTime = ob.get("warpupTime").getAsInt();
							CfgAgentLogin login = cfgAgentLogin.getAgentLogin(service, tenant_dbid, switch_dbid, logincode, dbid);
							CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
							logininfo.setAgentLoginDBID(login.getDBID());
							logininfo.setAgentLogin(login);
							logininfo.setWrapupTime(warpupTime);
							person.getAgentInfo().getAgentLogins().add(logininfo);
						}
					}
				}

				if(personinfovo.getSkills() != null){
					Collection<CfgSkillLevel> skill = new HashSet<CfgSkillLevel>();
					psarry = personinfovo.getSkills().getAsJsonArray();
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						int level = ob.get("level").getAsInt();
						CfgSkillLevel li = new CfgSkillLevel(service, null);
						li.setSkillDBID(dbid);
						li.setLevel(level);
						skill.add(li);
					}
					person.getAgentInfo().setSkillLevels(skill);
				}

				if(personinfovo.getAnnex() != null){
					psarry = personinfovo.getAnnex().getAsJsonArray();
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
					person.setUserProperties(obj_kv);
				}

				person.save();
				personmodelvo.setPerson(person);
				personmodelvo.setRtnMessage2("T");
				personmodelvo.setRtnMessage3(person.getEmployeeID());
				personmodelvo.setRtnMessage4(person.getDBID() + "");
			}

			if(person != null && personinfovo.getAgentGroupName() != "" && personinfovo.getAgentGroupName() != null) {
				String[] temparry = personinfovo.getAgentGroupName().split("\\|");
				for(int i=0; i<temparry.length; i++){
					cfgAgentGroup.assignPersonToAgentGroup(service, tenant_dbid, temparry[i], personinfovo.getEmployeeId());
				}
			}

		}catch (ConfigException e) {
			e.printStackTrace();
			personmodelvo.setRtnMessage1("Person 생성 실패(ConfigException)");
			personmodelvo.setRtnMessage2("F");
		}catch (Exception e) {
			e.printStackTrace();
			personmodelvo.setRtnMessage1("Person 생성 실패(Exception)");
			personmodelvo.setRtnMessage2("F");
		}	
		return personmodelvo;
	}

	/**
	 * Person을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param switch_dbid2
	 * @param person
	 * @param PersonInfoVo
	 * @return
	 */
	public HundaiModelVo modifyPerson_HD(
			final IConfService service, 
			int tenant_dbid, 
			int switch_dbid,
			int switch_dbid2,
			CfgPerson person,
			PersonInfoVo personinfovo){
		// Read configuration objects:
		HundaiModelVo personmodelvo = new HundaiModelVo();
		try {
			person.setFirstName(personinfovo.getFirstName());
			person.setLastName(personinfovo.getLastName());
			person.setUserName(personinfovo.getUserName());
			person.setEmployeeID(personinfovo.getEmployeeId());

			if(personinfovo.getState() == 1){		//State
				person.setState(CfgObjectState.CFGEnabled);
			}else{
				person.setState(CfgObjectState.CFGDisabled);
			}
			
			person.save();
			personmodelvo.setPerson(person);
			//personmodelvo.setRtnMessage1("Person 수정 성공");
			personmodelvo.setRtnMessage1("");
			personmodelvo.setRtnMessage2("T");
			personmodelvo.setRtnMessage3(person.getEmployeeID());
		}catch (ConfigException e) {
			e.printStackTrace();
			personmodelvo.setRtnMessage1("Person 수정 실패(ConfigException)");
			personmodelvo.setRtnMessage2("F");
		}catch (Exception e) {
			e.printStackTrace();
			personmodelvo.setRtnMessage1("Person 수정 실패(Exception)");
			personmodelvo.setRtnMessage2("F");
		}	
		return personmodelvo;
	}

	
	/**
	 * Person을 삭제한다.
	 * @param service
	 * @param person
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public HundaiModelVo deletePerson_HD(
			final IConfService service,
			CfgPerson person
			)
					throws ConfigException, InterruptedException {
		HundaiModelVo personmodelvo = new HundaiModelVo();
		try{
			personmodelvo.setRtnMessage3(person.getEmployeeID());
			person.delete();
			//personmodelvo.setRtnMessage1("Person 삭제 성공");
			personmodelvo.setRtnMessage1("");
			personmodelvo.setRtnMessage2("T");
		}catch (ConfigException e) {
			e.printStackTrace();
			personmodelvo.setRtnMessage1("Person 삭제 실패(ConfigException)");
			personmodelvo.setRtnMessage2("F");
		}catch (Exception e) {
			e.printStackTrace();
			personmodelvo.setRtnMessage1("Person 삭제 실패(Exception)");
			personmodelvo.setRtnMessage2("F");
		}	
		return personmodelvo;
	}	
	
	/**
	 * Person을 수정한다.
	 * @param service
	 * @param person
	 * @param first_name
	 * @param last_name
	 * @param employee_id
	 * @param user_name
	 * @return
	 */
	public CfgPerson modifyPerson(
			final IConfService service, 
			CfgPerson person,
			String first_name,
			String last_name,
			String employee_id,
			String user_name){
		// Read configuration objects:
		try {
			person.setFirstName(first_name);
			person.setLastName(last_name);
			person.setUserName(user_name);
			
			person.save();
		}catch (ConfigException e) {
			e.printStackTrace();
		}
		return person;
	}

	/**
	 * Person을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param person_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deletePerson(
			final IConfService service,
			int tenant_dbid,
			int person_dbid
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		CfgPerson person = null;
		boolean rtnbool = false;
		try{
			CfgPersonQuery personquery = new CfgPersonQuery();
			personquery.setTenantDbid(tenant_dbid);
			personquery.setDbid(person_dbid);

			person = service.retrieveObject(CfgPerson.class,personquery);
			person.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	
	
	/**
	 * Person을 삭제한다.
	 * @param service
	 * @param person
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deletePerson(
			final IConfService service,
			CfgPerson person
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			person.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	
	
	/**
	 * Person의 상태를 변경한다. 
	 * @param service
	 * @param person 	상태 변경할 Person 객체
	 * @param flag 		(true = Enabled, false = Disabled)
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgPerson setPersonState(
			final IConfService service,
			CfgPerson person,
			boolean flag
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		if(flag){
			person.setState(CfgObjectState.CFGEnabled);
		}else{
			person.setState(CfgObjectState.CFGDisabled);
		}
		person.save();
		return person;
	}
	
	/**
	 * Person에 AgentLogin을 Assign한다.
	 * @param service
	 * @param person
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param employee_id
	 * @return
	 */
	public boolean setAgentLoginToPerson(
			final IConfService service, 
			CfgPerson person, 
			int tenant_dbid, 
			int switch_dbid,
			String employee_id) {
		ConfigAgentLogin configagentlogin = new ConfigAgentLogin();
		boolean assignyn = false;
		boolean rtnResult = false;

		try {
			CfgAgentLogin agentlogin = configagentlogin.getAgentLogin(service, tenant_dbid, switch_dbid, employee_id);
			
			if(agentlogin == null) {
				agentlogin = configagentlogin.createAgentLogin(service, tenant_dbid, switch_dbid, employee_id);
				assignyn = true;
			}else {
				assignyn = configagentlogin.checkAssignYn(service, tenant_dbid, switch_dbid, employee_id);
			}
			System.out.println(assignyn);

			//AgentLoginId가 Assign된 상태가 아니면(assignyn true이면 Assign가능)
			if(assignyn) {
				CfgAgentLoginInfo logininfo = new CfgAgentLoginInfo(service, null);
				logininfo.setAgentLoginDBID(agentlogin.getDBID());
				logininfo.setAgentLogin(agentlogin);
				logininfo.setWrapupTime(0);
				person.getAgentInfo().getAgentLogins().add(logininfo);
				person.save();			
				rtnResult = true;
			}			
		}catch (ConfigException e) {
			rtnResult = false;
			e.printStackTrace();
		}catch ( InterruptedException e) {
			rtnResult = false;
			e.printStackTrace();
		}
		return rtnResult;
	}
	
	/**
	 * Person에 Assign된 AgentLoginID를 삭제한다.
	 * @param service
	 * @param person
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param employee_id
	 * @return
	 */
	public boolean removeAgentLoginToPerson(
			final IConfService service, 
			CfgPerson person, 
			int tenant_dbid, 
			int switch_dbid,
			String employee_id) {
		boolean deleteYn = false;

		try {
			Collection<CfgAgentLoginInfo> agentlogininfo = person.getAgentInfo().getAgentLogins();		//기존 AgentLogin정보
			Collection<CfgAgentLoginInfo> newinfoAgentLoginInfos = new HashSet<CfgAgentLoginInfo>();	//새로운 AgentLogin정보
			 
			Iterator<CfgAgentLoginInfo> iter = agentlogininfo.iterator();
			while (iter.hasNext()) {
				CfgAgentLoginInfo cfgagentlogininfo = iter.next();
				if(!cfgagentlogininfo.getAgentLogin().getLoginCode().equals(employee_id)) {
					newinfoAgentLoginInfos.add(cfgagentlogininfo);
				}
			}

			person.getAgentInfo().getAgentLogins().clear();
			person.getAgentInfo().setAgentLogins(newinfoAgentLoginInfos);
			person.save();
			deleteYn = true;
		}catch (ConfigException e) {
			deleteYn = false;
			e.printStackTrace();
		}
		return deleteYn;
	}
	
	/**
	 * Person에Skill를 Assign한다.
	 * @param service
	 * @param personobj
	 * @param skill_dbids
	 * @param skill_levels
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean setSkillsToPerson(
			final IConfService service,
			CfgPerson personobj,
			String[] skill_dbids,
			String[] skill_levels)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			Collection<CfgSkillLevel> skill = new HashSet<CfgSkillLevel>();
			if(skill_dbids != null && skill_levels != null) {
				for(int i=0; i<skill_dbids.length; i++) {
					CfgSkillLevel li = new CfgSkillLevel(service, null);
					li.setSkillDBID(Integer.parseInt(skill_dbids[i]));
					li.setLevel(Integer.parseInt(skill_levels[i]));
					skill.add(li);
				}
				personobj.getAgentInfo().setSkillLevels(skill);
				personobj.save();
				rtnbool = true;				
			}
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;				
	}

	/**
	 * Person의 Skill을 삭제한다
	 * @param service
	 * @param person
	 * @param skill_dbids
	 * @param skill_levels
	 * @return
	 */
	public boolean removeSkillsToPerson(
			final IConfService service, 
			CfgPerson person, 
			String[] skill_dbids,
			String[] skill_levels) {
		boolean deleteYn = false;

		try {
			if(person.getAgentInfo().getSkillLevels() != null){
				Collection<CfgSkillLevel> cfgSkillLevel = null;
				Collection<CfgSkillLevel> tmpSkillLevel = new HashSet<CfgSkillLevel>();
				cfgSkillLevel = person.getAgentInfo().getSkillLevels();
				tmpSkillLevel.addAll(cfgSkillLevel);
				if(cfgSkillLevel.size() > 0){
					for(CfgSkillLevel lvl : cfgSkillLevel){
						for(int i=0; i<skill_dbids.length; i++){
							if(lvl.getSkillDBID().equals(Integer.parseInt(skill_dbids[i]))){
								if(lvl.getLevel().equals(Integer.parseInt(skill_levels[i]))){
									tmpSkillLevel.remove(lvl);
								}
							}
						}
					}
					person.getAgentInfo().setSkillLevels(tmpSkillLevel);
					person.save();
					deleteYn = true;
				}
			}	
		}catch (ConfigException e) {
			deleteYn = false;
			e.printStackTrace();
		}
		return deleteYn;
	}
	
	/**
	 * Person의 모든 Skill을 삭제한다.
	 * @param service
	 * @param person
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean removeAllSkillsToPerson(
			final IConfService service, 
			CfgPerson person) {
		boolean rtnbool = false;
		try{
			person.getAgentInfo().getSkillLevels().clear();
			person.save();
			rtnbool = true;
		}catch (ConfigException e) {
			rtnbool = false;
			e.printStackTrace();
		}
		return rtnbool;				
	}	
}
