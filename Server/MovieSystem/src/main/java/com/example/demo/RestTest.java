/**
 * @author Tony
 * @date 2018-01-10
 * @project rest_demo
 */
package com.example.demo;

import com.example.demo.ucpaas.AbsRestClientUcpaas;
import com.example.demo.ucpaas.JsonReqClientUcpaas;


public class RestTest {

	static AbsRestClientUcpaas InstantiationRestAPI() {
		return new JsonReqClientUcpaas();
	}
	
	public static void testSendSms(String sid, String token, String appid, String templateid, String param, String mobile, String uid){
		try {
			String result=InstantiationRestAPI().sendSms(sid, token, appid, templateid, param, mobile, uid);
			System.out.println("Response content is: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 测试说明  启动main方法后，请在控制台输入数字(数字对应 相应的调用方法)，回车键结束
	 * 参数名称含义，请参考rest api 文档
	 * @throws IOException 
	 * @method main
	 */
//	public static void main(String[] args) throws IOException{
//			String sid = "d91697765509dd1393d63c38b75ba19c";
//			String token = "d5bf4190d1b4bfc030602e5cdc61e500";
//			String appid = "4710772c32854bceba501cd0b6dad105";
//			String templateid = "481376";
//			String param = "123654";
//			String mobile = "18833212207";
//			String uid = "bby";
//			testSendSms(sid, token, appid, templateid, param, mobile, uid);
//	}
}
