package kr.co.i4way.genesys.model;

import com.google.gson.JsonElement;

public class FilterInfoVo {
	int dbid;
	String name;
	String description;
	int format_dbid;
	String format_name;
	int folderId;
	JsonElement annex;
	String annex_name;
	int objectType;
	String objectPath;
	String obj;
	int state;
	String command;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getFormat_dbid() {
		return format_dbid;
	}
	public void setFormat_dbid(int format_dbid) {
		this.format_dbid = format_dbid;
	}
	public String getFormat_name() {
		return format_name;
	}
	public void setFormat_name(String format_name) {
		this.format_name = format_name;
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
	public String getAnnex_name() {
		return annex_name;
	}
	public void setAnnex_name(String annex_name) {
		this.annex_name = annex_name;
	}
	public String getObj() {
		return obj;
	}
	public void setObj(String obj) {
		this.obj = obj;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	
}
