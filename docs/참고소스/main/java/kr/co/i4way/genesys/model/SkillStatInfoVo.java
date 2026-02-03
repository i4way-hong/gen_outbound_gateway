package kr.co.i4way.genesys.model;

import com.google.gson.JsonElement;

public class SkillStatInfoVo {
	int tenant_dbid;
	int dbid;
	String name;
	
	public int getTenant_dbid() {
		return tenant_dbid;
	}
	public void setTenant_dbid(int tenant_dbid) {
		this.tenant_dbid = tenant_dbid;
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
	@Override
	public String toString() {
		return "SkillStatInfoVo [tenant_dbid=" + tenant_dbid + ", dbid=" + dbid + ", name=" + name + "]";
	}
	
	
	
}
