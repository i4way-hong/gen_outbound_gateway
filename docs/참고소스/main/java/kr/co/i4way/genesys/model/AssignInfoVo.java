package kr.co.i4way.genesys.model;

import java.util.ArrayList;
import java.util.List;

import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentGroup;
import com.google.gson.JsonElement;

public class AssignInfoVo {

	List<String> fail_emp_id;
	CfgAgentGroup agentgroup;
	
	public List<String> getFail_emp_id() {
		return fail_emp_id;
	}
	public void setFail_emp_id(List<String> fail_emp_id) {
		this.fail_emp_id = fail_emp_id;
	}
	public CfgAgentGroup getAgentgroup() {
		return agentgroup;
	}
	public void setAgentgroup(CfgAgentGroup agentgroup) {
		this.agentgroup = agentgroup;
	}
	@Override
	public String toString() {
		return "AssignInfoVo [fail_emp_id=" + fail_emp_id + ", agentgroup=" + agentgroup + "]";
	} 

	
}
