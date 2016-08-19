package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.view.ui.bar.StoreCarReturnBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Summer on 2016/7/25.
 */
public class StoreApplyCarReturnDataHolder extends RecyclerDataHolder<ProductModel> {

    public StoreApplyCarReturnDataHolder(ProductModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(new StoreCarReturnBar(context));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ProductModel data) {
        StoreCarReturnBar bar = (StoreCarReturnBar) vHolder.itemView;
        bar.onBindData(data);
    }
}
