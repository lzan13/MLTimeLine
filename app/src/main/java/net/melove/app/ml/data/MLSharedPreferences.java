package net.melove.app.ml.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import net.melove.app.ml.constants.MLAppConstant;

/**
 * Created by Administrator on 2015/3/24.
 */
public class MLSharedPreferences {

    private Context mContext;

    private static MLSharedPreferences instance;

    private SharedPreferences sp;

    public MLSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new MLSharedPreferences(context);
        }
        return instance;
    }

    public MLSharedPreferences(Context context) {
        mContext = context;
        sp = mContext.getSharedPreferences(MLAppConstant.SHARED_FILE_NAME, 0);
        init();
    }

    private void init() {
        isFirstRun();
    }

    private void isFirstRun() {
        if (sp.getBoolean("first", true)) {
            Editor editor = sp.edit();

            editor.putBoolean("first", false);
            editor.putString("version", "1.0");
            editor.putString("access_token", "");
            editor.putBoolean("auto_refresh", true);

            editor.commit();
        }
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public String getString(String key) {
        return sp.getString(key, "");
    }

    public int getInt(String key) {
        return sp.getInt(key, -1);
    }

    public void putString(String key, String value) {
        sp.edit().putString(key, value);
        sp.edit().commit();
    }


}
