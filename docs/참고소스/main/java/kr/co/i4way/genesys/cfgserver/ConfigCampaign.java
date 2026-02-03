package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.i4way.genesys.model.CampaignInfoVo;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingList;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCallingListInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaign;
import com.genesyslab.platform.applicationblocks.com.queries.CfgCampaignQuery;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigCampaign {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	public ConfigCampaign(){
	}
	
	/**
	 * Campaign을 조회한다. 
	 * @param service
	 * @param tenant_dbid
	 * @param campaign_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgCampaign getCampaign(
			final IConfService service,
			int tenant_dbid,
			String campaign_name
			) {
		CfgCampaign campaign = null;
		try {
			CfgCampaignQuery campaignquery = new CfgCampaignQuery();
			campaignquery.setTenantDbid(tenant_dbid);
			campaignquery.setName(campaign_name);
			campaign = service.retrieveObject(CfgCampaign.class, campaignquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return campaign;
	}
	
	/**
	 * Campaign을 조회한다. 
	 * @param service
	 * @param tenant_dbid
	 * @param campaign_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgCampaign getCampaign(
			final IConfService service,
			int tenant_dbid,
			int campaign_dbid
			) {
		CfgCampaign campaign = null;
		try {
			CfgCampaignQuery campaignquery = new CfgCampaignQuery();
			campaignquery.setTenantDbid(tenant_dbid);
			campaignquery.setDbid(campaign_dbid);
			campaign = service.retrieveObject(CfgCampaign.class, campaignquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return campaign;
	}

	/**
	 * Campaign을 조회한다.
	 * @param iTenantDBID
	 * @param service
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public Collection<CfgCampaign> getCampaign(
			final IConfService service,
			int tenant_dbid
			) {
		Collection<CfgCampaign> campaigns = null;
		try {
			CfgCampaignQuery campaignquery = new CfgCampaignQuery();
			campaignquery.setTenantDbid(tenant_dbid);
			campaigns = service.retrieveMultipleObjects(CfgCampaign.class,	campaignquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return campaigns;
	}

	/**
	 * Campaign을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param campaign_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public int deleteCampaign(
			final IConfService service,
			int tenant_dbid,
			String campaign_name
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		int returnval = 0;
		CfgCampaign campaign = getCampaign(service, tenant_dbid, campaign_name);
		if(campaign != null) {
			campaign.delete();
            returnval = 1;
		}
		
		return returnval;
	}

	/**
	 * Campaign을 삭제한다.
	 * @param service
	 * @param dn
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteCampaign(
			final IConfService service,
			CfgCampaign campaign
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			campaign.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	

	/**
	 * Campaign을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param campaign_name
	 * @param description
	 * @param script_dbid
	 * @param callinglist
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgCampaign modifyCampaign(
			final IConfService service,
			int tenant_dbid,
			String campaign_name,
			String description,
			int script_dbid,
			String callinglist_names
			)
					throws ConfigException, InterruptedException {
		String[] arr_callinglist = callinglist_names.split(",");
		Collection<CfgCallingListInfo> callinglistinfos = new HashSet<CfgCallingListInfo>();
		CfgCampaign campaign = getCampaign(service, tenant_dbid, campaign_name);
		ConfigCallingList configCallingList = new ConfigCallingList();
		CfgCallingList callinglist = null;
		CfgCallingListInfo callinglistinfo = null;
		if(arr_callinglist.length > 0) {
			for(int i=0; i<arr_callinglist.length; i++) {
				callinglist = null;
				callinglistinfo = new CfgCallingListInfo(service, callinglist);
				callinglist = configCallingList.getCallingList(service, tenant_dbid, arr_callinglist[i]);
				callinglistinfo.setCallingList(callinglist);
				callinglistinfo.setIsActive(CfgFlag.CFGTrue);
				callinglistinfo.setShare(10);
				callinglistinfos.add(callinglistinfo);
			}
		}
		
		campaign.setName(campaign_name);
		campaign.setDescription(description);
		campaign.setScriptDBID(script_dbid);
		campaign.setCallingLists(callinglistinfos);
		callinglist.save();
		
		return campaign;
	}	

	/**
	 * CallingList을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param campaign_name
	 * @param description
	 * @param script_dbid
	 * @param callinglist_names
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgCampaign createCampaign(
			final IConfService service,
			int tenant_dbid,
			String campaign_name,
			String description,
			int script_dbid,
			String callinglist_names
			)
					throws ConfigException, InterruptedException {

		String[] arr_callinglist = callinglist_names.split(",");
		Collection<CfgCallingListInfo> callinglistinfos = new HashSet<CfgCallingListInfo>();
		CfgCampaign campaign = new CfgCampaign(service);
		ConfigCallingList configCallingList = new ConfigCallingList();
		CfgCallingList callinglist = null;
		CfgCallingListInfo callinglistinfo = null;
		if(arr_callinglist.length > 0) {
			for(int i=0; i<arr_callinglist.length; i++) {
				callinglist = null;
				callinglistinfo = new CfgCallingListInfo(service, callinglist);
				callinglist = configCallingList.getCallingList(service, tenant_dbid, arr_callinglist[i]);
				callinglistinfo.setCallingList(callinglist);
				callinglistinfo.setIsActive(CfgFlag.CFGTrue);
				callinglistinfo.setShare(10);
				callinglistinfos.add(callinglistinfo);
			}
		}
		campaign.setTenantDBID(tenant_dbid);
		campaign.setName(campaign_name);
		campaign.setDescription(description);
		campaign.setScriptDBID(script_dbid);
		campaign.setCallingLists(callinglistinfos);
		callinglist.save();

		return campaign;
	}

	/**
	 * campaign를 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param CampaignInfoVo
	 * @return
	 */
	public CfgCampaign createCampaign(
			final IConfService service, 
			int tenant_dbid, 
			CampaignInfoVo campaignvo) {
		CfgCampaign campaign = null;
		try{
			// Read configuration objects:
			campaign = new CfgCampaign(service);
			campaign.setTenantDBID(tenant_dbid);	//Tenant DBID
			campaign.setDescription(campaignvo.getDescription());
			campaign.setScriptDBID(campaignvo.getScript_dbid());

			if(campaignvo.getState() == 1){		//State
				campaign.setState(CfgObjectState.CFGEnabled);
			}else{
				campaign.setState(CfgObjectState.CFGDisabled);
			}

			if(campaignvo.getFolderId() > 0) {	// Folder DBID
				campaign.setFolderId(campaignvo.getFolderId());
			}

			campaign.save();

			if(campaign != null) {
				JsonArray callinglistarry = null;
				
				if(campaignvo.getCallinglist() != null){
					ConfigCallingList configCallingList = new ConfigCallingList();
					CfgCallingListInfo callinglistinfo = null;
					Collection<CfgCallingListInfo> callinglistinfos = new HashSet<CfgCallingListInfo>();
					CfgCallingList callinglist = null;
					callinglistarry = campaignvo.getCallinglist().getAsJsonArray();
					for(int i=0; i<callinglistarry.size(); i++){
						callinglistinfo = new CfgCallingListInfo(service, callinglist);
						JsonObject ob =  callinglistarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						callinglist = configCallingList.getCallingList(service, tenant_dbid, dbid);
						callinglistinfo.setCallingList(callinglist);
						callinglistinfo.setIsActive(CfgFlag.CFGTrue);
						callinglistinfo.setShare(10);
						callinglistinfos.add(callinglistinfo);
					}
					campaign.setCallingLists(callinglistinfos);
				}
				JsonArray psarry = null;
				if(campaignvo.getAnnex() != null){
					psarry = campaignvo.getAnnex().getAsJsonArray();
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
					campaign.setUserProperties(obj_kv);
				}

				campaign.save();
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return campaign;
	}

	/**
	 * campaign를 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param campaign
	 * @param CampaignInfoVo
	 * @return
	 */
	public CfgCampaign modifyCampaign(
			final IConfService service, 
			int tenant_dbid, 
			CfgCampaign campaign,
			CampaignInfoVo campaignvo) {
		try{
			campaign.setTenantDBID(tenant_dbid);	//Tenant DBID
			campaign.setDescription(campaignvo.getDescription());
			campaign.setScriptDBID(campaignvo.getScript_dbid());

			if(campaignvo.getState() == 1){		//State
				campaign.setState(CfgObjectState.CFGEnabled);
			}else{
				campaign.setState(CfgObjectState.CFGDisabled);
			}

			if(campaignvo.getFolderId() > 0) {	// Folder DBID
				campaign.setFolderId(campaignvo.getFolderId());
			}

			campaign.save();

			if(campaign != null) {
				JsonArray callinglistarry = null;
				
				if(campaignvo.getCallinglist() != null){
					ConfigCallingList configCallingList = new ConfigCallingList();
					CfgCallingListInfo callinglistinfo = null;
					Collection<CfgCallingListInfo> callinglistinfos = new HashSet<CfgCallingListInfo>();
					CfgCallingList callinglist = null;
					callinglistarry = campaignvo.getCallinglist().getAsJsonArray();
					for(int i=0; i<callinglistarry.size(); i++){
						callinglistinfo = new CfgCallingListInfo(service, callinglist);
						JsonObject ob =  callinglistarry.get(i).getAsJsonObject();
						int dbid = ob.get("dbid").getAsInt();
						callinglist = configCallingList.getCallingList(service, tenant_dbid, dbid);
						callinglistinfo.setCallingList(callinglist);
						callinglistinfo.setIsActive(CfgFlag.CFGTrue);
						callinglistinfo.setShare(10);
						callinglistinfos.add(callinglistinfo);
					}
					campaign.setCallingLists(callinglistinfos);
				}
				JsonArray psarry = null;
				if(campaignvo.getAnnex() != null){
					psarry = campaignvo.getAnnex().getAsJsonArray();
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
					campaign.setUserProperties(obj_kv);
				}

				campaign.save();
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return campaign;
	}

}
