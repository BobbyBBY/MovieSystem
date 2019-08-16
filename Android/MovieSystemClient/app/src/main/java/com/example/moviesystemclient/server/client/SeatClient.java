package com.example.moviesystemclient.server.client;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.moviesystemclient.bean.ScreeningView;
import com.example.moviesystemclient.bean.Screeningroom;
import com.example.moviesystemclient.bean.Seat;
import com.example.moviesystemclient.server.ErrorCode;
import com.example.moviesystemclient.server.HttpUtils;
import com.example.moviesystemclient.server.UrlManagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeatClient {

	private static final String URL = "http://"+ UrlManagement.urlip+":8090/movieorder/seat/";

	public static List<Seat> getSeats(Context context, int screeningroomId, int screeningId, int seatStatus) {
        JSONObject message = new JSONObject();
        message.put("action", "QuerySeatRequest");
        JSONObject content = new JSONObject();
        content.put("screeningroomId", screeningroomId);
        content.put("screeningId", screeningId);
        content.put("seatStatus", seatStatus);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"getseats", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：getSeats",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            return null;
        }
        int errorCode = ErrorCode.translate(context,result.getString("result"));
        if(errorCode == 100) {
            return result.getJSONObject("content").getJSONArray("seats").toJavaList(Seat.class);
        }
        else {
            return null;
        }
	}

    public static int updateSeat(Context context, Seat seat){
        JSONObject message = new JSONObject();
        message.put("action", "UpdateSeatRequest");
        message.put("content", JSONObject.toJSON(seat));
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"updateseat", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：updateSeat",Toast.LENGTH_SHORT).show();
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

    public static int addSeats(Context context, int screeningroomId, int row, int line){
	    //row,ling小于100
        if(row>100||line>100||row<=0||line<=0){
            return 0;
        }
        List<Seat> seatList = new ArrayList<Seat>();
        String screeningroomIdCut = String.format("%04d",screeningroomId%10000);
        String rowStr = String.format("%02d",row);
        String lineStr = String.format("%02d",line);
        String numStr = screeningroomIdCut+rowStr+lineStr;
        StringBuilder name = new StringBuilder();
        StringBuilder location = new StringBuilder();
        for(int i=0;i<row;++i){
            String iStr = String.format("%02d",i);
            for(int j=0;j<line;++j){
                name.append("第");
                name.append(i+1);
                name.append("排 第");
                name.append(j+1);
                name.append("号");
                location.append(numStr);
                location.append(iStr);
                location.append(String.format("%02d",j));
                Seat temp = new Seat(screeningroomId,location.toString(),name.toString(),1);
                seatList.add(temp);
            }
        }
        JSONObject message = new JSONObject();
        message.put("action", "AddSeatRequest");
        JSONObject content = new JSONObject();
        JSONArray seatArray = JSONArray.parseArray(JSON.toJSONString(seatList));
        content.put("seats",seatArray);
        message.put("content", content);
        JSONObject result = HttpUtils.doHttpPostSlow(URL+"addseats", message);
        if (result==null) {
//            Looper.prepare();
            Toast.makeText(context, "网络错误：addSeats",Toast.LENGTH_SHORT).show();
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
