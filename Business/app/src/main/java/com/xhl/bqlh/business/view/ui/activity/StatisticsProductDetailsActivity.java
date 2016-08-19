package com.xhl.bqlh.business.view.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.App.OrderQueryCondition;
import com.xhl.bqlh.business.Model.App.ProductQueryCondition;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.StatisticsModel;
import com.xhl.bqlh.business.Model.StatisticsProductModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.fragment.OrderManagerWithTitleFragment;
import com.xhl.bqlh.business.view.ui.recyclerHolder.OrderProductStatisticsDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/20.
 */
@ContentView(R.layout.activity_order_statistics_details)
public class StatisticsProductDetailsActivity extends BaseAppActivity {

    private ProductQueryCondition mCondition;

    @ViewInject(R.id.tv_time_hint)
    private TextView tv_time_hint;

    @ViewInject(R.id.tv_left_money)
    private TextView tv_left_money;

    @ViewInject(R.id.tv_right_money)
    private TextView tv_right_money;

    @ViewInject(R.id.tv_coupon_money)
    private TextView tv_coupon_money;

    @ViewInject(R.id.tv_text_null)
    private View tv_text_null;

    @ViewInject(R.id.view_coupon_line)
    private View view_coupon_line;
    @ViewInject(R.id.fl_coupon_content)
    private View fl_coupon_content;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private RecyclerAdapter mAdapter;

    private boolean mIsLoading = false;

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle("统计汇总");

        mCondition = (ProductQueryCondition) getIntent().getSerializableExtra("data");
        view_coupon_line.setVisibility(View.VISIBLE);
        fl_coupon_content.setVisibility(View.VISIBLE);
        String title = "统计时间: " + mCondition.startTime + " 至 " + mCondition.endTime;
        tv_time_hint.setText(title);

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.TOP);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true);
                loadData();
            }
        }, 200);
    }

    @Override
    public void onRefreshLoadData() {
        loadData();
    }

    private void loadData() {

        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        ApiControl.getApi().orderStatistics(mCondition, new Callback.CommonCallback<ResponseModel<StatisticsModel>>() {
            @Override
            public void onSuccess(ResponseModel<StatisticsModel> result) {
                if (result.isSuccess()) {
                    StatisticsModel obj = result.getObj();
                    //总额
                    tv_left_money.setText(obj.getTotalMoney());
                    //实收
                    tv_right_money.setText(obj.getRealOrderMoney());
                    //优惠金额
                    tv_coupon_money.setText(obj.getCouponsMoney());
                    //商品
                    createData(obj.getList());
                } else {
                    SnackUtil.longShow(mToolbar, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mIsLoading = false;
                mRefresh.setRefreshing(false);
            }
        });

    }

    private void createData(List<StatisticsProductModel> orders) {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (StatisticsProductModel order : orders) {
            holders.add(new OrderProductStatisticsDataHolder(order));
        }
        if (holders.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }
        mAdapter.setDataHolders(holders);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu_statistics_order, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        OrderQueryCondition condition = new OrderQueryCondition();
        condition.startTime = mCondition.startTime;
        condition.endTime = mCondition.endTime;
        pushFragmentToBackStack(OrderManagerWithTitleFragment.class, condition);
        return true;
    }

    @Override
    protected boolean needRoot() {
        return false;
    }
}
