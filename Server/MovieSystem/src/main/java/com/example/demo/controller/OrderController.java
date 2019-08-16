package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.example.demo.management.OrderManagement;
import com.example.demo.management.PasswordManagement;
import com.example.demo.pojo.Ordertable;
import com.example.demo.pojo.OrderView;
import com.example.demo.pojo.Passwordtable;
import com.example.demo.pojo.Seat;
import com.example.demo.pojo.Ticket;
import com.example.demo.service.OrderService;
import com.example.demo.service.PasswordService;
import com.example.demo.service.TicketService;

/**
 * 
* @ClassName: OrderController 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:03:00 
*
 */
@RequestMapping(value = "/movieorder/order")
@RestController
public class OrderController {

	@Resource
	private PasswordService passwordService;
	@Resource
	private OrderService orderService;
	@Resource
	private TicketService ticketService; 
	
	
	/**
	 * 
	* @Title: buildOrder 
	* @Description:  	逐个生成，生成订单的时候同时生成电影票（加锁机制。先在内存中生成电影票，走强制单线程通道，抢占单线程锁，写入数据库，释放锁）
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/buildorder", method = RequestMethod.POST)
	public String buildOrder(@RequestBody JSONObject jsonParam) {
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
		response.action = "BuildOrderResponse";
		response.despassword = request.despassword;
	    String audiencePhone = request.content.getString("audiencePhone");
		long orderGeneratedtime = request.content.getLongValue("orderGeneratedtime");
		double orderTotalprice = request.content.getDoubleValue("orderTotalprice");
		int screeningId = request.content.getIntValue("screeningId");
//		JSONArray temparray = request.content.getJSONArray("seats");
//		List<Seat> seatJSONObjectList2 = JSONObject.parseArray(request.content.getJSONArray("seats").toJSONString(), Seat.class);
		List<JSONObject> seatJSONObjectList = JSONObject.parseArray(request.content.getJSONArray("seats").toJSONString(), JSONObject.class);
		
		//在内存中生成订单
		Ordertable order = new Ordertable();
		order.setAudiencePhone(audiencePhone);
		order.setOrderGeneratedtime(new Date(orderGeneratedtime));
		order.setOrderTotalprice(orderTotalprice);
		order.setOrderStatus(1);
		order.setOrderTicketcount(seatJSONObjectList.size());
		//orderId为 17位日期时间+13位随机码
		StringBuilder orderIdSb = new StringBuilder();
		String DATE_PATTERN = "yyyyMMddHHmmssSSS";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		orderIdSb.append(sdf.format(orderGeneratedtime));
		orderIdSb.append("1234567890123");
		do {
			orderIdSb.delete(orderIdSb.length()-13, orderIdSb.length());
			orderIdSb.append(String.format("%06d",(int)(Math.random()*999999)));
			orderIdSb.append(String.format("%07d",(int)(Math.random()*9999999)));
			order.setOrderId(orderIdSb.toString());
		}while(orderService.existsById(order.getOrderId()));
		//在内存中生成电影票
		List<Ticket> ticketList = new ArrayList<Ticket>();
		Iterator<JSONObject> iterator = seatJSONObjectList.iterator();
		JSONObject tempSeatJSONObject;
		while(iterator.hasNext()) {
			tempSeatJSONObject = iterator.next();
			Ticket ticket = new Ticket();
			ticket.setSeatId(tempSeatJSONObject.getInteger("seatId"));
			ticket.setScreeningId(screeningId);
			ticket.setOrderId(order.getOrderId());
			ticket.setTicketStatus(1);
			//ticketId  8位日期+4位低截断场次号+4位低截断座位号+4位随机码
			StringBuilder ticketIdSb = new StringBuilder();
			String DATE_PATTERN_SEAT = "yyyyMMdd";
			SimpleDateFormat sdf_seat = new SimpleDateFormat(DATE_PATTERN_SEAT);
			ticketIdSb.append(sdf_seat.format(orderGeneratedtime));
			String screeningIdStr =  String.format("%04d",ticket.getScreeningId());
			ticketIdSb.append(screeningIdStr.substring(screeningIdStr.length()-4, screeningIdStr.length()));
			String seatIdStr =  String.format("%04d",ticket.getSeatId());
			ticketIdSb.append(seatIdStr.substring(seatIdStr.length()-4, seatIdStr.length()));
			ticketIdSb.append(1111);
			do {
				ticketIdSb.delete(ticketIdSb.length()-4, ticketIdSb.length());
				ticketIdSb.append(String.format("%04d",(int)(Math.random()*9999)));
				ticket.setTicketId(ticketIdSb.toString());
			}while(ticketService.existsById(ticket.getTicketId()));
			ticketList.add(ticket);
		}
		//至此数据全部处理完成
		try {
			if(!OrderManagement.getInstance().takeLock()) {
				response.time = new Date().getTime();
				response.result = "601";
			    return JsonResponse.getJsonResponse(response);
			}
			else {
				boolean exist = false;
				exist = exist || orderService.existsById(order.getOrderId());
				Iterator<Ticket> iterator_ticket = ticketList.iterator();
				while(iterator_ticket.hasNext()) {
					exist = exist || ticketService.existsById(iterator_ticket.next().getTicketId());
				}
				if(exist) {
					response.time = new Date().getTime();
					response.result = "504";
				}
				else {
					orderService.buildOrder(order);
					iterator_ticket = ticketList.iterator();
					while(iterator_ticket.hasNext()) {
						ticketService.buildTicket(iterator_ticket.next());
					}
					JSONObject orderId = new JSONObject();
					orderId.put("orderId", order.getOrderId());
					response.content = orderId;
					response.time = new Date().getTime();
					response.result = "100";
				}
				OrderManagement.getInstance().releaseLock();
				return JsonResponse.getJsonResponse(response);
			}
		}
		catch (Exception e) {
			// : handle exception
			response.time = new Date().getTime();
			response.result = "400";
			OrderManagement.getInstance().releaseLock();
		    return JsonResponse.getJsonResponse(response);
		}
	}
	
	/**
	 * 
	* @Title: updateOrder 
	* @Description:  逐个修改，只能修改订单状态  不能修改已取消的订单
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateorder", method = RequestMethod.POST)
	public String updateOrder(@RequestBody JSONObject jsonParam) {
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
		response.version = request.version;
		response.action = "UpdateOrderResponse";
		response.despassword = request.despassword;
		String orderId = request.content.getString("orderId");
		int orderStatus = request.content.getIntValue("orderStatus");
		//至此数据全部处理完成
		try {
			int updateResult = orderService.updateOrder(orderId,orderStatus);
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
	* @Title: getOrders 
	* @Description:  根据id查询，根据观众手机号查，同时可以筛选状态。分页查询。Id条件优先
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getorders", method = RequestMethod.POST)
	public String getOrders(@RequestBody JSONObject jsonParam) {
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
		response.action = "QueryOrderResponse";
		response.despassword = request.despassword;
	    String orderId = request.content.getString("orderId");
	    String audiencePhone = request.content.getString("audiencePhone");
		int orderStatus = request.content.getIntValue("orderStatus");
		Pageable pageable = PageRequest.of(request.pageno, request.pagesize);
		Page<OrderView> page = null;
		List<Ordertable> orderList = new ArrayList<Ordertable>();
	    //至此数据全部处理完成
		try {
			//查询条件判断，注意优先级，顺序不能乱
			if(orderId!=null&&!orderId.equals("")) {
				orderList.add(orderService.queryById(orderId));
			}
			else if(audiencePhone!=null&&!audiencePhone.equals("")&&orderStatus>=1&&orderStatus<=5) {
				page = orderService.queryByAudiencePhone(audiencePhone,orderStatus,pageable);
			}
			else {
				//筛选条件组合不合法
				response.time = new Date().getTime();
				response.result = "301";
			    return JsonResponse.getJsonResponse(response);
			}
			// 封装返回结果
			JSONObject orders = new JSONObject();
			if(page != null) {
				response.pageno =  page.getNumber();
				response.pagesize = page.getSize();
				List<OrderView> ordersList = page.getContent();
				JSONArray ordersArray = JSONArray.parseArray(JSON.toJSONString(ordersList));
				orders.put("orders", ordersArray);
			}
			else {
				response.pageno =  0;
				response.pagesize = 10;
				JSONArray ordersArray = JSONArray.parseArray(JSON.toJSONString(orderList));
				orders.put("orders", ordersArray);
			}
			response.content = orders;
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
