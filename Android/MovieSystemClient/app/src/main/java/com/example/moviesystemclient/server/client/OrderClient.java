package com.example.moviesystemclient.server.client;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.moviesystemclient.bean.OrderView;
import com.example.moviesystemclient.bean.Ordertable;
import com.example.moviesystemclient.bean.Seat;
import com.example.moviesystemclient.bean.TicketView;
import com.example.moviesystemclient.encryption.Password;
import com.example.moviesystemclient.server.ErrorCode;
import com.example.moviesystemclient.server.HttpUtils;
import com.example.moviesystemclient.server.UrlManagement;

import java.util.Date;
import java.util.List;

/**
 * @Title: OrderClient.java
 * @Package: com.example.moviesystemclient.server
 * @Description:
 * @author xuanpengyu@foxmail.com
 * @date 2019/7/7 11:01
 * @version V1.0
 */
public class OrderClient  {

	private static final String URL = "http://"+ UrlManagement.urlip+":8090/movieorder/order/";



	public static String buildOrder(Context context, String audiencePhone, long orderGeneratedtime, double orderTotalprice, List<Seat> seats, int screeningId) {
        Password password = PasswordClient.getQuick(context);
        if(password==null){
            return null;
        }
        JSONObject message = new JSONObject();
        message.put("action", "BuildOrderRequest");
        JSONObject content = new JSONObject();
        content.put("audiencePhone", audiencePhone);
        content.put("orderGeneratedtime", orderGeneratedtime);
        content.put("orderTotalprice", orderTotalprice);
        content.put("screeningId", screeningId);
        content.put("seats", JSONArray.parseArray(JSON.toJSONString(seats)));
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostQuickSignature(URL+"buildorder", message, password);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：buildOrder",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return null;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100) {
            return result.getJSONObject("content").getString("orderId");
        }
        else {
            return null;
        }
	}

	public static int updateOrder(Context context, Ordertable ordertable) {
        Password password = PasswordClient.getQuick(context);
        if(password==null){
            return 0;
        }
        JSONObject message = new JSONObject();
        message.put("action", "UpdateOrderRequest");
        message.put("content", JSONObject.toJSON(ordertable));
        JSONObject result = HttpUtils.doHttpPostQuickSignature(URL+"updateorder", message, password);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：updateOrder",Toast.LENGTH_SHORT).show();
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
	

	public static List<OrderView> getOrders(Context context, String orderId, String audiencePhone, int orderStatus, int page) {
        Password password = PasswordClient.getQuick(context);
        if(password==null){
            return null;
        }
        JSONObject message = new JSONObject();
        message.put("action", "QueryOrderRequest");
        JSONObject JSONPage = new JSONObject();
        JSONPage.put("pageno", page);
        JSONPage.put("pagesize", 5);
        message.put("page", JSONPage);
        JSONObject content = new JSONObject();
        content.put("orderId", orderId);
        content.put("audiencePhone", audiencePhone);
        content.put("orderStatus", orderStatus);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostQuickSignature(URL+"getorders", message, password);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：getOrders",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return null;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100) {
            return result.getJSONObject("content").getJSONArray("orders").toJavaList(OrderView.class);
        }
        else {
            return null;
        }
	}
	
	
}
