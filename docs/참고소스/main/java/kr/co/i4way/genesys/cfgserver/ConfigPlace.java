package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDN;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPlace;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPlaceGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTransaction;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPlaceGroupQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPlaceQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgTransactionQuery;

public class ConfigPlace {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	private ConfigDn configdn = null;
	public ConfigPlace() {
		
	}
	
	/**
	 * CfgPlace을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @return
	 */
	public Collection<CfgPlace> getPlace(final IConfService service, int tenant_dbid) {
		Collection<CfgPlace> rtnplace = null;
		CfgPlaceQuery placequery = new CfgPlaceQuery();
		placequery.setTenantDbid(tenant_dbid);
		try {
			rtnplace = service.retrieveMultipleObjects(CfgPlace.class, placequery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return rtnplace;
	}

	/**
	 * CfgPlace을 쿼리한다.
	 * @param service 
	 * @param tenant_dbid
	 * @param place_name
	 * @return
	 */
	public CfgPlace getPlace(final IConfService service, int tenant_dbid, String place_name) {
		CfgPlace rtnplace = null;
		CfgPlaceQuery placequery = new CfgPlaceQuery();
		placequery.setTenantDbid(tenant_dbid);
		placequery.setName(place_name);
		try {
			rtnplace = service.retrieveObject(CfgPlace.class, placequery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnplace;
	}
	
	/**
	 * CfgPlace을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param place_dbid
	 * @return
	 */
	public CfgPlace getPlace(final IConfService service, int tenant_dbid, int place_dbid) {
		CfgPlace rtnplace = null;
		CfgPlaceQuery placequery = new CfgPlaceQuery();
		placequery.setTenantDbid(tenant_dbid);
		placequery.setDbid(place_dbid);
		try {
			rtnplace = service.retrieveObject(CfgPlace.class, placequery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnplace;
	}	

	/**
	 * DN의 Dependency를 조회한다.
	 * @param service
	 * @param tenant_dbid
	 * @param dn_dbid
	 * @return
	 */
	public Collection<CfgPlace> getDnDependency(final IConfService service, int tenant_dbid, int dn_dbid) {
		Collection<CfgPlace> rtnplace = null;
		CfgPlaceQuery placequery = new CfgPlaceQuery();
		placequery.setTenantDbid(tenant_dbid);
		placequery.setDnDbid(dn_dbid);
		try {
			rtnplace = service.retrieveMultipleObjects(CfgPlace.class, placequery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return rtnplace;
	}
	
	/**
	 * CfgPlace을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param place_name
	 * @param dn
	 * @return
	 */
	public CfgPlace createPlace(
			final IConfService service,
			int tenant_dbid,
			int switch_dbid,
			String place_name,
			String[] dn
			) {
		CfgPlace new_place = new CfgPlace(service);
		configdn = new ConfigDn();
		CfgDN tmpdn = null;
		Collection<CfgDN> dns = new HashSet<CfgDN>();
		try {
			new_place.setTenantDBID(tenant_dbid);
			new_place.setName(place_name);
			if(dn.length > 0) {
				for(int i=0; i<dn.length;i++) {
					tmpdn = null;
					tmpdn = configdn.getDn(service, tenant_dbid, switch_dbid, dn[i]);
					dns.add(tmpdn);
					new_place.setDNs(dns);
				}
			}
			new_place.save();
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}		
		return new_place;
	}

		/**
	 * CfgPlace을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param place_name
	 * @param dn
	 * @return
	 */
	public CfgPlace createPlace(
			final IConfService service,
			int tenant_dbid,
			int switch_dbid,
			String place_name,
			CfgDN dn
			) {
		CfgPlace new_place = new CfgPlace(service);
		configdn = new ConfigDn();
		Collection<CfgDN> dns = new HashSet<CfgDN>();
		try {
			new_place.setTenantDBID(tenant_dbid);
			new_place.setName(place_name);
			dns.add(dn);
			new_place.setDNs(dns);
			new_place.save();
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}		
		return new_place;
	}
	
	/**
	 * CfgPlace을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param switch_dbid
	 * @param old_place_name
	 * @param new_place_name
	 * @param dn
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgPlace modifyPlace(
			final IConfService service,
			int tenant_dbid,
			int switch_dbid,
			String old_place_name,
			String new_place_name,
			String[] dn
			) {
		CfgPlace place = null;
		configdn = new ConfigDn();
		CfgDN tmpdn = null;
		Collection<CfgDN> dns = new HashSet<CfgDN>();
		try {
			place = getPlace(service, tenant_dbid, old_place_name);
			if(place != null) {
				place.setName(new_place_name);
				if(dn.length > 0) {
					for(int i=0; i<dn.length;i++) {
						tmpdn = null;
						tmpdn = configdn.getDn(service, tenant_dbid, switch_dbid, dn[i]);
						dns.add(tmpdn);
						place.setDNs(dns);
					}
				}
				place.save();
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return place;
	}	
	
	/**
	 * CfgPlace을 삭제한다.
	 * @param service
	 * @param tenant_dbid
	 * @param place_name
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deletePlace(
			final IConfService service,
			int tenant_dbid,
			String place_name
			) {
		CfgPlace place = null;
		boolean rtnval = false;
		
		try {
			place = getPlace(service, tenant_dbid, place_name);
			if(place != null) {
				place.delete();
				rtnval = true;
			}
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnval;
	}	
	
	
	/**
	 * CfgPlace의 Transaction을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param place_name
	 * @return
	 */
	public CfgTransaction getTr(final IConfService service, int tenant_dbid, int switch_dbid, String place_name) {
		CfgTransaction rtnplace = null;
		CfgTransactionQuery placequery = new CfgTransactionQuery();
		placequery.setTenantDbid(tenant_dbid);
		placequery.setName(place_name);
		try {
			rtnplace = service.retrieveObject(CfgTransaction.class, placequery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnplace;
	}
	
	/**
	 * CfgPlaceGroup을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @return
	 */
	public Collection<CfgPlaceGroup> getPlaceGroup(final IConfService service, int tenant_dbid) {
		Collection<CfgPlaceGroup> rtnplacegroup = null;
		CfgPlaceGroupQuery placequery = new CfgPlaceGroupQuery();
		placequery.setTenantDbid(tenant_dbid);
		try {
			rtnplacegroup = service.retrieveMultipleObjects(CfgPlaceGroup.class, placequery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		return rtnplacegroup;
	}

	/**
	 * CfgPlaceGroup을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param place_name
	 * @return
	 */
	public CfgPlaceGroup getPlaceGroup(final IConfService service, int tenant_dbid, String group_name) {
		CfgPlaceGroup rtnplacegroup = null;
		CfgPlaceGroupQuery groupquery = new CfgPlaceGroupQuery();
		groupquery.setTenantDbid(tenant_dbid);
		groupquery.setName(group_name);
		try {
			rtnplacegroup = service.retrieveObject(CfgPlaceGroup.class, groupquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnplacegroup;
	}
	
	/**
	 * CfgPlace을 쿼리한다.
	 * @param service
	 * @param tenant_dbid
	 * @param place_dbid
	 * @return
	 */
	public CfgPlaceGroup getPlaceGroup(final IConfService service, int tenant_dbid, int place_dbid) {
		CfgPlaceGroup rtnplacegroup = null;
		CfgPlaceGroupQuery groupquery = new CfgPlaceGroupQuery();
		groupquery.setTenantDbid(tenant_dbid);
		groupquery.setDbid(place_dbid);
		try {
			rtnplacegroup = service.retrieveObject(CfgPlaceGroup.class, groupquery);
		} catch (ConfigException e) {
			logger.error("ConfigException", e);
		}
		return rtnplacegroup;
	}
	

	
}
