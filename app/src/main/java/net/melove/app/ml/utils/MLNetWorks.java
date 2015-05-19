package net.melove.app.ml.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.melove.app.ml.MLApp;

/**
 * Created by Administrator on 2015/5/12.
 */
public class MLNetWorks {
    /**
     * 检测设备是否有网络连接
     *
     * @return
     */
    public static boolean checkNetworkState() {
        boolean isNetwork = false;
        ConnectivityManager manager = (ConnectivityManager) MLApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null) {
            isNetwork = manager.getActiveNetworkInfo().isAvailable();
        }
        return isNetwork;
    }

    /**
     * 判断当前网络环境是wifi还是移动gprs网络
     *
     * @return
     */
    public static boolean isNetworkInfo() {
        ConnectivityManager manager = (ConnectivityManager) MLApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        }
        return false;
    }
}
