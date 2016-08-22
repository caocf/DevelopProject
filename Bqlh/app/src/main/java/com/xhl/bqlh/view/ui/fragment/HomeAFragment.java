package com.xhl.bqlh.view.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.data.PreferenceData;
import com.xhl.bqlh.model.AdInfoModel;
import com.xhl.bqlh.model.AdModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.utils.barcode.ui.CaptureActivity;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.FragmentContainerHelper;
import com.xhl.bqlh.view.helper.pub.PageScrollListener;
import com.xhl.bqlh.view.ui.recyclerHolder.home.HomeHotSuggestDataHlder;
import com.xhl.bqlh.view.ui.recyclerHolder.home.HomeLDataHolder;
import com.xhl.bqlh.view.ui.recyclerHolder.home.HomeLooperAdDataHolder;
import com.xhl.bqlh.view.ui.recyclerHolder.home.HomeMenuDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sum on 16/7/1.
 * 首页
 */
@ContentView(R.layout.fragment_home_a_home)
public class HomeAFragment extends BaseAppFragment {

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout swipe_refresh_layout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recycler_view;

    @ViewInject(R.id.tv_bar_location)
    private TextView tv_bar_location;

    @ViewInject(R.id.iv_scroll_top)
    private View iv_scroll_top;

    @Event(R.id.iv_scroll_top)
    private void scrollTopClick(View v) {
        if (mLayoutManager.findLastVisibleItemPosition() > 7) {
            mLayoutManager.scrollToPosition(2);
            mLayoutManager.smoothScrollToPosition(recycler_view, null, 0);
        } else {
            mLayoutManager.smoothScrollToPosition(recycler_view, null, 0);
        }
    }

    @Event(R.id.fl_scan)//扫码
    private void onScanClick(View view) {
        startActivity(new Intent(getContext(), CaptureActivity.class));
    }

    @Event(R.id.fl_search)//搜索
    private void onSearchClick(View view) {
        FragmentContainerHelper.startFragment(getContext(), FragmentContainerHelper.fragment_search);
    }

    @Event(R.id.tv_bar_location)//区域
    private void onAreaClick(View view) {
        FragmentContainerHelper.startFragment(getContext(), FragmentContainerHelper.fragment_area);
    }

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    private RecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean mIsLoading = false;

