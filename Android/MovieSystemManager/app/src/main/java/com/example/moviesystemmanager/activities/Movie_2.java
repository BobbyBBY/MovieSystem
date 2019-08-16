package com.example.moviesystemmanager.activities;

import android.content.Intent;
import android.opengl.ETC1;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.moviesystemmanager.R;
import com.example.moviesystemmanager.bean.Movie;
import com.example.moviesystemmanager.bean.Ordertable;
import com.example.moviesystemmanager.server.client.MovieClient;
import com.example.moviesystemmanager.server.client.OrderClient;

import java.text.ParseException;
import java.util.Date;

public class Movie_2 extends AppCompatActivity {
    private Button save;
    private Button back;

    private EditText movieId;
    private EditText movieName;
    private EditText doubanId;
    private EditText onlineTime;
    private EditText offlineTime;

    private EditText introduction;
    private EditText duration;
    private EditText status;

    private String movieIdStr;
    private String moviewNameStr;
    private String doubanIdStr;
    private String onlineTimeStr;
    private String offlineTimeStr;

    private String introductionStr;
    private String durationStr;
    private String statusStr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_2);

        Intent i = getIntent();

        movieIdStr = i.getStringExtra("movieId");
        moviewNameStr = i.getStringExtra("movieName");
        doubanIdStr = i.getStringExtra("doubanId");
        onlineTimeStr = i.getStringExtra("onlineTime");
        offlineTimeStr = i.getStringExtra("offlineTime");

        introductionStr = i.getStringExtra("introduction");
        durationStr = i.getStringExtra("duration");
        statusStr = i.getStringExtra("status");

        intiView();
        initViewData();

    }
    private void intiView(){
        movieId=findViewById(R.id.movieid);
        movieName=findViewById(R.id.moviename);
        doubanId=findViewById(R.id.doubanid);
        onlineTime=findViewById(R.id.onlinetimeet);
        offlineTime=findViewById(R.id.offlinetimeet);

        introduction=findViewById(R.id.introduction);
        duration=findViewById(R.id.duration);
        status=findViewById(R.id.status);
        save=findViewById(R.id.save);
        back=findViewById(R.id.backBtn);

        save.setOnClickListener(new ButtonListener());
        back.setOnClickListener(new ButtonListener());

    }

    private void initViewData(){
        movieId.setText(movieIdStr);
        movieName.setText(moviewNameStr);
        doubanId.setText(doubanIdStr);
        onlineTime.setText(onlineTimeStr);
        offlineTime.setText(offlineTimeStr);

        introduction.setText(introductionStr);
        duration.setText(durationStr);
        status.setText(statusStr);

    }


    private class ButtonListener implements View.OnClickListener{
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn: {
                    finish();
                    break;
                }
                case R.id.save: {
                    movie = new Movie();

                    movie.setMovieId(movieId.getText().toString());
                    movie.setHarddiskId("123");
                    movie.setMovieDoubanid(doubanId.getText().toString());
                    movie.setMovieDuration(Integer.valueOf(duration.getText().toString()));
                    movie.setMovieIntroduction(introduction.getText().toString());

                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd");

                    movie.setMovieName(movieName.getText().toString());
                    try {
                        movie.setMovieOfflinetime(sdf.parse(offlineTime.getText().toString()));
                    } catch (ParseException e) {
                        movie.setMovieOfflinetime(new Date());
                    }
                    try {
                        movie.setMovieOnlinetime(sdf.parse(onlineTime.getText().toString()));
                    } catch (ParseException e) {
                        movie.setMovieOfflinetime(new Date());
                    }
                    movie.setMovieStatus(Integer.valueOf(status.getText().toString()));

                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            MovieClient.updateMovie(getApplicationContext(),movie);
                        }
                    }).start();
                    finish();
                    break;
                }
            }
        }

    }
    private Movie movie;

}
