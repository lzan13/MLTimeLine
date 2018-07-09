package com.vmloft.develop.app.timeline.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vmloft.develop.app.timeline.R;

/**
 * 自定义通用的 TopBar 控件
 */
public class VMTopBar extends RelativeLayout {

    private LayoutInflater inflater;

    private ImageButton leftBtn;
    private ImageButton rightBtn;
    private ImageButton moreBtn;
    private View spaceView;
    private TextView titleView;

    public VMTopBar(Context context) {
        this(context, null);
    }

    public VMTopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化控件
     */
    private void init(Context context) {
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.vm_widget_top_bar, this);

        leftBtn = findViewById(R.id.vm_btn_left);
        rightBtn = findViewById(R.id.vm_btn_right);
        moreBtn = findViewById(R.id.vm_btn_more);
        spaceView = findViewById(R.id.vm_view_space);
        titleView = findViewById(R.id.vm_text_title);
    }

    /**
     * 设置返回按钮资源
     */
    public void setLeftIcon(int resId) {
        leftBtn.setImageResource(resId);
        leftBtn.setVisibility(VISIBLE);
        spaceView.setVisibility(GONE);
    }

    /**
     * 设置返回按钮资源
     */
    public void setRightIcon(int resId) {
        rightBtn.setImageResource(resId);
        rightBtn.setVisibility(VISIBLE);
    }

    /**
     * 设置更多按钮的资源图片
     */
    public void setMoreIcon(int resId) {
        moreBtn.setImageResource(resId);
        moreBtn.setVisibility(VISIBLE);
    }

    /**
     * 设置 title 内容，可以是资源 Id
     */
    public void setTitle(int resId) {
        titleView.setText(resId);
    }

    /**
     * 也可以是字符串
     */
    public void setTitle(String str) {
        titleView.setText(str);
    }

    /**
     * 设置左侧按钮点击监听
     */
    public void setLeftOnClick(OnClickListener listener) {
        spaceView.setVisibility(GONE);
        leftBtn.setVisibility(VISIBLE);
        leftBtn.setOnClickListener(listener);
    }

    /**
     * 设置右侧按钮点击监听
     */
    public void setRightOnClick(OnClickListener listener) {
        rightBtn.setVisibility(VISIBLE);
        rightBtn.setOnClickListener(listener);
    }

    /**
     * 设置更多按钮点击监听
     */
    public void setMoreOnClick(OnClickListener listener) {
        moreBtn.setVisibility(VISIBLE);
        moreBtn.setOnClickListener(listener);
    }
}
