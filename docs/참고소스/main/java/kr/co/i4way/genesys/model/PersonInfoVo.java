package kr.co.i4way.genesys.model;

import com.google.gson.JsonElement;

public class PersonInfoVo {

	String firstName;
	String lastName;
	int isAgent;
	String autoMakeAgentLogin;
	String obj;
	int dbid;
	int tenant_dbid;
	String employeeId;
	String agentGroupName;
	int state;
	String userName;
	String command;
	String agentLoginId;
	String usePossibleYn;
	int folderId;

	JsonElement annex;
	JsonElement agentLogins;
	JsonElement skills; 

	
	
	public String getUsePossibleYn() {
		return usePossibleYn;
	}
	public void setUsePossibleYn(String usePossibleYn) {
		this.usePossibleYn = usePossibleYn;
	}
	public int getTenant_dbid() {
		return tenant_dbid;
	}
	public void setTenant_dbid(int tenant_dbid) {
		this.tenant_dbid = tenant_dbid;
	}
	public String getAgentLoginId() {
		return agentLoginId;
	}
	public void setAgentLoginId(String agentLoginId) {
		this.agentLoginId = agentLoginId;
	}
	public String getAgentGroupName() {
		return agentGroupName;
	}
	public void setAgentGroupName(String agentGroupName) {
		this.agentGroupName = agentGroupName;
	}
	public String getAutoMakeAgentLogin() {
		return autoMakeAgentLogin;
	}
	public void setAutoMakeAgentLogin(String autoMakeAgentLogin) {
		this.autoMakeAgentLogin = autoMakeAgentLogin;
	}
	public int getFolderId() {
		return folderId;
	}
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getIsAgent() {
		return isAgent;
	}
	public void setIsAgent(int isAgent) {
		this.isAgent = isAgent;
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
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public JsonElement getAgentLogins() {
		return agentLogins;
	}
	public void setAgentLogins(JsonElement agentLogins) {
		this.agentLogins = agentLogins;
	}
	public JsonElement getSkills() {
		return skills;
	}
	public void setSkills(JsonElement skills) {
		this.skills = skills;
	}
	@Override
	public String toString() {
		return "PersonInfoVo [agentGroupName=" + agentGroupName + ", agentLogins=" + agentLogins + ", annex=" + annex
				+ ", autoMakeAgentLogin=" + autoMakeAgentLogin + ", command=" + command + ", dbid=" + dbid
				+ ", employeeId=" + employeeId + ", firstName=" + firstName + ", folderId=" + folderId + ", isAgent="
				+ isAgent + ", lastName=" + lastName + ", obj=" + obj + ", skills=" + skills + ", state=" + state
				+ ", userName=" + userName + "]";
	}
	
	

	
}
