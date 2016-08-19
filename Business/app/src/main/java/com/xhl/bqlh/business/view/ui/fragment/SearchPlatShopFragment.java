package com.xhl.bqlh.business.view.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.App.SearchShopModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ModelHelper;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.event.AutoLocationEvent;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.SearchDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sum on 16/4/21.
 */
@ContentView(R.layout.fragment_search_near_by)
public class SearchPlatShopFragment extends BaseAppFragment {


    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    private CloudManager cloudManager;

    private int mPageIndex = 0;

    @Override
    protected void initParams() {
        tv_text_null.setText("附近没有百企会员");

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.BOTTOM);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        cloudManager = CloudManager.getInstance();
        cloudManager.init(cloudListener);

        tv_text_null.setVisibility(View.VISIBLE);
    }

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cloudManager.init(null);
        cloudManager = null;
    }

    public void onEvent(AutoLocationEvent event) {
        search();
    }

    @Override
    public void onRefreshTop() {
        mPageIndex = 0;
        search();
    }

    @Override
    public void onRefreshMore() {
        mPageIndex++;
        search();
    }

    private void search() {
        String loc = GlobalParams.mSelfLatLngStr;
        if (TextUtils.isEmpty(loc)) {
            return;
        }
        NearbySearchInfo info = new NearbySearchInfo();
        info.radius = GlobalParams.BaiduMap_LBS_DISTANCE;
        info.geoTableId = GlobalParams.BaiduMap_LBS_TABLE;
        info.ak = GlobalParams.BaiduMap_LBS_AK;
        info.location = loc;
//        info.location = "117.03959623969,33.640779765682";
        info.pageIndex = mPageIndex;
        info.pageSize = 20;
        info.sortby = "distance:1";
        cloudManager.nearbySearch(info);
    }

    private CloudListener cloudListener = new CloudListener() {
        @Override
        public void onGetSearchResult(CloudSearchResult cloudSearchResult, int i) {

            mRefresh.setRefreshing(false);
            if (cloudSearchResult == null) {
                if (mPageIndex == 0) {
                    ViewHelper.setViewGone(tv_text_null, false);
                }
                return;
            }
            List<CloudPoiInfo> poiList = cloudSearchResult.poiList;
            if (poiList == null || poiList.size() == 0) {
                if (mPageIndex == 0) {
                    ViewHelper.setViewGone(tv_text_null, false);
                }
                return;
            }
            List<RecyclerDataHolder> holders = new ArrayList<>();

            List<SearchShopModel> shops = new ArrayList<>();
            for (CloudPoiInfo poi : poiList) {
                shops.add(change(poi));
            }

            //组合店铺数据
            ModelHelper.addMember2SearchShopModel(shops);

            for (SearchShopModel shop : shops) {
                holders.add(new SearchDataHolder(shop));
            }
            if (holders.size() == 0 && mPageIndex == 0) {
                ViewHelper.setViewGone(tv_text_null, false);
            } else {
                ViewHelper.setViewGone(tv_text_null, true);
            }
            if (mPageIndex > 0) {
                mAdapter.addDataHolder(holders);
            } else {
                mAdapter.setDataHolders(holders);
            }
        }

        @Override
        public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
            mRefresh.setRefreshing(false);
        }
    };

    private SearchShopModel change(CloudPoiInfo poi) {
        SearchShopModel model = new SearchShopModel();
        model.setDistance(String.valueOf(poi.distance));
        model.setLatitude(poi.latitude);
        model.setLongitude(poi.longitude);
        model.setShopType(1);
        //额外参数
        Map<String, Object> extras = poi.extras;
        String shopId = (String) extras.get("retailerId");
        String shopName = (String) extras.get("retailerName");
        String shopAddress = (String) extras.get("retailerAddress");
        String shopType = (String) extras.get("companyTypeName");
        model.setShopId(shopId);
        model.setShopName(shopName);
        model.setShopLocation(shopAddress);
        model.setCompanyType(shopType);

        return model;
    }

}
