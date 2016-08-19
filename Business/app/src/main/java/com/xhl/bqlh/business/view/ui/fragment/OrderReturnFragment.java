package com.xhl.bqlh.business.view.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ProductReturn;
import com.xhl.bqlh.business.Model.Type.OrderReturnType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.OrderReturnDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2016/7/28.
 * 退货管理
 */
@ContentView(R.layout.fragment_order_product_statics)
public class OrderReturnFragment extends BaseAppFragment {

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    private RecyclerAdapter mAdapter;

    private boolean mHasLoad = false;

    public static OrderReturnFragment instance(int type) {
        OrderReturnFragment fragment = new OrderReturnFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int mType;

    @Override
    protected void initParams() {
        mType = getArguments().getInt("type", OrderReturnType.TYPE_APPLY);
        tv_text_null.setText("暂无相关数据");

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.BOTH);

        mAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    protected void onVisibleLoadData() {
        super.onVisibleLoadData();
        if (mHasLoad) {
            return;
        }
        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefreshLoadData();
            }
        }, 300);
    }

    @Override
    public void onRefreshLoadData() {
        super.onRefreshLoadData();
        if (!mRefresh.isRefreshing()) {
            mRefresh.setRefreshing(true);
        }
        ApiControl.getApi().productReturnQuery(getPageSize(), getPageIndex(), String.valueOf(mType), new DefaultCallback<ResponseModel<ProductReturn>>() {
            @Override
            public void success(ResponseModel<ProductReturn> result) {
                mHasLoad = true;
                List<ProductReturn> objList = result.getObjList();
                setTotalSize(result.getPageSize());
                loadData(objList);
            }

            @Override
            public void finish() {
                mRefresh.setRefreshing(false);
            }
        });
    }

    private void loadData(List<ProductReturn> data) {
        if (data.size() == 0 && getPageIndex() == 1) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (ProductReturn re : data) {
            holders.add(new OrderReturnDataHolder(re));
        }
        if (getPageIndex() > 1) {
            mAdapter.addDataHolder(holders);
        } else {
            mAdapter.setDataHolders(holders);
        }
    }
}
