package net.melove.app.ml.http;

import android.os.Handler;
import android.os.Message;


/**
 * Created by Administrator on 2015/3/23.
 */
public class MLBaseResponseListener {

    private Handler mHandler;

    public MLBaseResponseListener(){
        super();
    }

    public void onFinish() {}

    public void onFailure(int state, String content){}

    public void sendFailureMessage(int state, String content){
        sendMessage(obtainMessage(MLHttpConstants.WHAT_FAILURE, new Object[]{state, content}));
    }


    public Message obtainMessage(int what, Object obj) {
        Message msg = null;
        if (mHandler != null) {
            msg = mHandler.obtainMessage(what, obj);
        } else {
            msg = Message.obtain();
            msg.what = what;
            msg.obj = obj;
        }
        return msg;
    }

    public void sendMessage(Message msg) {
        if (msg != null) {
            msg.sendToTarget();
        }
    }

    public Handler getmHandler() {
        return mHandler;
    }

    public void setmHandler(Handler handler) {
        mHandler = handler;
    }


}
