package kr.co.i4way.genesys.model;

import com.google.gson.JsonElement;

public class TransactionVo {
	int folderId;
	String command;
	int objectType;
	String objectPath;
	String obj;
	int dbid;
	String name;
	String alias;
	int type;
	String type_name;
	String data_type;
	int state;
	JsonElement annex;
	String section_name;
	String change_section_name;
	String option_alias;
	String key;
	String change_key;
	String value;
 
	
	public String getOption_alias() {
		return option_alias;
	}
	public void setOption_alias(String option_alias) {
		this.option_alias = option_alias;
	}
	public String getChange_key() {
		return change_key;
	}
	public void setChange_key(String change_key) {
		this.change_key = change_key;
	}
	public String getChange_section_name() {
		return change_section_name;
	}
	public void setChange_section_name(String change_section_name) {
		this.change_section_name = change_section_name;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getSection_name() {
		return section_name;
	}
	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key_name) {
		this.key = key_name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value_name) {
		this.value = value_name;
	}
	public int getFolderId() {
		return folderId;
	}
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public int getObjectType() {
		return objectType;
	}
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	public String getObjectPath() {
		return objectPath;
	}
	public void setObjectPath(String objectPath) {
		this.objectPath = objectPath;
	}
	public String getObj() {
		return obj;
	}
	public void setObj(String obj) {
		this.obj = obj;
	}
	public int getDbid() {
		return dbid;
	}
	public void setDbid(int dbid) {
		this.dbid = dbid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public JsonElement getAnnex() {
		return annex;
	}
	public void setAnnex(JsonElement annex) {
		this.annex = annex;
	}

	
	
}
