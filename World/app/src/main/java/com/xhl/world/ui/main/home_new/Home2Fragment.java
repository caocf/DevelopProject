package com.xhl.world.ui.main.home_new;

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
import com.xhl.world.model.AdvHTest;
import com.xhl.world.model.AdvListModel;
import com.xhl.world.model.AdvModel;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.ProductModel;
import com.xhl.world.ui.event.AdvEvent;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.fragment.BaseAppFragment;
import com.xhl.world.ui.main.home.recyclerHolder.TopAdvDataHolder;
import com.xhl.world.ui.main.home_new.holder.FreeDataHolder;
import com.xhl.world.ui.main.home_new.holder.GuessDataHolder;
import com.xhl.world.ui.main.home_new.holder.GuessTipDataHolder;
import com.xhl.world.ui.main.home_new.holder.Lopper2DataHolder;
import com.xhl.world.ui.main.home_new.holder.MenuDataHolder;
import com.xhl.world.ui.main.home_new.holder.Null2DataHolder;
import com.xhl.world.ui.main.home_new.holder.ProductADataHolder;
import com.xhl.world.ui.main.home_new.holder.ProductBDataHolder;
import com.xhl.world.ui.main.home_new.holder.SeasonBuyDataHolder;
import com.xhl.world.ui.main.home_new.holder.TimeBuyDataHolder;
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
 * Created by Summer on 2016/8/25.
 */
@ContentView(R.layout.fragment_home)
public class Home2Fragment extends BaseAppFragment implements Callback.CommonCallback<ResponseModel<AdvListModel>> {
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
                        mRecyclerAdapter.addDataHolder(new GuessTipDataHolder(null));
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
            GuessDataHolder holder = new GuessDataHolder(models);
            mRecyclerAdapter.addDataHolder(holder);
        }
    }

    private void CreateDataHolder(AdvListModel advs) {

        mDatas = new ArrayList<>();
        mHasAddGuessLike = false;

        List<AdvHTest> adv = testInfo.getAdv();

        //轮播
        mDatas.add(new TopAdvDataHolder(adv.get(0).getList()));
//        mDatas.add(new LopperDataHolder(adv.get(0)));
        //菜单
        mDatas.add(new MenuDataHolder(null));
        addNull();
        //轮播
//        mDatas.add(new SecondAdvDataHolder(advs.getTwo()));
        mDatas.add(new Lopper2DataHolder(adv.get(2)));
        addNull();
        //门店体验
        mDatas.add(new FreeDataHolder(adv.get(3)));
        addNull();
        //汇买上新
        mDatas.add(new ProductADataHolder(adv.get(4)));
        addNull();
        //限时购买
        mDatas.add(new TimeBuyDataHolder(adv.get(5)));
        addNull();
        //季节专项
        mDatas.add(new SeasonBuyDataHolder(adv.get(6)));
//        addNull();
        //茶果饮料
        mDatas.add(new ProductBDataHolder(adv.get(7)));
        addNull();
        //坚果谷物
        mDatas.add(new ProductADataHolder(adv.get(8)));
        addNull();
        //米面油脂
        mDatas.add(new ProductADataHolder(adv.get(9)));
        addNull();
        //年份果酒
        mDatas.add(new ProductBDataHolder(adv.get(10)));
        addNull();
        //烘培烹饪
        mDatas.add(new ProductADataHolder(adv.get(11)));
        addNull();

        mRecyclerAdapter.setDataHolders(mDatas);
    }

    private void addNull() {
        mDatas.add(new Null2DataHolder());
    }

    @Override
    public void onResume() {
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
