package com.example.demo.controller;



import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.example.demo.pojo.Movie;
import com.example.demo.pojo.Passwordtable;
import com.example.demo.service.MovieService;
import com.example.demo.service.PasswordService;


/**
 * 
* @ClassName: MovieController 
* @Description: 
* @author xuanpengyu@faxmail.com
* @date 2019年7月1日 下午7:02:56 
*
 */
@RequestMapping(value = "/movieorder/movie")
@RestController
public class MovieController {

	@Resource
	private PasswordService passwordService;
	@Resource
	private MovieService moviceService;
	
	
	/**
	 * 
	* @Title: getMovies 
	* @Description:  可以根据电影名称模糊查询，可以筛选时间（按天取整），同时筛选电影状态，分页查询，一页5个。名称条件优先 
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getmovies", method = RequestMethod.POST)
	public String getMovies(@RequestBody JSONObject jsonParam) {
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
			response.action = "QueryMovieResponse";
			response.despassword = request.despassword;
		    String movieName = request.content.getString("movieName");
			int movieStatus = request.content.getIntValue("movieStatus");
//			对long时间进行运算时，要转换为GMT+00：00
//			Timestamp timestamp = new Timestamp(((request.content.getLongValue("movieOnlineTime")+28800000)/86400000)*86400000-28800000);//对天取整
			String DATE_PATTERN = "yyyyMMddHHmmssSSS";
			SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_PATTERN);
			String timestr = sdf.format(new Date(request.content.getLongValue("movieOnlineTime")));
			timestr = timestr.substring(0,8)+"0000000";
			Timestamp timestamp;
			try {
				timestamp = new Timestamp(sdf.parse(timestr).getTime());
			} catch (ParseException e1) {
				//  自动生成的 catch 块
				timestamp = null;
			}
			Pageable pageable = PageRequest.of(request.pageno, request.pagesize);
			Page<Movie> page;
			//至此数据全部处理完成
		try {
			//查询条件判断，注意优先级，顺序不能乱
			if(movieStatus==5) {
				//给自己挖的坑，之前没考虑到即将上映。用状态位=5表示搜索即将上映的电影
				page = moviceService.queryMovieOnline(timestamp, pageable);
			}
			else if(movieName!=null&&!movieName.equals("")) {
				page = moviceService.queryByName(movieName,movieStatus, pageable);
			}
			else if(timestamp!=null) {
				page = moviceService.queryMovieByTime(timestamp,movieStatus,pageable);
			}
			else {
				//筛选条件组合不合法
				response.time = new Date().getTime();
				response.result = "301";
			    return JsonResponse.getJsonResponse(response);
			}
			
		    // 封装返回结果
			JSONObject movies = new JSONObject();
			if(page != null) {
				response.pageno =  page.getNumber();
				response.pagesize = page.getSize();
				List<Movie> movieList = page.getContent();
				JSONArray moviesArray = JSONArray.parseArray(JSON.toJSONString(movieList));
				movies.put("movies", moviesArray);
			}
			response.content = movies;
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
	* @Title: updateMovies 
	* @Description:  	逐个添加/修改，如果存在该电影id则为修改，硬盘编号为外键。不能修改永久关闭的电影。全字段修改
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatemovie", method = RequestMethod.POST)
	public String updateMovie(@RequestBody JSONObject jsonParam) {
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
		response.action = "UpdateMovieResponse";
		response.despassword = request.despassword;
		Movie movie = new Movie();
		movie.setMovieId(request.content.getString("movieId"));
		movie.setHarddiskId(request.content.getString("harddiskId"));
		movie.setMovieName(request.content.getString("movieName"));
		movie.setMovieDoubanid(request.content.getString("movieDoubanid"));
		movie.setMovieOnlinetime(new Date(request.content.getLong("movieOnlinetime")));
		movie.setMovieOfflinetime(new Date(request.content.getLong("movieOfflinetime")));
		movie.setMovieDuration(request.content.getIntValue("movieDuration"));
		movie.setMovieStatus(request.content.getIntValue("movieStatus"));
		movie.setMovieIntroduction(request.content.getString("movieIntroduction"));
		//至此数据全部处理完成
		try {
			int updateResult = moviceService.updateMovie(movie);
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
	 * 
	 * 
	* @Title: deleteMovie 
	* @Description:  逐个删除，如果已经安排场次了，那么不能删除，将自动标记为永久关闭
	* @param jsonParam
	* @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletemovie", method = RequestMethod.POST)
	public String deleteMovie(@RequestBody JSONObject jsonParam) {
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
		response.action = "DeleteMovieResponse";
		response.despassword = request.despassword;
		String movieId = request.content.getString("movieId");
		//至此数据全部处理完成
		try {
			int deleteResult = moviceService.deleteById(movieId);
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
