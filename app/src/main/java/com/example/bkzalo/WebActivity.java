package com.example.bkzalo;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.webView);
        Intent intent =getIntent();
        String link =intent.getStringExtra("link");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onLoadResource(WebView view, String link) {
                super.onLoadResource(view, link);
            }
        });
        webView.loadUrl(link);
    }
}
