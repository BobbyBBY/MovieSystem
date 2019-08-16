package com.example.moviesystemclient.encryption;

import com.alibaba.fastjson.JSONObject;

/**
 * @Title: DecryptionJson.java
 * @Package: com.example.moviesystemclient.encryption
 * @Description:
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 11:00
 * @version V1.0
 */
public class DecryptionJson {
    public static JSONObject slow(JSONObject response){
        response.put("content",DesUtil.UNDES(response.getString("content"),PasswordManagement.getDES(),"UTF-8"));
        return response;
    }
    public static JSONObject quick(JSONObject response, Password password){
        response.put("content",DesUtil.UNDES(response.getString("content"),password.getDESPassword(),"UTF-8"));
        return response;
    }
    public static JSONObject quickAndSignature(JSONObject response, Password password){
        response.put("content",DesUtil.UNDES(response.getString("content"),password.getDESPassword(),"UTF-8"));
        return response;
    }

}
