package net.melove.app.ml.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;

import net.melove.app.ml.MLApp;
import net.melove.app.ml.R;
import net.melove.app.ml.activity.MLMainActivity;
import net.melove.app.ml.activity.MLUserActivity;
import net.melove.app.ml.config.MLConfig;
import net.melove.app.ml.db.MLDBConstants;
import net.melove.app.ml.db.MLDBHelper;
import net.melove.app.ml.http.MLHttpConstants;
import net.melove.app.ml.http.MLHttpUtil;
import net.melove.app.ml.http.MLRequestParams;
import net.melove.app.ml.http.MLStringResponseListener;
import net.melove.app.ml.info.UserInfo;
import net.melove.app.ml.utils.MLCrypto;
import net.melove.app.ml.utils.MLSPUtil;
import net.melove.app.ml.views.MLToast;
import net.melove.app.ml.views.MLImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/4/17.
 */
public class MLSigninFragment extends MLBaseFragment {

    private Activity mActivity;
    private MLFragmentCallback mlCallback;

    private MLImageView mAvatarImageView;

    private EditText mNameEdit;
    private EditText mPassEdit;

    private Button mSigninBtn;
    private Button mGotoSignupBtn;
    private Button mForgotPassBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();

        View view = inflater.inflate(R.layout.ml_signin_fragment_layout, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        mAvatarImageView = (MLImageView) view.findViewById(R.id.ml_img_signin_avatar);
        mNameEdit = (EditText) view.findViewById(R.id.ml_edit_signinname);
        mPassEdit = (EditText) view.findViewById(R.id.ml_edit_password);

        mSigninBtn = (Button) view.findViewById(R.id.ml_btn_signin);
        mGotoSignupBtn = (Button) view.findViewById(R.id.ml_btn_goto_signup);
        mForgotPassBtn = (Button) view.findViewById(R.id.ml_btn_forget_pass);
        mSigninBtn.setOnClickListener(listener);
        mGotoSignupBtn.setOnClickListener(listener);
        mForgotPassBtn.setOnClickListener(listener);

    }

