package kr.co.i4way.genesys.statserver;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.reporting.protocol.StatServerProtocol;
import com.genesyslab.platform.reporting.protocol.statserver.Notification;
import com.genesyslab.platform.reporting.protocol.statserver.NotificationMode;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticMetric;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticObject;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticObjectType;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventStatisticOpened;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestCloseStatistic;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestOpenStatistic;

import kr.co.i4way.genesys.model.GenesysInfoVo;

public class StatCtrl2 {
	//20240415수정
	private int rid = 0;
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public void setStatistics_GA(StatServerProtocol protocol, GenesysInfoVo genesysinfovo, String object_id, String stat) {
		setstat(protocol, object_id, StatisticObjectType.GroupAgents,	stat, "Default",
					"", "",NotificationMode.Periodical, numberGen(6), genesysinfovo.getStat_delay());
	} 
	
	private static int numberGen(int len) {
		Random rand = new Random();
		int createNum = 0;
		String resultStr = "";
		int returnNum = 0;
		
		for (int i=0;  i<len; i++) {
			createNum = rand.nextInt(9);
			resultStr += Integer.toString(createNum);
		}
		returnNum =  Integer.parseInt(resultStr);
	
		return returnNum;
	}
	
	public void setstat(StatServerProtocol protocol, String objectid, StatisticObjectType statobjtype,
			String stattype, String t_profile, String t_range, String t_filter,
			NotificationMode notimode, int refId, int delay_msec) {
		RequestOpenStatistic requestOpenStatistic;
		StatisticObject object;
		StatisticMetric metric;
		Notification notification;
		RequestCloseStatistic closereq;
		Message msg;
		
		//20240415수정 closeStat으로 분리
		rid = refId;
		try {
			requestOpenStatistic = RequestOpenStatistic
					.create();

			object = StatisticObject.create();
			object.setObjectId(objectid);
			object.setObjectType(statobjtype);
			object.setTenantName("Environment");
			object.setTenantPassword("");

			metric = StatisticMetric.create();
			metric.setStatisticType(stattype);
			metric.setTimeProfile(t_profile);
			metric.setTimeRange(t_range);
			metric.setFilter(t_filter);

			notification = Notification.create();
			notification.setMode(notimode);
			
			notification.setFrequency(10);

			requestOpenStatistic.setStatisticObject(object);
			requestOpenStatistic.setStatisticMetric(metric);
			requestOpenStatistic.setNotification(notification);

			requestOpenStatistic.setReferenceId(refId);

			//20240415수정 closeStat으로 분리
			closereq = RequestCloseStatistic.create();
			closereq.setReferenceId(refId);
			closereq.setStatisticId(refId);

			if(protocol.getState().equals(ChannelState.Opened)) {
				logger.info("========================================== setStat ==========================================");
				msg = protocol.request(requestOpenStatistic);
				//logger.info("refId : " + refId + " send ok!");
				if(msg.messageName().equals("EventStatisticOpened")){		//메세지 명이 Statistic open, close이면
					EventStatisticOpened eventStatisticOpened = (EventStatisticOpened) msg;
					logger.info(msg.messageName() + "(ReferenceId : " + eventStatisticOpened.getReferenceId() + ", Timestamp : " + eventStatisticOpened.getTimestamp() + ")");
				}
				Thread.sleep(delay_msec);
				logger.info("========================================= closeStat =========================================");
				protocol.send(closereq);
			}
		} catch (Exception e) {
			logger.error("refId : " + refId + " has error...");
			logger.error("setstat.Exception", e);
		}
		finally {
			requestOpenStatistic = null;
			closereq = null;
			object = null;
			metric = null;
			notification = null;
		}
	}
	
//	/**
//	 * 20240415수정 closeStat으로 분리
//	 * @param protocol
//	 * @param refId
//	 */
//	public void closeStat(StatServerProtocol protocol) {
//		RequestCloseStatistic closereq = null; 
//		Message msg;
//		try {
//			closereq = RequestCloseStatistic.create();
//			closereq.setReferenceId(rid);
//			closereq.setStatisticId(rid);
//			logger.info("========================================= closeStat =========================================");
//			if(protocol.getState().equals(ChannelState.Opened)) {
//				msg = protocol.request(closereq);
//			}
//		}catch(Exception e) {
//			logger.error("closeStat.Exception", e);
//			try {
//				protocol.close(true);
//			}catch(ProtocolException e1) {
//				logger.error("closeStat.Exception", e1);
//			}catch(IllegalStateException e2) {
//				logger.error("closeStat.IllegalStateException", e2);
//			}catch(InterruptedException e3) {
//				logger.error("closeStat.InterruptedException", e3);
//			}
//		}
//		finally {
//			closereq = null;
//		}
//		
//	}
}
