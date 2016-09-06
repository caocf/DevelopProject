package com.xhl.bqlh.view.ui.activity;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.AppConfig.GlobalParams;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.doman.ShopHelper;
import com.xhl.bqlh.model.event.ShopEvent;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.bqlh.view.ui.fragment.HomeCarFragment;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Sum on 16/7/2.
 */
@ContentView(R.layout.activity_details_shop)
public class ShopDetailsActivity extends BaseAppActivity {


    @ViewInject(R.id.main_drawer_layout)
    private DrawerLayout mDrawer;//侧滑容器

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    @ViewInject(R.id.recycler_view_filter)//过滤数据
    private RecyclerView recycler_view_filter;

    @ViewInject(R.id.tv_brand_null)
    private View tv_brand_null;

    @ViewInject(R.id.tv_brand)
    private TextView tv_brand;

    @ViewInject(R.id.iv_car)
    private ImageView iv_car;

    @Event(R.id.fl_car) // 购物车
    private void onShoppingCarClick(View view) {
        if (!AppDelegate.appContext.isLogin(this)) {
            return;
        }
        pushFragmentToBackStack(HomeCarFragment.class, 1);
    }

    private RecyclerAdapter mAdapter;
    private RecyclerAdapter mFilterAdapter;
    private String mShopId;
    private ShopHelper mHelper;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handIntent();
    }

    @Override
    protected void initParams() {

        super.initBackBar("店铺详情", true);

        super.initBadgeView(iv_car);
        mBadgeView.setBadgeMargin(4);

        super.initRefreshStyle(swipeRefreshLayout, SwipeRefreshLayoutDirection.BOTH);
        getPresent().setPageDefaultDo(false);

        mAdapter = new RecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        mFilterAdapter = new RecyclerAdapter(this);
        recycler_view_filter.setLayoutManager(new GridLayoutManager(this, 3));
        recycler_view_filter.setAdapter(mFilterAdapter);

        handIntent();
    }

    private void handIntent() {
        mShopId = getIntent().getStringExtra("id");
        mHelper = new ShopHelper(this);
        mHelper.onCreate();
    }

    @Override
    public Object getValue(int type) {
        if (type == ShopHelper.TYPE_GET_SHOP_ID) {
            return mShopId;
        }
        if (type == ShopHelper.TYPE_GET_BRAND_ID) {
            return getIntent().getStringExtra("brandId");
        }
        return super.getValue(type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHelper.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.onDestroy();
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
    protected boolean needRoot() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showValue(int type, Object obj) {
        super.showValue(type, obj);

        if (type == ShopHelper.TYPE_RES_ALL_DATA) {
            List<RecyclerDataHolder> holders = (List<RecyclerDataHolder>) obj;
            mAdapter.setDataHolders(holders);
        } else if (type == ShopHelper.TYPE_RES_ALL_DATA_MORE) {
            List<RecyclerDataHolder> holders = (List<RecyclerDataHolder>) obj;
            mAdapter.addDataHolder(holders);
        } else if (type == ShopHelper.TYPE_RES_FILTER_CATEGORY_DATA) {
            if (obj == null) {
                ViewHelper.setViewGone(tv_brand_null, false);
                mFilterAdapter.setDataHolders(null);
            } else {
                List<RecyclerDataHolder> holders = (List<RecyclerDataHolder>) obj;
                if (holders.size() == 0) {
                    ViewHelper.setViewGone(tv_brand_null, false);
                } else {
                    ViewHelper.setViewGone(tv_brand_null, true);
                }
                recycler_view_filter.setLayoutManager(new LinearLayoutManager(this));
                mFilterAdapter.setDataHolders(holders);
            }

        } else if (type == ShopHelper.TYPE_RES_FILTER_BRAND_DATA) {
            if (obj == null) {
                ViewHelper.setViewGone(tv_brand_null, false);
                mFilterAdapter.setDataHolders(null);
            } else {
                List<RecyclerDataHolder> holders = (List<RecyclerDataHolder>) obj;
                if (holders.size() == 0) {
                    ViewHelper.setViewGone(tv_brand_null, false);
                } else {
                    ViewHelper.setViewGone(tv_brand_null, true);
                }
                recycler_view_filter.setLayoutManager(new GridLayoutManager(this, 3));
                mFilterAdapter.setDataHolders(holders);
            }

        } else if (type == ShopHelper.TYPE_RES_FILTER_OPEN) {
            mDrawer.openDrawer(GravityCompat.END);
        } else if (type == ShopHelper.TYPE_RES_SHOW_LOADING) {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        } else if (type == ShopHelper.TYPE_RES_HIED_LOADING) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    public void onEvent(ShopEvent event) {
        if (event.getTag() == ShopEvent.TAG_BRAND) {
            tv_brand.setText("全部品牌");
            mHelper.onShopBrandClick();
        } else if (event.getTag() == ShopEvent.TAG_CATEGORY) {
            tv_brand.setText("全部分类");
            mHelper.onShopCategoryClick();
        } else if (event.getTag() == ShopEvent.TAG_SERVICE) {
            mHelper.onShopServiceClick();
        } else if (event.getTag() == ShopEvent.TAG_PRODUCT) {
            mHelper.onShopProductClick();
        } else if (event.getTag() == ShopEvent.TAG_SEARCH_BRAND) {
            Intent intent = new Intent(this, SearchProductResActivity.class);
            intent.putExtra(SearchProductResActivity.SEARCH_TYPE, SearchProductResActivity.SEARCH_TYPE_BRAND);
            intent.putExtra(SearchProductResActivity.SEARCH_PARAMS, event.filterId);
            intent.putExtra(GlobalParams.Intent_shop_id, mShopId);
            startActivity(intent);
        }
        /*else if (event.getTag() == ShopEvent.TAG_SEARCH_CATEGORY) {
            Intent intent = new Intent(this, SearchProductResActivity.class);
            intent.putExtra(SearchProductResActivity.SEARCH_TYPE, SearchProductResActivity.SEARCH_TYPE_CATEGORY);
            intent.putExtra(SearchProductResActivity.SEARCH_PARAMS, event.filterId);
            intent.putExtra(GlobalParams.Intent_shop_id, mShopId);
            startActivity(intent);
        }*/
    }
}
