package com.vmloft.develop.app.timeline.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.View;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.OnClick;
import com.avos.avoscloud.AVUser;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.app.timeline.common.base.AppActivity;
import com.vmloft.develop.app.timeline.common.base.AppFragment;
import com.vmloft.develop.app.timeline.common.router.NavRouter;
import com.vmloft.develop.app.timeline.home.contacts.ContactsFragment;
import com.vmloft.develop.app.timeline.home.event.EventFragment;
import com.vmloft.develop.app.timeline.home.me.MeFragment;
import com.vmloft.develop.app.timeline.home.timeline.TimelineFragment;
import com.vmloft.develop.app.timeline.widget.VMTopBar;
import com.vmloft.develop.library.tools.widget.VMToast;

/**
 * Created by lzan13 on 2014/12/17.
 */
public class MainActivity extends AppActivity {

    @BindView(R.id.widget_top_bar) VMTopBar topBar;
    @BindView(R.id.widget_tab_layout) TabLayout tabLayout;
    @BindView(R.id.widget_view_pager) ViewPager viewPager;

    private TimelineFragment timelineFragment;
    private EventFragment eventFragment;
    private ContactsFragment contactsFragment;
    private MeFragment meFragment;
    private AppFragment[] fragments;
    private int tabTitles[] = { R.string.timeline, R.string.event, R.string.contacts, R.string.me };
    private int tabIcons[] = {
        R.drawable.ic_time, R.drawable.ic_discover, R.drawable.ic_contacts_2, R.drawable.ic_me
    };

    private int currentIndex = 0;
    private AppFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 将主题设置为正常主题
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化界面 layout id
     */
    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化界面
     */
    @Override
    protected void initView() {
        initTopBar();
        initFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AVUser.getCurrentUser() == null) {
            NavRouter.goSign(activity);
            return;
        }
    }

    /**
     * 初始化自定义的 TopBar
     */
    private void initTopBar() {
        topBar.setTitle(tabTitles[currentIndex]);
        topBar.setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VMToast.make("搜索").showDone();
            }
        });
        topBar.setMoreOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VMToast.make("更多").showDone();
            }
        });
    }

    /**
     * 初始化 Fragment
     */
    private void initFragment() {
        timelineFragment = TimelineFragment.newInstance();
        eventFragment = EventFragment.newInstance();
        contactsFragment = ContactsFragment.newInstance();
        meFragment = MeFragment.newInstance();

        fragments = new AppFragment[] {
            timelineFragment, eventFragment, contactsFragment, meFragment
        };
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        // 设置 ViewPager 缓存个数
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(currentIndex);
        // 添加 ViewPager 页面改变监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                topBar.setTitle(tabTitles[position]);
                currentFragment = fragments[position];
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        tabLayout.setupWithViewPager(viewPager);
        // 实现自定义 TabLayout item
        for (int i = 0; i < fragments.length; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.widget_tab_item);
            ImageButton tabIconBtn = tab.getCustomView().findViewById(R.id.img_tab_icon);
            tabIconBtn.setImageResource(tabIcons[i]);
            if (i == 0) {
                tabIconBtn.setSelected(true);
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.img_tab_icon).setSelected(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.img_tab_icon).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @OnClick({ R.id.fab_add })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.fab_add:
            NavRouter.goNoteCreate(activity);
            break;
        }
    }

    /**
     * 自定义 ViewPager 适配器子类
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private AppFragment fragments[];

        public ViewPagerAdapter(FragmentManager fm, AppFragment fragments[]) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public AppFragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
