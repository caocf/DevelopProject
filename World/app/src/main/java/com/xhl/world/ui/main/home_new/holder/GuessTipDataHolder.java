package com.xhl.world.ui.main.home_new.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhl.world.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 16/1/7.
 */
public class GuessTipDataHolder extends RecyclerDataHolder {
    public GuessTipDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = View.inflate(context, R.layout.item_guess_like_tip, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        AutoUtils.autoSize(view);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
    }
}
