package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;
import java.util.Set;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.i4way.genesys.model.AgentLoginInfoVo;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLogin;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLoginInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDN;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSwitch;
import com.genesyslab.platform.applicationblocks.com.queries.CfgAgentLoginQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPersonQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgSwitchQuery;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigAgentLogin {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	public ConfigAgentLogin() {
		
	}

	/**
	 * employee_id에 해당하는 Person에 Assign된 AgentLogin정보를 조회한다.
	 * @param service
	 * @param tenant_dbid
	 * @param employee_id
	 * @return
	 */
	public Collection<CfgAgentLoginInfo> getAgentLoginInfo(final IConfService service, int tenant_dbid, String employee_id){
		CfgPerson tmpCfgPerson = null;
		Collection<CfgAgentLoginInfo> rtnAgentLoginInfo = null;
		CfgPersonQuery personquery = new CfgPersonQuery();
		personquery.setTenantDbid(tenant_dbid); 
		personquery.setEmployeeId(employee_id);
		try {
			tmpCfgPerson = service.retrieveObject(CfgPerson.class, personquery);
			
			if(tmpCfgPerson != null) {
				rtnAgentLoginInfo = tmpCfgPerson.getAgentInfo().getAgentLogins();
			}
		}catch (ConfigException e) {
			logger.error(e.getMessage());
		
		}
		
		return rtnAgentLoginInfo;
	}

	/**
	 * AgentLogin을 조회한다.(multi)
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param assignable
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public Collection<CfgAgentLogin> getAgentLogins(
			final IConfService service, int tenant_dbid, int switch_dbid, Boolean assignable)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		CfgAgentLoginQuery agentloginquery = new CfgAgentLoginQuery();
		agentloginquery.setTenantDbid(tenant_dbid);
		agentloginquery.setSwitchDbid(switch_dbid);
		if(assignable) {
			agentloginquery.setNoPersonDbid(2);
		}
		
		Collection<CfgAgentLogin> agentlogin = null;
		agentlogin = service.retrieveMultipleObjects(CfgAgentLogin.class,
				agentloginquery);
		return agentlogin;
	}


	/**
	 * Switch를 조회한다.(multi)
	 * @param service
	 * @param tenant_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public Collection<CfgSwitch> getSwitchs(
			final IConfService service, int tenant_dbid)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		CfgSwitchQuery switchquery = new CfgSwitchQuery();
		switchquery.setTenantDbid(tenant_dbid);
		
		Collection<CfgSwitch> switchs = null;
		switchs = service.retrieveMultipleObjects(CfgSwitch.class,
		switchquery);
		return switchs;
	}

	/**
	 * AgentLogin을 조회한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param login_code
	 * @return CfgAgentLogin
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentLogin getAgentLogin(
			final IConfService service, int tenant_dbid, int switch_dbid, String login_code
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		CfgAgentLogin agentlogin = null;
		CfgAgentLoginQuery agentloginquery = new CfgAgentLoginQuery();
		agentloginquery.setTenantDbid(tenant_dbid);
		agentloginquery.setSwitchDbid(switch_dbid);
		agentloginquery.setLoginCode(login_code);

		agentlogin = service.retrieveObject(CfgAgentLogin.class, agentloginquery);
		return agentlogin;
	}	

	/**
	 * AgentLogin을 조회한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param agentLogin_dbid
	 * @return CfgAgentLogin
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentLogin getAgentLogin(
			final IConfService service,
			int tenant_dbid,
			int switch_dbid,
			String login_code,
			int agentLogin_dbid
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		CfgAgentLogin agentlogin = null;
		CfgAgentLoginQuery agentloginquery = new CfgAgentLoginQuery();
		agentloginquery.setTenantDbid(tenant_dbid);
		agentloginquery.setSwitchDbid(switch_dbid);
		agentloginquery.setDbid(agentLogin_dbid);
		agentloginquery.setLoginCode(login_code);
		agentlogin = service.retrieveObject(CfgAgentLogin.class, agentloginquery);
		return agentlogin;
	}	

	/**
	 * AgentLogin의 Assign여부를 확인한다.
	 * @param loginDBID
	 * @param service
	 * @return true : Assign 가능, false : 이미 Assign중.
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean checkAssignYn(IConfService service, 
			int tenant_dbid,
			int switch_dbid,
			String login_code
			)
					throws ConfigException, InterruptedException {
		boolean rtnval = false;

		String warpuptm = "";

		CfgAgentLoginQuery loginquery = new CfgAgentLoginQuery();
		loginquery.setTenantDbid(tenant_dbid);
		loginquery.setSwitchDbid(switch_dbid);
		loginquery.setLoginCode(login_code);
		loginquery.setNoPersonDbid(2);
		
		CfgAgentLogin agentlogin = null;
		agentlogin = service.retrieveObject(CfgAgentLogin.class,loginquery);
		
		if(agentlogin != null){
			KeyValueCollection appOptions = agentlogin.getUserProperties();
				
			for(Object selectionObj : appOptions){
				KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
				
		        for (Object recordObj : sectionKvp.getTKVValue()) {
		            KeyValuePair recordKvp = (KeyValuePair) recordObj;
		            if(recordKvp.getStringKey().equals("wrap-up-time")){
		            	warpuptm = recordKvp.getStringValue();
		            	//logger.info("wrap-up-time=" + warpuptm);
		            }
		        }
			}
			if(warpuptm.equals("")){
				rtnval = true;
			}else{
				rtnval = false;
			}
		}
		return rtnval;
	}


	/**
	 * AgentLogin을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param AgentLoginInfoVo
	 * @return
	 */
	public CfgAgentLogin createAgentLogin(
			final IConfService service, 
			int tenant_dbid, 
			int switch_dbid,
			AgentLoginInfoVo agentlogininfovo) {
		CfgAgentLogin agentlogin = null;
		try{
			// Read configuration objects:
			agentlogin = new CfgAgentLogin(service);
			agentlogin.setTenantDBID(tenant_dbid);	//Tenant DBID
			agentlogin.setSwitchDBID(agentlogininfovo.getSwitch_dbid());
			agentlogin.setLoginCode(agentlogininfovo.getCode());
			agentlogin.setSwitchSpecificType(agentlogininfovo.getSwitchSpecificType());
			if(agentlogininfovo.getState() == 1){		//State
				agentlogin.setState(CfgObjectState.CFGEnabled);
			}else{
				agentlogin.setState(CfgObjectState.CFGDisabled);
			}

			if(agentlogininfovo.getFolderId() > 0) {	// Folder DBID
				agentlogin.setFolderId(agentlogininfovo.getFolderId());
			}

			agentlogin.save();

			if(agentlogin != null) {
				JsonArray psarry = null;
				if(agentlogininfovo.getAnnex() != null){
					psarry = agentlogininfovo.getAnnex().getAsJsonArray();
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
					agentlogin.setUserProperties(obj_kv);
				}

				agentlogin.save();
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return agentlogin;
	}

	/**
	 * AgentLogin을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param AgentLoginInfoVo
	 * @return
	 */
	public CfgAgentLogin createAgentLogin(
			final IConfService service, 
			int tenant_dbid, 
			int switch_dbid,
			String agent_login_id) {
		CfgAgentLogin agentlogin = null;
		KeyValuePair tmppair;
		boolean isSection = false;
		boolean isOption = false;
		
		try{
			// Read configuration objects:
			agentlogin = new CfgAgentLogin(service);
			agentlogin.setTenantDBID(tenant_dbid);	//Tenant DBID
			agentlogin.setSwitchDBID(switch_dbid);
			agentlogin.setLoginCode(agent_login_id);
			agentlogin.setSwitchSpecificType(1);

			agentlogin.save();
			
			agentlogin = setDialPlan(agentlogin, "DialPlan_Agent");

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return agentlogin;
	}
	
	/**
	 * AgentLogin의 Dialplan을 변경한다. 
	 * @param service
	 * @param AgentLogin 	상태 변경할 AgentLogin 객체
	 * @param flag 		(true = 설정, false = 삭제)
	 * @param dialplan명
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentLogin setDialPlan(
			CfgAgentLogin agentlogin,
			String dialplan_name
			)
					throws ConfigException, InterruptedException {
		KeyValuePair tmppair;
		boolean isSection = false;
		boolean isOption = false;

		if(agentlogin != null) {
			KeyValueCollection obj_kv = agentlogin.getUserProperties();
			KeyValuePair targetSectionKvp = null;
			if(obj_kv != null){
				for (Object selectionObj : obj_kv) {
					KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
					if(sectionKvp.getStringKey().equals("TServer")){
						isSection = true;
						targetSectionKvp = sectionKvp;
						for (Object recordObj : sectionKvp.getTKVValue()) {
							tmppair = (KeyValuePair) recordObj;
							if(tmppair.getStringKey().equals("dial-plan")){
								isOption = true;
								break;
							}
						}
					}
				}
				if(isSection && isOption){
					KeyValuePair tmpKvp;
					KeyValueCollection tmp_kv = targetSectionKvp.getTKVValue();
					tmp_kv.remove("dial-plan");
					tmp_kv.addString("dial-plan", dialplan_name);

					tmpKvp = new KeyValuePair("TServer", tmp_kv);
					obj_kv.remove("TServer");
					obj_kv.addPair(tmpKvp);
					agentlogin.setUserProperties(obj_kv);
					agentlogin.save();
				}else if(isSection && !isOption){
					KeyValuePair tmpKvp;
					KeyValueCollection tmp_kv = targetSectionKvp.getTKVValue();
					tmp_kv.addString("dial-plan", dialplan_name);

					tmpKvp = new KeyValuePair("TServer", tmp_kv);
					obj_kv.remove("TServer");
					obj_kv.addPair(tmpKvp);
					agentlogin.setUserProperties(obj_kv);
					agentlogin.save();
				}else if(!isSection){
					KeyValuePair tmpKvp;
					KeyValueCollection tmp_kv = new KeyValueCollection();
					tmp_kv.addString("dial-plan", dialplan_name);

					tmpKvp = new KeyValuePair("TServer", tmp_kv);
					obj_kv.addPair(tmpKvp);
					agentlogin.setUserProperties(obj_kv);
					agentlogin.save();
				}
			}
		}
		return agentlogin;
	}

	/**
	 * AgentLogin을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param agentlogin
	 * @param AgentLoginInfoVo
	 * @return
	 */
	public CfgAgentLogin modifyAgentLogin(
			final IConfService service, 
			int tenant_dbid, 
			int switch_dbid,
			CfgAgentLogin agentlogin,
			AgentLoginInfoVo agentlogininfovo){
		// Read configuration objects:
		try {
			agentlogin.setSwitchSpecificType(agentlogininfovo.getSwitchSpecificType());
			if(agentlogininfovo.getState() == 1){		//State
				agentlogin.setState(CfgObjectState.CFGEnabled);
			}else{
				agentlogin.setState(CfgObjectState.CFGDisabled);
			}
			
			agentlogin.save();

			if(agentlogin != null) {
				JsonArray psarry = null;

				if(agentlogininfovo.getAnnex() != null){
					psarry = agentlogininfovo.getAnnex().getAsJsonArray();
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
					agentlogin.setUserProperties(obj_kv);
				}

				agentlogin.save();
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return agentlogin;
	}
	
	/**
	 * AgentLogin을 삭제한다.
	 * @param service
	 * @param agentlogin
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteAgentLogin(
			final IConfService service,
			CfgAgentLogin agentlogin
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			agentlogin.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	

	/**
	 * AgentLogin의 상태를 변경한다. 
	 * @param service
	 * @param AgentLogin 	상태 변경할 AgentLogin 객체
	 * @param flag 		(true = Enabled, false = Disabled)
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgAgentLogin setAgentLoginState(
			final IConfService service,
			CfgAgentLogin agentlogin,
			boolean flag
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		if(flag){
			agentlogin.setState(CfgObjectState.CFGEnabled);
		}else{
			agentlogin.setState(CfgObjectState.CFGDisabled);
		}
		agentlogin.save();
		return agentlogin;
	}

}
