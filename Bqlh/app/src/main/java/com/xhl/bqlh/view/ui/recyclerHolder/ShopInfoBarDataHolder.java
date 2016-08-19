package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.model.AShopDetails;
import com.xhl.bqlh.view.ui.bar.ShopInfoBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/7/11.
 */
public class ShopInfoBarDataHolder extends RecyclerDataHolder<AShopDetails> {
    public ShopInfoBarDataHolder(AShopDetails data) {
        super(data);
    }

    @Override
    public int getType() {
        return 11;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(new ShopInfoBar(context));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, AShopDetails data) {
        ShopInfoBar bar = (ShopInfoBar) vHolder.itemView;
        bar.onBindData(data);
    }
}
