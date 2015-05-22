package net.melove.app.ml.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.melove.app.ml.MLApp;
import net.melove.app.ml.R;
import net.melove.app.ml.constants.MLAppConstant;
import net.melove.app.ml.db.MLDBConstants;
import net.melove.app.ml.db.MLDBHelper;
import net.melove.app.ml.http.MLHttpConstants;
import net.melove.app.ml.http.MLHttpUtil;
import net.melove.app.ml.http.MLImageResponseListener;
import net.melove.app.ml.info.NoteInfo;
import net.melove.app.ml.info.UserInfo;
import net.melove.app.ml.manager.MLSystemBarManager;
import net.melove.app.ml.utils.MLCrypto;
import net.melove.app.ml.utils.MLDate;
import net.melove.app.ml.utils.MLFile;
import net.melove.app.ml.utils.MLImageCompress;
import net.melove.app.ml.utils.MLLog;
import net.melove.app.ml.utils.MLSPUtil;
import net.melove.app.ml.utils.MLScreen;
import net.melove.app.ml.views.MLToast;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by lzan13 on 2015/5/10.
 */
public class MLNotePutActivity extends MLBaseActivity {

    private Activity mActivity;

    private MLSystemBarManager mlManager;
    private View mStatusBarView;

    private UserInfo mUserInfo;

    private Toolbar mToolbar;

