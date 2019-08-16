package com.example.demo.json;


import com.alibaba.fastjson.JSONObject;

/**
 * 
* @ClassName: JsonRequest 
* @Description: 封装request交互数据json的最外层
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:45:25 
*
 */
public class JsonRequest {

	
	public String version;
	public String action;
	public long time;
	public int passwordkey=0;
	public String rsapassword;
	public String despassword;
	public String signature;
	public int sign=-1;
	public int signaturekey;
	public int pageno;
	public int pagesize;
	public JSONObject content;
	public String contentStr;
	
	
	/**
	 * 
	* @Title: getOuter 
	* @Description: 
	* @param request
	* @param result
	 */
	public static void getOuter(JSONObject request, JsonRequest result) {
		result.version = request.getString("version");
		result.action = request.getString("action");
		result.time = request.getLong("time");
		result.passwordkey = request.getIntValue("passwordkey");
		result.rsapassword = request.getString("rsapassword");
		result.despassword = request.getString("despassword");
		result.signature = request.getString("signature");
		result.signaturekey = request.getIntValue("signaturekey");
		result.contentStr = request.getString("content");
		JSONObject tempPage = request.getJSONObject("page");
		if(tempPage!=null) {
			result.pageno = tempPage.getIntValue("pageno");
			result.pagesize = tempPage.getIntValue("pagesize");
		}
	}
	
	

}
