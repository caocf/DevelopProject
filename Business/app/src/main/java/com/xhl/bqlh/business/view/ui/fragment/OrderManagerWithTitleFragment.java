package com.xhl.bqlh.business.view.ui.fragment;

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
@ContentView(R.layout.fragment_order_product_statics_with_title)
public class OrderManagerWithTitleFragment extends BaseAppFragment {

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    private RecyclerAdapter mAdapter;

    private OrderQueryCondition mCondition;

    private OrderQueryHelper mHelper;

    @Override
    protected void initParams() {
        super.initToolbar();
        mToolbar.setTitle(R.string.menu_statistics_order);

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
        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHelper.onVisibleLoad();
            }
        }, 300);
    }

    @Override
    public void onEnter(Object data) {
        if (data != null) {
            mCondition = (OrderQueryCondition) data;
        }
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
