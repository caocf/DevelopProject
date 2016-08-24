package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.xhl.world.model.CollectionModel;
import com.xhl.world.ui.activity.bar.ProductItemBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/1/9.
 */
public class ProductItemDataHolder extends RecyclerDataHolder {

    public ProductItemDataHolder(CollectionModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        ProductItemBar bar = new ProductItemBar(context);
        return new RecyclerViewHolder(bar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        ProductItemBar bar = (ProductItemBar) vHolder.itemView;
        bar.setProductInfo((CollectionModel) data);
    }
}
