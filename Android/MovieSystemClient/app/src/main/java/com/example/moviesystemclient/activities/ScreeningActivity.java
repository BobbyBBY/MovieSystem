package com.example.moviesystemclient.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.example.moviesystemclient.R;
import com.example.moviesystemclient.adapters.ScreeningAdapter;
import com.example.moviesystemclient.bean.ScreeningView;
import com.example.moviesystemclient.bean.item.ScreeningItem;
import com.example.moviesystemclient.server.client.ScreeningClient;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

public class ScreeningActivity extends AppCompatActivity {
    private Button backIBtn;//返回按钮
    private Button doubanBtn;
    private RadioGroup tabBar;
    private RadioButton today;
    private RadioButton tomorrow;
    private RadioButton dat;
    private TextView topTitle;
    private ListView listView;
    private RefreshLayout refreshLayout;
    private SharedPreferences sharedPreferences;
    private String movieId;
    private String movieNameStr;
    private List<ScreeningItem> listItemList;
    private ScreeningAdapter listAdapternew;
    private List<ScreeningView> screeningList;
    private List<ScreeningView> listforloading;
    private int radioStatus;
    private int page;
    private String doubanStr;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:{
                    if(screeningList==null){
                        return ;
                    }
                    List<ScreeningItem> listItemList = ScreeningItem.getItemList(screeningList);
                    setList(listItemList);
                    break;
                }
                case 1:{
                    if(listforloading==null){
                        return ;
                    }
                    List<ScreeningItem> listItemList = ScreeningItem.getItemList(listforloading);
                    screeningList.addAll(listforloading);
                    updateList(listItemList);
                    break;
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screeningactivity);
        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        movieId=sharedPreferences.getString("movieId","");
        doubanStr=sharedPreferences.getString("douban","");
        movieNameStr =sharedPreferences.getString("movieName","选择场次");
        screeningList=new ArrayList<ScreeningView>();
        radioStatus=1;
        intiView();
//        List<ScreeningItem> listtemp = new ArrayList<ScreeningItem>();
//        listtemp.add(new ScreeningItem("追龙2",(new java.text.SimpleDateFormat("hh:mm")).format(new Date(Long.valueOf("1560476476763"))),(new java.text.SimpleDateFormat("hh:mm")).format(new Date(Long.valueOf("1560476476763"))),"2D","一号厅（建设银行厅）","40.5"));
//        setList(listtemp);
    }

    public void intiView() {
        backIBtn = findViewById(R.id.backBtn);
        doubanBtn = findViewById(R.id.douban);
        tabBar = findViewById(R.id.tab_bar);
        today = findViewById(R.id.tab_item_today);
        tomorrow = findViewById(R.id.tab_item_tomorrow);
        dat = findViewById(R.id.tab_item_dat);
        listView = findViewById(R.id.list_new);
        topTitle = findViewById(R.id.toptitle);
        refreshLayout = findViewById(R.id.refreshLayout);
        topTitle.setText(movieNameStr);
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000/*,false*/);//传入false表示加载失败
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what=1;
                        listforloading = ScreeningClient.getScreenings(getApplicationContext(),movieId,-1,-1,radioStatus-1,++page);
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
        backIBtn.setOnClickListener(new ButtonListener());
        doubanBtn.setOnClickListener(new ButtonListener());
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        ScreeningView selected = screeningList.get(position);
                        editor.putInt("screeningId", selected.getScreeningId());
                        editor.putInt("screeningroomId", selected.getScreeningroomId());
                        editor.putString("screeningroomName", selected.getScreeningroomName());
                        editor.putFloat("price", selected.getScreeningPrice().floatValue());
                        editor.putString("movieName", selected.getMovieName());
                        editor.commit();
                        Intent intent = new Intent(ScreeningActivity.this, SeatActivity.class);
                        startActivity(intent);
                    }
                }
        );
        tabBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int itemId) {
                switch (itemId) {
                    case R.id.tab_item_today:{
                        radioStatus=1;
                        page=0;
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Message message = Message.obtain();
                                message.what=0;
                                screeningList = ScreeningClient.getScreenings(getApplicationContext(),movieId,-1,-1,radioStatus-1,page);
                                handler.sendMessage(message);
                            }
                        }).start();
                        break;
                    }
                    case R.id.tab_item_tomorrow:{
                        radioStatus=2;
                        page=0;
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Message message = Message.obtain();
                                message.what=0;
                                screeningList = ScreeningClient.getScreenings(getApplicationContext(),movieId,-1,-1,radioStatus-1,page);
                                handler.sendMessage(message);
                            }
                        }).start();
                        break;
                    }
                    case R.id.tab_item_dat:{
                        radioStatus=3;
                        page=0;
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Message message = Message.obtain();
                                message.what=0;
                                screeningList = ScreeningClient.getScreenings(getApplicationContext(),movieId,-1,-1,radioStatus-1,page);
                                handler.sendMessage(message);
                            }
                        }).start();
                        break;
                    }
                }
            }
        });
    }

    public void setList(List<ScreeningItem> itemList){
        listItemList = itemList;
        listAdapternew = new ScreeningAdapter(this, R.layout.screening_list, listItemList);
        listView.setAdapter(listAdapternew);
    }
    public void updateList(List<ScreeningItem> itemList){
        listItemList.addAll(itemList);
        listAdapternew.notifyDataSetChanged();
    }


    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn: {
                    finish();
                    break;
                }
                case R.id.douban: {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("webviewUrl", "https://movie.douban.com/subject/"+doubanStr);
                    editor.commit();
                    Intent intent = new Intent(ScreeningActivity.this, webview.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    }
}
