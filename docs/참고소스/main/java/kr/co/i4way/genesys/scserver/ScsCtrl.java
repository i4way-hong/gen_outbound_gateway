package kr.co.i4way.genesys.scserver;

import java.util.List;

import org.json.JSONObject;

import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.management.protocol.ApplicationExecutionMode;
import com.genesyslab.platform.management.protocol.SolutionControlServerProtocol;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.events.EventInfo;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.requests.applications.RequestChangeExecutionMode;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.requests.applications.RequestGetApplicationInfo;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.requests.applications.RequestStartApplication;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.requests.applications.RequestStopApplication;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.requests.applications.RequestStopApplicationGracefully;

import kr.co.i4way.genesys.model.GenesysInfoVo;

public class ScsCtrl {
	private volatile static ScsCtrl m_scsNotifyInstance;

    static CfgAppType clientType = CfgAppType.CFGStatServer;
	public SolutionControlServerProtocol protocol;
	EventInfo eventinfo = null;
	List<String> target_servers;
	GenesysInfoVo genesysinfovo;
    public ScsCtrl(GenesysInfoVo vo){
		genesysinfovo = vo;
		openProtocol(vo);
    }

	public static ScsCtrl getInstance(GenesysInfoVo genesysinfovo) {
	    if(m_scsNotifyInstance == null) {
            synchronized(ScsCtrl.class) {
	            if(m_scsNotifyInstance == null) {
					m_scsNotifyInstance = new ScsCtrl(genesysinfovo); 
	            }
	        }
	    }
	    return m_scsNotifyInstance;
    }
     
    public void openProtocol(GenesysInfoVo genesysinfovo) {

		PropertyConfiguration connConf = new PropertyConfiguration();
		connConf.setUseAddp(true); 
		connConf.setAddpServerTimeout(10);
		connConf.setAddpClientTimeout(10);
		connConf.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, genesysinfovo.getSc_charset());

		Endpoint endpoint = new Endpoint(genesysinfovo.getSc_endpoint_p(), genesysinfovo.getSc_ip_p(),
		genesysinfovo.getSc_port_p(), connConf);

		protocol = new SolutionControlServerProtocol(endpoint);
		protocol.setClientName(genesysinfovo.getSc_client_name());

		protocol.setClientId(genesysinfovo.getSc_client_dbid());
		protocol.setUserName(genesysinfovo.getSc_id());

		MessageHandler scsrMessageHandler = new MessageHandler() {
			public void onMessage(Message message) {
			  switch(message.messageId()){
				case EventInfo.ID: 
				System.out.println("Message=" + message.toString());
//					OnEventInfo(message);
			  }
			}
		  };
		protocol.setMessageHandler(scsrMessageHandler);
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

	public JSONObject requestApplicationInfo(int dbid){
		EventInfo info = null;
		JSONObject rtnjson = null;
			try {
				RequestGetApplicationInfo requestGetApplicationInfo =
				RequestGetApplicationInfo.create(dbid);
				info = (EventInfo)protocol.request(requestGetApplicationInfo);

				rtnjson = new JSONObject();
				rtnjson.put("desc",info.getDescription());
				rtnjson.put("ctrl_dbid",info.getControlObjectId());
				rtnjson.put("ctrl_status",info.getControlStatus());
				rtnjson.put("exec_mode",info.getExecutionMode().toString()).toString();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		return rtnjson;
	}

	public void requestStartApplication(int dbid){
			try {
				RequestStartApplication requestStartApplication = 
				RequestStartApplication.create(dbid);
				protocol.request(requestStartApplication);


/*
				if(msg.messageName().equals("EventInfo")){

				}
				info = (EventInfo)protocol.request(requestStartApplication);

				rtnjson = new JSONObject();
				rtnjson.put("desc",info.getDescription());
				rtnjson.put("ctrl_dbid",info.getControlObjectId());
				rtnjson.put("ctrl_status",info.getControlStatus());
				rtnjson.put("exec_mode",info.getExecutionMode().toString()).toString();
*/
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
	}

	public void requestStopApplication(int dbid){
			try {
				RequestStopApplication requestStopApplication = 
				RequestStopApplication.create(dbid);
				protocol.request(requestStopApplication);
/*
				rtnjson = new JSONObject();
				rtnjson.put("desc",info.getDescription());
				rtnjson.put("ctrl_dbid",info.getControlObjectId());
				rtnjson.put("ctrl_status",info.getControlStatus());
				rtnjson.put("exec_mode",info.getExecutionMode().toString()).toString();
*/
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
	}

	public void requestStopApplicationGracefully(int dbid){
			try {
				RequestStopApplicationGracefully requestStopApplicationGracefully = 
				RequestStopApplicationGracefully.create(dbid);
				protocol.request(requestStopApplicationGracefully);
/*
				rtnjson = new JSONObject();
				rtnjson.put("desc",info.getDescription());
				rtnjson.put("ctrl_dbid",info.getControlObjectId());
				rtnjson.put("ctrl_status",info.getControlStatus());
				rtnjson.put("exec_mode",info.getExecutionMode().toString()).toString();
*/
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
	}

	public void requestChangeExecutionMode(int dbid, String mode){
		ApplicationExecutionMode current_Mode = ApplicationExecutionMode.Exiting;
			try {
				if(mode.equals("BACKUP")){
					current_Mode = ApplicationExecutionMode.Primary;
				}else if(mode.equals("PRIMARY")){
					current_Mode = ApplicationExecutionMode.Backup;
				}
				RequestChangeExecutionMode requestChangeExecutionMode = 
				RequestChangeExecutionMode.create(dbid, current_Mode);
				protocol.request(requestChangeExecutionMode);
/*
				rtnjson = new JSONObject();
				rtnjson.put("desc",info.getDescription());
				rtnjson.put("ctrl_dbid",info.getControlObjectId());
				rtnjson.put("ctrl_status",info.getControlStatus());
				rtnjson.put("exec_mode",info.getExecutionMode().toString()).toString();
*/
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
	}
}
