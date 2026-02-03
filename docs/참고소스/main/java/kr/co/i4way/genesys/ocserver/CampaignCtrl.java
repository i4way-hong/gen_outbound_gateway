package kr.co.i4way.genesys.ocserver;

import java.net.URI;
import java.net.URISyntaxException;

import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.outbound.protocol.OutboundServerProtocol;
import com.genesyslab.platform.outbound.protocol.outboundserver.DialMode;
import com.genesyslab.platform.outbound.protocol.outboundserver.OptimizationMethod;
import com.genesyslab.platform.outbound.protocol.outboundserver.events.EventCampaignLoaded;
import com.genesyslab.platform.outbound.protocol.outboundserver.events.EventCampaignStatus;
import com.genesyslab.platform.outbound.protocol.outboundserver.events.EventCampaignUnloaded;
import com.genesyslab.platform.outbound.protocol.outboundserver.events.EventDialingStarted;
import com.genesyslab.platform.outbound.protocol.outboundserver.events.EventDialingStopped;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestForceUnloadCampaign;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestGetCampaignStatus;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestLoadCampaign;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestStartDialing;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestStopDialing;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestUnloadCampaign;

public class CampaignCtrl {

	URI outboundServerUri;
	public OutboundServerProtocol outboundServerProtocol;
	
	String ocServerUri = null;
	String clientName = null;
	String clientPw = null;
	String appName = null;
	String appPw = null;
	
	
	public void initalize(String svrUri, String app_nm, String appPw, String cl_nm, String cl_pw){
		ocServerUri = svrUri;
		clientName = cl_nm;
		clientPw = cl_pw;
		appName = app_nm; 
		appPw = appPw;
		
		
	}
	
