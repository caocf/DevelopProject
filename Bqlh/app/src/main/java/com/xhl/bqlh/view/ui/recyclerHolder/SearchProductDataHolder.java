package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.view.ui.bar.SearchItemBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/7/6.
 */
public class SearchProductDataHolder extends RecyclerDataHolder<ProductModel> {

    public SearchProductDataHolder(ProductModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        SearchItemBar bar = new SearchItemBar(context);
        return new RecyclerViewHolder(bar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ProductModel data) {
        SearchItemBar bar = (SearchItemBar) vHolder.itemView;
        bar.onBindData(data);
    }
}
