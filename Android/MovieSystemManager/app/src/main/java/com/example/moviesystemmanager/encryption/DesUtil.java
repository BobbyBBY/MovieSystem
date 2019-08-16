package com.example.moviesystemmanager.encryption;



import com.sun.crypto.provider.SunJCE;

import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * 
* @ClassName: DesUtil 
* @Description: TODO
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:41:23 
*
 */
public class DesUtil {
	private static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * 
	* @Title: getDESPassword 
	* @Description: TODO normally ,running time is 0.9ms
	* @return
	 */
	public static String getDESPassword() {
		//192bit
		StringBuilder result = new StringBuilder();
		for(int i=0;i<48;++i) {
			result.append(hexDigits[(int)(Math.random()*16)]);
		}
		return result.toString();
	}
	
	public static String UNDES(String args, String inputKey,String CHARSET) {
		try {
			return new String(DES3Decode(inputKey, base64Decode(args)),CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String DES(String args, String inputKey) {
		try {
			byte[] bb = DES3Encode(inputKey, args.getBytes());
			return base64Encode(bb);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String DES(String args, String inputKey,String charSet) {
		try {
			byte[] bb = DES3Encode(inputKey, args.getBytes(charSet));
			return base64Encode(bb);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private static byte[] DES3Decode(String key, byte[] source) {
		try {
			SecretKey deskey = new SecretKeySpec(Hex2Bin(key), "DESede");
			Cipher c3des = Cipher.getInstance("DESede");
			c3des.init(Cipher.DECRYPT_MODE, deskey);
			return c3des.doFinal(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] base64Decode(String str) throws Exception {
		return Base64.getDecoder().decode(str.getBytes("UTF-8"));
	}

	protected static byte[] Hex2Bin(String hex) throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}

	private static byte[] DES3Encode(String key, byte[] source) {
		try {
			Security.addProvider(new SunJCE());
			SecretKey deskey = new SecretKeySpec(Hex2Bin(key), "DESede");
			Cipher c3des = Cipher.getInstance("DESede");
			c3des.init(Cipher.ENCRYPT_MODE, deskey);
			return c3des.doFinal(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String base64Encode(byte[] bt) throws Exception {

		return Base64.getEncoder().encodeToString(bt);
	}

}
