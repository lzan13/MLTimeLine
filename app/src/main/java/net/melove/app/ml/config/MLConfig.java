package net.melove.app.ml.config;

import android.content.Context;
import android.content.SharedPreferences;

import net.melove.app.ml.MLApp;
import net.melove.app.ml.R;
import net.melove.app.ml.constants.MLAppConstant;
import net.melove.app.ml.db.MLDBConstants;
import net.melove.app.ml.db.MLDBHelper;
import net.melove.app.ml.utils.MLFile;
import net.melove.app.ml.utils.MLSPUtil;

import java.io.File;

/**
 * Created by Administrator on 2015/3/25.
 */
public class MLConfig {

    /**
     * 检测是否第一次运行，如果是 则初始化APP
     *
     * @param context
     */
    public static void chackFirstRun(Context context) {
        boolean isFirst = (boolean) MLSPUtil.get(context, "first_run", true);
        if (isFirst) {
            MLSPUtil.put(context, "first_run", false);
            MLSPUtil.put(context, "version", context.getResources().getString(R.string.ml_version));
            MLSPUtil.put(context, "love_id", "");
            MLSPUtil.put(context, "access_token", "");
            MLSPUtil.put(context, "signinname", "");
            MLSPUtil.put(context, "auto_refresh", true);
            MLSPUtil.put(context, "update_at", "2015-03-31 12:12:12");
        }
    }

    /**
     * 检测是否已经登录，否则跳转到登录界面
     *
     * @param context
     */
    public static boolean isAccessToken(Context context) {
        String accessToken = (String) MLSPUtil.get(context, MLDBConstants.COL_ACCESS_TOKEN, "");
        if (accessToken.equals("") || accessToken == null) {
            return false;
        }
        String userPath = (String) MLSPUtil.get(context, MLDBConstants.COL_SIGNINNAME, "");
        MLApp.setUserPath(userPath);
        return true;
    }

    /**
     * 登录及注册成功，根据用户账户 创建相对应的文件夹
     */
    public static void initDir() {
        MLFile.createDirectory(MLApp.getAudio());
        MLFile.createDirectory(MLApp.getCache());
        MLFile.createDirectory(MLApp.getDb());
        MLFile.createDirectory(MLApp.getImage());
        MLFile.createDirectory(MLApp.getUserImage());
        MLFile.createDirectory(MLApp.getLogs());
        MLFile.createDirectory(MLApp.getTemp());
        MLFile.createDirectory(MLApp.getUpdate());
        MLFile.createDirectory(MLApp.getVideo());
    }

    public static boolean changeDBExists() {
        File file = new File(MLApp.getDb() + MLDBConstants.DB_NAME);
        if (!file.isFile()) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 数据库的初始化
     */
    public static void initDatabase() {
        MLFile.createFile(MLApp.getDb() + MLDBConstants.DB_NAME);

        // 初始化数据库
        MLDBHelper mDBHelper = MLDBHelper.getInstance();
        mDBHelper.initTable();
    }


    /**
     * 检测是否需要升级
     *
     * @param context
     * @return
     */
    public static boolean checkUpgrade(Context context) {

        return false;
    }

}