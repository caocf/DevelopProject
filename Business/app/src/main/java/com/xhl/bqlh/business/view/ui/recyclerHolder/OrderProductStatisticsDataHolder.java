package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.StatisticsProductModel;
import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/4/18.
 */
public class OrderProductStatisticsDataHolder extends RecyclerDataHolder<StatisticsProductModel> {
    public OrderProductStatisticsDataHolder(StatisticsProductModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(View.inflate(context, R.layout.item_order_product, null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, StatisticsProductModel data) {
        View parent = vHolder.itemView;
        TextView name = (TextView) parent.findViewById(R.id.tv_product_name);
        name.setText(data.getProductName());
        TextView num = (TextView) parent.findViewById(R.id.tv_product_numb);
        num.setText(context.getResources().getString(R.string.product_num,data.getProductNum()));
        TextView price = (TextView) parent.findViewById(R.id.tv_product_price);
        price.setText(context.getResources().getString(R.string.price,data.getTotalPrice()));
    }
}
