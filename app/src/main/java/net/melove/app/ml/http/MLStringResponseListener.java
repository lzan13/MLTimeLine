package net.melove.app.ml.http;

import java.util.Objects;

/**
 * Created by Administrator on 2015/3/24.
 */
public class MLStringResponseListener extends MLBaseResponseListener {
    public void onSuccess(int state, String content){

    }
    public void sendSuccessMessage(int state, String content) {
        sendMessage(obtainMessage(MLHttpConstants.WHAT_SUCCESS, new Object[]{state, content}));
    }

}
