package com.xhl.bqlh.business.view.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.StoreApplyAddHelper;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.event.SelectOrderEvent;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.activity.ConfirmProductActivity;
import com.xhl.bqlh.business.view.ui.activity.SelectProductActivity;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/30.
 * 装车单申请
 */
@ContentView(R.layout.fragment_store_apply_product)
public class StoreApplyFragment extends BaseAppFragment {

    @ViewInject(R.id.tv_text_null)
    private View tv_text_null;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @Event(R.id.fab_add)
    private void onAddClick(View view) {
        //车销商品申请
        Intent intent = new Intent(getContext(), SelectProductActivity.class);
        intent.putExtra(ConfirmProductActivity.TYPE_PRODUCT_OPERATOR, ConfirmProductActivity.TYPE_CAR_CREATE_ADD);
        startActivity(intent);
    }

    private RecyclerAdapter mAdapter;
    private StoreApplyAddHelper mHelper;

    private String mOrderIds;

    @Override
    protected void initParams() {

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.TOP);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mHelper = new StoreApplyAddHelper(this);
        mHelper.loadCarLeftData();
    }

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    @Override
    public void onRefreshTop() {
        mHelper.loadCarLeftData();
    }

    public List<ApplyModel> getAllProducts() {
        return mHelper.getAllApplyProducts();
    }

    public String getOrderIds(){
        return mOrderIds;
    }

    //加载车销新增商品
    public void addCarProducts(ArrayList<ShopCarModel> cars) {
        mHelper.loadCarProducts(cars);
    }

    //选择的订单商品
    public void onEvent(SelectOrderEvent event) {
        mHelper.loadOrderProducts(event.orderProducts);
        mOrderIds = event.storeCode;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHelper.onDestroy();
    }

    @Override
    public Object getValue(int type) {
        return super.getValue(type);
    }

    @Override
    public void showValue(int type, Object obj) {
        super.showValue(type, obj);
        //最后的显示结果集
        if (type == StoreApplyAddHelper.TYPE_SHOW_RES) {
            List<RecyclerDataHolder> holders = (List<RecyclerDataHolder>) obj;
            if (holders.size() == 0) {
                ViewHelper.setViewGone(tv_text_null, false);
            } else {
                ViewHelper.setViewGone(tv_text_null, true);
            }
            mAdapter.setDataHolders(holders);
        } else if (type == StoreApplyAddHelper.TYPE_SHOW_REFRESH_TRUE) {
            if (!mRefresh.isRefreshing()) {
                mRefresh.setRefreshing(true);
            }
        } else if (type == StoreApplyAddHelper.TYPE_SHOW_REFRESH_FALSE) {
            if (mRefresh.isRefreshing()) {
                mRefresh.setRefreshing(false);
            }
        }
    }
}
