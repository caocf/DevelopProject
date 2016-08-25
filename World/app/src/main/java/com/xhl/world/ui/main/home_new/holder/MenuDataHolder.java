package com.xhl.world.ui.main.home_new.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.xhl.world.ui.main.home_new.bar.HomeMenuBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 15/12/2.
 */
public class MenuDataHolder extends RecyclerDataHolder {


    public MenuDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        return new RecyclerViewHolder(new HomeMenuBar(context));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
    }
}
