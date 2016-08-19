package com.xhl.bqlh.business.view.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Summer on 2016/8/8.
 */
@ContentView(R.layout.activity_store_car_return_state)
public class StoreCarReturnApplyActivity extends BaseAppActivity {


    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private RecyclerAdapter mAdapter;

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle(R.string.menu_store_manager_apply);
        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.BOTH);

        mAdapter = new RecyclerAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        onRefreshLoadData();
    }

    @Override
    public void onRefreshLoadData() {
        super.onRefreshLoadData();

        ApiControl.getApi().storeCarReturnApply(getPageSize(), getPageIndex(), new DefaultCallback<ResponseModel<ApplyModel>>() {
            @Override
            public void success(ResponseModel<ApplyModel> result) {
                setTotalSize(result.getPageSize());
            }

            @Override
            public void finish() {
                mRefresh.setRefreshing(false);
            }
        });
    }
}
