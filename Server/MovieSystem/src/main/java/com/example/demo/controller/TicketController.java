package com.example.demo.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.encryption.Signature;
import com.example.demo.json.JsonRequest;
import com.example.demo.json.JsonResponse;
import com.example.demo.management.PasswordManagement;
import com.example.demo.pojo.Passwordtable;
import com.example.demo.pojo.TicketView;
import com.example.demo.service.PasswordService;
import com.example.demo.service.TicketService;


/**
 * 
* @ClassName: TicketController 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:03:14 
*
 */
@RequestMapping(value = "/movieorder/ticket")
@RestController
public class TicketController {

	@Resource
	private PasswordService passwordService;
	@Resource
	private TicketService ticketService; 
	
	/**
	 * 
	* @Title: updateTicket 
	* @Description:  逐个修改，只能修改未取票的电影票,只能修改电影票状态
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateticket", method = RequestMethod.POST)
	public String updateTicket(@RequestBody JSONObject jsonParam) {
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
		response.action = "UpdateTicketResponse";
		response.despassword = request.despassword;
		String ticketId = request.content.getString("ticketId");
		int ticketStatus = request.content.getIntValue("ticketStatus");
		//至此数据全部处理完成
		try {
			int updateResult = ticketService.updateTicket(ticketId,ticketStatus);
			if(updateResult==1) {
				response.time = new Date().getTime();
				response.result = "100";
			}
			else if(updateResult==2) {
				response.time = new Date().getTime();
				response.result = "501";
			}
			else if(updateResult==3) {
				response.time = new Date().getTime();
				response.result = "503";
			}
			else {
				response.time = new Date().getTime();
				response.result = "401";
			}
			return JsonResponse.getJsonResponse(response);
		}
		catch (Exception e) {
			// : handle exception
			response.time = new Date().getTime();
			response.result = "400";
			    return JsonResponse.getJsonResponse(response);
		}
	}
	
	/**
	 * 
	* @Title: getTickets 
	* @Description:    根据订单id查询
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/gettickets", method = RequestMethod.POST)
	public String getTickets(@RequestBody JSONObject jsonParam) {
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
		response.action = "QueryTicketResponse";
		response.despassword = request.despassword;
	    String orderId = request.content.getString("orderId");
	    List<TicketView> ticketviewList;
	    //至此数据全部处理完成
	    try {
	    	ticketviewList = ticketService.queryTicketsByOrderId(orderId);
			JSONArray ticketviewArray = JSONArray.parseArray(JSON.toJSONString(ticketviewList));
			JSONObject tickets = new JSONObject();
			tickets.put("tickets", ticketviewArray);
			response.content = tickets;
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
