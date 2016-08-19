package com.xhl.bqlh.view.ui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppConfig.GlobalParams;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.ShopModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.bqlh.view.ui.bar.SearchShopItemBar;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.ui.recyclerview.line.DividerItemDecoration;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2016/7/18.
 */
@ContentView(R.layout.activity_search_shop)
public class SearchShopResActivity extends BaseAppActivity {

    @ViewInject(R.id.tv_text_null)
    private View mNullView;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    private String mSearchContent;

    private RecyclerAdapter mAdapter;

    private Callback.Cancelable mCall;

    @Override
    protected void initParams() {
        mSearchContent = getIntent().getStringExtra(GlobalParams.Intent_serach_text);

        super.initBackBar("店铺搜索", true, false);

        super.initRefreshStyle(swipeRefreshLayout, SwipeRefreshLayoutDirection.TOP);

        mAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefreshLoadData();
            }
        }, DELAY_TIME);
    }

    @Override
    public void onRefreshLoadData() {

        mCall = ApiControl.getApi().searchShop(mSearchContent, new DefaultCallback<ResponseModel<GarbageModel<ShopModel>>>() {
            @Override
            public void success(ResponseModel<GarbageModel<ShopModel>> result) {
                GarbageModel<ShopModel> obj = result.getObj();
                List<ShopModel> resultData = obj.getResultData();
                loadSearData(resultData);
            }

            @Override
            public void finish() {
                swipeRefreshLayout.setRefreshing(false);
                mCall = null;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCall != null) {
            mCall.cancel();
        }
    }

    private void loadSearData(List<ShopModel> data) {

        if (data == null || data.size() == 0) {
            ViewHelper.setViewGone(mNullView, false);
            return;
        } else {
            ViewHelper.setViewGone(mNullView, true);
        }

        List<RecyclerDataHolder> holders = new ArrayList<>();
        int i = 0;
        for (ShopModel shop : data) {
            holders.add(new ShopDataHolder(shop, i++));
        }

        mAdapter.setDataHolders(holders);
    }


    private static class ShopDataHolder extends RecyclerDataHolder<ShopModel> {
        private int type;

        public ShopDataHolder(ShopModel data, int type) {
            super(data);
            this.type = type;
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
            return new RecyclerViewHolder(new SearchShopItemBar(context));
        }

        @Override
        public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ShopModel data) {
            SearchShopItemBar bar = (SearchShopItemBar) vHolder.itemView;
            bar.onBindData(data);
        }
    }
}
