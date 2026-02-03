package kr.co.i4way.genesys.model;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "genesysinfo")
public class GenesysInfoVo {

	private String cfg_ip_p;
	private String cfg_ip_b;
	private int cfg_port_p;
	private int cfg_port_b; 
	private String cfg_endpoint_p;
	private String cfg_endpoint_b;
	private String cfg_client_name;
	private int cfg_tenant_dbid;
	private int cfg_switch_dbid;
	private int cfg_switch_dbid2;
	private String cfg_id;
	private String cfg_passwd;
	private String cfg_charset;

	String 	oc_ip_p; 
	int 	oc_port_p;
	String 	oc_id;
	String 	oc_passwd;
	String  oc_client_id;
	String  oc_client_passwd;
	String 	oc_charset;

	String sc_ip_p;
	String sc_ip_b;
	int sc_port_p;
	int sc_port_b;
	String sc_endpoint_p;
	String sc_endpoint_b;
	String sc_client_name;
	int sc_client_dbid;
	String sc_id;
	String sc_tenant_dbid;
	String sc_charset;

	String stat_ip_p;
	int stat_port_p;
	String stat_ip_b;
	int stat_port_b;
	String stat_charset;
	String stat_endpoint1;
	String stat_endpoint2;
	String stat_clientname;
	String stat_tenant;
	int stat_tenantdbid;
	String stat_switch;
	int stat_frequency;
	int stat_lastrefid;
	int stat_delay;
	int stat_timeout;
	
	
	String t_ip_p;
	String t_ip_b;
    int t_port_p;
    int t_port_b;
    String t_endpoint_p;
    String t_endpoint_b;
    String t_client_name;
    String t_tenant_dbid;
    String t_charset;

	public int getStat_delay() {
		return stat_delay;
	}
	public void setStat_delay(int stat_delay) {
		this.stat_delay = stat_delay;
	}
	public int getStat_timeout() {
		return stat_timeout;
	}
	public void setStat_timeout(int stat_timeout) {
		this.stat_timeout = stat_timeout;
	}
   
	public String getT_ip_p() {
		return t_ip_p;
	}
	public void setT_ip_p(String t_ip_p) {
		this.t_ip_p = t_ip_p;
	}
	public String getT_ip_b() {
		return t_ip_b;
	}
	public void setT_ip_b(String t_ip_b) {
		this.t_ip_b = t_ip_b;
	}
	public int getT_port_p() {
		return t_port_p;
	}
	public void setT_port_p(int t_port_p) {
		this.t_port_p = t_port_p;
	}
	public int getT_port_b() {
		return t_port_b;
	}
	public void setT_port_b(int t_port_b) {
		this.t_port_b = t_port_b;
	}
	public String getT_endpoint_p() {
		return t_endpoint_p;
	}
	public void setT_endpoint_p(String t_endpoint_p) {
		this.t_endpoint_p = t_endpoint_p;
	}
	public String getT_endpoint_b() {
		return t_endpoint_b;
	}
	public void setT_endpoint_b(String t_endpoint_b) {
		this.t_endpoint_b = t_endpoint_b;
	}
	public String getT_client_name() {
		return t_client_name;
	}
	public void setT_client_name(String t_client_name) {
		this.t_client_name = t_client_name;
	}
	public String getT_tenant_dbid() {
		return t_tenant_dbid;
	}
	public void setT_tenant_dbid(String t_tenant_dbid) {
		this.t_tenant_dbid = t_tenant_dbid;
	}
	public String getT_charset() {
		return t_charset;
	}
	public void setT_charset(String t_charset) {
		this.t_charset = t_charset;
	}
	List<String> sc_monitoring_dbids;
	
