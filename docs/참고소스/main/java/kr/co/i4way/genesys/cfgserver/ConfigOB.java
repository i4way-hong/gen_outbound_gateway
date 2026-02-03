package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFilter;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFormat;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTreatment;
import com.genesyslab.platform.applicationblocks.com.queries.CfgFilterQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgFormatQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgTreatmentQuery;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

public class ConfigOB {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	public ConfigOB() {
		
	}
	
	/**
	 * CfgFilter을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @return 
	 */
	public Collection<CfgFilter> getFilter(final IConfService service, int tenant_dbid) {
		Collection<CfgFilter> rtnfilter = null;
		CfgFilterQuery fquery = new CfgFilterQuery();
		fquery.setTenantDbid(tenant_dbid);
		try {
			rtnfilter = service.retrieveMultipleObjects(CfgFilter.class, fquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return rtnfilter;
	}
	
	/**
	 * CfgFilter을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param filter_name
	 * @return
	 */
	public CfgFilter getFilter(final IConfService service, int tenant_dbid, String filter_name) {
		CfgFilter rtnfilter = null;
		CfgFilterQuery fquery = new CfgFilterQuery();
		fquery.setTenantDbid(tenant_dbid);
		fquery.setName(filter_name);
		try {
			rtnfilter = service.retrieveObject(CfgFilter.class, fquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnfilter;
	}
	
	/**
	 * CfgFilter을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param filter_dbid
	 * @return
	 */
	public CfgFilter getFilter(final IConfService service, int tenant_dbid, int filter_dbid) {
		CfgFilter rtnfilter = null;
		CfgFilterQuery fquery = new CfgFilterQuery();
		fquery.setTenantDbid(tenant_dbid);
		fquery.setDbid(filter_dbid);
		try {
			rtnfilter = service.retrieveObject(CfgFilter.class, fquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnfilter;
	}
	
	/**
	 * CfgFormat을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param format_name
	 * @return
	 */
	public CfgFormat getFormat(final IConfService service, int tenant_dbid, String format_name) {
		CfgFormat rtnformat = null;
		CfgFormatQuery fquery = new CfgFormatQuery();
		fquery.setTenantDbid(tenant_dbid);
		fquery.setName(format_name);
		try {
			rtnformat = service.retrieveObject(CfgFormat.class, fquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnformat;
	}
	
	/**
	 * CfgTreatment을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param treatment_name
	 * @return
	 */
	public CfgTreatment getTreatment(final IConfService service, int tenant_dbid, String treatment_name) {
		CfgTreatment rtnTreatment = null;
		CfgTreatmentQuery tquery = new CfgTreatmentQuery();
		tquery.setTenantDbid(tenant_dbid);
		tquery.setName(treatment_name);
		try {
			rtnTreatment = service.retrieveObject(CfgTreatment.class, tquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnTreatment;
	}
	
	/**
	 * CfgFilter을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param filter_name
	 * @param format_name
	 * @param criteria
	 * @param orderby
	 * @return
	 */
	public CfgFilter createFilter(
			final IConfService service,
			int tenant_dbid,
			String filter_name,
			String format_name,
			String criteria,
			String orderby
			) {
		// Read configuration objects:
		CfgFilter new_filter = new CfgFilter(service);
		CfgFormat format = null;
		try {
			new_filter.setTenantDBID(tenant_dbid);
			new_filter.setName(filter_name);
			
			KeyValueCollection mainSection = new KeyValueCollection();
            KeyValueCollection defaultSection = new KeyValueCollection();
            if (criteria != null)
                defaultSection.addString("criteria", criteria);
            if (orderby != null)
                defaultSection.addString("order_by", orderby);

            mainSection.addObject("default", defaultSection);
            new_filter.setUserProperties(mainSection);
            format = getFormat(service, tenant_dbid, format_name);
            new_filter.setFormat(format);
            new_filter.save();
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		
		return new_filter;
	}
	
	/**
	 * CfgFilter을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param filter_name
	 * @param format_name
	 * @param criteria
	 * @param orderby
	 * @return
	 */
	public CfgFilter modifyFilter(
			final IConfService service,
			int tenant_dbid,
			String filter_name,
			String criteria,
			String orderby
			) {
		// Read configuration objects:
		CfgFilter filter = getFilter(service, tenant_dbid, filter_name);
		try {
			KeyValueCollection mainSection = new KeyValueCollection();
            KeyValueCollection defaultSection = new KeyValueCollection();
            if (criteria != null)
                defaultSection.addString("criteria", criteria);
            if (orderby != null)
                defaultSection.addString("order_by", orderby);

            mainSection.addObject("default", defaultSection);
            filter.setUserProperties(mainSection);
            filter.save();
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		
		return filter;
	}
	
	/**
	 * CfgFilter을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param filter_name
	 * @return
	 */
	public int deleteFilter(
			final IConfService service,
			int tenant_dbid,
			String filter_name
			) {
		int returnval = 0;
		CfgFilter filter = getFilter(service, tenant_dbid, filter_name);
		try {
			if(filter != null) {
	            filter.delete();
	            returnval = 1;
			}
		} catch (ConfigException e) {
			e.printStackTrace();
		}		
		return returnval;
	}
	

}
