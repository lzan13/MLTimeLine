package com.vmloft.develop.app.timeline;

import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import com.vmloft.develop.app.timeline.common.C;
import com.vmloft.develop.app.timeline.common.bean.Account;
import com.vmloft.develop.app.timeline.common.bean.Moment;
import com.vmloft.develop.library.tools.VMApp;
import com.vmloft.develop.library.tools.VMTools;

/**
 * Created by lzan13 on 2015/3/25.
 */
public class App extends VMApp {

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        initTools();
        initBaasSDK();
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 初始化自己的工具库
     */
    private void initTools() {
        VMTools.init(context);
    }

    /**
     * 初始化第三方 Baas 服务 SDK
     */
    private void initBaasSDK() {
        // 注册子类
        AVObject.registerSubclass(Moment.class);
        AVUser.registerSubclass(Account.class);
        AVUser.alwaysUseSubUserClass(Account.class);
        // 初始化 sdk
        AVOSCloud.initialize(context, C.LC_APP_ID, C.LC_APP_KEY);
        AVOSCloud.setDebugLogEnabled(true);
    }
}
