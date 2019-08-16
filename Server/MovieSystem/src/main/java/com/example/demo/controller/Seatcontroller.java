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
import com.example.demo.pojo.Seat;
import com.example.demo.service.PasswordService;
import com.example.demo.service.SeatService;

/**
 * 
* @ClassName: Seatcontroller 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:03:10 
*
 */
@RequestMapping(value = "/movieorder/seat")
@RestController
public class Seatcontroller {

	@Resource
	private PasswordService passwordService;
	@Resource
	private SeatService seatService;
	
	/**
	 * 
	* @Title: getSeats 
	* @Description:	 根据影厅id筛选，查询该影厅所有座位，同时可以筛选座位状态。根据场次id筛选，查询该场次所有被占用的座位。不分页
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getseats", method = RequestMethod.POST)
	public String getSeats(@RequestBody JSONObject jsonParam) {
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
		response.action = "QuerySeatResponse";
		response.despassword = request.despassword;
	    int screeningroomId = request.content.getIntValue("screeningroomId");
	    int screeningId = request.content.getIntValue("screeningId");
	    int seatStatus = request.content.getIntValue("seatStatus");
	    List<Seat> seatList = new ArrayList<Seat>();
	    //至此数据全部处理完成
  		try {
  			//查询条件判断，注意优先级，顺序不能乱
  			if(screeningroomId>=0&&seatStatus>0) {
  				seatList = seatService.queryAllSeat(screeningroomId,seatStatus);
  			}
  			else if(screeningroomId>=0) {
  				seatList = seatService.queryAllSeat(screeningroomId);
  			}
  			else if(screeningId>=0) {
  				//根据场次id筛选，查询该场次所有被占用的座位。
  				seatList = seatService.queryAllSeatUsed(screeningId);
  			}
  			else {
  				//筛选条件不合法
  				response.time = new Date().getTime();
				response.result = "301";
			    return JsonResponse.getJsonResponse(response);
  			}
  			
  		    // 封装返回结果
  			JSONObject seats = new JSONObject();
  			JSONArray seatsArray = JSONArray.parseArray(JSON.toJSONString(seatList));
  			seats.put("seats", seatsArray);
  			response.content = seats;
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
	* @Title: updateSeat 
	* @Description:  逐个修改。不能修改永久关闭的座位。不能删除座位
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateseat", method = RequestMethod.POST)
	public String updateSeat(@RequestBody JSONObject jsonParam) {
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
		response.action = "UpdateSeatResponse";
		response.despassword = request.despassword;
		Seat seat = new Seat();
		seat.setSeatId(request.content.getIntValue("seatId"));
		seat.setScreeningroomId(request.content.getIntValue("screeningroomId"));
		seat.setSeatLocation(request.content.getString("seatLocation"));
		seat.setSeatName(request.content.getString("seatName"));
		seat.setSeatStatus(request.content.getIntValue("seatStatus"));
		//至此数据全部处理完成
		try {
			int updateResult = seatService.updateSeat(seat);
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
	* @Title: addSeat 
	* @Description:	批量添加。座位id自增。（以后可以传入关键参数，在此批量生成座位，减少传输流量）
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addseats", method = RequestMethod.POST)
	public String addSeats(@RequestBody JSONObject jsonParam) {
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
		response.action = "AddSeatResponse";
		response.despassword = request.despassword;
		List<Seat> seatList = JSONObject.parseArray(response.content.getJSONArray("seats").toJSONString(), Seat.class);
		//至此数据全部处理完成
		try {
			int updateResult = seatService.addSeats(seatList);
			if(updateResult==1) {
				response.time = new Date().getTime();
				response.result = "100";
			}
			else if(updateResult==2) {
				//应该永远不能进入这个分支
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
}
