package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.view.ui.bar.ShopOpBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/7/11.
 */
public class ShopOpBarDataHolder extends RecyclerDataHolder {
    public ShopOpBarDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return 12;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(new ShopOpBar(context));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
    }
}
