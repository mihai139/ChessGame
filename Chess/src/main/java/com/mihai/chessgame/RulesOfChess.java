package com.mihai.chessgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

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
