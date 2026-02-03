package kr.co.i4way.genesys.model;

import java.util.List;

import com.google.gson.JsonElement;

public class FolderInfoVo {
	int ownerType;
	String obj_order;
	int ownerDbid;
	int type;
	int folderId;
	String command;
	int objectType;
	JsonElement objectIds;
	String objectPath;
	String obj;
	int dbid;
	int objectDbid;
	String name;
	int state;
	JsonElement annex;

	public int getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(int ownerType) {
		this.ownerType = ownerType;
	}
	public String getObj_order() {
		return obj_order;
	}
	public void setObj_order(String obj_order) { 
		this.obj_order = obj_order;
	}
	public int getOwnerDbid() {
		return ownerDbid;
	}
	public void setOwnerDbid(int ownerDbid) {
		this.ownerDbid = ownerDbid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public JsonElement getObjectIds() {
		return objectIds;
	}
	public void setObjectIds(JsonElement objectIds) {
		this.objectIds = objectIds;
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
}
