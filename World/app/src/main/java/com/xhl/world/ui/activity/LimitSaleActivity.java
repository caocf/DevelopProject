package com.xhl.world.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.ui.view.pub.PageScrollListener;
import com.xhl.world.ui.view.pub.callback.RecyclerViewScrollBottomListener;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Sum on 16/1/19.
 */
@ContentView(R.layout.activity_limit_sale)
public class LimitSaleActivity extends BaseAppActivity implements RecyclerViewScrollBottomListener {

    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.time_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.title_name)
    private TextView mTitle;

    //滚动到顶部
    @ViewInject(R.id.ripple_scroll_to_top)
    private RippleView mRippleToTop;


    @Event(value = R.id.ripple_scroll_to_top, type = RippleView.OnRippleCompleteListener.class)
    private void scrollTopClick(View v) {
        mRecyclerView.smoothScrollToPosition(0);
    }


    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        finish();
    }

    private List<RecyclerDataHolder> mRecyclerData;
    private LinearLayoutManager mLayoutManger;
    private RecyclerAdapter mRecyclerAdapter;

    @Override
    protected void initParams() {
        mTitle.setText("限时特卖");
        mRecyclerAdapter = new RecyclerAdapter(this);
        mLayoutManger = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManger);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        PageScrollListener listener = new PageScrollListener(mLayoutManger);
        listener.setTopView(mRippleToTop);
        listener.addBottomListener(this);
        mRecyclerView.addOnScrollListener(listener);
        initPullRefresh();
    }

    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    loadData();
                }
            }
        });
    }

    private void loadData() {



    }


    @Override
    public void onScrollBottom() {

    }
}
