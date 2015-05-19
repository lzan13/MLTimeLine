package net.melove.app.ml.utils;

import android.content.Intent;
import android.net.Uri;

import net.melove.app.ml.MLApp;

import java.io.File;

/**
 * Created by Administrator on 2014/12/17.
 */
public class MLUtil {

    /**
     * 调用系统分享功能分享图片
     *
     * @param imagePath
     * @param shareMsg
     */
    public static void gameShareImage(String imagePath, String shareMsg) {
        Uri imageUri = Uri.fromFile(new File(imagePath));
        //调用更多分享
        Intent sharedIntent = new Intent();
        sharedIntent.setType("image/*");
        sharedIntent.setAction(Intent.ACTION_SEND);
        sharedIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sharedIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
        sharedIntent.putExtra(Intent.EXTRA_TITLE, "选择分享");

        MLApp.getContext().startActivity(Intent.createChooser(sharedIntent, "选择分享"));
    }

}
