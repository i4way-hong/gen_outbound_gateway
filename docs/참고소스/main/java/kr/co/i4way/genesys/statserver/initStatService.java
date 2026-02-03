package kr.co.i4way.genesys.statserver;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyListener;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyStateChangedEvent;
import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.reporting.protocol.StatServerProtocol;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventError;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventInfo;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventStatisticClosed;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventStatisticOpened;

import kr.co.i4way.genesys.model.GenesysInfoVo;

public class initStatService {
	public StatServerProtocol protocol;
	public WarmStandbyService wsService;
	public EventInfo eventinfo;
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	private GenesysInfoVo genesysinfovo;

	private static initStatService m_initStatServiceInstance = null;
	
	public initStatService() {
	}
	
	public static initStatService getInstance() {
        if (m_initStatServiceInstance == null)
        	m_initStatServiceInstance = new initStatService();
        return m_initStatServiceInstance;         
    }

	public ChannelState openStatService(GenesysInfoVo vo) throws RegistrationException, ProtocolException, IllegalStateException, InterruptedException {
		genesysinfovo = vo;
		
		if(checkStatService() == ChannelState.Opened) {
			return ChannelState.Opened;
		}
		
		PropertyConfiguration config = new PropertyConfiguration();
		config.setUseAddp(true);
		config.setAddpClientTimeout(5);
		config.setAddpServerTimeout(5);
		config.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, genesysinfovo.getCfg_charset());

		Endpoint statServerEndpoint = new Endpoint(genesysinfovo.getStat_endpoint1(), genesysinfovo.getStat_ip_p(),
				genesysinfovo.getStat_port_p(), config);
		
		Endpoint statServerEndpoint_b = new Endpoint(genesysinfovo.getStat_endpoint2(), genesysinfovo.getStat_ip_b(),
				genesysinfovo.getStat_port_b(), config);
		
		protocol = new StatServerProtocol(statServerEndpoint);
		protocol.addChannelListener(new ChannelListener() {
			@Override
			public void onChannelClosed(ChannelClosedEvent arg0) {
				protocolClosed(arg0);
			}
			@Override
			public void onChannelError(ChannelErrorEvent arg0) {
				protocolError(arg0);
			}
			@Override
			public void onChannelOpened(EventObject arg0) {
				protocolOpend(arg0);
			}
		});

		protocol.setClientName(genesysinfovo.getStat_clientname());
		protocol.setTimeout(genesysinfovo.getStat_timeout());
		
//		//웜스텐바이 설정
//		WarmStandbyConfiguration wsConf = new WarmStandbyConfiguration(
//				statServerEndpoint, statServerEndpoint_b);
//		wsConf.setAttempts((short) 5); // - WarmStandby typified options like
//										// "WarmStandbyAttempts", etc
//		wsConf.setTimeout(2000);
//		wsService = new WarmStandbyService(protocol);
//		wsService.applyConfiguration(wsConf);
//		wsService.start();
//		wsService.addListener(new WarmStandbyListener() {
//			@Override
//			public void onSwitchover(EventObject event) {
//				onWarmStandbySwitchedOver(event);
//			}
//			@Override
//			public void onStateChanged(WarmStandbyStateChangedEvent event) {
//				onWarmStandbyStateChanged(event);
//			}
//		});
		
		protocol.setMessageHandler(new MessageHandler() {	//메세지 발생시 처리
			public void onMessage(final Message message) {
				if(message.messageName().equals("EventInfo")){		//메세지 명이 EventInfo이면
					//logger.info("eventinfo.toString()");
					eventinfo = (EventInfo) message;
					logger.info("getReferenceId: " + eventinfo.getReferenceId());
					//logger.info(eventinfo.toString());
				}else if(message.messageName().equals("EventError")){		//메세지 명이 EventError이면
					EventError eventerror = (EventError) message;	
					logger.error(eventerror.toString());
				}else if(message.messageName().equals("EventStatisticOpened")){		//메세지 명이 Statistic open, close이면
					EventStatisticOpened eventStatisticOpened = (EventStatisticOpened) message;
					logger.info(message.messageName() + " (ReferenceId : " + eventStatisticOpened.getReferenceId() + ", Timestamp : " + eventStatisticOpened.getTimestamp() + ")");
				}else if(message.messageName().equals("EventStatisticClosed")){		//메세지 명이 Statistic open, close이면
					EventStatisticClosed eventStatisticClosed = (EventStatisticClosed) message;
					logger.info(message.messageName() + " (ReferenceId : " + eventStatisticClosed.getReferenceId() + ", Timestamp : " + eventStatisticClosed.getTimestamp() + ")");
				}else {
					logger.info(message.toString());
				}
			}
		});

		try {
			protocol.open();
			//logger.info("protocol open!");
		} catch (RegistrationException e) {
			logger.error("openProtocol.RegistrationException", e);
		} catch (ProtocolException e) {
			logger.error("openProtocol.ProtocolException_P", e);
			protocol = new StatServerProtocol(statServerEndpoint_b);
			try {
				logger.error("ReConnect");
				protocol.open();
			}catch (ProtocolException ex) {
				logger.error("openProtocol.ProtocolException_B", ex);
			}
		} catch (IllegalStateException e) {
			logger.error("openProtocol.IllegalStateException", e);
		} catch (InterruptedException e) {
			logger.error("openProtocol.InterruptedException", e);
		}
		return protocol.getState();
	}
	
//	private void onWarmStandbySwitchedOver(final EventObject event) {
//		logger.info("Warm Standby Switched Over!");
//	}
//
//	private void onWarmStandbyStateChanged(
//			final WarmStandbyStateChangedEvent event) {
//		logger.info("Warm Standby State Change!");
//	}

	private void protocolClosed(ChannelClosedEvent args) {
		logger.info("protocol close!!");
	}
	private void protocolError(ChannelErrorEvent args) {
		logger.info("protocol error");
	}
	private void protocolOpend(EventObject args) {
		logger.info("protocol open!");
	}	

	public void closeStatService() throws ProtocolException, IllegalStateException, InterruptedException {
		try {
			if (protocol.getState() != ChannelState.Closed) {
				protocol.close();
				
			}
			protocol = null;
		}catch(Exception ex) {
			logger.error("Exception", ex);
		}
	}

	public String checkAndConnectStatService() throws RegistrationException, ProtocolException, IllegalStateException {
		String rtnStr = "null";
		try {
			if(protocol != null) {
				if (protocol.getState() != ChannelState.Closed) {
					closeStatService();
					rtnStr = "close";
				}
			}
		} catch (ProtocolException | IllegalStateException | InterruptedException e) {
			logger.error("Exception", e);
			rtnStr = "error_close";
		}
		try {
			if(genesysinfovo != null) {
				openStatService(genesysinfovo);
				rtnStr = "open";
			}
		} catch (InterruptedException e) {
			logger.error("ConfigException | InterruptedException", e);
			rtnStr = "error_open";
		}
		return rtnStr;
	}

	public ChannelState checkStatService() {
		ChannelState rtnState = ChannelState.Closed;
		if(protocol == null) {
			rtnState = ChannelState.Closed;
		}else {
			rtnState = protocol.getState();
		}
		return rtnState;
	}
}
