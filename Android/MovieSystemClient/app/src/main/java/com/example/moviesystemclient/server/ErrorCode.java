package com.example.moviesystemclient.server;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.example.moviesystemclient.encryption.PasswordManagement;
import com.example.moviesystemclient.server.client.PasswordClient;

public class ErrorCode {

    public static int translate(final Context context, String errorCode){
        int codeInt = 800;
        try{
            codeInt = Integer.valueOf(errorCode);
        }
        catch(Exception e){
            return 800;
        }
        String text = "";
        switch(codeInt){
            case 100:{
//                text = "成功";
                break;
            }
            case 200:{
                text = "密钥错误未知错误代码:";
                break;
            }
            case 201:{
                text = "密钥过期:";
                break;
            }
            case 202:{
                text = "解密失败:";
                break;
            }
            case 203:{
                text = "解密失败：";
                break;
            }
            case 204:{
                text = "des密钥错误：";
                break;
            }
            case 205:{
                text = "密钥强度低：";
                break;
            }
            case 206:{
                text = "签名密钥错误：";
                break;
            }
            case 207:{
                text = "签名过期：";
                break;
            }
            case 208:{
                text = "获取密钥失败：";
                break;
            }
            case 209:{
                text = "手机验证码错误：";
                break;
            }
            case 210:{
                text = "获取签名失败：";
                break;
            }
            case 211:{
                text = "密钥不存在：";
                break;
            }
            case 300:{
                text = "条件非法未知错误代码：";
                break;
            }
            case 301:{
                text = "筛选条件组合非法：";
                break;
            }
            case 400:{
                text = "contoller未知错误：";
                break;
            }
            case 401:{
                text = "service未知错误：";
                break;
            }
            case 500:{
                text = "逻辑错误未知错误代码：";
                break;
            }
            case 501:{
                text = "修改了永久关闭记录：";
                break;
            }
            case 502:{
                text = "删除了不存在记录：";
                break;
            }
            case 503:{
                text = "修改了不存在的记录：";
                break;
            }
            case 504:{
                text = "插入了已存在主键的记录：";
                break;
            }
            case 600:{
                text = "抢占锁错误未知错误代码：";
                break;
            }
            case 601:{
                text = "抢占超时：";
                break;
            }
            case 602:{
                text = "座位锁定失败：";
                break;
            }
            case 700:{
                text = "接口自定义错误未知错误代码：";
                break;
            }
            default:{
                text = "未知错误代码：";
                break;
            }

        }
//        Looper.prepare();
//        Toast.makeText(context, text+codeInt,Toast.LENGTH_SHORT).show();
//        Looper.loop();

        if(codeInt>=201&&codeInt<=205||codeInt==211||codeInt==208){
            new Thread(new Runnable(){
                @Override
                public void run() {
                    if(getSlowBooslena()){
                        return;
                    }
                    else{
                        setSlowBooslena(true);
                        PasswordClient.getSlow(context);
                        setSlowBooslena(false);
                    }
                }
            }).start();

            //清空密钥，并提示请重试
            PasswordManagement.clearPassword();
//            Looper.prepare();
//            Toast.makeText(context, "请重试",Toast.LENGTH_SHORT).show();
//            Looper.loop();

        }
        else if(codeInt==206||codeInt==207||codeInt==210){
            //清空密钥、签名、账号，并提示请重新登陆
            PasswordManagement.clearPasswordAndSignature();
//            Looper.prepare();
//            Toast.makeText(context, "请重新登陆",Toast.LENGTH_SHORT).show();
//            Looper.loop();

        }
        return codeInt;
    }




    private static boolean slowBooslena;
    private static synchronized boolean getSlowBooslena(){
        return slowBooslena;
    }
    private static synchronized void setSlowBooslena(boolean temp){
        slowBooslena = temp;
    }
}
