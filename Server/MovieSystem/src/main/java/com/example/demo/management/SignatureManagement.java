package com.example.demo.management;

import java.util.Date;

import com.example.demo.encryption.Signature;

public class SignatureManagement {
	/**
	 * 
	* @Title: getSignature 
	* @Description: 获取签名。签名默认有效期，31天
	* @param phone
	* @return
	 */
	public static Signature getSignature(String phone) {
		//2678400000
		Signature signature = new Signature();
		long timeNow = new Date().getTime();
		signature.setGeneratedTime(timeNow);
		signature.setPhone(phone);
		signature.setEndTime(timeNow + Long.valueOf("2678400000"));
		return signature;
	}
	
	/**
	 * 
	* @Title: getSignature 
	* @Description: 获取签名。自定义有效期
	* @param phone
	* @param time 签名有效期
	* @return
	 */
	public static Signature getSignature(String phone,long time) {
		Signature signature = new Signature();
		long timeNow = new Date().getTime();
		signature.setGeneratedTime(timeNow);
		signature.setPhone(phone);
		signature.setEndTime(timeNow + time);
		return signature;
	}
	
	/**
	 * 
	* @Title: judgeSignature 
	* @Description: 判断签名是否有效
	* @param request
	* @return
	 */
	public static int judgeSignature(Signature signature) {
		Date now = new Date();
		//if 打日志用
		if(now.after(new Date(signature.getEndTime()))) {
			return 7;
		}
		else {
			return 0;
		}
	}
}
