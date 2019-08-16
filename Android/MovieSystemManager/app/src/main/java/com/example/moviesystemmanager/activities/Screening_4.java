package com.example.moviesystemmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.moviesystemmanager.R;
import com.example.moviesystemmanager.bean.Screening;
import com.example.moviesystemmanager.server.client.ScreeningClient;

import java.text.ParseException;
import java.util.Date;

public class Screening_4  extends AppCompatActivity {
    private Button save;
    private Button back;

    private EditText startTime;
    private EditText price;
    private EditText se;

    private String movieIdStr;
    private int screeningIdStr;
    private String startTimeStr;
    private String priceStr;
    private String seStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screening_4);

        Intent i = getIntent();
        movieIdStr = i.getStringExtra("movieId");
        startTimeStr = i.getStringExtra("startTime");
        screeningIdStr = i.getIntExtra("screeningId",0);
        priceStr = i.getStringExtra("price");
        seStr = i.getStringExtra("se");
        intiView();
        initViewData();

    }
    private void intiView(){
        startTime=findViewById(R.id.starttimeet);
        price=findViewById(R.id.price);
        se=findViewById(R.id.se);
        save=findViewById(R.id.save);
        back=findViewById(R.id.backBtn);

        save.setOnClickListener(new ButtonListener());
        back.setOnClickListener(new ButtonListener());

    }
    private void initViewData(){
        startTime.setText(startTimeStr);
        price.setText(priceStr);
        se.setText(seStr);
    }

    private class ButtonListener implements View.OnClickListener{
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn: {
                    finish();
                    break;
                }
                case R.id.save: {
                    screening = new Screening();
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd/HH/mm");
                    screening.setMovieId(movieIdStr);
                    screening.setScreeningId(screeningIdStr);
                    screening.setScreeningroomId(1);
                    screening.setScreeningPrice(Double.valueOf(price.getText().toString()));
                    try {
                        screening.setScreeningStarttime(sdf.parse(startTime.getText().toString()));
                    } catch (ParseException e) {
                        screening.setScreeningStarttime(new Date());
                    }
                    screening.setScreeningSpecialeffect(Integer.valueOf(se.getText().toString()));
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            ScreeningClient.updateScreening(getApplicationContext(),screening);
                        }
                    }).start();
                    finish();
                    break;
                }
            }
        }

    }
    private Screening screening;



}
