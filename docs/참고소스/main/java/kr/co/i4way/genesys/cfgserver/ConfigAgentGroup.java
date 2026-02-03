package kr.co.i4way.genesys.cfgserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.i4way.genesys.model.AgentGroupInfoVo;
import kr.co.i4way.genesys.model.AssignInfoVo;
import kr.co.i4way.genesys.model.HundaiModelVo;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.queries.CfgAgentGroupQuery;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigAgentGroup {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	ConfigPerson ps = null;
	ConfigCommon configcommon = null;
	public ConfigAgentGroup(){
		configcommon = new ConfigCommon();
	}
	
	/**
	 * AgentGroup을 조회한다. 
	 * @param service
	 * @param tenant_dbid
	 * @param group_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup getAgentGroupInfo(
			final IConfService service,
			int tenant_dbid,
			String group_name
			) {
		CfgAgentGroup agentgroup = null;
		try {
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setName(group_name);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return agentgroup;
	}
	
	/**
	 * AgentGroup을 조회한다. 
	 * @param service
	 * @param tenant_dbid
	 * @param group_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup getAgentGroupInfo(
			final IConfService service,
			int tenant_dbid,
			int group_dbid
			) {
		CfgAgentGroup agentgroup = null;
		try {
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setDbid(group_dbid);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		
		return agentgroup;
	}

	/**
	 * AgentGroup을 조회한다.
	 * @param iTenantDBID
	 * @param service
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public Collection<CfgAgentGroup> getAgentGroupInfo(
			final IConfService service,
			int tenant_dbid,
			String virtual_gubun,
			String tmp
			) { 
		Collection<CfgAgentGroup> agentgroups = null;
		Collection<CfgAgentGroup> rtnag = null;
		try {
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroups = service.retrieveMultipleObjects(CfgAgentGroup.class,
					agentgroupquery);
					rtnag = new HashSet<CfgAgentGroup>();	//새로운 CfgAgentGroup정보
			for(CfgAgentGroup ag : agentgroups){ 
				if(!configcommon.checkScript(ag.getGroupInfo().getUserProperties())){
					rtnag.add(ag);
				}
			}

		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return rtnag;
	}

	public Collection<CfgAgentGroup> getPersonDependency(final IConfService service, int tenant_dbid, int person_dbid) {
		Collection<CfgAgentGroup> agentgroups = null;
		CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
		agentgroupquery.setTenantDbid(tenant_dbid);
		agentgroupquery.setPersonDbid(person_dbid);

		try {
			agentgroups = service.retrieveMultipleObjects(CfgAgentGroup.class, agentgroupquery);
		} catch (ConfigException e) {
			e.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		return agentgroups;
	}
	
	/**
	 * AgentGroup에 Assign 조회한다.
	 * @param service
	 * @param tenant_dbid
	 * @param person_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public Collection<CfgAgentGroup> getAssignAgentGroup(
			final IConfService service,
			int tenant_dbid,
			int person_dbid
			) {
		Collection<CfgAgentGroup> agentgroups = null;
		try {
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setPersonDbid(person_dbid);

			agentgroups = service.retrieveMultipleObjects(CfgAgentGroup.class,
					agentgroupquery);

		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return agentgroups;
	}
	
	/**
	 * AgentGroup을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param group_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup createAgentGroup(
			final IConfService service,
			int tenant_dbid,
			String group_name) {
		CfgGroup group = new CfgGroup(service, null);
		CfgAgentGroup cfggroup = null;
		KeyValueCollection kvc = new KeyValueCollection();		
		
		try {
			group.setTenantDBID(tenant_dbid);
			group.setName(group_name);
			kvc.addString("app", "true");
			group.getUserProperties().addList("app", kvc);
			//group.setUserProperties(kvc);

			cfggroup = new CfgAgentGroup(service);
			cfggroup.setGroupInfo(group);
			
			//cfggroup.getGroupInfo().setUserProperties(kvc);
			//KeyValuePair kv = new KeyValuePair("app", "true");
			//cfggroup.setProperty("app", "true");
			
			cfggroup.save();
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return cfggroup;
	}
	
	/**
	 * AgentGroup을 수정한다
	 * @param service
	 * @param tenant_dbid
	 * @param group_dbid
	 * @param group_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup modifyAgentGroup(
			final IConfService service,
			int tenant_dbid,
			int group_dbid,
			String group_name) {
		// Read configuration objects:
		CfgAgentGroup agentgroup = null;
		try {
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setDbid(group_dbid);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,agentgroupquery);

			if(agentgroup != null){
				agentgroup.getGroupInfo().setName(group_name);
				agentgroup.save();
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		
		return agentgroup;
	}
	

	/**
	 * AgentGroup을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param group_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public Boolean deleteAgentGroup(
			final IConfService service,
			int tenant_dbid,
			int group_dbid) {
		Boolean rtnval = false;
		
		try {
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setDbid(group_dbid);
			CfgAgentGroup agentgroup = service.retrieveObject(CfgAgentGroup.class,agentgroupquery);

			if(agentgroup != null){
				agentgroup.delete();
				rtnval = true;
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnval;
	}

	/**
	 * AgentGroup을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param AgentGroupInfoVo
	 * @return
	 */
	public CfgAgentGroup createAgentGroupInfo(
			final IConfService service, 
			int tenant_dbid, 
			AgentGroupInfoVo agentgroupinfovo) {
			CfgAgentGroup agentgroup = null;
			CfgGroup group = null;
		try{
			// Read configuration objects:
			agentgroup = new CfgAgentGroup(service);
			group = new CfgGroup(service, null);
			group.setTenantDBID(tenant_dbid);
			group.setName(agentgroupinfovo.getName());
			agentgroup.setGroupInfo(group);
			if(agentgroupinfovo.getFolderId() > 0) {	// Folder DBID
				agentgroup.setFolderId(agentgroupinfovo.getFolderId());
			}

			if(agentgroupinfovo.getState() == 1) {	// State
				agentgroup.getGroupInfo().setState(CfgObjectState.CFGEnabled);
			}else{
				agentgroup.getGroupInfo().setState(CfgObjectState.CFGDisabled);
			}

			agentgroup.save();

			if(agentgroup != null) {
				JsonArray psarry = null;
				if(agentgroupinfovo.getAnnex() != null){
					psarry = agentgroupinfovo.getAnnex().getAsJsonArray();
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
					agentgroup.getGroupInfo().setUserProperties(obj_kv);
				}

				agentgroup.save();
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return agentgroup;
	}


		/**
	 * AgentGroup을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param agentgroup
	 * @param AgentGroupInfoVo
	 * @return
	 */
	public CfgAgentGroup modifyAgentGroupInfo(
			final IConfService service, 
			int tenant_dbid, 
			CfgAgentGroup agentgroup,
			AgentGroupInfoVo agentgroupinfovo) {
		try{
			agentgroup.getGroupInfo().setName(agentgroupinfovo.getName());
			if(agentgroupinfovo.getState() == 1) {	// State
				agentgroup.getGroupInfo().setState(CfgObjectState.CFGEnabled);
			}else{
				agentgroup.getGroupInfo().setState(CfgObjectState.CFGDisabled);
			}
			agentgroup.save();

			if(agentgroup != null) {
				JsonArray psarry = null;
				if(agentgroupinfovo.getAnnex() != null){
					psarry = agentgroupinfovo.getAnnex().getAsJsonArray();
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
					agentgroup.getGroupInfo().setUserProperties(obj_kv);
				}

				agentgroup.save();
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return agentgroup;
	}

	/**
	 * AgentGroup을 삭제한다.
	 * @param service
	 * @param agentgroup
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteAgentGroupInfo(
			final IConfService service,
			CfgAgentGroup agentgroup
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			agentgroup.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	













	
	/**
	 * AgentGroup을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param AgentGroupInfoVo
	 * @return
	 */
	public HundaiModelVo createAgentGroupInfo_HD(
			final IConfService service, 
			int tenant_dbid, 
			AgentGroupInfoVo agentgroupinfovo) {
			CfgAgentGroup agentgroup = null;
			HundaiModelVo agentgroupmodelvo = new HundaiModelVo();
			CfgGroup group = null;
		try{
			// Read configuration objects:
			agentgroup = new CfgAgentGroup(service);
			group = new CfgGroup(service, null);
			group.setTenantDBID(tenant_dbid);
			group.setName(agentgroupinfovo.getName());
			agentgroup.setGroupInfo(group);
			if(agentgroupinfovo.getFolderId() > 0) {	// Folder DBID
				agentgroup.setFolderId(agentgroupinfovo.getFolderId());
			}
			agentgroup.getGroupInfo().setState(CfgObjectState.CFGEnabled);

			agentgroup.save();

			if(agentgroup != null) {
				agentgroupmodelvo.setAgentgroup(agentgroup);
				//agentgroupmodelvo.setRtnMessage1("AgentGroup 생성 성공");
				agentgroupmodelvo.setRtnMessage1("");
				agentgroupmodelvo.setRtnMessage2("T");
				agentgroupmodelvo.setRtnMessage3(agentgroup.getGroupInfo().getName());
				agentgroupmodelvo.setRtnMessage4(agentgroup.getDBID() + "");
			}

		}catch (ConfigException e) {
			e.printStackTrace();
			agentgroupmodelvo.setRtnMessage1("AgentGroup 생성 실패(ConfigException)");
			agentgroupmodelvo.setRtnMessage2("F");
		}catch (Exception e) {
			e.printStackTrace();
			agentgroupmodelvo.setRtnMessage1("AgentGroup 생성 실패(Exception)");
			agentgroupmodelvo.setRtnMessage2("F");
		}	
		return agentgroupmodelvo;
	}


		/**
	 * AgentGroup을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param agentgroup
	 * @param AgentGroupInfoVo
	 * @return
	 */
	public HundaiModelVo modifyAgentGroupInfo_HD(
			final IConfService service, 
			int tenant_dbid, 
			CfgAgentGroup agentgroup,
			AgentGroupInfoVo agentgroupinfovo) {

			HundaiModelVo agentgroupmodelvo = new HundaiModelVo();
		try{
			agentgroup.getGroupInfo().setName(agentgroupinfovo.getName());
			agentgroup.save();
			if(agentgroup != null) {
				agentgroupmodelvo.setAgentgroup(agentgroup);
				//agentgroupmodelvo.setRtnMessage1("AgentGroup 수정 성공");
				agentgroupmodelvo.setRtnMessage1("");
				agentgroupmodelvo.setRtnMessage2("T");
				agentgroupmodelvo.setRtnMessage3(agentgroup.getGroupInfo().getName());
				agentgroupmodelvo.setRtnMessage4(agentgroup.getDBID() + "");
			}
		}catch (ConfigException e) {
			e.printStackTrace();
			agentgroupmodelvo.setRtnMessage1("AgentGroup 수정 실패(ConfigException)");
			agentgroupmodelvo.setRtnMessage2("F");
		}catch (Exception e) {
			e.printStackTrace();
			agentgroupmodelvo.setRtnMessage1("AgentGroup 수정 실패(Exception)");
			agentgroupmodelvo.setRtnMessage2("F");
		}	
		return agentgroupmodelvo;
	}

	/**
	 * AgentGroup을 삭제한다.
	 * @param service
	 * @param agentgroup
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public HundaiModelVo deleteAgentGroupInfo_HD(
			final IConfService service,
			CfgAgentGroup agentgroup
			)
					throws ConfigException, InterruptedException {
			HundaiModelVo agentgroupmodelvo = new HundaiModelVo();
		try{
			agentgroup.delete();		
			//agentgroupmodelvo.setRtnMessage1("AgentGroup 삭제 성공");
			agentgroupmodelvo.setRtnMessage1("");
			agentgroupmodelvo.setRtnMessage2("T");
		}catch (ConfigException e) {
			e.printStackTrace();
			agentgroupmodelvo.setRtnMessage1("AgentGroup 삭제 실패(ConfigException)");
			agentgroupmodelvo.setRtnMessage2("F");
		}catch (Exception e) {
			e.printStackTrace();
			agentgroupmodelvo.setRtnMessage1("AgentGroup 삭제 실패(Exception)");
			agentgroupmodelvo.setRtnMessage2("F");
		}	
		return agentgroupmodelvo;
	}	











	/**
	 * AgentGroup의 상태를 변경한다. 
	 * @param service
	 * @param agentgroup 	상태 변경할 AgentGroup 객체
	 * @param flag 		(true = Enabled, false = Disabled)
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup setAgentGroupState(
			final IConfService service,
			CfgAgentGroup agentgroup,
			boolean flag
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		if(flag){
			agentgroup.getGroupInfo().setState(CfgObjectState.CFGEnabled);
		}else{
			agentgroup.getGroupInfo().setState(CfgObjectState.CFGDisabled);
		}
		agentgroup.save();
		return agentgroup;
	}
	
	/**
	 * AgentGroup에 상담사를 Assign한다.
	 * @param service
	 * @param tenant_dbid
	 * @param group_name
	 * @param employee_ids
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup assignPersonToAgentGroup(
			final IConfService service,
			int tenant_dbid,
			String group_name,
			String employee_ids
			) {
		CfgAgentGroup agentgroup = null;
		Collection<CfgPerson> rtn_persons = null;
		CfgPerson rtn_person = null;
		try{
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setName(group_name);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
			String[] emps = employee_ids.split(",");
			if(emps != null && emps.length > 0) {
				ps = new ConfigPerson();

				if(agentgroup != null){
					if(agentgroup.getAgentDBIDs() != null){
						for(int i=0; i<emps.length; i++){
							rtn_person = null;
							rtn_person = ps.getPersonInfo(service, tenant_dbid, "emp", emps[i]);
							agentgroup.getAgentDBIDs().add(rtn_person.getDBID());
						}
					}else{
						rtn_persons = new HashSet<CfgPerson>();

						for(int i=0; i<emps.length; i++){
							rtn_person = null;
							rtn_person = ps.getPersonInfo(service, tenant_dbid, "emp", emps[i]);
							if(rtn_person != null) {
								rtn_persons.add(rtn_person);
							}
						}
						agentgroup.setAgents(rtn_persons);
					}
				}							
			}
			agentgroup.save();

		}catch(ConfigException ex){
			logger.error("ConfigException", ex);
		}
		return agentgroup;
	}
	
	/**
	 * AgentGroup에 상담사를 Assign한다.
	 * @param service
	 * @param tenant_dbid
	 * @param group_name
	 * @param employee_ids
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public AssignInfoVo assignPersonToAgentGroup_HD(
			final IConfService service,
			int tenant_dbid,
			String group_name,
			JsonElement employee_ids
			) {
		CfgAgentGroup agentgroup = null;
		Collection<CfgPerson> rtn_persons = null;
		CfgPerson rtn_person = null;
		AssignInfoVo assigninfovo = new AssignInfoVo();
		List<String> fail_emp_id = new ArrayList<String>();
		try{
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setName(group_name);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
			JsonArray emps = employee_ids.getAsJsonArray();
			if(emps != null && emps.size() > 0) {
				ps = new ConfigPerson();

				if(agentgroup != null){
					if(agentgroup.getAgentDBIDs() != null){
						for(int i=0; i<emps.size(); i++){
							rtn_person = null;
							rtn_person = ps.getPersonInfo(service, tenant_dbid, "emp", emps.get(i).getAsString());
							if(rtn_person != null) {
								agentgroup.getAgentDBIDs().add(rtn_person.getDBID());
							}else {
								fail_emp_id.add(emps.get(i).getAsString());
							}
						}
					}else{
						rtn_persons = new HashSet<CfgPerson>();

						for(int i=0; i<emps.size(); i++){
							rtn_person = null;
							rtn_person = ps.getPersonInfo(service, tenant_dbid, "emp", emps.get(i).getAsString());
							if(rtn_person != null) {
								rtn_persons.add(rtn_person);
							}else {
								fail_emp_id.add(emps.get(i).getAsString());
							}
						}
						agentgroup.setAgents(rtn_persons);
					}
				}							
			}
			if(fail_emp_id.size() == 0) {
				agentgroup.save();
			}
		}catch(ConfigException ex){
			logger.error("ConfigException", ex);
		}finally {
			assigninfovo.setFail_emp_id(fail_emp_id);
			assigninfovo.setAgentgroup(agentgroup);
		}
		return assigninfovo;
	}

	/**
	 * AgentGroup에 Assign된 상담사를 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param group_name
	 * @param employee_id
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public AssignInfoVo unAssignPersonToAgentGroup_HD(
			final IConfService service,
			int tenant_dbid,
			String group_name,
			JsonElement employee_ids
			) {
		CfgAgentGroup agentgroup = null;
		CfgPerson rtn_person = null;
		AssignInfoVo assigninfovo = new AssignInfoVo();
		List<String> fail_emp_id = new ArrayList<String>();
		try{
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setName(group_name);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
			JsonArray emps = employee_ids.getAsJsonArray();
			if(emps != null && emps.size() > 0) {
				ps = new ConfigPerson();

				if(agentgroup != null){
					if(agentgroup.getAgentDBIDs() != null){
						for(int i=0; i<emps.size(); i++){
							rtn_person = null;
							rtn_person = ps.getPersonInfo(service, tenant_dbid, "emp", emps.get(i).getAsString());
							if(rtn_person != null) {
								agentgroup.getAgentDBIDs().remove(rtn_person.getDBID());
							}else {
								fail_emp_id.add(emps.get(i).getAsString());
							}
						}
					}else {
						for(int i=0; i<emps.size(); i++){
							fail_emp_id.add(emps.get(i).getAsString());
						}
					}
				}
			}
			if(fail_emp_id.size() == 0) {
				agentgroup.save();
			}
		}catch(ConfigException ex){
			logger.error("ConfigException", ex);
		}finally {
			assigninfovo.setFail_emp_id(fail_emp_id);
			assigninfovo.setAgentgroup(agentgroup);
		}
		return assigninfovo;
	}





	/**
	 * AgentGroup에 상담사를 Assign한다.
	 * @param service
	 * @param tenant_dbid
	 * @param moveObj,
	 * @param employee_ids
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup assignPersonToAgentGroup(
			final IConfService service,
			int tenant_dbid,
			JsonElement moveObj,
			String group_name
			) {
		CfgAgentGroup agentgroup = null;
		Collection<CfgPerson> rtn_persons = null;
		CfgPerson rtn_person = null;
		JsonArray psarry = null;
		try{
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setName(group_name);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
			if(moveObj != null){
				psarry = moveObj.getAsJsonArray();
				ps = new ConfigPerson();

				if(agentgroup != null){
					if(agentgroup.getAgentDBIDs() != null){
						for(int i=0; i<psarry.size(); i++){
							JsonObject ob =  psarry.get(i).getAsJsonObject();
							String empid = ob.get("employeeId").getAsString();
							rtn_person = null;
							rtn_person = ps.getPersonInfo(service, tenant_dbid, "emp", empid);
							agentgroup.getAgentDBIDs().add(rtn_person.getDBID());
						}
					}else{
						rtn_persons = new HashSet<CfgPerson>();
						for(int i=0; i<psarry.size(); i++){
							JsonObject ob =  psarry.get(i).getAsJsonObject();
							String empid = ob.get("employeeId").getAsString();
							rtn_person = null;
							rtn_person = ps.getPersonInfo(service, tenant_dbid, "emp", empid);
							if(rtn_person != null) {
								rtn_persons.add(rtn_person);
							}
						}
						agentgroup.setAgents(rtn_persons); 
					}
				}
				agentgroup.save();
			}

		}catch(ConfigException ex){
			agentgroup = null;
			logger.error("ConfigException", ex);
		}
		return agentgroup;
	}

	/**
	 * AgentGroup에 Assign된 상담사를 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param group_name
	 * @param employee_ids
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup unAssignPersonToAgentGroup(
			final IConfService service,
			int tenant_dbid,
			JsonElement moveObj,
			String group_name
			) {
		CfgAgentGroup agentgroup = null;
		CfgPerson rtn_person = null;
		JsonArray psarry = null;
		try{
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setName(group_name);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
			if(moveObj != null){
				psarry = moveObj.getAsJsonArray();
				ps = new ConfigPerson();

				if(agentgroup != null){
					if(agentgroup.getAgentDBIDs() != null){
						for(int i=0; i<psarry.size(); i++){
							JsonObject ob =  psarry.get(i).getAsJsonObject();
							String empid = ob.get("employeeId").getAsString();
							rtn_person = null;
							rtn_person = ps.getPersonInfo(service, tenant_dbid, "emp", empid);
							agentgroup.getAgentDBIDs().remove(rtn_person.getDBID());
						}
					}
				}
				agentgroup.save();
			}
		}catch(ConfigException ex){
			logger.error("ConfigException", ex);
		}
		return agentgroup;
	}

	/**
	 * AgentGroup에 Assign된 상담사를 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param group_name
	 * @param employee_id
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup unAssignPersonToAgentGroup(
			final IConfService service,
			int tenant_dbid,
			String group_name,
			String employee_id
			) {
		CfgAgentGroup agentgroup = null;
		CfgPerson rtn_person = null;
		JsonArray psarry = null;
		try{
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setName(group_name);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
			ps = new ConfigPerson();

			if(agentgroup != null){
				if(agentgroup.getAgentDBIDs() != null){
					rtn_person = null;
					rtn_person = ps.getPersonInfo(service, tenant_dbid, "emp", employee_id);
					agentgroup.getAgentDBIDs().remove(rtn_person.getDBID());
				}
			}
			agentgroup.save();
		}catch(ConfigException ex){
			logger.error("ConfigException", ex);
		}
		return agentgroup;
	}
	
	/**
	 * AgentGroup에 상담사를 Assign한다.
	 * @param service
	 * @param tenant_dbid
	 * @param group_dbid
	 * @param person_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup assignPersonToAgentGroup(
			final IConfService service,
			int tenant_dbid,
			int group_dbid,
			int person_dbid
			) {
		Collection<CfgPerson> rtn_persons = null;
		CfgAgentGroup agentgroup = null;
		try{
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setDbid(group_dbid);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
			
			if(agentgroup != null){
				if(agentgroup.getAgentDBIDs() != null){
					logger.info("null아님");
					agentgroup.getAgentDBIDs().add(person_dbid);
				}else{
					logger.info("null임");
					rtn_persons = ps.getPersonInfo(service, tenant_dbid, person_dbid);
					agentgroup.setAgents(rtn_persons);
				}
				agentgroup.save();
			}			
		}catch(ConfigException ex){
			logger.error("ConfigException", ex);
		}
		return agentgroup;
	}
	
	/**
	 * AgentGroup에 Assign된 상담사를 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param group_dbid
	 * @param person_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup removePersonFromAgentGroup(
			final IConfService service,
			int tenant_dbid,
			String group_name,
			String employee_ids		 
			){

		CfgPerson person = null;
		CfgAgentGroup agentgroup = null;
		try{			
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setName(group_name);
			agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
			String[] emps = employee_ids.split(",");
			if(agentgroup != null){
				ps = new ConfigPerson();
				for(int i=0; i<emps.length; i++){
					person = null;
					person = ps.getPersonInfo(service, tenant_dbid, "emp", emps[i]);
					if(agentgroup.getAgentDBIDs() != null) {
						agentgroup.getAgentDBIDs().remove(person.getDBID());
					}else {
						System.out.println("Agent Is Null");
					}
				}
				agentgroup.save();
			}			
		}catch(ConfigException ex){	
			logger.error("ConfigException", ex);
		}
		return agentgroup;
	}
	
	/**
	 * Source AgentGroup에 속한 Person을 Target AgentGroup으로 이동한다.
	 * @param service
	 * @param tenant_dbid
	 * @param source_group_name
	 * @param target_group_name
	 * @param delete_yn
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup movePerson(
			final IConfService service,
			int tenant_dbid,
			String source_group_name,
			String target_group_name,
			String delete_yn
			){

		CfgAgentGroup s_agentgroup = null;
		CfgAgentGroup t_agentgroup = null;
		try{
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setName(source_group_name);
			s_agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
			agentgroupquery.setName(target_group_name);
			t_agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);			
			
			if(s_agentgroup != null) {
				if(s_agentgroup.getAgentDBIDs() != null) {
					if(t_agentgroup != null) {
						if(t_agentgroup.getAgentDBIDs() != null) {
							for(int dbid : s_agentgroup.getAgentDBIDs()) {
								t_agentgroup.getAgentDBIDs().add(dbid);
							}
						}else {
							t_agentgroup.setAgents(s_agentgroup.getAgents());
						}
						t_agentgroup.save();
						if(delete_yn.equals("Y")) {
							Collection<CfgPerson> rtnpersons = new HashSet<CfgPerson>();
							s_agentgroup.setAgents(rtnpersons);
							s_agentgroup.save();
						}
					}
				}
			}
		}catch(ConfigException ex){
			logger.error("ConfigException", ex);
		}
		return t_agentgroup;
	}
	
	/**
	 * Source AgentGroup에 속한 Person을 Target AgentGroup으로 이동한다.
	 * @param service
	 * @param tenant_dbid
	 * @param source_group_name
	 * @param target_group_name
	 * @param employee_ids
	 * @param delete_yn
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentGroup movePerson2(
			final IConfService service,
			int tenant_dbid,
			String source_group_name,
			String target_group_name,
			String employee_ids,
			String delete_yn
			){

		CfgAgentGroup s_agentgroup = null;
		CfgAgentGroup t_agentgroup = null;
		Collection<CfgPerson> rtn_persons = null;
		try{
			CfgAgentGroupQuery agentgroupquery = new CfgAgentGroupQuery();
			agentgroupquery.setTenantDbid(tenant_dbid);
			agentgroupquery.setName(source_group_name);
			s_agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);
			agentgroupquery.setName(target_group_name);
			t_agentgroup = service.retrieveObject(CfgAgentGroup.class,	agentgroupquery);			
			String[] emps = employee_ids.split(",");
			if(s_agentgroup != null) {
				if(s_agentgroup.getAgentDBIDs() != null) {
					if(t_agentgroup != null) {

						
						if(emps != null && emps.length > 0) {
							if(t_agentgroup.getAgentDBIDs() != null) {	//Person이 있을때 (있는거에 add)
								for(CfgPerson person : s_agentgroup.getAgents()) {
									for(int i=0; i<emps.length; i++) {
										if(person.getEmployeeID().equals(emps[i])) {
											t_agentgroup.getAgentDBIDs().add(person.getDBID());										
										}
									}
								}
							}else {										//Person이 없을때(Collection만들어서 setAgent)
								rtn_persons = new HashSet<CfgPerson>();
								for(CfgPerson person : s_agentgroup.getAgents()) {
									for(int i=0; i<emps.length; i++) {
										if(person.getEmployeeID().equals(emps[i])) {	//상담그룹의 상담사 목록에 emps가 있으면
											rtn_persons.add(person);
										}
									}
								}
								t_agentgroup.setAgents(rtn_persons);
							}
						}else {
							if(t_agentgroup.getAgentDBIDs() != null) {	//Person이 있을때 (있는거에 add)
								for(int dbid : s_agentgroup.getAgentDBIDs()) {
									t_agentgroup.getAgentDBIDs().add(dbid);
								}
							}else {										//Person이 없을때(Collection만들어서 setAgent)
								t_agentgroup.setAgents(s_agentgroup.getAgents());
							}
						}
						t_agentgroup.save();
						if(delete_yn.equals("Y")) {
							
							Collection<CfgPerson> delpersons = s_agentgroup.getAgents();
							if(delpersons != null) {
								if(rtn_persons != null) {
									for(CfgPerson ps : rtn_persons) {
										delpersons.remove(ps);
									}
								}else {
									delpersons = new HashSet<CfgPerson>();
								}
							}else {
								delpersons = new HashSet<CfgPerson>();
							}
							s_agentgroup.setAgents(delpersons);
							s_agentgroup.save();
						}
					}
				}
			}
		}catch(ConfigException ex){
			logger.error("ConfigException", ex);
		}
		return t_agentgroup;
	}
}
