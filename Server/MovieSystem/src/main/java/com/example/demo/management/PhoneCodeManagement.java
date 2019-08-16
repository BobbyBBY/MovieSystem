package com.example.demo.management;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.example.demo.encryption.PhoneCode;
import com.example.demo.ucpaas.AbsRestClientUcpaas;
import com.example.demo.ucpaas.JsonReqClientUcpaas;

/**
 * 
* @ClassName: PhoneCodeManagement 
* @Description: 维护手机验证码，有效期3分钟
* @author xuanpengyu@faxmail.com
* @date 2019年7月4日 上午11:31:51 
*
 */
public class PhoneCodeManagement {
	
	static AbsRestClientUcpaas InstantiationRestAPI() {
		return new JsonReqClientUcpaas();
	}
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
						 phoneCodeManager();
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

	private static HashMap<String ,PhoneCode> phoneCodeHP = new HashMap<String ,PhoneCode>();
	
	private static Queue<String> phoneQueue = new LinkedList<String>();
	
	public static void phoneCodeManager() {
		PhoneCode temp;
		Long time = new Date().getTime() - 180000;
		if(phoneCodeHP.size()>0) {
			temp = phoneCodeHP.get(phoneQueue.peek());
			if(temp.getTime()<time) {
				phoneCodeHP.remove(phoneQueue.poll());
			}
		}
	}
	
	public static String getPhoneCode(String phone) {
		if(phoneCodeHP.containsKey(phone)) {
			return phoneCodeHP.get(phone).getCode();
		}
		else {
			String code = String.format("%06d",(int)(Math.random()*999999));
			PhoneCode phoneCode = new PhoneCode(phone,code);
			phoneCodeHP.put(phone, phoneCode);
			phoneQueue.add(phone);
			return code;
		}
	}
	
	public static int judgePhoneCode(String phone, String code) {
		if(phoneCodeHP.containsKey(phone)) {
			if(phoneCodeHP.get(phone).getCode().equals(code)) {
				phoneCodeHP.remove(phone);
				phoneQueue.remove(phone);
				return 1;
			}
		}
		return 0;
	}
	
	private static String sendSms(String sid, String token, String appid, String templateid, String param, String mobile, String uid){
		return InstantiationRestAPI().sendSms(sid, token, appid, templateid, param, mobile, uid);
	}
	public static String sendPhoneCode(String phone, String code){
		String sid = "d91697765509dd1393d63c38b75ba19c";
		String token = "d5bf4190d1b4bfc030602e5cdc61e500";
		String appid = "4710772c32854bceba501cd0b6dad105";
		String templateid = "481376";
		String uid = "xuanpengyu";
		return sendSms(sid, token, appid, templateid, code, phone, uid);
	}
	
}
