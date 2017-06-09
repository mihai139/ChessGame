package com.mihai.chessgame;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RulesOfChess extends AppCompatActivity {

    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rules_of_chess);
        webView = (WebView)findViewById(R.id.myWebView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl("file:///android_asset/web/rules.html");


    }





}


