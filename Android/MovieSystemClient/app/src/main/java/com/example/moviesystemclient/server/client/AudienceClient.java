package com.example.moviesystemclient.server.client;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.moviesystemclient.bean.Movie;
import com.example.moviesystemclient.encryption.Password;
import com.example.moviesystemclient.encryption.PasswordManagement;
import com.example.moviesystemclient.encryption.PhoneManagement;
import com.example.moviesystemclient.server.ErrorCode;
import com.example.moviesystemclient.server.HttpUtils;
import com.example.moviesystemclient.server.UrlManagement;

import java.util.Date;

/**
 * @Title: AudienceClient.java
 * @Package: com.example.moviesystemclient.server
 * @Description: 
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 11:02
 * @version V1.0
 */
public class AudienceClient {

	private static final String URL = "http://"+ UrlManagement.urlip+":8090/movieorder/audience/";

    /**
     * 目前仅用来判断慢密钥是否过期，判断签名是否过期
     * @param phone
     * @return
     */
	public static String getAudience(Context context){
        Password password = PasswordClient.getQuick(context);
	    if(password==null){
	        return null;
        }
        JSONObject message = new JSONObject();
        message.put("action", "QueryAudienceRequest");
        JSONObject content = new JSONObject();
        content.put("audiencePhone", PhoneManagement.getPhone());
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostQuickSignature(URL+"getaudience", message, password);
        if (result==null) {
//            Looper.prepare();
//            Toast.makeText(context, "网络错误：getAudience",Toast.LENGTH_SHORT).show();
//            Looper.loop();
            return null;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100){
            return result.getJSONObject("content").getString("audiencePhone");
        }
        else{
//            Looper.prepare();
//            Toast.makeText(context, "请重试",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return null;
        }
    }

}
