package com.example.demo.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.encryption.Password;
import com.example.demo.encryption.RSAEncrypt;
import com.example.demo.encryption.Signature;
import com.example.demo.json.JsonRequest;
import com.example.demo.json.JsonResponse;
import com.example.demo.management.PasswordManagement;
import com.example.demo.management.PhoneCodeManagement;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.Passwordtable;
import com.example.demo.service.AudienceService;
import com.example.demo.service.PasswordService;

/**
 * 
* @ClassName: PasswordController 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:02:38 
*
 */
@RequestMapping(value = "/movieorder/password")
@RestController
public class PasswordController {

	@Resource
	private PasswordService passwordService;
	@Resource
	private AudienceService audienceService;
	
	
	/**
	 * 
	* @Title: getSlow 
	* @Description: 更新原来的慢密钥。若密钥没有过期，则可能返回相同的密钥
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getslow", method = RequestMethod.POST)
	public String getSlow(@RequestBody JSONObject jsonParam) {
		JsonRequest request = new JsonRequest(); 
		JsonRequest.getOuter(jsonParam, request);
		JsonResponse response = new JsonResponse();
		String RSAPublic_C = request.rsapassword;
		response.version = request.version;
		response.action = "SlowPasswordResponse";
		if(request.passwordkey==0) {
			//暂时跳过密码强度验证
//			if(request.rsapassword.length()<512) {
//				response.action = "DecrytionErrorResponse";
//				response.time = new Date().getTime();
//				response.result = "205";
//				return JsonResponse.getJsonResponseRSA(response);
//			}
			Password password;
			try {
				password = passwordService.getSlow();
			} catch (NoSuchAlgorithmException e) {
				response.action = "DecrytionErrorResponse";
				response.time = new Date().getTime();
				response.result = "208";
				return JsonResponse.getJsonResponse(response);
			}
			JSONObject content = new JSONObject();
			try {
				content.put("DESPassword", RSAEncrypt.encrypt(password.getDESPassword(), RSAPublic_C));
			} catch (Exception e) {
				response.action = "DecrytionErrorResponse";
				response.time = new Date().getTime();
				response.result = "203";
				return JsonResponse.getJsonResponse(response);
			}
			content.put("RSAPublic", password.getPublicPassword());
			content.put("RSAKey", password.getKey());
			response.content = content;
			response.rsapassword = RSAPublic_C;
			return JsonResponse.getJsonResponseWithoutEncryption(response);
		}
		else {
			Passwordtable passwordTable = passwordService.querySlowByKeyWithDelete(request);
			PasswordManagement.decryptionWithoutSignature(request,passwordTable);
			if(request.sign>0) {
				response = new JsonResponse();
				response.version = request.version;
				response.action = "DecrytionErrorResponse";
				response.time = new Date().getTime();
				response.result = String.valueOf(200+request.sign);
			    return JsonResponse.getJsonResponse(response);
			}
			response = new JsonResponse();
			Password password;
			try {
				password = passwordService.getSlow();
			} catch (NoSuchAlgorithmException e) {
				response.action = "DecrytionErrorResponse";
				response.time = new Date().getTime();
				response.result = "208";
				return JsonResponse.getJsonResponse(response);
			}
			JSONObject content = new JSONObject();
			try {
				content.put("DESPassword", RSAEncrypt.encrypt(password.getDESPassword(), RSAPublic_C));
			} catch (Exception e) {
				response.action = "DecrytionErrorResponse";
				response.time = new Date().getTime();
				response.result = "203";
				return JsonResponse.getJsonResponse(response);
			}
			content.put("RSAPublic", password.getPublicPassword());
			content.put("RSAKey", password.getKey());
			response.content = content;
			return JsonResponse.getJsonResponseWithoutEncryption(response);
		}
	}
	
	/**
	 * 
	* @Title: getQuick 
	* @Description: 获取新的的快密钥，快密钥都是一次性的。
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getquick", method = RequestMethod.POST)
	public String getQuick(@RequestBody JSONObject jsonParam) {
		JsonRequest request = new JsonRequest(); 
		JsonRequest.getOuter(jsonParam, request);
		Passwordtable passwordTable = passwordService.querySlowByKeyWithDelete(request);
		PasswordManagement.decryptionWithoutSignature(request,passwordTable);
		JsonResponse response = new JsonResponse();
		response.version = request.version;
		if(request.sign>0) {
			response.action = "DecrytionErrorResponse";
			response.time = new Date().getTime();
			response.result = String.valueOf(200+request.sign);
		    return JsonResponse.getJsonResponse(response);
		}
		response.action = "QuickPasswordResponse";
		response.despassword = passwordTable.getPasswordDes();
		Password password;
		try {
			password = passwordService.getQuick();
		} catch (NoSuchAlgorithmException e) {
			response.time = new Date().getTime();
			response.result = "401";
			return JsonResponse.getJsonResponse(response);
		}
		JSONObject content = new JSONObject();
		content.put("DESPassword", password.getDESPassword());
		content.put("RSAPublic", password.getPublicPassword());
		content.put("RSAKey", password.getKey());
		response.content = content;
		response.result = "100";
		return JsonResponse.getJsonResponse(response);
	}

	/**
	 * 
	* @Title: register 
	* @Description: 先获取慢密钥，再获取手机验证码，再获取快密钥，再登陆。登陆时使用快密钥加密。登陆即是获取签名的过程，比对手机验证码成功则返回签名
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@RequestBody JSONObject jsonParam) {
		JsonRequest request = new JsonRequest(); 
		JsonRequest.getOuter(jsonParam, request);
		Passwordtable passwordTable = new Passwordtable(passwordService.queryQuickByKey(request));
		PasswordManagement.decryptionWithoutSignature(request,passwordTable);
		JsonResponse response = new JsonResponse();
		response.version = request.version;
		if(request.sign>0) {
			response.action = "DecrytionErrorResponse";
			response.time = new Date().getTime();
			response.result = String.valueOf(200+request.sign);
		    return JsonResponse.getJsonResponse(response);
		}
		response.action = "RegisterResponse";
		response.despassword = request.despassword;
		String phone = request.content.getString("phone");
		String phoneCode = request.content.getString("phoneCode");
		//至此数据全部处理完成
		//比对手机验证码，如果正确获取签名。
		if(PhoneCodeManagement.judgePhoneCode(phone, phoneCode)==1) {
			Signature signature;
			JSONObject content = new JSONObject();
			try {
				signature = passwordService.getSignature(phone);
				content.put("signature", RSAEncrypt.encrypt(signature.toString(),signature.getRsaPublic()));
				System.out.println("signature");
				System.out.println(content.getString("signature"));
				System.out.println("signatureKey");
				System.out.println(signature.getKey());
			} catch (Exception e) {
				System.out.println(e.getMessage());
				response.time = new Date().getTime();
				response.result = "210";
				return JsonResponse.getJsonResponse(response);
			}
			Audience audience = new Audience();
			audience.setAudiencePhone(phone);
			audience.setAudienceVipstatus(1);
			//应该不会写入失败
			int updateResult = audienceService.updateAudience(audience);
			if(updateResult==1) {
				content.put("signatureKey", signature.getKey());
				response.result = "100";
				response.time = new Date().getTime();
				response.content = content;
			}
			else  {
				response.time = new Date().getTime();
				response.result = "401";
			}
		}
		else {
			response.time = new Date().getTime();
			response.result = "209";
		}
		return JsonResponse.getJsonResponse(response);
	}
	
	/**
	 * 
	* @Title: getPhoneCode 
	* @Description: 获取手机验证码
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST)
	public String getPhoneCode(@RequestBody JSONObject jsonParam) {
		JsonRequest request = new JsonRequest(); 
		JsonRequest.getOuter(jsonParam, request);
		Passwordtable passwordTable = passwordService.querySlowByKeyWithDelete(request);
		PasswordManagement.decryptionWithoutSignature(request,passwordTable);
		JsonResponse response = new JsonResponse();
		response.version = request.version;
		if(request.sign>0) {
			response.action = "DecrytionErrorResponse";
			response.time = new Date().getTime();
			response.result = String.valueOf(200+request.sign);
		    return JsonResponse.getJsonResponse(response);
		}
		response.action = "PhoneCodeResponse";
		response.despassword = request.despassword;
		String phone = request.content.getString("phone");
		try {
			String newPhoneCode = PhoneCodeManagement.getPhoneCode(phone);
			System.out.println(newPhoneCode);
			String sendResult = PhoneCodeManagement.sendPhoneCode(phone, newPhoneCode);
			//打日志
			System.out.println(sendResult);
			response.result = "100";
			response.time = new Date().getTime();
			return JsonResponse.getJsonResponse(response);
		}
		catch (Exception e) {
			response.time = new Date().getTime();
			response.result = "209";
			return JsonResponse.getJsonResponse(response);
		}
	}
}
