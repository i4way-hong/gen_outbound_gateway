package kr.co.i4way.genesys.model;

import java.util.List;

import com.google.gson.JsonElement;

public class AgentGroupInfoVo {

	int tenant_dbid;
	String obj;
	int dbid;
	String name;
	int state;
	String command;
	int folderId;

	JsonElement annex;

	public int getTenant_dbid() {
		return tenant_dbid;
	}

	public void setTenant_dbid(int tenant_dbid) {
		this.tenant_dbid = tenant_dbid;
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

	@Override
	public String toString() {
		return "AgentGroupInfoVo [tenant_dbid=" + tenant_dbid + ", obj=" + obj + ", dbid=" + dbid + ", name=" + name
				+ ", state=" + state + ", command=" + command + ", folderId=" + folderId + ", annex=" + annex + "]";
	}

	
	

	
}
