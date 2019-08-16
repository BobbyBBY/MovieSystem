package com.example.moviesystemclient.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.moviesystemclient.R;
import com.example.moviesystemclient.Util.QRCodeUtil;
import com.example.moviesystemclient.adapters.TicketAdapter;
import com.example.moviesystemclient.bean.TicketView;
import com.example.moviesystemclient.bean.item.TicketItem;
import com.example.moviesystemclient.server.client.TicketClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class TicketActivity extends AppCompatActivity {

    private Button backIBtn;//返回按钮
    private TextView orderId;
    private ImageView orderImage;
    private ListView listView;
    private SharedPreferences sharedPreferences;
    private String orderIdstr;
    private List<TicketView> ticketList;


    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:{
                    if(ticketList==null){
                        return ;
                    }
                    List<TicketItem> listItemList = TicketItem.getItemList(ticketList);
                    setList(listItemList);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketactivity);
        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        orderIdstr=sharedPreferences.getString("orderId","");
        try {
            intiView();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable(){
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what=0;
                ticketList = TicketClient.getTickets(getApplicationContext(),orderIdstr);
                handler.sendMessage(message);

            }
        }).start();
    }

    public void intiView() throws FileNotFoundException {
        backIBtn = findViewById(R.id.backBtn);
        orderId = findViewById(R.id.orderid);
        orderImage = findViewById(R.id.img_qr);
        listView = findViewById(R.id.list_new);
        if(QRCodeUtil.createQRImage(orderIdstr,200,200,null,"data/data/com.example.moviesystemclient/databases")){
            FileInputStream fis = new FileInputStream("data/data/com.example.moviesystemclient/databases");
            Bitmap bitmap= BitmapFactory.decodeStream(fis);
            orderImage.setImageBitmap(bitmap);
        }
        orderId.setText(orderIdstr);
        backIBtn.setOnClickListener(new ButtonListener());
    }


    public void setList(List<TicketItem> listItemList ){
        TicketAdapter listAdapternew = new TicketAdapter(this, R.layout.ticket_list, listItemList);
        listView.setAdapter(listAdapternew);
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn: {
                    finish();
                    break;
                }
            }
        }
    }
}
