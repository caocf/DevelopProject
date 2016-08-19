package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.CarModel;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.view.ui.activity.ShopDetailsActivity;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.ui.recyclerview.layoutManager.FullGridViewManager;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/8.
 */
public class CarItemBar extends BaseBar {

    public CarItemBar(Context context) {
        super(context);
    }

    public CarItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tv_active_hint)
    private TextView tv_active_hint;

    @ViewInject(R.id.tv_shop_name)
    private TextView tv_shop_name;

    @ViewInject(R.id.rb_parent_check_all)
    private CheckBox rb_parent_check_all;

    @Event(value = {R.id.tv_active_hint, R.id.tv_shop_name})
    private void onShopClick(View view) {
        if (!TextUtils.isEmpty(mShopId)) {
            Intent i = new Intent(mContext, ShopDetailsActivity.class);
            i.putExtra("id", mShopId);
            mContext.startActivity(i);
        }
    }

    @Event(R.id.rb_parent_check_all)
    private void onCheckedAllClick(View view) {
        boolean checked = rb_parent_check_all.isChecked();
        if (mData != null) {
            for (CarModel car : mData) {
                car.isChecked = checked;
            }
            mAdapter.notifyDataSetChanged();
        }
        //刷新父状态
        updateParent();
    }

    private RecycleViewCallBack mCallback;

    private RecyclerAdapter mAdapter;

    private String mShopId;

    private List<CarModel> mData;

    private boolean mHasBindProduct = false;

    @Override
    protected void initParams() {
        mAdapter = new RecyclerAdapter(getContext());
        recyclerView.setLayoutManager(new FullGridViewManager(getContext(), 1));
        recyclerView.setAdapter(mAdapter);
    }

    //更新状态
    public void updateSelectState() {
        boolean selectAll = true;
        for (CarModel car : mData) {
            if (!car.isChecked) {
                selectAll = false;
                break;
            }
        }
        rb_parent_check_all.setChecked(selectAll);
        updateParent();
    }

    private void updateParent() {
        if (mCallback != null) {
            mCallback.onItemClick(1, null);
        }
    }

    public void onBindData(List<CarModel> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        mData = data;
        //店铺信息
        CarModel carModel = data.get(0);
        mShopId = carModel.getProduct().getStoreId();
        tv_active_hint.setText(getResources().getString(R.string.shop_product_min_hint, carModel.getMinOrderPrice()));
        tv_shop_name.setText(carModel.getShopName());
        //选择状态
        updateSelectState();

        //该集合数据不用重复刷新，只需要刷新选中的状态即可
        if (!mHasBindProduct) {
            mHasBindProduct = true;
            //商品数据
            List<RecyclerDataHolder> holders = new ArrayList<>();
            for (CarModel car : data) {
                holders.add(new ItemProduct(car, this));
            }
            mAdapter.setDataHolders(holders);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_car_item;
    }

    public void setmCallback(RecycleViewCallBack mCallback) {
        this.mCallback = mCallback;
    }

    private static class ItemProduct extends RecyclerDataHolder<CarModel> {
        private CarItemBar mBar;

        public ItemProduct(CarModel data, CarItemBar bar) {
            super(data);
            mBar = bar;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
            return new RecyclerViewHolder(new CarItemProductBar(context, mBar));
        }

        @Override
        public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, CarModel data) {
            CarItemProductBar bar = (CarItemProductBar) vHolder.itemView;
            bar.onBindData(data);
        }

    }
}
