package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.business.view.ui.bar.OrderBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/4/15.
 */
public class OrderDataHolder extends RecyclerDataHolder<OrderModel> {

    private RecycleViewCallBack callBack;

    public OrderDataHolder(OrderModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        OrderBar bar = new OrderBar(context);
        bar.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return new RecyclerViewHolder(bar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, OrderModel data) {
        OrderBar bar = (OrderBar) vHolder.itemView;
        bar.onBindData(data);
        bar.setCallBack(callBack);
        bar.setPosition(position);
    }

    public void setCallBack(RecycleViewCallBack callBack) {
        this.callBack = callBack;
    }
}
