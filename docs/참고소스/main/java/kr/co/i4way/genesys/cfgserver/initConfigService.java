package kr.co.i4way.genesys.cfgserver;

import java.util.EventObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;

import kr.co.i4way.genesys.model.GenesysInfoVo;
import kr.co.i4way.util.AES256;

public class initConfigService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	private GenesysInfoVo genesysinfovo;
	public IConfService service;

	private static initConfigService m_initConfigServiceInstance = null;
	
	public initConfigService() {
	}
	
	public static initConfigService getInstance() {
        if (m_initConfigServiceInstance == null)
        	m_initConfigServiceInstance = new initConfigService();
        return m_initConfigServiceInstance;         
    }
	
	public ChannelState openConfigService(GenesysInfoVo vo) throws ConfigException, InterruptedException {
		genesysinfovo = vo;
		
		if(checkConfService() == ChannelState.Opened) {
			return ChannelState.Opened;
		}
		
		PropertyConfiguration config = new PropertyConfiguration();
		config.setUseAddp(true);
		config.setAddpClientTimeout(5);
		config.setAddpServerTimeout(5);
		config.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, genesysinfovo.getCfg_charset());

		Endpoint cfgServerEndpoint = new Endpoint(genesysinfovo.getCfg_endpoint_p(), genesysinfovo.getCfg_ip_p(),
				genesysinfovo.getCfg_port_p(), config);
		
		ConfServerProtocol protocol = new ConfServerProtocol(cfgServerEndpoint);
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

		protocol.setClientName(genesysinfovo.getCfg_client_name());
		protocol.setClientApplicationType(CfgAppType.CFGSCE.ordinal());
		protocol.setUserName(genesysinfovo.getCfg_id());
		protocol.setUserPassword(genesysinfovo.getCfg_passwd());

		service = ConfServiceFactory.createConfService(protocol);
		try {
			logger.info("Primary서버로 접속시도 : IP=" + genesysinfovo.getCfg_ip_p() + ", Port=" + genesysinfovo.getCfg_port_p());
			protocol.open();
			logger.info(service.getProtocol().getEndpoint().getConfiguration().toString());

			Integer cfgServerEncoding = protocol.getServerContext().getServerEncoding();
			if (cfgServerEncoding != null && cfgServerEncoding.intValue() == 1) {
				logger.info("Charset을 UTF-8로 변경");
				protocol.close();
				config.setStringsEncoding("UTF-8");
				protocol.setEndpoint(new Endpoint(genesysinfovo.getCfg_endpoint_p(), genesysinfovo.getCfg_ip_p(),
						genesysinfovo.getCfg_port_p(), config));
				protocol.open();
			} else {
				logger.info("Charset 변경안함");
			}
		} catch (ProtocolException e) {
			logger.error("ProtocolException", e);
			logger.info("Backup서버로 접속시도 : IP=" + genesysinfovo.getCfg_ip_b() + ", Port=" + genesysinfovo.getCfg_port_b());
			protocol.setEndpoint(new Endpoint(genesysinfovo.getCfg_endpoint_b(), genesysinfovo.getCfg_ip_b(),
					genesysinfovo.getCfg_port_b(), config));
			try {
				protocol.open();
				logger.info(service.getProtocol().getEndpoint().getConfiguration().toString());

				Integer cfgServerEncoding = protocol.getServerContext().getServerEncoding();
				if (cfgServerEncoding != null && cfgServerEncoding.intValue() == 1) {
					logger.info("Charset을 UTF-8로 변경");
					protocol.close();
					config.setStringsEncoding("UTF-8");
					protocol.setEndpoint(new Endpoint(genesysinfovo.getCfg_endpoint_b(), genesysinfovo.getCfg_ip_b(),
							genesysinfovo.getCfg_port_b(), config));
					protocol.open();
				} else {
					logger.info("Charset 변경안함");
				}
			} catch (ProtocolException | IllegalStateException e1) {
				logger.error("ProtocolException | IllegalStateException", e1);
			}
		}
		return service.getProtocol().getState();
	}

	private void protocolClosed(ChannelClosedEvent args) {
		logger.info("protocol Closed");
	}
	private void protocolError(ChannelErrorEvent args) {
		logger.info("protocol Error");
	}
	private void protocolOpend(EventObject args) {
		logger.info("protocol Opend");
	}	

	public void closeConfigService() throws ProtocolException, IllegalStateException, InterruptedException {
		try {
			if (service.getProtocol().getState() != ChannelState.Closed) {
				service.getProtocol().close();
				
			}
			ConfServiceFactory.releaseConfService(service);
			service = null;
		}catch(Exception ex) {
			logger.error("Exception", ex);
		}
	}

	public String checkAndConnectConfigService() {
		String rtnStr = "null";
		try {
			if(service != null) {
				if (service.getProtocol().getState() != ChannelState.Closed) {
					closeConfigService();
					rtnStr = "close";
				}
			}
		} catch (ProtocolException | IllegalStateException | InterruptedException e) {
			e.printStackTrace();
			rtnStr = "error_close";
		}
		try {
			if(genesysinfovo != null) {
				openConfigService(genesysinfovo);
				rtnStr = "open";
			}
		} catch (ConfigException | InterruptedException e) {
			logger.error("ConfigException | InterruptedException", e);
			rtnStr = "error_open";
		}
		return rtnStr;
	}

	public ChannelState checkConfService() {
		ChannelState rtnState = ChannelState.Closed;
		if(service == null) {
			rtnState = ChannelState.Closed;
		}else {
			rtnState = service.getProtocol().getState();
		}
		return rtnState;
	}
}
