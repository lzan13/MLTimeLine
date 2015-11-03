package net.melove.app.ml.activity;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import net.melove.app.ml.R;
import net.melove.app.ml.fragment.MLBaseFragment;
import net.melove.app.ml.fragment.MLCommemorateFragment;
import net.melove.app.ml.fragment.MLDrawerLeftFragment;
import net.melove.app.ml.fragment.MLMessageFragment;
import net.melove.app.ml.fragment.MLTimeLineFragment;
import net.melove.app.ml.config.MLConfig;
import net.melove.app.ml.views.MLToast;


/**
 * Created by lzan13 on 2014/12/17.
 */
public class MLMainActivity extends MLBaseActivity implements MLBaseFragment.MLFragmentCallback {

    private Activity mActivity;

    private int mMenuType;
    private Intent mIntent;
    private Fragment mCurrentFragment;
    private int mCurrentIndex;

    private Toolbar mToolbar;
    public DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean isDrawerOpen;

    private MLDrawerLeftFragment mlDrawerLeftFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;

        //检测软件是否第一次运行
        MLConfig.chackFirstRun(mActivity);


    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 打开app时，首先检测是否已经登录，没有登录信息则跳转到登录界面
         */

        if (!MLConfig.isAccessToken(mActivity)) {
            Intent intent = new Intent();
            intent.setClass(mActivity, MLSignActivity.class);
            startActivity(intent);
            mActivity.finish();
            return;
        }
        initToolbar();
        initFragment();

    }


    private void initFragment() {
        // 侧滑菜单Fragment
        FragmentTransaction ftd = getFragmentManager().beginTransaction();
        mlDrawerLeftFragment = new MLDrawerLeftFragment();
        ftd.replace(R.id.ml_fragment_drawer_left, mlDrawerLeftFragment);
        ftd.commit();

        // 主Activity 默认显示 TimeLineFragment
        mCurrentIndex = 0;
        mCurrentFragment = new MLTimeLineFragment();
        mToolbar.setTitle(mActivity.getResources().getString(R.string.ml_timeline));
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.ml_fragment_container, mCurrentFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    /**
     * 初始化Toolbar组件
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.ml_toolbar_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.ml_drawerlayout);

        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.ml_white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 设置Toolbar与DrawerLayout 联动的按钮
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.ml_open, R.string.ml_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mMenuType = -1;
                isDrawerOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                switch (mMenuType) {
                    case 0:
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.ml_fragment_container, mCurrentFragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();
                        break;
                    case 1:
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(mActivity,
                                R.anim.ml_activity_right_in, R.anim.ml_activity_left_out);
                        ActivityCompat.startActivity(mActivity, mIntent, optionsCompat.toBundle());
                        mActivity.finish();
                        break;
                    default:
                        break;
                }
                isDrawerOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // 当toolbar单独使用是需要手动加载menu
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
//            public boolean onMenuItemClick(MenuItem item){
//                return true;
//            }
//        });
//        mToolbar.inflateMenu(R.menu.menu_main);
    }

    @Override
    public void mlClickListener(int i) {
        Resources res = getResources();
        switch (i) {
            case 0:
                // 时间线
                if (mCurrentIndex != i) {
                    mCurrentIndex = i;
                    mCurrentFragment = new MLTimeLineFragment();
                    mToolbar.setTitle(res.getString(R.string.ml_timeline));
                    mMenuType = 0;
                }
                break;
            case 1:
                // 纪念日
                if (mCurrentIndex != i) {
                    mCurrentIndex = i;
                    mCurrentFragment = new MLCommemorateFragment();
                    mToolbar.setTitle(res.getString(R.string.ml_commemorate));
                    mMenuType = 0;
                }
                break;
            case 2:
                // 消息
                if (mCurrentIndex != i) {
                    mCurrentIndex = i;
                    mCurrentFragment = new MLMessageFragment();
                    mToolbar.setTitle(res.getString(R.string.ml_message));
                    mMenuType = 0;
                }
                break;
            case 10:
                MLToast.makeToast(R.mipmap.icon_emotion_sad_24dp,
                        mActivity.getResources().getString(R.string.ml_error_db_null)).show();
                Intent intent = new Intent();
                intent.setClass(mActivity, MLSignActivity.class);
                startActivity(intent);
                mActivity.finish();
                break;
            case R.id.ml_img_user_avatar:
                mIntent = new Intent();
                mIntent.setClass(mActivity, MLUserActivity.class);
                mMenuType = 1;
                break;
            case R.id.ml_drawer_menu_setting:
                break;
            case R.id.ml_drawer_menu_help:

                break;
        }
        mDrawerLayout.closeDrawer(Gravity.START);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ml_menu_action_search:
                MLToast.makeToast(R.mipmap.icon_emotion_smile_24dp,
                        getResources().getString(R.string.ml_hello)).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isDrawerOpen) {
                mDrawerLayout.closeDrawer(Gravity.START);
            } else if (mCurrentIndex != 0) {
                mCurrentIndex = 0;
                mCurrentFragment = new MLTimeLineFragment();
                mToolbar.setTitle(mActivity.getResources().getString(R.string.ml_timeline));
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.ml_fragment_container, mCurrentFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                mlDrawerLeftFragment.getmListView().setItemChecked(0, true);
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return true;
    }
}
