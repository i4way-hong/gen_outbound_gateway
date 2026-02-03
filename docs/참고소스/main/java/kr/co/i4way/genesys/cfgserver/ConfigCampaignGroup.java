package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaignGroup;
import com.genesyslab.platform.applicationblocks.com.queries.CfgCampaignGroupQuery;

public class ConfigCampaignGroup {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	ConfigPerson ps = null;
	ConfigAgentGroup ag = null;

	public ConfigCampaignGroup(){
	}

// 	/**
// 	 * CampaignGroup을 조회한다. 
// 	 * @param service
// 	 * @param tenant_dbid
// 	 * @param group_name
// 	 * @return
// 	 * @throws ConfigException
// 	 * @throws InterruptedException
// 	 */
// 	public CfgCampaignGroup getCampaignGroup(
// 			final IConfService service,
// 			int tenant_dbid,
// 			String group_name
// 			) {
// 		CfgCampaignGroup campaigngroup = null;
// 		try {
// 			CfgCampaignGroupQuery campaigngroupquery = new CfgCampaignGroupQuery();
// 			campaigngroupquery.setTenantDbid(tenant_dbid);
// 			campaigngroupquery.setName(group_name);
// 			campaigngroup = service.retrieveObject(CfgCampaignGroup.class,	campaigngroupquery);
// 		} catch (ConfigException e) {
// 			logger.error("ConfigException", e);
// 		}
// 		return campaigngroup;
// 	}

// 	/**
// 	 * CampaignGroup을 조회한다. 
// 	 * @param service
// 	 * @param tenant_dbid
// 	 * @param group_dbid
// 	 * @return
// 	 * @throws ConfigException
// 	 * @throws InterruptedException
// 	 */
// 	public CfgCampaignGroup getCampaignGroup(
// 			final IConfService service,
// 			int tenant_dbid,
// 			int group_dbid
// 			) {
// 		CfgCampaignGroup campaigngroup = null;
// 		try {
// 			CfgCampaignGroupQuery campaigngroupquery = new CfgCampaignGroupQuery();
// 			campaigngroupquery.setTenantDbid(tenant_dbid);
// 			campaigngroupquery.setDbid(group_dbid);
// 			campaigngroup = service.retrieveObject(CfgCampaignGroup.class,	campaigngroupquery);
// 		} catch (ConfigException e) {
// 			logger.error("ConfigException", e);
// 		}
// 		return campaigngroup;
// 	}

// 	/**
// 	 * CampaignGroup을 조회한다.
// 	 * @param iTenantDBID
// 	 * @param service
// 	 * @return
// 	 * @throws ConfigException
// 	 * @throws InterruptedException
// 	 */
// 	public Collection<CfgCampaignGroup> getCampaignGroup(
// 			final IConfService service,
// 			int tenant_dbid
// 			) {
// 		Collection<CfgCampaignGroup> campaigngroup = null;
// 		try {
// 			CfgCampaignGroupQuery campaigngroupquery = new CfgCampaignGroupQuery();
// 			campaigngroupquery.setTenantDbid(tenant_dbid);
// 			campaigngroup = service.retrieveMultipleObjects(CfgCampaignGroup.class,
// 					campaigngroupquery);

// 		} catch (ConfigException e) {
// 			logger.error("ConfigException", e);
// 		}catch (InterruptedException e) {
// 			logger.error("InterruptedException", e);
// 		}
// 		return campaigngroup;
// 	}
	
// //	/**
// //	 * CampaignGroup을 조회한다. 
// //	 * @param service
// //	 * @param tenant_dbid
// //	 * @param group_name
// //	 * @return
// //	 * @throws ConfigException
// //	 * @throws InterruptedException
// //	 */
// //	public Collection<CfgCampaignGroupInfo> getCampaignGroupInfo(
// //			final IConfService service,
// //			int tenant_dbid,
// //			String group_name
// //			) {
// //		Collection<CfgCampaignGroupInfo> campaigngroup = null;
// //		try {
// //			CfgCampaignGroupInfo bbb = null;
// //			CfgCampaignGroup aa = null;
// //			aa.
// ////			CfgCamp
// ////			
// ////			CfgCampaignGroupQuery campaigngroupquery = new CfgCampaignGroupQuery();
// ////			campaigngroupquery.setTenantDbid(tenant_dbid);
// ////			campaigngroupquery.setName(group_name);
// //			campaigngroup = service.retrieveMultipleObjects(CfgCampaignGroupInfo.class);
// //		} catch (ConfigException e) {
// //			logger.error("ConfigException", e);
// //		}catch (InterruptedException e) {
// //			logger.error("InterruptedException", e);
// //		}
// //		return campaigngroup;
// //	}
	
	

// 	/**
// 	 * 캠페인 그룹을 생성한다.
// 	 * @param service
// 	 * @param tenant_dbid
// 	 * @param group_name
// 	 * @param campaign_name
// 	 * @param obj_type(CFGAgentGroup, CFGPlaceGroup)
// 	 * @param dialmode(CFGDMPower, CFGDMPredict, CFGDMPreview, CFGDMProgress)
// 	 * @param opmode(CFGOMMaxOperationMode, CFGOMNoOperationMode, CFGOMSchedule)
// 	 * @param optmode(CFGOMBusyFactor, CFGOMDistributionTime, CFGOMMaximumGain, CFGOMMaxOptimizationMethod, CFGOMNoOptimizationMethod, CFGOMOverdialRate, CFGOMWaitTime)
// 	 * @return
// 	 */
// 	public CfgCampaignGroup createCampaignGroup(final IConfService service,
// 			int tenant_dbid,
// 			String group_name,
// 			String obj_type,
// 			CfgCampaign cm,
// 			String dialmode,
// 			String opmode,
// 			String optmode,
// 			int ocs_dbid,
// 			int min_rec_buff_size,
// 			int rec_buff_size,
// 			int num_channel,
// 			int method_value
// 			) {
// 		CfgCampaignGroup cmp = new CfgCampaignGroup(service);
// 		ag = new ConfigAgentGroup();

// 		try {
// 			CfgAgentGroup agentgroup = ag.getAgentGroupInfo(service, tenant_dbid, group_name);
// 			Collection<Integer> server = new HashSet<Integer>();
// 			if(agentgroup != null) {
// 				cmp.setName(cm.getName() + "@" + group_name);
// 				cmp.setCampaignDBID(cm.getDBID());
// 				cmp.setGroupDBID(agentgroup.getDBID());
// 				cmp.setGroupType(CfgObjectType.valueOf(obj_type));
// 				cmp.setDialMode(CfgDialMode.valueOf(dialmode));
// 				cmp.setOperationMode(CfgOperationMode.valueOf(opmode));
// 				cmp.setOptMethod(CfgOptimizationMethod.valueOf(optmode));
// 				server.add(ocs_dbid);
// 				cmp.setServerDBIDs(server);
// 				cmp.setMinRecBuffSize(min_rec_buff_size);
// 				cmp.setOptRecBuffSize(rec_buff_size);
// 				cmp.setNumOfChannels(num_channel);
// 				cmp.setOptMethodValue(method_value);
// 				cmp.save();
// 			}else {
// 				logger.info("agentGroup is null");
// 			}
// 		} catch (ConfigException e) {
// 			logger.error("ConfigException", e);
// 		}
// 		return cmp;
// 	}
	
// 	/**
// 	 * 캠페인 그룹을 수정한다.
// 	 * @param service
// 	 * @param tenant_dbid
// 	 * @param group_name
// 	 * @param campaign_name
// 	 * @param obj_type(CFGAgentGroup, CFGPlaceGroup)
// 	 * @param dialmode(CFGDMPower, CFGDMPredict, CFGDMPreview, CFGDMProgress)
// 	 * @param opmode(CFGOMMaxOperationMode, CFGOMNoOperationMode, CFGOMSchedule)
// 	 * @param optmode(CFGOMBusyFactor, CFGOMDistributionTime, CFGOMMaximumGain, CFGOMMaxOptimizationMethod, CFGOMNoOptimizationMethod, CFGOMOverdialRate, CFGOMWaitTime)
// 	 * @return
// 	 */
// 	public CfgCampaignGroup modifyCampaignGroup(final IConfService service,
// 			int tenant_dbid,
// 			CfgCampaignGroup campaigngroup,
// 			String group_name,
// 			String obj_type,
// 			CfgCampaign cm,
// 			String dialmode,
// 			String opmode,
// 			String optmode,
// 			int ocs_dbid,
// 			int min_rec_buff_size,
// 			int rec_buff_size,
// 			int num_channel,
// 			int method_value
// 			) {
// 		CfgAgentGroup agentgroup = null;
// 		ag = new ConfigAgentGroup();

// 		try {
// 			agentgroup = ag.getAgentGroupInfo(service, tenant_dbid, group_name);
// 			Collection<Integer> server = new HashSet<Integer>();
// 			if(agentgroup != null) {
// 				campaigngroup.setName(cm.getName() + "@" + group_name);
// 				campaigngroup.setGroupDBID(agentgroup.getDBID());
// 				campaigngroup.setGroupType(CfgObjectType.valueOf(obj_type));
// 				campaigngroup.setDialMode(CfgDialMode.valueOf(dialmode));
// 				campaigngroup.setOperationMode(CfgOperationMode.valueOf(opmode));
// 				campaigngroup.setOptMethod(CfgOptimizationMethod.valueOf(optmode));
// 				server.add(ocs_dbid);
// 				campaigngroup.setServerDBIDs(server);
// 				campaigngroup.setMinRecBuffSize(min_rec_buff_size);
// 				campaigngroup.setOptRecBuffSize(rec_buff_size);
// 				campaigngroup.setNumOfChannels(num_channel);
// 				campaigngroup.setOptMethodValue(method_value);
// 				campaigngroup.save();
// 			}else {
// 				logger.info("agentGroup is null");
// 			}
// 		} catch (ConfigException e) {
// 			logger.error("ConfigException", e);
// 		}
// 		return campaigngroup;
// 	}
























































