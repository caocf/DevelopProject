package com.xhl.world.ui.main.home;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.RelativeLayout;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.data.PreferenceData;
import com.xhl.world.model.AdvListModel;
import com.xhl.world.model.AdvModel;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.ProductModel;
import com.xhl.world.ui.event.AdvEvent;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.fragment.BaseAppFragment;
import com.xhl.world.ui.main.home.recyclerHolder.BrandDataHolder;
import com.xhl.world.ui.main.home.recyclerHolder.ClassifyDataHolder;
import com.xhl.world.ui.main.home.recyclerHolder.GuessLikeTipDataHolder;
import com.xhl.world.ui.main.home.recyclerHolder.GuessYouLikeDataHolder;
import com.xhl.world.ui.main.home.recyclerHolder.SecondAdvDataHolder;
import com.xhl.world.ui.main.home.recyclerHolder.SeoulDataHolder;
import com.xhl.world.ui.main.home.recyclerHolder.SpecialSellDataHolder;
import com.xhl.world.ui.main.home.recyclerHolder.ThirdAdvDataHolder;
import com.xhl.world.ui.main.home.recyclerHolder.TopAdvDataHolder;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.world.ui.webUi.WebPageActivity;
import com.xhl.xhl_library.ui.recyclerview.OnPageScrollListener;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/2.
 */
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseAppFragment implements Callback.CommonCallback<ResponseModel<AdvListModel>> {

    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.home_root_recyclerView)
    private RecyclerView mRecycleView;

    @ViewInject(R.id.rl_null_show)
    private RelativeLayout rl_null_show;

    //滚动到顶部
    @ViewInject(R.id.ripple_scroll_to_top)
    private RippleView mRippleToTop;

    @Event(R.id.ripple_scroll_to_top)
    private void scrollTopClick(View v) {
        if (mLayoutManager.findLastVisibleItemPosition() > 8) {
            mLayoutManager.scrollToPosition(0);
        } else {
            mLayoutManager.smoothScrollToPosition(mRecycleView, null, 0);
        }
    }

    private RecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<RecyclerDataHolder> mDatas;

    private boolean isLoadingMore = false;
    private boolean isEnd = false;
    private int mCurPage = 0;
    private int mPageSize = 8;
    private boolean mHasAddGuessLike = false;

    @Override
    protected void initParams() {

        initPullRefresh();
        mRippleToTop.setVisibility(View.GONE);

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.offsetChildrenVertical(0);

        mRecyclerAdapter = new RecyclerAdapter(getContext());
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mRecyclerAdapter);

        mRecycleView.addOnScrollListener(new myPageScrollListener(mLayoutManager));
        //读取首页缓存数据
        readCache();
        //加载数据
        initData();
    }

    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setDistanceToTriggerSync(200);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    initData();
                }
            }
        });
    }

    //读取请求缓存
    private void readCache() {
        AdvListModel requestCache = PreferenceData.getInstance().getHomeRequestCache();
        if (requestCache == null) {
            showLoadingDialog();
        } else {
            CreateDataHolder(requestCache);
        }
    }

    //保存缓存
    private void saveCache(AdvListModel advs) {
        PreferenceData.getInstance().saveHomeRequestRespone(advs);
    }

    private void initData() {
        isEnd = false;
        mCurPage = 0;

        rl_null_show.setVisibility(View.GONE);

        ApiControl.getApi().homeAdvQueryList(this);

    }

    private void bottomDo() {
        if (isLoadingMore || isEnd) {
            return;
        }
        isLoadingMore = true;

        ApiControl.getApi().homeGuessLike(mCurPage, mPageSize, new Callback.CommonCallback<ResponseModel<Response<ProductModel>>>() {
            @Override
            public void onSuccess(ResponseModel<Response<ProductModel>> result) {
                if (result.isSuccess()) {
                    mCurPage++;
                    List<ProductModel> rows = result.getResultObj().getRows();
                    if (rows.size() <= 0) {
                        isEnd = true;
                        SnackMaker.shortShow(mSwipRefresh, R.string.bottom_tip);
                        return;
                    }
                    //猜你喜欢
                    if (!mHasAddGuessLike) {
                        mHasAddGuessLike = true;
                        GuessLikeTipDataHolder guessTip = new GuessLikeTipDataHolder(null);
                        mRecyclerAdapter.addDataHolder(guessTip);
                    }
                    addGuessLikeProduce(rows);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.longShow(mRecycleView, R.string.network_error);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isLoadingMore = false;
            }
        });
    }

    private void addGuessLikeProduce(List<ProductModel> data) {
        int num = data.size() / 2;

        for (int i = 0; i < num; i++) {
            List<ProductModel> models = new ArrayList<>();
            for (int j = i; j < i + 2; j++) {
                models.add(data.get(j));
            }
            GuessYouLikeDataHolder holder = new GuessYouLikeDataHolder(models);
            mRecyclerAdapter.addDataHolder(holder);
        }
    }

    private void CreateDataHolder(AdvListModel advs) {
        //保存缓存
        saveCache(advs);

        mDatas = new ArrayList<>();
        mHasAddGuessLike = false;
        //L1广告
        TopAdvDataHolder top = new TopAdvDataHolder(advs.getOne());
        mDatas.add(top);

        //分类
        ClassifyDataHolder classify = new ClassifyDataHolder(null);
        mDatas.add(classify);

//        //限时抢购
//        FlashSaleDataHolder flashSale = new FlashSaleDataHolder(getStrings());
//        mDatas.add(flashSale);
        if (advs.getTwo().size() > 0) {
            //L2广告
            SecondAdvDataHolder secAdv = new SecondAdvDataHolder(advs.getTwo());
            mDatas.add(secAdv);
        }

        //L3品牌广告
        BrandDataHolder brand = new BrandDataHolder(advs.getThree());
        brand.setAdName(advs.getName().getThreeName());//第三个广告位的名称
        mDatas.add(brand);

        //L4首尔广告
        SeoulDataHolder seoul = new SeoulDataHolder(advs.getFour());
        seoul.setAdName(advs.getName().getFourName());//第四个广告位的名称

        mDatas.add(seoul);


        if (advs.getFive().size() > 0) {
            //L5广告
            ThirdAdvDataHolder fiveAdv = new ThirdAdvDataHolder(advs.getFive());
            mDatas.add(fiveAdv);
        }

        //L6特卖广告
        if (advs.getSix().size() > 0) {
            SpecialSellDataHolder specialSell = new SpecialSellDataHolder(advs.getSix());
            specialSell.setAdName(advs.getName().getSixName());//第四个广告位的名称
            mDatas.add(specialSell);
        }

        mRecyclerAdapter.setDataHolders(mDatas);

    }


    @Override
    public void onResume()   {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSwipRefresh.setRefreshing(false);
    }

    //统一广告处理
    public void onEvent(AdvEvent event) {
        AdvModel ad = event.adv;
        if (ad.getMoldType().equals("1")) {//1:广告 2:商品
            if (URLUtil.isNetworkUrl(ad.getUrl())) {
                Intent intent = new Intent(mContext, WebPageActivity.class);
                intent.putExtra(WebPageActivity.TAG_URL, ad.getUrl());
                intent.putExtra(WebPageActivity.TAG_TITLE, ad.getDescribtion());
                getActivity().startActivity(intent);
            }
        } else if (ad.getMoldType().equals("2")) {
            if (!TextUtils.isEmpty(ad.getProductId())) {
                EventBusHelper.postProductDetails(ad.getProductId());
            }
        }
    }

    @Override
    public void onSuccess(ResponseModel<AdvListModel> result) {
        if (result.isSuccess()) {
            //创建数据
            CreateDataHolder(result.getResultObj());
        } else {
            Snackbar.make(mRecycleView, result.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        rl_null_show.setVisibility(View.VISIBLE);
        mRecyclerAdapter.setDataHolders(null);
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        mSwipRefresh.setRefreshing(false);
        hideLoadingDialog();
    }


    class myPageScrollListener extends OnPageScrollListener {


        public myPageScrollListener(LinearLayoutManager manager) {
            super(manager);
        }

        @Override
        public void onItemCount(int firstVisible, int visibleCount, int totalCount, int dx, int dy) {
            if (firstVisible > 1) {
                ViewUtils.changeViewVisible(mRippleToTop, true);
            } else {
                ViewUtils.changeViewVisible(mRippleToTop, false);
            }
        }

        @Override
        public void onPageScrolled(RecyclerView recyclerView) {
            bottomDo();
        }
    }


}
