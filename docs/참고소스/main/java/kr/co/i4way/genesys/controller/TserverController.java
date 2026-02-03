package kr.co.i4way.genesys.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genesyslab.platform.commons.protocol.ChannelState;
import com.google.gson.JsonParser;

import kr.co.i4way.common.util.JsonUtil;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.LogOutInfoVo;
import kr.co.i4way.genesys.tserver.TsvrCtrl;
import net.minidev.json.JSONObject;


/**
 * Place용 Controller
 * @author jkhong
 *
 */
@RestController
@CrossOrigin(origins = "*")
public class TserverController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	private GenesysInfoVo genesysinfovo;
	private TsvrCtrl ts = null;
	
	@Value("${ccc-service.enc-iv}")
    private String enc_iv;
	
	@Value("${ccc-service.enc-key}")
    private String enc_key;
	
	@Value("${ccc-service.enc-yn}")
    private String enc_yn;
	
	//상담사 원격로그아웃 송신 IF
	@RequestMapping("/PGICCC2080")
	public JSONObject logout(@RequestBody Map<String, Object> param) throws Exception{
		JSONObject rtnobj = new JSONObject();
		LogOutInfoVo logoutvo = null;

		String result = "F";
		String result_msg = "";
		try {
			logoutvo = getParseLogOutInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			if(connectTserver()) {
				ts.register(logoutvo.getDn());
				ts.logout(logoutvo.getDn(), "");
				ts.unregister(logoutvo.getDn());
				result = "T";
				result_msg = "";
			}
			rtnobj.put("RESULT", result);
			rtnobj.put("RESULT_MSG", result_msg);
		}catch(Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", e.getMessage());
		}finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
			if(disConnectTserver()) {				
			}
		}
		return rtnobj;
	}	
	
	//상담사 원격대기 송신 IF
	@RequestMapping("/PGICCC22081")
	public JSONObject ready(@RequestBody Map<String, Object> param) throws Exception{
		JSONObject rtnobj = new JSONObject();
		LogOutInfoVo logoutvo = null;

		String result = "F";
		String result_msg = "";
		try {
			logoutvo = getParseLogOutInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			if(connectTserver()) {
				ts.register(logoutvo.getDn());
				ts.ready(logoutvo.getDn());
				ts.unregister(logoutvo.getDn());
				result = "T";
				result_msg = "";
			}
			rtnobj.put("RESULT", result);
			rtnobj.put("RESULT_MSG", result_msg);
		}catch(Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", e.getMessage());
		}finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
			if(disConnectTserver()) {				
			}
		}
		return rtnobj;
	}

	//상담사 원격이석 송신 IF
	@RequestMapping("/PGICCC2190")
	public JSONObject notReady(@RequestBody Map<String, Object> param) throws Exception{
		JSONObject rtnobj = new JSONObject();
		LogOutInfoVo logoutvo = null;

		String result = "F";
		String result_msg = "";
		try {
			logoutvo = getParseLogOutInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			if(connectTserver()) {
				ts.register(logoutvo.getDn());
				ts.notReady(logoutvo.getDn(), logoutvo.getReason());
				ts.unregister(logoutvo.getDn());
				result = "T";
				result_msg = "";
			}
			rtnobj.put("RESULT", result);
			rtnobj.put("RESULT_MSG", result_msg);
		}catch(Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("RESULT_MSG", e.getMessage());
		}finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
			if(disConnectTserver()) {				
			}
		}
		return rtnobj;
	}	
	
	//상담사 상태확인 송신 IF
	@RequestMapping("/PGICCC2999")
	public JSONObject checkAgentStatus(@RequestBody Map<String, Object> param) throws Exception{
		JSONObject rtnobj = new JSONObject();
		LogOutInfoVo logoutvo = null;

		String result = "F";
		String login_id = "";
		String result_msg = "";
		try {
			logoutvo = getParseLogOutInfo(JsonUtil.decrypt_param(param, enc_key, enc_iv, enc_yn));
			if(connectTserver()) {
				login_id = ts.register(logoutvo.getDn());
				ts.unregister(logoutvo.getDn());
			}
			rtnobj.put("RESULT", "T");
			rtnobj.put("LOGIN_ID", login_id);
			rtnobj.put("RESULT_MSG", result_msg);
		}catch(Exception e) {
			rtnobj.put("RESULT", "F");
			rtnobj.put("LOGIN_ID", login_id);
			rtnobj.put("RESULT_MSG", e.getMessage());
		}finally {
			logger.info(rtnobj.toJSONString());
			rtnobj = JsonUtil.encrypt_param(rtnobj, enc_key, enc_iv, enc_yn);
			if(disConnectTserver()) {				
			}
		}
		return rtnobj;
	}	
	
	private String getParam(Map<String, Object> param, String qryStr, String type){
		String rtnstr = "";
		if(type.equals("I")){
			rtnstr = "0";
		}
		try{
			rtnstr = param.get(qryStr).toString();
		}catch(Exception e){ 
			//rtnstr = "";
		}
		return rtnstr;
	}
	
	// param의 id 오브젝트 -> 문자열 파싱 -> jsonElement 파싱
	private LogOutInfoVo getParseLogOutInfo(Map<String, Object> param){
		LogOutInfoVo logoutvo = new LogOutInfoVo();
		try {
			
			logoutvo.setAgentLogin(getParam(param, "AGENT_LOGINS", "S"));
			logoutvo.setDn(getParam(param, "INLINE_NUM", "S"));
			logoutvo.setReason(getParam(param, "REASON_CODE", "S"));
		}catch(Exception ex){
			logoutvo = null;
		}
		return logoutvo;
	}
	
	
	/**
	 * TServer에 접속한다.
	 * @return boolean
	 */
	private boolean connectTserver(){
		boolean rtnval = false;
		try {
			ts = TsvrCtrl.getInstance(genesysinfovo);
			if(ts != null){
					ts.openProtocol(genesysinfovo);
					if(ts.protocol != null) {
						ts.protocol.toString();
						System.out.println("TServer service Is Opend");
					}else {
						System.out.println("TServer service Is null");
					}
					rtnval = true;
			}else{
				System.out.println("ts is null");
			}
		} catch(Exception ex){
			ex.printStackTrace();
			rtnval = false;
		}
		return rtnval;
	}

	/**
	 * TServer와 접속을 해제한다.
	 * @return boolean
	 */
	public boolean disConnectTserver(){
		boolean rtnval = false;
		try {
			ts = TsvrCtrl.getInstance(genesysinfovo);
			if(ts != null){
				if(ts.protocol.getState() == ChannelState.Opened){
					ts.closeProtocol();
					System.out.println("TServer service Is Closed");
					rtnval = true;
				}else{
					System.out.println("TServer service Already Close");
				}
			}else{
				System.out.println("ts is null");
			}
		} catch(Exception ex){
			ex.printStackTrace();
			rtnval = false;
		}
		return rtnval;
	}
}
