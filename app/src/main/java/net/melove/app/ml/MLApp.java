package net.melove.app.ml;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

/**
 * Created by Administrator on 2015/3/25.
 */
public class MLApp extends Application {
    // 文件路径名
    private static String SDCard = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    private static String app = SDCard + "MLApp/MLTimeLine/";
    private static String audio = "audio/";
    private static String cache = "cache/";
    private static String db = "db/";
    private static String image = "image/";
    private static String logs = ".logs/";
    private static String temp = "temp/";
    private static String update = "update/";
    private static String video = "video/";
    private static String userPath;

    // 全局变量
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }


    public static void setUserPath(String userPath) {
        MLApp.userPath = app + userPath + "/";
    }

    public static String getUserPath() {
        return userPath;
    }

    public static String getApp() {
        return app;
    }

    public static String getAudio() {
        return app + audio;
    }

    public static String getCache() {
        return app + cache;
    }

    public static String getDb() {
        return userPath + db;
    }

    public static String getImage() {
        return app + image;
    }

    public static String getUserImage() {
        return userPath + image;
    }

    public static String getLogs() {
        return app + logs;
    }

    public static String getTemp() {
        return app + temp;
    }

    public static String getUpdate() {
        return app + update;
    }

    public static String getVideo() {
        return app + video;
    }

    public static String getSDCard() {
        return SDCard;
    }
}
