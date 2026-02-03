package kr.co.i4way.genesys.model;

import java.util.ArrayList;
import java.util.List;

import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentGroup;
import com.google.gson.JsonElement;

public class LogOutInfoVo {

	private String agentLogin;
	private String dn;
	private String reason;
	
	
	public String getAgentLogin() {
		return agentLogin;
	}
	public void setAgentLogin(String agentLogin) {
		this.agentLogin = agentLogin;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Override
	public String toString() {
		return "LogOutInfoVo [agentLogin=" + agentLogin + ", dn=" + dn + ", reason=" + reason + "]";
	}
	
	
	 
	
}
