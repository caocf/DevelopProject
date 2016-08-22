package com.xhl.bqlh.view.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.ShopModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.bqlh.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.view.ui.activity.CollectionActivity;
import com.xhl.bqlh.view.ui.recyclerHolder.CollectionProductDataHolder;
import com.xhl.bqlh.view.ui.recyclerHolder.CollectionShopDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2016/7/18.
 */
@ContentView(R.layout.fragment_collection)
public class CollectionFragment extends BaseAppFragment implements RecycleViewCallBack {

    public static CollectionFragment instance(int type) {
        CollectionFragment fragment = new CollectionFragment();

        Bundle bundle = new Bundle();

        bundle.putInt("type", type);

        fragment.setArguments(bundle);

        return fragment;
    }

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerAdapter mAdapter;

    private int mType;


    @Override
    protected void initParams() {

        mType = getArguments().getInt("type");

        super.initRefreshStyle(swipeRefreshLayout, SwipeRefreshLayoutDirection.BOTH);

        mAdapter = new RecyclerAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        onRefreshLoadData();
    }


    @Override
    public void onRefreshLoadData() {
        if (mType == CollectionActivity.TAG_PRODUCT) {
            tv_text_null.setText("暂无收藏商品");
            loadProduct();
        } else {
            tv_text_null.setText("暂无收藏店铺");
            loadShop();
        }
    }

    private void loadProduct() {
        ApiControl.getApi().collectQueryProduct(getPageSize(), getPageIndex(), new DefaultCallback<ResponseModel<GarbageModel<ProductModel>>>() {
            @Override
            public void success(ResponseModel<GarbageModel<ProductModel>> result) {
                List<ProductModel> list = result.getObj().getList();
                showProduct(list);
                setTotalSize(result.getPageSize());
                networkErrorHide();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                networkErrorShow();
                Logger.e(ex.getMessage());
            }

            @Override
            public void finish() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showProduct(List<ProductModel> data) {
        if (data == null || data.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
            return;
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }

        List<RecyclerDataHolder> ho = new ArrayList<>();

        for (ProductModel pro : data) {
            ho.add(new CollectionProductDataHolder(pro, this));
        }
        if (getPageIndex() > 1) {
            mAdapter.addDataHolder(ho);
        } else {
            mAdapter.setDataHolders(ho);
        }
    }

    private void loadShop() {
        ApiControl.getApi().collectQueryShop(getPageSize(), getPageIndex(), new DefaultCallback<ResponseModel<GarbageModel<ShopModel>>>() {
            @Override
            public void success(ResponseModel<GarbageModel<ShopModel>> result) {
                List<ShopModel> list = result.getObj().getList();
                setTotalSize(result.getPageSize());
                showShop(list);
                networkErrorHide();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                networkErrorShow();
                Logger.e(ex.getMessage());
            }

            @Override
            public void finish() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showShop(List<ShopModel> data) {
        if (data == null || data.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
            return;
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }
        List<RecyclerDataHolder> ho = new ArrayList<>();

        for (ShopModel pro : data) {
            ho.add(new CollectionShopDataHolder(pro, this));
        }

        mAdapter.setDataHolders(ho);
    }

    @Override
    public void onItemClick(int position, Object o) {
        mAdapter.removeDataHolder(position);
        EventHelper.postReLoadCollectionNumEvent();
    }
}
