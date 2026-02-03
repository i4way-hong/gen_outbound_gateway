package kr.co.i4way.genesys.statserver;

import java.net.URI;
import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.genesyslab.platform.reporting.protocol.statserver.Notification;
import com.genesyslab.platform.reporting.protocol.statserver.NotificationMode;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticMetric;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticObject;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticObjectType;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventError;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventInfo;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestCloseStatistic;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestOpenStatistic;

public class StatCtrl {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	URI statServerUri;
	public StatServerProtocol protocol;
	public WarmStandbyService wsService;
	public EventInfo eventinfo;
	String stat_ip_p = null;
	String stat_ip_b = null;
	String stat_ep_p = null;
	String stat_ep_b = null;
	int stat_port_p = 0;
	int stat_port_b = 0;
	
	String clientName = null;
	String appName = null;
	String char_set = null;
	
	String object_id = null;
	String curr_stat = null;
	
	private int delay_msec;
	private int timeout_msec;
	
	public void initalize(String ip_p, String ip_b, String ep_p, String ep_b, int port_p, int port_b, String app_nm, String cl_nm, String charset, String obj_id, String stat, int timeout_msec, int delay_msec){
		stat_ip_p = ip_p;
		stat_ip_b = ip_b;
		stat_ep_p = ep_p;
		stat_ep_b = ep_b;
		stat_port_p = port_p;
		stat_port_b = port_b;
		clientName = cl_nm;
		appName = app_nm; 
		char_set = charset;
		object_id = obj_id;
		curr_stat = stat;
		this.timeout_msec = timeout_msec;
		this.delay_msec = delay_msec;
	}
	
	/**
	 * Stat Server와 연결방법을 설정하고 연결한다. 
	 */
	public void openProtocol() {

		PropertyConfiguration connConf = new PropertyConfiguration();
		connConf.setUseAddp(false); // - ConnectionConfiguration typified options
									// like "UseAddp", "AddpClientTimeout", etc
		connConf.setAddpServerTimeout(1);
		connConf.setAddpClientTimeout(1);
		connConf.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, char_set);

		Endpoint endpoint = new Endpoint(stat_ep_p, stat_ip_p,
				stat_port_p, connConf); // - Target server host/port are here
		Endpoint endpointBackup = new Endpoint(stat_ep_b, stat_ip_b,
				stat_port_b, connConf); // - Backup server host/port
													// are here
		protocol = new StatServerProtocol(endpoint);
		protocol.setClientName(clientName); // - Protocol handshake typified
												// options like "ClientName",
												// etc
		protocol.setTimeout(timeout_msec);

//		//웜스텐바이 설정
//		WarmStandbyConfiguration wsConf = new WarmStandbyConfiguration(
//				endpoint, endpointBackup);
//		wsConf.setAttempts((short) 5); // - WarmStandby typified options like
//										// "WarmStandbyAttempts", etc
//		wsConf.setTimeout(2000);
//		wsService = new WarmStandbyService(protocol);
//		wsService.applyConfiguration(wsConf);
//		wsService.start();
//		wsService.addListener(new WarmStandbyListener() {		//warm Standby 연결시 이벤트 리스너 설정
//			public void onStateChanged(final WarmStandbyStateChangedEvent event) {		//연결상태 변경시
//				onWarmStandbyStateChanged(event);
//			}
//
//			@Override
//			public void onSwitchover(EventObject event) {
//				onWarmStandbySwitchedOver(event);
//				
//			}
//		});
		
		protocol.addChannelListener(new ChannelListener() {		//채널에 Listener가 추가됐을때
			public void onChannelOpened(final EventObject event) {	//채널Open
				onProtocolChannelOpened(event);
			}

			public void onChannelClosed(final ChannelClosedEvent event) {	//채널Close
				onProtocolChannelClosed(event);
			}

			public void onChannelError(final ChannelErrorEvent event) {		//채널 Error
				//onProtocolChannelError(event);
			}
		});
		
		protocol.setMessageHandler(new MessageHandler() {	//메세지 발생시 처리
			public void onMessage(final Message message) {
				if(message.messageName().equals("EventInfo")){		//메세지 명이 EventInfo이면
					formatStatistics(message);	//통계 데이터 처리 메서드 호출
				}else if(message.messageName().equals("EventError")){		//메세지 명이 EventInfo이면
					EventError eventerror = (EventError) message;	
					logger.error(eventerror.toString());
					closeProtocol();
				}
			}
		});

