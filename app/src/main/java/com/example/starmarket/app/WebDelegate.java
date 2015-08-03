package com.example.starmarket.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.google.gson.Gson;

import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenx on 2015/8/3.
 */
class WebDelegate extends WebViewClient implements BDLocationListener {

    interface FunCall{
        Map<String,Object> call(Map<String,Object> param);
    }
    Map<String,FunCall> funs=new HashMap<String, FunCall>();
    Gson gson = new Gson();
    WebView view;
    LocationClient mLocationClient = null;
    ProgressDialog pro;
    Context context;
    WebDelegate(WebView wview,Context cont){
        this.view=wview;
        this.context=cont;
        mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(this);
        pro=new ProgressDialog(context);
        pro.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pro.setMessage("正在加载数据..");
        pro.setCancelable(true);
        funs.put("getVersion", new FunCall() {
            @Override
            public Map<String, Object> call(Map<String,Object> param) {
                Map<String, Object> res = new HashMap<String, Object>();
                res.put("version", 1.0);
                return res;
            }
        });
        funs.put("startLocationUpdate", new FunCall() {
            @Override
            public Map<String, Object> call(Map<String, Object> param) {
                mLocationClient.start();
                Map<String, Object> res = new HashMap<String, Object>();
                res.put("result", "1");
                return res;
            }
        });
        funs.put("stopLocationUpdate", new FunCall() {
            @Override
            public Map<String, Object> call(Map<String,Object> param) {
                mLocationClient.stop();
                Map<String,Object> res = new HashMap<String,Object>();
                res.put("result", "1");
                return res;
            }
        });
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            URI uri = new URI(url);
            if(uri.getScheme().compareToIgnoreCase("jsinter")==0){
                String rcode=uri.getHost();
                String fun=uri.getPath().substring(1);
                FunCall callfn=funs.get(fun);
                if(callfn!=null) {
                    Map<String, Object> params = new HashMap<String,Object>();
                    String query = uri.getQuery();
                    if(query.length()>0) {
                        String[] pairs = query.split("&");
                        for (String pair : pairs) {
                            int idx = pair.indexOf("=");
                            if(idx>0) {
                                params.put(pair.substring(0, idx), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                            }
                        }
                    }
                    Map<String, Object> res=callfn.call(params);
                    String jsonStr= gson.toJson(res);
                    String jsStr=String.format("javascript:InterCall.DoCallBack('%s',%s)", rcode, jsonStr);
                    view.loadUrl(jsStr);
                }
                return true;
            }
            mLocationClient.stop();
            view.loadUrl(url);
        }catch (Exception e){

        }
        return true;
    }
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        Map<String,Number> point=new HashMap<String, Number>();
        point.put("lat",bdLocation.getLatitude());
        point.put("long",bdLocation.getLongitude());
        String jsonStr= gson.toJson(point);
        String jsStr=String.format("javascript:InterCall.positionUpdate(%s)", jsonStr);
        view.loadUrl(jsStr);
    }
    @Override
    public void onPageFinished(WebView view, String url)
    {
        pro.dismiss();
        super.onPageFinished(view, url);
    }
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
        pro.show();
        super.onPageStarted(view, url, favicon);
    }
}
