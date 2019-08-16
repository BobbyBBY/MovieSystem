package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.example.demo.json.JsonRequest;
import com.example.demo.json.JsonResponse;
import com.example.demo.management.PasswordManagement;
import com.example.demo.pojo.Passwordtable;
import com.example.demo.pojo.Screening;
import com.example.demo.pojo.ScreeningView;
import com.example.demo.service.MovieService;
import com.example.demo.service.PasswordService;
import com.example.demo.service.ScreeningService;
import com.example.demo.service.ScreeningroomService;

/**
 * 
* @ClassName: ScreeningController 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:03:05 
*
 */
@RequestMapping(value = "/movieorder/screening")
@RestController
public class ScreeningController {

	@Resource
	private PasswordService passwordService;
	@Resource
	private ScreeningService screeningService;
	
	@Resource
	private ScreeningroomService screeningroomService;
	@Resource
	private MovieService moiveService;
	
	/**
	 * 
	* @Title: getScreenings 
	* @Description: 	筛选开始时间晚于当前时间的场次且根据电影id查询所有场次，筛选开始时间晚于当前时间的场次且根据影厅id查询所有场次，根据场次id查询场次。条件优先级如上。分页查询，一页10个
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getscreenings", method = RequestMethod.POST)
	public String getScreenings(@RequestBody JSONObject jsonParam) {
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
		response.action = "QueryScreeningResponse";
		response.despassword = request.despassword;
	    String movieId = request.content.getString("movieId");
		int screeningroomId = request.content.getIntValue("screeningroomId");
	    int screeningId = request.content.getIntValue("screeningId");
	    Pageable pageable = PageRequest.of(request.pageno, request.pagesize);
	    Screening screening = null;
	    Page<ScreeningView> pageScreeningView = null;
		//至此数据全部处理完成
		try {
			//查询条件判断，注意优先级，顺序不能乱
			String DATE_PATTERN = "yyyyMMddHHmmssSSS";
			SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_PATTERN);
			String timestr = sdf.format(new Date(request.content.getLongValue("screeningOnlineTime")));
			timestr = timestr.substring(0,8)+"0000000";
			long timelate;
			try {
				if(screeningroomId==-2) {
					timelate = sdf.parse(timestr).getTime()+864000000;
				}
				else {
					timelate = sdf.parse(timestr).getTime()+86400000;
				}
				
			} catch (ParseException e1) {
				//  自动生成的 catch 块
				timelate = new Date().getTime();
			}
			if(movieId!=null&&!movieId.equals("")) {
				//开始时间晚于当前时间的场次且根据 电影id 查询所有场次
				pageScreeningView = screeningService.queryByMovieId(request.content.getLongValue("screeningOnlineTime"),timelate, movieId,pageable);
			}
			else if(screeningroomId>=0) {
				//开始时间晚于当前时间的场次且根据 影厅id 查询所有场次
				pageScreeningView = screeningService.queryByScreeningroomId(request.content.getLongValue("screeningOnlineTime"),timelate, screeningroomId,pageable);
			}
			else if(screeningId>=0) {
				//场次id查询场次
				screening = screeningService.queryById(screeningId);
			}
			else {
				//筛选条件不合法
				response.time = new Date().getTime();
				response.result = "301";
			    return JsonResponse.getJsonResponse(response);
			}
			
		    // 封装返回结果
			JSONObject screenings = new JSONObject();
			if(pageScreeningView != null) {
				response.pageno =  pageScreeningView.getNumber();
				response.pagesize = pageScreeningView.getSize();
				List<ScreeningView> screeningsList = pageScreeningView.getContent();
				JSONArray screeningsArray = JSONArray.parseArray(JSON.toJSONString(screeningsList));
				screenings.put("screenings", screeningsArray);
			}
			else {
				response.pageno =  0;
				response.pagesize = 10;
				List<Screening> screeningsList = new ArrayList<Screening>();
				screeningsList.add(screening);
				JSONArray screeningsArray = JSONArray.parseArray(JSON.toJSONString(screeningsList));
				screenings.put("screenings", screeningsArray);
			}
			
			response.content = screenings;
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
	* @Title: updateScreening 
	* @Description: 逐个添加/修改，如果存在该场次id则为修改，字段为空则不修改，不能修改永久关闭的场次
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatescreening", method = RequestMethod.POST)
	public String updateScreening(@RequestBody JSONObject jsonParam) {
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
		response.action = "UpdateScreeningResponse";
		response.despassword = request.despassword;
		Screening screening = new Screening();
		screening.setMovieId(request.content.getString("movieId"));;
		screening.setScreeningPrice(request.content.getDoubleValue("screeningPrice"));
		screening.setScreeningroomId(request.content.getIntValue("screeningroomId"));
		screening.setScreeningSpecialeffect(request.content.getIntValue("screeningSpecialeffect"));
		screening.setScreeningStarttime(new Date(request.content.getLongValue("screeningStarttime")));
		//至此数据全部处理完成
		try {
			int updateResult = screeningService.updateScreening(screening);
			if(updateResult==1) {
				response.time = new Date().getTime();
				response.result = "100";
			}
//			else if(updateResult==2) {
//				response.time = new Date().getTime();
//				response.result = "501";
//			}
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
	* @Title: deleteScreening 
	* @Description: 逐个删除，只能删除没有关联的场次，若有关联，自动修改状态为永久关闭。
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletescreening", method = RequestMethod.POST)
	public String deleteScreening(@RequestBody JSONObject jsonParam) {
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
		response.action = "DeleteScreeningResponse";
		response.despassword = request.despassword;
		int screeningId = request.content.getIntValue("screeningId");
		//至此数据全部处理完成
		try {
			int deleteResult = screeningService.deleteById(screeningId);
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
