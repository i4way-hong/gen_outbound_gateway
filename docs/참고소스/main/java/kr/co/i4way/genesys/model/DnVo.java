package kr.co.i4way.genesys.model;

import com.google.gson.JsonElement;

public class DnVo {
	int dbid;
	String place_name;
	String number;
	String type_name;
	int type;
	int switch_dbid;
	String switch_name;
	String association;
	String register_name;
	int register;
	int state;
	String name;
	String route_type_name;
	int route_type;
	int group_dbid;
	String use_override_name;
	int use_override;
	String dn_login_id;
	int switch_specific_type;
	int trunks;
	int folderId;
	JsonElement annex;
	String annex_name;
	String obj;
	String dialplan;
	
	
	public String getAnnex_name() {
		return annex_name;
	} 
	public void setAnnex_name(String annex_name) {
		this.annex_name = annex_name;
	}
	public String getDialplan() {
		return dialplan;
	}
	public void setDialplan(String dialplan) {
		this.dialplan = dialplan;
	}
	public String getPlace_name() {
		return place_name;
	}
	public void setPlace_name(String place_name) {
		this.place_name = place_name;
	}
	public int getDbid() {
		return dbid;
	}
	public void setDbid(int dbid) {
		this.dbid = dbid;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSwitch_dbid() {
		return switch_dbid;
	}
	public void setSwitch_dbid(int switch_dbid) {
		this.switch_dbid = switch_dbid;
	}
	public String getSwitch_name() {
		return switch_name;
	}
	public void setSwitch_name(String switch_name) {
		this.switch_name = switch_name;
	}
	public String getAssociation() {
		return association;
	}
	public void setAssociation(String association) {
		this.association = association;
	}
	public String getRegister_name() {
		return register_name;
	}
	public void setRegister_name(String register_name) {
		this.register_name = register_name;
	}
	public int getRegister() {
		return register;
	}
	public void setRegister(int register) {
		this.register = register;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoute_type_name() {
		return route_type_name;
	}
	public void setRoute_type_name(String route_type_name) {
		this.route_type_name = route_type_name;
	}
	public int getRoute_type() {
		return route_type;
	}
	public void setRoute_type(int route_type) {
		this.route_type = route_type;
	}
	public int getGroup_dbid() {
		return group_dbid;
	}
	public void setGroup_dbid(int group_dbid) {
		this.group_dbid = group_dbid;
	}
	public String getUse_override_name() {
		return use_override_name;
	}
	public void setUse_override_name(String use_override_name) {
		this.use_override_name = use_override_name;
	}
	public int getUse_override() {
		return use_override;
	}
	public void setUse_override(int use_override) {
		this.use_override = use_override;
	}
	public String getDn_login_id() {
		return dn_login_id;
	}
	public void setDn_login_id(String dn_login_id) {
		this.dn_login_id = dn_login_id;
	}
	public int getSwitch_specific_type() {
		return switch_specific_type;
	}
	public void setSwitch_specific_type(int switch_specific_type) {
		this.switch_specific_type = switch_specific_type;
	}
	public int getTrunks() {
		return trunks;
	}
	public void setTrunks(int trunks) {
		this.trunks = trunks;
	}
	public int getFolderId() {
		return folderId;
	}
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
	public JsonElement getAnnex() {
		return annex;
	}
	public void setAnnex(JsonElement annex) {
		this.annex = annex;
	}
	public String getObj() {
		return obj;
	}
	public void setObj(String obj) {
		this.obj = obj;
	}

	
}
