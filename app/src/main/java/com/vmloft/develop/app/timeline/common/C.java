package com.vmloft.develop.app.timeline.common;

/**
 * Created by lzan13 on 2018/4/8.
 * 常量类
 */
public class C {

    public static final String LC_APP_ID = "6jVlos1YxADiOWlTo8Qkd76X-gzGzoHsz";
    public static final String LC_APP_KEY = "daCnqD44P8wn45TuJNBSYMPU";

    // 测试环境
    public static final String BETA_BASE_URL = "http://172.17.3.196:521577/";
    public static final String BETA_UPLOAD_URL = "http://172.17.3.196:521577/";
    public static final String BETA_SHARE_URL = BETA_UPLOAD_URL;

    // 线上环境
    public static final String ONLINE_BASE_URL = "http://vmnote.melove.net/api/v1/";
    public static final String ONLINE_UPLOAD_URL = "http://vmnote.melove.net";
    public static final String ONLINE_SHARE_URL = ONLINE_UPLOAD_URL;
    // 图片缓存文件夹
    public static final String CACHE_IMAGES = "images";

    public static final String NAV_EXT = "nav_ext";

    public static final String TEMP_PHOTO = "temp.jpg";

    public static final int PHOTO_REQUEST_CAMERA = 1;
    public static final int PHOTO_REQUEST_GALLERY = 2;
    public static final int PHOTO_REQUEST_CLIP = 3;

}
