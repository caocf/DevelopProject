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

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppConfig.GlobalParams;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.BrandModel;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.app.SearchParams;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.FragmentContainerHelper;
import com.xhl.bqlh.view.helper.ViewHelper;
import com.xhl.bqlh.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.view.helper.pub.PageScrollListener;
import com.xhl.bqlh.view.ui.bar.SearchSortBar;
import com.xhl.bqlh.view.ui.fragment.HomeCarFragment;
import com.xhl.bqlh.view.ui.recyclerHolder.SearchBrandDataHolder;
import com.xhl.bqlh.view.ui.recyclerHolder.SearchProductDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/5.
 */
@ContentView(R.layout.activity_search_details)
public class SearchProductResActivity extends BaseAppActivity implements SearchSortBar.SearchSortListener,
        Callback.CommonCallback<ResponseModel<GarbageModel<ProductModel>>>, RecycleViewCallBack<BrandModel> {

    //进入的类型
    public static final String SEARCH_TYPE = "search_type";
    //输入的参数
    public static final String SEARCH_PARAMS = "search_params";

    public static final int SEARCH_TYPE_SCAN = 1;//扫码
    public static final int SEARCH_TYPE_INPUT = 2;//输入
    public static final int SEARCH_TYPE_CATEGORY = 3;//分类
    public static final int SEARCH_TYPE_BRAND = 4;//品牌

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

    @ViewInject(R.id.search_bar)
    private SearchSortBar search_bar;

    @ViewInject(R.id.iv_car)
    private ImageView iv_car;

    @ViewInject(R.id.input_text)
    private TextView input_text;

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    @ViewInject(R.id.iv_scroll_top)
    private View iv_scroll_top;

    @Event(R.id.iv_scroll_top)
    private void scrollTopClick(View v) {
        mLayoutManager.smoothScrollToPosition(recyclerView, null, 0);
    }

    @Event(R.id.fl_back)
    private void onBackClick(View view) {
        if (!mIsLoading) {
            finish();
        }
    }

    @Event(R.id.fl_car) // 购物车
    private void onShoppingCarClick(View view) {
        if (!AppDelegate.appContext.isLogin(this)) {
            return;
        }
        pushFragmentToBackStack(HomeCarFragment.class, 1);
    }

    @Event(R.id.fl_search)
    private void onSearchClick(View view) {
        FragmentContainerHelper.startFragment(this, FragmentContainerHelper.fragment_search);
    }

    private boolean mIsLoading = false;
    private boolean mBrandOk = false;

    //搜索关键字
    private int mSearchType;

    private SearchParams mCondition;
    private RecyclerAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mFilterAdapter;

    @Override
    protected boolean needRoot() {
        return false;
    }

    @Override
    protected void initParams() {

        super.initBadgeView(iv_car);
        mBadgeView.setBadgeMargin(5);

        super.initRefreshStyle(swipeRefreshLayout, SwipeRefreshLayoutDirection.BOTH);

        handIntent(getIntent());
        mLayoutManager = new LinearLayoutManager(this);
        PageScrollListener listener = new PageScrollListener(mLayoutManager);
        listener.setTopView(iv_scroll_top);

        recyclerView.addOnScrollListener(listener);
        mAdapter = new RecyclerAdapter(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mFilterAdapter = new RecyclerAdapter(this);
        recycler_view_filter.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
        recycler_view_filter.setAdapter(mFilterAdapter);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handIntent(intent);
    }

    private void handIntent(Intent intent) {
        mBrandOk = false;
        mSearchType = intent.getIntExtra(SEARCH_TYPE, -1);
        String mSearchParams = intent.getStringExtra(SEARCH_PARAMS);
        String shopId = intent.getStringExtra(GlobalParams.Intent_shop_id);
        //创建筛选条件
        mCondition = new SearchParams();
        if (mSearchType == SEARCH_TYPE_SCAN) {
            mCondition.sku = mSearchParams;
        } else if (mSearchType == SEARCH_TYPE_INPUT) {
            mCondition.productName = mSearchParams;
            //显示搜索内容
            input_text.setText(mSearchParams);
        } else if (mSearchType == SEARCH_TYPE_CATEGORY) {
            mCondition.categoryId = mSearchParams;
        } else if (mSearchType == SEARCH_TYPE_BRAND) {
            mCondition.brandId = mSearchParams;
        }

        //店铺id
        mCondition.shopId = shopId;

        //设置搜索条件
        search_bar.setSearchParams(mCondition);
        search_bar.setListener(this);
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
        //分页参数
        mCondition.pageNum = getPageIndex();
        mCondition.pageSize = getPageSize();
        loadSearchData(mCondition);
    }

    //加载搜索数据
    private void loadSearchData(SearchParams searchParams) {
        if (!mIsLoading) {
            mIsLoading = true;
            ApiControl.getApi().searchProduct(searchParams, this);
        }
    }

    private void loadBrand() {
        if (mBrandOk) {
            return;
        }
        ApiControl.getApi().searchProductBrand(mCondition, new DefaultCallback<ResponseModel<List<BrandModel>>>() {
            @Override
            public void success(ResponseModel<List<BrandModel>> result) {
                List<BrandModel> obj = result.getObj();
                if (obj != null && obj.size() > 0) {
                    List<RecyclerDataHolder> holders = new ArrayList<>();
                    //全部
                    BrandModel brandModel = new BrandModel();
                    brandModel.setBrandName("全部");
                    SearchBrandDataHolder firstH = new SearchBrandDataHolder(brandModel);
                    firstH.setCallBack(SearchProductResActivity.this);
                    holders.add(firstH);
                    //其他
                    for (BrandModel brand : obj) {
                        SearchBrandDataHolder holder = new SearchBrandDataHolder(brand);
                        holder.setCallBack(SearchProductResActivity.this);
                        holders.add(holder);
                    }
                    mFilterAdapter.setDataHolders(holders);
                    ViewHelper.setViewGone(tv_brand_null, true);
                } else {
                    ViewHelper.setViewGone(tv_brand_null, false);
                }
                mBrandOk = true;
            }

            @Override
            public void finish() {

            }
        });
    }

    @Override
    public void filter() {
        if (mDrawer != null) {
            mDrawer.openDrawer(GravityCompat.END);
            loadBrand();
        }
    }

    @Override
    public void search(final SearchParams searchParams) {
        loadSearchData(searchParams);
    }

    private void loadData(List<ProductModel> data) {
        if (data == null) {
            return;
        }
        List<RecyclerDataHolder> orders = new ArrayList<>();

        for (ProductModel pro : data) {
            orders.add(new SearchProductDataHolder(pro));
        }

        if (orders.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }
        if (getPageIndex() > 1) {
            mAdapter.addDataHolder(orders);
        } else {
            mAdapter.setDataHolders(orders);
        }
    }

    @Override
    public void onSuccess(ResponseModel<GarbageModel<ProductModel>> result) {
        if (result.isSuccess()) {
            List<ProductModel> resultData = result.getObj().getResultData();
            setTotalSize(result.getObj().getTotalResult());
            loadData(resultData);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ToastUtil.showToastShort(ex.getMessage());
        Logger.e(ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {
        mDrawer.openDrawer(GravityCompat.END);
    }

    @Override
    public void onFinished() {
        mIsLoading = false;
        swipeRefreshLayout.setRefreshing(false);
        hideLoadingDialog();
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
    public void onItemClick(int position, final BrandModel brandModel) {

        onBackPressed();

        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                mCondition.brandId = brandModel.getId();
                loadSearchData(mCondition);
                //设置品牌
                search_bar.updateBrand(brandModel.getBrandName());
            }
        }, DELAY_TIME);
    }
}
