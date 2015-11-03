package net.melove.app.ml.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.melove.app.ml.MLApplication;
import net.melove.app.ml.R;
import net.melove.app.ml.activity.MLMainActivity;
import net.melove.app.ml.config.MLConfig;
import net.melove.app.ml.db.MLDBConstants;
import net.melove.app.ml.db.MLDBHelper;
import net.melove.app.ml.http.MLHttpConstants;
import net.melove.app.ml.http.MLHttpUtil;
import net.melove.app.ml.http.MLRequestParams;
import net.melove.app.ml.http.MLStringResponseListener;
import net.melove.app.ml.info.UserInfo;
import net.melove.app.ml.utils.MLCrypto;
import net.melove.app.ml.utils.MLLog;
import net.melove.app.ml.utils.MLSPUtil;
import net.melove.app.ml.views.MLToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/4/17.
 */
public class MLSignupFragment extends MLBaseFragment {
    private Activity mActivity;
    private MLFragmentCallback mlCallback;

    private EditText mNameEdit;
    private EditText mEmailEdit;
    private EditText mPasswordEdit;
    private EditText mConfirmPasswordEdit;
    private Button mSignupBtn;
    private Button mCancelGotoSigninBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();

        View view = inflater.inflate(R.layout.ml_signup_fragment_layout, container, false);
        initView(view);


        return view;
    }

    /**
     * 初始化Fragment 界面
     *
     * @param view
     */
    private void initView(View view) {
        mNameEdit = (EditText) view.findViewById(R.id.ml_edit_signinname);
        mEmailEdit = (EditText) view.findViewById(R.id.ml_edit_email);
        mPasswordEdit = (EditText) view.findViewById(R.id.ml_edit_password1);
        mConfirmPasswordEdit = (EditText) view.findViewById(R.id.ml_edit_confirm_password);

        mSignupBtn = (Button) view.findViewById(R.id.ml_btn_signup);
        mCancelGotoSigninBtn = (Button) view.findViewById(R.id.ml_btn_cancel_goto_signin);
        mSignupBtn.setOnClickListener(listener);
        mCancelGotoSigninBtn.setOnClickListener(listener);


    }

    /**
     * 点击注册按钮触发注册事件
     */
    private void userSignup() {
        Resources res = mActivity.getResources();
        String name = mNameEdit.getText().toString();
        String email = mEmailEdit.getText().toString();
        String password = mPasswordEdit.getText().toString();
        String confirmPassword = mConfirmPasswordEdit.getText().toString();
        if (name.equals("") || name.equals(null)) {
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_toast_name_not_null)).show();
            return;
        }
        if (email.equals("") || email.equals(null)) {
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_toast_email_not_null)).show();
            return;
        }
        if (password.equals("") || password.equals(null)) {
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_toast_password_not_null)).show();
            return;
        }
        if (confirmPassword.equals("") || confirmPassword.equals(null)) {
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_toast_confirm_password_null)).show();
            return;
        }
        if (!confirmPassword.equals(password)) {
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_toast_confirm_password_null)).show();
            return;
        }
        String hexPass = MLCrypto.cryptoStr2MD5(password);

        MLRequestParams params = new MLRequestParams();
        params.putParams("signinname", name);
        params.putParams("password", hexPass);
        params.putParams("email", email);
        MLHttpUtil.getInstance(mActivity).post(MLHttpConstants.API_URL + MLHttpConstants.API_SIGNUP,
                params, new MLStringResponseListener() {
                    // 获取返回数据成功，接下来进一步解析数据
                    @Override
                    public void onSuccess(int state, String content) {
                        parseJsonAndSave(content);
                    }

                    // 获取返回数据失败，一般是网络超时导致（或者无法连接服务器）
                    @Override
                    public void onFailure(int state, String content) {
                        signupToEnd(state);
                    }
                });
        mNameEdit.setEnabled(false);
        mEmailEdit.setEnabled(false);
        mPasswordEdit.setEnabled(false);
        mConfirmPasswordEdit.setEnabled(false);
        mSignupBtn.setEnabled(false);
        mCancelGotoSigninBtn.setEnabled(false);
        MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, res.getString(R.string.ml_signup_loading)).show();
    }

    /**
     * 解析注册返回的数据
     *
     * @param jsonStr
     */
    private void parseJsonAndSave(String jsonStr) {
        MLLog.i(jsonStr);
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            // 判断json中是否包含某个特定的KEY
            if (jsonObject.isNull("error")) {
                JSONObject user = jsonObject.getJSONObject("user");
                UserInfo userInfo = new UserInfo(user);

                // 首先保存access_token 和signinname 为以后做准备
                MLSPUtil.put(mActivity, MLDBConstants.COL_LOVE_ID, userInfo.getLoveId());
                MLSPUtil.put(mActivity, MLDBConstants.COL_ACCESS_TOKEN, userInfo.getAccessToken());
                MLSPUtil.put(mActivity, MLDBConstants.COL_SIGNINNAME, userInfo.getSigninname());

                // 登录成功，初始化当前登录账户目录以及数据库
                MLApplication.setUserPath(userInfo.getSigninname());
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

                signupToEnd(0);
            } else {
                String error = jsonObject.getString("error");
                if (error.equals("user")) {
                    signupToEnd(1);
                } else {
                    signupToEnd(-1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            signupToEnd(-1);
        }
    }

    /**
     * 注册事件结束
     *
     * @param i
     */
    private void signupToEnd(int i) {
        mNameEdit.setEnabled(true);
        mEmailEdit.setEnabled(true);
        mPasswordEdit.setEnabled(true);
        mConfirmPasswordEdit.setEnabled(true);
        mSignupBtn.setEnabled(true);
        mCancelGotoSigninBtn.setEnabled(true);
        Resources res = mActivity.getResources();
        switch (i) {
            case 0:
                MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, res.getString(R.string.ml_toast_signup_success)).show();
                break;
            case 1:
                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, res.getString(R.string.ml_toast_name_exist)).show();
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
     * 注册界面onclick事件
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ml_btn_signup) {
                userSignup();
            }
            if (v.getId() == R.id.ml_btn_cancel_goto_signin) {
                mlCallback.mlClickListener(v.getId());
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mlCallback = (MLFragmentCallback) activity;
    }
}
