package net.melove.app.ml.activity;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import net.melove.app.ml.MLApp;
import net.melove.app.ml.R;
import net.melove.app.ml.constants.MLAppConstant;
import net.melove.app.ml.db.MLDBConstants;
import net.melove.app.ml.db.MLDBHelper;
import net.melove.app.ml.http.MLHttpConstants;
import net.melove.app.ml.http.MLHttpUtil;
import net.melove.app.ml.http.MLImageResponseListener;
import net.melove.app.ml.http.MLRequestParams;
import net.melove.app.ml.http.MLStringResponseListener;
import net.melove.app.ml.info.UserInfo;
import net.melove.app.ml.manager.MLSystemBarManager;
import net.melove.app.ml.utils.MLCrypto;
import net.melove.app.ml.utils.MLFile;
import net.melove.app.ml.utils.MLSPUtil;
import net.melove.app.ml.utils.MLScreen;
import net.melove.app.ml.views.MLToast;
import net.melove.app.ml.views.MLFilterImageView;
import net.melove.app.ml.views.MLImageView;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by lzan13 on 2015/4/23.
 */
public class MLUserActivity extends MLBaseActivity {

    private Activity mActivity;

    private MLSystemBarManager mlManager;
    private Toolbar mToolbar;


    private SwipeRefreshLayout mSwipeRefreshLayout;

    private UserInfo mUserInfo;
    private UserInfo mSpouseInfo;
    private boolean isChange = false;

    private MLFilterImageView mUserCover;
    private MLImageView mUserAvatar;
    private MLImageView mSpouseAvatar;
    private File mTempFile;
    private Uri mTempUri;
    private boolean isAvatar = true;

    private TextView mUserIdView;
    private TextView mAccountView;
    private TextView mEmailView;

    private EditText mNicknameView;
    private EditText mSignatureView;
    private EditText mLocationView;

    private RadioGroup mGenderView;
    private RadioButton mGenderManView;
    private RadioButton mGenderWomanView;

    private TextView mPasswordView;

