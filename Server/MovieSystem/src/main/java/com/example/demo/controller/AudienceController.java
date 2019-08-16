package com.example.demo.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.encryption.Signature;
import com.example.demo.json.JsonRequest;
import com.example.demo.json.JsonResponse;
import com.example.demo.management.PasswordManagement;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.Passwordtable;
import com.example.demo.service.AudienceService;
import com.example.demo.service.PasswordService;

/**
 * 
* @ClassName: AudienceController 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:02:47 
*
 */
@RequestMapping(value = "/movieorder/audience")
@RestController
public class AudienceController {

	@Resource
	private PasswordService passwordService;
	@Resource
	private AudienceService audienceService;
	
	/**
	 * 
	* @Title: getAudience 
	* @Description: 根据手机号查询。精确查询
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getaudience", method = RequestMethod.POST)
	public String getAudience(@RequestBody JSONObject jsonParam) {
		JsonRequest request = new JsonRequest(); 
		JsonRequest.getOuter(jsonParam, request);
		Passwordtable passwordTable = new Passwordtable(passwordService.queryQuickByKey(request));
		Passwordtable SigKey = passwordService.querySlowByKeyWithoutDelete(request.signaturekey);
		PasswordManagement.decryptionWithSignature(request,passwordTable,SigKey);
		JsonResponse response = new JsonResponse();
		response.version = request.version;
		if(request.sign>0) {
			response.action = "DecrytionErrorResponse";
			response.time = new Date().getTime();
			response.result = String.valueOf(200+request.sign);
		    return JsonResponse.getJsonResponse(response);
		}
		Signature signature = new Signature(request.signature);
		signature.setKey(SigKey.getPasswordKey());
		signature.setRsaPublic(SigKey.getPasswordPublic());
		if(passwordService.judgeSignature(signature)==1){
			response.action = "SignatureOverdueResponse";
			response.time = new Date().getTime();
			response.result = "207";
			response.rsapassword = request.rsapassword;
			return JsonResponse.getJsonResponse(response);
		}
		response.action = "UpdateAudienceResponse";
		response.despassword = request.despassword;
	    String audiencePhone = request.content.getString("audiencePhone");
	    Audience audience ;
	    //至此数据全部处理完成
  		try {
  			//查询条件判断，注意优先级，顺序不能乱
  			if(audiencePhone!=null&&!audiencePhone.equals("")) {
  				audience = audienceService.queryById(audiencePhone);
  			}
  			else {
  				//筛选条件组合不合法
  				response.time = new Date().getTime();
  				response.result = "301";
  			    return JsonResponse.getJsonResponse(response);
  			}
  		    // 封装返回结果
  			JSONObject audienceJSONObject = new JSONObject();
  			audienceJSONObject.put("audiencePhone", audience.getAudiencePhone());
  			audienceJSONObject.put("audienceVipstatus", audience.getAudienceVipstatus());
  			audienceJSONObject.put("vipId", audience.getVipId());
  			response.content = audienceJSONObject;
  			response.result = "100";
  		    return JsonResponse.getJsonResponse(response);
  		}
  		catch (Exception e) {
  			// : handle exception
  			response.time = new Date().getTime();
  			response.result = "400";
  		    return JsonResponse.getJsonResponse(response);
  		}
	}
}
