package net.melove.app.ml.db;

/**
 * Created by Administrator on 2015/3/24.
 */
public class MLDBConstants {

    public static final String DB_NAME = "ml_app_timeline.db";

    //table name
    public static final String TB_USER = "user";
    public static final String TB_NOTE = "note";
    public static final String TB_REPLY = "reply";
    public static final String TB_MESSAGE = "message";

    // column name
    public static final String COL_LOVE_ID = "love_id";
    public static final String COL_SPOUSE_ID = "spouse_id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_NOTE_ID = "note_id";
    public static final String COL_REPLY_ID = "reply_id";
    public static final String COL_MESSAGE_ID = "message_id";
    public static final String COL_NICKNAME = "nickname";
    public static final String COL_SIGNINNAME = "signinname";
    public static final String COL_EMAIL = "email";
    public static final String COL_AVATAR = "avatar";
    public static final String COL_COVER = "cover";
    public static final String COL_GENDER = "gender";
    public static final String COL_LOCATION = "location";
    public static final String COL_SIGNATURE = "signature";
    public static final String COL_NOTE_COUNT = "note_count";
    public static final String COL_REPLY_COUNT = "reply_count";
    public static final String COL_ACCESS_TOKEN = "access_token";
    public static final String COL_SEND_USER_ID = "send_user_id";
    public static final String COL_RECEIVE_USER_ID = "receive_user_id";
    public static final String COL_NOTE_TYPE = "note_type";
    public static final String COL_MESSAGE_TYPE = "message_type";
    public static final String COL_STATE = "state";
    public static final String COL_IMAGE = "image";
    public static final String COL_CONTENT = "content";
    public static final String COL_CREATE_AT = "create_at";
    public static final String COL_UPDATE_AT = "update_at";

    /**
     * 创建数据表 sql 语句
     */
    /*创建 Message 表*/
    public static final String CREATE_TABLE_MESSAGE = "create table if not exists "
            + TB_MESSAGE + " ("
            + COL_SEND_USER_ID + " varchar, "
            + COL_RECEIVE_USER_ID + " varchar, "
            + COL_MESSAGE_ID + " varchar unique, "
            + COL_MESSAGE_TYPE + " varchar, "
            + COL_CONTENT + " text, "
            + COL_STATE + " integer, "
            + COL_CREATE_AT + " varchar" + " )";

    /*创建 Note 表*/
    public static final String CREATE_TABLE_NOTE = "create table if not exists "
            + TB_NOTE + " ("
            + COL_LOVE_ID + " varchar not null, "
            + COL_USER_ID + " varchar not null, "
            + COL_NOTE_ID + " varchar not null unique, "
            + COL_NOTE_TYPE + " varchar, "
            + COL_IMAGE + " image_url, "
            + COL_CONTENT + " text, "
            + COL_CREATE_AT + " varchar" + " )";

    /*创建 Reply 表*/
    public static final String CREATE_TABLE_REPLY = "create table if not exists "
            + TB_REPLY + " ("
            + COL_USER_ID + " varchar, "
            + COL_NOTE_ID + " varchar, "
            + COL_REPLY_ID + " varchar unique, "
            + COL_CONTENT + " text, "
            + COL_CREATE_AT + " varchar" + " )";

    /*创建 User 表*/
    public static final String CREATE_TABLE_USER = "create table if not exists "
            + TB_USER + " ("
            + COL_LOVE_ID + " varchar, "
            + COL_SPOUSE_ID + " varchar, "
            + COL_USER_ID + " varchar not null unique, "
            + COL_SIGNINNAME + " varchar unique, "
            + COL_EMAIL + " varchar not null unique, "
            + COL_NICKNAME + " varchar, "
            + COL_AVATAR + " text, "
            + COL_COVER + " text, "
            + COL_GENDER + " integer, "
            + COL_LOCATION + " text, "
            + COL_SIGNATURE + " text, "
            + COL_NOTE_COUNT + " integer, "
            + COL_REPLY_COUNT + " integer, "
            + COL_CREATE_AT + " varchar, "
            + COL_UPDATE_AT + " varchar, "
            + COL_ACCESS_TOKEN + " varchar" + " )";

}
