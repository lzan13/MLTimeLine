package net.melove.app.ml.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import net.melove.app.ml.MLApplication;
import net.melove.app.ml.R;
import net.melove.app.ml.constants.MLAppConstant;

/**
 * Created by lzan13 on 2015/5/11.
 */
public class MLImageCompress {
    private static final String CONTENT = "content";
    private static final String FILE = "file";

    private static int maxWidth = 1200;
    private static int maxHeight = 1920;

    /**
     * 压缩图片 通过Uri
     *
     * @param uri
     * @return
     */
    public static Bitmap compressFromUri(Uri uri) {
        String filepath = getFilePath(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设为true了
        // 这个参数的意义是仅仅解析边缘区域，从而可以得到图片的一些信息，比如大小，而不会整个解析图片，防止OOM
        options.inJustDecodeBounds = true;

        // 此时bitmap还是为空的
        Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);

        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;

        // 根据宽高计算缩放比例
        int scale = getZoomScale(actualWidth, actualHeight);
        if (scale <= 0) {
            scale = 1;
        }
        MLLog.d("scale " + scale);
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeFile(filepath, options);
        MLFile.saveBitmapToSDCard(bitmap, MLApplication.getTemp() + MLAppConstant.ML_TEMP_PHOTO);
        return getThumbnail(bitmap);
    }

    /**
     * 获取bitmap的缩略图
     *
     * @param bitmap
     * @return
     */
    private static Bitmap getThumbnail(Bitmap bitmap) {
        int thumbnailWidth = MLApplication.getContext().getResources().getDimensionPixelSize(R.dimen.ml_dimen_72);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale = 0.5f;
        if (w > h) {
            scale = (float) thumbnailWidth / w;
        } else {
            scale = (float) thumbnailWidth / h;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return result;
    }

    /**
     * 获取最佳缩放比例
     *
     * @param actualWidth
     * @param actualHeight
     * @return
     */
    private static int getZoomScale(int actualWidth, int actualHeight) {
        float scale = 1;
        if (actualWidth > actualHeight) {
            float ws = actualWidth / maxHeight;
            float hs = actualHeight / maxWidth;
            scale = ((ws + hs) / 2);
        } else {
            float ws = actualWidth / maxWidth;
            float hs = actualHeight / maxHeight;
            scale = ((ws + hs) / 2);
        }
        if (scale % 2 > 0.4) {
            scale += 1;
        }
        return (int) scale;
    }

    /**
     * 根据选择图片的Uri获取 图片的文件真实路径
     *
     * @param uri
     * @return
     */
    private static String getFilePath(Uri uri) {
        String path = null;
        if (CONTENT.equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = MLApplication.getContext().getContentResolver().query(uri,
                    new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor == null) {
                return null;
            }
            if (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

        }
        if (FILE.equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        return path;
    }


}
