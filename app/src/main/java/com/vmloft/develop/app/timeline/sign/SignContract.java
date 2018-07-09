package com.vmloft.develop.app.timeline.sign;

import com.vmloft.develop.app.timeline.common.base.BPresenter;
import com.vmloft.develop.app.timeline.common.CResult;
import com.vmloft.develop.app.timeline.common.Callback;

/**
 * Created by lzan13 on 2018/4/26.
 * 登录、注册相关接口定义契约类
 */
public final class SignContract {

    private SignContract() {}

    public interface ISignModel {

        /**
         * 创建账户
         */
        void createAccount(String username, String email, String password, Callback callback);

        /**
         * 认证账户，主要是登录获取 token
         */
        void authAccount(String username, String password, Callback callback);
    }

    public interface ISignView {

        /**
         * 账户注册结果
         */
        void signUpResult(CResult result);

        /**
         * 登录结果
         */
        void signInResult(CResult result);
    }

    public static abstract class ISignPresenter<V> extends BPresenter<V> {

        /**
         * 账户注册
         */
        public abstract void doSignUp(String username, String email, String password);

        /**
         * 账户登录
         */
        public abstract void doSignIn(String username, String password);
    }
}
