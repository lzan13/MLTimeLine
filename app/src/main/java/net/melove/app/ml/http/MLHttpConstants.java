package net.melove.app.ml.http;

/**
 * Created by Administrator on 2015/3/24.
 */
public class MLHttpConstants {


    public static final String UPLOAD_URL = "http://ml.melove.net/upload/";
    public static final String IMAGE_URL = "images/";
    public static final String VIDEO_URL = "videos/";

    // ML timeline api
    public static final String API_URL = "http://ml.melove.net/api/v1";
    public static final String API_SIGNIN = "/signin";
    public static final String API_SIGNUP = "/signup";

    public static final String API_USER = "/user";
    public static final String API_USER_SETINFO = "/user/setinfo";
    public static final String API_USER_SETPASSWORD = "/user/setpassword";
    public static final String API_USER_SETCOVER = "/user/setcover";
    public static final String API_USER_SETAVATAR = "/user/setavatar";
    public static final String API_USER_SETSPOUSE = "/user/setspouse";

    public static final String API_NOTE = "/note";
    public static final String API_NOTE_NEWEST = "/note/newest";
    public static final String API_NOTE_PUT_TEXT = "/note/puttext";
    public static final String API_NOTE_PUT_IMAGE = "/note/putimage";

    public static final String API_MESSAGE = "/message";

    public static final String API_REPLY = "/reply";

    /*Handler状态信息*/
    public static final int WHAT_SUCCESS = 0;
    public static final int WHAT_FAILURE = 1;
    public static final int WAHT_START = 2;
    public static final int WHAT_FINISH = 3;

    /*超时时间*/
    public static final int HTTP_TIME_OUT = 5 * 1000;
    public static final int POOL_TIME_OUT = 2 * 1000;

    /*最大链接数*/
    public static final int MAX_CONNECTIONS = 10;

    /*socket数据buffer缓冲大小*/
    public static final int SOCKER_BUFFER_SIZE = 10 * 1024 * 1024;
}
