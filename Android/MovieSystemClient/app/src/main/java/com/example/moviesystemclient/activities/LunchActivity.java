package com.example.moviesystemclient.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.example.moviesystemclient.encryption.PasswordManagement;
import com.example.moviesystemclient.encryption.PhoneManagement;
import com.example.moviesystemclient.server.client.AudienceClient;
import com.example.moviesystemclient.server.client.PasswordClient;

/**
 * @Title: LunchActivity
 * @Description:
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 10:33
 * @version V1.0
 */
public class LunchActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        new Thread( new Runnable( ) {
            @Override
            public void run() {
//                Looper.prepare();
//                Looper.loop();
                //加载慢密钥
                PasswordClient.getSlow(getApplicationContext());
                //加载手机号
                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("data", Context.MODE_PRIVATE);
                PhoneManagement.setPhone(sharedPreferences.getString("phone",""));
                PasswordManagement.setSignatureKey(sharedPreferences.getInt("signatureKey",0));
                PasswordManagement.setSignature(sharedPreferences.getString("signature",""));


                //验证签名
                AudienceClient.getAudience(getApplicationContext());
                //提醒是否登陆


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        LunchActivity.this.finish();
                    }
                });
            }
        } ).start();
    }
}
