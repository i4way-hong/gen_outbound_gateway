package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.i4way.genesys.model.DnVo;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDN;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDNGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSwitch;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTransaction;
import com.genesyslab.platform.applicationblocks.com.queries.CfgDNGroupQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgDNQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgSwitchQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgTransactionQuery;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.configuration.protocol.types.CfgDNGroupType;
import com.genesyslab.platform.configuration.protocol.types.CfgDNRegisterFlag;
import com.genesyslab.platform.configuration.protocol.types.CfgDNType;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genesyslab.platform.configuration.protocol.types.CfgRouteType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigDn {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	ConfigCommon configcommon = null;
	public ConfigDn() {
		configcommon = new ConfigCommon();
	}
	
	/**
	 * CfgDN을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @return
	 */
	public Collection<CfgDN> getDn(final IConfService service, int tenant_dbid, int switch_dbid) {
		Collection<CfgDN> rtndn = null;
		CfgDNQuery dnquery = new CfgDNQuery();
		dnquery.setTenantDbid(tenant_dbid);
		try {
			rtndn = service.retrieveMultipleObjects(CfgDN.class, dnquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return rtndn;
	}

	/**
	 * CfgDN을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @return
	 */
	public Collection<CfgDN> getExtensions(final IConfService service, int tenant_dbid, int switch_dbid) {
		Collection<CfgDN> rtndn = null;
		CfgDNQuery dnquery = new CfgDNQuery();
		dnquery.setTenantDbid(tenant_dbid);
		dnquery.setDnType(CfgDNType.CFGExtension);
		try {
			rtndn = service.retrieveMultipleObjects(CfgDN.class, dnquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return rtndn;
	}

	/**
	 * DialPlan을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @return
	 */
	public Collection<CfgDN> getDialPlan(final IConfService service, int tenant_dbid, int switch_dbid) {
		Collection<CfgDN> rtndn = null;
		Collection<CfgDN> dns = null;
		CfgDNQuery dnquery = new CfgDNQuery();
		dnquery.setTenantDbid(tenant_dbid);
		try {
			dns = service.retrieveMultipleObjects(CfgDN.class, dnquery);
			rtndn = new HashSet<CfgDN>();	//새로운 DN정보
			for(CfgDN dn : dns){
				if(configcommon.checkDialPlan(dn.getUserProperties())){
					rtndn.add(dn);
				}
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return rtndn;
	}

	/**
	 * DialPlan을 가지고 있는 DN을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @return
	 */
	public Collection<CfgDN> getDn_DialPlan(final IConfService service, int tenant_dbid, int switch_dbid, String dialplan) {
		Collection<CfgDN> rtndn = null;
		Collection<CfgDN> dns = null;
		CfgDNQuery dnquery = new CfgDNQuery();
		dnquery.setTenantDbid(tenant_dbid);
		try {
			dns = service.retrieveMultipleObjects(CfgDN.class, dnquery);
			rtndn = new HashSet<CfgDN>();	//새로운 DN정보
			for(CfgDN dn : dns){
				if(configcommon.checkDn_DialPlan(dn.getUserProperties(), dialplan)){
					rtndn.add(dn);
				}
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return rtndn;
	}
	
	/**
	 * CfgSwitch을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_name
	 * @return
	 */
	public CfgSwitch getSwitch(final IConfService service, int tenant_dbid, String switch_name) {
		CfgSwitch rtnswitch = null;
		CfgSwitchQuery switchquery = new CfgSwitchQuery();
		switchquery.setTenantDbid(tenant_dbid);
		switchquery.setName(switch_name);
		try {
			rtnswitch = service.retrieveObject(CfgSwitch.class, switchquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnswitch;
	}
	
	/**
	 * CfgDN을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param dn_name
	 * @return
	 */
	public CfgDN getDn(final IConfService service, int tenant_dbid, int switch_dbid, String dn_name) {
		CfgDN rtndn = null;
		CfgDNQuery dnquery = new CfgDNQuery();
		dnquery.setTenantDbid(tenant_dbid);
		dnquery.setDnNumber(dn_name);
		try {
			rtndn = service.retrieveObject(CfgDN.class, dnquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtndn;
	}
	
	/**
	 * CfgDN을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param dn_name
	 * @return
	 */
	public CfgTransaction gettr(final IConfService service, int tenant_dbid, int switch_dbid, String tr_name) {
		CfgTransaction rtndn = null;
		CfgTransactionQuery dnquery = new CfgTransactionQuery();
		dnquery.setTenantDbid(tenant_dbid);
		dnquery.setName(tr_name);
		try {
			rtndn = service.retrieveObject(CfgTransaction.class, dnquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtndn;
	}
	
	/**
	 * CfgDN을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param dn_dbid
	 * @return
	 */
	public CfgDN getDn(final IConfService service, int tenant_dbid, int switch_dbid, int dn_dbid) {
		CfgDN rtndn = null;
		CfgDNQuery dnquery = new CfgDNQuery();
		dnquery.setTenantDbid(tenant_dbid);
		dnquery.setDbid(dn_dbid);
		try {
			rtndn = service.retrieveObject(CfgDN.class, dnquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtndn;
	}

/**
	 * Dn을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param first_name
	 * @param last_name
	 * @param employee_id
	 * @param user_name
	 * @param folder_dbid
	 * @return
	 */
	public CfgDN createDn(
			final IConfService service, 
			int tenant_dbid, 
			DnVo vo) {
		CfgDN dn = null;
		try{
			// Read configuration objects:
			dn = new CfgDN(service);
			dn.setTenantDBID(tenant_dbid);
			dn.setSwitchDBID(vo.getSwitch_dbid());
			if(!vo.getAssociation().equals("")) {	// Association
				dn.setAssociation(vo.getAssociation());
			}
			
			if(!vo.getDn_login_id().equals("")) {	// login_id
				dn.setDNLoginID(vo.getDn_login_id());
			}

			if(vo.getFolderId() != 0) {	// Folder DBID
				dn.setFolderId(vo.getFolderId());
			}
			if(vo.getGroup_dbid() > 0) {	// group_dbid
				dn.setGroupDBID(vo.getGroup_dbid());
			}
			dn.setNumber(vo.getNumber());

			if(!vo.getName().equals("")) {	// name(Alias)
				dn.setName(vo.getName());
			}

			if(vo.getUse_override() == 1){		//override flag
				dn.setUseOverride(CfgFlag.CFGTrue);
			}else{
				dn.setUseOverride(CfgFlag.CFGFalse);
			}
			if(vo.getUse_override_name() != null && !vo.getUse_override_name().equals("")) {	// Use_override name
				dn.setOverride(vo.getUse_override_name());
			}

			if(vo.getState() == 1){
				dn.setState(CfgObjectState.CFGEnabled);
			}else{
				dn.setState(CfgObjectState.CFGDisabled);
			}

			dn.setType(CfgDNType.valueOf(vo.getType()));
			dn.setRegisterAll(CfgDNRegisterFlag.valueOf(vo.getRegister()));
			dn.setRouteType(CfgRouteType.valueOf(vo.getRoute_type()));
			dn.setSwitchSpecificType(vo.getSwitch_specific_type());
			dn.setTrunks(vo.getTrunks());

			dn.save();		
			
			if(dn != null) {
				JsonArray psarry = null;

				if(vo.getAnnex() != null){
					psarry = vo.getAnnex().getAsJsonArray();
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
							tmpValue = tmpValue.replaceAll("\\^\\^gt\\^", ">");
							tmpValue = tmpValue.replaceAll("\\^\\^lt\\^", "<");
							tmpValue = tmpValue.replaceAll("\\^\\^58\\^", ":");
							tmpValue = tmpValue.replaceAll("\\^\\^123\\^", "{");
							tmpValue = tmpValue.replaceAll("\\^\\^125\\^", "}");
							tmpValue = tmpValue.replaceAll("\\^\\^quot\\^", "\"");
							tmpValue = tmpValue.replaceAll("\\^\\^at\\^", "@");
							tmpValue = tmpValue.replaceAll("\\^\\^126\\^", "~");
							tmpValue = tmpValue.replaceAll("\\^\\^coma\\^", ",");
							tmpValue = tmpValue.replaceAll("\\^\\^47\\^", "/");
							insert_kv.addString(tmpkey, tmpValue); 
						}
						tmpkvp = new KeyValuePair(key, insert_kv);
						obj_kv.addPair(tmpkvp);
					}
					dn.setUserProperties(obj_kv);
				}

				dn.save();
			}			
		}catch (ConfigException e) {
			e.printStackTrace();
		}	
		return dn;
	}

	/**
	 * Dn을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param dn
	 * @param DnVo
	 * @return
	 */
	public CfgDN modifyDn(
			final IConfService service, 
			int tenant_dbid, 
			CfgDN dn,
			DnVo vo){
		// Read configuration objects:
		try {
			if(!vo.getAssociation().equals("")) {	// Association
				dn.setAssociation(vo.getAssociation());
			}
			
			if(!vo.getDn_login_id().equals("")) {	// login_id
				dn.setDNLoginID(vo.getDn_login_id());
			}
//			if(vo.getFolderId() != 0) {	// Folder DBID
//				dn.setFolderId(vo.getFolderId());
//			}
			if(vo.getGroup_dbid() > 0) {	// group_dbid
				dn.setGroupDBID(vo.getGroup_dbid());
			}

			if(!vo.getName().equals("")) {	// name(Alias)
				dn.setName(vo.getName());
			}

			if(vo.getUse_override() == 1){		//override flag
				dn.setUseOverride(CfgFlag.CFGTrue);
			}else{
				dn.setUseOverride(CfgFlag.CFGFalse);
			}

			if(vo.getState() == 1){
				dn.setState(CfgObjectState.CFGEnabled);
			}else{
				dn.setState(CfgObjectState.CFGDisabled);
			}

			dn.setRegisterAll(CfgDNRegisterFlag.valueOf(vo.getRegister()));
			dn.setRouteType(CfgRouteType.valueOf(vo.getRoute_type()));
			dn.setSwitchSpecificType(vo.getSwitch_specific_type());
			dn.setTrunks(vo.getTrunks());

			dn.save();

			if(dn != null) {
				JsonArray psarry = null;
				if(vo.getAnnex() != null){
					psarry = vo.getAnnex().getAsJsonArray();
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
							tmpValue = tmpValue.replaceAll("\\^\\^36\\^", "$");
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
					dn.setUserProperties(obj_kv);
				}

				dn.save();
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return dn;
	}

	/**
	 * DN을 삭제한다.
	 * @param service
	 * @param dn
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteDn(
			final IConfService service,
			CfgDN dn
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			dn.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	

	/**
	 * DN의 상태를 변경한다. 
	 * @param service
	 * @param dn 	상태 변경할 dn 객체
	 * @param flag 		(true = Enabled, false = Disabled)
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgDN setDnState(
			final IConfService service,
			CfgDN dn,
			boolean flag
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		if(flag){
			dn.setState(CfgObjectState.CFGEnabled);
		}else{
			dn.setState(CfgObjectState.CFGDisabled);
		}
		dn.save();
		return dn;
	}

	/**
	 * DN의 Dialplan을 변경한다. 
	 * @param service
	 * @param dn 	상태 변경할 dn 객체
	 * @param flag 		(true = 설정, false = 삭제)
	 * @param dialplan명
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgDN setDialPlan(
			final IConfService service,
			CfgDN dn,
			boolean flag,
			String dialplan_name
			)
					throws ConfigException, InterruptedException {
		KeyValuePair tmppair;
		boolean isSection = false;
		boolean isOption = false;

		if(dn != null) {
			KeyValueCollection obj_kv = dn.getUserProperties();
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
					if(flag){
						tmp_kv.addString("dial-plan", dialplan_name);
					}

					tmpKvp = new KeyValuePair("TServer", tmp_kv);
					obj_kv.remove("TServer");
					obj_kv.addPair(tmpKvp);
					dn.setUserProperties(obj_kv);
					dn.save();
				}else if(isSection && !isOption){
					KeyValuePair tmpKvp;
					KeyValueCollection tmp_kv = targetSectionKvp.getTKVValue();
					tmp_kv.addString("dial-plan", dialplan_name);

					tmpKvp = new KeyValuePair("TServer", tmp_kv);
					obj_kv.remove("TServer");
					obj_kv.addPair(tmpKvp);
					dn.setUserProperties(obj_kv);
					dn.save();
				}else if(!isSection){
					KeyValuePair tmpKvp;
					KeyValueCollection tmp_kv = new KeyValueCollection();
					tmp_kv.addString("dial-plan", dialplan_name);

					tmpKvp = new KeyValuePair("TServer", tmp_kv);
					obj_kv.addPair(tmpKvp);
					dn.setUserProperties(obj_kv);
					dn.save();
				}
			}
		}
		return dn;
	}

	/**
	 * DN의 setTsvrOption을 적용한다
	 * @param service
	 * @param dn 	상태 변경할 dn 객체
	 * @param section
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgDN setTsvrOption(
			final IConfService service,
			CfgDN dn,
			KeyValuePair section
			)
					throws ConfigException, InterruptedException {
		boolean isSection = false;

		if(dn != null) {
			KeyValueCollection obj_kv = dn.getUserProperties();
			if(obj_kv != null){
				for (Object selectionObj : obj_kv) {
					KeyValuePair sectionKvp = (KeyValuePair) selectionObj;
					if(sectionKvp.getStringKey().equals("TServer")){
						isSection = true;
					}
				}
				if(isSection){
					KeyValuePair tmpKvp;
					KeyValueCollection tmp_kv = section.getTKVValue();

					tmpKvp = new KeyValuePair("TServer", tmp_kv);
					obj_kv.remove("TServer");
					obj_kv.addPair(tmpKvp);
					dn.setUserProperties(obj_kv);
					dn.save();
				}else if(!isSection){
					KeyValuePair tmpKvp;
					KeyValueCollection tmp_kv = section.getTKVValue();

					tmpKvp = new KeyValuePair("TServer", tmp_kv);
					obj_kv.addPair(tmpKvp);
					dn.setUserProperties(obj_kv);
					dn.save();
				}
			}
		}
		return dn;
	}

/*	
	/**
	 * DN을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param number
	 * @param dn_type
	 * @param route_type
	 * @param switch_spec_type
	 * @return
	 */
	/* public CfgDN createDN(
			final IConfService service,
			int tenant_dbid,
			int switch_dbid,
			String number,
			String dn_type,
			String route_type,
			int switch_spec_type
			) {
		// Read configuration objects:
		CfgDN new_dn = new CfgDN(service);
		try {
			new_dn.setTenantDBID(tenant_dbid);
			new_dn.setNumber(number);
			new_dn.setSwitchDBID(switch_dbid);
			new_dn.setState(CfgObjectState.CFGEnabled);
			new_dn.setType(CfgDNType.valueOf(dn_type));
			new_dn.setRouteType(CfgRouteType.valueOf(route_type));
			new_dn.setSwitchSpecificType(switch_spec_type);
			new_dn.save();
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		
		return new_dn;
	} */
	
	/**
	 * AgentLogin을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param login_code
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
/* 	public boolean deleteDN(
			final IConfService service,
			int tenant_dbid,
			int switch_dbid,
			String dn_name
			) {
		CfgDN rtndn = null;
		boolean rtnval = false;
		CfgDNQuery dnquery = new CfgDNQuery();
		dnquery.setTenantDbid(tenant_dbid);
		dnquery.setSwitchDbid(switch_dbid);
		dnquery.setDnNumber(dn_name);
		try {
			rtndn = service.retrieveObject(CfgDN.class, dnquery);
			if(rtndn != null) {
				rtndn.delete();
				rtnval = true;
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnval;
	}	 */
	

	/**
	 * DN을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param login_code
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteDN(
			final IConfService service,
			int tenant_dbid,
			int switch_dbid,
			String dn_name,
			String dn_type
			) {
		CfgDN rtndn = null;
		boolean rtnval = false;
		CfgDNQuery dnquery = new CfgDNQuery();
		dnquery.setTenantDbid(tenant_dbid);
		dnquery.setDnNumber(dn_name);
		dnquery.setDnType(CfgDNType.valueOf(dn_type));
		try {
			rtndn = service.retrieveObject(CfgDN.class, dnquery);
			if(rtndn != null) {
				rtndn.delete();
				rtnval = false;
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnval;
	}	

	
	
	/**
	 * CfgDNGroup을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @return
	 */
	public Collection<CfgDNGroup> getDnGroup(final IConfService service, int tenant_dbid) {
		Collection<CfgDNGroup> rtndn = null;
		CfgDNGroupQuery dnquery = new CfgDNGroupQuery();
		dnquery.setTenantDbid(tenant_dbid);
		try {
			rtndn = service.retrieveMultipleObjects(CfgDNGroup.class, dnquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return rtndn;
	}
	
	/**
	 * CfgDNGroup을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param gorup_name
	 * @return
	 */
	public CfgDNGroup getDnGroup(final IConfService service, int tenant_dbid, String gorup_name) {
		CfgDNGroup rtndn = null;
		CfgDNGroupQuery dnquery = new CfgDNGroupQuery();
		dnquery.setTenantDbid(tenant_dbid);
		dnquery.setName(gorup_name);
		try {
			rtndn = service.retrieveObject(CfgDNGroup.class, dnquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtndn;
	}
		
	/**
	 * CfgDNGroup을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param group_dbid
	 * @return
	 */
	public CfgDNGroup getDnGroup(final IConfService service, int tenant_dbid, int group_dbid) {
		CfgDNGroup rtndn = null;
		CfgDNGroupQuery dnquery = new CfgDNGroupQuery();
		dnquery.setTenantDbid(tenant_dbid);
		dnquery.setDbid(group_dbid);
		try {
			rtndn = service.retrieveObject(CfgDNGroup.class, dnquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtndn;
	}
	
	/**
	 * CfgDNGroup을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param gorup_name
	 * @param dngroup_type
	 * @return
	 */
	public CfgDNGroup createDNGroup(
			final IConfService service,
			int tenant_dbid,
			String gorup_name,
			String dngroup_type
			) {
		// Read configuration objects:
		CfgDNGroup new_group = new CfgDNGroup(service);
		try {
			CfgGroup groupinfo = new CfgGroup(service, null);
			groupinfo.setName(gorup_name);
			groupinfo.setTenantDBID(tenant_dbid);
			new_group.setGroupInfo(groupinfo);
			new_group.setType(CfgDNGroupType.valueOf(dngroup_type));
			new_group.save();
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}		
		return new_group;
	}
	
	/**
	 * CfgDNGroup을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param gorup_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteDNGroup(
			final IConfService service,
			int tenant_dbid,
			String gorup_name
			) {
		CfgDNGroup group = null;
		boolean rtnval = false;
		
		try {
			group = getDnGroup(service, tenant_dbid, gorup_name);
			if(group != null) {
				group.delete();
				rtnval = true;
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnval;
	}	
}