	public void openProtocol(){
		try {
			outboundServerUri = new URI(ocServerUri);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		outboundServerProtocol =
				 new OutboundServerProtocol(
				  new Endpoint(
						  outboundServerUri));
		
		outboundServerProtocol.setClientName(clientName);
		outboundServerProtocol.setClientPassword(clientPw);
		outboundServerProtocol.setUserName(appName);
		outboundServerProtocol.setUserPassword(appPw);
		
				try {
					outboundServerProtocol.open();
				} catch (RegistrationException e) {
					e.printStackTrace();
				} catch (ProtocolException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	}
	
	public void closeProtocol(){
		if(outboundServerProtocol.getState() == ChannelState.Opened){
			try {
				outboundServerProtocol.close();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getCampaignStat(int campaignDBID, int agentGroupDBID)
	{
		String rtnval = "";
		if(outboundServerProtocol.getState() == ChannelState.Opened){
			RequestGetCampaignStatus req = 
					RequestGetCampaignStatus.create();
			req.setCampaignId(campaignDBID);
			req.setGroupId(agentGroupDBID);
			
			Message msg = null;
			try {
				msg = outboundServerProtocol.request(req);
				if ( msg != null && msg.messageId() == EventCampaignLoaded.ID )
				{
					EventCampaignLoaded evt = (EventCampaignLoaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStarted.ID )
				{
					EventDialingStarted evt = (EventDialingStarted)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStopped.ID )
				{
					EventDialingStopped evt = (EventDialingStopped)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignUnloaded.ID )
				{
					EventCampaignUnloaded evt = (EventCampaignUnloaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignStatus.ID )
				{
					EventCampaignStatus evt = (EventCampaignStatus)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else
				{
					rtnval = "Error_4";
				}
			} catch (ProtocolException e) {
				rtnval = "Error_1";
			} catch (IllegalStateException e) {
				rtnval = "Error_2";
			}
		}
		else
		{
			rtnval = "Error_3";
		}
		return rtnval;
	}
	
	public String LoadCampaign(int campaignDBID, int agentGroupDBID)
	{
		String rtnval = "";
		if(outboundServerProtocol.getState() == ChannelState.Opened){
			RequestLoadCampaign req = 
				RequestLoadCampaign.create();
			req.setCampaignId(campaignDBID);
			req.setGroupId(agentGroupDBID);

			Message msg = null;
			try {
				msg = outboundServerProtocol.request( req );
				if ( msg != null && msg.messageId() == EventCampaignLoaded.ID )
				{
					EventCampaignLoaded evt = (EventCampaignLoaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStarted.ID )
				{
					EventDialingStarted evt = (EventDialingStarted)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStopped.ID )
				{
					EventDialingStopped evt = (EventDialingStopped)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignUnloaded.ID )
				{
					EventCampaignUnloaded evt = (EventCampaignUnloaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignStatus.ID )
				{
					EventCampaignStatus evt = (EventCampaignStatus)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else
				{
					rtnval = "Error_4";
				}
			} catch (ProtocolException e) {
				rtnval = "Error_1";
			} catch (IllegalStateException e) {
				rtnval = "Error_2";
			}
		}
		else
		{
			rtnval = "Error_3";
		}
		return rtnval;
	}

	public String UnloadCampaign(int campaignDBID, int agentGroupDBID)
	{
		String rtnval = "";
		if(outboundServerProtocol.getState() == ChannelState.Opened){
			RequestUnloadCampaign req = 
				RequestUnloadCampaign.create(); 
			req.setCampaignId(campaignDBID);
			req.setGroupId(agentGroupDBID);
			
			Message msg = null;
			try {
				msg = outboundServerProtocol.request( req );
				if ( msg != null && msg.messageId() == EventCampaignLoaded.ID )
				{
					EventCampaignLoaded evt = (EventCampaignLoaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStarted.ID )
				{
					EventDialingStarted evt = (EventDialingStarted)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStopped.ID )
				{
					EventDialingStopped evt = (EventDialingStopped)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignUnloaded.ID )
				{
					EventCampaignUnloaded evt = (EventCampaignUnloaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignStatus.ID )
				{
					EventCampaignStatus evt = (EventCampaignStatus)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else
				{
					rtnval = "Error_4";
				}
			} catch (ProtocolException e) {
				rtnval = "Error_1";
			} catch (IllegalStateException e) {
				rtnval = "Error_2";
			}
		}
		else
		{
			rtnval = "Error_3";
		}
		return rtnval;
	}
	

	public String ForceUnloadCampaign(int campaignDBID, int agentGroupDBID)
	{
		String rtnval = "";
		if(outboundServerProtocol.getState() == ChannelState.Opened){
			RequestForceUnloadCampaign req = 
					RequestForceUnloadCampaign.create(); 
			req.setCampaignId(campaignDBID);
			req.setGroupId(agentGroupDBID);
			
			Message msg = null;
			try {
				msg = outboundServerProtocol.request( req );
				if ( msg != null && msg.messageId() == EventCampaignLoaded.ID )
				{
					EventCampaignLoaded evt = (EventCampaignLoaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStarted.ID )
				{
					EventDialingStarted evt = (EventDialingStarted)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStopped.ID )
				{
					EventDialingStopped evt = (EventDialingStopped)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignUnloaded.ID )
				{
					EventCampaignUnloaded evt = (EventCampaignUnloaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignStatus.ID )
				{
					EventCampaignStatus evt = (EventCampaignStatus)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else
				{
					rtnval = "Error_4";
				}
			} catch (ProtocolException e) {
				rtnval = "Error_1";
			} catch (IllegalStateException e) {
				rtnval = "Error_2";
			}


		}
		else
		{
			rtnval = "Error_3";
		}
		return rtnval;
	}
	
	private OptimizationMethod GetOptimizationMethod( String strOptimizationMethod )
	{
		if(strOptimizationMethod.equals("BusyFactor")){
			return OptimizationMethod.BusyFactor;
		}else if(strOptimizationMethod.equals("OverdialRate")){
			return OptimizationMethod.OverdialRate;
		}else if(strOptimizationMethod.equals("WaitTime")){
			return OptimizationMethod.WaitTime;
		}else{
			return OptimizationMethod.NoOptimizationMethod;
		}
	}
	
	private DialMode GetDialingMode( String strDialMode )
	{
		if(strDialMode.equals("Predict")){
			return DialMode.Predict;
		}else if(strDialMode.equals("PredictAndSeize")){
			return DialMode.PredictAndSeize;
		}else if(strDialMode.equals("Preview")){
			return DialMode.Preview;
		}else if(strDialMode.equals("Progress")){
			return DialMode.Progress;
		}else if(strDialMode.equals("ProgressAndSeize")){
			return DialMode.ProgressAndSeize;
		}else{
			return DialMode.NoDialMode;
		}
	}
	
	public String StartDialing(int campaignDBID, int agentGroupDBID, String dialingMode, String optimizeMethod, int optimizeGoal )
	{
		String rtnval = "";
		if(outboundServerProtocol.getState() == ChannelState.Opened){
			RequestStartDialing req = 
				RequestStartDialing.create();
			req.setCampaignId(campaignDBID);
			req.setGroupId(agentGroupDBID);
			req.setDialMode(GetDialingMode(dialingMode));
			req.setOptimizeBy(GetOptimizationMethod(optimizeMethod));
			req.setOptimizeGoal(optimizeGoal);
				
			Message msg = null;
			try {
				msg = outboundServerProtocol.request( req );
				if ( msg != null && msg.messageId() == EventCampaignLoaded.ID )
				{
					EventCampaignLoaded evt = (EventCampaignLoaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStarted.ID )
				{
					EventDialingStarted evt = (EventDialingStarted)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStopped.ID )
				{
					EventDialingStopped evt = (EventDialingStopped)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignUnloaded.ID )
				{
					EventCampaignUnloaded evt = (EventCampaignUnloaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignStatus.ID )
				{
					EventCampaignStatus evt = (EventCampaignStatus)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else
				{
					System.out.println(msg.messageId());
					rtnval = "Error_4";
				}
			} catch (ProtocolException e) {
				rtnval = "Error_1";
			} catch (IllegalStateException e) {
				rtnval = "Error_2";
			}
		}
		else
		{
			rtnval = "Error_3";
		}
		return rtnval;
	}
	
	public String StopDialing(int campaignDBID, int agentGroupDBID)
	{
		String rtnval = "";
		if(outboundServerProtocol.getState() == ChannelState.Opened){
			RequestStopDialing req = 
				RequestStopDialing.create();
			
			req.setCampaignId(campaignDBID);
			req.setGroupId(agentGroupDBID);

			Message msg = null;
			try {
				msg = outboundServerProtocol.request( req );
				if ( msg != null && msg.messageId() == EventCampaignLoaded.ID )
				{
					EventCampaignLoaded evt = (EventCampaignLoaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStarted.ID )
				{
					EventDialingStarted evt = (EventDialingStarted)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventDialingStopped.ID )
				{
					EventDialingStopped evt = (EventDialingStopped)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignUnloaded.ID )
				{
					EventCampaignUnloaded evt = (EventCampaignUnloaded)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else if ( msg != null && msg.messageId() == EventCampaignStatus.ID )
				{
					EventCampaignStatus evt = (EventCampaignStatus)msg;
					rtnval = evt.getGroupCampaignStatus().toString();
				}
				else
				{
					rtnval = "Error_4";
				}
			} catch (ProtocolException e) {
				rtnval = "Error_1";
			} catch (IllegalStateException e) {
				rtnval = "Error_2";
			}
		}
		else
		{
			rtnval = "Error_3";
		}
		return rtnval;
	}
	
	
}
