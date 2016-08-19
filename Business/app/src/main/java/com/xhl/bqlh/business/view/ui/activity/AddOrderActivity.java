package com.xhl.bqlh.business.view.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.App.OrderQueryCondition;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.OrderDetail;
import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.Model.Type.OrderType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ModelHelper;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.event.SelectOrderEvent;
import com.xhl.bqlh.business.view.helper.EventHelper;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.OrderSelectDataHolder;
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
 * Created by Sum on 16/5/3.
 * 代发货订单
 */
@ContentView(R.layout.activity_add_order)
public class AddOrderActivity extends BaseAppActivity implements Toolbar.OnMenuItemClickListener {

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.tv_text_null)
    private TextView tv_text_null;

    private boolean mIsQuerying = false;
    private OrderQueryCondition mCondition;

    private RecyclerAdapter mAdapter;
    private List<OrderModel> mDatas;

    @Override
    protected void initParams() {
        super.initToolbar(TYPE_child_other_clear);
        setTitle(R.string.menu_store_wait);

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.TOP);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mAdapter = new RecyclerAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        mCondition = new OrderQueryCondition();
        mCondition.orderState = OrderType.order_state_not_send;
        mCondition.details = 1;
        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mRefresh.isRefreshing()) {
                    mRefresh.setRefreshing(true);
                }
                loadData();
            }
        }, 100);

    }

    private void loadData() {
        if (mIsQuerying) {
            return;
        }
        mIsQuerying = true;
        ApiControl.getApi().orderQueryDetail(new Callback.CommonCallback<ResponseModel<OrderModel>>() {
            @Override
            public void onSuccess(ResponseModel<OrderModel> result) {
                if (result.isSuccess()) {
                    createData(result.getObjList());
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

    private void createData(List<OrderModel> orders) {
        if (orders.size() == 0 && mPresent.mPageIndex > 1) {
            SnackUtil.shortShow(mToolbar, R.string.load_null);
            return;
        }
        mDatas = orders;

        List<RecyclerDataHolder> holders = new ArrayList<>();

        for (OrderModel order : orders) {
            order.isNeedShowType = true;
            OrderSelectDataHolder holder = new OrderSelectDataHolder(order);
            holders.add(holder);
        }

        if (mPresent.mPageIndex == 1 && holders.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }

        if (mPresent.mPageIndex > 1) {
            mAdapter.addDataHolder(holders);
        } else {
            mAdapter.setDataHolders(holders);
        }
    }

    @Override
    public void onRefreshLoadData() {
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mToolbar.inflateMenu(R.menu.menu_commit);
        mToolbar.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        List<OrderModel> datas = this.mDatas;
        if (datas == null) {
            return true;
        }
        String code = "";
        ArrayList<List<OrderDetail>> proList = new ArrayList<>();
        for (OrderModel order : datas) {
            if (order.isChecked) {
                List<OrderDetail> orderDetailList = order.getOrderDetailList();
                for (OrderDetail product : orderDetailList) {
                    code += product.getSalseOrderId();
                    code += ";";
                }
                proList.add(orderDetailList);
            }
        }
        if (proList.size() == 0) {
            SnackUtil.shortShow(mToolbar, "请勾选待发货订单");
            return true;
        }
        //发货商品
        ArrayList<List<ProductModel>> pro = new ArrayList<>();
        for (List<OrderDetail> list : proList) {
            List<ProductModel> productModels = ModelHelper.OrderDetail2ProductModel(list);
            pro.add(productModels);
        }
        //排重过滤商品
        List<ProductModel> modelList = ModelHelper.mergeProductModel(pro);
        SelectOrderEvent event = new SelectOrderEvent();
        event.orderProducts = modelList;
        event.storeCode = code;
        EventHelper.postDefaultEvent(event);

        finish();

        return true;
    }

}
