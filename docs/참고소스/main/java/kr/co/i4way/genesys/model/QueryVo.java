package kr.co.i4way.genesys.model;

public class QueryVo {
	String qry_type;
	String qry_str;
	int folder_type;
	int folder_dbid;
	String folder_state;
	String src_obj;
	int dbid;
	int src_dbid;
	int tgt_dbid;
	int click_dbid;
	String employee_id;
	String skill_dbids;
	String skill_levels;
	int switch_dbid;
	int skill_level;
	String agent_login_id;
	Boolean assignable;
	String group_name;
	String tgt_group_name;
	int group_dbid;
	String delete_yn; 
	String dialplan_name;
	boolean flag;
	String exec_mode;

	public String getExec_mode() {
		return exec_mode;
	}
	public void setExec_mode(String exec_mode) {
		this.exec_mode = exec_mode;
	}
	public int getSkill_level() {
		return skill_level;
	}
	public void setSkill_level(int skill_level) {
		this.skill_level = skill_level;
	}
	public boolean getFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public String getDialplan_name() {
		return dialplan_name;
	}
	public void setDialplan_name(String dialplan_name) {
		this.dialplan_name = dialplan_name;
	}
	public int getDbid() {
		return dbid;
	}
	public void setDbid(int dbid) {
		this.dbid = dbid;
	}
	public int getSwitch_dbid() {
		return switch_dbid;
	}
	public void setSwitch_dbid(int switch_dbid) {
		this.switch_dbid = switch_dbid;
	}
	public Boolean getAssignable() {
		return assignable;
	}
	public void setAssignable(Boolean assignable) {
		this.assignable = assignable;
	}
	public String getTgt_group_name() {
		return tgt_group_name;
	}
	public void setTgt_group_name(String tgt_group_name) {
		this.tgt_group_name = tgt_group_name;
	}
	public String getDelete_yn() {
		return delete_yn;
	}
	public void setDelete_yn(String delete_yn) {
		this.delete_yn = delete_yn;
	}
	public int getGroup_dbid() {
		return group_dbid;
	}
	public void setGroup_dbid(int group_dbid) {
		this.group_dbid = group_dbid;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getSkill_dbids() {
		return skill_dbids;
	}
	public void setSkill_dbids(String skill_dbids) {
		this.skill_dbids = skill_dbids;
	}
	public String getSkill_levels() {
		return skill_levels;
	}
	public void setSkill_levels(String skill_levels) {
		this.skill_levels = skill_levels;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getAgent_login_id() {
		return agent_login_id;
	}
	public void setAgent_login_id(String agent_login_id) {
		this.agent_login_id = agent_login_id;
	}
	public String getFolder_state() {
		return folder_state;
	}
	public void setFolder_state(String folder_state) {
		this.folder_state = folder_state;
	}
	public int getClick_dbid() {
		return click_dbid;
	}
	public void setClick_dbid(int click_dbid) {
		this.click_dbid = click_dbid;
	}
	public String getQry_type() {
		return qry_type;
	}
	public void setQry_type(String qry_type) {
		this.qry_type = qry_type;
	}
	public String getQry_str() {
		return qry_str;
	}
	public void setQry_str(String qry_str) {
		this.qry_str = qry_str;
	}
	public int getFolder_type() {
		return folder_type;
	}
	public void setFolder_type(int folder_type) {
		this.folder_type = folder_type;
	}
	public int getFolder_dbid() {
		return folder_dbid;
	}
	public void setFolder_dbid(int folder_dbid) {
		this.folder_dbid = folder_dbid;
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
	
	
}
