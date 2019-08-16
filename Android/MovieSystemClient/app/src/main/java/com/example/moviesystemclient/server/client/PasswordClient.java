package com.example.moviesystemclient.server.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.moviesystemclient.encryption.Password;
import com.example.moviesystemclient.encryption.PasswordManagement;
import com.example.moviesystemclient.encryption.PhoneManagement;
import com.example.moviesystemclient.encryption.RSAEncrypt;
import com.example.moviesystemclient.server.ErrorCode;
import com.example.moviesystemclient.server.HttpUtils;
import com.example.moviesystemclient.server.UrlManagement;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @Title: PasswordClient.java
 * @Package: com.example.moviesystemclient.server
 * @Description: 
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 11:02
 * @version V1.0
 */
public class PasswordClient {

    private static final String URL = "http://"+ UrlManagement.urlip+":8090/movieorder/password/";

    /**
     * 这个接口自己处理加密解密
     * @return
     */
    public static int getSlow(Context context){
        String[] newPassword = new String[2];
        try {
            newPassword = RSAEncrypt.genKeyPair(512);
        } catch (NoSuchAlgorithmException e) {
            return 0;
        }
        JSONObject content = new JSONObject();
        content.put("time", new Date().getTime());
        JSONObject message = new JSONObject();
        message.put("action", "SlowPasswordRequest");
        message.put("rsapassword", newPassword[0]);
        message.put("time", new Date().getTime());
        message.put("version", "1.0");
        message.put("content", content);
        String resultStr = HttpUtils.doHttpPost(URL+"getslow",message);
        if(resultStr==null||resultStr.equals("")){
//            Looper.prepare();
//            Toast.makeText(context, "网络错误：getSlow",Toast.LENGTH_SHORT).show();
//            Looper.loop();
            return 0;
        }
        JSONObject result = JSONObject.parseObject(resultStr);
        JSONObject resultContent = result.getJSONObject("content");
        String RSAPublic = resultContent.getString("RSAPublic");
        int RSAKey = resultContent.getIntValue("RSAKey");
        String DESPassword = "";
        try {
            DESPassword = RSAEncrypt.decrypt(resultContent.getString("DESPassword"),newPassword[1]);
        } catch (Exception e) {
            return 0;
        }
        PasswordManagement.setDES(DESPassword);
        PasswordManagement.setRSAKey(RSAKey);
        PasswordManagement.setRSAPublic(RSAPublic);
        return 1;
    }

    public static Password getQuick(Context context){
        JSONObject message = new JSONObject();
        message.put("action", "QuickPasswordRequest");
        JSONObject content = new JSONObject();
        content.put("time", new Date().getTime());
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"getquick", message);
        if (result==null) {
//            Looper.prepare();
//            Toast.makeText(context, "网络错误：getQuick",Toast.LENGTH_SHORT).show();
//            Looper.loop();
            return null;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100){
            JSONObject resultContent = result.getJSONObject("content");
            return new Password(resultContent.getIntValue("RSAKey"), resultContent.getString("RSAPublic"), "", resultContent.getString("DESPassword"));
        }
        else{
//            Looper.prepare();
//            Toast.makeText(context, "请重试",Toast.LENGTH_SHORT).show();
//            Looper.loop();
            return null;
        }
    }

    public static int register(Context context, String phoneCode, String phone){
        Password password = PasswordClient.getQuick(context);
        if(password==null){
            return 0;
        }
        JSONObject message = new JSONObject();
        message.put("action", "RegisterRequest");
        JSONObject content = new JSONObject();
        content.put("phone", phone);
        content.put("phoneCode", phoneCode);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostQuick(URL+"register", message, password);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：register",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return 0;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100){
            JSONObject resultContent = result.getJSONObject("content");
            PasswordManagement.setSignature(resultContent.getString("signature"));
            PasswordManagement.setSignatureKey((resultContent.getIntValue("signatureKey")));
            PhoneManagement.setPhone(phone);
            SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("phone", phone);
            editor.putString("signature", resultContent.getString("signature"));
            editor.putInt("signatureKey", resultContent.getIntValue("signatureKey"));
            editor.commit();
            return 1;
        }
        else{
//            Looper.prepare();
//            Toast.makeText(context, "请重试",Toast.LENGTH_SHORT).show();
//            Looper.loop();
            return 0;
        }
    }

    public static int getPhoneCode(Context context, String phone){
        JSONObject message = new JSONObject();
        message.put("action", "PhoneCodeRequest");
        JSONObject content = new JSONObject();
        content.put("phone", phone);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"getphonecode", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：getPhoneCode",Toast.LENGTH_SHORT).show();
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
