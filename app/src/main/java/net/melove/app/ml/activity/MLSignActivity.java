package net.melove.app.ml.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import net.melove.app.ml.R;
import net.melove.app.ml.fragment.MLBaseFragment;
import net.melove.app.ml.fragment.MLForgetPassFragment;
import net.melove.app.ml.fragment.MLSigninFragment;
import net.melove.app.ml.fragment.MLSignupFragment;
import net.melove.app.ml.utils.MLBlur;

/**
 * Created by Administrator on 2015/3/24.
 */
public class MLSignActivity extends MLBaseActivity implements MLBaseFragment.MLFragmentCallback {

    private Activity mActivity;

    private ImageView mBackgroundImageView;

    private MLSigninFragment mlSigninFragment;
    private MLSignupFragment mlSignupFragment;
    private MLForgetPassFragment mlForgetPassFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ml_sign_layout);

        mActivity = this;

        initView();
        applyBlur();

        initFragment();

    }

    private void initView() {
        mBackgroundImageView = (ImageView) findViewById(R.id.ml_img_sign_bg);

    }

    private void initFragment(){
        mlSigninFragment = new MLSigninFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.ml_fragment_container, mlSigninFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }


    /**
     * 通过实现Fragment接口中的回调函数，来实现Fragment与activity的交互
     * @param i
     */
    @Override
    public void mlClickListener(int i) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (i) {
            case R.id.ml_btn_goto_signup:
                mlSignupFragment = new MLSignupFragment();
                ft.setCustomAnimations(R.animator.ml_fragment_top_in_animator, R.animator.ml_fragment_down_out_animator);
                ft.replace(R.id.ml_fragment_container, mlSignupFragment);
                break;
            case R.id.ml_btn_cancel_goto_signin:
                mlSigninFragment = new MLSigninFragment();
                ft.setCustomAnimations(R.animator.ml_fragment_down_in_animator, R.animator.ml_fragment_top_out_animator);
                ft.replace(R.id.ml_fragment_container, mlSigninFragment);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                break;
            case R.id.ml_btn_forget_pass:
                mlForgetPassFragment = new MLForgetPassFragment();
                ft.setCustomAnimations(R.animator.ml_fragment_down_in_animator, R.animator.ml_fragment_top_out_animator, 0, 0);
                ft.replace(R.id.ml_fragment_container, mlForgetPassFragment);
                break;
            case R.id.ml_btn_find_pass_goto_signin:
                mlSigninFragment = new MLSigninFragment();
                ft.setCustomAnimations(R.animator.ml_fragment_top_in_animator, R.animator.ml_fragment_down_out_animator, 0, 0);
                ft.replace(R.id.ml_fragment_container, mlSigninFragment);
                break;
        }
        ft.commit();
    }

    /**
     * 设置模糊背景
     */
    private void applyBlur() {
        mBackgroundImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mBackgroundImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                mBackgroundImageView.buildDrawingCache();
                Bitmap bitmap = mBackgroundImageView.getDrawingCache();
                MLBlur.MLBlurImage(mActivity, bitmap, mBackgroundImageView, 2.0f, 10.0f);
                return false;
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
