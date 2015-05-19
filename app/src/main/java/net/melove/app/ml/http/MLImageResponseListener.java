package net.melove.app.ml.http;

/**
 * Created by Administrator on 2015/5/6.
 */
public class MLImageResponseListener extends MLBaseResponseListener {

    public void onSuccess(int state, String content){

    }
    public void sendSuccessMessage(int state, String content) {
        sendMessage(obtainMessage(MLHttpConstants.WHAT_SUCCESS, new Object[]{state, content}));
    }

}
