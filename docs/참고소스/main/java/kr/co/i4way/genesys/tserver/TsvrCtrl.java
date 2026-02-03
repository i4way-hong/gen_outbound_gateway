package kr.co.i4way.genesys.tserver;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelClosedOnSendException;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.events.EventInfo;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.AddressType;
import com.genesyslab.platform.voice.protocol.tserver.AgentWorkMode;
import com.genesyslab.platform.voice.protocol.tserver.ControlMode;
import com.genesyslab.platform.voice.protocol.tserver.RegisterMode;
import com.genesyslab.platform.voice.protocol.tserver.events.EventRegistered;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentLogin;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentLogout;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentNotReady;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentReady;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestRegisterAddress;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestUnregisterAddress;

import kr.co.i4way.genesys.model.GenesysInfoVo;

public class TsvrCtrl {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	private volatile static TsvrCtrl m_tsvrNotifyInstance;

    static CfgAppType clientType = CfgAppType.CFGTServer;
	public TServerProtocol protocol;
	EventInfo eventinfo = null;
	List<String> target_servers;
	GenesysInfoVo genesysinfovo;
    public TsvrCtrl(GenesysInfoVo vo){
		genesysinfovo = vo;
		openProtocol(vo);
    }

	public static TsvrCtrl getInstance(GenesysInfoVo genesysinfovo) {
	    if(m_tsvrNotifyInstance == null) {
            synchronized(TsvrCtrl.class) {
	            if(m_tsvrNotifyInstance == null) {
					m_tsvrNotifyInstance = new TsvrCtrl(genesysinfovo); 
	            }
	        }
	    }
	    return m_tsvrNotifyInstance;
    }
     
	/**
	 * CTI서버와 접속한다.
	 * @param genesysinfovo
	 */
    public void openProtocol(GenesysInfoVo genesysinfovo) {

		PropertyConfiguration connConf = new PropertyConfiguration();
		connConf.setUseAddp(true); 
		connConf.setAddpServerTimeout(10);
		connConf.setAddpClientTimeout(10);
		connConf.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, genesysinfovo.getT_charset());

		Endpoint endpoint = new Endpoint(genesysinfovo.getT_endpoint_p(), genesysinfovo.getT_ip_p(),
		genesysinfovo.getT_port_p(), connConf);

		protocol = new TServerProtocol(endpoint);
		protocol.setClientName(genesysinfovo.getSc_client_name());

