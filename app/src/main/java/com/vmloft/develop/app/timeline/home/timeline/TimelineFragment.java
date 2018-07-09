package com.vmloft.develop.app.timeline.home.timeline;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.vmloft.develop.app.timeline.R;
import com.vmloft.develop.app.timeline.common.base.AppMVPFragment;
import com.vmloft.develop.app.timeline.common.bean.Moment;
import com.vmloft.develop.app.timeline.home.timeline.TimelineContract.ITimelineView;
import com.vmloft.develop.app.timeline.home.timeline.TimelineContract.ITimelinePresenter;
import com.vmloft.develop.app.timeline.home.timeline.presenter.TimelinePresenterImpl;
import com.vmloft.develop.library.tools.adapter.VMEmptyWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/26.
 */
public class TimelineFragment extends AppMVPFragment<ITimelineView, ITimelinePresenter<ITimelineView>> implements ITimelineView {

    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private TimelineAdapter adapter;
    private VMEmptyWrapper emptyWrapper;
    private List<Moment> moments = new ArrayList<>();

    /**
     * Fragment 工厂方法
     */
    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化 Fragment 界面 layout_id
     *
     * @return 返回布局 id
     */
    @Override
    protected int initLayoutId() {
        return R.layout.fragment_timeline;
    }

    /**
     * 初始化界面控件，将 Fragment 变量和 View 建立起映射关系
     */
    @Override
    protected void initView() {
        ButterKnife.bind(this, getView());
        initSwipeRefreshLayout();

        adapter = new TimelineAdapter(activity, moments);
        emptyWrapper = new VMEmptyWrapper(adapter);
        emptyWrapper.setEmptyView(R.layout.widget_empty_layout);

        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(emptyWrapper);
    }

    /**
     * 加载数据
     */
    @Override
    protected void initData() {

    }

    /**
     * 初始化下拉刷新组件
     */
    private void initSwipeRefreshLayout() {

        // 设置进度圈出现的位置及方式
        //        mSwipeRefreshLayout.setProgressViewOffset(false, 0, MLScreen.dp2px(R.dimen.dimen_48));
        //swipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.orange, R.color.green, R.color.red);
        //swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        //    @Override
        //    public void onRefresh() {
        //    }
        //});
    }

    @Override
    public ITimelinePresenter<ITimelineView> createPresenter() {
        return new TimelinePresenterImpl();
    }
}
