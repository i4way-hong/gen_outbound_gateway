package kr.co.i4way.genesys.scserver;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyListener;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyStateChangedEvent;
import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.management.protocol.SolutionControlServerProtocol;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.ControlObjectType;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.events.EventInfo;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.requests.RequestSubscribe;

import kr.co.i4way.genesys.model.GenesysInfoVo;

public class ScsNotify {
	String desc = "";			//Description
	int ctrl_dbid = 0;		//ControlObjectId
	int ctrl_status = 0;	//ControlStatus
	String exec_mode = "";		//ExecutionMode

	public interface scsSvrCallback {
		void scsCallback(String desc, int ctrl_dbid, int ctrl_status, String exec_mode);
	}

    public void setScsSvrCallback(scsSvrCallback callback) {
		scssvrcallback = callback;
	}

	private volatile static ScsNotify m_scsNotifyInstance;
    private scsSvrCallback scssvrcallback;

    static CfgAppType clientType = CfgAppType.CFGStatServer;
	SolutionControlServerProtocol protocol;
	EventInfo eventinfo = null;
	List<String> target_servers;
    public ScsNotify(GenesysInfoVo genesysinfovo){
		openProtocol(genesysinfovo);
		target_servers = genesysinfovo.getSc_monitoring_dbids();
    }

	public static ScsNotify getInstance(GenesysInfoVo genesysinfovo) {
	    if(m_scsNotifyInstance == null) {
            synchronized(ScsNotify.class) {
	            if(m_scsNotifyInstance == null) {
					m_scsNotifyInstance = new ScsNotify(genesysinfovo); 
	            }
	        }
	    }
	    return m_scsNotifyInstance;
    }
     
    private void openProtocol(GenesysInfoVo genesysinfovo) {

		PropertyConfiguration connConf = new PropertyConfiguration();
		connConf.setUseAddp(true); 
		connConf.setAddpServerTimeout(10);
		connConf.setAddpClientTimeout(10);
		connConf.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, genesysinfovo.getSc_charset());

		Endpoint endpoint = new Endpoint(genesysinfovo.getSc_endpoint_p(), genesysinfovo.getSc_ip_p(),
		genesysinfovo.getSc_port_p(), connConf);
		Endpoint endpointBackup = new Endpoint(genesysinfovo.getSc_endpoint_b(), genesysinfovo.getSc_ip_b(),
		genesysinfovo.getSc_port_b(), connConf);

		protocol = new SolutionControlServerProtocol(endpoint);
		protocol.setClientName(genesysinfovo.getSc_client_name());

		protocol.setClientId(genesysinfovo.getSc_client_dbid());
		protocol.setUserName(genesysinfovo.getSc_id());
		//웜스텐바이 설정
		WarmStandbyConfiguration wsConf = new WarmStandbyConfiguration(
				endpoint, endpointBackup);
		wsConf.setAttempts((short) 5); // - WarmStandby typified options like
										// "WarmStandbyAttempts", etc
		wsConf.setTimeout(2000);
		WarmStandbyService wsService = new WarmStandbyService(protocol);
		wsService.applyConfiguration(wsConf);
		wsService.start();
		wsService.addListener(new WarmStandbyListener() {		//warm Standby 연결시 이벤트 리스너 설정
			public void onStateChanged(final WarmStandbyStateChangedEvent event) {		//연결상태 변경시
				onWarmStandbyStateChanged(event);
			}

			@Override
			public void onSwitchover(EventObject event) {//연결된 Stat Server가 Switch Over 됐을때
				onWarmStandbySwitchedOver(event);
				
			}
		});
		protocol.addChannelListener(new ChannelListener() {		//채널에 Listener가 추가됐을때
			public void onChannelOpened(final EventObject event) {	//채널Open
				onProtocolChannelOpened(event);
			}

			public void onChannelClosed(final ChannelClosedEvent event) {	//채널Close
				onProtocolChannelClosed(event);
			}

			public void onChannelError(final ChannelErrorEvent event) {		//채널 Error
				onProtocolChannelError(event);
			}
		});
		MessageHandler scsrMessageHandler = new MessageHandler() {
			public void onMessage(Message message) {
			  switch(message.messageId()){
				case EventInfo.ID: 
					OnEventInfo(message);
					//break;
				//Other events.
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

	private void closeProtocol(){
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

	private void onWarmStandbySwitchedOver(final EventObject event) {
		System.out.println("onWarmStandbySwitchedOver");
	}

	private void onWarmStandbyStateChanged(
			final WarmStandbyStateChangedEvent event) {
        System.out.println("onWarmStandbyStateChanged");
	}

	private void onProtocolChannelOpened(final EventObject event) {
		System.out.println("onProtocolChannelOpened_scs");
		for(int i=0; i<target_servers.size(); i++){
			String val = target_servers.get(i);
			setAppInfo(Integer.parseInt(val));
		}
	}

	private void onProtocolChannelClosed(final ChannelClosedEvent event) {
		System.out.println("onProtocolChannelClosed");
	}

	private void onProtocolChannelError(final ChannelErrorEvent event) {
		System.out.println("onProtocolChannelError");
		System.out.println(event.toString());
	}

    private void OnEventInfo(Message message){
		eventinfo = (EventInfo) message;
		desc = "";			//Description
		ctrl_dbid = 0;		//ControlObjectId
		ctrl_status = 0;	//ControlStatus
		exec_mode = "";		//ExecutionMode
		desc = eventinfo.getDescription();
		ctrl_dbid = eventinfo.getControlObjectId();
		ctrl_status = eventinfo.getControlStatus();
		exec_mode = eventinfo.getExecutionMode().toString();
		scssvrcallback.scsCallback(desc, ctrl_dbid,  ctrl_status, exec_mode);	//
	}

	private void setAppInfo(int dbid){

		try {
			RequestSubscribe requestSubscribeApp = RequestSubscribe.create();
			requestSubscribeApp.setControlObjectType(ControlObjectType.Application);
			requestSubscribeApp.setControlObjectId(dbid);
			protocol.send(requestSubscribeApp);
		} catch (ProtocolException | IllegalStateException e) {
			e.printStackTrace();
		}
	}
}
