package kr.co.i4way.genesys.model;

import com.google.gson.JsonElement;

public class CallingListInfoVo {


	int dbid;
	String description;
	String name;
	int table_access_dbid;
	int log_table_access_dbid;
	int filter_dbid;
	int script_dbid;
	int calling_time_from;
	int calling_time_to;
	int max_attampt;
	int state;
	int folderId;
	String command;

	JsonElement annex;
	JsonElement treatments;
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
	public int getTable_access_dbid() {
		return table_access_dbid;
	}
	public void setTable_access_dbid(int table_access_dbid) {
		this.table_access_dbid = table_access_dbid;
	}
	public int getLog_table_access_dbid() {
		return log_table_access_dbid;
	}
	public void setLog_table_access_dbid(int log_table_access_dbid) {
		this.log_table_access_dbid = log_table_access_dbid;
	}
	public int getFilter_dbid() {
		return filter_dbid;
	}
	public void setFilter_dbid(int filter_dbid) {
		this.filter_dbid = filter_dbid;
	}
	public int getScript_dbid() {
		return script_dbid;
	}
	public void setScript_dbid(int script_dbid) {
		this.script_dbid = script_dbid;
	}
	public int getCalling_time_from() {
		return calling_time_from;
	}
	public void setCalling_time_from(int calling_time_from) {
		this.calling_time_from = calling_time_from;
	}
	public int getCalling_time_to() {
		return calling_time_to;
	}
	public void setCalling_time_to(int calling_time_to) {
		this.calling_time_to = calling_time_to;
	}
	public int getMax_attampt() {
		return max_attampt;
	}
	public void setMax_attampt(int max_attampt) {
		this.max_attampt = max_attampt;
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
	public JsonElement getTreatments() {
		return treatments;
	}
	public void setTreatments(JsonElement treatments) {
		this.treatments = treatments;
	}

	@Override
	public String toString() {
		return "CallingListInfoVo [annex=" + annex + ", calling_time_from=" + calling_time_from + ", calling_time_to="
				+ calling_time_to + ", command=" + command + ", dbid=" + dbid + ", description=" + description
				+ ", filter_dbid=" + filter_dbid + ", folderId=" + folderId + ", log_table_access_dbid="
				+ log_table_access_dbid + ", max_attampt=" + max_attampt + ", name=" + name + ", script_dbid="
				+ script_dbid + ", state=" + state + ", table_access_dbid=" + table_access_dbid + ", treatments="
				+ treatments + "]";
	}


	
}
