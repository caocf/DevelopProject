package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.model.OrderModel;
import com.xhl.bqlh.view.ui.bar.OrderItemBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/7/12.
 */
public class OrderQueryDataHolder extends RecyclerDataHolder<OrderModel> {
    private int mType;

    public OrderQueryDataHolder(OrderModel data, int type) {
        super(data);
        mType = type;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(new OrderItemBar(context));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, OrderModel data) {
        OrderItemBar bar = (OrderItemBar) vHolder.itemView;
        bar.onBindData(data);
    }
}
