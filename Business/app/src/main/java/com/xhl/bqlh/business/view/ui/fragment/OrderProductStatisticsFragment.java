package com.xhl.bqlh.business.view.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.App.ProductQueryCondition;
import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ProductQueryHelper;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ProductDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/20.
 * 统计商品
 */
@ContentView(R.layout.fragment_order_product_statics)
public class OrderProductStatisticsFragment extends BaseAppFragment {


    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private RecyclerAdapter mAdapter;

    private ProductQueryCondition mCondition;

    private ProductQueryHelper mHelper;

    public static OrderProductStatisticsFragment newInstance(ProductQueryCondition condition) {
        OrderProductStatisticsFragment fragment = new OrderProductStatisticsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", condition);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initParams() {
        mCondition = (ProductQueryCondition) getArguments().getSerializable("data");
        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.TOP);
        mPresent.setPageDefaultDo(false);

        tv_text_null.setText("无商品");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mHelper = new ProductQueryHelper(this);

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHelper.onRefreshTop();
            }
        }, 100);

    }

    @Override
    public void onDestroy() {
        mHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRefreshTop() {
        mHelper.onRefreshTop();
    }

    @Override
    public void onRefreshMore() {
        mHelper.onRefreshMore();
    }

    @Override
    public Object getValue(int type) {
        if (type == ProductQueryHelper.TYPE_GET_CONDITION) {
            return mCondition;
        }
        return mCondition;
    }

    @Override
    public void showValue(int type, Object obj) {
        super.showValue(type, obj);

        if (type == ProductQueryHelper.TYPE_SHOW_PRODUCT) {
            List<ProductModel> products = (List<ProductModel>) obj;
            List<RecyclerDataHolder> data = createProductData(products);
            mAdapter.setDataHolders(data);
        } else if (type == ProductQueryHelper.TYPE_SHOW_PRODUCT_ADD) {
            List<ProductModel> products = (List<ProductModel>) obj;
            List<RecyclerDataHolder> data = createProductData(products);
            mAdapter.addDataHolder(data);
        } else if (type == ProductQueryHelper.TYPE_SHOW_REFRESHING) {
            if (!mRefresh.isRefreshing()) {
                mRefresh.setRefreshing(true);
            }
        } else if (type == ProductQueryHelper.TYPE_SHOW_REFRESHING_FINISH) {
            mRefresh.setRefreshing(false);
        } else if (type == ProductQueryHelper.TYPE_SHOW_NULL) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else if (type == ProductQueryHelper.TYPE_SHOW_NULL_HIDE) {
            ViewHelper.setViewGone(tv_text_null, true);
        }
    }

    private List<RecyclerDataHolder> createProductData(List<ProductModel> products) {

        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (ProductModel product : products) {
            ProductDataHolder holder = new ProductDataHolder(product);
            holders.add(holder);
        }
        return holders;
    }


}
