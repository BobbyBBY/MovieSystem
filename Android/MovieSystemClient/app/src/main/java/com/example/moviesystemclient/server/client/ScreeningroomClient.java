package com.example.moviesystemclient.server.client;

import android.content.Context;
//import android.os.Looper;
import android.os.Looper;
import android.preference.ListPreference;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.moviesystemclient.bean.Screening;
import com.example.moviesystemclient.bean.ScreeningView;
import com.example.moviesystemclient.bean.Screeningroom;
import com.example.moviesystemclient.server.ErrorCode;
import com.example.moviesystemclient.server.HttpUtils;
import com.example.moviesystemclient.server.UrlManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScreeningroomClient {

    private static final String URL = "http://"+ UrlManagement.urlip+":8090/movieorder/screeningroom/";

    public static List<Screeningroom> getScreeningrooms(Context context, int screeningroomId, int screeningroomStatus){
        JSONObject message = new JSONObject();
        message.put("action", "QueryScreeningroomRequest");
        JSONObject content = new JSONObject();
        content.put("screeningroomId", screeningroomId);
        content.put("screeningroomStatus", screeningroomStatus);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"getscreenings", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：getScreeningrooms",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return null;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100) {
            return result.getJSONObject("content").getJSONArray("screeningrooms").toJavaList(Screeningroom.class);
        }
        else {
            return null;
        }
    }

    public static int updateSreeningroom(Context context, Screeningroom screeningroom){
        JSONObject message = new JSONObject();
        message.put("action", "UpdateScreeningroomRequest");
        message.put("content", JSONObject.toJSON(screeningroom));
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"updatescreeningroom", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：updateSreeningroom",Toast.LENGTH_SHORT).show();
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

    public static int deleteScreeningroom(Context context, int screeningroomId){
        JSONObject message = new JSONObject();
        message.put("action", "DeleteScreeningroomRequest");
        JSONObject content = new JSONObject();
        content.put("screeningroomId",screeningroomId);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"deletescreeningroom", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：deleteScreeningroom",Toast.LENGTH_SHORT).show();
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
