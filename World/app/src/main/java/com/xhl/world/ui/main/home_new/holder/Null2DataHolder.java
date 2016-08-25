package com.xhl.world.ui.main.home_new.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhl.world.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Summer on 2016/8/25.
 */
public class Null2DataHolder extends RecyclerDataHolder {

    public Null2DataHolder() {
        super(null);
    }

    @Override
    public int getType() {
        return 51;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        View view = new View(context);
        view.setLayoutParams(new RecyclerView.LayoutParams(-1, 24));
        view.setBackgroundResource(R.color.app_background);
        AutoUtils.autoSize(view);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {

    }
}
