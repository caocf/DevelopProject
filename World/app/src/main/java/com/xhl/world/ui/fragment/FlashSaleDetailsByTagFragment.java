package com.xhl.world.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhl.world.R;
import com.xhl.world.model.Entities.BaseTargetEntities;
import com.xhl.world.model.Entities.FlashSaleDetailsEntities;
import com.xhl.world.model.FlashSaleItemDetailsModel;
import com.xhl.world.mvp.domain.FlashSaleUserCaseController;
import com.xhl.world.mvp.presenters.FlashSalePresenter;
import com.xhl.world.mvp.views.FlashSaleView;
import com.xhl.world.ui.recyclerHolder.FlashSaleItemDetailsDataHolder;
import com.xhl.world.ui.recyclerHolder.FlashSaleItemDetailsLeftTimeDataHolder;
import com.xhl.world.ui.recyclerHolder.FlashSaleItemDetailsTopImageDataHolder;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.view.myLoadRelativeLayout;
import com.xhl.world.ui.view.pub.PageScrollListener;
import com.xhl.world.ui.view.pub.callback.RecyclerViewScrollBottomListener;
import com.xhl.xhl_library.Base.BaseFragment;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/12.
 */
@ContentView(R.layout.fragment_flash_sale_details_by_tag)
public class FlashSaleDetailsByTagFragment extends BaseFragment implements FlashSaleView<FlashSaleItemDetailsModel>, RecyclerViewScrollBottomListener {

    @ViewInject(R.id.bar_flash_sale_details_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.rl_content)
    private myLoadRelativeLayout mRlContent;

    //滚动到顶部
    @ViewInject(R.id.ripple_scroll_to_top)
    private RippleView mRippleToTop;

    @Event(value = R.id.ripple_scroll_to_top, type = RippleView.OnRippleCompleteListener.class)
    private void scrollTopClick(View v) {
        mRecyclerView.smoothScrollToPosition(0);
    }


    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;

    private FlashSaleDetailsEntities mData;//查询条件
    private BaseTargetEntities mTopImageData;//顶部广告图片

    private FlashSalePresenter mPresenter;
    private boolean isFirstLoading = true;

    public static FlashSaleDetailsByTagFragment newInstance(FlashSaleDetailsEntities entities) {
        FlashSaleDetailsByTagFragment fragment = new FlashSaleDetailsByTagFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", entities);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mData = (FlashSaleDetailsEntities) getArguments().get("data");
        if (mData == null) {
            throw new RuntimeException("FlashSaleDetailsEntities is null");
        }
        //顶部最小单位的一个广告实体
        mTopImageData = mData.getBase_entities();

        //抢购代理类
        mPresenter = new FlashSalePresenter(new FlashSaleUserCaseController());

        mPresenter.attachView(this);


    }

    @Override
    protected void initParams() {
        initPullRefresh();
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        PageScrollListener listener = new PageScrollListener(mLayoutManager);
        listener.setTopView(mRippleToTop);
        listener.addBottomListener(this);
        mRecyclerView.addOnScrollListener(listener);

        mPresenter.onStart();
    }

    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setDistanceToTriggerSync(200);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    mPresenter.refreshTop();
                    mSwipRefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }


    @Override
    public void setLoadData(List<FlashSaleItemDetailsModel> data) {
        if (data != null) {
            isFirstLoading = false;
            List<RecyclerDataHolder> mDatas = new ArrayList<>();
            if (mTopImageData != null) {
                FlashSaleItemDetailsTopImageDataHolder imageDataHolder = new FlashSaleItemDetailsTopImageDataHolder(mTopImageData);
                mDatas.add(imageDataHolder);
            }
            FlashSaleItemDetailsLeftTimeDataHolder leftTimeDataHolder = new FlashSaleItemDetailsLeftTimeDataHolder(mData);

            mDatas.add(leftTimeDataHolder);

            for (FlashSaleItemDetailsModel model : data) {
                mDatas.add(new FlashSaleItemDetailsDataHolder(model));
            }
            mRecyclerAdapter.setDataHolders(mDatas);
        }
    }

    @Override
    public void appendLoadData(List<FlashSaleItemDetailsModel> data) {
        List<RecyclerDataHolder> mDatas = new ArrayList<>();
        for (FlashSaleItemDetailsModel model : data) {
            mDatas.add(new FlashSaleItemDetailsDataHolder(model));
        }
        mRecyclerAdapter.addDataHolder(mDatas);
    }

    @Override
    public void onLoadFinish(String msg) {
        if (msg != null) {
            SnackMaker.shortShow(mSwipRefresh,msg);
        }
    }

    @Override
    public void showLoadingView() {
        mRlContent.showLoadingView();
    }

    @Override
    public void hideLoadingView() {
        mRlContent.closeLoadingView();
    }

    @Override
    public void showReLoadView() {
        mRlContent.showNetWorkErrorView();
    }

    @Override
    public boolean isFirstLoading() {
        return isFirstLoading;
    }

    @Override
    public void onScrollBottom() {
        mPresenter.refreshBottom();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }
}
