package com.vmloft.develop.app.timeline.home.me;

import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.app.timeline.common.base.AppFragment;
import com.vmloft.develop.app.timeline.common.router.NavRouter;
import com.vmloft.develop.library.tools.widget.VMImageView;

public class MeFragment extends AppFragment {

    @BindView(R.id.img_avatar) VMImageView avatarView;

    /**
     * Fragment 工厂方法
     */
    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
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
        return R.layout.fragment_me;
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

    @OnClick({ R.id.img_avatar })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.img_avatar:
            NavRouter.goAccount(activity);
            break;
        }
    }
}
