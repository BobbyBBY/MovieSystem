package com.example.moviesystemclient.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.moviesystemclient.R;


public class webview extends AppCompatActivity {

    private Button webviewBackIBtn;//返回按钮
    private Button refresh;
    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        intiView();
        SharedPreferences sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        url = sharedPreferences.getString("webviewUrl","https://www.douban.com");
        if(url!=null){
            refresh();
        }
        else{
            //webView.loadUrl("webviewUrl");//显示自定义页面
        }
    }
    private void refresh(){
        webView.loadUrl(url);
    }

    public void intiView() {
        webviewBackIBtn = findViewById(R.id.backBtn);
        refresh = findViewById(R.id.refresh);
        webView = (WebView) findViewById(R.id.webView);
        webviewBackIBtn.setOnClickListener(new ButtonListener());
        refresh.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn: {
                    finish();
                    break;
                }
                case R.id.refresh: {
                    refresh();
                    break;
                }
            }
        }
    }
}
