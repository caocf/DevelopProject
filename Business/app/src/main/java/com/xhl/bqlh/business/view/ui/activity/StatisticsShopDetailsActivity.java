package com.xhl.bqlh.business.view.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.App.OrderQueryCondition;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.OrderPayDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/11.
 * 赊账管理
 */
@ContentView(R.layout.activity_order_account_content_details)
public class StatisticsShopDetailsActivity extends BaseAppActivity {

    @ViewInject(R.id.tv_text_null)
    private View tv_text_null;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean mIsQuerying = false;

    private OrderQueryCondition mCondition;

    @Override
    protected void initParams() {
        super.initToolbar();

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.BOTH);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        String shopId = getIntent().getStringExtra(GlobalParams.Intent_shop_id);

        String shopName = getIntent().getStringExtra(GlobalParams.Intent_shop_name);
        setTitle(shopName);

        //查询所有赊账的订单
        mCondition = new OrderQueryCondition();
        mCondition.creditStatus = "2";
        mCondition.shopId = shopId;

        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
                loadData();
            }
        }, 400);
    }

    @Override
    public void onRefreshLoadData() {
        loadData();
    }

    private void loadData() {
        if (mIsQuerying) {
            return;
        }
        mIsQuerying = true;
        ApiControl.getApi().orderQuery(getPageSize(), getPageIndex(), mCondition, new Callback.CommonCallback<ResponseModel<OrderModel>>() {
            @Override
            public void onSuccess(ResponseModel<OrderModel> result) {
                if (result.isSuccess()) {
                    setTotalSize(result.getPageSize());
                    createData(result.getObjList());
                } else {
                    SnackUtil.longShow(mRecyclerView, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.longShow(mRecyclerView, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mIsQuerying = false;
                mRefresh.setRefreshing(false);
            }
        });
    }

    private void createData(List<OrderModel> orders) {
        if (orders.size() == 0 && getPageIndex() > 1) {
            SnackUtil.shortShow(mRecyclerView, R.string.load_null);
            return;
        }
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (OrderModel order : orders) {
            holders.add(new OrderPayDataHolder(order));
        }

        if (getPageIndex() == 1 && holders.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }

        if (getPageIndex() > 1) {
            mAdapter.addDataHolder(holders);
        } else {
            mAdapter.setDataHolders(holders);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.user_menu_order_acount, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, PayActivity.class));
        AVAnalytics.onEvent(this, "click to pay");
        return true;
    }
}
