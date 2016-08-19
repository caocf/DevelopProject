package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.view.ui.bar.ShopAdBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 16/7/11.
 */
public class ShopAdBarDataHolder extends RecyclerDataHolder {
    public ShopAdBarDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return 13;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {

        ShopAdBar bar = new ShopAdBar(context);
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(-1, 340);
        layoutParams.topMargin = 20;
        layoutParams.bottomMargin = 20;
        bar.setLayoutParams(layoutParams);

        AutoUtils.auto(bar);

        return new RecyclerViewHolder(bar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        ShopAdBar bar = (ShopAdBar) vHolder.itemView;
        bar.onBindData((String) data);
    }
}
