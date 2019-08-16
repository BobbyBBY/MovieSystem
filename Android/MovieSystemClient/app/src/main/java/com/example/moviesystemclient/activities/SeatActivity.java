package com.example.moviesystemclient.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.moviesystemclient.R;
import com.example.moviesystemclient.Util.SeatTable;
import com.example.moviesystemclient.bean.ScreeningView;
import com.example.moviesystemclient.bean.Seat;
import com.example.moviesystemclient.encryption.PhoneManagement;
import com.example.moviesystemclient.server.client.AudienceClient;
import com.example.moviesystemclient.server.client.OrderClient;
import com.example.moviesystemclient.server.client.SeatClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SeatActivity extends AppCompatActivity {

    private Button backIBtn;//返回按钮
    private Button save;//返回按钮
    private SeatTable seatTableView;
    private TextView topTitle;
    private SharedPreferences sharedPreferences;
    private int screeningId;
    private int screeningroomId;
    private String screeningroomName;
    private double price;
    private String movieName;
    private String phoneNumber;
    private List<Seat> allSeats;
    private List<Seat> usedSeats;

    private int row;
    private int column;
    private HashMap<Integer,Integer> allValidSeat;//行号+列号，id
    private HashMap<Integer,Integer> allSoldSeat;
    private HashMap<Integer,Integer> checkedSeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seatactivity);
        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);

        screeningId=sharedPreferences.getInt("screeningId",-1);
        screeningroomId=sharedPreferences.getInt("screeningroomId",-1);
        screeningroomName=sharedPreferences.getString("screeningroomName","");
        price=sharedPreferences.getFloat("price",(float)0.0);
        movieName=sharedPreferences.getString("movieName","");
        intiView();
        new Thread(new Runnable(){
            @Override
            public void run() {
                if(AudienceClient.getAudience(getApplicationContext())==null){
                    finish();
                }
                Message message = Message.obtain();
                message.what=0;
                allSeats= SeatClient.getSeats(getApplicationContext(),screeningroomId,-1,1);
                usedSeats= SeatClient.getSeats(getApplicationContext(),-1,screeningId,1);
                handler.sendMessage(message);
            }
        }).start();
    }

    public void intiView() {
        backIBtn = findViewById(R.id.backBtn);
        save = findViewById(R.id.save);
        topTitle = findViewById(R.id.toptitle);
        topTitle.setText("选择座位： "+movieName);
        save.setOnClickListener(new ButtonListener());
        backIBtn.setOnClickListener(new ButtonListener());
        seatTableView = findViewById(R.id.mSearchView);

    }

    public HashMap<Integer,Integer> getAllValidSeat(List<Seat> allSeats) {
        row = Integer.valueOf(allSeats.get(0).getSeatLocation().substring(4,6));
        column = Integer.valueOf(allSeats.get(0).getSeatLocation().substring(6,8));
        allValidSeat = new HashMap<Integer,Integer>();
        Iterator<Seat> iterator = allSeats.iterator();
        while (iterator.hasNext()){
            Seat temp = iterator.next();
            if(temp.getSeatStatus()==1){
                allValidSeat.put(Integer.valueOf(temp.getSeatLocation().substring(8,12)),temp.getSeatId());
            }
        }
        return allValidSeat;
    }

    public HashMap<Integer,Integer> getAllSoldSeat(List<Seat> usedSeats) {
        allSoldSeat =  new HashMap<Integer,Integer>();
        Iterator<Seat> iterator = usedSeats.iterator();
        while (iterator.hasNext()){
            Seat temp = iterator.next();
            if(temp.getSeatStatus()==1){
                allSoldSeat.put(Integer.valueOf(temp.getSeatLocation().substring(8,12)),temp.getSeatId());
            }
        }
        return allSoldSeat;
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:{
                    if(allSeats==null&&usedSeats==null){
                        return ;
                    }
                    updateSeats();
                    break;
                }
            }
        }
    };



    private void updateSeats(){
        if(allSeats==null||usedSeats==null){
//            Looper.prepare();
            Toast.makeText(this, "加载失败",Toast.LENGTH_SHORT).show();
//            Looper.loop();

            finish();
        }
        checkedSeat = new HashMap<Integer,Integer>();
        allValidSeat=getAllValidSeat(allSeats);
        allSoldSeat=getAllSoldSeat(usedSeats);
        //////////////////////////////////////////////////////////////
        seatTableView.setScreenName(screeningroomName);//设置屏幕名称
        seatTableView.setMaxSelected(4);//设置最多选中
        seatTableView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public HashMap<Integer,Integer> getAllValidSeat() {
                return allValidSeat;
            }

            @Override
            public HashMap<Integer,Integer> getAllSoldSeat() {
                return allSoldSeat;
            }

            @Override
            public boolean isValidSeat(HashMap<Integer,Integer> allValidSeat, int row, int column) {
                if(allValidSeat.containsKey(row*100+column)){
                    return true;
                }
                return false;
            }

            @Override
            public boolean isSold(HashMap<Integer,Integer> allSoldSeat, int row, int column) {
                if(allSoldSeat.containsKey(row*100+column)){
                    return true;
                }
                return false;
            }


            @Override
            public void checked(int row, int column,HashMap<Integer,Integer> allValidSeat) {
                checkedSeat.put(row*100+column,allValidSeat.get(row*100+column));
            }

            @Override
            public void unCheck(int row, int column) {
                if(checkedSeat.containsKey(row*100+column)){
                    checkedSeat.remove(row*100+column);
                }
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        seatTableView.setData(row+1,column+1);
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn: {
                    finish();
                    break;
                }
                case R.id.save: {
                    if(checkedSeat.size()<=0){
                        break;
                    }
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            List<Seat> checkedList = new ArrayList<Seat>();
                            Iterator<Map.Entry<Integer, Integer>> iterator = checkedSeat.entrySet().iterator();
                            for(Map.Entry<Integer, Integer> entry : checkedSeat.entrySet()){
                                checkedList.add(new Seat(entry.getValue(),screeningroomId));
                            }
                            String orderId = OrderClient.buildOrder(getApplicationContext(), PhoneManagement.getPhone(),new Date().getTime(),price*checkedSeat.size(),checkedList,screeningId);
                            if(orderId!=null){
                                Intent i = new Intent(SeatActivity.this, OrderActivity.class);
                                i.putExtra("ticketCount", checkedSeat.size());
                                i.putExtra("totalPrice", price);
                                i.putExtra("movieName", movieName);
                                i.putExtra("orderId", orderId);
                                startActivity(i);
                                finish();
                            }
                            else{
                                finish();
                                return;
                            }
                        }
                    }).start();
                    break;
                }
            }
        }
    }
}
