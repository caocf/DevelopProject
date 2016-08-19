package com.xhl.bqlh.business.view.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.App.OrderQueryCondition;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.OrderQueryHelper;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Sum on 16/4/15.
 */
@ContentView(R.layout.fragment_order_product_statics)
public class OrderManagerFragment extends BaseAppFragment{

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    private RecyclerAdapter mAdapter;
    private boolean mHasLoad = false;

    private OrderQueryCondition mCondition;

    private OrderQueryHelper mHelper;

    public static OrderManagerFragment newInstance(OrderQueryCondition condition) {
        OrderManagerFragment fragment = new OrderManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", condition);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initParams() {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("data")) {
            mCondition = (OrderQueryCondition) arguments.getSerializable("data");
        }

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.BOTH);
        mPresent.setPageDefaultDo(false);

        if (!TextUtils.isEmpty(mCondition.hint)) {
            tv_text_null.setText(mCondition.hint);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mHelper = new OrderQueryHelper(this);
    }


    @Override
    public void showValue(int type, Object obj) {
        super.showValue(type, obj);
        if (type == OrderQueryHelper.TYPE_SHOW_RES_SET) {
            List<RecyclerDataHolder> orders = (List<RecyclerDataHolder>) obj;
            if (orders.size() == 0) {
                ViewHelper.setViewGone(tv_text_null, false);
            } else {
                ViewHelper.setViewGone(tv_text_null, true);
            }
            mHasLoad = true;
            mAdapter.setDataHolders(orders);
        } else if (type == OrderQueryHelper.TYPE_SHOW_RES_ADD) {
            List<RecyclerDataHolder> orders = (List<RecyclerDataHolder>) obj;
            mAdapter.addDataHolder(orders);
        } else if (type == OrderQueryHelper.TYPE_SHOW_REFRESH_TRUE) {
            if (!mRefresh.isRefreshing()) {
                mRefresh.setRefreshing(true);
            }
        } else if (type == OrderQueryHelper.TYPE_SHOW_REFRESH_FALSE) {
            if (mRefresh.isRefreshing()) {
                mRefresh.setRefreshing(false);
            }
        } else if (type == OrderQueryHelper.TYPE_SHOW_CANCEL) {
            int pos = (int) obj;
            mAdapter.removeDataHolder(pos);
        }
    }

    @Override
    protected void onVisibleLoadData() {
        if (mHasLoad) {
            return;
        }
        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHelper.onVisibleLoad();
            }
        }, 300);
    }

    @Override
    public Object getValue(int type) {
        return mCondition;
    }

    @Override
    public void onRefreshTop() {
        mHelper.onRefreshTop();
    }

    @Override
    public void onRefreshMore() {
        mHelper.onRefreshMore();
    }
}
