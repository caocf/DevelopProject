package com.xhl.world.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.xhl.world.R;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.mvp.domain.OrderManagerUserCaseControl;
import com.xhl.world.mvp.domain.back.ErrorEvent;
import com.xhl.world.mvp.presenters.OrderManagerPresenter;
import com.xhl.world.mvp.views.OrderManagerView;
import com.xhl.world.ui.event.OrderManagerEvent;
import com.xhl.world.ui.event.ReLoadEvent;
import com.xhl.world.ui.recyclerHolder.OrderManagerItemDataHolder;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.utils.ViewUtils;
import com.xhl.world.ui.view.myLoadRelativeLayout;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/31.
 * 实例化5个管理订单对象，viewpager懒加载fragment
 */
@ContentView(R.layout.fragment_manager_order)
public class OrderManagerFragment extends BaseAppFragment implements OrderManagerView {

    @ViewInject(R.id.order_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.load_content)
    private myLoadRelativeLayout mLoadView;

    @ViewInject(R.id.rl_null_show)
    private RelativeLayout mOrderNullShow;

    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;
    private int mTag = -1;
    private boolean mIsFirstLoad = true;//首次加载显示不同的view
    private boolean mIsNeedLoadData = true;//view创建完成后，状态是当前可见的
    private boolean mIsPrepared = false;//view是否已经创建完成，可以加载数据
    private boolean mHasLoadOnce = false;

    private OrderManagerPresenter mPresenter;

    public static OrderManagerFragment instance(int tag) {
        OrderManagerFragment fragment = new OrderManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tag", tag);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTag = getArguments().getInt("tag");
    }

    @Override
    protected void initParams() {

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        initPullRefresh();
        //初始化代理
        mPresenter = new OrderManagerPresenter(new OrderManagerUserCaseControl());
        mPresenter.attachView(this);
    }

    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    mPresenter.onStart();
//                    mPresenter.actionWatchCommerce(null);
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsPrepared = true;
        loadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {//获取fragment的可见状态
            mIsNeedLoadData = true;
            loadData();
        } else {
            mIsNeedLoadData = false;
        }
    }

    //首次加载和多次加载
    private void loadData() {
        if (mIsPrepared && !mHasLoadOnce) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
        hideLoadingView();
    }

    @Override
    public void showNullView() {
        mIsFirstLoad = false;
        //隐藏刷新
        mSwipRefresh.setRefreshing(false);
        //显示空订单
        ViewUtils.changeViewVisible(mOrderNullShow, true);

        mRecyclerAdapter.setDataHolders(null);
    }

    @Override
    public void hideNullView() {
        ViewUtils.changeViewVisible(mOrderNullShow, false);
    }

    @Override
    public int viewTag() {
        return mTag;
    }

    @Override
    public void showTint(String error) {
        if (!TextUtils.isEmpty(error)) {
            if (error.equals(ErrorEvent.NETWORK_ERROR)) {
                SnackMaker.shortShow(mSwipRefresh, R.string.network_error);
            } else if (error.equals(ErrorEvent.NO_MORE_ERROR)) {
                SnackMaker.shortShow(mSwipRefresh, R.string.bottom_tip);
            } else {
                SnackMaker.shortShow(mSwipRefresh, error);
            }
        }
    }

    @Override
    public void setLoadData(List<Order> data) {
        mSwipRefresh.setRefreshing(false);
        mIsFirstLoad = false;
        mHasLoadOnce = true;
        if (data == null || data.size() <= 0) {
            return;
        }

        List<RecyclerDataHolder> holders = new ArrayList<>();

        for (Order order : data) {
            OrderManagerItemDataHolder holder = new OrderManagerItemDataHolder(order);
            holder.setViewTag(mTag);
            mRecyclerView.getRecycledViewPool().setMaxRecycledViews(mTag, 15);
            holders.add(holder);
        }
        mRecyclerAdapter.setDataHolders(holders);
    }

    @Override
    public void addLoadData(List<Order> data) {

        if (data == null || data.size() <= 0) {
            return;
        }

        List<RecyclerDataHolder> holders = new ArrayList<>();

        for (Order order : data) {
            OrderManagerItemDataHolder holder = new OrderManagerItemDataHolder(order);
            holders.add(holder);
        }
        mRecyclerAdapter.addDataHolder(holders);
    }

    @Override
    public boolean needLoadData() {
        return mIsNeedLoadData;
    }

    @Override
    public void showLoadingView() {
//        mLoadView.showLoadingView();
        showLoadingDialog();
    }

    @Override
    public void hideLoadingView() {
//        mLoadView.closeLoadingView();
        hideLoadingDialog();
    }

    @Override
    public void showReLoadView() {
//        mLoadView.showNetWorkErrorView();
    }

    @Override
    public boolean isFirstLoading() {
        return mIsFirstLoad;
    }

    @Override
    public Context getViewContext() {
        return mContext;
    }

    //重新加载
    public void onEvent(ReLoadEvent event) {
        mPresenter.onResume();
    }

    //订单管理事件统一处理
    public void onEvent(final OrderManagerEvent event) {
        Logger.v(event.getView_tag() + " action:" + event.getOrder_action());
        if (mTag != event.getView_tag()) {
            return;
        }
        final Order order = event.getOrder();
        switch (event.getOrder_action()) {
            case OrderManagerEvent.ORDER_ACTION_PAY://支付
                mPresenter.actionPayOrder(order);
                break;
            case OrderManagerEvent.ORDER_ACTION_CANCEL://取消订单
                mPresenter.actionCancelOrder(order);
                break;
            case OrderManagerEvent.ORDER_ACTION_sh://确认收货
                mPresenter.actionConfirmOrder(order);
                break;
            case OrderManagerEvent.ORDER_ACTION_wl://物流
                mPresenter.actionWatchCommerce(order);
                break;
            case OrderManagerEvent.ORDER_ACTION_tx://提醒发货
//                mPresenter.actionHintSend(order);
                mLoadView.showLoadingView();
                mLoadView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SnackMaker.shortShow(mSwipRefresh, "已提醒卖家发货");
                        mLoadView.closeLoadingView();
                    }
                }, 1000);
                break;
            case OrderManagerEvent.ORDER_ACTION_pj://评价
                mPresenter.actionGoJudge(order, event.getOrderDetail());
                break;
            case OrderManagerEvent.ORDER_ACTION_th://退货
                mPresenter.actionApply(order, event.getOrderDetail());
                break;
            case OrderManagerEvent.ORDER_ACTION_DETAILS://订单详情
                mPresenter.actionOrderDetails(order);
                break;
            case OrderManagerEvent.ORDER_ACTION_SINGLE_DETAILS://单个订单详情
                mPresenter.actionOrderDetails(order);
                break;
        }
    }
}
