package com.xhl.bqlh.view.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.OrderModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.bqlh.view.ui.recyclerHolder.OrderQueryDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/12.
 */
@ContentView(R.layout.fragment_order_query)
public class OrderQueryFragment extends BaseAppFragment {

    public static OrderQueryFragment instance(int type) {
        OrderQueryFragment f = new OrderQueryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        f.setArguments(bundle);
        return f;
    }

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean mHasLoad = false;

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    //订单类型
    private int mType;
    private RecyclerAdapter mAdapter;

    @Override
    protected void initParams() {
        mType = getArguments().getInt("type");
        super.initRefreshStyle(swipeRefreshLayout, SwipeRefreshLayoutDirection.BOTH);

        mAdapter = new RecyclerAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 20;
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        setText();
    }

    private void setText() {
        if (mType == 2) {
            tv_text_null.setText("暂无待付款订单");
        } else if (mType == 3) {
            tv_text_null.setText("暂无待收货订单");
        } else if (mType == 4) {
            tv_text_null.setText("暂无待评价订单");
        }
    }

    @Override
    protected void onVisibleLoadData() {
        if (!mHasLoad) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    onRefreshLoadData();
                }
            }, 500);
        }
    }

    @Override
    public void onRefreshLoadData() {
        super.onRefreshLoadData();
        ApiControl.getApi().orderQuery(mType, getPageSize(), getPageIndex(), new DefaultCallback<ResponseModel<GarbageModel>>() {
            @Override
            public void success(ResponseModel<GarbageModel> result) {
                List orderList = result.getObj().getOrderList();
                setTotalSize(result.getPageSize());
                loadData(orderList);
                mHasLoad = true;
                networkErrorHide();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                networkErrorShow();
            }

            @Override
            public void finish() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadData(List<OrderModel> data) {

        List<RecyclerDataHolder> holders = new ArrayList<>();
        int i = 0;
        for (OrderModel order : data) {
            order.setViewType(mType);
            holders.add(new OrderQueryDataHolder(order, i++));
        }

        if (holders.size() == 0 && getPageIndex() == 1) {
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
    public void onEvent(CommonEvent event) {
        super.onEvent(event);
        if (event.getEventTag() == CommonEvent.ET_RELOAD_ORDER_INFO) {
            if (event.refresh_order_view == mType) {//当前页直接刷新，全部界面可见时刷新
                onRefreshLoadData();
                if (mType == 1) {
                    mHasLoad = false;
                }
            } else if (event.refresh_order_view == -1) {
                onRefreshLoadData();
            }
        }
    }
}
