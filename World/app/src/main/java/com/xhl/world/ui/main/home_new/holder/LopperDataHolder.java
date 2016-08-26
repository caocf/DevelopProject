package com.xhl.world.ui.main.home_new.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.xhl.world.model.AdvHTest;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Summer on 2016/8/26.
 */
public class LopperDataHolder extends RecyclerDataHolder {

    public LopperDataHolder(Object data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new RecyclerView.LayoutParams(-1, 300));
        AutoUtils.autoSize(imageView);

        return new RecyclerViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        ImageView imageView = (ImageView) vHolder.itemView;
        AdvHTest advHTest = (AdvHTest) data;
        imageView.setImageResource(advHTest.getResId());
    }
}
