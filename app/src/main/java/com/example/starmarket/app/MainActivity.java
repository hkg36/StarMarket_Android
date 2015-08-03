package com.example.starmarket.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RadioGroup;
import butterknife.Bind;
import butterknife.ButterKnife;

import java.util.*;

public class MainActivity extends ActionBarActivity {
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.plane)
    RadioGroup plane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new MyWebDelegate(webView));
        webView.loadUrl("http://stardemo1.sinaapp.com/list.html");

        plane.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.main:
                        webView.loadUrl("http://stardemo1.sinaapp.com/list.html");
                        break;
                    case R.id.rank:
                        webView.loadUrl("http://stardemo1.sinaapp.com/Ranking.html");
                        break;
                    case R.id.me:
                        webView.loadUrl("http://testandroid1.sinaapp.com/main.html");
                        break;
                }
            }
        });
    }
    class MyWebDelegate extends WebDelegate{
        MyWebDelegate(WebView wview){
            super(wview,MainActivity.this);
            funs.put("openPage", new FunCall() {
                @Override
                public Map<String, Object> call(Map<String, Object> param) {
                    Intent newint=new Intent(MainActivity.this,WebActivity.class);
                    newint.putExtra("url",param.get("url").toString());
                    startActivity(newint);
                    Map<String, Object> res = new HashMap<String, Object>();
                    return res;
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