    private ImageView mNoteImageView;
    private EditText mNoteContentView;
    private boolean isHasImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ml_note_put_layout);
        mActivity = this;

        initStatusBar();
        initToolbar();

        initInfo();
        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

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
        mldbHelper.closeDatabase();
    }

    private void initView() {
        mNoteImageView = (ImageView) findViewById(R.id.ml_img_note_img);
        mNoteContentView = (EditText) findViewById(R.id.ml_edit_note_content);

        mNoteImageView.setOnClickListener(viewListener);
        mNoteContentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MLLog.d("beforeTextChanged: s " + s + "; start " + start + "; count " + count + "; after " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MLLog.d("onTextChanged: s " + s + "; start " + start + "; before " + before + "; count " + count);

            }

            @Override
            public void afterTextChanged(Editable s) {
                MLLog.d("onTextChanged: s " + s.toString());

            }
        });
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.ml_toolbar_view);
        mToolbar.setTitle(R.string.ml_note_put);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.ml_white));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.icon_close_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEditNote();
            }
        });
    }


    /**
     * 取消编辑Note 确认
     */
    private void cancelEditNote() {
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(mActivity);
        dialog.setTitle(mActivity.getResources().getString(R.string.ml_note_cancel_edit));
        dialog.setMessage(mActivity.getResources().getString(R.string.ml_note_cancel_edit_msg));
        dialog.setPositiveButton(R.string.ml_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backHome();
            }
        });
        dialog.setNegativeButton(R.string.ml_cancel, null);
        dialog.show();
    }

    /**
     * 拦截返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                cancelEditNote();
                break;
        }
        return true;
    }

    /**
     * 添加一条新的带有图片的Note
     */
    private void putTextNote() {
        if (mUserInfo.getLoveId().equals("null") || mUserInfo.getLoveId().equals("") || mUserInfo.getLoveId() == null) {
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                    mActivity.getResources().getString(R.string.ml_spouse_not_null)).show();

            Intent intent = new Intent();
            intent.setClass(mActivity, MLUserActivity.class);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(mActivity,
                    R.anim.ml_fade_in, R.anim.ml_fade_out);
            ActivityCompat.startActivity(mActivity, intent, optionsCompat.toBundle());
            mActivity.finish();
            return;
        }
        String content = mNoteContentView.getText().toString();

        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        StringBody stringBody = new StringBody(content, contentType);

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart(MLDBConstants.COL_CONTENT, stringBody);
        multipartEntityBuilder.addTextBody(MLDBConstants.COL_NOTE_TYPE, "text");
        multipartEntityBuilder.addTextBody(MLDBConstants.COL_ACCESS_TOKEN, mUserInfo.getAccessToken());
        String url = MLHttpConstants.API_URL + MLHttpConstants.API_NOTE_PUT_TEXT;
        MLHttpUtil.getInstance(mActivity).postImage(url, multipartEntityBuilder.build(),
                new MLImageResponseListener() {
                    // 获取返回数据成功，接下来进一步解析数据
                    @Override
                    public void onSuccess(int state, String content) {
                        parseNote(content);
                    }

                    // 获取返回数据失败，一般是网络超时导致（或者无法连接服务器）
                    @Override
                    public void onFailure(int state, String content) {
                        MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                                mActivity.getResources().getString(R.string.ml_error_http)).show();
                    }
                });
        backHome();
    }

    /**
     * 添加一条新的带有图片的Note
     */
    private void putImageNote() {
        if (mUserInfo.getLoveId().equals("null") || mUserInfo.getLoveId().equals("") || mUserInfo.getLoveId() == null) {
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                    mActivity.getResources().getString(R.string.ml_spouse_not_null)).show();
            Intent intent = new Intent();
            intent.setClass(mActivity, MLUserActivity.class);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(mActivity,
                    R.anim.ml_fade_in, R.anim.ml_fade_out);
            ActivityCompat.startActivity(mActivity, intent, optionsCompat.toBundle());
            mActivity.finish();
            return;
        }
        String dateTime = MLDate.getCurrentDate();
        String srcPath = MLApp.getTemp() + MLAppConstant.ML_TEMP_PHOTO;
        String destPath = MLApp.getImage() + MLCrypto.cryptoStr2MD5(dateTime) + "." + MLFile.getImageSize(srcPath) + ".jpg";
        String content = mNoteContentView.getText().toString();
        if (MLFile.copyFile(srcPath, destPath)) {

            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            StringBody stringBody = new StringBody(content, contentType);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addPart(MLDBConstants.COL_IMAGE, new FileBody(new File(destPath)));
            multipartEntityBuilder.addPart(MLDBConstants.COL_CONTENT, stringBody);
            multipartEntityBuilder.addTextBody(MLDBConstants.COL_NOTE_TYPE, "image");
            multipartEntityBuilder.addTextBody(MLDBConstants.COL_ACCESS_TOKEN, mUserInfo.getAccessToken());
            String url = MLHttpConstants.API_URL + MLHttpConstants.API_NOTE_PUT_IMAGE;
            MLHttpUtil.getInstance(mActivity).postImage(url, multipartEntityBuilder.build(),
                    new MLImageResponseListener() {
                        // 获取返回数据成功，接下来进一步解析数据
                        @Override
                        public void onSuccess(int state, String content) {
                            parseNote(content);
                        }

                        // 获取返回数据失败，一般是网络超时导致（或者无法连接服务器）
                        @Override
                        public void onFailure(int state, String content) {
                            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                                    mActivity.getResources().getString(R.string.ml_error_http)).show();
                        }
                    });
        }
        backHome();
    }

    /**
     * 解析Put Note成功后返回的数据
     *
     * @param content
     */
    private void parseNote(String content) {
        Resources res = mActivity.getResources();
        String toastStr = "";
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (jsonObject.isNull("error")) {

                JSONObject user = jsonObject.getJSONObject("user");
                UserInfo userInfo = new UserInfo(user);
                userInfo.changeInfo();

                JSONObject note = jsonObject.getJSONObject("note");
                NoteInfo noteInfo = new NoteInfo(note);

                ContentValues values = new ContentValues();
                values.put(MLDBConstants.COL_LOVE_ID, noteInfo.getLoveId());
                values.put(MLDBConstants.COL_USER_ID, noteInfo.getUserId());
                values.put(MLDBConstants.COL_NOTE_ID, noteInfo.getNoteId());
                values.put(MLDBConstants.COL_NOTE_TYPE, noteInfo.getNoteType());
                values.put(MLDBConstants.COL_IMAGE, noteInfo.getImage());
                values.put(MLDBConstants.COL_CONTENT, noteInfo.getContent());
                values.put(MLDBConstants.COL_CREATE_AT, noteInfo.getCreateAt());

                MLDBHelper mldbHelper = MLDBHelper.getInstance();
                mldbHelper.insterData(MLDBConstants.TB_NOTE, values);
                mldbHelper.closeDatabase();

                toastStr = res.getString(R.string.ml_note_send_success);
                MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp, toastStr).show();
            } else {
                String error = jsonObject.getString("error");
                if (error.equals("note")) {
                    toastStr = res.getString(R.string.ml_content_exists);
                }
                if (error.equals("user")) {
                    toastStr = res.getString(R.string.ml_user_access_token_overdue);
                }
                if (error.equals("rename")) {
                    toastStr = res.getString(R.string.ml_user_access_token_overdue);
                }
                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp, toastStr).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 菜单按钮点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ml_menu_action_send:
                if (isHasImage) {
                    putImageNote();
                } else {
                    putTextNote();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 加载菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.put_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * 控件点击事件监听器
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ml_img_note_img:
                    startGallery();
                    break;
            }
        }
    };

    /**
     * 调用系统图库选择图片
     */
    private void startGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        // 根据Intent启动一个带有返回值的Activity，这里启动的就是图库，返回选择图片的地址
        startActivityForResult(intent, MLAppConstant.PHOTO_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MLAppConstant.PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    Bitmap bitmap = MLImageCompress.compressFromUri(uri);
                    mNoteImageView.setImageBitmap(bitmap);
                    isHasImage = true;
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 确认不保存Note 返回
     */
    private void backHome() {
        Intent intent = new Intent();
        intent.setClass(mActivity, MLMainActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(mActivity,
                R.anim.ml_fade_in, R.anim.ml_fade_out);
        ActivityCompat.startActivity(mActivity, intent, optionsCompat.toBundle());
        mActivity.finish();
    }

    // php.pujiahh.com/overnitedynamite/
    // php.pujiahh.com/reusingnature
    /**
     * 初始化状态栏
     */
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(mActivity.getResources().getColor(R.color.ml_transparent_primary));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mlManager = new MLSystemBarManager(mActivity);
            mlManager.setStatusBarTintEnabled(true);
            mlManager.setStatusBarTintResource(R.color.ml_transparent_primary);
            mlManager.setNavigationBarTintEnabled(true);
            mlManager.setNavigationBarTintResource(R.color.ml_transparent_navigationbar);
        }
        if (MLScreen.getNavigationBarHeight() > 0) {
            LinearLayout reservedBottomLayout = (LinearLayout) findViewById(R.id.ml_reserved_layout_bottom);
            View v = new View(mActivity);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MLScreen.getNavigationBarHeight()));
            reservedBottomLayout.addView(v);
        }
    }
}
