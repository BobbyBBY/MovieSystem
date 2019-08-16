package com.example.moviesystemclient.server.client;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.moviesystemclient.bean.Harddisk;
import com.example.moviesystemclient.server.ErrorCode;
import com.example.moviesystemclient.server.HttpUtils;
import com.example.moviesystemclient.server.UrlManagement;

import java.util.Date;
import java.util.List;

/**
 * @Title: HarddiskClient.java
 * @Package: com.example.moviesystemclient.server
 * @Description: 
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 11:02
 * @version V1.0
 */
public class HarddiskClient {
    private static final String URL = "http://"+ UrlManagement.urlip+":8090/movieorder/harddisk/";

    public static List<Harddisk> getHarddisks(Context context, int page, String name, Date decryptiontime, Date expirationtime){
        JSONObject message = new JSONObject();
        message.put("action", "QueryHarddiskRequest");
        JSONObject JSONPage = new JSONObject();
        JSONPage.put("pageno", page);
        JSONPage.put("pagesize", 5);
        message.put("page", JSONPage);
        JSONObject content = new JSONObject();
        content.put("harddiskFilmstudio", name);
        content.put("harddiskDecryptiontime", decryptiontime.getTime());
        content.put("harddiskExpirationtime", expirationtime.getTime());
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"getharddisks", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：getHarddisks",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return null;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100){
            return result.getJSONObject("content").getJSONArray("harddisks").toJavaList(Harddisk.class);
        }
        else{
//            Looper.prepare();
            Toast.makeText(context, "请重试",Toast.LENGTH_SHORT).show();
//            Looper.loop();
            return null;
        }
    }
    public static int updateHarddisk(Context context, Harddisk harddisk){
        JSONObject message = new JSONObject();
        message.put("action", "UpdateHarddiskRequest");
//        JSONObject content = new JSONObject();
//        content.put("harddisk", JSONObject.toJSON(harddisk)); //这里有个大坑，之前server端没用tojson，jsonprase，导致没有harddisk对象，只有其成员变量
        message.put("content", JSONObject.toJSON(harddisk));
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"updateharddisk", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：updateHarddisk",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return 0;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100){
            return 1;
        }
        else{
//            Looper.prepare();
            Toast.makeText(context, "请重试",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return 0;
        }
    }

}
