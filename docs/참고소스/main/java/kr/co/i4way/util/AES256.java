package kr.co.i4way.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256 {

    public static String alg = "AES/CBC/PKCS5Padding";

    public static void main (String[] args) throws Exception {
    	
    	String encStr = "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";
    	System.out.println(decrypt(encStr, "12345678901234567890123456789012", "1234567890123456"));
    	    	
    	
    	//String jsonstr = "{\"TENANT_DBID\": \"1\",\"NAME\": \"HT102_H스킬_T1_관리고객\"}";
    	//String jsonstr = "{\"TENANT_DBID\": 1,\"DBID\": 0,\"NAME\": \"HT157_H스킬_T1_CCS_일반상담\"}";
//    	String jsonstr = "{\"QUEUE_NAME\": \"대표번호_접수_전체\",\"GROUP_NAME\": \"AG01C01T01P01_1조\",\"EMPLOYEE_ID\": \"9420480\"}";
    	//String aaa = encrypt(jsonstr, "12345678901234567890123456789012", "1234567890123456");
    	//System.out.println(aaa);
    	
    }
    
    public static String encrypt(String text, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String cipherText, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }

}