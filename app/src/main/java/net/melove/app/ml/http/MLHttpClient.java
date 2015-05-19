package net.melove.app.ml.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import net.melove.app.ml.R;
import net.melove.app.ml.utils.MLLog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2015/3/23.
 */
public class MLHttpClient {

    private Context mContext;
    private ThreadPoolExecutor mThreadPoolExecutor;


    /**
     * 构造函数
     *
     * @param context
     */
    public MLHttpClient(Context context) {
        mContext = context;
        mThreadPoolExecutor = MLThreadPool.getThreadPoolExecutor();
    }

    public void netOnLine(final String url) {
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpGet get = new HttpGet(url);

                    BasicHttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, MLHttpConstants.HTTP_TIME_OUT);
                    HttpConnectionParams.setSoTimeout(httpParams, MLHttpConstants.HTTP_TIME_OUT);
                    HttpClient client = new DefaultHttpClient(httpParams);
                    HttpResponse response = null;
                    response = client.execute(get);
                    int state = response.getStatusLine().getStatusCode();
                    if (state == 200) {
                        MLLog.d("Servers is open! ");
                    }
                } catch (ClientProtocolException e) {
                    MLLog.d("Servers is close! ClientProtocolException " + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    MLLog.d("Servers is close! IOException " + e.getMessage());
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * get方法
     *
     * @param url
     * @param params
     * @param listener
     */
    public void get(final String url, final MLRequestParams params, final MLBaseResponseListener listener) {
        listener.setmHandler(new ResponseHandler(listener));
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    /**
     * doGet get请求的具体执行函数
     *
     * @param url
     * @param params
     * @param listener
     */
    public void doGet(String url, MLRequestParams params, MLBaseResponseListener listener) {

    }

    /**
     * 带参数的 post 请求
     *
     * @param url
     * @param params
     * @param listener
     */
    public void post(final String url, final MLRequestParams params, final MLBaseResponseListener listener) {
        listener.setmHandler(new ResponseHandler(listener));
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                doPost(url, params, listener);
            }
        });
    }

    /**
     * doPost post请求具体执行函数
     *
     * @param url
     * @param params
     * @param listener
     */
    public void doPost(String url, MLRequestParams params, MLBaseResponseListener listener) {
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpClient httpClient = new DefaultHttpClient();

            HttpEntity httpEntity = params.toURLEntity();
            httpPost.setEntity(httpEntity);

            BasicHttpParams httpParams = new BasicHttpParams();
            // 设置ConnectionPoolTimeout：定义了从ConnectionManager管理的连接池中取出连接的超时时间
            ConnManagerParams.setTimeout(httpParams, MLHttpConstants.POOL_TIME_OUT);
            ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(MLHttpConstants.MAX_CONNECTIONS));
            ConnManagerParams.setMaxTotalConnections(httpParams, MLHttpConstants.MAX_CONNECTIONS);

            // 设置ConnectionTimeout：定义了通过网络与服务器建立连接的超时时间。
            // Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间，
            HttpConnectionParams.setConnectionTimeout(httpParams, MLHttpConstants.HTTP_TIME_OUT);
            // 设置SocketTimeout：定义了Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间
            HttpConnectionParams.setSoTimeout(httpParams, MLHttpConstants.HTTP_TIME_OUT);
            HttpConnectionParams.setTcpNoDelay(httpParams, true);
            HttpConnectionParams.setSocketBufferSize(httpParams, MLHttpConstants.SOCKER_BUFFER_SIZE);

            httpPost.setParams(httpParams);

            HttpResponse response = httpClient.execute(httpPost);
            int state = response.getStatusLine().getStatusCode();
            if (state == HttpStatus.SC_OK) {
                if (listener instanceof MLStringResponseListener) {
                    String result = EntityUtils.toString(response.getEntity());
                    ((MLStringResponseListener) listener).sendSuccessMessage(state, result);
                }
            } else {
                String result = EntityUtils.toString(response.getEntity());
                listener.sendFailureMessage(state, result);
            }

        } catch (IOException e) {
            listener.sendFailureMessage(-1, mContext.getResources().getString(R.string.ml_error_http));
        }
    }

    public void postImage(final String url, final HttpEntity httpEntiy, final MLBaseResponseListener listener) {
        listener.setmHandler(new ResponseHandler(listener));
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                doPostImage(url, httpEntiy, listener);
            }
        });
    }

    public void doPostImage(String url, HttpEntity httpEntiy, MLBaseResponseListener listener) {
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpClient httpClient = new DefaultHttpClient();

            httpPost.setEntity(httpEntiy);

            BasicHttpParams httpParams = new BasicHttpParams();
            // 设置ConnectionPoolTimeout：定义了从ConnectionManager管理的连接池中取出连接的超时时间
//            ConnManagerParams.setTimeout(httpParams, MLHttpConstants.POOL_TIME_OUT);
            ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(MLHttpConstants.MAX_CONNECTIONS));
            ConnManagerParams.setMaxTotalConnections(httpParams, MLHttpConstants.MAX_CONNECTIONS);

            // 设置ConnectionTimeout：定义了通过网络与服务器建立连接的超时时间。
            // Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间，
//            HttpConnectionParams.setConnectionTimeout(httpParams, MLHttpConstants.HTTP_TIME_OUT);
            // 设置SocketTimeout：定义了Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间
//            HttpConnectionParams.setSoTimeout(httpParams, MLHttpConstants.HTTP_TIME_OUT);
            HttpConnectionParams.setTcpNoDelay(httpParams, true);
            HttpConnectionParams.setSocketBufferSize(httpParams, MLHttpConstants.SOCKER_BUFFER_SIZE);

            httpPost.setParams(httpParams);

            HttpResponse response = httpClient.execute(httpPost);
            int state = response.getStatusLine().getStatusCode();
            if (state == HttpStatus.SC_OK) {
                if (listener instanceof MLImageResponseListener) {
                    String result = EntityUtils.toString(response.getEntity());
                    MLLog.i(result);
                    ((MLImageResponseListener) listener).sendSuccessMessage(state, result);
                }
            } else {
                String result = EntityUtils.toString(response.getEntity());
                MLLog.i(result);
                listener.sendFailureMessage(state, result);
            }

        } catch (IOException e) {
            listener.sendFailureMessage(-1, mContext.getResources().getString(R.string.ml_error_http));
        }
    }


    private class ResponseHandler extends Handler {

        private Object[] objs;
        private MLBaseResponseListener mListener;

        public ResponseHandler(MLBaseResponseListener listener) {
            mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case MLHttpConstants.WHAT_SUCCESS:
                    objs = (Object[]) msg.obj;
                    if (mListener instanceof MLStringResponseListener) {
                        if (objs.length >= 2) {
                            ((MLStringResponseListener) mListener).onSuccess((Integer) objs[0], (String) objs[1]);
                        }
                    }else if(mListener instanceof MLImageResponseListener){
                        if (objs.length >= 2) {
                            ((MLImageResponseListener) mListener).onSuccess((Integer) objs[0], (String) objs[1]);
                        }
                    }
                    break;
                case MLHttpConstants.WHAT_FAILURE:
                    objs = (Object[]) msg.obj;
                    if (objs.length >= 2) {
                        mListener.onFailure((Integer) objs[0], (String) objs[1]);
                    }
                    break;
                default:

                    break;
            }
        }
    }

}
