package com.xhl.world.ui.main.home_new.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.xhl.world.ui.main.home_new.bar.HomeFreeBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Summer on 2016/8/25.
 */
public class FreeDataHolder extends RecyclerDataHolder {

    public FreeDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        return new RecyclerViewHolder(new HomeFreeBar(context));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {

    }
}
