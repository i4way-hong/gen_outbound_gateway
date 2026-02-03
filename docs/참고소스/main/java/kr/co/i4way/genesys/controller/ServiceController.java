package kr.co.i4way.genesys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.genesyslab.platform.commons.protocol.ChannelState;
import kr.co.i4way.genesys.cfgserver.initConfigService;

@Controller
@CrossOrigin(origins = "*")
public class ServiceController {
	initConfigService initconfigservice = initConfigService.getInstance();
	
	@ResponseBody
	@RequestMapping("/service/resetService")
	public String resetService() throws Exception{
		return initconfigservice.checkAndConnectConfigService();
	}

	@ResponseBody
	@RequestMapping("/service/checkService")
	public String checkService() throws Exception{
	   	return initconfigservice.checkConfService().name();
	}	
}
