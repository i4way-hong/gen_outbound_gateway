package kr.co.i4way.genesys.model;

import com.google.gson.JsonElement;

public class CampaignInfoVo {
	int dbid;
	String description;
	String name;
	int script_dbid;
	int state;
	int folderId;
	String command;

	JsonElement annex;
	JsonElement callinglist;
 
	public int getDbid() {
		return dbid;
	}
	public void setDbid(int dbid) {
		this.dbid = dbid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScript_dbid() {
		return script_dbid;
	}
	public void setScript_dbid(int script_dbid) {
		this.script_dbid = script_dbid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
	public JsonElement getAnnex() {
		return annex;
	}
	public void setAnnex(JsonElement annex) {
		this.annex = annex;
	}
	public JsonElement getCallinglist() {
		return callinglist;
	}
	public void setCallinglist(JsonElement callinglist) {
		this.callinglist = callinglist;
	}

	
}