	public String getStat_ip_p() {
		return stat_ip_p;
	}
	public void setStat_ip_p(String stat_ip_p) {
		this.stat_ip_p = stat_ip_p;
	}
	public int getStat_port_p() {
		return stat_port_p;
	}
	public void setStat_port_p(int stat_port_p) {
		this.stat_port_p = stat_port_p;
	}
	public String getStat_ip_b() {
		return stat_ip_b;
	}
	public void setStat_ip_b(String stat_ip_b) {
		this.stat_ip_b = stat_ip_b;
	}
	public int getStat_port_b() {
		return stat_port_b;
	}
	public void setStat_port_b(int stat_port_b) {
		this.stat_port_b = stat_port_b;
	}
	public String getStat_charset() {
		return stat_charset;
	}
	public void setStat_charset(String stat_charset) {
		this.stat_charset = stat_charset;
	}
	public String getStat_endpoint1() {
		return stat_endpoint1;
	}
	public void setStat_endpoint1(String stat_endpoint1) {
		this.stat_endpoint1 = stat_endpoint1;
	}
	public String getStat_endpoint2() {
		return stat_endpoint2;
	}
	public void setStat_endpoint2(String stat_endpoint2) {
		this.stat_endpoint2 = stat_endpoint2;
	}
	public String getStat_clientname() {
		return stat_clientname;
	}
	public void setStat_clientname(String stat_clientname) {
		this.stat_clientname = stat_clientname;
	}
	public String getStat_tenant() {
		return stat_tenant;
	}
	public void setStat_tenant(String stat_tenant) {
		this.stat_tenant = stat_tenant;
	}
	public int getStat_tenantdbid() {
		return stat_tenantdbid;
	}
	public void setStat_tenantdbid(int stat_tenantdbid) {
		this.stat_tenantdbid = stat_tenantdbid;
	}
	public String getStat_switch() {
		return stat_switch;
	}
	public void setStat_switch(String stat_switch) {
		this.stat_switch = stat_switch;
	}
	public int getStat_frequency() {
		return stat_frequency;
	}
	public void setStat_frequency(int stat_frequency) {
		this.stat_frequency = stat_frequency;
	}
	public int getStat_lastrefid() {
		return stat_lastrefid;
	}
	public void setStat_lastrefid(int stat_lastrefid) {
		this.stat_lastrefid = stat_lastrefid;
	}
	public List<String> getSc_monitoring_dbids() {
		return sc_monitoring_dbids;
	}
	public void setSc_monitoring_dbids(List<String> sc_monitoring_dbids) {
		this.sc_monitoring_dbids = sc_monitoring_dbids;
	}
	public int getSc_client_dbid() {
		return sc_client_dbid;
	}
	public void setSc_client_dbid(int sc_client_dbid) {
		this.sc_client_dbid = sc_client_dbid;
	}
	public String getSc_id() {
		return sc_id;
	}
	public void setSc_id(String sc_id) {
		this.sc_id = sc_id;
	}
	public String getSc_ip_p() {
		return sc_ip_p;
	}
	public void setSc_ip_p(String sc_ip_p) {
		this.sc_ip_p = sc_ip_p;
	}
	public String getSc_ip_b() {
		return sc_ip_b;
	}
	public void setSc_ip_b(String sc_ip_b) {
		this.sc_ip_b = sc_ip_b;
	}
	public int getSc_port_p() {
		return sc_port_p;
	}
	public void setSc_port_p(int sc_port_p) {
		this.sc_port_p = sc_port_p;
	}
	public int getSc_port_b() {
		return sc_port_b;
	}
	public void setSc_port_b(int sc_port_b) {
		this.sc_port_b = sc_port_b;
	}
	public String getSc_endpoint_p() {
		return sc_endpoint_p;
	}
	public void setSc_endpoint_p(String sc_endpoint_p) {
		this.sc_endpoint_p = sc_endpoint_p;
	}
	public String getSc_endpoint_b() {
		return sc_endpoint_b;
	}
	public void setSc_endpoint_b(String sc_endpoint_b) {
		this.sc_endpoint_b = sc_endpoint_b;
	}
	public String getSc_client_name() {
		return sc_client_name;
	}
	public void setSc_client_name(String sc_client_name) {
		this.sc_client_name = sc_client_name;
	}
	public String getSc_tenant_dbid() {
		return sc_tenant_dbid;
	}
	public void setSc_tenant_dbid(String sc_tenant_dbid) {
		this.sc_tenant_dbid = sc_tenant_dbid;
	}
	public String getSc_charset() {
		return sc_charset;
	}
	public void setSc_charset(String sc_charset) {
		this.sc_charset = sc_charset;
	}
	public String getOc_ip_p() {
		return oc_ip_p;
	}
	public void setOc_ip_p(String oc_ip_p) {
		this.oc_ip_p = oc_ip_p;
	}
	public int getOc_port_p() {
		return oc_port_p;
	}
	public void setOc_port_p(int oc_port_p) {
		this.oc_port_p = oc_port_p;
	}
	public String getOc_id() {
		return oc_id;
	}
	public void setOc_id(String oc_id) {
		this.oc_id = oc_id;
	}
	public String getOc_passwd() {
		return oc_passwd;
	}
	public void setOc_passwd(String oc_passwd) {
		this.oc_passwd = oc_passwd;
	}
	public String getOc_client_id() {
		return oc_client_id;
	}
	public void setOc_client_id(String oc_client_id) {
		this.oc_client_id = oc_client_id;
	}
	public String getOc_client_passwd() {
		return oc_client_passwd;
	}
	public void setOc_client_passwd(String oc_client_passwd) {
		this.oc_client_passwd = oc_client_passwd;
	}
	public String getOc_charset() {
		return oc_charset;
	}
	public void setOc_charset(String oc_charset) {
		this.oc_charset = oc_charset;
	}
	
