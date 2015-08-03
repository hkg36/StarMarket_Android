package com.example.starmarket.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import butterknife.Bind;
import butterknife.ButterKnife;


public class WebActivity extends Activity {
    @Bind(R.id.webView)
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new MyWebDelegate(webView));
        webView.loadUrl(getIntent().getStringExtra("url"));
    }
    class MyWebDelegate extends WebDelegate{
        MyWebDelegate(WebView wview){
            super(wview,WebActivity.this);
        }
        @Override
        public void onPageFinished(WebView view, String url){
            WebActivity.this.setTitle( view.getTitle());
            super.onPageFinished(view,url);
        }
    }
    @Override
    public void onBackPressed(){
        if(webView.canGoBack())
            webView.goBack();
        else
            finish();
    }
}
