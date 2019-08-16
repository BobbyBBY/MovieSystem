package com.example.moviesystemclient.encryption;

import java.security.MessageDigest;

/**
 * 
* @ClassName: MD5Util 
* @Description: TODO
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:39:53 
*
 */
public class MD5Util {
	public static final String MD5(String s) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();

			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];

			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				str[(k++)] = hexDigits[(b >> 4 & 0xF)];
				str[(k++)] = hexDigits[(b & 0xF)];
			}
			return new String(str);
		} catch (Exception e) {
		}
		return null;
	}
}