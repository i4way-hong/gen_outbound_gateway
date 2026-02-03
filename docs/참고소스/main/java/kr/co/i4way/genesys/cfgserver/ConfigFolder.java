package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.i4way.genesys.model.FolderInfoVo;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDN;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDNGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.objects.CfgGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgObjectID;
import com.genesyslab.platform.applicationblocks.com.objects.CfgOwnerID;
import com.genesyslab.platform.applicationblocks.com.queries.CfgDNGroupQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgDNQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgFolderQuery;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.configuration.protocol.types.CfgDNGroupType;
import com.genesyslab.platform.configuration.protocol.types.CfgDNType;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.types.CfgRouteType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigFolder {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	public ConfigFolder() {
		
	}
		
	/**
	 * CfgFolder을 쿼리한다.
	 * @param service
	 * @param owner_type
	 * @param folder_dbid
	 * @return
	 */
	public CfgFolder getFolderInfo(final IConfService service, int owner_type, int folder_dbid) {
		CfgFolder rtnfolder = null;
		CfgFolderQuery folderquery = new CfgFolderQuery();
		folderquery.setOwnerType(owner_type);
		folderquery.setDbid(folder_dbid);
		try {
			rtnfolder = service.retrieveObject(CfgFolder.class, folderquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnfolder;
	}

	/**
	 * Folder를 이동한다 
	 * @param service
	 * @param src_folder_dbid
	 * @param tgt_folder_dbid
	 * @return
	 */
	public CfgFolder moveFolder(final IConfService service, JsonElement moveObj, int tgt_folder_dbid) {
		CfgFolder tgtfolder = null;
		CfgFolderQuery folderquery = new CfgFolderQuery();
		folderquery.setOwnerType(22);	//22:CfgFolder
		folderquery.setDbid(tgt_folder_dbid);
		try {
			tgtfolder = service.retrieveObject(CfgFolder.class, folderquery);
			Collection<CfgObjectID> objids = tgtfolder.getObjectIDs();
			CfgObjectID cfgobjectid = null;

			JsonArray psarry = null;
			if(moveObj != null){
				psarry = moveObj.getAsJsonArray();

				for(int i=0; i<psarry.size(); i++){
					cfgobjectid = new CfgObjectID(service, tgtfolder);
					JsonObject ob =  psarry.get(i).getAsJsonObject();
					int dbid = ob.get("src_dbid").getAsInt();
					String obj_type = ob.get("src_obj").getAsString();
					cfgobjectid.setDBID(dbid);
					if(obj_type.equals(("CfgFolder"))){
						cfgobjectid.setType(CfgObjectType.CFGFolder);
					}else if(obj_type.equals(("CfgPerson"))){
						cfgobjectid.setType(CfgObjectType.CFGPerson);
					}else if(obj_type.equals(("CfgSkill"))){
						cfgobjectid.setType(CfgObjectType.CFGSkill);
					}else if(obj_type.equals(("CfgAgentLogin"))){
						cfgobjectid.setType(CfgObjectType.CFGAgentLogin);
					}else if(obj_type.equals(("CfgAgentGroup"))){
						cfgobjectid.setType(CfgObjectType.CFGAgentGroup);
					}else if(obj_type.equals(("CfgDn"))){
						cfgobjectid.setType(CfgObjectType.CFGDN);
					}
					objids.add(cfgobjectid);
				}
				tgtfolder.setObjectIDs(objids);
				tgtfolder.save();
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return tgtfolder;
	}

	/**
	 * Folder를 생성한다
	 * @param service
	 * @param src_folder_dbid
	 * @param tgt_folder_dbid
	 * @return
	 */
	public CfgFolder createFolder(final IConfService service, FolderInfoVo folderinfovo) {
		CfgFolder folder = null;
		try {
			folder = new CfgFolder(service);
			folder.setName(folderinfovo.getName());
			folder.setFolderId(folderinfovo.getFolderId());
			folder.setType(CfgObjectType.valueOf(folderinfovo.getType()));
			final CfgOwnerID ownerid = new CfgOwnerID(service, null);
			ownerid.setDBID(folderinfovo.getOwnerDbid());
			ownerid.setType(CfgObjectType.valueOf(folderinfovo.getOwnerType()));
			folder.setOwnerID(ownerid);
			folder.save();

			if(folder != null) {
				JsonArray psarry = null;
				if(folderinfovo.getAnnex() != null){
					psarry = folderinfovo.getAnnex().getAsJsonArray();
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
					folder.setUserProperties(obj_kv);
				}
				folder.save();
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return folder;
	}

	/**
	 * Folder를 수정한다
	 * @param service
	 * @param src_folder_dbid
	 * @param tgt_folder_dbid
	 * @return
	 */
	public CfgFolder modifyFolder(final IConfService service, CfgFolder folder, FolderInfoVo folderinfovo) {
		try {
			folder.setName(folderinfovo.getName());
			folder.save();
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return folder;
	}

	/**
	 * Folder를 삭제한다
	 * @param service
	 * @param CfgFolder
	 * @return
	 */
	public boolean deleteFolder(
			final IConfService service,
			CfgFolder folder
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			folder.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	

	/**
	 * folder의 상태를 변경한다. 
	 * @param service
	 * @param folder 	상태 변경할 folder 객체
	 * @param flag 		(true = Enabled, false = Disabled)
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgFolder setFolderState(
			final IConfService service,
			CfgFolder folder,
			boolean flag
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		if(flag){
			folder.setState(CfgObjectState.CFGDisabled);
		}else{
			folder.setState(CfgObjectState.CFGEnabled);
		}
		folder.save();
		return folder;
	}
		
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
	public CfgDN createDN(
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
	}
	
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
	public boolean deleteDN(
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
	}	
	

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