    @Override
    protected void initParams() {
        super.initRefreshStyle(swipe_refresh_layout, SwipeRefreshLayoutDirection.TOP);

        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerAdapter = new RecyclerAdapter(getContext());
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setAdapter(mRecyclerAdapter);
        PageScrollListener listener = new PageScrollListener(mLayoutManager);
        listener.setTopView(iv_scroll_top);
        recycler_view.addOnScrollListener(listener);
        loadArea();

        swipe_refresh_layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe_refresh_layout.setRefreshing(true);
                onRefreshLoadData();
            }
        }, 300);
    }

    private void loadArea() {

        boolean login = AppDelegate.appContext.isLogin();
        if (login) {
            tv_bar_location.setVisibility(View.GONE);
            getAppApplication().setArea(AppDelegate.appContext.getUserInfo().areaId);
        } else {
            //读取区域信息
            String areaId = PreferenceData.getInstance().areaId();
            getAppApplication().setArea(areaId);
            //默认淮北数据
            String areaName = PreferenceData.getInstance().areaName();
            if (TextUtils.isEmpty(areaName)) {
                areaName = "淮北市";
            }
            tv_bar_location.setText(areaName);
        }
    }

    @Override
    public void onRefreshLoadData() {
        super.onRefreshLoadData();
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        ApiControl.getApi().homeAd(new DefaultCallback<ResponseModel<HashMap<String, AdModel>>>() {
            @Override
            public void success(ResponseModel<HashMap<String, AdModel>> result) {
                networkErrorHide();

                HashMap<String, AdModel> obj = result.getObj();

                loadAd(obj);
            }

            @Override
            public void finish() {
                swipe_refresh_layout.setRefreshing(false);
                mIsLoading = false;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                networkErrorShow();
            }
        });

    }

    //广告数据
    public void loadAd(HashMap<String, AdModel> ads) {

        List<RecyclerDataHolder> holders = new ArrayList<>();

        //轮播位置
        AdModel adPositionId201001001 = ads.get("adPositionId201001001");
        holders.add(new HomeLooperAdDataHolder(adPositionId201001001));

        //菜单
        holders.add(new HomeMenuDataHolder(null));

        //热门推荐
        AdModel adPositionId201002001_1 = ads.get("adPositionId201002001");
        AdModel adPositionId201002002_2 = ads.get("adPositionId201002002");
        AdModel adPositionId201002003_3 = ads.get("adPositionId201002003");
        List<AdInfoModel> hotInfo = new ArrayList<>();
        hotInfo.add(adPositionId201002001_1.getAdvert());
        hotInfo.add(adPositionId201002002_2.getAdvert());
        hotInfo.add(adPositionId201002003_3.getAdvert());
        holders.add(new HomeHotSuggestDataHlder(hotInfo));

        //1L
        AdModel adPositionId201003001_0 = ads.get("adPositionId201003001");
        AdModel adPositionId201003002_1 = ads.get("adPositionId201003002");
        AdModel adPositionId201003003_2 = ads.get("adPositionId201003003");
        AdModel adPositionId201003004_3 = ads.get("adPositionId201003004");
        AdModel adPositionId201003005_4 = ads.get("adPositionId201003005");
        List<AdInfoModel> ad1 = new ArrayList<>();
        ad1.add(adPositionId201003001_0.getAdvert());
        ad1.add(adPositionId201003002_1.getAdvert());
        ad1.add(adPositionId201003003_2.getAdvert());
        ad1.add(adPositionId201003004_3.getAdvert());
        ad1.add(adPositionId201003005_4.getAdvert());
        holders.add(new HomeLDataHolder(ad1, 1));

        //2L
        AdModel adPositionId201004001_0 = ads.get("adPositionId201004001");
        AdModel adPositionId201004002_1 = ads.get("adPositionId201004002");
        AdModel adPositionId201004003_2 = ads.get("adPositionId201004003");
        AdModel adPositionId201004004_3 = ads.get("adPositionId201004004");
        AdModel adPositionId201004005_4 = ads.get("adPositionId201004005");
        List<AdInfoModel> ad2 = new ArrayList<>();
        ad2.add(adPositionId201004001_0.getAdvert());
        ad2.add(adPositionId201004002_1.getAdvert());
        ad2.add(adPositionId201004003_2.getAdvert());
        ad2.add(adPositionId201004004_3.getAdvert());
        ad2.add(adPositionId201004005_4.getAdvert());
        holders.add(new HomeLDataHolder(ad2, 2));

        //3L
        AdModel adPositionId201005005_0 = ads.get("adPositionId201005001");
        AdModel adPositionId201005005_1 = ads.get("adPositionId201005002");
        AdModel adPositionId201005005_2 = ads.get("adPositionId201005003");
        AdModel adPositionId201005005_3 = ads.get("adPositionId201005004");
        AdModel adPositionId201005005_4 = ads.get("adPositionId201005005");
        List<AdInfoModel> ad3 = new ArrayList<>();
        ad3.add(adPositionId201005005_0.getAdvert());
        ad3.add(adPositionId201005005_1.getAdvert());
        ad3.add(adPositionId201005005_2.getAdvert());
        ad3.add(adPositionId201005005_3.getAdvert());
        ad3.add(adPositionId201005005_4.getAdvert());
        holders.add(new HomeLDataHolder(ad3, 3));

        //4L
        AdModel adPositionId201006001_0 = ads.get("adPositionId201006001");
        AdModel adPositionId201006002_1 = ads.get("adPositionId201006002");
        AdModel adPositionId201006003_2 = ads.get("adPositionId201006003");
        AdModel adPositionId201006004_3 = ads.get("adPositionId201006004");
        AdModel adPositionId201006005_4 = ads.get("adPositionId201006005");
        List<AdInfoModel> ad4 = new ArrayList<>();
        ad4.add(adPositionId201006001_0.getAdvert());
        ad4.add(adPositionId201006002_1.getAdvert());
        ad4.add(adPositionId201006003_2.getAdvert());
        ad4.add(adPositionId201006004_3.getAdvert());
        ad4.add(adPositionId201006005_4.getAdvert());
        holders.add(new HomeLDataHolder(ad4, 4));


        //5L
        AdModel adPositionId201007001_0 = ads.get("adPositionId201007001");
        AdModel adPositionId201007002_1 = ads.get("adPositionId201007002");
        AdModel adPositionId201007003_2 = ads.get("adPositionId201007003");
        AdModel adPositionId201007004_3 = ads.get("adPositionId201007004");
        AdModel adPositionId201007005_4 = ads.get("adPositionId201007005");
        List<AdInfoModel> ad5 = new ArrayList<>();
        ad5.add(adPositionId201007001_0.getAdvert());
        ad5.add(adPositionId201007002_1.getAdvert());
        ad5.add(adPositionId201007003_2.getAdvert());
        ad5.add(adPositionId201007004_3.getAdvert());
        ad5.add(adPositionId201007005_4.getAdvert());
        holders.add(new HomeLDataHolder(ad5, 5));


        //6L
        AdModel adPositionId201008001 = ads.get("adPositionId201008001");
        AdModel adPositionId201008002 = ads.get("adPositionId201008002");
        AdModel adPositionId201008003 = ads.get("adPositionId201008003");
        AdModel adPositionId201008004 = ads.get("adPositionId201008004");
        AdModel adPositionId201008005 = ads.get("adPositionId201008005");
        List<AdInfoModel> ad6 = new ArrayList<>();
        ad6.add(adPositionId201008001.getAdvert());
        ad6.add(adPositionId201008002.getAdvert());
        ad6.add(adPositionId201008003.getAdvert());
        ad6.add(adPositionId201008004.getAdvert());
        ad6.add(adPositionId201008005.getAdvert());
        holders.add(new HomeLDataHolder(ad6, 6));

        //7L
        AdModel adPositionId201009001_0 = ads.get("adPositionId201009001");
        AdModel adPositionId201009002_1 = ads.get("adPositionId201009002");
        AdModel adPositionId201009003_2 = ads.get("adPositionId201009003");
        AdModel adPositionId201009004_3 = ads.get("adPositionId201009004");
        AdModel adPositionId201009005_4 = ads.get("adPositionId201009005");
        List<AdInfoModel> ad7 = new ArrayList<>();
        ad7.add(adPositionId201009001_0.getAdvert());
        ad7.add(adPositionId201009002_1.getAdvert());
        ad7.add(adPositionId201009003_2.getAdvert());
        ad7.add(adPositionId201009004_3.getAdvert());
        ad7.add(adPositionId201009005_4.getAdvert());
        holders.add(new HomeLDataHolder(ad7, 7));

        mRecyclerAdapter.setDataHolders(holders);
    }

    @Override
    public void onEvent(CommonEvent event) {
        super.onEvent(event);
        if (event.getEventTag() == CommonEvent.ET_RELOAD_ADS ||
                event.getEventTag() == CommonEvent.ET_RELOAD_USER_INFO) {
            loadArea();
            swipe_refresh_layout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipe_refresh_layout.setRefreshing(true);
                    onRefreshLoadData();
                }
            }, 300);
        }
    }
}
