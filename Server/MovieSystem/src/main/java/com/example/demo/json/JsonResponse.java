package com.example.demo.json;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.encryption.DesUtil;

/**
 * 
* @ClassName: JsonResponse 
* @Description:  加密content
* @author xuanpengyu@faxmail.com
* @date 2019年7月2日 下午8:22:41 
*
 */
public class JsonResponse {

	public String version;
	public String action;
	public long time;
	public String rsapassword;
	public String despassword;
	public String result;
	public int pageno = -1;
	public int pagesize = -1;
	public JSONObject content;
	public String contentStr;
	
	public static String getJsonResponse(JsonResponse response, String despassword) {
		if(response.content!=null) {
			response.contentStr = DesUtil.DES(response.content.toString(), despassword, "UTF-8");
		}
		JSONObject result = new JSONObject();
		result.put("version", response.version);
	    result.put("action", response.action);
	    result.put("time", new Date().getTime());
	    result.put("rsapassword", response.rsapassword);
	    result.put("result", response.result);
	    if(response.pageno!=-1&&response.pagesize!=-1) {
	    	JSONObject page = new JSONObject();
		    page.put("pageno", response.pageno);
		    page.put("pagesize", response.pagesize);
		    result.put("page", page);
	    }
	    if(response.contentStr!=null&&!response.contentStr.equals("")) {
	    	result.put("content", response.contentStr);
	    }
		return result.toString();
		
	}
	
	public static String getJsonResponse(JsonResponse response) {
		return getJsonResponse(response,response.despassword);
	}
	public static String getJsonResponseWithoutEncryption(JsonResponse response) {
		if(response.content!=null) {
			response.contentStr = response.content.toString();
		}
		JSONObject result = new JSONObject();
		result.put("version", response.version);
	    result.put("action", response.action);
	    result.put("time", new Date().getTime());
	    result.put("rsapassword", response.rsapassword);
	    result.put("result", response.result);
	    if(response.pageno!=-1&&response.pagesize!=-1) {
	    	JSONObject page = new JSONObject();
		    page.put("pageno", response.pageno);
		    page.put("pagesize", response.pagesize);
		    result.put("page", page);
	    }
	    if(response.contentStr!=null&&!response.contentStr.equals("")) {
	    	result.put("content", response.contentStr);
	    }
		return result.toString();
	}
	
	
}
