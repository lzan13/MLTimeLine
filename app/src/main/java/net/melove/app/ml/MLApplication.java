package net.melove.app.ml;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

/**
 * Created by lzan13 on 2015/3/25.
 */
public class MLApplication extends Application {
    // 文件路径名
    private static String SDCard = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    private static String app = SDCard + "MLApplication/MLTimeLine/";
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
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .imageScaleType(ImageScaleType.EXACTLY)
                .resetViewBeforeLoading(false)
                .showImageForEmptyUri(R.mipmap.bg_transparent_gray)
                .showImageOnLoading(R.mipmap.bg_transparent_gray)
                .showImageOnFail(R.mipmap.bg_transparent_gray)
                .build();

        // 初始化ImageLoader，创建全局的配置
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCache(new UnlimitedDiskCache(new File(MLApplication.getCache())));
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.threadPoolSize(5);
        config.defaultDisplayImageOptions(options);

        // 开发模式，打印Debug调试信息
        config.writeDebugLogs();

        ImageLoader.getInstance().init(config.build());
    }

    public static Context getContext() {
        return context;
    }


    public static void setUserPath(String userPath) {
        MLApplication.userPath = app + userPath + "/";
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
