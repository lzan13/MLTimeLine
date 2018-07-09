package com.vmloft.develop.app.timeline.home.event;

import android.os.Bundle;
import butterknife.ButterKnife;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.app.timeline.common.base.AppFragment;

/**
 * Created by lzan13 on 2015/5/20.
 */
public class EventFragment extends AppFragment {

    /**
     * Fragment 工厂方法
     */
    public static EventFragment newInstance() {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化 Fragment 界面 layout_id
     *
     * @return 返回布局 id
     */
    @Override
    protected int initLayoutId() {
        return R.layout.fragment_event;
    }

    /**
     * 初始化界面控件，将 Fragment 变量和 View 建立起映射关系
     */
    @Override
    protected void initView() {
        ButterKnife.bind(this, getView());

    }

    /**
     * 加载数据
     */
    @Override
    protected void initData() {

    }
}
