package com.vmloft.develop.app.timeline.sign;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.app.timeline.common.base.AppFragment;

/**
 * Created by Administrator on 2015/4/18.
 */
public class ForgetFragment extends AppFragment {

    private FragmentListener listener;


    /**
     * 创建实例对象的工厂方法
     */
    public static ForgetFragment newInstance() {
        ForgetFragment fragment = new ForgetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override protected int initLayoutId() {
        return R.layout.fragment_forget_password;
    }

    @Override protected void initView() {
        ButterKnife.bind(this, getView());
    }

    @Override protected void initData() {

    }

    @OnClick({ R.id.btn_find_password, R.id.btn_back_sign_in }) public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_find_password:
            break;
        case R.id.btn_back_sign_in:
            listener.onAction(view.getId(), null);
            break;
        }
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentListener) context;
    }
}
