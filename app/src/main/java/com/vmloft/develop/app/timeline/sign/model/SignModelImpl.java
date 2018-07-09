package com.vmloft.develop.app.timeline.sign.model;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.vmloft.develop.app.timeline.common.bean.Account;
import com.vmloft.develop.app.timeline.common.Callback;
import com.vmloft.develop.app.timeline.common.SPManager;
import com.vmloft.develop.app.timeline.sign.SignContract;
import com.vmloft.develop.library.tools.utils.VMLog;

/**
 * Created by lzan13 on 2017/11/23.
 * 登录相关数据处理实现
 */
public class SignModelImpl implements SignContract.ISignModel {

    /**
     * 创建账户
     */
    @Override
    public void createAccount(String username, String email, String password, final Callback callback) {
        Account account = new Account();
        account.setUsername(username);
        account.setEmail(email);
        account.setPassword(password);
        account.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    VMLog.i("账户注册成功");
                    callback.onDone(null);
                } else {
                    VMLog.i("账户注册失败 %d, %s", e.getCode(), e.getMessage());
                    callback.onError(e.getCode(), e.getMessage());
                }
            }
        });
    }

    /**
     * 认证账户
     */
    @Override
    public void authAccount(String username, String password, final Callback callback) {
        Account.logInInBackground(username, password, new LogInCallback<Account>() {
            @Override
            public void done(Account account, AVException e) {
                if (e == null) {
                    SPManager.getInstance().putToken(account.getSessionToken());
                    callback.onDone(null);
                } else {
                    callback.onError(e.getCode(), e.getMessage());
                }
            }
        }, Account.class);
    }
}
