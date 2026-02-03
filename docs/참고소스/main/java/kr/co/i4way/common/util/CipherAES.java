package kr.co.i4way.common.util;

import java.lang.reflect.Field;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class CipherAES {
	static String AES_CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";

	static String MONITOR_KEY_VALUE = "BLUEQsiteUSE#MNS";
	static String MONITOR_IV_VALUE = "SmartLOGIN201407";

	private static String getKeyIvValue(String staticValue) throws Exception {
		Class cipherAes = CipherAES.class;
		Field cipherAesFields[] = cipherAes.getDeclaredFields();

		String keyIvValue = "";
		for (int i = 0; i < cipherAesFields.length; i++) {
			if (staticValue.equals(cipherAesFields[i].getName())) {
				keyIvValue = (String) cipherAesFields[i].get(cipherAes);

				break;
			}
		}
		return keyIvValue;
	}

// Key 생성
	public static SecretKey generateKey(String keyValue) throws Exception {
		return new SecretKeySpec(keyValue.getBytes("UTF-8"), "AES");
	}

// 복호화(String Decode)
	public static String decrypt(String keyScn, String param) throws Exception {
		System.out.println("keyScn==>"+keyScn);
		System.out.println("param==>"+param);
		return decrypt(AES_CBC_PKCS5Padding, param, getKeyIvValue(keyScn + "_KEY_VALUE"),
				getKeyIvValue(keyScn + "_IV_VALUE"), "string");
	}

// 복호화
	public static String decrypt(String transType, String srcData, String keyValue, String ivValue, String encodeType)
			throws Exception {
		String result = "";
		byte[] decData = null;

		try { 
			SecretKey key = generateKey(keyValue);

			Cipher cipher = Cipher.getInstance(transType);

			if (transType.equals(AES_CBC_PKCS5Padding)) {
				if (null != ivValue && ivValue.length() > 0) {
					cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivValue.getBytes()));
				} else {
					cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
				}
			} else {
				cipher.init(Cipher.DECRYPT_MODE, key);
			}

			decData = cipher.doFinal(Base64.decodeBase64(srcData)); 

			result = new String(decData);
		} catch (Exception e) {
			result = "";
		}

		return result;
	}
}
