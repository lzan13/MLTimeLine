package com.vmloft.develop.app.timeline.sign;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.vmloft.develop.app.timeline.common.base.AppFragment;
import com.vmloft.develop.app.timeline.common.SPManager;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.library.tools.utils.VMReg;
import com.vmloft.develop.library.tools.widget.VMToast;

/**
 * Created by Administrator on 2015/4/17.
 */
public class SignInFragment extends AppFragment {

    @BindView(R.id.edit_email) EditText emailEdit;
    @BindView(R.id.edit_password) EditText passwordEdit;
    @BindView(R.id.btn_clear_input) ImageButton clearInputBtn;
    @BindView(R.id.btn_control_password) ImageButton controlPasswordBtn;
    @BindView(R.id.btn_sign_in) Button signInBtn;

    private SignActivity activity;
    private String email, password;

    private FragmentListener listener;

    /**
     * 创建实例对象的工厂方法
     */
    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_sign_in;
    }

    @Override
    protected void initView() {
        activity = (SignActivity) getActivity();
        ButterKnife.bind(this, getView());
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                verifyInputBox();
            }
        });
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                verifyInputBox();
            }
        });
    }

    /**
     * 加载数据
     */
    @Override
    protected void initData() {
        email = SPManager.getInstance().getAccountName();
        emailEdit.setText(email);
    }

    @OnClick({
        R.id.btn_clear_input, R.id.btn_control_password, R.id.btn_sign_in, R.id.btn_go_find_password,
        R.id.btn_go_sign_up
    })
    void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_clear_input:
            emailEdit.setText("");
            break;
        case R.id.btn_control_password:
            controlPassHide();
            break;
        case R.id.btn_sign_in:
            signIn();
            break;
        case R.id.btn_go_find_password:
        case R.id.btn_go_sign_up:
            listener.onAction(view.getId(), null);
            break;
        }
    }

    /**
     * 登录
     */
    private void signIn() {
        verifyInputBox();
        if (!VMReg.isEmail(email)) {
            VMToast.make(getString(R.string.toast_invalid_phone)).showError();
            return;
        }
        if (!VMReg.isNormalPassword(password)) {
            VMToast.make(getString(R.string.toast_invalid_password)).showError();
            return;
        }
        listener.onAction(R.id.btn_sign_in, null);
        activity.getPresenter().doSignIn(email, password);
    }

    /**
     * 控制密码是否可见
     */
    private void controlPassHide() {
        if (passwordEdit.getTransformationMethod()
            .equals(PasswordTransformationMethod.getInstance())) {
            passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            controlPasswordBtn.setImageResource(R.drawable.ic_visibility);
        } else {
            passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            controlPasswordBtn.setImageResource(R.drawable.ic_visibility_off);
        }
    }

    /**
     * 校验输入框
     */
    private void verifyInputBox() {
        email = emailEdit.getText().toString().toLowerCase().trim();
        password = passwordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            clearInputBtn.setVisibility(View.INVISIBLE);
        } else {
            clearInputBtn.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            signInBtn.setEnabled(false);
            signInBtn.setAlpha(0.5f);
        } else {
            signInBtn.setEnabled(true);
            signInBtn.setAlpha(1.0f);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentListener) context;
    }
}
