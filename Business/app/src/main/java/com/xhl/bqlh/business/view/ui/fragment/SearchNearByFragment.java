package com.xhl.bqlh.business.view.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.utils.DistanceUtil;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.App.SearchShopModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.event.AutoLocationEvent;
import com.xhl.bqlh.business.view.ui.recyclerHolder.SearchDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.utils.NumberUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/3/22.
 */
@ContentView(R.layout.fragment_search_near_by)
public class SearchNearByFragment extends BaseAppFragment implements OnGetPoiSearchResultListener {


    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private PoiSearch mPoiSearch = null;
    private int mPageIndex = 0;
    private static int mPageNum = 20;
    private boolean mIsQuerying = false;
    public String key;

    private LatLng mCurLocation;

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPoiSearch.setOnGetPoiSearchResultListener(null);
        mPoiSearch = null;
    }

    public void onEvent(AutoLocationEvent event) {
        mCurLocation = event.latLng;
        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
                searchFor(key);
            }
        }, 100);

    }

    @Override
    protected void initParams() {
        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.BOTTOM);
        mCurLocation = GlobalParams.mSelfLatLng;

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Poi检索功能
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        key = "超市 商店 卖场 便利店";
        mPageIndex = 0;

        searchFor(key);
    }

    private void searchFor(String query) {
        if (TextUtils.isEmpty(query)) {
            mRefresh.setRefreshing(false);
            return;
        }
        Logger.v("搜索关键字:" + query);
        if (mCurLocation == null) {
            SnackUtil.shortShow(mView, R.string.locating);
            return;
        }
        if (mPoiSearch != null) {
            mPoiSearch.searchNearby(new PoiNearbySearchOption().location(mCurLocation).
                    keyword(query).pageNum(mPageIndex).pageCapacity(mPageNum).radius(10 * 1000).sortType(PoiSortType.distance_from_near_to_far));
        }
    }

    @Override
    public void onRefreshTop() {
        mPageIndex = 0;
        searchFor(key);
    }

    @Override
    public void onRefreshMore() {
        mPageIndex++;
        searchFor(key);
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        mRefresh.setRefreshing(false);
        if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }

        List<PoiInfo> allPoi = poiResult.getAllPoi();

        List<RecyclerDataHolder> holders = new ArrayList<>();

        for (PoiInfo poi : allPoi) {
            holders.add(new SearchDataHolder(change(poi)));
        }
        if (mPageIndex > 0) {
            int itemCount = mAdapter.getItemCount();
            mRecyclerView.smoothScrollToPosition(itemCount);
            mAdapter.addDataHolder(holders);
        } else {
            mAdapter.setDataHolders(holders);
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    private SearchShopModel change(PoiInfo poi) {
        SearchShopModel model = new SearchShopModel();
        double distance = DistanceUtil.getDistance(mCurLocation, poi.location);
        String noDecimal = NumberUtil.getDoubleNoDecimal(distance);
        model.setDistance(noDecimal);

        model.setShopName(poi.name);
        model.setShopLocation(poi.address);
        model.setLatitude(poi.location.latitude);
        model.setLongitude(poi.location.longitude);
        model.setShopType(0);
        return model;
    }


}
