package kr.co.i4way.genesys.cfgserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genesyslab.platform.applicationblocks.com.CfgObject;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentGroup;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAgentLogin;
import com.genesyslab.platform.applicationblocks.com.objects.CfgCampaign;
import com.genesyslab.platform.applicationblocks.com.objects.CfgDN;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTransaction;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;

import net.minidev.json.JSONObject;

public class ManageUserProperties {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	public ManageUserProperties() {
		
	}
	
	public JSONObject getUserProperties(CfgTransaction obj) {
		KeyValueCollection kv = null;
		JSONObject jsonObj = new JSONObject(); 
		JSONObject jsonObj2 = new JSONObject();
		JSONObject rtnObj = new JSONObject(); 
		String opt_name = null;
		kv = obj.getUserProperties();

		if(kv != null) {
			for(Object selObj : kv) {
				opt_name = "";
				KeyValuePair sectionKvp = (KeyValuePair) selObj;
				opt_name = sectionKvp.getStringKey();
	            jsonObj = new JSONObject();
				for (Object recordObj : sectionKvp.getTKVValue()) {
	                KeyValuePair recordKvp = (KeyValuePair) recordObj;
	    			jsonObj.put(recordKvp.getStringKey(), recordKvp.getStringValue());
	            }
				jsonObj2.put(opt_name, jsonObj);
			}
			rtnObj.put("Sections", jsonObj2);
			
		}
		//System.out.println(rtnObj.toJSONString());
		return rtnObj;
	}

	public CfgAgentGroup setUserProperties(CfgAgentGroup obj, String sec_name, String[] k, String[] v) {
		KeyValueCollection obj_kv = null;
		KeyValueCollection insert_kv = null;
		String opt_name = null;
		boolean update_flag = false;
		KeyValuePair sectionKvp = null;
		KeyValuePair tmpkvp = null;

		try {
			obj_kv = obj.getGroupInfo().getUserProperties();

			if(obj_kv != null && obj_kv.length() > 0) {
				for(Object selObj : obj_kv) {
					opt_name = "";
					sectionKvp = (KeyValuePair) selObj;
					opt_name = sectionKvp.getStringKey();
					if(sec_name.equals(opt_name)) {
						logger.info("update mode");
						update_flag = true;
						break;
					}else {
						logger.info("insert mode1");
					}
				}
				if(!update_flag) {
					insert_kv = new KeyValueCollection();
					for(int i=0; i<k.length; i++) {
						insert_kv.addString(k[i], v[i]);
					}
					tmpkvp = new KeyValuePair(sec_name, insert_kv);
					obj_kv.addPair(tmpkvp);
					obj.getGroupInfo().setUserProperties(obj_kv);
					obj.save();
				}else {
					if(sectionKvp != null) {
						if(sec_name.equals(opt_name)) {
							insert_kv = new KeyValueCollection();
							KeyValueCollection tmp_kv = sectionKvp.getTKVValue();
							for(int i=0; i<k.length; i++) {
								boolean ck = false;
								for(Object tmpObj : tmp_kv) {
									KeyValuePair kvp = (KeyValuePair) tmpObj;
									if(k[i].equals(kvp.getStringKey())){
										ck = true;
									}
								}
								if(ck) {
									logger.info("지우고 추가해  " + "key=" + k[i] + ", value=" + v[i]);
									tmp_kv.remove(k[i]);
									tmp_kv.addString(k[i], v[i]);
								}else {
									logger.info("추가해  " + "key=" + k[i] + ", value=" + v[i]);
									tmp_kv.addString(k[i], v[i]);
								}
							}
							tmpkvp = new KeyValuePair(sec_name, tmp_kv);
							obj_kv.remove(sec_name);
							obj_kv.addPair(tmpkvp);
							obj.getGroupInfo().setUserProperties(obj_kv);
							obj.save();
						}
					}				
				}
			}else {
				logger.info("insert mode2");
				insert_kv = new KeyValueCollection();
				logger.info("K의 길이는 " + k.length);
				for(int i=0; i<k.length; i++) {
					logger.info("insert key="+k[i] + ", value=" + v[i]);
					insert_kv.addString(k[i], v[i]);
				}
				tmpkvp = new KeyValuePair(sec_name, insert_kv);
				obj_kv = new KeyValueCollection();
				obj_kv.addPair(tmpkvp);
				obj.getGroupInfo().setUserProperties(obj_kv);
				obj.save();
			}
		}catch(Exception e) {
			logger.error("Exception", e);
		}
		return obj;
	}
	
