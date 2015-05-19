package net.melove.app.ml.http;


import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * Created by lzan13 on 2015/3/20.
 */

public class MLHttpUtil {

    private static MLHttpClient httpClient;

    /*静态内部类的方式实现单例模式*/
    private static class MLHttpUtilHolder{
        private static final MLHttpUtil instance = new MLHttpUtil();
    }
    public static MLHttpUtil getInstance(Context context){
        httpClient = new MLHttpClient(context);
        return MLHttpUtilHolder.instance;
    }
    /*私有的构造方法*/
    private MLHttpUtil(){}

    public void netOnLine(String url){
        httpClient.netOnLine(url);
    }

    public void get(String url, MLRequestParams params, MLBaseResponseListener listener){
        httpClient.get(url, params, listener);
    }

    public void post(String url, MLRequestParams params, MLBaseResponseListener listener){
        httpClient.post(url, params, listener);
    }

    public void postImage(String url, HttpEntity httpEntiy, MLBaseResponseListener listener){
        httpClient.postImage(url, httpEntiy, listener);
    }




}
