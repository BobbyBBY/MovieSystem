package com.example.moviesystemclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.moviesystemclient.R;
import com.example.moviesystemclient.bean.Ordertable;
import com.example.moviesystemclient.encryption.PhoneManagement;
import com.example.moviesystemclient.server.client.OrderClient;
import com.example.moviesystemclient.server.client.ScreeningClient;

import java.util.Date;
import java.util.HashMap;


public class OrderActivity extends AppCompatActivity {

    private Button backIBtn;//返回按钮
    private Button weixnBtn;
    private Button alipayBtn;
    private TextView movieNameTV;
    private TextView phoneTV;
    private TextView timeTV;
    private TextView countTV;
    private TextView priceTV;

    private String movieName;
    private double totalPrice;
    private int ticketCount;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderactivity);
        Intent i = getIntent();
        ticketCount = i.getIntExtra("ticketCount",0);
        totalPrice = i.getDoubleExtra("totalPrice",0.0);
        movieName = i.getStringExtra("movieName");
        orderId = i.getStringExtra("orderId");
        intiView();
    }

    public void intiView() {
        backIBtn = findViewById(R.id.backBtn);
        weixnBtn = findViewById(R.id.weixin);
        alipayBtn = findViewById(R.id.alipay);
        movieNameTV = findViewById(R.id.moviename);
        phoneTV = findViewById(R.id.phone);
        timeTV = findViewById(R.id.time);
        countTV = findViewById(R.id.count);
        priceTV = findViewById(R.id.price);

        backIBtn.setOnClickListener(new ButtonListener());
        weixnBtn.setOnClickListener(new ButtonListener());
        alipayBtn.setOnClickListener(new ButtonListener());
        movieNameTV.setText("订单包含： "+movieName);
        phoneTV.setText("电话： "+ PhoneManagement.getPhone());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        timeTV.setText("订单生成时间： "+sdf.format(new Date().getTime()));
        countTV.setText("共有"+ticketCount+"张票");
        priceTV.setText(totalPrice*ticketCount+" 元");
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.weixin: {
                    finish();
                    break;
                }case R.id.alipay: {
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            Ordertable ordertable = new Ordertable();
                            ordertable.setOrderId(orderId);
                            ordertable.setOrderStatus(2);
                            OrderClient.updateOrder(getApplicationContext(),ordertable);
                        }
                    }).start();
                    finish();
                    break;
                }

            }
        }
    }

}
