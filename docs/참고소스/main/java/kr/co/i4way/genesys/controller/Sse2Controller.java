package kr.co.i4way.genesys.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import com.genesyslab.platform.commons.protocol.ChannelState;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import kr.co.i4way.genesys.cfgserver.ConfigNotify;
import kr.co.i4way.genesys.cfgserver.initConfigService;
import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.genesys.model.QueryVo;
import kr.co.i4way.genesys.scserver.ScsCtrl;
import kr.co.i4way.genesys.scserver.ScsNotify;

@RestController
public class Sse2Controller {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	private static Sse2Controller m_sse2ControllerInstance = null;
	public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	public List<SseEmitter> emitters_scs = new CopyOnWriteArrayList<>();

	@Autowired
	private GenesysInfoVo genesysinfovo;

	@Value("${genesysinfo.cfg_ip_p}")
	private String cfg_ip_p;
	initConfigService initconfigservice = initConfigService.getInstance();
	ConfigNotify confignotify;
	ScsNotify scsnotify;
	ScsCtrl scsctrl;
	public Sse2Controller(){
	}

	//Bean 객체가 생성된 직후 초기화 할때 사용함..
	@PostConstruct
	private void initConfigNotify(){
		System.out.println("PASS1");
		try {
			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
				
			}
//			if (initconfigservice.openConfigService(genesysinfovo) == ChannelState.Opened) {
//				confignotify = new ConfigNotify(initconfigservice.service, genesysinfovo);
//				ConfigNotify.cfgSvrCallback callback_cfgsvr = new ConfigNotify.cfgSvrCallback() {
//					@Override
//					public void cfgCallback(String action_type, String obj_type, String obj_dbid, int folder_dbid) {
//						sendCfgEvent(action_type, obj_type, obj_dbid, folder_dbid);
//					}
//				};
//				confignotify.setCfgSvrCallback(callback_cfgsvr);
//			}
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
		}
		System.out.println("PASS2");
//		try {
//			scsnotify = new ScsNotify(genesysinfovo);
//			ScsNotify.scsSvrCallback callback_scssvr = new ScsNotify.scsSvrCallback(){
//				@Override
//				public void scsCallback(String desc, int ctrl_dbid, int ctrl_status, String exec_mode) {
//					sendScsEvent(desc, ctrl_dbid, ctrl_status, exec_mode);
//				}
//			};
//			scsnotify.setScsSvrCallback(callback_scssvr);
//		} catch (Exception e) {
//			logger.error("Exception", e);
//		} finally {
//		}
		System.out.println("PASS3");
	}

	public static Sse2Controller getInstance() {
        if (m_sse2ControllerInstance == null){
			m_sse2ControllerInstance = new Sse2Controller();
		}
        return m_sse2ControllerInstance;
    }

	@CrossOrigin
    @RequestMapping(value = "/notify/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		sendInitEvent(emitter);
		emitters.add(emitter);

		emitter.onCompletion(() -> emitters.remove(emitter));
		emitter.onTimeout(() -> emitters.remove(emitter));
		emitter.onCompletion(() -> emitters.remove(emitter));

        return emitter;
    }

	@CrossOrigin
	@RequestMapping(value = "/notify/subscribe_scs", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe_scs() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		sendInitEvent(emitter);
		emitters_scs.add(emitter);

		emitter.onCompletion(() -> emitters_scs.remove(emitter));
		emitter.onTimeout(() -> emitters_scs.remove(emitter));
		emitter.onCompletion(() -> emitters_scs.remove(emitter));

        return emitter;
    }

	@CrossOrigin
	@RequestMapping(value = "/solutionControl/getAppInfo")
    public String getAppInfo() {
		JSONObject rtnval = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		scsctrl = ScsCtrl.getInstance(genesysinfovo);
		if(scsctrl.protocol.getState() == ChannelState.Closed){
			scsctrl.openProtocol(genesysinfovo);
		}
		if(scsctrl.protocol.getState() == ChannelState.Opened){
			List<String> dbids = genesysinfovo.getSc_monitoring_dbids();
			for(int i=0; i<dbids.size(); i++){
				rtnval =  new JSONObject();
				int dbid = Integer.parseInt(dbids.get(i));
				rtnval = scsctrl.requestApplicationInfo(dbid);
				jsonArray.put(rtnval);
			}
		}
		String eventFormatted = jsonArray.toString();
		return eventFormatted;
	}

	@CrossOrigin
	@RequestMapping(value = "/solutionControl/requestStartApplication")
    public String requestStartApplication(@RequestBody QueryVo vo) {
		
		scsctrl = ScsCtrl.getInstance(genesysinfovo);
		if(scsctrl.protocol.getState() == ChannelState.Closed){
			scsctrl.openProtocol(genesysinfovo);
		}
		if(scsctrl.protocol.getState() == ChannelState.Opened){
			scsctrl.requestStartApplication(vo.getDbid());
		}
		return "";
	}

	@CrossOrigin
	@RequestMapping(value = "/solutionControl/requestStopApplication")
    public String requestStopApplication(@RequestBody QueryVo vo) {
		scsctrl = ScsCtrl.getInstance(genesysinfovo);
		if(scsctrl.protocol.getState() == ChannelState.Closed){
			scsctrl.openProtocol(genesysinfovo);
		}
		if(scsctrl.protocol.getState() == ChannelState.Opened){
			scsctrl.requestStopApplication(vo.getDbid());
		}
		return "";
	}

	@CrossOrigin
	@RequestMapping(value = "/solutionControl/requestStopGracefulApplication")
    public String requestStopGracefulApplication(@RequestBody QueryVo vo) {
		scsctrl = ScsCtrl.getInstance(genesysinfovo);
		if(scsctrl.protocol.getState() == ChannelState.Closed){
			scsctrl.openProtocol(genesysinfovo);
		}
		if(scsctrl.protocol.getState() == ChannelState.Opened){
			scsctrl.requestStopApplicationGracefully(vo.getDbid());
		}
		return "";
	}

	@CrossOrigin
	@RequestMapping(value = "/solutionControl/requestChangeExecutionMode")
    public String requestChangeExecutionMode(@RequestBody QueryVo vo) {
		scsctrl = ScsCtrl.getInstance(genesysinfovo);
		if(scsctrl.protocol.getState() == ChannelState.Closed){
			scsctrl.openProtocol(genesysinfovo);
		}
		if(scsctrl.protocol.getState() == ChannelState.Opened){
			scsctrl.requestChangeExecutionMode(vo.getDbid(), vo.getExec_mode());
		}
		return "";
	}


    @PostMapping(value = "/notify/dispatchEvent")
    public void dispatchEventToClients(@RequestParam String title, @RequestParam String text){
        String eventFormatted = new JSONObject()
			.put("title",title)
			.put("text",text).toString();
		for(SseEmitter emitter : emitters){
            try{
                emitter.send(SseEmitter.event().name("notifyEvent").data(eventFormatted));
            }catch(IOException ex){
				emitters.remove(emitter);
            }
        }
    }

	private void sendCfgEvent(String action_type, String obj_type, String obj_dbid, int folder_dbid){
		String eventFormatted = new JSONObject()
			.put("action_type",action_type)
			.put("obj_type",obj_type)
			.put("obj_dbid",obj_dbid)
			.put("folder_dbid",folder_dbid).toString();
		for(SseEmitter emitter : emitters){
            try{
                emitter.send(SseEmitter.event().name("notifyEvent").data(eventFormatted));
            }catch(IOException ex){
				emitters.remove(emitter);
            }
        }
	}

	private void sendScsEvent(String desc, int ctrl_dbid, int ctrl_status, String exec_mode){
		String eventFormatted = new JSONObject()
			.put("desc",desc)
			.put("ctrl_dbid",ctrl_dbid)
			.put("ctrl_status",ctrl_status)
			.put("exec_mode",exec_mode).toString();
		for(SseEmitter emitter : emitters_scs){
            try{
                emitter.send(SseEmitter.event().name("notifyEvent").data(eventFormatted));
            }catch(IOException ex){
				emitters_scs.remove(emitter);
            }
        }
	}

	private void sendInitEvent (SseEmitter emitter){
		try{
			emitter.send(SseEmitter.event().name("INIT"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}