	/**
	 * CampaignGroup을 조회한다. 
	 * @param service
	 * @param tenant_dbid
	 * @param campaign_group_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgCampaignGroup getCampaignGroup(
			final IConfService service,
			int tenant_dbid,
			String campaign_group_name
			) 
	{
				CfgCampaignGroup campaigngroup = null;
		try {
			CfgCampaignGroupQuery campaigngroupquery = new CfgCampaignGroupQuery();
			campaigngroupquery.setTenantDbid(tenant_dbid);
			campaigngroupquery.setName(campaign_group_name);
			campaigngroup = service.retrieveObject(CfgCampaignGroup.class, campaigngroupquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return campaigngroup;
	}
	
	/**
	 * CampaignGroup을 조회한다. 
	 * @param service
	 * @param tenant_dbid
	 * @param campaign_group_dbid
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgCampaignGroup getCampaignGroup(
			final IConfService service,
			int tenant_dbid,
			int campaign_group_dbid
			) 
	{
				CfgCampaignGroup campaigngroup = null;
		try {
			CfgCampaignGroupQuery campaigngroupquery = new CfgCampaignGroupQuery();
			campaigngroupquery.setTenantDbid(tenant_dbid);
			campaigngroupquery.setDbid(campaign_group_dbid);
			campaigngroup = service.retrieveObject(CfgCampaignGroup.class, campaigngroupquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return campaigngroup;
	}

	/**
	 * CampaignGroup을 조회한다.
	 * @param iTenantDBID
	 * @param service
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public Collection<CfgCampaignGroup> getCampaignGroup(
			final IConfService service,
			int tenant_dbid
			) {
		Collection<CfgCampaignGroup> campaigngroups = null;
		try {
			CfgCampaignGroupQuery campaigngroupquery = new CfgCampaignGroupQuery();
			campaigngroupquery.setTenantDbid(tenant_dbid);
			campaigngroups = service.retrieveMultipleObjects(CfgCampaignGroup.class,	campaigngroupquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return campaigngroups;
	}

	/**
	 * CampaignGroup을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param campaign_group_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public int deleteCampaignGroup(
			final IConfService service,
			int tenant_dbid,
			String campaign_group_name
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		int returnval = 0;
		CfgCampaignGroup campaigngroup = getCampaignGroup(service, tenant_dbid, campaign_group_name);
		if(campaigngroup != null) {
			campaigngroup.delete();
            returnval = 1;
		}
		
		return returnval;
	}

	/**
	 * CampaignGroup을 삭제한다.
	 * @param service
	 * @param dn
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteCampaignGroup(
			final IConfService service,
			CfgCampaignGroup campaigngroup
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			campaigngroup.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	

}