		MessageHandler tsvrMessageHandler = new MessageHandler() {
			public void onMessage(Message message) {
			  switch(message.messageId()){
				case EventInfo.ID: 
					logger.info("Message=" + message.toString());
			  }
			}
		  };
		protocol.setMessageHandler(tsvrMessageHandler);
		try {
			protocol.open();
		} catch (RegistrationException e) {
			System.out.println("RegistrationException");
		} catch (ProtocolException e) {
			System.out.println("ProtocolException");
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
		} catch (InterruptedException e) {
			System.out.println("InterruptedException");
		}
	}

    /**
     * CTI서버와의 연결을 끊는다
     */
	public void closeProtocol(){
		try {
			protocol.close(true);
		} catch (RegistrationException e) {
			System.out.println("RegistrationException");
		} catch (ProtocolException e) {
			System.out.println("ProtocolException");
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
		} catch (InterruptedException e) {
			System.out.println("InterruptedException");
		}
	}
	
	/**
	 * 레지스터
	 * @param dn
	 * @return CTI Login ID
	 */
	public String register(String dn) {
		Message response;
		RequestRegisterAddress request = RequestRegisterAddress.create(dn,
				RegisterMode.ModeShare, ControlMode.RegisterDefault, AddressType.DN);
		String returnStr = "";
		try {

			response = protocol.request(request);
			logger.info("Received: \n" + response.toString());
			EventRegistered regist = (EventRegistered)response;
			if(regist.getAgentID() != null) {
				returnStr = regist.getAgentID();
			}
		} catch (ChannelClosedOnSendException closedEx) {
			logger.error(String.format("Error happened: %s\n%s", closedEx.getMessage(), closedEx.toString()));
			closedEx.printStackTrace();
		} catch (Throwable e) {
			logger.error(String.format("Error happened: %s\n%s", e.getMessage(), e.toString()));
			e.printStackTrace();
		}
		return returnStr;
	}

	/**
	 * 언레지스터
	 * @param dn
	 */
	public void unregister(String dn) {
		Message response;
		RequestUnregisterAddress request = RequestUnregisterAddress.create(dn,
				ControlMode.RegisterDefault);
		try {
			response = protocol.request(request);
			logger.info("Received: \n" + response.toString());
		} catch (ChannelClosedOnSendException closedEx) {
			logger.error(String.format("Error happened: %s\n%s", closedEx.getMessage(), closedEx.toString()));
		} catch (Throwable e) {
			logger.error(String.format("Error happened: %s\n%s", e.getMessage(), e.toString()));
		}
	}

	/**
	 * 로그인
	 * @param dn
	 * @param queue
	 * @param agentId
	 * @param password
	 */
	public void login(String dn, String queue, String agentId, String password) {
		RequestAgentLogin request = RequestAgentLogin.create(dn, AgentWorkMode.Unknown, "",
				agentId, "", null, null);
		Message response;
		try {
			response = protocol.request(request);
			logger.info("Received: \n" + response.toString());
		} catch (ChannelClosedOnSendException closedEx) {
			logger.error(closedEx.getMessage() + "{{{{}}}}" +  closedEx.toString());
		} catch (Throwable e) {
			logger.error(e.getMessage() + "{{{{}}}}" +  e.toString());
		}
	}

	/**
	 * 로근아웃
	 * @param dn
	 * @param queue
	 */
	public void logout(String dn, String queue) {
		Message response;
		RequestAgentLogout request = RequestAgentLogout.create(dn, "", null, null);
		try {
			response = protocol.request(request);
			logger.info("Received: \n" + response.toString());
		} catch (ChannelClosedOnSendException closedEx) {
			logger.error(String.format("Error happened: %s\n%s", closedEx.getMessage(), closedEx.toString()));
		} catch (Throwable e) {
			logger.error(String.format("Error happened: %s\n%s", e.getMessage(), e.toString()));
		}
	}
	
	/**
	 * 상담사 상태변경(대기상태로...)
	 * @param dn
	 */
	public void ready(String dn) {
		Message response;
		AgentWorkMode workmode = AgentWorkMode.ManualIn;
		RequestAgentReady request = RequestAgentReady.create(dn, workmode);
		try {
			response = protocol.request(request);
			logger.info("Received: \n" + response.toString());
		} catch (ChannelClosedOnSendException closedEx) {
			logger.error(String.format("Error happened: %s\n%s", closedEx.getMessage(), closedEx.toString()));
		} catch (Throwable e) {
			logger.error(String.format("Error happened: %s\n%s", e.getMessage(), e.toString()));
		}
	}
	
	/**
	 * 상담사 로그인여부 확인
	 * @param dn
	 */
	public void checkStat(String dn) {
		Message response;
		AgentWorkMode workmode = AgentWorkMode.ManualIn;
		RequestAgentReady request = RequestAgentReady.create(dn, workmode);
		try {
			response = protocol.request(request);
			logger.info("Received: \n" + response.toString());
		} catch (ChannelClosedOnSendException closedEx) {
			logger.error(String.format("Error happened: %s\n%s", closedEx.getMessage(), closedEx.toString()));
		} catch (Throwable e) {
			logger.error(String.format("Error happened: %s\n%s", e.getMessage(), e.toString()));
		}
	}
	
	/**
	 * 상담사의 이석상태 변경
	 * @param dn
	 * @param reason_code
	 */
	public void notReady(String dn, String reason_code) {
		Message response;
		AgentWorkMode workmode = AgentWorkMode.ManualIn;
		KeyValueCollection kv = fnSetReason(reason_code);
		RequestAgentNotReady request = RequestAgentNotReady.create(dn, workmode, "", kv, kv);
		try {
			response = protocol.request(request);
			logger.info("Received: \n" + response.toString());
		} catch (ChannelClosedOnSendException closedEx) {
			logger.error(String.format("Error happened: %s\n%s", closedEx.getMessage(), closedEx.toString()));
		} catch (Throwable e) {
			logger.error(String.format("Error happened: %s\n%s", e.getMessage(), e.toString()));
		}
	}
	
	private KeyValueCollection fnSetReason(String rCode)
    {
        KeyValueCollection rtnValue = null;
        KeyValuePair kv_rcode = null;
        try
        {
            rtnValue = new KeyValueCollection();
            
            rtnValue.addString("ReasonCode", rCode);
        }
        catch (Exception ex)
        {
        }
        return rtnValue;

    }

}
