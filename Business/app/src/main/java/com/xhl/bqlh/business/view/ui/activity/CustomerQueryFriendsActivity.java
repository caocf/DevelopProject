package com.xhl.bqlh.business.view.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Db.Member;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ShopFriendsModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.CustomerFriendsDataHolder;
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
 * Created by Sum on 16/4/29.
 */
@ContentView(R.layout.activity_customer_query)
public class CustomerQueryFriendsActivity extends BaseAppActivity {

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean mIsQuerying = false;

    @Override
    protected void initParams() {
        super.initToolbar(TYPE_child_other_back);

        setTitle(R.string.menu_apply_friends);

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

        tv_text_null.setText("无新会员");
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
        ApiControl.getApi().registerQueryRetailerFriends(getPageSize(), getPageIndex(), new Callback.CommonCallback<ResponseModel<ShopFriendsModel>>() {
            @Override
            public void onSuccess(ResponseModel<ShopFriendsModel> result) {
                if (result.isSuccess()) {
                   setTotalSize(result.getPageSize());
                    createData(result.getObjList());
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
                mIsQuerying = false;
                mRefresh.setRefreshing(false);
            }
        });
    }

    private void createData(List<ShopFriendsModel> orders) {
        if (orders.size() == 0 && getPageIndex() > 1) {
            SnackUtil.shortShow(mRecyclerView, R.string.load_null);
            return;
        }
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (ShopFriendsModel order : orders) {
            //创建会员状态
            Member member = new Member(order.getRetailerId(),order.getState());
            order.setMember(member);
            holders.add(new CustomerFriendsDataHolder(order));
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
}
