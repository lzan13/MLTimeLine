package net.melove.app.ml.handler;

import android.os.Handler;
import android.os.Message;

/**
 * Created by lzan13 on 2015/3/25.
 */
public class MLHandler extends Handler{

    @Override
    public void handleMessage(Message msg) {
        int what = msg.what;
        switch (what) {
            case 0x00:

                break;
            case 0x01:

                break;
            case 0x10:

                break;
            case 0x20:

                break;
            default:
                break;
        }
    }
}
