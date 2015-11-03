package net.melove.app.ml.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import net.melove.app.ml.MLApplication;
import net.melove.app.ml.R;


/**
 * Created by Administrator on 2015/4/1.
 */
public class MLToast {

    private boolean isShow;

    private Context mContext = MLApplication.getContext();

    private WindowManager mWindowManager;
    private View mToastView;
    private WindowManager.LayoutParams mParams;

    private MLToast(int id, String text) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mToastView = inflater.inflate(R.layout.ml_toast_layout, null);
        ImageView imgView = (ImageView) mToastView.findViewById(R.id.ml_img_toast_icon);
        imgView.setImageResource(id);

        TextView textView = (TextView) mToastView.findViewById(R.id.ml_text_toast_text);
        textView.setText(text);

        // (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        isShow = false;
        setParams();
    }

    private void setParams() {
        mParams = new WindowManager.LayoutParams();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // MLToast 的进入和退出动画效果
        mParams.windowAnimations = R.style.ml_toast_anim;

        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.x = MLApplication.getContext().getResources().getDimensionPixelSize(R.dimen.ml_dimen_16);
        mParams.y = MLApplication.getContext().getResources().getDimensionPixelSize(R.dimen.ml_dimen_72);

    }

    public static MLToast makeToast(int id, String text) {
        MLToast toast = new MLToast(id, text);
        return toast;
    }

    public void show() {
        if (!isShow) {
            isShow = true;
            mWindowManager.addView(mToastView, mParams);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mWindowManager.removeView(mToastView);
                        isShow = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 3500);
        }
    }
}
