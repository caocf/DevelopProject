package com.xhl.bqlh.business.view.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.StatisticsModel;
import com.xhl.bqlh.business.Model.StatisticsShopModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.activity.OrderSearchActivity;
import com.xhl.bqlh.business.view.ui.recyclerHolder.OrderPayShopDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.utils.NumberUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/5/15.
 */
@ContentView(R.layout.fragment_home_statistics_shop)
public class HomeStatisticsShopFragment extends BaseAppFragment implements Toolbar.OnMenuItemClickListener {

    @ViewInject(R.id.tv_text_null)
    private View tv_text_null;

    @ViewInject(R.id.tv_left_title)
    private TextView tv_left_title;

    @ViewInject(R.id.tv_left_money)
    private TextView tv_left_money;

    @ViewInject(R.id.tv_right_title)
    private TextView tv_right_title;

    @ViewInject(R.id.tv_right_money)
    private TextView tv_right_money;

    @ViewInject(R.id.tv_coupon_title)
    private TextView tv_coupon_title;

    @ViewInject(R.id.tv_coupon_money)
    private TextView tv_coupon_money;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean mIsQuerying = false;

    @Override
    protected void initParams() {
        super.initHomeToolbar();
        mToolbar.setTitle(R.string.user_nav_main_order_account);
        mToolbar.inflateMenu(R.menu.user_menu_order_manager);
        mToolbar.setOnMenuItemClickListener(this);

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.TOP);

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        tv_left_title.setText("订单总额");
        tv_right_title.setText("实收总额");
        tv_coupon_title.setText("赊账总额");

        loadData();
    }

    @Override
    public void onRefreshLoadData() {
        loadData();
    }

    private void loadData() {
        if (mIsQuerying) {
            return;
        }
        mIsQuerying = true;
        ApiControl.getApi().orderAccountStatistics(null, new Callback.CommonCallback<ResponseModel<StatisticsModel>>() {
            @Override
            public void onSuccess(ResponseModel<StatisticsModel> result) {
                if (result.isSuccess()) {
                    StatisticsModel obj = result.getObj();
                    //总额
                    tv_left_money.setText(obj.getTotalMoney());
                    //实收
                    tv_right_money.setText(obj.getRealOrderMoney());

                    List<StatisticsShopModel> shopList = obj.getShopList();
                    float allArrears = 0;
                    for (StatisticsShopModel shop : shopList) {
                        String arrears = shop.getArrears();

                        allArrears += Float.valueOf(arrears);
                    }
                    //赊账总额
                    tv_coupon_money.setText(NumberUtil.getDouble(allArrears));

                    createData(shopList);
                } else {
                    SnackUtil.shortShow(mRecyclerView, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(mRecyclerView, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mIsQuerying = false;
                mRefresh.setRefreshing(false);
            }
        });
    }

    private void createData(List<StatisticsShopModel> orders) {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (StatisticsShopModel order : orders) {
            holders.add(new OrderPayShopDataHolder(order));
        }

        if (holders.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }
        mAdapter.setDataHolders(holders);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent = new Intent(getContext(), OrderSearchActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
        return false;
    }
}
