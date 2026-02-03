package kr.co.i4way.genesys.cfgserver;

import java.util.Collection;

import com.genesyslab.platform.applicationblocks.com.ConfEvent;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.NotificationFilter;
import com.genesyslab.platform.applicationblocks.com.NotificationQuery;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaFolder;
import com.genesyslab.platform.applicationblocks.com.objects.CfgObjectID;
import com.genesyslab.platform.applicationblocks.commons.Predicate;
import com.genesyslab.platform.applicationblocks.commons.broker.Subscriber;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.i4way.genesys.controller.Sse2Controller;
import kr.co.i4way.genesys.model.GenesysInfoVo;

public class ConfigNotify {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    public interface cfgSvrCallback {
		void cfgCallback(String action_type, String obj_type, String obj_dbids, int folder_dbid);
	}

    public void setCfgSvrCallback(cfgSvrCallback callback) {
		cfgsvrcallback = callback;
	}

	private volatile static ConfigNotify m_configNotifyInstance;
    private cfgSvrCallback cfgsvrcallback;
    IConfService confservice = null;
    mCfgEventHandler mSubscriber = null;

	public ConfigNotify(IConfService service, GenesysInfoVo genvo){
        confservice = service;
        initNotifying(genvo);
	}

    private void initNotifying(GenesysInfoVo genvo){
        //registerNotification(confservice, genvo.getCfg_tenant_dbid(), CfgObjectType.CFGAgentGroup);
        //registerNotification(confservice, genvo.getCfg_tenant_dbid(), CfgObjectType.CFGPerson);
        //registerNotification(confservice, genvo.getCfg_tenant_dbid(), CfgObjectType.CFGAgentLogin);
        //registerNotification(confservice, genvo.getCfg_tenant_dbid(), CfgObjectType.CFGSkill);
        //registerNotification(confservice, genvo.getCfg_tenant_dbid(), CfgObjectType.CFGDN);
        registerNotification(confservice, genvo.getCfg_tenant_dbid(), CfgObjectType.CFGFolder);
    }
	
	public static ConfigNotify getInstance(IConfService service, GenesysInfoVo genesysinfovo) {
	    if(m_configNotifyInstance == null) {
            synchronized(ConfigNotify.class) {
	            if(m_configNotifyInstance == null) {
	         	    m_configNotifyInstance = new ConfigNotify(service, genesysinfovo); 
	            }
	        }
	    }
	    return m_configNotifyInstance;
    }

    public void registerNotification(IConfService service, int pTenantDBID, CfgObjectType type){

		NotificationQuery mNotificationQuery = new NotificationQuery();
		mNotificationQuery.setObjectType(type);
		mNotificationQuery.setTenantDbid(pTenantDBID);

		NotificationFilter mFilter = new NotificationFilter(mNotificationQuery) ;

		mSubscriber = new mCfgEventHandler(mFilter) ;
		service.register(mSubscriber);
		try {
			service.subscribe(mNotificationQuery);
		} catch(NullPointerException ex){
			logger.error("ConfigNotify.registerNotification NullPointerException 에러 : " + ex.getMessage());
		}catch (ConfigException e) {
			logger.error("ConfigNotify.registerNotification ConfigException 에러 : " + e.getMessage());
		}
	}
	
	class mCfgEventHandler implements Subscriber<ConfEvent>{

		private Predicate<ConfEvent> mFilter;
		private Sse2Controller controller = null;
		public mCfgEventHandler(Predicate<ConfEvent> filter){
			this.mFilter = filter; 
			controller = Sse2Controller.getInstance();
		} 

		public void handle(ConfEvent cfgEvent){
			try{
				logger.info("-----------------------------------------------");
				if (cfgEvent.getEventType() == ConfEvent.EventType.ObjectUpdated|| cfgEvent.getEventType() == ConfEvent.EventType.ObjectDeleted){
					if(cfgEvent.getObjectType().equals(CfgObjectType.CFGFolder)){
                        String obj_type = "CFGFolder";
						String obj_dbid = "";
						String action_type = cfgEvent.getEventType().name();
						CfgDeltaFolder fl = null;
						Collection<CfgObjectID> obj;
						fl = (CfgDeltaFolder) cfgEvent.getCfgObject();
						obj = fl.getAddedObjectIDs();
						if(obj != null){	//add된 오브젝트가 없으면 delete된  오브젝트 확인
							for(CfgObjectID cfgobject : obj){
								obj_type = cfgobject.getType().name();
								obj_dbid += cfgobject.getDBID();
							}
						}else{
							obj = fl.getDeletedObjectIDs();
							if(obj != null){
								for(CfgObjectID cfgobject : obj){
									obj_type = cfgobject.getType().name();
									obj_dbid += cfgobject.getDBID();
								}
							}
						}
						//if(obj_type.equals("CFGPerson") || obj_type.equals("CFGAgentGroup")){
						//if(Cond.str(obj_type).notIn("CFGPerson", "CFGAgentGroup")){
						if(Cond.str(obj_type).in("CFGPerson", "CFGAgentGroup", "CFGAgentLogin", "CFGSkill", "CFGDN", "CFGFolder")){
							cfgsvrcallback.cfgCallback(action_type, obj_type,  obj_dbid, fl.getDBID());
						}
					}
				}
				logger.info("-----------------------------------------------");
			}catch(NullPointerException ex){
				logger.error("mCfgEventHandler.handle NullPointerException 에러 : " + ex.getMessage());
			}catch(Exception ex){
				logger.error("mCfgEventHandler.handle 에러 : " + ex.getMessage());
			}
		}

        public Predicate<ConfEvent> getFilter()
		{
			return mFilter;
		}
    }
}
 
class Cond {
    public static StrCond str(String s) {
        return new StrCond(s);
    }

    public static class StrCond {
        private String value;

        public StrCond(String value) {
            this.value = value;
        }

        public boolean in(String ... values) {
            for (String v : values) {
                if (v.equals(value)) return true;
            }
            return false;
        }

        public boolean notIn(String ... values) {
            for (String v : values) {
                if (v.equals(value)) return false;
            }
            return true;
        }
    }
}