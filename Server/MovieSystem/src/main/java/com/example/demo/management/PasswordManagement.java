package com.example.demo.management;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.encryption.DesUtil;
import com.example.demo.encryption.Password;
import com.example.demo.encryption.RSAEncrypt;
import com.example.demo.json.JsonRequest;
import com.example.demo.pojo.Passwordtable;

/**
 * 
* @ClassName: PasswordManagement 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午9:31:20 
*
 */
public class PasswordManagement {
	private static HashMap<Integer ,Password> quickHP = new HashMap<Integer ,Password>();
	
	private static Queue<Integer> quickQueue = new LinkedList<Integer>();

	private static boolean cycling = false ;
	
	public static synchronized void cycle() {
		if(!cycling) {
			cycling = true;
			new Thread() {
				 @Override
				    public void run() {
					 boolean temp = true;
					 while(temp) {
						 temp = false;
						 quickManager();
						 try {
							 temp=true;
							Thread.sleep(15000);
						} catch (InterruptedException e) {
							temp = false;
							e.printStackTrace();
						}
					 }
				    }
			}.start();
		}
	}
	
	/**
	 * 
	* @throws NoSuchAlgorithmException 
	 * @Title: quickManager 
	* @Description:  三分钟有效
	 */
	public static void quickManager() {
		Password temp;
		Long time = new Date().getTime() - 180000;
		if(quickHP.size()>0) {
			temp = quickHP.get(quickQueue.peek());
			if(temp.getTime()<time) {
				//过期了
				quickHP.remove(quickQueue.poll());
			}
		}
	}
	
	/**
	 * 
	* @Title: getQuick 
	* @Description: 获取新的快密钥
	* @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static Password getQuick() throws NoSuchAlgorithmException {
		String time = String.valueOf(new Date().getTime());
		String[] rsaPassword = RSAEncrypt.genKeyPair(1024);
		int key = Integer.valueOf(time.substring(time.length()-9, time.length()));
		Password newPassword = new Password(key, rsaPassword[0], rsaPassword[1], DesUtil.getDESPassword());
		quickQueue.add(newPassword.getKey());
		quickHP.put(newPassword.getKey(), newPassword);
		return newPassword;
	}
	
	/**
	 * 
	* @Title: getQuick 
	* @Description: 根据key查询快密钥，若没有该key，则返回null
	* @param key
	* @return
	 */
	public static Password getQuick(int key) {
		return quickHP.get(key);
	}
	
	/**
	 * 
	* @Title: getNewSlow 
	* @Description: 获取新的慢密钥。查询密钥在PasswordService中
	* @return
	* @throws NoSuchAlgorithmException
	 */
	public static Password getSlow() throws NoSuchAlgorithmException {
		Password newPassword = new Password();
		String time = String.valueOf(new Date().getTime());
		newPassword.setKey(Integer.valueOf(time.substring(time.length()-9, time.length())));//may be danger
		String[] rsaPassword = RSAEncrypt.genKeyPair(512);
		newPassword.setPublicPassword(rsaPassword[0]);
		newPassword.setPrivatePassword(rsaPassword[1]);
		newPassword.setDESPassword(DesUtil.getDESPassword());
		return newPassword;
	}
	
//	/**
//	 * 
//	* @Title: passwordOverdue 
//	* @Description: 判断如果过期，则准备好返回结果  已弃用
//	* @param jsonRequest
//	* @return
//	 */
//	public static JsonResponse passwordOverdueOld(JsonRequest jsonRequest) {
//		if(jsonRequest.sign==1) {
//			JsonResponse jsonResponse = new JsonResponse();
//			jsonResponse.version = jsonRequest.version;
//			jsonResponse.action = "PasswordOverdueResponse";
//			jsonResponse.time = new Date().getTime();
//			jsonResponse.rsapassword = jsonRequest.rsapassword;
//			jsonResponse.result = "201";
//			return jsonResponse;
//		}
//		return null;
//	}
	
	/**
	 * 
	* @Title: getJsonRequestWithSignature 
	* @Description: 解密content，包含签名
	* @param result
	* @param passwordtable
	* @param SignKey
	 */
	public static void decryptionWithSignature(JsonRequest result, Passwordtable passwordtable, Passwordtable SignKey) {
		if(result.version.equals("1.0")) {
			try {
				result.despassword =passwordtable.getPasswordDes();
			} catch (Exception e) {
				result.sign = 2;
				return;
			}
			try {
				result.content = JSONObject.parseObject(DesUtil.UNDES(result.contentStr, passwordtable.getPasswordDes(), "UTF-8"));
			}
			catch (Exception e) {
				result.sign = 4;
				return;
			}
			try {
				result.signature = RSAEncrypt.decrypt(result.signature, SignKey.getPasswordPrivate());
			} catch (Exception e) {
				result.sign = 6;
				return;
			}
		}
		
	}
	
	/**
	 * 
	* @Title: getJsonRequestWithoutSignature 
	* @Description: 解密content，不包含签名
	* @param result
	* @param passwordTable
	 */
	public static void decryptionWithoutSignature(JsonRequest result, Passwordtable passwordTable) {
		if(result.version.equals("1.0")) {
			try {
//				result.despassword = RSAEncrypt.decrypt(result.despassword, passwordTable.getPasswordPrivate());
				result.despassword = passwordTable.getPasswordDes();
			} catch (Exception e) {
				result.sign = 2;
				return;
			}
			try {
				result.content = JSONObject.parseObject(DesUtil.UNDES(result.contentStr, passwordTable.getPasswordDes(), "UTF-8"));
			}
			catch (Exception e) {
				result.sign = 4;
				return;
			}
		}
		
	}
	
}
