package com.xhl.bqlh.business.view.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.event.CommonEvent;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.business.view.ui.activity.StoreApplyAddActivity;
import com.xhl.bqlh.business.view.ui.activity.StoreCarActivity;
import com.xhl.bqlh.business.view.ui.recyclerHolder.StoreApplyDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/5/15.
 */
@ContentView(R.layout.fragment_home_store_apply)
public class HomeStoreApplyFragment extends BaseAppFragment implements Toolbar.OnMenuItemClickListener, RecycleViewCallBack {

    @Event(R.id.fab_add)
    private void onAddClick(View view) {
        startActivity(new Intent(getContext(), StoreApplyAddActivity.class));
    }

    @ViewInject(R.id.tv_text_null)
    private View tv_text_null;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private RecyclerAdapter mAdapter;
    private boolean mIsQuerying = false;

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    @Override
    protected void initParams() {
        super.initHomeToolbar();
        mToolbar.inflateMenu(R.menu.user_menu_store_apply);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitle(R.string.user_nav_main_store_manager);

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.TOP);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        load();
    }

    private void load() {
        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mRefresh.isRefreshing()) {
                    mRefresh.setRefreshing(true);
                }
                loadData();
            }
        }, 500);
    }

    public void onEvent(CommonEvent event) {
        //刷新数据
        if (event.eventType == CommonEvent.EVENT_REFRESH_STORE) {
            mIsQuerying = false;
            loadData();
        }
    }

    @Override
    public void onRefreshLoadData() {
        super.onRefreshLoadData();
        loadData();
    }

    private void loadData() {
        if (mIsQuerying) {
            return;
        }
        mIsQuerying = true;

        ApiControl.getApi().storeApplyQuery(new Callback.CommonCallback<ResponseModel<ApplyModel>>() {
            @Override
            public void onSuccess(ResponseModel<ApplyModel> result) {
                if (result.isSuccess()) {
                    List<ApplyModel> objList = result.getObjList();
                    createData(objList);
                } else {
                    SnackUtil.shortShow(mRecyclerView, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(mRecyclerView, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mRefresh.setRefreshing(false);
                mIsQuerying = false;
            }
        });
    }

    private void createData(List<ApplyModel> products) {

        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (ApplyModel pro : products) {
            holders.add(new StoreApplyDataHolder(pro, this));
        }
        if (holders.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }
        mAdapter.setDataHolders(holders);
    }

    @Override
    public void onItemClick(final int position) {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.removeDataHolder(position);
            }
        }, 100);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        startActivity(new Intent(getContext(), StoreCarActivity.class));
        return false;
    }
}
