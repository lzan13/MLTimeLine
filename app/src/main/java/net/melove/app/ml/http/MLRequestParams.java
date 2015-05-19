package net.melove.app.ml.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2015/3/23.
 */
public class MLRequestParams {

    //
    protected ConcurrentHashMap<String, String> strParams;

    private void initConcurrentParams(){
        strParams = new ConcurrentHashMap<String, String>();
    }

    public MLRequestParams(){
        initConcurrentParams();
    }

    public MLRequestParams(Map<String, String> map){
        initConcurrentParams();
        putParams(map);
    }

    public MLRequestParams(String key, String value){
        initConcurrentParams();
        putParams(key, value);
    }


    public void putParams(Map<String, String> map){
        for(Map.Entry<String, String> entry : map.entrySet()){
            putParams(entry.getKey(), entry.getValue());
        }
    }

    public void putParams(String key, String value){
        if(key != null && value != null) {
            strParams.put(key, value);
        }
    }

    public void removeParam(String key) {
        strParams.remove(key);
    }

    /**
     * 转换为get请求方式所需的参数形式（字符串形式）
     * @return
     */
    public String toStringParams(){
        StringBuffer result = new StringBuffer();
        for(ConcurrentHashMap.Entry<String, String> entry : strParams.entrySet()){
            if(result.length() > 0){
                result.append("&");
            }
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
        return result.toString();
    }

    /**
     * 转化为post请求方式所需的参数形式（集合形式）
     * @return
     */
    public List<BasicNameValuePair> toListParams() {
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        for(ConcurrentHashMap.Entry<String, String> entry : strParams.entrySet()){
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return params;
    }

    /**/
    public String toURLString() {
        return URLEncodedUtils.format(toListParams(), HTTP.UTF_8);
    }

    public HttpEntity toURLEntity() throws UnsupportedEncodingException {
        return new UrlEncodedFormEntity(toListParams(), HTTP.UTF_8);
    }
}
