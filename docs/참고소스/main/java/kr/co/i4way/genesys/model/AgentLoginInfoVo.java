package kr.co.i4way.genesys.model;

import com.google.gson.JsonElement;

public class AgentLoginInfoVo {
	int folderId;
	String command;
	int objectType;
	String objectPath;
	String obj;
	int dbid;
	int objectDbid;
	String code;
	int switch_dbid;
	int state;
	int switchSpecificType;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getSwitch_dbid() {
		return switch_dbid;
	}
	public void setSwitch_dbid(int switch_dbid) {
		this.switch_dbid = switch_dbid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getSwitchSpecificType() {
		return switchSpecificType;
	}
	public void setSwitchSpecificType(int switchSpecificType) {
		this.switchSpecificType = switchSpecificType;
	}
	public JsonElement getAnnex() {
		return annex;
	}
	public void setAnnex(JsonElement annex) {
		this.annex = annex;
	}
	@Override
	public String toString() {
		return "AgentLoginInfoVo [folderId=" + folderId + ", command=" + command + ", objectType=" + objectType
				+ ", objectPath=" + objectPath + ", obj=" + obj + ", dbid=" + dbid + ", objectDbid=" + objectDbid
				+ ", code=" + code + ", switch_dbid=" + switch_dbid + ", state=" + state + ", switchSpecificType="
				+ switchSpecificType + ", annex=" + annex + ", getFolderId()=" + getFolderId() + ", getCommand()="
				+ getCommand() + ", getObjectType()=" + getObjectType() + ", getObjectPath()=" + getObjectPath()
				+ ", getObj()=" + getObj() + ", getDbid()=" + getDbid() + ", getObjectDbid()=" + getObjectDbid()
				+ ", getCode()=" + getCode() + ", getSwitch_dbid()=" + getSwitch_dbid() + ", getState()=" + getState()
				+ ", getSwitchSpecificType()=" + getSwitchSpecificType() + ", getAnnex()=" + getAnnex()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	
	
}