    private Button mCopyUserIdBtn;
    private Button mDeleteAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ml_user_layout);

        mActivity = this;
        initStatusBar();
        initToolbar();
        initView();
        initSwipeRefreshLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initInfo();
        showUserInfo();
    }

    /**
     * 初始化账户数据
     */
    private void initInfo() {
        MLDBHelper mldbHelper = MLDBHelper.getInstance();
        if (mldbHelper == null) {
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                    mActivity.getResources().getString(R.string.ml_hello)).show();
            Intent intent = new Intent();
            intent.setClass(mActivity, MLSignActivity.class);
            startActivity(intent);
            return;
        }
        String s1 = MLDBConstants.COL_ACCESS_TOKEN + "=?";
        String args1[] = new String[]{(String) MLSPUtil.get(mActivity, MLDBConstants.COL_ACCESS_TOKEN, "")};
        Cursor c1 = mldbHelper.queryData(MLDBConstants.TB_USER, null, s1, args1, null, null, null, null);
        if (c1.moveToFirst()) {
            do {
                mUserInfo = new UserInfo(c1);
            } while (c1.moveToNext());
        }

        String s2 = MLDBConstants.COL_USER_ID + "=?";
        String args2[] = new String[]{mUserInfo.getSpouseId()};
        Cursor c2 = mldbHelper.queryData(MLDBConstants.TB_USER, null, s2, args2, null, null, null, null);
        if (c2.moveToFirst()) {
            do {
                mSpouseInfo = new UserInfo(c2);
            } while (c2.moveToNext());
        }
        mldbHelper.closeDatabase();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.ml_toolbar_view);
        mToolbar.setTitle(R.string.ml_set_account);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.ml_white));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.icon_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChange) {
                    if (!mUserInfo.getNickname().equals(mNicknameView.getText().toString())
                            || !mUserInfo.getSignature().equals(mSignatureView.getText().toString())
                            || !mUserInfo.getLocation().equals(mLocationView.getText().toString())) {
                        changeUserInfo();
                    }
                }
                backHome();
            }
        });

        if (MLScreen.getNavigationBarHeight() > 0) {
            LinearLayout reservedBottomLayout = (LinearLayout) findViewById(R.id.ml_reserved_layout_bottom);
            View v = new View(mActivity);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MLScreen.getNavigationBarHeight()));
            reservedBottomLayout.addView(v);
        }
    }

    /**
     * 初始化View
     */
    private void initView() {
        // 背景封面和头像
        mUserCover = (MLFilterImageView) findViewById(R.id.ml_img_user_cover);
        mUserAvatar = (MLImageView) findViewById(R.id.ml_img_user_avatar);
        mSpouseAvatar = (MLImageView) findViewById(R.id.ml_img_spouse_avatar);

        mUserIdView = (TextView) findViewById(R.id.ml_text_user_id);
        mCopyUserIdBtn = (Button) findViewById(R.id.ml_btn_copy_user_id);
        mAccountView = (TextView) findViewById(R.id.ml_text_user_account);
        mEmailView = (TextView) findViewById(R.id.ml_text_user_email);

        mNicknameView = (EditText) findViewById(R.id.ml_edit_user_nickname);
        mSignatureView = (EditText) findViewById(R.id.ml_edit_user_signature);
        mLocationView = (EditText) findViewById(R.id.ml_edit_user_location);

        mGenderView = (RadioGroup) findViewById(R.id.ml_radio_user_gender);
        mGenderManView = (RadioButton) findViewById(R.id.ml_radio_user_gender_1);
        mGenderWomanView = (RadioButton) findViewById(R.id.ml_radio_user_gender_0);

        mPasswordView = (TextView) findViewById(R.id.ml_text_user_password);

        mCopyUserIdBtn = (Button) findViewById(R.id.ml_btn_copy_user_id);
        mDeleteAccountBtn = (Button) findViewById(R.id.ml_btn_user_delete_account);

        // 添加焦点监听
        mNicknameView.setOnFocusChangeListener(focusChangeListener);
        mSignatureView.setOnFocusChangeListener(focusChangeListener);
        mLocationView.setOnFocusChangeListener(focusChangeListener);

        // 添加点击监听
        mUserCover.setOnClickListener(viewListener);
        mUserAvatar.setOnClickListener(viewListener);
        mSpouseAvatar.setOnClickListener(viewListener);
        mPasswordView.setOnClickListener(viewListener);
        mCopyUserIdBtn.setOnClickListener(viewListener);
        mDeleteAccountBtn.setOnClickListener(viewListener);
    }

    /**
     * 初始化下拉刷新控件
     */
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.ml_swipe_refresh_layout);

        mSwipeRefreshLayout.setProgressViewOffset(false, 0, MLScreen.dp2px(R.dimen.ml_dimen_96));
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.ml_blue,
                R.color.ml_orange,
                R.color.ml_green,
                R.color.ml_red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MLRequestParams params = new MLRequestParams();
                params.putParams(MLDBConstants.COL_ACCESS_TOKEN, mUserInfo.getAccessToken());
                MLHttpUtil.getInstance(mActivity).post(MLHttpConstants.API_URL + MLHttpConstants.API_USER, params,
                        new MLStringResponseListener() {
                            @Override
                            public void onFailure(int state, String content) {
                                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                                        mActivity.getResources().getString(R.string.ml_error_http)).show();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onSuccess(int state, String content) {
                                parseUserInfo(content);
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
            }
        });
    }

    /**
     * 设置显示当前账户数据
     */
    private void showUserInfo() {
        if (mUserInfo != null) {
            if (!mUserInfo.getCover().equals(null)) {
                String userCoverPath = MLApp.getUserImage() + mUserInfo.getCover();
                Bitmap cover = MLFile.fileToBitmap(userCoverPath);
                if (cover != null) {
                    mUserCover.setImageBitmap(cover);
                }
            }
            if (!mUserInfo.getAvatar().equals(null)) {
                String userAvatarPath = MLApp.getUserImage() + mUserInfo.getAvatar();
                Bitmap avatar = MLFile.fileToBitmap(userAvatarPath);
                if (avatar != null) {
                    mUserAvatar.setImageBitmap(avatar);
                }
            }
        }
        if (mSpouseInfo != null) {
            if (mSpouseInfo.getAvatar() != null) {
                String spouseAvatarPath = MLApp.getUserImage() + mSpouseInfo.getAvatar();
                Bitmap avatar = MLFile.fileToBitmap(spouseAvatarPath);
                if (avatar != null) {
                    mSpouseAvatar.setImageBitmap(avatar);
                }
            }
        }
        Animation avatarAnim = AnimationUtils.loadAnimation(mActivity, R.anim.ml_avatar_zoom_in);
        mUserAvatar.startAnimation(avatarAnim);
        mSpouseAvatar.startAnimation(avatarAnim);

        if (!mUserInfo.getLoveId().equals("null")) {
            mCopyUserIdBtn.setVisibility(View.INVISIBLE);
        }

        mUserIdView.setText(mUserInfo.getUserId());
        mAccountView.setText(mUserInfo.getSigninname());
        mEmailView.setText(mUserInfo.getEmail());
        mNicknameView.setText(mUserInfo.getNickname());
        mSignatureView.setText(mUserInfo.getSignature());
        mLocationView.setText(mUserInfo.getLocation());

        if (mUserInfo.getGender() == 1) {
            mGenderManView.setChecked(true);
        } else {
            mGenderWomanView.setChecked(true);
        }
        mGenderView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.ml_radio_user_gender_0:
                        mUserInfo.setGender(0);
                        changeUserInfo();
                        break;
                    case R.id.ml_radio_user_gender_1:
                        mUserInfo.setGender(1);
                        changeUserInfo();
                        break;
                }
            }
        });
    }

    /**
     * 修改密码
     */
    private void changeUserPassword() {
        AlertDialog.Builder changePassDialog = new AlertDialog.Builder(mActivity);
        changePassDialog.setTitle(mActivity.getResources().getString(R.string.ml_password_change));
        final View view = mActivity.getLayoutInflater().inflate(R.layout.ml_user_password_change_dialog, null);
        changePassDialog.setView(view);
        changePassDialog.setNegativeButton(mActivity.getResources().getString(R.string.ml_cancel), null);
        changePassDialog.setPositiveButton(mActivity.getResources().getString(R.string.ml_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final Resources res = mActivity.getResources();
                        EditText oldPassword = (EditText) view.findViewById(R.id.ml_edit_change_password_old);
                        EditText newPassword = (EditText) view.findViewById(R.id.ml_edit_change_password_new);
                        EditText confirmPassword = (EditText) view.findViewById(R.id.ml_edit_change_password_new_confirm);
                        String oldStr = oldPassword.getText().toString();
                        String newStr = newPassword.getText().toString();
                        String confirmStr = confirmPassword.getText().toString();


                        if (oldStr.equals("") || oldStr.equals(null)
                                || newStr.equals("") || newStr.equals(null)
                                || confirmStr.equals("") || confirmStr.equals(null)) {
                            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                                    res.getString(R.string.ml_toast_not_null)).show();
                            return;
                        }
                        if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                                    res.getString(R.string.ml_toast_confirm_password_null)).show();
                            return;
                        }
                        MLRequestParams params = new MLRequestParams();
                        params.putParams(MLDBConstants.COL_ACCESS_TOKEN, mUserInfo.getAccessToken());
                        params.putParams("old_password", MLCrypto.cryptoStr2MD5(oldStr));
                        params.putParams("new_password", MLCrypto.cryptoStr2MD5(confirmStr));
                        MLHttpUtil.getInstance(mActivity).post(MLHttpConstants.API_URL + MLHttpConstants.API_USER_SETPASSWORD,
                                params, new MLStringResponseListener() {

                                    // 获取返回数据成功，接下来进一步解析数据
                                    @Override
                                    public void onSuccess(int state, String content) {
                                        Resources res = mActivity.getResources();
                                        String toastStr = "";
                                        try {
                                            JSONObject jsonObject = new JSONObject(content);
                                            if (jsonObject.isNull("error")) {
                                                mUserInfo.changeInfo();
                                                toastStr = res.getString(R.string.ml_password_change_success);
                                                MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, toastStr).show();
                                            } else {
                                                String error = jsonObject.getString("error");
                                                if (error.equals("user")) {
                                                    toastStr = res.getString(R.string.ml_user_access_token_overdue);
                                                } else if (error.equals("password")) {
                                                    toastStr = res.getString(R.string.ml_password_old_error);
                                                }
                                                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, toastStr).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    // 获取返回数据失败，一般是网络超时导致（或者无法连接服务器）
                                    @Override
                                    public void onFailure(int state, String content) {
                                        MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                                                mActivity.getResources().getString(R.string.ml_error_http)).show();
                                    }
                                });
                    }
                });
        changePassDialog.show();
    }

    /**
     * 修改账户信息
     */
    private void changeUserInfo() {
        mUserInfo.setNickname(mNicknameView.getText().toString());
        mUserInfo.setSignature(mSignatureView.getText().toString());
        mUserInfo.setLocation(mLocationView.getText().toString());

        MLRequestParams params = new MLRequestParams();
        params.putParams(MLDBConstants.COL_NICKNAME, mUserInfo.getNickname());
        params.putParams(MLDBConstants.COL_SIGNATURE, mUserInfo.getSignature());
        params.putParams(MLDBConstants.COL_LOCATION, mUserInfo.getLocation());
        params.putParams(MLDBConstants.COL_GENDER, "" + mUserInfo.getGender());
        params.putParams(MLDBConstants.COL_ACCESS_TOKEN, mUserInfo.getAccessToken());

        MLHttpUtil.getInstance(mActivity).post(MLHttpConstants.API_URL + MLHttpConstants.API_USER_SETINFO,
                params, new MLStringResponseListener() {

                    // 获取返回数据成功，接下来进一步解析数据
                    @Override
                    public void onSuccess(int state, String content) {
                        Resources res = mActivity.getResources();
                        String toastStr = "";
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            if (jsonObject.isNull("error")) {
                                mUserInfo.changeInfo();
                                toastStr = res.getString(R.string.ml_user_info_change_success);
                                MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, toastStr).show();
                            } else {
                                String error = jsonObject.getString("error");
                                if (error.equals("user")) {
                                    toastStr = res.getString(R.string.ml_user_access_token_overdue);
                                }
                                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, toastStr).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // 获取返回数据失败，一般是网络超时导致（或者无法连接服务器）
                    @Override
                    public void onFailure(int state, String content) {
                        MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                                mActivity.getResources().getString(R.string.ml_error_http)).show();
                    }
                });
    }

    /**
     * 添加另一半
     */
    private void setSpouse() {
        AlertDialog.Builder addSpouseDialog = new AlertDialog.Builder(mActivity);
        addSpouseDialog.setTitle(R.string.ml_spouse_add);
        final EditText editText = new EditText(mActivity);
        editText.setHint(R.string.ml_spouse_id_or_account);
        editText.setMaxLines(1);
        addSpouseDialog.setView(editText);
        addSpouseDialog.setNegativeButton(R.string.ml_cancel, null);
        addSpouseDialog.setPositiveButton(R.string.ml_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String spouseId = editText.getText().toString();
                if (spouseId.equals("")) {
                    MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                            mActivity.getResources().getString(R.string.ml_toast_not_null)).show();
                    return;
                }
                MLRequestParams params = new MLRequestParams();
                params.putParams(MLDBConstants.COL_SPOUSE_ID, spouseId);
                params.putParams(MLDBConstants.COL_ACCESS_TOKEN, mUserInfo.getAccessToken());

                MLHttpUtil.getInstance(mActivity).post(MLHttpConstants.API_URL + MLHttpConstants.API_USER_SETSPOUSE,
                        params, new MLStringResponseListener() {
                            // 获取返回数据成功，接下来进一步解析数据
                            @Override
                            public void onSuccess(int state, String content) {
                                parseUserInfo(content);
                            }

                            // 获取返回数据失败，一般是网络超时导致（或者无法连接服务器）
                            @Override
                            public void onFailure(int state, String content) {
                                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                                        mActivity.getResources().getString(R.string.ml_error_http)).show();
                            }
                        });
            }
        });
        addSpouseDialog.show();
    }

    /**
     * 解析添加对方成功后返回的信息
     *
     * @param content
     */
    private void parseUserInfo(String content) {
        Resources res = mActivity.getResources();
        String toastStr = "";
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (jsonObject.isNull("error")) {
                JSONObject user = jsonObject.getJSONObject("user");
                UserInfo userInfo = new UserInfo(user);
                userInfo.changeInfo();

                JSONObject spouse = jsonObject.getJSONObject("spouse");
                UserInfo spouseInfo = new UserInfo(spouse);
                spouseInfo.changeInfo();

                toastStr = res.getString(R.string.ml_spouse_add_success);
                MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, toastStr).show();
            } else {
                String error = jsonObject.getString("error");
                if (error.equals("spouse")) {
                    toastStr = res.getString(R.string.ml_spouse_nonentity);
                } else if (error.equals("user")) {
                    toastStr = res.getString(R.string.ml_user_access_token_overdue);
                }
                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, toastStr).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改用户头像
     */
    private void changeUserAvatar() {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart("mlavatar", new FileBody(new File(MLApp.getUserImage() + mUserInfo.getAvatar())));
        multipartEntityBuilder.addTextBody("access_token", mUserInfo.getAccessToken());
        String url = MLHttpConstants.API_URL + MLHttpConstants.API_USER_SETAVATAR;
        uploadImage(url, multipartEntityBuilder.build());
    }

    /**
     * 修改用户封面
     */
    private void changeUserCover() {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart("mlcover", new FileBody(new File(MLApp.getUserImage() + mUserInfo.getCover())));
        multipartEntityBuilder.addTextBody("access_token", mUserInfo.getAccessToken());
        String url = MLHttpConstants.API_URL + MLHttpConstants.API_USER_SETCOVER;
        uploadImage(url, multipartEntityBuilder.build());
    }

    /**
     * 上传图片
     *
     * @param url
     * @param httpEntiy
     */
    private void uploadImage(String url, HttpEntity httpEntiy) {
        String toastStr = "";
        if (isAvatar) {
            toastStr = mActivity.getResources().getString(R.string.ml_user_avatar_change_submit);
        } else {
            toastStr = mActivity.getResources().getString(R.string.ml_user_cover_change_submit);
        }
        MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, toastStr).show();
        MLHttpUtil.getInstance(mActivity).postImage(url, httpEntiy, new MLImageResponseListener() {
            String str = "";

            // 获取返回数据成功，接下来进一步解析数据
            @Override
            public void onSuccess(int state, String content) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.isNull("error")) {
                        JSONObject user = jsonObject.getJSONObject("user");
                        UserInfo userInfo = new UserInfo(user);
                        userInfo.changeInfo();

                        Message msg = mHandler.obtainMessage();
                        if (isAvatar) {
                            msg.what = 0;
                            str = mActivity.getResources().getString(R.string.ml_user_avatar_change_success);
                        } else {
                            msg.what = 1;
                            str = mActivity.getResources().getString(R.string.ml_user_cover_change_success);
                        }
                        msg.sendToTarget();
                        MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, str).show();
                    } else {
                        if (isAvatar) {
                            str = mActivity.getResources().getString(R.string.ml_user_avatar_change_failed);
                        } else {
                            str = mActivity.getResources().getString(R.string.ml_user_cover_change_failed);
                        }
                        MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, str).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // 获取返回数据失败，一般是网络超时导致（或者无法连接服务器）
            @Override
            public void onFailure(int state, String content) {
                str = mActivity.getResources().getString(R.string.ml_error_http);
                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, str).show();
            }
        });
    }

    /**
     * 从图库获取图片
     */
    private void startGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        // 根据Intent启动一个带有返回值的Activity，这里启动的就是图库，返回选择图片的地址
        startActivityForResult(intent, MLAppConstant.PHOTO_REQUEST_GALLERY);
    }

    /**
     * 从相机获取图片
     */
    private void startCamera() {
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (MLFile.hasSdcard()) {
            mTempFile = new File(MLApp.getTemp(), MLAppConstant.ML_TEMP_PHOTO);
            // 从文件中创建uri
            mTempUri = Uri.fromFile(mTempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempUri);
        }
        // 根据Intent启动一个带有返回值的Activity，这里启动的就是相机，返回选择图片的地址
        startActivityForResult(intent, MLAppConstant.PHOTO_REQUEST_CAMERA);
    }

    /**
     * 剪切图片，根据传入的宽高决定图片大小，使用Uri 不返回数据
     *
     * @param uri
     * @param width
     * @param height
     */
    private void clipImage(Uri uri, int width, int height) {
        String tempPath = MLApp.getTemp() + MLAppConstant.ML_TEMP_PHOTO;
        mTempUri = Uri.fromFile(new File(tempPath));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        if (isAvatar) {
            // 设置剪切框大小为1:1
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        } else {
            // 设置剪切框大小为1:1
            intent.putExtra("aspectX", 3);
            intent.putExtra("aspectY", 2);
        }
        // 设置输出图片大小
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);

        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempUri);
        //图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, MLAppConstant.PHOTO_REQUEST_CLIP);
    }

    /**
     * 通过Uri获取Bitmap
     *
     * @param uri
     * @return
     */
    private Bitmap decodeUriToBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MLAppConstant.PHOTO_REQUEST_CAMERA:
                if (MLFile.hasSdcard()) {
                    Uri uri = Uri.fromFile(mTempFile);
                    if (isAvatar) {
                        clipImage(uri, 1512, 1512);
                    } else {
                        clipImage(uri, 900, 600);
                    }
                }
                break;
            case MLAppConstant.PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    if (isAvatar) {
                        clipImage(uri, 512, 512);
                    } else {
                        clipImage(uri, 900, 600);
                    }
                }
                break;
            case MLAppConstant.PHOTO_REQUEST_CLIP:
                Bitmap bitmap = null;
                try {
                    bitmap = decodeUriToBitmap(mTempUri);
                    if (isAvatar) {
                        mUserAvatar.setImageBitmap(bitmap);
                        mUserInfo.setAvatar("avatar_" + mUserInfo.getSigninname() + ".jpg");
                        MLFile.saveBitmapToSDCard(bitmap, MLApp.getUserImage() + mUserInfo.getAvatar());
                        changeUserAvatar();
                    } else {
                        mUserCover.setImageBitmap(bitmap);
                        mUserInfo.setCover("cover_" + mUserInfo.getSigninname() + ".jpg");
                        MLFile.saveBitmapToSDCard(bitmap, MLApp.getUserImage() + mUserInfo.getCover());
                        changeUserCover();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 界面内可点击控件的监听
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AnimatorSet userAnimSet;
            AnimatorSet spouseAnimSet;
            switch (v.getId()) {
                case R.id.ml_img_user_avatar:
                    isAvatar = true;
                    startGallery();
                    break;
                case R.id.ml_img_spouse_avatar:
                    if (mUserInfo.getSpouseId().equals("null")) {
                        setSpouse();
                    }
                    break;
                case R.id.ml_img_user_cover:
                    isAvatar = false;
                    startGallery();
                    break;
                case R.id.ml_btn_copy_user_id:
                    // 获取CLipboardManager对象，并将需要的内容添加进剪切板
                    ClipboardManager cManager = (ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("user_id", mUserInfo.getUserId());
                    cManager.setPrimaryClip(clipData);
                    MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp,
                            mActivity.getResources().getString(R.string.ml_spouse_send_id)).show();
                    break;
                case R.id.ml_text_user_password:
                    changeUserPassword();
                    break;
                case R.id.ml_btn_user_delete_account:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                    dialog.setTitle(mActivity.getResources().getString(R.string.ml_delete_account));
                    dialog.setMessage(mActivity.getResources().getString(R.string.ml_delete_account_msg));
                    dialog.setPositiveButton(R.string.ml_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MLSPUtil.clear(mActivity);
                            backHome();
                        }
                    });
                    dialog.setNegativeButton(R.string.ml_cancel, null);

                    dialog.show();
                    break;
            }
        }
    };

    /**
     * 可输入控件获取或失去焦点的监听
     */
    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                isChange = true;
            } else {
                if (!mUserInfo.getNickname().equals(mNicknameView.getText().toString())
                        || !mUserInfo.getSignature().equals(mSignatureView.getText().toString())
                        || !mUserInfo.getLocation().equals(mLocationView.getText().toString())) {
                    changeUserInfo();
                }
                isChange = false;
            }
        }
    };

    /**
     * 拦截返回按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isChange) {
                    if (!mUserInfo.getNickname().equals(mNicknameView.getText().toString())
                            || !mUserInfo.getSignature().equals(mSignatureView.getText().toString())
                            || !mUserInfo.getLocation().equals(mLocationView.getText().toString())) {
                        changeUserInfo();
                    }
                }
                backHome();
                break;
        }
        return true;
    }


    /**
     * 返回
     */
    private void backHome() {
        Intent intent = new Intent();
        intent.setClass(mActivity, MLMainActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(mActivity,
                R.anim.ml_activity_left_in, R.anim.ml_activity_right_out);
        ActivityCompat.startActivity(mActivity, intent, optionsCompat.toBundle());

        mActivity.finish();
    }

    /**
     * 初始化状态栏
     */
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mlManager = new MLSystemBarManager(mActivity);
            mlManager.setStatusBarTintEnabled(true);
            mlManager.setStatusBarTintResource(R.color.ml_transparent_primary);
            mlManager.setNavigationBarTintEnabled(true);
            mlManager.setNavigationBarTintResource(R.color.ml_transparent_navigationbar);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout rtl = (LinearLayout) findViewById(R.id.ml_reserved_layout_top);
            View v = new View(mActivity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    MLScreen.getStatusBarHeight() + MLScreen.getToolbarHeight());
            v.setLayoutParams(lp);
            rtl.addView(v);
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 0:
                    mUserAvatar.setImageBitmap(BitmapFactory.decodeFile(MLApp.getUserImage() + mUserInfo.getAvatar()));
                    break;
                case 1:
                    mUserCover.setImageBitmap(BitmapFactory.decodeFile(MLApp.getUserImage() + mUserInfo.getCover()));
                    break;
            }
        }
    };
}