    /**
     * 点击登录按钮，触发登录事件
     */
    private void userSignin() {
        Resources res = mActivity.getResources();

        String name = mNameEdit.getText().toString();
        String pass = mPassEdit.getText().toString();
        if (name.equals(null) || name.equals("")) {
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_toast_name_not_null)).show();
            return;
        }
        if (pass.equals(null) || pass.equals("")) {
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_toast_password_not_null)).show();
            return;
        }
        String hexpass = MLCrypto.cryptoStr2MD5(pass);

        MLRequestParams params = new MLRequestParams();
        params.putParams("signinname", name);
        params.putParams("password", hexpass);
        MLHttpUtil.getInstance(mActivity).post(MLHttpConstants.URL + MLHttpConstants.API_SIGNIN,
                params, new MLStringResponseListener() {
                    // 获取网络请求成功，登录却不一定成功，需要进一步解析数据
                    @Override
                    public void onSuccess(int state, String content) {
                        parseSigninInfo(content);
                    }

                    // 登录失败（一般是网络请求错误会执行到这里）
                    @Override
                    public void onFailure(int state, String content) {
                        signinToEnd(state);
                    }
                });
        // 开始进行登录请求，开始动画
        Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.ml_signin_avatar_rotate);
        anim.setInterpolator(new LinearInterpolator());
        mAvatarImageView.startAnimation(anim);

        mNameEdit.setEnabled(false);
        mPassEdit.setEnabled(false);
        mSigninBtn.setEnabled(false);
        mGotoSignupBtn.setEnabled(false);
        mForgotPassBtn.setEnabled(false);
        MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, res.getString(R.string.ml_signin_loading)).show();
    }

    /**
     * 解析接收到登录返回json数据，如果登录成功，就保存用户信息
     *
     * @param jsonStr
     */
    private void parseSigninInfo(String jsonStr) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            // 判断json中是否包含某个特定的KEY
            if (jsonObject.isNull("error")) {
                JSONObject user = jsonObject.getJSONObject("user");
                UserInfo userInfo = new UserInfo(user);

                // 首先保存access_token 和signinname 为以后做准备
                MLSPUtil.put(mActivity, MLDBConstants.COL_LOVE_ID, userInfo.getLoveId());
                MLSPUtil.put(mActivity, MLDBConstants.COL_ACCESS_TOKEN, userInfo.getAccessToken());
                MLSPUtil.put(mActivity, MLDBConstants.COL_SIGNINNAME, userInfo.getSigninname());

                // 登录成功，初始化当前登录账户目录以及数据库
                MLApp.setUserPath(userInfo.getSigninname());
                MLConfig.initDir();
                MLConfig.initDatabase();

                ContentValues values = new ContentValues();
                values.put(MLDBConstants.COL_LOVE_ID, userInfo.getLoveId());
                values.put(MLDBConstants.COL_SPOUSE_ID, userInfo.getSpouseId());
                values.put(MLDBConstants.COL_USER_ID, userInfo.getUserId());
                values.put(MLDBConstants.COL_SIGNINNAME, userInfo.getSigninname());
                values.put(MLDBConstants.COL_EMAIL, userInfo.getEmail());
                values.put(MLDBConstants.COL_NICKNAME, userInfo.getNickname());
                values.put(MLDBConstants.COL_AVATAR, userInfo.getAvatar());
                values.put(MLDBConstants.COL_COVER, userInfo.getCover());
                values.put(MLDBConstants.COL_GENDER, userInfo.getGender());
                values.put(MLDBConstants.COL_LOCATION, userInfo.getLocation());
                values.put(MLDBConstants.COL_SIGNATURE, userInfo.getSignature());
                values.put(MLDBConstants.COL_NOTE_COUNT, userInfo.getNoteCount());
                values.put(MLDBConstants.COL_REPLY_COUNT, userInfo.getReplyCount());
                values.put(MLDBConstants.COL_CREATE_AT, userInfo.getCreateAt());
                values.put(MLDBConstants.COL_UPDATE_AT, userInfo.getUpdateAt());
                values.put(MLDBConstants.COL_ACCESS_TOKEN, userInfo.getAccessToken());

                MLDBHelper mDBHelper = MLDBHelper.getInstance();
                mDBHelper.insterData(MLDBConstants.TB_USER, values);
                mDBHelper.closeDatabase();

                signinToEnd(0);
            } else {
                String error = jsonObject.getString("error");
                if (error.equals("user")) {
                    signinToEnd(1);
                } else if (error.equals("password")) {
                    signinToEnd(2);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            signinToEnd(-1);
        }
    }

    /**
     * 登录事件结束
     *
     * @param i 登录结束状态
     */
    private void signinToEnd(int i) {
        mNameEdit.setEnabled(true);
        mPassEdit.setEnabled(true);
        mSigninBtn.setEnabled(true);
        mGotoSignupBtn.setEnabled(true);
        mForgotPassBtn.setEnabled(true);
        Resources res = mActivity.getResources();
        // 登录请求结束，关闭动画
        mAvatarImageView.clearAnimation();

        switch (i) {
            case 0:
                MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, res.getString(R.string.ml_toast_signin_success)).show();
                break;
            case 1:
                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_toast_name_nonentity)).show();
                break;
            case 2:
                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_toast_password_error)).show();
                break;
            default:
                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_error_http)).show();
                break;
        }

        if (i == 0) {
            Intent intent = new Intent();
            intent.setClass(mActivity, MLMainActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    /**
     * 控件点击事件监听
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ml_btn_signin) {
                userSignin();
            }
            mlCallback.mlClickListener(v.getId());
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mlCallback = (MLFragmentCallback) activity;
    }
}
