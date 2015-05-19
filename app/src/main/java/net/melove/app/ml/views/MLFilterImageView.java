package net.melove.app.ml.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import net.melove.app.ml.R;

/**
 * Created by Administrator on 2015/5/11.
 */
public class MLFilterImageView extends ImageView {
    private Paint mPressPaint;

    private int mFilterColor;

    private int mPressAlpha;
    private int mPressColor;

    public MLFilterImageView(Context context) {
        super(context);
        init(context, null);
    }

    public MLFilterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){
        //初始化默认值
        mPressAlpha = 45;
        mPressColor = getResources().getColor(R.color.ml_gray);
        mFilterColor = getResources().getColor(R.color.ml_transparent);

        // 获取控件的属性值
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MLCustomView);
            mPressAlpha = array.getInteger(R.styleable.MLCustomView_press_alpha, mPressAlpha);
            mPressColor = array.getColor(R.styleable.MLCustomView_press_color, mPressColor);
            mFilterColor = array.getColor(R.styleable.MLCustomView_filter_color, mFilterColor);
            array.recycle();
        }
        // 按下的画笔设置
        mPressPaint = new Paint();
        mPressPaint.setAntiAlias(true);
        mPressPaint.setStyle(Paint.Style.FILL);
        mPressPaint.setColor(mPressColor);
        mPressPaint.setAlpha(mPressAlpha);
        mPressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        setClickable(true);
        setDrawingCacheEnabled(true);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFilter(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPressPaint.setAlpha(mPressAlpha);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mPressPaint.setAlpha(0);
                invalidate();
                break;
            default:
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置滤镜
     */
    private void drawFilter(Canvas canvas) {
        //先获取设置的src图片
        Drawable drawable = getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            //设置滤镜
            drawable.setColorFilter(mFilterColor, PorterDuff.Mode.MULTIPLY);
        }
    }

    /**
     * 清除滤镜
     */
    private void removeFilter() {
        //先获取设置的src图片
        Drawable drawable = getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            //清除滤镜
            drawable.clearColorFilter();
        }
    }
}
