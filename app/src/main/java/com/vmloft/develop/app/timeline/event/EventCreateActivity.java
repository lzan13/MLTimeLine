package com.vmloft.develop.app.timeline.event;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.app.timeline.common.base.AppActivity;
import com.vmloft.develop.app.timeline.widget.VMTopBar;

/**
 * Created by lzan13 on 2015/5/21.
 */
public class EventCreateActivity extends AppActivity {

    @BindView(R.id.widget_top_bar) VMTopBar topBar;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_create_event;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(activity);

        initTopBar();
    }

    /**
     * 初始化 TopBar
     */
    private void initTopBar() {
        topBar.setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
