package com.example.demo.controller;

import java.util.ArrayList;
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
import com.example.demo.json.JsonRequest;
import com.example.demo.json.JsonResponse;
import com.example.demo.management.PasswordManagement;
import com.example.demo.pojo.Passwordtable;
import com.example.demo.pojo.Screeningroom;
import com.example.demo.service.PasswordService;
import com.example.demo.service.ScreeningroomService;

/**
 * 
* @ClassName: SreeningRoomController 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月3日 上午9:44:03 
*
 */
@RequestMapping(value = "/movieorder/screeningroom")
@RestController
public class SreeningroomController {

	@Resource
	private PasswordService passwordService;
	@Resource
	private ScreeningroomService screeningroomService;
	
	
	/**
	 * 
	* @Title: getSreeningrooms 
	* @Description:	可以根据影厅id查询，可以筛选影厅状态，不分页
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getscreeningrooms", method = RequestMethod.POST)
	public String getSreeningRooms(@RequestBody JSONObject jsonParam) {
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
		response.action = "QueryScreeningroomResponse";
		response.despassword = request.despassword;
	    int screeningroomId = request.content.getIntValue("screeningroomId");
		int screeningroomStatus = request.content.getIntValue("screeningroomStatus");
		Screeningroom screeningroom = null;
		List<Screeningroom> screeningroomList = null;
		//至此数据全部处理完成
	try {
		//查询条件判断，注意优先级，顺序不能乱
		if(screeningroomId>0) {
			screeningroom = screeningroomService.queryById(screeningroomId);
			screeningroomList = new ArrayList<Screeningroom>();
			screeningroomList.add(screeningroom);
		}
		else if(screeningroomStatus>0) {
			screeningroomList = screeningroomService.queryByStatus(screeningroomStatus);
		}
		else {
			//筛选条件组合不合法
			response.time = new Date().getTime();
			response.result = "301";
		    return JsonResponse.getJsonResponse(response);
		}
	    // 封装返回结果
		JSONObject screeningrooms = new JSONObject();
		JSONArray screeningroomsArray = JSONArray.parseArray(JSON.toJSONString(screeningroomList));
		screeningrooms.put("screeningrooms", screeningroomsArray);
		response.content = screeningrooms;
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
	
	/**
	 * 
	* @Title: updateSreeningroom 
	* @Description:	逐个添加/修改，如果存在该影厅id则为修改。不能修改永久关闭的影厅
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatescreeningroom", method = RequestMethod.POST)
	public String updateSreeningroom(@RequestBody JSONObject jsonParam) {
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
		response.action = "UpdateScreeningroomResponse";
		response.despassword = request.despassword;
		Screeningroom screeningroom = new Screeningroom();
		screeningroom.setScreeningroomName(request.content.getString("screeningroomName"));
		screeningroom.setScreeningroomStatus(request.content.getIntValue("screeningroomStatus"));
		//至此数据全部处理完成
		try {
			int updateResult = screeningroomService.updateScreeningroom(screeningroom);
			if(updateResult==1) {
				response.time = new Date().getTime();
				response.result = "100";
			}
			else if(updateResult==2) {
				response.time = new Date().getTime();
				response.result = "501";
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
	* @Title: deleteSreeningroom 
	* @Description: 	逐个删除，如果有外键关联则不能删除，自动修改为永久关闭
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletescreeningroom", method = RequestMethod.POST)
	public String deleteSreeningroom(@RequestBody JSONObject jsonParam) {
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
		response.action = "DeleteScreeningroomResponse";
		response.despassword = request.despassword;
		int screeningroomId = request.content.getIntValue("screeningroomId");
		//至此数据全部处理完成
		try {
			int deleteResult = screeningroomService.deleteById(screeningroomId);
			if(deleteResult==1) {
				response.time = new Date().getTime();
				response.result = "100";
			}
			else if(deleteResult==2) {
				response.time = new Date().getTime();
				response.result = "502";
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
}
