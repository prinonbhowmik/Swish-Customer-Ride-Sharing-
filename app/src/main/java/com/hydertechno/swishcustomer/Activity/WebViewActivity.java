package com.hydertechno.swishcustomer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.hydertechno.swishcustomer.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private String UrlLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView=findViewById(R.id.webview);
        Intent intent=getIntent();
        UrlLink=intent.getStringExtra("link");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(UrlLink);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else
            finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }
}