	public String getCfg_ip_p() {
		return cfg_ip_p;
	}
	public void setCfg_ip_p(String cfg_ip_p) {
		this.cfg_ip_p = cfg_ip_p;
	}
	public String getCfg_ip_b() {
		return cfg_ip_b;
	}
	public void setCfg_ip_b(String cfg_ip_b) {
		this.cfg_ip_b = cfg_ip_b;
	}
	public int getCfg_port_p() {
		return cfg_port_p;
	}
	public void setCfg_port_p(int cfg_port_p) {
		this.cfg_port_p = cfg_port_p;
	}
	public int getCfg_port_b() {
		return cfg_port_b;
	}
	public void setCfg_port_b(int cfg_port_b) {
		this.cfg_port_b = cfg_port_b;
	}
	public String getCfg_endpoint_p() {
		return cfg_endpoint_p;
	}
	public void setCfg_endpoint_p(String cfg_endpoint_p) {
		this.cfg_endpoint_p = cfg_endpoint_p;
	}
	public String getCfg_endpoint_b() {
		return cfg_endpoint_b;
	}
	public void setCfg_endpoint_b(String cfg_endpoint_b) {
		this.cfg_endpoint_b = cfg_endpoint_b;
	}
	public String getCfg_client_name() {
		return cfg_client_name;
	}
	public void setCfg_client_name(String cfg_client_name) {
		this.cfg_client_name = cfg_client_name;
	}
	public String getCfg_id() {
		return cfg_id;
	}
	public void setCfg_id(String cfg_id) {
		this.cfg_id = cfg_id;
	}
	public String getCfg_passwd() {
		return cfg_passwd;
	}
	public void setCfg_passwd(String cfg_passwd) {
		this.cfg_passwd = cfg_passwd;
	}
	public String getCfg_charset() {
		return cfg_charset;
	}
	public void setCfg_charset(String cfg_charset) {
		this.cfg_charset = cfg_charset;
	}
	public int getCfg_tenant_dbid() {
		return cfg_tenant_dbid;
	}
	public void setCfg_tenant_dbid(int cfg_tenant_dbid) {
		this.cfg_tenant_dbid = cfg_tenant_dbid;
	}
	public int getCfg_switch_dbid() {
		return cfg_switch_dbid;
	}
	public void setCfg_switch_dbid(int cfg_switch_dbid) {
		this.cfg_switch_dbid = cfg_switch_dbid;
	}
	@Override
	public String toString() {
		return "GenesysInfoVo [cfg_ip_p=" + cfg_ip_p + ", cfg_ip_b=" + cfg_ip_b + ", cfg_port_p=" + cfg_port_p
				+ ", cfg_port_b=" + cfg_port_b + ", cfg_endpoint_p=" + cfg_endpoint_p + ", cfg_endpoint_b="
				+ cfg_endpoint_b + ", cfg_client_name=" + cfg_client_name + ", cfg_tenant_dbid=" + cfg_tenant_dbid
				+ ", cfg_switch_dbid=" + cfg_switch_dbid + ", cfg_id=" + cfg_id + ", cfg_passwd=" + cfg_passwd
				+ ", cfg_charset=" + cfg_charset + "]";
	}
	public int getCfg_switch_dbid2() {
		return cfg_switch_dbid2;
	}
	public void setCfg_switch_dbid2(int cfg_switch_dbid2) {
		this.cfg_switch_dbid2 = cfg_switch_dbid2;
	}

}