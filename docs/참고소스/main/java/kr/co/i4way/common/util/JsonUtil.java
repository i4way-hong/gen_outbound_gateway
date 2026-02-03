package kr.co.i4way.common.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.i4way.util.AES256;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
 
/**
 *    일반문자열 유틸.
 *
 * @author someone
 * @version 1.0.0
 */
public class JsonUtil {
 
	@Value("${ccc-service.enc-iv}")
    private String enc_iv;
	
	@Value("${ccc-service.enc-key}")
    private String enc_key;


    /**
     * Map을 json으로 변환한다.
     *
     * @param map Map<String, Object>.
     * @return JSONObject.
     */
    public static JSONObject getJsonStringFromMap( Map<String, Object> map )
    {
        JSONObject jsonObject = new JSONObject();
        for( Map.Entry<String, Object> entry : map.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();
            jsonObject.put(key, value);
        }
        
        return jsonObject;
    }
    
    /**
     * List<Map>을 jsonArray로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return JSONArray.
     */
    public static JSONArray getJsonArrayFromList( List<Map<String, Object>> list )
    {
        JSONArray jsonArray = new JSONArray();
        for( Map<String, Object> map : list ) {
            jsonArray.add( getJsonStringFromMap( map ) );
        }
        
        return jsonArray;
    }
    
    /**
     * List<Map>을 jsonString으로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return String.
     */
    public static String getJsonStringFromList( List<Map<String, Object>> list )
    {
        JSONArray jsonArray = getJsonArrayFromList( list );
        return jsonArray.toJSONString();
    }
 
    /**
     * JsonObject를 Map<String, String>으로 변환한다.
     *
     * @param jsonObj JSONObject.
     * @return Map<String, Object>.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapFromJsonObject( JSONObject jsonObj )
    {
        Map<String, Object> map = null;
        
        try {
            
            map = new ObjectMapper().readValue(jsonObj.toJSONString(), Map.class) ;
            
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return map;
    }
 
    /**
     * JsonArray를 List<Map<String, String>>으로 변환한다.
     *
     * @param jsonArray JSONArray.
     * @return List<Map<String, Object>>.
     */
    public static List<Map<String, Object>> getListMapFromJsonArray( JSONArray jsonArray )
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        if( jsonArray != null )
        {
            int jsonSize = jsonArray.size();
            for( int i = 0; i < jsonSize; i++ )
            {
                Map<String, Object> map = JsonUtil.getMapFromJsonObject( ( JSONObject ) jsonArray.get(i) );
                list.add( map );
            }
        }
        
        return list;
    }
    

    public static Map<String, Object> decrypt_param(Map<String, Object> param, String enc_key, String enc_iv, String enc_yn){
		Map<String, Object> rtn_param = null;
		String decData;
		try {
			if(enc_yn.equals("Yes")) {
				String encData = getParam(param, "encData", "S");
				decData = AES256.decrypt(encData, enc_key, enc_iv);
				JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
				JSONObject json = (JSONObject) parser.parse(decData);
				rtn_param = JsonUtil.getMapFromJsonObject(json);
			}else {
				rtn_param = param;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn_param;
	}
	
    public static JSONObject encrypt_param(JSONObject obj, String enc_key, String enc_iv, String enc_yn){
		JSONObject encobj = new JSONObject();
		try {
			if(enc_yn.equals("Yes")) {
				encobj.put("encData", AES256.encrypt(obj.toString(), enc_key, enc_iv));
			}else {
				encobj = obj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encobj;
	}
    
	private static String getParam(Map<String, Object> param, String qryStr, String type){
		String rtnstr = "";
		if(type.equals("I")){
			rtnstr = "0";
		}
		try{
			rtnstr = param.get(qryStr).toString();
		}catch(Exception e){ 
			//rtnstr = "";
		}
		return rtnstr;
	}
}