	public CfgAgentGroup removeUserProperties(CfgAgentGroup obj, String sec_name, String[] k, String[] v) {
		KeyValueCollection obj_kv = null;
		KeyValueCollection insert_kv = null;
		String opt_name = null;
		boolean update_flag = false;
		KeyValuePair sectionKvp = null;
		KeyValuePair tmpkvp = null;

		try {
			obj_kv = obj.getGroupInfo().getUserProperties();

			if(obj_kv != null && obj_kv.length() > 0) {
				for(Object selObj : obj_kv) {
					opt_name = "";
					sectionKvp = (KeyValuePair) selObj;
					opt_name = sectionKvp.getStringKey();
					KeyValueCollection tmp_kv = sectionKvp.getTKVValue();
					if(sec_name.equals(opt_name)) {
						if(k != null && k.length > 0) {
							if(sectionKvp != null) {
								if(sec_name.equals(opt_name)) {
									insert_kv = new KeyValueCollection();
									for(int i=0; i<k.length; i++) {
										boolean ck = false;
										for(Object tmpObj : tmp_kv) {
											KeyValuePair kvp = (KeyValuePair) tmpObj;
											if(k[i].equals(kvp.getStringKey())){
												ck = true;
											}
										}
										if(ck) {
											logger.info("지워  " + "key=" + k[i] + ", value=" + v[i]);
											tmp_kv.remove(k[i]);
										}
									}
									tmpkvp = new KeyValuePair(sec_name, tmp_kv);
									obj_kv.remove(sec_name);
									obj_kv.addPair(tmpkvp);
									obj.getGroupInfo().setUserProperties(obj_kv);
									obj.save();
								}
							}			
						}else {
							obj_kv.remove(sec_name);
							obj.getGroupInfo().setUserProperties(obj_kv);
							obj.save();
						}
						break;
					}
				}
//				if(!update_flag) {
//					insert_kv = new KeyValueCollection();
//					for(int i=0; i<k.length; i++) {
//						insert_kv.addString(k[i], v[i]);
//					}
//					tmpkvp = new KeyValuePair(sec_name, insert_kv);
//					obj_kv.addPair(tmpkvp);
//					obj.getGroupInfo().setUserProperties(obj_kv);
//					obj.save();
//				}else {
//					if(sectionKvp != null) {
//						if(sec_name.equals(opt_name)) {
//							insert_kv = new KeyValueCollection();
//							KeyValueCollection tmp_kv = sectionKvp.getTKVValue();
//							for(int i=0; i<k.length; i++) {
//								boolean ck = false;
//								for(Object tmpObj : tmp_kv) {
//									KeyValuePair kvp = (KeyValuePair) tmpObj;
//									if(k[i].equals(kvp.getStringKey())){
//										ck = true;
//									}
//								}
//								if(ck) {
//									logger.info("지우고 추가해  " + "key=" + k[i] + ", value=" + v[i]);
//									tmp_kv.remove(k[i]);
//									tmp_kv.addString(k[i], v[i]);
//								}else {
//									logger.info("추가해  " + "key=" + k[i] + ", value=" + v[i]);
//									tmp_kv.addString(k[i], v[i]);
//								}
//							}
//							tmpkvp = new KeyValuePair(sec_name, tmp_kv);
//							obj_kv.remove(sec_name);
//							obj_kv.addPair(tmpkvp);
//							obj.getGroupInfo().setUserProperties(obj_kv);
//							obj.save();
//						}
//					}				
//				}
			}
		}catch(Exception e) {
			logger.error("Exception", e);
		}
		return obj;
	}
	
//	public CfgTransaction setUserProperties(CfgTransaction obj, String sec_name, String[] k, String[] v) {
//		KeyValueCollection obj_kv = null;
//		String opt_name = null;
//		
//		try {
//			obj_kv = obj.getUserProperties();
//
//			if(obj_kv != null) {
//				for(Object selObj : obj_kv) {
//					opt_name = "";
//					KeyValuePair sectionKvp = (KeyValuePair) selObj;
//					opt_name = sectionKvp.getStringKey();
//					if(sec_name.equals(opt_name)) {
//						logger.info("update mode");
//					}else {
//						KeyValueCollection insert_kv = new KeyValueCollection();
//						KeyValuePair tmpkvp = null;
//						for(int i=0; i>k.length; i++) {
//							insert_kv.addString(k[i], v[i]);
//						}
//						tmpkvp = new KeyValuePair(sec_name, insert_kv);
//						obj_kv.addPair(tmpkvp);
//						obj.setUserProperties(obj_kv);
//						obj.save();
//					}
//				}
//				
//			}
//		}catch(Exception e) {
//			
//		}
//		return obj;
//	}
	
//	public JSONObject getUserProperties(CfgObject obj) {
//		KeyValueCollection kv = null;
//		JSONObject jsonObj = new JSONObject(); 
//		JSONObject jsonObj2 = new JSONObject();
//		JSONObject rtnObj = new JSONObject(); 
//		String opt_name = null;
//		if (obj instanceof CfgPerson) {
//            CfgPerson cast_obj = (CfgPerson) obj;
//            kv = cast_obj.getUserProperties();
//		}else if (obj instanceof CfgAgentGroup) {
//			CfgAgentGroup cast_obj = (CfgAgentGroup) obj;
//			kv = cast_obj.getGroupInfo().getUserProperties();
//		}else if (obj instanceof CfgAgentLogin) {
//			CfgAgentLogin cast_obj = (CfgAgentLogin) obj;
//			kv = cast_obj.getUserProperties();
//		}else if (obj instanceof CfgCampaign) {
//			CfgCampaign cast_obj = (CfgCampaign) obj;
//			kv = cast_obj.getUserProperties();
//		}else if (obj instanceof CfgDN) {
//			CfgDN cast_obj = (CfgDN) obj;
//			kv = cast_obj.getUserProperties();
//		}else if (obj instanceof CfgTransaction) {
//			CfgTransaction cast_obj = (CfgTransaction) obj;
//			kv = cast_obj.getUserProperties();
//		}
//		if(kv != null) {
//			for(Object selObj : kv) {
//				opt_name = "";
//				KeyValuePair sectionKvp = (KeyValuePair) selObj;
//				opt_name = sectionKvp.getStringKey();
//                jsonObj = new JSONObject();
//				for (Object recordObj : sectionKvp.getTKVValue()) {
//	                KeyValuePair recordKvp = (KeyValuePair) recordObj;
//	    			jsonObj.put(recordKvp.getStringKey(), recordKvp.getStringValue());
//	            }
//				jsonObj2.put(opt_name, jsonObj);
//			}
//			rtnObj.put("Sections", jsonObj2);
//			
//		}
//		//System.out.println(rtnObj.toJSONString());
//		return rtnObj;
//	}

