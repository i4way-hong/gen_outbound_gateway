package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigApplication {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	public ConfigApplication() {		
	}


	public CfgApplication getApplicationInfo_dbid(final IConfService service,
					int tenant_dbid,
					int dbid
					)
			throws ConfigException, InterruptedException {
		CfgApplication rtnval = null;
		try{
			CfgApplicationQuery applicationquery = new CfgApplicationQuery();
			applicationquery.setTenantDbid(tenant_dbid);
			applicationquery.setDbid(dbid);
			rtnval = service.retrieveObject(CfgApplication.class,
			applicationquery);
			
		}catch(Exception ex){
			logger.error(ex.getMessage());
		}
		return rtnval; 
	}

	public CfgApplication getApplicationInfo_dbid(final IConfService service,
					int tenant_dbid,
					String name
					)
			throws ConfigException, InterruptedException {
		CfgApplication rtnval = null;
		try{
			CfgApplicationQuery applicationquery = new CfgApplicationQuery();
			applicationquery.setTenantDbid(tenant_dbid);
			applicationquery.setName(name);
			rtnval = service.retrieveObject(CfgApplication.class,
			applicationquery);

		}catch(Exception ex){

		}
		return rtnval;
	}

	public Collection<CfgApplication> getApplicationInfo(final IConfService service,
			int tenant_dbid
			)
			throws ConfigException, InterruptedException {
		Collection<CfgApplication> rtnval = null;
		try{
			CfgApplicationQuery applicationquery = new CfgApplicationQuery();
			applicationquery.setTenantDbid(tenant_dbid);
			rtnval = service.retrieveMultipleObjects(CfgApplication.class,
			applicationquery);
		}catch(Exception ex){

		}
		return rtnval;
	}



}
