package com.vmloft.develop.app.timeline.sign;

import android.support.v4.app.FragmentTransaction;

import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.app.timeline.common.base.AppFragment;
import com.vmloft.develop.app.timeline.common.base.AppMVPActivity;
import com.vmloft.develop.app.timeline.common.CResult;
import com.vmloft.develop.app.timeline.common.router.NavRouter;
import com.vmloft.develop.app.timeline.sign.presenter.SignPresenterImpl;
import com.vmloft.develop.library.tools.VMFragment;
import com.vmloft.develop.app.timeline.sign.SignContract.ISignView;
import com.vmloft.develop.app.timeline.sign.SignContract.ISignPresenter;
import com.vmloft.develop.library.tools.widget.VMToast;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SignActivity extends AppMVPActivity<ISignView, ISignPresenter<ISignView>> implements ISignView, VMFragment.FragmentListener {

    @BindView(R.id.layout_sign_progress) LinearLayout progressView;

    private AppFragment[] fragments;
    private SignUpFragment signUpFragment;
    private SignInFragment signInFragment;
    private ForgetFragment forgetFragment;
    private int currIndex = 1;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(activity);

        initFragment();
    }

    private void initFragment() {
        forgetFragment = ForgetFragment.newInstance();
        signInFragment = SignInFragment.newInstance();
        signUpFragment = SignUpFragment.newInstance();
        fragments = new AppFragment[] { forgetFragment, signInFragment, signUpFragment };

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, forgetFragment);
        ft.add(R.id.fragment_container, signInFragment);
        ft.add(R.id.fragment_container, signUpFragment);
        ft.hide(forgetFragment);
        ft.hide(signUpFragment);
        ft.show(signInFragment);
        ft.commit();
    }

    @Override
    public SignContract.ISignPresenter<SignContract.ISignView> createPresenter() {
        return new SignPresenterImpl();
    }

    /**
     * 通过实现 Fragment 中定义的回调接口，来实现 Fragment 与 Activity 的交互
     */
    @Override
    public void onAction(int action, Object obj) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (action) {
        case R.id.btn_sign_in:
            showDialog(true);
            break;
        case R.id.btn_sign_up:
            showDialog(true);
            break;
        case R.id.btn_go_sign_up:
            ft.setCustomAnimations(R.animator.down_in_animator, R.animator.top_out_animator);
            switchFragment(ft, 2);
            break;
        case R.id.btn_go_sign_in:
            ft.setCustomAnimations(R.animator.top_in_animator, R.animator.down_out_animator);
            switchFragment(ft, 1);
            break;
        case R.id.btn_go_find_password:
            ft.setCustomAnimations(R.animator.top_in_animator, R.animator.down_out_animator);
            switchFragment(ft, 0);
            break;
        case R.id.btn_back_sign_in:
            ft.setCustomAnimations(R.animator.down_in_animator, R.animator.top_out_animator);
            switchFragment(ft, 1);
            break;
        }
    }

    /**
     * 切换 fragment
     */
    private void switchFragment(FragmentTransaction ft, int index) {
        if (!fragments[index].isAdded()) {
            ft.add(R.id.fragment_container, fragments[index]);
        }
        ft.hide(fragments[currIndex]);
        ft.show(fragments[index]);
        ft.commit();
        currIndex = index;
    }

    /**
     * 账户注册结果
     */
    @Override
    public void signUpResult(CResult result) {
        showDialog(false);
        if (result.code == 0) {
            VMToast.make(R.string.sign_up_success).showDone();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.top_in_animator, R.animator.down_out_animator);
            switchFragment(ft, 1);
        } else {
            VMToast.make(result.msg).showError();
        }
    }

    /**
     * 登录结果
     */
    @Override
    public void signInResult(CResult result) {
        showDialog(false);
        if (result.code == 0) {
            VMToast.make(R.string.sign_in_success).showDone();
            NavRouter.goMain(activity);
        } else {
            VMToast.make(result.msg).showError();
        }
    }

    /**
     * 控制进度窗的显示
     */
    private void showDialog(boolean show) {
        if (show) {
            progressView.setVisibility(View.VISIBLE);
        } else {
            progressView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
