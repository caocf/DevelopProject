package com.xhl.world.ui.main.home_new.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhl.world.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Summer on 2016/8/25.
 */
public class NullDataHolder extends RecyclerDataHolder {

    public NullDataHolder() {
        super(null);
    }

    @Override
    public int getType() {
        return 5;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        return new RecyclerViewHolder(View.inflate(context,R.layout.bar_h_null,null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {

    }
}
