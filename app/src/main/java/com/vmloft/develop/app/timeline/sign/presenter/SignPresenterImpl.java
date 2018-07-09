package com.vmloft.develop.app.timeline.sign.presenter;

import com.vmloft.develop.app.timeline.common.CResult;
import com.vmloft.develop.app.timeline.common.Callback;
import com.vmloft.develop.app.timeline.sign.SignContract.ISignModel;
import com.vmloft.develop.app.timeline.sign.SignContract.ISignPresenter;
import com.vmloft.develop.app.timeline.sign.SignContract.ISignView;
import com.vmloft.develop.app.timeline.sign.model.SignModelImpl;

/**
 * Created by lzan13 on 2017/11/23.
 * 登录处理实现
 */
public class SignPresenterImpl extends ISignPresenter<ISignView> {

    private ISignModel signModel;

    public SignPresenterImpl() {
        signModel = new SignModelImpl();
    }

    /**
     * 账户注册
     */
    @Override
    public void doSignUp(String username, String email, String password) {
        signModel.createAccount(username, email, password, new Callback() {
            @Override
            public void onDone(Object object) {
                obtainView().signUpResult(new CResult());
            }

            @Override
            public void onError(int code, String desc) {
                obtainView().signUpResult(new CResult(code, desc));
            }
        });
    }

    /**
     * 处理登录操作
     */
    @Override
    public void doSignIn(String username, String password) {
        signModel.authAccount(username, password, new Callback() {
            @Override
            public void onDone(Object object) {
                obtainView().signInResult(new CResult());
            }

            @Override
            public void onError(int code, String desc) {
                obtainView().signInResult(new CResult(code, desc));
            }
        });
    }
}
