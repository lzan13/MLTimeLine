package net.melove.app.ml.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.melove.app.ml.R;
import net.melove.app.ml.db.MLDBConstants;
import net.melove.app.ml.http.MLHttpConstants;
import net.melove.app.ml.http.MLHttpUtil;
import net.melove.app.ml.http.MLImageResponseListener;
import net.melove.app.ml.http.MLStringResponseListener;
import net.melove.app.ml.manager.MLSystemBarManager;
import net.melove.app.ml.utils.MLSPUtil;
import net.melove.app.ml.views.MLToast;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

/**
 * Created by lzan13 on 2015/5/21.
 */
public class MLCommemoratePutActivity extends MLBaseActivity {

    private Activity mActivity;
    private MLSystemBarManager mlManager;

    private Toolbar mToolbar;

    private TextView mCommemorateTime;
    private EditText mCommemorateTitle;
    private EditText mCommemorateContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ml_commemorate_put_layout);

        mActivity = this;

        initStatusBar();
        initToolbar();
        initView();

    }

    private void initView() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.put_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ml_menu_action_send:
                putCommemorate();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void putCommemorate() {
        String loveId = (String) MLSPUtil.get(mActivity, MLDBConstants.COL_LOVE_ID, "");
        String accessToken = (String) MLSPUtil.get(mActivity, MLDBConstants.COL_ACCESS_TOKEN, "");
        if (loveId.equals("null") || loveId.equals("") || loveId == null) {
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
        String time = mCommemorateTime.getText().toString();
        String title = mCommemorateTitle.getText().toString();
        String content = mCommemorateContent.getText().toString();

        if(title.equals("") || title == null){
            MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                    mActivity.getResources().getString(R.string.ml_commemorate_title_not_null)).show();
            return;
        }

        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        StringBody titleBody = new StringBody(title, contentType);
        StringBody contentBody = new StringBody(content, contentType);

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart(MLDBConstants.COL_CONTENT, titleBody);
        multipartEntityBuilder.addPart(MLDBConstants.COL_CONTENT, contentBody);
        multipartEntityBuilder.addTextBody(MLDBConstants.COL_NOTE_TYPE, "text");
        multipartEntityBuilder.addTextBody(MLDBConstants.COL_ACCESS_TOKEN, accessToken);
        String url = MLHttpConstants.API_URL + MLHttpConstants.API_NOTE_PUT_TEXT;
        MLHttpUtil.getInstance(mActivity).postImage(url, multipartEntityBuilder.build(),
                new MLStringResponseListener() {
                    // 获取返回数据成功，接下来进一步解析数据
                    @Override
                    public void onSuccess(int state, String content) {
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
     * 初始化Toolbar
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.ml_toolbar_view);
        mToolbar.setTitle(R.string.ml_commemorate_put);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.ml_white));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.icon_close_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
