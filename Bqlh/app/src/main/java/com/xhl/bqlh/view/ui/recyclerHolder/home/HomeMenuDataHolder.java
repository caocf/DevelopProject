package com.xhl.bqlh.view.ui.recyclerHolder.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.view.ui.bar.HomeOperatorBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Summer on 2016/7/19.
 */
public class HomeMenuDataHolder extends RecyclerDataHolder {
    public HomeMenuDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(new HomeOperatorBar(context));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        HomeOperatorBar bar = (HomeOperatorBar) vHolder.itemView;
        bar.onBindData();
    }
}