	private void createProperties() {
		
	}
	
	private void modifyProperties() {
		
	}
	
//	public KeyValueCollection setUserProperties(CfgObject obj, String section_name, String k, String v) {
//		KeyValueCollection kv = null;
//		KeyValueCollection tmpkv1 = new KeyValueCollection();
//		KeyValueCollection tmpkv2 = new KeyValueCollection();
//		KeyValueCollection tmpkv3 = new KeyValueCollection();
//		
//		boolean tmpFlag = false;
//		String opt_name = null;
//
//		try {
//			if (obj instanceof CfgPerson) {
//	            CfgPerson cast_obj = (CfgPerson) obj;
//	            kv = cast_obj.getUserProperties();
//			}else if (obj instanceof CfgAgentGroup) {
//				CfgAgentGroup cast_obj = (CfgAgentGroup) obj;
//				kv = cast_obj.getGroupInfo().getUserProperties();
//			}else if (obj instanceof CfgAgentLogin) {
//				CfgAgentLogin cast_obj = (CfgAgentLogin) obj;
//				kv = cast_obj.getUserProperties();
//			}else if (obj instanceof CfgCampaign) {
//				CfgCampaign cast_obj = (CfgCampaign) obj;
//				kv = cast_obj.getUserProperties();
//			}else if (obj instanceof CfgDN) {
//				CfgDN cast_obj = (CfgDN) obj;
//				kv = cast_obj.getUserProperties();
//			}else if (obj instanceof CfgTransaction) {
//				CfgTransaction cast_obj = (CfgTransaction) obj;
//				kv = cast_obj.getUserProperties();
//			}
//
//			if(kv != null) {
//				for(Object selObj : kv) {
//					KeyValuePair sectionKvp = (KeyValuePair) selObj;
//					opt_name = sectionKvp.getStringKey();
//					if(opt_name.equals(section_name)) {
//						logger.info("modify mode");
//					}else {
//						logger.info("create mode");
//					}
//				}
//				
//				
//				
//				tmpkv1 = new KeyValueCollection();
//			}
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
////			if(kv != null) {
////				tmpkv1 = new KeyValueCollection();
////				for(Object selObj : kv) {
////					opt_name = "";
////					KeyValuePair sectionKvp = (KeyValuePair) selObj;
////					opt_name = sectionKvp.getStringKey();
////					
////					if(opt_name.equals(section_name)) {	//기존에 있던 옵션명이면	
////						tmpkv2 = sectionKvp.getTKVValue();
////						for(Object kvp : tmpkv2) {
////							KeyValuePair section2Kvp = (KeyValuePair) kvp;
////							if(section2Kvp.getStringKey().equals(k)) {
////								tmpFlag = true;
////							}
////						}
////						if(tmpFlag) {
////							tmpkv2.remove(k);
////							tmpkv2.addString(k, v);
////							sectionKvp.getTKVValue().remove(key);
////							
////						}
////						
////						
//////						KeyValueCollection aa = new KeyValueCollection();
//////						KeyValuePair tmpKeyValuePair = new KeyValuePair(k, v);
//////						aa.add(tmpKeyValuePair);
//////						sectionKvp.setTKVValue(aa);
//////						kv.addPair(sectionKvp);
////					}else {								//새로만들어야할 옵션명이면
////						tmpkv = new KeyValueCollection();
////						tmpkv.addString(k, v);
////						kv.addList(section_name, tmpkv);
////					}
////				}
////			}
//		}catch(Exception ex) {
//			logger.error("Exception", ex);
//		}
//		//System.out.println(rtnObj.toJSONString());
//		return kv;
//	}

	private KeyValueCollection getKv(CfgObject obj) {
		KeyValueCollection rtnkv = null;
		if (obj instanceof CfgPerson) {
            CfgPerson cast_obj = (CfgPerson) obj;
            rtnkv = cast_obj.getUserProperties();
		}else if (obj instanceof CfgAgentGroup) {
			CfgAgentGroup cast_obj = (CfgAgentGroup) obj;
			rtnkv = cast_obj.getGroupInfo().getUserProperties();
		}else if (obj instanceof CfgAgentLogin) {
			CfgAgentLogin cast_obj = (CfgAgentLogin) obj;
			rtnkv = cast_obj.getUserProperties();
		}else if (obj instanceof CfgCampaign) {
			CfgCampaign cast_obj = (CfgCampaign) obj;
			rtnkv = cast_obj.getUserProperties();
		}else if (obj instanceof CfgDN) {
			CfgDN cast_obj = (CfgDN) obj;
			rtnkv = cast_obj.getUserProperties();
		}else if (obj instanceof CfgTransaction) {
			CfgTransaction cast_obj = (CfgTransaction) obj;
			rtnkv = cast_obj.getUserProperties();
		}
		return rtnkv;
	}
	
}
