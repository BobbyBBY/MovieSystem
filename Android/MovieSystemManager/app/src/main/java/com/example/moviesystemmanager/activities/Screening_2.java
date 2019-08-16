package com.example.moviesystemmanager.activities;

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

import com.example.moviesystemmanager.R;
import com.example.moviesystemmanager.adapters.ScreeningAdapter;
import com.example.moviesystemmanager.bean.Screening;
import com.example.moviesystemmanager.bean.ScreeningView;
import com.example.moviesystemmanager.bean.item.ScreeningItem;
import com.example.moviesystemmanager.server.client.MovieClient;
import com.example.moviesystemmanager.server.client.ScreeningClient;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class Screening_2 extends AppCompatActivity {
    private Button backIBtn;//返回按钮
    private Button newScreening;
    private ListView listView;
    private RefreshLayout refreshLayout;
    private String movieId;
    private List<ScreeningItem> listItemList;
    private ScreeningAdapter listAdapternew;
    private List<ScreeningView> screeningList;
    private List<ScreeningView> listforloading;
    private int page;

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
        setContentView(R.layout.screening_2);
        Intent i = getIntent();
        movieId=i.getStringExtra("movieId");
        page =0;
        intiView();
    }

    public void intiView() {
        backIBtn = findViewById(R.id.backBtn);
        newScreening = findViewById(R.id.newscreening);
        listView = findViewById(R.id.list_new);
        refreshLayout = findViewById(R.id.refreshLayout);
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what=0;
                        page = 0;
                        screeningList = ScreeningClient.getScreenings(getApplicationContext(),movieId,-2,-1,0,page);
                        handler.sendMessage(message);
                    }
                }).start();

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000/*,false*/);//传入false表示加载失败
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what=1;
                        listforloading = ScreeningClient.getScreenings(getApplicationContext(),movieId,-2,-1,0,++page);
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
        backIBtn.setOnClickListener(new ButtonListener());
        newScreening.setOnClickListener(new ButtonListener());
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ScreeningView selected = screeningList.get(position);
                        Intent intent = new Intent(Screening_2.this, Screening_4.class);
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd/HH/mm");
                        intent.putExtra("screeningId",selected.getScreeningId());
                        intent.putExtra("startTime",sdf.format(selected.getScreeningStarttime()));
                        intent.putExtra("price",selected.getScreeningPrice());
                        intent.putExtra("se",selected.getScreeningSpecialeffect());
                        startActivity(intent);
                    }
                }
        );
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
                case R.id.newscreening: {
                    Intent intent = new Intent(Screening_2.this, Screening_4.class);
                    intent.putExtra("movieId",movieId);
                    startActivity(intent);
                    break;
                }
            }
        }
    }



}
