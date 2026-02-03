package kr.co.i4way.genesys.cfgserver;

import java.util.Iterator;
import java.util.Set;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgTransaction;
import com.genesyslab.platform.applicationblocks.com.queries.CfgTransactionQuery;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import com.genesyslab.platform.configuration.protocol.types.CfgTransactionType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import kr.co.i4way.genesys.model.TransactionVo;

public class ConfigTransaction {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	public ConfigTransaction() {		
	}

	public CfgTransaction getTransactionInfo_dbid(final IConfService service,
					int tenant_dbid,
					int dbid
					)
			throws ConfigException, InterruptedException {
		CfgTransaction rtnval = null;
		try{
			CfgTransactionQuery transactionquery = new CfgTransactionQuery();
			transactionquery.setDbid(dbid);
			transactionquery.setObjectType(CfgTransactionType.CFGTRTList);
			rtnval = service.retrieveObject(CfgTransaction.class,
					transactionquery);
			
		}catch(Exception ex){ 

		}
		return rtnval;
	}

	public CfgTransaction getTransactionInfo_name(final IConfService service,
					int tenant_dbid,
					String name
					)
			throws ConfigException, InterruptedException {
		CfgTransaction rtnval = null;
		try{
			CfgTransactionQuery transactionquery = new CfgTransactionQuery();
			transactionquery.setName(name);
			transactionquery.setObjectType(CfgTransactionType.CFGTRTList);
			rtnval = service.retrieveObject(CfgTransaction.class,
					transactionquery);
			
		}catch(Exception ex){

		}
		return rtnval;
	}
	

	/**
	 * Transaction을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param TransactionVo
	 * @return
	 */
	public CfgTransaction createTransaction(
			final IConfService service, 
			int tenant_dbid, 
			TransactionVo transactionvo) {
		CfgTransaction transaction = null;
		try{
			// Read configuration objects:
			transaction = new CfgTransaction(service);
			transaction.setTenantDBID(tenant_dbid);
			transaction.setName(transactionvo.getName());
			transaction.setAlias(transactionvo.getAlias());
			transaction.setType(CfgTransactionType.valueOf(transactionvo.getType()));
			if(transactionvo.getFolderId() > 0) {	// Folder DBID
				transaction.setFolderId(transactionvo.getFolderId());
			}

			transaction.save();

			if(transaction != null) {
				JsonArray psarry = null;
				if(transactionvo.getAnnex() != null){
					psarry = transactionvo.getAnnex().getAsJsonArray();
					KeyValueCollection obj_kv = transaction.getUserProperties();
					KeyValueCollection insert_kv = null;
					KeyValuePair tmpkvp = null;
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						Set<String> keyarr = ob.keySet();
						Iterator<String> keys = keyarr.iterator();
						String key = keys.next();
						JsonElement ob2 = ob.get(key);
						JsonArray tmparry = ob2.getAsJsonArray();
						insert_kv = new KeyValueCollection();
						for(int j=0; j<tmparry.size(); j++){
							JsonObject tmpob =  tmparry.get(j).getAsJsonObject();
							Set<String> tmpkeyarr = tmpob.keySet();
							Iterator<String> tmpkeys = tmpkeyarr.iterator();
							String tmpkey = tmpkeys.next();
							String tmpValue = tmpob.get(tmpkey).getAsString();
							tmpValue = tmpValue.replaceAll("\\^\\^61\\^", "=");
							tmpValue = tmpValue.replaceAll("\\^\\^at\\^", "@");
							tmpValue = tmpValue.replaceAll("\\^\\^gt\\^", ">");
							tmpValue = tmpValue.replaceAll("\\^\\^lt\\^", "<");
							tmpValue = tmpValue.replaceAll("\\^\\^58\\^", ":");
							tmpValue = tmpValue.replaceAll("\\^\\^123\\^", "{");
							tmpValue = tmpValue.replaceAll("\\^\\^125\\^", "}");
							tmpValue = tmpValue.replaceAll("\\^\\^quot\\^", "\"");
							tmpValue = tmpValue.replaceAll("\\^\\^126\\^", "~");
							tmpValue = tmpValue.replaceAll("\\^\\^coma\\^", ",");
							tmpValue = tmpValue.replaceAll("\\^\\^47\\^", "/");
							insert_kv.addString(tmpkey, tmpValue); 
						}
						tmpkvp = new KeyValuePair(key, insert_kv);
						obj_kv.addPair(tmpkvp);
					}
					transaction.setUserProperties(obj_kv);
				}

				transaction.save();
			}

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return transaction;
	}

