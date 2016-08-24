package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.xhl.world.R;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.ui.activity.bar.OrderManagerItemBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 16/1/5.
 */
public class OrderManagerItemDataHolder extends RecyclerDataHolder {

    private int mViewTag;

    public OrderManagerItemDataHolder(Object data) {
        super(data);
    }

    public void setViewTag(int tag) {
        mViewTag = tag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        int margin = context.getResources().getDimensionPixelSize(R.dimen.px_dimen_20);
        OrderManagerItemBar bar = new OrderManagerItemBar(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
        params.topMargin = margin;
        bar.setLayoutParams(params);

        AutoUtils.autoSize(bar);

        return new RecyclerViewHolder(bar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        OrderManagerItemBar bar = (OrderManagerItemBar) vHolder.itemView;
        if (data instanceof Order) {
            bar.setViewTag(mViewTag);//先设置viewTag
            bar.onBindData((Order) data);
        }
    }


}
