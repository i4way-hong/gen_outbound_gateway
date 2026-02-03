package kr.co.i4way.genesys.model;

import com.google.gson.JsonElement;

public class MoveInfoVo {
	String src_obj;
	int src_dbid;
	int tgt_dbid;
	String tgt_name;
	int click_dbid;
	int tenant_dbid;
	String employee_id;
	String name;
	JsonElement moveObj;
	JsonElement emp_ids;


	

	public JsonElement getEmp_ids() {
		return emp_ids;
	}
	public void setEmp_ids(JsonElement emp_ids) {
		this.emp_ids = emp_ids;
	}
	public int getTenant_dbid() { 
		return tenant_dbid; 
	}
	public void setTenant_dbid(int tenant_dbid) {
		this.tenant_dbid = tenant_dbid;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTgt_name() {
		return tgt_name;
	}
	public void setTgt_name(String tgt_name) {
		this.tgt_name = tgt_name;
	}
	public JsonElement getMoveObj() {
		return moveObj;
	}
	public void setMoveObj(JsonElement moveObj) {
		this.moveObj = moveObj;
	}
	public String getSrc_obj() {
		return src_obj;
	}
	public void setSrc_obj(String src_obj) {
		this.src_obj = src_obj;
	}
	public int getSrc_dbid() {
		return src_dbid;
	}
	public void setSrc_dbid(int src_dbid) {
		this.src_dbid = src_dbid;
	}
	public int getTgt_dbid() {
		return tgt_dbid;
	}
	public void setTgt_dbid(int tgt_dbid) {
		this.tgt_dbid = tgt_dbid;
	}
	public int getClick_dbid() {
		return click_dbid;
	}
	public void setClick_dbid(int click_dbid) {
		this.click_dbid = click_dbid;
	}
	@Override
	public String toString() {
		return "MoveInfoVo [src_obj=" + src_obj + ", src_dbid=" + src_dbid + ", tgt_dbid=" + tgt_dbid + ", tgt_name="
				+ tgt_name + ", click_dbid=" + click_dbid + ", tenant_dbid=" + tenant_dbid + ", employee_id="
				+ employee_id + ", name=" + name + ", moveObj=" + moveObj + ", emp_ids=" + emp_ids + "]";
	}
	
	
}