	/**
	 * Transaction을 생성한다.
	 * @param service
	 * @param tenant_dbid
	 * @param transaction_name
	 * @param type_id
	 * @return
	 */
	public CfgTransaction createTransaction(
			final IConfService service, 
			int tenant_dbid, 
			String transaction_name,
			int type_id) {
		CfgTransaction transaction = null;
		try{
			// Read configuration objects:
			transaction = new CfgTransaction(service);
			transaction.setTenantDBID(tenant_dbid);	//Tenant DBID
			transaction.setName(transaction_name);
			transaction.setType(CfgTransactionType.valueOf(type_id));
			transaction.save();

		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return transaction;
	}

	/**
	 * Transaction을 수정한다.
	 * @param service
	 * @param tenant_dbid
	 * @param transaction
	 * @param AgentLoginInfoVo
	 * @return
	 */
	public CfgTransaction modifyTransaction(
			final IConfService service, 
			int tenant_dbid, 
			CfgTransaction transaction,
			TransactionVo transactionvo){
		// Read configuration objects:
		try {
			transaction.setName(transactionvo.getName());
			transaction.setAlias(transactionvo.getAlias());
			transaction.save();

			if(transaction != null) {
				JsonArray psarry = null;

				if(transactionvo.getAnnex() != null){
					psarry = transactionvo.getAnnex().getAsJsonArray();
					KeyValueCollection obj_kv = transaction.getUserProperties();
					KeyValueCollection insert_kv = null;
					KeyValuePair tmpkvp = null;
					for(int i=0; i<psarry.size(); i++){
						JsonObject ob =  psarry.get(i).getAsJsonObject();
						Set<String> keyarr = ob.keySet();
						Iterator<String> keys = keyarr.iterator();
						String key = keys.next();
						JsonElement ob2 = ob.get(key);
						JsonArray tmparry = ob2.getAsJsonArray();
						insert_kv = new KeyValueCollection();
						for(int j=0; j<tmparry.size(); j++){
							JsonObject tmpob =  tmparry.get(j).getAsJsonObject();
							Set<String> tmpkeyarr = tmpob.keySet();
							Iterator<String> tmpkeys = tmpkeyarr.iterator();
							String tmpkey = tmpkeys.next();
							String tmpValue = tmpob.get(tmpkey).getAsString();
							tmpValue = tmpValue.replaceAll("\\^\\^61\\^", "=");
							tmpValue = tmpValue.replaceAll("\\^\\^at\\^", "@");
							tmpValue = tmpValue.replaceAll("\\^\\^gt\\^", ">");
							tmpValue = tmpValue.replaceAll("\\^\\^lt\\^", "<");
							tmpValue = tmpValue.replaceAll("\\^\\^58\\^", ":");
							tmpValue = tmpValue.replaceAll("\\^\\^123\\^", "{");
							tmpValue = tmpValue.replaceAll("\\^\\^125\\^", "}");
							tmpValue = tmpValue.replaceAll("\\^\\^quot\\^", "\"");
							tmpValue = tmpValue.replaceAll("\\^\\^126\\^", "~");
							tmpValue = tmpValue.replaceAll("\\^\\^coma\\^", ",");
							tmpValue = tmpValue.replaceAll("\\^\\^47\\^", "/");
							insert_kv.addString(tmpkey, tmpValue); 
						}
						tmpkvp = new KeyValuePair(key, insert_kv);
						obj_kv.addPair(tmpkvp);
					}
					transaction.setUserProperties(obj_kv);
				}

				transaction.save();
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return transaction;
	}
	
	/**
	 * Transaction을 삭제한다.
	 * @param service
	 * @param transaction
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public boolean deleteTransaction(
			final IConfService service,
			CfgTransaction transaction
			)
					throws ConfigException, InterruptedException {
		boolean rtnbool = false;
		try{
			transaction.delete();		
			rtnbool = true;
		}catch(Exception e){
			rtnbool = false;
		}
		return rtnbool;
	}	

	/**
	 * Transaction의 상태를 변경한다. 
	 * @param service
	 * @param transaction 	상태 변경할 Transaction 객체
	 * @param flag 		(true = Enabled, false = Disabled)
	 * @return
	 * @throws ConfigException
	 * @throws InterruptedException
	 */
	public CfgTransaction setTransactionState(
			final IConfService service,
			CfgTransaction transaction,
			boolean flag
			)
					throws ConfigException, InterruptedException {
		// Read configuration objects:
		if(flag){
			transaction.setState(CfgObjectState.CFGEnabled);
		}else{
			transaction.setState(CfgObjectState.CFGDisabled);
		}
		transaction.save();
		return transaction;
	}

	/**
	 * section을 삽입한다.
	 * @param service
	 * @param transaction
	 * @param TransactionVo
	 * @return
	 */
	public CfgTransaction addSection(
			final IConfService service, 
			CfgTransaction transaction,
			TransactionVo transactionvo){
		// Read configuration objects:
		try {
			if(transaction != null) {

				//if(transactionvo.getAnnex() != null){
					KeyValueCollection obj_kv = transaction.getUserProperties();
					KeyValuePair tmpkvp = null;
					KeyValueCollection kv = new KeyValueCollection();
					kv.addString("dummy", "");
					tmpkvp = new KeyValuePair(transactionvo.getSection_name(), kv);
					obj_kv.addPair(tmpkvp);
					transaction.setUserProperties(obj_kv);
				//}

				transaction.save();
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return transaction;
	}

	/**
	 * section을 수정한다.
	 * @param service
	 * @param transaction
	 * @param TransactionVo
	 * @return
	 */
	public CfgTransaction modifySection(
			final IConfService service, 
			CfgTransaction transaction,
			TransactionVo transactionvo){
		// Read configuration objects:
		try {
			if(transaction != null) {

				//해당섹션내용 백업(oldkvp) 및 변경할 섹션 생성(tmpkvp)
				KeyValueCollection obj_kv = transaction.getUserProperties();
				KeyValuePair oldkvp = obj_kv.getPair(transactionvo.getSection_name());
				KeyValuePair tmpkvp = null;
				tmpkvp = new KeyValuePair(transactionvo.getChange_section_name(), oldkvp.getTKVValue());
					
				//기존 섹션 삭제
				obj_kv.remove(transactionvo.getSection_name());
				transaction.setUserProperties(obj_kv);
				transaction.save();

				//변경할 섹션 추가 하고 저장
				obj_kv.addPair(tmpkvp);
				transaction.setUserProperties(obj_kv);
				transaction.save();
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return transaction;
	}

	/**
	 * section을 삭제한다.
	 * @param service
	 * @param transaction
	 * @param TransactionVo
	 * @return
	 */
	public CfgTransaction removeSection(
			final IConfService service, 
			CfgTransaction transaction,
			TransactionVo transactionvo){
		// Read configuration objects:
		try {
			if(transaction != null) {

				KeyValueCollection obj_kv = transaction.getUserProperties();
				obj_kv.remove(transactionvo.getSection_name());
				transaction.setUserProperties(obj_kv);

				transaction.save();
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return transaction;
	}

	/**
	 * option을 삽입한다.
	 * @param service
	 * @param transaction
	 * @param TransactionVo
	 * @return
	 */
	public CfgTransaction addOption(
			final IConfService service, 
			CfgTransaction transaction,
			TransactionVo transactionvo){
		KeyValueCollection insert_kv = null;
		KeyValueCollection insert_kv_type = null;
		String opt_name = "";
		String templete_name = "";
		String opt_name_type = "";
		KeyValuePair sectionKvp = null;
		KeyValuePair sectionKvp_type = null;
		KeyValuePair tmpKvp = null;
		KeyValuePair tmpKvp_type = null;
		boolean update_flag = false;
		boolean update_trmplete_flag = false;

		try {
			if(transaction != null) {	//transaction이 널이 아니면
				KeyValueCollection obj_kv = transaction.getUserProperties();	//user propertie를 가져온다
				if(obj_kv != null && obj_kv.length() > 0) {	//user propertie가 널이 아니면
					for(Object selObj : obj_kv) {	//user propertie 만큼 loop
						opt_name = "";
						sectionKvp = (KeyValuePair) selObj;
						opt_name = sectionKvp.getStringKey() + "";	//section key 추출
						if(transactionvo.getSection_name().equals(opt_name)) {	//section명이 일치하면
							update_flag = true;	//업데이트 모드
							break;
						}else {
						}
					}
					for(Object selObj : obj_kv) {	//user propertie 만큼 loop
						opt_name_type = "";
						sectionKvp_type = (KeyValuePair) selObj;
						opt_name_type = sectionKvp_type.getStringKey() + "";	//section key 추출
						templete_name = "Template_" + transactionvo.getSection_name();
						if(templete_name.equals(opt_name_type)) {	//Trmplete section명이 일치하면
							update_trmplete_flag = true;	//업데이트 모드
							break;
						}else {
						}
					}
					
					//option 등록
					if(!update_flag) {	//등록모드일때
						insert_kv = new KeyValueCollection();	//key, value
						insert_kv.addString(transactionvo.getKey(), transactionvo.getValue());
						tmpKvp = new KeyValuePair(transactionvo.getSection_name(), insert_kv);	//section명으로 option등록
						obj_kv.addPair(tmpKvp);
						transaction.setUserProperties(obj_kv);
						transaction.save();
					}else {	//업데이트 모드일때
						if(sectionKvp != null) {
							if(transactionvo.getSection_name().equals(opt_name)) {	//section명이 동일한지 비교
								insert_kv = new KeyValueCollection();
								KeyValueCollection tmp_kv = sectionKvp.getTKVValue();
								boolean ck = false;
								for(Object tmpObj : tmp_kv) {
									KeyValuePair kvp = (KeyValuePair) tmpObj;
									if(transactionvo.getKey().equals(kvp.getStringKey())){
										ck = true;
									}
								}
								if(ck) {
									logger.info("지우고 추가해  " + "key=" + transactionvo.getKey() + ", value=" + transactionvo.getValue());
									tmp_kv.remove(transactionvo.getKey());
									tmp_kv.addString(transactionvo.getKey(), transactionvo.getValue());
								}else {
									logger.info("추가해  " + "key=" + transactionvo.getKey() + ", value=" + transactionvo.getValue());
									tmp_kv.addString(transactionvo.getKey(), transactionvo.getValue());
								}
								tmpKvp = new KeyValuePair(transactionvo.getSection_name(), tmp_kv);
								obj_kv.remove(transactionvo.getSection_name());
								obj_kv.addPair(tmpKvp);
								transaction.setUserProperties(obj_kv);
								transaction.save();
							}
						}				
					}
					//Templete 등록
					if(!update_trmplete_flag) {	//등록모드일때
						insert_kv_type = new KeyValueCollection();	//key, type
						insert_kv_type.addString(transactionvo.getKey(), transactionvo.getData_type() + "^@" + transactionvo.getAlias() + "^@" + transactionvo.getValue());
						tmpKvp_type = new KeyValuePair("Template_" + transactionvo.getSection_name(), insert_kv_type);	//Template_ + section명으로 option type 등록
						obj_kv.addPair(tmpKvp_type);
						transaction.setUserProperties(obj_kv);
						transaction.save();
					}else {	//업데이트 모드일때
						if(sectionKvp_type != null) {
							if(templete_name.equals(opt_name_type)) {	//section명이 동일한지 비교
								insert_kv = new KeyValueCollection();
								KeyValueCollection tmp_kv = sectionKvp_type.getTKVValue();
								boolean ck = false;
								for(Object tmpObj : tmp_kv) {
									KeyValuePair kvp = (KeyValuePair) tmpObj;
									if(transactionvo.getKey().equals(kvp.getStringKey())){
										ck = true;
									}
								}
								if(ck) {
									logger.info("지우고 추가해  " + "key=" + transactionvo.getKey() + ", value=" + transactionvo.getData_type() + "^@" + transactionvo.getAlias() + "^@" + transactionvo.getValue());
									tmp_kv.remove(transactionvo.getKey());
									tmp_kv.addString(transactionvo.getKey(), transactionvo.getData_type() + "^@" + transactionvo.getAlias() + "^@" + transactionvo.getValue());
								}else {
									logger.info("추가해  " + "key=" + transactionvo.getKey() + ", value=" + transactionvo.getData_type() + "^@" + transactionvo.getAlias() + "^@" + transactionvo.getValue());
									tmp_kv.addString(transactionvo.getKey(), transactionvo.getData_type() + "^@" + transactionvo.getAlias() + "^@" + transactionvo.getValue());
								}
								tmpKvp = new KeyValuePair(templete_name, tmp_kv);
								obj_kv.remove(templete_name);
								obj_kv.addPair(tmpKvp);
								transaction.setUserProperties(obj_kv);
								transaction.save();
							}
						}				
					}
				}
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return transaction;
	}	

	/**
	 * option을 수정한다.
	 * @param service
	 * @param transaction
	 * @param TransactionVo
	 * @return
	 */
	public CfgTransaction modifyOption(
			final IConfService service, 
			CfgTransaction transaction,
			TransactionVo transactionvo){
		KeyValueCollection insert_kv = null;
		KeyValueCollection insert_kv_type = null;
		String opt_name = "";
		String templete_name = "";
		String opt_name_type = "";
		KeyValuePair sectionKvp = null;
		KeyValuePair sectionKvp_type = null;
		KeyValuePair tmpKvp = null;
		KeyValuePair tmpKvp_type = null;
		boolean update_flag = false;
		boolean update_trmplete_flag = false;
		
		try {
			if(transaction != null) {	//transaction이 널이 아니면
				KeyValueCollection obj_kv = transaction.getUserProperties();	//user propertie를 가져온다
				if(obj_kv != null && obj_kv.length() > 0) {	//user propertie가 널이 아니면
					for(Object selObj : obj_kv) {	//user propertie 만큼 loop
						opt_name = "";
						sectionKvp = (KeyValuePair) selObj;
						opt_name = sectionKvp.getStringKey() + "";	//section key 추출
						if(transactionvo.getSection_name().equals(opt_name)) {	//section명이 일치하면
							update_flag = true;	//업데이트 모드
							break;
						}else {
						}
					}
					for(Object selObj : obj_kv) {	//user propertie 만큼 loop
						opt_name_type = "";
						sectionKvp_type = (KeyValuePair) selObj;
						opt_name_type = sectionKvp_type.getStringKey() + "";	//section key 추출
						templete_name = "Template_" + transactionvo.getSection_name();
						if(templete_name.equals(opt_name_type)) {	//Trmplete section명이 일치하면
							update_trmplete_flag = true;	//업데이트 모드
							break;
						}else {
						}
					}
							
					//option 등록
					if(!update_flag) {	//등록모드일때
						insert_kv = new KeyValueCollection();	//key, value
						insert_kv.addString(transactionvo.getKey(), transactionvo.getData_type() + "^@" + transactionvo.getAlias() + "^@" + transactionvo.getValue());
						tmpKvp = new KeyValuePair(transactionvo.getSection_name(), insert_kv);	//section명으로 option등록
						obj_kv.addPair(tmpKvp);
						transaction.setUserProperties(obj_kv);
						transaction.save();
					}else {	//업데이트 모드일때
						if(sectionKvp != null) {
							if(transactionvo.getSection_name().equals(opt_name)) {	//section명이 동일한지 비교
								insert_kv = new KeyValueCollection();
								KeyValueCollection tmp_kv = sectionKvp.getTKVValue();
								boolean ck = false;
								String cv = "";
								for(Object tmpObj : tmp_kv) {
									KeyValuePair kvp = (KeyValuePair) tmpObj;
									if(transactionvo.getKey().equals(kvp.getStringKey())){
										ck = true;
										cv = kvp.getStringValue();	//key가 있을때 해당키의 값을 저장
									}
								}
								if(ck) {
									logger.info("지우고 추가해  " + "key=" + transactionvo.getKey() + ", value=" + cv);
									tmp_kv.remove(transactionvo.getKey());
									tmp_kv.addString(transactionvo.getChange_key(), cv);
								}else {
									logger.info("추가해  " + "key=" + transactionvo.getKey() + ", value=" + transactionvo.getValue());
									tmp_kv.addString(transactionvo.getChange_key(), "");
								}
								tmpKvp = new KeyValuePair(transactionvo.getSection_name(), tmp_kv);
								obj_kv.remove(transactionvo.getSection_name());
								obj_kv.addPair(tmpKvp);
								transaction.setUserProperties(obj_kv);
								transaction.save();
							}
						}				
					}
					//Templete 등록
					if(!update_trmplete_flag) {	//등록모드일때
						insert_kv_type = new KeyValueCollection();	//key, type
						insert_kv_type.addString(transactionvo.getKey(), transactionvo.getData_type());
						tmpKvp_type = new KeyValuePair("Template_" + transactionvo.getSection_name(), insert_kv_type);	//Template_ + section명으로 option type 등록
						obj_kv.addPair(tmpKvp_type);
						transaction.setUserProperties(obj_kv);
						transaction.save();
					}else {	//업데이트 모드일때
						if(sectionKvp_type != null) {
							if(templete_name.equals(opt_name_type)) {	//section명이 동일한지 비교
								insert_kv = new KeyValueCollection();
								KeyValueCollection tmp_kv = sectionKvp_type.getTKVValue();
								boolean ck = false;
								for(Object tmpObj : tmp_kv) {
									KeyValuePair kvp = (KeyValuePair) tmpObj;
									if(transactionvo.getKey().equals(kvp.getStringKey())){
										ck = true;
									}
								}
								if(ck) {
									logger.info("지우고 추가해  " + "key=" + transactionvo.getKey() + ", value=" + transactionvo.getData_type() + "^@" + transactionvo.getAlias() + "^@" + transactionvo.getValue());
									tmp_kv.remove(transactionvo.getKey());
									tmp_kv.addString(transactionvo.getChange_key(), transactionvo.getData_type() + "^@" + transactionvo.getAlias() + "^@" + transactionvo.getValue());
								}else {
									logger.info("추가해  " + "key=" + transactionvo.getKey() + ", value=" + transactionvo.getData_type() + "^@" + transactionvo.getAlias() + "^@" + transactionvo.getValue());
									tmp_kv.addString(transactionvo.getChange_key(), transactionvo.getData_type() + "^@" + transactionvo.getAlias() + "^@" + transactionvo.getValue());
								}

								tmpKvp = new KeyValuePair(templete_name, tmp_kv);
								obj_kv.remove(templete_name);
								obj_kv.addPair(tmpKvp);
								transaction.setUserProperties(obj_kv);
								transaction.save();
							}
						}				
					}
				}
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return transaction;
	}	

	/**
	 * option을 삭제한다.
	 * @param service
	 * @param transaction
	 * @param TransactionVo
	 * @return
	 */
	public CfgTransaction removeOption(
			final IConfService service, 
			CfgTransaction transaction,
			TransactionVo transactionvo){
		KeyValueCollection insert_kv = null;
		String opt_name = "";
		KeyValuePair sectionKvp = null;
		KeyValuePair tmpKvp = null;
		boolean update_flag = false;

		try {
			if(transaction != null) {
				KeyValueCollection obj_kv = transaction.getUserProperties();
				if(obj_kv != null && obj_kv.length() > 0) {
					for(Object selObj : obj_kv) {
						opt_name = "";
						sectionKvp = (KeyValuePair) selObj;
						opt_name = sectionKvp.getStringKey();
						if(transactionvo.getSection_name().equals(opt_name)) {
							update_flag = true;
							break;
						}else {
						}
					}

					if(!update_flag) {
					}else {
						if(sectionKvp != null) {
							if(transactionvo.getSection_name().equals(opt_name)) {
								insert_kv = new KeyValueCollection();
								KeyValueCollection tmp_kv = sectionKvp.getTKVValue();
								boolean ck = false;
								for(Object tmpObj : tmp_kv) {
									KeyValuePair kvp = (KeyValuePair) tmpObj;
									if(transactionvo.getKey().equals(kvp.getStringKey())){
										ck = true;
									}
								}
								if(ck) {
									logger.info("지워  " + "key=" + transactionvo.getKey() + ", value=" + transactionvo.getValue());
									tmp_kv.remove(transactionvo.getKey());
								}
								if(tmp_kv.length() > 0){
									tmpKvp = new KeyValuePair(transactionvo.getSection_name(), tmp_kv);
									obj_kv.remove(transactionvo.getSection_name());
									obj_kv.addPair(tmpKvp);
									transaction.setUserProperties(obj_kv);
									transaction.save();
								}else{
									tmpKvp = new KeyValuePair(transactionvo.getSection_name(), new KeyValueCollection());
									obj_kv.remove(transactionvo.getSection_name());
									transaction.setUserProperties(obj_kv);
									transaction.save();
									obj_kv.addPair(tmpKvp);
									transaction.setUserProperties(obj_kv);
									transaction.save();
								}
							}
						}				
					}
				}
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return transaction;
	}	

		/**
	 * Option들을 저장한다
	 * @param service
	 * @param transaction
	 * @param TransactionVo
	 * @return
	 */
	public CfgTransaction saveOptions(
			final IConfService service, 
			CfgTransaction transaction,
			TransactionVo transactionvo){
		KeyValueCollection insert_kv = null;
		String opt_name = "";
		KeyValuePair sectionKvp = null;
		boolean update_flag = false;
		
		try {
			if(transaction != null) {	//transaction이 널이 아니면
				KeyValueCollection obj_kv = transaction.getUserProperties();	//user propertie를 가져온다
				if(obj_kv != null && obj_kv.length() > 0) {	//user propertie가 널이 아니면
					for(Object selObj : obj_kv) {	//user propertie 만큼 loop
						opt_name = "";
						sectionKvp = (KeyValuePair) selObj;
						opt_name = sectionKvp.getStringKey() + "";	//section key 추출
						if(transactionvo.getSection_name().equals(opt_name)) {	//section명이 일치하면
							update_flag = true;	//업데이트 모드
							break;
						}else {
						}
					}
							
					//option 등록
					if(!update_flag) {	//등록모드일때
						JsonArray psarry = null;
						KeyValuePair tmpkvp = null;
						if(transactionvo.getAnnex() != null){
							psarry = transactionvo.getAnnex().getAsJsonArray();
							for(int i=0; i<psarry.size(); i++){
								JsonObject ob =  psarry.get(i).getAsJsonObject();
								Set<String> keyarr = ob.keySet();
								Iterator<String> keys = keyarr.iterator();
								String key = keys.next();
								JsonElement ob2 = ob.get(key);
								JsonArray tmparry = ob2.getAsJsonArray();
								insert_kv = new KeyValueCollection();
								for(int j=0; j<tmparry.size(); j++){
									JsonObject tmpob =  tmparry.get(j).getAsJsonObject();
									Set<String> tmpkeyarr = tmpob.keySet();
									Iterator<String> tmpkeys = tmpkeyarr.iterator();
									String tmpkey = tmpkeys.next();
									String tmpValue = tmpob.get(tmpkey).getAsString();
									tmpValue = tmpValue.replaceAll("\\^\\^61\\^", "=");
									tmpValue = tmpValue.replaceAll("\\^\\^at\\^", "@");
									tmpValue = tmpValue.replaceAll("\\^\\^gt\\^", ">");
									tmpValue = tmpValue.replaceAll("\\^\\^lt\\^", "<");
									tmpValue = tmpValue.replaceAll("\\^\\^58\\^", ":");
									tmpValue = tmpValue.replaceAll("\\^\\^123\\^", "{");
									tmpValue = tmpValue.replaceAll("\\^\\^125\\^", "}");
									tmpValue = tmpValue.replaceAll("\\^\\^quot\\^", "\"");
									tmpValue = tmpValue.replaceAll("\\^\\^126\\^", "~");
									tmpValue = tmpValue.replaceAll("\\^\\^coma\\^", ",");
									tmpValue = tmpValue.replaceAll("\\^\\^47\\^", "/");

									insert_kv.addString(tmpkey, tmpValue);  
								}
								tmpkvp = new KeyValuePair(key, insert_kv);
								obj_kv.addPair(tmpkvp);
							}
						}
						transaction.setUserProperties(obj_kv);
						transaction.save();
					}else {	//업데이트 모드일때
						if(sectionKvp != null) {
							insert_kv = new KeyValueCollection();
							JsonArray psarry = null;
							KeyValuePair tmpkvp = null;
							if(transactionvo.getAnnex() != null){
								psarry = transactionvo.getAnnex().getAsJsonArray();
								for(int i=0; i<psarry.size(); i++){
									JsonObject tmpob =  psarry.get(i).getAsJsonObject();
									Set<String> tmpkeyarr = tmpob.keySet();
									Iterator<String> tmpkeys = tmpkeyarr.iterator();
									String tmpkey = tmpkeys.next();
									String tmpValue = tmpob.get(tmpkey).getAsString();
									tmpValue = tmpValue.replaceAll("\\^\\^61\\^", "=");
									tmpValue = tmpValue.replaceAll("\\^\\^at\\^", "@");
									tmpValue = tmpValue.replaceAll("\\^\\^gt\\^", ">");
									tmpValue = tmpValue.replaceAll("\\^\\^lt\\^", "<");
									tmpValue = tmpValue.replaceAll("\\^\\^58\\^", ":");
									tmpValue = tmpValue.replaceAll("\\^\\^123\\^", "{");
									tmpValue = tmpValue.replaceAll("\\^\\^125\\^", "}");
									tmpValue = tmpValue.replaceAll("\\^\\^quot\\^", "\"");
									tmpValue = tmpValue.replaceAll("\\^\\^126\\^", "~");
									tmpValue = tmpValue.replaceAll("\\^\\^coma\\^", ",");
									tmpValue = tmpValue.replaceAll("\\^\\^47\\^", "/");
									insert_kv.addString(tmpkey, tmpValue);
								}
								tmpkvp = new KeyValuePair(transactionvo.getSection_name(), insert_kv);
								obj_kv.remove(transactionvo.getSection_name());
								obj_kv.addPair(tmpkvp);

							}
							transaction.setUserProperties(obj_kv);
							transaction.save();
						}				
					}
				}
			}
		}catch (ConfigException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return transaction;
	}	


}
