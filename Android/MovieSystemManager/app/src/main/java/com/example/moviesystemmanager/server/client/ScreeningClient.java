package com.example.moviesystemmanager.server.client;

import android.content.Context;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;
import com.example.moviesystemmanager.bean.Screening;
import com.example.moviesystemmanager.bean.ScreeningView;
import com.example.moviesystemmanager.server.ErrorCode;
import com.example.moviesystemmanager.server.HttpUtils;
import com.example.moviesystemmanager.server.UrlManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//import android.os.Looper;

public class ScreeningClient{

	private static final String URL = "http://"+ UrlManagement.urlip+":8090/movieorder/screening/";
	

	public static List<ScreeningView> getScreenings(Context context, String movieId, int screeningroomId, int screeningId, int day, int page) {
        JSONObject message = new JSONObject();
        message.put("action", "QueryScreeningRequest");
        JSONObject JSONPage = new JSONObject();
        JSONPage.put("pageno", page);
        JSONPage.put("pagesize", 10);
        message.put("page", JSONPage);
        JSONObject content = new JSONObject();
        content.put("movieId", movieId);
        content.put("screeningroomId", screeningroomId);
        content.put("screeningId", screeningId);
        String DATE_PATTERN = "yyyyMMddHHmmssSSS";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        String timestr = sdf.format(new Date());
        timestr = timestr.substring(0,8)+"0000000";
        Date screeningOnlineTime;
        try {
            screeningOnlineTime = new Date(sdf.parse(timestr).getTime()+day*86400000);
        } catch (Exception e1) {
            screeningOnlineTime = new Date();
        }
        content.put("screeningOnlineTime", screeningOnlineTime);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"getscreenings", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：getScreenings",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return null;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100) {
            return result.getJSONObject("content").getJSONArray("screenings").toJavaList(ScreeningView.class);
        }
        else {
            return null;
        }
	}

    public static int updateScreening(Context context, Screening screening){
        JSONObject message = new JSONObject();
        message.put("action", "UpdateScreeningRequest");
        message.put("content", JSONObject.toJSON(screening));
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"updatescreening", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：updateScreening",Toast.LENGTH_SHORT).show();
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
    public static int deleteScreening(Context context, int screeningId){
        JSONObject message = new JSONObject();
        message.put("action", "DeleteScreeningRequest");
        JSONObject content = new JSONObject();
        content.put("screeningId",screeningId);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"deletescreening", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：deleteScreening",Toast.LENGTH_SHORT).show();
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
