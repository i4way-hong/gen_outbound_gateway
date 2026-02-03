package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.i4way.genesys.model.CallingListInfoVo;
import kr.co.i4way.genesys.model.FilterInfoVo;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingList;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingListInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFilter;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTableAccess;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTreatment;
import com.genesyslab.platform.applicationblocks.com.queries.CfgCallingListQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgTableAccessQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgFilterQuery;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigCallingList {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	ConfigPerson ps = null;

	ConfigOB ob = null;
	public ConfigCallingList(){
	}
	
	/**
	 * CallingList을 조회한다. 
	 * @param service 
	 * @param tenant_dbid
	 * @param callinglist_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgCallingList getCallingList(
			final IConfService service,
			int tenant_dbid,
			String callinglist_name
			) {
		CfgCallingList callinglist = null;
		try {
			CfgCallingListQuery callinglistquery = new CfgCallingListQuery();
			callinglistquery.setTenantDbid(tenant_dbid);
			callinglistquery.setName(callinglist_name);
			callinglist = service.retrieveObject(CfgCallingList.class,	callinglistquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return callinglist;
	}
	
	/**
	 * CallingList을 조회한다. 
	 * @param service
	 * @param tenant_dbid
	 * @param callinglist_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgCallingList getCallingList(
			final IConfService service,
			int tenant_dbid,
			int callinglist_dbid
			) {
		CfgCallingList callinglist = null;
		try {
			CfgCallingListQuery callinglistquery = new CfgCallingListQuery();
			callinglistquery.setTenantDbid(tenant_dbid);
			callinglistquery.setDbid(callinglist_dbid);
			callinglist = service.retrieveObject(CfgCallingList.class,	callinglistquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return callinglist;
	}

	/**
	 * CallingList을 조회한다.
	 * @param iTenantDBID
	 * @param service
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public Collection<CfgCallingList> getCallingList(
			final IConfService service,
			int tenant_dbid
			) {
		Collection<CfgCallingList> callinglist = null;
		try {
			CfgCallingListQuery callinglistquery = new CfgCallingListQuery();
			callinglistquery.setTenantDbid(tenant_dbid);
			callinglist = service.retrieveMultipleObjects(CfgCallingList.class,	callinglistquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return callinglist;
	}






























	/**
	 * filter를 조회한다. 
		 * @param <CfgFilterQuery>
	 * @param service
	 * @param tenant_dbid
	 * @param filter_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgFilter getFilter(
			final IConfService service,
			int tenant_dbid,
			String filter_name
			) {
		CfgFilter filter = null;
		try {
			CfgFilterQuery filterquery = new CfgFilterQuery();
			filterquery.setTenantDbid(tenant_dbid);
			filterquery.setName(filter_name);
			filter = service.retrieveObject(CfgFilter.class,	filterquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return filter;
	}
	
	/**
	 * filter를 조회한다. 
	 * @param service
	 * @param tenant_dbid
	 * @param filter_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgFilter getFilter(
			final IConfService service,
			int tenant_dbid,
			int filter_dbid
			) {
		CfgFilter filter = null;
		try {
			CfgFilterQuery filterquery = new CfgFilterQuery();
			filterquery.setTenantDbid(tenant_dbid);
			filterquery.setDbid(filter_dbid);
			filter = service.retrieveObject(CfgFilter.class,	filterquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return filter;
	}

	/**
	 * Filter을 조회한다.
	 * @param iTenantDBID
	 * @param service
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public Collection<CfgFilter> getFilter(
			final IConfService service,
			int tenant_dbid
			) {
		Collection<CfgFilter> filter = null;
		try {
			CfgFilterQuery filterquery = new CfgFilterQuery();
			filterquery.setTenantDbid(tenant_dbid);
			filter = service.retrieveMultipleObjects(CfgFilter.class,	filterquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return filter;
	}



































	
	/**
	 * CallingList을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param calinglist_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public int deleteCallingList(
			final IConfService service,
			int tenant_dbid,
			String calinglist_name
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		int returnval = 0;
		CfgCallingList callinglist = getCallingList(service, tenant_dbid, calinglist_name);
		if(callinglist != null) {
			callinglist.delete();
            returnval = 1;
		}
		
		return returnval;
	}

	/**
	 * CallingList을 삭제한다.
	 * @param service
	 * @param dn
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteCallingList(
			final IConfService service,
			CfgCallingList callinglist
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			callinglist.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	

	

	/**
	 * CallingList을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param calinglist_name
	 * @param table_name
	 * @param filter_name
	 * @param maximum
	 * @param treatment_names
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgCallingList modifyCallingList(
			final IConfService service,
			int tenant_dbid,
			String calinglist_name,
			String table_name,
			String filter_name,
			int maximum,
			String treatment_names
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		String[] arr_treatment = treatment_names.split(",");
		Collection<CfgTreatment> treatments = new HashSet<CfgTreatment>();
		CfgCallingList callinglist = getCallingList(service, tenant_dbid, calinglist_name);
		ob = new ConfigOB();
		CfgTreatment tmptreatment = null;
		if(arr_treatment.length > 0) {
			for(int i=0; i<arr_treatment.length; i++) {
				tmptreatment = null;
				tmptreatment = ob.getTreatment(service, tenant_dbid, arr_treatment[i]);
				treatments.add(tmptreatment);
			}
		}
		CfgTableAccess ta = getTableAccess(service, tenant_dbid, table_name);
		callinglist.setTableAccess(ta);
		callinglist.setTreatments(treatments);
		callinglist.setMaxAttempts(maximum);
		callinglist.save();
		
		return callinglist;
	}
	

	/**
	 * CallingList을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param calinglist_name
	 * @param table_name
	 * @param filter_name
	 * @param maximum
	 * @param treatment_names
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgCallingList createCallingList(
			final IConfService service,
			int tenant_dbid,
			String calinglist_name,
			String table_name,
			String filter_name,
			int maximum,
			String treatment_names
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		String[] arr_treatment = treatment_names.split(",");
		Collection<CfgTreatment> treatments = new HashSet<CfgTreatment>();
		CfgCallingList callinglist = new CfgCallingList(service);
		callinglist.setTenantDBID(tenant_dbid);
		callinglist.setName(calinglist_name);
		ob = new ConfigOB();
		CfgTreatment tmptreatment = null;
		if(arr_treatment.length > 0) {
			for(int i=0; i<arr_treatment.length; i++) {
				tmptreatment = null;
				tmptreatment = ob.getTreatment(service, tenant_dbid, arr_treatment[i]);
				treatments.add(tmptreatment);
			}
		}
		callinglist.setName(calinglist_name);
		CfgTableAccess ta = getTableAccess(service, tenant_dbid, table_name);
		callinglist.setTableAccess(ta);
		callinglist.setTreatments(treatments);
		//callinglist.setFilterDBID(filter.getDBID());
		callinglist.setMaxAttempts(maximum);
		callinglist.save();
		
		return callinglist;
	}
	

	/**
	 * callinglist를 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param CallingListInfoVo
	 * @return
	 */
	public CfgCallingList createCallingList(
			final IConfService service, 
			int tenant_dbid, 
			CallingListInfoVo callinglistvo) {
		CfgCallingList callinglist = null;
		try{
			// Read configuration objects:
			callinglist = new CfgCallingList(service);
			callinglist.setTenantDBID(tenant_dbid);	//Tenant DBID
			callinglist.setDescription(callinglistvo.getDescription());
			callinglist.setFilterDBID(callinglistvo.getFilter_dbid());
			callinglist.setLogTableAccessDBID(callinglistvo.getLog_table_access_dbid());
			callinglist.setMaxAttempts(callinglistvo.getMax_attampt());
			callinglist.setName(callinglistvo.getName());
			callinglist.setScriptDBID(callinglistvo.getScript_dbid());
			callinglist.setTableAccessDBID(callinglistvo.getTable_access_dbid());
			callinglist.setTimeFrom(callinglistvo.getCalling_time_from());
			callinglist.setTimeUntil(callinglistvo.getCalling_time_to());

			if(callinglistvo.getState() == 1){		//State
				callinglist.setState(CfgObjectState.CFGEnabled);
			}else{
				callinglist.setState(CfgObjectState.CFGDisabled);
			}

			if(callinglistvo.getFolderId() > 0) {	// Folder DBID
				callinglist.setFolderId(callinglistvo.getFolderId());
			}

			callinglist.save();

			if(callinglist != null) {
				JsonArray treatmentarry = null;
				
				if(callinglistvo.getTreatments() != null){
					Collection<Integer> treatment_dbids = new HashSet<Integer>();
					treatmentarry = callinglistvo.getTreatments().getAsJsonArray();
					for(int i=0; i<treatmentarry.size(); i++){
						JsonObject ob =  treatmentarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						treatment_dbids.add(dbid);
					}
					callinglist.setTreatmentDBIDs(treatment_dbids);
				}
				JsonArray psarry = null;
				if(callinglistvo.getAnnex() != null){
					psarry = callinglistvo.getAnnex().getAsJsonArray();
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
					callinglist.setUserProperties(obj_kv);
				}

				callinglist.save();
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return callinglist;
	}

		/**
	 * callinglist를 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param CallingListInfoVo
	 * @return
	 */
	public CfgCallingList modifyCallingList(
			final IConfService service, 
			int tenant_dbid, 
			CfgCallingList callinglist,
			CallingListInfoVo callinglistvo) {
		try{
			callinglist.setDescription(callinglistvo.getDescription());
			callinglist.setFilterDBID(callinglistvo.getFilter_dbid());
			callinglist.setLogTableAccessDBID(callinglistvo.getLog_table_access_dbid());
			callinglist.setMaxAttempts(callinglistvo.getMax_attampt());
			callinglist.setName(callinglistvo.getName());
			callinglist.setScriptDBID(callinglistvo.getScript_dbid());
			callinglist.setTableAccessDBID(callinglistvo.getTable_access_dbid());
			callinglist.setTimeFrom(callinglistvo.getCalling_time_from());
			callinglist.setTimeUntil(callinglistvo.getCalling_time_to());

			if(callinglistvo.getState() == 1){		//State
				callinglist.setState(CfgObjectState.CFGEnabled);
			}else{
				callinglist.setState(CfgObjectState.CFGDisabled);
			}

			if(callinglistvo.getFolderId() > 0) {	// Folder DBID
				callinglist.setFolderId(callinglistvo.getFolderId());
			}

			callinglist.save();

			if(callinglist != null) {
				JsonArray treatmentarry = null;
				
				if(callinglistvo.getTreatments() != null){
					Collection<Integer> treatment_dbids = new HashSet<Integer>();
					treatmentarry = callinglistvo.getTreatments().getAsJsonArray();
					for(int i=0; i<treatmentarry.size(); i++){
						JsonObject ob =  treatmentarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						treatment_dbids.add(dbid);
					}
					callinglist.setTreatmentDBIDs(treatment_dbids);
				}
				JsonArray psarry = null;
				if(callinglistvo.getAnnex() != null){
					psarry = callinglistvo.getAnnex().getAsJsonArray();
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
					callinglist.setUserProperties(obj_kv);
				}

				callinglist.save();
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return callinglist;
	}

	/**
	 * Filter을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param filter_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public int deleteFilter(
			final IConfService service,
			int tenant_dbid,
			String filter_name
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		int returnval = 0;
		CfgFilter filter = getFilter(service, tenant_dbid, filter_name);
		if(filter != null) {
			filter.delete();
            returnval = 1;
		}
		
		return returnval;
	}

	/**
	 * filter을 삭제한다.
	 * @param service
	 * @param filter
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteFilter(
			final IConfService service,
			CfgFilter filter
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			filter.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	

	/**
	 * Filter을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param filter_dbid
	 * @param filter_name
	 * @param desc
	 * @param format_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgFilter modifyFilter(
			final IConfService service,
			int tenant_dbid,
			int filter_dbid,
			String filter_name,
			String desc,
			int format_dbid
			)
					throws ConfigException, InterruptedException {
		CfgFilter filter = getFilter(service, tenant_dbid, filter_dbid);
		if(filter != null){
			filter.setFormatDBID(format_dbid);
			filter.setName(filter_name);
			filter.setDescription(desc);
			filter.save();
		}
		return filter;
	}

	/**
	 * Filter를 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param filter_name
	 * @param desc
	 * @param format_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgFilter createFilter(
			final IConfService service,
			int tenant_dbid,
			String filter_name,
			String desc,
			int format_dbid
			)
					throws ConfigException, InterruptedException {
		CfgFilter filter = new CfgFilter(service);
		filter.setFormatDBID(format_dbid);
		filter.setName(filter_name);
		filter.setDescription(desc);
		filter.save();
		
		return filter;
	}

	/**
	 * Filter를 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param FilterInfoVo
	 * @return
	 */
	public CfgFilter createFilter(
			final IConfService service, 
			int tenant_dbid, 
			FilterInfoVo filtervo) {
		CfgFilter filter = null;
		try{
			filter = new CfgFilter(service);
			filter.setName(filtervo.getName());
			filter.setDescription(filtervo.getDescription());
			filter.setFormatDBID(filtervo.getFormat_dbid());
			
			if(filtervo.getState() == 1){		//State
				filter.setState(CfgObjectState.CFGEnabled);
			}else{
				filter.setState(CfgObjectState.CFGDisabled);
			}

			if(filtervo.getFolderId() > 0) {	// Folder DBID
				filter.setFolderId(filtervo.getFolderId());
			}

			filter.save();

			if(filter != null) {
				JsonArray psarry = null;
				if(filtervo.getAnnex() != null){
					psarry = filtervo.getAnnex().getAsJsonArray();
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
					filter.setUserProperties(obj_kv);
				}

				filter.save();
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return filter;
	}

		/**
	 * filter를 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param FilterInfoVo
	 * @return
	 */
	public CfgFilter modifyFilter(
			final IConfService service, 
			int tenant_dbid, 
			CfgFilter filter,
			FilterInfoVo filtervo) {
		try{
			filter.setName(filtervo.getName());
			filter.setDescription(filtervo.getDescription());
			filter.setFormatDBID(filtervo.getFormat_dbid());
			
			if(filtervo.getState() == 1){		//State
				filter.setState(CfgObjectState.CFGEnabled);
			}else{
				filter.setState(CfgObjectState.CFGDisabled);
			}

			if(filtervo.getFolderId() > 0) {	// Folder DBID
				filter.setFolderId(filtervo.getFolderId());
			}

			filter.save();

			if(filter != null) {
				JsonArray psarry = null;
				if(filtervo.getAnnex() != null){
					psarry = filtervo.getAnnex().getAsJsonArray();
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
					filter.setUserProperties(obj_kv);
				}

				filter.save();
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return filter;
	}

	/**
	 * callinglistinfo Collection을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param cl	콜링리스트
	 * @return
	 */
	public Collection<CfgCallingListInfo> createCallingListInfo(final IConfService service,
			int tenant_dbid,
			CfgCallingList cl
			) {
		Collection<CfgCallingListInfo> cmp = null;
		CfgCallingListInfo newcli = null;

		try {
			newcli = new CfgCallingListInfo(service, null);
			cmp = new HashSet<CfgCallingListInfo>();
			newcli.setIsActive(CfgFlag.CFGTrue);
			newcli.setShare(10);
			newcli.setCallingList(cl);
			cmp.add(newcli);
		} catch (Exception e) {
			logger.error("ConfigException", e);
		}
		return cmp;
	}
	
	/**
	 * TableAccess을 조회한다. 
	 * @param service
	 * @param tenant_dbid
	 * @param ta_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgTableAccess getTableAccess(
			final IConfService service,
			int tenant_dbid,
			String ta_name
			) {
		CfgTableAccess tableaccess = null;
		try {
			CfgTableAccessQuery taquery = new CfgTableAccessQuery();
			taquery.setTenantDbid(tenant_dbid);
			taquery.setName(ta_name);
			tableaccess = service.retrieveObject(CfgTableAccess.class,	taquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return tableaccess;
	}
}
