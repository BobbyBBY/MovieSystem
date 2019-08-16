package com.example.moviesystemclient.server.client;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.moviesystemclient.bean.Movie;
import com.example.moviesystemclient.bean.Ticket;
import com.example.moviesystemclient.bean.TicketView;
import com.example.moviesystemclient.encryption.Password;
import com.example.moviesystemclient.server.ErrorCode;
import com.example.moviesystemclient.server.HttpUtils;
import com.example.moviesystemclient.server.UrlManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TicketClient{

	private static final String URL = "http://"+ UrlManagement.urlip+":8090/movieorder/ticket/";
	

	public static List<TicketView> getTickets(Context context, String orderId) {
        Password password = PasswordClient.getQuick(context);
        if(password==null){
            return null;
        }
        JSONObject message = new JSONObject();
        message.put("action", "QueryTicketRequest");
        JSONObject content = new JSONObject();
        content.put("orderId", orderId);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostQuickSignature(URL+"gettickets", message, password);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：getTickets",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return null;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100) {
            return result.getJSONObject("content").getJSONArray("tickets").toJavaList(TicketView.class);
        }
        else {
            return null;
        }
	}
	

	public static int updateTicket(Context context, Ticket ticket) {
        Password password = PasswordClient.getQuick(context);
        if(password==null){
            return 0;
        }
        JSONObject message = new JSONObject();
        message.put("action", "UpdateTicketRequest");
        message.put("content", JSONObject.toJSON(ticket));
        JSONObject result = HttpUtils.doHttpPostQuickSignature(URL+"updateticket", message, password);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：updateTicket",Toast.LENGTH_SHORT).show();
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
