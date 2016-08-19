package com.xhl.bqlh.business.view.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ShopApplyModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ModelHelper;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.CustomerApplyDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/29.
 */
@ContentView(R.layout.activity_customer_query)
public class CustomerQueryActivity extends BaseAppActivity {

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_text_null)
    private View tv_text_null;

    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean mIsQuerying = false;

    @Override
    protected void initParams() {
        super.initToolbar(TYPE_child_other_back);

        setTitle(R.string.menu_apply);
        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.BOTH);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
                loadData();
            }
        }, 100);
    }

    @Override
    public void onRefreshLoadData() {
        loadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            loadData();
        }
    }

    private void loadData() {
        if (mIsQuerying) {
            return;
        }
        mIsQuerying = true;
        ApiControl.getApi().registerQueryRetailer(getPageSize(), getPageIndex(), new DefaultCallback<ResponseModel<ShopApplyModel>>() {
            @Override
            public void success(ResponseModel<ShopApplyModel> result) {
                setTotalSize(result.getPageSize());
                createData(result.getObjList());
            }

            @Override
            public void finish() {
                mIsQuerying = false;
                mRefresh.setRefreshing(false);
            }
        });
    }

    private void createData(List<ShopApplyModel> orders) {
        if (orders.size() == 0 && getPageIndex() > 1) {
            SnackUtil.shortShow(mRecyclerView, R.string.load_null);
            return;
        }
        //添加状态信息
        ModelHelper.addMember2ShopApplyModel(orders);

        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (ShopApplyModel order : orders) {
            holders.add(new CustomerApplyDataHolder(order));
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
        getMenuInflater().inflate(R.menu.user_menu_customer_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, CustomerQueryFriendsActivity.class));
        return true;
    }

}
