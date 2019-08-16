package com.example.demo.controller;

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
import com.example.demo.pojo.Harddisk;
import com.example.demo.pojo.Passwordtable;
import com.example.demo.service.HarddiskService;
import com.example.demo.service.PasswordService;

/**
 * 
* @ClassName: HarddiskController 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:02:51 
*
 */
@RequestMapping(value = "/movieorder/harddisk")
@RestController
public class HarddiskController {

	@Resource
	private PasswordService passwordService;
	@Resource
	private HarddiskService harddiskService;
	
	/**
	 * 
	* @Title: getHarddisks 
	* @Description: 	可以根据公司名模糊查询，筛选最早解密时间晚于当前时间，筛选最早解密时间早于当前时间且到期时间晚于当前时间，筛选到期时间晚于当前时间。单条件查询，名称条件优先。分页查询，一页10个。
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getharddisks", method = RequestMethod.POST)
	public String getHarddisks(@RequestBody JSONObject jsonParam) {
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
		response.action = "QueryHarddiskResponse";
		response.despassword = request.despassword;
	    String harddiskFilmstudio = request.content.getString("harddiskFilmstudio");
	    long harddiskDecryptiontime = request.content.getLong("harddiskDecryptiontime");
	    long harddiskExpirationtime = request.content.getLong("harddiskExpirationtime");
		Pageable pageable = PageRequest.of(request.pageno, request.pagesize);
		Page<Harddisk> page;
		//至此数据全部处理完成
		try {
			//查询条件判断，注意优先级，顺序不能乱
			if(harddiskFilmstudio!=null&&!harddiskFilmstudio.equals("")) {
				//根据公司名模糊查询
				page = harddiskService.queryByFilmstudio(harddiskFilmstudio, pageable);
			}
			else if(harddiskDecryptiontime!=0&&harddiskExpirationtime==0) {
				//最早解密时间晚于当前时间
				page = harddiskService.queryDecrytion(harddiskDecryptiontime, pageable);
			}
			else if(harddiskDecryptiontime!=0&&harddiskExpirationtime!=0) {
				//最早解密时间早于当前时间且到期时间晚于当前时间
				page = harddiskService.queryValidity(harddiskDecryptiontime,harddiskExpirationtime, pageable);
			}
			else if(harddiskDecryptiontime==0&&harddiskExpirationtime!=0) {
				//期时间晚于当前时间
				page = harddiskService.queryExpiration(harddiskExpirationtime, pageable);
			}
			else {
				//筛选条件不合法
				response.time = new Date().getTime();
				response.result = "301";
			    return JsonResponse.getJsonResponse(response);
			}
			
		    // 封装返回结果
			JSONObject harddisks = new JSONObject();
			if(page != null) {
				response.pageno =  page.getNumber();
				response.pagesize = page.getSize();
				List<Harddisk> harddiskList = page.getContent();
				JSONArray harddisksArray = JSONArray.parseArray(JSON.toJSONString(harddiskList));
				harddisks.put("harddisks", harddisksArray);
			}
			response.time = new Date().getTime();
			response.content = harddisks;
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
	* @Title: updateHarddisk 
	* @Description: 逐个添加/修改，如果存在该硬盘id则为修改。全字段修改。硬盘不能被删除
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateharddisk", method = RequestMethod.POST)
	public String updateHarddisk(@RequestBody JSONObject jsonParam) {
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
		response.action = "UpdateHarddiskResponse";
		response.despassword = request.despassword;
		Harddisk harddisk = new Harddisk();
		harddisk.setHarddiskId(request.content.getString("harddiskId"));
		harddisk.setHarddiskFilmstudio(request.content.getString("harddiskFilmstudio"));
		harddisk.setHarddiskDecryptiontime(new Date(request.content.getLong("harddiskDecryptiontime")));
		harddisk.setHarddiskValidityduration(request.content.getIntValue("harddiskValidityduration"));
		harddisk.setHarddiskExpirationtime(new Date(request.content.getLong("harddiskExpirationtime")));
		harddisk.setHarddiskPassword(request.content.getString("harddiskPassword"));
		//至此数据全部处理完成
		try {
			int updateResult = harddiskService.updateHarddisk(harddisk);
			if(updateResult==1) {
				response.time = new Date().getTime();
				response.result = "100";
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
