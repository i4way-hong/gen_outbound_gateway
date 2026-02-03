package kr.co.i4way.genesys.model;

import com.google.gson.JsonElement;

public class SkillInfoVo {
	int folderId;
	String command;
	int objectType;
	String objectPath;
	String obj;
	int dbid;
	int objectDbid;
	String name;
	int state;
	JsonElement annex;

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
	public int getObjectDbid() {
		return objectDbid;
	}
	public void setObjectDbid(int objectDbid) {
		this.objectDbid = objectDbid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	@Override
	public String toString() {
		return "SkillInfoVo [folderId=" + folderId + ", command=" + command + ", objectType=" + objectType
				+ ", objectPath=" + objectPath + ", obj=" + obj + ", dbid=" + dbid + ", objectDbid=" + objectDbid
				+ ", name=" + name + ", state=" + state + ", annex=" + annex + "]";
	}

	
}
