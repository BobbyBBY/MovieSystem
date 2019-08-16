package com.example.moviesystemclient.server;

import com.alibaba.fastjson.JSONObject;
import com.example.moviesystemclient.encryption.DecryptionJson;
import com.example.moviesystemclient.encryption.EncryptionJson;
import com.example.moviesystemclient.encryption.Password;
import com.example.moviesystemclient.encryption.PasswordManagement;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.util.Date;

/**
 * @Title: HttpUtils.java
 * @Package: com.example.moviesystemclient.server
 * @Description: 
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 11:02
 * @version V1.0
 */
public class HttpUtils {

	public static String doHttpPost(String url, JSONObject message){
		String resultStr = null;
		if(message==null){
			return null;
		}
		try {

			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(url);
			client.getParams().setContentCharset("GBK");
			method.setRequestHeader("ContentType", "application/json;charset=GBK");
			RequestEntity se = new StringRequestEntity(message.toString() ,"application/json" ,"GBK");
			method.setRequestEntity(se);
			client.executeMethod(method);
			resultStr = method.getResponseBodyAsString();
			return resultStr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSONObject doHttpPostSlow(String url, JSONObject message) {
		message.put("rsapassword", PasswordManagement.getRSAPublic());
		message.put("passwordkey", PasswordManagement.getRSAKey());
		message.put("time", new Date().getTime());
		message.put("version", "1.0");
		message = EncryptionJson.slow(message);

		String resultStr = doHttpPost(url,message);

		if (resultStr==null||resultStr.equals("")) {
			return null;
		}
		JSONObject result = JSONObject.parseObject(resultStr);
		result = DecryptionJson.slow(result);
		return result;
	}

	public static JSONObject doHttpPostQuick(String url, JSONObject message, Password password) {
		message.put("rsapassword", password.getPublicPassword());
		message.put("passwordkey", password.getKey());
		message.put("time", new Date().getTime());
		message.put("version", "1.0");
		message = EncryptionJson.quick(message,password);

		String resultStr = doHttpPost(url,message);

		if (resultStr==null||resultStr.equals("")) {
			return null;
		}
		JSONObject result = JSONObject.parseObject(resultStr);
		result = DecryptionJson.quick(result,password);
		return result;
	}
	public static JSONObject doHttpPostQuickSignature(String url, JSONObject message, Password password) {
		message.put("rsapassword", password.getPublicPassword());
		message.put("passwordkey", password.getKey());
		message.put("signature", PasswordManagement.getSignature());
		message.put("signaturekey", PasswordManagement.getSignatureKey());
		message.put("time", new Date().getTime());
		message.put("version", "1.0");
		message = EncryptionJson.quickAndSignature(message,password);

		String resultStr = doHttpPost(url,message);

		if (resultStr==null||resultStr.equals("")) {
			return null;
		}
		JSONObject result = JSONObject.parseObject(resultStr);
		result = DecryptionJson.quickAndSignature(result,password);
		return result;
	}


}
