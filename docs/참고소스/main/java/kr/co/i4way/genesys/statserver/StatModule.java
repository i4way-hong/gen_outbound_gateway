package kr.co.i4way.genesys.statserver;

import com.genesyslab.platform.commons.protocol.ChannelState;

public class StatModule {
//
//	StatCtrl cc = null;
//	String 	ocSvrIp = ""; 
//	int 	ocSvrPort = 0;
//	String 	ocSvrId = "";
//	String 	ocSvrPasswd = "";
//	String  ocClientName = "";
//	String  ocClientPasswd = ""; 
//	String 	charSet = "";
//
//	/**
//	 * 아웃바운드서버 접속정보를 세팅한다.
//	 * @param String svrIp		CTI서버 IP
//	 * @param int svrPort	CTI서비스 Port
//	 * @param String charset	케릭터셋  
//	 */
//	public void setOcServerInfo(String svrIp, int svrPort, String charset){
//		ocSvrIp = svrIp;
//		ocSvrPort = svrPort;
//		ocSvrId = "ocserver";
//		ocSvrPasswd = null;
//		ocClientName = "default";
//		ocClientPasswd = "";
//		String mOCServerURI = "tcp://"+svrIp+":" + svrPort;
//		cc = new StatCtrl();
//		cc.initalize(mOCServerURI, ocSvrId, ocSvrPasswd, ocClientName, ocClientPasswd);
//	}
//	
//	/**
//	 * OutboundContact 서버에 접속한다.
//	 * @return boolean
//	 */
//	public boolean connectOcServer(){
//		boolean rtnval = false;
//		try {
//			if(cc != null){
//					cc.openProtocol();
//					if(cc.outboundServerProtocol != null) {
//						cc.outboundServerProtocol.getState().toString();
//						System.out.println("OcServer service Is Opend");
//					}else {
//						System.out.println("OcServer service Is null");
//					}
//					rtnval = true;
//			}else{
//				System.out.println("cc is null");
//			}
//		} catch(Exception ex){
//			ex.printStackTrace();
//			rtnval = false;
//		}
//		return rtnval;
//	}
//
//	/**
//	 * OutboundContact 서버와 접속을 해제한다.
//	 * @return boolean
//	 */
//	public boolean disConnectOcServer(){
//		boolean rtnval = false;
//		try {
//			if(cc != null){
//				if(cc.outboundServerProtocol.getState() == ChannelState.Opened){
//					cc.closeProtocol();
//					System.out.println("OcServer service Is Closed");
//					rtnval = true;
//				}else{
//					System.out.println("OcServer service Already Close");
//				}
//			}else{
//				System.out.println("cc is null");
//			}
//		} catch(Exception ex){
//			ex.printStackTrace();
//			rtnval = false;
//		}
//		return rtnval;
//	}
//	
//	/**
//	 * 캠페인 상태를 확인한다
//	 * @return boolean
//	 */
//	public String getCampaignStat(int campaignDBID, int agentGroupDBID){
//		String rtnval = "";
//		try {
//			if(cc != null){
//				if(cc.outboundServerProtocol.getState() == ChannelState.Opened){
//					rtnval = cc.getCampaignStat(campaignDBID, agentGroupDBID);
//				}else{
//					System.out.println("OcServer service is Not Opend");
//				}
//			}else{
//				System.out.println("cc is null");
//			}
//		} catch(Exception ex){
//			ex.printStackTrace();
//			rtnval = "";
//		}
//		return rtnval;
//	}
//
//	/**
//	 * 캠페인을 로드한다
//	 * @return String
//	 */
//	public String loadCampaign(int campaignDBID, int agentGroupDBID){
//		String rtnval = "";
//		try {
//			if(cc != null){
//				if(cc.outboundServerProtocol.getState() == ChannelState.Opened){
//					rtnval = cc.LoadCampaign(campaignDBID, agentGroupDBID);
//				}else{
//					System.out.println("OcServer service is Not Opend");
//				}
//			}else{
//				System.out.println("cc is null");
//			}
//		} catch(Exception ex){
//			ex.printStackTrace();
//			rtnval = "";
//		}
//		return rtnval;
//	}
//	
//	/**
//	 * 캠페인을 언로드한다
//	 * @return String
//	 */
//	public String unloadCampaign(int campaignDBID, int agentGroupDBID){
//		String rtnval = "";
//		try {
//			if(cc != null){
//				if(cc.outboundServerProtocol.getState() == ChannelState.Opened){
//					rtnval = cc.UnloadCampaign(campaignDBID, agentGroupDBID);
//				}else{
//					System.out.println("OcServer service is Not Opend");
//				}
//			}else{
//				System.out.println("cc is null");
//			}
//		} catch(Exception ex){
//			ex.printStackTrace();
//			rtnval = "";
//		}
//		return rtnval;
//	}
//	
//	/**
//	 * 캠페인을 강제 언로드한다
//	 * @return String
//	 */
//	public String forceUnloadCampaign(int campaignDBID, int agentGroupDBID){
//		String rtnval = "";
//		try {
//			if(cc != null){
//				if(cc.outboundServerProtocol.getState() == ChannelState.Opened){
//					rtnval = cc.ForceUnloadCampaign(campaignDBID, agentGroupDBID);
//				}else{
//					System.out.println("OcServer service is Not Opend");
//				}
//			}else{
//				System.out.println("cc is null");
//			}
//		} catch(Exception ex){
//			ex.printStackTrace();
//			rtnval = "";
//		}
//		return rtnval;
//	}
//	
//	/**
//	 * 다이얼링을 시작한다
//	 * @return String
//	 */
//	public String startDialing(int campaignDBID, int agentGroupDBID, String dialingMode, String optimizeMethod, int optimizeGoal){
//		String rtnval = "";
//		try {
//			if(cc != null){
//				if(cc.outboundServerProtocol.getState() == ChannelState.Opened){
//					rtnval = cc.StartDialing(campaignDBID, agentGroupDBID, dialingMode, optimizeMethod, optimizeGoal);
//				}else{
//					System.out.println("OcServer service is Not Opend");
//				}
//			}else{
//				System.out.println("cc is null");
//			}
//		} catch(Exception ex){
//			ex.printStackTrace();
//			rtnval = "";
//		}
//		return rtnval;
//	}
//	
//
//	/**
//	 * 다이얼링을 정지한다
//	 * @return String
//	 */
//	public String stopDialing(int campaignDBID, int agentGroupDBID){
//		String rtnval = "";
//		try {
//			if(cc != null){
//				if(cc.outboundServerProtocol.getState() == ChannelState.Opened){
//					rtnval = cc.StopDialing(campaignDBID, agentGroupDBID);
//				}else{
//					System.out.println("OcServer service is Not Opend");
//				}
//			}else{
//				System.out.println("cc is null");
//			}
//		} catch(Exception ex){
//			ex.printStackTrace();
//			rtnval = "";
//		}
//		return rtnval;
//	}
}