		try {
			protocol.open();
			logger.info("protocol open!");
		} catch (RegistrationException e) {
			logger.error("openProtocol.RegistrationException", e);
		} catch (ProtocolException e) {
			logger.error("openProtocol.ProtocolException", e);
		} catch (IllegalStateException e) {
			logger.error("openProtocol.IllegalStateException", e);
		} catch (InterruptedException e) {
			logger.error("openProtocol.InterruptedException", e);
		}
	}

	public void closeProtocol(){
		try {
			protocol.close(true);
			//wsService.stop();
			logger.info("protocol close!");
		} catch (RegistrationException e) {
			logger.error("closeProtocol.RegistrationException", e);
		} catch (ProtocolException e) {
			logger.error("closeProtocol.RegistrationException", e);
		} catch (IllegalStateException e) {
			logger.error("closeProtocol.RegistrationException", e);
		} catch (InterruptedException e) {
			logger.error("closeProtocol.RegistrationException", e);
		}
	}
	
	private void onProtocolChannelOpened(final EventObject event) {
		//setStatistics_GA(object_id, curr_stat);
	}

	private void onProtocolChannelClosed(final ChannelClosedEvent event) {
	}
	
	private void onWarmStandbySwitchedOver(final EventObject event) {
	}

	private void onWarmStandbyStateChanged(
			final WarmStandbyStateChangedEvent event) {
	}

	private void formatStatistics(Message message) {
		eventinfo = (EventInfo) message;
		//logger.info(eventinfo.toString());
		//closeProtocol();
	}
	
	public void setStatistics_GA(String object_id, String stat) {
		setstat(object_id, StatisticObjectType.GroupAgents,	stat, "Default",
					"", "",NotificationMode.Periodical, 1);
	} 
	
	public void setstat(String objectid, StatisticObjectType statobjtype,
			String stattype, String t_profile, String t_range, String t_filter,
			NotificationMode notimode, int refId) {
		try {
			RequestOpenStatistic requestOpenStatistic = RequestOpenStatistic
					.create();

			StatisticObject object = StatisticObject.create();
			object.setObjectId(objectid);
			object.setObjectType(statobjtype);
			object.setTenantName("Environment");
			object.setTenantPassword("");

			StatisticMetric metric = StatisticMetric.create();
			metric.setStatisticType(stattype);
			metric.setTimeProfile(t_profile);
			metric.setTimeRange(t_range);
			metric.setFilter(t_filter);

			Notification notification = Notification.create();
			notification.setMode(notimode);
			
			notification.setFrequency(10);

			requestOpenStatistic.setStatisticObject(object);
			requestOpenStatistic.setStatisticMetric(metric);
			requestOpenStatistic.setNotification(notification);

			requestOpenStatistic.setReferenceId(refId);

//			if(protocol.getState().equals(ChannelState.Opened)) {
//				protocol.request(requestOpenStatistic);
//			}

			RequestCloseStatistic closereq = RequestCloseStatistic.create();
			closereq.setReferenceId(refId);

			if(protocol.getState().equals(ChannelState.Opened)) {
				protocol.request(requestOpenStatistic);
				Thread.sleep(delay_msec);
				protocol.send(closereq);			
				protocol.beginClose();
				logger.info("protocol close!!");
			}
			
			//protocol.send(requestOpenStatistic);
			//work_logger.info("Sending:\n" + requestOpenStatistic);
			
//			RequestCloseStatistic closereq = RequestCloseStatistic.create();
//			closereq.setStatisticId(refId);
//
//			Message msg = protocol.request(requestOpenStatistic);
//
//			if(msg.messageName().equals("EventInfo")){		//메세지 명이 EventInfo이면
//				formatStatistics(msg);	//통계 데이터 처리 메서드 호출
//			}
//			protocol.send(closereq);			
//			protocol.beginClose();
			
			
			//Message msg = null;
			//msg = protocol.request(requestOpenStatistic);
			//eventinfo = (EventInfo) msg;		
			//logger.info(eventinfo.toString());
			//formatStatistics(msg);
			requestOpenStatistic = null;
			notification = null;
			object = null;
			metric = null;
			//msg = null;
		} catch (Exception e) {
			logger.error("setstat.Exception", e);
			closeProtocol();
		}
		finally {
		}
	}
}
