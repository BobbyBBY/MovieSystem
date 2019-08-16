package com.example.moviesystemclient.server.client;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.moviesystemclient.bean.Movie;
import com.example.moviesystemclient.server.ErrorCode;
import com.example.moviesystemclient.server.HttpUtils;
import com.example.moviesystemclient.server.UrlManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @Title: MovieClient.java
 * @Package: com.example.moviesystemclient.server
 * @Description: 
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 11:02
 * @version V1.0
 */
public class MovieClient {

	private static final String URL = "http://"+ UrlManagement.urlip+":8090/movieorder/movie/";
	

	public static List<Movie> getMovies(Context context, String movieName, int movieStatus, int page) {
        JSONObject message = new JSONObject();
        message.put("action", "QueryMovieRequest");
        JSONObject JSONPage = new JSONObject();
        JSONPage.put("pageno", page);
        JSONPage.put("pagesize", 5);
        message.put("page", JSONPage);
        JSONObject content = new JSONObject();
        content.put("movieName", movieName);
        content.put("movieStatus", movieStatus);
        content.put("movieOnlineTime", new Date().getTime());
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"getmovies", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：getMovies",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return null;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100) {
        	return result.getJSONObject("content").getJSONArray("movies").toJavaList(Movie.class);
        }
        else {
        	return null;
        }
	}

    public static int updateMovie(Context context, Movie movie){
        JSONObject message = new JSONObject();
        message.put("action", "UpdateMovieRequest");
        message.put("content", JSONObject.toJSON(movie));
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"updatemovie", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：updateMovie",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return 0;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public static int deleteMovie(Context context, String movieId){
        JSONObject message = new JSONObject();
        message.put("action", "DeleteMovieRequest");
        JSONObject content = new JSONObject();
        content.put("movieId",movieId);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"deletemovie", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：deleteMovie",Toast.LENGTH_SHORT).show();
//            Looper.loop();
            return 0;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100) {
            return 1;
        }
        else {
            return 0;
        }
    }
	
}
