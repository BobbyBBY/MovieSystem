package com.example.moviesystemmanager.encryption;


import com.alibaba.fastjson.JSONObject;

/**
 * @Title: EncryptionJson.java
 * @Package: com.example.moviesystemclient.encryption
 * @Description:
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 11:01
 * @version V1.0
 */
public class EncryptionJson {

    public static JSONObject slow(JSONObject response){
        response.put("content",DesUtil.DES(response.getString("content"),PasswordManagement.getDES()));
        return response;
    }
    public static JSONObject quick(JSONObject response, Password password){
        response.put("content",DesUtil.DES(response.getString("content"),password.getDESPassword()));
        return response;
    }
    public static JSONObject quickAndSignature(JSONObject response, Password password){
        response.put("content",DesUtil.DES(response.getString("content"),password.getDESPassword()));
        return response;
    }
}
